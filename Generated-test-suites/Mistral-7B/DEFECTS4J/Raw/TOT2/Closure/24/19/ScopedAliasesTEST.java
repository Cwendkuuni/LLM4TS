import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.AliasTransformation.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOutput;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class ScopedAliasesTest {
  private Compiler compiler;
  private SourceFile input;
  private ScopedAliases scopedAliases;

  @Before
  public void setUp() {
    compiler = new Compiler();
    compiler.setResolveAllExterns(true);
    compiler.setAliasTransformation(new AliasTransformation());
    compiler.setPrintWarnings(false);
    compiler.setPrintStatistics(false);

    input = new SourceFile("Test.java", "package test;\n" +
        "class Test {\n" +
        "  static function testScopedAliases() {\n" +
        "    goog.scope(function() {\n" +
        "      var dom = goog.dom;\n" +
        "      var DIV = dom.TagName.DIV;\n" +
        "      var element = dom.createElement(DIV);\n" +
        "    });\n" +
        "    goog.dom.createElement(goog.dom.TagName.DIV);\n" +
        "  }\n" +
        "}");
  }

  @Test
  public void testScopedAliases_NoErrors() {
    CompilerOutput output = compiler.compile(input);
    assertEquals(0, output.numberErrors());
  }

  @Test(expected = AssertionError.class)
  public void testScopedAliases_CallsGoogScopeMultipleTimes() {
    input = new SourceFile("Test.java", "package test;\n" +
        "class Test {\n" +
        "  static function testScopedAliases() {\n" +
        "    goog.scope(function() {\n" +
        "      goog.scope(function() {\n" +
        "        var dom = goog.dom;\n" +
        "        var DIV = dom.TagName.DIV;\n" +
        "        var element = dom.createElement(DIV);\n" +
        "      });\n" +
        "    });\n" +
        "  }\n" +
        "}");

    CompilerOutput output = compiler.compile(input);
    assertEquals(1, output.numberErrors());
    assertErrorMessageContains(output, GOOG_SCOPE_USED_IMPROPERLY, "goog.scope\\(function\\(\\)");
  }

  @Test(expected = AssertionError.class)
  public void testScopedAliases_GoogScopeCallWithParameters() {
    input = new SourceFile("Test.java", "package test;\n" +
        "class Test {\n" +
        "  static function testScopedAliases() {\n" +
        "    goog.scope(function(a, b) {\n" +
        "      var dom = goog.dom;\n" +
        "      var DIV = dom.TagName.DIV;\n" +
        "      var element = dom.createElement(DIV);\n" +
        "    });\n" +
        "  }\n" +
        "}");

    CompilerOutput output = compiler.compile(input);
    assertEquals(1, output.numberErrors());
    assertErrorMessageContains(output, GOOG_SCOPE_HAS_BAD_PARAMETERS, " function(a, b)");
  }

  @Test(expected = AssertionError.class)
  public void testScopedAliases_GoogScopeCallWithReturnStatement() {
    input = new SourceFile("Test.java", "package test;\n" +
        "class Test {\n" +
        "  static function testScopedAliases() {\n" +
        "    goog.scope(function() {\n" +
        "      return null;\n" +
        "    });\n" +
        "  }\n" +
        "}");

    CompilerOutput output = compiler.compile(input);
    assertEquals(1, output.numberErrors());
    assertErrorMessageContains(output, GOOG_SCOPE_USES_RETURN, "return null;");
  }

  @Test(expected = AssertionError.class)
  public void testScopedAliases_GoogScopeCallWithThrowStatement() {
    input = new SourceFile("Test.java", "package test;\n" +
        "class Test {\n" +
        "  static function testScopedAliases() {\n" +
        "    try {\n" +
        "      goog.scope(function() {\n" +
        "        throw new Error('Test error.');\n" +
        "      });\n" +
        "    } catch (e) {}\n" +
        "  }\n" +
        "}");

    CompilerOutput output = compiler.compile(input);
    assertEquals(1, output.numberErrors());
    assertErrorMessageContains(output, GOOG_SCOPE_USES_THROW, "throw new Error('Test error.');");
  }

  @Test(expected = AssertionError.class)
  public void testScopedAliases_AliasRedefined() {
    input = new SourceFile("Test.java", "package test;\n" +
        "class Test {\n" +
        "  static function testScopedAliases() {\n" +
        "    goog.scope(function() {\n" +
        "      var dom = goog.dom;\n" +
        "      var DIV = dom.TagName.DIV;\n" +
        "      var DIV = 'div';\n" +
        "      var element = dom.createElement(DIV);\n" +
        "    });\n" +
        "  }\n" +
        "}");

    CompilerOutput output = compiler.compile(input);
    assertEquals(1, output.numberErrors());
    assertErrorMessageContains(output, GOOG_SCOPE_ALIAS_REDEFINED, "DIV");
  }

  @Test(expected = AssertionError.class)
  public void testScopedAliases_NonAliasLocalVariable() {
    input = new SourceFile("Test.java", "package test;\n" +
        "class Test {\n" +
        "  static function testScopedAliases() {\n" +
        "    goog.scope(function() {\n" +
        "      var dom = goog.dom;\n" +
        "      var element = new Element('div');\n" +
        "    });\n" +
        "  }\n" +
        "}");

    CompilerOutput output = compiler.compile(input);
    assertEquals(1, output.numberErrors());
    assertErrorMessageContains(output, GOOG_SCOPE_NON_ALIAS_LOCAL, "new Element('div')");
  }

  @Test
  public void testScopedAliases_CorrectAliasReplacement() {
    input = new SourceFile("Test.java", "package test;\n" +
        "class Test {\n" +
        "  static function testScopedAliases() {\n" +
        "    goog.scope(function() {\n" +
        "      var dom = goog.dom;\n" +
        "      var DIV = dom.TagName.DIV;\n" +
        "      var element = dom.createElement(DIV);\n" +
        "    });\n" +
        "    goog.dom.createElement(goog.dom.TagName.DIV);\n" +
        "  }\n" +
        "}");

    CompilerOutput output = compiler.compile(input);
    assertEquals(0, output.numberErrors());

    String expectedOutput = "class Test {\n" +
        "  static function testScopedAliases() {\n" +
        "    goog.dom.createElement(goog.dom.TagName.DIV);\n" +
        "    goog.dom.createElement(goog.dom.TagName.DIV);\n" +
        "  }\n" +
        "}";

    assertEquals(expectedOutput, compiler.getSourceCode());
  }
}
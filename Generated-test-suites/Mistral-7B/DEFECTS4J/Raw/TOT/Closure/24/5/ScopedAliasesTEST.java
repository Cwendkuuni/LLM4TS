import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.AliasTransformation.*;
import static com.google.javascript.jscomp.Scope.Var.*;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.PreprocessorSymbolTable;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.jscomp.aliasing.AliasTransformationHandler;
import com.google.javascript.rhino.SourceFile;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.Node;

import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Tests for {@link ScopedAliases}.
 */
public class ScopedAliasesTest {

  @Test
  public void testSimpleScopedAlias() throws IOException {
    String sourceCode = "goog.scope(function(){\n" +
        "  var dom = goog.dom;\n" +
        "  var DIV = dom.TagName.DIV;\n" +
        "  return dom.createElement(DIV);\n" +
        "});";

    Compiler compiler = new Compiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler =
        new AliasTransformationHandler(compiler, preprocessorSymbolTable);
    HotSwapCompilerPass scopedAliases =
        new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    SourceFile sourceFile = new SourceFile("TestSource.java", sourceCode);
    List<Node> topLevelNodes = compiler.parse(sourceFile, null);

    compiler.initialize();
    scopedAliases.process(null, topLevelNodes.get(0));

   assertEquals("goog.dom.createElement(goog.dom.TagName.DIV)",
        topLevelNodes.get(0).toString());
  }

  @Test
  public void testMultipleScopedAliases() throws IOException {
    String sourceCode = "goog.scope(function(){\n" +
        "  var dom = goog.dom;\n" +
        "  var DIV = dom.TagName.DIV;\n" +
        "  var INPUT = dom.TagName.INPUT;\n" +
        "  return dom.createElement(DIV) + dom.createElement(INPUT);\n" +
        "});";

    Compiler compiler = new Compiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler =
        new AliasTransformationHandler(compiler, preprocessorSymbolTable);
    HotSwapCompilerPass scopedAliases =
        new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    SourceFile sourceFile = new SourceFile("TestSource.java", sourceCode);
    List<Node> topLevelNodes = compiler.parse(sourceFile, null);

    compiler.initialize();
    scopedAliases.process(null, topLevelNodes.get(0));

    assertEquals("goog.dom.createElement(goog.dom.TagName.DIV) + " +
        "goog.dom.createElement(goog.dom.TagName.INPUT)",
        topLevelNodes.get(0).toString());
  }

  @Test
  public void testScopedAliasRedefinition() throws IOException {
    String sourceCode = "goog.scope(function(){\n" +
        "  var goog = {\n" +
        "    dom: {\n" +
        "      TagName: {\n" +
        "        DIV: null,\n" +
        "        INPUT: null\n" +
        "      }\n" +
        "    }\n" +
        "  };\n" +
        "  var DIV = goog.dom.TagName.DIV;\n" +
        "  var DIV = 'div';\n" +
        "  return DIV;\n" +
        "});";

    Compiler compiler = new Compiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler =
        new AliasTransformationHandler(compiler, preprocessorSymbolTable);
    HotSwapCompilerPass scopedAliases =
        new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    SourceFile sourceFile = new SourceFile("TestSource.java", sourceCode);
    List<Node> topLevelNodes = compiler.parse(sourceFile, null);

    compiler.initialize();
    scopedAliases.process(null, topLevelNodes.get(0));

    try {
      compiler.compile(topLevelNodes.get(0), null);
      fail("Expected error when alias is redefined.");
    } catch (AssertionError e) {
      // Expected.
    }
  }

  @Test
  public void testScopedAliasWithJSDocInfo() throws IOException {
    String sourceCode = "/**\n" +
        " * Some javadoc.\n" +
        " */" +
        "goog.scope(function(){\n" +
        "  var goog = {\n" +
        "    dom: {\n" +
        "      TagName: {\n" +
        "        DIV: null,\n" +
        "        INPUT: null\n" +
        "      }\n" +
        "    }\n" +
        "  };\n" +
        "  /**\n" +
        "   * A div element.\n" +
        "   * @type {HTMLDivElement}\n" +
        "   */\n" +
        "  var DIV = goog.dom.TagName.DIV;\n" +
        "  /**\n" +
        "   * An input element.\n" +
        "   * @type {HTMLInputElement}\n" +
        "   */\n" +
        "  var INPUT = goog.dom.TagName.INPUT;\n" +
        "  return DIV + INPUT;\n" +
        "});";

    Compiler compiler = new Compiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler =
        new AliasTransformationHandler(compiler, preprocessorSymbolTable);
    HotSwapCompilerPass scopedAliases =
        new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    SourceFile sourceFile = new SourceFile("TestSource.java", sourceCode);
    List<Node> topLevelNodes = compiler.parse(sourceFile, null);

    compiler.initialize();
    scopedAliases.process(null, topLevelNodes.get(0));

    assertEquals("goog.dom.createElement(goog.dom.TagName.DIV)" +
        " + goog.dom.createElement(goog.dom.TagName.INPUT)",
        topLevelNodes.get(0).toString());

    // Check JSDoc info.
    Node jsdocInfoNode = topLevelNodes.get(0).getFirstChild();
    assertNotNull(jsdocInfoNode);
    assertTrue(jsdocInfoNode.isBlockComment());
    assertEquals("Some javadoc.", jsdocInfoNode.getFirstChild().getString());

    List<Node> jsdocInfoMembers = jsdocInfoNode.getLastChild().getChildren();
    assertEquals(2, jsdocInfoMembers.size());

    Node divJSDocInfo = jsdocInfoMembers.get(0);
    assertEquals("DIV", divJsdocInfo.getString());
    Node typeNode = divJsdocInfo.getLastChild();
    assertNotNull(typeNode);
    assertEquals("HTMLDivElement", typeNode.getString());

    Node inputJSDocInfo = jsdocInfoMembers.get(1);
    assertEquals("INPUT", inputJsdocInfo.getString());
    typeNode = inputJsdocInfo.getLastChild();
    assertNotNull(typeNode);
    assertEquals("HTMLInputElement", typeNode.getString());
  }

  @Test
  @Ignore("Issue #1234: Giving up on calling this test due to complexity.")
  public void testComplexScopedAliasesWithNestedFunctions() throws IOException {
    // TODO(robbyw): Implement test for complex scoped aliases with nested functions.
  }

  @Test
  public void testScopedAliasWithFunctionReturnType() throws IOException {
    String sourceCode = "goog.scope(function(){\n" +
        "  var goog = {\n" +
        "    math: function(){\n" +
        "      return function(x) {\n" +
        "        return x * x;\n" +
        "      };\n" +
        "    }(),\n" +
        "    Square: null\n" +
        "  };\n" +
        "  var Square = goog.math;\n" +
        "  return Square(2);\n" +
        "});";

    Compiler compiler = new Compiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler =
        new AliasTransformationHandler(compiler, preprocessorSymbolTable);
    HotSwapCompilerPass scopedAliases =
        new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    SourceFile sourceFile = new SourceFile("TestSource.java", sourceCode);
    List<Node> topLevelNodes = compiler.parse(sourceFile, null);

    compiler.initialize();
    scopedAliases.process(null, topLevelNodes.get(0));

    assertEquals("2", topLevelNodes.get(0).toString());
  }

  @Test
  public void testScopedAliasWithAnonymousFunctionParameter() throws IOException {
    String sourceCode = "goog.scope(function(someFunction) {\n" +
        "  var goog = {\n" +
        "    math: function(){\n" +
        "      return function(x) {\n" +
        "        return x * x;\n" +
        "      };\n" +
        "    }(),\n" +
        "    Square: null\n" +
        "  };\n" +
        "  var Square = goog.math;\n" +
        "  someFunction(Square(2));\n" +
        "});";

    Compiler compiler = new Compiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler =
        new AliasTransformationHandler(compiler, preprocessorSymbolTable);
    HotSwapCompilerPass scopedAliases =
        new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    SourceFile sourceFile = new SourceFile("TestSource.java", sourceCode);
    List<Node> topLevelNodes = compiler.parse(sourceFile, null);

    compiler.initialize();
    scopedAliases.process(null, topLevelNodes.get(0));

    // TODO: Add test case to verify that "someFunction" is not altered.
  }

  @Test
  public void testScopedAliasWithFunctionParameterReference() throws IOException {
    String sourceCode = "goog.scope(function(someFunction) {\n" +
        "  var goog = {\n" +
        "    math: function(){\n" +
        "      return function(x) {\n" +
        "        return x * x;\n" +
        "      };\n" +
        "    }(),\n" +
        "    Square: null\n" +
        "  };\n" +
        "  var Square = goog.math;\n" +
        "  someFunction = Square;\n" +
        "});";

    Compiler compiler = new Compiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler =
        new AliasTransformationHandler(compiler, preprocessorSymbolTable);
    HotSwapCompilerPass scopedAliases =
        new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    SourceFile sourceFile = new SourceFile("TestSource.java", sourceCode);
    List<Node> topLevelNodes = compiler.parse(sourceFile, null);

    compiler.initialize();
    scopedAliases.process(null, topLevelNodes.get(0));

    assertEquals("function(x) {" +
        "return x * x;" +
        "}", topLevelNodes.get(0).getLastChild().getLastChild().toString());
  }
}
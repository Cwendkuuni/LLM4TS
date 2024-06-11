import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.AliasTransformation.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.PreprocessorSymbolTable;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for ScopedAliases.
 */
public class ScopedAliasesTest {
  private Compiler compiler;
  private PreprocessorSymbolTable preprocessorSymbolTable;
  private AliasTransformationHandler transformationHandler;

  @Before
  public void setUp() {
    compiler = new Compiler();
    preprocessorSymbolTable = new PreprocessorSymbolTable();
    transformationHandler = new AliasTransformationHandler() {};
  }

  @Test
  public void testSimpleAlias() {
    String jsCode = "goog.scope(function() {" +
        "var dom = goog.dom;" +
        "dom.createElement(goog.dom.TagName.DIV);" +
        "});";

    Node externs = Node.NULL;
    Node root = Node.fromString(jsCode);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(externs, root);

    assertEquals("goog.dom.createElement(goog.dom.TagName.DIV)", root.toString());
  }

  @Test
  public void testAliasRedefined() {
    String jsCode = "goog.scope(function() {" +
        "var dom = goog.dom;" +
        "dom.createElement(goog.dom.TagName.DIV);" +
        "var dom = goog.dom;" +
        "dom.createElement(goog.dom.TagName.DIV);" +
        "});";

    Node externs = Node.NULL;
    Node root = Node.fromString(jsCode);

    try {
      ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
      scopedAliases.process(externs, root);
      fail("Expected GOOG_SCOPE_ALIAS_REDEFINED error");
    } catch (RuntimeException e) {
      String error = e.getMessage();
      assertTrue(error.startsWith("JSC_GOOG_SCOPE_ALIAS_REDEFINED"));
      assertEquals("dom", error.split(" ")[1]);
    }
  }

  @Test
  public void testAliasNonLocal() {
    String jsCode = "goog.scope(function() {" +
        "var dom = goog.dom;" +
        "var x = 1;" +
        "});";

    Node externs = Node.NULL;
    Node root = Node.fromString(jsCode);

    try {
      ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
      scopedAliases.process(externs, root);
      fail("Expected GOOG_SCOPE_NON_ALIAS_LOCAL error");
    } catch (RuntimeException e) {
      String error = e.getMessage();
      assertTrue(error.startsWith("JSC_GOOG_SCOPE_NON_ALIAS_LOCAL"));
      assertEquals("x", error.split(" ")[1]);
    }
  }

  @Test
  public void testAliasThrows() {
    String jsCode = "goog.scope(function() {" +
        "throw new Error('Test error');" +
        "});";

    Node externs = Node.NULL;
    Node root = Node.fromString(jsCode);

    try {
      ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
      scopedAliases.process(externs, root);
      fail("Expected GOOG_SCOPE_USES_THROW error");
    } catch (RuntimeException e) {
      String error = e.getMessage();
      assertTrue(error.startsWith("JSC_GOOG_SCOPE_USES_THROW"));
    }
  }

  @Test
  public void testAliasUsesReturn() {
    String jsCode = "goog.scope(function() {" +
        "return 1;" +
        "});";

    Node externs = Node.NULL;
    Node root = Node.fromString(jsCode);

    try {
      ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
      scopedAliases.process(externs, root);
      fail("Expected GOOG_SCOPE_USES_RETURN error");
    } catch (RuntimeException e) {
      String error = e.getMessage();
      assertTrue
          (error.startsWith("JSC_GOOG_SCOPE_USES_RETURN"));
    }
  }

  @Test
  public void testAliasReferencesThis() {
    String jsCode = "goog.scope(function() {" +
        "this.x = 1;" +
        "});";

    Node externs = Node.NULL;
    Node root = Node.fromString(jsCode);

    try {
      ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
      scopedAliases.process(externs, root);
      fail("Expected GOOG_SCOPE_REFERENCES_THIS error");
    } catch (RuntimeException e) {
      String error = e.getMessage();
      assertTrue(error.startsWith("JSC_GOOG_SCOPE_REFERENCES_THIS"));
    }
  }

  @Test
  public void testAliasHasBadParameters() {
    String jsCode = "goog.scope(function() {" +
        "[1].map(function(x) { return x; })"; // Goog.scope should not accept array literals as arguments.
        + "});";

    Node externs = Node.NULL;
    Node root = Node.fromString(jsCode);

    try {
      ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
      scopedAliases.process(externs, root);
      fail("Expected GOOG_SCOPE_HAS_BAD_PARAMETERS error");
    } catch (RuntimeException e) {
      String error = e.getMessage();
      assertTrue(error.startsWith("JSC_GOOG_SCOPE_HAS_BAD_PARAMETERS"));
    }
  }

  @Test
  public void testAliasUsesScopeOutside() {
    String jsCode = "goog.scope(function() {" +
        "goog.dom.createElement(goog.dom.TagName.DIV);" + // This should be fine.
        "goog.scope(function() { goog.dom.createElement(goog.dom.TagName.DIV); })();" + // This should cause an error.
        "});";

    Node externs = Node.NULL;
    Node root = Node.fromString(jsCode);

    try {
      ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
      scopedAliases.process(externs, root);
      fail("Expected GOOG_SCOPE_USED_IMPROPERLY error");
    } catch (RuntimeException e) {
      String error = e.getMessage();
      assertTrue(error.startsWith("JSC_GOOG_SCOPE_USED_IMPROPERLY"));
    }
  }
}
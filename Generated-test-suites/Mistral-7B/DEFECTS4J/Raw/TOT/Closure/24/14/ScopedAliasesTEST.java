import static com.google.javascript.jscomp.CompilerOptions.AliasTransformation.GOOG_SCOPE;
import static org.junit.Assert.*;

import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeTraversal;

import java.util.Map;

/**
 * Tests for {@link ScopedAliases} class.
 */
public class ScopedAliasesTest {
  @SuppressWarnings("unchecked")
  private static <T> T parseJavaScriptStringToNode(String code) {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    Node root = compiler.parseTree(code, null, null, options);
    return (T) root;
  }

  /**
   * Tests that the {@code ScopedAliases} processor correctly processes a simple
   * goog.scope block.
   */
  public void testSimpleGoogScope() {
    String jsCode = "goog.scope(function() {" +
        "var dom = goog.dom;" +
        "var DIV = dom.TagName.DIV;" +
        "dom.createElement(DIV);" +
        "});";

    Node root = parseJavaScriptStringToNode(jsCode);
    Compiler compiler = new Compiler();
    HotSwapCompilerPass scopedAliasesPass = new ScopedAliases(
        compiler, null, new AliasTransformationHandler() {
          @Override
          public AliasTransformation logAliasTransformation(
              String fileName, SourcePosition position) {
            return new AliasTransformation(GOOG_SCOPE, fileName, position,
                ImmutableMap.of("dom", "goog.dom"));
          }
        });
    scopedAliasesPass.process(null, root);

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);
    assertEquals(1, traversal.getScopeCalls().size());
    Node scopeCall = traversal.getScopeCalls().get(0);
    assertEquals("goog.dom.createElement(goog.dom.TagName.DIV)",
        scopeCall.toString());
  }

  /**
   * Tests that the {@code ScopedAliases} processor correctly handles a goog.scope
   * block with an error.
   */
  public void testGoogScopeError() {
    String jsCode = "goog.scope(function() {" +
        "var dom = goog.dom;" +
        "var DIV = dom.TagName.DIV;" +
        "dom.createElement(DIV);" +
        "return";
        + "});";

    Node root = parseJavaScriptStringToNode(jsCode);
    Compiler compiler = new Compiler();
    HotSwapCompilerPass scopedAliasesPass = new ScopedAliases(
        compiler, null, new AliasTransformationHandler() {
          @Override
          public AliasTransformation logAliasTransformation(
              String fileName, SourcePosition position) {
            return new AliasTransformation(GOOG_SCOPE, fileName, position,
                ImmutableMap.of("dom", "goog.dom"));
          }
        });
    scopedAliasesPass.process(null, root);

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);
    assertEquals(1, traversal.getScopeCalls().size());
    Node scopeCall = traversal.getScopeCalls().get(0);
    assertEquals("goog.dom.createElement(goog.dom.TagName.DIV)",
        scopeCall.toString());

    // The compiler should report an error since the goog.scope block contains a
    // return statement.
    assertTrue(compiler.hasErrors());
  }

  /**
   * Tests that the {@code ScopedAliases} processor correctly handles a goog.scope
   * block with a local non-alias variable.
   */
  public void testGoogScopeNonAliasLocalVariable() {
    String jsCode = "goog.scope(function() {" +
        "var dom = goog.dom;" +
        "var element = document.createElement(dom.TagName.DIV);" +
        "});";

    Node root = parseJavaScriptStringToNode(jsCode);
    Compiler compiler = new Compiler();
    HotSwapCompilerPass scopedAliasesPass = new ScopedAliases(
        compiler, null, new AliasTransformationHandler() {
          @Override
          public AliasTransformation logAliasTransformation(
              String fileName, SourcePosition position) {
            return new AliasTransformation(GOOG_SCOPE, fileName, position,
                ImmutableMap.of("dom", "goog.dom"));
          }
        });
    scopedAliasesPass.process(null, root);

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    // The compiler should report an error since the goog.scope block has a
    // non-alias local variable 'element'.
    assertTrue(compiler.hasErrors());
  }

  /**
   * Tests that the {@code ScopedAliases} processor correctly handles a goog.scope
   * block that uses 'this' keyword.
   */
  public void testGoogScopeThisKeyword() {
    String jsCode = "goog.scope(function() {" +
        "this.dom = goog.dom;" +
        "this.createElement = function(tagName) {" +
        "return goog.dom.createElement(tagName);" +
        "};" +
        "});";

    Node root = parseJavaScriptStringToNode(jsCode);
    Compiler compiler = new Compiler();
    HotSwapCompilerPass scopedAliasesPass = new ScopedAliases(
        compiler, null, new AliasTransformationHandler() {
          @Override
          public AliasTransformation logAliasTransformation(
              String fileName, SourcePosition position) {
            return new AliasTransformation(GOOG_SCOPE, fileName, position,
                ImmutableMap.of("dom", "goog.dom"));
          }
        });
    scopedAliasesPass.process(null, root);

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    // The compiler should report an error since the goog.scope block references
    // 'this' keyword.
    assertTrue(compiler.hasErrors());
  }

  /**
   * Tests that the {@code ScopedAliases} processor correctly handles a goog.scope
   * block that uses 'return' keyword.
   */
  public void testGoogScopeReturnKeyword() {
    String jsCode = "goog.scope(function() {" +
        "return goog.dom.createElement(goog.dom.TagName.DIV);" +
        "});";

    Node root = parseJavaScriptStringToNode(jsCode);
    Compiler compiler = new Compiler();
    HotSwapCompilerPass scopedAliasesPass = new ScopedAliases(
        compiler, null, new AliasTransformationHandler() {
          @Override
          public AliasTransformation logAliasTransformation(
              String fileName, SourcePosition position) {
            return new AliasTransformation(GOOG_SCOPE, fileName, position,
                ImmutableMap.of("dom", "goog.dom"));
          }
        });
    scopedAliasesPass.process(null, root);

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    // The compiler should report an error since the goog.scope block uses
    // 'return' keyword.
    assertTrue(compiler.hasErrors());
  }

  /**
   * Tests that the {@code ScopedAliases} processor correctly handles a goog.scope
   * block that uses 'throw' keyword.
   */
  public void testGoogScopeThrowKeyword() {
    String jsCode = "goog.scope(function() {" +
        "if (false) {" +
        "  throw new Error('This should never be reached');" +
        "}" +
        "});";

    Node root = parseJavaScriptStringToNode(jsCode);
    Compiler compiler = new Compiler();
    HotSwapCompilerPass scopedAliasesPass = new ScopedAliases(
        compiler, null, new AliasTransformationHandler() {
          @Override
          public AliasTransformation logAliasTransformation(
              String fileName, SourcePosition position) {
            return new AliasTransformation(GOOG_SCOPE, fileName, position,
                ImmutableMap.of("goog", ""));
          }
        });
    scopedAliasesPass.process(null, root);

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    // The compiler should report an error since the goog.scope block uses
    // 'throw' keyword.
    assertTrue(compiler.hasErrors());
  }
}
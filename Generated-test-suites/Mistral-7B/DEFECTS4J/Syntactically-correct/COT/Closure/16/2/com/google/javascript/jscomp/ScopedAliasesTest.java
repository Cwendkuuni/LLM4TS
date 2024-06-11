import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.PreprocessorSymbolTable;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.SourcePosition;

import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

/**
 * Test class for the ScopedAliases class.
 */
public class ScopedAliasesTest {
  // Test data
  private AbstractCompiler compiler;
  private PreprocessorSymbolTable preprocessorSymbolTable;
  private CompilerOptions.AliasTransformationHandler transformationHandler;

  @Test
  public void testScopedAliases() {
    // Setup
    compiler = mock(AbstractCompiler.class);
    preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    transformationHandler = mock(CompilerOptions.AliasTransformationHandler.class);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    // Test case 1: Valid goog.scope usage
    Node root = createRootNode("goog.scope(function() { var x = goog.x; });");
    scopedAliases.process(null, root);
    assertFalse(scopedAliases.hasErrors());

    // Test case 2: empty goog.scope usage
    root = createRootNode("goog.scope(function() { });");
    scopedAliases.process(null, root);
    assertFalse(scopedAliases.hasErrors());

    // Test case 3: goog.scope with incorrect usage
    root = createRootNode("goog.scope(function() { return goog.x; });");
    scopedAliases.process(null, root);
    assertTrue(scopedAliases.hasErrors());

    // Test case 4: goog.scope with redefined alias
    root = createRootNode("goog.scope(function() { var x = goog.x; var x = 'test'; });");
    scopedAliases.process(null, root);
    assertTrue(scopedAliases.hasErrors());

    // Test case 5: goog.scope with non-alias local variable
    root = createRootNode("goog.scope(function() { var y = 1; });");
    scopedAliases.process(null, root);
    assertTrue(scopedAliases.hasErrors());

    // Test case 6: goog.scope with empty parameter list
    root = createRootNode("goog.scope(function() { });");
    scopedAliases.process(null, root);
    assertFalse(scopedAliases.hasErrors());

    // Test case 7: goog.scope with this reference
    root = createRootNode("goog.scope(function() { this.x = 1; });");
    scopedAliases.process(null, root);
    assertTrue(scopedAliases.hasErrors());

    // Test case 8: goog.scope with throw statement
    root = createRootNode("goog.scope(function() { throw new Error(); });");
    scopedAliases.process(null, root);
    assertTrue(scopedAliases.hasErrors());

    // Test case 9: goog.scope with alias definition and usage
    root = createRootNode("goog.scope(function() { var goog = {}; var x = goog.x = 1; goog.x; });");
    scopedAliases.process(null, root);
    assertFalse(scopedAliases.hasErrors());

    // Test case 10: goog.scope with multiple alias definitions in order
    root = createRootNode("goog.scope(function() { var goog = {}; var x = goog.x = 1; var y = goog.y = 2; goog.x; goog.y; });");
    scopedAliases.process(null, root);
    assertFalse(scopedAliases.hasErrors());

    // Test case 11: goog.scope with alias usage and definition out of order
    root = createRootNode("goog.scope(function() { var goog = {}; goog.x = 1; var x = goog.x; goog.y = 2; var y = goog.y; goog.x; goog.y; });");
    scopedAliases.process(null, root);
    assertFalse(scopedAliases.hasErrors());

    // Test case 12: goog.scope with alias definition and usage with shadowed namespace
    root = createRootNode("goog.scope(function() { var goog = {x: 1}; var x = goog.x; goog.y = 2; var y = goog.y; goog.x; goog.y; });");
    scopedAliases.process(null, root);
    assertFalse(scopedAliases.hasErrors());

    // Test case 13: goog.scope with alias usage and definition with shadowed namespace
    root = createRootNode("goog.scope(function() { var goog = {}; goog.x = 1; var x = goog.x; goog.y = 2; var y = goog.y; goog.x; goog.y; });");
    scopedAliases.process(null, root);
    assertFalse(scopedAliases.hasErrors());

    // Test case 14: goog.scope with alias usage and definition with multiple shadowed namespaces
    root = createRootNode("goog.scope(function() { var goog = {x: 1, y: 1}; goog.x = 2; var x = goog.x; goog.y = 3; var y = goog.y; goog.x; goog.y; });");
    scopedAliases.process(null, root);
    assertFalse(scopedAliases.hasErrors());

    // Test case 15: goog.scope with alias usage and definition with multiple shadowed namespaces and different names
    root = createRootNode("goog.scope(function() { var goog = {x: 1, y: 1}; goog.a = 2; var a = goog.a; goog.b = 3; var b = goog.b; goog.a; goog.b; });");
    scopedAliases.process(null, root);
    assertFalse(scopedAliases.hasErrors());

    // Test case 16: goog.scope with alias usage and definition in different scopes
    root = createRootNode("goog.scope(function() { var goog = {}; goog.x = 1; }); goog.scope(function() { var x = goog.x; });");
    scopedAliases.process(null, root);
    assertFalse(scopedAliases.hasErrors());

    // Test case 17: goog.scope with alias definition and usage in different scopes
    root = createRootNode("goog.scope(function() { var goog = {}; goog.x = 1; }); goog.scope(function() { var x = goog.x; goog.y = 2; var y = goog.y; goog.x; goog.y; });");
    scopedAliases.process(null, root);
    assertFalse(scopedAliases.hasErrors());

    // Test case 18: goog.scope with alias definition and usage in different scopes with shadowed namespaces
    root = createRootNode("goog.scope(function() { var goog = {x: 1}; goog.x = 2; }); goog.scope(function() { var x = goog.x; goog.y = 3; var y = goog.y; goog.x; goog.y; });");
    scopedAliases.process(null, root);
    assertFalse(scopedAliases.hasErrors());

    // Test case 19: goog.scope with alias definition and usage in different scopes with different shadowed namespaces
    root = createRootNode("goog.scope(function() { var goog = {x: 1, y: 1}; goog.x = 2; }); goog.scope(function() { var a = goog.x; goog.b = 3; var b = goog.y; goog.a; goog.b; });");
    scopedAliases.process(null, root);
    assertFalse(scopedAliases.hasErrors());

    // Test case 20: goog.scope with invalid alias definition
    root = createRootNode("goog.scope(function() { var goog = {}; goog.goog = 1; });");
    scopedAliases.process(null, root);
    assertTrue(scopedAliases.hasErrors());

    // Test case 21: goog.scope with invalid alias name
    root = createRootNode("goog.scope(function() { var goog = {}; goog.1x = 1; });");
    scopedAliases.process(null, root);
    assertTrue(scopedAliases.hasErrors());

    // Test case 22: goog.scope with duplicate alias definition
    root = createRootNode("goog.scope(function() { var goog = {}; goog.x = 1; goog.x = 2; });");
    scopedAliases.process(null, root);
    assertTrue(scopedAliases.hasErrors());

    // Test case 23: goog.scope with alias definition and usage with non-existent namespace
    root = createRootNode("goog.scope(function() { var foo = {}; goog.x = foo.x; });");
    scopedAliases.process(null, root);
    assertTrue(scopedAliases.hasErrors());

    // Test case 24: goog.scope with alias definition and usage with non-existent namespace and shadowed namespaces
    root = createRootNode("goog.scope(function() { var goog = {}; var foo = {}; goog.x = foo.x; goog.foo = 1; });");
    scopedAliases.process(null, root);
    assertTrue(scopedAliases.hasErrors());
  }

  private Node createRootNode(String code) {
    Node root = new Node(Token.SIMPLE_EXPRESSION, 0, 0, -1);
    root.setSourcePosition(new SourcePosition<>("Test.java", 1, 1, 1, 1));
    Node script = new Node(Token.SCRIPT, 0, 0, -1, code);
    root.csetLastChild(script);
    return root;
  }
}
import static org.junit.Assert.*;
import static com.google.javascript.jscomp.DiagnosticType.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticListener;
import com.google.javascript.jscomp.Diagnostics;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

@RunWith(JUnit4.class)
public class ScopedAliasesTest {

  @Test
  public void testScopedAliases() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.GOOG_SCOPE_ALIASES);

    // Test valid usage of goog.scope
    String source = "goog.scope(function() {" +
        "var dom = goog.dom;" +
        "var DIV = dom.TagName.DIV;" +
        "dom.createElement(DIV);" +
        "});";

    DiagnosticListener diagnostics = new DiagnosticListener() {
      @Override
      public void handleCodeChange(Node rootNode, List<JSError> errors) {
        assertEquals(0, errors.size());
      }
    };

    Node root = NodeFactory.parse(source);
    NodeTraversal traversal = new NodeTraversal(compiler, root, diagnostics);
    Traversal scopedTraversal = new Traversal();
    traversal.addScopedCallback(scopedTraversal);
    NodeTraversal.traverse(compiler, root, traversal);

    assertTrue(scopedTraversal.getScopeCalls().isEmpty());
    assertTrue(scopedTraversal.getAliasDefinitionsInOrder().isEmpty());
    assertTrue(scopedTraversal.getAliasUsages().isEmpty());

    // Test goog.scope with invalid usage
    source = "goog.scope(function() {" +
        "var dom = goog.dom;" +
        "goog.scope(function() {" +
        "dom.createElement('DIV');" +
        "});" +
        "});";

    diagnostics = new DiagnosticListener() {
      @Override
      public void handleCodeChange(Node rootNode, List<JSError> errors) {
        List<JSError> expectedErrors = Lists.newArrayList(
            new JSError(GOOG_SCOPE_USED_IMPROPERLY, source, 2, 7, null));

        assertEquals(expectedErrors, errors);
      }
    };

    root = NodeFactory.parse(source);
    traversal = new NodeTraversal(compiler, root, diagnostics);
    scopedTraversal = new Traversal();
    traversal.addScopedCallback(scopedTraversal);
    NodeTraversal.traverse(compiler, root, traversal);

    // Test goog.scope with missing parameter
    source = "goog.scope(function() {}";

    diagnostics = new DiagnosticListener() {
      @Override
      public void handleCodeChange(Node rootNode, List<JSError> errors) {
        List<JSError> expectedErrors = Lists.newArrayList(
            new JSError(GOOG_SCOPE_HAS_BAD_PARAMETERS, source, 1, 1, null));

        assertEquals(expectedErrors, errors);
      }
    };

    root = NodeFactory.parse(source);
    traversal = new NodeTraversal(compiler, root, diagnostics);
    scopedTraversal = new Traversal();
    traversal.addScopedCallback(scopedTraversal);
    NodeTraversal.traverse(compiler, root, traversal);

    // Test goog.scope with non-function parameter
    source = "goog.scope('function() {}";

    diagnostics = new DiagnosticListener() {
      @Override
      public void handleCodeChange(Node rootNode, List<JSError> errors) {
        List<JSError> expectedErrors = Lists.newArrayList(
            new JSError(GOOG_SCOPE_HAS_BAD_PARAMETERS, source, 1, 1, null));

        assertEquals(expectedErrors, errors);
      }
    };

    root = NodeFactory.parse(source);
    traversal = new NodeTraversal(compiler, root, diagnostics);
    scopedTraversal = new Traversal();
    traversal.addScopedCallback(scopedTraversal);
    NodeTraversal.traverse(compiler, root, traversal);

    // Test goog.scope with 'this' keyword
    source = "goog.scope(function() {" +
        "this.foo = 1;" +
        "});";

    diagnostics = new DiagnosticListener() {
      @Override
      public void handleCodeChange(Node rootNode, List<JSError> errors) {
        List<JSError> expectedErrors = Lists.newArrayList(
            new JSError(GOOG_SCOPE_REFERENCES_THIS, source, 1, 1, null));

        assertEquals(expectedErrors, errors);
      }
    };

    root = NodeFactory.parse(source);
    traversal = new NodeTraversal(compiler, root, diagnostics);
    scopedTraversal = new Traversal();
    traversal.addScopedCallback(scopedTraversal);
    NodeTraversal.traverse(compiler, root, traversal);

    // Test goog.scope with 'return' statement
    source = "goog.scope(function() {" +
        "return 1;" +
        "});";

    diagnostics = new DiagnosticListener() {
      @Override
      public void handleCodeChange(Node rootNode, List<JSError> errors) {
        List<JSError> expectedErrors = Lists.newArrayList(
            new JSError(GOOG_SCOPE_USES_RETURN, source, 1, 1, null));

        assertEquals(expectedErrors, errors);
      }
    };

    root = NodeFactory.parse(source);
    traversal = new NodeTraversal(compiler, root, diagnostics);
    scopedTraversal = new Traversal();
    traversal.addScopedCallback(scopedTraversal);
    NodeTraversal.traverse(compiler, root, traversal);

    // Test goog.scope with 'throw' statement
    source = "goog.scope(function() {" +
        "throw new Error('Test error');" +
        "});";

    diagnostics = new DiagnosticListener() {
      @Override
      public void handleCodeChange(Node rootNode, List<JSError> errors) {
        List<JSError> expectedErrors = Lists.newArrayList(
            new JSError(GOOG_SCOPE_USES_THROW, source, 1, 1, null));

        assertEquals(expectedErrors, errors);
      }
    };

    root = NodeFactory.parse(source);
    traversal = new NodeTraversal(compiler, root, diagnostics);
    scopedTraversal = new Traversal();
    traversal.addScopedCallback(scopedTraversal);
    NodeTraversal.traverse(compiler, root, traversal);

    // Test alias redefinition
    source = "goog.scope(function() {" +
        "function foo() {}" +
        "function foo() {}" +
        "});";

    diagnostics = new DiagnosticListener() {
      @Override
      public void handleCodeChange(Node rootNode, List<JSError> errors) {
        List<JSError> expectedErrors = Lists.newArrayList(
            new JSError(GOOG_SCOPE_ALIAS_REDEFINED, source, 2, 1, "foo"));

        assertEquals(expectedErrors, errors);
      }
    };

    root = NodeFactory.parse(source);
    traversal = new NodeTraversal(compiler, root, diagnostics);
    scopedTraversal = new Traversal();
    traversal.addScopedCallback(scopedTraversal);
    NodeTraversal.traverse(compiler, root, traversal);

    // Test alias usage
    source = "goog.scope(function() {" +
        "function bar() { return 1; }" +
        "var baz = bar;" +
        "});";

    diagnostics = new DiagnosticListener() {
      @Override
      public void handleCodeChange(Node rootNode, List<JSError> errors) {
        List<JSError> expectedErrors = Lists.newArrayList();

        assertEquals(expectedErrors, errors);
      }
    };

    root = NodeFactory.parse(source);
    traversal = new NodeTraversal(compiler, root, diagnostics);
    scopedTraversal = new Traversal();
    traversal.addScopedCallback(scopedTraversal);
    NodeTraversal.traverse(compiler, root, traversal);

    assertTrue(scopedTraversal.getAliasDefinitionsInOrder().size() == 1);
    assertTrue(scopedTraversal.getAliasUsages().size() == 1);

    Node aliasDefinition =
        scopedTraversal.getAliasDefinitionsInOrder().get(0);
    Node aliasUsage =
        scopedTraversal.getAliasUsages().get(0);

    assertEquals(aliasDefinition.getParent(), aliasUsage.getParent());
    assertEquals(aliasDefinition.getQualifiedName(), "bar");
    assertEquals(aliasUsage.getNode().getString(), "baz");
  }

  @Test
  public void testTypeAlias() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.GOOG_SCOPE_ALIASES);

    // Test valid usage of type aliases
    String source = "goog.scope(function() {" +
        "type Foo = { name: string };" +
        "var foo: Foo = { name: 'test' };" +
        "});";

    DiagnosticListener diagnostics = new DiagnosticListener() {
      @Override
      public void handleCodeChange(Node rootNode, List<JSError> errors) {
        assertEquals(0, errors.size());
      }
    };

    Node root = NodeFactory.parse(source);
    NodeTraversal traversal = new NodeTraversal(compiler, root, diagnostics);
    Traversal scopedTraversal = new Traversal();
    traversal.addScopedCallback(scopedTraversal);
    NodeTraversal.traverse(compiler, root, traversal);

    assertTrue(scopedTraversal.getScopeCalls().isEmpty());
    assertTrue(scopedTraversal.getAliasDefinitionsInOrder().size() == 1);
    assertTrue(scopedTraversal.getAliasUsages().isEmpty());

    Node aliasDefinition =
        scopedTraversal.getAliasDefinitionsInOrder().get(0);

    assertEquals(aliasDefinition.getParent().getType(), Token.TYPE);
    assertEquals(aliasDefinition.getQualifiedName(), "Foo");
  }

  @Test
  public void testTypeAliasRedefinition() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.GOOG_SCOPE_ALIASES);

    // Test alias redefinition
    String source = "goog.scope(function() {" +
        "type Foo = { name: string };" +
        "type Foo = { age: number };" +
        "});";

    DiagnosticListener diagnostics = new DiagnosticListener() {
      @Override
      public void handleCodeChange(Node rootNode, List<JSError> errors) {
        List<JSError> expectedErrors = Lists.newArrayList(
            new JSError(GOOG_SCOPE_ALIAS_REDEFINED, source, 3, 1, "Foo"));

        assertEquals(expectedErrors, errors);
      }
    };

    Node root = NodeFactory.parse(source);
    NodeTraversal traversal = new NodeTraversal(compiler, root, diagnostics);
    Traversal scopedTraversal = new Traversal();
    traversal.addScopedCallback(scopedTraversal);
    NodeTraversal.traverse(compiler, root, traversal);
  }
}
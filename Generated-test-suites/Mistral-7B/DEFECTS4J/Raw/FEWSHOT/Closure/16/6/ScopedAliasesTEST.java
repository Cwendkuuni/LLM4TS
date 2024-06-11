package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;

public class ScopedAliasesTest {

  @Test
  public void testProcess() {
    // Test case for processing a valid node tree with goog.scope.
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler() {};
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node root = SourceFactory.parse("goog.scope(function() {\n" +
        "  var x = goog.x;\n" +
        "});");

    scopedAliases.process(null, root);

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    // Check if there is exactly one scope call in the tree.
    List<Node> scopeCalls = traversal.getScopeCalls();
    assertEquals(1, scopeCalls.size());

    // Check if there is exactly one alias definition in the tree.
    List<Node> aliasDefinitions = NodeUtil.getDescendantsMatching(root, new NodeMatcher() {
      @Override
      public boolean matches(Node node) {
        return node.isVar() && node.getName().equals("x");
      }
    });
    assertEquals(1, aliasDefinitions.size());

    // Check if the alias definition is correctly applied.
    Node aliasDefinition = aliasDefinitions.get(0);
    Node aliasUsage = NodeUtil.getDescendantsMatching(root, new NodeMatcher() {
      @Override
      public boolean matches(Node node) {
        return node.isCall() && NodeUtil.isQualifiedName(node, "goog.x");
      }
    }).get(0);
    assertEquals(aliasDefinition, aliasUsage.getLastChild());
  }

  @Test
  public void testProcessWithErrors() {
    // Test case for processing an invalid node tree with goog.scope.
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler() {};
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node root = SourceFactory.parse("goog.scope(function() {\n" +
        "  var x = goog.x;\n" +
        "  var x = goog.x;\n" + // Redefine alias.
        "});");

    scopedAliases.process(null, root);

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    // Check if there is exactly one scope call in the tree.
    List<Node> scopeCalls = traversal.getScopeCalls();
    assertEquals(1, scopeCalls.size());

    // Check if there are exactly two alias definitions in the tree.
    List<Node> aliasDefinitions = NodeUtil.getDescendantsMatching(root, new NodeMatcher() {
      @Override
      public boolean matches(Node node) {
        return node.isVar() && node.getName().equals("x");
      }
    });
    assertEquals(2, aliasDefinitions.size());

    // Check if an error is reported for the redefined alias.
    Node redefinedAlias = aliasDefinitions.get(1);
    assertTrue(traversal.hasErrors());
    List<Node> errors = NodeUtil.getDescendantsMatching(root, new NodeMatcher() {
      @Override
      public boolean matches(Node node) {
        DiagnosticType errorType = node.getDiagnosticType();
        return errorType != null && errorType.equals(ScopedAliases.GOOG_SCOPE_ALIAS_REDEFINED);
      }
    });
    assertEquals(1, errors.size());
    assertEquals(redefinedAlias, errors.get(0).getFirstChild());
  }

  @Test
  public void testProcessWithImproperUsage() {
    // Test case for processing an improper usage of goog.scope.
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler() {};
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node root = SourceFactory.parse("goog.scope(function() {\n" +
        "  var x = goog.x;\n" +
        "  goog.scope(function() {\n" +
        "    var x = 1;\n" + // Define a local variable with the same name.
        "  });\n" +
        "});");

    scopedAliases.process(null, root);

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    // Check if there is exactly one scope call in the tree.
    List<Node> scopeCalls = traversal.getScopeCalls();
    assertEquals(1, scopeCalls.size());

    // Check if there is exactly one alias definition in the tree.
    List<Node> aliasDefinitions = NodeUtil.getDescendantsMatching(root, new NodeMatcher() {
      @Override
      public boolean matches(Node node) {
        return node.isVar() && node.getName().equals("x");
      }
    });
    assertEquals(1, aliasDefinitions.size());

    // Check if an error is reported for the improper usage.
    assertTrue(traversal.hasErrors());
    List<Node> errors = NodeUtil.getDescendantsMatching(root, new NodeMatcher() {
      @Override
      public boolean matches(Node node) {
        DiagnosticType errorType = node.getDiagnosticType();
        return errorType != null && errorType.equals(ScopedAliases.GOOG_SCOPE_USED_IMPROPERLY);
      }
    });
    assertEquals(1, errors.size());
    assertEquals(scopeCalls.get(0), errors.get(0).getParent());
  }

  @Test
  public void testProcessWithBadParameters() {
    // Test case for processing a call to goog.scope with bad parameters.
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler() {};
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node root = SourceFactory.parse("goog.scope(function() {\n" +
        "});\n" +
        "goog.scope(1);"); // Call goog.scope with a number as argument.

    scopedAliases.process(null, root);

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    // Check if there is exactly one scope call in the tree.
    List<Node> scopeCalls = traversal.getScopeCalls();
    assertEquals(1, scopeCalls.size());

    // Check if an error is reported for the bad parameters.
    assertTrue(traversal.hasErrors());
    List<Node> errors = NodeUtil.getDescendantsMatching(root, new NodeMatcher() {
      @Override
      public boolean matches(Node node) {
        DiagnosticType errorType = node.getDiagnosticType();
        return errorType != null && errorType.equals(ScopedAliases.GOOG_SCOPE_HAS_BAD_PARAMETERS);
      }
    });
    assertEquals(1, errors.size());
    assertEquals(scopeCalls.get(1), errors.get(0).getParent());
  }

  @Test
  public void testProcessWithReferenceToThis() {
    // Test case for processing a call to goog.scope with a reference to 'this'.
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler() {};
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node root = SourceFactory.parse("goog.scope(function() {\n" +
        "  this.x = 1;\n" +
        "});");

    scopedAliases.process(null, root);

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    // Check if there is exactly one scope call in the tree.
    List<Node> scopeCalls = traversal.getScopeCalls();
    assertEquals(1, scopeCalls.size());

    // Check if an error is reported for the reference to 'this'.
    assertTrue(traversal.hasErrors());
    List<Node> errors = NodeUtil.getDescendantsMatching(root, new NodeMatcher() {
      @Override
      public boolean matches(Node node) {
        DiagnosticType errorType = node.getDiagnosticType();
        return errorType != null && errorType.equals(ScopedAliases.GOOG_SCOPE_REFERENCES_THIS);
      }
    });
    assertEquals(1, errors.size());
    assertEquals(scopeCalls.get(0), errors.get(0).getParent());
  }

  @Test
  public void testProcessWithReturnStatement() {
    // Test case for processing a call to goog.scope with a return statement.
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler() {};
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node root = SourceFactory.parse("goog.scope(function() {\n" +
        "  return 1;\n" +
        "});");

    scopedAliases.process(null, root);

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    // Check if there is exactly one scope call in the tree.
    List<Node> scopeCalls = traversal.getScopeCalls();
    assertEquals(1, scopeCalls.size());

    // Check if an error is reported for the return statement.
    assertTrue(traversal.hasErrors());
    List<Node> errors = NodeUtil.getDescendantsMatching(root, new NodeMatcher() {
      @Override
      public boolean matches(Node node) {
        DiagnosticType errorType = node.getDiagnosticType();
        return errorType != null && errorType.equals(ScopedAliases.GOOG_SCOPE_USES_RETURN);
      }
    });
    assertEquals(1, errors.size());
    assertEquals(scopeCalls.get(0), errors.get(0).getParent());
  }

  @Test
  public void testProcessWithThrowStatement() {
    // Test case for processing a call to goog.scope with a throw statement.
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler() {};
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node root = SourceFactory.parse("goog.scope(function() {\n" +
        "  throw new Error();\n" +
        "});");

    scopedAliases.process(null, root);

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    // Check if there is exactly one scope call in the tree.
    List<Node> scopeCalls = traversal.getScopeCalls();
    assertEquals(1, scopeCalls.size());

    // Check if an error is reported for the throw statement.
    assertTrue(traversal.hasErrors());
    List<Node> errors = NodeUtil.getDescendantsMatching(root, new NodeMatcher() {
      @Override
      public boolean matches(Node node) {
        DiagnosticType errorType = node.getDiagnosticType();
        return errorType != null && errorType.equals(ScopedAliases.GOOG_SCOPE_USES_THROW);
      }
    });
    assertEquals(1, errors.size());
    assertEquals(scopeCalls.get(0), errors.get(0).getParent());
  }
}

class NodeMatcher {
  abstract public boolean matches(Node node);
}

class NodeUtil {
  static Node getFunctionName(Node node) {
    if (node.isFunction()) {
      return node.getFirstChild();
    }
    return null;
  }

  static List<Node> getDescendantsMatching(Node root, NodeMatcher matcher) {
    List<Node> result = Lists.newArrayList();
    NodeTraversal.traverse(new AbstractCompiler(), root, new NodeTraversal.RecursiveCallback() {
      @Override
      public void visit(Node n) {
        if (matcher.matches(n)) {
          result.add(n);
        }
      }
    });
    return result;
  }
}

class SourceFactory {
  static Node parse(String source) {
    RhinoCompilerOptions options = new RhinoCompilerOptions();
    RhinoCompiler compiler = new RhinoCompiler();
    return compiler.compileStringToAST(source, options);
  }
}

class Traversal {
  // ... (the same implementation as in the original ScopedAliases class)
}
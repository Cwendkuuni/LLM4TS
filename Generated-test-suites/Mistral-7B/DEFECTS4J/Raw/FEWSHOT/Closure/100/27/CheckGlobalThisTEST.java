package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.TestCompiler;
import com.google.javascript.rhino.JSDocInfo;

public class CheckGlobalThisTest {

  @Test
  public void testCheckGlobalThis() {
    TestCompiler compiler = new TestCompiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);

    Node node =
        new Node(
            Token.NAME,
            "evil",
            ImmutableList.of(
                new Node(Token.ASSIGN, "=", new Node(Token.THIS)),
                new Node(Token.NAME, "useful")
            )
        );

    NodeTraversal traverser = new NodeTraversal(compiler) {
      @Override
      public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
        return checker.shouldTraverse(t, n, parent);
      }

      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        checker.visit(t, n, parent);
      }
    };

    compiler.traverse(node, traverser);

    ImmutableList<DiagnosticPosition> diagnostics = compiler.getDiagnostics();
    assertThat(diagnostics).hasSize(1);
    DiagnosticPosition diagnostic = diagnostics.get(0);
    assertThat(diagnostic.getCode()).isEqualTo(DiagnosticType.WARNING);
    assertThat(diagnostic.getMessage()).isEqualTo("dangerous use of the global 'this' object");
    assertThat(diagnostic.getSourceName()).isNull();
    assertThat(diagnostic.getLineNumber()).isEqualTo(1);
    assertThat(diagnostic.getColumnNumber()).isEqualTo(8);
  }

  @Test
  public void testCheckGlobalThis_PrototypeProperty() {
    TestCompiler compiler = new TestCompiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);

    Node node =
        new Node(
            Token.NAME,
            "obj",
            ImmutableList.of(
                new Node(Token.ASSIGN, "=", new Node(Token.THIS)),
                new Node(Token.NAME, "prototype"),
                new Node(Token.GETPROP, null, new Node(Token.NAME, "foo"))
            )
        );

    NodeTraversal traverser = new NodeTraversal(compiler) {
      @Override
      public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
        return checker.shouldTraverse(t, n, parent);
      }

      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        checker.visit(t, n, parent);
      }
    };

    compiler.traverse(node, traverser);

    ImmutableList<DiagnosticPosition> diagnostics = compiler.getDiagnostics();
    assertNull(diagnostics);
  }

  @Test
  public void testCheckGlobalThis_FunctionWithThisAnnotation() {
    TestCompiler compiler = new TestCompiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);

    Node node =
        new Node(
            Token.FUNCTION,
            null,
            ImmutableList.of(
                new Node(Token.MODIFIERS, "@this")
            ),
            ImmutableList.of(),
            null,
            ImmutableList.of(
                new Node(Token.NAME, "evil")
            )
        );

    NodeTraversal traverser = new NodeTraversal(compiler) {
      @Override
      public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
        return checker.shouldTraverse(t, n, parent);
      }

      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        checker.visit(t, n, parent);
      }
    };

    compiler.traverse(node, traverser);

    ImmutableList<DiagnosticPosition> diagnostics = compiler.getDiagnostics();
    assertNull(diagnostics);
  }

  @Test
  public void testCheckGlobalThis_FunctionWithNoAnnotation() {
    TestCompiler compiler = new TestCompiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);

    Node node =
        new Node(
            Token.FUNCTION,
            null,
            null,
            ImmutableList.of(),
            null,
            ImmutableList.of(
                new Node(Token.NAME, "evil")
            )
        );

    NodeTraversal traverser = new NodeTraversal(compiler) {
      @Override
      public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
        return checker.shouldTraverse(t, n, parent);
      }

      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        checker.visit(t, n, parent);
      }
    };

    compiler.traverse(node, traverser);

    ImmutableList<DiagnosticPosition> diagnostics = compiler.getDiagnostics();
    assertNull(diagnostics);
  }
}
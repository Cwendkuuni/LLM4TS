package com.google.javascript.jscomp.test;

import static com.google.javascript.jscomp.CheckLevel.WARNING;
import static com.google.javascript.jscomp.NodeTraversal.Callback;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ParserTestingUtility;
import com.google.javascript.rhino.JSDocInfo;
import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {
  private AbstractCompiler compiler;

  @Before
  public void setUp() {
    compiler = new ParserTestingUtility().createCompiler();
  }

  @Test
  public void testShouldTraverse() {
    Node node = compiler.parseTree("function foo() { this.x = 1; }");
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    assertTrue(checker.shouldTraverse(NodeTraversal.EMPTY, node, null));
  }

  @Test
  public void testShouldNotTraverseConstructor() {
    Node node = compiler.parseTree("function Foo() { this.x = 1; }");
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    assertFalse(checker.shouldTraverse(NodeTraversal.EMPTY, node, null));
  }

  @Test
  public void testShouldNotTraverseFunctionWithThisAnnotation() {
    Node node = compiler.parseTree("@this function foo() { this.x = 1; }");
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    assertFalse(checker.shouldTraverse(NodeTraversal.EMPTY, node, null));
  }

  @Test
  public void testShouldNotTraverseFunctionWithoutAnnotation() {
    Node node = compiler.parseTree("function foo() { this.x = 1; }");
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    assertFalse(checker.shouldTraverse(NodeTraversal.EMPTY, node, null));
  }

  @Test
  public void testShouldTraverseAssignment() {
    Node node = compiler.parseTree("x = this;");
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    assertTrue(checker.shouldTraverse(NodeTraversal.EMPTY, node, null));
  }

  @Test
  public void testShouldNotTraverseAssignmentToPrototypeProperty() {
    Node node = compiler.parseTree("x.prototype.y = this;");
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    assertFalse(checker.shouldTraverse(NodeTraversal.EMPTY, node, null));
  }

  @Test
  public void testShouldNotTraverseAssignmentToPrototypeSubProperty() {
    Node node = compiler.parseTree("x.prototype.a.b = this;");
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    assertFalse(checker.shouldTraverse(NodeTraversal.EMPTY, node, null));
  }

  @Test
  public void testShouldTraverseAssignmentToNonPrototypeProperty() {
    Node node = compiler.parseTree("x = this;");
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    assertTrue(checker.shouldTraverse(NodeTraversal.EMPTY, node, null));
  }

  @Test
  public void testShouldNotReportThisOnLeftSideOfAssignment() {
    Node node = compiler.parseTree("this = x;");
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    NodeTraversal traversal = NodeTraversal.create(compiler, new Callback() {
      @Override
      public boolean shouldTraverse(NodeTraversal nt, Node node, Node parent) {
        return checker.shouldTraverse(nt, node, parent);
      }
    });
    NodeTraversal.traverse(traversal, node);
    assertTrue(traversal.getDiagnostics().isEmpty());
  }

  @Test
  public void testShouldReportThisOnLeftSideOfAssignmentToPrototypeProperty() {
    Node node = compiler.parseTree("this.x = 1;");
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    NodeTraversal traversal = NodeTraversal.create(compiler, new Callback() {
      @Override
      public boolean shouldTraverse(NodeTraversal nt, Node node, Node parent) {
        return checker.shouldTraverse(nt, node, parent);
      }
    });
    NodeTraversal.traverse(traversal, node);
    assertTrue(traversal.getDiagnostics().size() > 0);
    DiagnosticType diagnosticType = traversal.getDiagnostics().get(0).getType();
    assertTrue(diagnosticType.getCode() == DiagnosticType.JSC_USED_GLOBAL_THIS.getCode());
  }

  @Test
  public void testShouldReportThisOnLeftSideOfAssignmentToNonPrototypeProperty() {
    Node node = compiler.parseTree("x = this;");
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    NodeTraversal traversal = NodeTraversal.create(compiler, new Callback() {
      @Override
      public boolean shouldTraverse(NodeTraversal nt, Node node, Node parent) {
        return checker.shouldTraverse(nt, node, parent);
      }

      @Override
      public void visit(NodeTraversal nt, Node n, Node parent) {
        if (n.getType() == Token.THIS && checker.shouldReportThis(n, parent)) {
          nt.report(nt.makeError(n, WARNING, CheckGlobalThis.GLOBAL_THIS));
        }
      }
    });
    NodeTraversal.traverse(traversal, node);
    assertTrue(traversal.getDiagnostics().size() > 0);
    DiagnosticType diagnosticType = traversal.getDiagnostics().get(0).getType();
    assertTrue(diagnosticType.getCode() == DiagnosticType.JSC_USED_GLOBAL_THIS.getCode());
  }

  @Test
  public void testShouldNotReportThisOnRightSideOfAssignment() {
    Node node = compiler.parseTree("x = this;");
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    NodeTraversal traversal = NodeTraversal.create(compiler, new Callback() {
      @Override
      public boolean shouldTraverse(NodeTraversal nt, Node node, Node parent) {
        return checker.shouldTraverse(nt, node, parent);
      }

      @Override
      public void visit(NodeTraversal nt, Node n, Node parent) {
        if (n.getType() == Token.THIS && checker.shouldReportThis(n, parent)) {
          nt.report(nt.makeError(n, WARNING, CheckGlobalThis.GLOBAL_THIS));
        }
      }
    });
    NodeTraversal.traverse(traversal, node);
    assertTrue(traversal.getDiagnostics().isEmpty());
  }

  @Test
  public void testShouldReportFunctionWithThisAnnotationOnLeftSideOfAssignment() {
    Node node = compiler.parseTree("@this function foo() { this.x = 1; }");
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    NodeTraversal traversal = NodeTraversal.create(compiler, new Callback() {
      @Override
      public boolean shouldTraverse(NodeTraversal nt, Node node, Node parent) {
        return checker.shouldTraverse(nt, node, parent);
      }

      @Override
      public void visit(NodeTraversal nt, Node n, Node parent) {
        if (n.getType() == Token.THIS && checker.shouldReportThis(n, parent)) {
          nt.report(nt.makeError(n, WARNING, CheckGlobalThis.GLOBAL_THIS));
        }
      }
    });
    NodeTraversal.traverse(traversal, node);
    assertTrue(traversal.getDiagnostics().isEmpty());
  }
}
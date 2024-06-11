import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Callback;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;

public class CheckGlobalThisTest {

  @org.junit.Test
  public void testShouldTraverseFunctionsWithAnnotations() {
    CheckGlobalThis checker = new CheckGlobalThis(null, WARNING);

    Node functionNode = new Node(Token.FUNCTION, null);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setConstructor(true);
    functionNode.setJSDocInfo(jsDoc);

    NodeTraversal traversal = new NodeTraversal() {
      @Override
      public boolean shouldTraverse(NodeTraversal n, Node node, Node parent) {
        return checker.shouldTraverse(this, node, parent);
      }
    };

    assertTrue(checker.shouldTraverse(traversal, functionNode, null));
  }

  @org.junit.Test
  public void testShouldNotTraverseFunctionsWithoutAnnotationsOrInNonGlobalContext() {
    CheckGlobalThis checker = new CheckGlobalThis(null, WARNING);

    Node functionNode = new Node(Token.FUNCTION, null);

    NodeTraversal traversal = new NodeTraversal() {
      @Override
      public boolean shouldTraverse(NodeTraversal n, Node node, Node parent) {
        return checker.shouldTraverse(this, node, parent);
      }
    };

    assertFalse(checker.shouldTraverse(traversal, functionNode, new Node(Token.VAR, null)));
  }

  @org.junit.Test
  public void testShouldReportGlobalThisUse() {
    CheckGlobalThis checker = new CheckGlobalThis(null, WARNING);

    Node functionNode = new Node(Token.FUNCTION, null);
    Node thisNode = new Node(Token.THIS, null);

    NodeTraversal traversal = new NodeTraversal() {
      @Override
      public void visit(NodeTraversal n, Node node, Node parent) {
        checker.visit(this, node, parent);
      }
    };

    checker.visit(traversal, functionNode, null);
    checker.visit(traversal, thisNode, functionNode);

    assertEquals(1, checker.compiler.getDiagnostics().size());
    assertEquals(DiagnosticType.WARNING, checker.compiler.getDiagnostics().get(0).getType());
  }

  @org.junit.Test
  public void testShouldNotReportThisUseInPrototypePropertyAccess() {
    CheckGlobalThis checker = new CheckGlobalThis(null, WARNING);

    Node functionNode = new Node(Token.FUNCTION, null);
    Node getPrototypeNode = new Node(Token.GETPROP, null, new Node(Token.STRING, "prototype"));
    Node thisNode = new Node(Token.THIS, null);
    Node getThisNode = new Node(Token.GETPROP, null, new Node(Token.STRING, "prototype"));

    NodeTraversal traversal = new NodeTraversal() {
      @Override
      public void visit(NodeTraversal n, Node node, Node parent) {
        checker.visit(this, node, parent);
      }
    };

    checker.visit(traversal, functionNode, null);
    checker.visit(traversal, thisNode, functionNode);
    checker.visit(traversal, getPrototypeNode, functionNode);
    checker.visit(traversal, getThisNode, getPrototypeNode);

    assertEquals(0, checker.compiler.getDiagnostics().size());
  }

  @org.junit.Test
  public void testShouldReportThisUseInPrototypePropertyAssignment() {
    CheckGlobalThis checker = new CheckGlobalThis(null, WARNING);

    Node functionNode = new Node(Token.FUNCTION, null);
    Node setPrototypeNode = new Node(Token.SETPROP, null, new Node(Token.STRING, "prototype"), null);
    Node thisNode = new Node(Token.THIS, null);

    NodeTraversal traversal = new NodeTraversal() {
      @Override
      public void visit(NodeTraversal n, Node node, Node parent) {
        checker.visit(this, node, parent);
      }
    };

    checker.visit(traversal, functionNode, null);
    checker.visit(traversal, thisNode, functionNode);
    checker.visit(traversal, setPrototypeNode, functionNode);
    checker.visit(traversal, thisNode, setPrototypeNode);

    assertEquals(1, checker.compiler.getDiagnostics().size());
    assertEquals(DiagnosticType.WARNING, checker.compiler.getDiagnostics().get(0).getType());
  }

  @org.junit.Test(expected = AssertionError.class)
  public void testShouldTraverseAndReportThisUseInNestedAssignments() {
    CheckGlobalThis checker = new CheckGlobalThis(null, WARNING);

    Node assignNode = new Node(Token.ASSIGN, null, null);
    Node leftAssignNode = new Node(Token.ASSIGN, null, null);
    Node thisNode = new Node(Token.THIS, null);

    NodeTraversal traversal = new NodeTraversal() {
      @Override
      public boolean shouldTraverse(NodeTraversal n, Node node, Node parent) {
        return checker.shouldTraverse(this, node, parent);
      }

      @Override
      public void visit(NodeTraversal n, Node node, Node parent) {
        checker.visit(this, node, parent);
      }
    };

    checker.visit(traversal, assignNode, null);
    checker.visit(traversal, leftAssignNode, assignNode);
    checker.visit(traversal, thisNode, leftAssignNode);

    assertEquals(1, checker.compiler.getDiagnostics().size());
    assertEquals(DiagnosticType.WARNING, checker.compiler.getDiagnostics().get(0).getType());
  }
}
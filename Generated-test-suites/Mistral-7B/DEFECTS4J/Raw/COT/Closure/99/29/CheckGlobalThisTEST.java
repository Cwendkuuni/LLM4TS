import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.NodeTraversal.Callback;

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private CheckGlobalThis checker;
  private NodeTraversal traversal;

  @Before
  public void setup() {
    compiler = mock(AbstractCompiler.class);
    checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    traversal = new NodeTraversal(null, checker, null);
  }

  @Test
  public void testShouldTraverseFunctionWithNoAnnotations() {
    Node functionNode = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChild(functionNode);

    assertTrue(checker.shouldTraverse(traversal, functionNode, parent));
  }

  @Test
  public void testShouldNotTraverseFunctionWithConstructorAnnotation() {
    Node functionNode = new Node(Token.FUNCTION);
    Node constructorAnnotation = new Node(Token.ANNOTATION, "constructor");
    functionNode.addChild(constructorAnnotation);
    Node parent = new Node(Token.SCRIPT);
    parent.addChild(functionNode);

    assertFalse(checker.shouldTraverse(traversal, functionNode, parent));
  }

  @Test
  public void testShouldNotTraverseFunctionWithThisAnnotation() {
    Node functionNode = new Node(Token.FUNCTION);
    Node thisAnnotation = new Node(Token.ANNOTATION, "@this");
    functionNode.addChild(thisAnnotation);
    Node parent = new Node(Token.SCRIPT);
    parent.addChild(functionNode);

    assertFalse(checker.shouldTraverse(traversal, functionNode, parent));
  }

  @Test
  public void testShouldNotTraverseFunctionNotInParentFunction() {
    Node functionNode = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);

    assertFalse(checker.shouldTraverse(traversal, functionNode, parent));
  }

  @Test
  public void testShouldTraverseLeftSideOfAssignment() {
    Node assignNode = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.NAME, "a");
    Node rhs = new Node(Token.NUMBER, 1);
    assignNode.addChild(lhs);
    assignNode.addChild(rhs);
    Node parent = new Node(Token.SCRIPT);
    parent.addChild(assignNode);

    Node targetNode = new Node(Token.THIS);
    assertTrue(checker.shouldReportThis(targetNode, assignNode));
  }

  @Test
  public void testShouldNotTraverseLeftSideOfAssignmentToPrototypeProperty() {
    Node assignNode = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.GETPROP, new Node(Token.NAME, "a"), new Node(Token.STRING, "prototype"));
    Node rhs = new Node(Token.NUMBER, 1);
    assignNode.addChild(lhs);
    assignNode.addChild(rhs);
    Node parent = new Node(Token.SCRIPT);
    parent.addChild(assignNode);

    assertFalse(checker.shouldReportThis(new Node(Token.THIS), assignNode));
  }

  @Test
  public void testShouldNotTraverseLeftSideOfAssignmentToSubProperty() {
    Node assignNode = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.GETPROP, new Node(Token.NAME, "a"), new Node(Token.STRING, "x"));
    Node rhs = new Node(Token.NUMBER, 1);
    assignNode.addChild(lhs);
    assignNode.addChild(rhs);
    Node parent = new Node(Token.SCRIPT);
    parent.addChild(assignNode);

    assertFalse(checker.shouldReportThis(new Node(Token.THIS), assignNode));
  }

  @Test
  public void testShouldReportThisOnLeftSideOfAssignment() {
    Node assignNode = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.THIS);
    Node rhs = new Node(Token.NUMBER, 1);
    assignNode.addChild(lhs);
    assignNode.addChild(rhs);
    Node parent = new Node(Token.SCRIPT);
    parent.addChild(assignNode);

    checker.visit(traversal, assignNode, parent);
    when(compiler.report(traversal.makeError(lhs, CheckLevel.WARNING, DiagnosticType.warning("JSC_USED_GLOBAL_THIS", "dangerous use of the global 'this' object"))))
        .thenReturn(null);
    traversal.traverse();

    // No error reported.
    assertTrue(true);
  }

  @Test(expected = AssertionError.class)
  public void testShouldReportThisOnLeftSideOfAssignmentWithError() {
    Node assignNode = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.THIS);
    Node rhs = new Node(Token.NUMBER, 1);
    assignNode.addChild(lhs);
    assignNode.addChild(rhs);
    Node parent = new Node(Token.SCRIPT);
    parent.addChild(assignNode);

    checker.visit(traversal, assignNode, parent);
    when(compiler.report(traversal.makeError(lhs, CheckLevel.WARNING, DiagnosticType.warning("JSC_USED_GLOBAL_THIS", "dangerous use of the global 'this' object"))))
        .thenThrow(new RuntimeException());
    traversal.traverse();

    // Error should be reported.
    fail("Error was not reported.");
  }

  @Test
  public void testShouldNotReportThisOnRightSideOfAssignment() {
    Node assignNode = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.NAME, "a");
    Node rhs = new Node(Token.THIS);
    assignNode.addChild(lhs);
    assignNode.addChild(rhs);
    Node parent = new Node(Token.SCRIPT);
    parent.addChild(assignNode);

    checker.visit(traversal, rhs, assignNode);

    // No error reported.
    assertTrue(true);
  }
}
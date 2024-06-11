import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private CheckGlobalThis checker;
  private NodeTraversal traversal;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    traversal = mock(NodeTraversal.class);
  }

  @Test
  public void testShouldTraverseFunctionAnnotatedWithConstructor() {
    Node functionNode = Node.newFunctionNode(42, null);
    JSDocInfo jsDoc = Node.newJsDocNode("constructor");
    when(functionNode.getJSDocInfo()).thenReturn(jsDoc);

    assertTrue(checker.shouldTraverse(traversal, functionNode, null));
  }

  @Test
  public void testShouldTraverseFunctionAnnotatedWithThis() {
    Node functionNode = Node.newFunctionNode(42, null);
    JSDocInfo jsDoc = Node.newJsDocNode("@this");
    when(functionNode.getJSDocInfo()).thenReturn(jsDoc);

    assertTrue(checker.shouldTraverse(traversal, functionNode, null));
  }

  @Test
  public void testShouldTraverseFunctionWithNoAnnotation() {
    Node functionNode = Node.newFunctionNode(42, null);

    assertTrue(checker.shouldTraverse(traversal, functionNode, null));
  }

  @Test
  public void testShouldNotTraverseFunctionAnnotatedWithInterface() {
    Node functionNode = Node.newFunctionNode(42, null);
    JSDocInfo jsDoc = Node.newJsDocNode("interface");
    when(functionNode.getJSDocInfo()).thenReturn(jsDoc);

    assertFalse(checker.shouldTraverse(traversal, functionNode, null));
  }

  @Test
  public void testShouldNotTraverseFunctionAnnotatedWithOverride() {
    Node functionNode = Node.newFunctionNode(42, null);
    JSDocInfo jsDoc = Node.newJsDocNode("override");
    when(functionNode.getJSDocInfo()).thenReturn(jsDoc);

    assertFalse(checker.shouldTraverse(traversal, functionNode, null));
  }

  @Test
  public void testShouldTraverseFunctionWhereNoLogicalPlaceForThisAnnotation() {
    Node functionNode = Node.newFunctionNode(42, null);

    assertTrue(checker.shouldTraverse(traversal, functionNode, null));
  }

  @Test
  public void testShouldTraverseAssignmentToNullLeftHandSide() {
    Node assignmentNode = Node.newAssignNode(Token.ASSIGN, null, null);
    Node leftHandSide = Node.newNullNode();

    assertTrue(checker.shouldTraverse(traversal, leftHandSide, assignmentNode));
  }

  @Test
  public void testShouldNotTraverseFunctionWithPrototypePropertyAccess() {
    Node functionNode = Node.newFunctionNode(42, null);
    Node prototypeAccess = Node.newGetPropNode(Token.GETPROP, null, "prototype");

    Node propertyAccess = Node.newGetPropNode(Token.GETPROP, null, "prototype");
    Node leftHandSide = Node.newGetPropNode(Token.GETPROP, functionNode, "prototype");
    Node rightHandSide = propertyAccess;

    Node assignmentNode = Node.newAssignNode(Token.ASSIGN, leftHandSide, rightHandSide);

    assertFalse(checker.shouldTraverse(traversal, functionNode, assignmentNode));
  }

  @Test
  public void testShouldNotTraverseFunctionWithPrototypeSubPropertyAccess() {
    Node functionNode = Node.newFunctionNode(42, null);
    Node prototypeAccess = Node.newGetPropNode(Token.GETPROP, null, "prototype");

    Node propertyAccess = Node.newGetPropNode(Token.GETPROP, null, "prototype");
    Node subPropertyAccess = Node.newGetPropNode(Token.GETPROP, propertyAccess, "subProperty");

    Node leftHandSide = Node.newGetPropNode(Token.GETPROP, functionNode, "prototype");
    Node rightHandSide = subPropertyAccess;

    Node assignmentNode = Node.Node.newAssignNode(Token.ASSIGN, leftHandSide, rightHandSide);

    assertFalse(checker.shouldTraverse(traversal, functionNode, assignmentNode));
  }

  @Test
  public void testShouldTraverseThisKeywordOnLeftSideOfAssignment() {
    Node assignmentNode = Node.newAssignNode(Token.ASSIGN, null, null);
    Node leftHandSide = Node.newThisNode();

    assertTrue(checker.shouldTraverse(traversal, leftHandSide, assignmentNode));
  }

  @Test
  public void testShouldTraverseThisKeywordOnLeftSideOfPropertyAccess() {
    Node propertyAccessNode = Node.newGetPropNode(Token.GETPROP, null, "propertyName");
    Node thisNode = Node.newThisNode();

    Node node = Node.newGetPropNode(Token.GETPROP, thisNode, "propertyName");
    Node parentNode = Node.newAssignNode(Token.ASSIGN, null, node);

    assertTrue(checker.shouldTraverse(traversal, thisNode, parentNode));
  }

  @Test(expected = AssertionError.class)
  public void testShouldReportThisKeywordOnLeftSideOfAssignment() {
    Node assignmentNode = Node.newAssignNode(Token.ASSIGN, null, null);
    Node leftHandSide = Node.newThisNode();

    checker.visit(traversal, leftHandSide, assignmentNode);
  }

  @Test(expected = AssertionError.class)
  public void testShouldReportThisKeywordOnLeftSideOfPropertyAccess() {
    Node propertyAccessNode = Node.newGetPropNode(Token.GETPROP, null, "propertyName");
    Node thisNode = Node.newThisNode();

    Node node = Node.newGetPropNode(Token.GETPROP, thisNode, "propertyName");
    Node parentNode = Node.newAssignNode(Token.ASSIGN, null, node);

    checker.visit(traversal, thisNode, parentNode);
  }

  @Test
  public void testShouldNotReportThisKeywordOnRightSideOfAssignment() {
    Node assignmentNode = Node.newAssignNode(Token.ASSIGN, null, null);
    Node leftHandSide = Node.newThisNode();
    Node rightHandSide = Node.newIntNode(42);

    assertFalse(checker.shouldReportThis(leftHandSide, assignmentNode));
  }

  @Test
  public void testShouldNotReportThisKeywordOnRightSideOfPropertyAccess() {
    Node propertyAccessNode = Node.newGetPropNode(Token.GETPROP, null, "propertyName");
    Node thisNode = Node.newThisNode();

    Node node = Node.newGetPropNode(Token.GETPROP, thisNode, "propertyName");
    Node parentNode = Node.newGetPropNode(Token.GETPROP, null, "propertyName");

    assertFalse(checker.shouldReportThis(thisNode, parentNode));
  }
}
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
import com.google.javascript.rhino.JSDocInfo;

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

  private CheckGlobalThis checker;
  private AbstractCompiler compilerMock;

  @Before
  public void setUp() {
    compilerMock = mock(AbstractCompiler.class);
    checker = new CheckGlobalThis(compilerMock, CheckLevel.WARNING);
  }

  @Test
  public void testShouldTraverseFunction() {
    Node functionNode = Node.newFunctionNode(Node.newNameNode("functionName"));
    Node parentNode = Node.newBlockNode();

    when(functionNode.getParent()).thenReturn(parentNode);
    when(parentNode.getType()).thenReturn(Token.SCRIPT);

    assertTrue(checker.shouldTraverse(NodeTraversal.create(null, null), functionNode, parentNode));
  }

  @Test
  public void testShouldNotTraverseConstructorFunction() {
    Node constructorFunctionNode = Node.newFunctionNode(Node.newNameNode("constructorFunctionName"), Node.newNameNode("ClassName"), Node.newListNode(Node.newNameNode("this"), Node.newNameNode("Argument1")));

    Node parentNode = Node.newBlockNode();

    when(constructorFunctionNode.getParent()).thenReturn(parentNode);
    when(constructorFunctionNode.getJSDocInfo()).thenReturn(new JSDocInfo(constructorFunctionNode, true, null, null, null));

    assertFalse(checker.shouldTraverse(NodeTraversal.create(null, null), constructorFunctionNode, parentNode));
  }

  @Test
  public void testShouldNotTraverseFunctionWithThisAnnotation() {
    Node functionNode = Node.newFunctionNode(Node.newNameNode("functionName"), null, Node.newListNode());
    Node thisAnnotationNode = Node.newIdentNode("this");
    functionNode.setJSDocInfo(new JSDocInfo(functionNode, null, null, null, new Node[] { thisAnnotationNode }));

    Node parentNode = Node.newBlockNode();

    when(functionNode.getParent()).thenReturn(parentNode);

    assertFalse(checker.shouldTraverse(NodeTraversal.create(null, null), functionNode, parentNode));
  }

  @Test
  public void testShouldNotTraverseFunctionWithNoLogicalPlaceForThisAnnotation() {
    Node functionNode = Node.newFunctionNode(Node.newNameNode("functionName"), null, Node.newListNode());

    Node parentNode = Node.newBlockNode();

    when(functionNode.getParent()).thenReturn(parentNode);
    when(parentNode.getType()).thenReturn(Token.ASSIGN);

    assertFalse(checker.shouldTraverse(NodeTraversal.create(null, null), functionNode, parentNode));
  }

  @Test
  public void testShouldTraverseAssignmentLeftSide() {
    Node assignNode = Node.newAssignNode(Node.newNameNode("assignVariableName"), Node.newIdentNode("this"));
    Node parentNode = Node.newBlockNode();

    assertTrue(checker.shouldTraverse(NodeTraversal.create(null, null), assignNode, parentNode));
  }

  @Test
  public void testShouldNotTraverseAssignmentRightSide() {
    Node assignNode = Node.newAssignNode(Node.newIdentNode("assignVariableName"), Node.newIdentNode("this"));
    Node parentNode = Node.newBlockNode();

    Node getPropertyNode = Node.newGetNode(Node.newIdentNode("assignVariableName"), Node.newNameNode("prototype"));

    when(assignNode.getParent()).thenReturn(parentNode);
    when(parentNode.getType()).thenReturn(Token.NAME);
    when(parentNode.getFirstChild()).thenReturn(getPropertyNode);

    assertFalse(checker.shouldTraverse(NodeTraversal.create(null, null), assignNode, parentNode));
  }

  @Test(expected = AssertionError.class)
  public void testShouldReportThisKeyword() {
    Node thisKeywordNode = Node.newKeywordNode(Token.THIS);
    Node parentNode = Node.newBlockNode();

    checker.shouldReportThis(NodeTraversal.create(null, null), thisKeywordNode, parentNode);
  }
}
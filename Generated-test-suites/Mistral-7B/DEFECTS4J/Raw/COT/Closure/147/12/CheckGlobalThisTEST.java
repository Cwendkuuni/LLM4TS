import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.CheckLevel.WARNING;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;

@RunWith(MockitoJUnitRunner.class)
public class CheckGlobalThisTest {

  @Mock
  private AbstractCompiler compiler;
  private CheckGlobalThis checker;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    checker = new CheckGlobalThis(compiler, WARNING);
  }

  @Test
  public void testShouldTraverseFunctionWithNoAnnotations() {
    Node functionNode = Node.newFunctionNode(1, null, null, null, null, null, null);
    Node parentNode = Node.newBlockNode(1, null);
    assertTrue(checker.shouldTraverse(new NodeTraversal(checker), functionNode, parentNode));
  }

  @Test
  public void testShouldNotTraverseFunctionWithConstructorAnnotation() {
    Node functionNode = Node.newFunctionNode(1, null, null, null, null, null, null);
    JSDocInfo jsDoc = Node.newJSDocInfo("constructor");
    functionNode.setJSDocInfo(jsDoc);
    Node parentNode = Node.newBlockNode(1, null);
    assertFalse(checker.shouldTraverse(new NodeTraversal(checker), functionNode, parentNode));
  }

  @Test
  public void testShouldNotTraverseFunctionWithThisAnnotation() {
    Node functionNode = Node.newFunctionNode(1, null, null, null, null, null, null);
    JSDocInfo jsDoc = Node.newJSDocInfo("this");
    functionNode.setJSDocInfo(jsDoc);
    Node parentNode = Node.newBlockNode(1, null);
    assertFalse(checker.shouldTraverse(new NodeTraversal(checker), functionNode, parentNode));
  }

  @Test
  public void testShouldNotTraverseFunctionInsidePrototypeProperty() {
    Node functionNode = Node.newFunctionNode(1, null, null, null, null, null, null);
    Node getPropNode = Node.newGetpropNode(1, Node.newIdentifierNode("prototype"), functionNode);
    Node parentNode = Node.newBlockNode(1, null);
    assertFalse(checker.shouldTraverse(new NodeTraversal(checker), functionNode, parentNode));
  }

  @Test
  public void testShouldNotTraverseFunctionInsidePrototypeSubProperty() {
    Node functionNode = Node.newFunctionNode(1, null, null, null, null, null, null);
    Node getPropNode = Node.newGetpropNode(1, Node.newIdentifierNode("prototype"), Node.newIdentifierNode("property"));
    Node parentNode = Node.newBlockNode(1, null);
    assertFalse(checker.shouldTraverse(new NodeTraversal(checker), functionNode, parentNode));
  }

  @Test
  public void testShouldTraverseLeftSideOfAssignment() {
    Node assignNode = Node.newAssignNode(1, Node.newIdentifierNode("a"), Node.newIdentifierNode("this"));
    Node parentNode = Node.newBlockNode(1, null);
    assertTrue(checker.shouldTraverse(new NodeTraversal(checker), assignNode, parentNode));
  }

  @Test
  public void testShouldNotTraverseRightSideOfAssignmentToPrototypeProperty() {
    Node assignNode = Node.newAssignNode(1, Node.newIdentifierNode("prototype"), Node.newIdentifierNode("this"));
    Node parentNode = Node.newBlockNode(1, null);
    assertFalse(checker.shouldTraverse(new NodeTraversal(checker), assignNode, parentNode));
  }

  @Test
  public void testShouldNotTraverseRightSideOfAssignmentToPrototypeSubProperty() {
    Node assignNode = Node.newAssignNode(1, Node.newGetpropNode(1, Node.newIdentifierNode("prototype"), Node.newIdentifierNode("property")), Node.newIdentifierNode("this"));
    Node parentNode = Node.newBlockNode(1, null);
    assertFalse(checker.shouldTraverse(new NodeTraversal(checker), assignNode, parentNode));
  }

  @Test
  public void testShouldNotTraverseRightSideOfAssignmentToNonPrototypeProperty() {
    Node assignNode = Node.newAssignNode(1, Node.newIdentifierNode("a"), Node.newIdentifierNode("this"));
    Node parentNode = Node.newBlockNode(1, null);
    assertFalse(checker.shouldTraverse(new NodeTraversal(checker), assignNode, parentNode));
  }

  @Test
  public void testShouldReportThisKeywordOnLeftSideOfAssignment() {
    Node assignNode = Node.newAssignNode(1, Node.newIdentifierNode("a"), Node.newIdentifierNode("this"));
    Node parentNode = Node.newBlockNode(1, null);
    assertTrue(checker.shouldReportThis(assignNode, parentNode));
  }

  @Test
  public void testShouldReportThisKeywordOnLeftSideOfGetProperty() {
    Node getNode = Node.newGetpropNode(1, Node.newIdentifierNode("this"), Node.newIdentifierNode("property"));
    Node parentNode = Node.newBlockNode(1, null);
    assertTrue(checker.shouldReportThis(getNode, parentNode));
  }

  @Test
  public void testShouldNotReportThisKeywordOnRightSideOfAssignment() {
    Node assignNode = Node.newAssignNode(1, Node.newIdentifierNode("this"), Node.newIdentifierNode("a"));
    Node parentNode = Node.newBlockNode(1, null);
    assertFalse(checker.shouldReportThis(assignNode, parentNode));
  }

  @Test
  public void testShouldReportThisKeywordOnRightSideOfGetProperty() {
    Node getNode = Node.newGetpropNode(1, Node.newIdentifierNode("this"), Node.newIdentifierNode("property"));
    Node parentNode = Node.newBlockNode(1, null);
    assertFalse(checker.shouldReportThis(getNode, parentNode));
  }
}
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
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private CheckGlobalThis checker;
  private NodeTraversal traversal;

  @Before
  public void setup() {
    compiler = mock(AbstractCompiler.class);
    LevelMock levelMock = new LevelMock();
    checker = new CheckGlobalThis(compiler, levelMock);
    traversal = mock(NodeTraversal.class);
  }

  @Test
  public void testShouldTraverseFunctionWithoutAnnotation() {
    Node functionNode = Node.newFunctionNode("functionName", null, null, null, null, null, null, null, null, null);
    Node parentNode = Node.newNameNode("parentName");
    Node grandParentNode = Node.newNameNode("grandParentName");

    when(compiler.getTraversal()).thenReturn(traversal);
    when(traversal.getContext()).thenReturn(grandParentNode);

    assertTrue(checker.shouldTraverse(ArgumentMatchers.eq(traversal), ArgumentMatchers.eq(functionNode), ArgumentMatchers.eq(parentNode)));
  }

  @Test
  public void testShouldNotTraverseConstructorFunction() {
    Node functionNode = Node.newFunctionNode("constructorFunction", null, null, null, null, null, null, null, null, null);
    Node parentNode = Node.newNameNode("parentNode");

    JSDocInfo jsDocInfo = Node.newJSDocInfo().setConstructor(true);
    when(functionNode.getJSDocInfo()).thenReturn(jsDocInfo);

    assertFalse(checker.shouldTraverse(ArgumentMatchers.eq(traversal), ArgumentMatchers.eq(functionNode), ArgumentMatchers.eq(parentNode)));
  }

  @Test
  public void testShouldNotTraverseFunctionWithThisAnnotation() {
    Node functionNode = Node.newFunctionNode("functionWithThisAnnotation", null, null, null, null, null, null, null, null, null);
    Node parentNode = Node.newNameNode("parentNode");

    JSDocInfo jsDocInfo = Node.newJSDocInfo().setThisType(true);
    when(functionNode.getJSDocInfo()).thenReturn(jsDocInfo);

    assertFalse(checker.shouldTraverse(ArgumentMatchers.eq(traversal), ArgumentMatchers.eq(functionNode), ArgumentMatchers.eq(parentNode)));
  }

  @Test
  public void testShouldNotTraverseFunctionWithOverrideAnnotation() {
    Node functionNode = Node.newFunctionNode("functionWithOverrideAnnotation", null, null, null, null, null, null, null, null, null);
    Node parentNode = Node.newNameNode("parentNode");

    JSDocInfo jsDocInfo = Node.newJSDocInfo().setOverride(true);
    when(functionNode.getJSDocInfo()).thenReturn(jsDocInfo);

    assertFalse(checker.shouldTraverse(ArgumentMatchers.eq(traversal), ArgumentMatchers.eq(functionNode), ArgumentMatchers.eq(parentNode)));
  }

  @Test
  public void testShouldNotTraverseFunctionNotInGlobalContext() {
    Node functionNode = Node.newFunctionNode("functionNotInGlobalContext", null, null, null, null, null, null, null, null, null);
    Node parentNode = Node.newBlockNode();

    assertFalse(checker.shouldTraverse(ArgumentMatchers.eq(traversal), ArgumentMatchers.eq(functionNode), ArgumentMatchers.eq(parentNode)));
  }

  @Test
  public void testShouldTraverseLeftSideOfAssignment() {
    Node assignNode = Node.newAssignNode(null, null, null, null, null, null, null, null, null, null, null);
    Node leftSideNode = assignNode.getLeftHandSide();
    Node rightSideNode = assignNode.getRightHandSide();

    Node parentNode = Node.newNameNode("parentNode");

    when(compiler.getTraversal()).thenReturn(traversal);

    assertTrue(checker.shouldTraverse(ArgumentMatchers.eq(traversal), ArgumentMatchers.eq(leftSideNode), ArgumentMatchers.eq(parentNode)));
  }

  @Test
  public void testShouldNotTraversePrototypePropertyAccess() {
    Node getPropNode = Node.newGetPropNode(null, Node.newNameNode("prototype"), null, null, null, null, null, null, null, null);
    Node parentNode = Node.newNameNode("parentNode");

    assertFalse(checker.shouldTraverse(ArgumentMatchers.eq(traversal), ArgumentMatchers.eq(getPropNode), ArgumentMatchers.eq(parentNode)));
  }

  @Test
  public void testShouldReportThisKeywordOnLeftSideOfAssignment() {
    Node assignNode = Node.newAssignNode(null, null, Node.newThisNode(), null, null, null, null, null, null, null, null);
    Node parentNode = Node.newNameNode("parentNode");

    Node rootNode = Node.newBlockNode();
    when(traversal.getContext()).thenReturn(rootNode);

    checker.visit(ArgumentMatchers.eq(traversal), ArgumentMatchers.eq(assignNode), ArgumentMatchers.eq(parentNode));

    Node diagnostic = compiler.getDiagnostics().get(0);
    assertEquals(DiagnosticType.WARNING, diagnostic.getType());
    assertEquals("JSC_USED_GLOBAL_THIS", diagnostic.getMessageId());
  }

  @Test
  public void testShouldNotReportThisKeywordOnRightSideOfAssignment() {
    Node assignNode = Node.newAssignNode(Node.newThisNode(), null, null, null, null, null, null, null, null, null, null);
    Node parentNode = Node.newNameNode("parentNode");

    when(compiler.getTraversal()).thenReturn(traversal);

    assertFalse(checker.shouldReportThis(ArgumentMatchers.eq(assignNode), ArgumentMatchers.eq(parentNode)));
  }

  @Test
  public void testShouldNotReportThisKeywordOnGetPropertyAccess() {
    Node getPropNode = Node.newGetPropNode(null, Node.newNameNode("this"), null, null, null, null, null, null, null, null);
    Node parentNode = Node.newNameNode("parentNode");

    when(compiler.getTraversal()).thenReturn(traversal);

    assertFalse(checker.shouldReportThis(ArgumentMatchers.eq(getPropNode), ArgumentMatchers.eq(parentNode)));
  }

  private static class LevelMock extends CheckLevel {
    @Override
    public boolean isOn(DiagnosticType type) {
      return true;
    }
  }
}
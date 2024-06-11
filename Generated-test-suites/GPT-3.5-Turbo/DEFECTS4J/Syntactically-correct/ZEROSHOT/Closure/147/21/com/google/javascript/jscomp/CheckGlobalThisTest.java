import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;

public class CheckGlobalThisTest {

  @Mock
  private AbstractCompiler compiler;

  @Mock
  private NodeTraversal traversal;

  private CheckLevel level;

  private CheckGlobalThis checker;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    level = CheckLevel.WARNING;
    checker = new CheckGlobalThis(compiler, level);
  }

  @Test
  public void shouldTraverse_ReturnsFalse_WhenFunctionIsConstructor() {
    Node functionNode = createFunctionNode("function Test() {}", true, false, false, false);
    boolean result = checker.shouldTraverse(traversal, functionNode, null);
    assertEquals(false, result);
  }

  @Test
  public void shouldTraverse_ReturnsFalse_WhenFunctionIsInterface() {
    Node functionNode = createFunctionNode("function Test() {}", false, true, false, false);
    boolean result = checker.shouldTraverse(traversal, functionNode, null);
    assertEquals(false, result);
  }

  @Test
  public void shouldTraverse_ReturnsFalse_WhenFunctionHasThisTypeAnnotation() {
    Node functionNode = createFunctionNode("function Test() {}", false, false, true, false);
    boolean result = checker.shouldTraverse(traversal, functionNode, null);
    assertEquals(false, result);
  }

  @Test
  public void shouldTraverse_ReturnsFalse_WhenFunctionIsOverride() {
    Node functionNode = createFunctionNode("function Test() {}", false, false, false, true);
    boolean result = checker.shouldTraverse(traversal, functionNode, null);
    assertEquals(false, result);
  }

  @Test
  public void shouldTraverse_ReturnsFalse_WhenParentIsNotValid() {
    Node parent = createNode(Token.ADD);
    Node functionNode = createFunctionNode("function Test() {}", false, false, false, false);
    boolean result = checker.shouldTraverse(traversal, functionNode, parent);
    assertEquals(false, result);
  }

  @Test
  public void shouldTraverse_ReturnsTrue_WhenFunctionIsValid() {
    Node parent = createNode(Token.BLOCK);
    Node functionNode = createFunctionNode("function Test() {}", false, false, false, false);
    boolean result = checker.shouldTraverse(traversal, functionNode, parent);
    assertEquals(true, result);
  }

  @Test
  public void visit_CallsCompilerReport_WhenThisIsUnsafe() {
    Node thisNode = createNode(Token.THIS);
    Node parent = createNode(Token.GETPROP);
    when(traversal.makeError(thisNode, level, CheckGlobalThis.GLOBAL_THIS)).thenReturn(mock(Error.class));

    checker.visit(traversal, thisNode, parent);
    verify(compiler).report(any(Error.class));
  }

  @Test
  public void visit_DoesNotCallCompilerReport_WhenThisIsSafe() {
    Node thisNode = createNode(Token.THIS);
    Node parent = createNode(Token.ADD);
    when(traversal.makeError(thisNode, level, CheckGlobalThis.GLOBAL_THIS)).thenReturn(mock(Error.class));

    checker.visit(traversal, thisNode, parent);
    verify(compiler, never()).report(any(Error.class));
  }

  @Test
  public void visit_ResetsAssignLhsChild_WhenNodeIsAssignLhsChild() {
    Node thisNode = createNode(Token.THIS);
    Node parent = createNode(Token.NAME);
    checker.assignLhsChild = thisNode;

    checker.visit(traversal, thisNode, parent);
    assertEquals(null, checker.assignLhsChild);
  }

  private Node createFunctionNode(String code, boolean isConstructor, boolean isInterface, boolean hasThisType, boolean isOverride) {
    Node functionNode = createNode(Token.FUNCTION);
    Node nameNode = createNode(Token.NAME);
    Node blockNode = createNode(Token.BLOCK);

    functionNode.addChildToBack(nameNode);
    functionNode.addChildToBack(blockNode);

    if (isConstructor) {
      setJsDocInfo(functionNode, true, false, false, false);
    }

    if (isInterface) {
      setJsDocInfo(functionNode, false, true, false, false);
    }

    if (hasThisType) {
      setJsDocInfo(functionNode, false, false, true, false);
    }

    if (isOverride) {
      setJsDocInfo(functionNode, false, false, false, true);
    }

    return functionNode;
  }

  private Node createNode(int type) {
    Node node = new Node(type);
    return node;
  }

  private void setJsDocInfo(Node node, boolean isConstructor, boolean isInterface, boolean hasThisType, boolean isOverride) {
    node.setJSDocInfo(mock(JSDocInfo.class));

    when(node.getJSDocInfo().isConstructor()).thenReturn(isConstructor);
    when(node.getJSDocInfo().isInterface()).thenReturn(isInterface);
    when(node.getJSDocInfo().hasThisType()).thenReturn(hasThisType);
    when(node.getJSDocInfo().isOverride()).thenReturn(isOverride);
  }
}
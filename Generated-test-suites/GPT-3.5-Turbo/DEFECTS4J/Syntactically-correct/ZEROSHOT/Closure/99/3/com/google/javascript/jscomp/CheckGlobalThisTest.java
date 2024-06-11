import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class CheckGlobalThisTest {
  
  private AbstractCompiler compilerMock;
  private CheckLevel levelMock;
  private NodeTraversal traversalMock;
  private Node nodeMock;
  private Node parentMock;
  private CheckGlobalThis checkGlobalThis;
  
  @Before
  public void setUp() {
    compilerMock = mock(AbstractCompiler.class);
    levelMock = mock(CheckLevel.class);
    traversalMock = mock(NodeTraversal.class);
    nodeMock = mock(Node.class);
    parentMock = mock(Node.class);
    checkGlobalThis = new CheckGlobalThis(compilerMock, levelMock);
  }
  
  @Test
  public void testShouldTraverse_FunctionWithConstructorAnnotation_ReturnsFalse() {
    Node functionNode = mock(Node.class);
    JSDocInfo jsDocInfoMock = mock(JSDocInfo.class);
    when(functionNode.getType()).thenReturn(Token.FUNCTION);
    when(checkGlobalThis.getFunctionJsDocInfo(functionNode)).thenReturn(jsDocInfoMock);
    when(jsDocInfoMock.isConstructor()).thenReturn(true);
    
    boolean result = checkGlobalThis.shouldTraverse(traversalMock, functionNode, parentMock);
    
    verifyZeroInteractions(traversalMock);
    verifyZeroInteractions(parentMock);
    assertEquals(false, result);
  }
  
  @Test
  public void testShouldTraverse_FunctionWithThisTypeAnnotation_ReturnsFalse() {
    Node functionNode = mock(Node.class);
    JSDocInfo jsDocInfoMock = mock(JSDocInfo.class);
    when(functionNode.getType()).thenReturn(Token.FUNCTION);
    when(checkGlobalThis.getFunctionJsDocInfo(functionNode)).thenReturn(jsDocInfoMock);
    when(jsDocInfoMock.hasThisType()).thenReturn(true);
    
    boolean result = checkGlobalThis.shouldTraverse(traversalMock, functionNode, parentMock);
    
    verifyZeroInteractions(traversalMock);
    verifyZeroInteractions(parentMock);
    assertEquals(false, result);
  }
  
  @Test
  public void testShouldTraverse_FunctionWithOverrideAnnotation_ReturnsFalse() {
    Node functionNode = mock(Node.class);
    JSDocInfo jsDocInfoMock = mock(JSDocInfo.class);
    when(functionNode.getType()).thenReturn(Token.FUNCTION);
    when(checkGlobalThis.getFunctionJsDocInfo(functionNode)).thenReturn(jsDocInfoMock);
    when(jsDocInfoMock.isOverride()).thenReturn(true);
    
    boolean result = checkGlobalThis.shouldTraverse(traversalMock, functionNode, parentMock);
    
    verifyZeroInteractions(traversalMock);
    verifyZeroInteractions(parentMock);
    assertEquals(false, result);
  }
  
  @Test
  public void testShouldTraverse_FunctionWithinIllegalParent_ReturnsFalse() {
    Node functionNode = mock(Node.class);
    Node invalidParentNode = mock(Node.class);
    when(functionNode.getType()).thenReturn(Token.FUNCTION);
    when(parentMock.getType()).thenReturn(invalidParentNode.getType());
    
    boolean result = checkGlobalThis.shouldTraverse(traversalMock, functionNode, parentMock);
    
    verifyZeroInteractions(traversalMock);
    verifyZeroInteractions(parentMock);
    assertEquals(false, result);
  }
  
  @Test
  public void testShouldTraverse_FunctionWithinValidParent_ReturnsTrue() {
    Node functionNode = mock(Node.class);
    when(functionNode.getType()).thenReturn(Token.FUNCTION);
    when(parentMock.getType()).thenReturn(Token.BLOCK);
    
    boolean result = checkGlobalThis.shouldTraverse(traversalMock, functionNode, parentMock);
    
    verifyZeroInteractions(traversalMock);
    verifyZeroInteractions(parentMock);
    assertEquals(true, result);
  }
  
  @Test
  public void testShouldTraverse_FunctionInsideAssign_WithAssignLhsChild_ReturnsTrue() {
    Node functionNode = mock(Node.class);
    Node lhsNode = mock(Node.class);
    Node rhsNode = mock(Node.class);
    when(parentMock.getType()).thenReturn(Token.ASSIGN);
    when(parentMock.getFirstChild()).thenReturn(lhsNode);
    when(lhsNode.getNext()).thenReturn(rhsNode);
    checkGlobalThis.assignLhsChild = lhsNode;
    
    boolean result = checkGlobalThis.shouldTraverse(traversalMock, functionNode, parentMock);
    
    verifyZeroInteractions(traversalMock);
    verifyZeroInteractions(parentMock);
    assertNull(checkGlobalThis.assignLhsChild);
    assertEquals(true, result);
  }
  
  @Test
  public void testShouldTraverse_FunctionInsideAssign_WithoutAssignLhsChild_ReturnsFalse() {
    Node functionNode = mock(Node.class);
    Node lhsNode = mock(Node.class);
    Node rhsNode = mock(Node.class);
    when(functionNode.getType()).thenReturn(Token.FUNCTION);
    when(parentMock.getType()).thenReturn(Token.ASSIGN);
    when(parentMock.getFirstChild()).thenReturn(lhsNode);
    when(lhsNode.getNext()).thenReturn(rhsNode);
    
    boolean result = checkGlobalThis.shouldTraverse(traversalMock, functionNode, parentMock);
    
    verifyZeroInteractions(traversalMock);
    verifyZeroInteractions(parentMock);
    assertNull(checkGlobalThis.assignLhsChild);
    assertEquals(false, result);
  }
  
  @Test
  public void testShouldTraverse_DefinedRightChildOfAssign_ReturnsFalse() {
    Node childNode = mock(Node.class);
    Node rhsNode = mock(Node.class);
    Node lhsNode = mock(Node.class);
    when(parentMock.getType()).thenReturn(Token.ASSIGN);
    when(parentMock.getFirstChild()).thenReturn(lhsNode);
    when(lhsNode.getType()).thenReturn(Token.GETPROP);
    when(lhsNode.getLastChild()).thenReturn(childNode);
    when(childNode.getString()).thenReturn("prototype");
    when(childNode.getType()).thenReturn(Token.STRING);
    
    boolean result = checkGlobalThis.shouldTraverse(traversalMock, rhsNode, parentMock);
    
    verifyZeroInteractions(traversalMock);
    verifyZeroInteractions(parentMock);
    assertEquals(false, result);
  }
  
  @Test
  public void testShouldTraverse_DefinedLeftChildOfAssignInRange_ReturnsTrue() {
    Node childNode = mock(Node.class);
    Node rhsNode = mock(Node.class);
    Node lhsNode = mock(Node.class);
    when(parentMock.getType()).thenReturn(Token.ASSIGN);
    when(parentMock.getFirstChild()).thenReturn(lhsNode);
    when(parentMock.getFirstChild() == rhsNode).thenReturn(false);
    when(lhsNode.getType()).thenReturn(Token.GETPROP);
    when(lhsNode.getQualifiedName()).thenReturn("object.prototype.property");
    when(lhsNode.getQualifiedName().contains(".prototype.")).thenReturn(true);
    
    boolean result = checkGlobalThis.shouldTraverse(traversalMock, lhsNode, parentMock);
    
    verifyZeroInteractions(traversalMock);
    verifyZeroInteractions(parentMock);
    assertEquals(true, result);
  }
  
  @Test
  public void testShouldTraverse_DefinedLeftChildOfAssignNotInRange_ReturnsFalse() {
    Node childNode = mock(Node.class);
    Node rhsNode = mock(Node.class);
    Node lhsNode = mock(Node.class);
    when(parentMock.getType()).thenReturn(Token.ASSIGN);
    when(parentMock.getFirstChild()).thenReturn(lhsNode);
    when(parentMock.getFirstChild() == rhsNode).thenReturn(false);
    when(lhsNode.getType()).thenReturn(Token.GETPROP);
    when(lhsNode.getQualifiedName()).thenReturn("object.property");
    when(lhsNode.getQualifiedName().contains(".prototype.")).thenReturn(false);
    
    boolean result = checkGlobalThis.shouldTraverse(traversalMock, lhsNode, parentMock);
    
    verifyZeroInteractions(traversalMock);
    verifyZeroInteractions(parentMock);
    assertEquals(false, result);
  }
  
  @Test
  public void testVisit_ThisNodeWithShouldReportThis_ReturnsTrue() {
    Node thisNode = mock(Node.class);
    when(thisNode.getType()).thenReturn(Token.THIS);
    when(checkGlobalThis.shouldReportThis(thisNode, parentMock)).thenReturn(true);
    
    checkGlobalThis.visit(traversalMock, thisNode, parentMock);
    
    verify(compilerMock).report(any());
  }
  
  @Test
  public void testVisit_ThisNodeWithoutShouldReportThis_DoesNotReportError() {
    Node thisNode = mock(Node.class);
    when(thisNode.getType()).thenReturn(Token.THIS);
    when(checkGlobalThis.shouldReportThis(thisNode, parentMock)).thenReturn(false);
    
    checkGlobalThis.visit(traversalMock, thisNode, parentMock);
    
    verifyZeroInteractions(compilerMock);
  }
  
  @Test
  public void testVisit_NodeEqualsAssignLhsChild_SetsAssignLhsChildToNull() {
    Node assignChildNode = mock(Node.class);
    checkGlobalThis.assignLhsChild = assignChildNode;
    
    checkGlobalThis.visit(traversalMock, assignChildNode, parentMock);
    
    assertNull(checkGlobalThis.assignLhsChild);
  }
}
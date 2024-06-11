import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class CheckGlobalThisTest {

  @InjectMocks
  private CheckGlobalThis checkGlobalThis;

  @Before
  public void initMocks() {
    MockitoAnnotations.initMocks(this);
  }
  
  @Test
  public void testShouldTraverse_NonFunctionNode_ReturnsTrue() {
    NodeTraversal t = mock(NodeTraversal.class);
    Node n = mock(Node.class);
    Node parent = mock(Node.class);
    when(n.getType()).thenReturn(Token.SCRIPT);
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertTrue(result);
  }
  
  @Test
  public void testShouldTraverse_FunctionNode_Constructor_ReturnsFalse() {
    NodeTraversal t = mock(NodeTraversal.class);
    Node n = mock(Node.class);
    Node parent = mock(Node.class);
    JSDocInfo jsDoc = mock(JSDocInfo.class);
    when(n.getType()).thenReturn(Token.FUNCTION);
    when(checkGlobalThis.getFunctionJsDocInfo(n)).thenReturn(jsDoc);
    when(jsDoc.isConstructor()).thenReturn(true);
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_FunctionNode_ThisTypeAnnotation_ReturnsFalse() {
    NodeTraversal t = mock(NodeTraversal.class);
    Node n = mock(Node.class);
    Node parent = mock(Node.class);
    JSDocInfo jsDoc = mock(JSDocInfo.class);
    when(n.getType()).thenReturn(Token.FUNCTION);
    when(checkGlobalThis.getFunctionJsDocInfo(n)).thenReturn(jsDoc);
    when(jsDoc.hasThisType()).thenReturn(true);
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_FunctionNode_OverrideAnnotation_ReturnsFalse() {
    NodeTraversal t = mock(NodeTraversal.class);
    Node n = mock(Node.class);
    Node parent = mock(Node.class);
    JSDocInfo jsDoc = mock(JSDocInfo.class);
    when(n.getType()).thenReturn(Token.FUNCTION);
    when(checkGlobalThis.getFunctionJsDocInfo(n)).thenReturn(jsDoc);
    when(jsDoc.isOverride()).thenReturn(true);
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionNode_ValidParent_ReturnsTrue() {
    NodeTraversal t = mock(NodeTraversal.class);
    Node n = mock(Node.class);
    Node parent = mock(Node.class);
    when(n.getType()).thenReturn(Token.FUNCTION);
    when(checkGlobalThis.getFunctionJsDocInfo(n)).thenReturn(null);
    when(parent.getType()).thenReturn(Token.BLOCK);
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertTrue(result);
  }
  
  @Test
  public void testShouldTraverse_FunctionNode_InvalidParent_ReturnsFalse() {
    NodeTraversal t = mock(NodeTraversal.class);
    Node n = mock(Node.class);
    Node parent = mock(Node.class);
    when(n.getType()).thenReturn(Token.FUNCTION);
    when(checkGlobalThis.getFunctionJsDocInfo(n)).thenReturn(null);
    when(parent.getType()).thenReturn(Token.ADD);
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertFalse(result);
  }
  
  @Test
  public void testVisit_ThisNode_ReportsError() {
    NodeTraversal t = mock(NodeTraversal.class);
    Node n = mock(Node.class);
    Node parent = mock(Node.class);
    when(n.getType()).thenReturn(Token.THIS);
    when(checkGlobalThis.shouldReportThis(n, parent)).thenReturn(true);
    
    checkGlobalThis.visit(t, n, parent);
    
    verify(checkGlobalThis.compiler).report(any());
  }
  
  @Test
  public void testVisit_ThisNode_DoesNotReportError() {
    NodeTraversal t = mock(NodeTraversal.class);
    Node n = mock(Node.class);
    Node parent = mock(Node.class);
    when(n.getType()).thenReturn(Token.THIS);
    when(checkGlobalThis.shouldReportThis(n, parent)).thenReturn(false);
    
    checkGlobalThis.visit(t, n, parent);
    
    verify(checkGlobalThis.compiler, never()).report(any());
  }
  
  @Test
  public void testVisit_AssignLhsChild_ResetsAssignLhsChild() {
    NodeTraversal t = mock(NodeTraversal.class);
    Node n = mock(Node.class);
    Node parent = mock(Node.class);
    Node assignLhsChild = mock(Node.class);
    checkGlobalThis.assignLhsChild = assignLhsChild;
    when(n.getType()).thenReturn(Token.ADD);
    
    checkGlobalThis.visit(t, n, parent);
    
    assertNull(checkGlobalThis.assignLhsChild);
  }
  
  @Test
  public void testGetFunctionJsDocInfo_NullJsDocInfo_ReturnsParentJsDocInfo() {
    Node n = mock(Node.class);
    Node parent = mock(Node.class);
    JSDocInfo parentJsDoc = mock(JSDocInfo.class);
    when(n.getJSDocInfo()).thenReturn(null);
    when(n.getParent()).thenReturn(parent);
    when(parent.getType()).thenReturn(Token.ADD);
    when(parent.getJSDocInfo()).thenReturn(parentJsDoc);
    
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
    
    assertEquals(parentJsDoc, result);
  }
  
  @Test
  public void testGetFunctionJsDocInfo_AssignParent_ReturnsGrandparentJsDocInfo() {
    Node n = mock(Node.class);
    Node parent = mock(Node.class);
    Node grandparent = mock(Node.class);
    JSDocInfo grandparentJsDoc = mock(JSDocInfo.class);
    when(n.getJSDocInfo()).thenReturn(null);
    when(n.getParent()).thenReturn(parent);
    when(parent.getType()).thenReturn(Token.NAME);
    when(parent.getParent()).thenReturn(grandparent);
    when(grandparent.getType()).thenReturn(Token.VAR);
    when(grandparent.getJSDocInfo()).thenReturn(grandparentJsDoc);
    
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
    
    assertEquals(grandparentJsDoc, result);
  }
  
  @Test
  public void testGetFunctionJsDocInfo_FunctionHasJsDocInfo_ReturnsFunctionJsDocInfo() {
    Node n = mock(Node.class);
    JSDocInfo functionJsDoc = mock(JSDocInfo.class);
    when(n.getJSDocInfo()).thenReturn(functionJsDoc);
    when(n.getParent()).thenReturn(null);
    
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
    
    assertEquals(functionJsDoc, result);
  }
}
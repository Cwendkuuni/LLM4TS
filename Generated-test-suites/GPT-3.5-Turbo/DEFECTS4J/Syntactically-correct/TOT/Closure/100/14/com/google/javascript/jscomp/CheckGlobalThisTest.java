import org.junit.Test;
import static org.mockito.Mockito.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.jscomp.NodeTraversal;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverse_FunctionNotConstructor_NoThisAnnotation_ShouldReturnTrue() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, null);
    NodeTraversal t = mock(NodeTraversal.class);
    Node n = mock(Node.class);
    Node parent = mock(Node.class);
    
    when(n.getType()).thenReturn(Token.FUNCTION);
    when(checkGlobalThis.getFunctionJsDocInfo(n)).thenReturn(null);
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertTrue(result);
  }
  
  @Test
  public void testShouldTraverse_FunctionIsConstructor_ShouldReturnFalse() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, null);
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
  public void testShouldTraverse_FunctionHasThisAnnotation_ShouldReturnFalse() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, null);
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
  public void testShouldTraverse_AssignToLeftHandSideChildIsNull_ShouldReturnTrue() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, null);
    NodeTraversal t = mock(NodeTraversal.class);
    Node n = mock(Node.class);
    Node parent = mock(Node.class);
    Node lhs = mock(Node.class);
    Node rhs = mock(Node.class);
    
    when(n.getType()).thenReturn(Token.NAME);
    when(parent.getType()).thenReturn(Token.ASSIGN);
    when(parent.getFirstChild()).thenReturn(lhs);
    when(lhs.getNext()).thenReturn(rhs);
    when(lhs.getType()).thenReturn(Token.GETPROP);
    when(lhs.getLastChild()).thenReturn(mock(Node.class));
    when(lhs.getLastChild().getString()).thenReturn("prototype");
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertTrue(result);
  }
  
  @Test
  public void testShouldTraverse_AssignToLeftHandSideChildIsNotNull_ShouldReturnFalse() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, null);
    NodeTraversal t = mock(NodeTraversal.class);
    Node n = mock(Node.class);
    Node parent = mock(Node.class);
    Node lhs = mock(Node.class);
    Node rhs = mock(Node.class);
    
    checkGlobalThis.setAssignLhsChild(lhs);
    when(n.getType()).thenReturn(Token.NAME);
    when(parent.getType()).thenReturn(Token.ASSIGN);
    when(parent.getFirstChild()).thenReturn(lhs);
    when(lhs.getNext()).thenReturn(rhs);
    when(lhs.getType()).thenReturn(Token.GETPROP);
    when(lhs.getLastChild()).thenReturn(mock(Node.class));
    when(lhs.getLastChild().getString()).thenReturn("prototype");
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertFalse(result);
  }
  
  @Test
  public void testVisit_NodeIsThisAndShouldReportThis_ShouldReportError() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, null);
    NodeTraversal t = mock(NodeTraversal.class);
    Node n = mock(Node.class);
    Node parent = mock(Node.class);
    
    checkGlobalThis.setAssignLhsChild(null);
    when(n.getType()).thenReturn(Token.THIS);
    when(checkGlobalThis.shouldReportThis(n, parent)).thenReturn(true);
    
    checkGlobalThis.visit(t, n, parent);
    
    verify(compiler).report(any());
  }
  
  @Test
  public void testVisit_NodeIsNotThis_ShouldNotReportError() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, null);
    NodeTraversal t = mock(NodeTraversal.class);
    Node n = mock(Node.class);
    Node parent = mock(Node.class);
    
    checkGlobalThis.setAssignLhsChild(null);
    when(n.getType()).thenReturn(Token.NAME);
    when(checkGlobalThis.shouldReportThis(n, parent)).thenReturn(false);
    
    checkGlobalThis.visit(t, n, parent);
    
    verify(compiler, never()).report(any());
  }
  
  @Test
  public void testVisit_NodeIsSameAsAssignLhsChild_ShouldResetAssignLhsChild() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, null);
    NodeTraversal t = mock(NodeTraversal.class);
    Node n = mock(Node.class);
    Node parent = mock(Node.class);
    
    checkGlobalThis.setAssignLhsChild(n);
    checkGlobalThis.visit(t, n, parent);
    
    assertEquals(null, checkGlobalThis.getAssignLhsChild());
  }
  
}
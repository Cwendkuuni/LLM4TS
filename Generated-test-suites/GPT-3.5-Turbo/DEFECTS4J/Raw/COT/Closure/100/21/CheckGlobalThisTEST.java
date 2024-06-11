import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;
import com.google.javascript.jscomp.NodeTraversal.Callback;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class CheckGlobalThisTest {

  private CheckGlobalThis checkGlobalThis;
  private AbstractCompiler compiler;
  private CheckLevel level;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    compiler = mock(AbstractCompiler.class);
    level = mock(CheckLevel.class);
    checkGlobalThis = new CheckGlobalThis(compiler, level);
  }

  // Test visit() method

  @Test
  public void testVisit_ThisOnLeftSideOfAssign_Reported() {
    NodeTraversal t = mock(NodeTraversal.class);
    Node n = mock(Node.class);
    Node parent = mock(Node.class);

    when(n.getType()).thenReturn(Token.THIS);
    when(checkGlobalThis.shouldReportThis(n, parent)).thenReturn(true);
    
    checkGlobalThis.visit(t, n, parent);

    verify(compiler).report(any());
  }

  @Test
  public void testVisit_ThisNotOnLeftSideOfAssign_NotReported() {
    NodeTraversal t = mock(NodeTraversal.class);
    Node n = mock(Node.class);
    Node parent = mock(Node.class);

    when(n.getType()).thenReturn(Token.THIS);
    when(checkGlobalThis.shouldReportThis(n, parent)).thenReturn(false);
    
    checkGlobalThis.visit(t, n, parent);

    verify(compiler, never()).report(any());
  }

  @Test
  public void testVisit_AssignLhsChildNotNull_ResetAssignLhsChild() {
    NodeTraversal t = mock(NodeTraversal.class);
    Node n = mock(Node.class);
    Node parent = mock(Node.class);
    Node assignLhsChild = mock(Node.class);

    checkGlobalThis.setAssignLhsChild(assignLhsChild);
    
    checkGlobalThis.visit(t, n, parent);
    
    assertNull(checkGlobalThis.getAssignLhsChild());
  }

  // Test shouldTraverse() method

  @Test
  public void testShouldTraverse_FunctionConstructor_NotTraverse() {
    NodeTraversal t = mock(NodeTraversal.class);
    Node n = mock(Node.class);
    Node parent = mock(Node.class);
    Node jsDocNode = mock(Node.class);
    JSDocInfo jsDocInfo = mock(JSDocInfo.class);
    
    when(n.getType()).thenReturn(Token.FUNCTION);
    when(checkGlobalThis.getFunctionJsDocInfo(n)).thenReturn(jsDocInfo);
    when(jsDocInfo.isConstructor()).thenReturn(true);

    assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
  }

  @Test
  public void testShouldTraverse_FunctionHasThisType_NotTraverse() {
    NodeTraversal t = mock(NodeTraversal.class);
    Node n = mock(Node.class);
    Node parent = mock(Node.class);
    Node jsDocNode = mock(Node.class);
    JSDocInfo jsDocInfo = mock(JSDocInfo.class);
    
    when(n.getType()).thenReturn(Token.FUNCTION);
    when(checkGlobalThis.getFunctionJsDocInfo(n)).thenReturn(jsDocInfo);
    when(jsDocInfo.hasThisType()).thenReturn(true);

    assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
  }

  @Test
  public void testShouldTraverse_FunctionNotConstructorOrHasThisType_Traverse() {
    NodeTraversal t = mock(NodeTraversal.class);
    Node n = mock(Node.class);
    Node parent = mock(Node.class);
    Node jsDocNode = mock(Node.class);
    JSDocInfo jsDocInfo = mock(JSDocInfo.class);
    
    when(n.getType()).thenReturn(Token.FUNCTION);
    when(checkGlobalThis.getFunctionJsDocInfo(n)).thenReturn(jsDocInfo);
    when(jsDocInfo.isConstructor()).thenReturn(false);
    when(jsDocInfo.hasThisType()).thenReturn(false);

    assertTrue(checkGlobalThis.shouldTraverse(t, n, parent));
  }

  @Test
  public void testShouldTraverse_ParentAssign_LeftSideTraverse() {
    NodeTraversal t = mock(NodeTraversal.class);
    Node n = mock(Node.class);
    Node parent = mock(Node.class);
    Node lhs = mock(Node.class);
    Node rhs = mock(Node.class);
    
    when(parent.getType()).thenReturn(Token.ASSIGN);
    when(parent.getFirstChild()).thenReturn(lhs);
    when(lhs.getNext()).thenReturn(rhs);
    when(n == lhs).thenReturn(true);

    assertTrue(checkGlobalThis.shouldTraverse(t, n, parent));
  }

  @Test
  public void testShouldTraverse_ParentAssign_RightSideNotTraverse() {
    NodeTraversal t = mock(NodeTraversal.class);
    Node n = mock(Node.class);
    Node parent = mock(Node.class);
    Node lhs = mock(Node.class);
    Node rhs = mock(Node.class);
    
    when(parent.getType()).thenReturn(Token.ASSIGN);
    when(parent.getFirstChild()).thenReturn(lhs);
    when(lhs.getNext()).thenReturn(rhs);
    when(n == lhs).thenReturn(false);
    when(lhs.getType()).thenReturn(Token.GETPROP);

    assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
  }

  // Add more test cases for other methods in CheckGlobalThis class

}
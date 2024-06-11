import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {
  
  private CheckGlobalThis checkGlobalThis;
  
  @Before
  public void setUp() {
    checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
  }
  
  @Test
  public void testShouldTraverse_FunctionConstructor_ReturnFalse() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    n.putProp("JSDocInfo", new JSDocInfo());
    Node parent = new Node(Token.NAME);
    parent.putProp("JSDocInfo", new JSDocInfo().setConstructor(true));
    
    assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
  }
  
  @Test
  public void testShouldTraverse_FunctionThisType_ReturnFalse() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    n.putProp("JSDocInfo", new JSDocInfo().setThisType(new JSTypeExpression()));
    Node parent = new Node(Token.NAME);
    
    assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
  }
  
  @Test
  public void testShouldTraverse_FunctionInterface_ReturnFalse() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    n.putProp("JSDocInfo", new JSDocInfo().setInterface(true));
    Node parent = new Node(Token.NAME);
    
    assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
  }
  
  @Test
  public void testShouldTraverse_FunctionOverride_ReturnFalse() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    n.putProp("JSDocInfo", new JSDocInfo().setOverride(true));
    Node parent = new Node(Token.NAME);
    
    assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
  }
  
  @Test
  public void testShouldTraverse_FunctionBlock_ReturnTrue() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    
    assertTrue(checkGlobalThis.shouldTraverse(t, n, parent));
  }
  
  @Test
  public void testShouldTraverse_FunctionScript_ReturnTrue() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    
    assertTrue(checkGlobalThis.shouldTraverse(t, n, parent));
  }
  
  @Test
  public void testShouldTraverse_FunctionName_ReturnTrue() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    
    assertTrue(checkGlobalThis.shouldTraverse(t, n, parent));
  }
  
  @Test
  public void testShouldTraverse_FunctionAssign_ReturnTrue() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.ASSIGN);
    
    assertTrue(checkGlobalThis.shouldTraverse(t, n, parent));
  }
  
  @Test
  public void testShouldTraverse_StringObjectLiteralKey_ReturnFalse() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.STRING);
    Node parent = new Node(Token.OBJECTLIT);
    
    assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
  }
  
  @Test
  public void testShouldTraverse_StringPrototype_ReturnFalse() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.STRING);
    Node parent = new Node(Token.GETPROP);
    parent.setString("prototype");
    
    assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
  }
  
  @Test
  public void testShouldTraverse_NumberPrototype_ReturnFalse() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.NUMBER);
    Node parent = new Node(Token.GETPROP);
    parent.setString("prototype");
    
    assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
  }
  
  @Test
  public void testShouldTraverse_AssignLeftSide_ReturnTrue() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.EXPR_RESULT);
    Node parent = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.NAME);
    parent.addChildToFront(lhs);
    parent.addChildToBack(new Node(Token.STRING));
    n.addChildToBack(parent);
    
    assertTrue(checkGlobalThis.shouldTraverse(t, lhs, parent));
  }
  
  @Test
  public void testShouldTraverse_AssignRightSide_ReturnFalse() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.EXPR_RESULT);
    Node parent = new Node(Token.ASSIGN);
    parent.addChildToFront(new Node(Token.NAME));
    Node rhs = new Node(Token.THIS);
    parent.addChildToBack(rhs);
    n.addChildToBack(parent);
    
    assertFalse(checkGlobalThis.shouldTraverse(t, rhs, parent));
  }
  
  @Test
  public void testVisit_ThisAndShouldReportThis_ReturnTrue() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    
    assertTrue(checkGlobalThis.shouldReportThis(n, parent));
  }
  
  @Test
  public void testVisit_NotThis_ReturnFalse() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.GETPROP);
    
    assertFalse(checkGlobalThis.shouldReportThis(n, parent));
  }
  
  @Test
  public void testVisit_ThisAndNotShouldReportThis_ReturnFalse() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.NAME);
    
    assertFalse(checkGlobalThis.shouldReportThis(n, parent));
  }
  
  @Test
  public void testVisit_LhsChildIsNull() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.VALIANT);
    checkGlobalThis.assignLhsChild = null;
    
    assertFalse(checkGlobalThis.shouldReportThis(n, parent));
  }
  
  @Test
  public void testVisit_LhsChildNotNullAndSameAsN_ReturnTrue() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.VALIANT);
    checkGlobalThis.assignLhsChild = n;
    
    assertTrue(checkGlobalThis.shouldReportThis(n, parent));
    assertNull(checkGlobalThis.assignLhsChild);
  }
  
  @Test
  public void testVisit_LhsChildNotNullButDifferentFromN_ReturnFalse() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.VALIANT);
    checkGlobalThis.assignLhsChild = new Node(Token.THIS);
    
    assertFalse(checkGlobalThis.shouldReportThis(n, parent));
    assertNotNull(checkGlobalThis.assignLhsChild);
  }
  
  @Test
  public void testVisit_ReportWarning() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    checkGlobalThis.compiler = mock(AbstractCompiler.class);
    
    checkGlobalThis.visit(t, n, parent);
    verify(checkGlobalThis.compiler).report(any());
  }
  
  @Test
  public void testVisit_NotReportWarning() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.GETPROP);
    checkGlobalThis.compiler = mock(AbstractCompiler.class);
    
    checkGlobalThis.visit(t, n, parent);
    verify(checkGlobalThis.compiler, never()).report(any());
  }
  
  @Test
  public void testGetFunctionJsDocInfo_NullJsDoc_ReturnNull() {
    Node n = new Node(Token.FUNCTION);
    n.putProp("JSDocInfo", null);
    Node parent = new Node(Token.NAME);
    
    assertNull(checkGlobalThis.getFunctionJsDocInfo(n));
  }
  
  @Test
  public void testGetFunctionJsDocInfo_JsDocInParentAssign_ReturnJsDoc() {
    Node n = new Node(Token.FUNCTION);
    n.putProp("JSDocInfo", null);
    Node parent = new Node(Token.ASSIGN);
    parent.putProp("JSDocInfo", new JSDocInfo());
    
    assertNotNull(checkGlobalThis.getFunctionJsDocInfo(n));
  }
  
  @Test
  public void testGetFunctionJsDocInfo_JsDocInParentName_ReturnJsDoc() {
    Node n = new Node(Token.FUNCTION);
    n.putProp("JSDocInfo", null);
    Node parent = new Node(Token.NAME);
    parent.putProp("JSDocInfo", new JSDocInfo());
    
    assertNotNull(checkGlobalThis.getFunctionJsDocInfo(n));
  }
  
  @Test
  public void testGetFunctionJsDocInfo_JsDocInGrandparentVar_ReturnJsDoc() {
    Node n = new Node(Token.FUNCTION);
    n.putProp("JSDocInfo", null);
    Node parent = new Node(Token.NAME);
    Node gramps = new Node(Token.VAR);
    gramps.putProp("JSDocInfo", new JSDocInfo());
    parent.addChildToFront(gramps);
    
    assertNotNull(checkGlobalThis.getFunctionJsDocInfo(n));
  }
  
  @Test
  public void testGetFunctionJsDocInfo_JsDocInParentVar_ReturnNull() {
    Node n = new Node(Token.FUNCTION);
    n.putProp("JSDocInfo", null);
    Node parent = new Node(Token.NAME);
    Node gramps = new Node(Token.VAR);
    gramps.putProp("JSDocInfo", null);
    parent.addChildToFront(gramps);
    
    assertNull(checkGlobalThis.getFunctionJsDocInfo(n));
  }
  
  @Test
  public void testGetFunctionJsDocInfo_JsDocInN_ReturnJsDoc() {
    Node n = new Node(Token.FUNCTION);
    n.putProp("JSDocInfo", new JSDocInfo());
    Node parent = new Node(Token.NAME);
    
    assertNotNull(checkGlobalThis.getFunctionJsDocInfo(n));
  }
  
}
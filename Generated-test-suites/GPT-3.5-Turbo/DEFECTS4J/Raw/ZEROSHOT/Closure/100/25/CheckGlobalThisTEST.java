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
  public void testShouldTraverse_FunctionConstructor_DontTraverse() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setConstructor(true);
    n.setJSDocInfo(jsDoc);
    Node parent = new Node();
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_FunctionWithThisAnnotation_DontTraverse() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordThisType();
    n.setJSDocInfo(jsDoc);
    Node parent = new Node();
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_Function_NoThisAnnotation_Traverse() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    n.setJSDocInfo(jsDoc);
    Node parent = new Node();
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertTrue(result);
  }
  
  @Test
  public void testShouldTraverse_Assign_LeftSide_Traverse() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.ASSIGN);
    parent.addChildToFront(n);
    parent.addChildToFront(new Node());
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertTrue(result);
  }
  
  @Test
  public void testShouldTraverse_Assign_RightSide_PrototypeProperty_DontTraverse() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.NAME, new Node(Token.GETPROP, new Node(Token.THIS), new Node(Token.STRING, "prototype")));
    Node parent = new Node(Token.ASSIGN);
    parent.addChildToFront(new Node());
    parent.addChildToFront(n);
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_Assign_RightSide_PrototypeSubproperty_DontTraverse() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.NAME, new Node(Token.GETPROP, new Node(Token.NAME, new Node(Token.GETPROP, new Node(Token.THIS), new Node(Token.STRING, "prototype"))), new Node(Token.STRING, "subproperty")));
    Node parent = new Node(Token.ASSIGN);
    parent.addChildToFront(new Node());
    parent.addChildToFront(n);
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_Assign_RightSide_Traverse() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.ASSIGN);
    parent.addChildToFront(new Node());
    parent.addChildToFront(n);
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertTrue(result);
  }
  
  @Test
  public void testVisit_This_ShouldReport() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node();
    CheckLevel level = CheckLevel.WARNING;
    
    checkGlobalThis.visit(t, n, parent);
    
    // Assert error reported
  }
  
  @Test
  public void testVisit_NotAssignLhsChild_DontResetAssignLhsChild() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.NAME);
    Node parent = new Node();
    checkGlobalThis.setAssignLhsChild(new Node());
    
    checkGlobalThis.visit(t, n, parent);
    
    assertNotNull(checkGlobalThis.getAssignLhsChild());
  }
  
  @Test
  public void testVisit_NullAssignLhsChild_ResetAssignLhsChild() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.NAME);
    Node parent = new Node();
    checkGlobalThis.setAssignLhsChild(null);
    
    checkGlobalThis.visit(t, n, parent);
    
    assertNull(checkGlobalThis.getAssignLhsChild());
  }
  
  @Test
  public void testShouldReportThis_AssignLhsChild_ShouldReport() {
    Node n = new Node(Token.NAME);
    Node parent = new Node();
    checkGlobalThis.setAssignLhsChild(new Node());
    
    boolean result = checkGlobalThis.shouldReportThis(n, parent);
    
    assertTrue(result);
  }
  
  @Test
  public void testShouldReportThis_NotAssignLhsChild_DontReport() {
    Node n = new Node(Token.NAME);
    Node parent = new Node();
    checkGlobalThis.setAssignLhsChild(null);
    
    boolean result = checkGlobalThis.shouldReportThis(n, parent);
    
    assertFalse(result);
  }
  
  @Test
  public void testGetFunctionJsDocInfo_FunctionWithJsDoc_ReturnJsDoc() {
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    n.setJSDocInfo(jsDoc);
    Node parent = new Node();
    
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
    
    assertEquals(jsDoc, result);
  }
  
  @Test
  public void testGetFunctionJsDocInfo_VariableWithJsDoc_ReturnJsDoc() {
    Node n = new Node(Token.NAME);
    JSDocInfo jsDoc = new JSDocInfo();
    n.setJSDocInfo(jsDoc);
    Node parent = new Node(Token.NAME, n);
    
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
    
    assertEquals(jsDoc, result);
  }
  
  @Test
  public void testGetFunctionJsDocInfo_VariableWithNoJsDocAndVarWithJsDoc_ReturnJsDocOfVar() {
    Node n = new Node(Token.NAME);
    JSDocInfo jsDoc = new JSDocInfo();
    Node varNode = new Node(Token.VAR);
    varNode.setJSDocInfo(jsDoc);
    Node parent = new Node(Token.NAME, varNode);
    
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
    
    assertEquals(jsDoc, result);
  }
  
  @Test
  public void testGetFunctionJsDocInfo_VariableWithNoJsDocAndVarWithNoJsDoc_ReturnNull() {
    Node n = new Node(Token.NAME);
    Node varNode = new Node(Token.VAR);
    Node parent = new Node(Token.NAME, varNode);
    
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
    
    assertNull(result);
  }
  
  private class NodeTraversal {
    
    public NodeTraversal() {
      
    }
    
    public void report() {
      
    }
    
    public CheckLevel makeError(Node n, CheckLevel level, DiagnosticType diagnosticType) {
      return level;
    }
  }
}
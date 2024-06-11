import static org.junit.Assert.*;
import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverse_ReturnsTrue_WhenNodeIsFunction() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = null;
    Node n = new Node(Token.FUNCTION);
    Node parent = null;
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertTrue(result);
  }
  
  @Test
  public void testShouldTraverse_ReturnsFalse_WhenNodeIsFunctionAndConstructor() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = null;
    Node n = new Node(Token.FUNCTION);
    Node parent = null;
    JSDocInfo jsDoc = createConstructorJSDoc();
    n.setJSDocInfo(jsDoc);
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_ReturnsFalse_WhenNodeIsFunctionAndInterface() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = null;
    Node n = new Node(Token.FUNCTION);
    Node parent = null;
    JSDocInfo jsDoc = createInterfaceJSDoc();
    n.setJSDocInfo(jsDoc);
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_ReturnsFalse_WhenNodeIsFunctionAndHasThisTypeAnnotation() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = null;
    Node n = new Node(Token.FUNCTION);
    Node parent = null;
    JSDocInfo jsDoc = createThisTypeJSDoc();
    n.setJSDocInfo(jsDoc);
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_ReturnsFalse_WhenNodeIsFunctionAndHasOverrideAnnotation() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = null;
    Node n = new Node(Token.FUNCTION);
    Node parent = null;
    JSDocInfo jsDoc = createOverrideJSDoc();
    n.setJSDocInfo(jsDoc);
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_ReturnsFalse_WhenParentIsNotBlockScriptNameOrAssign() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = null;
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.ADD);
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_ReturnsTrue_WhenParentIsBlock() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = null;
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertTrue(result);
  }
  
  @Test
  public void testShouldTraverse_ReturnsTrue_WhenParentIsScript() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = null;
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertTrue(result);
  }
  
  @Test
  public void testShouldTraverse_ReturnsTrue_WhenParentIsName() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = null;
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertTrue(result);
  }
  
  @Test
  public void testShouldTraverse_ReturnsTrue_WhenParentIsAssign() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = null;
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.ASSIGN);
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertTrue(result);
  }
  
  @Test
  public void testShouldTraverse_ReturnsTrue_WhenParentIsAssignAndNotNestedAssign() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = null;
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertTrue(result);
  }
  
  @Test
  public void testShouldTraverse_ReturnsTrue_WhenParentIsAssignAndLeftHandSideIsNestedAssign() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = null;
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.GETPROP);
    lhs.addChildToBack(new Node(Token.THIS));
    parent.addChildToFront(lhs);
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertTrue(result);
  }
  
  @Test
  public void testShouldTraverse_ReturnsTrue_WhenParentIsAssignAndLeftHandSideIsPrototypeProperty() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = null;
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.GETPROP);
    lhs.addChildToBack(new Node(Token.NAME, "prototype"));
    parent.addChildToFront(lhs);
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertTrue(result);
  }
  
  @Test
  public void testShouldTraverse_ReturnsFalse_WhenParentIsGetAndLeftHandSideIsPrototypeProperty() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = null;
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    Node lhs = new Node(Token.NAME, "prototype");
    parent.addChildToFront(lhs);
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertFalse(result);
  }
  
  @Test
  public void testShouldReportThis_ReturnsTrue_WhenAssignLhsChildIsNotNull() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    Node n = new Node(Token.THIS);
    Node parent = null;
    checkGlobalThis.visit(null, n, parent);
    n = new Node(Token.NAME, "a");
    parent = new Node(Token.ASSIGN);
    checkGlobalThis.visit(null, n, parent);
    
    boolean result = checkGlobalThis.shouldReportThis(n, parent);
    
    assertTrue(result);
  }
  
  @Test
  public void testShouldReportThis_ReturnsFalse_WhenAssignLhsChildIsNullAndParentIsNotGet() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    Node n = new Node(Token.THIS);
    Node parent = null;
    checkGlobalThis.visit(null, n, parent);
    
    boolean result = checkGlobalThis.shouldReportThis(n, parent);
    
    assertFalse(result);
  }
  
  @Test
  public void testShouldReportThis_ReturnsFalse_WhenAssignLhsChildIsNullAndParentIsGet() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    
    boolean result = checkGlobalThis.shouldReportThis(n, parent);
    
    assertFalse(result);
  }
  
  @Test
  public void testVisit_ReportsWarning_WhenNodeIsGlobalThis() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal(null, null, null);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ADD);
    t.reportError = (node, diagnosticType, reason, param) ->
        assertEquals("dangerous use of the global 'this' object", reason);
    
    checkGlobalThis.visit(t, n, parent);
  }
  
  @Test
  public void testVisit_DoesNotReportWarning_WhenNodeIsNotGlobalThis() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal(null, null, null);
    Node n = new Node(Token.NAME, "a");
    Node parent = new Node(Token.ADD);
    t.reportError = (node, diagnosticType, reason, param) ->
        fail("Unexpected error reported");
    
    checkGlobalThis.visit(t, n, parent);
  }

  private JSDocInfo createConstructorJSDoc() {
    return new JSDocInfo().setConstructor(true);
  }
  
  private JSDocInfo createInterfaceJSDoc() {
    return new JSDocInfo().setInterface(true);
  }
  
  private JSDocInfo createThisTypeJSDoc() {
    return new JSDocInfo().setThisType(new JSTypeExpression(null, null));
  }
  
  private JSDocInfo createOverrideJSDoc() {
    return new JSDocInfo().setOverride(true);
  }
}
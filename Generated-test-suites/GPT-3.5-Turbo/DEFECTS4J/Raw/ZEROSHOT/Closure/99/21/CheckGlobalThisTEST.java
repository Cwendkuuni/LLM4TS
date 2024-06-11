import static org.junit.Assert.*;
import org.junit.Test;

public class CheckGlobalThisTest {
  
  @Test
  public void testShouldTraverse_withFunction_annotation() {
    NodeTraversal t = null;
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_withFunction_constructor() {
    NodeTraversal t = null;
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    
    JSDocInfo jsDoc = new JSDocInfoBuilder().annotateConstructor().build();
    n.setJSDocInfo(jsDoc);
    
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_withFunction_thisType() {
    NodeTraversal t = null;
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    
    JSDocInfo jsDoc = new JSDocInfoBuilder().recordThisType().build();
    n.setJSDocInfo(jsDoc);
    
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_withFunction_override() {
    NodeTraversal t = null;
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    
    JSDocInfo jsDoc = new JSDocInfoBuilder().annotateOverride().build();
    n.setJSDocInfo(jsDoc);
    
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_withFunction_uppercaseBlock() {
    NodeTraversal t = null;
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertTrue(result);
  }
  
  @Test
  public void testShouldTraverse_withFunction_uppercaseScript() {
    NodeTraversal t = null;
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertTrue(result);
  }
  
  @Test
  public void testShouldTraverse_withFunction_uppercaseName() {
    NodeTraversal t = null;
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertTrue(result);
  }
  
  @Test
  public void testShouldTraverse_withFunction_uppercaseAssign() {
    NodeTraversal t = null;
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.ASSIGN);
    
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertTrue(result);
  }
  
  @Test
  public void testShouldTraverse_withParent_assignLhsChildNotNull() {
    NodeTraversal t = null;
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.NAME);
    
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    checkGlobalThis.assignLhsChild = new Node(Token.THIS);
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertTrue(result);
  }
  
  @Test
  public void testShouldTraverse_withParent_notAssignmentToPrototype_property() {
    NodeTraversal t = null;
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    
    Node lhs = new Node(Token.STRING);
    lhs.setString("prototype");
    
    parent.addChildToBack(lhs);
    
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_withParent_notAssignmentToPrototype_subproperty() {
    NodeTraversal t = null;
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    
    Node lhs = new Node(Token.STRING);
    lhs.setString("prototype");
    
    Node subProperty = new Node(Token.STRING);
    subProperty.setString("subproperty");
    
    parent.addChildToBack(lhs);
    parent.addChildToBack(subProperty);
    
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_withParent_notAssignmentToPrototype_subpropertyQualifiedName() {
    NodeTraversal t = null;
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    
    Node lhs = new Node(Token.STRING);
    lhs.setString("prototype");
    
    Node subProperty = new Node(Token.STRING);
    subProperty.setString("subproperty");
    
    parent.addChildToBack(lhs);
    parent.addChildToBack(subProperty);
    
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    lhs.setQualifiedName("prototype.subproperty");
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertFalse(result);
  }
  
  @Test
  public void testVisit_withThis_shouldReportThis() {
    NodeTraversal t = null;
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
    checkGlobalThis.assignLhsChild = null;
    
    checkGlobalThis.visit(t, n, parent);
    
    // assert the error report logic
  }
  
  @Test
  public void testVisit_withAssignLhsChild_shouldResetAssignLhsChild() {
    NodeTraversal t = null;
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
    checkGlobalThis.assignLhsChild = n;
    
    checkGlobalThis.visit(t, n, parent);
    
    assertNull(checkGlobalThis.assignLhsChild);
  }
  
}
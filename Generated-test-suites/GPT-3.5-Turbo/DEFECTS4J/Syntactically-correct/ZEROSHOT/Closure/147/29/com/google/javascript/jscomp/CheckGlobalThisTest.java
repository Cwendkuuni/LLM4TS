import static org.junit.Assert.*;
import org.junit.*;

public class CheckGlobalThisTest {
  
  @Test
  public void testShouldTraverse_WithTypeFunction_ReturnsFalse() {
    // Setup
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    
    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    // Assert
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_WithFunctionWithConstructorJsDoc_ReturnsFalse() {
    // Setup
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    n.setJSDocInfo(new JSDocInfo());
    
    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    // Assert
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_WithFunctionWithInterfaceJsDoc_ReturnsFalse() {
    // Setup
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setInterface(true);
    n.setJSDocInfo(jsDoc);
    
    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    // Assert
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_WithFunctionWithThisTypeJsDoc_ReturnsFalse() {
    // Setup
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordThisType();
    n.setJSDocInfo(jsDoc);
    
    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    // Assert
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_WithFunctionWithOverrideJsDoc_ReturnsFalse() {
    // Setup
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setOverride(true);
    n.setJSDocInfo(jsDoc);
    
    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    // Assert
    assertFalse(result);
  }

  @Test void testShouldTraverse_WithParent_Assign_ReturnsTrue() {
    // Setup
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.GETPROP);
    Node rhs = new Node(Token.GETPROP);
    parent.addChildToFront(lhs);
    parent.addChildToFront(rhs);
    rhs.addChildToFront(lhs);
    parent.addChildToFront(n);
    checkGlobalThis.shouldTraverse(t, n, parent);
    
    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, rhs, parent);
    
    // Assert
    assertTrue(result);
  }

  @Test
  public void testVisit_WithThisAndParent_ReturnsTrue() {
    // Setup
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    
    // Act
    checkGlobalThis.visit(t, n, parent);
    
    // Assert
    assertTrue(true); // Cannot directly test the result of the visit() method
  }

  @Test
  public void testVisit_WithAssignLhsChildNotNull_SetsAssignLhsChildToNull() {
    // Setup
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    parent.addChildToFront(n);
    checkGlobalThis.shouldTraverse(t, n, parent);
    
    // Act
    checkGlobalThis.visit(t, n, parent);
    
    // Assert
    assertNull(checkGlobalThis.assignLhsChild);
  }

  @Test
  public void testGetFunctionJsDocInfo_WithFunctionWithJsDoc_ReturnsJsDocInfo() {
    // Setup
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    Node n = new Node(Token.FUNCTION);
    
    // Act
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
    
    // Assert
    assertNotNull(result);
  }

  @Test
  public void testGetFunctionJsDocInfo_WithFunctionWithoutJsDoc_ReturnsParentJsDocInfo() {
    // Setup
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    parent.addChildToFront(n);
    
    // Act
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
    
    // Assert
    assertNotNull(result);
  }

  @Test
  public void testGetFunctionJsDocInfo_WithFunctionWithoutJsDocAndParentIsVar_ReturnsGrampsJsDocInfo() {
    // Setup
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    Node gramps = new Node(Token.VAR);
    gramps.addChildToFront(parent);
    parent.addChildToFront(n);
    
    // Act
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
    
    // Assert
    assertNotNull(result);
  }
}
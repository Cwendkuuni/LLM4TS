import org.junit.Test;
import static org.junit.Assert.*;

public class CheckGlobalThisTest {
  @Test
  public void testShouldTraverse() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    // Test shouldTraverse for a function that is a constructor and has @this annotation
    assertFalse(checkGlobalThis.shouldTraverse(null, new Node(Token.FUNCTION), new Node(Token.FUNCTION)));
    
    // Test shouldTraverse for a function that is not a constructor and does not have @this annotation
    assertFalse(checkGlobalThis.shouldTraverse(null, new Node(Token.FUNCTION), new Node(Token.SCRIPT)));
    
    // Test shouldTraverse for a function that is not a constructor but has @this annotation
    assertFalse(checkGlobalThis.shouldTraverse(null, new Node(Token.FUNCTION), new Node(Token.SCRIPT, new Node(Token.BLOCK))));
    
    // Test shouldTraverse for a function with unknown parent type
    assertFalse(checkGlobalThis.shouldTraverse(null, new Node(Token.FUNCTION), new Node(123)));
    
    // Test shouldTraverse for a function with parent type ASSIGN and left side is not a GETPROP
    assertTrue(checkGlobalThis.shouldTraverse(null, new Node(Token.FUNCTION), new Node(Token.ASSIGN, new Node(Token.NAME))));
    
    // Test shouldTraverse for a function with parent type ASSIGN and left side is a GETPROP with the last child being "prototype"
    assertFalse(checkGlobalThis.shouldTraverse(null, new Node(Token.FUNCTION), new Node(Token.ASSIGN, new Node(Token.GETPROP, new Node(Token.NAME), new Node(Token.STRING,"prototype")))));
    
    // Test shouldTraverse for a function with parent type ASSIGN and left side is a GETPROP with the qualified name containing ".prototype."
    assertFalse(checkGlobalThis.shouldTraverse(null, new Node(Token.FUNCTION), new Node(Token.ASSIGN, new Node(Token.GETPROP, new Node(Token.NAME, "prototype")))));
  }

  @Test
  public void testVisit() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    // Test visit for a node that is not a THIS token
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = new Node(Token.ADD);
    Node parent = new Node(Token.SCRIPT);
    checkGlobalThis.visit(t, n, parent);
    
    // Test visit for a THIS token that should not be reported
    n = new Node(Token.THIS);
    parent = new Node(Token.ADD, n);
    checkGlobalThis.visit(t, n, parent);
    
    // Test visit for a THIS token that should be reported
    n = new Node(Token.THIS);
    parent = new Node(Token.GETPROP, n);
    checkGlobalThis.visit(t, n, parent);
  }
  
  @Test
  public void testGetFunctionJsDocInfo() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    // Test getFunctionJsDocInfo for a node with JSDocInfo
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    n.setJSDocInfo(jsDoc);
    assertNull(checkGlobalThis.getFunctionJsDocInfo(n));
    
    // Test getFunctionJsDocInfo for a node with JSDocInfo in parent NAME node
    Node parent = new Node(Token.NAME);
    parent.setJSDocInfo(jsDoc);
    n.setParent(parent);
    assertEquals(jsDoc, checkGlobalThis.getFunctionJsDocInfo(n));
    
    // Test getFunctionJsDocInfo for a node with no JSDocInfo, parent ASSIGN node and JSDocInfo in ASSIGN parent VAR node
    Node grandParent = new Node(Token.VAR);
    grandParent.setJSDocInfo(jsDoc);
    parent = new Node(Token.ASSIGN);
    parent.setParent(grandParent);
    n.setParent(parent);
    assertNull(checkGlobalThis.getFunctionJsDocInfo(n));
    
    // Test getFunctionJsDocInfo for a node with no JSDocInfo, parent ASSIGN node and no JSDocInfo in ASSIGN parent VAR node
    grandParent.setJSDocInfo(null);
    parent.setJSDocInfo(null);
    assertNull(checkGlobalThis.getFunctionJsDocInfo(n));
  }
}
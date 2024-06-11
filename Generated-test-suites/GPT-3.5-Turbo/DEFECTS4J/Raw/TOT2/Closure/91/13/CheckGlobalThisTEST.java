import static org.junit.Assert.*;
import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testConstructor() {
    // create a CheckGlobalThis object
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    assertNotNull(checkGlobalThis);
  }

  @Test
  public void testShouldTraverse() {
    // test the shouldTraverse method
    // edge case: n.getType() is Token.FUNCTION
    // create a dummy NodeTraversal, Node, and parent
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    assertTrue(checkGlobalThis.shouldTraverse(t, n, parent));
  }

  @Test
  public void testVisit() {
    // test the visit method
    // edge case: n.getType() is Token.THIS
    // create a dummy NodeTraversal, Node, and parent
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    checkGlobalThis.visit(t, n, parent);
    // assert that an error was reported
    // assertEquals(expectedError, actualError);
  }
  
  @Test
  public void testGetFunctionJsDocInfo() {
    // test the getFunctionJsDocInfo method
    // edge case: node has JSDocInfo
    Node node = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfoBuilder().build();
    node.setJSDocInfo(jsDoc);
    assertEquals(jsDoc, checkGlobalThis.getFunctionJsDocInfo(node));
    
    // edge case: parent has JSDocInfo
    Node parent = new Node(Token.NAME);
    parent.setJSDocInfo(jsDoc);
    node.getParent().replaceChild(node, parent);
    assertEquals(jsDoc, checkGlobalThis.getFunctionJsDocInfo(node));
    
    // edge case: grandparent has JSDocInfo
    Node grandparent = new Node(Token.VAR);
    grandparent.setJSDocInfo(jsDoc);
    parent.getParent().replaceChild(parent, grandparent);
    assertEquals(jsDoc, checkGlobalThis.getFunctionJsDocInfo(node));
  }
}
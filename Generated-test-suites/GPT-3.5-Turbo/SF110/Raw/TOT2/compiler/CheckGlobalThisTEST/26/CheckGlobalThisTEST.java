import static org.junit.Assert.*;
import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testShouldReportThis() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    
    // Test for THIS on the left side of an assignment
    assertTrue(checkGlobalThis.shouldReportThis(new Node(Token.THIS)));
    
    // Test for THIS with a property access
    Node parent = new Node(Token.GETPROP);
    parent.addChildToBack(new Node(Token.THIS));
    assertTrue(checkGlobalThis.shouldReportThis(new Node(Token.GETPROP)));
  }
  
  @Test
  public void testGetFunctionJsDocInfo() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    Node functionNode = new Node(Token.FUNCTION);
    
    // Test for function with JSDoc information
    functionNode.setJSDocInfo(new JSDocInfo(false));
    Node parent = new Node(Token.NAME);
    parent.addChildToBack(functionNode);
    assertEquals(functionNode.getJSDocInfo(), checkGlobalThis.getFunctionJsDocInfo(functionNode));
    
    // Test for function without JSDoc information but with parent information
    parent.setType(Token.ASSIGN);
    parent.setJSDocInfo(new JSDocInfo(false));
    assertEquals(parent.getJSDocInfo(), checkGlobalThis.getFunctionJsDocInfo(functionNode));
    
    // Test for function without JSDoc information or parent information
    parent.setType(Token.NAME);
    Node grandparent = new Node(Token.VAR);
    grandparent.addChildToBack(parent);
    grandparent.setJSDocInfo(new JSDocInfo(false));
    assertEquals(grandparent.getJSDocInfo(), checkGlobalThis.getFunctionJsDocInfo(functionNode));
  }
}
import static org.junit.Assert.*;
import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testShouldReportThis() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis();

    // Typical use case
    assertTrue(checkGlobalThis.shouldReportThis(new Node(Token.THIS)));

    // Edge case
    Node parent = new Node(Token.GETPROP);
    parent.addChildToBack(new Node(Token.NAME, "obj"));
    parent.addChildToBack(new Node(Token.STRING, "prop"));
    assertTrue(checkGlobalThis.shouldReportThis(parent));

    // Error scenario
    assertFalse(checkGlobalThis.shouldReportThis(new Node(Token.NULL)));
  }

  @Test
  public void testGetFunctionJsDocInfo() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis();

    // Typical use case
    Node functionNode = new Node(Token.FUNCTION);
    functionNode.setJSDocInfo(new JSDocInfoBuilder().build());
    assertEquals(functionNode.getJSDocInfo(), checkGlobalThis.getFunctionJsDocInfo(functionNode));

    // Edge case
    Node assignNode = new Node(Token.ASSIGN);
    assignNode.addChildToBack(new Node(Token.NAME, "func"));
    assignNode.getFirstChild().setJSDocInfo(new JSDocInfoBuilder().build());
    assertEquals(assignNode.getJSDocInfo(), checkGlobalThis.getFunctionJsDocInfo(assignNode));

    // Error scenario
    Node varNode = new Node(Token.VAR);
    assertNull(checkGlobalThis.getFunctionJsDocInfo(varNode));
  }
}
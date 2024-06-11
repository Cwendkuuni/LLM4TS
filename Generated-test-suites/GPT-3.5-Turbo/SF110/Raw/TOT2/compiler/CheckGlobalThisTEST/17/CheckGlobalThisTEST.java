import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testShouldReportThis() {
    // Test when assignLhsChild is null
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    assertEquals(false, checkGlobalThis.shouldReportThis(new Node()));

    // Test when assignLhsChild is not null
    checkGlobalThis.assignLhsChild = new Node();
    assertEquals(true, checkGlobalThis.shouldReportThis(new Node()));
  }

  @Test
  public void testGetFunctionJsDocInfo() {
    // Test when JSDocInfo is null for function node
    Node functionNode = new Node(Token.FUNCTION);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    assertEquals(null, checkGlobalThis.getFunctionJsDocInfo(functionNode));

    // Test when JSDocInfo is not null for function node
    JSDocInfo jsDocInfo = new JSDocInfoBuilder().build();
    functionNode.setJSDocInfo(jsDocInfo);
    assertEquals(jsDocInfo, checkGlobalThis.getFunctionJsDocInfo(functionNode));
  }
}
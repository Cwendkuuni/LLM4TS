import static org.junit.Assert.*;
import org.junit.Test;

public class CheckGlobalThisTest {
  
  @Test
  public void testShouldTraverse() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal traversal = new NodeTraversal(compiler, checkGlobalThis);
    Node n = new Node(Token.SCRIPT);
    Node parent = new Node(Token.SCRIPT);
    assertFalse(checkGlobalThis.shouldTraverse(traversal, n, parent));
  }
  
  @Test
  public void testVisit() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal traversal = new NodeTraversal(compiler, checkGlobalThis);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    checkGlobalThis.visit(traversal, n, parent);
    // Assert that an error is reported
  }
  
  @Test
  public void testGetFunctionJsDocInfo() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = checkGlobalThis.getFunctionJsDocInfo(n);
    // Assert that jsDoc is null
  }
  
  @Test
  public void testShouldReportThis() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    assertTrue(checkGlobalThis.shouldReportThis(n, parent));
  }
  
  @Test
  public void testShouldReportThis_assignLhsChildNotNull() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
    checkGlobalThis.assignLhsChild = new Node(Token.THIS);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    assertTrue(checkGlobalThis.shouldReportThis(n, parent));
  }
  
}
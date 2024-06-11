import static org.junit.Assert.*;
import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testShouldReportThis() {
    CheckGlobalThis checker = new CheckGlobalThis();
    Node node = new Node(Token.THIS);
    assertTrue(checker.shouldReportThis(node));
  }

  @Test
  public void testGetFunctionJsDocInfo() {
    CheckGlobalThis checker = new CheckGlobalThis();
    Node node = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    node.setJSDocInfo(jsDoc);
    assertEquals(jsDoc, checker.getFunctionJsDocInfo(node));
  }

  @Test
  public void testTraverse() {
    CheckGlobalThis checker = new CheckGlobalThis();
    AbstractCompiler compiler = new AbstractCompiler();
    checker.compiler = compiler;
    Node node = new Node(Token.THIS);
    NodeTraversal t = new NodeTraversal(compiler, checker);
    checker.visit(t, node, null);
    // No error should be reported in this case
  }

  @Test
  public void testShouldTraverse() {
    CheckGlobalThis checker = new CheckGlobalThis();
    AbstractCompiler compiler = new AbstractCompiler();
    NodeTraversal t = new NodeTraversal(compiler, checker);
    Node node = new Node(Token.THIS);
    Node parent = new Node(Token.FUNCTION);
    assertFalse(checker.shouldTraverse(t, node, parent));
  }

}
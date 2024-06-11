import static org.junit.Assert.*;
import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverse() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);

    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node n = createNode();
    Node parent = createNode();

    assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
  }

  @Test
  public void testVisit() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);

    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node n = createNode();
    Node parent = createNode();

    checkGlobalThis.visit(t, n, parent);

    // Test assertion here
  }

  @Test
  public void testShouldReportThis() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis();

    Node n = createNode();

    assertTrue(checkGlobalThis.shouldReportThis(n));
  }

  @Test
  public void testGetFunctionJsDocInfo() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis();

    Node n = createNode();

    JSDocInfo jsDocInfo = checkGlobalThis.getFunctionJsDocInfo(n);

    // Test assertion here
  }

  private Node createNode() {
    // Create and return a new Node
  }
}
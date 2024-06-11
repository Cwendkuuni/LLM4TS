import static org.junit.Assert.*;
import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverse() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node n = new Node(Token.FUNCTION);

    // Test with a function that is a constructor
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setConstructor(true);
    n.setJSDocInfo(jsDoc);
    boolean result = checkGlobalThis.shouldTraverse(t, n, null);
    assertFalse(result);

    // Test with a function that has @this annotation
    jsDoc = new JSDocInfo();
    jsDoc.recordThisType();
    n.setJSDocInfo(jsDoc);
    result = checkGlobalThis.shouldTraverse(t, n, null);
    assertFalse(result);

    // Test with a regular function
    result = checkGlobalThis.shouldTraverse(t, n, null);
    assertTrue(result);
  }

  @Test
  public void testVisit() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node n = new Node(Token.THIS);

    // Test with THIS on the left side of an assignment
    checkGlobalThis.visit(t, n, new Node(Token.ASSIGN));
    // Check that an error is reported
    assertEquals(1, compiler.getErrorCount());

    // Test with THIS with a property access
    checkGlobalThis.visit(t, n, new Node(Token.GETPROP));
    // Check that an error is reported
    assertEquals(2, compiler.getErrorCount());
  }
}
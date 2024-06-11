import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.Assert.*;

public class CheckGlobalThisTest {

  @Test
  public void testCheckGlobalThisConstructor() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    assertNotNull(checkGlobalThis);
  }
  
  @Test(expected = NullPointerException.class)
  public void testCheckGlobalThisConstructorWithNullAbstractCompiler() {
    AbstractCompiler compiler = null;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
  }

  @Test
  public void testShouldTraverse() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    Node parent = new Node();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler());

    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertTrue(result);
  }

  @Test
  public void testShouldTraverseWithNullParameters() {
    NodeTraversal t = null;
    Node n = null;
    Node parent = null;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler());

    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertFalse(result);
  }

  @Test
  public void testVisit() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node();
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    System.setOut(new PrintStream(baos));

    checkGlobalThis.visit(t, n, parent);
    String output = baos.toString();
    assertTrue(output.contains("dangerous use of the global 'this' object"));
  }

  @Test
  public void testVisitWithNullParameters() {
    NodeTraversal t = null;
    Node n = null;
    Node parent = null;
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    System.setOut(new PrintStream(baos));

    checkGlobalThis.visit(t, n, parent);
    String output = baos.toString();
    assertFalse(output.contains("dangerous use of the global 'this' object"));
  }

  @Test
  public void testShouldReportThis() {
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler());

    boolean result = checkGlobalThis.shouldReportThis(n, parent);
    assertTrue(result);
  }

  @Test
  public void testShouldReportThisWithNullParameters() {
    Node n = null;
    Node parent = null;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler());

    boolean result = checkGlobalThis.shouldReportThis(n, parent);
    assertFalse(result);
  }

  @Test
  public void testGetFunctionJsDocInfo() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler());

    JSDocInfo jsDoc = checkGlobalThis.getFunctionJsDocInfo(n);
    assertNull(jsDoc);
  }

  @Test
  public void testGetFunctionJsDocInfoWithNullNode() {
    Node n = null;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler());

    JSDocInfo jsDoc = checkGlobalThis.getFunctionJsDocInfo(n);
    assertNull(jsDoc);
  }

}
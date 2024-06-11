import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class CheckGlobalThisTest {

  private CheckGlobalThis checkGlobalThis;
  private AbstractCompiler compiler;
  private NodeTraversal.Callback callback;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    checkGlobalThis = new CheckGlobalThis(compiler);
    callback = checkGlobalThis;
  }

  @Test
  public void shouldTraverseReturnsTrueForRegularFunction() {
    NodeTraversal t = createNodeTraversal(Node.newString(Token.FUNCTION, "foo"));
    Node n = t.getNode();
    Node parent = n.getNext();

    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    assertTrue(result);
  }

  @Test
  public void shouldTraverseReturnsFalseForConstructorFunctionWithThisAnnotation() {
    NodeTraversal t = createNodeTraversal(Node.newString(Token.FUNCTION, "foo"));
    Node n = t.getNode();
    Node parent = n.getNext();
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordConstructor();
    n.setJSDocInfo(jsDoc);

    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    assertFalse(result);
  }

  @Test
  public void shouldTraverseReturnsFalseForFunctionWithThisAnnotation() {
    NodeTraversal t = createNodeTraversal(Node.newString(Token.FUNCTION, "foo"));
    Node n = t.getNode();
    Node parent = n.getNext();
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordThis();
    n.setJSDocInfo(jsDoc);

    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    assertFalse(result);
  }

  @Test
  public void shouldTraverseReturnsFalseForFunctionWithOverrideAnnotation() {
    NodeTraversal t = createNodeTraversal(Node.newString(Token.FUNCTION, "foo"));
    Node n = t.getNode();
    Node parent = n.getNext();
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordOverride();
    n.setJSDocInfo(jsDoc);

    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    assertFalse(result);
  }

  @Test
  public void shouldTraverseReturnsFalseForFunctionWithInterfaceAnnotation() {
    NodeTraversal t = createNodeTraversal(Node.newString(Token.FUNCTION, "foo"));
    Node n = t.getNode();
    Node parent = n.getNext();
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordInterface();
    n.setJSDocInfo(jsDoc);

    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    assertFalse(result);
  }

  @Test
  public void shouldTraverseReturnsFalseForFunctionInObjectLiteral() {
    NodeTraversal t = createNodeTraversal(Node.newString(Token.FUNCTION, "foo"));
    Node n = t.getNode();
    Node parent = n.getNext().getLastChild();
    parent.setType(Token.STRING_KEY);

    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    assertFalse(result);
  }

  @Test
  public void shouldTraverseReturnsFalseForObjectLiteralKeyWithLendsAnnotation() {
    NodeTraversal t = createNodeTraversal(Node.newString(Token.FUNCTION, "foo"));
    Node n = t.getNode();
    Node parent = n.getNext().getLastChild();
    parent.setType(Token.STRING_KEY);
    JSDocInfo jsDoc = parent.getParent().getJSDocInfo();
    jsDoc.recordLends(new Node(Token.NAME, "prototype"));
    parent.getParent().setJSDocInfo(jsDoc);

    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    assertFalse(result);
  }

  @Test
  public void shouldTraverseReturnsTrueForRegularFunctionWithLendsAnnotation() {
    NodeTraversal t = createNodeTraversal(Node.newString(Token.FUNCTION, "foo"));
    Node n = t.getNode();
    Node parent = n.getNext();
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordLends(new Node(Token.NAME, "prototype"));
    parent.setJSDocInfo(jsDoc);

    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    assertTrue(result);
  }

  @Test
  public void shouldTraverseReturnsFalseForFunctionInBlockStatement() {
    NodeTraversal t = createNodeTraversal(Node.newString(Token.FUNCTION, "foo"));
    Node n = t.getNode();
    Node parent = n.getNext().getFirstChild();
    parent.setType(Token.BLOCK);

    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    assertFalse(result);
  }

  @Test
  public void shouldTraverseReturnsFalseForFunctionInScript() {
    NodeTraversal t = createNodeTraversal(Node.newString(Token.FUNCTION, "foo"));
    Node n = t.getNode();
    Node parent = n.getNext();
    parent.setType(Token.SCRIPT);

    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    assertFalse(result);
  }

  @Test
  public void shouldTraverseReturnsFalseForFunctionInAssign() {
    NodeTraversal t = createNodeTraversal(Node.newString(Token.FUNCTION, "foo"));
    Node n = t.getNode();
    Node parent = n.getNext();
    parent.setType(Token.ASSIGN);

    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    assertFalse(result);
  }

  @Test
  public void shouldTraverseReturnsFalseForFunctionInGet() {
    NodeTraversal t = createNodeTraversal(Node.newString(Token.FUNCTION, "foo"));
    Node n = t.getNode();
    Node parent = n.getNext();
    Node lhs = new Node(Token.GETPROP, new Node(Token.NAME, "a"), new Node(Token.STRING, "property"));
    parent.getFirstChild().replaceChild(lhs, n);

    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    assertFalse(result);
  }

  @Test
  public void visitReportsErrorForThisNodeIfShouldReportThis() {
    NodeTraversal t = createNodeTraversal(Node.newString(Token.THIS, "this"));
    Node n = t.getNode();
    Node parent = n.getNext();
    checkGlobalThis.assignLhsChild = new Node(Token.NAME, "a");

    checkGlobalThis.visit(t, n, parent);

    assertNotNull(compiler.getErrorLevel(t.makeError(n, CheckGlobalThis.GLOBAL_THIS)));
  }

  @Test
  public void visitDoesNotReportErrorForThisNodeIfShouldNotReportThis() {
    NodeTraversal t = createNodeTraversal(Node.newString(Token.THIS, "this"));
    Node n = t.getNode();
    Node parent = n.getNext();
    checkGlobalThis.assignLhsChild = null;

    checkGlobalThis.visit(t, n, parent);

    assertNull(compiler.getErrorLevel(t.makeError(n, CheckGlobalThis.GLOBAL_THIS)));
  }

  private NodeTraversal createNodeTraversal(Node n) {
    return new NodeTraversal(compiler, callback, n);
  }
}
import static org.junit.Assert.*;
import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void shouldReportGlobalThisWithAssignment() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    boolean result = checkGlobalThis.shouldReportThis(n, parent);
    assertTrue(result);
  }

  @Test
  public void shouldReportGlobalThisWithPropertyAccess() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    boolean result = checkGlobalThis.shouldReportThis(n, parent);
    assertTrue(result);
  }

  @Test
  public void shouldNotReportGlobalThisOutsideAssignmentOrPropertyAccess() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.NAME);
    boolean result = checkGlobalThis.shouldReportThis(n, parent);
    assertFalse(result);
  }

  @Test
  public void shouldNotReportGlobalThisInsideConstructorFunctionWithJsDoc() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordConstructor();
    n.setJSDocInfo(jsDoc);
    Node parent = new Node(Token.ASSIGN);
    boolean result = checkGlobalThis.shouldReportThis(n, parent);
    assertFalse(result);
  }

  @Test
  public void shouldNotReportGlobalThisInsideFunctionWithThisJsDoc() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordThisType();
    n.setJSDocInfo(jsDoc);
    Node parent = new Node(Token.ASSIGN);
    boolean result = checkGlobalThis.shouldReportThis(n, parent);
    assertFalse(result);
  }

  @Test
  public void shouldNotReportGlobalThisInsideFunctionWithoutLogicalPlaceForJsDoc() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.ASSIGN);
    boolean result = checkGlobalThis.shouldReportThis(n, parent);
    assertFalse(result);
  }

  @Test
  public void shouldNotReportGlobalThisInsideConstructorFunctionWithInterfaceJsDoc() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordInterface();
    n.setJSDocInfo(jsDoc);
    Node parent = new Node(Token.ASSIGN);
    boolean result = checkGlobalThis.shouldReportThis(n, parent);
    assertFalse(result);
  }

  @Test
  public void shouldNotReportGlobalThisInsideFunctionWithOverrideJsDoc() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordOverride();
    n.setJSDocInfo(jsDoc);
    Node parent = new Node(Token.ASSIGN);
    boolean result = checkGlobalThis.shouldReportThis(n, parent);
    assertFalse(result);
  }

  @Test
  public void shouldNotReportGlobalThisInsideNonGlobalContext() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.BLOCK);
    boolean result = checkGlobalThis.shouldReportThis(n, parent);
    assertFalse(result);
  }

  @Test
  public void shouldNotReportGlobalThisInsideNonGlobalContextWithObjectLiteralKey() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.STRING);
    boolean result = checkGlobalThis.shouldReportThis(n, parent);
    assertFalse(result);
  }

  @Test
  public void shouldNotReportGlobalThisInsideNonGlobalContextWithObjectLiteralNumberKey() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.NUMBER);
    boolean result = checkGlobalThis.shouldReportThis(n, parent);
    assertFalse(result);
  }

  @Test
  public void shouldNotReportGlobalThisInsideNonGlobalContextWithAssignedFunction() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.SCRIPT);
    boolean result = checkGlobalThis.shouldReportThis(n, parent);
    assertFalse(result);
  }

  @Test
  public void shouldNotReportGlobalThisInsideNonGlobalContextWithFunctionDeclaration() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.NAME);
    boolean result = checkGlobalThis.shouldReportThis(n, parent);
    assertFalse(result);
  }

}
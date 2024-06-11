import org.junit.Test;
import static org.junit.Assert.*;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverse_withFunctionConstructor() {
    // Create test input
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis check = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal(compiler, check);
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setConstructor(true);
    n.setJSDocInfo(jsDoc);
    Node parent = new Node(Token.BLOCK);

    // Execute the method under test
    boolean result = check.shouldTraverse(t, n, parent);

    // Verify the test result
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_withFunctionThisAnnotation() {
    // Create test input
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis check = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal(compiler, check);
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordThisType();
    n.setJSDocInfo(jsDoc);
    Node parent = new Node(Token.BLOCK);

    // Execute the method under test
    boolean result = check.shouldTraverse(t, n, parent);

    // Verify the test result
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_withFunctionNoLogicalPlaceForThisAnnotation() {
    // Create test input
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis check = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal(compiler, check);
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.CATCH);

    // Execute the method under test
    boolean result = check.shouldTraverse(t, n, parent);

    // Verify the test result
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_withValidFunction() {
    // Create test input
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis check = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal(compiler, check);
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);

    // Execute the method under test
    boolean result = check.shouldTraverse(t, n, parent);

    // Verify the test result
    assertTrue(result);
  }

  @Test
  public void testShouldTraverse_withValidFunctionAssignment() {
    // Create test input
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis check = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal(compiler, check);
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.ASSIGN);

    // Execute the method under test
    boolean result = check.shouldTraverse(t, n, parent);

    // Verify the test result
    assertTrue(result);
  }

  @Test
  public void testVisit_withThisNode() {
    // Create test input
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis check = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal(compiler, check);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.NAME);

    // Execute the method under test
    check.visit(t, n, parent);

    // Verify the test result
    // No assertions. The method doesn't return anything.
  }

  @Test
  public void testVisit_withAssignmentThisNode() {
    // Create test input
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis check = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal(compiler, check);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    check.shouldReportThis = true;

    // Execute the method under test
    check.visit(t, n, parent);

    // Verify the test result
    // No assertions. The method doesn't return anything.
  }

  @Test
  public void testGetFunctionJsDocInfo_withJSDocInfoNotNull() {
    // Create test input
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    n.setJSDocInfo(jsDoc);
    Node parent = new Node(Token.BLOCK);
    CheckGlobalThis check = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);

    // Execute the method under test
    JSDocInfo result = check.getFunctionJsDocInfo(n, parent);

    // Verify the test result
    assertSame(jsDoc, result);
  }

  @Test
  public void testGetFunctionJsDocInfo_withParentNameAndJSDocInfo() {
    // Create test input
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    Node parent = new Node(Token.NAME);
    parent.setJSDocInfo(jsDoc);
    CheckGlobalThis check = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);

    // Execute the method under test
    JSDocInfo result = check.getFunctionJsDocInfo(n, parent);

    // Verify the test result
    assertSame(jsDoc, result);
  }

  @Test
  public void testGetFunctionJsDocInfo_withParentAssignAndJSDocInfo() {
    // Create test input
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    Node parent = new Node(Token.ASSIGN);
    parent.setJSDocInfo(jsDoc);
    CheckGlobalThis check = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);

    // Execute the method under test
    JSDocInfo result = check.getFunctionJsDocInfo(n, parent);

    // Verify the test result
    assertSame(jsDoc, result);
  }

  @Test
  public void testGetFunctionJsDocInfo_withParentNameAssignAndJSDocInfo() {
    // Create test input
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    Node parent = new Node(Token.NAME);
    parent.addChildToFront(new Node(Token.ASSIGN));
    parent.getFirstChild().setJSDocInfo(jsDoc);
    CheckGlobalThis check = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);

    // Execute the method under test
    JSDocInfo result = check.getFunctionJsDocInfo(n, parent);

    // Verify the test result
    assertSame(jsDoc, result);
  }

  @Test
  public void testGetFunctionJsDocInfo_withParentBlock() {
    // Create test input
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    CheckGlobalThis check = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);

    // Execute the method under test
    JSDocInfo result = check.getFunctionJsDocInfo(n, parent);

    // Verify the test result
    assertNull(result);
  }
}
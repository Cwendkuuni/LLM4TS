import static org.junit.Assert.*;
import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverse() {
    // Test shouldTraverse() method
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

    // Test for function with constructor annotation
    NodeTraversal t1 = new NodeTraversal();
    Node n1 = new Node(Token.FUNCTION);
    JSDocInfo jsDoc1 = new JSDocInfo();
    jsDoc1.setConstructor(true);
    n1.setJSDocInfo(jsDoc1);
    assertFalse(checkGlobalThis.shouldTraverse(t1, n1, null));

    // Test for function with interface annotation
    NodeTraversal t2 = new NodeTraversal();
    Node n2 = new Node(Token.FUNCTION);
    JSDocInfo jsDoc2 = new JSDocInfo();
    jsDoc2.setInterface(true);
    n2.setJSDocInfo(jsDoc2);
    assertFalse(checkGlobalThis.shouldTraverse(t2, n2, null));

    // Test for function with @this annotation
    NodeTraversal t3 = new NodeTraversal();
    Node n3 = new Node(Token.FUNCTION);
    JSDocInfo jsDoc3 = new JSDocInfo();
    jsDoc3.recordThisType();
    n3.setJSDocInfo(jsDoc3);
    assertFalse(checkGlobalThis.shouldTraverse(t3, n3, null));

    // Test for function with @override annotation
    NodeTraversal t4 = new NodeTraversal();
    Node n4 = new Node(Token.FUNCTION);
    JSDocInfo jsDoc4 = new JSDocInfo();
    jsDoc4.setOverride(true);
    n4.setJSDocInfo(jsDoc4);
    assertFalse(checkGlobalThis.shouldTraverse(t4, n4, null));

    // Test for function with valid parent types
    NodeTraversal t5 = new NodeTraversal();
    Node n5 = new Node(Token.FUNCTION);
    Node parent5 = new Node(Token.BLOCK);
    Node grandparent5 = new Node(Token.SCRIPT);
    assertTrue(checkGlobalThis.shouldTraverse(t5, n5, parent5));
    assertTrue(checkGlobalThis.shouldTraverse(t5, n5, grandparent5));

    // Test for function with invalid parent type
    NodeTraversal t6 = new NodeTraversal();
    Node n6 = new Node(Token.FUNCTION);
    Node parent6 = new Node(Token.GETPROP);
    assertFalse(checkGlobalThis.shouldTraverse(t6, n6, parent6));

    // Test for non-function node
    NodeTraversal t7 = new NodeTraversal();
    Node n7 = new Node(Token.NAME);
    assertFalse(checkGlobalThis.shouldTraverse(t7, n7, null));
  }

  @Test
  public void testVisit() {
    // Test visit() method
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();

    // Test THIS node with assignLhsChild node
    Node n1 = new Node(Token.THIS);
    Node parent1 = new Node(Token.ASSIGN);
    checkGlobalThis.assignLhsChild = new Node(Token.NAME);
    checkGlobalThis.visit(t, n1, parent1);
    assertNull(checkGlobalThis.assignLhsChild);

    // Test THIS node with Get node as parent
    Node n2 = new Node(Token.THIS);
    Node parent2 = new Node(Token.GETPROP);
    checkGlobalThis.visit(t, n2, parent2);
    assertTrue(compiler.hasReportedError());

    // Test THIS node with non-Get node as parent
    Node n3 = new Node(Token.THIS);
    Node parent3 = new Node(Token.NAME);
    checkGlobalThis.visit(t, n3, parent3);
    assertTrue(compiler.hasReportedError());
  }

  @Test
  public void testShouldReportThis() {
    // Test shouldReportThis() method
    Node n1 = new Node(Token.THIS);
    Node parent1 = new Node(Token.ASSIGN);
    CheckGlobalThis checkGlobalThis1 = new CheckGlobalThis(null, null);
    checkGlobalThis1.assignLhsChild = new Node(Token.NAME);
    assertTrue(checkGlobalThis1.shouldReportThis(n1, parent1));

    Node n2 = new Node(Token.THIS);
    Node parent2 = new Node(Token.GETPROP);
    CheckGlobalThis checkGlobalThis2 = new CheckGlobalThis(null, null);
    assertFalse(checkGlobalThis2.shouldReportThis(n2, parent2));

    Node n3 = new Node(Token.THIS);
    Node parent3 = new Node(Token.NAME);
    CheckGlobalThis checkGlobalThis3 = new CheckGlobalThis(null, null);
    assertFalse(checkGlobalThis3.shouldReportThis(n3, parent3));
  }

  @Test
  public void testGetFunctionJsDocInfo() {
    // Test getFunctionJsDocInfo() method
    Node n1 = new Node(Token.FUNCTION);
    JSDocInfo expectedJsDoc1 = new JSDocInfo();
    n1.setJSDocInfo(expectedJsDoc1);
    CheckGlobalThis checkGlobalThis1 = new CheckGlobalThis(null, null);
    JSDocInfo actualJsDoc1 = checkGlobalThis1.getFunctionJsDocInfo(n1);
    assertEquals(expectedJsDoc1, actualJsDoc1);

    Node n2 = new Node(Token.NAME);
    JSDocInfo expectedJsDoc2 = new JSDocInfo();
    Node parent2 = new Node(Token.VAR);
    parent2.setJSDocInfo(expectedJsDoc2);
    n2.setParent(parent2);
    CheckGlobalThis checkGlobalThis2 = new CheckGlobalThis(null, null);
    JSDocInfo actualJsDoc2 = checkGlobalThis2.getFunctionJsDocInfo(n2);
    assertEquals(expectedJsDoc2, actualJsDoc2);

    Node n3 = new Node(Token.ASSIGN);
    JSDocInfo expectedJsDoc3 = new JSDocInfo();
    Node parent3 = new Node(Token.VAR);
    parent3.setJSDocInfo(expectedJsDoc3);
    n3.setParent(parent3);
    CheckGlobalThis checkGlobalThis3 = new CheckGlobalThis(null, null);
    JSDocInfo actualJsDoc3 = checkGlobalThis3.getFunctionJsDocInfo(n3);
    assertEquals(expectedJsDoc3, actualJsDoc3);

    Node n4 = new Node(Token.FUNCTION);
    CheckGlobalThis checkGlobalThis4 = new CheckGlobalThis(null, null);
    JSDocInfo actualJsDoc4 = checkGlobalThis4.getFunctionJsDocInfo(n4);
    assertNull(actualJsDoc4);
  }
}
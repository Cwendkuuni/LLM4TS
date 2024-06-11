import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverse() {
    // Create a CheckGlobalThis instance
    CheckGlobalThis globalThis = new CheckGlobalThis(null, null);
    
    // Test shouldTraverse for FUNCTION node with constructor @this annotation
    NodeTraversal t1 = null;
    Node n1 = new Node(Token.FUNCTION);
    Node parent1 = null;
    assertFalse(globalThis.shouldTraverse(t1, n1, parent1));
    
    // Test shouldTraverse for FUNCTION node with @this annotation
    NodeTraversal t2 = null;
    Node n2 = new Node(Token.FUNCTION);
    JSDocInfo jsDoc2 = new JSDocInfo();
    jsDoc2.recordThisType();
    n2.setJSDocInfo(jsDoc2);
    Node parent2 = null;
    assertFalse(globalThis.shouldTraverse(t2, n2, parent2));
    
    // Test shouldTraverse for ASSIGN node with left side as prototype property
    NodeTraversal t3 = null;
    Node n3 = new Node(Token.NAME);
    Node lhs3 = new Node(Token.GETPROP);
    lhs3.addChildToBack(new Node(Token.NAME, "Object"));
    lhs3.addChildToBack(new Node(Token.STRING, "prototype"));
    n3.addChildToBack(lhs3);
    Node parent3 = new Node(Token.ASSIGN);
    parent3.addChildToBack(n3);
    assertTrue(globalThis.shouldTraverse(t3, n3, parent3));
    
    // Test shouldTraverse for ASSIGN node with left side as subproperty of prototype property
    NodeTraversal t4 = null;
    Node n4 = new Node(Token.NAME);
    Node lhs4 = new Node(Token.GETPROP);
    lhs4.addChildToBack(new Node(Token.NAME, "Object"));
    lhs4.addChildToBack(new Node(Token.STRING, "prototype.a"));
    n4.addChildToBack(lhs4);
    Node parent4 = new Node(Token.ASSIGN);
    parent4.addChildToBack(n4);
    assertTrue(globalThis.shouldTraverse(t4, n4, parent4));
    
    // Test shouldTraverse for ASSIGN node with right side as non-prototype property
    NodeTraversal t5 = null;
    Node n5 = new Node(Token.NAME);
    Node lhs5 = new Node(Token.NAME, "this");
    lhs5.putBooleanProp(Node.ANALYZED_DURING_GTI, true);
    Node rhs5 = new Node(Token.NAME, "undefined");
    n5.addChildToBack(lhs5);
    n5.addChildToBack(rhs5);
    Node parent5 = new Node(Token.ASSIGN);
    parent5.addChildToBack(n5);
    assertTrue(globalThis.shouldTraverse(t5, n5, parent5));
    
    // Test shouldTraverse for ASSIGN node with right side as prototype property
    NodeTraversal t6 = null;
    Node n6 = new Node(Token.NAME);
    Node lhs6 = new Node(Token.NAME, "this");
    lhs6.putBooleanProp(Node.ANALYZED_DURING_GTI, true);
    Node rhs6 = new Node(Token.GETPROP);
    rhs6.addChildToBack(new Node(Token.NAME, "Object"));
    rhs6.addChildToBack(new Node(Token.STRING, "prototype"));
    n6.addChildToBack(lhs6);
    n6.addChildToBack(rhs6);
    Node parent6 = new Node(Token.ASSIGN);
    parent6.addChildToBack(n6);
    assertFalse(globalThis.shouldTraverse(t6, n6, parent6));
    
    // Test shouldTraverse for ASSIGN node with right side as subproperty of prototype property
    NodeTraversal t7 = null;
    Node n7 = new Node(Token.NAME);
    Node lhs7 = new Node(Token.NAME, "this");
    lhs7.putBooleanProp(Node.ANALYZED_DURING_GTI, true);
    Node rhs7 = new Node(Token.GETPROP);
    rhs7.addChildToBack(new Node(Token.NAME, "Object"));
    rhs7.addChildToBack(new Node(Token.STRING, "prototype.a"));
    n7.addChildToBack(lhs7);
    n7.addChildToBack(rhs7);
    Node parent7 = new Node(Token.ASSIGN);
    parent7.addChildToBack(n7);
    assertFalse(globalThis.shouldTraverse(t7, n7, parent7));
    
    // Test shouldTraverse for unnamed function
    NodeTraversal t8 = null;
    Node n8 = new Node(Token.FUNCTION);
    Node grandParent8 = new Node(Token.VAR);
    Node parent8 = new Node(Token.ASSIGN);
    grandParent8.addChildToBack(parent8);
    parent8.addChildToBack(n8);
    assertFalse(globalThis.shouldTraverse(t8, n8, parent8));
    
    // Test shouldTraverse for named function
    NodeTraversal t9 = null;
    Node n9 = new Node(Token.FUNCTION);
    Node parent9 = new Node(Token.ASSIGN);
    parent9.addChildToBack(n9);
    assertFalse(globalThis.shouldTraverse(t9, n9, parent9));
  }

  @Test
  public void testVisit() {
    // Create a CheckGlobalThis instance
    CheckGlobalThis globalThis = new CheckGlobalThis(null, null);
    
    // Test visit with THIS node that should be reported
    NodeTraversal t1 = null;
    Node n1 = new Node(Token.THIS);
    Node parent1 = null;
    
    // TODO: Add assertion for compiler.report() being called with expected error
    
    // Test visit with THIS node that should not be reported
    NodeTraversal t2 = null;
    Node n2 = new Node(Token.THIS);
    Node parent2 = new Node(Token.ASSIGN);
    
    // TODO: Add assertion for compiler.report() not being called
    
    // Test visit with assignLhsChild being set
    NodeTraversal t3 = null;
    Node n3 = new Node(Token.THIS);
    Node parent3 = new Node(Token.NAME);
    Node grandParent3 = new Node(Token.ASSIGN);
    grandParent3.addChildToFront(parent3);
    parent3.addChildToFront(n3);
    globalThis.assignLhsChild = parent3;
    
    // TODO: Add assertion for compiler.report() being called with expected error
    
    // Test visit with assignLhsChild being null
    NodeTraversal t4 = null;
    Node n4 = new Node(Token.THIS);
    Node parent4 = new Node(Token.NAME);
    Node grandParent4 = new Node(Token.ASSIGN);
    grandParent4.addChildToFront(parent4);
    parent4.addChildToFront(n4);
    globalThis.assignLhsChild = null;
    
    // TODO: Add assertion for compiler.report() not being called
  }

  @Test
  public void testShouldReportThis() {
    // Create a CheckGlobalThis instance
    CheckGlobalThis globalThis = new CheckGlobalThis(null, null);
    
    // Test shouldReportThis with assignLhsChild not being null
    Node n1 = new Node(Token.THIS);
    Node parent1 = null;
    globalThis.assignLhsChild = parent1;
    assertTrue(globalThis.shouldReportThis(n1, parent1));
    
    // Test shouldReportThis with assignLhsChild being null
    Node n2 = new Node(Token.THIS);
    Node parent2 = new Node(Token.NAME);
    globalThis.assignLhsChild = null;
    assertFalse(globalThis.shouldReportThis(n2, parent2));
  }

  @Test
  public void testGetFunctionJsDocInfo() {
    // Create a CheckGlobalThis instance
    CheckGlobalThis globalThis = new CheckGlobalThis(null, null);
    
    // Test getFunctionJsDocInfo with JSDocInfo available on FUNCTION node
    Node n1 = new Node(Token.FUNCTION);
    JSDocInfo jsDoc1 = new JSDocInfo();
    n1.setJSDocInfo(jsDoc1);
    assertEquals(jsDoc1, globalThis.getFunctionJsDocInfo(n1));
    
    // Test getFunctionJsDocInfo with parent of FUNCTION node being NAME node
    Node n2 = new Node(Token.FUNCTION);
    JSDocInfo jsDoc2 = new JSDocInfo();
    Node parent2 = new Node(Token.NAME);
    parent2.setJSDocInfo(jsDoc2);
    parent2.addChildToBack(n2);
    assertEquals(jsDoc2, globalThis.getFunctionJsDocInfo(n2));
    
    // Test getFunctionJsDocInfo with grandparent of FUNCTION node being VAR node
    Node n3 = new Node(Token.FUNCTION);
    JSDocInfo jsDoc3 = new JSDocInfo();
    Node parent3 = new Node(Token.NAME);
    parent3.setJSDocInfo(jsDoc3);
    Node grandParent3 = new Node(Token.VAR);
    grandParent3.addChildToBack(parent3);
    parent3.addChildToBack(n3);
    assertEquals(jsDoc3, globalThis.getFunctionJsDocInfo(n3));
    
    // Test getFunctionJsDocInfo with JSDocInfo not available on FUNCTION node or its parents
    Node n4 = new Node(Token.FUNCTION);
    assertNull(globalThis.getFunctionJsDocInfo(n4));
  }
}
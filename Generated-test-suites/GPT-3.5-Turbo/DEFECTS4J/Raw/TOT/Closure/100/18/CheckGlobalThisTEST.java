import static org.junit.Assert.*;
import org.junit.Test;

public class CheckGlobalThisTest {

   // Test case for shouldTraverse() method
   @Test
   public void testShouldTraverse() {
      // Typical use case: Test when n.getType() is FUNCTION and jsDoc.isConstructor() is true
      // The function should not be traversed
      CheckGlobalThis instance = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
      Node n = new Node(Token.FUNCTION);
      JSDocInfo jsDoc = new JSDocInfoBuilder().recordConstructor().build();
      n.setJSDocInfo(jsDoc);
      assertFalse(instance.shouldTraverse(new NodeTraversal(), n, null));
      
      // Typical use case: Test when n.getType() is FUNCTION and jsDoc.hasThisType() is true
      // The function should not be traversed
      n = new Node(Token.FUNCTION);
      jsDoc = new JSDocInfoBuilder().recordThisType().build();
      n.setJSDocInfo(jsDoc);
      assertFalse(instance.shouldTraverse(new NodeTraversal(), n, null));
      
      // Typical use case: Test when parent.getType() is ASSIGN and n is left child of assign node
      // The left side of an assignment should always be traversed
      n = new Node(Token.NAME);
      Node parent = new Node(Token.ASSIGN);
      parent.addChildToFront(n);
      assertTrue(instance.shouldTraverse(new NodeTraversal(), n, parent));
      
      // Typical use case: Test when parent.getType() is ASSIGN and n is right child of assign node
      // If the assignment is to a prototype property or subproperty, the right side should not be traversed
      n = new Node(Token.NAME);
      parent = new Node(Token.ASSIGN);
      parent.addChildToFront(new Node(Token.GETPROP));
      parent.getFirstChild().addChildToFront(n);
      assertFalse(instance.shouldTraverse(new NodeTraversal(), n, parent));
      
      // Edge case: Test when parent.getType() is not ASSIGN
      // The node should be traversed
      n = new Node(Token.NAME);
      parent = new Node(Token.VAR);
      assertTrue(instance.shouldTraverse(new NodeTraversal(), n, parent));
   }

   // Test case for visit() method
   @Test
   public void testVisit() {
      // Typical use case: Test when n.getType() is THIS and shouldReportThis() is true
      // An error should be reported
      CheckGlobalThis instance = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
      Node n = new Node(Token.THIS);
      Node parent = new Node(Token.ASSIGN);
      assertTrue(instance.shouldReportThis(n, parent));
      NodeTraversal t = new NodeTraversal();
      instance.visit(t, n, parent);
      assertEquals(1, t.getErrorCount());
      
      // Typical use case: Test when n is the same as assignLhsChild
      // assignLhsChild should be set to null after visit()
      n = new Node(Token.THIS);
      instance.assignLhsChild = n;
      instance.visit(t, n, null);
      assertNull(instance.assignLhsChild);
      
      // Edge case: Test when n.getType() is not THIS
      // No error should be reported
      n = new Node(Token.NAME);
      parent = new Node(Token.ASSIGN);
      assertFalse(instance.shouldReportThis(n, parent));
      t = new NodeTraversal();
      instance.visit(t, n, parent);
      assertEquals(0, t.getErrorCount());
   }
   
   // Test case for getFunctionJsDocInfo() method
   @Test
   public void testGetFunctionJsDocInfo() {
      // Typical use case: Test when n.getJSDocInfo() is not null
      // n.getJSDocInfo() should be returned
      CheckGlobalThis instance = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
      Node n = new Node(Token.FUNCTION);
      JSDocInfo jsDoc = new JSDocInfoBuilder().recordConstructor().build();
      n.setJSDocInfo(jsDoc);
      assertSame(jsDoc, instance.getFunctionJsDocInfo(n));
      
      // Typical use case: Test when parent.isName() is true and parent.getJSDocInfo() is not null
      // parent.getJSDocInfo() should be returned
      Node parent = new Node(Token.ASSIGN);
      parent.setJSDocInfo(jsDoc);
      n = new Node(Token.NAME);
      parent.addChildToFront(n);
      assertSame(jsDoc, instance.getFunctionJsDocInfo(n));
      
      // Edge case: Test when parent.isName() is true and parent.getJSDocInfo() is null
      // grandparent of parent has JSDocInfo and grandparent is of type VAR
      // grandparent.getJSDocInfo() should be returned
      Node grandparent = new Node(Token.VAR);
      grandparent.setJSDocInfo(jsDoc);
      n = new Node(Token.NAME);
      parent = new Node(Token.ASSIGN);
      parent.addChildToFront(n);
      grandparent.addChildToFront(parent);
      assertSame(jsDoc, instance.getFunctionJsDocInfo(n));
      
      // Edge case: Test when parent.isName() is true and parent.getJSDocInfo() is null
      // grandparent of parent has JSDocInfo and grandparent is not of type VAR
      // null should be returned
      grandparent = new Node(Token.FUNCTION);
      grandparent.setJSDocInfo(jsDoc);
      n = new Node(Token.NAME);
      parent = new Node(Token.ASSIGN);
      parent.addChildToFront(n);
      grandparent.addChildToFront(parent);
      assertNull(instance.getFunctionJsDocInfo(n));
      
      // Edge case: Test when parent.getType() is not ASSIGN and n.getJSDocInfo() is null
      // null should be returned
      n = new Node(Token.NAME);
      parent = new Node(Token.VAR);
      assertNull(instance.getFunctionJsDocInfo(n));
   }
}
import static org.junit.Assert.*;
import org.junit.Test;

public class CheckGlobalThisTest {

   @Test
   public void testShouldTraverse_WithFunctionAnnotatedWithConstructor() {
      // Arrange
      AbstractCompiler compiler = new AbstractCompiler();
      CheckLevel level = CheckLevel.WARNING;
      CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
      NodeTraversal t = new NodeTraversal();

      Node n = new Node(Token.FUNCTION);
      JSDocInfo jsDoc = new JSDocInfo();
      jsDoc.setConstructor(true);
      n.setJSDocInfo(jsDoc);

      // Act
      boolean result = checkGlobalThis.shouldTraverse(t, n, null);

      // Assert
      assertFalse(result);
   }

   @Test
   public void testShouldTraverse_WithFunctionAnnotatedWithThis() {
      // Arrange
      AbstractCompiler compiler = new AbstractCompiler();
      CheckLevel level = CheckLevel.WARNING;
      CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
      NodeTraversal t = new NodeTraversal();

      Node n = new Node(Token.FUNCTION);
      JSDocInfo jsDoc = new JSDocInfo();
      jsDoc.recordThisType();
      n.setJSDocInfo(jsDoc);

      // Act
      boolean result = checkGlobalThis.shouldTraverse(t, n, null);

      // Assert
      assertFalse(result);
   }

   @Test
   public void testShouldTraverse_WithFunctionAnnotatedWithOverride() {
      // Arrange
      AbstractCompiler compiler = new AbstractCompiler();
      CheckLevel level = CheckLevel.WARNING;
      CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
      NodeTraversal t = new NodeTraversal();

      Node n = new Node(Token.FUNCTION);
      JSDocInfo jsDoc = new JSDocInfo();
      jsDoc.setOverride(true);
      n.setJSDocInfo(jsDoc);

      // Act
      boolean result = checkGlobalThis.shouldTraverse(t, n, null);

      // Assert
      assertFalse(result);
   }

   @Test
   public void testShouldTraverse_WithFunctionAnnotatedWithInterface() {
      // Arrange
      AbstractCompiler compiler = new AbstractCompiler();
      CheckLevel level = CheckLevel.WARNING;
      CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
      NodeTraversal t = new NodeTraversal();

      Node n = new Node(Token.FUNCTION);
      JSDocInfo jsDoc = new JSDocInfo();
      jsDoc.setInterface(true);
      n.setJSDocInfo(jsDoc);

      // Act
      boolean result = checkGlobalThis.shouldTraverse(t, n, null);

      // Assert
      assertFalse(result);
   }

   @Test
   public void testShouldTraverse_WithFunctionNotInValidParent() {
      // Arrange
      AbstractCompiler compiler = new AbstractCompiler();
      CheckLevel level = CheckLevel.WARNING;
      CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
      NodeTraversal t = new NodeTraversal();

      Node n = new Node(Token.FUNCTION);

      Node parent = new Node(Token.ARRAYLIT);
      parent.addChildToFront(n);

      // Act
      boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

      // Assert
      assertFalse(result);
   }

   @Test
   public void testShouldTraverse_WithFunctionInValidParent() {
      // Arrange
      AbstractCompiler compiler = new AbstractCompiler();
      CheckLevel level = CheckLevel.WARNING;
      CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
      NodeTraversal t = new NodeTraversal();

      Node n = new Node(Token.FUNCTION);

      Node parent = new Node(Token.SCRIPT);
      parent.addChildToFront(n);

      // Act
      boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

      // Assert
      assertTrue(result);
   }

   @Test
   public void testShouldTraverse_WithFunctionInValidParentNestedAssignment() {
      // Arrange
      AbstractCompiler compiler = new AbstractCompiler();
      CheckLevel level = CheckLevel.WARNING;
      CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
      NodeTraversal t = new NodeTraversal();

      Node n = new Node(Token.FUNCTION);

      Node assignNode = new Node(Token.ASSIGN);
      assignNode.addChildToFront(n);

      Node parent = new Node(Token.SCRIPT);
      parent.addChildToFront(assignNode);

      // Act
      boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

      // Assert
      assertTrue(result);
   }

   @Test
   public void testShouldReportThis_WithThisOnLeftSideOfAssign() {
      // Arrange
      AbstractCompiler compiler = new AbstractCompiler();
      CheckLevel level = CheckLevel.WARNING;
      CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
      NodeTraversal t = new NodeTraversal();

      Node n = new Node(Token.THIS);
      Node parent = new Node(Token.ASSIGN);

      // Act
      boolean result = checkGlobalThis.shouldReportThis(n, parent);

      // Assert
      assertTrue(result);
   }

   @Test
   public void testShouldReportThis_WithThisNotOnLeftSideOfAssign() {
      // Arrange
      AbstractCompiler compiler = new AbstractCompiler();
      CheckLevel level = CheckLevel.WARNING;
      CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
      NodeTraversal t = new NodeTraversal();

      Node n = new Node(Token.THIS);
      Node parent = new Node(Token.GETPROP);

      // Act
      boolean result = checkGlobalThis.shouldReportThis(n, parent);

      // Assert
      assertTrue(result);
   }

   @Test
   public void testShouldReportThis_WithThisNotOnLeftSideOfAssignAndNoParent() {
      // Arrange
      AbstractCompiler compiler = new AbstractCompiler();
      CheckLevel level = CheckLevel.WARNING;
      CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
      NodeTraversal t = new NodeTraversal();

      Node n = new Node(Token.THIS);
      Node parent = null;

      // Act
      boolean result = checkGlobalThis.shouldReportThis(n, parent);

      // Assert
      assertFalse(result);
   }

   @Test
   public void testVisit_OnThisWithNoParent() {
      // Arrange
      AbstractCompiler compiler = new AbstractCompiler();
      CheckLevel level = CheckLevel.WARNING;
      CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
      NodeTraversal t = new NodeTraversal();

      Node n = new Node(Token.THIS);

      // Act
      checkGlobalThis.visit(t, n, null);

      // Assert
      // No assertion possible, method does not return anything
   }

   @Test
   public void testVisit_OnThisWithAssignLhsChildSet() {
      // Arrange
      AbstractCompiler compiler = new AbstractCompiler();
      CheckLevel level = CheckLevel.WARNING;
      CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
      NodeTraversal t = new NodeTraversal();

      checkGlobalThis.assignLhsChild = new Node(Token.THIS);

      Node n = new Node(Token.THIS);
      Node parent = new Node(Token.GETPROP);

      // Act
      checkGlobalThis.visit(t, n, parent);

      // Assert
      assertFalse(checkGlobalThis.assignLhsChild == null);
   }

   @Test
   public void testVisit_OnThisWithAssignLhsChildNotSet() {
      // Arrange
      AbstractCompiler compiler = new AbstractCompiler();
      CheckLevel level = CheckLevel.WARNING;
      CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
      NodeTraversal t = new NodeTraversal();

      checkGlobalThis.assignLhsChild = null;

      Node n = new Node(Token.THIS);
      Node parent = new Node(Token.GETPROP);

      // Act
      checkGlobalThis.visit(t, n, parent);

      // Assert
      assertTrue(checkGlobalThis.assignLhsChild == null);
   }

   @Test
   public void testVisit_OnThisWithAssignLhsChildReset() {
      // Arrange
      AbstractCompiler compiler = new AbstractCompiler();
      CheckLevel level = CheckLevel.WARNING;
      CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
      NodeTraversal t = new NodeTraversal();

      checkGlobalThis.assignLhsChild = new Node(Token.THIS);

      Node n = new Node(Token.THIS);
      Node parent = new Node(Token.GETPROP);

      // Act
      checkGlobalThis.visit(t, n, parent);

      // Assert
      assertTrue(checkGlobalThis.assignLhsChild == null);
   }

   @Test
   public void testGetFunctionJsDocInfo_WithJsDocInfoOnFunction() {
      // Arrange
      Node n = new Node(Token.FUNCTION);
      JSDocInfo jsDoc = new JSDocInfo();
      n.setJSDocInfo(jsDoc);

      // Act
      JSDocInfo result = CheckGlobalThis.getFunctionJsDocInfo(n);

      // Assert
      assertEquals(jsDoc, result);
   }

   @Test
   public void testGetFunctionJsDocInfo_WithJsDocInfoOnParentName() {
      // Arrange
      Node n = new Node(Token.FUNCTION);
      Node parent = new Node(Token.NAME);
      JSDocInfo jsDoc = new JSDocInfo();
      parent.setJSDocInfo(jsDoc);
      parent.addChildToFront(n);

      // Act
      JSDocInfo result = CheckGlobalThis.getFunctionJsDocInfo(n);

      // Assert
      assertEquals(jsDoc, result);
   }

   @Test
   public void testGetFunctionJsDocInfo_WithJsDocInfoOnGrandParentVar() {
      // Arrange
      Node n = new Node(Token.FUNCTION);
      Node parent = new Node(Token.NAME);
      Node grandParent = new Node(Token.VAR);
      JSDocInfo jsDoc = new JSDocInfo();
      grandParent.setJSDocInfo(jsDoc);
      parent.setParent(grandParent);
      parent.addChildToFront(n);
      grandParent.addChildToFront(parent);

      // Act
      JSDocInfo result = CheckGlobalThis.getFunctionJsDocInfo(n);

      // Assert
      assertEquals(jsDoc, result);
   }

   @Test
   public void testGetFunctionJsDocInfo_WithNoJsDocInfo() {
      // Arrange
      Node n = new Node(Token.FUNCTION);

      // Act
      JSDocInfo result = CheckGlobalThis.getFunctionJsDocInfo(n);

      // Assert
      assertNull(result);
   }

}
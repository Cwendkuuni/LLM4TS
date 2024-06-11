import static org.junit.Assert.*;
import org.junit.Test;

public class CheckGlobalThisTest {

   @Test
   public void testShouldTraverse_withFunctionConstructor() {
      // Arrange
      AbstractCompiler compiler = new AbstractCompiler();
      CheckLevel level = CheckLevel.WARNING;
      CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
      NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
      Node n = new Node(Token.FUNCTION);
      Node parent = new Node(Token.NAME);
      parent.setType(Token.NAME);
      JSDocInfo jsDoc = new JSDocInfo();
      jsDoc.setConstructor(true);
      n.setJSDocInfo(jsDoc);

      // Act
      boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

      // Assert
      assertFalse(result);
   }

   @Test
   public void testShouldTraverse_withFunctionThisAnnotation() {
      // Arrange
      AbstractCompiler compiler = new AbstractCompiler();
      CheckLevel level = CheckLevel.WARNING;
      CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
      NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
      Node n = new Node(Token.FUNCTION);
      Node parent = new Node(Token.NAME);
      parent.setType(Token.NAME);
      JSDocInfo jsDoc = new JSDocInfo();
      jsDoc.recordThisType();
      n.setJSDocInfo(jsDoc);

      // Act
      boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

      // Assert
      assertFalse(result);
   }

   @Test
   public void testShouldTraverse_withFunctionNoAnnotation() {
      // Arrange
      AbstractCompiler compiler = new AbstractCompiler();
      CheckLevel level = CheckLevel.WARNING;
      CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
      NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
      Node n = new Node(Token.FUNCTION);
      Node parent = new Node(Token.NAME);
      parent.setType(Token.NAME);
      JSDocInfo jsDoc = null;
      n.setJSDocInfo(jsDoc);

      // Act
      boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

      // Assert
      assertTrue(result);
   }

   @Test
   public void testShouldTraverse_withAssignmentLeftSideNested() {
      // Arrange
      AbstractCompiler compiler = new AbstractCompiler();
      CheckLevel level = CheckLevel.WARNING;
      CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
      NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
      Node n = new Node(Token.NAME);
      Node parent = new Node(Token.ASSIGN);
      parent.setType(Token.ASSIGN);
      Node lhs = new Node(Token.NAME);
      lhs.setType(Token.NAME);
      Node rhs = new Node(Token.THIS);
      rhs.setType(Token.THIS);
      parent.addChildToBack(lhs);
      parent.addChildToBack(rhs);
      lhs.addChildBefore(n, null);
      checkGlobalThis.assignLhsChild = lhs;

      // Act
      boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

      // Assert
      assertTrue(result);
   }

   @Test
   public void testShouldTraverse_withAssignmentLeftSideNotNested() {
      // Arrange
      AbstractCompiler compiler = new AbstractCompiler();
      CheckLevel level = CheckLevel.WARNING;
      CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
      NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
      Node n = new Node(Token.NAME);
      Node parent = new Node(Token.ASSIGN);
      parent.setType(Token.ASSIGN);
      Node lhs = new Node(Token.NAME);
      lhs.setType(Token.NAME);
      Node rhs = new Node(Token.THIS);
      rhs.setType(Token.THIS);
      parent.addChildToBack(lhs);
      parent.addChildToBack(rhs);
      lhs.addChildBefore(n, null);
      checkGlobalThis.assignLhsChild = null;

      // Act
      boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

      // Assert
      assertFalse(result);
   }

   @Test
   public void testShouldTraverse_withAssignmentLeftSidePrototypeProperty() {
      // Arrange
      AbstractCompiler compiler = new AbstractCompiler();
      CheckLevel level = CheckLevel.WARNING;
      CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
      NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
      Node n = new Node(Token.NAME);
      Node parent = new Node(Token.ASSIGN);
      parent.setType(Token.ASSIGN);
      Node lhs = new Node(Token.GETPROP);
      lhs.setType(Token.GETPROP);
      Node lhsFirstChild = new Node(Token.NAME);
      lhsFirstChild.setType(Token.NAME);
      lhsFirstChild.setString("prototype");
      Node lhsLastChild = new Node(Token.NEWSTRING);
      lhsLastChild.setType(Token.NEWSTRING);
      lhsLastChild.setString("property");
      lhs.addChildToBack(lhsFirstChild);
      lhs.addChildToBack(lhsLastChild);
      Node rhs = new Node(Token.THIS);
      rhs.setType(Token.THIS);
      parent.addChildToBack(lhs);
      parent.addChildToBack(rhs);
      lhsFirstChild.addChildBefore(n, null);
      checkGlobalThis.assignLhsChild = null;

      // Act
      boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

      // Assert
      assertFalse(result);
   }

   @Test
   public void testShouldTraverse_withAssignmentLeftSideNestedPrototypeProperty() {
      // Arrange
      AbstractCompiler compiler = new AbstractCompiler();
      CheckLevel level = CheckLevel.WARNING;
      CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
      NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
      Node n = new Node(Token.NAME);
      Node parent = new Node(Token.ASSIGN);
      parent.setType(Token.ASSIGN);
      Node lhs = new Node(Token.GETPROP);
      lhs.setType(Token.GETPROP);
      Node lhsFirstChild = new Node(Token.NAME);
      lhsFirstChild.setType(Token.NAME);
      lhsFirstChild.setString("prototype");
      Node lhsGrandChild = new Node(Token.GETPROP);
      lhsGrandChild.setType(Token.GETPROP);
      Node lhsGrandChildFirstChild = new Node(Token.THIS);
      lhsGrandChildFirstChild.setType(Token.THIS);
      Node lhsGrandChildLastChild = new Node(Token.NEWSTRING);
      lhsGrandChildLastChild.setType(Token.NEWSTRING);
      lhsGrandChildLastChild.setString("property");
      lhsGrandChild.addChildToBack(lhsGrandChildFirstChild);
      lhsGrandChild.addChildToBack(lhsGrandChildLastChild);
      lhs.addChildToBack(lhsFirstChild);
      lhs.addChildToBack(lhsGrandChild);
      Node rhs = new Node(Token.THIS);
      rhs.setType(Token.THIS);
      parent.addChildToBack(lhs);
      parent.addChildToBack(rhs);
      lhsFirstChild.addChildBefore(n, null);
      checkGlobalThis.assignLhsChild = lhsGrandChildFirstChild;

      // Act
      boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

      // Assert
      assertTrue(result);
   }

   @Test
   public void testVisit_withThis_shouldReportThis() {
      // Arrange
      AbstractCompiler compiler = new AbstractCompiler();
      CheckLevel level = CheckLevel.WARNING;
      CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
      NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
      Node n = new Node(Token.THIS);
      Node parent = new Node(Token.NAME);

      // Act
      checkGlobalThis.visit(t, n, parent);

      // Assert
      // Assert that the error is reported
      // TODO: Implement assertion for error reporting
   }

   @Test
   public void testVisit_withNotAssignLhsChild() {
      // Arrange
      AbstractCompiler compiler = new AbstractCompiler();
      CheckLevel level = CheckLevel.WARNING;
      CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
      NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
      Node n = new Node(Token.THIS);
      Node parent = new Node(Token.NAME);
      checkGlobalThis.assignLhsChild = null;

      // Act
      checkGlobalThis.visit(t, n, parent);

      // Assert
      assertFalse(checkGlobalThis.assignLhsChild);
   }


   @Test
   public void testShouldReportThis_withAssignLhsChildNotNull() {
      // Arrange
      CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
      Node n = new Node(Token.THIS);
      Node parent = new Node(Token.NAME);
      checkGlobalThis.assignLhsChild = new Node(Token.NAME);

      // Act
      boolean result = checkGlobalThis.shouldReportThis(n, parent);

      // Assert
      assertTrue(result);
   }

   @Test
   public void testShouldReportThis_withAssignLhsChildNull() {
      // Arrange
      CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
      Node n = new Node(Token.THIS);
      Node parent = new Node(Token.NAME);
      checkGlobalThis.assignLhsChild = null;

      // Act
      boolean result = checkGlobalThis.shouldReportThis(n, parent);

      // Assert
      assertFalse(result);
   }

   @Test
   public void testGetFunctionJsDocInfo_withJsDocNotNull() {
      // Arrange
      CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
      Node n = new Node(Token.FUNCTION);
      JSDocInfo jsDoc = new JSDocInfo();
      n.setJSDocInfo(jsDoc);

      // Act
      JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

      // Assert
      assertEquals(jsDoc, result);
   }

   @Test
   public void testGetFunctionJsDocInfo_withParentNameAndJsDocNotNull() {
      // Arrange
      CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
      Node n = new Node(Token.FUNCTION);
      Node parent = new Node(Token.NAME);
      parent.setType(Token.NAME);
      parent.setJSDocInfo(null);
      n.setParent(parent);
      JSDocInfo jsDoc = new JSDocInfo();
      parent.setJSDocInfo(jsDoc);

      // Act
      JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

      // Assert
      assertEquals(jsDoc, result);
   }

   @Test
   public void testGetFunctionJsDocInfo_withParentAssignNameAndJsDocNotNull() {
      // Arrange
      CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
      Node n = new Node(Token.FUNCTION);
      Node parent = new Node(Token.NAME);
      parent.setType(Token.NAME);
      parent.setJSDocInfo(null);
      Node gramps = new Node(Token.VAR);
      gramps.setJSDocInfo(null);
      gramps.addChildToBack(parent);
      n.setParent(parent);
      parent.setParent(gramps);
      JSDocInfo jsDoc = new JSDocInfo();
      gramps.setJSDocInfo(jsDoc);

      // Act
      JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

      // Assert
      assertEquals(jsDoc, result);
   }

   @Test
   public void testGetFunctionJsDocInfo_withResultJsDocNull() {
      // Arrange
      CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
      Node n = new Node(Token.FUNCTION);
      Node parent = new Node(Token.NAME);
      parent.setType(Token.NAME);
      parent.setJSDocInfo(null);
      Node gramps = new Node(Token.VAR);
      gramps.setJSDocInfo(null);
      gramps.addChildToBack(parent);
      n.setParent(parent);
      parent.setParent(gramps);

      // Act
      JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

      // Assert
      assertNull(result);
   }
}
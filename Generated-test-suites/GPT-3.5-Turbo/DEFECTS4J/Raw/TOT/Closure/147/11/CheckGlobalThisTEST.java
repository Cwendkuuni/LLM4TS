import org.junit.Assert;
import org.junit.Test;
import com.google.javascript.jscomp.*;

public class CheckGlobalThisTest {

   @Test
   public void testShouldTraverse_WithConstructorFunction_ReturnsFalse() {
      AbstractCompiler compiler = new AbstractCompiler();
      CheckGlobalThis check = new CheckGlobalThis(compiler, CheckLevel.WARNING);
      NodeTraversal t = new NodeTraversal(compiler, check);
      Node n = new Node(Token.FUNCTION);
      n.putProp(Node.JSDOC_INFO_PROP, JSDocInfo.builder(false).markAsConstructor().build());
      boolean result = check.shouldTraverse(t, n, null);
      Assert.assertFalse(result);
   }

   @Test
   public void testShouldTraverse_WithThisAnnotation_ReturnsFalse() {
      AbstractCompiler compiler = new AbstractCompiler();
      CheckGlobalThis check = new CheckGlobalThis(compiler, CheckLevel.WARNING);
      NodeTraversal t = new NodeTraversal(compiler, check);
      Node n = new Node(Token.FUNCTION);
      n.putProp(Node.JSDOC_INFO_PROP, JSDocInfo.builder(false).markAsConstructor().build());
      boolean result = check.shouldTraverse(t, n, null);
      Assert.assertFalse(result);
   }

   @Test
   public void testShouldTraverse_WithPrototypePropertyAssignment_ReturnsFalse() {
      AbstractCompiler compiler = new AbstractCompiler();
      CheckGlobalThis check = new CheckGlobalThis(compiler, CheckLevel.WARNING);
      NodeTraversal t = new NodeTraversal(compiler, check);
      Node parent = new Node(Token.ASSIGN);
      Node n = new Node(Token.GETPROP);
      n.addChildToBack(Node.newString(Token.NAME, "prototype"));
      parent.addChildToBack(n);
      boolean result = check.shouldTraverse(t, n, parent);
      Assert.assertFalse(result);
   }

   @Test
   public void testShouldTraverse_WithNestedAssignments_ReturnsFalse() {
      AbstractCompiler compiler = new AbstractCompiler();
      CheckGlobalThis check = new CheckGlobalThis(compiler, CheckLevel.WARNING);
      NodeTraversal t = new NodeTraversal(compiler, check);
      Node parent = new Node(Token.ASSIGN);
      Node n = new Node(Token.GETPROP);
      n.addChildToBack(Node.newString(Token.NAME, "prototype"));
      parent.addChildToBack(n);
      Node grandParent = new Node(Token.ASSIGN);
      grandParent.addChildToBack(parent);
      boolean result = check.shouldTraverse(t, n, grandParent);
      Assert.assertFalse(result);
   }

   @Test
   public void testShouldTraverse_WithNonGetParent_ReturnsTrue() {
      AbstractCompiler compiler = new AbstractCompiler();
      CheckGlobalThis check = new CheckGlobalThis(compiler, CheckLevel.WARNING);
      NodeTraversal t = new NodeTraversal(compiler, check);
      Node n = new Node(Token.FUNCTION);
      n.putProp(Node.JSDOC_INFO_PROP, JSDocInfo.builder(false).markAsConstructor().build());
      Node parent = new Node(Token.SCRIPT);
      boolean result = check.shouldTraverse(t, n, parent);
      Assert.assertTrue(result);
   }

   @Test
   public void testVisit_WithThisNodeAndParent_ReturnsTrue() {
      AbstractCompiler compiler = new AbstractCompiler();
      CheckGlobalThis check = new CheckGlobalThis(compiler, CheckLevel.WARNING);
      NodeTraversal t = new NodeTraversal(compiler, check);
      Node n = new Node(Token.THIS);
      Node parent = new Node(Token.GETPROP);
      boolean result = check.shouldReportThis(n, parent);
      Assert.assertTrue(result);
   }

   @Test
   public void testVisit_WithThisNodeAndAssignLhsChildNull_ReturnsTrue() {
      AbstractCompiler compiler = new AbstractCompiler();
      CheckGlobalThis check = new CheckGlobalThis(compiler, CheckLevel.WARNING);
      NodeTraversal t = new NodeTraversal(compiler, check);
      Node n = new Node(Token.THIS);
      check.visit(t, n, null);
      Assert.assertEquals(null, check.assignLhsChild);
   }

   @Test
   public void testVisit_WithThisNodeAndAssignLhsChildNotNull_ReturnsTrue() {
      AbstractCompiler compiler = new AbstractCompiler();
      CheckGlobalThis check = new CheckGlobalThis(compiler, CheckLevel.WARNING);
      NodeTraversal t = new NodeTraversal(compiler, check);
      Node n = new Node(Token.THIS);
      check.assignLhsChild = new Node(Token.NAME);
      check.visit(t, n, null);
      Assert.assertEquals(null, check.assignLhsChild);
   }

   @Test
   public void testGetFunctionJsDocInfo_WithFunctionNodeWithoutJsDoc_ReturnsNull() {
      Node n = new Node(Token.FUNCTION);
      CheckGlobalThis check = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
      JSDocInfo result = check.getFunctionJsDocInfo(n);
      Assert.assertNull(result);
   }

   @Test
   public void testGetFunctionJsDocInfo_WithParentNameNode_ReturnsParentJsDocInfo() {
      Node n = new Node(Token.FUNCTION);
      Node parent = new Node(Token.NAME);
      parent.putProp(Node.JSDOC_INFO_PROP, JSDocInfo.builder(false).build());
      n.setParent(parent);
      CheckGlobalThis check = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
      JSDocInfo result = check.getFunctionJsDocInfo(n);
      Assert.assertNotNull(result);
   }

   @Test
   public void testGetFunctionJsDocInfo_WithParentAssignNode_ReturnsParentJsDocInfo() {
      Node n = new Node(Token.FUNCTION);
      Node parent = new Node(Token.ASSIGN);
      parent.putProp(Node.JSDOC_INFO_PROP, JSDocInfo.builder(false).build());
      n.setParent(parent);
      CheckGlobalThis check = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
      JSDocInfo result = check.getFunctionJsDocInfo(n);
      Assert.assertNotNull(result);
   }

   @Test
   public void testGetFunctionJsDocInfo_WithGrampsVarNode_ReturnsGrampsJsDocInfo() {
      Node n = new Node(Token.FUNCTION);
      Node parent = new Node(Token.NAME);
      Node gramps = new Node(Token.VAR);
      gramps.putProp(Node.JSDOC_INFO_PROP, JSDocInfo.builder(false).build());
      parent.setParent(gramps);
      n.setParent(parent);
      CheckGlobalThis check = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
      JSDocInfo result = check.getFunctionJsDocInfo(n);
      Assert.assertNotNull(result);
   }
}
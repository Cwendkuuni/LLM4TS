import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class RemoveConstantExpressionsTest {

   private RemoveConstantExpressions removeConstantExpressions;

   @Before
   public void setUp() {
      removeConstantExpressions = new RemoveConstantExpressions(new AbstractCompiler());
   }

   @Test
   public void testProcess_NullInputs_NoExceptionsThrown() {
      removeConstantExpressions.process(null, null);
   }

   @Test
   public void testRemoveConstantRValuesCallback_Visit_ExprResultNode_NoSideEffect() {
      RemoveConstantExpressions.RemoveConstantRValuesCallback callback = 
         removeConstantExpressions.new RemoveConstantRValuesCallback();
      
      Node parent = new Node(Token.EXPR_RESULT);
      Node node = new Node(Token.STRING, "Hello");
      parent.addChildToFront(node);
      
      callback.visit(null, node, parent);
      
      assertEquals(node.getType(), Token.STRING);
   }

   @Test
   public void testRemoveConstantRValuesCallback_Visit_ExprResultNode_HasSideEffect() {
      RemoveConstantExpressions.RemoveConstantRValuesCallback callback = 
         removeConstantExpressions.new RemoveConstantRValuesCallback();
      
      Node parent = new Node(Token.EXPR_RESULT);
      Node node = new Node(Token.GETPROP);
      parent.addChildToFront(node);
      
      callback.visit(null, node, parent);
      
      assertNotEquals(node.getType(), Token.GETPROP);
   }

   @Test
   public void testRemoveConstantRValuesCallback_TrySimplify_NodeNotExprResult_NoChanges() {
      RemoveConstantExpressions.RemoveConstantRValuesCallback callback = 
         removeConstantExpressions.new RemoveConstantRValuesCallback();
      
      Node parent = new Node(Token.STRING, "Hello");
      Node node = new Node(Token.STRING, "World");
      
      callback.trySimplify(parent, node);
      
      assertEquals(parent.getType(), Token.STRING);
      assertEquals(node.getType(), Token.STRING);
   }

   @Test
   public void testRemoveConstantRValuesCallback_TrySimplify_ExprResultNode_NoSideEffect_NoChanges() {
      RemoveConstantExpressions.RemoveConstantRValuesCallback callback = 
         removeConstantExpressions.new RemoveConstantRValuesCallback();
      
      Node parent = new Node(Token.EXPR_RESULT);
      Node node = new Node(Token.STRING, "Hello");
      parent.addChildToFront(node);
      
      callback.trySimplify(parent, node);
      
      assertEquals(parent.getType(), Token.EXPR_RESULT);
      assertEquals(node.getType(), Token.STRING);
   }

   @Test
   public void testRemoveConstantRValuesCallback_TrySimplify_ExprResultNode_HasSideEffect_ReplacedWithSideEffectNodes() {
      RemoveConstantExpressions.RemoveConstantRValuesCallback callback = 
         removeConstantExpressions.new RemoveConstantRValuesCallback();
      
      Node parent = new Node(Token.EXPR_RESULT);
      Node node = new Node(Token.GETPROP);
      parent.addChildToFront(node);
      
      callback.trySimplify(parent, node);
      
      assertEquals(parent.getType(), Token.EXPR_RESULT);
      assertNotEquals(node.getType(), Token.GETPROP);
   }

   @Test
   public void testRemoveConstantRValuesCallback_GetSideEffectNodes_ExtractsSideEffectNodes() {
      RemoveConstantExpressions.RemoveConstantRValuesCallback callback = 
         removeConstantExpressions.new RemoveConstantRValuesCallback();
      
      Node node = new Node(Token.ADD);
      Node left = new Node(Token.STRING, "Hello");
      Node right = new Node(Token.STRING, "World");
      node.addChildToFront(left);
      node.addChildToFront(right);
      
      List<Node> sideEffectNodes = callback.getSideEffectNodes(node);
      
      assertEquals(sideEffectNodes.size(), 2);
   }

   @Test
   public void testReportCodeHasChangedListener_NodeRemoved_ChangedFlagSet() {
      RemoveConstantExpressions.ReportCodeHasChangedListener listener = 
         removeConstantExpressions.new ReportCodeHasChangedListener(new RemoveConstantExpressions.Result());
      
      listener.nodeRemoved(new Node(Token.STRING, "Hello"));
      
      assertEquals(listener.result.changed, true);
   }

}
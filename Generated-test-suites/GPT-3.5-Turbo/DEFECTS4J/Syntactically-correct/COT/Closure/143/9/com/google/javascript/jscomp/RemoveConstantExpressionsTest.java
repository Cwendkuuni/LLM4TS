import org.junit.Assert;
import org.junit.Test;

public class RemoveConstantExpressionsTest {

  @Test
  public void testRemoveConstantRValuesCallback_trySimplify_ExprResultType() {
    RemoveConstantExpressions.RemoveConstantRValuesCallback callback = new RemoveConstantExpressions.RemoveConstantRValuesCallback();
    Node parent = new Node(Token.EXPR_RESULT);
    Node node = new Node(Token.ADD);

    callback.trySimplify(parent, node);

    //Assert method functionality here
  }

  @Test
  public void testRemoveConstantRValuesCallback_trySimplify_SubexpressionsWithSideEffects() {
    RemoveConstantExpressions.RemoveConstantRValuesCallback callback = new RemoveConstantExpressions.RemoveConstantRValuesCallback();
    Node parent = new Node(Token.EXPR_RESULT);
    Node exprBody = new Node(Token.ADD);

    callback.trySimplify(parent, exprBody);

    //Assert method functionality here
  }

  @Test
  public void testRemoveConstantRValuesCallback_trySimplify_SubexpressionsWithoutSideEffects() {
    RemoveConstantExpressions.RemoveConstantRValuesCallback callback = new RemoveConstantExpressions.RemoveConstantRValuesCallback();
    Node parent = new Node(Token.EXPR_RESULT);
    Node exprBody = new Node(Token.NAME);

    callback.trySimplify(parent, exprBody);

    //Assert method functionality here
  }

  @Test
  public void testRemoveConstantRValuesCallback_getSideEffectNodes() {
    RemoveConstantExpressions.RemoveConstantRValuesCallback callback = new RemoveConstantExpressions.RemoveConstantRValuesCallback();
    Node node = new Node(Token.ADD);

    List<Node> sideEffectNodes = callback.getSideEffectNodes(node);

    //Assert method functionality here
  }

  @Test
  public void testRemoveConstantRValuesCallback_getResult() {
    RemoveConstantExpressions.RemoveConstantRValuesCallback callback = new RemoveConstantExpressions.RemoveConstantRValuesCallback();

    Result result = callback.getResult();

    //Assert method functionality here
  }
  
  @Test
  public void testRemoveConstantExpressions_process() {
    RemoveConstantExpressions removeConstantExpressions = new RemoveConstantExpressions(new AbstractCompiler());
    Node externs = new Node(Token.EXTERN);
    Node root = new Node(Token.ROOT);
    
    removeConstantExpressions.process(externs, root);
    
    //Assert method functionality here
  }
}
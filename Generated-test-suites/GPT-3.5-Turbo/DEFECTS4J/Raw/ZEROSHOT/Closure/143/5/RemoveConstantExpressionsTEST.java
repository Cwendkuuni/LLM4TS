import static org.junit.Assert.*;
import org.junit.Test;

public class RemoveConstantExpressionsTest {
  
  @Test
  public void testTrySimplify_noSubexpressions() {
    RemoveConstantExpressions.RemoveConstantRValuesCallback callback = 
        new RemoveConstantExpressions.RemoveConstantRValuesCallback();
    Node parent = new Node(Token.ADD, new Node(Token.NUMBER, "1"), new Node(Token.NUMBER, "2"));
    Node node = new Node(Token.EXPR_RESULT, parent);
    
    callback.trySimplify(parent, node);
    
    List<Node> actual = callback.getSideEffectNodes(node);
    List<Node> expected = new ArrayList<>();
    
    assertEquals(expected, actual);
  }
  
  @Test
  public void testTrySimplify_withSubexpressions() {
    RemoveConstantExpressions.RemoveConstantRValuesCallback callback = 
        new RemoveConstantExpressions.RemoveConstantRValuesCallback();
    Node subexpression1 = new Node(Token.STRING, "hello");
    Node subexpression2 = new Node(Token.ADD, new Node(Token.NUMBER, "2"), new Node(Token.NUMBER, "3"));
    Node parent = new Node(Token.ADD, subexpression1, subexpression2);
    Node node = new Node(Token.EXPR_RESULT, parent);
    
    callback.trySimplify(parent, node);
    
    List<Node> actual = callback.getSideEffectNodes(node);
    List<Node> expected = new ArrayList<>();
    expected.add(new Node(Token.EXPR_RESULT, subexpression1));
    expected.add(new Node(Token.EXPR_RESULT, subexpression2));
    
    assertEquals(expected, actual);
  }
  
  @Test
  public void testTrySimplify_emptyParent() {
    RemoveConstantExpressions.RemoveConstantRValuesCallback callback = 
        new RemoveConstantExpressions.RemoveConstantRValuesCallback();
    Node parent = new Node(Token.EMPTY);
    Node node = new Node(Token.EXPR_RESULT, new Node(Token.STRING, "hello"));
    
    callback.trySimplify(parent, node);
    
    List<Node> actual = callback.getSideEffectNodes(node);
    List<Node> expected = new ArrayList<>();
    expected.add(new Node(Token.EXPR_RESULT, new Node(Token.STRING, "hello")));
    
    assertEquals(expected, actual);
  }
  
  @Test
  public void testGetSideEffectNodes_noSideEffects() {
    RemoveConstantExpressions.RemoveConstantRValuesCallback callback = 
        new RemoveConstantExpressions.RemoveConstantRValuesCallback();
    Node node = new Node(Token.STRING, "hello");
    
    List<Node> actual = callback.getSideEffectNodes(node);
    List<Node> expected = new ArrayList<>();
    
    assertEquals(expected, actual);
  }
  
  @Test
  public void testGetSideEffectNodes_withSideEffects() {
    RemoveConstantExpressions.RemoveConstantRValuesCallback callback = 
        new RemoveConstantExpressions.RemoveConstantRValuesCallback();
    Node subexpression1 = new Node(Token.CALL, new Node(Token.NAME, "foo"));
    Node subexpression2 = new Node(Token.CALL, new Node(Token.NAME, "bar"));
    Node node = new Node(Token.ADD, subexpression1, subexpression2);
    
    List<Node> actual = callback.getSideEffectNodes(node);
    List<Node> expected = new ArrayList<>();
    expected.add(new Node(Token.EXPR_RESULT, subexpression1));
    expected.add(new Node(Token.EXPR_RESULT, subexpression2));
    
    assertEquals(expected, actual);
  }
  
  @Test
  public void testNodeRemoved() {
    RemoveConstantExpressions.ReportCodeHasChangedListener listener = 
        new RemoveConstantExpressions.ReportCodeHasChangedListener(new RemoveConstantExpressions.Result());
    Node node = new Node(Token.EXPR_RESULT, new Node(Token.STRING, "hello"));
    
    listener.nodeRemoved(node);
    
    assertTrue(listener.result.changed);
  }
}
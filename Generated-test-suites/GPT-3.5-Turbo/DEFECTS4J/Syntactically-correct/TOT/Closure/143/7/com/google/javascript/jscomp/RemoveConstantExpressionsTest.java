import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class RemoveConstantExpressionsTest {

  @Test
  public void testProcess() {
    // typical use cases
    // case 1: expression with side effects
    AbstractCompiler compiler1 = new AbstractCompiler();
    RemoveConstantExpressions rce1 = new RemoveConstantExpressions(compiler1);
    Node externs1 = null;
    Node root1 = new Node(Token.SCRIPT);
    Node exprNode1 = new Node(Token.EXPR_RESULT);
    Node expression1 = new Node(Token.ADD);
    expression1.addChildToFront(new Node(Token.NUMBER));
    expression1.addChildToBack(new Node(Token.EXPR_RESULT));
    exprNode1.addChildToFront(expression1);
    root1.addChildToFront(exprNode1);
    rce1.process(externs1, root1);
    // assert something here

    // case 2: expression without side effects
    AbstractCompiler compiler2 = new AbstractCompiler();
    RemoveConstantExpressions rce2 = new RemoveConstantExpressions(compiler2);
    Node externs2 = null;
    Node root2 = new Node(Token.SCRIPT);
    Node exprNode2 = new Node(Token.EXPR_RESULT);
    Node expression2 = new Node(Token.ADD);
    expression2.addChildToFront(new Node(Token.NUMBER));
    exprNode2.addChildToFront(expression2);
    root2.addChildToFront(exprNode2);
    rce2.process(externs2, root2);
    // assert something here

    // edge cases
    // case 1: empty externs and root
    AbstractCompiler compiler3 = new AbstractCompiler();
    RemoveConstantExpressions rce3 = new RemoveConstantExpressions(compiler3);
    Node externs3 = null;
    Node root3 = new Node(Token.SCRIPT);
    rce3.process(externs3, root3);
    // assert something here

    // case 2: large input with multiple expressions
    AbstractCompiler compiler4 = new AbstractCompiler();
    RemoveConstantExpressions rce4 = new RemoveConstantExpressions(compiler4);
    Node externs4 = null;
    Node root4 = new Node(Token.SCRIPT);
    for (int i = 0; i < 1000; i++) {
      Node exprNode4 = new Node(Token.EXPR_RESULT);
      Node expression4 = new Node(Token.ADD);
      expression4.addChildToFront(new Node(Token.NUMBER));
      exprNode4.addChildToFront(expression4);
      root4.addChildToFront(exprNode4);
    }
    rce4.process(externs4, root4);
    // assert something here

    // error scenarios
    // case 1: null compiler
    RemoveConstantExpressions rce5 = new RemoveConstantExpressions(null);
    Node externs5 = null;
    Node root5 = new Node(Token.SCRIPT);
    rce5.process(externs5, root5);
    // assert something here

  }

}
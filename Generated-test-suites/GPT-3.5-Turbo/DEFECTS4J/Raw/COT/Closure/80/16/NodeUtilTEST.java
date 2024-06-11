import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    // test case 1: n represents a FOR-IN loop
    Node n1 = new Node(Token.FOR, new Node(Token.BLOCK), new Node(Token.EMPTY), new Node(Token.EMPTY));
    assert(NodeUtil.isForIn(n1));
    
    // test case 2: n does not represent a FOR-IN loop
    Node n2 = new Node(Token.IF, new Node(Token.BLOCK), new Node(Token.EMPTY));
    assert(!NodeUtil.isForIn(n2));
  }

  @Test
  public void testIsStatement() {
    // test case 1: n is used as a statement
    Node n1 = new Node(Token.SCRIPT, new Node(Token.BLOCK));
    assert(NodeUtil.isStatement(n1));

    // test case 2: n is not used as a statement
    Node n2 = new Node(Token.IF, new Node(Token.BLOCK));
    assert(!NodeUtil.isStatement(n2));
  }

  @Test
  public void testIsFunctionDeclaration() {
    // test case 1: n is a function declaration
    Node n1 = new Node(Token.FUNCTION);
    Node parent1 = new Node(Token.SCRIPT, n1);
    n1.setParent(parent1);
    assert(NodeUtil.isFunctionDeclaration(n1));

    // test case 2: n is not a function declaration
    Node n2 = new Node(Token.VAR);
    Node parent2 = new Node(Token.SCRIPT, n2);
    n2.setParent(parent2);
    assert(!NodeUtil.isFunctionDeclaration(n2));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    // test case 1: n is a hoisted function declaration
    Node n1 = new Node(Token.FUNCTION);
    Node parent1 = new Node(Token.SCRIPT, n1);
    n1.setParent(parent1);
    assert(NodeUtil.isHoistedFunctionDeclaration(n1));

    // test case 2: n is not a hoisted function declaration
    Node n2 = new Node(Token.FUNCTION);
    Node parent2 = new Node(Token.FUNCTION, n2);
    n2.setParent(parent2);
    assert(!NodeUtil.isHoistedFunctionDeclaration(n2));
  }
}
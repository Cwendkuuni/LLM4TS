import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RemoveConstantExpressionsTest {

  private RemoveConstantExpressions pass;

  @Before
  public void setUp() {
    pass = new RemoveConstantExpressions(new AbstractCompiler());
  }

  @Test
  public void testProcess_WithExpressionStatements_ReturnsExpectedResult() {
    // Arrange
    Node externs = null;
    Node root = createTestAST();
    
    // Act
    pass.process(externs, root);
    
    // Assert
    // Check if the expression statements have been simplified correctly
    
    // Predefined AST structure:
    //                 ROOT
    //              /   |    \
    //     VAR_DECL  EXPR_RESULT  VAR_DECL
    //      /   \        |       /   \
    //   NAME   EXPR   CALL   NAME    EXPR
    //           |           |            |
    //          ADD          |           ADD
    //     /     |      \      |      /     |
    //   NUMBER   CALL   CALL  NUMBER   CALL
    //            |        |               |
    //           NUMBER   NAME           NAME
    
    // Expected AST structure:
    //                 ROOT
    //              /   |    \
    //     VAR_DECL  EXPR_RESULT  VAR_DECL
    //      /   \        |       /   \
    //   NAME   EXPR   NAME    NAME    NAME
    //                |                 |
    //              EXPR_RESULT    EXPR_RESULT
    //                |                 |
    //               NUMBER            NUMBER
    
    Node expectedRoot = createExpectedAST();
    assertTrue(compareAST(root, expectedRoot));
  }

  // Helper method to create a mock AST for testing
  private Node createTestAST() {
    Node root = new Node(Token.ROOT);
    Node varDecl1 = new Node(Token.VAR);
    Node varName1 = new Node(Token.NAME, "var1");
    Node assign1 = new Node(Token.ASSIGN);
    Node exprResult1 = new Node(Token.EXPR_RESULT, assign1);
    Node add1 = new Node(Token.ADD);
    Node number1 = new Node(Token.NUMBER, "1");
    Node call1 = new Node(Token.EXPR_RESULT, new Node(Token.CALL));
    varDecl1.addChildToBack(varName1);
    add1.addChildrenToBack(number1);
    add1.addChildrenToBack(call1);
    assign1.addChildToBack(varName1);
    assign1.addChildToBack(add1);
    exprResult1.addChildToBack(assign1);

    Node exprResult2 = new Node(Token.EXPR_RESULT, new Node(Token.CALL));

    Node varDecl2 = new Node(Token.VAR);
    Node varName2 = new Node(Token.NAME, "var2");
    Node assign2 = new Node(Token.ASSIGN);
    Node exprResult3 = new Node(Token.EXPR_RESULT, assign2);
    Node add2 = new Node(Token.ADD);
    Node number2 = new Node(Token.NUMBER, "2");
    Node call2 = new Node(Token.EXPR_RESULT, new Node(Token.CALL));
    varDecl2.addChildToBack(varName2);
    add2.addChildrenToBack(number2);
    add2.addChildrenToBack(call2);
    assign2.addChildToBack(varName2);
    assign2.addChildToBack(add2);
    exprResult3.addChildToBack(assign2);

    root.addChildrenToBack(varDecl1);
    root.addChildrenToBack(exprResult1);
    root.addChildrenToBack(exprResult2);
    root.addChildrenToBack(varDecl2);
    root.addChildrenToBack(exprResult3);

    return root;
  }
  
  // Helper method to create the expected AST for testing
  private Node createExpectedAST() {
    Node root = new Node(Token.ROOT);
    Node varDecl1 = new Node(Token.VAR);
    Node varName1 = new Node(Token.NAME, "var1");
    Node assign1 = new Node(Token.ASSIGN);
    Node exprResult1 = new Node(Token.EXPR_RESULT, assign1);
    Node name1 = new Node(Token.NAME, "var1");
    Node exprResult2 = new Node(Token.EXPR_RESULT, new Node(Token.NUMBER, "1"));
    varDecl1.addChildToBack(varName1);
    assign1.addChildToBack(varName1);
    assign1.addChildToBack(name1);
    exprResult1.addChildToBack(assign1);
    
    Node exprResult3 = new Node(Token.EXPR_RESULT, new Node(Token.CALL));
    
    Node varDecl2 = new Node(Token.VAR);
    Node varName2 = new Node(Token.NAME, "var2");
    Node assign2 = new Node(Token.ASSIGN);
    Node exprResult4 = new Node(Token.EXPR_RESULT, assign2);
    Node name2 = new Node(Token.NAME, "var2");
    Node exprResult5 = new Node(Token.EXPR_RESULT, new Node(Token.NUMBER, "2"));
    varDecl2.addChildToBack(varName2);
    assign2.addChildToBack(varName2);
    assign2.addChildToBack(name2);
    exprResult4.addChildToBack(assign2);
    
    root.addChildrenToBack(varDecl1);
    root.addChildrenToBack(exprResult1);
    root.addChildrenToBack(exprResult2);
    root.addChildrenToBack(varDecl2);
    root.addChildrenToBack(exprResult4);
    
    return root;
  }

  // Helper method to compare two ASTs for equality
  private boolean compareAST(Node actual, Node expected) {
    // Compare node types
    if (actual.getType() != expected.getType()) {
      return false;
    }
    // Compare node values
    if (!actual.getString().equals(expected.getString())) {
      return false;
    }
    // Compare child count
    if (actual.getChildCount() != expected.getChildCount()) {
      return false;
    }
    // Compare children recursively
    for (int i = 0; i < actual.getChildCount(); i++) {
      Node actualChild = actual.getChildAtIndex(i);
      Node expectedChild = expected.getChildAtIndex(i);
      if (!compareAST(actualChild, expectedChild)) {
        return false;
      }
    }
    return true;
  }
}
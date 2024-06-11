import static org.junit.Assert.*;
import org.junit.Test;

public class RemoveConstantExpressionsTest {

  @Test
  public void testProcess_basicInput() {
    // Create a basic input AST
    Node externs = new Node(Token.EXTERN, new Node(Token.NAME, "externs"));
    Node root = new Node(Token.ROOT, new Node(Token.NAME, "root"));
    root.addChildToBack(new Node(Token.EXPR_RESULT, 
        new Node(Token.ADD,
            new Node(Token.ADD,
                new Node(Token.NUMBER, 1),
                new Node(Token.NAME, "foo")
            ),
            new Node(Token.NAME, "bar")
        )
    ));

    // Set up the Compiler and Pass
    Compiler compiler = new Compiler(new CompilerOptions());
    RemoveConstantExpressions pass = new RemoveConstantExpressions(compiler);

    // Run the process method
    pass.process(externs, root);

    // Verify the expected result
    // foo();bar();
    Node expectedAST = new Node(Token.ROOT, new Node(Token.NAME, "root"));
    expectedAST.addChildToBack(new Node(Token.EXPR_RESULT, new Node(Token.NAME, "foo")));
    expectedAST.addChildToBack(new Node(Token.EXPR_RESULT, new Node(Token.NAME, "bar")));

    assertEquals(expectedAST.toStringTree(), root.toStringTree());
  }

  // TODO: Generate additional test cases for edge cases and exceptions

}
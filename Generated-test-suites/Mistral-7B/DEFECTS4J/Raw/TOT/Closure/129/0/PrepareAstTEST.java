import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.PrepareAst;
import org.junit.Test;
import static org.junit.Assert.*;

public class PrepareAstTest {

  @Test
  public void testNormalizeNodeTypes() {
    AbstractCompiler compiler = new AbstractCompiler();
    PrepareAst prepareAst = new PrepareAst(compiler, false);

    // Create a simple Node tree for testing
    Node root = IR.block()
        .add(IR.exprStatement(IR.assignment(
            IR.name("x"), IR.numberLiteral(1))))
        .add(IR.returnStatement(IR.stringLiteral("Hello World")))
        .srcref(new Object(){});

    prepareAst.process(null, root);

    // Check that the root node type is now EXPR_BLOCK
    assertEquals("expected root node type to be EXPR_BLOCK",
        NodeType.EXPR_BLOCK, root.getType());

    // Check that the assignment node type is now EXPR_ASSIGN
    Node assignmentNode = root.getFirstChild();
    assertEquals("expected assignment node type to be EXPR_ASSIGN",
        NodeType.EXPR_ASSIGN, assignmentNode.getType());

    // Check that the return statement node type is now STMT_RETURN
    Node returnStatementNode = assignmentNode.getNext();
    assertEquals("expected return statement node type to be STMT_RETURN",
        NodeType.STMT_RETURN, returnStatementNode.getType());
  }

  @Test
  public void testNormalizeBlocks() {
    AbstractCompiler compiler = new AbstractCompiler();
    PrepareAst prepareAst = new PrepareAst(compiler, false);

    // Create a Node tree with a missing code block in an if statement
    Node root = IR.ifStatement(
        IR.booleanLiteral(false),
        IR.block().add(IR.printStatement("Statement inside if block")))
        .add(IR.printStatement("Statement outside if block"))
        .srcref(new Object(){});

    prepareAst.process(null, root);

    // Check that an empty code block has been added to the if statement
    Node ifStatementNode = root.getFirstChild();
    Node ifBlockNode = ifStatementNode.getFirstChild();
    assertTrue("expected if statement to have a code block",
        ifBlockNode.isBlock());
    assertFalse("expected if block to have children", ifBlockNode.hasChildren());

    // Check that the print statement outside the if block has not been affected
    Node printStatementNode = ifBlockNode.getNext();
    assertEquals("expected print statement node type to be PRINT_STATEMENT",
        NodeType.PRINT_STATEMENT, printStatementNode.getType());
  }

  @Test
  public void testPrepareAnnotations() {
    AbstractCompiler compiler = new AbstractCompiler();
    PrepareAst prepareAst = new PrepareAst(compiler, false);

    // Create a Node tree with a function literal and JSDoc annotations
    Node root = IR.block()
        .add(IR.expressionStatement(
            IR.assignment(
                IR.name("x"),
                IR.functionLiteral(
                    IR.block().add(IR.printStatement("Hello World")),
                    new Node[]{}))))
        .add(IR.returnStatement(IR.numberLiteral(1)))
        .srcref(new Object(){});

    prepareAst.process(null, root);

    // Check that the JSDoc annotations have been copied to the function literal node
    Node functionLiteralNode = root.getFirstChild().getLastChild();
    assertEquals("expected function literal node to have JSDoc info",
        "Hello World", functionLiteralNode.getJSDocInfo().getDescription());
  }

  @Test(expected = AssertionError.class)
  public void testNormalizeNodeTypesViolation() {
    AbstractCompiler compiler = new AbstractCompiler();
    PrepareAst prepareAst = new PrepareAst(compiler, false);

    // Create a Node tree that violates the normalizeNodeTypes constraint
    Node root = IR.block()
        .add(IR.returnStatement(IR.numberLiteral(1)))
        .add(IR.returnStatement(IR.numberLiteral(2)))
        .srcref(new Object(){});

    prepareAst.process(null, root);
  }

  // Add any additional tests as needed

  enum NodeType { EXPR_BLOCK, EXPR_ASSIGN, STMT_RETURN, PRINT_STATEMENT }
}
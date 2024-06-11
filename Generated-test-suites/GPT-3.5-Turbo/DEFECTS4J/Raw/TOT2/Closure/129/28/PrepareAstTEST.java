import static org.junit.Assert.*;
import org.junit.Test;

public class PrepareAstTest {

  @Test
  public void testProcess() {
    // Create a new compiler
    AbstractCompiler compiler = new AbstractCompiler();
    
    // Create a PrepareAst object with checkOnly set to false
    PrepareAst prepareAst = new PrepareAst(compiler, false);
    
    // Create a sample externs Node
    Node externs = new Node(Token.EXTERN, new Node(Token.VAR));
    
    // Create a sample root Node
    Node root = new Node(Token.ROOT, new Node(Token.BLOCK));
    
    // Test the process method with sample externs and root Nodes
    prepareAst.process(externs, root);
    
    // Assert that no exceptions are thrown
    assertTrue(true);
  }

  @Test
  public void testNormalizeNodeTypes() {
    // Create a new Node with type EXPR_VOID
    Node node = new Node(Token.EXPR_VOID, new Node(Token.NAME, "test"));
    
    // Create a new PrepareAst object
    PrepareAst prepareAst = new PrepareAst(new AbstractCompiler());
    
    // Test the normalizeNodeTypes method with the sample Node
    prepareAst.normalizeNodeTypes(node);
    
    // Assert that the node type is changed to EXPR_RESULT
    assertEquals(Token.EXPR_RESULT, node.getType());
  }

  @Test
  public void testNormalizeBlocks() {
    // Create a new Node with type IF
    Node node = new Node(Token.IF, new Node(Token.TRUE));
    
    // Create a new PrepareAst object
    PrepareAst prepareAst = new PrepareAst(new AbstractCompiler());
    
    // Test the normalizeBlocks method with the sample Node
    prepareAst.normalizeBlocks(node);
    
    // Assert that a block is added to the IF Node
    assertTrue(node.getFirstChild().isBlock());
  }

  @Test
  public void testPrepareAnnotations() {
    // Create a new PrepareAst object
    PrepareAst prepareAst = new PrepareAst(new AbstractCompiler());
    
    // Create a sample function Node
    Node functionNode = new Node(Token.FUNCTION, new Node(Token.NAME, "test"));
    
    // Create a sample parent Node
    Node parentNode = new Node(Token.ASSIGN, new Node(Token.STRING, "test"), functionNode);
    
    // Test the annotateDispatchers method with the sample function Node and parent Node
    prepareAst.new PrepareAnnotations().annotateDispatchers(functionNode, parentNode);
    
    // Assert that the function Node is marked as a dispatcher
    assertTrue(functionNode.getBooleanProp(Node.IS_DISPATCHER));
  }

}
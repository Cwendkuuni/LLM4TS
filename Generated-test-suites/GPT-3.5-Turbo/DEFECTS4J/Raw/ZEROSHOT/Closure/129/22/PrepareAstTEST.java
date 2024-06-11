import org.junit.Test;
import static org.junit.Assert.*;

public class PrepareAstTest {
  
  @Test
  public void testProcess() {
    PrepareAst prepareAst = new PrepareAst(new AbstractCompiler());
    Node externs = new Node(Token.EXTERN);
    Node root = new Node(Token.ROOT);
    prepareAst.process(externs, root);
  }
  
  @Test
  public void testNormalizeNodeTypes() {
    PrepareAst prepareAst = new PrepareAst(new AbstractCompiler(), true);
    Node node = new Node(Token.ROOT);
    prepareAst.normalizeNodeTypes(node);
  }
  
  @Test
  public void testNormalizeBlocks() {
    PrepareAst prepareAst = new PrepareAst(new AbstractCompiler(), true);
    Node node = new Node(Token.ROOT);
    prepareAst.normalizeBlocks(node);
  }
  
  @Test
  public void testPrepareAnnotations() {
    PrepareAst.PrepareAnnotations prepareAnnotations = new PrepareAst.PrepareAnnotations();
    NodeTraversal traversal = new NodeTraversal(new AbstractCompiler(), prepareAnnotations);
    Node node = new Node(Token.ROOT);
    Node parent = new Node(Token.FUNCTION);
    traversal.visit(node, parent);
  }
}
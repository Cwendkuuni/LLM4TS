import org.junit.*;
import static org.junit.Assert.*;

public class VarCheckTest {

  @Test
  public void testProcess() {
    // create a compiler
    AbstractCompiler compiler = new AbstractCompiler();

    // create a VarCheck instance
    VarCheck varCheck = new VarCheck(compiler);

    // create input nodes
    Node externs = new Node(Token.ROOT);
    Node root = new Node(Token.ROOT);

    // process the nodes
    varCheck.process(externs, root);
  }

  @Test
  public void testVisit() {
    // create a compiler
    AbstractCompiler compiler = new AbstractCompiler();

    // create a VarCheck instance
    VarCheck varCheck = new VarCheck(compiler);

    // create a NodeTraversal instance
    NodeTraversal t = new NodeTraversal(compiler, null);

    // create nodes
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.ROOT);

    // test visit method
    varCheck.visit(t, n, parent);
  }
}
import static org.junit.Assert.*;
import org.junit.Test;

public class ScopedAliasesTest {

  @Test
  public void testProcess() {
    // Create a new compiler
    Compiler compiler = new Compiler();
    
    // Create a new ScopedAliases object
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    
    // Create a new externs node
    Node externs = new Node(Token.EXTERN_NAME);
    
    // Create a new root node
    Node root = new Node(Token.ROOT);
    
    // Call the process method with the externs and root nodes
    scopedAliases.process(externs, root);
  }

  @Test
  public void testHotSwapScript() {
    // Create a new compiler
    Compiler compiler = new Compiler();
    
    // Create a new ScopedAliases object
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    
    // Create a new root node
    Node root = new Node(Token.ROOT);
    
    // Create a new originalRoot node
    Node originalRoot = new Node(Token.ROOT);
    
    // Call the hotSwapScript method with the root and originalRoot nodes
    scopedAliases.hotSwapScript(root, originalRoot);
  }

  @Test
  public void testEnterScope() {
    // Create a new compiler
    Compiler compiler = new Compiler();
    
    // Create a new ScopedAliases object
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    
    // Create a new traversal object
    ScopedAliases.Traversal traversal = scopedAliases.new Traversal();
    
    // Create a new node traversal object
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, traversal);
    
    // Create a new scope node
    Node scope = new Node(Token.SCOPE);
    
    // Call the enterScope method with the nodeTraversal and scope nodes
    traversal.enterScope(nodeTraversal, scope);
  }

  @Test
  public void testExitScope() {
    // Create a new compiler
    Compiler compiler = new Compiler();
    
    // Create a new ScopedAliases object
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    
    // Create a new traversal object
    ScopedAliases.Traversal traversal = scopedAliases.new Traversal();
    
    // Create a new node traversal object
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, traversal);
    
    // Create a new scope node
    Node scope = new Node(Token.SCOPE);
    
    // Call the exitScope method with the nodeTraversal and scope nodes
    traversal.exitScope(nodeTraversal, scope);
  }

  @Test
  public void testShouldTraverse() {
    // Create a new compiler
    Compiler compiler = new Compiler();
    
    // Create a new ScopedAliases object
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    
    // Create a new traversal object
    ScopedAliases.Traversal traversal = scopedAliases.new Traversal();
    
    // Create a new node traversal object
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, traversal);
    
    // Create a new node
    Node node = new Node(Token.VAR);
    
    // Call the shouldTraverse method with the nodeTraversal, node, and null
    traversal.shouldTraverse(nodeTraversal, node, null);
  }

  @Test
  public void testVisit() {
    // Create a new compiler
    Compiler compiler = new Compiler();
    
    // Create a new ScopedAliases object
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    
    // Create a new traversal object
    ScopedAliases.Traversal traversal = scopedAliases.new Traversal();
    
    // Create a new node traversal object
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, traversal);
    
    // Create a new node
    Node node = new Node(Token.VAR);
    
    // Create a new parent node
    Node parent = new Node(Token.EXPR_RESULT);
    
    // Call the visit method with the nodeTraversal, node, and parent nodes
    traversal.visit(nodeTraversal, node, parent);
  }

}
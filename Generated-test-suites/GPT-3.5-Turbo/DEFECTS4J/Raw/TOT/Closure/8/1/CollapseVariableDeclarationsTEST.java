import static org.junit.Assert.*;
import org.junit.Test;

public class CollapseVariableDeclarationsTest {

  @Test
  public void testProcess() {
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(new AbstractCompiler());
    Node externs = new Node(Token.EXTERN, new Node(Token.NAME, "externs"), new Node(Token.NAME, "root"));
    Node root = new Node(Token.EXTERN, new Node(Token.NAME, "externs"), new Node(Token.NAME, "root"));
    pass.process(externs, root);
    // Test if the collapses list is empty
    assertEquals(0, pass.collapses.size());
  }

  @Test
  public void testGatherCollapses() {
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(new AbstractCompiler());
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.NAME, "test");
    Scope s = new Scope();
    // Test sthat the blacklistedVars set is empty
    assertEquals(0, pass.GatherCollapses.blacklistedVars.size());
    pass.GatherCollapses.blacklistStubVars(t, n);
    // Test if 'test' is added to the set blacklistedVars
    assertEquals(1, pass.GatherCollapses.blacklistedVars.size());
    pass.GatherCollapses.canBeRedeclared(n, s);
    // Test if canBeRedeclared returns false for a non-assignment node
    assertFalse(pass.GatherCollapses.canBeRedeclared(n, s));
  }

  @Test
  public void testApplyCollapses() {
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(new AbstractCompiler());
    CollapseVariableDeclarations.Collapse collapse = new CollapseVariableDeclarations.Collapse(new Node(Token.NAME, "startNode"), new Node(Token.NAME, "endNode"), new Node(Token.NAME, "parent"));
    Node node = new Node(Token.NAME, "node");
    node.addChildToFront(new Node(Token.NAME, "var"));
    collapse.parent.addChildToBack(node);
    pass.applyCollapses();
    // Test if the var node is replaced with a single VAR node containing the children
    assertEquals(Token.VAR, collapse.parent.getFirstChild().getType());
  }

}
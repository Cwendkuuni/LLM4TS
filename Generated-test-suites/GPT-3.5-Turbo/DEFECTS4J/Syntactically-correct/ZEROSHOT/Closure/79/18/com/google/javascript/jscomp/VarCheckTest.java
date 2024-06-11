import static org.junit.Assert.*;
import org.junit.Test;

public class VarCheckTest {

  @Test
  public void testProcess() {
    VarCheck varCheck = new VarCheck(new AbstractCompiler());

    // Test empty externs and root
    Node externs = new Node(Token.ROOT);
    Node root = new Node(Token.ROOT);
    varCheck.process(externs, root);

    // Test externs with variable declarations
    Node externsWithVar = new Node(Token.ROOT);
    externsWithVar.addChildToBack(new Node(Token.VAR, Node.newString(Token.NAME, "x")));
    varCheck.process(externsWithVar, root);

    // Test root with variable declaration
    Node rootWithVar = new Node(Token.ROOT);
    rootWithVar.addChildToBack(new Node(Token.VAR, Node.newString(Token.NAME, "x")));
    varCheck.process(externs, rootWithVar);
  }

  @Test
  public void testVisit() {
    VarCheck varCheck = new VarCheck(new AbstractCompiler());

    // Test visit with variable defined in the same file
    Scope scope = new Scope(null, new Node(Token.ROOT), null);
    scope.declare("x", new Node(Token.NAME), null, null);
    NodeTraversal t = new NodeTraversal(varCheck, new AbstractPostOrderCallback() {
      public void visit(NodeTraversal t, Node n, Node parent) {
        // Do nothing
      }
    });
    Node rootNode = new Node(Token.ROOT);
    rootNode.addChildToBack(new Node(Token.VAR, Node.newString(Token.NAME, "x")));
    t.visit(null, rootNode, null);

    // Test visit with variable not defined in the same file
    t = new NodeTraversal(varCheck, new AbstractPostOrderCallback() {
      public void visit(NodeTraversal t, Node n, Node parent) {
        // Do nothing
      }
    });
    rootNode = new Node(Token.ROOT);
    rootNode.addChildToBack(new Node(Token.VAR, Node.newString(Token.NAME, "y")));
    t.visit(null, rootNode, null);
  }

  @Test
  public void testCreateSynthesizedExternVar() {
    VarCheck varCheck = new VarCheck(new AbstractCompiler());

    // Test create synthesized extern variable
    varCheck.createSynthesizedExternVar("x");
  }

  @Test
  public void testGetSynthesizedExternsInput() {
    VarCheck varCheck = new VarCheck(new AbstractCompiler());

    // Test get synthesized extern input
    assertNotNull(varCheck.getSynthesizedExternsInput());
  }

  @Test
  public void testGetSynthesizedExternsRoot() {
    VarCheck varCheck = new VarCheck(new AbstractCompiler());

    // Test get synthesized extern root
    assertNotNull(varCheck.getSynthesizedExternsRoot());
  }
}
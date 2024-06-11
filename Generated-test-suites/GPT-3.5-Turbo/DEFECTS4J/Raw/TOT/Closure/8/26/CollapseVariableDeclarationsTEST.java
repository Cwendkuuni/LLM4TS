import org.junit.Test;
import static org.junit.Assert.*;

public class CollapseVariableDeclarationsTest {

  @Test
  public void testProcess_NoCollapses() {
    // Create the necessary objects for testing
    AbstractCompiler compiler = new AbstractCompiler();
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    Node externs = new Node(Token.EXTERN);
    Node root = new Node(Token.ROOT);

    // Perform the process
    pass.process(externs, root);

    // Assert that there are no collapses
    assertTrue(pass.collapses.isEmpty());
  }

  @Test
  public void testProcess_CollapsesExist() {
    // Create the necessary objects for testing
    AbstractCompiler compiler = new AbstractCompiler();
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    Node externs = new Node(Token.EXTERN);
    Node root = createCollapseTestNode();

    // Perform the process
    pass.process(externs, root);

    // Assert that there are collapses
    assertFalse(pass.collapses.isEmpty());

    // Assert that the collapses are correct
    CollapseVariableDeclarations.Collapse collapse = pass.collapses.get(0);
    assertEquals(Token.VAR, collapse.startNode.getToken());
    assertEquals(Token.END, collapse.endNode.getToken());
    assertEquals(Token.ROOT, collapse.parent.getToken());
  }

  @Test
  public void testApplyCollapses_NoAssignments() {
    // Create the necessary objects for testing
    AbstractCompiler compiler = new AbstractCompiler();
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    Node externs = new Node(Token.EXTERN);
    Node root = createCollapseTestNode();

    // Perform the process
    pass.process(externs, root);

    // Perform the applyCollapses method
    pass.applyCollapses();

    // Assert that the variable declarations were collapsed as expected
    CollapseVariableDeclarations.Collapse collapse = pass.collapses.get(0);
    Node var = collapse.parent.getFirstChild();
    assertEquals(Token.NAME, var.getToken());
    assertEquals("a", var.getString());
    assertNull(var.getNext());

    // Assert that the parent node was updated correctly
    assertEquals(var, collapse.parent.getFirstChild());
  }

  @Test
  public void testApplyCollapses_WithAssignments() {
    // Create the necessary objects for testing
    AbstractCompiler compiler = new AbstractCompiler();
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    Node externs = new Node(Token.EXTERN);
    Node root = createCollapseTestNode();

    // Add assignments to the collapse test node
    Node assign1 = createAssignmentNode("b", "1");
    Node assign2 = createAssignmentNode("c", "2");
    root.addChildToBack(assign1);
    root.addChildToBack(assign2);

    // Perform the process
    pass.process(externs, root);

    // Perform the applyCollapses method
    pass.applyCollapses();

    // Assert that the variable declarations and assignments were collapsed as expected
    CollapseVariableDeclarations.Collapse collapse = pass.collapses.get(0);
    Node var = collapse.parent.getFirstChild();
    assertEquals(Token.NAME, var.getToken());
    assertEquals("a", var.getString());
    assertEquals(assign2, var.getNext());
    assertEquals(assign1, assign2.getNext());

    // Assert that the parent node was updated correctly
    assertEquals(var, collapse.parent.getFirstChild());
  }

  private Node createCollapseTestNode() {
    Node root = new Node(Token.ROOT);

    // Create the variable declarations
    Node var1 = createVariableNode("a");
    Node var2 = createVariableNode("b");
    Node var3 = createVariableNode("c");

    // Add the variable declarations to the root node
    root.addChildToBack(var1);
    root.addChildToBack(var2);
    root.addChildToBack(var3);

    return root;
  }

  private Node createVariableNode(String varName) {
    Node var = new Node(Token.VAR);
    Node name = createNameNode(varName);
    var.addChildToBack(name);
    return var;
  }

  private Node createAssignmentNode(String varName, String value) {
    Node assign = new Node(Token.ASSIGN);
    Node name = createNameNode(varName);
    Node val = new Node(Token.STRING, value);
    assign.addChildToBack(name);
    assign.addChildToBack(val);
    return assign;
  }

  private Node createNameNode(String name) {
    Node result = new Node(Token.NAME);
    result.setString(name);
    return result;
  }

}
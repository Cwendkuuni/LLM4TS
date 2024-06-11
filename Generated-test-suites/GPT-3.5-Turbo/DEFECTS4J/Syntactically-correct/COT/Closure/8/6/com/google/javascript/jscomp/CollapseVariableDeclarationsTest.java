
import org.junit.Test;
import static org.junit.Assert.*;

public class CollapseVariableDeclarationsTest {

  @Test
  public void testCollapseVariableDeclarations() {
    // Test case 1
    CollapseVariableDeclarations collapseVariableDeclarations = new CollapseVariableDeclarations();
    Node var1 = new Node(Token.VAR);
    Node var2 = new Node(Token.VAR);
    Node var3 = new Node(Token.VAR);

    Node assign1 = new Node(Token.ASSIGN);
    Node assign2 = new Node(Token.ASSIGN);

    Node name1 = new Node(Token.NAME, "a");
    Node name2 = new Node(Token.NAME, "b");
    Node name3 = new Node(Token.NAME, "c");

    Node value1 = new Node(Token.TRUE);
    Node value2 = new Node(Token.TRUE);
    Node value3 = new Node(Token.TRUE);

    assign1.addChildToBack(name1);
    assign1.addChildToBack(value1);
    assign2.addChildToBack(name2);
    assign2.addChildToBack(value2);

    var1.addChildToBack(name1);
    var1.addChildToBack(value1);

    var2.addChildToBack(assign1);
    var2.addChildToBack(assign2);

    var3.addChildToBack(name3);
    var3.addChildToBack(value3);

    Node root = new Node(Token.ROOT);
    root.addChildToBack(var1);
    root.addChildToBack(var2);
    root.addChildToBack(var3);

    collapseVariableDeclarations.process(null, root);

    assertTrue(root.getFirstChild().isVar());
    assertTrue(root.getChildAtIndex(1).isVar());
    assertTrue(root.getLastChild().isVar());

    assertEquals(Token.VAR, root.getFirstChild().getToken());
    assertEquals(Token.VAR, root.getChildAtIndex(1).getToken());
    assertEquals(Token.VAR, root.getLastChild().getToken());

    assertEquals(1, root.getFirstChild().getChildCount());
    assertEquals(2, root.getChildAtIndex(1).getChildCount());
    assertEquals(1, root.getLastChild().getChildCount());

    assertEquals(name1, root.getFirstChild().getFirstChild().getFirstChild());
    assertEquals(name2, root.getChildAtIndex(1).getFirstChild().getFirstChild().getFirstChild());
    assertEquals(name3, root.getLastChild().getFirstChild());

    assertEquals(value1, root.getFirstChild().getFirstChild().getLastChild());
    assertEquals(value2, root.getChildAtIndex(1).getFirstChild().getLastChild().getLastChild());
    assertEquals(value3, root.getLastChild().getFirstChild().getLastChild());
  }

  @Test
  public void testCollapseVariableAssignments() {
    // Test case 2
    CollapseVariableDeclarations collapseVariableDeclarations = new CollapseVariableDeclarations();
    Node var1 = new Node(Token.VAR);
    Node var2 = new Node(Token.VAR);
    Node var3 = new Node(Token.VAR);

    Node assign1 = new Node(Token.ASSIGN);
    Node assign2 = new Node(Token.ASSIGN);

    Node name1 = new Node(Token.NAME, "a");
    Node name2 = new Node(Token.NAME, "b");
    Node name3 = new Node(Token.NAME, "c");

    Node value1 = new Node(Token.TRUE);
    Node value2 = new Node(Token.TRUE);
    Node value3 = new Node(Token.TRUE);

    assign1.addChildToBack(name1);
    assign1.addChildToBack(value1);
    assign2.addChildToBack(name2);
    assign2.addChildToBack(value2);

    var1.addChildToBack(assign1);
    var1.addChildToBack(assign2);

    var2.addChildToBack(name1);
    var2.addChildToBack(value1);

    var3.addChildToBack(name3);
    var3.addChildToBack(value3);

    Node root = new Node(Token.ROOT);
    root.addChildToBack(var1);
    root.addChildToBack(var2);
    root.addChildToBack(var3);

    collapseVariableDeclarations.process(null, root);

    assertTrue(root.getFirstChild().isVar());
    assertTrue(root.getChildAtIndex(1).isVar());
    assertTrue(root.getLastChild().isVar());

    assertEquals(Token.VAR, root.getFirstChild().getToken());
    assertEquals(Token.VAR, root.getChildAtIndex(1).getToken());
    assertEquals(Token.VAR, root.getLastChild().getToken());

    assertEquals(2, root.getFirstChild().getChildCount());
    assertEquals(1, root.getChildAtIndex(1).getChildCount());
    assertEquals(1, root.getLastChild().getChildCount());

    assertEquals(name1, root.getFirstChild().getFirstChild().getFirstChild());
    assertEquals(name2, root.getFirstChild().getChildAtIndex(1).getFirstChild().getFirstChild().getFirstChild());
    assertEquals(name3, root.getLastChild().getFirstChild());

    assertEquals(value1, root.getFirstChild().getFirstChild().getLastChild());
    assertEquals(value2, root.getFirstChild().getChildAtIndex(1).getFirstChild().getLastChild().getLastChild());
    assertEquals(value3, root.getLastChild().getFirstChild().getLastChild());
  }

}

import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.MaybeReachingVariableUse;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class MaybeReachingVariableUseTest {
  @Test
  public void testSimpleAssignment() {
    // Given
    Node root = new Node();
    root.setType(Token.BLOCK);

    Node defNode = new Node();
    defNode.setType(Token.ASSIGN);
    defNode.setLeft(new Node("x"));
    defNode.setRight(new Node(1));
    root.addChild(defNode);

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(
        new ControlFlowGraph<Node>(root), new Scope(null), null);

    // When
    ReachingUses result = analysis.computeReachingUses();

    // Then
    Collection<Node> uses = analysis.getUses("x", defNode);
    assertTrue(uses.isEmpty());
  }

  @Test
  public void testConditionalAssignment() {
    // Given
    Node root = new Node();
    root.setType(Token.BLOCK);

    Node defNode = new Node();
    defNode.setType(Token.IF);
    defNode.setCondition(new Node(2));
    defNode.setElse(new Node());

    Node assignNode1 = new Node();
    assignNode1.setType(Token.ASSIGN);
    assignNode1.setLeft(new Node("x"));
    assignNode1.setRight(new Node("y"));
    defNode.addChild(assignNode1);

    Node assignNode2 = new Node();
    assignNode2.setType(Token.ASSIGN);
    assignNode2.setLeft(new Node("x"));
    assignNode2.setRight(new Node(3));
    defNode.getElse().addChild(assignNode2);

    root.addChild(defNode);

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(
        new ControlFlowGraph<Node>(root), new Scope(null), null);

    // When
    ReachingUses result = analysis.computeReachingUses();

    // Then
    Collection<Node> uses = analysis.getUses("x", defNode);
    assertEquals(1, uses.size());
    assertEquals(assignNode1, uses.iterator().next());
  }

  @Test(expected = AssertionError.class)
  public void testInvalidVariable() {
    // Given
    Node root = new Node();
    root.setType(Token.BLOCK);

    Node defNode = new Node();
    defNode.setType(Token.ASSIGN);
    defNode.setLeft(new Node("invalidVar"));
    defNode.setRight(new Node(1));
    root.addChild(defNode);

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(
        new ControlFlowGraph<Node>(root), new Scope(null), null);

    // When and Then
    try {
      analysis.getUses("invalidVar", defNode);
      fail("Expected AssertionError");
    } catch (AssertionError e) {
      // Expected
    }
  }
}
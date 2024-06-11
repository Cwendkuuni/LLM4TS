import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.javascript.jscomp.MustBeReachingVariableDef;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import org.junit.Before;
import org.junit.Test;

/**
 * Test the MustBeReachingVariableDef class.
 */
public class MustBeReachingVariableDefTest {

  private Scope scope;
  private MustBeReachingVariableDef analyzer;

  @Before
  public void setUp() {
    scope = new Scope(new NodeFactory());
    analyzer = new MustBeReachingVariableDef(
        new ControlFlowGraph<>(scope, new NodeFactory()), scope, null);
  }

  @Test
  public void testSimpleAssignment() {
    Node rootNode = new Node(Node.ROOT, null);
    Node aAssignment = new Node(Token.ASSIGN, null, null, null, rootNode, null);
    Node a = new Node(Token.NAME, "a", null, null, aAssignment, null);
    Node bAssignment = new Node(Token.ASSIGN, null, null, null, rootNode, null);
    Node b = new Node(Token.NAME, "b", null, null, bAssignment, null);
    Node aAssignToA = new Node(Token.ASSIGN, null, null, null, rootNode, null);
    aAssignToA.setFirstChild(aAssignment);
    aAssignToA.setLastChild(a);
    bAssignToA = new Node(Token.ASSIGN, null, null, null, rootNode, null);
    bAssignToA.setFirstChild(bAssignment);
    bAssignToA.setLastChild(b);

    analyzer.computeMustDef(aAssignToA, rootNode, analyzer.createEntryLattice(), false);
    assertEquals(a, analyzer.getDef("a", aAssignToA));
    assertEquals(b, analyzer.getDef("b", bAssignToA));
  }

  @Test
  public void testMultipleAssignments() {
    Node rootNode = new Node(Node.ROOT, null);
    Node aAssignment1 = new Node(Token.ASSIGN, null, null, null, rootNode, null);
    Node a1 = new Node(Token.NAME, "a1", null, null, aAssignment1, null);
    Node aAssignment2 = new Node(Token.ASSIGN, null, null, null, rootNode, null);
    Node a2 = new Node(Token.NAME, "a2", null, null, aAssignment2, null);
    Node a1AssignToA1 = new Node(Token.ASSIGN, null, null, null, rootNode, null);
    a1AssignToA1.setFirstChild(aAssignment1);
    a1AssignToA1.setLastChild(a1);
    Node a2AssignToA1 = new Node(Token.ASSIGN, null, null, null, rootNode, null);
    a2AssignToA1.setFirstChild(aAssignment2);
    a2AssignToA1.setLastChild(a2);

    analyzer.computeMustDef(a1AssignToA1, rootNode, analyzer.createEntryLattice(), false);
    assertEquals(a1, analyzer.getDef("a1", a1AssignToA1));
    assertEquals(a2, analyzer.getDef("a2", a2AssignToA1));

    analyzer.computeMustDef(a2AssignToA1, rootNode, analyzer.createEntryLattice(), false);
    assertNull(analyzer.getDef("a1", a2AssignToA1));
    assertNull(analyzer.getDef("a2", a2AssignToA1));
  }

  @Test
  public void testConditionalAssignment() {
    Node rootNode = new Node(Node.ROOT, null);
    Node condition = new Node(Token.IF, null, null, null, rootNode, null);
    Node aAssignment1 = new Node(Token.ASSIGN, null, null, null, rootNode, null);
    Node a1 = new Node(Token.NAME, "a1", null, null, aAssignment1, null);
    Node aAssignment2 = new Node(Token.ASSIGN, null, null, null, rootNode, null);
    Node a2 = new Node(Token.NAME, "a2", null, null, aAssignment2, null);
    Node a1AssignToA1 = new Node(Token.ASSIGN, null, null, null, rootNode, null);
    a1AssignToA1.setFirstChild(aAssignment1);
    a1AssignToA1.setLastChild(a1);
    Node a2AssignToA1 = new Node(Token.ASSIGN, null, null, null, rootNode, null);
    a2AssignToA1.setFirstChild(aAssignment2);
    a2AssignToA1.setLastChild(a2);

    Node ifTrue = new Node(Token.BLOCK, null, null, null, rootNode, null);
    condition.setFirstChild(new Node(Token.IF, null, null, null, condition, null));
    condition.setLastChild(ifTrue);
    ifTrue.setFirstChild(a1AssignToA1);
    ifTrue.setLastChild(a1);

    Node ifFalse = new Node(Token.BLOCK, null, null, null, rootNode, null);
    condition.setNext(new Node(Token.ELSE, null, null, null, condition, null));
    condition.getLastChild().setFirstChild(ifFalse);
    ifFalse.setFirstChild(a2AssignToA1);
    ifFalse.setLastChild(a2);

    analyzer.computeMustDef(rootNode, rootNode, analyzer.createEntryLattice(), false);
    assertEquals(a1, analyzer.getDef("a1", a1AssignToA1));
    assertEquals(a2, analyzer.getDef("a2", a2AssignToA1));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidNode() {
    Node rootNode = new Node(Node.ROOT, null);
    Node invalidNode = new Node(Token.INVALID, null, null, null, rootNode, null);
    analyzer.computeMustDef(invalidNode, rootNode, analyzer.createEntryLattice(), false);
  }
}
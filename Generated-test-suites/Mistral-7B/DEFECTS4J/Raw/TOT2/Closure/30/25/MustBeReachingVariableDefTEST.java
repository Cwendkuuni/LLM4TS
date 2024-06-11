import com.google.javascript.jscomp.MustBeReachingVariableDef;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.graph.GraphNode;
import com.google.javascript.rhino.Node;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class MustBeReachingVariableDefTest {

  private MustBeReachingVariableDef analysis;
  private Scope jsScope;

  @Before
  public void setUp() {
    // Assuming we have a valid Scope object and AbstractCompiler instance
    GraphNode<Node, Branch> controlFlowGraph = null; // initialize CFG here
    jsScope = new Scope();
    analysis = new MustBeReachingVariableDef(controlFlowGraph, jsScope, null);
  }

  @Test
  public void testSimpleAssignment() {
    Node useNode = Node.newIdentifier("x"); // use node
    Node defNode = Node.newAssignment(Node.newName("a"), Node.newName("x")); // definition node
    analysis.getDef("x", useNode);
    assertEquals(defNode, analysis.getDef("x", useNode));
  }

  @Test
  public void testMultipleAssignments() {
    Node useNode = Node.newIdentifier("x");
    Node defNode1 = Node.newAssignment(Node.newName("a"), Node.newName("x"));
    Node defNode2 = Node.newAssignment(Node.newName("b"), Node.newName("x"));
    analysis.getDef("x", useNode);
    assertEquals(defNode1, analysis.getDef("x", useNode));
    assertNull(analysis.getDef("b", useNode)); // b should not be reached
  }

  @Test
  public void testConditionalAssignment() {
    Node conditionNode = Node.newBooleanLiteral(true);
    Node useNode = Node.newConditional(Node.newName("x"), conditionNode,
        Node.newAssignment(Node.newName("a"), Node.newName("x")),
        Node.newAssignment(Node.newName("b"), Node.newName("x")));
    analysis.getDef("x", useNode);
    assertEquals(Node.newAssignment(Node.newName("a"), Node.newName("x")),
        analysis.getDef("x", useNode));
    assertNull(analysis.getDef("b", useNode));
  }

  @Test
  public void testMultipleUses() {
    Node nameNode = Node.newIdentifier("x");
    Node defNode = Node.newAssignment(Node.newName("a"), Node.newName("x"));
    Node useNode1 = Node.newAssignment(Node.newName("c"), Node.newName("x"));
    Node useNode2 = Node.newAssignment(Node.newName("d"), Node.newName("x"));
    analysis.getDef("x", useNode1);
    assertEquals(defNode, analysis.getDef("x", useNode1));
    assertEquals(defNode, analysis.getDef("x", useNode2));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullUseNode() {
    analysis.getDef(null, Node.newIdentifier("x"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullDefNode() {
    Node useNode = Node.newIdentifier("x");
    analysis.getDef("y", useNode);
  }

  @Test
  public void testVariableOutsideOfScope() {
    Node nameNode = Node.newIdentifier("z"); // variable outside of the scope
    Node useNode = Node.newIdentifier("x");
    assertNull(analysis.getDef(nameNode.getString(), useNode));
  }

  @Test
  public void testVariableInOuterScope() {
    String outerScopeVar = "outerVar";
    Node outerScopeDefNode = Node.newAssignment(Node.newName(outerScopeVar),
        Node.newNumberLiteral(1));
    Node outerScopeUseNode = Node.newIdentifier(outerScopeVar);
    Scope outerScope = new Scope();
    outerScope.setRootNode(outerScopeDefNode);
    jsScope.setParent(outerScope);

    Node nameNode = Node.newIdentifier("x");
    Node useNode = Node.newIdentifier("x");
    analysis.getDef(nameNode.getString(), useNode);
    assertNull(analysis.getDef(nameNode.getString(), useNode));

    jsScope = jsScope.getParent();
  }
}
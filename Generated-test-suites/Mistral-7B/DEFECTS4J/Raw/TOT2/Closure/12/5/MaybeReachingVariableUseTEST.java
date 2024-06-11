import com.google.javascript.jscomp.MaybeReachingVariableUse;
import com.google.javascript.jscomp.Scope;
import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(JUnit4.class)
public class MaybeReachingVariableUseTest {

  @Test
  public void testMaybeReachingVariableUse() {
    // Test case 1: Simple assignment
    Scope scope = new Scope();
    ControlFlowGraph<Node> controlFlowGraph = new ControlFlowGraph<>();
    MaybeReachingVariableUse reachingVariableUse = new MaybeReachingVariableUse(
        controlFlowGraph, scope, null);

    // Assigning a value to a variable
    Node assignNode = Node.newNode(Token.ASSIGN, new Node[]{}, new Node[]{
        Node.newNode(Token.NAME, "x"), Node.newNumber(1)});

    // The definition node where the variable is assigned
    Node definitionNode = Node.newBlock(new Node[]{assignNode});

    // Create a new node for testing
    Node testNode = Node.newBlock(new Node[]{
        Node.newName("print"),
        Node.newName("x")
    });

    // Execute the analysis
    reachingVariableUse.process(definitionNode);

    // Check if the variable x is used in the test node
    Collection<Node> uses = reachingVariableUse.getUses("x", testNode);
    assertEquals("Expected one use of x", 1, uses.size());
    assertEquals("Expected use of node where x is defined", definitionNode, uses.iterator().next());

    // Test case 2: Assignment in a conditional statement
    scope = new Scope();
    controlFlowGraph = new ControlFlowGraph<>();
    reachingVariableUse = new MaybeReachingVariableUse(
        controlFlowGraph, scope, null);

    Node ifNode = Node.newIf(Node.newBoolean(true), new Node[]{
        Node.newName("x"),
        Node.newName("y")
    }, Node.newBlock(new Node[]{
      Node.newAssign(Node.newName("x"), Node.newNumber(2)),
      Node.newName("z")
    }));

    Node definitionNodeIf = ifNode;

    Node testNodeIf = Node.newBlock(new Node[]{
      Node.newName("print"),
      Node.newName("x")
    });

    reachingVariableUse.process(definitionNodeIf);

    uses = reachingVariableUse.getUses("x", testNodeIf);
    assertEquals("Expected one use of x", 1, uses.size());
    assertEquals("Expected use of node where x is defined", definitionNodeIf, uses.iterator().next());

    // Test case 3: Assignment in a loop
    scope = new Scope();
    controlFlowGraph = new ControlFlowGraph<>();
    reachingVariableUse = new MaybeReachingVariableUse(
        controlFlowGraph, scope, null);

    Node forNode = Node.newFor(Node.newName("i"), Node.newNumber(1), Node.newNumber(3), new Node[]{
        Node.newAssign(Node.newName("x"), Node.newName("i")),
        Node.newName("y")
    }, Node.newBlock(new Node[]{
      Node.newAssign(Node.newName("x"), Node.newNumber(2)),
      Node.newName("z")
    }));

    Node definitionNodeFor = forNode;

    Node testNodeFor = Node.newBlock(new Node[]{
      Node.newName("print"),
      Node.newName("x")
    });

    reachingVariableUse.process(definitionNodeFor);

    uses = reachingVariableUse.getUses("x", testNodeFor);
    assertEquals("Expected one use of x", 1, uses.size());
    assertEquals("Expected use of node where x is defined", definitionNodeFor, uses.iterator().next());

    // Test case 4: Assignment in an empty block
    scope = new Scope();
    controlFlowGraph = new ControlFlowGraph<>();
    reachingVariableUse = new MaybeReachingVariableUse(
        controlFlowGraph, scope, null);

    Node emptyBlock = Node.newBlock(new Node[]{});

    Node definitionNodeEmpty = emptyBlock;

    Node testNodeEmpty = Node.newBlock(new Node[]{
      Node.newName("print"),
      Node.newName("x")
    });

    reachingVariableUse.process(definitionNodeEmpty);

    uses = reachingVariableUse.getUses("x", testNodeEmpty);
    assertNull("Expected no use of x", uses);

    // Test case 5: Assignment in a function
    scope = new Scope();
    controlFlowGraph = new ControlFlowGraph<>();
    reachingVariableUse = new MaybeReachingVariableUse(
        controlFlowGraph, scope, null);

    Node functionNode = Node.newFunction("test", new Node[0], new Node[]{
      Node.newAssign(Node.newName("x"), Node.newNumber(1)),
      Node.newName("y")
    }, Node.newBlock(new Node[]{
      Node.newName("print"),
      Node.newName("x")
    }));

    Node definitionNodeFunction = functionNode;

    Node testNodeFunction = Node.newName("test");

    reachingVariableUse.process(definitionNodeFunction);

    uses = reachingVariableUse.getUses("x", testNodeFunction);
    assertEquals("Expected one use of x", 1, uses.size());
    assertEquals("Expected use of node where x is defined", definitionNodeFunction, uses.iterator().next());
  }
}
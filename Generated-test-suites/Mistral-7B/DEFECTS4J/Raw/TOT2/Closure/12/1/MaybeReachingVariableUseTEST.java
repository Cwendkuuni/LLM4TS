import com.google.javascript.jscomp.MaybeReachingVariableUse;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MaybeReachingVariableUseTest {

  private MaybeReachingVariableUse maybeReachingVariableUse;
  private Scope jsScope;

  @Before
  public void setup() {
    // Create a mock Scope for testing
    jsScope = new Scope();

    // Initialize the MaybeReachingVariableUse instance
    Node entryNode = Node.newBlock("entry");
    maybeReachingVariableUse = new MaybeReachingVariableUse(
        new ControlFlowGraph<>(), jsScope, null);
    maybeReachingVariableUse.initialize(entryNode);
  }

  @Test
  public void testBasicFunctionality() {
    // Test a simple function with a local variable
    String functionName = "testFunction";
    String variableName = "x";

    Node functionNode = Node.newFunction(Node.newName(functionName), null, null);
    Node variableAssignmentNode = Node.newAssign(Node.newName(variableName), Node.newInt(1));
    Node useNode = Node.newAssign(Node.newName(variableName), Node.newInt(2));
    Node returnNode = Node.newReturn(Node.newName(variableName));

    maybeReachingVariableUse.flowThrough(functionNode, maybeReachingVariableUse.createEntryLattice());
    maybeReachingVariableUse.flowThrough(variableAssignmentNode, maybeReachingVariableUse.createEntryLattice());
    maybeReachingVariableUse.flowThrough(useNode, maybeReachingVariableUse.createEntryLattice());
    maybeReachingVariableUse.flowThrough(returnNode, maybeReachingVariableUse.createEntryLattice());

    Collection<Node> uses = maybeReachingVariableUse.getUses(variableName, functionNode);
    assertNotNull(uses);
    assertEquals(1, uses.size());
    assertEquals(useNode, uses.iterator().next());
  }

  @Test
  public void testEdgeCases() {
    // Test a function with multiple variable assignments and conditionals
    String functionName = "testFunction";
    String variableName1 = "x";
    String variableName2 = "y";

    Node functionNode = Node.newFunction(Node.newName(functionName), null, null);
    Node variableAssignmentNode1 = Node.newAssign(Node.newName(variableName1), Node.newInt(1));
    Node variableAssignmentNode2 = Node.newAssign(Node.newName(variableName2), Node.newInt(2));
    Node conditionalNode1 = Node.newIf(Node.newBool(true), Node.newBlock("ifBlock1"), Node.newBlock("elseBlock1"));
    Node conditionalNode2 = Node.newIf(Node.newBool(false), Node.newBlock("ifBlock2"), Node.newBlock("elseBlock2"));
    Node useNode1 = Node.newAssign(Node.newName(variableName1), Node.newInt(3));
    Node useNode2 = Node.newAssign(Node.newName(variableName2), Node.newInt(4));
    Node returnNode = Node.newReturn(Node.newName(variableName2));

    maybeReachingVariableUse.flowThrough(functionNode, maybeReachingVariableUse.createEntryLattice());
    maybeReachingVariableUse.flowThrough(variableAssignmentNode1, maybeReachingVariableUse.createEntryLattice());
    maybeReachingVariableUse.flowThrough(conditionalNode1, maybeReachingVariableUse.createEntryLattice());
    maybeReachingVariableUse.flowThrough(variableAssignmentNode2, maybeReachingVariableUse.createEntryLattice());
    maybeReachingVariableUse.flowThrough(conditionalNode2, maybeReachingVariableUse.createEntryLattice());
    maybeReachingVariableUse.flowThrough(useNode1, maybeReachingVariableUse.createEntryLattice());
    maybeReachingVariableUse.flowThrough(useNode2, maybeReachingVariableUse.createEntryLattice());
    maybeReachingVariableUse.flowThrough(returnNode, maybeReachingVariableUse.createEntryLattice());

    Collection<Node> uses1 = maybeReachingVariableUse.getUses(variableName1, functionNode);
    assertNotNull(uses1);
    assertEquals(1, uses1.size());
    assertEquals(variableAssignmentNode1, uses1.iterator().next());

    Collection<Node> uses2 = maybeReachingVariableUse.getUses(variableName2, functionNode);
    assertNotNull(uses2);
    assertEquals(2, uses2.size());
    assertTrue(uses2.contains(useNode1));
    assertTrue(uses2.contains(useNode2));
  }

  @Test
  public void testExceptionHandling() {
    // Test a function with exception handling
    String functionName = "testFunction";
    String variableName = "x";

    Node functionNode = Node.newFunction(Node.newName(functionName), null, null);
    Node variableAssignmentNode = Node.newAssign(Node.newName(variableName), Node.newInt(1));
    Node throwNode = Node.newThrow(Node.newName("MyException"));
    Node catchNode = Node.newTryCatchBlock(null, null, Node.newBlock("catchBlock"), null);
    Node useNode = Node.newAssign(Node.newName(variableName), Node.newInt(2));
    Node returnNode = Node.newReturn(Node.newName(variableName));

    maybeReachingVariableUse.flowThrough(functionNode, maybeReachingVariableUse.createEntryLattice());
    maybeReachingVariableUse.flowThrough(variableAssignmentNode, maybeReachingVariableUse.createEntryLattice());
    maybeReachingVariableUse.flowThrough(throwNode, maybeReachingVariableUse.createEntryLattice());
    maybeReachingVariableUse.flowThrough(catchNode, maybeReachingVariableUse.createEntryLattice());
    maybeReachingVariableUse.flowThrough(useNode, maybeReachingVariableUse.createEntryLattice());
    maybeReachingVariableUse.flowThrough(returnNode, maybeReachingVariableUse.createEntryLattice());

    Collection<Node> uses = maybeReachingVariableUse.getUses(variableName, functionNode);
    assertNotNull(uses);
    assertEquals(1, uses.size());
    assertEquals(useNode, uses.iterator().next());

    // Check if the exception handling doesn't affect the reachability of the variable
    Node throwNodeInsideUse = Node.newAssign(Node.newName(variableName), Node.newThrow(Node.newName("MyException")));
    maybeReachingVariableUse.flowThrough(throwNodeInsideUse, maybeReachingVariableUse.createEntryLattice());

    Collection<Node> usesAfterException = maybeReachingVariableUse.getUses(variableName, functionNode);
    assertNotNull(usesAfterException);
    assertEquals(1, usesAfterException.size());
    assertEquals(useNode, usesAfterException.iterator().next());
  }

  @Test
  public void testFunctionCalls() {
    // Test a function that calls another function with a local variable
    String outerFunctionName = "outerFunction";
    String innerFunctionName = "innerFunction";
    String variableName = "x";

    Node outerFunctionNode = Node.newFunction(Node.newName(outerFunctionName), null, null);
    Node innerFunctionNode = Node.newFunction(Node.newName(innerFunctionName), null, null);
    Node variableAssignmentNode = Node.newAssign(Node.newName(variableName), Node.newInt(1));
    Node innerFunctionCallNode = Node.newCall(Node.newName(innerFunctionName), Node.newList());
    Node useNode = Node.newAssign(Node.newName(variableName), Node.newInt(2));
    Node returnNode = Node.newReturn(Node.newName(variableName));

    maybeReachingVariableUse.flowThrough(outerFunctionNode, maybeReachingVariableUse.createEntryLattice());
    maybeReachingVariableUse.flowThrough(variableAssignmentNode, maybeReachingVariableUse.createEntryLattice());
    maybeReachingVariableUse.flowThrough(innerFunctionNode, maybeReachingVariableUse.createEntryLattice());
    maybeReachingVariableUse.flowThrough(variableAssignmentNode, maybeReachingVariableUse.createEntryLattice());
    maybeReachingVariableUse.flowThrough(innerFunctionCallNode, maybeReachingVariableUse.createEntryLattice());
    maybeReachingVariableUse.flowThrough(useNode, maybeReachingVariableUse.createEntryLattice());
    maybeReachingVariableUse.flowThrough(returnNode, maybeReachingVariableUse.createEntryLattice());

    Collection<Node> uses = maybeReachingVariableUse.getUses(variableName, outerFunctionNode);
    assertNotNull(uses);
    assertEquals(1, uses.size());
    assertEquals(useNode, uses.iterator().next());
  }
}
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class MaybeReachingVariableUseTest {

  @Test
  public void testMaybeReachingVariableUse() {
    // Arrange
    Scope scope = Scope.newGlobalScope(new AbstractCompiler());
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(cfg, scope, new AbstractCompiler());

    // Act
    ReachingUses result = maybeReachingVariableUse.forwardPropagate(cfg.getEntryNode());

    // Assert
    assertNotNull(result);
    assertTrue(result.mayUseMap.isEmpty());

    // Arrange - Define variable A and add usage of A
    Node defA = Node.newLocal(Token.NAME, "A", new Node[0], null, null);
    Node useA1 = Node.newName("useA1");
    Node useA2 = Node.newName("useA2");

    Node assignmentA = Node.newAssignment(Token.ASSIGN, Token.NAME, "A", Node.newName("value"));
    Node blockA = Node.newBlock(new Node[] { assignmentA, useA1, useA2 });

    Node definitionNodeA = Node.newVariable(Token.VAR, Token.NAME, "A", ImmutableSet.of(defA), null);
    Node usageNodeA1 = Node.newExpressionStatement(useA1);
    Node usageNodeA2 = Node.newExpressionStatement(useA2);

    cfg.addNode(definitionNodeA, ImmutableSet.of(NodeUtil.createBranch(usageNodeA1)));
    cfg.addNode(usageNodeA1, ImmutableSet.of());
    cfg.addNode(usageNodeA2, ImmutableSet.of());

    // Act
    result = maybeReachingVariableUse.forwardPropagate(definitionNodeA);

    // Assert
    assertNotNull(result);
    assertTrue(result.mayUseMap.containsKey(scope.getVar("A")));
    assertTrue(result.mayUseMap.get(scope.getVar("A")).contains(definitionNodeA));
    assertTrue(result.mayUseMap.get(scope.getVar("A")).contains(usageNodeA1));
    assertTrue(result.mayUseMap.get(scope.getVar("A")).contains(usageNodeA2));

    // Arrange - Define variable A again and add usage of A
    Node defA2 = Node.newLocal(Token.NAME, "A", new Node[0], null, null);
    Node usageNodeA3 = Node.newName("useA3");

    Node assignmentA2 = Node.newAssignment(Token.ASSIGN, Token.NAME, "A", Node.newName("value2"));
    Node blockA2 = Node.newBlock(new Node[] { assignmentA2, usageNodeA3 });

    Node definitionNodeA2 = Node.newVariable(Token.VAR, Token.NAME, "A", ImmutableSet.of(defA2), null);
    Node usageNodeA3 = Node.newExpressionStatement(useA3);

    cfg.addNode(definitionNodeA2, ImmutableSet.of());
    cfg.addNode(usageNodeA3, ImmutableSet.of(NodeUtil.createBranch(definitionNodeA)));

    // Act
    result = maybeReachingVariableUse.forwardPropagate(definitionNodeA2);

    // Assert
    assertNotNull(result);
    assertTrue(result.mayUseMap.containsKey(scope.getVar("A")));
    assertTrue(result.mayUseMap.get(scope.getVar("A")).contains(definitionNodeA));
    assertTrue(result.mayUseMap.get(scope.getVar("A")).contains(usageNodeA1));
    assertTrue(result.mayUseMap.get(scope.getVar("A")).contains(usageNodeA2));
    assertTrue(result.mayUseMap.get(scope.getVar("A")).contains(definitionNodeA2));
    assertTrue(result.mayUseMap.get(scope.getVar("A")).contains(usageNodeA3));

    // Act - Test usage of escaped variable
    Node definitionEscapedVar = Node.newVariable(Token.VAR, Token.NAME, "escapedVar", ImmutableSet.of(), null);
    Node usageEscapedVar = Node.newName("useEscapedVar");

    cfg.addNode(definitionEscapedVar, ImmutableSet.of());
    cfg.addNode(usageEscapedVar, ImmutableSet.of());

    maybeReachingVariableUse.escaped.add(scope.getVar("escapedVar"));

    // Act
    result = maybeReachingVariableUse.forwardPropagate(definitionEscapedVar);

    // Assert
    assertNotNull(result);
    assertFalse(result.mayUseMap.containsKey(scope.getVar("escapedVar")));
  }

  @Test
  public void testMayReachingVariableUseWithConditional() {
    // Arrange
    Scope scope = Scope.newGlobalScope(new AbstractCompiler());
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(cfg, scope, new AbstractCompiler());

    // Act
    ReachingUses result = maybeReachingVariableUse.forwardPropagate(cfg.getEntryNode());

    // Assert
    assertNotNull(result);
    assertTrue(result.mayUseMap.isEmpty());

    // Arrange - Define variable A and add usage of A in conditional statement
    Node defA = Node.newLocal(Token.NAME, "A", new Node[0], null, null);
    Node useA = Node.newName("useA");
    Node condition = Node.newExpressionStatement(Node.newBinaryOp(Token.LOGICAL_OR, useA, Node.newName("true")));

    Node definitionNodeA = Node.newVariable(Token.VAR, Token.NAME, "A", ImmutableSet.of(defA), null);

    cfg.addNode(definitionNodeA, ImmutableSet.of());
    cfg.addNode(condition, ImmutableSet.of(NodeUtil.createBranch(definitionNodeA)));

    // Act
    result = maybeReachingVariableUse.forwardPropagate(condition);

    // Assert
    assertNotNull(result);
    assertTrue(result.mayUseMap.containsKey(scope.getVar("A")));
    assertTrue(result.mayUseMap.get(scope.getVar("A")).contains(definitionNodeA));
    assertTrue(result.mayUseMap.get(scope.getVar("A")).contains(useA));

    // Arrange - Define variable A again and add usage of A in conditional statement
    Node defA2 = Node.newLocal(Token.NAME, "A", new Node[0], null, null);

    Node definitionNodeA2 = Node.newVariable(Token.VAR, Token.NAME, "A", ImmutableSet.of(defA2), null);

    cfg.addNode(definitionNodeA2, ImmutableSet.of());

    Node condition2 = Node.newExpressionStatement(Node.newBinaryOp(Token.LOGICAL_OR, useA, Node.newName("true2")));

    cfg.addNode(condition2, ImmutableSet.of(NodeUtil.createBranch(definitionNodeA2)));

    // Act
    result = maybeReachingVariableUse.forwardPropagate(condition2);

    // Assert
    assertNotNull(result);
    assertTrue(result.mayUseMap.containsKey(scope.getVar("A")));
    assertTrue(result.mayUseMap.get(scope.getVar("A")).contains(definitionNodeA));
    assertTrue(result.mayUseMap.get(scope.getVar("A")).contains(definitionNodeA2));
    assertTrue(result.mayUseMap.get(scope.getVar("A")).contains(useA));
  }
}
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

@RunWith(JUnit4.class)
public class MaybeReachingVariableUseTest {
  @Test
  public void testMaybeReachingVariableUse() {
    // Given
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    MaybeReachingVariableUse useUnderTest = new MaybeReachingVariableUse(cfg, jsScope, compiler);

    // Basic functionality test
    Node functionNode = Node.newFunctionNode("testFunction");
    useUnderTest.flowThrough(functionNode, new ReachingUses());
    Node localVariableNode = Node.newNameNode("localVariable");
    Node assignmentNode = Node.newAssign(Token.ASSIGN, localVariableNode, Node.newNumberNode(1));
    Node readNode = Node.newNameNode("localVariable");

    ReachingUses expectedOutput = new ReachingUses();
    expectedOutput.mayUseMap.put(jsScope.getVar("localVariable"), assignmentNode);
    expectedOutput.mayUseMap.put(jsScope.getVar("localVariable"), readNode);

    ReachingUses actualOutput = useUnderTest.flowThrough(readNode, new ReachingUses());
    assertTrue(actualOutput.equals(expectedOutput));

    // Edge cases and exception handling tests

    // Test empty code block
    Node emptyBlockNode = Node.newBlockNode();
    ReachingUses emptyBlockOutput = useUnderTest.flowThrough(emptyBlockNode, new ReachingUses());
    assertTrue(emptyBlockOutput.equals(new ReachingUses()));

    // Test usage of non-local variable
    String nonLocalVariableName = "nonLocalVariable";
    Var nonLocalVariable = jsScope.getVar(nonLocalVariableName);
    Node nonLocalVariableAssignmentNode = Node.newAssign(Token.ASSIGN, Node.newNameNode(nonLocalVariableName), Node.newNumberNode(1));
    Node nonLocalVariableReadNode = Node.newNameNode(nonLocalVariableName);

    useUnderTest.addToUseIfLocal(nonLocalVariableName, NodeUtil.getParent(nonLocalVariableAssignmentNode), new ReachingUses());
    ReachingUses nonLocalVariableUsage = useUnderTest.flowThrough(nonLocalVariableReadNode, new ReachingUses());
    assertFalse(nonLocalVariableUsage.mayUseMap.containsKey(nonLocalVariable));

    // Test usage of function parameter variable
    String functionParameterVariableName = "functionParameterVariable";
    Var functionParameterVariable = jsScope.declareVar(functionParameterVariableName, Token.VAR_PARAM, null);
    Node functionParameterVariableAssignmentNode = Node.newAssign(Token.ASSIGN, Node.newNameNode(functionParameterVariableName), Node.newNumberNode(1));
    Node functionParameterVariableReadNode = Node.newNameNode(functionParameterVariableName);

    useUnderTest.addToUseIfLocal(functionParameterVariableName, NodeUtil.getParent(functionParameterVariableAssignmentNode), new ReachingUses());
    ReachingUsages functionParameterVariableUsage = useUnderTest.flowThrough(functionParameterVariableReadNode, new ReachingUses());
    assertFalse(functionParameterVariableUsage.mayUseMap.containsKey(functionParameterVariable));

    // Test usage of escaped variable
    String escapedVariableName = "escapedVariable";
    Var escapedVariable = jsScope.declareVar(escapedVariableName, Token.VAR, null);
    jsScope.setVariableValue(escapedVariable, Node.newStringLiteral(escapedVariableName));
    Node escapedVariableAssignmentNode = Node.newAssign(Token.ASSIGN, Node.newNameNode(escapedVariableName), Node.newStringLiteral(escapedVariableName));
    Node escapedVariableReadNode = Node.newNameNode(escapedVariableName);

    useUnderTest.escaped.add(escapedVariable);
    ReachingUses escapedVariableUsage = useUnderTest.flowThrough(escapedVariableReadNode, new ReachingUses());
    assertFalse(escapedVariableUsage.mayUseMap.containsKey(escapedVariable));

    // Test usage of variable declared in a catch clause
    String catchVariableName = "catchVariable";
    Node catchNode = Node.newTryCatchBlockNode(functionNode, new Node[0], Node.newBlockNode(), Node.newNameNode(catchVariableName), Node.newBlockNode());
    Node catchVariableAssignmentNode = Node.newAssign(Token.ASSIGN, Node.newNameNode(catchVariableName), Node.newNumberNode(1));
    Node catchVariableReadNode = Node.newNameNode(catchVariableName);

    useUnderTest.computeEscaped(jsScope, Sets.newHashSet(), compiler);
    useUnderTest.addToUseIfLocal(catchVariableName, NodeUtil.getParent(catchVariableAssignmentNode), new ReachingUses());
    ReachingUses catchVariableUsage = useUnderTest.flowThrough(catchVariableReadNode, new ReachingUses());
    assertFalse(catchVariableUsage.mayUseMap.containsKey(jsScope.getVar(catchVariableName)));
  }
}
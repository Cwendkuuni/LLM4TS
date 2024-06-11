import com.google.common.collect.Multimaps;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.DataFlowAnalysis;
import com.google.javascript.jscomp.DataFlowAnalysis.JoinOp;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class MaybeReachingVariableUseTest {

  private ControlFlowGraph<Node> controlFlowGraph;
  private Scope jsScope;
  private AbstractCompiler compiler;
  private MaybeReachingVariableUse maybeReachingVariableUse;

  @Before
  public void setUp() {
    controlFlowGraph = mock(ControlFlowGraph.class);
    jsScope = mock(Scope.class);
    compiler = mock(AbstractCompiler.class);
    when(jsScope.getVar("test")).thenReturn(new Var("test", 0, jsScope));

    maybeReachingVariableUse = new MaybeReachingVariableUse(
        controlFlowGraph, jsScope, compiler);
  }

  @Test
  public void testCreateEntryLattice() {
    ReachingUses reachingUses = maybeReachingVariableUse.createEntryLattice();
    assertEquals(newHashSet(), reachingUses.mayUseMap.keySet());
  }

  @Test
  public void testCreateInitialEstimateLattice() {
    ReachingUses reachingUses = maybeReachingVariableUse.createInitialEstimateLattice();
    assertEquals(newHashSet(), reachingUses.mayUseMap.keySet());
  }

  @Test
  public void testIsForward() {
    boolean isForward = maybeReachingVariableUse.isForward();
    assertEquals(false, isForward);
  }

  @Test
  public void testFlowThroughName() {
    Node node = mock(Node.class);
    ReachingUses input = new ReachingUses();
    input.mayUseMap.put(jsScope.getVar("test"), node);

    ReachingUses output = maybeReachingVariableUse.flowThrough(
        node, input);

    assertEquals(input.mayUseMap, output.mayUseMap);
  }

  @Test
  public void testFlowThroughBlock() {
    Node block = mock(Node.class);
    ReachingUses input = new ReachingUses();

    ReachingUses output = maybeReachingVariableUse.flowThrough(
        block, input);

    assertEquals(input.mayUseMap, output.mayUseMap);
  }

  @Test
  public void testFlowThroughFunction() {
    Node function = mock(Node.class);
    ReachingUses input = new ReachingUses();

    ReachingUses output = maybeReachingVariableUse.flowThrough(
        function, input);

    assertEquals(input.mayUseMap, output.mayUseMap);
  }

  @Test
  public void testFlowThroughAssignment() {
    Node assign = createAssignmentNode("a", "b");
    Node defNode = assign.getNext();
    ReachingUses input = new ReachingUses();

    ReachingUses output = maybeReachingVariableUse.flowThrough(
        assign, input);

    Collection<Node> uses = maybeReachingVariableUse.getUses("a", defNode);
    assertEquals(Sets.newHashSet(assign), uses);
    assertEquals(input.mayUseMap, output.mayUseMap);
  }

  @Test
  public void testFlowThroughConditional() {
    Node conditional = createConditionalNode();
    Node defNode = conditional.getFirstChild();
    ReachingUses input = new ReachingUses();

    ReachingUses output = maybeReachingVariableUse.flowThrough(
        conditional, input);

    assertEquals(input.mayUseMap, output.mayUseMap);
  }

  @Test
  public void testFlowThroughWhileLoop() {
    Node whileLoop = createWhileLoopNode();
    Node defNode = whileLoop.getFirstChild();
    ReachingUses input = new ReachingUses();

    ReachingUses output = maybeReachingVariableUse.flowThrough(
        whileLoop, input);

    assertEquals(input.mayUseMap, output.mayUseMap);
  }

  @Test
  public void testFlowThroughIfStatement() {
    Node ifStatement = createIfStatementNode();
    Node defNode = ifStatement.getFirstChild();
    ReachingUses input = new ReachingUses();

    ReachingUsuses output = maybeReachingVariableUse.flowThrough(
        ifStatement, input);

    assertEquals(input.mayUseMap, output.mayUseMap);
  }

  @Test
  public void testFlowThroughForLoop() {
    Node forLoop = createForLoopNode();
    Node defNode = forLoop.getFirstChild();
    ReachingUses input = new ReachingUses();

    ReachingUsages output = maybeReachingVariableUse.flowThrough(
        forLoop, input);

    assertEquals(input.mayUseMap, output.mayUseMap);
  }

  @Test(expected = IllegalStateException.class)
  public void testFlowThroughUnknownNodeType() {
    Node unknownNode = mock(Node.class);
    ReachingUses input = new ReachingUses();

    maybeReachingVariableUse.flowThrough(unknownNode, input);
  }

  @Test
  public void testJoinOp() {
    ReachingUses reachingUses1 = new ReachingUses();
    reachingUses1.mayUseMap.put(jsScope.getVar("test1"), mock(Node.class));

    ReachingUses reachingUses2 = new ReachingUses();
    reachingUses2.mayUseMap.put(jsScope.getVar("test2"), mock(Node.class));

    JoinOp<ReachingUses> joinOp = maybeReachingVariableUse.getJoinOp();
    ReachingUses result = joinOp.apply(List.of(reachingUses1, reachingUses2));

    assertEquals(
        Multimaps.union(reachingUses1.mayUseMap, reachingUses2.mayUseMap),
        result.mayUseMap);
  }

  private Node createAssignmentNode(String variableName, String value) {
    Node assign = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.NAME, variableName);
    Node rhs = new Node(Token.NAME, value);
    assign.setFirstChild(lhs);
    assign.setLastChild(rhs);
    return assign;
  }

  private Node createConditionalNode() {
    Node conditional = new Node(Token.IF);
    Node condition = new Node(Token.BINARY, Token.LT, new Node(Token.NUM, 1), new Node(Token.NUM, 2));
    Node trueBranch = new Node(Token.BLOCK);
    Node falseBranch = new Node(Token.BLOCK);
    conditional.setFirstChild(condition);
    conditional.setLastChild(trueBranch);
    trueBranch.setLastChild(new Node(Token.PRINT, new Node(Token.NAME, "test")));
    return conditional;
  }

  private Node createWhileLoopNode() {
    Node whileLoop = new Node(Token.WHILE);
    Node condition = new Node(Token.BINARY, Token.LTE, new Node(Token.NUM, 1), new Node(Token.NUM, 2));
    Node body = new Node(Token.BLOCK);
    whileLoop.setFirstChild(condition);
    whileLoop.setLastChild(body);
    return whileLoop;
  }

  private Node createIfStatementNode() {
    Node ifStatement = new Node(Token.IF);
    Node condition = new Node(Token.BINARY, Token.EQ, new Node(Token.NAME, "test"), new Node(Token.NUM, 0));
    Node trueBranch = new Node(Token.BLOCK);
    Node falseBranch = new Node(Token.BLOCK);
    ifStatement.setFirstChild(condition);
    ifStatement.setLastChild(falseBranch);
    trueBranch.setLastChild(new Node(Token.PRINT, new Node(Token.NAME, "test")));
    ifStatement.setChildBefore(ifStatement.getLastChild(), trueBranch);
    return ifStatement;
  }

  private Node createForLoopNode() {
    Node forLoop = new Node(Token.FOR);
    Node initialization = new Node(Token.VAR, "i", new Node(Token.NUM, 0));
    Node condition = new Node(Token.BINARY, Token.LTE, new Node(Token.NAME, "i"), new Node(Token.NUM, 5));
    Node increment = new Node(Token.ASSIGN, new Node(Token.NAME, "i"), new Node(Token.NUM, 1));
    Node body = new Node(Token.BLOCK);
    forLoop.setFirstChild(initialization);
    forLoop.setChildBefore(forLoop.getLastChild(), condition);
    forLoop.setChildBefore(forLoop.getChildBefore(forLoop.getLastChild()), body);
    forLoop.setChildBefore(forLoop.getChildBefore(body), increment);
    return forLoop;
  }
}
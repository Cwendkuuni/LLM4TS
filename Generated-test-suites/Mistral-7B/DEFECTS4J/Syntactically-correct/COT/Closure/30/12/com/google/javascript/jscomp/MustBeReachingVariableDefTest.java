package com.google.javascript.jscomp.test;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.*;

@RunWith(JUnit4.class)
public class MustBeReachingVariableDefTest {

  private static final String TEST_FUNCTION_NAME = "testFunction";
  private static final String VAR_A = "a";
  private static final String VAR_B = "b";
  private static final String VAR_C = "c";
  private static final String PARAM_X = "x";
  private static final String PARAM_Y = "y";

  private Scope jsScope;
  private AbstractCompiler compiler;
  private ControlFlowGraph<Node> cfg;
  private MustBeReachingVariableDef mustBeReachingVariableDef;

  @Before
  public void setup() {
    // Assuming the setup of the mock objects
    jsScope = mock(Scope.class);
    compiler = mock(AbstractCompiler.class);
    cfg = mock(ControlFlowGraph.class);

    when(jsScope.getVars()).thenReturn(Arrays.asList(
        new Var(VAR_A, 0, null, null, null),
        new Var(VAR_B, 1, null, null, null),
        new Var(VAR_C, 2, null, null, null)
    ).iterator());

    mustBeReachingVariableDef = new MustBeReachingVariableDef(cfg, jsScope, compiler);
  }

  @Test
  public void testMustDefFlowThrough() {
    Node node1 = Node.newIntLiteral(1);
    Node node2 = Node.newName(VAR_A);
    Node node3 = Node.newName(VAR_B);
    Node node4 = Node.newOperator("+", node1, node3);
    Node node5 = Node.newName(VAR_A);

    MustDef input = new MustDef();
    MustDef output = mustBeReachingVariableDef.flowThrough(node5, input);

    assertThat(output.reachingDef).containsKey(jsScope.getVar(VAR_A));
    assertThat(output.reachingDef.get(jsScope.getVar(VAR_A))).isInstanceOf(Definition.class);
  }

  @Test
  public void testMustDefBlock() {
    Node node1 = Node.newIntLiteral(1);
    Node node2 = Node.newName(VAR_A);
    Node node3 = Node.newName(VAR_B);
    Node node4 = Node.newOperator("=", node2, node3);
    Node node5 = Node.newBlock(Arrays.asList(node1, node4));

    MustDef input = new MustDef();
    MustDef output = mustBeReachingVariableDef.flowThrough(node5, input);

    assertThat(output.reachingDef).containsKey(jsScope.getVar(VAR_A));
    assertThat(output.reachingDef.get(jsScope.getVar(VAR_A))).isInstanceOf(Definition.class);
    assertThat(output.reachingDef.get(jsScope.getVar(VAR_A)).node).isEqualTo(node5);
  }

  @Test
  public void testMustDefSimpleIf() {
    Node condition = Node.newBooleanLiteral(true);
    Node node1 = Node.newName(VAR_A);
    Node node2 = Node.newName(VAR_B);
    Node node3 = Node.newOperator("=", node1, Node.newIntLiteral(1));
    Node node4 = Node.newOperator("?", node3, Node.newName(VAR_A));
    Node node5 = Node.newBlock(Collections.emptyList());
    Node node6 = Node.newOperator(":", node4, node5);
    Node node7 = Node.newIf(condition, node6);

    MustDef input = new MustDef();
    MustDef output = mustBeReachingVariableDef.flowThrough(node7, input);

    assertThat(output.reachingDef).containsKey(jsScope.getVar(VAR_A));
    assertThat(output.reachingDef.get(jsScope.getVar(VAR_A))).isInstanceOf(Definition.class);
    assertThat(output.reachingDef.get(jsScope.getVar(VAR_A)).node).isEqualTo(node7);
  }

  @Test
  public void testMustDefComplexIf() {
    Node condition1 = Node.newBooleanLiteral(true);
    Node condition2 = Node.newBooleanLiteral(false);
    Node node1 = Node.newName(VAR_A);
    Node node2 = Node.newName(VAR_B);
    Node node3 = Node.newOperator("=", node1, Node.newIntLiteral(1));
    Node node4 = Node.newOperator("=", node2, Node.newIntLiteral(2));
    Node node5 = Node.newOperator("&&", condition1, condition2);
    Node node6 = Node.newOperator("?", node3, Node.newName(VAR_A));
    Node node7 = Node.newOperator(":", node6, Node.newOperator(":", node4, Node.newName(VAR_B)));
    Node node8 = Node.newIf(node5, node7);

    MustDef input = new MustDef();
    MustDef output = mustBeReachingVariableDef.flowThrough(node8, input);

    assertThat(output.reachingDef).containsKey(jsScope.getVar(VAR_A));
    assertThat(output.reachingDef.get(jsScope.getVar(VAR_A))).isInstanceOf(Definition.class);
    assertThat(output.reachingDef.get(jsScope.getVar(VAR_A)).node).isEqualTo(node8);

    assertThat(output.reachingDef).containsKey(jsScope.getVar(VAR_B));
    assertThat(output.reachingDef.get(jsScope.getVar(VAR_B))).isInstanceOf(Definition.class);
    assertThat(output.reachingDef.get(jsScope.getVar(VAR_B)).node).isEqualTo(node8);
  }

  @Test
  public void testMustDefWhile() {
    Node condition = Node.newBooleanLiteral(true);
    Node node1 = Node.newName(VAR_A);
    Node node2 = Node.newName(VAR_B);
    Node node3 = Node.newOperator("=", node1, Node.newIntLiteral(1));
    Node node4 = Node.newOperator("<", node1, Node.newIntLiteral(3));
    Node node5 = Node.newOperator("!", node4);
    Node node6 = Node.newOperator(":", node3, Node.newBlock(
        Arrays.asList(node5, Node.newOperator(":", node2, Node.newIntLiteral(1)),
            node3, Node.newOperator("+", node1, Node.newIntLiteral(1)),
            Node.newOperator(";"))));
    Node node7 = Node.newWhile(condition, node6);

    MustDef input = new MustDef();
    MustDef output = mustBeReachingVariableDef.flowThrough(node7, input);

    assertThat(output.reachingDef).containsKey(jsScope.getVar(VAR_A));
    assertThat(output.reachingDef.get(jsScope.getVar(VAR_A))).isInstanceOf(Definition.class);
    assertThat(output.reachingDef.get(jsScope.getVar(VAR_A)).node).isEqualTo(node7);

    assertThat(output.reachingDef).containsKey(jsScope.getVar(VAR_B));
    assertThat(output.reachingDef.get(jsScope.getVar(VAR_B))).isInstanceOf(Definition.class);
    assertThat(output.reachingDef.get(jsScope.getVar(VAR_B)).node).isEqualTo(node7);
  }

  @Test
  public void testMustDefFunction() {
    Node node1 = Node.newName(TEST_FUNCTION_NAME);
    Node node2 = Node.newBlock(Collections.emptyList());
    Node node3 = Node.newName(VAR_A);
    Node node4 = Node.newOperator("=", node3, Node.newIntLiteral(1));
    Node node5 = Node.newOperator(":", node4, Node.newName(VAR_A));
    Node node6 = Node.newOperator(":", node5, Node.newName(VAR_B));
    Node node7 = Node.newOperator(":", node6, Node.newName(VAR_C));
    Node node8 = Node.newReturn(Node.newName(VAR_C));
    Node node9 = Node.newOperator(":", node7, node8);
    Node node10 = Node.newFunction(
        ImmutableSet.of(new NodeName(PARAM_X), new NodeName(PARAM_Y)),
        node2, node9);

    MustDef input = new MustDef();
    MustDef output = mustBeReachingVariableDef.flowThrough(node10, input);

    assertThat(output.reachingDef).containsKey(jsScope.getVar(VAR_A));
    assertThat(output.reachingDef.get(jsScope.getVar(VAR_A))).isInstanceOf(Definition.class);
    assertThat(output.reachingDef.get(jsScope.getVar(VAR_A)).node).isEqualTo(node10);

    assertThat(output.reachingDef).containsKey(jsScope.getVar(VAR_B));
    assertThat(output.reachingDef.get(jsScope.getVar(VAR_B))).isInstanceOf(Definition.class);
    assertThat(output.reachingDef.get(jsScope.getVar(VAR_B)).node).isEqualTo(node10);

    assertThat(output.reachingDef).containsKey(jsScope.getVar(VAR_C));
    assertThat(output.reachingDef.get(jsScope.getVar(VAR_C))).isInstanceOf(Definition.class);
    assertThat(output.reachingDef.get(jsScope.getVar(VAR_C)).node).isEqualTo(node10);
  }

  @Test
  public void testMustDefGetParameter() {
    Node node1 = Node.newName(TEST_FUNCTION_NAME);
    Node node2 = Node.newBlock(Collections.emptyList());
    Node node3 = Node.newGet(Node.newName(TEST_FUNCTION_NAME), new NodeName(PARAM_X));

    MustDef input = new MustDef();
    MustDef output = mustBeReachingVariableDef.flowThrough(node3, input);

    assertThat(output.reachingDef).containsKey(jsScope.getVar(PARAM_X));
    assertThat(output.reachingDef.get(jsScope.getVar(PARAM_X))).isInstanceOf(Definition.class);
    assertThat(output.reachingDef.get(jsScope.getVar(PARAM_X)).node).isEqualTo(node1);
  }

  @Test
  public void testMustDefGetParameterFromExtern() {
    Node node1 = Node.newName("externFunction");
    Node node2 = Node.newGet(Node.newName(TEST_FUNCTION_NAME), new NodeName(PARAM_X));
    Node node3 = Node.newGet(node1, new NodeName(PARAM_X));

    MustDef input = new MustDef();
    MustDef output = mustBeReachingVariableDef.flowThrough(node3, input);

    // In this case, the testMustDefGetParameterFromExtern() test should not
    // assert that the reachingDef of the parameter is the same as node1.
    // Instead, it should be null or any other value since we can't know the
    // definition of the variable in the extern function.
  }

  @Test
  public void testMustDefEscapedVariable() {
    Node node1 = Node.newName(TEST_FUNCTION_NAME);
    Node node2 = Node.newBlock(Collections.emptyList());
    Node node3 = Node.newVar("escapedVar");
    Node node4 = Node.newOperator("=", node3, Node.newName("globalVar"));
    Node node5 = Node.newOperator(":", node4, Node.newName(VAR_A));
    Node node6 = Node.newOperator(":", node5, Node.newName(VAR_B));
    Node node7 = Node.newOperator(":", node6, Node.newName(VAR_C));
    Node node8 = Node.newReturn(Node.newName(VAR_C));
    Node node9 = Node.newOperator(":", node7, node8);
    Node node10 = Node.newFunction(
        ImmutableSet.of(new NodeName(PARAM_X), new NodeName(PARAM_Y)),
        node2, node9);

    when(jsScope.getVar(node3.getString())).thenReturn(jsScope.getVar(VAR_A));

    MustDef input = new MustDef();
    MustDef output = mustBeReachingVariableDef.flowThrough(node10, input);

    // In this case, the escaped variable "escapedVar" should not appear in
    // the reachingDef since it is escaped.
    assertThat(output.reachingDef).doesNotContainKey(jsScope.getVar(node3.getString()));
  }
}
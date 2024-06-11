package com.google.javascript.jscomp;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class MustBeReachingVariableDefTest {
  private ControlFlowGraph<Node> cfg;
  private Scope jsScope;
  private AbstractCompiler compiler;
  private MustBeReachingVariableDef analyzer;

  @Before
  public void setup() {
    jsScope = mock(Scope.class);
    compiler = mock(AbstractCompiler.class);
    cfg = mock(ControlFlowGraph.class);
    when(cfg.getNode(NodeUtil.getConditionExpression(Token.NEW)))
        .thenReturn(ControlFlowGraph.newNode());

    analyzer = new MustBeReachingVariableDef(cfg, jsScope, compiler);
  }

  @Test
  public void testCreateEntryLattice() {
    MustDef entryLattice = analyzer.createEntryLattice();
    assertNull(entryLattice.reachingDef.get("someVar"));

    Iterator<Var> vars = jsScope.getVars().iterator();
    while (vars.hasNext()) {
      Var var = vars.next();
      Definition def = new Definition(var.scope.getRootNode());
      entryLattice.reachingDef.put(var, def);
    }

    Map<Var, Definition> expected = Maps.newHashMap();
    expected.put(jsScope.getVar("someVar"), new Definition(Node.NULL));
    assertSame(expected, entryLattice.reachingDef);
  }

  @Test
  public void testFlowThroughSimpleAssignments() {
    Node varNode = NodeUtil.newNameNode("someVar");
    Node valueNode = NodeUtil.newNumberNode(1);

    MustDef input = new MustDef();
    MustDef output = analyzer.flowThrough(varNode, input);

    Definition expectedDefinition = new Definition(varNode);
    output.reachingDef.put(jsScope.getVar("someVar"), expectedDefinition);
    assertSame(expectedDefinition, output.reachingDef.get("someVar"));
  }

  @Test
  public void testFlowThroughAssignmentsWithDependencies() {
    Node xNode = NodeUtil.newNameNode("x");
    Node yNode = NodeUtil.newNameNode("y");
    Node zNode = NodeUtil.newNameNode("z");
    Node assignmentNode = NodeUtil.newAssignment(xNode, NodeUtil.newNameNode("y"));

    MustDef input = new MustDef();
    MustDef output = analyzer.flowThrough(assignmentNode, input);

    Definition expectedDefinition = new Definition(assignmentNode);
    expectedDefinition.depends.add(jsScope.getVar("y"));
    output.reachingDef.put(jsScope.getVar("x"), expectedDefinition);
    assertSame(expectedDefinition, output.reachingDef.get("x"));
  }

  @Test
  public void testFlowThroughComplexExpressions() {
    Node aNode = NodeUtil.newNameNode("a");
    Node bNode = NodeUtil.newNameNode("b");
    Node cNode = NodeUtil.newNameNode("c");
    Node xNode = NodeUtil.newNameNode("x");
    Node assignmentNode = NodeUtil.newAssignment(xNode, NodeUtil.newBinaryOp(
        NodeUtil.newBinaryOp(aNode, NodeUtil.op("+")), bNode));

    MustDef input = new MustDef();
    MustDef output = analyzer.flowThrough(assignmentNode, input);

    Definition aDefinition = new Definition(aNode);
    Definition bDefinition = new Definition(bNode);
    Definition xDefinition = new Definition(assignmentNode);
    Definition expectedDefinition = new Definition(Node.NULL);

    xDefinition.depends.addAll(aDefinition.depends);
    xDefinition.depends.addAll(bDefinition.depends);

    output.reachingDef.put(jsScope.getVar("x"), xDefinition);
    output.reachingDef.put(jsScope.getVar("a"), aDefinition);
    output.reachingDef.put(jsScope.getVar("b"), bDefinition);

    assertSame(expectedDefinition, output.reachingDef.get("a"));
    assertSame(expectedDefinition, output.reachingDef.get("b"));
    assertSame(xDefinition, output.reachingDef.get("x"));
  }
}
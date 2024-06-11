package org.apache.commons.cli; // for consistency with the given example

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.isNull;
import static org.junit.Assert.assertNotNull;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.graph.DiGraph;

import java.util.BitSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class LiveVariablesAnalysisTest {

  private static class TestScope implements Scope {
    private final String[] varNames;

    TestScope(String[] varNames) {
      this.varNames = varNames;
    }

    @Override
    public Var getVar(String name, boolean allowFunction) {
      for (String v : varNames) {
        if (v.equals(name)) {
          return new Var(-1, name);
        }
      }
      return null;
    }

    @Override
    public int getVarCount() {
      return varNames.length;
    }

    @Override
    public Node getRootNode() {
      return null;
    }
  }

  private static Node createTestNode(int type, Node... children) {
    Node node = new Node(type);
    for (Node child : children) {
      node.addChild(child);
    }
    return node;
  }

  private ControlFlowGraph<Node> cfg;
  private Scope jsScope;
  private AbstractCompiler compiler;
  private LiveVariablesAnalysis analysis;

  @Before
  public void setup() {
    List<Node> entryNodes = Sets.newArrayList();
    Node root = createTestNode(Token.SCRIPT, Token.BLOCK);
    Node testFunction = createTestNode(Token.FUNCTION, Node.newName("test"));
    entryNodes.add(testFunction);
    root.addChild(Node.newName("main"), entryNodes.toArray(new Node[0]));
    this.cfg = new ControlFlowGraph<>(root, new DiGraph.FactoryForTest());
    this.jsScope = new TestScope(new String[]{"a", "b", "c"});
    this.compiler = new AbstractCompiler(this.cfg, this.jsScope, null);
    this.analysis = new LiveVariablesAnalysis(this.cfg, this.jsScope, this.compiler);
  }

  @Test
  public void testCreateEntryLattice() {
    LiveVariablesAnalysis.LiveVariableLattice lattice = analysis.createEntryLattice();
    assertThat(lattice.liveSet).isEqualTo(Sets.bitSetOf(0, 1, 2));
  }

  @Test
  public void testCreateInitialEstimateLattice() {
    LiveVariablesAnalysis.LiveVariableLattice lattice = analysis.createInitialEstimateLattice();
    assertThat(lattice.liveSet).isEqualTo(Sets.bitSetOf(0, 1, 2));
  }

  @Test
  public void testIsForward() {
    assertFalse(analysis.isForward());
  }

  @Test
  public void testFlowThrough_Script() {
    LiveVariablesAnalysis.LiveVariableLattice lattice = analysis.flowThrough(createTestNode(Token.SCRIPT), new LiveVariablesAnalysis.LiveVariableLattice(jsScope.getVarCount()));
    assertThat(lattice.liveSet).isEqualTo(Sets.bitSetOf(0, 1, 2));
  }

  @Test
  public void testFlowThrough_Block() {
    LiveVariablesAnalysis.LiveVariableLattice lattice = analysis.flowThrough(createTestNode(Token.BLOCK), new LiveVariablesAnalysis.LiveVariableLattice(jsScope.getVarCount()));
    assertThat(lattice.liveSet).isEqualTo(Sets.bitSetOf(0, 1, 2));
  }

  @Test
  public void testFlowThrough_Function() {
    LiveVariablesAnalysis.LiveVariableLattice lattice = analysis.flowThrough(testFunction, new LiveVariablesAnalysis.LiveVariableLattice(jsScope.getVarCount()));
    assertThat(lattice.liveSet).isEqualTo(Sets.bitSetOf(0, 1, 2));
  }

  @Test
  public void testFlowThrough_While() {
    Node whileNode = createTestNode(Token.WHILE, createTestNode(Token.NAME, "condition"), Node.newName("body"));
    LiveVariablesAnalysis.LiveVariableLattice lattice = analysis.flowThrough(whileNode, new LiveVariablesAnalysis.LiveVariableLattice(jsScope.getVarCount()));
    assertThat(lattice.liveSet).isEqualTo(Sets.bitSetOf(0, 1, 2));
  }

  @Test
  public void testFlowThrough_If() {
    Node ifNode = createTestNode(Token.IF, createTestNode(Token.NAME, "condition"), createTestNode(Token.BLOCK), createTestNode(Token.NAME, "else"));
    LiveVariablesAnalysis.LiveVariableLattice lattice = analysis.flowThrough(ifNode, new LiveVariablesAnalysis.LiveVariableLattice(jsScope.getVarCount()));
    assertThat(lattice.liveSet).isEqualTo(Sets.bitSetOf(0, 1, 2));
  }

  @Test
  public void testFlowThrough_For() {
    Node forNode = createTestNode(Token.FOR, Node.newName("init"), createTestNode(Token.NAME, "condition"), Node.newName("body"));
    LiveVariablesAnalysis.LiveVariableLattice lattice = analysis.flowThrough(forNode, new LiveVariablesAnalysis.LiveVariableLattice(jsScope.getVarCount()));
    assertThat(lattice.liveSet).isEqualTo(Sets.bitSetOf(0, 1, 2));
  }

  @Test
  public void testFlowThrough_And() {
    Node andNode = createTestNode(Token.AND, createTestNode(Token.NAME, "condition1"), createTestNode(Token.NAME, "condition2"));
    LiveVariablesAnalysis.LiveVariableLattice lattice = analysis.flowThrough(andNode, new LiveVariablesAnalysis.LiveVariableLattice(jsScope.getVarCount()));
    assertThat(lattice.liveSet).isEqualTo(Sets.bitSetOf(0, 1, 2));
  }

  @Test
  public void testFlowThrough_Name() {
    Node nameNode = createTestNode(Token.NAME, "a");
    LiveVariablesAnalysis.LiveVariableLattice lattice = analysis.flowThrough(nameNode, new LiveVariablesAnalysis.LiveVariableLattice(jsScope.getVarCount()));
    assertTrue(lattice.liveSet.get(0));
    assertFalse(lattice.liveSet.get(1));
    assertFalse(lattice.liveSet.get(2));
  }

  @Test
  public void testFlowThrough_Assignment() {
    Node lhs = createTestNode(Token.NAME, "a");
    Node rhs = createTestNode(Token.NAME, "b");
    Node assignNode = createTestNode(Token.ASSIGN, lhs, rhs);
    LiveVariablesAnalysis.LiveVariableLattice lattice = analysis.flowThrough(assignNode, new LiveVariablesAnalysis.LiveVariableLattice(jsScope.getVarCount()));
    assertTrue(lattice.liveSet.get(0));
    assertTrue(lattice.liveSet.get(1));
    assertFalse(lattice.liveSet.get(2));
  }

  @Test
  public void testFlowThrough_ArgumentsName() {
    Node argumentsNameNode = createTestNode(Token.NAME, "arguments");
    LiveVariablesAnalysis.LiveVariableLattice lattice = analysis.flowThrough(argumentsNameNameNode, new LiveVariablesAnalysis.LiveVariableLattice(jsScope.getVarCount()));
    assertNotNull(analysis.getEscapedLocals());
    assertTrue(analysis.getEscapedLocals().contains(jsScope.getVar("arguments")));
    assertFalse(lattice.liveSet.get(0));
    assertFalse(lattice.liveSet.get(1));
    assertTrue(lattice.liveSet.get(2));
  }
}
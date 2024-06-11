import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.ControlFlowGraph.AbstractCfgNodeTraversalCallback;
import com.google.javascript.jscomp.ControlFlowGraph.Branch;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

public class MustBeReachingVariableDefTest {

  private ControlFlowGraph<Node> cfg;
  private Scope jsScope;
  private AbstractCompiler compiler;

  @Before
  public void setup() {
    jsScope = mock(Scope.class);
    compiler = mock(AbstractCompiler.class);
    cfg = new ControlFlowGraph<>(null);
    new MustBeReachingVariableDef(cfg, jsScope, compiler);
  }

  @Test
  public void testEmpty() {
    MustDef mustDef = new MustDef();
    assertSame(mustDef, mustDef.join(mustDef));
  }

  @Test
  public void testJoin() {
    MustDef a = new MustDef();
    MustDef b = new MustDef();

    // Joining two MustDefs with no overlapping variables.
    MustDef result = a.join(b);
    assertSame(a, result);

    // Joining two MustDefs with overlapping variables.
    a.reachingDef.put(new Var("x"), new Definition(new Node(Token.NAME, "x")));
    b.reachingDef.put(new Var("x"), new Definition(new Node(Token.NAME, "y")));
    result = a.join(b);
    assertNull(result.reachingDef.get(new Var("x")));
    assertSame(a.reachingDef.get(new Var("x")), result.reachingDef.get(new Var("x")));
  }

  @Test
  public void testFlowThrough() {
    Node assignmentNode = new Node(Token.ASSIGN, null);
    Node useNode = new Node(Token.NAME, "x");

    MustDef input = new MustDef();
    MustDef output = new MustDef();

    new MustBeReachingVariableDef(cfg, jsScope, compiler).flowThrough(assignmentNode, input, output);
    assertSame(input, output);

    input.reachingDef.put(new Var("x"), new Definition(assignmentNode));
    output = new MustDef();
    new MustBeReachingVariableDef(cfg, jsScope, compiler).flowThrough(assignmentNode, input, output);
    assertSame(input, output);

    input.reachingDef.put(new Var("x"), null);
    output = new MustDef();
    new MustBeReachingVariableDef(cfg, jsScope, compiler).flowThrough(assignmentNode, input, output);
    assertNull(output.reachingDef.get(new Var("x")));
  }

  @Test
  public void testComputeMustDef() {
    Node assignmentNode = new Node(Token.ASSIGN, null);
    Node useNode = new Node(Token.NAME, "x");

    MustDef output = new MustDef();
    new MustBeReachingVariableDef(cfg, jsScope, compiler).computeMustDef(assignmentNode, useNode, output, false);
    assertNull(output.reachingDef.get(new Var("x")));

    output = new MustDef();
    Definition def = new Definition(assignmentNode);
    output.reachingDef.put(new Var("x"), def);
    new MustBeReachingVariableDef(cfg, jsScope, compiler).computeMustDef(assignmentNode, useNode, output, false);
    assertSame(def, output.reachingDef.get(new Var("x")));
  }

  @Test
  public void testGetDef() {
    Node assignmentNode = new Node(Token.ASSIGN, null);
    Node useNode = new Node(Token.NAME, "x");

    Definition definition = new Definition(assignmentNode);
    MustDef mustDef = new MustDef();
    mustDef.reachingDef.put(new Var("x"), definition);

    when(cfg.getNode(useNode)).thenReturn(new GraphNode<>(useNode, null));
    when(cfg.getNode(assignmentNode)).thenReturn(new GraphNode<>(assignmentNode, null));

    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(cfg, jsScope, compiler);
    assertSame(assignmentNode, analyzer.getDef("x", useNode));
    assertSame(definition, analyzer.getDef("x", useNode).reachingDef.get(new Var("x")));
  }

  @Test
  public void testGetDef_dependsOnOuterScopeVars() {
    Node useNode = new Node(Token.NAME, "x");

    when(jsScope.getVar("x")).thenReturn(new Var("x", null, new Node(Token.NAME, "x"), null));
    when(jsScope.getVars().hasNext()).thenReturn(true);
    when(jsScope.getNextVar()).thenReturn(new Var("outerScopeVar", null, new Node(Token.NAME, "outerScopeVar"), null));

    MustDef mustDef = new MustDef();
    mustDef.reachingDef.put(new Var("x"), null);
    mustDef.reachingDef.put(new Var("outerScopeVar"), new Definition(useNode));

    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(cfg, jsScope, compiler);
    assertNull(analyzer.getDef("x", useNode));
  }

  @Test
  public void testEscapeParameters() {
    MustDef input = new MustDef();
    MustDef output = new MustDef();

    for (Var v : Sets.newHashSet(new Var("p1"), new Var("p2"))) {
      input.reachingDef.put(v, new Definition(new Node(Token.NAME, v.getName())));
    }

    new MustBeReachingVariableDef(cfg, jsScope, compiler).escapeParameters(output);
    assertNull(output.reachingDef.get(new Var("p1")));
    assertNull(output.reachingDef.get(new Var("p2")));
  }
}

class Definition {
  Node node;
  Set<Var> depends = Sets.newHashSet();

  Definition(Node node) {
    this.node = node;
  }

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof Definition)) {
      return false;
    }
    Definition otherDef = (Definition) other;
    return otherDef.node == node;
  }
}
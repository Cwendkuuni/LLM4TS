import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowAnalysis;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.GraphReachability;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Token;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UnreachableCodeEliminationTest {

  @SuppressWarnings("unchecked")
  private static <N extends Node> ControlFlowGraph<N> createMockControlFlowGraph() {
    ControlFlowGraph<Node> mockCfg = mock(ControlFlowGraph.class);
    when(mockCfg.getCfg()).thenReturn(mockCfg);
    when(mockCfg.getEntry()).thenReturn(mock(DiGraphNode.class));
    when(mockCfg.getDirectedGraphNode(MockNode.class)).thenAnswer(
        i -> mock(DiGraphNode.class));
    return mockCfg;
  }

  private static class MockCompiler extends AbstractCompiler {
    @Override
    public ControlFlowAnalysis createControlFlowAnalysis(
        boolean optimizationPass, boolean enableTypeChecking) {
      return mockControlFlowAnalysis();
    }
  }

  private static ControlFlowAnalysis mockControlFlowAnalysis() {
    ControlFlowAnalysis mockCfa = mock(ControlFlowAnalysis.class);
    when(mockCfa.process(null, MockNode.class)).thenReturn(null);
    when(mockCfa.getCfg()).thenReturn(createMockControlFlowGraph());
    return mockCfa;
  }

  private static class MockNode extends Node {
    @Override
    public Node getParent() {
      return null;
    }

    @Override
    public Token getType() {
      return Token.INVALID;
    }

    @Override
    public Node getLastChild() {
      return null;
    }

    @Override
    public boolean isFunction() {
      return false;
    }

    @Override
    public boolean isScript() {
      return false;
    }
  }

  private static class MockFunctionNode extends Node {
    @Override
    public Node getLastChild() {
      return null;
    }

    @Override
    public boolean isFunction() {
      return true;
    }
  }

  @org.junit.Test
  public void testUnreachableCodeElimination_BasicFunctionality() {
    AbstractCompiler compiler = new MockCompiler(new CompilerOptions());
    Node root = new MockNode();
    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, false);
    eliminator.process(null, root);
    assertFalse(eliminator.codeChanged);
  }

  @org.junit.Test
  public void testUnreachableCodeElimination_RemoveDeadCodeAfterReturn() {
    AbstractCompiler compiler = new MockCompiler(new CompilerOptions());
    Node root = new MockFunctionNode();
    root.addChild(new NodeOperation("return", null, null));
    root.addChild(new NodeOperation("alert", null, null));

    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, false);
    eliminator.process(null, root);
    assertSame(root.getLastChild(), null);
  }

  @org.junit.Test
  public void testUnreachableCodeElimination_RemoveNoOpStatement() {
    AbstractCompiler compiler = new MockCompiler(new CompilerOptions());
    Node root = new MockNode();
    root.addChild(new NodeOperation("a.b.MyClass.prototype.propertyName", null, null));

    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, true);
    eliminator.process(null, root);
    assertSame(root.getLastChild(), null);
  }

  @org.junit.Test
  public void testUnreachableCodeElimination_RemoveUnconditionalBranch() {
    AbstractCompiler compiler = new MockCompiler(new CompilerOptions());
    Node root = new MockNode();
    root.addChild(new NodeOperation("if (true)", null, null));
    root.addChild(new NodeOperation("break", null, null));
    root.addChild(new NodeOperation("return", null, null));

    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, false);
    eliminator.process(null, root);
    assertSame(root.getLastChild(), null);
  }

  @org.junit.Test
  public void testUnreachableCodeElimination_DoNotRemoveFunctionBody() {
    AbstractCompiler compiler = new MockCompiler(new CompilerOptions());
    Node root = new MockFunctionNode();

    root.addChild(new NodeOperation("function foo() {", null, null));
    root.addChild(new NodeOperation("}", null, null));

    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, false);
    eliminator.process(null, root);
    assertSame(root.getLastChild(), null);
    // Function body should not be removed
    assertSame(root.getFirstChild(), root.getFirstChild());
  }

  @org.junit.Test
  public void testUnreachableCodeElimination_DoNotRemoveVarDeclaration() {
    AbstractCompiler compiler = new MockCompiler(new CompilerOptions());
    Node root = new NodeOperation("var x", null, null);

    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, false);
    eliminator.process(null, root);
    assertSame(root, null);
  }

  @org.junit.Test
  public void testUnreachableCodeElimination_RemoveBlockWithNoChildren() {
    AbstractCompiler compiler = new MockCompiler(new CompilerOptions());
    Node root = new NodeOperation("{};", null, null);

    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, false);
    eliminator.process(null, root);
    assertSame(root, null);
  }

  @org.junit.Test
  public void testUnreachableCodeElimination_RemoveCatchBlock() {
    AbstractCompiler compiler = new MockCompiler(new CompilerOptions());
    Node root = new MockNode();
    root.addChild(new NodeOperation("try {", null, null));
    root.addChild(new NodeOperation("catch (e) {", null, null));
    root.addChild(new NodeOperation("}", null, null));

    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, false);
    eliminator.process(null, root);
    assertSame(root.getLastChild(), null);
  }

  @org.junit.Test
  public void testUnreachableCodeElimination_RemoveBreakOrContinue() {
    AbstractCompiler compiler = new MockCompiler(new CompilerOptions());
    Node root = new MockNode();
    root.addChild(new NodeOperation("for (;;)", null, null));
    root.addChild(new NodeOperation("break", null, null));

    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, false);
    eliminator.process(null, root);
    assertSame(root.getLastChild(), null);
  }

  @org.junit.Test
  public void testUnreachableCodeElimination_RemoveUnreachableNodeByFollowingNode() {
    AbstractCompiler compiler = new MockCompiler(new CompilerOptions());
    Node root = new MockNode();
    Node current = root;
    Map<Node, Node> nodes = new HashMap<>();
    nodes.put(current, current);

    for (int i = 0; i < 10; i++) {
      current.addChild(new NodeOperation("node" + i, null, null));
      current = current.getLastChild();
      nodes.put(current, current);
    }

    // Remove the last node
    root.addChild(new NodeOperation("remove", null, null));
    root.addChild(new NodeOperation("node9", null, null));

    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, false);
    eliminator.process(null, root);
    assertNull(nodes.get(nodes.size() - 1));
  }

  @org.junit.Test
  public void testUnreachableCodeElimination_RemoveUnreachableNodeByFollowingEdge() {
    AbstractCompiler compiler = new MockCompiler(new CompilerOptions());
    Node root = new MockNode();
    Node current = root;
    Map<Node, Node> nodes = new HashMap<>();
    nodes.put(current, current);

    for (int i = 0; i < 10; i++) {
      current.addChild(new NodeOperation("node" + i, null, null));
      current = current.getLastChild();
      nodes.put(current, current);
    }

    // Create a loop
    current.addChild(nodes.get(3));

    // Remove the last node
    root.addChild(new NodeOperation("remove", null, null));
    root.addChild(new NodeOperation("node9", null, null));

    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, false);
    eliminator.process(null, root);
    assertNull(nodes.get(nodes.size() - 1));
  }
}
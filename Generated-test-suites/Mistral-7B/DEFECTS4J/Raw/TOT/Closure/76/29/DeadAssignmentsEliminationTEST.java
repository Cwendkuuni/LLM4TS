import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPostOrderCallback;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.DataFlowAnalysis;
import com.google.javascript.jscomp.DeadAssignmentsElimination;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.graph.DiGraph;
import com.google.javascript.rhino.NodeUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DeadAssignmentsEliminationTest {

  @Mock
  private AbstractCompiler compiler;

  @Mock
  private ControlFlowGraph<Node> controlFlowGraph;

  @Mock
  private DataFlowAnalysis dataFlowAnalysis;

  @Mock
  private NodeTraversal nodeTraversal;

  @Mock
  private Scope scope;

  @InjectMocks
  private DeadAssignmentsElimination deadAssignmentsElimination;

  @Test
  public void testDeadAssignmentElimination_SimpleCase() {
    // setup
    Node root = new Node(Token.PROGRAM, null);
    Node var1 = new Node(Token.VAR, "x", null);
    Node assign1 = new Node(Token.ASSIGN, var1, new Node(Token.NUMBER, 1));
    Node assign2 = new Node(Token.ASSIGN, var1, new Node(Token.NUMBER, 2));

    root.addChild(var1);
    root.addChild(assign1);
    root.addChild(assign2);

    when(compiler.getControlFlowGraph()).thenReturn(controlFlowGraph);
    when(controlFlowGraph.getDirectedGraphNodes()).thenReturn(mock(Iterable.class));

    // test
    deadAssignmentsElimination.process(null, root);

    // verify
    Node actualRoot = controlFlowGraph.getEntryNodeValue();
    Preconditions.checkState(actualRoot == root, "Unexpected control flow graph.");
    Preconditions.checkState(
        !NodeUtil.has(actualRoot, DeadAssignmentsElimination.matchRemovableAssigns),
        "Unexpected dead assignment remaining.");
  }

  @Test
  public void testDeadAssignmentElimination_NoDeadAssignments() {
    // setup
    Node root = new Node(Token.PROGRAM, null);
    Node var1 = new Node(Token.VAR, "x", null);
    Node assign1 = new Node(Token.ASSIGN, var1, new Node(Token.NUMBER, 1));
    Node assign2 = new Node(Token.ASSIGN, var1, new Node(Token.NUMBER, 2));
    Node print = new Node(Token.CALL, NodeUtil.createQualifiedIdentifier("print"),
        Node.newList(var1));

    root.addChild(var1);
    root.addChild(assign1);
    root.addChild(assign2);
    root.addChild(print);

    when(compiler.getControlFlowGraph()).thenReturn(controlFlowGraph);
    when(controlFlowGraph.getDirectedGraphNodes()).thenReturn(mock(Iterable.class));

    // test
    deadAssignmentsElimination.process(null, root);

    // verify
    Node actualRoot = controlFlowGraph.getEntryNodeValue();
    Preconditions.checkState(actualRoot == root, "Unexpected control flow graph.");
    Preconditions.checkState(
        NodeUtil.has(actualRoot, DeadAssignmentsElimination.matchRemovableAssigns),
        "Unexpected dead assignments removed.");
    Node actualAssign2 = NodeUtil.findChildByType(actualRoot, Token.ASSIGN);
    Preconditions.checkState(actualAssign2 != null, "Unexpected missing assignment.");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDeadAssignmentElimination_NullCompiler() {
    // setup
    Node root = new Node(Token.PROGRAM, null);
    DeadAssignmentsElimination deadAssignmentsElimination = new DeadAssignmentsElimination(null);

    // test
    deadAssignmentsElimination.process(null, root);
  }
}
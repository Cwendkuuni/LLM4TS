import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowAnalysis;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.DiGraph;
import com.google.javascript.jscomp.DiGraphEdge;
import com.google.javascript.jscomp.DiGraphNode;
import com.google.javascript.jscomp.GraphReachability;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Token;

public class UnreachableCodeEliminationTest {

 private AbstractCompiler compiler;
 private ControlFlowAnalysis controlFlowAnalysis;
 private ControlFlowGraph<Node> controlFlowGraph;
 private GraphReachability<Node, ControlFlowGraph.Branch> graphReachability;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 compiler = mock(AbstractCompiler.class);
 controlFlowAnalysis = mock(ControlFlowAnalysis.class);
 controlFlowGraph = mock(ControlFlowGraph.class);
 graphReachability = mock(GraphReachability.class);
 when(compiler.getControlFlowAnalysis()).thenReturn(controlFlowAnalysis);
 when(controlFlowAnalysis.getCfg()).thenReturn(controlFlowGraph);
 when(graphReachability.compute(controlFlowGraph, null)).thenReturn(graphReachability);
 }

 @Test
 public void testConstructor() {
 UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);
 assertSame(compiler, elimination.compiler);
 assertFalse(elimination.removeNoOpStatements);
 }

 @Test
 public void testEnterScope() {
 NodeTraversal nodeTraversal = mock(NodeTraversal.class);
 Scope scope = mock(Scope.class);
 when(scope.getRootNode()).thenReturn(mock(Node.class));
 elimination.enterScope(nodeTraversal);
 assertSame(controlFlowGraph, elimination.curCfg);
 }

 @Test
 public void testExitScope() {
 NodeTraversal nodeTraversal = mock(NodeTraversal.class);
 elimination.exitScope(nodeTraversal);
 assertNull(elimination.curCfg);
 }

 @Test
 public void testProcess() {
 Node externs = mock(Node.class);
 Node root = mock(Node.class);
 elimination.process(externs, root);
 assertNull(elimination.curCfg);
 }

 @Test
 public void testVisit() {
 NodeTraversal nodeTraversal = mock(NodeTraversal.class);
 Node root = mock(Node.class);
 Node node = mock(Node.class);
 Node parent = mock(Node.class);
 when(parent.getType()).thenReturn(Token.SCRIPT);
 when(node.getType()).thenReturn(Token.RETURN);
 when(node.getParent()).thenReturn(parent);

 elimination.visit(nodeTraversal, node, parent);
 assertNull(node);
 }

 @Test
 public void testTryRemoveUnconditionalBranching() {
 Node node = mock(Node.class);
 DiGraphNode<Node, Branch> gNode = mock(DiGraphNode.class);
 when(gNode.getValue()).thenReturn(node);
 when(controlFlowGraph.getDirectedGraphNode(node)).thenReturn(gNode);
 when(curCfg.getEntry()).thenReturn(gNode);
 when(graphReachability.isReachable(gNode)).thenReturn(true);

 Node successor = mock(Node.class);
 DiGraphEdge<Node, Branch> edge = mock(DiGraphEdge.class);
 when(edge.getDestination()).thenReturn(gNode);
 when(edge.getValue()).thenReturn(Branch.UNCOND);
 when(curCfg.getOutEdges()).thenReturn(new ArrayList<DiGraphEdge<Node, Branch>>() {{
 add(edge);
 }});

 Node result = elimination.tryRemoveUnconditionalBranching(node);
 assertSame(successor, result);
 }

 @Test
 public void testRemoveDeadExprStatementSafely() {
 Node node = mock(Node.class);
 Node parent = mock(Node.class);
 elimination.removeDeadExprStatementSafely(node);
 assertNull(node);
 }
}
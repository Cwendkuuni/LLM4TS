import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.rhino.Node;
import com.google.javascript.jscomp.*;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.javascript.jscomp.ControlFlowGraph.Branch;
import com.google.javascript.jscomp.DataFlowAnalysis.FlowState;
import com.google.javascript.jscomp.LiveVariablesAnalysis.LiveVariableLattice;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.jscomp.NodeTraversal.ScopedCallback;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.graph.DiGraph.DiGraphNode;

public class DeadAssignmentsEliminationTest {
	
	@Test
	public void testProcess() {
		AbstractCompiler compiler = new AbstractCompiler();
		DeadAssignmentsElimination elimination = new DeadAssignmentsElimination(compiler);
		Node externs = new Node(Token.EMPTY);
		Node root = new Node(Token.EMPTY);
		elimination.process(externs, root);
		// Assert something
	}
	
	@Test
	public void testEnterScope_1() {
		AbstractCompiler compiler = new AbstractCompiler();
		DeadAssignmentsElimination elimination = new DeadAssignmentsElimination(compiler);
		NodeTraversal t = new NodeTraversal(compiler, null);
		elimination.enterScope(t);
		// Assert something
	}
	
	@Test
	public void testEnterScope_2() {
		AbstractCompiler compiler = new AbstractCompiler();
		DeadAssignmentsElimination elimination = new DeadAssignmentsElimination(compiler);
		NodeTraversal t = new NodeTraversal(compiler, null);
		Scope scope = new Scope(null);
		t.pushScope(scope);
		elimination.enterScope(t);
		// Assert something
	}
	
	@Test
	public void testExitScope() {
		AbstractCompiler compiler = new AbstractCompiler();
		DeadAssignmentsElimination elimination = new DeadAssignmentsElimination(compiler);
		NodeTraversal t = new NodeTraversal(compiler, null);
		elimination.exitScope(t);
		// Assert something
	}
	
	@Test
	public void testVisit() {
		AbstractCompiler compiler = new AbstractCompiler();
		DeadAssignmentsElimination elimination = new DeadAssignmentsElimination(compiler);
		NodeTraversal t = new NodeTraversal(compiler, null);
		Node n = new Node(Token.EMPTY);
		Node parent = new Node(Token.EMPTY);
		elimination.visit(t, n, parent);
		// Assert something
	}
	
	@Test
	public void testTryRemoveDeadAssignments() {
		AbstractCompiler compiler = new AbstractCompiler();
		DeadAssignmentsElimination elimination = new DeadAssignmentsElimination(compiler);
		NodeTraversal t = new NodeTraversal(compiler, null);
		ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>();
		elimination.tryRemoveDeadAssignments(t, cfg);
		// Assert something
	}
	
	@Test
	public void testTryRemoveAssignment_1() {
		AbstractCompiler compiler = new AbstractCompiler();
		DeadAssignmentsElimination elimination = new DeadAssignmentsElimination(compiler);
		NodeTraversal t = new NodeTraversal(compiler, null);
		Node n = new Node(Token.EMPTY);
		FlowState<LiveVariableLattice> state = new FlowState<LiveVariableLattice>();
		elimination.tryRemoveAssignment(t, n, n, state);
		// Assert something
	}
	
	@Test
	public void testTryRemoveAssignment_2() {
		AbstractCompiler compiler = new AbstractCompiler();
		DeadAssignmentsElimination elimination = new DeadAssignmentsElimination(compiler);
		NodeTraversal t = new NodeTraversal(compiler, null);
		Node n = new Node(Token.EMPTY);
		Node exprRoot = new Node(Token.EMPTY);
		FlowState<LiveVariableLattice> state = new FlowState<LiveVariableLattice>();
		elimination.tryRemoveAssignment(t, n, exprRoot, state);
		// Assert something
	}
	
	@Test
	public void testTryRemoveAssignment_3() {
		AbstractCompiler compiler = new AbstractCompiler();
		DeadAssignmentsElimination elimination = new DeadAssignmentsElimination(compiler);
		NodeTraversal t = new NodeTraversal(compiler, null);
		Node n = new Node(Token.FOR);
		Node exprRoot = new Node(Token.EMPTY);
		FlowState<LiveVariableLattice> state = new FlowState<LiveVariableLattice>();
		elimination.tryRemoveAssignment(t, n, exprRoot, state);
		// Assert something
	}
	
	@Test
	public void testIsVariableStillLiveWithinExpression() {
		AbstractCompiler compiler = new AbstractCompiler();
		DeadAssignmentsElimination elimination = new DeadAssignmentsElimination(compiler);
		Node n = new Node(Token.EMPTY);
		Node exprRoot = new Node(Token.EMPTY);
		String variable = "x";
		boolean result = elimination.isVariableStillLiveWithinExpression(n, exprRoot, variable);
		// Assert something
	}
	
	@Test
	public void testIsVariableReadBeforeKill() {
		AbstractCompiler compiler = new AbstractCompiler();
		DeadAssignmentsElimination elimination = new DeadAssignmentsElimination(compiler);
		Node n = new Node(Token.EMPTY);
		String variable = "x";
		VariableLiveness result = elimination.isVariableReadBeforeKill(n, variable);
		// Assert something
	}
	
}
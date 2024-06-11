
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Branch;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.DataFlowAnalysis;
import com.google.javascript.jscomp.LiveVariablesAnalysis;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.graph.DiGraph;
import com.google.javascript.rhino.Node;
import com.google.javascript.jscomp.CompilerPass;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.util.*;

public class DeadAssignmentsEliminationTest {
	private DeadAssignmentsElimination deadAssignmentsElimination;

	@Before
	public void setup() {
		AbstractCompiler compiler = new AbstractCompiler();
		deadAssignmentsElimination = new DeadAssignmentsElimination(compiler);
	}

	@Test
	public void testProcess() {
		Node externs = null;
		Node root = null;
		deadAssignmentsElimination.process(externs, root);
	}

	@Test
	public void testEnterScope() {
		NodeTraversal t = null;
		deadAssignmentsElimination.enterScope(t);
	}

	@Test
	public void testExitScope() {
		NodeTraversal t = null;
		deadAssignmentsElimination.exitScope(t);
	}

	@Test
	public void testVisit() {
		NodeTraversal t = null;
		Node n = null;
		Node parent = null;
		deadAssignmentsElimination.visit(t, n, parent);
	}

	@Test
	public void testTryRemoveDeadAssignments() {
		NodeTraversal t = null;
		ControlFlowGraph<Node> cfg = null;
		deadAssignmentsElimination.tryRemoveDeadAssignments(t, cfg);
	}

	@Test
	public void testTryRemoveAssignment() {
		NodeTraversal t = null;
		Node n = null;
		FlowState<LiveVariableLattice> state = null;
		deadAssignmentsElimination.tryRemoveAssignment(t, n, state);
	}

	@Test
	public void testIsVariableStillLiveWithinExpression() {
		Node n = null;
		Node exprRoot = null;
		String variable = null;
		deadAssignmentsElimination.isVariableStillLiveWithinExpression(n, exprRoot, variable);
	}

	@Test
	public void testIsVariableReadBeforeKill() {
		Node n = null;
		String variable = null;
		deadAssignmentsElimination.isVariableReadBeforeKill(n, variable);
	}
}

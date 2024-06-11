import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class MustBeReachingVariableDefTest {

	private MustBeReachingVariableDef mustBeReachingVariableDef;

	@Before
	public void setUp() {
		ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>();
		Scope jsScope = new Scope();
		AbstractCompiler compiler = new AbstractCompiler();
		mustBeReachingVariableDef = new MustBeReachingVariableDef(cfg, jsScope, compiler);
	}

	@Test
	public void testGetDef() {
		Node defNode = mustBeReachingVariableDef.getDef("name", new Node());
		assertEquals(null, defNode);
	}

	@Test
	public void testDependsOnOuterScopeVars() {
		boolean depends = mustBeReachingVariableDef.dependsOnOuterScopeVars("name", new Node());
		assertEquals(false, depends);
	}

}
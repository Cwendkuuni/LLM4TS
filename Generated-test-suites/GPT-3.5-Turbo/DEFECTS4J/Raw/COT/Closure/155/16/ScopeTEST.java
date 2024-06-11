import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class ScopeTest {

	private Scope scope;

	@Before
	public void setUp() {
		scope = new Scope(null, null);
	}

	@Test
	public void testDeclare() {
		String name = "testVar";
		JSType type = new JSType();
		CompilerInput input = new CompilerInput();
		Var var = scope.declare(name, null, type, input);
		assertEquals(name, var.getName());
		assertSame(type, var.getType());
		assertSame(scope, var.getScope());
		assertSame(input, var.getInput());
	}

	@Test
	public void testGetVar() {
		String name = "testVar";
		JSType type = new JSType();
		CompilerInput input = new CompilerInput();
		Var var = scope.declare(name, null, type, input);
		Var foundVar = scope.getVar(name);
		assertSame(var, foundVar);
	}

	@Test
	public void testGetVar_NotFound() {
		String name = "testVar";
		Var foundVar = scope.getVar(name);
		assertNull(foundVar);
	}

	@Test
	public void testGetVar_SearchParentScope() {
		String name = "testVar";
		JSType type = new JSType();
		CompilerInput input = new CompilerInput();
		Var var = scope.declare(name, null, type, input);
		Scope childScope = new Scope(scope, null);
		Var foundVar = childScope.getVar(name);
		assertSame(var, foundVar);
	}

	@Test
	public void testIsDeclared() {
		String name = "testVar";
		scope.declare(name, null, null, null);
		boolean isDeclared = scope.isDeclared(name, false);
		assertTrue(isDeclared);
	}

	@Test
	public void testIsDeclared_Recursive() {
		String name = "testVar";
		scope.declare(name, null, null, null);
		Scope childScope = new Scope(scope, null);
		boolean isDeclared = childScope.isDeclared(name, true);
		assertTrue(isDeclared);
	}

	@Test
	public void testIsDeclared_NotFound() {
		String name = "testVar";
		boolean isDeclared = scope.isDeclared(name, false);
		assertFalse(isDeclared);
	}

	@Test
	public void testIsDeclared_RecursiveNotFound() {
		String name = "testVar";
		Scope childScope = new Scope(scope, null);
		boolean isDeclared = childScope.isDeclared(name, true);
		assertFalse(isDeclared);
	}

	@Test
	public void testGetVars() {
		String name1 = "testVar1";
		JSType type1 = new JSType();
		CompilerInput input1 = new CompilerInput();
		Var var1 = scope.declare(name1, null, type1, input1);

		String name2 = "testVar2";
		JSType type2 = new JSType();
		CompilerInput input2 = new CompilerInput();
		Var var2 = scope.declare(name2, null, type2, input2);

		Iterator<Var> varsIterator = scope.getVars();
		assertTrue(varsIterator.hasNext());
		assertSame(var1, varsIterator.next());
		assertTrue(varsIterator.hasNext());
		assertSame(var2, varsIterator.next());
		assertFalse(varsIterator.hasNext());
	}

	@Test
	public void testGetVarCount() {
		scope.declare("testVar1", null, null, null);
		scope.declare("testVar2", null, null, null);
		assertEquals(2, scope.getVarCount());
	}

	@Test
	public void testIsGlobal() {
		assertTrue(scope.isGlobal());
	}

	@Test
	public void testIsLocal() {
		assertFalse(scope.isLocal());
	}
}
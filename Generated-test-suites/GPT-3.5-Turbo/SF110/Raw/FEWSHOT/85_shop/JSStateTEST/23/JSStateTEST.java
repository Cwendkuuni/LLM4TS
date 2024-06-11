package umd.cs.shop;

import org.junit.Assert;
import org.junit.Test;

public class JSStateTest {

	@Test
	public void testAddElementsToState() {
		JSListLogicalAtoms s = new JSListLogicalAtoms();
		s.addElement(new JSPredicateForm("predicate1"));
		
		JSState state = new JSState();
		state.addElementsToState(s);
		
		Assert.assertEquals(1, state.size());
		Assert.assertEquals("predicate1", state.elementAt(0).getName());
	}

	@Test
	public void testApply() {
		JSPlan pl = new JSPlan();
		
		JSState state = new JSState();
		JSState newState = state.apply(pl);
		
		Assert.assertNotNull(newState);
		Assert.assertEquals(0, newState.size());
	}

	@Test
	public void testApplyOp() {
		JSOperator op = new JSOperator();
		JSSubstitution alpha = new JSSubstitution();
		JSListLogicalAtoms addL = new JSListLogicalAtoms();
		JSListLogicalAtoms delL = new JSListLogicalAtoms();
		
		JSState state = new JSState();
		JSTState newState = state.applyOp(op, alpha, addL, delL);
		
		Assert.assertNotNull(newState);
		Assert.assertEquals(0, newState.getState().size());
		Assert.assertEquals(0, newState.getAddList().size());
		Assert.assertEquals(0, newState.getDeleteList().size());
	}

	@Test
	public void testSatisfies() {
		JSListLogicalAtoms conds = new JSListLogicalAtoms();
		JSSubstitution alpha = new JSSubstitution();
		JSListAxioms axioms = new JSListAxioms();
		
		JSState state = new JSState();
		JSSubstitution result = state.satisfies(conds, alpha, axioms);
		
		Assert.assertNotNull(result);
		Assert.assertTrue(result.isEmpty());
		Assert.assertFalse(result.isFailure());
	}

	@Test
	public void testSatisfiesAll() {
		JSListLogicalAtoms conds = new JSListLogicalAtoms();
		JSSubstitution alpha = new JSSubstitution();
		JSListAxioms axioms = new JSListAxioms();
		
		JSState state = new JSState();
		JSListSubstitution result = state.satisfiesAll(conds, alpha, axioms);
		
		Assert.assertNotNull(result);
		Assert.assertTrue(result.isEmpty());
	}

	@Test
	public void testSatisfiesTAm() {
		JSPredicateForm t = new JSPredicateForm("predicate");
		JSSubstitution alpha = new JSSubstitution();
		
		JSState state = new JSState();
		JSListSubstitution result = state.satisfiesTAm(t, alpha);
		
		Assert.assertNotNull(result);
		Assert.assertTrue(result.isEmpty());
	}

}
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class JSStateTest {

    @Test
    public void testAddElementsToState() {
        JSListLogicalAtoms atoms = new JSListLogicalAtoms();
        JSState state = new JSState();

        state.addElementsToState(atoms);
        assertEquals(0, state.size());

        atoms.addElement(new JSPredicateForm("atom1"));
        atoms.addElement(new JSPredicateForm("atom2"));
        atoms.addElement(new JSPredicateForm("atom3"));

        state.addElementsToState(atoms);
        assertEquals(3, state.size());
    }

    @Test
    public void testApply() {
        JSState state = new JSState();
        JSPlan plan = new JSPlan();

        JSState result = state.apply(plan);

        // assert the result is not null
        assertNotNull(result);

        // assert the result is an instance of JSState
        assertTrue(result instanceof JSState);
    }

    @Test
    public void testApplyOp() {
        JSState state = new JSState();
        JSOperator op = new JSOperator();
        JSSubstitution alpha = new JSSubstitution();
        JSListLogicalAtoms addL = new JSListLogicalAtoms();
        JSListLogicalAtoms delL = new JSListLogicalAtoms();

        JSTState tState = state.applyOp(op, alpha, addL, delL);

        // assert the result is not null
        assertNotNull(tState);

        // assert the result is an instance of JSTState
        assertTrue(tState instanceof JSTState);

        // assert the state in JSTState is not null
        assertNotNull(tState.state());

        // assert the addList in JSTState is not null
        assertNotNull(tState.addList());

        // assert the deleteList in JSTState is not null
        assertNotNull(tState.deleteList());
    }

    @Test
    public void testSatisfies() {
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();

        JSSubstitution result = new JSState().satisfies(conds, alpha, axioms);

        // assert the result is not null
        assertNotNull(result);

        // assert the result is an instance of JSSubstitution
        assertTrue(result instanceof JSSubstitution);

        // assert the result is not a failure
        assertFalse(result.isFailure());
    }

    @Test
    public void testSatisfiesAll() {
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();

        JSListSubstitution result = new JSState().satisfiesAll(conds, alpha, axioms);

        // assert the result is not null
        assertNotNull(result);

        // assert the result is an instance of JSListSubstitution
        assertTrue(result instanceof JSListSubstitution);
    }

    @Test
    public void testSatisfiesTAm() {
        JSPredicateForm t = new JSPredicateForm("predicate");
        JSSubstitution alpha = new JSSubstitution();

        JSListSubstitution result = new JSState().satisfiesTAm(t, alpha);

        // assert the result is not null
        assertNotNull(result);

        // assert the result is an instance of JSListSubstitution
        assertTrue(result instanceof JSListSubstitution);
    }
}
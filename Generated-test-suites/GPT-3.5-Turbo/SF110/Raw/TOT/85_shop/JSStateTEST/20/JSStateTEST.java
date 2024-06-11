import org.junit.Test;

import static org.junit.Assert.*;

public class JSStateTest {

    @Test
    public void testAddElementsToState() {
        JSListLogicalAtoms s1 = new JSListLogicalAtoms();
        s1.addElement(new JSPredicateForm("Atom1"));
        s1.addElement(new JSPredicateForm("Atom2"));
        s1.addElement(new JSPredicateForm("Atom3"));

        JSState state = new JSState();
        state.addElement(new JSPredicateForm("Atom4"));
        state.addElement(new JSPredicateForm("Atom5"));

        state.addElementsToState(s1);

        assertEquals(5, state.size());
        assertTrue(state.contains(new JSPredicateForm("Atom1")));
        assertTrue(state.contains(new JSPredicateForm("Atom2")));
        assertTrue(state.contains(new JSPredicateForm("Atom3")));
        assertTrue(state.contains(new JSPredicateForm("Atom4")));
        assertTrue(state.contains(new JSPredicateForm("Atom5")));
    }

    @Test
    public void testApply() {
        JSPlan plan = new JSPlan();

        JSState state = new JSState();
        JSState newState = state.apply(plan);

        assertNotNull(newState);
        assertEquals(0, newState.size());
    }

    @Test
    public void testApplyOp() {
        JSOperator op = new JSOperator();
        JSSubstitution alpha = new JSSubstitution();
        JSListLogicalAtoms addL = new JSListLogicalAtoms();
        JSListLogicalAtoms delL = new JSListLogicalAtoms();

        JSState state = new JSState();
        JSTState newState = state.applyOp(op, alpha, addL, delL);

        assertNotNull(newState);
        assertNotNull(newState.getNewState());
        assertNotNull(newState.getNewAddList());
        assertNotNull(newState.getNewDelList());
    }

    @Test
    public void testSatisfies() {
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();

        JSState state = new JSState();
        JSSubstitution satisfies = state.satisfies(conds, alpha, axioms);

        assertNotNull(satisfies);
    }

    @Test
    public void testSatisfiesAll() {
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();

        JSState state = new JSState();
        JSListSubstitution satisfiesAll = state.satisfiesAll(conds, alpha, axioms);

        assertNotNull(satisfiesAll);
    }

    @Test
    public void testSatisfiesTAm() {
        JSPredicateForm t = new JSPredicateForm("Goal");
        JSSubstitution alpha = new JSSubstitution();

        JSState state = new JSState();
        JSListSubstitution satisfiesTAm = state.satisfiesTAm(t, alpha);

        assertNotNull(satisfiesTAm);
    }
}
package umd.cs.shop;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class JSStateTest {

    @Test
    public void testAddElementsToState() {
        JSListLogicalAtoms s = new JSListLogicalAtoms();
        s.addElement("element1");
        s.addElement("element2");
        s.addElement("element3");
        JSState state = new JSState();

        state.addElementsToState(s);

        assertEquals(3, state.size());
        assertEquals("element1", state.elementAt(0));
        assertEquals("element2", state.elementAt(1));
        assertEquals("element3", state.elementAt(2));
    }

    @Test
    public void testApply() {
        JSPlan plan = new JSPlan();
        JSState state = new JSState();

        JSState newState = state.apply(plan);

        // Assert the implementation not yet implemented
        // assertEquals(expected, newState);
    }

    @Test
    public void testApplyOp() {
        JSOperator op = new JSOperator();
        JSSubstitution alpha = new JSSubstitution();
        JSListLogicalAtoms addL = new JSListLogicalAtoms();
        addL.addElement("addElement1");
        addL.addElement("addElement2");
        JSListLogicalAtoms delL = new JSListLogicalAtoms();
        delL.addElement("delElement1");
        delL.addElement("delElement2");
        JSState state = new JSState();

        JSTState newState = state.applyOp(op, alpha, addL, delL);

        // Assert the implementation
        // assertEquals(expected, newState);
    }

    @Test
    public void testSatisfies() {
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        conds.addElement("cond1");
        conds.addElement("cond2");
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();
        JSState state = new JSState();

        JSSubstitution satisfies = state.satisfies(conds, alpha, axioms);

        // Assert the implementation
        // assertEquals(expected, satisfies);
    }

    @Test
    public void testSatisfiesAll() {
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        conds.addElement("cond1");
        conds.addElement("cond2");
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();
        JSState state = new JSState();

        JSListSubstitution satisfiesAll = state.satisfiesAll(conds, alpha, axioms);

        // Assert the implementation
        // assertEquals(expected, satisfiesAll);
    }

    @Test
    public void testSatisfiesTAm() {
        JSPredicateForm t = new JSPredicateForm();
        JSSubstitution alpha = new JSSubstitution();
        JSState state = new JSState();

        JSListSubstitution satisfiesTAm = state.satisfiesTAm(t, alpha);

        // Assert the implementation
        // assertEquals(expected, satisfiesTAm);
    }
}
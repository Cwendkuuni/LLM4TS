package umd.cs.shop;

import org.junit.Test;
import static org.junit.Assert.*;

public class JSStateTest {

    @Test
    public void testAddElementsToState() {
        JSListLogicalAtoms s = new JSListLogicalAtoms();
        JSState state = new JSState();

        JSListLogicalAtoms element1 = new JSListLogicalAtoms();
        element1.addElement(new JSPredicateForm("element1"));

        JSListLogicalAtoms element2 = new JSListLogicalAtoms();
        element2.addElement(new JSPredicateForm("element2"));

        JSListLogicalAtoms element3 = new JSListLogicalAtoms();
        element3.addElement(new JSPredicateForm("element3"));

        s.addElement(element1);
        s.addElement(element2);
        s.addElement(element3);

        state.addElementsToState(s);

        assertEquals(3, state.size());
        assertEquals(element1, state.elementAt(0));
        assertEquals(element2, state.elementAt(1));
        assertEquals(element3, state.elementAt(2));
    }

    @Test
    public void testApply() {
        JSPlan pl = new JSPlan();
        JSState state = new JSState();

        JSState result = state.apply(pl);
        // Add assertions

    }

    @Test
    public void testApplyOp() {
        JSOperator op = new JSOperator();
        JSSubstitution alpha = new JSSubstitution();
        JSListLogicalAtoms addL = new JSListLogicalAtoms();
        JSListLogicalAtoms delL = new JSListLogicalAtoms();

        JSTState result = state.applyOp(op, alpha, addL, delL);
        // Add assertions

    }

    @Test
    public void testSatisfies() {
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();

        JSSubstitution result = state.satisfies(conds, alpha, axioms);
        // Add assertions

    }

    @Test
    public void testSatisfiesAll() {
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();

        JSListSubstitution result = state.satisfiesAll(conds, alpha, axioms);
        // Add assertions

    }

    @Test
    public void testSatisfiesTAm() {
        JSPredicateForm t = new JSPredicateForm();
        JSSubstitution alpha = new JSSubstitution();

        JSListSubstitution result = state.satisfiesTAm(t, alpha);
        // Add assertions

    }
}
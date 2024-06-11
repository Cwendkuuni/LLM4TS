

package umd.cs.shop;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class JSStateTest {

    @Test
    public void testAddElementsToState() {
        JSListLogicalAtoms s = new JSListLogicalAtoms();
        s.addElement(new JSPredicateForm("test"));
        JSState state = new JSState();
        state.addElement(new JSPredicateForm("foo"));
        state.addElement(new JSPredicateForm("bar"));
        state.addElementsToState(s);
        assertTrue(state.contains(new JSPredicateForm("foo")));
        assertTrue(state.contains(new JSPredicateForm("bar")));
        assertTrue(state.contains(new JSPredicateForm("test")));
    }

    @Test
    public void testApply() {
        JSPlan pl = new JSPlan();
        JSState state = new JSState();
        JSState ns = state.apply(pl);
        assertEquals(ns.size(), 0);
    }

    @Test
    public void testApplyOp() {
        JSOperator op = new JSOperator();
        JSSubstitution alpha = new JSSubstitution();
        JSListLogicalAtoms addL = new JSListLogicalAtoms();
        addL.addElement(new JSPredicateForm("add1"));
        addL.addElement(new JSPredicateForm("add2"));
        JSListLogicalAtoms delL = new JSListLogicalAtoms();
        delL.addElement(new JSPredicateForm("del1"));
        delL.addElement(new JSPredicateForm("del2"));
        JSTState tState = state.applyOp(op, alpha, addL, delL);
        assertFalse(tState.getNewState().contains(new JSPredicateForm("del1")));
        assertFalse(tState.getNewState().contains(new JSPredicateForm("del2")));
        assertTrue(tState.getNewState().contains(new JSPredicateForm("add1")));
        assertTrue(tState.getNewState().contains(new JSPredicateForm("add2")));
    }

    @Test
    public void testSatisfies() {
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        conds.addElement(new JSPredicateForm("cond1"));
        JSListAxioms axioms = new JSListAxioms();
        JSSubstitution alpha = new JSSubstitution();
        JSSubstitution answer = state.satisfies(conds, alpha, axioms);
        assertTrue(answer.fail());
    }

    @Test
    public void testSatisfiesAll() {
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        conds.addElement(new JSPredicateForm("cond1"));
        JSListAxioms axioms = new JSListAxioms();
        JSSubstitution alpha = new JSSubstitution();
        JSListSubstitution answers = state.satisfiesAll(conds, alpha, axioms);
        assertEquals(answers.size(), 0);
    }

    @Test
    public void testSatisfiesTAm() {
        JSPredicateForm t = new JSPredicateForm("test");
        JSSubstitution alpha = new JSSubstitution();
        JSListSubstitution answers = state.satisfiesTAm(t, alpha);
        assertEquals(answers.size(), 0);
    }
}

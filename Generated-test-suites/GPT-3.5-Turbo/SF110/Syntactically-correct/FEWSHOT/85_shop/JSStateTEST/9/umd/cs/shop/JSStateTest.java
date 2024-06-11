package umd.cs.shop;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class JSStateTest {
    @Test
    public void testAddElementsToState() {
        JSListLogicalAtoms s = new JSListLogicalAtoms();
        s.addElement(new JSPredicateForm("atom1"));
        s.addElement(new JSPredicateForm("atom2"));
        JSState state = new JSState();
        state.addElementsToState(s);
        assertEquals(2, state.size());
        assertEquals("atom1", state.elementAt(0).toString());
        assertEquals("atom2", state.elementAt(1).toString());
    }
    
    @Test
    public void testApply() {
        JSPlan pl = new JSPlan();
        JSState state = new JSState();
        JSState newState = state.apply(pl);
        // test the result of the apply method
    }
    
    @Test
    public void testApplyOp() {
        JSOperator op = new JSOperator();
        JSSubstitution alpha = new JSSubstitution();
        JSListLogicalAtoms addL = new JSListLogicalAtoms();
        JSListLogicalAtoms delL = new JSListLogicalAtoms();
        JSState state = new JSState();
        JSTState tstate = state.applyOp(op, alpha, addL, delL);
        // test the result of the applyOp method
    }
    
    @Test
    public void testSatisfies() {
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();
        JSState state = new JSState();
        JSSubstitution result = state.satisfies(conds, alpha, axioms);
        // test the result of the satisfies method
    }
    
    @Test
    public void testSatisfiesAll() {
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();
        JSState state = new JSState();
        JSListSubstitution result = state.satisfiesAll(conds, alpha, axioms);
        // test the result of the satisfiesAll method
    }
    
    @Test
    public void testSatisfiesTAm() {
        JSPredicateForm t = new JSPredicateForm();
        JSSubstitution alpha = new JSSubstitution();
        JSState state = new JSState();
        JSListSubstitution result = state.satisfiesTAm(t, alpha);
        // test the result of the satisfiesTAm method
    }
}
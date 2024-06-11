package umd.cs.shop;

import static org.junit.Assert.*;

import java.io.StreamTokenizer;

import org.junit.Test;

public class JSStateTest {

    JSState state;
    
    @Test
    public void testAddElementsToState() {
        state = new JSState();
        JSListLogicalAtoms s = new JSListLogicalAtoms();
        s.addElement(new JSPredicateForm());
        state.addElementsToState(s);
        assertEquals(1, state.size());
    }
    
    @Test
    public void testApply() {
        JSPlan pl = new JSPlan();
        state = new JSState();
        JSState result = state.apply(pl);
        assertNotNull(result);
    }
    
    @Test
    public void testApplyOp() {
        JSOperator op = new JSOperator();
        JSSubstitution alpha = new JSSubstitution();
        JSListLogicalAtoms addL = new JSListLogicalAtoms();
        JSListLogicalAtoms delL = new JSListLogicalAtoms();
        JSTState result = state.applyOp(op, alpha, addL, delL);
        assertNotNull(result);
    }
    
    @Test
    public void testSatisfies() {
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();
        JSSubstitution result = state.satisfies(conds, alpha, axioms);
        assertNotNull(result);
    }
    
    @Test
    public void testSatisfiesAll() {
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();
        JSListSubstitution result = state.satisfiesAll(conds, alpha, axioms);
        assertNotNull(result);
    }
    
    @Test
    public void testSatisfiesTAm() {
        JSPredicateForm t = new JSPredicateForm();
        JSSubstitution alpha = new JSSubstitution();
        JSListSubstitution result = state.satisfiesTAm(t, alpha);
        assertNotNull(result);
    }

}
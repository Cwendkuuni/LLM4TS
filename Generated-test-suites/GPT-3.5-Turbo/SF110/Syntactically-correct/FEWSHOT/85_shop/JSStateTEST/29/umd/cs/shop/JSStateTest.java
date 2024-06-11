package umd.cs.shop;

import org.junit.Test;
import static org.junit.Assert.*;

public class JSStateTest {
    
    @Test
    public void testAddElementsToState() {
        JSListLogicalAtoms s = new JSListLogicalAtoms();
        s.addElement(new JSPredicateForm());
        
        JSState state = new JSState();
        state.addElementsToState(s);
        
        assertEquals(1, state.size());
        assertEquals(s.elementAt(0), state.elementAt(0));
    }
    
    @Test
    public void testApply() {
        JSPlan pl = new JSPlan();
        
        JSState state = new JSState();
        JSState ns = state.apply(pl);
        
        assertNotNull(ns);
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
    }
    
    @Test
    public void testSatisfies() {
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();
        
        JSState state = new JSState();
        JSSubstitution result = state.satisfies(conds, alpha, axioms);
        
        assertNotNull(result);
    }
    
    @Test
    public void testSatisfiesAll() {
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();
        conds.addElement(new JSPredicateForm());
        
        JSState state = new JSState();
        JSListSubstitution results = state.satisfiesAll(conds, alpha, axioms);
        
        assertEquals(1, results.size());
    }
    
    @Test
    public void testSatisfiesTAm() {
        JSPredicateForm t = new JSPredicateForm();
        JSSubstitution alpha = new JSSubstitution();
        
        JSState state = new JSState();
        JSListSubstitution results = state.satisfiesTAm(t, alpha);
        
        assertNotNull(results);
    }
}
package umd.cs.shop;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class JSStateTest {
    @Test
    public void testAddElementsToState() {
        // Create JSListLogicalAtoms s to pass to the addElementsToState method
        JSListLogicalAtoms s = new JSListLogicalAtoms();
        s.addElement(new JSPredicateForm()); // Add sample element
        s.addElement(new JSPredicateForm()); // Add sample element
        
        // Create JSState object
        JSState state = new JSState();
        
        // Call the addElementsToState method
        state.addElementsToState(s);
        
        // Assert that size of the JSState object is equal to the size of s
        assertEquals(s.size(), state.size());
    }
    
    @Test
    public void testApply() {
        // Create JSPlan object
        JSPlan pl = new JSPlan();
        
        // Create JSState object
        JSState state = new JSState();
        
        // Call the apply method
        JSState ns = state.apply(pl);
        
        // Assert that ns is not null
        assertNotNull(ns);
    }
    
    @Test
    public void testApplyOp() {
        // Create JSOperator object
        JSOperator op = new JSOperator();
        
        // Create JSSubstitution object
        JSSubstitution alpha = new JSSubstitution();
        
        // Create JSListLogicalAtoms objects
        JSListLogicalAtoms addL = new JSListLogicalAtoms();
        JSListLogicalAtoms delL = new JSListLogicalAtoms();
        
        // Create JSState object
        JSState state = new JSState();
        
        // Call the applyOp method
        JSTState result = state.applyOp(op, alpha, addL, delL);
        
        // Assert that result is not null
        assertNotNull(result);
    }
    
    @Test
    public void testSatisfies() {
        // Create JSListLogicalAtoms object
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        
        // Create JSSubstitution object
        JSSubstitution alpha = new JSSubstitution();
        
        // Create JSListAxioms object
        JSListAxioms axioms = new JSListAxioms();
        
        // Create JSState object
        JSState state = new JSState();
        
        // Call the satisfies method
        JSSubstitution result = state.satisfies(conds, alpha, axioms);
        
        // Assert that result is not null
        assertNotNull(result);
    }
    
    @Test
    public void testSatisfiesAll() {
        // Create JSListLogicalAtoms object
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        
        // Create JSSubstitution object
        JSSubstitution alpha = new JSSubstitution();
        
        // Create JSListAxioms object
        JSListAxioms axioms = new JSListAxioms();
        
        // Create JSState object
        JSState state = new JSState();
        
        // Call the satisfiesAll method
        JSListSubstitution result = state.satisfiesAll(conds, alpha, axioms);
        
        // Assert that result is not null
        assertNotNull(result);
    }
    
    @Test
    public void testSatisfiesTAm() {
        // Create JSPredicateForm object
        JSPredicateForm t = new JSPredicateForm();
        
        // Create JSSubstitution object
        JSSubstitution alpha = new JSSubstitution();
        
        // Create JSState object
        JSState state = new JSState();
        
        // Call the satisfiesTAm method
        JSListSubstitution result = state.satisfiesTAm(t, alpha);
        
        // Assert that result is not null
        assertNotNull(result);
    }
}
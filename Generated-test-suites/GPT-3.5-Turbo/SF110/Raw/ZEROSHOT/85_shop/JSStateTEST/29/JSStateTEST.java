package umd.cs.shop;

import org.junit.Test;
import static org.junit.Assert.*;

public class JSStateTest {

    @Test
    public void testAddElementsToState() {
        JSListLogicalAtoms s = new JSListLogicalAtoms();
        JSListLogicalAtoms expected = new JSListLogicalAtoms();
        
        JSState state = new JSState();
        
        // Add elements to s
        JSPredicateForm element1 = new JSPredicateForm();
        JSPredicateForm element2 = new JSPredicateForm();
        JSPredicateForm element3 = new JSPredicateForm();
        s.addElement(element1);
        s.addElement(element2);
        s.addElement(element3);
        
        // Add elements to expected state
        expected.addElement(element1);
        expected.addElement(element2);
        expected.addElement(element3);
        
        state.addElementsToState(s);
        
        assertEquals(expected, state);
    }
    
    @Test
    public void testApply() {
        JSPlan pl = new JSPlan();
        JSState expected = new JSState();
        
        JSState state = new JSState();
        
        // Apply the plan
        JSState result = state.apply(pl);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testApplyOp() {
        JSOperator op = new JSOperator();
        JSSubstitution alpha = new JSSubstitution();
        JSListLogicalAtoms addL = new JSListLogicalAtoms();
        JSListLogicalAtoms delL = new JSListLogicalAtoms();
        
        JSListLogicalAtoms expectedAddL = new JSListLogicalAtoms();
        JSListLogicalAtoms expectedDelL = new JSListLogicalAtoms();
        JSState expected = new JSState();
        
        JSState state = new JSState();
        
        // Apply the operator
        JSTState result = state.applyOp(op, alpha, addL, delL);
        
        assertEquals(expectedAddL, result.getAddL());
        assertEquals(expectedDelL, result.getDelL());
        assertEquals(expected, result.getState());
    }
    
    @Test
    public void testSatisfies() {
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();
        
        JSListSubstitution expected = new JSListSubstitution();
        
        JSState state = new JSState();
        
        // Perform satisfaction check
        JSListSubstitution result = state.satisfies(conds, alpha, axioms);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testSatisfiesAll() {
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();
        
        JSListSubstitution expected = new JSListSubstitution();
        
        JSState state = new JSState();
        
        // Perform satisfaction check
        JSListSubstitution result = state.satisfiesAll(conds, alpha, axioms);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testSatisfiesTAm() {
        JSPredicateForm t = new JSPredicateForm();
        JSSubstitution alpha = new JSSubstitution();
        
        JSListSubstitution expected = new JSListSubstitution();
        
        JSState state = new JSState();
        
        // Perform satisfaction check
        JSListSubstitution result = state.satisfiesTAm(t, alpha);
        
        assertEquals(expected, result);
    }
}
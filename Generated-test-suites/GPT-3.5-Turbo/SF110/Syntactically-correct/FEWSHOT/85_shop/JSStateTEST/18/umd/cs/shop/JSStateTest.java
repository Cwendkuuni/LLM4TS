package umd.cs.shop;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class JSStateTest {
    @Test
    public void testAddElementsToState() {
        JSListLogicalAtoms s1 = new JSListLogicalAtoms();
        JSListLogicalAtoms s2 = new JSListLogicalAtoms();
        JSState state = new JSState();
        
        s1.addElement("atom1");
        s1.addElement("atom2");
        s1.addElement("atom3");
        
        s2.addElement("atom4");
        s2.addElement("atom5");
        s2.addElement("atom6");
        
        state.addElementsToState(s1);
        assertEquals(3, state.size());
        
        state.addElementsToState(s2);
        assertEquals(6, state.size());
        
        // Checking for duplicates
        state.addElementsToState(s1);
        assertEquals(6, state.size());
    }
    
    @Test
    public void testApply() {
        JSState state = new JSState();
        JSPlan plan = new JSPlan();
        
        JSState newState = state.apply(plan);
        
        // TODO: add assertions for the expected behavior of the apply() method
    }
    
    @Test
    public void testApplyOp() {
        JSState state = new JSState();
        JSOperator op = new JSOperator();
        JSSubstitution alpha = new JSSubstitution();
        JSListLogicalAtoms addL = new JSListLogicalAtoms();
        JSListLogicalAtoms delL = new JSListLogicalAtoms();
        
        JSTState newState = state.applyOp(op, alpha, addL, delL);
        
        // TODO: add assertions for the expected behavior of the applyOp() method
    }
    
    @Test
    public void testSatisfies() {
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();
        
        JSSubstitution result = state.satisfies(conds, alpha, axioms);
        
        // TODO: add assertions for the expected behavior of the satisfies() method
    }
    
    @Test
    public void testSatisfiesAll() {
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();
        
        JSListSubstitution result = state.satisfiesAll(conds, alpha, axioms);
        
        // TODO: add assertions for the expected behavior of the satisfiesAll() method
    }
    
    @Test
    public void testSatisfiesTAm() {
        JSPredicateForm t = new JSPredicateForm();
        JSSubstitution alpha = new JSSubstitution();
        
        JSListSubstitution result = state.satisfiesTAm(t, alpha);
        
        // TODO: add assertions for the expected behavior of the satisfiesTAm() method
    }
}
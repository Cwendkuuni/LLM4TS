
package umd.cs.shop;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class JSStateTest {
    
    @Test
    public void testAddElementsToState() {
        JSListLogicalAtoms s1 = new JSListLogicalAtoms();
        s1.addElement(new JSPredicateForm("atom1"));
        s1.addElement(new JSPredicateForm("atom2"));
        s1.addElement(new JSPredicateForm("atom3"));
        JSListLogicalAtoms s2 = new JSListLogicalAtoms();
        s2.addElement(new JSPredicateForm("atom3"));
        s2.addElement(new JSPredicateForm("atom4"));
        
        JSState state = new JSState();
        state.addElementsToState(s1);
        
        assertEquals(3, state.size());
        assertTrue(state.contains(new JSPredicateForm("atom1")));
        assertTrue(state.contains(new JSPredicateForm("atom2")));
        assertTrue(state.contains(new JSPredicateForm("atom3")));
        
        state.addElementsToState(s2);
        
        assertEquals(4, state.size());
        assertTrue(state.contains(new JSPredicateForm("atom1")));
        assertTrue(state.contains(new JSPredicateForm("atom2")));
        assertTrue(state.contains(new JSPredicateForm("atom3")));
        assertTrue(state.contains(new JSPredicateForm("atom4")));
    }
    
    @Test
    public void testApply() {
        JSState state = new JSState();
        JSPlan plan = new JSPlan();
        
        JSState newState = state.apply(plan);
        
        assertEquals(0, newState.size());
    }
    
    @Test
    public void testApplyOp() {
        JSState state = new JSState();
        JSOperator op = new JSOperator();
        JSSubstitution alpha = new JSSubstitution();
        JSListLogicalAtoms addL = new JSListLogicalAtoms();
        JSListLogicalAtoms delL = new JSListLogicalAtoms();
        
        JSTState newState = state.applyOp(op, alpha, addL, delL);
        
        assertEquals(0, newState.getAddL().size());
        assertEquals(0, newState.getDelL().size());
        assertEquals(0, newState.getNs().size());
    }
    
    @Test
    public void testSatisfies() {
        JSState state = new JSState();
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();
        
        JSSubstitution result = state.satisfies(conds, alpha, axioms);
        
        assertFalse(result.isFailure());
    }
    
    @Test
    public void testSatisfiesAll() {
        JSState state = new JSState();
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();
        
        JSListSubstitution result = state.satisfiesAll(conds, alpha, axioms);
        
        assertEquals(0, result.size());
    }
    
    @Test
    public void testSatisfiesTAm() {
        JSState state = new JSState();
        JSPredicateForm t = new JSPredicateForm("goal");
        JSSubstitution alpha = new JSSubstitution();
        
        JSListSubstitution result = state.satisfiesTAm(t, alpha);
        
        assertEquals(0, result.size());
    }
}

package umd.cs.shop;

import org.junit.Test;
import static org.junit.Assert.*;

public class JSStateTest {
    @Test
    public void testAddElementsToState() {
        JSState state = new JSState();
        JSListLogicalAtoms atoms = new JSListLogicalAtoms();
        state.addElementsToState(atoms);
        assertTrue(state.size() == 0);

        JSListLogicalAtoms atoms2 = new JSListLogicalAtoms();
        atoms2.addElement(new JSPredicateForm());
        atoms2.addElement(new JSPredicateForm());
        atoms2.addElement(new JSPredicateForm());
        
        state.addElementsToState(atoms2);
        
        assertTrue(state.size() == 3);
    }
    
    @Test
    public void testApply() {
        JSState state = new JSState();
        JSPlan plan = new JSPlan();
        
        JSState newState = state.apply(plan);
        assertTrue(newState.size() == 0);
    }
    
    @Test
    public void testApplyOp() {
        JSState state = new JSState();
        JSOperator op = new JSOperator();
        JSSubstitution alpha = new JSSubstitution();
        JSListLogicalAtoms addL = new JSListLogicalAtoms();
        JSListLogicalAtoms delL = new JSListLogicalAtoms();
        
        JSTState newState = state.applyOp(op, alpha, addL, delL);
        assertTrue(newState.getNewState().size() == 0);
        assertTrue(newState.getNewAddList().size() == 0);
        assertTrue(newState.getNewDelList().size() == 0);
    }
    
    @Test
    public void testSatisfies() {
        JSState state = new JSState();
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();
        
        JSSubstitution answer = state.satisfies(conds, alpha, axioms);
        assertTrue(answer.isEmpty());
    }
    
    @Test
    public void testSatisfiesAll() {
        JSState state = new JSState();
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();
        
        JSListSubstitution answers = state.satisfiesAll(conds, alpha, axioms);
        assertTrue(answers.isEmpty());
    }
    
    @Test
    public void testSatisfiesTAm() {
        JSState state = new JSState();
        JSPredicateForm t = new JSPredicateForm();
        JSSubstitution alpha = new JSSubstitution();
        
        JSListSubstitution answers = state.satisfiesTAm(t, alpha);
        assertTrue(answers.isEmpty());
    }
}
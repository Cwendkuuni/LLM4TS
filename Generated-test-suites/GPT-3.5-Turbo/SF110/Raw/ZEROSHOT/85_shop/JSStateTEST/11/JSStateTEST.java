package umd.cs.shop;

import org.junit.Test;
import static org.junit.Assert.*;

public class JSStateTest {
    
    @Test
    public void testAddElementsToState() {
        JSListLogicalAtoms s = new JSListLogicalAtoms();
        // add elements to s
        
        JSState state = new JSState();
        state.addElementsToState(s);
        
        // assert statements to check the result
    }
    
    @Test
    public void testApply() {
        JSPlan pl = new JSPlan();
        // set up pl
        
        JSState state = new JSState();
        JSState result = state.apply(pl);
        
        // assert statements to check the result
    }
    
    @Test
    public void testApplyOp() {
        JSOperator op = new JSOperator();
        JSSubstitution alpha = new JSSubstitution();
        JSListLogicalAtoms addL = new JSListLogicalAtoms();
        JSListLogicalAtoms delL = new JSListLogicalAtoms();
        // set up op, alpha, addL, delL
        
        JSState state = new JSState();
        JSTState result = state.applyOp(op, alpha, addL, delL);
        
        // assert statements to check the result
    }
    
    @Test
    public void testSatisfies() {
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();
        // set up conds, alpha, axioms
        
        JSState state = new JSState();
        JSSubstitution result = state.satisfies(conds, alpha, axioms);
        
        // assert statements to check the result
    }
    
    @Test
    public void testSatisfiesAll() {
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();
        // set up conds, alpha, axioms
        
        JSState state = new JSState();
        JSListSubstitution result = state.satisfiesAll(conds, alpha, axioms);
        
        // assert statements to check the result
    }
    
    @Test
    public void testSatisfiesTAm() {
        JSPredicateForm t = new JSPredicateForm();
        JSSubstitution alpha = new JSSubstitution();
        // set up t, alpha
        
        JSState state = new JSState();
        JSListSubstitution result = state.satisfiesTAm(t, alpha);
        
        // assert statements to check the result
    }
}
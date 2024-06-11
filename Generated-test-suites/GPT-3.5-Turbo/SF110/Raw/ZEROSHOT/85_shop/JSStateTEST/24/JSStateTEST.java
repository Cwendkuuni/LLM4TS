package umd.cs.shop;

import org.junit.Test;
import static org.junit.Assert.*;

public class JSStateTest {

    @Test
    public void testAddElementsToState() {
        JSState state = new JSState();
        JSListLogicalAtoms list = new JSListLogicalAtoms();
        list.addElement(new JSPredicateForm());
        list.addElement(new JSPredicateForm());
        
        state.addElementsToState(list);
        
        assertEquals(2, state.size());
    }
    
    @Test
    public void testApply() {
        JSState state = new JSState();
        JSPlan plan = new JSPlan();
        
        JSState newState = state.apply(plan);
        
        assertNotNull(newState);
    }
    
    @Test
    public void testApplyOp() {
        JSState state = new JSState();
        JSOperator op = new JSOperator();
        JSSubstitution alpha = new JSSubstitution();
        JSListLogicalAtoms addL = new JSListLogicalAtoms();
        JSListLogicalAtoms delL = new JSListLogicalAtoms();
        
        JSTState applyOpResult = state.applyOp(op, alpha, addL, delL);
        
        assertNotNull(applyOpResult);
    }
    
    @Test
    public void testSatisfies() {
        JSState state = new JSState();
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();
        
        JSSubstitution satisfiesResult = state.satisfies(conds, alpha, axioms);
        
        assertNotNull(satisfiesResult);
    }
    
    @Test
    public void testSatisfiesAll() {
        JSState state = new JSState();
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();
        
        JSListSubstitution satisfiesAllResult = state.satisfiesAll(conds, alpha, axioms);
        
        assertNotNull(satisfiesAllResult);
    }
    
    @Test
    public void testSatisfiesTAm() {
        JSState state = new JSState();
        JSPredicateForm t = new JSPredicateForm();
        JSSubstitution alpha = new JSSubstitution();
        
        JSListSubstitution satisfiesTAmResult = state.satisfiesTAm(t, alpha);
        
        assertNotNull(satisfiesTAmResult);
    }
}
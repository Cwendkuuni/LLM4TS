package umd.cs.shop;
import static org.junit.Assert.*;

import org.junit.Test;

public class JSStateTest {

    @Test
    public void testAddElementsToState() {
        JSState state = new JSState();
        JSListLogicalAtoms list = new JSListLogicalAtoms();
        list.addElement(new JSPredicateForm("test"));
        state.addElementsToState(list);
        assertEquals(1, state.size());
    }
    
    @Test
    public void testApply() {
        JSPlan plan = new JSPlan();
        JSState state = new JSState();
        JSState newState = state.apply(plan);
        assertNotNull(newState);
    }
    
    @Test
    public void testApplyOp() {
        JSOperator op = new JSOperator();
        JSSubstitution alpha = new JSSubstitution();
        JSListLogicalAtoms addList = new JSListLogicalAtoms();
        JSListLogicalAtoms delList = new JSListLogicalAtoms();
        JSState state = new JSState();
        JSTState newState = state.applyOp(op, alpha, addList, delList);
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
        JSState state = new JSState();
        JSListSubstitution results = state.satisfiesAll(conds, alpha, axioms);
        assertNotNull(results);
    }
    
    @Test
    public void testSatisfiesTAm() {
        JSPredicateForm predicate = new JSPredicateForm("test");
        JSSubstitution alpha = new JSSubstitution();
        JSState state = new JSState();
        JSListSubstitution results = state.satisfiesTAm(predicate, alpha);
        assertNotNull(results);
    }
}
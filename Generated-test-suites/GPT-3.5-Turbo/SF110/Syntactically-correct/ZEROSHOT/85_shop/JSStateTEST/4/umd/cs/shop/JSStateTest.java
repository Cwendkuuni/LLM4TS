package umd.cs.shop;

import org.junit.*;
import static org.junit.Assert.*;

public class JSStateTest {

    @Test
    public void testAddElementsToState() {
        JSState state = new JSState();
        JSListLogicalAtoms list = new JSListLogicalAtoms();
        list.addElement(new JSPredicateForm());
        
        state.addElementsToState(list);
        
        assertEquals(1, state.size());
        assertEquals(list.elementAt(0), state.elementAt(0));
    }

    @Test
    public void testApply() {
        JSState state = new JSState();
        JSPlan plan = new JSPlan();
        
        JSState result = state.apply(plan);
        
        assertNotNull(result);
    }

    @Test
    public void testApplyOp() {
        JSState state = new JSState();
        JSOperator op = new JSOperator();
        JSSubstitution alpha = new JSSubstitution();
        JSListLogicalAtoms addL = new JSListLogicalAtoms();
        JSListLogicalAtoms delL = new JSListLogicalAtoms();
        
        JSTState result = state.applyOp(op, alpha, addL, delL);
        
        assertNotNull(result);
    }

    @Test
    public void testSatisfies() {
        JSState state = new JSState();
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();
        
        JSSubstitution result = state.satisfies(conds, alpha, axioms);
        
        assertNotNull(result);
    }

    @Test
    public void testSatisfiesAll() {
        JSState state = new JSState();
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();
        
        JSListSubstitution result = state.satisfiesAll(conds, alpha, axioms);
        
        assertNotNull(result);
    }

    @Test
    public void testSatisfiesTAm() {
        JSState state = new JSState();
        JSPredicateForm t = new JSPredicateForm();
        JSSubstitution alpha = new JSSubstitution();
        
        JSListSubstitution result = state.satisfiesTAm(t, alpha);
        
        assertNotNull(result);
    }
}
package umd.cs.shop;

import org.junit.Test;
import static org.junit.Assert.*;

public class JSStateTest {

    @Test
    public void testAddElementsToState() {
        JSState state = new JSState();
        JSListLogicalAtoms s = new JSListLogicalAtoms();
        state.addElementsToState(s);
        assertEquals(0, state.size());
        
        JSPredicateForm el1 = new JSPredicateForm();
        s.addElement(el1);
        state.addElementsToState(s);
        assertEquals(1, state.size());
        assertEquals(el1, state.elementAt(0));
        
        JSPredicateForm el2 = new JSPredicateForm();
        s.addElement(el2);
        state.addElementsToState(s);
        assertEquals(2, state.size());
        assertEquals(el1, state.elementAt(0));
        assertEquals(el2, state.elementAt(1));
    }

    @Test
    public void testApply() {
        JSState state = new JSState();
        JSPlan pl = new JSPlan();
        JSState ns = state.apply(pl);
        // Assert the result
    }

    @Test
    public void testApplyOp() {
        JSState state = new JSState();
        JSOperator op = new JSOperator();
        JSSubstitution alpha = new JSSubstitution();
        JSListLogicalAtoms addL = new JSListLogicalAtoms();
        JSListLogicalAtoms delL = new JSListLogicalAtoms();
        
        JSTState result = state.applyOp(op, alpha, addL, delL);
        // Assert the result
    }

    @Test
    public void testSatisfies() {
        JSState state = new JSState();
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();
        
        JSSubstitution result = state.satisfies(conds, alpha, axioms);
        // Assert the result
    }

    @Test
    public void testSatisfiesAll() {
        JSState state = new JSState();
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();
        
        JSListSubstitution result = state.satisfiesAll(conds, alpha, axioms);
        // Assert the result
    }

    @Test
    public void testSatisfiesTAm() {
        JSState state = new JSState();
        JSPredicateForm t = new JSPredicateForm();
        JSSubstitution alpha = new JSSubstitution();
        
        JSListSubstitution result = state.satisfiesTAm(t, alpha);
        // Assert the result
    }

}
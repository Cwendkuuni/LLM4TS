package umd.cs.shop;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class JSStateTest {
    @Test
    public void testAddElementsToState() {
        JSListLogicalAtoms s = new JSListLogicalAtoms();
        JSState state = new JSState();
        state.addElementsToState(s);
        assertEquals(0, state.size());
        
        JSPredicateForm atom1 = new JSPredicateForm();
        JSPredicateForm atom2 = new JSPredicateForm();
        JSPredicateForm atom3 = new JSPredicateForm();
        s.addElement(atom1);
        s.addElement(atom2);
        s.addElement(atom3);
        
        state.addElementsToState(s);
        assertEquals(3, state.size());
        assertEquals(atom1, state.elementAt(0));
        assertEquals(atom2, state.elementAt(1));
        assertEquals(atom3, state.elementAt(2));
    }
    
    @Test
    public void testApply() {
        JSState state = new JSState();
        JSPlan pl = new JSPlan();
        JSState ns = state.apply(pl);
        assertEquals(ns, new JSState());
    }
    
    @Test
    public void testApplyOp() {
        JSState state = new JSState();
        JSOperator op = new JSOperator();
        JSSubstitution alpha = new JSSubstitution();
        JSListLogicalAtoms addL = new JSListLogicalAtoms();
        JSListLogicalAtoms delL = new JSListLogicalAtoms();
        JSTState tstate = state.applyOp(op, alpha, addL, delL);
        
        assertEquals(tstate.getNewState(), new JSState());
        assertEquals(tstate.getNewAddList(), addL);
        assertEquals(tstate.getNewDeleteList(), delL);
    }
    
    @Test
    public void testSatisfies() {
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();
        
        JSState state = new JSState();
        JSSubstitution satisfies = state.satisfies(conds, alpha, axioms);
        assertEquals(new JSSubstitution(), satisfies);
    }
    
    @Test
    public void testSatisfiesAll() {
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();
        
        JSState state = new JSState();
        JSListSubstitution satisfiesAll = state.satisfiesAll(conds, alpha, axioms);
        assertEquals(new JSListSubstitution(), satisfiesAll);
    }
    
    @Test
    public void testSatisfiesTAm() {
        JSPredicateForm t = new JSPredicateForm();
        JSSubstitution alpha = new JSSubstitution();
        
        JSState state = new JSState();
        JSListSubstitution satisfiesTAm = state.satisfiesTAm(t, alpha);
        assertEquals(new JSListSubstitution(), satisfiesTAm);
    }
}
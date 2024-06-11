package umd.cs.shop;

import static org.junit.Assert.*;

import org.junit.Test;

public class JSStateTest {
    @Test
    public void testAddElementsToState() {
        JSListLogicalAtoms s = new JSListLogicalAtoms();
        s.addElement(new JSPredicateForm("test1"));
        s.addElement(new JSPredicateForm("test2"));
        s.addElement(new JSPredicateForm("test3"));
        
        JSState state = new JSState();
        state.addElement(new JSPredicateForm("test4"));
        state.addElement(new JSPredicateForm("test5"));
        
        state.addElementsToState(s);
        
        assertEquals(5, state.size());
        assertEquals("test4", state.elementAt(0).getLabel());
        assertEquals("test5", state.elementAt(1).getLabel());
        assertEquals("test1", state.elementAt(2).getLabel());
        assertEquals("test2", state.elementAt(3).getLabel());
        assertEquals("test3", state.elementAt(4).getLabel());
    }
    
    @Test
    public void testApply() {
        JSPlan pl = new JSPlan();
        
        JSState state = new JSState();
        state.addElement(new JSPredicateForm("test1"));
        state.addElement(new JSPredicateForm("test2"));
        
        JSState newState = state.apply(pl);
        
        assertEquals(0, newState.size());
    }
    
    @Test
    public void testApplyOp() {
        JSOperator op = new JSOperator();
        JSSubstitution alpha = new JSSubstitution();
        JSListLogicalAtoms addL = new JSListLogicalAtoms();
        JSListLogicalAtoms delL = new JSListLogicalAtoms();
        
        op.addList().addElement(new JSPredicateForm("test1"));
        op.addList().addElement(new JSPredicateForm("test2"));
        op.deleteList().addElement(new JSPredicateForm("test3"));
        op.deleteList().addElement(new JSPredicateForm("test4"));
        
        alpha.addElement(new JSSubstitutionTerm(new JSTerm("test1"), new JSTerm("test3")));
        
        addL.addElement(new JSPredicateForm("test5"));
        addL.addElement(new JSPredicateForm("test6"));
        
        delL.addElement(new JSPredicateForm("test7"));
        delL.addElement(new JSPredicateForm("test8"));
        
        JSState state = new JSState();
        state.addElement(new JSPredicateForm("test1"));
        state.addElement(new JSPredicateForm("test2"));
        state.addElement(new JSPredicateForm("test3"));
        state.addElement(new JSPredicateForm("test4"));
        state.addElement(new JSPredicateForm("test9"));
        
        JSTState tState = state.applyOp(op, alpha, addL, delL);
        
        assertEquals(6, tState.size());
        assertEquals("test1", tState.getState().elementAt(0).getLabel());
        assertEquals("test2", tState.getState().elementAt(1).getLabel());
        assertEquals("test9", tState.getState().elementAt(2).getLabel());
        assertEquals("test5", tState.getState().elementAt(3).getLabel());
        assertEquals("test6", tState.getState().elementAt(4).getLabel());
        assertEquals("test3", tState.getState().elementAt(5).getLabel());
        assertEquals(2, tState.getAddList().size());
        assertEquals("test5", tState.getAddList().elementAt(0).getLabel());
        assertEquals("test6", tState.getAddList().elementAt(1).getLabel());
        assertEquals(2, tState.getDelList().size());
        assertEquals("test7", tState.getDelList().elementAt(0).getLabel());
        assertEquals("test8", tState.getDelList().elementAt(1).getLabel());
    }
    
    @Test
    public void testSatisfies() {
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        conds.addElement(new JSPredicateForm("test1"));
        conds.addElement(new JSPredicateForm("test2"));
        
        JSListAxioms axioms = new JSListAxioms();
        JSState state = new JSState();
        state.addElement(new JSPredicateForm("test1"));
        state.addElement(new JSPredicateForm("test2"));
        
        JSSubstitution alpha = new JSSubstitution();
        
        JSSubstitution result = state.satisfies(conds, alpha, axioms);
        
        assertEquals(0, result.size());
    }
    
    @Test
    public void testSatisfiesAll() {
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        conds.addElement(new JSPredicateForm("test1"));
        conds.addElement(new JSPredicateForm("test2"));
        
        JSListAxioms axioms = new JSListAxioms();
        JSState state = new JSState();
        state.addElement(new JSPredicateForm("test1"));
        state.addElement(new JSPredicateForm("test2"));
        state.addElement(new JSPredicateForm("test3"));
        
        JSSubstitution alpha = new JSSubstitution();
        
        JSListSubstitution results = state.satisfiesAll(conds, alpha, axioms);
        
        assertEquals(1, results.size());
        assertEquals("test1", results.elementAt(0).get(0).getName());
    }
    
    @Test
    public void testSatisfiesTAm() {
        JSPredicateForm t = new JSPredicateForm("test1");
        
        JSSubstitution alpha = new JSSubstitution();
        
        JSState state = new JSState();
        state.addElement(new JSPredicateForm("test1"));
        state.addElement(new JSPredicateForm("test2"));
        
        JSListSubstitution results = state.satisfiesTAm(t, alpha);
        
        assertEquals(1, results.size());
        assertEquals("test1", results.elementAt(0).get(0).getName());
    }
}
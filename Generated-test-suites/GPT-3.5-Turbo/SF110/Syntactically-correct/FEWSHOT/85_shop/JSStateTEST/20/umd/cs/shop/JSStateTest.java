package umd.cs.shop;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;
import java.io.StreamTokenizer;

import org.junit.Before;
import org.junit.Test;

public class JSStateTest {
    private JSState state;
    
    @Before
    public void setUp() {
        state = new JSState();
    }
    
    @Test
    public void testAddElementsToState() {
        JSListLogicalAtoms s = new JSListLogicalAtoms();
        JSPredicateForm element1 = new JSPredicateForm();
        JSPredicateForm element2 = new JSPredicateForm();
        s.addElement(element1);
        s.addElement(element2);
        
        state.addElementsToState(s);
        
        assertEquals(element1, state.elementAt(0));
        assertEquals(element2, state.elementAt(1));
    }
    
    @Test
    public void testApply() {
        JSPlan pl = new JSPlan();
        
        JSState newState = state.apply(pl);
        
        assertEquals(JSState.class, newState.getClass());
    }
    
    @Test
    public void testApplyOp() {
        JSOperator op = new JSOperator();
        JSSubstitution alpha = new JSSubstitution();
        JSListLogicalAtoms addL = new JSListLogicalAtoms();
        JSListLogicalAtoms delL = new JSListLogicalAtoms();
        
        JSTState tState = state.applyOp(op, alpha, addL, delL);
        
        assertTrue(tState instanceof JSTState);
    }
    
    @Test
    public void testSatisfies() {
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();
        
        JSSubstitution satisfies = state.satisfies(conds, alpha, axioms);
        
        assertTrue(satisfies instanceof JSSubstitution);
    }
    
    @Test
    public void testSatisfiesAll() {
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();
        
        JSListSubstitution satisfiesAll = state.satisfiesAll(conds, alpha, axioms);
        
        assertTrue(satisfiesAll instanceof JSListSubstitution);
    }
    
    @Test
    public void testSatisfiesTAm() {
        JSPredicateForm t = new JSPredicateForm();
        JSSubstitution alpha = new JSSubstitution();
        
        JSListSubstitution satisfiesTAm = state.satisfiesTAm(t, alpha);
        
        assertTrue(satisfiesTAm instanceof JSListSubstitution);
    }
}
package umd.cs.shop.test;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;
import umd.cs.shop.JSState;
import umd.cs.shop.JSPlan;
import umd.cs.shop.JSListLogicalAtoms;
import umd.cs.shop.JSOperator;
import umd.cs.shop.JSSubstitution;
import umd.cs.shop.JSListAxioms;
import umd.cs.shop.JSPredicateForm;
import umd.cs.shop.JSTState;
import umd.cs.shop.JSUtil;
import umd.cs.shop.JSJshopVars;

public class JSStateTest {
    @Test
    public void testConstructor1() {
        JSState state = new JSState();
        assertNotNull(state);
    }
    
    @Test
    public void testConstructor2() {
        StreamTokenizer tokenizer = new StreamTokenizer(new FileReader("input.txt"));
        JSState state = new JSState(tokenizer);
        assertNotNull(state);
    }
    
    @Test
    public void testAddElementsToState() {
        JSState state = new JSState();
        JSListLogicalAtoms s = new JSListLogicalAtoms();
        s.addElement(new JSPredicateForm());
        
        state.addElementsToState(s);
        assertEquals(1, state.size());
    }
    
    @Test
    public void testApply() {
        JSState state = new JSState();
        JSPlan pl = new JSPlan();
        
        JSState result = state.apply(pl);
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
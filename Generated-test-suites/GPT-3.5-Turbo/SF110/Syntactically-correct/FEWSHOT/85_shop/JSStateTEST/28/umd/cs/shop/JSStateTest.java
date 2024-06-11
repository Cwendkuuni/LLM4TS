package umd.cs.shop;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

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
        JSListLogicalAtoms atoms = new JSListLogicalAtoms();
        atoms.addElement(new JSPredicateForm("atom1", "arg1"));
        atoms.addElement(new JSPredicateForm("atom2", "arg2"));
        
        state.addElementsToState(atoms);
        
        assertEquals(state.size(), 2);
        assertTrue(state.contains(new JSPredicateForm("atom1", "arg1")));
        assertTrue(state.contains(new JSPredicateForm("atom2", "arg2")));
    }
    
    @Test
    public void testApply() {
        JSPlan plan = new JSPlan();
        
        JSState newState = state.apply(plan);
        
        assertNotNull(newState);
        //assert additional assertions
        
    }
    
    @Test
    public void testApplyOp() {
        JSOperator op = new JSOperator();
        JSSubstitution alpha = new JSSubstitution();
        JSListLogicalAtoms addList = new JSListLogicalAtoms();
        JSListLogicalAtoms delList = new JSListLogicalAtoms();
        
        JSTState newState = state.applyOp(op, alpha, addList, delList);
        
        assertNotNull(newState);
        //assert additional assertions
        
    }
    
    @Test
    public void testSatisfies() {
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();
        
        JSSubstitution answer = state.satisfies(conds, alpha, axioms);
        
        assertNotNull(answer);
        //assert additional assertions
        
    }
    
    @Test
    public void testSatisfiesAll() {
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();
        
        JSListSubstitution answers = state.satisfiesAll(conds, alpha, axioms);
        
        assertNotNull(answers);
        //assert additional assertions
        
    }
    
    @Test
    public void testSatisfiesTAm() {
        JSPredicateForm t = new JSPredicateForm("atom", "arg");
        JSSubstitution alpha = new JSSubstitution();
        
        JSListSubstitution answers = state.satisfiesTAm(t, alpha);
        
        assertNotNull(answers);
        //assert additional assertions
        
    }

}
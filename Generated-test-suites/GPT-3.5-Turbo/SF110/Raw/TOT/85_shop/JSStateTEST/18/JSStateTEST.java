import static org.junit.Assert.*;
import org.junit.Test;
import java.io.*;
import umd.cs.shop.*;

public class JSStateTest {
    
    @Test
    public void testAddElementsToState() {
        JSListLogicalAtoms atoms = new JSListLogicalAtoms();
        atoms.addElement(new JSPredicateForm("Atom1"));
        atoms.addElement(new JSPredicateForm("Atom2"));
        
        JSState state = new JSState();
        state.addElement(new JSPredicateForm("Atom3"));
        state.addElement(new JSPredicateForm("Atom4"));
        state.addElementsToState(atoms);
        
        assertEquals(4, state.size());
        assertEquals("Atom3", state.elementAt(0).getName());
        assertEquals("Atom4", state.elementAt(1).getName());
        assertEquals("Atom1", state.elementAt(2).getName());
        assertEquals("Atom2", state.elementAt(3).getName());
    }
    
    @Test
    public void testApply() {
        JSState state = new JSState();
        JSPlan emptyPlan = new JSPlan();
        JSState newState = state.apply(emptyPlan);
        
        // Verify that apply returns new JSState object
        assertNotSame(state, newState);
        
        // Verify that apply method has not been implemented yet
        assertNull(newState.elementAt(0));
    }
    
    @Test
    public void testApplyOp() {
        JSOperator op = new JSOperator();
        JSSubstitution alpha = new JSSubstitution();
        JSListLogicalAtoms addList = new JSListLogicalAtoms();
        addList.addElement(new JSPredicateForm("Atom1"));
        JSListLogicalAtoms deleteList = new JSListLogicalAtoms();
        deleteList.addElement(new JSPredicateForm("Atom2"));
        
        JSState state = new JSState();
        JSListLogicalAtoms addL = new JSListLogicalAtoms();
        JSListLogicalAtoms delL = new JSListLogicalAtoms();
        
        JSTState newState = state.applyOp(op, alpha, addL, delL);
        
        // Verify that applyOp returns new JSTState object
        assertNotSame(state, newState);
        
        // Verify that the resulting state is the same as the original state
        assertEquals(state.size(), newState.getState().size());
        for (int i = 0; i < state.size(); i++) {
            assertEquals(state.elementAt(i), newState.getState().elementAt(i));
        }
        
        // Verify that the resulting add list is the same as the original add list
        assertEquals(addL.size(), newState.getAddList().size());
        for (int i = 0; i < addL.size(); i++) {
            assertEquals(addL.elementAt(i), newState.getAddList().elementAt(i));
        }
        
        // Verify that the resulting delete list is the same as the original delete list
        assertEquals(delL.size(), newState.getDeleteList().size());
        for (int i = 0; i < delL.size(); i++) {
            assertEquals(delL.elementAt(i), newState.getDeleteList().elementAt(i));
        }
    }
    
    @Test
    public void testSatisfies() {
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        conds.addElement(new JSPredicateForm("Condition1"));
        conds.addElement(new JSPredicateForm("Condition2"));
        
        JSListAxioms axioms = new JSListAxioms();
        axioms.addElement(new JSAxiom());
        
        JSState state = new JSState();
        JSSubstitution alpha = new JSSubstitution();
        
        JSSubstitution result = state.satisfies(conds, alpha, axioms);
        
        // Verify that satisfies returns a JSSubstitution object
        assertNotNull(result);
        
        // Verify that satisfies returns an empty substitution when there are no satisfiers
        assertTrue(result.fail());
    }
    
    @Test
    public void testSatisfiesAll() {
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        conds.addElement(new JSPredicateForm("Condition1"));
        conds.addElement(new JSPredicateForm("Condition2"));
        
        JSListAxioms axioms = new JSListAxioms();
        axioms.addElement(new JSAxiom());
        
        JSState state = new JSState();
        JSSubstitution alpha = new JSSubstitution();
        
        JSListSubstitution result = state.satisfiesAll(conds, alpha, axioms);
        
        // Verify that satisfiesAll returns a JSListSubstitution object
        assertNotNull(result);
        
        // Verify that satisfiesAll returns an empty list of substitutions when there are no satisfiers
        assertEquals(0, result.size());
    }
    
    @Test
    public void testSatisfiesTAm() {
        JSPredicateForm goal = new JSPredicateForm("Goal");
        JSSubstitution alpha = new JSSubstitution();
        
        JSState state = new JSState();
        state.addElement(new JSPredicateForm("Atom1"));
        state.addElement(new JSPredicateForm("Atom2"));
        
        JSListSubstitution result = state.satisfiesTAm(goal, alpha);
        
        // Verify that satisfiesTAm returns a JSListSubstitution object
        assertNotNull(result);
        
        // Verify that satisfiesTAm returns an empty list of substitutions when there are no matches
        assertEquals(0, result.size());
    }
}
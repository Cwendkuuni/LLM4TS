import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class JSStateTest {
    
    private JSState state;
    
    @Before
    public void setUp() {
        state = new JSState();
    }
    
    @Test
    public void testAddElementsToState_existingElement() {
        JSListLogicalAtoms atoms = new JSListLogicalAtoms();
        atoms.addElement(new JSPredicateForm("atom"));
        state.addElement(new JSPredicateForm("atom"));
        
        state.addElementsToState(atoms);
        
        assertEquals(1, state.size());
        assertTrue(state.contains(new JSPredicateForm("atom")));
    }
    
    @Test
    public void testAddElementsToState_newElement() {
        JSListLogicalAtoms atoms = new JSListLogicalAtoms();
        atoms.addElement(new JSPredicateForm("newAtom"));
        
        state.addElementsToState(atoms);
        
        assertEquals(1, state.size());
        assertTrue(state.contains(new JSPredicateForm("newAtom")));
    }
    
    @Test
    public void testApply_validInput() {
        JSPlan pl = new JSPlan();
        JSState ns = state.apply(pl);
        
        assertEquals(0, ns.size());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testApplyOp() {
        JSOperator op = new JSOperator();
        JSSubstitution alpha = new JSSubstitution();
        JSListLogicalAtoms addL = new JSListLogicalAtoms();
        JSListLogicalAtoms delL = new JSListLogicalAtoms();
        
        state.applyOp(op, alpha, addL, delL);
    }
    
    @Test
    public void testSatisfies_noSatisfyingSubstitutions() {
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        conds.addElement(new JSPredicateForm("cond1"));
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();
        
        JSSubstitution result = state.satisfies(conds, alpha, axioms);
        
        assertEquals(true, result.isFailure());
    }
    
    @Test
    public void testSatisfies_satisfyingSubstitution() {
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        conds.addElement(new JSPredicateForm("cond1"));
        JSListLogicalAtoms atoms = new JSListLogicalAtoms();
        atoms.addElement(new JSPredicateForm("cond1"));
        state.addElementsToState(atoms);
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();
        
        JSSubstitution result = state.satisfies(conds, alpha, axioms);
        
        assertEquals(false, result.isFailure());
        assertTrue(result.contains(new JSSubstitutionElement("cond1", "cond1")));
    }
    
    @Test
    public void testSatisfiesAll_noSatisfyingSubstitutions() {
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        conds.addElement(new JSPredicateForm("cond1"));
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();
        
        JSListSubstitution result = state.satisfiesAll(conds, alpha, axioms);
        
        assertEquals(0, result.size());
    }
    
    @Test
    public void testSatisfiesAll_satisfyingSubstitutions() {
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        conds.addElement(new JSPredicateForm("cond1"));
        JSListLogicalAtoms atoms = new JSListLogicalAtoms();
        atoms.addElement(new JSPredicateForm("cond1"));
        state.addElementsToState(atoms);
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();
        
        JSListSubstitution result = state.satisfiesAll(conds, alpha, axioms);
        
        assertEquals(1, result.size());
        assertTrue(result.contains(new JSSubstitutionElement("cond1", "cond1")));
    }
    
    @Test
    public void testSatisfiesTAm_noMatchingAtoms() {
        JSPredicateForm t = new JSPredicateForm("goal");
        JSSubstitution alpha = new JSSubstitution();
        
        JSListSubstitution result = state.satisfiesTAm(t, alpha);
        
        assertEquals(0, result.size());
    }
    
    @Test
    public void testSatisfiesTAm_matchingAtoms() {
        JSPredicateForm t = new JSPredicateForm("goal");
        JSListLogicalAtoms atoms = new JSListLogicalAtoms();
        atoms.addElement(new JSPredicateForm("goal"));
        state.addElementsToState(atoms);
        JSSubstitution alpha = new JSSubstitution();
        
        JSListSubstitution result = state.satisfiesTAm(t, alpha);
        
        assertEquals(1, result.size());
        assertTrue(result.contains(new JSSubstitutionElement("goal", "goal")));
    }
}
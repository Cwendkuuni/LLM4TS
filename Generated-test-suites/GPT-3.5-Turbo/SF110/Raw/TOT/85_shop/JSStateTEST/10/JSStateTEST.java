import org.junit.Test;
import static org.junit.Assert.*;

public class JSStateTest {
    
    @Test
    public void testAddElementsToState() {
        JSState state = new JSState();
        JSListLogicalAtoms atoms = new JSListLogicalAtoms();
        atoms.addElement(new JSPredicateForm("atom1"));
        atoms.addElement(new JSPredicateForm("atom2"));
        atoms.addElement(new JSPredicateForm("atom3"));
        
        state.addElementsToState(atoms);
        
        assertEquals(3, state.size());
        assertTrue(state.contains(new JSPredicateForm("atom1")));
        assertTrue(state.contains(new JSPredicateForm("atom2")));
        assertTrue(state.contains(new JSPredicateForm("atom3")));
    }
    
    @Test
    public void testApply() {
        JSState state = new JSState();
        JSPlan plan = new JSPlan();
        
        JSState newState = state.apply(plan);
        
        // Check if a new instance of JSState is returned
        assertNotSame(state, newState);
        // Check if newState is empty
        assertEquals(0, newState.size());
    }
    
    @Test
    public void testApplyOp() {
        JSState state = new JSState();
        JSOperator op = new JSOperator();
        JSSubstitution alpha = new JSSubstitution();
        JSListLogicalAtoms addL = new JSListLogicalAtoms();
        addL.addElement(new JSPredicateForm("atom1"));
        JSListLogicalAtoms delL = new JSListLogicalAtoms();
        delL.addElement(new JSPredicateForm("atom2"));
        
        JSTState newState = state.applyOp(op, alpha, addL, delL);
        
        // Check if a new instance of JSTState is returned
        assertNotSame(state, newState);
        // Check if newState contains the added atom
        assertTrue(newState.contains(new JSPredicateForm("atom1")));
        // Check if newState does not contain the deleted atom
        assertFalse(newState.contains(new JSPredicateForm("atom2")));
    }
    
    @Test
    public void testSatisfies() {
        JSState state = new JSState();
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        conds.addElement(new JSPredicateForm("cond1"));
        conds.addElement(new JSPredicateForm("cond2"));
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();
        
        JSSubstitution answer = state.satisfies(conds, alpha, axioms);
        
        // Check if answer is not null
        assertNotNull(answer);
        // Check if answer is an instance of JSSubstitution
        assertTrue(answer instanceof JSSubstitution);
    }
    
    @Test
    public void testSatisfiesAll() {
        JSState state = new JSState();
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        conds.addElement(new JSPredicateForm("cond1"));
        conds.addElement(new JSPredicateForm("cond2"));
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();
        
        JSListSubstitution answers = state.satisfiesAll(conds, alpha, axioms);
        
        // Check if answers is not null
        assertNotNull(answers);
        // Check if answers is an instance of JSListSubstitution
        assertTrue(answers instanceof JSListSubstitution);
    }
    
    @Test
    public void testSatisfiesTAm() {
        JSState state = new JSState();
        JSPredicateForm t = new JSPredicateForm("atom1");
        JSSubstitution alpha = new JSSubstitution();
        
        JSListSubstitution answers = state.satisfiesTAm(t, alpha);
        
        // Check if answers is not null
        assertNotNull(answers);
        // Check if answers is an instance of JSListSubstitution
        assertTrue(answers instanceof JSListSubstitution);
    }
}
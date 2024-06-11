import static org.junit.Assert.*;
import org.junit.Test;

public class JSStateTest {

    @Test
    public void testAddElementsToState() {
        JSListLogicalAtoms s = new JSListLogicalAtoms();
        s.addElement(new JSPredicateForm("predicate1"));
        
        JSState state = new JSState();
        state.addElementsToState(s);
        
        assertEquals(1, state.size());
        assertTrue(state.contains(new JSPredicateForm("predicate1")));
    }
    
    @Test
    public void testApply() {
        JSPlan plan = new JSPlan();
        JSState state = new JSState();
        
        JSState newState = state.apply(plan);
        
        assertNotNull(newState);
    }
    
    @Test
    public void testApplyOp() {
        JSOperator op = new JSOperator();
        JSSubstitution alpha = new JSSubstitution();
        JSListLogicalAtoms addL = new JSListLogicalAtoms();
        JSListLogicalAtoms delL = new JSListLogicalAtoms();
        
        JSState state = new JSState();
        JSTState newState = state.applyOp(op, alpha, addL, delL);
        
        assertNotNull(newState);
    }
    
    @Test
    public void testSatisfies() {
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();
        
        JSState state = new JSState();
        JSSubstitution result = state.satisfies(conds, alpha, axioms);
        
        assertNotNull(result);
    }
    
    @Test
    public void testSatisfiesAll() {
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();
        
        JSState state = new JSState();
        JSListSubstitution results = state.satisfiesAll(conds, alpha, axioms);
        
        assertNotNull(results);
    }
    
    @Test
    public void testSatisfiesTAm() {
        JSPredicateForm t = new JSPredicateForm("predicate1");
        JSSubstitution alpha = new JSSubstitution();
        
        JSState state = new JSState();
        JSListSubstitution results = state.satisfiesTAm(t, alpha);
        
        assertNotNull(results);
    }
}
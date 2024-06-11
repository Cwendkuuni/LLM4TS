import org.junit.Test;
import static org.junit.Assert.*;

public class JSStateTest {

    @Test
    public void testAddElementsToState_basic() {
        JSState state = new JSState();
        JSListLogicalAtoms list = new JSListLogicalAtoms();
        list.addElement(new JSPredicateForm("predicate"));
        
        state.addElementsToState(list);
        
        assertEquals(1, state.size());
        assertEquals("predicate", state.elementAt(0).toString());
    }

    @Test
    public void testApply_basic() {
        JSState state = new JSState();
        JSPlan plan = new JSPlan();
        
        JSState newState = state.apply(plan);
        
        // need to implement this method to properly test
        // possible testing: check if newState is not null
        assertNotNull(newState);
    }

    @Test
    public void testApplyOp_basic() {
        JSState state = new JSState();
        JSOperator op = new JSOperator();
        JSSubstitution alpha = new JSSubstitution();
        JSListLogicalAtoms addL = new JSListLogicalAtoms();
        JSListLogicalAtoms delL = new JSListLogicalAtoms();
        
        JSTState newState = state.applyOp(op, alpha, addL, delL);
        
        // need to implement this method to properly test
        // possible testing: check if newState is not null
        assertNotNull(newState);
    }

    @Test
    public void testSatisfies_basic() {
        JSState state = new JSState();
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();
        
        JSSubstitution answer = state.satisfies(conds, alpha, axioms);
        
        assertNotNull(answer);
    }

    @Test
    public void testSatisfiesAll_basic() {
        JSState state = new JSState();
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();
        
        JSListSubstitution answers = state.satisfiesAll(conds, alpha, axioms);
        
        assertNotNull(answers);
    }

    @Test
    public void testSatisfiesTAm_basic() {
        JSState state = new JSState();
        JSPredicateForm t = new JSPredicateForm("goal");
        JSSubstitution alpha = new JSSubstitution();
        
        JSListSubstitution answers = state.satisfiesTAm(t, alpha);
        
        // need to implement this method to properly test
        // possible testing: check if answers is not null
        assertNotNull(answers);
    }

}
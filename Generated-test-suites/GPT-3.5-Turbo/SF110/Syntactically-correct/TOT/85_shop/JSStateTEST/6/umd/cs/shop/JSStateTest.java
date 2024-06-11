import org.junit.Test;
import static org.junit.Assert.*;

public class JSStateTest {
    
    @Test
    public void testAddElementsToState() {
        JSListLogicalAtoms s = new JSListLogicalAtoms();
        // typical use case
        s.addElement(new JSPredicateForm("predicate1"));
        
        JSState state = new JSState();
        state.addElementsToState(s);
        
        JSListLogicalAtoms expected = new JSListLogicalAtoms();
        expected.addElement(new JSPredicateForm("predicate1"));
        
        assertEquals(expected, state);
    }
    
    @Test
    public void testApply() {
        JSPlan pl = new JSPlan();
        // typical use case
        JSState ns = new JSState().apply(pl);
        
        assertEquals(new JSState(), ns);
    }
    
    @Test
    public void testApplyOp() {
        JSOperator op = new JSOperator();
        JSSubstitution alpha = new JSSubstitution();
        JSListLogicalAtoms addL = new JSListLogicalAtoms();
        JSListLogicalAtoms delL = new JSListLogicalAtoms();
        
        // edge case - empty lists
        JSTState result = new JSState().applyOp(op, alpha, addL, delL);
        
        assertEquals(new JSTState(new JSState(), new JSListLogicalAtoms(), new JSListLogicalAtoms()), result);
    }
    
    @Test
    public void testSatisfies() {
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();
        
        // error scenario - no satisfiers
        JSSubstitution result = new JSState().satisfies(conds, alpha, axioms);
        
        assertTrue(result.fail());
    }
    
    @Test
    public void testSatisfiesAll() {
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();
        
        // typical use case
        JSListSubstitution answers = new JSState().satisfiesAll(conds, alpha, axioms);
        
        assertEquals(new JSListSubstitution(), answers);
    }
    
    @Test
    public void testSatisfiesTAm() {
        JSPredicateForm t = new JSPredicateForm("predicate1");
        JSSubstitution alpha = new JSSubstitution();
        
        // edge case - empty state
        JSListSubstitution answers = new JSState().satisfiesTAm(t, alpha);
        
        assertEquals(new JSListSubstitution(), answers);
    }

}
import org.junit.*;
import static org.junit.Assert.*;

public class JSStateTest {
    
    @Test
    public void testAddElementsToState() {
        JSListLogicalAtoms s = new JSListLogicalAtoms();
        JSState state = new JSState();
        state.addElement(new JSPredicateForm("A"));
        state.addElement(new JSPredicateForm("B"));
        state.addElementsToState(s);
        assertEquals(s.size(), 2);
        assertEquals(s.elementAt(0).toStrng(), "A");
        assertEquals(s.elementAt(1).toStrng(), "B");
    }
    
    @Test
    public void testApply() {
        JSState state = new JSState();
        JSPlan plan = new JSPlan();
        JSState result = state.apply(plan);
        assertNull(result);
    }
    
    @Test
    public void testApplyOp() {
        JSOperator op = new JSOperator();
        JSSubstitution alpha = new JSSubstitution();
        JSListLogicalAtoms addL = new JSListLogicalAtoms();
        JSListLogicalAtoms delL = new JSListLogicalAtoms();
        JSTState state = state.applyOp(op, alpha, addL, delL);
        assertNotNull(state);
        assertNull(state.getAddList());
        assertNull(state.getDeleteList());
    }
    
    @Test
    public void testSatisfies() {
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();
        JSSubstitution result = state.satisfies(conds, alpha, axioms);
        assertNotNull(result);
        assertFalse(result.isFailure());
    }
    
    @Test
    public void testSatisfiesAll() {
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();
        JSListSubstitution result = state.satisfiesAll(conds, alpha, axioms);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testSatisfiesTAm() {
        JSPredicateForm t = new JSPredicateForm("A");
        JSSubstitution alpha = new JSSubstitution();
        JSListSubstitution result = state.satisfiesTAm(t, alpha);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
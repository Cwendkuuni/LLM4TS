import static org.junit.Assert.*;
import org.junit.Test;

public class JSStateTest {
    
    @Test
    public void testAddElementsToState() {
        JSListLogicalAtoms s = new JSListLogicalAtoms();
        JSListLogicalAtoms element = new JSListLogicalAtoms();
        element.addElement("element1");
        element.addElement("element2");
        s.addElement(element);
        JSListLogicalAtoms expected = new JSListLogicalAtoms();
        expected.addElement(element);
        JSState state = new JSState();
        state.addElementsToState(s);
        assertEquals(expected, state);
    }
    
    @Test
    public void testApply() {
        JSPlan pl = new JSPlan();
        JSState expected = new JSState();
        JSState state = new JSState();
        JSState result = state.apply(pl);
        assertEquals(expected, result);
    }
    
    @Test
    public void testApplyOp() {
        JSOperator op = new JSOperator();
        JSSubstitution alpha = new JSSubstitution();
        JSListLogicalAtoms addL = new JSListLogicalAtoms();
        JSListLogicalAtoms delL = new JSListLogicalAtoms();
        JSTState expected = new JSTState(new JSState(), new JSListLogicalAtoms(), new JSListLogicalAtoms());
        JSState state = new JSState();
        JSTState result = state.applyOp(op, alpha, addL, delL);
        assertEquals(expected, result);
    }
    
    @Test
    public void testSatisfies() {
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();
        JSSubstitution expected = new JSSubstitution();
        expected.assignFailure();
        JSState state = new JSState();
        JSSubstitution result = state.satisfies(conds, alpha, axioms);
        assertEquals(expected, result);
    }
    
    @Test
    public void testSatisfiesAll() {
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();
        JSListSubstitution expected = new JSListSubstitution();
        JSState state = new JSState();
        JSListSubstitution result = state.satisfiesAll(conds, alpha, axioms);
        assertEquals(expected, result);
    }
    
    @Test
    public void testSatisfiesTAm() {
        JSPredicateForm t = new JSPredicateForm();
        JSSubstitution alpha = new JSSubstitution();
        JSListSubstitution expected = new JSListSubstitution();
        JSState state = new JSState();
        JSListSubstitution result = state.satisfiesTAm(t, alpha);
        assertEquals(expected, result);
    }
}
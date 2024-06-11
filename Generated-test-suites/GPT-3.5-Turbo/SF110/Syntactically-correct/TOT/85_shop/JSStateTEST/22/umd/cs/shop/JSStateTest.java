import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JSStateTest {

    private JSState jsState;
    
    @Before
    public void setUp() {
        jsState = new JSState();
    }
    
    @Test
    public void testAddElementsToState_typicalCase() {
        JSListLogicalAtoms s = new JSListLogicalAtoms();
        JSPredicateForm pf1 = new JSPredicateForm("predicate1");
        JSPredicateForm pf2 = new JSPredicateForm("predicate2");
        s.addElement(pf1);
        s.addElement(pf2);
        jsState.addElement(pf1);
        
        jsState.addElementsToState(s);
        
        assertEquals(2, jsState.size());
        assertTrue(jsState.contains(pf1));
        assertTrue(jsState.contains(pf2));
    }
    
    @Test
    public void testAddElementsToState_edgeCase() {
        JSListLogicalAtoms s = new JSListLogicalAtoms();
        JSPredicateForm pf1 = new JSPredicateForm("predicate1");
        
        jsState.addElementsToState(s);
        
        assertEquals(0, jsState.size());
        assertFalse(jsState.contains(pf1));
    }
    
    @Test
    public void testApply_typicalCase() {
        JSPlan pl = new JSPlan();
        
        JSState ns = jsState.apply(pl);
        
        assertNotNull(ns);
    }
    
    @Test
    public void testApplyOp_typicalCase() {
        JSOperator op = new JSOperator();
        JSSubstitution alpha = new JSSubstitution();
        JSListLogicalAtoms addL = new JSListLogicalAtoms();
        JSListLogicalAtoms delL = new JSListLogicalAtoms();
        
        JSTState result = jsState.applyOp(op, alpha, addL, delL);
        
        assertNotNull(result);
    }
    
    @Test
    public void testSatisfies_typicalCase() {
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();
        
        JSSubstitution result = jsState.satisfies(conds, alpha, axioms);
        
        assertNotNull(result);
    }
    
    @Test
    public void testSatisfiesAll_typicalCase() {
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();
        
        JSListSubstitution result = jsState.satisfiesAll(conds, alpha, axioms);
        
        assertNotNull(result);
    }
    
    @Test
    public void testSatisfiesTAm_typicalCase() {
        JSPredicateForm t = new JSPredicateForm("predicate");
        JSSubstitution alpha = new JSSubstitution();
        
        JSListSubstitution result = jsState.satisfiesTAm(t, alpha);
        
        assertNotNull(result);
    }
}
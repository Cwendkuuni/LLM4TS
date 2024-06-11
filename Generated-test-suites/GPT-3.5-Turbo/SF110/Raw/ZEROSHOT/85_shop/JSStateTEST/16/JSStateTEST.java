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
    public void testAddElementsToState() {
        JSListLogicalAtoms s = new JSListLogicalAtoms();
        // Add elements to s
        
        jsState.addElementsToState(s);
        
        // Assert statements
    }
    
    @Test
    public void testApply() {
        JSPlan pl = new JSPlan();
        
        JSState result = jsState.apply(pl);
        
        // Assert statements
    }
    
    @Test
    public void testApplyOp() {
        JSOperator op = new JSOperator();
        JSSubstitution alpha = new JSSubstitution();
        JSListLogicalAtoms addL = new JSListLogicalAtoms();
        JSListLogicalAtoms delL = new JSListLogicalAtoms();
        
        JSTState result = jsState.applyOp(op, alpha, addL, delL);
        
        // Assert statements
    }
    
    @Test
    public void testSatisfies() {
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();
        
        JSSubstitution result = jsState.satisfies(conds, alpha, axioms);
        
        // Assert statements
    }
    
    @Test
    public void testSatisfiesAll() {
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();
        
        JSListSubstitution result = jsState.satisfiesAll(conds, alpha, axioms);
        
        // Assert statements
    }
    
    @Test
    public void testSatisfiesTAm() {
        JSPredicateForm t = new JSPredicateForm();
        JSSubstitution alpha = new JSSubstitution();
        
        JSListSubstitution result = jsState.satisfiesTAm(t, alpha);
        
        // Assert statements
    }
}
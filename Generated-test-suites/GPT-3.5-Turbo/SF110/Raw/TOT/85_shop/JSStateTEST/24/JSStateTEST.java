import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;

public class JSStateTest {

    private StreamTokenizer tokenizer;
    
    @Before
    public void setUp() {
        // Set up the StreamTokenizer
        String input = "Hello World";
        Reader reader = new StringReader(input);
        tokenizer = new StreamTokenizer(reader);
    }
    
    @Test
    public void testJSStateConstructor() {
        JSState state = new JSState();
        assertNotNull(state);
    }
    
    @Test
    public void testJSStateConstructorWithTokenizer() {
        JSState state = new JSState(tokenizer);
        assertNotNull(state);
    }
    
    @Test
    public void testAddElementsToState() {
        JSListLogicalAtoms s = new JSListLogicalAtoms();
        JSState state = new JSState();
        
        // Test adding elements to an empty state
        state.addElementsToState(s);
        assertEquals(0, state.size());
        
        // Test adding elements to a non-empty state
        JSPredicateForm element1 = new JSPredicateForm();
        JSPredicateForm element2 = new JSPredicateForm();
        s.addElement(element1);
        s.addElement(element2);
        state.addElementsToState(s);
        assertEquals(2, state.size());
        assertTrue(state.contains(element1));
        assertTrue(state.contains(element2));
    }
    
    @Test
    public void testApply() {
        JSPlan pl = new JSPlan();
        JSState state = new JSState();
        
        // Test applying the plan to an empty state
        JSState newState = state.apply(pl);
        assertNotNull(newState);
        assertEquals(0, newState.size());
    }
    
    @Test
    public void testApplyOp() {
        JSOperator op = new JSOperator();
        JSSubstitution alpha = new JSSubstitution();
        JSListLogicalAtoms addL = new JSListLogicalAtoms();
        JSListLogicalAtoms delL = new JSListLogicalAtoms();
        JSState state = new JSState();
        
        // Test applying the operator to an empty state
        JSTState result = state.applyOp(op, alpha, addL, delL);
        assertNotNull(result);
        assertEquals(0, result.getNewState().size());
        assertEquals(0, result.getNewAddL().size());
        assertEquals(0, result.getNewDelL().size());
    }
    
    @Test
    public void testSatisfies() {
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();
        JSState state = new JSState();
        
        // Test satisfying conditions when axioms fail
        JSSubstitution result = state.satisfies(conds, alpha, axioms);
        assertNotNull(result);
        assertTrue(result.isFailure());
        
        // Test satisfying conditions when axioms succeed
        JSSubstitution alphaClone = (JSSubstitution) alpha.clone();
        JSListSubstitution axiomsResult = new JSListSubstitution();
        axiomsResult.addElement(alphaClone);
        axioms.setResult(axiomsResult);
        result = state.satisfies(conds, alpha, axioms);
        assertNotNull(result);
        assertFalse(result.isFailure());
        assertEquals(axioms.getResult().elementAt(0), result);
    }
    
    @Test
    public void testSatisfiesAll() {
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();
        JSState state = new JSState();
        
        // Test satisfying conditions when axioms fail
        JSListSubstitution result = state.satisfiesAll(conds, alpha, axioms);
        assertNotNull(result);
        assertEquals(0, result.size());
        
        // Test satisfying conditions when axioms succeed
        JSSubstitution alphaClone = (JSSubstitution) alpha.clone();
        JSListSubstitution axiomsResult = new JSListSubstitution();
        JSSubstitution beta = new JSSubstitution();
        axiomsResult.addElement(beta);
        axioms.setResult(axiomsResult);
        result = state.satisfiesAll(conds, alpha, axioms);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(beta, result.elementAt(0));
    }
    
    @Test
    public void testSatisfiesTAm() {
        JSPredicateForm t = new JSPredicateForm();
        JSSubstitution alpha = new JSSubstitution();
        JSState state = new JSState();
        
        // Test satisfying goal when no matching atoms exist
        JSListSubstitution result = state.satisfiesTAm(t, alpha);
        assertNotNull(result);
        assertEquals(0, result.size());
        
        // Test satisfying goal when matching atoms exist
        JSPredicateForm el = new JSPredicateForm();
        JSListLogicalAtoms atoms = new JSListLogicalAtoms();
        atoms.addElement(el);
        state = new JSState();
        state.setElements(atoms);
        result = state.satisfiesTAm(t, alpha);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(alpha, result.elementAt(0));
    }
}
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JSTermTest {
    
    private JSTerm term;
    
    @Before
    public void setUp() {
        term = new JSTerm();
    }
    
    @Test
    public void testIsVariable() {
        assertFalse(term.isVariable());
    }
    
    @Test
    public void testIsConstant() {
        assertFalse(term.isConstant());
    }
    
    @Test
    public void testIsFunction() {
        assertTrue(term.isFunction());
    }
    
    @Test
    public void testIsEval() {
        assertFalse(term.isEval());
    }
    
    // Edge cases and exception handling scenarios to be tested

    @Test
    public void testParseListWithEmptyList() {
        // Test parsing an empty list
        JSTerm emptyList = new JSTerm();
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("()"));
        JSTerm result = emptyList.parseList(tokenizer);
        assertEquals("nil", result.elementAt(0));
    }
    
    @Test
    public void testApplySubstitutionWithConstant() {
        // Test applying substitution to a constant term
        JSTerm constantTerm = new JSTerm();
        constantTerm.addElement("constant");
        constantTerm.makeConstant();
        
        JSSubstitution alpha = new JSSubstitution();
        alpha.addSubstitution(new JSPairVarTerm(new JSTerm("constant"), new JSTerm("newConstant")));
        
        JSTerm result = constantTerm.applySubstitutionT(alpha);
        assertEquals("constant", result.elementAt(0));
    }
    
    @Test
    public void testMatchesWithConstants() {
        // Test matching two constant terms
        JSTerm term1 = new JSTerm("constant");
        JSTerm term2 = new JSTerm("constant");
        JSSubstitution result = term1.matches(term2);
        assertFalse(result.fail());
    }
    
    @Test
    public void testEqualsWithConstants() {
        // Test equality check between two constant terms
        JSTerm term1 = new JSTerm("constant");
        JSTerm term2 = new JSTerm("constant");
        assertTrue(term1.equals(term2));
    }

}
import org.junit.Test;
import static org.junit.Assert.*;

public class JSPredicateFormTest {

    @Test
    public void testJSPredicateFormConstructor() {
        // Typical use case
        JSPredicateForm p1 = new JSPredicateForm("(call (+ a b))");
        assertEquals("(call (+ a b))", p1.toStr().toString());
        
        // Edge case - empty string
        JSPredicateForm p2 = new JSPredicateForm("");
        assertEquals("Predicateform.print(): 0 elements", p2.toStr().toString());
        
        // Error scenario - null string
        JSPredicateForm p3 = new JSPredicateForm(null);
        assertEquals("Predicateform.print(): 0 elements", p3.toStr().toString());
    }
    
    @Test
    public void testJSPredicateFormInit() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(call (+ a b))"));
        JSPredicateForm p = new JSPredicateForm();
        
        // Typical use case
        p.JSPredicateFormInit(tokenizer);
        assertEquals("(call (+ a b))", p.toStr().toString());
        
        // Error scenario - null tokenizer
        try {
            tokenizer = null;
            p.JSPredicateFormInit(tokenizer);
            fail("Expected JSParserError to be thrown");
        } catch (JSParserError e) {
            // Pass
        }
        
        // Error scenario - missing '('
        tokenizer = new StreamTokenizer(new StringReader("call (+ a b))"));
        try {
            p.JSPredicateFormInit(tokenizer);
            fail("Expected JSParserError to be thrown");
        } catch (JSParserError e) {
            // Pass
        }
    }
    
    @Test
    public void testPrint() {
        JSPredicateForm p = new JSPredicateForm("(call (+ a b))");
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        // Typical use case
        p.print();
        assertEquals("(call (+ a b))", outContent.toString().trim());
    }
    
    @Test
    public void testToStr() {
        JSPredicateForm p = new JSPredicateForm("(call (+ a b))");
        
        // Typical use case
        assertEquals("(call (+ a b))", p.toStr().toString());
    }
    
    @Test
    public void testClonePF() {
        JSPredicateForm p1 = new JSPredicateForm("(call (+ a b))");
        
        // Typical use case
        JSPredicateForm p2 = p1.clonePF();
        assertEquals("(call (+ a b))", p2.toStr().toString());
    }
    
    @Test
    public void testApplySubstitutionPF() {
        JSSubstitution alpha = new JSSubstitution();
        JSPredicateForm p1 = new JSPredicateForm("(call (+ a b))");
        
        // Typical use case
        JSPredicateForm p2 = p1.applySubstitutionPF(alpha);
        assertEquals("(call (+ a b))", p2.toStr().toString());
    }
    
    @Test
    public void testMatches() {
        JSPredicateForm p1 = new JSPredicateForm("(call (+ a b))");
        JSPredicateForm p2 = new JSPredicateForm("(call (+ x y))");
        
        // Typical use case - successful match
        JSSubstitution alpha = new JSSubstitution();
        JSSubstitution beta = p1.matches(p2, alpha);
        assertFalse(beta.fail());
        
        // Typical use case - failed match
        JSPredicateForm p3 = new JSPredicateForm("(call (- a b))");
        JSSubstitution gamma = p1.matches(p3, alpha);
        assertTrue(gamma.fail());
    }
    
    @Test
    public void testEquals() {
        JSPredicateForm p1 = new JSPredicateForm("(call (+ a b))");
        JSPredicateForm p2 = new JSPredicateForm("(call (+ a b))");
        JSPredicateForm p3 = new JSPredicateForm("(call (- a b))");
        
        // Typical use case - equal predicates
        assertTrue(p1.equals(p2));
        
        // Typical use case - unequal predicates
        assertFalse(p1.equals(p3));
    }
    
    @Test
    public void testStandardizerPredicateForm() {
        JSPredicateForm p1 = new JSPredicateForm("(call (+ a b))");
        
        // Typical use case
        JSPredicateForm p2 = p1.standarizerPredicateForm();
        assertEquals("(call (+ a b))", p2.toStr().toString());
    }
}
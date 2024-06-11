import static org.junit.Assert.*;
import org.junit.Test;

public class JSTermTest {

    @Test
    public void testJSTermConstructor() {
        // Typical use case
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(call expect)"));
        JSTerm term = new JSTerm(tokenizer);
        assertNotNull(term);
        
        // Edge case - empty input
        tokenizer = new StreamTokenizer(new StringReader(""));
        term = new JSTerm(tokenizer);
        assertNotNull(term);
        
        // Error scenario - invalid input
        tokenizer = new StreamTokenizer(new StringReader("(call"));
        try {
            term = new JSTerm(tokenizer);
            fail("Expected JSParserError to be thrown");
        } catch (JSParserError e) {
            // Expected
        }
    }

    @Test
    public void testJSTermPrint() {
        // Typical use case
        JSTerm term = new JSTerm();
        term.addElement("nil");
        term.makeConstant();
        term.print();
        // Assuming this output will be validated visually
    }

    @Test
    public void testJSTermEquals() {
        // Typical use case
        JSTerm term1 = new JSTerm();
        term1.addElement("A");
        term1.makeConstant();
        
        JSTerm term2 = new JSTerm();
        term2.addElement("A");
        term2.makeConstant();
        
        assertTrue(term1.equals(term2));
        
        // Edge case - different constants
        term2 = new JSTerm();
        term2.addElement("B");
        term2.makeConstant();
        
        assertFalse(term1.equals(term2));
        
        // Error scenario - comparing different types
        term2 = new JSTerm();
        term2.addElement("A");
        term2.makeFunction();
        
        assertFalse(term1.equals(term2));
    }

    @Test
    public void testJSTermMatches() {
        // Typical use case
        JSTerm term1 = new JSTerm();
        term1.addElement("A");
        term1.makeConstant();
        
        JSTerm term2 = new JSTerm();
        term2.addElement("A");
        term2.makeConstant();
        
        JSSubstitution substitution = term1.matches(term2);
        assertNotNull(substitution);
        
        // Edge case - matching variables
        term2 = new JSTerm();
        term2.addElement("?X");
        term2.makeVariable();
        
        substitution = term1.matches(term2);
        assertNotNull(substitution);
        
        // Error scenario - incompatible terms
        term2 = new JSTerm();
        term2.addElement("B");
        term2.makeConstant();
        
        substitution = term1.matches(term2);
        assertNull(substitution);
    }

    @Test
    public void testJSTermCloneT() {
        // Typical use case
        JSTerm term = new JSTerm();
        term.addElement("A");
        term.makeConstant();
        
        JSTerm clonedTerm = term.cloneT();
        assertNotNull(clonedTerm);
        
        // Edge case - cloning a function
        term = new JSTerm();
        term.addElement("list");
        term.makeFunction();
        term.addElement("A");
        
        clonedTerm = term.cloneT();
        assertNotNull(clonedTerm);
        
        // Error scenario - cloning an eval term
        term = new JSTerm();
        term.addElement("A");
        term.makeFunction();
        term.makeEval(true);
        
        clonedTerm = term.cloneT();
        assertNotNull(clonedTerm);
    }
}
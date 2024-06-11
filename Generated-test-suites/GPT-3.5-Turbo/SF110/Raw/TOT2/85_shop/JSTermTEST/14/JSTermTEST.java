import org.junit.Test;
import static org.junit.Assert.*;

public class JSTermTest {
    
    @Test
    public void testJSTermConstructor() {
        // Test the constructor with a StreamTokenizer containing valid input
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(foo bar)"));
        JSTerm term = new JSTerm(tokenizer);
        assertNotNull(term);
        assertEquals(false, term.isVariable);
        assertEquals(false, term.isConstant);
        assertEquals(true, term.isFunction);
    }
    
    @Test
    public void testParseList() {
        // Test the parseList method with valid input
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(foo bar . (bar baz))"));
        JSTerm term = new JSTerm(tokenizer);
        JSTerm result = term.parseList(tokenizer);
        assertNotNull(result);
        assertEquals("(bar baz)", result.toString());
    }

    @Test
    public void testPrintList() {
        // Test the printList method
        JSTerm term = new JSTerm();
        term.addElement("nil");
        term.makeConstant();
        term.printList(term);
        // Assert output to console
    }

    @Test
    public void testCloneT() {
        // Test the cloneT method
        JSTerm term = new JSTerm();
        term.addElement("foo");
        term.makeFunction();
        JSTerm clone = term.cloneT();
        assertNotNull(clone);
        assertEquals(term.toString(), clone.toString());
    }

    @Test
    public void testApplySubstitutionT() {
        // Test the applySubstitutionT method
        JSSubstitution alpha = new JSSubstitution();
        JSTerm term = new JSTerm();
        term.addElement("foo");
        term.makeVariable();
        JSTerm result = term.applySubstitutionT(alpha);
        assertNotNull(result);
        assertEquals("(foo)", result.toString());
    }

    @Test
    public void testMatches() {
        // Test the matches method
        JSTerm t1 = new JSTerm();
        t1.addElement("x");
        t1.makeVariable();
        JSTerm t2 = new JSTerm();
        t2.addElement("y");
        t2.makeVariable();
        JSSubstitution alpha = t1.matches(t2);
        assertNotNull(alpha);
        assertTrue(alpha.fail());
    }

    @Test
    public void testEquals() {
        // Test the equals method
        JSTerm t1 = new JSTerm();
        t1.addElement("foo");
        t1.makeConstant();
        JSTerm t2 = new JSTerm();
        t2.addElement("bar");
        t2.makeConstant();
        assertFalse(t1.equals(t2));
    }

    @Test
    public void testIsConstant() {
        // Test the isConstant method
        JSTerm term = new JSTerm();
        term.addElement("foo");
        term.makeConstant();
        assertTrue(term.isConstant());
    }

    @Test
    public void testIsFunction() {
        // Test the isFunction method
        JSTerm term = new JSTerm();
        term.addElement("foo");
        term.makeFunction();
        assertTrue(term.isFunction());
    }

    @Test
    public void testIsVariable() {
        // Test the isVariable method
        JSTerm term = new JSTerm();
        term.addElement("x");
        term.makeVariable();
        assertTrue(term.isVariable());
    }

    @Test
    public void testIsEval() {
        // Test the isEval method
        JSTerm term = new JSTerm();
        term.makeEval(true);
        assertTrue(term.isEval());
    }

    @Test
    public void testToStr() {
        // Test the toStr method
        JSTerm term = new JSTerm();
        term.addElement("foo");
        term.makeFunction();
        assertEquals("(foo) ", term.toStr().toString());
    }

    @Test
    public void testIsGround() {
        // Test the isGround method
        JSTerm term = new JSTerm();
        term.addElement("foo");
        term.makeConstant();
        assertTrue(term.isGround());
    }

    @Test
    public void testStandardizerTerm() {
        // Test the standardizerTerm method
        JSTerm term = new JSTerm();
        term.addElement("foo");
        term.makeVariable();
        JSTerm result = term.standardizerTerm();
        assertNotNull(result);
        assertTrue(result.isVariable());
    }

    @Test
    public void testCall() {
        // Test the call method
        JSTerm term = new JSTerm();
        term.addElement("+");
        term.addElement("1");
        term.addElement("2");
        JSTerm result = term.call();
        assertNotNull(result);
        assertEquals("( 3 )", result.toString());
    }
}
package umd.cs.shop;

import org.junit.Test;
import static org.junit.Assert.*;

public class JSTermTest {

    @Test
    public void testConstructor1() {
        JSTerm term = new JSTerm();
        assertNotNull(term);
        assertFalse(term.isVariable());
        assertFalse(term.isConstant());
        assertFalse(term.isFunction());
        assertFalse(term.isEval());
    }

    @Test
    public void testConstructor2() {
        StreamTokenizer tokenizer = new StreamTokenizer();
        JSTerm term = new JSTerm(tokenizer);
        assertNotNull(term);
        assertFalse(term.isVariable());
        assertFalse(term.isConstant());
        assertFalse(term.isFunction());
        assertFalse(term.isEval());
    }

    @Test
    public void testParseList() {
        StreamTokenizer tokenizer = new StreamTokenizer();
        JSTerm term = new JSTerm();
        JSTerm result = term.parseList(tokenizer);
        assertNotNull(result);
        assertTrue(result.isConstant());
        assertEquals("nil", result.elementAt(0));
    }

    @Test
    public void testPrint() {
        JSTerm term = new JSTerm();
        term.makeVariable();
        term.addElement("?x");
        term.print();
    }

    @Test
    public void testCloneT() {
        JSTerm term = new JSTerm();
        JSTerm clone = term.cloneT();
        assertNotNull(clone);
        assertEquals(term.isVariable(), clone.isVariable());
        assertEquals(term.isConstant(), clone.isConstant());
        assertEquals(term.isFunction(), clone.isFunction());
        assertEquals(term.isEval(), clone.isEval());
    }

    @Test
    public void testApplySubstitutionT() {
        JSTerm term = new JSTerm();
        JSSubstitution alpha = new JSSubstitution();
        JSTerm result = term.applySubstitutionT(alpha);
        assertNotNull(result);
    }

    @Test
    public void testMatches() {
        JSTerm term1 = new JSTerm();
        JSTerm term2 = new JSTerm();
        JSSubstitution alpha = term1.matches(term2);
        assertNotNull(alpha);
    }

    @Test
    public void testEquals() {
        JSTerm term1 = new JSTerm();
        JSTerm term2 = new JSTerm();
        assertTrue(term1.equals(term2));
    }

    @Test
    public void testIsGround() {
        JSTerm term = new JSTerm();
        assertFalse(term.isGround());
    }

    @Test
    public void testStandardizerTerm() {
        JSTerm term = new JSTerm();
        JSTerm result = term.standardizerTerm();
        assertNotNull(result);
    }

    @Test
    public void testCall() {
        JSTerm term = new JSTerm();
        JSTerm result = term.call();
        assertNotNull(result);
    }
}
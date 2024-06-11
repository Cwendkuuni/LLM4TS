package umd.cs.shop;

import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;

import static org.junit.Assert.*;

public class JSTermTest {

    private JSTerm jsterm;

    @Before
    public void setup() {
        jsterm = new JSTerm();
    }

    @Test
    public void testJSTerm() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(""));
        JSTerm term = new JSTerm(tokenizer);
        assertNotNull(term);
    }

    @Test
    public void testParseList() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(""));
        JSTerm term = new JSTerm();
        JSTerm parsedList = term.parseList(tokenizer);
        assertNotNull(parsedList);
    }

    @Test
    public void testPrint() {
        jsterm.print();
    }

    @Test
    public void testCloneT() {
        JSTerm clone = jsterm.cloneT();
        assertNotNull(clone);
    }

    @Test
    public void testApplySubstitutionT() {
        JSSubstitution alpha = new JSSubstitution();
        JSTerm substituted = jsterm.applySubstitutionT(alpha);
        assertNotNull(substituted);
    }

    @Test
    public void testMatches() {
        JSTerm term = new JSTerm();
        JSSubstitution matches = jsterm.matches(term);
        assertNotNull(matches);
    }

    @Test
    public void testEquals() {
        JSTerm term = new JSTerm();
        boolean equals = jsterm.equals(term);
        assertFalse(equals);
    }

    @Test
    public void testMakeFunction() {
        jsterm.makeFunction();
        assertTrue(jsterm.isFunction());
    }

    @Test
    public void testMakeVariable() {
        jsterm.makeVariable();
        assertTrue(jsterm.isVariable());
    }

    @Test
    public void testMakeConstant() {
        jsterm.makeConstant();
        assertTrue(jsterm.isConstant());
    }

    @Test
    public void testMakeEval() {
        jsterm.makeEval(true);
        assertTrue(jsterm.isEval());
    }

    @Test
    public void testToStr() {
        StringBuffer str = jsterm.toStr();
        assertNotNull(str);
    }

    @Test
    public void testIsGround() {
        boolean isGround = jsterm.isGround();
        assertFalse(isGround);
    }

    @Test
    public void testStandardizerTerm() {
        JSTerm standardizerTerm = jsterm.standardizerTerm();
        assertNotNull(standardizerTerm);
    }

    @Test
    public void testCall() {
        JSTerm call = jsterm.call();
        assertNotNull(call);
    }
}
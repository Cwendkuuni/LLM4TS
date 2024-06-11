package umd.cs.shop;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;
import java.util.stream.StreamTokenizer;

import org.junit.Test;

public class JSPredicateFormTest {

    @Test
    public void testJSPredicateFormInit() {
        JSPredicateForm jspf = new JSPredicateForm("(A B C)");
        assertEquals(3, jspf.size());
        assertEquals("A", jspf.elementAt(0));
        assertEquals("B", jspf.elementAt(1));
        assertEquals("C", jspf.elementAt(2));
        
        jspf = new JSPredicateForm("(not (A B C))");
        assertEquals(2, jspf.size());
        assertEquals("not", jspf.elementAt(0));
        assertTrue(jspf.elementAt(1) instanceof JSPredicateForm);
    }

    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitNullTokenizer() {
        JSPredicateForm jspf = new JSPredicateForm(null);
    }

    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitUnexpectedRightPar() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(")"));
        JSPredicateForm jspf = new JSPredicateForm(tokenizer);
    }

    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitNotOperatorNameExpected() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("!"));
        JSPredicateForm jspf = new JSPredicateForm(tokenizer);
    }

    @Test(expected = JSParserError.class)
    public void testJSPredicateFormApplySubstitutionPFNullAlpha() {
        JSPredicateForm jspf = new JSPredicateForm();
        jspf.applySubstitutionPF(null);
    }

    @Test
    public void testJSPredicateFormMatches() {
        JSPredicateForm jspf1 = new JSPredicateForm("(A B C)");
        JSPredicateForm jspf2 = new JSPredicateForm("(A B C)");
        JSSubstitution alpha = new JSSubstitution();
        JSSubstitution match = jspf1.matches(jspf2, alpha);
        assertFalse(match.fail());
        assertEquals(0, match.size());

        jspf1 = new JSPredicateForm("(A B C)");
        jspf2 = new JSPredicateForm("(A B D)");
        alpha = new JSSubstitution();
        match = jspf1.matches(jspf2, alpha);
        assertFalse(match.fail());
        assertEquals(1, match.size());
        assertEquals("C", match.elementAt(0));

        jspf1 = new JSPredicateForm("(not (A B C))");
        jspf2 = new JSPredicateForm("(not (A B C))");
        alpha = new JSSubstitution();
        match = jspf1.matches(jspf2, alpha);
        assertFalse(match.fail());
        assertEquals(0, match.size());

        jspf1 = new JSPredicateForm("(not (A B C))");
        jspf2 = new JSPredicateForm("(not (A B D))");
        alpha = new JSSubstitution();
        match = jspf1.matches(jspf2, alpha);
        assertTrue(match.fail());
        assertEquals(0, match.size());
    }

    @Test
    public void testEquals() {
        JSPredicateForm jspf1 = new JSPredicateForm("(A B C)");
        JSPredicateForm jspf2 = new JSPredicateForm("(A B C)");
        assertTrue(jspf1.equals(jspf2));

        jspf1 = new JSPredicateForm("(A B C)");
        jspf2 = new JSPredicateForm("(A B D)");
        assertFalse(jspf1.equals(jspf2));

        jspf1 = new JSPredicateForm("(not (A B C))");
        jspf2 = new JSPredicateForm("(not (A B C))");
        assertTrue(jspf1.equals(jspf2));

        jspf1 = new JSPredicateForm("(not (A B C))");
        jspf2 = new JSPredicateForm("(not (A B D))");
        assertFalse(jspf1.equals(jspf2));
    }
}
package umd.cs.shop;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class JSPredicateFormTest {

    @Test
    public void testJSPredicateFormStringConstructor() {
        JSPredicateForm jpf = new JSPredicateForm("");
        assertEquals(0, jpf.size());

        jpf = new JSPredicateForm("()");
        assertEquals(0, jpf.size());

        jpf = new JSPredicateForm("(not)");
        assertEquals(2, jpf.size());
        assertTrue(jpf.contains("not"));
        assertTrue(jpf.elementAt(1) instanceof JSPredicateForm);

        jpf = new JSPredicateForm("(call)");
        assertEquals(2, jpf.size());
        assertTrue(jpf.contains("call"));
        assertTrue(jpf.elementAt(1) instanceof JSTerm);

        jpf = new JSPredicateForm("(not (call))");
        assertEquals(3, jpf.size());
        assertTrue(jpf.contains("not"));
        assertTrue(jpf.contains("call"));
        assertTrue(jpf.elementAt(1) instanceof JSPredicateForm);
        assertTrue(jpf.elementAt(2) instanceof JSTerm);

        jpf = new JSPredicateForm("(word foo bar)");
        assertEquals(4, jpf.size());
        assertTrue(jpf.contains("word"));
        assertTrue(jpf.contains("foo"));
        assertTrue(jpf.contains("bar"));
        assertTrue(jpf.elementAt(1) instanceof JSTerm);
        assertTrue(jpf.elementAt(2) instanceof JSTerm);
        assertTrue(jpf.elementAt(3) instanceof JSTerm);
    }

    @Test
    public void testJSPredicateFormTokenizerConstructor() {
        JSPredicateForm jpf = new JSPredicateForm(null);
        assertEquals(0, jpf.size());

        jpf = new JSPredicateForm(new StreamTokenizer(new StringReader("")));
        assertEquals(0, jpf.size());

        // Test with valid tokenizer

        // Test with invalid tokenizer
    }

    @Test
    public void testJSPredicateFormInit() {
        // Test with valid StreamTokenizer
        JSPredicateForm jpf = new JSPredicateForm();
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("()"));
        jpf.JSPredicateFormInit(tokenizer);
        assertEquals(0, jpf.size());

        jpf = new JSPredicateForm();
        tokenizer = new StreamTokenizer(new StringReader("(not)"));
        jpf.JSPredicateFormInit(tokenizer);
        assertEquals(2, jpf.size());
        assertTrue(jpf.contains("not"));
        assertTrue(jpf.elementAt(1) instanceof JSPredicateForm);

        jpf = new JSPredicateForm();
        tokenizer = new StreamTokenizer(new StringReader("(call)"));
        jpf.JSPredicateFormInit(tokenizer);
        assertEquals(2, jpf.size());
        assertTrue(jpf.contains("call"));
        assertTrue(jpf.elementAt(1) instanceof JSTerm);

        jpf = new JSPredicateForm();
        tokenizer = new StreamTokenizer(new StringReader("(not (call))"));
        jpf.JSPredicateFormInit(tokenizer);
        assertEquals(3, jpf.size());
        assertTrue(jpf.contains("not"));
        assertTrue(jpf.contains("call"));
        assertTrue(jpf.elementAt(1) instanceof JSPredicateForm);
        assertTrue(jpf.elementAt(2) instanceof JSTerm);

        jpf = new JSPredicateForm();
        tokenizer = new StreamTokenizer(new StringReader("(word foo bar)"));
        jpf.JSPredicateFormInit(tokenizer);
        assertEquals(4, jpf.size());
        assertTrue(jpf.contains("word"));
        assertTrue(jpf.contains("foo"));
        assertTrue(jpf.contains("bar"));
        assertTrue(jpf.elementAt(1) instanceof JSTerm);
        assertTrue(jpf.elementAt(2) instanceof JSTerm);
        assertTrue(jpf.elementAt(3) instanceof JSTerm);

        // Test with invalid StreamTokenizer
    }

    @Test
    public void testPrint() {
        // Test with empty predicate form

        // Test with non-empty predicate form
    }

    @Test
    public void testToStr() {
        // Test with empty predicate form

        // Test with non-empty predicate form
    }

    @Test
    public void testClonePF() {
        // Test with empty predicate form

        // Test with non-empty predicate form
    }

    @Test
    public void testApplySubstitutionPF() {
        // Test with empty predicate form

        // Test with non-empty predicate form
    }

    @Test
    public void testMatches() {
        // Test with empty predicate forms

        // Test with non-empty predicate forms and no substitution

        // Test with non-empty predicate forms and with substitution

        // Test with non-matching predicate forms
    }

    @Test
    public void testEquals() {
        // Test with equal predicate forms

        // Test with non-equal predicate forms
    }

    @Test
    public void testStandarizerPredicateForm() {
        // Test with empty predicate forms

        // Test with non-empty predicate forms
    }
}
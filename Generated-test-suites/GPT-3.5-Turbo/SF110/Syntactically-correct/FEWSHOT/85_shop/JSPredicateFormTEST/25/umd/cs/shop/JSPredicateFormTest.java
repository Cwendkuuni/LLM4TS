package umd.cs.shop;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.StreamTokenizer;
import java.io.StringReader;

import org.junit.Test;

public class JSPredicateFormTest {

    @Test
    public void testJSPredicateForm() {
        JSPredicateForm predicateForm = new JSPredicateForm();
        assertTrue(predicateForm.isEmpty());

        String text = "(predicate)";
        predicateForm = new JSPredicateForm(text);
        assertFalse(predicateForm.isEmpty());
        assertEquals(1, predicateForm.size());
        assertEquals("predicate", predicateForm.elementAt(0));

        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(text));
        predicateForm = new JSPredicateForm(tokenizer);
        assertFalse(predicateForm.isEmpty());
        assertEquals(1, predicateForm.size());
        assertEquals("predicate", predicateForm.elementAt(0));
    }

    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitWithNullTokenizer() {
        JSPredicateForm predicateForm = new JSPredicateForm(null);
    }

    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitWithoutOpeningParenthesis() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("predicate)"));
        JSPredicateForm predicateForm = new JSPredicateForm(tokenizer);
    }

    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitWithoutPredicate() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("()"));
        JSPredicateForm predicateForm = new JSPredicateForm(tokenizer);
    }

    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitWithUnexpectedClosingParenthesis() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(predicate))"));
        JSPredicateForm predicateForm = new JSPredicateForm(tokenizer);
    }

    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitWithUnexpectedExclamation() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(! predicate)"));
        JSPredicateForm predicateForm = new JSPredicateForm(tokenizer);
    }

    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitWithInvalidNotation() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(not ( predicate ) )"));
        JSPredicateForm predicateForm = new JSPredicateForm(tokenizer);
    }

    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitWithInvalidCallNotation() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(call ( predicate ) )"));
        JSPredicateForm predicateForm = new JSPredicateForm(tokenizer);
    }

    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitWithInvalidTerm() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(predicate term)"));
        JSPredicateForm predicateForm = new JSPredicateForm(tokenizer);
    }

    @Test
    public void testPrint() {
        JSPredicateForm predicateForm = new JSPredicateForm();
        predicateForm.addElement("predicate");
        predicateForm.addElement("term");

        predicateForm.print();
    }

    @Test
    public void testToStr() {
        JSPredicateForm predicateForm = new JSPredicateForm();
        predicateForm.addElement("predicate");
        predicateForm.addElement("term");

        StringBuffer str = predicateForm.toStr();
        assertEquals("(predicate term)", str.toString());
    }

    @Test
    public void testClonePF() {
        JSPredicateForm original = new JSPredicateForm();
        original.addElement("not");
        JSPredicateForm innerForm = new JSPredicateForm();
        innerForm.addElement("predicate");
        original.addElement(innerForm);

        JSPredicateForm clone = original.clonePF();
        assertEquals(original, clone);
    }

    @Test
    public void testApplySubstitutionPF() {
        JSPredicateForm original = new JSPredicateForm();
        original.addElement("not");
        JSPredicateForm innerForm = new JSPredicateForm();
        innerForm.addElement("predicate");
        original.addElement(innerForm);

        JSSubstitution alpha = new JSSubstitution();

        JSPredicateForm result = original.applySubstitutionPF(alpha);
        assertEquals(original, result);
    }

    @Test
    public void testMatches() {
        JSPredicateForm predicateForm1 = new JSPredicateForm();
        predicateForm1.addElement("not");
        JSPredicateForm innerForm1 = new JSPredicateForm();
        innerForm1.addElement("predicate");
        predicateForm1.addElement(innerForm1);

        JSPredicateForm predicateForm2 = new JSPredicateForm();
        predicateForm2.addElement("not");
        JSPredicateForm innerForm2 = new JSPredicateForm();
        innerForm2.addElement("predicate");
        predicateForm2.addElement(innerForm2);

        JSSubstitution alpha = predicateForm1.matches(predicateForm2);
        assertFalse(alpha.fail());
        assertTrue(alpha.isEmpty());
    }

    @Test
    public void testMatchesWithFailure() {
        JSPredicateForm predicateForm1 = new JSPredicateForm();
        predicateForm1.addElement("not");
        JSPredicateForm innerForm1 = new JSPredicateForm();
        innerForm1.addElement("predicate1");
        predicateForm1.addElement(innerForm1);

        JSPredicateForm predicateForm2 = new JSPredicateForm();
        predicateForm2.addElement("not");
        JSPredicateForm innerForm2 = new JSPredicateForm();
        innerForm2.addElement("predicate2");
        predicateForm2.addElement(innerForm2);

        JSSubstitution alpha = predicateForm1.matches(predicateForm2);
        assertTrue(alpha.fail());
    }

    @Test
    public void testEquals() {
        JSPredicateForm predicateForm1 = new JSPredicateForm();
        predicateForm1.addElement("predicate");
        JSTerm term1 = new JSTerm();
        term1.addElement("term");
        predicateForm1.addElement(term1);

        JSPredicateForm predicateForm2 = new JSPredicateForm();
        predicateForm2.addElement("predicate");
        JSTerm term2 = new JSTerm();
        term2.addElement("term");
        predicateForm2.addElement(term2);

        assertTrue(predicateForm1.equals(predicateForm2));
    }

    @Test
    public void testEqualsWithNull() {
        JSPredicateForm predicateForm = new JSPredicateForm();
        assertFalse(predicateForm.equals(null));
    }

    @Test
    public void testEqualsWithDifferentPredicate() {
        JSPredicateForm predicateForm1 = new JSPredicateForm();
        predicateForm1.addElement("predicate1");
        JSTerm term1 = new JSTerm();
        term1.addElement("term");
        predicateForm1.addElement(term1);

        JSPredicateForm predicateForm2 = new JSPredicateForm();
        predicateForm2.addElement("predicate2");
        JSTerm term2 = new JSTerm();
        term2.addElement("term");
        predicateForm2.addElement(term2);

        assertFalse(predicateForm1.equals(predicateForm2));
    }

    @Test
    public void testStandarizerPredicateForm() {
        JSPredicateForm predicateForm1 = new JSPredicateForm();
        predicateForm1.addElement("not");
        JSPredicateForm innerForm1 = new JSPredicateForm();
        innerForm1.addElement("predicate");
        predicateForm1.addElement(innerForm1);

        JSPredicateForm predicateForm2 = new JSPredicateForm();
        predicateForm2.addElement("not");
        JSPredicateForm innerForm2 = new JSPredicateForm();
        innerForm2.addElement("predicate");
        predicateForm2.addElement(innerForm2);

        JSPredicateForm standardPredicateForm = predicateForm1.standarizerPredicateForm();
        assertEquals(predicateForm2, standardPredicateForm);
    }
}
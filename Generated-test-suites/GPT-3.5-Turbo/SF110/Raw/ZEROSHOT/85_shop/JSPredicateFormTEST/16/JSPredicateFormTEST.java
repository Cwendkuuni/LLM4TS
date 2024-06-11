import static org.junit.Assert.*;
import org.junit.Test;

public class JSPredicateFormTest {

    @Test
    public void testJSPredicateForm() {
        JSPredicateForm form = new JSPredicateForm();
        assertNotNull(form);
    }

    @Test
    public void testJSPredicateFormString() {
        String text = "(predicate)";
        JSPredicateForm form = new JSPredicateForm(text);
        assertNotNull(form);
    }

    @Test
    public void testJSPredicateFormTokenizer() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(predicate)"));
        JSPredicateForm form = new JSPredicateForm(tokenizer);
        assertNotNull(form);
    }

    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitWithNullTokenizer() {
        JSPredicateForm form = new JSPredicateForm();
        form.JSPredicateFormInit(null);
    }

    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitWithMissingLeftPar() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("predicate)"));
        JSPredicateForm form = new JSPredicateForm();
        form.JSPredicateFormInit(tokenizer);
    }

    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitWithMissingPredicate() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("()"));
        JSPredicateForm form = new JSPredicateForm();
        form.JSPredicateFormInit(tokenizer);
    }

    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitWithUnexpectedRightPar() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(predicate))"));
        JSPredicateForm form = new JSPredicateForm();
        form.JSPredicateFormInit(tokenizer);
    }

    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitWithUnexpectedExclamation() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(! predicate)"));
        JSPredicateForm form = new JSPredicateForm();
        form.JSPredicateFormInit(tokenizer);
    }

    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitWithMissingOperatorName() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("( ! )"));
        JSPredicateForm form = new JSPredicateForm();
        form.JSPredicateFormInit(tokenizer);
    }

    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitWithInvalidOperatorName() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("( ! %%% )"));
        JSPredicateForm form = new JSPredicateForm();
        form.JSPredicateFormInit(tokenizer);
    }

    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitWithInvalidJSPredicateForm() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("( not () "));
        JSPredicateForm form = new JSPredicateForm();
        form.JSPredicateFormInit(tokenizer);
    }

    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitWithInvalidCallOperator() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("( call ) "));
        JSPredicateForm form = new JSPredicateForm();
        form.JSPredicateFormInit(tokenizer);
    }

    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitWithInvalidToken() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("( invalid ) "));
        JSPredicateForm form = new JSPredicateForm();
        form.JSPredicateFormInit(tokenizer);
    }

    @Test
    public void testPrint() {
        JSPredicateForm form = new JSPredicateForm("(predicate term)");
        form.print();
    }

    @Test
    public void testToStr() {
        JSPredicateForm form = new JSPredicateForm("(predicate term)");
        StringBuffer str = form.toStr();
        assertNotNull(str);
    }

    @Test
    public void testClonePF() {
        JSPredicateForm form = new JSPredicateForm("(predicate term)");
        JSPredicateForm clone = form.clonePF();
        assertNotNull(clone);
        assertEquals(form, clone);
    }

    @Test
    public void testApplySubstitutionPF() {
        JSPredicateForm form = new JSPredicateForm("(predicate term)");
        JSSubstitution alpha = new JSSubstitution();
        JSPredicateForm result = form.applySubstitutionPF(alpha);
        assertNotNull(result);
    }

    @Test
    public void testMatches() {
        JSPredicateForm form1 = new JSPredicateForm("(predicate1 term1)");
        JSPredicateForm form2 = new JSPredicateForm("(predicate2 term2)");
        JSSubstitution result = form1.matches(form2);
        assertNotNull(result);
    }

    @Test
    public void testMatchesWithAlpha() {
        JSPredicateForm form1 = new JSPredicateForm("(predicate1 term1)");
        JSPredicateForm form2 = new JSPredicateForm("(predicate2 term2)");
        JSSubstitution alpha = new JSSubstitution();
        JSSubstitution result = form1.matches(form2, alpha);
        assertNotNull(result);
    }

    @Test
    public void testEquals() {
        JSPredicateForm form1 = new JSPredicateForm("(predicate1 term1)");
        JSPredicateForm form2 = new JSPredicateForm("(predicate1 term1)");
        assertTrue(form1.equals(form2));
    }

    @Test
    public void testEqualsWithDifferentPredicate() {
        JSPredicateForm form1 = new JSPredicateForm("(predicate1 term1)");
        JSPredicateForm form2 = new JSPredicateForm("(predicate2 term1)");
        assertFalse(form1.equals(form2));
    }

    @Test
    public void testEqualsWithDifferentSize() {
        JSPredicateForm form1 = new JSPredicateForm("(predicate1 term1)");
        JSPredicateForm form2 = new JSPredicateForm("(predicate1 term1 term2)");
        assertFalse(form1.equals(form2));
    }

    @Test
    public void testStandarizerPredicateForm() {
        JSPredicateForm form = new JSPredicateForm("(predicate term)");
        JSPredicateForm standard = form.standarizerPredicateForm();
        assertNotNull(standard);
        assertEquals(form, standard);
    }
}
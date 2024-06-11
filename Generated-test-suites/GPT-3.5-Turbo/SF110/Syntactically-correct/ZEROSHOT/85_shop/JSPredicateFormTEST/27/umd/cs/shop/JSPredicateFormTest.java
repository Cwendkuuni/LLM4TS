import org.junit.Test;
import org.junit.Assert;

public class JSPredicateFormTest {

    @Test
    public void testJSPredicateForm() {
        JSPredicateForm form = new JSPredicateForm();
        Assert.assertNotNull(form);
    }

    @Test
    public void testJSPredicateFormWithString() {
        JSPredicateForm form = new JSPredicateForm("test");
        Assert.assertNotNull(form);
    }

    @Test
    public void testJSPredicateFormWithTokenizer() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("test"));
        JSPredicateForm form = new JSPredicateForm(tokenizer);
        Assert.assertNotNull(form);
    }

    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitWithNullTokenizer() {
        JSPredicateForm form = new JSPredicateForm();
        form.JSPredicateFormInit(null);
    }

    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitWithInvalidLeftPar() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("test"));
        JSPredicateForm form = new JSPredicateForm();
        form.JSPredicateFormInit(tokenizer);
    }

    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitWithInvalidPredicate() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("( )"));
        JSPredicateForm form = new JSPredicateForm();
        form.JSPredicateFormInit(tokenizer);
    }

    @Test
    public void testPrintWithEmptyForm() {
        JSPredicateForm form = new JSPredicateForm();
        form.print();
    }

    @Test
    public void testPrintWithNotForm() {
        JSPredicateForm form = new JSPredicateForm("!(A)");
        form.print();
    }

    @Test
    public void testPrintWithPredicateForm() {
        JSPredicateForm form = new JSPredicateForm("(A B C)");
        form.print();
    }

    @Test
    public void testToStrWithEmptyForm() {
        JSPredicateForm form = new JSPredicateForm();
        StringBuffer res = form.toStr();
        Assert.assertEquals("", res.toString());
    }

    @Test
    public void testToStrWithNotForm() {
        JSPredicateForm form = new JSPredicateForm("!(A)");
        StringBuffer res = form.toStr();
        Assert.assertEquals("(!A)", res.toString());
    }

    @Test
    public void testToStrWithPredicateForm() {
        JSPredicateForm form = new JSPredicateForm("(A B C)");
        StringBuffer res = form.toStr();
        Assert.assertEquals("(A B C)", res.toString());
    }

    @Test
    public void testClonePFWithNotForm() {
        JSPredicateForm form = new JSPredicateForm("!(A)");
        JSPredicateForm clone = form.clonePF();
        Assert.assertNotSame(form, clone);
    }

    @Test
    public void testClonePFWithPredicateForm() {
        JSPredicateForm form = new JSPredicateForm("(A B C)");
        JSPredicateForm clone = form.clonePF();
        Assert.assertNotSame(form, clone);
    }

    @Test
    public void testApplySubstitutionPFWithNotForm() {
        JSPredicateForm form = new JSPredicateForm("!(A)");
        JSSubstitution alpha = new JSSubstitution();
        JSPredicateForm appliedForm = form.applySubstitutionPF(alpha);
        Assert.assertNotNull(appliedForm);
    }

    @Test
    public void testApplySubstitutionPFWithPredicateForm() {
        JSPredicateForm form = new JSPredicateForm("(A B C)");
        JSSubstitution alpha = new JSSubstitution();
        JSPredicateForm appliedForm = form.applySubstitutionPF(alpha);
        Assert.assertNotNull(appliedForm);
    }

    @Test
    public void testMatchesWithDifferentSize() {
        JSPredicateForm form1 = new JSPredicateForm("(A)");
        JSPredicateForm form2 = new JSPredicateForm("(A B)");
        JSSubstitution result = form1.matches(form2);
        Assert.assertTrue(result.fail());
    }

    @Test
    public void testMatchesWithDifferentFunctor() {
        JSPredicateForm form1 = new JSPredicateForm("(A)");
        JSPredicateForm form2 = new JSPredicateForm("(B)");
        JSSubstitution result = form1.matches(form2);
        Assert.assertTrue(result.fail());
    }

    @Test
    public void testMatchesWithNotForm() {
        JSPredicateForm form1 = new JSPredicateForm("!(A)");
        JSPredicateForm form2 = new JSPredicateForm("!(B)");
        JSSubstitution result = form1.matches(form2);
        Assert.assertTrue(result.fail());
    }

    @Test
    public void testMatchesWithMatchingTerms() {
        JSPredicateForm form1 = new JSPredicateForm("(A B)");
        JSPredicateForm form2 = new JSPredicateForm("(A B)");
        JSSubstitution result = form1.matches(form2);
        Assert.assertFalse(result.fail());
        Assert.assertEquals(0, result.size());
    }

    @Test
    public void testMatchesWithNonMatchingTerms() {
        JSPredicateForm form1 = new JSPredicateForm("(A B)");
        JSPredicateForm form2 = new JSPredicateForm("(A C)");
        JSSubstitution result = form1.matches(form2);
        Assert.assertTrue(result.fail());
    }

    @Test
    public void testEqualsWithNull() {
        JSPredicateForm form = new JSPredicateForm();
        boolean result = form.equals(null);
        Assert.assertFalse(result);
    }

    @Test
    public void testEqualsWithDifferentSize() {
        JSPredicateForm form1 = new JSPredicateForm("(A)");
        JSPredicateForm form2 = new JSPredicateForm("(A B)");
        boolean result = form1.equals(form2);
        Assert.assertFalse(result);
    }

    @Test
    public void testEqualsWithDifferentFunctor() {
        JSPredicateForm form1 = new JSPredicateForm("(A)");
        JSPredicateForm form2 = new JSPredicateForm("(B)");
        boolean result = form1.equals(form2);
        Assert.assertFalse(result);
    }

    @Test
    public void testEqualsWithNotForm() {
        JSPredicateForm form1 = new JSPredicateForm("!(A)");
        JSPredicateForm form2 = new JSPredicateForm("!(B)");
        boolean result = form1.equals(form2);
        Assert.assertFalse(result);
    }

    @Test
    public void testEqualsWithMatchingTerms() {
        JSPredicateForm form1 = new JSPredicateForm("(A B)");
        JSPredicateForm form2 = new JSPredicateForm("(A B)");
        boolean result = form1.equals(form2);
        Assert.assertTrue(result);
    }

    @Test
    public void testEqualsWithNonMatchingTerms() {
        JSPredicateForm form1 = new JSPredicateForm("(A B)");
        JSPredicateForm form2 = new JSPredicateForm("(A C)");
        boolean result = form1.equals(form2);
        Assert.assertFalse(result);
    }

    @Test
    public void testStandarizerPredicateFormWithNotForm() {
        JSPredicateForm form = new JSPredicateForm("!(A)");
        JSPredicateForm result = form.standarizerPredicateForm();
        Assert.assertNotNull(result);
    }

    @Test
    public void testStandarizerPredicateFormWithPredicateForm() {
        JSPredicateForm form = new JSPredicateForm("(A B C)");
        JSPredicateForm result = form.standarizerPredicateForm();
        Assert.assertNotNull(result);
    }
}
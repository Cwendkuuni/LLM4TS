package umd.cs.shop;

import org.junit.Test;
import static org.junit.Assert.*;

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
    public void testJSPredicateFormInitNullTokenizer() {
        StreamTokenizer tokenizer = null;
        JSPredicateForm form = new JSPredicateForm();
        form.JSPredicateFormInit(tokenizer);
    }
    
    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitExpectedLeftPar() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("predicate)"));
        JSPredicateForm form = new JSPredicateForm();
        form.JSPredicateFormInit(tokenizer);
    }
    
    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitReadingPredicateFailed() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(predicate)"));
        JSPredicateForm form = new JSPredicateForm();
        form.JSPredicateFormInit(tokenizer);
    }
    
    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitUnexpectedRightPar() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("()"));
        JSPredicateForm form = new JSPredicateForm();
        form.JSPredicateFormInit(tokenizer);
    }
    
    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitNotExpectedOperatorName() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(!)"));
        JSPredicateForm form = new JSPredicateForm();
        form.JSPredicateFormInit(tokenizer);
    }
    
    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitReadingPredicateFormFailed() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(predicate"));
        JSPredicateForm form = new JSPredicateForm();
        form.JSPredicateFormInit(tokenizer);
    }
    
    @Test
    public void testPrintEmpty() {
        JSPredicateForm form = new JSPredicateForm();
        form.print();
    }
    
    @Test
    public void testPrintNot() {
        JSPredicateForm form = new JSPredicateForm();
        form.addElement("not");
        form.addElement(new JSPredicateForm());
        form.print();
    }
    
    @Test
    public void testPrintPredicate() {
        JSPredicateForm form = new JSPredicateForm();
        form.addElement("predicate");
        form.addElement(new JSTerm());
        form.addElement(new JSTerm());
        form.print();
    }
    
    @Test
    public void testToStrEmpty() {
        JSPredicateForm form = new JSPredicateForm();
        StringBuffer res = form.toStr();
        assertNotNull(res);
    }
    
    @Test
    public void testToStrNot() {
        JSPredicateForm form = new JSPredicateForm();
        form.addElement("not");
        form.addElement(new JSPredicateForm());
        StringBuffer res = form.toStr();
        assertNotNull(res);
    }
    
    @Test
    public void testToStrPredicate() {
        JSPredicateForm form = new JSPredicateForm();
        form.addElement("predicate");
        form.addElement(new JSTerm());
        form.addElement(new JSTerm());
        StringBuffer res = form.toStr();
        assertNotNull(res);
    }
    
    @Test
    public void testClonePF() {
        JSPredicateForm form = new JSPredicateForm();
        form.addElement("predicate");
        JSPredicateForm clone = form.clonePF();
        assertEquals(form, clone);
    }
    
    @Test
    public void testApplySubstitutionPF() {
        JSSubstitution alpha = new JSSubstitution();
        JSPredicateForm form = new JSPredicateForm();
        form.addElement("predicate");
        JSPredicateForm result = form.applySubstitutionPF(alpha);
        assertNotNull(result);
    }
    
    @Test
    public void testMatches() {
        JSPredicateForm form1 = new JSPredicateForm();
        form1.addElement("predicate");
        JSPredicateForm form2 = new JSPredicateForm();
        form2.addElement("predicate");
        JSSubstitution result = form1.matches(form2);
        assertNotNull(result);
    }
    
    @Test
    public void testMatchesWithAlpha() {
        JSPredicateForm form1 = new JSPredicateForm();
        form1.addElement("predicate");
        JSPredicateForm form2 = new JSPredicateForm();
        form2.addElement("predicate");
        JSSubstitution alpha = new JSSubstitution();
        JSSubstitution result = form1.matches(form2, alpha);
        assertNotNull(result);
    }
    
    @Test
    public void testEqualsWithNull() {
        JSPredicateForm form1 = new JSPredicateForm();
        form1.addElement("predicate");
        boolean result = form1.equals(null);
        assertFalse(result);
    }
    
    @Test
    public void testEqualsWithDifferentSize() {
        JSPredicateForm form1 = new JSPredicateForm();
        form1.addElement("predicate");
        JSPredicateForm form2 = new JSPredicateForm();
        form2.addElement("predicate");
        form2.addElement(new JSTerm());
        boolean result = form1.equals(form2);
        assertFalse(result);
    }
    
    @Test
    public void testEqualsWithDifferentFunctor() {
        JSPredicateForm form1 = new JSPredicateForm();
        form1.addElement("predicate");
        JSPredicateForm form2 = new JSPredicateForm();
        form2.addElement("different");
        boolean result = form1.equals(form2);
        assertFalse(result);
    }
    
    @Test
    public void testEqualsWithNot() {
        JSPredicateForm form1 = new JSPredicateForm();
        form1.addElement("not");
        form1.addElement(new JSPredicateForm());
        JSPredicateForm form2 = new JSPredicateForm();
        form2.addElement("not");
        form2.addElement(new JSPredicateForm());
        boolean result = form1.equals(form2);
        assertTrue(result);
    }
    
    @Test
    public void testEqualsWithDifferentTerm() {
        JSPredicateForm form1 = new JSPredicateForm();
        form1.addElement("predicate");
        form1.addElement(new JSTerm());
        JSPredicateForm form2 = new JSPredicateForm();
        form2.addElement("predicate");
        form2.addElement(new JSTerm());
        boolean result = form1.equals(form2);
        assertTrue(result);
    }
    
    @Test
    public void testStandarizerPredicateForm() {
        JSPredicateForm form = new JSPredicateForm();
        form.addElement("predicate");
        JSPredicateForm result = form.standarizerPredicateForm();
        assertNotNull(result);
    }
}
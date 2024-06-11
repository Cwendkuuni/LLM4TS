package umd.cs.shop;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class JSPredicateFormTest {
    
    @Test
    public void testJSPredicateForm() {
        JSPredicateForm form = new JSPredicateForm();
        assertNotNull(form);
    }
    
    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitNullTokenizer() {
        StreamTokenizer tokenizer = null;
        JSPredicateForm form = new JSPredicateForm(tokenizer);
    }
    
    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitNoLeftPar() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(""));
        JSPredicateForm form = new JSPredicateForm(tokenizer);
    }
    
    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitNoPredicate() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("()"));
        JSPredicateForm form = new JSPredicateForm(tokenizer);
    }
    
    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitUnexpectedRightPar() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(")"));
        JSPredicateForm form = new JSPredicateForm(tokenizer);
    }
    
    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitNotWithNoOperator() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(not)"));
        JSPredicateForm form = new JSPredicateForm(tokenizer);
    }
    
    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitNotWithInvalidOperator() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(not %%%)"));
        JSPredicateForm form = new JSPredicateForm(tokenizer);
    }
    
    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitNotWithIncompleteForm() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(not (predicate))"));
        JSPredicateForm form = new JSPredicateForm(tokenizer);
    }
    
    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitCallWithNoLeftPar() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(call f)"));
        JSPredicateForm form = new JSPredicateForm(tokenizer);
    }
    
    @Test
    public void testPrint() {
        JSPredicateForm form = new JSPredicateForm();
        form.add("predicate");
        form.add("argument");
        form.print();
    }
    
    @Test
    public void testToStr() {
        JSPredicateForm form = new JSPredicateForm();
        form.add("predicate");
        form.add("argument");
        StringBuffer expected = new StringBuffer();
        expected.append("(predicate argument)");
        StringBuffer result = form.toStr();
        assertEquals(expected.toString(), result.toString());
    }
    
    @Test
    public void testClonePF() {
        JSPredicateForm form = new JSPredicateForm();
        form.add("!not");
        JSPredicateForm subForm = new JSPredicateForm();
        subForm.add("predicate");
        subForm.add("argument");
        form.add(subForm);
        
        JSPredicateForm clonedForm = form.clonePF();
        assertNotNull(clonedForm);
        assertFalse(clonedForm.equals(form));
    }
    
    @Test(expected = JSParserError.class)
    public void testApplySubstitutionPFNullAlpha() {
        JSSubstitution alpha = null;
        JSPredicateForm form = new JSPredicateForm();
        form.add("predicate");
        form.add("argument");
        form.applySubstitutionPF(alpha);
    }
    
    @Test
    public void testMatches() {
        JSPredicateForm form = new JSPredicateForm();
        form.add("predicate");
        form.add("argument");
        
        JSPredicateForm form2 = new JSPredicateForm();
        form2.add("predicate");
        form2.add("argument");
        
        JSSubstitution result = form.matches(form2);
        assertNotNull(result);
    }
    
    @Test
    public void testEquals() {
        JSPredicateForm form = new JSPredicateForm();
        form.add("predicate");
        form.add("argument");
        
        JSPredicateForm form2 = new JSPredicateForm();
        form2.add("predicate");
        form2.add("argument");
        
        boolean result = form.equals(form2);
        assertEquals(true, result);
    }
    
    @Test
    public void testStandarizerPredicateForm() {
        JSPredicateForm form = new JSPredicateForm();
        form.add("not");
        JSPredicateForm subForm = new JSPredicateForm();
        subForm.add("predicate");
        subForm.add("argument");
        form.add(subForm);
        
        JSPredicateForm result = form.standarizerPredicateForm();
        assertNotNull(result);
    }
}
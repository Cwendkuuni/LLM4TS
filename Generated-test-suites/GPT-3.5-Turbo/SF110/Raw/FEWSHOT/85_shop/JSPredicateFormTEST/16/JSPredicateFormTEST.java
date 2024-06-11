package umd.cs.shop;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import java.io.StringReader;
import java.util.stream.StreamTokenizer;

import org.junit.Test;

public class JSPredicateFormTest {
    @Test
    public void testJSPredicateForm() {
        JSPredicateForm form = new JSPredicateForm();
        assertEquals(0, form.size());
        
        form = new JSPredicateForm((String) null);
        assertEquals(0, form.size());
        
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(null));
        form = new JSPredicateForm(tokenizer);
        assertEquals(0, form.size());
    }
    
    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitWithNullTokenizer() {
        JSPredicateForm form = new JSPredicateForm();
        form.JSPredicateFormInit(null);
    }
    
    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitWithUnexpectedLeftPar() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("test"));
        
        JSPredicateForm form = new JSPredicateForm();
        form.JSPredicateFormInit(tokenizer);
    }
    
    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitWithUnexpectedRightPar() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(")"));
        
        JSPredicateForm form = new JSPredicateForm();
        form.JSPredicateFormInit(tokenizer);
    }
    
    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitWithUnexpectedOperatorName() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("!"));
        
        JSPredicateForm form = new JSPredicateForm();
        form.JSPredicateFormInit(tokenizer);
    }
    
    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitWithMissingParenthesis() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("("));
        
        JSPredicateForm form = new JSPredicateForm();
        form.JSPredicateFormInit(tokenizer);
    }
    
    @Test
    public void testPrintWithEmptyForm() {
        JSPredicateForm form = new JSPredicateForm();
        form.print();
        // No assert needed as it only prints to console
    }
    
    @Test
    public void testToStrWithEmptyForm() {
        JSPredicateForm form = new JSPredicateForm();
        StringBuffer result = form.toStr();
        assertEquals("()", result.toString());
    }
    
    @Test
    public void testClonePFWithEmptyForm() {
        JSPredicateForm form = new JSPredicateForm();
        JSPredicateForm clone = form.clonePF();
        assertEquals(0, clone.size());
    }
    
    @Test
    public void testApplySubstitutionPFWithEmptyForm() {
        JSPredicateForm form = new JSPredicateForm();
        JSSubstitution alpha = new JSSubstitution();
        JSPredicateForm result = form.applySubstitutionPF(alpha);
        assertEquals(0, result.size());
    }
    
    @Test
    public void testMatchesWithDifferentSize() {
        JSPredicateForm form = new JSPredicateForm();
        JSPredicateForm t = new JSPredicateForm();
        JSSubstitution result = form.matches(t);
        assertTrue(result.fail());
    }
    
    @Test
    public void testMatchesWithDifferentFunctor() {
        JSPredicateForm form = new JSPredicateForm();
        JSPredicateForm t = new JSPredicateForm();
        form.addElement("functor1");
        t.addElement("functor2");
        
        JSSubstitution result = form.matches(t);
        assertTrue(result.fail());
    }
    
    @Test
    public void testMatchesWithFunctorNot() {
        JSPredicateForm form = new JSPredicateForm();
        JSPredicateForm t = new JSPredicateForm();
        form.addElement("not");
        t.addElement("not");
        
        form.addElement("predicate1");
        t.addElement("predicate1");
        
        JSSubstitution result = form.matches(t);
        assertFalse(result.fail());
    }
    
    @Test
    public void testMatchesWithFunctorNotFail() {
        JSPredicateForm form = new JSPredicateForm();
        JSPredicateForm t = new JSPredicateForm();
        form.addElement("not");
        t.addElement("not");
        
        form.addElement("predicate1");
        t.addElement("predicate2");
        
        JSSubstitution result = form.matches(t);
        assertTrue(result.fail());
    }
    
    @Test
    public void testMatchesWithFunctorNotDifferentArgs() {
        JSPredicateForm form = new JSPredicateForm();
        JSPredicateForm t = new JSPredicateForm();
        form.addElement("not");
        t.addElement("not");
        
        form.addElement("predicate1");
        form.addElement("arg1");
        
        t.addElement("predicate1");
        t.addElement("arg2");
        
        JSSubstitution result = form.matches(t);
        assertFalse(result.fail());
    }
    
    @Test
    public void testEqualsWithDifferentSize() {
        JSPredicateForm form = new JSPredicateForm();
        JSPredicateForm t = new JSPredicateForm();
        form.addElement("predicate1");
        
        assertFalse(form.equals(t));
    }
    
    @Test
    public void testEqualsWithDifferentFunctor() {
        JSPredicateForm form = new JSPredicateForm();
        form.addElement("functor1");
        
        JSPredicateForm t = new JSPredicateForm();
        t.addElement("functor2");
        
        assertFalse(form.equals(t));
    }
    
    @Test
    public void testEqualsWithFunctorNot() {
        JSPredicateForm form = new JSPredicateForm();
        form.addElement("not");
        form.addElement("predicate1");
        
        JSPredicateForm t = new JSPredicateForm();
        t.addElement("not");
        t.addElement("predicate1");
        
        assertTrue(form.equals(t));
    }
    
    @Test
    public void testStandarizerPredicateFormWithEmptyForm() {
        JSPredicateForm form = new JSPredicateForm();
        JSPredicateForm result = form.standarizerPredicateForm();
        assertEquals(0, result.size());
    }
}
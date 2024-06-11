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
    public void testJSPredicateFormWithText() {
        String text = "(predicate)";
        JSPredicateForm form = new JSPredicateForm(text);
        assertNotNull(form);
    }
    
    @Test
    public void testJSPredicateFormWithTokenizer() {
        JSPredicateForm form = new JSPredicateForm(null);
        assertNotNull(form);
    }
    
    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitWithNullTokenizer() {
        JSPredicateForm form = new JSPredicateForm();
        form.JSPredicateFormInit(null);
    }
    
    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitWithInvalidTokenType() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("test"));
        JSPredicateForm form = new JSPredicateForm();
        form.JSPredicateFormInit(tokenizer);
    }
    
    @Test
    public void testPrintWithEmptyPredicateForm() {
        JSPredicateForm form = new JSPredicateForm();
        form.print();
    }
    
    @Test
    public void testPrintWithNotPredicateForm() {
        JSPredicateForm form = new JSPredicateForm();
        form.addElement("!");
        form.addElement("predicate");
        form.print();
    }
    
    @Test
    public void testPrintWithPredicateForm() {
        JSPredicateForm form = new JSPredicateForm();
        form.addElement("predicate");
        form.addElement(new JSTerm());
        form.print();
    }
    
    @Test
    public void testToStrWithEmptyPredicateForm() {
        JSPredicateForm form = new JSPredicateForm();
        assertEquals("()", form.toStr().toString());
    }
    
    @Test
    public void testToStrWithNotPredicateForm() {
        JSPredicateForm form = new JSPredicateForm();
        form.addElement("!");
        form.addElement("predicate");
        assertEquals("(!predicate)", form.toStr().toString());
    }
    
    @Test
    public void testToStrWithPredicateForm() {
        JSPredicateForm form = new JSPredicateForm();
        form.addElement("predicate");
        form.addElement(new JSTerm());
        assertEquals("(predicate)", form.toStr().toString());
    }
    
    @Test
    public void testClonePFWithNotPredicateForm() {
        JSPredicateForm form = new JSPredicateForm();
        form.addElement("!");
        form.addElement("predicate");
        JSPredicateForm clone = form.clonePF();
        assertEquals(form, clone);
    }
    
    @Test
    public void testClonePFWithPredicateForm() {
        JSPredicateForm form = new JSPredicateForm();
        form.addElement("predicate");
        form.addElement(new JSTerm());
        JSPredicateForm clone = form.clonePF();
        assertEquals(form, clone);
    }
    
    @Test
    public void testApplySubstitutionPFWithNotPredicateForm() {
        JSPredicateForm form = new JSPredicateForm();
        form.addElement("!");
        form.addElement("predicate");
        JSSubstitution alpha = new JSSubstitution();
        JSPredicateForm result = form.applySubstitutionPF(alpha);
        assertEquals(form, result);
    }
    
    @Test
    public void testApplySubstitutionPFWithPredicateForm() {
        JSPredicateForm form = new JSPredicateForm();
        form.addElement("predicate");
        form.addElement(new JSTerm());
        JSSubstitution alpha = new JSSubstitution();
        JSPredicateForm result = form.applySubstitutionPF(alpha);
        assertEquals(form, result);
    }
    
    @Test
    public void testMatchesWithSamePredicateForm() {
        JSPredicateForm form1 = new JSPredicateForm();
        form1.addElement("predicate");
        form1.addElement(new JSTerm());
        JSPredicateForm form2 = new JSPredicateForm();
        form2.addElement("predicate");
        form2.addElement(new JSTerm());
        JSSubstitution result = form1.matches(form2);
        assertTrue(result);
    }
    
    @Test
    public void testMatchesWithDifferentPredicateForm() {
        JSPredicateForm form1 = new JSPredicateForm();
        form1.addElement("predicate1");
        form1.addElement(new JSTerm());
        JSPredicateForm form2 = new JSPredicateForm();
        form2.addElement("predicate2");
        form2.addElement(new JSTerm());
        JSSubstitution result = form1.matches(form2);
        assertFalse(result);
    }
    
    @Test
    public void testEqualsWithNullPredicateForm() {
        JSPredicateForm form1 = new JSPredicateForm();
        form1.addElement("predicate");
        form1.addElement(new JSTerm());
        JSPredicateForm form2 = null;
        boolean result = form1.equals(form2);
        assertFalse(result);
    }
    
    @Test
    public void testEqualsWithDifferentSize() {
        JSPredicateForm form1 = new JSPredicateForm();
        form1.addElement("predicate");
        form1.addElement(new JSTerm());
        JSPredicateForm form2 = new JSPredicateForm();
        form2.addElement("predicate");
        boolean result = form1.equals(form2);
        assertFalse(result);
    }
    
    @Test
    public void testEqualsWithDifferentFunctor() {
        JSPredicateForm form1 = new JSPredicateForm();
        form1.addElement("predicate1");
        form1.addElement(new JSTerm());
        JSPredicateForm form2 = new JSPredicateForm();
        form2.addElement("predicate2");
        form2.addElement(new JSTerm());
        boolean result = form1.equals(form2);
        assertFalse(result);
    }
    
    @Test
    public void testEqualsWithNotPredicateForm() {
        JSPredicateForm form1 = new JSPredicateForm();
        form1.addElement("!");
        form1.addElement("predicate");
        JSPredicateForm form2 = new JSPredicateForm();
        form2.addElement("!");
        form2.addElement("predicate");
        boolean result = form1.equals(form2);
        assertTrue(result);
    }
    
    @Test
    public void testEqualsWithPredicateForm() {
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
    public void testStandarizerPredicateFormWithNotPredicateForm() {
        JSPredicateForm form = new JSPredicateForm();
        form.addElement("!");
        form.addElement("predicate");
        JSPredicateForm result = form.standarizerPredicateForm();
        assertEquals(form, result);
    }
    
    @Test
    public void testStandarizerPredicateFormWithPredicateForm() {
        JSPredicateForm form = new JSPredicateForm();
        form.addElement("predicate");
        form.addElement(new JSTerm());
        JSPredicateForm result = form.standarizerPredicateForm();
        assertEquals(form, result);
    }
}
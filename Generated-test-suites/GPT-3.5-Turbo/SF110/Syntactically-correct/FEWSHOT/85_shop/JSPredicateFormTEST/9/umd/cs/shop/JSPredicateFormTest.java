
package umd.cs.shop;

import static org.junit.Assert.*;

import java.io.StreamTokenizer;
import java.io.StringReader;

import org.junit.Test;

public class JSPredicateFormTest {
    
    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInit_NullTokenizer_ThrowsJSParserError() {
        StreamTokenizer tokenizer = null;
        JSPredicateForm form = new JSPredicateForm(tokenizer);
    }
    
    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInit_InvalidLeftPar_ThrowsJSParserError() {
        String text = ")predicate(form)";
        JSPredicateForm form = new JSPredicateForm(text);
    }
    
    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInit_MissingPredicate_ThrowsJSParserError() {
        String text = "()";
        JSPredicateForm form = new JSPredicateForm(text);
    }
    
    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInit_UnexpectedRightPar_ThrowsJSParserError() {
        String text = "(predicate))";
        JSPredicateForm form = new JSPredicateForm(text);
    }
    
    @Test
    public void testJSPredicateFormInit_NotOperator_ReturnsCorrectResult() {
        String text = "(!predicate)";
        JSPredicateForm form = new JSPredicateForm(text);
        assertEquals(2, form.size());
        assertEquals("!", form.elementAt(0));
        assertEquals("predicate", form.elementAt(1));
    }
    
    @Test
    public void testJSPredicateFormInit_NotOperatorWithNestedForm_ReturnsCorrectResult() {
        String text = "(! (predicate))";
        JSPredicateForm form = new JSPredicateForm(text);
        assertEquals(2, form.size());
        assertEquals("!", form.elementAt(0));
        assertEquals(2, ((JSPredicateForm)form.elementAt(1)).size());
    }
    
    @Test
    public void testJSPredicateFormInit_CallOperator_ReturnsCorrectResult() {
        String text = "(call (predicate))";
        JSPredicateForm form = new JSPredicateForm(text);
        assertEquals(2, form.size());
        assertEquals("call", form.elementAt(0));
        assertEquals(1, ((JSPredicateForm)form.elementAt(1)).size());
    }
    
    @Test
    public void testJSPredicateFormInit_StandardForm_ReturnsCorrectResult() {
        String text = "(predicate form1 form2)";
        JSPredicateForm form = new JSPredicateForm(text);
        assertEquals(3, form.size());
        assertEquals("predicate", form.elementAt(0));
        assertEquals("form1", form.elementAt(1));
        assertEquals("form2", form.elementAt(2));
    }
    
    @Test
    public void testPrint_EmptyForm_ReturnsCorrectString() {
        JSPredicateForm form = new JSPredicateForm();
        assertEquals(0, form.size());
        form.print();
    }
    
    @Test
    public void testPrint_FormWithNotOperator_ReturnsCorrectString() {
        JSPredicateForm form = new JSPredicateForm("! (predicate)");
        assertEquals(2, form.size());
        form.print();
    }
    
    @Test
    public void testToStr_EmptyForm_ReturnsCorrectString() {
        JSPredicateForm form = new JSPredicateForm();
        assertEquals(0, form.size());
        StringBuffer str = form.toStr();
        assertEquals("()", str.toString());
    }
    
    @Test
    public void testToStr_FormWithNotOperator_ReturnsCorrectString() {
        JSPredicateForm form = new JSPredicateForm("! (predicate)");
        assertEquals(2, form.size());
        StringBuffer str = form.toStr();
        assertEquals("(! (predicate))", str.toString());
    }
    
    @Test
    public void testClonePF_EmptyForm_ReturnsNewForm() {
        JSPredicateForm form = new JSPredicateForm();
        assertEquals(0, form.size());
        JSPredicateForm clone = form.clonePF();
        assertNotSame(form, clone);
    }
    
    @Test
    public void testMatches_FormNotEqualToT_ReturnsFailure() {
        JSPredicateForm form = new JSPredicateForm("(predicate1 form1)");
        JSPredicateForm t = new JSPredicateForm("(predicate2 form2)");
        JSSubstitution result = form.matches(t);
        assertTrue(result.fail());
    }
    
    @Test
    public void testMatches_FormEqualToT_ReturnsSuccessfulSubstitution() {
        JSPredicateForm form = new JSPredicateForm("(pred form1 form2)");
        JSPredicateForm t = new JSPredicateForm("(pred term1 term2)");
        JSSubstitution result = form.matches(t);
        assertFalse(result.fail());
    }
    
    @Test
    public void testEquals_FormNotEqualToT_ReturnsFalse() {
        JSPredicateForm form = new JSPredicateForm("(pred form1 form2)");
        JSPredicateForm t = new JSPredicateForm("(pred term1 term2)");
        assertFalse(form.equals(t));
    }
    
    @Test
    public void testEquals_FormEqualToT_ReturnsTrue() {
        JSPredicateForm form = new JSPredicateForm("(pred form1 form2)");
        JSPredicateForm t = new JSPredicateForm("(pred form1 form2)");
        assertTrue(form.equals(t));
    }
    
    @Test
    public void testStandarizerPredicateForm_EmptyForm_ReturnsNewStandardizedForm() {
        JSPredicateForm form = new JSPredicateForm();
        assertEquals(0, form.size());
        JSPredicateForm standardizedForm = form.standarizerPredicateForm();
        assertNotSame(form, standardizedForm);
    }
    
    @Test
    public void testStandarizerPredicateForm_FormWithNotOperator_ReturnsNewStandardizedForm() {
        JSPredicateForm form = new JSPredicateForm("(! (predicate))");
        assertEquals(2, form.size());
        JSPredicateForm standardizedForm = form.standarizerPredicateForm();
        assertNotSame(form, standardizedForm);
    }
}


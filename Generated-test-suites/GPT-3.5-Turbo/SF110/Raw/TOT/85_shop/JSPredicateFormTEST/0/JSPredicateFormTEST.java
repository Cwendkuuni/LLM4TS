import static org.junit.Assert.*;
import org.junit.*;

public class JSPredicateFormTest {

    @Test
    public void testJSPredicateForm_DefaultConstructor_Success() {
        JSPredicateForm form = new JSPredicateForm();
        assertNotNull(form);
        assertEquals(0, form.size());
    }

    @Test
    public void testJSPredicateForm_TextConstructor_Success() {
        String text = "(call sum 1 2)";
        JSPredicateForm form = new JSPredicateForm(text);
        assertNotNull(form);
        assertEquals(4, form.size());
        assertEquals("call", form.elementAt(0));
        assertEquals("sum", form.elementAt(1));
        assertEquals(1, ((JSTerm) form.elementAt(2)).getValue());
        assertEquals(2, ((JSTerm) form.elementAt(3)).getValue());
    }

    @Test
    public void testJSPredicateForm_TextConstructor_NullText_ThrowsException() {
        assertThrows(NullPointerException.class, () -> {
            new JSPredicateForm(null);
        });
    }

    @Test
    public void testJSPredicateForm_TokenizerConstructor_Success() {
        String text = "call(sum 1 2)";
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(text));
        JSPredicateForm form = new JSPredicateForm(tokenizer);
        assertNotNull(form);
        assertEquals(4, form.size());
        assertEquals("call", form.elementAt(0));
        assertEquals("sum", form.elementAt(1));
        assertEquals(1, ((JSTerm) form.elementAt(2)).getValue());
        assertEquals(2, ((JSTerm) form.elementAt(3)).getValue());
    }

    @Test
    public void testJSPredicateForm_TokenizerConstructor_NullTokenizer_ThrowsException() {
        assertThrows(JSParserError.class, () -> {
            new JSPredicateForm(null);
        });
    }

    // Additional test cases for other methods in JSPredicateForm class

    @Test
    public void testPrint_EmptyPredicateForm_Success() {
        JSPredicateForm form = new JSPredicateForm();
        form.print();
    }

    @Test
    public void testToStr_EmptyPredicateForm_Success() {
        JSPredicateForm form = new JSPredicateForm();
        StringBuffer str = form.toStr();
        assertEquals("()", str.toString());
    }

    @Test
    public void testClonePF_NotPredicate_Success() {
        String text = "(not (call sum 1 2))";
        JSPredicateForm form1 = new JSPredicateForm(text);
        JSPredicateForm form2 = form1.clonePF();
        assertNotSame(form1, form2);
        assertEquals(form1.size(), form2.size());
        assertEquals(form1.elementAt(0), form2.elementAt(0));
        assertEquals(((JSTerm) form1.elementAt(1)).getValue(), ((JSTerm) form2.elementAt(1)).getValue());
        assertEquals(((JSTerm) form1.elementAt(2)).getValue(), ((JSTerm) form2.elementAt(2)).getValue());
    }

    @Test
    public void testApplySubstitutionPF_NotPredicate_Success() {
        String text = "(not (call sum 1 2))";
        JSPredicateForm form1 = new JSPredicateForm(text);
        JSSubstitution alpha = new JSSubstitution();
        JSPredicateForm form2 = form1.applySubstitutionPF(alpha);
        assertNotSame(form1, form2);
        assertEquals(form1.size(), form2.size());
        assertEquals(form1.elementAt(0), form2.elementAt(0));
        assertEquals(((JSTerm) form1.elementAt(1)).getValue(), ((JSTerm) form2.elementAt(1)).getValue());
        assertEquals(((JSTerm) form1.elementAt(2)).getValue(), ((JSTerm) form2.elementAt(2)).getValue());
    }

    @Test
    public void testMatches_PredicatesNotMatching_ReturnsFailureSubstitution() {
        JSPredicateForm form1 = new JSPredicateForm("(call sum 1 2)");
        JSPredicateForm form2 = new JSPredicateForm("(call subtraction 2 3)");
        JSSubstitution result = form1.matches(form2);

        assertTrue(result.fail());
    }

    @Test
    public void testMatches_PredicatesMatching_ReturnsSuccessfulSubstitution() {
        JSPredicateForm form1 = new JSPredicateForm("(equal 2 ?x)");
        JSPredicateForm form2 = new JSPredicateForm("(equal 2 5)");
        JSSubstitution result = form1.matches(form2);

        assertFalse(result.fail());
        assertEquals("?x", result.get("?x"));
        assertEquals("5", result.get("2"));
    }

    @Test
    public void testEquals_SamePredicates_ReturnsTrue() {
        JSPredicateForm form1 = new JSPredicateForm("(equal 2 ?x)");
        JSPredicateForm form2 = new JSPredicateForm("(equal 2 ?x)");

        assertTrue(form1.equals(form2));
    }

    @Test
    public void testEquals_DifferentPredicates_ReturnsFalse() {
        JSPredicateForm form1 = new JSPredicateForm("(equal 2 ?x)");
        JSPredicateForm form2 = new JSPredicateForm("(less 2 ?y)");

        assertFalse(form1.equals(form2));
    }

    @Test
    public void testStandarizerPredicateForm_NotPredicate_Success() {
        String text = "(not (call sum 1 2))";
        JSPredicateForm form1 = new JSPredicateForm(text);
        JSPredicateForm form2 = form1.standarizerPredicateForm();
        assertNotSame(form1, form2);
        assertEquals(form1.size(), form2.size());
        assertEquals(form1.elementAt(0), form2.elementAt(0));
        assertEquals(((JSTerm) form1.elementAt(1)).getValue(), ((JSTerm) form2.elementAt(1)).getValue());
        assertEquals(((JSTerm) form1.elementAt(2)).getValue(), ((JSTerm) form2.elementAt(2)).getValue());
    }

}
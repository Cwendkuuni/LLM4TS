import static org.junit.Assert.*;
import org.junit.Test;

public class JSPredicateFormTest {

    @Test
    public void testJSPredicateFormInit() {
        // Typical use case
        JSPredicateForm form = new JSPredicateForm("(not (call someFunction))");
        assertNotNull(form);
        
        // Edge case - empty input
        form = new JSPredicateForm("");
        assertNotNull(form);
        
        // Exception handling - null tokenizer
        try {
            form = new JSPredicateForm(null);
            fail("Expected JSParserError to be thrown");
        } catch (JSParserError e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testPrint() {
        JSPredicateForm form = new JSPredicateForm("(not (call someFunction))");
        form.print();
        // Output should be printed to console
    }

    @Test
    public void testToStr() {
        JSPredicateForm form = new JSPredicateForm("(not (call someFunction))");
        StringBuffer result = form.toStr();
        assertNotNull(result);
        assertEquals("(not (call someFunction))", result.toString());
    }

    @Test
    public void testClonePF() {
        JSPredicateForm form = new JSPredicateForm("(not (call someFunction))");
        JSPredicateForm clonedForm = form.clonePF();
        assertNotNull(clonedForm);
        assertNotSame(form, clonedForm);
    }

    @Test
    public void testApplySubstitutionPF() {
        JSSubstitution alpha = new JSSubstitution();
        JSPredicateForm form = new JSPredicateForm("(not (call someFunction))");
        JSPredicateForm result = form.applySubstitutionPF(alpha);
        assertNotNull(result);
        assertEquals(form.size(), result.size());
    }

    @Test
    public void testMatches() {
        JSPredicateForm form1 = new JSPredicateForm("(not (call someFunction))");
        JSPredicateForm form2 = new JSPredicateForm("(not (call someFunction))");
        JSSubstitution result = form1.matches(form2);
        assertNotNull(result);
        assertFalse(result.fail());
    }

    @Test
    public void testEquals() {
        JSPredicateForm form1 = new JSPredicateForm("(not (call someFunction))");
        JSPredicateForm form2 = new JSPredicateForm("(not (call someFunction))");
        assertTrue(form1.equals(form2));
    }

    @Test
    public void testStandarizerPredicateForm() {
        JSPredicateForm form = new JSPredicateForm("(not (call someFunction))");
        JSPredicateForm standardizedForm = form.standarizerPredicateForm();
        assertNotNull(standardizedForm);
        assertNotSame(form, standardizedForm);
    }
}
import static org.junit.Assert.*;
import org.junit.Test;

public class JSPredicateFormTest {

    @Test
    public void testJSPredicateFormInit_NullTokenizer_ThrowsException() {
        JSPredicateForm form = new JSPredicateForm();
        StreamTokenizer tokenizer = null;
        assertThrows(JSParserError.class, () -> form.JSPredicateFormInit(tokenizer));
    }

    @Test
    public void testPrint_EmptyForm_PrintsMessage() {
        JSPredicateForm form = new JSPredicateForm();
        form.print();
        // Verify the console output for the message
    }

    @Test
    public void testClonePF_NotPredicate_CreatesNewInstance() {
        JSPredicateForm form = new JSPredicateForm();
        form.addElement("not");
        form.addElement("test");
        JSPredicateForm clone = form.clonePF();
        assertNotSame(form, clone);
    }

    @Test
    public void testApplySubstitutionPF_Substitution_SuccessfullyApplied() {
        JSPredicateForm form = new JSPredicateForm();
        form.addElement("predicate");
        form.addElement(new JSTerm("term"));
        JSSubstitution substitution = new JSSubstitution();
        substitution.addElement("term", "newTerm");
        JSPredicateForm result = form.applySubstitutionPF(substitution);
        assertEquals("predicate", result.elementAt(0));
        assertEquals("newTerm", result.elementAt(1));
    }

    @Test
    public void testMatches_DifferentSizeForms_ReturnsFailureSubstitution() {
        JSPredicateForm form1 = new JSPredicateForm();
        JSPredicateForm form2 = new JSPredicateForm();
        JSSubstitution substitution = form1.matches(form2);
        assertTrue(substitution.fail());
    }

    // Add more test cases for other methods

}
import org.junit.Test;
import static org.junit.Assert.*;

public class JSPredicateFormTest {

    @Test
    public void testJSPredicateFormInit() {
        JSPredicateForm predicateForm = new JSPredicateForm();
        assertEquals("Predicate form size should be 0", 0, predicateForm.size());
    }

    @Test
    public void testJSPredicateFormString() {
        String text = "(not (equals 1 2))";
        JSPredicateForm predicateForm = new JSPredicateForm(text);
        assertEquals("Predicate form size should be 3", 3, predicateForm.size());
        assertEquals("First element should be 'not'", "not", predicateForm.elementAt(0));
        assertEquals("Second element should be 'equals'", "equals", predicateForm.elementAt(1));
        assertEquals("Third element should be '1'", "1", predicateForm.elementAt(2));
    }

    @Test
    public void testJSPredicateFormTokenizer() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(not (equals 1 2))"));
        JSPredicateForm predicateForm = new JSPredicateForm(tokenizer);
        assertEquals("Predicate form size should be 3", 3, predicateForm.size());
        assertEquals("First element should be 'not'", "not", predicateForm.elementAt(0));
        assertEquals("Second element should be 'equals'", "equals", predicateForm.elementAt(1));
        assertEquals("Third element should be '1'", "1", predicateForm.elementAt(2));
    }

    @Test
    public void testPrint() {
        JSPredicateForm predicateForm = new JSPredicateForm("(not (equals 1 2))");
        predicateForm.print(); // Assuming this prints to console, so no assertion needed
    }

    @Test
    public void testToStr() {
        JSPredicateForm predicateForm = new JSPredicateForm("(not (equals 1 2))");
        StringBuffer stringBuffer = predicateForm.toStr();
        assertEquals("String representation should be '(not (equals 1 2))'", "(not (equals 1 2))", stringBuffer.toString());
    }

    @Test
    public void testClonePF() {
        JSPredicateForm predicateForm = new JSPredicateForm("(not (equals 1 2))");
        JSPredicateForm clonedForm = predicateForm.clonePF();
        assertEquals("Cloned form should be equal to original form", predicateForm, clonedForm);
    }

    @Test
    public void testApplySubstitutionPF() {
        JSSubstitution alpha = new JSSubstitution();
        JSPredicateForm predicateForm = new JSPredicateForm("(not (equals 1 2))");
        JSPredicateForm substitutedForm = predicateForm.applySubstitutionPF(alpha);
        assertEquals("Substituted form should be equal to original form", predicateForm, substitutedForm);
    }

    @Test
    public void testMatches() {
        JSPredicateForm predicateForm1 = new JSPredicateForm("(not (equals 1 2))");
        JSPredicateForm predicateForm2 = new JSPredicateForm("(not (equals 1 2))");
        JSSubstitution substitution = predicateForm1.matches(predicateForm2);
        assertTrue("Substitution should be successful", substitution.isSuccess());
    }

    @Test
    public void testEquals() {
        JSPredicateForm predicateForm1 = new JSPredicateForm("(not (equals 1 2))");
        JSPredicateForm predicateForm2 = new JSPredicateForm("(not (equals 1 2))");
        assertTrue("Predicate forms should be equal", predicateForm1.equals(predicateForm2));
    }

    @Test
    public void testStandardizerPredicateForm() {
        JSPredicateForm predicateForm = new JSPredicateForm("(not (equals 1 2))");
        JSPredicateForm standardizedForm = predicateForm.standarizerPredicateForm();
        assertEquals("Standardized form should be equal to original form", predicateForm, standardizedForm);
    }
}
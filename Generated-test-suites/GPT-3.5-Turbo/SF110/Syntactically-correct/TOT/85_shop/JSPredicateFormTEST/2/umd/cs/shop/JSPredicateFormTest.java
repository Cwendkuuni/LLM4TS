import org.junit.Test;
import static org.junit.Assert.*;

public class JSPredicateFormTest {

    @Test
    public void testJSPredicateForm() {
        // Typical use case
        JSPredicateForm form1 = new JSPredicateForm("(call (age ?x))");
        assertEquals("(call (age ?x))", form1.toStr().toString());

        // Edge case - empty string input
        JSPredicateForm form2 = new JSPredicateForm("");
        assertEquals("Predicateform.print(): 0 elements", form2.toStr().toString());

        // Error scenario - null input
        try {
            JSPredicateForm form3 = new JSPredicateForm(null);
            fail("Should throw JSParserError");
        } catch (JSParserError e) {
            // Exception should be thrown
        }
        
        // Error scenario - unmatched parentheses
        try {
            JSPredicateForm form4 = new JSPredicateForm("(call (age ?x)");
            fail("Should throw JSParserError");
        } catch (JSParserError e) {
            // Exception should be thrown
        }
    }

    @Test
    public void testPrint() {
        // Typical use case
        JSPredicateForm form1 = new JSPredicateForm("(call (age ?x))");
        form1.print();
        // Expected output: "(call (age ?x))"

        // Edge case - empty form
        JSPredicateForm form2 = new JSPredicateForm();
        form2.print();
        // Expected output: "Predicateform.print(): 0 elements"
    }

    @Test
    public void testToStr() {
        // Typical use case
        JSPredicateForm form1 = new JSPredicateForm("(call (age ?x))");
        assertEquals("(call (age ?x))", form1.toStr().toString());

        // Edge case - empty form
        JSPredicateForm form2 = new JSPredicateForm();
        assertEquals("Predicateform.print(): 0 elements", form2.toStr().toString());
    }

    @Test
    public void testClonePF() {
        // Typical use case
        JSPredicateForm form1 = new JSPredicateForm("(call (age ?x))");
        JSPredicateForm clone1 = form1.clonePF();
        assertEquals(form1.toStr().toString(), clone1.toStr().toString());

        // Typical use case - with negation
        JSPredicateForm form2 = new JSPredicateForm("(not (call (age ?x)))");
        JSPredicateForm clone2 = form2.clonePF();
        assertEquals(form2.toStr().toString(), clone2.toStr().toString());

        // Edge case - empty form
        JSPredicateForm form3 = new JSPredicateForm();
        JSPredicateForm clone3 = form3.clonePF();
        assertEquals(form3.toStr().toString(), clone3.toStr().toString());
    }

    @Test
    public void testApplySubstitutionPF() {
        // Typical use case
        JSPredicateForm form1 = new JSPredicateForm("(call (age ?x))");
        JSSubstitution alpha1 = new JSSubstitution();
        alpha1.addSubstitution("?x", "John");
        JSPredicateForm result1 = form1.applySubstitutionPF(alpha1);
        assertEquals("(call (age John))", result1.toStr().toString());

        // Typical use case - with negation
        JSPredicateForm form2 = new JSPredicateForm("(not (call (age ?x)))");
        JSSubstitution alpha2 = new JSSubstitution();
        alpha2.addSubstitution("?x", "John");
        JSPredicateForm result2 = form2.applySubstitutionPF(alpha2);
        assertEquals("(not (call (age John)))", result2.toStr().toString());

        // Edge case - empty form
        JSPredicateForm form3 = new JSPredicateForm();
        JSSubstitution alpha3 = new JSSubstitution();
        JSPredicateForm result3 = form3.applySubstitutionPF(alpha3);
        assertEquals("Predicateform.print(): 0 elements", result3.toStr().toString());
    }

    @Test
    public void testMatches() {
        // Typical use case - matching forms
        JSPredicateForm form1 = new JSPredicateForm("(call (age ?x))");
        JSPredicateForm form2 = new JSPredicateForm("(call (age ?x))");
        JSSubstitution result1 = form1.matches(form2);
        assertFalse(result1.fail());
        
        // Typical use case - not matching forms
        JSPredicateForm form3 = new JSPredicateForm("(call (age ?x))");
        JSPredicateForm form4 = new JSPredicateForm("(call (name ?x))");
        JSSubstitution result2 = form3.matches(form4);
        assertTrue(result2.fail());

        // Edge case - empty forms
        JSPredicateForm form5 = new JSPredicateForm();
        JSPredicateForm form6 = new JSPredicateForm();
        JSSubstitution result3 = form5.matches(form6);
        assertFalse(result3.fail());
    }

    @Test
    public void testEquals() {
        // Typical use case - equal forms
        JSPredicateForm form1 = new JSPredicateForm("(call (age ?x))");
        JSPredicateForm form2 = new JSPredicateForm("(call (age ?x))");
        assertTrue(form1.equals(form2));
        
        // Typical use case - not equal forms
        JSPredicateForm form3 = new JSPredicateForm("(call (age ?x))");
        JSPredicateForm form4 = new JSPredicateForm("(call (name ?x))");
        assertFalse(form3.equals(form4));

        // Edge case - empty forms
        JSPredicateForm form5 = new JSPredicateForm();
        JSPredicateForm form6 = new JSPredicateForm();
        assertTrue(form5.equals(form6));
    }

    @Test
    public void testStandarizerPredicateForm() {
        // Typical use case
        JSPredicateForm form1 = new JSPredicateForm("(call (age ?x))");
        JSPredicateForm result1 = form1.standarizerPredicateForm();
        assertEquals("(call (age ?x))", result1.toStr().toString());

        // Typical use case - with negation
        JSPredicateForm form2 = new JSPredicateForm("(not (call (age ?x)))");
        JSPredicateForm result2 = form2.standarizerPredicateForm();
        assertEquals("(not (call (age ?x)))", result2.toStr().toString());

        // Edge case - empty form
        JSPredicateForm form3 = new JSPredicateForm();
        JSPredicateForm result3 = form3.standarizerPredicateForm();
        assertEquals("Predicateform.print(): 0 elements", result3.toStr().toString());
    }
}
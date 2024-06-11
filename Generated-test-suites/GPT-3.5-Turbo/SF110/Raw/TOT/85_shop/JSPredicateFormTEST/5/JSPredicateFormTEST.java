import org.junit.Test;
import static org.junit.Assert.*;

public class JSPredicateFormTest {

    @Test
    public void testConstructor1() {
        JSPredicateForm form = new JSPredicateForm();
        assertEquals(0, form.size());
    }

    @Test
    public void testConstructor2() {
        String text = "(not (call (+ 1 2)))";
        JSPredicateForm form = new JSPredicateForm(text);

        assertEquals(3, form.size());
        assertEquals("not", form.elementAt(0));
        assertEquals("call", form.elementAt(1));
        assertEquals("(+ 1 2)", form.elementAt(2));
    }

    @Test
    public void testConstructor3() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(call foo bar)"));
        JSPredicateForm form = new JSPredicateForm(tokenizer);

        assertEquals(3, form.size());
        assertEquals("call", form.elementAt(0));
        assertEquals("foo", form.elementAt(1));
        assertEquals("bar", form.elementAt(2));
    }

    @Test(expected = JSParserError.class)
    public void testConstructor_InvalidTokenizer() {
        StreamTokenizer tokenizer = null;
        JSPredicateForm form = new JSPredicateForm(tokenizer);
    }

    @Test(expected = JSParserError.class)
    public void testConstructor_UnexpectedLeftPar() {
        String text = "(not";
        JSPredicateForm form = new JSPredicateForm(text);
    }

    @Test(expected = JSParserError.class)
    public void testConstructor_ExpectedPredicate() {
        String text = "()";
        JSPredicateForm form = new JSPredicateForm(text);
    }

    @Test(expected = JSParserError.class)
    public void testConstructor_UnexpectedRightPar() {
        String text = "(not))";
        JSPredicateForm form = new JSPredicateForm(text);
    }

    @Test(expected = JSParserError.class)
    public void testConstructor_NotOperatorNameExpected() {
        String text = "(not %%%)";
        JSPredicateForm form = new JSPredicateForm(text);
    }

    @Test(expected = JSParserError.class)
    public void testConstructor_InvalidPredicateForm() {
        String text = "(call foo ))";
        JSPredicateForm form = new JSPredicateForm(text);
    }

    @Test
    public void testPrint() {
        JSPredicateForm form = new JSPredicateForm("(not (call foo bar))");
        form.print();
        //Manual verification of the output
    }

    @Test
    public void testToStr() {
        JSPredicateForm form = new JSPredicateForm("(not (call foo bar))");
        String expectedStr = "(not (call foo bar))";
        assertEquals(expectedStr, form.toStr().toString());
    }

    @Test
    public void testClonePF() {
        JSPredicateForm original = new JSPredicateForm("(not (call foo bar))");
        JSPredicateForm clone = original.clonePF();

        assertEquals(original.elementAt(0), clone.elementAt(0));
        assertEquals(original.elementAt(1).toStr().toString(), clone.elementAt(1).toStr().toString());
    }

    @Test
    public void testApplySubstitutionPF() {
        JSSubstitution alpha = new JSSubstitution();
        alpha.addSubstitution("foo", "fizz");
        alpha.addSubstitution("bar", "buzz");

        JSPredicateForm form = new JSPredicateForm("(not (call foo bar))");
        JSPredicateForm updatedForm = form.applySubstitutionPF(alpha);

        assertEquals("(not (call fizz buzz))", updatedForm.toStr().toString());
    }

    @Test
    public void testMatches_SameForm() {
        JSPredicateForm form1 = new JSPredicateForm("(call foo bar)");
        JSPredicateForm form2 = new JSPredicateForm("(call foo bar)");

        JSSubstitution substitution = form1.matches(form2);

        assertFalse(substitution.fail());
        assertTrue(substitution.isEmpty());
    }

    @Test
    public void testMatches_DifferentForm() {
        JSPredicateForm form1 = new JSPredicateForm("(call foo bar)");
        JSPredicateForm form2 = new JSPredicateForm("(not (call foo bar))");

        JSSubstitution substitution = form1.matches(form2);

        assertTrue(substitution.fail());
        assertTrue(substitution.isEmpty());
    }

    @Test
    public void testMatches_MatchingForm() {
        JSPredicateForm form1 = new JSPredicateForm("(call foo bar)");
        JSPredicateForm form2 = new JSPredicateForm("(call fizz buzz)");

        JSSubstitution substitution = form1.matches(form2);

        assertFalse(substitution.fail());
        assertTrue(substitution.containsSubstitution("foo", "fizz"));
        assertTrue(substitution.containsSubstitution("bar", "buzz"));
    }

    @Test
    public void testEquals_SameForm() {
        JSPredicateForm form1 = new JSPredicateForm("(call foo bar)");
        JSPredicateForm form2 = new JSPredicateForm("(call foo bar)");

        assertTrue(form1.equals(form2));
    }

    @Test
    public void testEquals_DifferentForm() {
        JSPredicateForm form1 = new JSPredicateForm("(call foo bar)");
        JSPredicateForm form2 = new JSPredicateForm("(not (call foo bar))");

        assertFalse(form1.equals(form2));
    }

    @Test
    public void testStandarizerPredicateForm() {
        JSPredicateForm form1 = new JSPredicateForm("(call foo bar)");
        JSPredicateForm standardForm1 = form1.standarizerPredicateForm();

        assertEquals("(call foo bar)", standardForm1.toStr().toString());

        JSPredicateForm form2 = new JSPredicateForm("(not (call foo bar))");
        JSPredicateForm standardForm2 = form2.standarizerPredicateForm();

        assertEquals("(not (call foo bar))", standardForm2.toStr().toString());
    }
}
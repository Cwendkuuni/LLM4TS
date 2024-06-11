import org.junit.Test;
import static org.junit.Assert.*;

public class JSPredicateFormTest {

    @Test
    public void testJSPredicateFormConstructor1() {
        JSPredicateForm form = new JSPredicateForm();
        assertNotNull(form);
    }
  
    @Test
    public void testJSPredicateFormConstructor2() {
        String text = "(predicate)";
        JSPredicateForm form = new JSPredicateForm(text);
        assertEquals(text, form.toStr().toString());
    }
  
    @Test
    public void testJSPredicateFormConstructor3() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(predicate)"));
        JSPredicateForm form = new JSPredicateForm(tokenizer);
        assertEquals("(predicate)", form.toStr().toString());
    }
  
    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitNullTokenizer() {
        StreamTokenizer tokenizer = null;
        JSPredicateForm form = new JSPredicateForm();
        form.JSPredicateFormInit(tokenizer);
    }
  
    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitMissingLeftPar() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("predicate)"));
        JSPredicateForm form = new JSPredicateForm();
        form.JSPredicateFormInit(tokenizer);
    }
  
    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitUnexpectedRightPar() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(predicate))"));
        JSPredicateForm form = new JSPredicateForm();
        form.JSPredicateFormInit(tokenizer);
    }
  
    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitMissingPredicate() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("()"));
        JSPredicateForm form = new JSPredicateForm();
        form.JSPredicateFormInit(tokenizer);
    }
  
    @Test
    public void testJSPredicateFormInitSingleNegatedPredicate() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(!predicate)"));
        JSPredicateForm form = new JSPredicateForm();
        form.JSPredicateFormInit(tokenizer);
        assertEquals("(!predicate)", form.toStr().toString());
    }
  
    @Test
    public void testJSPredicateFormInitNestedNegatedPredicate() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(not (predicate))"));
        JSPredicateForm form = new JSPredicateForm();
        form.JSPredicateFormInit(tokenizer);
        assertEquals("(not (predicate))", form.toStr().toString());
    }
  
    @Test
    public void testJSPredicateFormInitCallPredicate() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(call (predicate))"));
        JSPredicateForm form = new JSPredicateForm();
        form.JSPredicateFormInit(tokenizer);
        assertEquals("(call (predicate))", form.toStr().toString());
    }
  
    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitMissingRightPar() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(predicate"));
        JSPredicateForm form = new JSPredicateForm();
        form.JSPredicateFormInit(tokenizer);
    }
  
    @Test
    public void testPrintWithEmptyForm() {
        JSPredicateForm form = new JSPredicateForm();
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        form.print();
        assertEquals("Predicateform.print(): 0 elements\n", outContent.toString());
    }
  
    @Test
    public void testPrintWithNegatedPredicate() {
        JSPredicateForm form = new JSPredicateForm();
        form.addElement("not");
        form.addElement(new JSPredicateForm("(predicate)"));
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        form.print();
        assertEquals("(not (predicate) ) ", outContent.toString());
    }
  
    @Test
    public void testToStrWithEmptyForm() {
        JSPredicateForm form = new JSPredicateForm();
        StringBuffer result = form.toStr();
        assertEquals("()", result.toString());
    }
  
    @Test
    public void testToStrWithNegatedPredicate() {
        JSPredicateForm form = new JSPredicateForm();
        form.addElement("not");
        form.addElement(new JSPredicateForm("(predicate)"));
        StringBuffer result = form.toStr();
        assertEquals("(not (predicate))", result.toString());
    }
  
    @Test
    public void testClonePFWithNegatedPredicate() {
        JSPredicateForm form = new JSPredicateForm();
        form.addElement("not");
        form.addElement(new JSPredicateForm("(predicate)"));
        JSPredicateForm clone = form.clonePF();
        assertEquals(form.toStr().toString(), clone.toStr().toString());
    }
  
    @Test
    public void testApplySubstitutionPFWithNegatedPredicate() {
        JSPredicateForm form = new JSPredicateForm();
        form.addElement("not");
        form.addElement(new JSPredicateForm("(predicate)"));
        JSSubstitution alpha = new JSSubstitution();
        JSPredicateForm result = form.applySubstitutionPF(alpha);
        assertEquals(form.toStr().toString(), result.toStr().toString());
    }
  
    @Test
    public void testMatchesWithEqualForms() {
        JSPredicateForm form1 = new JSPredicateForm("(predicate)");
        JSPredicateForm form2 = new JSPredicateForm("(predicate)");
        JSSubstitution result = form1.matches(form2);
        assertFalse(result.fail());
    }
  
    @Test
    public void testMatchesWithDifferentSizes() {
        JSPredicateForm form1 = new JSPredicateForm("(predicate)");
        JSPredicateForm form2 = new JSPredicateForm("(predicate argument)");
        JSSubstitution result = form1.matches(form2);
        assertTrue(result.fail());
    }
  
    @Test
    public void testMatchesWithDifferentFunctors() {
        JSPredicateForm form1 = new JSPredicateForm("(predicate)");
        JSPredicateForm form2 = new JSPredicateForm("(otherPredicate)");
        JSSubstitution result = form1.matches(form2);
        assertTrue(result.fail());
    }
  
    @Test
    public void testMatchesWithNestedNegatedPredicates() {
        JSPredicateForm form1 = new JSPredicateForm("(not (predicate))");
        JSPredicateForm form2 = new JSPredicateForm("(not (predicate))");
        JSSubstitution result = form1.matches(form2);
        assertFalse(result.fail());
    }
  
    @Test
    public void testMatchesWithNonMatchingTerms() {
        JSPredicateForm form1 = new JSPredicateForm("(predicate argument1 argument2)");
        JSPredicateForm form2 = new JSPredicateForm("(predicate argument1 argument3)");
        JSSubstitution result = form1.matches(form2);
        assertTrue(result.fail());
    }
  
    @Test
    public void testEqualsWithEqualForms() {
        JSPredicateForm form1 = new JSPredicateForm("(predicate)");
        JSPredicateForm form2 = new JSPredicateForm("(predicate)");
        assertTrue(form1.equals(form2));
    }
  
    @Test
    public void testEqualsWithDifferentSizes() {
        JSPredicateForm form1 = new JSPredicateForm("(predicate)");
        JSPredicateForm form2 = new JSPredicateForm("(predicate argument)");
        assertFalse(form1.equals(form2));
    }
  
    @Test
    public void testEqualsWithDifferentFunctors() {
        JSPredicateForm form1 = new JSPredicateForm("(predicate)");
        JSPredicateForm form2 = new JSPredicateForm("(otherPredicate)");
        assertFalse(form1.equals(form2));
    }
  
    @Test
    public void testEqualsWithNestedNegatedPredicates() {
        JSPredicateForm form1 = new JSPredicateForm("(not (predicate))");
        JSPredicateForm form2 = new JSPredicateForm("(not (predicate))");
        assertTrue(form1.equals(form2));
    }
  
    @Test
    public void testEqualsWithNonMatchingTerms() {
        JSPredicateForm form1 = new JSPredicateForm("(predicate argument1 argument2)");
        JSPredicateForm form2 = new JSPredicateForm("(predicate argument1 argument3)");
        assertFalse(form1.equals(form2));
    }
  
    @Test
    public void testStandarizerPredicateFormWithNegatedPredicate() {
        JSPredicateForm form = new JSPredicateForm();
        form.addElement("not");
        form.addElement(new JSPredicateForm("(predicate)"));
        JSPredicateForm result = form.standarizerPredicateForm();
        assertEquals("(not (predicate))", result.toStr().toString());
    }
}
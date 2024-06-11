import static org.junit.Assert.*;
import org.junit.*;

public class JSPredicateFormTest {

  @Test
  public void testJSPredicateFormDefaultConstructor() {
    JSPredicateForm form = new JSPredicateForm();
    assertEquals(0, form.size());
  }

  @Test
  public void testJSPredicateFormStringConstructor() {
    String text = "(predicate arg1 arg2)";
    JSPredicateForm form = new JSPredicateForm(text);
    assertEquals(3, form.size());
    assertEquals("predicate", form.elementAt(0));
    assertEquals("arg1", form.elementAt(1));
    assertEquals("arg2", form.elementAt(2));
  }

  @Test
  public void testJSPredicateFormTokenizerConstructor() {
    StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(predicate arg1 arg2)"));
    JSPredicateForm form = new JSPredicateForm(tokenizer);
    assertEquals(3, form.size());
    assertEquals("predicate", form.elementAt(0));
    assertEquals("arg1", form.elementAt(1));
    assertEquals("arg2", form.elementAt(2));
  }

  @Test(expected = JSParserError.class)
  public void testJSPredicateFormInitNullTokenizer() {
    JSPredicateForm form = new JSPredicateForm();
    form.JSPredicateFormInit(null);
  }

  @Test(expected = JSParserError.class)
  public void testJSPredicateFormInitMissingLeftPar() {
    StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("predicate arg1 arg2)"));
    JSPredicateForm form = new JSPredicateForm();
    form.JSPredicateFormInit(tokenizer);
  }

  // Add more test cases for the remaining methods
  
  @Test
  public void testPrintEmptyPredicateForm() {
    JSPredicateForm form = new JSPredicateForm();
    form.print(); // No assertion, just checking for exception
  }

  @Test
  public void testPrintPredicateFormWithElements() {
    JSPredicateForm form = new JSPredicateForm();
    form.addElement("predicate");
    form.addElement("arg1");
    form.addElement("arg2");
    form.print(); // No assertion, just checking for exception
  }

  @Test
  public void testToStrEmptyPredicateForm() {
    JSPredicateForm form = new JSPredicateForm();
    assertEquals("()", form.toStr().toString());
  }

  @Test
  public void testToStrPredicateFormWithElements() {
    JSPredicateForm form = new JSPredicateForm();
    form.addElement("predicate");
    form.addElement("arg1");
    form.addElement("arg2");
    assertEquals("(predicate arg1 arg2)", form.toStr().toString());
  }

  @Test
  public void testClonePFEmptyPredicateForm() {
    JSPredicateForm form = new JSPredicateForm();
    JSPredicateForm clone = form.clonePF();
    assertEquals(form.size(), clone.size());
  }

  @Test
  public void testClonePFPredicateFormWithElements() {
    JSPredicateForm form = new JSPredicateForm();
    form.addElement("predicate");
    form.addElement("arg1");
    form.addElement("arg2");
    JSPredicateForm clone = form.clonePF();
    assertEquals(form.size(), clone.size());
    for (int i = 0; i < form.size(); i++) {
      assertEquals(form.elementAt(i), clone.elementAt(i));
    }
  }

  @Test
  public void testApplySubstitutionPFEmptyPredicateForm() {
    JSPredicateForm form = new JSPredicateForm();
    JSSubstitution substitution = new JSSubstitution();
    JSPredicateForm result = form.applySubstitutionPF(substitution);
    assertEquals(form.size(), result.size());
  }

  // Add test cases for the remaining methods

  @Test
  public void testMatchesEmptyPredicateForms() {
    JSPredicateForm form1 = new JSPredicateForm();
    JSPredicateForm form2 = new JSPredicateForm();
    JSSubstitution substitution = form1.matches(form2);
    assertFalse(substitution.fail());
    assertEquals(0, substitution.size());
  }

  @Test
  public void testMatchesMatchingPredicateForms() {
    JSPredicateForm form1 = new JSPredicateForm();
    form1.addElement("predicate");
    form1.addElement("arg1");
    form1.addElement("arg2");
    JSPredicateForm form2 = new JSPredicateForm();
    form2.addElement("predicate");
    form2.addElement("arg1");
    form2.addElement("arg2");
    JSSubstitution substitution = form1.matches(form2);
    assertFalse(substitution.fail());
    assertEquals(0, substitution.size());
  }

  @Test
  public void testMatchesMismatchingPredicateForms() {
    JSPredicateForm form1 = new JSPredicateForm();
    form1.addElement("predicate");
    form1.addElement("arg1");
    form1.addElement("arg2");
    JSPredicateForm form2 = new JSPredicateForm();
    form2.addElement("predicate");
    form2.addElement("arg1");
    form2.addElement("arg3");
    JSSubstitution substitution = form1.matches(form2);
    assertTrue(substitution.fail());
  }

  @Test
  public void testEqualsMatchingPredicateForms() {
    JSPredicateForm form1 = new JSPredicateForm();
    form1.addElement("predicate");
    form1.addElement("arg1");
    form1.addElement("arg2");
    JSPredicateForm form2 = new JSPredicateForm();
    form2.addElement("predicate");
    form2.addElement("arg1");
    form2.addElement("arg2");
    assertTrue(form1.equals(form2));
  }

  @Test
  public void testEqualsMismatchingPredicateForms() {
    JSPredicateForm form1 = new JSPredicateForm();
    form1.addElement("predicate");
    form1.addElement("arg1");
    form1.addElement("arg2");
    JSPredicateForm form2 = new JSPredicateForm();
    form2.addElement("predicate");
    form2.addElement("arg1");
    form2.addElement("arg3");
    assertFalse(form1.equals(form2));
  }

  @Test
  public void testStandarizerPredicateFormEmptyPredicateForm() {
    JSPredicateForm form = new JSPredicateForm();
    JSPredicateForm standardForm = form.standarizerPredicateForm();
    assertEquals(form.size(), standardForm.size());
  }

  @Test
  public void testStandarizerPredicateFormPredicateFormWithElements() {
    JSPredicateForm form = new JSPredicateForm();
    form.addElement("predicate");
    form.addElement("arg1");
    form.addElement("arg2");
    JSPredicateForm standardForm = form.standarizerPredicateForm();
    assertEquals(form.size(), standardForm.size());
    for (int i = 0; i < form.size(); i++) {
      assertEquals(form.elementAt(i), standardForm.elementAt(i));
    }
  }

}
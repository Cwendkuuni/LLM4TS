import org.junit.Test;
import static org.junit.Assert.*;

public class JSPredicateFormTest {

  @Test
  public void testJSPredicateFormInit() {
    JSPredicateForm form = new JSPredicateForm("(x > 5)");
    assertEquals("(x > 5)", form.toStr().toString());
  }

  @Test(expected = JSParserError.class)
  public void testJSPredicateFormInit_NullTokenizer() {
    new JSPredicateForm(null);
  }

  @Test(expected = JSParserError.class)
  public void testJSPredicateFormInit_InvalidPredicate() {
    new JSPredicateForm("(x > 5");
  }

  @Test
  public void testPrint() {
    JSPredicateForm form = new JSPredicateForm("(x > 5)");
    form.print();
    // Assert the printed output
  }

  @Test
  public void testToStr() {
    JSPredicateForm form = new JSPredicateForm("(x > 5)");
    assertEquals("(x > 5)", form.toStr().toString());
  }

  @Test
  public void testClonePF() {
    JSPredicateForm form = new JSPredicateForm("(x > 5)");
    JSPredicateForm clone = form.clonePF();
    assertEquals(form.toStr().toString(), clone.toStr().toString());
  }

  @Test
  public void testApplySubstitutionPF() {
    JSPredicateForm form = new JSPredicateForm("(x > 5)");
    JSSubstitution substitution = new JSSubstitution();
    JSPredicateForm newForm = form.applySubstitutionPF(substitution);
    assertEquals(form.toStr().toString(), newForm.toStr().toString());
  }

  @Test
  public void testMatches() {
    JSPredicateForm form1 = new JSPredicateForm("(x > 5)");
    JSPredicateForm form2 = new JSPredicateForm("(y > 10)");
    JSSubstitution substitution = form1.matches(form2);
    assertEquals(false, substitution.fail());
  }

  @Test
  public void testEquals() {
    JSPredicateForm form1 = new JSPredicateForm("(x > 5)");
    JSPredicateForm form2 = new JSPredicateForm("(x > 5)");
    boolean isEqual = form1.equals(form2);
    assertEquals(true, isEqual);
  }

  @Test
  public void testStandarizerPredicateForm() {
    JSPredicateForm form = new JSPredicateForm("(x > 5)");
    JSPredicateForm standardizedForm = form.standarizerPredicateForm();
    assertEquals(form.toStr().toString(), standardizedForm.toStr().toString());
  }
}
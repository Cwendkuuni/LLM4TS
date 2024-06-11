import static org.junit.Assert.*;
import org.junit.Test;

public class JSPredicateFormTest {

    @Test
    public void testJSPredicateFormInit() {
        JSPredicateForm form = new JSPredicateForm("!not test");
        assertNotNull(form);
        assertEquals("Expected size to be 2", 2, form.size());
        assertEquals("Expected first element to be '!'", "!", form.elementAt(0));
        assertEquals("Expected second element to be 'not test'", "not test", form.elementAt(1));
    }
    
    @Test
    public void testPrint() {
        JSPredicateForm form = new JSPredicateForm("test");
        assertEquals("Expected print result to be (test )", "(test )", form.toStr().toString());
    }
    
    @Test
    public void testClonePF() {
        JSPredicateForm form = new JSPredicateForm();
        form.addElement("not");
        form.addElement(new JSPredicateForm("test"));
        
        JSPredicateForm clonedForm = form.clonePF();
        assertNotSame("Expected cloned form to be a different object", form, clonedForm);
        assertEquals("Expected cloned form to have the same elements", form.toString(), clonedForm.toString());
    }
    
    @Test
    public void testMatches() {
        JSPredicateForm form1 = new JSPredicateForm("not test1");
        JSPredicateForm form2 = new JSPredicateForm("not test2");
        JSSubstitution substitution = form1.matches(form2);
        assertFalse("Expected no match between form1 and form2", substitution.fail());
    }
    
    @Test
    public void testEquals() {
        JSPredicateForm form1 = new JSPredicateForm("test1");
        JSPredicateForm form2 = new JSPredicateForm("test1");
        assertTrue("Expected form1 to be equal to form2", form1.equals(form2));
    }
    
    @Test
    public void testStandarizerPredicateForm() {
        JSPredicateForm form = new JSPredicateForm("test");
        JSPredicateForm standardizedForm = form.standarizerPredicateForm();
        assertNotNull("Expected standardized form to not be null", standardizedForm);
    }
}
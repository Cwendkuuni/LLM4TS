import org.junit.Test;
import static org.junit.Assert.*;

public class JSPredicateFormTest {
    @Test
    public void testConstructor1() {
        JSPredicateForm form = new JSPredicateForm();
        assertNotNull(form);
        assertEquals(0, form.size());
    }
    
    @Test
    public void testConstructor2() {
        String text = "(test predicate)";
        JSPredicateForm form = new JSPredicateForm(text);
        assertNotNull(form);
        assertEquals(1, form.size());
        assertEquals("test predicate", form.elementAt(0));
    }
    
    @Test
    public void testConstructor3() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(test predicate)"));
        JSPredicateForm form = new JSPredicateForm(tokenizer);
        assertNotNull(form);
        assertEquals(1, form.size());
        assertEquals("test predicate", form.elementAt(0));
    }
    
    @Test
    public void testJSPredicateFormInit() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(test predicate)"));
        JSPredicateForm form = new JSPredicateForm();
        form.JSPredicateFormInit(tokenizer);
        assertNotNull(form);
        assertEquals(1, form.size());
        assertEquals("test predicate", form.elementAt(0));
    }
    
    @Test
    public void testPrint() {
        JSPredicateForm form = new JSPredicateForm("(test predicate)");
        form.print();
        // Verify the output in the console manually
    }
    
    @Test
    public void testToStr() {
        JSPredicateForm form = new JSPredicateForm("(test predicate)");
        StringBuffer res = form.toStr();
        assertNotNull(res);
        assertEquals("(test predicate)", res.toString());
    }
    
    @Test
    public void testClonePF() {
        JSPredicateForm form = new JSPredicateForm("(test predicate)");
        JSPredicateForm clone = form.clonePF();
        assertNotNull(clone);
        assertEquals(form.size(), clone.size());
        assertEquals(form.elementAt(0), clone.elementAt(0));
        for (int i = 1; i < form.size(); i++) {
            assertEquals(form.elementAt(i), clone.elementAt(i));
        }
    }
    
    @Test
    public void testApplySubstitutionPF() {
        JSPredicateForm form = new JSPredicateForm("(test predicate)");
        JSSubstitution alpha = new JSSubstitution();
        JSPredicateForm result = form.applySubstitutionPF(alpha);
        assertNotNull(result);
        assertEquals(form.size(), result.size());
        assertEquals(form.elementAt(0), result.elementAt(0));
        for (int i = 1; i < form.size(); i++) {
            assertEquals(form.elementAt(i), result.elementAt(i));
        }
    }
    
    @Test
    public void testMatches1() {
        JSPredicateForm form1 = new JSPredicateForm("(test predicate)");
        JSPredicateForm form2 = new JSPredicateForm("(test predicate)");
        JSSubstitution result = form1.matches(form2);
        assertNotNull(result);
        assertFalse(result.fail());
    }
    
    @Test
    public void testMatches2() {
        JSPredicateForm form1 = new JSPredicateForm("(not (test predicate))");
        JSPredicateForm form2 = new JSPredicateForm("(not (test predicate))");
        JSSubstitution result = form1.matches(form2);
        assertNotNull(result);
        assertFalse(result.fail());
    }
    
    @Test
    public void testEquals() {
        JSPredicateForm form1 = new JSPredicateForm("(test predicate)");
        JSPredicateForm form2 = new JSPredicateForm("(test predicate)");
        assertTrue(form1.equals(form2));
    }
    
    @Test
    public void testStandarizerPredicateForm() {
        JSPredicateForm form = new JSPredicateForm("(test predicate)");
        JSPredicateForm result = form.standarizerPredicateForm();
        assertNotNull(result);
        assertEquals(form.size(), result.size());
        assertEquals(form.elementAt(0), result.elementAt(0));
        for (int i = 1; i < form.size(); i++) {
            assertEquals(form.elementAt(i), result.elementAt(i));
        }
    }
}
import org.junit.Test;
import static org.junit.Assert.*;

public class JSPredicateFormTest {

    @Test
    public void testJSPredicateForm() {
        // Test constructor without parameters
        JSPredicateForm form1 = new JSPredicateForm();
        assertNotNull(form1);
        
        // Test constructor with String parameter
        String text = "(pred1 arg1 arg2)";
        JSPredicateForm form2 = new JSPredicateForm(text);
        assertNotNull(form2);
        
        // Test constructor with StreamTokenizer parameter
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(text));
        JSPredicateForm form3 = new JSPredicateForm(tokenizer);
        assertNotNull(form3);
    }
    
    @Test
    public void testJSPredicateFormInit() {
        // Test JSPredicateFormInit method
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(pred1 arg1 arg2)"));
        JSPredicateForm form = new JSPredicateForm();
        form.JSPredicateFormInit(tokenizer);
        
        assertEquals(3, form.size());
        assertEquals("pred1", form.elementAt(0));
        assertEquals("arg1", form.elementAt(1));
        assertEquals("arg2", form.elementAt(2));
    }
    
    @Test
    public void testPrint() {
        // Test print method
        JSPredicateForm form = new JSPredicateForm("(pred1 arg1 arg2)");
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        form.print();
        assertEquals("(pred1 arg1 arg2 ) ", outContent.toString());
    }
    
    @Test
    public void testToStr() {
        // Test toStr method
        JSPredicateForm form = new JSPredicateForm("(pred1 arg1 arg2)");
        
        assertEquals("(pred1 arg1 arg2)", form.toStr().toString());
    }
    
    @Test
    public void testClonePF() {
        // Test clonePF method
        JSPredicateForm form1 = new JSPredicateForm("(pred1 arg1 arg2)");
        JSPredicateForm form2 = form1.clonePF();
        
        assertEquals(form1, form2);
    }
    
    @Test
    public void testApplySubstitutionPF() {
        // Test applySubstitutionPF method
        JSPredicateForm form1 = new JSPredicateForm("(pred1 arg1 arg2)");
        JSSubstitution alpha = new JSSubstitution();
        JSPredicateForm form2 = form1.applySubstitutionPF(alpha);
        
        assertEquals(form1, form2);
    }
    
    @Test
    public void testMatches() {
        // Test matches method
        JSPredicateForm form1 = new JSPredicateForm("(pred1 arg1 arg2)");
        JSPredicateForm form2 = new JSPredicateForm("(pred1 arg1 arg2)");
        JSSubstitution alpha = new JSSubstitution();
        JSSubstitution beta = form1.matches(form2, alpha);
        
        assertTrue(beta.isEmpty());
    }
    
    @Test
    public void testEquals() {
        // Test equals method
        JSPredicateForm form1 = new JSPredicateForm("(pred1 arg1 arg2)");
        JSPredicateForm form2 = new JSPredicateForm("(pred1 arg1 arg2)");
        
        assertTrue(form1.equals(form2));
    }
    
    @Test
    public void testStandarizerPredicateForm() {
        // Test standarizerPredicateForm method
        JSPredicateForm form1 = new JSPredicateForm("(pred1 arg1 arg2)");
        JSPredicateForm form2 = form1.standarizerPredicateForm();
        
        assertEquals(form1, form2);
    }
}
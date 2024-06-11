import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class JSPredicateFormTest {
    @Test
    public void testJSPredicateFormInit() {
        String text = "(foo)";
        JSPredicateForm form = new JSPredicateForm(text);
        assertEquals("foo", form.elementAt(0));
        
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(text));
        JSPredicateForm form2 = new JSPredicateForm(tokenizer);
        assertEquals("foo", form2.elementAt(0));
        
        String nullText = null;
        JSPredicateForm form3 = new JSPredicateForm(nullText);
        assertEquals(0, form3.size());
        
        StreamTokenizer nullTokenizer = null;
        try {
            JSPredicateForm form4 = new JSPredicateForm(nullTokenizer);
            // If StreamTokenizer is null, it should throw an exception, so this line should not be executed
            assertEquals(0, form4.size());
        } catch (JSParserError e) {
            // Expected exception, test passed.
        }
    }
    
    @Test
    public void testPrint() {
        JSPredicateForm form = new JSPredicateForm();
        form.addElement("foo");
        form.addElement("bar");
        form.addElement("baz");
        
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        PrintStream oldOut = System.out;
        try {
            System.setOut(new PrintStream(outStream));
            form.print();
            assertEquals("(foo bar baz ) ", outStream.toString());
        } finally {
            System.setOut(oldOut);
        }
    }
    
    @Test
    public void testToStr() {
        JSPredicateForm form = new JSPredicateForm();
        form.addElement("foo");
        form.addElement("bar");
        form.addElement("baz");
        
        StringBuffer buffer = form.toStr();
        assertEquals("(foo bar baz)", buffer.toString());
    }
    
    @Test
    public void testClonePF() {
        JSPredicateForm form = new JSPredicateForm();
        form.addElement("foo");
        form.addElement("bar");
        form.addElement("baz");
        
        JSPredicateForm clone = form.clonePF();
        assertEquals(form.elementAt(0), clone.elementAt(0));
        assertEquals(form.elementAt(1), clone.elementAt(1));
        assertEquals(form.elementAt(2), clone.elementAt(2));
    }
    
    @Test
    public void testApplySubstitutionPF() {
        JSPredicateForm form = new JSPredicateForm();
        form.addElement("foo");
        form.addElement("bar");
        form.addElement("baz");
        
        JSSubstitution alpha = new JSSubstitution();
        JSSubstitution subform = form.applySubstitutionPF(alpha);
        assertEquals(form.elementAt(0), subform.elementAt(0));
        assertEquals(form.elementAt(1), subform.elementAt(1));
        assertEquals(form.elementAt(2), subform.elementAt(2));
    }
    
    @Test
    public void testMatches() {
        JSPredicateForm form = new JSPredicateForm();
        form.addElement("foo");
        form.addElement("bar");
        form.addElement("baz");
        
        JSPredicateForm t = new JSPredicateForm();
        t.addElement("foo");
        t.addElement("bar");
        t.addElement("baz");
        
        JSSubstitution alpha = form.matches(t);
        assertNull(alpha.getFailure());
    }
    
    @Test
    public void testEquals() {
        JSPredicateForm form = new JSPredicateForm();
        form.addElement("foo");
        form.addElement("bar");
        form.addElement("baz");
        
        JSPredicateForm t = new JSPredicateForm();
        t.addElement("foo");
        t.addElement("bar");
        t.addElement("baz");
        
        assertEquals(true, form.equals(t));
    }
    
    @Test
    public void testStandarizerPredicateForm() {
        JSPredicateForm form = new JSPredicateForm();
        form.addElement("foo");
        form.addElement("bar");
        form.addElement("baz");
        
        JSPredicateForm standarizedForm = form.standarizerPredicateForm();
        assertEquals(form.elementAt(0), standarizedForm.elementAt(0));
        assertEquals(form.elementAt(1), standarizedForm.elementAt(1));
        assertEquals(form.elementAt(2), standarizedForm.elementAt(2));
    }
}
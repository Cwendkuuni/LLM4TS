import static org.junit.Assert.*;
import org.junit.*;

public class JSPredicateFormTest {

    @Test
    public void testJSPredicateFormConstructor1() {
        String text = null;
        JSPredicateForm form = new JSPredicateForm(text);
        assertEquals(0, form.size());
    }
    
    @Test
    public void testJSPredicateFormConstructor2() {
        String text = "(not (p))";
        JSPredicateForm form = new JSPredicateForm(text);
        assertEquals(2, form.size());
        assertEquals("not", form.elementAt(0));
        assertEquals("p", ((JSPredicateForm) form.elementAt(1)).elementAt(0));
    }
    
    @Test
    public void testJSPredicateFormConstructor3() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(call (f 1 2))"));
        JSPredicateForm form = new JSPredicateForm(tokenizer);
        assertEquals(3, form.size());
        assertEquals("call", form.elementAt(0));
        assertEquals("f", ((JSTerm) form.elementAt(1)).elementAt(0));
        assertEquals(1, ((JSTerm) form.elementAt(1)).elementAt(1));
        assertEquals(2, ((JSTerm) form.elementAt(1)).elementAt(2));
    }
    
    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitWithNullTokenizer() {
        StreamTokenizer tokenizer = null;
        JSPredicateForm form = new JSPredicateForm(tokenizer);
    }
    
    @Test
    public void testJSPredicateFormPrint() {
        JSPredicateForm form = new JSPredicateForm("(not (p))");
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        form.print();
        String expectedOutput = "((print p ) ) ";
        assertEquals(expectedOutput, outContent.toString());
    }
    
    @Test
    public void testJSPredicateFormToStr() {
        JSPredicateForm form = new JSPredicateForm("(not (p))");
        StringBuffer str = form.toStr();
        String expectedStr = "((p))";
        assertEquals(expectedStr, str.toString());
    }
    
    @Test
    public void testJSPredicateFormClonePF() {
        JSPredicateForm form = new JSPredicateForm("(not (p))");
        JSPredicateForm cloneForm = form.clonePF();
        assertTrue(form.equals(cloneForm));
        assertNotSame(form, cloneForm);
        assertNotSame(form.elementAt(0), cloneForm.elementAt(0));
        assertEquals(form.elementAt(1), cloneForm.elementAt(1));
    }
    
    @Test
    public void testJSPredicateFormApplySubstitutionPF() {
        JSPredicateForm form = new JSPredicateForm("(not (p x y))");
        JSSubstitution alpha = new JSSubstitution();
        alpha.addSubstitution("x", "1");
        alpha.addSubstitution("y", "2");
        JSPredicateForm appliedForm = form.applySubstitutionPF(alpha);
        assertEquals("not", appliedForm.elementAt(0));
        assertEquals("p", ((JSTerm) appliedForm.elementAt(1)).elementAt(0));
        assertEquals("1", ((JSTerm) appliedForm.elementAt(1)).elementAt(1));
        assertEquals("2", ((JSTerm) appliedForm.elementAt(1)).elementAt(2));
    }
    
    @Test
    public void testJSPredicateFormMatches1() {
        JSPredicateForm form1 = new JSPredicateForm("(p)");
        JSPredicateForm form2 = new JSPredicateForm("(p)");
        JSSubstitution alpha = form1.matches(form2);
        assertTrue(alpha.isSuccess());
    }
    
    @Test
    public void testJSPredicateFormMatches2() {
        JSPredicateForm form1 = new JSPredicateForm("(not (p))");
        JSPredicateForm form2 = new JSPredicateForm("(not (p))");
        JSSubstitution alpha = form1.matches(form2);
        assertTrue(alpha.isSuccess());
    }
    
    @Test
    public void testJSPredicateFormMatches3() {
        JSPredicateForm form1 = new JSPredicateForm("(p)");
        JSPredicateForm form2 = new JSPredicateForm("(q)");
        JSSubstitution alpha = form1.matches(form2);
        assertFalse(alpha.isSuccess());
    }
    
    @Test
    public void testJSPredicateFormMatches4() {
        JSPredicateForm form1 = new JSPredicateForm("(p x y)");
        JSPredicateForm form2 = new JSPredicateForm("(p y x)");
        JSSubstitution alpha = form1.matches(form2);
        assertFalse(alpha.isSuccess());
    }
    
    @Test
    public void testJSPredicateFormEquals1() {
        JSPredicateForm form1 = new JSPredicateForm("(p)");
        JSPredicateForm form2 = new JSPredicateForm("(p)");
        assertTrue(form1.equals(form2));
    }
    
    @Test
    public void testJSPredicateFormEquals2() {
        JSPredicateForm form1 = new JSPredicateForm("(not (p))");
        JSPredicateForm form2 = new JSPredicateForm("(not (p))");
        assertTrue(form1.equals(form2));
    }
    
    @Test
    public void testJSPredicateFormEquals3() {
        JSPredicateForm form1 = new JSPredicateForm("(p)");
        JSPredicateForm form2 = new JSPredicateForm("(q)");
        assertFalse(form1.equals(form2));
    }
    
    @Test
    public void testJSPredicateFormEquals4() {
        JSPredicateForm form1 = new JSPredicateForm("(p x y)");
        JSPredicateForm form2 = new JSPredicateForm("(p y x)");
        assertFalse(form1.equals(form2));
    }
    
    @Test
    public void testJSPredicateFormStandarizerPredicateForm() {
        JSPredicateForm form = new JSPredicateForm("(not (p x y))");
        JSPredicateForm stdForm = form.standarizerPredicateForm();
        assertEquals("not", stdForm.elementAt(0));
        assertEquals("p", ((JSTerm) stdForm.elementAt(1)).elementAt(0));
        assertTrue(((JSTerm) stdForm.elementAt(1)).elementAt(1) instanceof String);
        assertTrue(((JSTerm) stdForm.elementAt(1)).elementAt(2) instanceof String);
    }
}
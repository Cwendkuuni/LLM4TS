import org.junit.*;
import static org.junit.Assert.*;

public class JSPredicateFormTest {

    // Test case for constructor JSPredicateForm()
    @Test
    public void testJSPredicateFormConstructor1() {
        JSPredicateForm pf = new JSPredicateForm();
        assertEquals(0, pf.size());
    }
    
    // Test case for constructor JSPredicateForm(String text)
    @Test
    public void testJSPredicateFormConstructor2() {
        String text = "(not (equals x y))";
        JSPredicateForm pf = new JSPredicateForm(text);
        assertEquals(2, pf.size());
        assertEquals("not", pf.elementAt(0));
        JSPredicateForm subform = pf.elementAt(1);
        assertEquals("equals", subform.elementAt(0));
        assertEquals("x", subform.elementAt(1));
        assertEquals("y", subform.elementAt(2));
    }
    
    // Test case for constructor JSPredicateForm(StreamTokenizer tokenizer)
    @Test
    public void testJSPredicateFormConstructor3() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(not (equals x y))"));
        JSPredicateForm pf = new JSPredicateForm(tokenizer);
        assertEquals(2, pf.size());
        assertEquals("not", pf.elementAt(0));
        JSPredicateForm subform = pf.elementAt(1);
        assertEquals("equals", subform.elementAt(0));
        assertEquals("x", subform.elementAt(1));
        assertEquals("y", subform.elementAt(2));
    }
    
    // Test case for method JSPredicateFormInit(StreamTokenizer tokenizer)
    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitException1() {
        StreamTokenizer tokenizer = null;
        JSPredicateForm pf = new JSPredicateForm();
        pf.JSPredicateFormInit(tokenizer);
    }
    
    // Test case for method JSPredicateFormInit(StreamTokenizer tokenizer)
    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitException2() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(not (equals x y)"));
        JSPredicateForm pf = new JSPredicateForm();
        pf.JSPredicateFormInit(tokenizer);
    }
    
    // Test case for method JSPredicateFormInit(StreamTokenizer tokenizer)
    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitException3() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(not )"));
        JSPredicateForm pf = new JSPredicateForm();
        pf.JSPredicateFormInit(tokenizer);
    }
    
    // Test case for method print()
    @Test
    public void testPrint() {
        JSPredicateForm pf = new JSPredicateForm("(not (equals x y))");
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        pf.print();
        assertEquals("(not (equals x y) ) ", outContent.toString());
    }
    
    // Test case for method toStr()
    @Test
    public void testToStr() {
        JSPredicateForm pf = new JSPredicateForm("(not (equals x y))");
        StringBuffer expected = new StringBuffer("(not (equals x y))");
        StringBuffer result = pf.toStr();
        assertEquals(expected.toString(), result.toString());
    }
    
    // Test case for method clonePF()
    @Test
    public void testClonePF() {
        JSPredicateForm pf = new JSPredicateForm("(not (equals x y))");
        JSPredicateForm clone = pf.clonePF();
        assertTrue(pf.equals(clone));
        assertNotSame(pf, clone);
    }
    
    // Test case for method applySubstitutionPF(JSSubstitution alpha)
    @Test
    public void testApplySubstitutionPF() {
        JSSubstitution alpha = new JSSubstitution();
        alpha.addSubstitution("x", "a");
        alpha.addSubstitution("y", "b");
        JSPredicateForm pf = new JSPredicateForm("(not (equals x y))");
        JSPredicateForm result = pf.applySubstitutionPF(alpha);
        JSPredicateForm expected = new JSPredicateForm("(not (equals a b))");
        assertTrue(result.equals(expected));
    }
    
    // Test case for method matches(JSPredicateForm t)
    @Test
    public void testMatches1() {
        JSPredicateForm pf1 = new JSPredicateForm("(not (equals x y))");
        JSPredicateForm pf2 = new JSPredicateForm("(not (equals a b))");
        JSSubstitution result = pf1.matches(pf2);
        assertFalse(result.hasFailed());
        assertTrue(result.isEmpty());
    }
    
    // Test case for method matches(JSPredicateForm t)
    @Test
    public void testMatches2() {
        JSPredicateForm pf1 = new JSPredicateForm("(not (equals x y))");
        JSPredicateForm pf2 = new JSPredicateForm("(not (equals x b))");
        JSSubstitution result = pf1.matches(pf2);
        assertTrue(result.hasFailed());
        assertTrue(result.isEmpty());
    }
    
    // Test case for method equals(JSPredicateForm t)
    @Test
    public void testEquals() {
        JSPredicateForm pf1 = new JSPredicateForm("(not (equals x y))");
        JSPredicateForm pf2 = new JSPredicateForm("(not (equals x y))");
        assertTrue(pf1.equals(pf2));
    }
    
    // Test case for method standarizerPredicateForm()
    @Test
    public void testStandarizerPredicateForm() {
        JSPredicateForm pf = new JSPredicateForm("(not (equals x y))");
        JSPredicateForm standarized = pf.standarizerPredicateForm();
        JSPredicateForm expected = new JSPredicateForm("(not (equals ?x ?y))");
        assertTrue(expected.equals(standarized));
    }
    
}
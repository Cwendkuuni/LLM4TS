package umd.cs.shop;

import org.junit.Test;
import static org.junit.Assert.*;

public class JSPredicateFormTest {

    @Test
    public void testJSPredicateForm() {
        JSPredicateForm jspf1 = new JSPredicateForm();
        assertNotNull(jspf1);
        
        String text = "((reader moveobj booksto sell1))";
        JSPredicateForm jspf2 = new JSPredicateForm(text);
        assertNotNull(jspf2);
        
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(text));
        JSPredicateForm jspf3 = new JSPredicateForm(tokenizer);
        assertNotNull(jspf3);
    }

    @Test(expected = JSParserError.class)
    public void testJSPredicateForm_Exception() {
        String text = "((reader moveobj booksto sell1))";
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(text));
        tokenizer.nextToken();
        tokenizer.nextToken();
        tokenizer.pushBack();
        JSPredicateForm jspf = new JSPredicateForm(tokenizer);
    }

    @Test
    public void testJSPredicateFormInit() {
        JSPredicateForm jspf = new JSPredicateForm();
        assertNotNull(jspf);
        
        String text = "((reader moveobj booksto sell1))";
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(text));
        jspf.JSPredicateFormInit(tokenizer);
    }

    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInit_Exception1() {
        JSPredicateForm jspf = new JSPredicateForm();
        assertNotNull(jspf);
        
        StreamTokenizer tokenizer = null;
        jspf.JSPredicateFormInit(tokenizer);
    }

    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInit_Exception2() {
        JSPredicateForm jspf = new JSPredicateForm();
        assertNotNull(jspf);
        
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("((reader moveobj booksto sell1))"));
        tokenizer.nextToken();
        tokenizer.nextToken();
        jspf.JSPredicateFormInit(tokenizer);
    }

    @Test
    public void testPrint() {
        JSPredicateForm jspf = new JSPredicateForm();
        assertNotNull(jspf);
        
        jspf.addElement("not");
        jspf.addElement(new JSPredicateForm("((reader moveobj booksto sell1))"));
        
        jspf.print();
    }

    @Test
    public void testToStr() {
        JSPredicateForm jspf = new JSPredicateForm();
        assertNotNull(jspf);
        
        jspf.addElement("not");
        jspf.addElement(new JSPredicateForm("((reader moveobj booksto sell1))"));
        
        StringBuffer res = jspf.toStr();
        assertNotNull(res);
    }

    @Test
    public void testClonePF() {
        JSPredicateForm jspf = new JSPredicateForm();
        assertNotNull(jspf);
        
        jspf.addElement("not");
        jspf.addElement(new JSPredicateForm("((reader moveobj booksto sell1))"));
        
        JSPredicateForm clone = jspf.clonePF();
        assertNotNull(clone);
    }

    @Test
    public void testApplySubstitutionPF() {
        JSPredicateForm jspf = new JSPredicateForm();
        assertNotNull(jspf);
        
        jspf.addElement("not");
        jspf.addElement(new JSPredicateForm("((reader moveobj booksto sell1))"));
        
        JSSubstitution alpha = new JSSubstitution();
        JSPredicateForm applied = jspf.applySubstitutionPF(alpha);
        assertNotNull(applied);
    }

    @Test
    public void testMatches() {
        JSPredicateForm jspf1 = new JSPredicateForm();
        assertNotNull(jspf1);
        
        jspf1.addElement("not");
        jspf1.addElement(new JSPredicateForm("((reader moveobj booksto sell1))"));
        
        JSPredicateForm jspf2 = new JSPredicateForm();
        assertNotNull(jspf2);
        
        jspf2.addElement("not");
        jspf2.addElement(new JSPredicateForm("((reader moveobj booksto sell1))"));
        
        JSSubstitution result = jspf1.matches(jspf2);
        assertNotNull(result);
    }

    @Test
    public void testMatches_JSSubstitution() {
        JSPredicateForm jspf1 = new JSPredicateForm();
        assertNotNull(jspf1);
        
        jspf1.addElement("not");
        jspf1.addElement(new JSPredicateForm("((reader moveobj booksto sell1))"));
        
        JSPredicateForm jspf2 = new JSPredicateForm();
        assertNotNull(jspf2);
        
        jspf2.addElement("not");
        jspf2.addElement(new JSPredicateForm("((reader moveobj booksto sell1))"));
        
        JSSubstitution alpha = new JSSubstitution();
        JSSubstitution result = jspf1.matches(jspf2, alpha);
        assertNotNull(result);
    }

    @Test
    public void testEquals() {
        JSPredicateForm jspf1 = new JSPredicateForm();
        assertNotNull(jspf1);
        
        jspf1.addElement("not");
        jspf1.addElement(new JSPredicateForm("((reader moveobj booksto sell1))"));
        
        JSPredicateForm jspf2 = new JSPredicateForm();
        assertNotNull(jspf2);
        
        jspf2.addElement("not");
        jspf2.addElement(new JSPredicateForm("((reader moveobj booksto sell1))"));
        
        boolean result = jspf1.equals(jspf2);
        assertTrue(result);
    }

    @Test
    public void testStandarizerPredicateForm() {
        JSPredicateForm jspf = new JSPredicateForm();
        assertNotNull(jspf);
        
        jspf.addElement("not");
        jspf.addElement(new JSPredicateForm("((reader moveobj booksto sell1))"));
        
        JSPredicateForm standard = jspf.standarizerPredicateForm();
        assertNotNull(standard);
    }
}
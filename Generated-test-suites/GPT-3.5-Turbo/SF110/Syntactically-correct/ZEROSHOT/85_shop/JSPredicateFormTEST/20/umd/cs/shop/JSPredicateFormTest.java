package umd.cs.shop;

import static org.junit.Assert.*;
import org.junit.Test;

public class JSPredicateFormTest {

    @Test
    public void testJSPredicateForm() {
        JSPredicateForm predicateForm = new JSPredicateForm();
        assertNotNull(predicateForm);
    }

    @Test
    public void testJSPredicateFormString() {
        JSPredicateForm predicateForm = new JSPredicateForm("test");
        assertNotNull(predicateForm);
    }

    @Test
    public void testJSPredicateFormStreamTokenizer() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("test"));
        JSPredicateForm predicateForm = new JSPredicateForm(tokenizer);
        assertNotNull(predicateForm);
    }

    @Test
    public void testJSPredicateFormInit() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("test"));
        JSPredicateForm predicateForm = new JSPredicateForm();
        predicateForm.JSPredicateFormInit(tokenizer);
        // Add assertion here
    }

    @Test
    public void testPrint() {
        JSPredicateForm predicateForm = new JSPredicateForm("test");
        predicateForm.print();
        // Add assertion here
    }

    @Test
    public void testToStr() {
        JSPredicateForm predicateForm = new JSPredicateForm("test");
        StringBuffer str = predicateForm.toStr();
        assertNotNull(str);
    }

    @Test
    public void testClonePF() {
        JSPredicateForm predicateForm = new JSPredicateForm("test");
        JSPredicateForm clonePF = predicateForm.clonePF();
        assertNotNull(clonePF);
    }

    @Test
    public void testApplySubstitutionPF() {
        JSSubstitution alpha = new JSSubstitution();
        JSPredicateForm predicateForm = new JSPredicateForm("test");
        JSPredicateForm applySubstitutionPF = predicateForm.applySubstitutionPF(alpha);
        assertNotNull(applySubstitutionPF);
    }

    @Test
    public void testMatches() {
        JSPredicateForm predicateForm = new JSPredicateForm("test");
        JSSubstitution alpha = new JSSubstitution();
        JSPredicateForm t = new JSPredicateForm("test");
        JSSubstitution matches = predicateForm.matches(t, alpha);
        assertNotNull(matches);
    }

    @Test
    public void testEquals() {
        JSPredicateForm predicateForm = new JSPredicateForm("test");
        JSPredicateForm t = new JSPredicateForm("test");
        boolean equals = predicateForm.equals(t);
        assertEquals(true, equals);
    }

    @Test
    public void testStandarizerPredicateForm() {
        JSPredicateForm predicateForm = new JSPredicateForm("test");
        JSPredicateForm standarizerPredicateForm = predicateForm.standarizerPredicateForm();
        assertNotNull(standarizerPredicateForm);
    }
}
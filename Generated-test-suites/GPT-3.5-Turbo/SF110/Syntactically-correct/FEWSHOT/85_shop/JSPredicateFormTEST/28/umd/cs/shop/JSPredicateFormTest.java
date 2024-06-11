package umd.cs.shop;

import org.junit.Test;
import java.io.StreamTokenizer;
import java.io.StringReader;
import static org.junit.Assert.*;

public class JSPredicateFormTest {

    // Test Constructor 1
    @Test
    public void testJSPredicateForm_1() {
        JSPredicateForm form = new JSPredicateForm();
        assertNotNull(form);
        assertEquals(0, form.size());
    }

    // Test Constructor 2
    @Test
    public void testJSPredicateForm_2() {
        String text = "(predicate)";
        JSPredicateForm form = new JSPredicateForm(text);
        assertNotNull(form);
        assertEquals(1, form.size());
        assertEquals("predicate", form.elementAt(0));
    }

    // Test Constructor 3
    @Test
    public void testJSPredicateForm_3() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(predicate)"));
        JSPredicateForm form = new JSPredicateForm(tokenizer);
        assertNotNull(form);
        assertEquals(1, form.size());
        assertEquals("predicate", form.elementAt(0));
    }

    // Test JSPredicateFormInit
    @Test
    public void testJSPredicateFormInit() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(predicate)"));
        JSPredicateForm form = new JSPredicateForm();
        form.JSPredicateFormInit(tokenizer);
        assertNotNull(form);
        assertEquals(1, form.size());
        assertEquals("predicate", form.elementAt(0));
    }

    //Test print
    @Test
    public void testPrint(){
        JSPredicateForm form = new JSPredicateForm();
        form.addElement("predicate");
        form.print();
    }

    // Test toStr
    @Test
    public void testToStr() {
        JSPredicateForm form = new JSPredicateForm();
        form.addElement("predicate");
        form.addElement("term1");
        form.addElement("term2");
        StringBuffer res = form.toStr();
        assertEquals("(predicate term1 term2)", res.toString());
    }

    // Test clonePF
    @Test
    public void testClonePF() {
        JSPredicateForm form = new JSPredicateForm();
        form.addElement("not");
        JSPredicateForm innerForm = new JSPredicateForm();
        innerForm.addElement("predicate");
        innerForm.addElement("term1");
        form.addElement(innerForm);

        JSPredicateForm clonedForm = form.clonePF();
        assertNotNull(clonedForm);
        assertEquals(form.elementAt(0), clonedForm.elementAt(0));
        assertEquals(2, clonedForm.size());
        JSPredicateForm clonedInnerForm = (JSPredicateForm)clonedForm.elementAt(1);
        assertEquals(innerForm.elementAt(0), clonedInnerForm.elementAt(0));
        assertEquals(innerForm.elementAt(1), clonedInnerForm.elementAt(1));
    }

    // Test applySubstitutionPF
    @Test
    public void testApplySubstitutionPF() {
        JSPredicateForm form = new JSPredicateForm();
        form.addElement("predicate");
        form.addElement("term1");
        form.addElement("term2");
        JSSubstitution alpha = new JSSubstitution();
        JSTerm term = new JSTerm();
        term.addElement("term1");
        term.addElement("replaced");
        alpha.addElement(term);

        JSPredicateForm newForm = form.applySubstitutionPF(alpha);
        assertNotNull(newForm);
        assertEquals(form.elementAt(0), newForm.elementAt(0));
        assertEquals(3, newForm.size());
        assertEquals(form.elementAt(1), newForm.elementAt(1));
        assertEquals("replaced", newForm.elementAt(2));
    }

    // Test matches
    @Test
    public void testMatches() {
        JSPredicateForm form = new JSPredicateForm();
        form.addElement("predicate");
        form.addElement("term1");
        form.addElement("term2");
        JSPredicateForm t = new JSPredicateForm();
        t.addElement("predicate");
        t.addElement("term1");
        t.addElement("term2");

        JSSubstitution alpha = form.matches(t);
        assertNotNull(alpha);
        assertFalse(alpha.fail());
    }

    // Test equals
    @Test
    public void testEquals() {
        JSPredicateForm form = new JSPredicateForm();
        form.addElement("predicate");
        form.addElement("term1");
        form.addElement("term2");
        JSPredicateForm t = new JSPredicateForm();
        t.addElement("predicate");
        t.addElement("term1");
        t.addElement("term2");

        boolean result = form.equals(t);
        assertTrue(result);
    }

    // Test standarizerPredicateForm
    @Test
    public void testStandarizerPredicateForm() {
        JSPredicateForm form = new JSPredicateForm();
        form.addElement("predicate");
        form.addElement("term1");
        form.addElement("term2");

        JSPredicateForm newForm = form.standarizerPredicateForm();
        assertNotNull(newForm);
        assertEquals(form.elementAt(0), newForm.elementAt(0));
        assertEquals(3, newForm.size());
        assertEquals(form.elementAt(1), newForm.elementAt(1));
        assertEquals(form.elementAt(2), newForm.elementAt(2));
    }
}
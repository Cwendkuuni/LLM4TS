import org.junit.*;
import static org.junit.Assert.*;

public class JSPredicateFormTest {

    @Test
    public void testJSPredicateForm_1() {
        JSPredicateForm jsPredicateForm = new JSPredicateForm();
        assertNotNull(jsPredicateForm);
    }

    @Test
    public void testJSPredicateForm_2() {
        String text = "test";
        JSPredicateForm jsPredicateForm = new JSPredicateForm(text);
        assertNotNull(jsPredicateForm);
    }

    @Test
    public void testJSPredicateForm_3() {
        StreamTokenizer tokenizer = new StreamTokenizer();
        JSPredicateForm jsPredicateForm = new JSPredicateForm(tokenizer);
        assertNotNull(jsPredicateForm);
    }

    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInit_1() {
        StreamTokenizer tokenizer = null;
        JSPredicateForm jsPredicateForm = new JSPredicateForm();
        jsPredicateForm.JSPredicateFormInit(tokenizer);
    }

    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInit_2() {
        StreamTokenizer tokenizer = new StreamTokenizer();
        tokenizer.pushBack();
        tokenizer.pushBack();
        tokenizer.pushBack();
        JSPredicateForm jsPredicateForm = new JSPredicateForm();
        jsPredicateForm.JSPredicateFormInit(tokenizer);
    }

    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInit_3() {
        StreamTokenizer tokenizer = new StreamTokenizer();
        tokenizer.ttype = JSJshopVars.rightPar;
        JSPredicateForm jsPredicateForm = new JSPredicateForm();
        jsPredicateForm.JSPredicateFormInit(tokenizer);
    }

    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInit_4() {
        StreamTokenizer tokenizer = new StreamTokenizer();
        tokenizer.ttype = JSJshopVars.rightPar;
        tokenizer.pushBack();
        tokenizer.pushBack();
        JSPredicateForm jsPredicateForm = new JSPredicateForm();
        jsPredicateForm.JSPredicateFormInit(tokenizer);
    }

    @Test
    public void testPrint_1() {
        JSPredicateForm jsPredicateForm = new JSPredicateForm();
        jsPredicateForm.print();
    }

    @Test
    public void testPrint_2() {
        JSPredicateForm jsPredicateForm = new JSPredicateForm();
        jsPredicateForm.addElement("not");
        JSPredicateForm el = new JSPredicateForm();
        jsPredicateForm.addElement(el);
        jsPredicateForm.print();
    }

    @Test
    public void testToStr_1() {
        JSPredicateForm jsPredicateForm = new JSPredicateForm();
        StringBuffer res = jsPredicateForm.toStr();
        assertNotNull(res);
        assertEquals("", res.toString());
    }

    @Test
    public void testToStr_2() {
        JSPredicateForm jsPredicateForm = new JSPredicateForm();
        jsPredicateForm.addElement("not");
        JSPredicateForm el = new JSPredicateForm();
        jsPredicateForm.addElement(el);
        StringBuffer res = jsPredicateForm.toStr();
        assertNotNull(res);
        assertEquals("()", res.toString());
    }

    @Test
    public void testClonePF_1() {
        JSPredicateForm jsPredicateForm = new JSPredicateForm();
        JSPredicateForm np = jsPredicateForm.clonePF();
        assertNotNull(np);
    }

    @Test
    public void testClonePF_2() {
        JSPredicateForm jsPredicateForm = new JSPredicateForm();
        jsPredicateForm.addElement("not");
        JSPredicateForm el = new JSPredicateForm();
        jsPredicateForm.addElement(el);
        JSPredicateForm np = jsPredicateForm.clonePF();
        assertNotNull(np);
    }

    @Test
    public void testApplySubstitutionPF_1() {
        JSSubstitution alpha = new JSSubstitution();
        JSPredicateForm jsPredicateForm = new JSPredicateForm();
        JSPredicateForm np = jsPredicateForm.applySubstitutionPF(alpha);
        assertNotNull(np);
    }

    @Test
    public void testApplySubstitutionPF_2() {
        JSSubstitution alpha = new JSSubstitution();
        JSPredicateForm jsPredicateForm = new JSPredicateForm();
        jsPredicateForm.addElement("not");
        JSPredicateForm el = new JSPredicateForm();
        jsPredicateForm.addElement(el);
        JSPredicateForm np = jsPredicateForm.applySubstitutionPF(alpha);
        assertNotNull(np);
    }

    @Test
    public void testMatches_1() {
        JSPredicateForm t = new JSPredicateForm();
        JSPredicateForm jsPredicateForm = new JSPredicateForm();
        JSSubstitution beta = jsPredicateForm.matches(t);
        assertNotNull(beta);
    }

    @Test
    public void testMatches_2() {
        JSPredicateForm t = new JSPredicateForm();
        JSPredicateForm jsPredicateForm = new JSPredicateForm();
        JSSubstitution alpha = new JSSubstitution();
        JSSubstitution beta = jsPredicateForm.matches(t, alpha);
        assertNotNull(beta);
    }

    @Test
    public void testEquals_1() {
        JSPredicateForm t = new JSPredicateForm();
        JSPredicateForm jsPredicateForm = new JSPredicateForm();
        boolean result = jsPredicateForm.equals(t);
        assertFalse(result);
    }

    @Test
    public void testStandarizerPredicateForm_1() {
        JSPredicateForm jsPredicateForm = new JSPredicateForm();
        JSPredicateForm nTA = jsPredicateForm.standarizerPredicateForm();
        assertNotNull(nTA);
    }

    @Test
    public void testStandarizerPredicateForm_2() {
        JSPredicateForm jsPredicateForm = new JSPredicateForm();
        jsPredicateForm.addElement("not");
        JSPredicateForm el = new JSPredicateForm();
        jsPredicateForm.addElement(el);
        JSPredicateForm nTA = jsPredicateForm.standarizerPredicateForm();
        assertNotNull(nTA);
    }
}
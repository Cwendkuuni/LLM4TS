import org.junit.Test;
import static org.junit.Assert.*;

public class JSPredicateFormTest {

    @Test
    public void testJSPredicateFormConstructorText() {
        JSPredicateForm jspf = new JSPredicateForm("test");
        assertNotNull(jspf);
    }
    
    @Test
    public void testJSPredicateFormConstructorTokenizer() {
        JSPredicateForm jspf = new JSPredicateForm(new StreamTokenizer(new StringReader("test")));
        assertNotNull(jspf);
    }
    
    @Test
    public void testJSPredicateFormInitNullTokenizer() {
        try {
            JSPredicateForm jspf = new JSPredicateForm();
            jspf.JSPredicateFormInit(null);
            fail("Exception not thrown for null tokenizer");
        } catch (JSParserError e) {
            assertTrue(true);
        }
    }
    
    @Test
    public void testJSPredicateFormInitInvalidToken() {
        try {
            JSPredicateForm jspf = new JSPredicateForm();
            StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("invalid_token"));
            jspf.JSPredicateFormInit(tokenizer);
            fail("Exception not thrown for invalid token");
        } catch (JSParserError e) {
            assertTrue(true);
        }
    }
    
    @Test
    public void testPrintEmpty() {
        JSPredicateForm jspf = new JSPredicateForm();
        assertEquals("( )", jspf.toStr().toString());
    }
    
    @Test
    public void testClonePF() {
        JSPredicateForm jspf = new JSPredicateForm("!test");
        JSPredicateForm clonedPF = jspf.clonePF();
        assertEquals(jspf.toStr().toString(), clonedPF.toStr().toString());
    }
    
    @Test
    public void testApplySubstitutionPF() {
        JSPredicateForm jspf = new JSPredicateForm("test");
        JSSubstitution alpha = new JSSubstitution();
        JSPredicateForm appliedPF = jspf.applySubstitutionPF(alpha);
        assertEquals(jspf.toStr().toString(), appliedPF.toStr().toString());
    }
    
    @Test
    public void testMatches() {
        JSPredicateForm pf1 = new JSPredicateForm("test");
        JSPredicateForm pf2 = new JSPredicateForm("test");
        JSSubstitution sub = pf1.matches(pf2);
        assertNotNull(sub);
        assertTrue(sub.success());
    }
    
    @Test
    public void testEquals() {
        JSPredicateForm pf1 = new JSPredicateForm("test");
        JSPredicateForm pf2 = new JSPredicateForm("test");
        assertTrue(pf1.equals(pf2));
    }
    
    @Test
    public void testStandarizerPredicateForm() {
        JSPredicateForm jspf = new JSPredicateForm("!test");
        assertEquals("( !test )", jspf.standarizerPredicateForm().toStr().toString());
    }

}
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.StringReader;
import java.io.StreamTokenizer;

public class JSPredicateFormTest {

    @Test
    public void testJSPredicateFormConstructorText() {
        String text = "(test)";
        JSPredicateForm jspf = new JSPredicateForm(text);
        assertNotNull(jspf);
        assertEquals(1, jspf.size());
        assertEquals("test", jspf.elementAt(0));
    }

    @Test
    public void testJSPredicateFormConstructorTokenizer() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(test)"));
        JSPredicateForm jspf = new JSPredicateForm(tokenizer);
        assertNotNull(jspf);
        assertEquals(1, jspf.size());
        assertEquals("test", jspf.elementAt(0));
    }

    @Test
    public void testJSPredicateFormInit() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(test)"));
        JSPredicateForm jspf = new JSPredicateForm();
        jspf.JSPredicateFormInit(tokenizer);
        assertEquals(1, jspf.size());
        assertEquals("test", jspf.elementAt(0));
    }

    @Test
    public void testPrint() {
        JSPredicateForm jspf = new JSPredicateForm("(test)");
        jspf.print(); // This method prints to console, so no return value to check
    }

    @Test
    public void testToStr() {
        JSPredicateForm jspf = new JSPredicateForm("(test)");
        StringBuffer str = jspf.toStr();
        assertNotNull(str);
        assertEquals("(test)", str.toString());
    }

    @Test
    public void testClonePF() {
        JSPredicateForm jspf = new JSPredicateForm("(not (test))");
        JSPredicateForm cloned = jspf.clonePF();
        assertNotNull(cloned);
        assertEquals(jspf.size(), cloned.size());
    }

    @Test
    public void testApplySubstitutionPF() {
        JSPredicateForm jspf = new JSPredicateForm("(not (test))");
        JSSubstitution alpha = new JSSubstitution();
        JSPredicateForm result = jspf.applySubstitutionPF(alpha);
        assertNotNull(result);
    }

    @Test
    public void testMatches() {
        JSPredicateForm jspf1 = new JSPredicateForm("(A B C)");
        JSPredicateForm jspf2 = new JSPredicateForm("(A B C)");
        JSSubstitution alpha = jspf1.matches(jspf2);
        assertNotNull(alpha);
        assertFalse(alpha.fail());
    }

    @Test
    public void testEquals() {
        JSPredicateForm jspf1 = new JSPredicateForm("(A B C)");
        JSPredicateForm jspf2 = new JSPredicateForm("(A B C)");
        assertTrue(jspf1.equals(jspf2));
    }

    @Test
    public void testStandarizerPredicateForm() {
        JSPredicateForm jspf = new JSPredicateForm("(not (test))");
        JSPredicateForm standardized = jspf.standarizerPredicateForm();
        assertNotNull(standardized);
    }
}
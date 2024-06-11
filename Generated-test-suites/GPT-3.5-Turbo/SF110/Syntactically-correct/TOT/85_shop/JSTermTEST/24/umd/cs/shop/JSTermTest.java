import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JSTermTest {
  
    private JSTerm term;
  
    @Before
    public void setUp() {
        term = new JSTerm();
    }
  
    @Test
    public void testConstructor_emptyTerm() {
        assertNotNull(term);
        assertFalse(term.isVariable());
        assertFalse(term.isConstant());
        assertFalse(term.isFunction());
        assertFalse(term.isEval());
        assertEquals(0, term.size());
    }
  
    @Test
    public void testConstructor_variableTerm() {
        StreamTokenizer tokenizer = new StreamTokenizer("?");
        JSTerm variableTerm = new JSTerm(tokenizer);
        assertNotNull(variableTerm);
        assertTrue(variableTerm.isVariable());
        assertFalse(variableTerm.isConstant());
        assertFalse(variableTerm.isFunction());
        assertFalse(variableTerm.isEval());
        assertEquals(1, variableTerm.size());
        assertEquals("?", variableTerm.elementAt(0));
    }
  
    @Test
    public void testConstructor_constantTerm() {
        StreamTokenizer tokenizer = new StreamTokenizer("5");
        JSTerm constantTerm = new JSTerm(tokenizer);
        assertNotNull(constantTerm);
        assertFalse(constantTerm.isVariable());
        assertTrue(constantTerm.isConstant());
        assertFalse(constantTerm.isFunction());
        assertFalse(constantTerm.isEval());
        assertEquals(1, constantTerm.size());
        assertEquals("5", constantTerm.elementAt(0));
    }

    //... additional test cases for each method
  
}
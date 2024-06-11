
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;
import java.io.*;

public class JSPredicateFormTest {
    
    private JSPredicateForm predicate;
    
    @Before
    public void setUp() {
        predicate = new JSPredicateForm();
    }
    
    // Testing JSPredicateForm constructors
    
    @Test
    public void testDefaultConstructor() {
        assertNotNull(predicate);
    }
    
    @Test
    public void testConstructorWithText() {
        String text = "(call 1 2)";
        predicate = new JSPredicateForm(text);
        assertNotNull(predicate);
    }
    
    @Test
    public void testConstructorWithTokenizer() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(call 1 2)"));
        predicate = new JSPredicateForm(tokenizer);
        assertNotNull(predicate);
    }
    
    // Testing JSPredicateFormInit method
    
    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInit_NullTokenizer() {
        predicate.JSPredicateFormInit(null);
    }
    
    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInit_ExpectLeftPar() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(""));
        predicate.JSPredicateFormInit(tokenizer);
    }
    
    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInit_ReadingPredicate() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("("));
        predicate.JSPredicateFormInit(tokenizer);
    }
    
    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInit_UnexpectedRightPar() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(")"));
        predicate.JSPredicateFormInit(tokenizer);
    }
    
    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInit_OperatorNameExpected() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("!(!!)"));
        predicate.JSPredicateFormInit(tokenizer);
    }
    
    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInit_InvalidWord() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("!(%%%"));
        predicate.JSPredicateFormInit(tokenizer);
    }
    
    // Testing print method
    
    @Test
    public void testPrint_EmptyPredicate() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        predicate.print();
        String expectedOutput = "Predicateform.print(): 0 elements\n";
        assertEquals(expectedOutput, outContent.toString());
    }
    
    @Test
    public void testPrint_NotPredicate() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        predicate.addElement("not");
        JSPredicateForm innerPredicate = new JSPredicateForm();
        innerPredicate.addElement("call");
        innerPredicate.addElement("1");
        innerPredicate.addElement("2");
        predicate.addElement(innerPredicate);
        
        predicate.print();
        String expectedOutput = "(! (call 1 2 ) ) \n";
        assertEquals(expectedOutput, outContent.toString());
    }
    
    // Testing toStr method
    
    @Test
    public void testToStr_EmptyPredicate() {
        assertEquals("()", predicate.toStr().toString());
    }
    
    @Test
    public void testToStr_NotPredicate() {
        predicate.addElement("not");
        JSPredicateForm innerPredicate = new JSPredicateForm();
        innerPredicate.addElement("call");
        innerPredicate.addElement("1");
        innerPredicate.addElement("2");
        predicate.addElement(innerPredicate);
        
        assertEquals("(! (call 1 2 ))", predicate.toStr().toString());
    }
    
    // Testing clonePF method
    
    @Test
    public void testClonePF_NotPredicate() {
        predicate.addElement("not");
        JSPredicateForm innerPredicate = new JSPredicateForm();
        innerPredicate.addElement("call");
        innerPredicate.addElement("1");
        innerPredicate.addElement("2");
        predicate.addElement(innerPredicate);
        
        JSPredicateForm clonedPredicate = predicate.clonePF();
        
        assertEquals(predicate, clonedPredicate);
        assertNotSame(predicate, clonedPredicate);
    }
    
    // Testing applySubstitutionPF method
    
    @Test
    public void testApplySubstitutionPF_NotPredicate() {
        JSSubstitution alpha = new JSSubstitution();
        JSTerm term1 = new JSTerm();
        term1.addElement("1");
        JSTerm term2 = new JSTerm();
        term2.addElement("2");
        alpha.addElement("A");
        alpha.addElement(term1);
        alpha.addElement("B");
        alpha.addElement(term2);
        
        predicate.addElement("not");
        JSPredicateForm innerPredicate = new JSPredicateForm();
        innerPredicate.addElement("call");
        innerPredicate.addElement("A");
        innerPredicate.addElement("B");
        predicate.addElement(innerPredicate);
        
        JSPredicateForm substitutedPredicate = predicate.applySubstitutionPF(alpha);
        
        JSPredicateForm expectedPredicate = new JSPredicateForm();
        expectedPredicate.addElement("not");
        JSPredicateForm expectedInnerPredicate = new JSPredicateForm();
        expectedInnerPredicate.addElement("call");
        expectedInnerPredicate.addElement("1");
        expectedInnerPredicate.addElement("2");
        expectedPredicate.addElement(expectedInnerPredicate);
        
        assertEquals(expectedPredicate, substitutedPredicate);
    }
    
    // Testing matches method
    
    @Test
    public void testMatches_SizeMismatch() {
        JSPredicateForm otherPredicate = new JSPredicateForm();
        otherPredicate.addElement("not");
        otherPredicate.addElement("call");
        otherPredicate.addElement("1");
        otherPredicate.addElement("2");
        
        assertNull(predicate.matches(otherPredicate));
    }
    
    @Test
    public void testMatches_FunctorMismatch() {
        JSPredicateForm otherPredicate = new JSPredicateForm();
        otherPredicate.addElement("and");
        otherPredicate.addElement("call");
        otherPredicate.addElement("1");
        otherPredicate.addElement("2");
        
        assertNull(predicate.matches(otherPredicate));
    }
    
    @Test
    public void testMatches_NotPredicate() {
        predicate.addElement("not");
        JSPredicateForm innerPredicate1 = new JSPredicateForm();
        innerPredicate1.addElement("call");
        innerPredicate1.addElement("A");
        innerPredicate1.addElement("B");
        predicate.addElement(innerPredicate1);
        
        JSPredicateForm otherPredicate = new JSPredicateForm();
        otherPredicate.addElement("not");
        JSPredicateForm innerPredicate2 = new JSPredicateForm();
        innerPredicate2.addElement("call");
        innerPredicate2.addElement("A");
        innerPredicate2.addElement("B");
        otherPredicate.addElement(innerPredicate2);
        
        JSSubstitution expectedAlpha = new JSSubstitution();
        expectedAlpha.assignSuccess();
        
        JSSubstitution alpha = predicate.matches(otherPredicate);
        
        assertEquals(expectedAlpha, alpha);
    }
    
    @Test
    public void testMatches_TermMismatch() {
        predicate.addElement("call");
        predicate.addElement("A");
        predicate.addElement("B");
        
        JSPredicateForm otherPredicate = new JSPredicateForm();
        otherPredicate.addElement("call");
        otherPredicate.addElement("1");
        otherPredicate.addElement("2");
        
        assertNull(predicate.matches(otherPredicate));
    }
    
    // Testing equals method
    
    @Test
    public void testEquals_NullObject() {
        assertFalse(predicate.equals(null));
    }
    
    @Test
    public void testEquals_SizeMismatch() {
        JSPredicateForm otherPredicate = new JSPredicateForm();
        otherPredicate.addElement("not");
        otherPredicate.addElement("call");
        otherPredicate.addElement("1");
        otherPredicate.addElement("2");
        
        assertFalse(predicate.equals(otherPredicate));
    }
    
    @Test
    public void testEquals_FunctorMismatch() {
        JSPredicateForm otherPredicate = new JSPredicateForm();
        otherPredicate.addElement("and");
        otherPredicate.addElement("call");
        otherPredicate.addElement("1");
        otherPredicate.addElement("2");
        
        assertFalse(predicate.equals(otherPredicate));
    }
    
    @Test
    public void testEquals_NotPredicate() {
        predicate.addElement("not");
        JSPredicateForm innerPredicate1 = new JSPredicateForm();
        innerPredicate1.addElement("call");
        innerPredicate1.addElement("A");
        innerPredicate1.addElement("B");
        predicate.addElement(innerPredicate1);
        
        JSPredicateForm otherPredicate = new JSPredicateForm();
        otherPredicate.addElement("not");
        JSPredicateForm innerPredicate2 = new JSPredicateForm();
        innerPredicate2.addElement("call");
        innerPredicate2.addElement("A");
        innerPredicate2.addElement("B");
        otherPredicate.addElement(innerPredicate2);
        
        assertTrue(predicate.equals(otherPredicate));
    }
    
    @Test
    public void testEquals_TermMismatch() {
        predicate.addElement("call");
        predicate.addElement("A");
        predicate.addElement("B");
        
        JSPredicateForm otherPredicate = new JSPredicateForm();
        otherPredicate.addElement("call");
        JSTerm term = new JSTerm();
        term.addElement("1");
        otherPredicate.addElement(term);
        otherPredicate.addElement("B");
        
        assertFalse(predicate.equals(otherPredicate));
    }
    
    // Testing standarizerPredicateForm method
    
    @Test
    public void testStandarizerPredicateForm_NotPredicate() {
        predicate.addElement("not");
        JSPredicateForm innerPredicate = new JSPredicateForm();
        innerPredicate.addElement("call");
        innerPredicate.addElement("A");
        innerPredicate.addElement("B");
        predicate.addElement(innerPredicate);
        
        JSPredicateForm standardPredicate = predicate.standarizerPredicateForm();
        
        JSPredicateForm expectedPredicate = new JSPredicateForm();
        expectedPredicate.addElement("not");
        JSPredicateForm expectedInnerPredicate = new JSPredicateForm();
        expectedInnerPredicate.addElement("call");
        expectedInnerPredicate.addElement("A1");
        expectedInnerPredicate.addElement("B1");
        expectedPredicate.addElement(expectedInnerPredicate);
        
        assertEquals(expectedPredicate, standardPredicate);
    }
}


import org.junit.*;
import static org.junit.Assert.*;

public class LookupTranslatorTest {

    @Test
    public void testTranslate_typicalCases() {
        CharSequence[] seq1 = { "cat", "dog" };
        CharSequence[] seq2 = { "apple", "orange" };
        LookupTranslator lt = new LookupTranslator(seq1, seq2);
        
        StringBuilder input = new StringBuilder("I have a cat and an apple");
        StringBuilder expectedOutput = new StringBuilder("I have a dog and an orange");
        
        assertEquals(expectedOutput.toString(), lt.translate(input));
    }
    
    @Test
    public void testTranslate_emptyInput() {
        CharSequence[] seq1 = { "cat", "dog" };
        LookupTranslator lt = new LookupTranslator(seq1);
        
        StringBuilder input = new StringBuilder("");
        StringBuilder expectedOutput = new StringBuilder("");
        
        assertEquals(expectedOutput.toString(), lt.translate(input));
    }
    
    @Test
    public void testTranslate_emptyLookup() {
        LookupTranslator lt = new LookupTranslator();
        
        StringBuilder input = new StringBuilder("I have a cat");
        StringBuilder expectedOutput = new StringBuilder("I have a cat");
        
        assertEquals(expectedOutput.toString(), lt.translate(input));
    }
    
    @Test
    public void testTranslate_keyNotFound() {
        CharSequence[] seq1 = { "cat", "dog" };
        CharSequence[] seq2 = { "apple", "orange" };
        LookupTranslator lt = new LookupTranslator(seq1, seq2);
        
        StringBuilder input = new StringBuilder("I have a tiger");
        StringBuilder expectedOutput = new StringBuilder("I have a tiger");
        
        assertEquals(expectedOutput.toString(), lt.translate(input));
    }
    
    @Test
    public void testTranslate_partialMatch() {
        CharSequence[] seq1 = { "cat", "dog" };
        CharSequence[] seq2 = { "apple", "orange" };
        LookupTranslator lt = new LookupTranslator(seq1, seq2);
        
        StringBuilder input = new StringBuilder("I have a cat and an apple");
        StringBuilder expectedOutput = new StringBuilder("I have a dog and an apple");
        
        assertEquals(expectedOutput.toString(), lt.translate(input));
    }
    
    @Test
    public void testTranslate_consecutiveMatches() {
        CharSequence[] seq1 = { "aa", "bb", "aaa", "bbbb" };
        LookupTranslator lt = new LookupTranslator(seq1);
        
        StringBuilder input = new StringBuilder("aaabbb");
        StringBuilder expectedOutput = new StringBuilder("bbbbb");
        
        assertEquals(expectedOutput.toString(), lt.translate(input));
    }
    
    @Test
    public void testTranslate_unsupportedCharacter() {
        CharSequence[] seq1 = { "cat", "dog" };
        LookupTranslator lt = new LookupTranslator(seq1);
        
        StringBuilder input = new StringBuilder("I have a çãt");
        StringBuilder expectedOutput = new StringBuilder("I have a çãt");
        
        assertEquals(expectedOutput.toString(), lt.translate(input));
    }
    
}
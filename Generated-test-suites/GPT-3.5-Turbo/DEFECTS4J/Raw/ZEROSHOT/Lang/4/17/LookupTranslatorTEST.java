import static org.junit.Assert.*;
import org.junit.Test;

public class LookupTranslatorTest {
    
    @Test
    public void testConstructor() {
        CharSequence[] seq1 = {"a", "1"};
        CharSequence[] seq2 = {"b", "2"};
        CharSequence[][] lookup = {seq1, seq2};
        LookupTranslator translator = new LookupTranslator(lookup);
        
        HashMap<CharSequence, CharSequence> expectedMap = new HashMap<CharSequence, CharSequence>();
        expectedMap.put("a", "1");
        expectedMap.put("b", "2");
        
        assertEquals(expectedMap, translator.lookupMap);
        assertEquals(1, translator.shortest);
        assertEquals(1, translator.longest);
    }
    
    @Test
    public void testTranslate_noMatch() {
        LookupTranslator translator = new LookupTranslator();
        StringBuilder input = new StringBuilder("abc");
        StringBuilder output = new StringBuilder();
        
        try {
            int result = translator.translate(input, 0, output);
            assertEquals(0, result);
            assertEquals("", output.toString());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testTranslate_partialMatch() {
        CharSequence[] seq1 = {"ab", "1"};
        CharSequence[] seq2 = {"bc", "2"};
        CharSequence[][] lookup = {seq1, seq2};
        LookupTranslator translator = new LookupTranslator(lookup);
        
        StringBuilder input = new StringBuilder("abc");
        StringBuilder output = new StringBuilder();
        
        try {
            int result = translator.translate(input, 0, output);
            assertEquals(2, result);
            assertEquals("1", output.toString());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testTranslate_exactMatch() {
        CharSequence[] seq1 = {"ab", "1"};
        CharSequence[] seq2 = {"bc", "2"};
        CharSequence[][] lookup = {seq1, seq2};
        LookupTranslator translator = new LookupTranslator(lookup);
        
        StringBuilder input = new StringBuilder("bc");
        StringBuilder output = new StringBuilder();
        
        try {
            int result = translator.translate(input, 0, output);
            assertEquals(2, result);
            assertEquals("2", output.toString());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

}
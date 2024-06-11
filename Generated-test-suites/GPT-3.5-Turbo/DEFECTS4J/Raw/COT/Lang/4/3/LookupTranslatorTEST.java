import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class LookupTranslatorTest {
    
    private LookupTranslator lookupTranslator;
    
    @Before
    public void setUp() {
        // Define the lookup table
        CharSequence[] seq1 = {"ab", "12"};
        CharSequence[] seq2 = {"cd", "34"};
        CharSequence[][] lookup = {seq1, seq2};
        lookupTranslator = new LookupTranslator(lookup);
    }
    
    @Test
    public void testTranslate_WithValidLookup_ReturnsTranslatedInput() throws IOException {
        CharSequence input = "abcd";
        StringWriter writer = new StringWriter();
        int translatedLength = lookupTranslator.translate(input, 0, writer);
        
        assertEquals("1234", writer.toString());
        assertEquals(4, translatedLength);
    }
    
    @Test
    public void testTranslate_WithInvalidLookup_ReturnsZeroTranslatedLength() throws IOException {
        CharSequence input = "efgh";
        StringWriter writer = new StringWriter();
        int translatedLength = lookupTranslator.translate(input, 0, writer);
        
        assertEquals("", writer.toString());
        assertEquals(0, translatedLength);
    }
    
    @Test
    public void testTranslate_WithPartialMatch_ReturnsTranslatedInputAndCorrectTranslatedLength() throws IOException {
        CharSequence input = "abef";
        StringWriter writer = new StringWriter();
        int translatedLength = lookupTranslator.translate(input, 0, writer);
        
        assertEquals("12", writer.toString());
        assertEquals(2, translatedLength);
    }
    
    @Test
    public void testTranslate_WithInputShorterThanShortestLookup_ReturnsZeroTranslatedLength() throws IOException {
        CharSequence input = "a";
        StringWriter writer = new StringWriter();
        int translatedLength = lookupTranslator.translate(input, 0, writer);
        
        assertEquals("", writer.toString());
        assertEquals(0, translatedLength);
    }
    
    @Test
    public void testTranslate_WithInputLongerThanLongestLookup_ReturnsTranslatedInputAndCorrectTranslatedLength() throws IOException {
        CharSequence input = "abcdefgh";
        StringWriter writer = new StringWriter();
        int translatedLength = lookupTranslator.translate(input, 0, writer);
        
        assertEquals("1234", writer.toString());
        assertEquals(4, translatedLength);
    }
}
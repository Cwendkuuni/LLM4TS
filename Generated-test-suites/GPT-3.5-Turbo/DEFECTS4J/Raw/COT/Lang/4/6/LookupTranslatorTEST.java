import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.io.StringWriter;

public class LookupTranslatorTest {
    
    private LookupTranslator lookupTranslator;
    
    @Before
    public void setUp() {
        lookupTranslator = new LookupTranslator();
    }
    
    @Test
    public void testTranslate_noMatchingLookup_returnZero() throws IOException {
        CharSequence input = "Test";
        StringWriter out = new StringWriter();
        int index = 0;
        
        int result = lookupTranslator.translate(input, index, out);
        
        assertEquals(0, result);
        assertEquals("", out.toString());
    }
    
    @Test
    public void testTranslate_singleMatchingLookup_returnLengthOfLookup() throws IOException {
        CharSequence input = "ABC";
        StringWriter out = new StringWriter();
        int index = 0;
        CharSequence[] lookup = { "AB", "123" };
        lookupTranslator = new LookupTranslator(lookup);
        
        int result = lookupTranslator.translate(input, index, out);
        
        assertEquals(2, result);
        assertEquals("123", out.toString());
    }
    
    @Test
    public void testTranslate_multipleMatchingLookup_returnLengthOfLongestLookup() throws IOException {
        CharSequence input = "ABC";
        StringWriter out = new StringWriter();
        int index = 0;
        CharSequence[] lookup = { "AB", "123", "ABC", "456" };
        lookupTranslator = new LookupTranslator(lookup);
        
        int result = lookupTranslator.translate(input, index, out);
        
        assertEquals(3, result);
        assertEquals("123", out.toString());
    }
    
    @Test
    public void testTranslate_inputTooShort_returnZero() throws IOException {
        CharSequence input = "ABC";
        StringWriter out = new StringWriter();
        int index = 1;
        CharSequence[] lookup = { "ABC", "123", "AB", "456" };
        lookupTranslator = new LookupTranslator(lookup);
        
        int result = lookupTranslator.translate(input, index, out);
        
        assertEquals(0, result);
        assertEquals("", out.toString());
    }
}
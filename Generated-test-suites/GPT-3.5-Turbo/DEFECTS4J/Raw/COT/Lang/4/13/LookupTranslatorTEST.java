import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;

import org.junit.Test;

public class LookupTranslatorTest {
    
    @Test
    public void testLookupTranslatorConstructor() {
        CharSequence[] lookup1 = { "a", "1" };
        CharSequence[] lookup2 = { "b", "2" };
        CharSequence[][] lookup = { lookup1, lookup2 };
        
        LookupTranslator translator = new LookupTranslator(lookup);
        
        // Check if lookupMap is populated correctly
        HashMap<CharSequence, CharSequence> expectedLookupMap = new HashMap<>();
        expectedLookupMap.put("a", "1");
        expectedLookupMap.put("b", "2");
        assertEquals(expectedLookupMap, translator.lookupMap);
        
        // Check if shortest and longest are set correctly
        int expectedShortest = 1;
        int expectedLongest = 1;
        assertEquals(expectedShortest, translator.shortest);
        assertEquals(expectedLongest, translator.longest);
    }
    
    @Test
    public void testTranslate() throws IOException {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] { { "a", "1" } });
        StringBuilder input = new StringBuilder("a");
        StringWriter output = new StringWriter();
    
        int result = translator.translate(input, 0, output);
    
        // Check if substitution is performed correctly
        assertEquals(1, result);
        assertEquals("1", output.toString());
    }
    
    @Test
    public void testTranslateWithEmptyInput() throws IOException {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] { { "a", "1" } });
        StringBuilder input = new StringBuilder("");
        StringWriter output = new StringWriter();
    
        int result = translator.translate(input, 0, output);
    
        // Check if no substitution is performed
        assertEquals(0, result);
        assertEquals("", output.toString());
    }
    
    @Test
    public void testTranslateWithNoMatches() throws IOException {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] { { "a", "1" } });
        StringBuilder input = new StringBuilder("b");
        StringWriter output = new StringWriter();
    
        int result = translator.translate(input, 0, output);
    
        // Check if no substitution is performed
        assertEquals(0, result);
        assertEquals("", output.toString());
    }
    
    @Test(expected = IOException.class)
    public void testTranslateIOException() throws IOException {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] { { "a", "1" } });
        StringBuilder input = new StringBuilder("a");
        Writer writer = new Writer() {
            @Override
            public void write(char[] cbuf, int off, int len) throws IOException {
                throw new IOException();
            }
    
            @Override
            public void flush() throws IOException {
            }
    
            @Override
            public void close() throws IOException {
            }
        };
    
        translator.translate(input, 0, writer);
    }
}
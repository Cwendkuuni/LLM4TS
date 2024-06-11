
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;

public class CharSequenceTranslatorTest {
    
    @Test
    public void testTranslate_CharsequenceIntWriter() throws IOException {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                if (index >= input.length()) {
                    return 0;
                } else {
                    char[] c = Character.toChars(Character.codePointAt(input, index));
                    out.write(c);
                    return 1;
                }
            }
        };
        
        StringWriter writer = new StringWriter();
        
        // Test case 1: Empty input
        String input = "";
        int expectedCount = 0;
        int actualCount = translator.translate(input, 0, writer);
        assertEquals(expectedCount, actualCount);
        assertEquals("", writer.toString());
        
        writer.getBuffer().setLength(0);
        
        // Test case 2: Single character input
        input = "A";
        expectedCount = 1;
        actualCount = translator.translate(input, 0, writer);
        assertEquals(expectedCount, actualCount);
        assertEquals("A", writer.toString());
        
        writer.getBuffer().setLength(0);
        
        // Test case 3: Multiple character input
        input = "Hello";
        expectedCount = 4;
        actualCount = translator.translate(input, 1, writer);
        assertEquals(expectedCount, actualCount);
        assertEquals("e", writer.toString());
    }
    
    @Test
    public void testTranslate_Charsequence() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };
        
        // Test case 1: Null input
        String input = null;
        assertNull(translator.translate(input));
        
        // Test case 2: Empty input
        input = "";
        assertEquals("", translator.translate(input));
        
        // Test case 3: Non-empty input
        input = "Hello";
        assertEquals("", translator.translate(input));
    }
    
    @Test
    public void testTranslate_CharsequenceWriter() throws IOException {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                if (index >= input.length()) {
                    return 0;
                } else {
                    char[] c = Character.toChars(Character.codePointAt(input, index));
                    out.write(c);
                    return 1;
                }
            }
        };
        
        StringWriter writer = new StringWriter();
        
        // Test case 1: Null input
        String input = null;
        translator.translate(input, writer);
        assertEquals("", writer.toString());
        
        writer.getBuffer().setLength(0);
        
        // Test case 2: Empty input
        input = "";
        translator.translate(input, writer);
        assertEquals("", writer.toString());
        
        writer.getBuffer().setLength(0);
        
        // Test case 3: Non-empty input
        input = "Hello";
        translator.translate(input, writer);
        assertEquals("Hello", writer.toString());
    }
    
    @Test
    public void testWith_CharSequenceTranslatorArray() {
        CharSequenceTranslator translator1 = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };
        
        CharSequenceTranslator translator2 = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };
        
        CharSequenceTranslator translator3 = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };
        
        CharSequenceTranslator mergedTranslator = translator1.with(translator2, translator3);
        
        assertEquals(translator1, mergedTranslator);
    }
    
    @Test
    public void testHex() {
        int codepoint = 65;
        String expected = "41";
        String actual = CharSequenceTranslator.hex(codepoint);
        assertEquals(expected, actual);
    }
}

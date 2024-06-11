
import org.junit.Test;
import static org.junit.Assert.*;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate() {
        // Create an instance of CharSequenceTranslator
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                // Dummy translation method for testing purposes
                out.write(input.charAt(index));
                return 1;
            }
        };
        
        // Test null input
        assertNull(translator.translate(null));
        
        // Test empty input
        assertEquals("", translator.translate(""));
        
        // Test normal input
        assertEquals("abc", translator.translate("abc"));
        
        // Test translation with basic escape
        assertEquals("\\u0061bc", translator.translate("abc"));
    }

    @Test
    public void testTranslateWithWriter() {
        // Create an instance of CharSequenceTranslator
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                // Dummy translation method for testing purposes
                out.write(input.charAt(index));
                return 1;
            }
        };
        
        // Test null input
        StringWriter writer = new StringWriter();
        translator.translate(null, writer);
        assertEquals("", writer.toString());
        
        // Test empty input
        writer = new StringWriter();
        translator.translate("", writer);
        assertEquals("", writer.toString());
        
        // Test normal input
        writer = new StringWriter();
        translator.translate("abc", writer);
        assertEquals("abc", writer.toString());
        
        // Test translation with basic escape
        writer = new StringWriter();
        translator.translate("abc", writer);
        assertEquals("\\u0061bc", writer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithNullWriter() throws IOException {
        // Create an instance of CharSequenceTranslator
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                // Empty translation method for testing purposes
                return 0;
            }
        };

        // Test null writer
        translator.translate("abc", null);
    }

    @Test
    public void testWith() {
        // Create an instance of CharSequenceTranslator
        CharSequenceTranslator translator1 = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                // Dummy translation method for testing purposes
                out.write(input.charAt(index));
                return 1;
            }
        };
        
        CharSequenceTranslator translator2 = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                // Dummy translation method for testing purposes
                out.write(Character.toUpperCase(input.charAt(index)));
                return 1;
            }
        };
        
        // Test merging of translators
        CharSequenceTranslator mergedTranslator = translator1.with(translator2);
        
        // Test null input
        assertEquals("", mergedTranslator.translate(null));
        
        // Test empty input
        assertEquals("", mergedTranslator.translate(""));
        
        // Test normal input
        assertEquals("aA", mergedTranslator.translate("a"));
        
        // Test translation with basic escape
        assertEquals("A\\u0041", mergedTranslator.translate("aA"));
    }

    @Test
    public void testHex() {
        // Test hexadecimal conversion of a codepoint
        assertEquals("41", CharSequenceTranslator.hex(65));
        
        // Test hexadecimal conversion of a codepoint with lower case
        assertEquals("61", CharSequenceTranslator.hex(97));
    }
}


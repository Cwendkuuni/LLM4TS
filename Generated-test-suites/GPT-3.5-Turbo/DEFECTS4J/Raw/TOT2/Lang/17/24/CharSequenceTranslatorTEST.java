import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate() throws IOException {
        // Test null input
        CharSequenceTranslator translator = new MockCharSequenceTranslator();
        assertNull(translator.translate(null));
        
        // Test basic input
        assertEquals("abc", translator.translate("abc"));
        
        // Test empty input
        assertEquals("", translator.translate(""));
        
        // Test special characters
        assertEquals("&lt;&gt;&amp;&quot;", translator.translate("<>&\""));
        
        // Test Unicode characters
        assertEquals("\\u0041\\u0042\\u0043", translator.translate("ABC"));
        
        // Test edge cases
        assertEquals("XYZ", translator.translate("\uD83D\uDC7BXYZ\uD83D\uDC7C"));
    }

    @Test
    public void testTranslateWithWriter() throws IOException {
        // Test null input
        CharSequenceTranslator translator = new MockCharSequenceTranslator();
        StringWriter writer = new StringWriter();
        translator.translate(null, writer);
        assertEquals("", writer.toString());
        
        // Test basic input
        writer = new StringWriter();
        translator.translate("abc", writer);
        assertEquals("abc", writer.toString());
        
        // Test special characters
        writer = new StringWriter();
        translator.translate("<>&\"", writer);
        assertEquals("&lt;&gt;&amp;&quot;", writer.toString());
        
        // Test Unicode characters
        writer = new StringWriter();
        translator.translate("ABC", writer);
        assertEquals("\\u0041\\u0042\\u0043", writer.toString());
        
        // Test edge cases
        writer = new StringWriter();
        translator.translate("\uD83D\uDC7BXYZ\uD83D\uDC7C", writer);
        assertEquals("XYZ", writer.toString());
    }

    @Test
    public void testTranslateWithIndex() throws IOException {
        // Test null input
        CharSequenceTranslator translator = new MockCharSequenceTranslator();
        StringWriter writer = new StringWriter();
        assertEquals(0, translator.translate(null, 0, writer));
        
        // Test basic input
        writer = new StringWriter();
        assertEquals(3, translator.translate("abc", 0, writer));
        assertEquals("abc", writer.toString());
        
        // Test special characters
        writer = new StringWriter();
        assertEquals(0, translator.translate("<>&\"", 0, writer));
        assertEquals("&lt;&gt;&amp;&quot;", writer.toString());
        
        // Test Unicode characters
        writer = new StringWriter();
        assertEquals(3, translator.translate("ABC", 0, writer));
        assertEquals("\\u0041\\u0042\\u0043", writer.toString());
        
        // Test edge cases
        writer = new StringWriter();
        assertEquals(3, translator.translate("\uD83D\uDC7BXYZ\uD83D\uDC7C", 0, writer));
        assertEquals("XYZ", writer.toString());
    }

    @Test
    public void testHex() {
        assertEquals("41", CharSequenceTranslator.hex(65));
        assertEquals("7B", CharSequenceTranslator.hex(123));
        assertEquals("3039", CharSequenceTranslator.hex(12345));
    }

    private class MockCharSequenceTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (input == null || out == null) {
                return 0;
            }
            if (input.charAt(index) == '<') {
                out.write("&lt;");
                return 1;
            } else if (input.charAt(index) == '>') {
                out.write("&gt;");
                return 1;
            } else if (input.charAt(index) == '&') {
                out.write("&amp;");
                return 1;
            } else if (input.charAt(index) == '"') {
                out.write("&quot;");
                return 1;
            }
            return 0;
        }
    }

}
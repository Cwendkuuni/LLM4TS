import org.junit.Test;
import static org.junit.Assert.*;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate() {
        CharSequenceTranslator translator = new TestTranslator();

        // Test null input
        assertNull(translator.translate(null));

        // Test empty input
        assertEquals("", translator.translate(""));

        // Test translation
        assertEquals("translated", translator.translate("input"));

        // Test translation with special characters
        assertEquals("translated&#33;&#34;&#35;", translator.translate("input!!!\"#"));

        // Test translation with multiple translations
        assertEquals("translated1translated2translated3", translator.translate("input123"));
    }
    
    // Inner class for testing
    private class TestTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (input.charAt(index) == 'i') {
                out.write("translated");
                return 1;
            } else if (Character.isDigit(input.charAt(index))) {
                out.write("translated" + input.charAt(index));
                return 1;
            } else if (Character.isWhitespace(input.charAt(index))) {
                return 0;
            } else {
                CharSequenceTranslator.escapeHtml4(out, Character.codePointAt(input, index));
                return 1;
            }
        }
    }
}
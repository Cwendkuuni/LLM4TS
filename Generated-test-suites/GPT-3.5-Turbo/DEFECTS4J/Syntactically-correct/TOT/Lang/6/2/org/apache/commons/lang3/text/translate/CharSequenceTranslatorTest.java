import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate_basicTranslation() {
        // Test for basic translation of input
        CharSequenceTranslator translator = new ConcreteTranslator();
        StringBuilder sb = new StringBuilder();
        StringWriter writer = new StringWriter(sb);
        try {
            translator.translate("Test Input", writer);
            writer.flush();
        } catch (IOException e) {
            fail("IOException occurred");
        }
        assertEquals("Translation did not match expected output", "&#84;&#101;&#115;&#116;&#32;&#73;&#110;&#112;&#117;&#116;", sb.toString());
    }

    @Test
    public void testTranslate_nullInput() {
        // Test for translation of null input
        CharSequenceTranslator translator = new ConcreteTranslator();
        assertNull("Translation did not return null for null input", translator.translate(null));
    }

    @Test
    public void testTranslate_emptyInput() {
        // Test for translation of empty input
        CharSequenceTranslator translator = new ConcreteTranslator();
        assertEquals("Translation did not return empty string for empty input", "", translator.translate(""));
    }

    @Test
    public void testTranslate_withSpecialCharacters() {
        // Test for translation with special characters
        CharSequenceTranslator translator = new ConcreteTranslator();
        assertEquals("Translation did not handle special characters correctly", "&", translator.translate("&"));
    }

    @Test
    public void testWith_mergingTranslators() {
        // Test for merging translators with with() method
        CharSequenceTranslator translator1 = new ConcreteTranslator();
        CharSequenceTranslator translator2 = new ConcreteTranslator();
        CharSequenceTranslator mergedTranslator = translator1.with(translator2);
        assertNotNull("Merged translator is null", mergedTranslator);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslate_nullWriter() throws IOException {
        // Test for translation with null writer
        CharSequenceTranslator translator = new ConcreteTranslator();
        translator.translate("Test Input", null);
    }

    private class ConcreteTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (input.charAt(index) == ' ') {
                out.write("&#32;");
                return 1;
            }
            out.write("&#" + (int) input.charAt(index) + ";");
            return 1;
        }
    }

}
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.Writer;
import java.util.Locale;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate() {
        CharSequenceTranslator translator = new MockCharSequenceTranslator();

        // Test null input
        String result = translator.translate(null);
        Assert.assertNull(result);

        // Test empty input
        result = translator.translate("");
        Assert.assertEquals("", result);
        
        // Test non-null input
        result = translator.translate("Hello");
        Assert.assertEquals("Translated Hello", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithNullWriter() throws IOException {
        CharSequenceTranslator translator = new MockCharSequenceTranslator();
        translator.translate("Test", null);
    }

    @Test
    public void testTranslateWithWriter() throws IOException {
        CharSequenceTranslator translator = new MockCharSequenceTranslator();
        StringWriter writer = new StringWriter();
        translator.translate("Test", writer);
        String result = writer.toString();
        Assert.assertEquals("Translated Test", result);
    }

    @Test
    public void testWith() {
        CharSequenceTranslator translator1 = new MockCharSequenceTranslator();
        CharSequenceTranslator translator2 = new MockCharSequenceTranslator();
        CharSequenceTranslator translator3 = new MockCharSequenceTranslator();

        CharSequenceTranslator mergedTranslator = translator1.with(translator2, translator3);
        Assert.assertNotNull(mergedTranslator);
    }
    
    @Test
    public void testHex() {
        String hexString = CharSequenceTranslator.hex(10);
        Assert.assertEquals("A", hexString);
    }

    private static class MockCharSequenceTranslator extends CharSequenceTranslator {

        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            out.write("Translated " + input.subSequence(index, input.length()));
            return input.length();
        }

    }
}
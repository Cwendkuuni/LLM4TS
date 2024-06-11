import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;

import java.io.IOException;
import java.io.StringWriter;

public class CharSequenceTranslatorTest {

    private static class TestingTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            // Translator implementation for testing purposes
            return Character.codePointAt(input, index);
        }
    }
    
    private TestingTranslator testingTranslator = new TestingTranslator();

    @Test
    public void testTranslateNullInput() {
        String result = testingTranslator.translate(null);
        Assert.assertNull(result);
    }

    @Test
    public void testTranslateEmptyInput() {
        String result = testingTranslator.translate("");
        Assert.assertEquals("", result);
    }

    @Test
    public void testTranslateSingleCharacter() {
        String result = testingTranslator.translate("a");
        Assert.assertEquals("97", result);
    }

    @Test
    public void testTranslateMultipleCharacters() {
        String result = testingTranslator.translate("abc");
        Assert.assertEquals("979899", result);
    }

    @Test
    public void testTranslateSurrogatePair() {
        String result = testingTranslator.translate("😀");
        Assert.assertEquals("128512", result);
    }

    @Test
    public void testTranslateWithWriterNullInput() throws IOException {
        StringWriter writer = new StringWriter();
        testingTranslator.translate(null, writer);
        Assert.assertEquals("", writer.toString());
    }

    @Test
    public void testTranslateWithWriterEmptyInput() throws IOException {
        StringWriter writer = new StringWriter();
        testingTranslator.translate("", writer);
        Assert.assertEquals("", writer.toString());
    }

    @Test
    public void testTranslateWithWriterSingleCharacter() throws IOException {
        StringWriter writer = new StringWriter();
        testingTranslator.translate("a", writer);
        Assert.assertEquals("97", writer.toString());
    }

    @Test
    public void testTranslateWithWriterMultipleCharacters() throws IOException {
        StringWriter writer = new StringWriter();
        testingTranslator.translate("abc", writer);
        Assert.assertEquals("979899", writer.toString());
    }

    @Test
    public void testTranslateWithWriterSurrogatePair() throws IOException {
        StringWriter writer = new StringWriter();
        testingTranslator.translate("😀", writer);
        Assert.assertEquals("128512", writer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithWriterNullWriter() throws IOException {
        testingTranslator.translate("abc", null);
    }

    @Test
    public void testHex() {
        String result = CharSequenceTranslator.hex(65);
        Assert.assertEquals("41", result);
    }

}
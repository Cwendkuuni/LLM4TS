
import org.junit.Test;
import org.junit.Assert;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslateCharSequenceIntWriter() throws IOException {
        CharSequenceTranslator translator = new MockTranslator();
        StringWriter writer = new StringWriter();
        int consumed = translator.translate("Hello, world!", 0, writer);
        Assert.assertEquals(13, consumed);
        Assert.assertEquals("Hello, world!", writer.toString());
    }
    
    @Test
    public void testTranslateCharSequenceWriter() throws IOException {
        CharSequenceTranslator translator = new MockTranslator();
        String input = "Hello, world!";
        String expectedOutput = "Hello, world!";
        String result = translator.translate(input);
        Assert.assertEquals(expectedOutput, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateNullWriter() throws IOException {
        CharSequenceTranslator translator = new MockTranslator();
        translator.translate("Hello, world!", null);
    }
    
    @Test
    public void testTranslateNullCharSequence() {
        CharSequenceTranslator translator = new MockTranslator();
        String input = null;
        String expectedOutput = null;
        String result = translator.translate(input);
        Assert.assertEquals(expectedOutput, result);
    }
    
    @Test
    public void testWith() {
        CharSequenceTranslator[] translators = new CharSequenceTranslator[1];
        translators[0] = new MockTranslator();
        CharSequenceTranslator translator = new MockTranslator().with(translators);
        Assert.assertNotNull(translator);
    }
    
    @Test
    public void testHex() {
        int codepoint = 65;
        String expectedOutput = "41";
        String result = CharSequenceTranslator.hex(codepoint);
        Assert.assertEquals(expectedOutput, result);
    }
    
    private static class MockTranslator extends CharSequenceTranslator {

        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            char c = input.charAt(index);
            out.write(c);
            return 1;
        }
    }
}


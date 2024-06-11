import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

public class CharSequenceTranslatorTest {

    private static class TestTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            return 0;
        }
    }

    @Test
    public void testTranslate() {
        CharSequenceTranslator translator = new TestTranslator();
        String input = "input";
        String expectedOutput = "input";

        String result = translator.translate(input);

        Assert.assertEquals(expectedOutput, result);
    }

    @Test
    public void testWith() {
        CharSequenceTranslator translator1 = new TestTranslator();
        CharSequenceTranslator translator2 = new TestTranslator();

        CharSequenceTranslator result = translator1.with(translator2);

        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof AggregateTranslator);
    }

    @Test
    public void testHex() {
        int codepoint = 65;
        String expectedOutput = "41";

        String result = CharSequenceTranslator.hex(codepoint);

        Assert.assertEquals(expectedOutput, result);
    }
}

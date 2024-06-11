import org.junit.*;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

public class CharSequenceTranslatorTest {
    
    private class TestTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            return 0;
        }
    }
    
    private class TestTranslator2 extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            return 1;
        }
    }

    private CharSequenceTranslator translator;

    @Before
    public void setUp() {
        translator = new TestTranslator();
    }

    @Test
    public void testTranslate() {
        String input = "Hello";
        String output = translator.translate(input);
        Assert.assertEquals(input, output);
    }

    @Test
    public void testTranslate_nullInput() {
        String input = null;
        String output = translator.translate(input);
        Assert.assertNull(output);
    }

    @Test
    public void testTranslateWriter() throws IOException {
        StringWriter writer = new StringWriter();
        String input = "Hello";
        translator.translate(input, writer);
        String output = writer.toString();
        Assert.assertEquals(input, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWriter_nullWriter() throws IOException {
        Writer writer = null;
        String input = "Hello";
        translator.translate(input, writer);
    }

    @Test
    public void testWith_translators() {
        CharSequenceTranslator translator2 = new TestTranslator2();
        CharSequenceTranslator mergedTranslator = translator.with(translator2);
        Assert.assertTrue(mergedTranslator instanceof AggregateTranslator);
    }

    @Test
    public void testHex() {
        int codepoint = 65;
        String expected = "41";
        String output = CharSequenceTranslator.hex(codepoint);
        Assert.assertEquals(expected, output);
    }

    @Test
    public void testHex_lowerCase() {
        int codepoint = 97;
        String expected = "61";
        String output = CharSequenceTranslator.hex(codepoint);
        Assert.assertEquals(expected, output);
    }

    @Test
    public void testHex_nonAscii() {
        int codepoint = 8364;
        String expected = "20AC";
        String output = CharSequenceTranslator.hex(codepoint);
        Assert.assertEquals(expected, output);
    }
}
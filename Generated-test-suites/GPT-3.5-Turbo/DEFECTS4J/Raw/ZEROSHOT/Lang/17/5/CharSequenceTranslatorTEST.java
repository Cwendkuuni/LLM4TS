import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CharSequenceTranslatorTest {

    private CharSequenceTranslator translator;

    @Before
    public void setUp() {
        translator = new CharSequenceTranslatorImpl();
    }

    @After
    public void tearDown() {
        translator = null;
    }

    @Test
    public void testTranslate() {
        // Test translation with null input
        String result = translator.translate(null);
        assertNull(result);

        // Test translation with empty input
        result = translator.translate("");
        assertEquals("", result);

        // Test translation with input containing only ASCII characters
        String input = "Hello, world!";
        result = translator.translate(input);
        assertEquals(input, result);

        // Test translation with input containing non-ASCII characters
        input = "你好，世界！";
        result = translator.translate(input);
        assertEquals(input, result);

        // Test translation with input containing special characters
        input = "Hello, <b>world</b>!";
        result = translator.translate(input);
        assertEquals("Hello, &lt;b&gt;world&lt;/b&gt;!", result);
    }

    @Test
    public void testTranslateIndexOutofBound() {
        // Test translation with index out of bound
        String input = "Hello, world!";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            try {
                translator.translate(input, i, sb);
            } catch (StringIndexOutOfBoundsException e) {
                fail("Should not throw IndexOutOfBoundsException");
            }
        }
    }

    @Test
    public void testTranslateWriter() throws Exception {
        // Test translation with writer
        String input = "Hello, <b>world</b>!";
        StringWriter writer = new StringWriter();
        translator.translate(input, writer);
        assertEquals("Hello, &lt;b&gt;world&lt;/b&gt;!", writer.toString());
    }

    @Test
    public void testWith() {
        // Test with method
        CharSequenceTranslator otherTranslator = new OtherCharSequenceTranslatorImpl();
        CharSequenceTranslator mergedTranslator = translator.with(otherTranslator);
        assertNotNull(mergedTranslator);
        String input = "Hello, <b>world</b>!";
        String result = mergedTranslator.translate(input);
        assertEquals("Hello, *b*world*/b*!", result);
    }

    @Test
    public void testHex() {
        // Test hex method
        int codepoint = 97;
        String hexString = CharSequenceTranslator.hex(codepoint);
        assertEquals("61", hexString);

        codepoint = 8364;
        hexString = CharSequenceTranslator.hex(codepoint);
        assertEquals("20AC", hexString);
    }

    private class CharSequenceTranslatorImpl extends CharSequenceTranslator {

        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (input == null || index < 0 || index >= input.length()) {
                return 0;
            }
            char c = input.charAt(index);
            out.write(c);
            return 1;
        }
    }

    private class OtherCharSequenceTranslatorImpl extends CharSequenceTranslator {

        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (input == null || index < 0 || index >= input.length()) {
                return 0;
            }
            char c = input.charAt(index);
            if (c == '<') {
                out.write("*");
                return 0;
            } else if (c == '>') {
                out.write("/");
                return 0;
            } else {
                out.write(c);
                return 1;
            }
        }
    }
}
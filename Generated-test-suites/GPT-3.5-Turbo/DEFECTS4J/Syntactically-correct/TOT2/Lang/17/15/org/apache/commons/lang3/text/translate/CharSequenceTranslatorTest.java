import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.junit.Test;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate() {
        CharSequenceTranslator translator = new MockCharSequenceTranslator();

        String input = "Hello World!";
        String expectedOutput = "Hello World!";
        String actualOutput = translator.translate(input);

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testTranslateWithNullInput() {
        CharSequenceTranslator translator = new MockCharSequenceTranslator();

        String actualOutput = translator.translate(null);

        assertNull(actualOutput);
    }

    @Test
    public void testInnerTranslate() throws IOException {
        CharSequenceTranslator translator = new MockCharSequenceTranslator();
        StringWriter writer = new StringWriter();

        translator.translate("Hello World!", writer);

        assertEquals("Hello World!", writer.toString());
    }

    @Test
    public void testTranslateWithIOException() {
        CharSequenceTranslator translator = new MockCharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                throw new IOException("IOException test case");
            }
        };

        try {
            translator.translate("Hello World!");
            fail("Expected IOException was not thrown");
        } catch (RuntimeException e) {
            assertTrue(e.getCause() instanceof IOException);
            assertEquals("IOException test case", e.getCause().getMessage());
        }
    }

    @Test
    public void testWithMethod() {
        MockCharSequenceTranslator translator1 = new MockCharSequenceTranslator();
        MockCharSequenceTranslator translator2 = new MockCharSequenceTranslator();
        CharSequenceTranslator mergedTranslator = translator1.with(translator2);

        assertNotNull(mergedTranslator);
        assertTrue(mergedTranslator instanceof AggregateTranslator);
    }

    @Test
    public void testHex() {
        int codepoint = 65;
        String expectedHex = "41";

        String actualHex = CharSequenceTranslator.hex(codepoint);

        assertEquals(expectedHex, actualHex);
    }

    private static class MockCharSequenceTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            out.write(input.subSequence(index, input.length()).toString());
            return 0;
        }
    }
}
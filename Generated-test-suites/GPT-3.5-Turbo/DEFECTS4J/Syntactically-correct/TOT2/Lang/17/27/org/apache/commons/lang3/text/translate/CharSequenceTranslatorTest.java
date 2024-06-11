import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CharSequenceTranslatorTest {

    private CharSequenceTranslator translator;

    @Before
    public void setUp() {
        translator = new MockTranslator();
    }

    @Test
    public void testTranslate() {
        String input = "abc";
        String expectedOutput = "def";
        String actualOutput = translator.translate(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testTranslateWithNullInput() {
        String actualOutput = translator.translate(null);
        assertNull(actualOutput);
    }

    @Test
    public void testTranslateWithIOException() {
        CharSequenceTranslator exceptionTranslator = new ExceptionTranslator();
        try {
            exceptionTranslator.translate("test");
        } catch (RuntimeException e) {
            assertTrue(true);
        } catch (IOException e) {
            assertTrue(false);
        }
    }

    @Test
    public void testWith() {
        CharSequenceTranslator translator2 = new MockTranslator();
        CharSequenceTranslator compositeTranslator = translator.with(translator2);
        assertEquals(compositeTranslator.getClass(), AggregateTranslator.class);
    }

    private static class MockTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) {
            try {
                out.write("def");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return 3;
        }
    }

    private static class ExceptionTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            throw new IOException("IOException");
        }
    }

}
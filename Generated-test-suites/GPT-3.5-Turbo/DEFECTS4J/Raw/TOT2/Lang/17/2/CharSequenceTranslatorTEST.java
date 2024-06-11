import static org.junit.Assert.*;
import org.junit.Test;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslateWithIndexAndWriter() {
        CharSequenceTranslator translator = new ConcreteTranslator();
        StringWriter writer = new StringWriter();
        StringBuilder input = new StringBuilder("abc");
        int index = 0;
        try {
            int consumed = translator.translate(input, index, writer);
            assertEquals(3, consumed);
            assertEquals("abc", writer.toString());
        } catch (IOException e) {
            fail("IOException should not occur");
        }
    }

    @Test
    public void testTranslateWithInputNull() {
        CharSequenceTranslator translator = new ConcreteTranslator();
        assertNull(translator.translate(null));
    }

    @Test
    public void testTranslateWithInputAndWriter() {
        CharSequenceTranslator translator = new ConcreteTranslator();
        StringWriter writer = new StringWriter();
        translator.translate(new StringBuilder("abc"), writer);
        assertEquals("abc", writer.toString());
    }

    @Test
    public void testWith() {
        CharSequenceTranslator firstTranslator = new ConcreteTranslator();
        CharSequenceTranslator secondTranslator = new ConcreteTranslator();
        CharSequenceTranslator mergedTranslator = firstTranslator.with(secondTranslator);
        assertTrue(mergedTranslator instanceof AggregateTranslator);
    }

    @Test
    public void testHex() {
        assertEquals("41", CharSequenceTranslator.hex(65));
    }

    private static class ConcreteTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            out.write(input.charAt(index));
            return 1;
        }
    }

}
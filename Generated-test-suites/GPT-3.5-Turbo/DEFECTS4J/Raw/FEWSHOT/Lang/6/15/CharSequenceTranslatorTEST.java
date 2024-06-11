import static org.junit.Assert.*;
import org.junit.Test;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate() {
        // Test with null input
        assertNull(translate(null));

        // Test with empty input
        assertEquals("", translate(""));

        // Test with non-null input
        assertEquals("hello", translate("hello"));
    }

    @Test
    public void testTranslate_withWriter() {
        // Test with null input and null writer
        assertEquals("", translate(null, null));

        // Test with null input and non-null writer
        assertEquals("", translate(null, new StringWriter()));

        // Test with empty input and null writer
        assertEquals("", translate("", null));

        // Test with empty input and non-null writer
        assertEquals("", translate("", new StringWriter()));

        // Test with non-null input and null writer
        assertEquals("hello", translate("hello", null));

        // Test with non-null input and non-null writer
        assertEquals("hello", translate("hello", new StringWriter()));
    }

    @Test
    public void testTranslate_withIndexAndWriter() {
        Writer writer = new StringWriter();

        // Test with null input, index 0 and null writer
        assertEquals(0, translate(null, 0, null));

        // Test with null input, index 0 and non-null writer
        assertEquals(0, translate(null, 0, writer));
        assertEquals("", writer.toString());

        // Test with empty input, index 0 and null writer
        assertEquals(0, translate("", 0, null));

        // Test with empty input, index 0 and non-null writer
        assertEquals(0, translate("", 0, writer));
        assertEquals("", writer.toString());

        // Test with non-null input, index 0 and null writer
        assertEquals(5, translate("hello", 0, null));

        // Test with non-null input, index 0 and non-null writer
        assertEquals(5, translate("hello", 0, writer));
        assertEquals("hello", writer.toString());
    }

    @Test
    public void testWith() {
        // Test with null translators
        assertNotNull(with(null));

        // Test with empty translators array
        assertNotNull(with(new CharSequenceTranslator[] {}));

        // Test with non-empty translators array
        assertNotNull(with(new CharSequenceTranslator[] { new MyTranslator() }));
    }

    @Test
    public void testHex() {
        // Test with 0
        assertEquals("0", hex(0));

        // Test with negative number
        assertEquals("FFFFFFFF", hex(-1));

        // Test with positive number
        assertEquals("1", hex(1));
    }

    private class MyTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) {
            return 0;
        }
    }

}
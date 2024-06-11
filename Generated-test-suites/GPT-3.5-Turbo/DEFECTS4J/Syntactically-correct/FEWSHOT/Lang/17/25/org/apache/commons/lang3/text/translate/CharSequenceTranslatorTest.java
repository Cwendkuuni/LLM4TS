import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

import org.junit.Test;

public class CharSequenceTranslatorTest {
    
    @Test
    public void testTranslate() {
        assertEquals("foo", new TestTranslator().translate("foo"));
        assertNull(new TestTranslator().translate(null));
    }
    
    @Test
    public void testTranslateWithWriter() throws IOException {
        StringWriter writer = new StringWriter();
        new TestTranslator().translate("foo", writer);
        assertEquals("foo", writer.toString());
    }

    @Test
    public void testTranslateWithIndexAndWriter() throws IOException {
        StringWriter writer = new StringWriter();
        assertEquals(3, new TestTranslator().translate("foo", 0, writer));
        assertEquals("foo", writer.toString());
        writer = new StringWriter();
        assertEquals(0, new TestTranslator().translate(null, 0, writer));
        assertEquals("", writer.toString());
    }
    
    @Test
    public void testHex() {
        assertEquals("41", CharSequenceTranslator.hex(65));
        assertEquals("7A", CharSequenceTranslator.hex(122));
    }
}

class TestTranslator extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        if (input == null) {
            return 0;
        }
        out.write(input.toString());
        return input.length();
    }
}
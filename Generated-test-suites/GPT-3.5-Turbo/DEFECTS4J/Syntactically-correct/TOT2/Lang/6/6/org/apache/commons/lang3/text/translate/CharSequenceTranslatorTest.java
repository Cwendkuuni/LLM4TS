import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.junit.Test;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                // Custom translation logic for testing
                return 0;
            }
        };

        try {
            String result = translator.translate("abc");
            assertNotNull(result);
            assertEquals("", result);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }

        try {
            String result = translator.translate(null);
            assertNull(result);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }

    @Test
    public void testTranslateWithWriter() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                out.write("test");
                return 4;
            }
        };

        StringWriter writer = new StringWriter();
        try {
            translator.translate("abc", writer);
            assertEquals("test", writer.toString());
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }

    @Test
    public void testHex() {
        assertEquals("41", CharSequenceTranslator.hex(65)); // Upper case A
        assertEquals("5A", CharSequenceTranslator.hex(90)); // Upper case Z
    }

}
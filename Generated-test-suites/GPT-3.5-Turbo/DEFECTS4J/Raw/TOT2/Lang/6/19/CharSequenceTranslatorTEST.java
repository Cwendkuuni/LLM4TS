import static org.junit.Assert.*;
import org.junit.Test;
import static org.mockito.Mockito.*;
import java.io.IOException;
import java.io.Writer;

public class CharSequenceTranslatorTest {

    private CharSequenceTranslator translator;

    @Test
    public void testTranslateWithWriter() throws IOException {
        translator = new ConcreteTranslator();
        StringWriter writer = new StringWriter();
        int result = translator.translate("test", 0, writer);
        assertEquals(4, result);
        assertEquals("test", writer.toString());
    }

    @Test
    public void testTranslateWithNullInput() {
        translator = new ConcreteTranslator();
        String result = translator.translate(null);
        assertNull(result);
    }

    @Test
    public void testTranslateWithNullWriter() {
        translator = new ConcreteTranslator();
        assertThrows(IllegalArgumentException.class, () -> translator.translate("test", null));
    }

    @Test
    public void testHex() {
        assertEquals("41", CharSequenceTranslator.hex(65));
        assertEquals("1F4A9", CharSequenceTranslator.hex(128169));
    }

    class ConcreteTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            out.write(input.toString());
            return input.length();
        }
    }

}
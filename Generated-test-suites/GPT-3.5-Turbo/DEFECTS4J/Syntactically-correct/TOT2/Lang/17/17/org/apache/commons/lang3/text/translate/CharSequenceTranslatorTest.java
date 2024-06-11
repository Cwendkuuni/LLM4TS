import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate() {
        CharSequenceTranslator translator = new ConcreteCharSequenceTranslator();
        String input = "abc";
        String expected = "abc";
        assertEquals(expected, translator.translate(input));
    }

    @Test
    public void testTranslateWithNullInput() {
        CharSequenceTranslator translator = new ConcreteCharSequenceTranslator();
        assertNull(translator.translate(null));
    }

    @Test
    public void testTranslateWithIOException() {
        CharSequenceTranslator translator = new ConcreteCharSequenceTranslator();
        assertThrows(RuntimeException.class, () -> translator.translate("abc"));
    }

    @Test
    public void testTranslateToWriter() {
        CharSequenceTranslator translator = new ConcreteCharSequenceTranslator();
        String input = "abc";
        StringWriter writer = new StringWriter();
        translator.translate(input, writer);
        assertEquals("abc", writer.toString());
    }

    @Test
    public void testTranslateToWriterWithNullInput() {
        CharSequenceTranslator translator = new ConcreteCharSequenceTranslator();
        StringWriter writer = new StringWriter();
        translator.translate(null, writer);
        assertEquals("", writer.toString());
    }

    @Test
    public void testWith() {
        CharSequenceTranslator translator1 = new ConcreteCharSequenceTranslator();
        CharSequenceTranslator translator2 = new ConcreteCharSequenceTranslator();
        CharSequenceTranslator combined = translator1.with(translator2);
        assertNotNull(combined);
    }

    @Test
    public void testHex() {
        assertEquals("41", CharSequenceTranslator.hex(65));
    }
}
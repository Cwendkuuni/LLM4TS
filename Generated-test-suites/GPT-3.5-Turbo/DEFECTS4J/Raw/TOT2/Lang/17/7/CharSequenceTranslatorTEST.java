import org.junit.Test;
import static org.junit.Assert.*;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate() {
        CharSequenceTranslator translator = new ConcreteCharSequenceTranslator();
        assertEquals("abc", translator.translate("abc"));
        assertEquals("123", translator.translate("123"));
        assertNull(translator.translate(null));
    }

    @Test
    public void testTranslateWithIndexAndWriter() {
        CharSequenceTranslator translator = new ConcreteCharSequenceTranslator();
        StringWriter writer = new StringWriter();
        try {
            int consumed = translator.translate("abc123", 0, writer);
            assertEquals(6, consumed);
            assertEquals("abc123", writer.toString());
        } catch (IOException e) {
            fail("IOException should not be thrown here");
        }
    }

    @Test
    public void testWithMethod() {
        CharSequenceTranslator translator = new ConcreteCharSequenceTranslator();
        CharSequenceTranslator otherTranslator = new OtherConcreteCharSequenceTranslator();
        CharSequenceTranslator result = translator.with(otherTranslator);
        assertNotNull(result);
    }

    @Test
    public void testHexMethod() {
        assertEquals("41", CharSequenceTranslator.hex(65));
        assertEquals("2A", CharSequenceTranslator.hex(42));
    }
}
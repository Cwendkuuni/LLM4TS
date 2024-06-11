import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.io.StringWriter;

public class LookupTranslatorTest {

    @Test
    public void testTranslate_withValidInput_shouldTranslateSuccessfully() throws IOException {
        CharSequence[][] lookup = {{"ab", "xy"}, {"cd", "ef"}, {"gh", "ij"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter writer = new StringWriter();

        int translatedLength = translator.translate("abcdefgh", 0, writer);

        assertEquals("xyijklmno", writer.toString());
        assertEquals(2, translatedLength);
    }

    @Test
    public void testTranslate_withEmptyInput_shouldNotTranslate() throws IOException {
        CharSequence[][] lookup = {{"ab", "xy"}, {"cd", "ef"}, {"gh", "ij"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter writer = new StringWriter();

        int translatedLength = translator.translate("", 0, writer);

        assertEquals("", writer.toString());
        assertEquals(0, translatedLength);
    }

    @Test
    public void testTranslate_withNullInput_shouldNotTranslate() throws IOException {
        CharSequence[][] lookup = {{"ab", "xy"}, {"cd", "ef"}, {"gh", "ij"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter writer = new StringWriter();

        int translatedLength = translator.translate(null, 0, writer);

        assertEquals("", writer.toString());
        assertEquals(0, translatedLength);
    }

    @Test
    public void testTranslate_withInvalidIndex_shouldNotTranslate() throws IOException {
        CharSequence[][] lookup = {{"ab", "xy"}, {"cd", "ef"}, {"gh", "ij"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter writer = new StringWriter();

        int translatedLength = translator.translate("abcdefgh", 10, writer);

        assertEquals("", writer.toString());
        assertEquals(0, translatedLength);
    }

    @Test
    public void testTranslate_withNoMatch_shouldNotTranslate() throws IOException {
        CharSequence[][] lookup = {{"ab", "xy"}, {"cd", "ef"}, {"gh", "ij"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter writer = new StringWriter();

        int translatedLength = translator.translate("klmnopqr", 0, writer);

        assertEquals("klmnopqr", writer.toString());
        assertEquals(0, translatedLength);
    }

    @Test
    public void testTranslate_withSingleCharacterMatch_shouldTranslateSuccessfully() throws IOException {
        CharSequence[][] lookup = {{"a", "x"}, {"b", "y"}, {"c", "z"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter writer = new StringWriter();

        int translatedLength = translator.translate("abc", 0, writer);

        assertEquals("xyz", writer.toString());
        assertEquals(1, translatedLength);
    }
  
}
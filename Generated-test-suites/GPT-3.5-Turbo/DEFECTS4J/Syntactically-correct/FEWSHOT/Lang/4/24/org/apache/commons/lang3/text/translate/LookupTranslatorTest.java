import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.io.StringWriter;
import org.junit.Test;

public class LookupTranslatorTest {

    @Test
    public void testConstructorWithLookupTable() {
        CharSequence[][] lookup = {{"a", "1"}, {"b", "2"}, {"c", "3"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter writer = new StringWriter();
        try {
            translator.translate("abc", 0, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals("123", writer.toString());
    }

    @Test
    public void testConstructorWithNullLookupTable() {
        LookupTranslator translator = new LookupTranslator((CharSequence[][])(null));
        StringWriter writer = new StringWriter();
        try {
            translator.translate("abc", 0, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals("abc", writer.toString());
    }

    @Test
    public void testTranslateWithValidInput() {
        CharSequence[][] lookup = {{"a", "1"}, {"b", "2"}, {"c", "3"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter writer = new StringWriter();
        try {
            translator.translate("abc", 0, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals("123", writer.toString());
    }

    @Test
    public void testTranslateWithInvalidInput() {
        CharSequence[][] lookup = {{"a", "1"}, {"b", "2"}, {"c", "3"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter writer = new StringWriter();
        try {
            translator.translate("def", 0, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals("def", writer.toString());
    }

    @Test
    public void testTranslateWithNullInput() {
        CharSequence[][] lookup = {{"a", "1"}, {"b", "2"}, {"c", "3"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter writer = new StringWriter();
        try {
            translator.translate(null, 0, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals("", writer.toString());
    }
}
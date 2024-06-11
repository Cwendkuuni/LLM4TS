import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class LookupTranslatorTest {

    @Test
    public void testTranslate() throws IOException {
        LookupTranslator translator = createTranslator();

        assertEquals("", translate(translator, ""));
        assertEquals("A", translate(translator, "A"));
        assertEquals("B", translate(translator, "B"));
        assertEquals("AB", translate(translator, "AB"));
        assertEquals("BA", translate(translator, "BA"));
        assertEquals("ABCD", translate(translator, "ABCD"));
    }

    @Test
    public void testTranslate_LongestSequence() throws IOException {
        LookupTranslator translator = createTranslator("AB", "CD", "EFG");

        assertEquals("AB", translate(translator, "AB"));
        assertEquals("ABCD", translate(translator, "ABCD"));
        assertEquals("EFGAB", translate(translator, "EFGAB"));
    }

    @Test
    public void testTranslate_IgnoreCase() throws IOException {
        LookupTranslator translator = createTranslator("a", "A");

        assertEquals("A", translate(translator, "a"));
        assertEquals("A", translate(translator, "A"));
    }

    @Test
    public void testTranslate_NoMatch() throws IOException {
        LookupTranslator translator = createTranslator("ABC", "DEF");

        assertEquals("", translate(translator, "XYZ"));
        assertEquals("", translate(translator, "PQR"));
    }

    private LookupTranslator createTranslator(CharSequence[]... lookup) {
        return new LookupTranslator(lookup);
    }

    private String translate(LookupTranslator translator, String input) throws IOException {
        StringWriter writer = new StringWriter();
        translator.translate(input, 0, writer);
        return writer.toString();
    }
}
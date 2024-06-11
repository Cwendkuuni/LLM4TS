import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class LookupTranslatorTest {

    private LookupTranslator translator;

    @Before
    public void setup() {
        CharSequence[] lookup1 = { "a", "1" };
        CharSequence[] lookup2 = { "ab", "12" };
        CharSequence[] lookup3 = { "abc", "123" };
        CharSequence[][] lookup = { lookup1, lookup2, lookup3 };
        translator = new LookupTranslator(lookup);
    }

    @Test
    public void testTranslate_oneCharLookup_HappyPath() throws IOException {
        StringWriter writer = new StringWriter();
        int result = translator.translate("a", 0, writer);
        assertEquals("1", writer.toString());
        assertEquals(1, result);
    }

    @Test
    public void testTranslate_multipleCharLookup_HappyPath() throws IOException {
        StringWriter writer = new StringWriter();
        int result = translator.translate("abc", 0, writer);
        assertEquals("123", writer.toString());
        assertEquals(3, result);
    }

    @Test
    public void testTranslate_emptyInput_NoTranslation() throws IOException {
        StringWriter writer = new StringWriter();
        int result = translator.translate("", 0, writer);
        assertEquals("", writer.toString());
        assertEquals(0, result);
    }

    @Test
    public void testTranslate_noMatch_NoTranslation() throws IOException {
        StringWriter writer = new StringWriter();
        int result = translator.translate("def", 0, writer);
        assertEquals("", writer.toString());
        assertEquals(0, result);
    }

    @Test
    public void testTranslate_partialInput_MatchesLongestTranslation() throws IOException {
        StringWriter writer = new StringWriter();
        int result = translator.translate("abcdefg", 0, writer);
        assertEquals("123", writer.toString());
        assertEquals(3, result);
    }

    @Test
    public void testTranslate_endOfString_HasPartialMatch() throws IOException {
        StringWriter writer = new StringWriter();
        int result = translator.translate("xyzab", 3, writer);
        assertEquals("123", writer.toString());
        assertEquals(3, result);
    }

    @Test
    public void testTranslate_errorInWriter_Exception() throws IOException {
        StringWriter writer = new StringWriter() {
            @Override
            public void write(String str) throws IOException {
                throw new IOException("Error in writer");
            }
        };
        try {
            translator.translate("abc", 0, writer);
        } catch (IOException e) {
            assertEquals("Error in writer", e.getMessage());
        }
    }
}
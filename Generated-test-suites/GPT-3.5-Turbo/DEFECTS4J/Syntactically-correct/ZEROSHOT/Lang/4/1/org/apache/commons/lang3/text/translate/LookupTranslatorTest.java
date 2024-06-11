import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.io.StringWriter;
import org.apache.commons.lang3.text.translate.LookupTranslator;

public class LookupTranslatorTest {

    @Test
    public void testTranslate() throws IOException {
        LookupTranslator translator = new LookupTranslator();
        String input = "abcdefg";
        StringWriter output = new StringWriter();
        int index = 0;
        int result = translator.translate(input, index, output);
        assertEquals("Translate does not return expected result.", 0, result);
        assertEquals("Output is not empty when no translation is found.", "", output.toString());
    }

    @Test
    public void testTranslateWithLookup() throws IOException {
        CharSequence[] sequence1 = { "abc", "def" };
        CharSequence[] sequence2 = { "def", "ghi" };
        LookupTranslator translator = new LookupTranslator(sequence1, sequence2);
        String input = "abcdef";
        StringWriter output = new StringWriter();
        int index = 0;
        int result = translator.translate(input, index, output);
        assertEquals("Translate does not return expected result.", 3, result);
        assertEquals("Output is not correct after translation.", "ghi", output.toString());
    }

    @Test
    public void testTranslateWithLargeLookupMap() throws IOException {
        CharSequence[] sequence1 = { "abc", "def" };
        CharSequence[] sequence2 = { "def", "ghi" };
        CharSequence[] sequence3 = { "ghi", "jkl" };
        CharSequence[] sequence4 = { "jkl", "mno" };
        LookupTranslator translator = new LookupTranslator(sequence1, sequence2, sequence3, sequence4);
        String input = "abcdefghijklmno";
        StringWriter output = new StringWriter();
        int index = 0;
        int result = translator.translate(input, index, output);
        assertEquals("Translate does not return expected result.", 12, result);
        assertEquals("Output is not correct after translation.", "mnopqr", output.toString());
    }
}
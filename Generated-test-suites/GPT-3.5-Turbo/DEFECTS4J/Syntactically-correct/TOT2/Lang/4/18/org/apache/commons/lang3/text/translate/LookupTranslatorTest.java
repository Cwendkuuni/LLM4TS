import org.junit.Test;
import static org.junit.Assert.*;

public class LookupTranslatorTest {

    @Test
    public void testTranslate_basicLookup() {
        LookupTranslator translator = new LookupTranslator(
                new String[][]{{"foo", "bar"}}
        );

        StringBuilder output = new StringBuilder();
        translator.translate("foo", 0, output);

        assertEquals("bar", output.toString());
    }

    @Test
    public void testTranslate_multipleEntries() {
        LookupTranslator translator = new LookupTranslator(
                new String[][]{{"hello", "world"}, {"foo", "bar"}}
        );

        StringBuilder output = new StringBuilder();
        translator.translate("hello foo", 0, output);

        assertEquals("world bar", output.toString());
    }

    @Test
    public void testTranslate_missingEntry() {
        LookupTranslator translator = new LookupTranslator(
                new String[][]{{"foo", "bar"}}
        );

        StringBuilder output = new StringBuilder();
        translator.translate("hello", 0, output);

        assertEquals(0, output.length());
    }

    // Add more test cases for each method as needed

}
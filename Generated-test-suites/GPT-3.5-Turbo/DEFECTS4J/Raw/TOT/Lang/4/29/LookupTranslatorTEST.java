
import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.*;

public class LookupTranslatorTest {

    @Test
    public void testTranslate_typical() throws IOException {
        CharSequence[] lookup = {
                new String[]{"apple", "fruit"},
                new String[]{"orange", "fruit"},
                new String[]{"car", "vehicle"},
                new String[]{"dog", "animal"}
        };
        LookupTranslator lookupTranslator = new LookupTranslator(lookup);
        StringWriter writer = new StringWriter();

        int numCharsTranslated = lookupTranslator.translate("I have an apple, orange, car, and dog", 0, writer);

        assertEquals(4, numCharsTranslated);
        assertEquals("I have an fruit, fruit, vehicle, and animal", writer.toString());
    }

    @Test
    public void testTranslate_edge() throws IOException {
        CharSequence[] lookup = {
                new String[]{"apple", "fruit"},
                new String[]{"orange", "fruit"},
                new String[]{"car", "vehicle"},
                new String[]{"dog", "animal"}
        };
        LookupTranslator lookupTranslator = new LookupTranslator(lookup);
        StringWriter writer = new StringWriter();

        int numCharsTranslated = lookupTranslator.translate("I have an apple, orange, car, and dog", 36, writer);

        assertEquals(3, numCharsTranslated);
        assertEquals("and animal", writer.toString());
    }

    @Test
    public void testTranslate_error() throws IOException {
        CharSequence[] lookup = {
                new String[]{"apple", "fruit"},
                new String[]{"orange", "fruit"},
                new String[]{"car", "vehicle"},
                new String[]{"dog", "animal"}
        };
        LookupTranslator lookupTranslator = new LookupTranslator(lookup);
        StringWriter writer = new StringWriter();

        int numCharsTranslated = lookupTranslator.translate("I have an apple, orange, car, and dog", 10, writer);

        assertEquals(0, numCharsTranslated);
        assertEquals("", writer.toString());
    }

}

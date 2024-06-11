import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;

import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.junit.Test;

public class NumericEntityUnescaperTest {
    
    private NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
    
    @Test
    public void testTranslate() throws IOException {
        // Test hexadecimal entity value
        StringWriter writer = new StringWriter();
        CharSequenceTranslator.translate("&#x41;", 0, writer);
        assertEquals("A", writer.toString());

        // Test decimal entity value
        writer = new StringWriter();
        CharSequenceTranslator.translate("&#65;", 0, writer);
        assertEquals("A", writer.toString());

        // Test multiple entity values
        writer = new StringWriter();
        CharSequenceTranslator.translate("&#65;&#x41;", 0, writer);
        assertEquals("AA", writer.toString());

        // Test invalid entity value
        writer = new StringWriter();
        CharSequenceTranslator.translate("&#invalid;", 0, writer);
        assertEquals("", writer.toString());

        // Test entity value at the beginning of the string
        writer = new StringWriter();
        CharSequenceTranslator.translate("&#x41;text", 0, writer);
        assertEquals("Atext", writer.toString());

        // Test entity value at the end of the string
        writer = new StringWriter();
        CharSequenceTranslator.translate("text&#x41;", 4, writer);
        assertEquals("textA", writer.toString());

        // Test entity value in the middle of the string
        writer = new StringWriter();
        CharSequenceTranslator.translate("text&#x41;text", 4, writer);
        assertEquals("textAtext", writer.toString());
    }

}
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.StringWriter;

public class LookupTranslatorTest {

    @Test
    public void testTranslate() throws Exception {
        LookupTranslator translator = new LookupTranslator(new String[]{"foo", "bar"});
        StringWriter writer = new StringWriter();
        
        // Typical use case
        int result = translator.translate("foo", 0, writer);
        assertEquals(3, result);
        assertEquals("bar", writer.toString());
        
        writer.getBuffer().setLength(0); // Clear the writer
        
        // Edge case: input longer than the longest lookup value
        result = translator.translate("foobar", 0, writer);
        assertEquals(3, result); // Should match "foo" and output "bar"
        assertEquals("barbar", writer.toString()); // "bar" is replaced twice
        
        writer.getBuffer().setLength(0); // Clear the writer
        
        // Edge case: input containing multiple lookup values
        result = translator.translate("foofoo", 0, writer);
        assertEquals(3, result); // Should match first "foo" and output "bar"
        assertEquals("barfoo", writer.toString()); // "bar" is replaced once

        writer.getBuffer().setLength(0); // Clear the writer
        
        // Error scenario: input not found in the lookup table
        result = translator.translate("abc", 0, writer);
        assertEquals(0, result); // No match found
        assertEquals("", writer.toString()); // No output
        
    }
}
package org.apache.commons.lang3.text.translate;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.StringWriter;
import org.apache.commons.lang3.text.translate.LookupTranslator;

public class LookupTranslatorTest {

    @Test
    public void testTranslate() {
        LookupTranslator translator = new LookupTranslator(
            new CharSequence[]{"foo", "bar"},
            new CharSequence[]{"hello", "world"}
        );
        
        StringWriter writer = new StringWriter();
        
        try {
            int result = translator.translate("foo hello", 0, writer);
            assertEquals(3, result);
            assertEquals("bar world", writer.toString());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testTranslateEdgeCases() {
        LookupTranslator translator = new LookupTranslator(
            new CharSequence[]{"abcdef", "123"},
            new CharSequence[]{"ghijkl", "456"}
        );
        
        StringWriter writer = new StringWriter();
        
        try {
            // Testing with empty input
            int result1 = translator.translate("", 0, writer);
            assertEquals(0, result1);
            assertEquals("", writer.toString());
            
            // Testing with input longer than lookup
            int result2 = translator.translate("abcdefghijk", 0, writer);
            assertEquals(6, result2);
            assertEquals("123kl", writer.toString());
            
            // Testing with non-matching input
            int result3 = translator.translate("xyz", 0, writer);
            assertEquals(0, result3);
            assertEquals("", writer.toString());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
}
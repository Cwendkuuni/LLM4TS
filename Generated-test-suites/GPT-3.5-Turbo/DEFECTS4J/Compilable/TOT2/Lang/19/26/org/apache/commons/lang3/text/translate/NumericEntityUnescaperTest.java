package org.apache.commons.lang3.text.translate;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import java.io.StringWriter;
import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_basicDecoding() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        try {
            int result = unescaper.translate("&#65;", 0, writer);
            assertEquals(5, result); // Check if the translation was successful
            assertEquals("A", writer.toString()); // Check if the character 'A' was decoded
        } catch (Exception e) {
            fail("Exception thrown when decoding basic numeric entity: " + e.getMessage());
        }

    }

    @Test
    public void testTranslate_noSemicolon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        try {
            int result = unescaper.translate("&#65", 0, writer);
            assertEquals(4, result); // Check if the translation was successful without the semicolon
            assertEquals("A", writer.toString()); // Check if the character 'A' was decoded
        } catch (Exception e) {
            fail("Exception thrown when decoding numeric entity without semicolon: " + e.getMessage());
        }

    }

    @Test
    public void testTranslate_hexadecimal() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        try {
            int result = unescaper.translate("&#x41;", 0, writer);
            assertEquals(6, result); // Check if the translation of hexadecimal entity was successful
            assertEquals("A", writer.toString()); // Check if the character 'A' was decoded
        } catch (Exception e) {
            fail("Exception thrown when decoding hexadecimal numeric entity: " + e.getMessage());
        }

    }

    @Test
    public void testTranslate_largeCodePoint() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        try {
            int result = unescaper.translate("&#100000;", 0, writer);
            assertEquals(10, result); // Check if the translation of large Unicode code point was successful
            assertEquals("\uD869\uDED6", writer.toString()); // Check if the correct characters were decoded
        } catch (Exception e) {
            fail("Exception thrown when decoding large Unicode code point: " + e.getMessage());
        }

    }

    @Test
    public void testTranslate_invalidNumericEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        try {
            int result = unescaper.translate("&#invalid;", 0, writer);
            assertEquals(0, result); // Check if the translation fails for an invalid numeric entity
        } catch (Exception e) {
            fail("Unexpected exception thrown when decoding invalid numeric entity: " + e.getMessage());
        }

    }
}
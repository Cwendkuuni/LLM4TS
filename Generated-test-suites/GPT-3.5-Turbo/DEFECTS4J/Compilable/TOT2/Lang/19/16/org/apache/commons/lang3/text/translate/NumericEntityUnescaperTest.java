package org.apache.commons.lang3.text.translate;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import java.io.StringWriter;
import java.io.IOException;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslateWithNumericEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        try {
            int result = unescaper.translate("&#65;", 0, writer);
            assertEquals(3, result);
            assertEquals("A", writer.toString());
        } catch (IOException e) {
            fail("IOException should not be thrown.");
        }
    }

    @Test
    public void testTranslateWithHexEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        try {
            int result = unescaper.translate("&#x41;", 0, writer);
            assertEquals(4, result);
            assertEquals("A", writer.toString());
        } catch (IOException e) {
            fail("IOException should not be thrown.");
        }
    }

    @Test
    public void testTranslateWithSemicolon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        try {
            int result = unescaper.translate("&#65;", 0, writer);
            assertEquals(3, result);
            assertEquals("A", writer.toString());
        } catch (IOException e) {
            fail("IOException should not be thrown.");
        }
    }

    // Test edge case: Maximum entity value
    @Test
    public void testTranslateWithMaxEntityValue() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        try {
            int result = unescaper.translate("&#65535;", 0, writer);
            assertEquals(7, result);
            assertEquals(Character.toString(Character.toChars(65535)[0]) + Character.toString(Character.toChars(65535)[1]), writer.toString());
        } catch (IOException e) {
            fail("IOException should not be thrown.");
        }
    }

    // Test exception handling: Invalid entity format
    @Test
    public void testTranslateWithInvalidEntityFormat() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        try {
            int result = unescaper.translate("&A;", 0, writer);
            assertEquals(0, result);
            assertEquals("", writer.toString());
        } catch (IOException e) {
            fail("IOException should not be thrown.");
        }
    }
}
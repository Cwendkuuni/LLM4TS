package org.apache.commons.lang3.text.translate;
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.StringWriter;
import java.io.IOException;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslateValidNumericEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        try {
            int translated = unescaper.translate("&#65;", 0, writer);
            assertEquals(4, translated);
            assertEquals("A", writer.toString());
        } catch (IOException e) {
            fail("IOException should not occur");
        }
    }

    @Test
    public void testTranslateInvalidNumericEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        try {
            int translated = unescaper.translate("&#", 0, writer);
            assertEquals(0, translated);
            assertEquals("", writer.toString());
        } catch (IOException e) {
            fail("IOException should not occur");
        }
    }

    @Test
    public void testTranslateHexadecimalEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        try {
            int translated = unescaper.translate("&#x41;", 0, writer);
            assertEquals(6, translated);
            assertEquals("A", writer.toString());
        } catch (IOException e) {
            fail("IOException should not occur");
        }
    }

    @Test
    public void testTranslateInvalidHexadecimalEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        try {
            int translated = unescaper.translate("&#xG;", 0, writer);
            assertEquals(0, translated);
            assertEquals("", writer.toString());
        } catch (IOException e) {
            fail("IOException should not occur");
        }
    }
}
package org.apache.commons.lang3.text.translate;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        try {
            int result = unescaper.translate("&#65;", 0, writer);
            assertEquals('A', writer.toString().charAt(0));
            assertEquals(5, result);

        } catch (IOException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testTranslateWithHexEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        try {
            int result = unescaper.translate("&#x41;", 0, writer);
            assertEquals('A', writer.toString().charAt(0));
            assertEquals(5, result);

        } catch (IOException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testTranslateOutOfBoundsIndex() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        try {
            int result = unescaper.translate("&#65;", 1, writer);
            assertEquals(0, result);

        } catch (IOException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testTranslateInvalidNumericEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        try {
            int result = unescaper.translate("&#invalid;", 0, writer);
            assertEquals(0, result);

        } catch (IOException e) {
            fail("Exception thrown");
        }
    }
}
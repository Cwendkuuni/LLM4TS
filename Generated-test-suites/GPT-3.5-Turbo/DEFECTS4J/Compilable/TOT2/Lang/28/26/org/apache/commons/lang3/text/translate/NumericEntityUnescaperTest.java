package org.apache.commons.lang3.text.translate;
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.StringWriter;
import java.io.IOException;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_BasicNumericEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        int result = unescaper.translate("&#65;", 0, writer);

        assertEquals(2, result);
        assertEquals('A', writer.toString().charAt(0));
    }

    @Test
    public void testTranslate_HexNumericEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        int result = unescaper.translate("&#x41;", 0, writer);

        assertEquals(3, result);
        assertEquals('A', writer.toString().charAt(0));
    }

    @Test
    public void testTranslate_InvalidNumericEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        int result = unescaper.translate("&#invalid;", 0, writer);

        assertEquals(0, result);
        assertEquals(0, writer.toString().length());
    }

    @Test
    public void testTranslate_OutOfBounds() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        // Simulate ArrayIndexOutOfBoundsException
        int result = unescaper.translate("&#65;", 10, writer);

        assertEquals(0, result);
        assertEquals(0, writer.toString().length());
    }
}
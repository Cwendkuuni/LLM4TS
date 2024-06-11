package org.apache.commons.lang3.text.translate;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.StringWriter;
import java.io.Writer;

public class NumericEntityUnescaperTest {

    private NumericEntityUnescaper unescaper;
    private Writer writer;

    @Before
    public void setUp() {
        unescaper = new NumericEntityUnescaper();
        writer = new StringWriter();
    }

    @Test
    public void testTranslate() throws Exception {
        String input = "&#65;";
        int result = unescaper.translate(input, 0, writer);
        String output = writer.toString();
        assertEquals(5, result);
        assertEquals("A", output);
    }

    @Test
    public void testTranslateWithHexadecimal() throws Exception {
        String input = "&#x41;";
        int result = unescaper.translate(input, 0, writer);
        String output = writer.toString();
        assertEquals(7, result);
        assertEquals("A", output);
    }

    @Test
    public void testTranslateWithoutSemicolon() throws Exception {
        String input = "&#65";
        int result = unescaper.translate(input, 0, writer);
        String output = writer.toString();
        assertEquals(4, result);
        assertEquals("A", output);
    }

    @Test
    public void testTranslateWithInvalidNumberFormat() throws Exception {
        String input = "&#INVALID;";
        int result = unescaper.translate(input, 0, writer);
        String output = writer.toString();
        assertEquals(0, result);
        assertEquals("", output);
    }

    @Test
    public void testTranslateWithInvalidHexadecimalFormat() throws Exception {
        String input = "&#xINVALID;";
        int result = unescaper.translate(input, 0, writer);
        String output = writer.toString();
        assertEquals(0, result);
        assertEquals("", output);
    }

    @Test
    public void testTranslateWithEntityValueGreaterThanFFFF() throws Exception {
        String input = "&#65536;";
        int result = unescaper.translate(input, 0, writer);
        String output = writer.toString();
        assertEquals(9, result);
        assertEquals("\uD800\uDC00", output);
    }

    @Test
    public void testTranslateWithEntityValueLowerThanFFFF() throws Exception {
        String input = "&#64;";
        int result = unescaper.translate(input, 0, writer);
        String output = writer.toString();
        assertEquals(4, result);
        assertEquals("@", output);
    }

}
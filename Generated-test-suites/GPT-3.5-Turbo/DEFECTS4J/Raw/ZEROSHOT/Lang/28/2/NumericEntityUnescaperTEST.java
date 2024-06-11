import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.Before;
import org.junit.Test;

public class NumericEntityUnescaperTest {

    private NumericEntityUnescaper unescaper;

    @Before
    public void setUp() {
        unescaper = new NumericEntityUnescaper();
    }

    @Test
    public void testTranslate_ValidInput_ReturnsTranslated() throws IOException {
        String input = "&#65;";
        StringWriter writer = new StringWriter();
        int index = 0;
        int result = unescaper.translate(input, index, writer);
        assertEquals(4, result);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_InvalidInput_ReturnsZero() throws IOException {
        String input = "&#A;";
        StringWriter writer = new StringWriter();
        int index = 0;
        int result = unescaper.translate(input, index, writer);
        assertEquals(0, result);
    }

    @Test
    public void testTranslate_HexadecimalInput_ReturnsTranslated() throws IOException {
        String input = "&#x41;";
        StringWriter writer = new StringWriter();
        int index = 0;
        int result = unescaper.translate(input, index, writer);
        assertEquals(5, result);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_EntityAtTheEnd_ReturnsTranslated() throws IOException {
        String input = "&#65;;";
        StringWriter writer = new StringWriter();
        int index = 0;
        int result = unescaper.translate(input, index, writer);
        assertEquals(5, result);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_NoEntity_ReturnsZero() throws IOException {
        String input = "";
        StringWriter writer = new StringWriter();
        int index = 0;
        int result = unescaper.translate(input, index, writer);
        assertEquals(0, result);
    }

}
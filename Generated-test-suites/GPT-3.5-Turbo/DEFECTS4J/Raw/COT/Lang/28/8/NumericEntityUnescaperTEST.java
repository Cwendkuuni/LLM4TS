// Generated test cases for NumericEntityUnescaper

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.StringWriter;
import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() throws Exception {
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        String input = "&#65;";
        StringWriter out = new StringWriter();
        int result = translator.translate(input, 0, out);
        assertEquals(5, result);
        assertEquals("A", out.toString());
    }

    @Test
    public void testTranslateWithInvalidInput() throws Exception {
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        String input = "&#invalid;";
        StringWriter out = new StringWriter();
        int result = translator.translate(input, 0, out);
        assertEquals(0, result);
        assertEquals("", out.toString());
    }

    @Test
    public void testTranslateWithEmptyInput() throws Exception {
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        String input = "";
        StringWriter out = new StringWriter();
        int result = translator.translate(input, 0, out);
        assertEquals(0, result);
        assertEquals("", out.toString());
    }

    @Test
    public void testTranslateWithHexadecimalEntity() throws Exception {
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        String input = "&#x41;";
        StringWriter out = new StringWriter();
        int result = translator.translate(input, 0, out);
        assertEquals(6, result);
        assertEquals("A", out.toString());
    }

    @Test
    public void testTranslateWithoutClosingSemicolon() throws Exception {
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        String input = "&#65";
        StringWriter out = new StringWriter();
        int result = translator.translate(input, 0, out);
        assertEquals(3, result);
        assertEquals("", out.toString());
    }
}


import static org.junit.Assert.*;
import java.io.IOException;
import java.io.StringWriter;
import org.junit.Test;
import org.apache.commons.lang3.text.translate.*;
import java.util.Arrays;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_NormalCase() throws IOException {
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        String input = "&#65;";
        int index = 0;

        int result = translator.translate(input, index, writer);

        assertEquals(5, result);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_NoSemicolon() throws IOException {
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        String input = "&#65";
        int index = 0;

        int result = translator.translate(input, index, writer);

        assertEquals(4, result);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_HexadecimalCode() throws IOException {
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        String input = "&#x41;";
        int index = 0;

        int result = translator.translate(input, index, writer);

        assertEquals(6, result);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_InvalidInput() throws IOException {
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        String input = "&#invalid;";
        int index = 0;

        int result = translator.translate(input, index, writer);

        assertEquals(0, result);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_LargeCharacterCode() throws IOException {
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        String input = "&#65536;";
        int index = 0;

        int result = translator.translate(input, index, writer);

        assertEquals(9, result);
        assertEquals("𐀀", writer.toString());
    }
}
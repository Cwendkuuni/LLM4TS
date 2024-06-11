import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_NormalDecimalNumericEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&#65;";
        StringBuilder output = new StringBuilder();
        unescaper.translate(input, 0, output);
        assertEquals("A", output.toString());
    }

    @Test
    public void testTranslate_HexadecimalNumericEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&#x41;";
        StringBuilder output = new StringBuilder();
        unescaper.translate(input, 0, output);
        assertEquals("A", output.toString());
    }

    @Test
    public void testTranslate_InvalidNumericEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&#invalid;";
        StringBuilder output = new StringBuilder();
        unescaper.translate(input, 0, output);
        assertEquals("", output.toString());
    }

    @Test
    public void testTranslate_EmptyStringInput() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "";
        StringBuilder output = new StringBuilder();
        unescaper.translate(input, 0, output);
        assertEquals("", output.toString());
    }

    @Test
    public void testTranslate_NULLInput() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = null;
        StringBuilder output = new StringBuilder();
        unescaper.translate(input, 0, output);
        assertEquals("", output.toString());
    }

}
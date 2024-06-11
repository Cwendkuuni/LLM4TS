import org.junit.Test;

import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_withDecimalEntity() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder();
        input.append("Test&#65;");
        StringBuilder output = new StringBuilder();
        unescaper.translate(input, 4, output);
        assertEquals("TestA", output.toString());
    }

    @Test
    public void testTranslate_withHexEntity() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder();
        input.append("Test&#x41;");
        StringBuilder output = new StringBuilder();
        unescaper.translate(input, 4, output);
        assertEquals("TestA", output.toString());
    }

    @Test
    public void testTranslate_withoutEntity() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder();
        input.append("Test");
        StringBuilder output = new StringBuilder();
        unescaper.translate(input, 0, output);
        assertEquals("", output.toString());
    }

    @Test
    public void testTranslate_withInvalidEntity() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder();
        input.append("Test&#X41;");
        StringBuilder output = new StringBuilder();
        unescaper.translate(input, 4, output);
        assertEquals("", output.toString());
    }
}
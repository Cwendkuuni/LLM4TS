import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder("&#65;");
        StringBuilder expectedOutput = new StringBuilder("A");

        StringBuilder output = new StringBuilder();
        try {
            unescaper.translate(input, 0, output);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }

        assertEquals(expectedOutput.toString(), output.toString());
    }

    @Test
    public void testTranslateWithHexEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder("&#x41;");
        StringBuilder expectedOutput = new StringBuilder("A");

        StringBuilder output = new StringBuilder();
        try {
            unescaper.translate(input, 0, output);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }

        assertEquals(expectedOutput.toString(), output.toString());
    }

    @Test
    public void testTranslateWithUnknownEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder("&#999999;");
        StringBuilder expectedOutput = new StringBuilder();

        StringBuilder output = new StringBuilder();
        try {
            unescaper.translate(input, 0, output);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }

        assertEquals(expectedOutput.toString(), output.toString());
    }

    @Test
    public void testTranslateWithInvalidHexEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder("&#xG;");
        StringBuilder expectedOutput = new StringBuilder();

        StringBuilder output = new StringBuilder();
        try {
            unescaper.translate(input, 0, output);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }

        assertEquals(expectedOutput.toString(), output.toString());
    }
}
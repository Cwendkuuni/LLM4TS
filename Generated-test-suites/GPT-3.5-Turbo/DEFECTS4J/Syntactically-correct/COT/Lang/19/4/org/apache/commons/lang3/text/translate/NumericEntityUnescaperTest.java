import static org.junit.Assert.*;
import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_basic() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder("&#65;");
        StringBuilder output = new StringBuilder();
        try {
            unescaper.translate(input, 0, output);
        } catch (IOException e) {
            fail("IOException occurred");
        }
        assertEquals("A", output.toString());
    }

    @Test
    public void testTranslate_noSemicolon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder("&#65");
        StringBuilder output = new StringBuilder();
        try {
            unescaper.translate(input, 0, output);
        } catch (IOException e) {
            fail("IOException occurred");
        }
        assertEquals("A", output.toString());
    }

    @Test
    public void testTranslate_hexadecimal() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder("&#x41;");
        StringBuilder output = new StringBuilder();
        try {
            unescaper.translate(input, 0, output);
        } catch (IOException e) {
            fail("IOException occurred");
        }
        assertEquals("A", output.toString());
    }

    @Test
    public void testTranslate_invalidDecimal() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder("&#10a;");
        StringBuilder output = new StringBuilder();
        try {
            unescaper.translate(input, 0, output);
            fail("NumberFormatException should have been thrown");
        } catch (IOException e) {
            fail("IOException occurred");
        } catch (NumberFormatException e) {
            // Expected exception
        }
    }

    @Test
    public void testTranslate_invalidHexadecimal() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder("&#xzz;");
        StringBuilder output = new StringBuilder();
        try {
            unescaper.translate(input, 0, output);
            fail("NumberFormatException should have been thrown");
        } catch (IOException e) {
            fail("IOException occurred");
        } catch (NumberFormatException e) {
            // Expected exception
        }
    }

    @Test
    public void testTranslate_highCodepoint() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder("&#131083;");
        StringBuilder output = new StringBuilder();
        try {
            unescaper.translate(input, 0, output);
        } catch (IOException e) {
            fail("IOException occurred");
        }
        assertEquals("𐄃", output.toString());
    }

    @Test
    public void testTranslate_noCodepoint() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder("&#;");
        StringBuilder output = new StringBuilder();
        try {
            unescaper.translate(input, 0, output);
        } catch (IOException e) {
            fail("IOException occurred");
        }
        assertEquals("", output.toString());
    }

    @Test
    public void testTranslate_invalidInput() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder("ABCDEFG");
        StringBuilder output = new StringBuilder();
        try {
            unescaper.translate(input, 0, output);
        } catch (IOException e) {
            fail("IOException occurred");
        }
        assertEquals("", output.toString());
    }

}
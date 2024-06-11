import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

    // Test method: translate(CharSequence input, int index, Writer out)

    @Test
    public void testTranslateWithValidHexadecimalEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder("This is a valid entity: &#x41;");
        StringBuilder output = new StringBuilder();
        int index = 19;
        try {
            int result = unescaper.translate(input, index, output);
            assertEquals(7, result);
            assertEquals("This is a valid entity: A", output.toString());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testTranslateWithValidDecimalEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder("This is a valid entity: &#65;");
        StringBuilder output = new StringBuilder();
        int index = 19;
        try {
            int result = unescaper.translate(input, index, output);
            assertEquals(6, result);
            assertEquals("This is a valid entity: A", output.toString());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testTranslateWithInvalidEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder("This is an invalid entity: &#xyz;");
        StringBuilder output = new StringBuilder();
        int index = 24;
        try {
            int result = unescaper.translate(input, index, output);
            assertEquals(0, result);
            assertEquals("This is an invalid entity: &#xyz;", output.toString());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testTranslateWithInvalidCharactersBeforeEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder("This is an invalid entity: &amp;#x41;");
        StringBuilder output = new StringBuilder();
        int index = 29;
        try {
            int result = unescaper.translate(input, index, output);
            assertEquals(0, result);
            assertEquals("This is an invalid entity: &amp;#x41;", output.toString());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testTranslateWithIncompleteEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder("This is an incomplete entity: &#x41");
        StringBuilder output = new StringBuilder();
        int index = 26;
        try {
            int result = unescaper.translate(input, index, output);
            assertEquals(0, result);
            assertEquals("This is an incomplete entity: &#x41", output.toString());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testTranslateWithNonEndingSemicolonEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder("This is a non-ending semicolon entity: &#x41");
        StringBuilder output = new StringBuilder();
        int index = 34;
        try {
            int result = unescaper.translate(input, index, output);
            assertEquals(0, result);
            assertEquals("This is a non-ending semicolon entity: &#x41", output.toString());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

}
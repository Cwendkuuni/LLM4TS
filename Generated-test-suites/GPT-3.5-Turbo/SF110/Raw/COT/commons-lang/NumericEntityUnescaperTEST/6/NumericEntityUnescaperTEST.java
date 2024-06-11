import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

    @Test
    public void testIsSet() {
        // Test case for isSet method with OPTION.semiColonRequired
        NumericEntityUnescaper unescaper1 = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        assertTrue(unescaper1.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));

        // Test case for isSet method with OPTION.semiColonOptional
        NumericEntityUnescaper unescaper2 = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);
        assertTrue(unescaper2.isSet(NumericEntityUnescaper.OPTION.semiColonOptional));

        // Test case for isSet method with OPTION.errorIfNoSemiColon
        NumericEntityUnescaper unescaper3 = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        assertTrue(unescaper3.isSet(NumericEntityUnescaper.OPTION.errorIfNoSemiColon));
    }

    @Test
    public void testTranslate() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        // Test case for numeric entity with decimal value
        StringBuilder input1 = new StringBuilder("&#65;");
        StringBuilder output1 = new StringBuilder();
        assertEquals(4, unescaper.translate(input1, 0, new StringWriter()));
        assertEquals("A", input1.toString());

        // Test case for numeric entity with hexadecimal value
        StringBuilder input2 = new StringBuilder("&#x61;");
        StringBuilder output2 = new StringBuilder();
        assertEquals(5, unescaper.translate(input2, 0, new StringWriter()));
        assertEquals("a", input2.toString());

        // Test case for numeric entity with decimal value exceeding 65535
        StringBuilder input3 = new StringBuilder("&#65536;");
        StringBuilder output3 = new StringBuilder();
        assertEquals(9, unescaper.translate(input3, 0, new StringWriter()));
        assertEquals("\uD800\uDC00", input3.toString());

        // Test case for invalid numeric entity with missing semi-colon
        StringBuilder input4 = new StringBuilder("&#65");
        StringBuilder output4 = new StringBuilder();
        assertEquals(0, unescaper.translate(input4, 0, new StringWriter()));
        assertEquals("&#65", input4.toString());

        // Test case for invalid numeric entity with missing semi-colon and errorIfNoSemiColon option set
        NumericEntityUnescaper unescaper5 = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        StringBuilder input5 = new StringBuilder("&#65");
        StringBuilder output5 = new StringBuilder();
        try {
            unescaper5.translate(input5, 0, new StringWriter());
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Semi-colon required at end of numeric entity", e.getMessage());
        }
        assertEquals("&#65", input5.toString());

        // Test case for invalid numeric entity with non-numeric characters
        StringBuilder input6 = new StringBuilder("&#65a;");
        StringBuilder output6 = new StringBuilder();
        assertEquals(0, unescaper.translate(input6, 0, new StringWriter()));
        assertEquals("&#65a;", input6.toString());
    }

}
package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for NumericEntityUnescaper.
 */
public class NumericEntityUnescaperTest {

    private NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

    /**
     * Test translating a simple numeric entity.
     */
    @Test
    public void testSimpleNumericEntity() {
        String input = "&amp;#123;";
        String expectedOutput = "&#123;";
        String actualOutput = "";
        try {
            Writer out = new StringWriter();
            int len = unescaper.translate(new StringReader(input), 0, out);
            actualOutput = out.getBuffer().toString();
        } catch (IOException e) {
            fail("IOException occurred during testing: " + e.getMessage());
        }
        assertEquals(expectedOutput, actualOutput);
    }

    /**
     * Test translating a hexadecimal numeric entity.
     */
    @Test
    public void testHexNumericEntity() {
        String input = "&amp;#x1F4;";
        String expectedOutput = "\u007F";
        String actualOutput = "";
        try {
            Writer out = new StringWriter();
            int len = unescaper.translate(new StringReader(input), 0, out);
            actualOutput = out.getBuffer().toString();
        } catch (IOException e) {
            fail("IOException occurred during testing: " + e.getMessage());
        }
        assertEquals(expectedOutput, actualOutput);
    }

    /**
     * Test translating a numeric entity without a semicolon.
     */
    @Test
    public void testMissingSemicolon() {
        String input = "&amp;#123";
        String expectedErrorMessage = "FAIL: &#123[0][5]";
        try {
            Writer out = new StringWriter();
            int len = unescaper.translate(new StringReader(input), 0, out);
            fail("Expected NumberFormatException, but received a translation instead.");
        } catch (IOException e) {
            // This is expected, as the test is checking for the error message
        } catch (NumberFormatException nfe) {
            String errorMessage = nfe.getMessage();
            if (!errorMessage.equals(expectedErrorMessage)) {
                fail("Unexpected error message: " + errorMessage);
            }
        }
    }

    /**
     * Test translating a non-numeric entity.
     */
    @Test
    public void testNonNumericEntity() {
        String input = "&amp;#abc;";
        String expectedErrorMessage = "FAIL: #abc[0][3]";
        try {
            Writer out = new StringWriter();
            int len = unescaper.translate(new StringReader(input), 0, out);
            fail("Expected NumberFormatException, but received a translation instead.");
        } catch (IOException e) {
            // This is expected
        } catch (NumberFormatException nfe) {
            String errorMessage = nfe.getMessage();
            if (!errorMessage.equals(expectedErrorMessage)) {
                fail("Unexpected error message: " + errorMessage);
            }
        }
    }

    /**
     * Test translating an empty input.
     */
    @Test
    public void testEmptyInput() {
        String input = "";
        String expectedOutput = "";
        String actualOutput = "";
        try {
            Writer out = new StringWriter();
            int len = unescaper.translate(new StringReader(input), 0, out);
            actualOutput = out.getBuffer().toString();
        } catch (IOException e) {
            fail("IOException occurred during testing: " + e.getMessage());
        }
        assertEquals(expectedOutput, actualOutput);
    }

    /**
     * Test translating a large entity value.
     */
    @Test
    public void testLargeEntityValue() {
        String input = "&amp;#123456789;";
        String expectedOutput = new String(Character.toChars(0x12345678));
        String actualOutput = "";
        try {
            Writer out = new StringWriter();
            int len = unescaper.translate(new StringReader(input), 0, out);
            actualOutput = out.getBuffer().toString();
        } catch (IOException e) {
            fail("IOException occurred during testing: " + e.getMessage());
        }
        assertEquals(expectedOutput, actualOutput);
    }
}
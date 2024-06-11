package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String givenStr = "--test";
        String expectedResult = "test";
        String result = Util.stripLeadingHyphens(givenStr);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String givenStr = "\"test\"";
        String expectedResult = "test";
        String result = Util.stripLeadingAndTrailingQuotes(givenStr);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testStripLeadingHyphens_EmptyString() {
        String givenStr = "";
        String expectedResult = "";
        String result = Util.stripLeadingHyphens(givenStr);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testStripLeadingHyphens_NoLeadingHyphens() {
        String givenStr = "test";
        String expectedResult = "test";
        String result = Util.stripLeadingHyphens(givenStr);
        assertEquals(expectedResult, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStripLeadingHyphens_NullString() {
        String givenStr = null;
        Util.stripLeadingHyphens(givenStr);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_NoQuotes() {
        String givenStr = "test";
        String expectedResult = "test";
        String result = Util.stripLeadingAndTrailingQuotes(givenStr);
        assertEquals(expectedResult, result);
    }

}
package org.apache.commons.cli.test;

import org.junit.Test;
import org.apache.commons.cli.Util;

import static org.junit.Assert.assertEquals;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String strWithDoubleHyphens = "--test";
        String expectedResult = "test";
        String actualResult = Util.stripLeadingHyphens(strWithDoubleHyphens);
        assertEquals(expectedResult, actualResult);

        String strWithSingleHyphen = "-test";
        expectedResult = "test";
        actualResult = Util.stripLeadingHyphens(strWithSingleHyphen);
        assertEquals(expectedResult, actualResult);

        String strWithoutHyphens = "test";
        expectedResult = strWithoutHyphens;
        actualResult = Util.stripLeadingHyphens(strWithoutHyphens);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String strWithLeadingAndTrailingQuotes = "\"test\"";
        String expectedResult = "test";
        String actualResult = Util.stripLeadingAndTrailingQuotes(strWithLeadingAndTrailingQuotes);
        assertEquals(expectedResult, actualResult);

        String strWithLeadingQuotesOnly = "\"test";
        expectedResult = "test";
        actualResult = Util.stripLeadingAndTrailingQuotes(strWithLeadingQuotesOnly);
        assertEquals(expectedResult, actualResult);

        String strWithTrailingQuotesOnly = "test\"";
        expectedResult = "test";
        actualResult = Util.stripLeadingAndTrailingQuotes(strWithTrailingQuotesOnly);
        assertEquals(expectedResult, actualResult);

        String strWithoutQuotes = "test";
        expectedResult = strWithoutQuotes;
        actualResult = Util.stripLeadingAndTrailingQuotes(strWithoutQuotes);
        assertEquals(expectedResult, actualResult);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStripLeadingHyphensWithNullString() {
        String nullString = null;
        Util.stripLeadingHyphens(nullString);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStripLeadingAndTrailingQuotesWithNullString() {
        String nullString = null;
        Util.stripLeadingAndTrailingQuotes(nullString);
    }
}
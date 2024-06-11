package org.apache.commons.cli;
import org.junit.Test;
import org.junit.Assert;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens_WithStartingDoubleHyphen_ReturnsStringWithoutHyphens() {
        String input = "--test";
        String expectedResult = "test";

        String result = Util.stripLeadingHyphens(input);

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testStripLeadingHyphens_WithStartingSingleHyphen_ReturnsStringWithoutHyphen() {
        String input = "-test";
        String expectedResult = "test";

        String result = Util.stripLeadingHyphens(input);

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testStripLeadingHyphens_WithoutStartingHyphen_ReturnsSameString() {
        String input = "test";
        String expectedResult = "test";

        String result = Util.stripLeadingHyphens(input);

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_WithLeadingAndTrailingQuotes_ReturnsStringWithoutQuotes() {
        String input = "\"test\"";
        String expectedResult = "test";

        String result = Util.stripLeadingAndTrailingQuotes(input);

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_WithoutQuotes_ReturnsSameString() {
        String input = "test";
        String expectedResult = "test";

        String result = Util.stripLeadingAndTrailingQuotes(input);

        Assert.assertEquals(expectedResult, result);
    }

}
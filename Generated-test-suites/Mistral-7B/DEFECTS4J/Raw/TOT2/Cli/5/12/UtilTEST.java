import static org.junit.Assert.assertEquals;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String input = "--foo";
        String expectedOutput = "foo";
        String output = Util.stripLeadingHyphens(input);
        assertEquals(expectedOutput, output);

        input = "foo";
        expectedOutput = "foo";
        output = Util.stripLeadingHyphens(input);
        assertEquals(expectedOutput, output);

        input = "---foo---";
        expectedOutput = "foo";
        output = Util.stripLeadingHyphens(input);
        assertEquals(expectedOutput, output);

        input = "";
        expectedOutput = "";
        output = Util.stripLeadingHyphens(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String input = "'abc'";
        String expectedOutput = "abc";
        String output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expectedOutput, output);

        input = "abc";
        expectedOutput = "abc";
        output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expectedOutput, output);

        input = "\"abc\"";
        expectedOutput = "abc";
        output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expectedOutput, output);

        input = "";
        expectedOutput = "";
        output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expectedOutput, output);

        input = "\"ab\"c\"";
        expectedOutput = "abc";
        output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expectedOutput, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStripLeadingHyphensWithNullString() {
        String input = null;
        Util.stripLeadingHyphens(input);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStripLeadingAndTrailingQuotesWithNullString() {
        String input = null;
        Util.stripLeadingAndTrailingQuotes(input);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStripLeadingHyphensWithEmptyString() {
        String input = "";
        Util.stripLeadingHyphens(input);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStripLeadingAndTrailingQuotesWithEmptyString() {
        String input = "";
        Util.stripLeadingAndTrailingQuotes(input);
    }

}
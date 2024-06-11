import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test class for Util methods.
 */
public class UtilTest
{
    @Test
    public void testStripLeadingHyphens()
    {
        // Basic functionality test
        String input = "--test";
        String expectedOutput = "test";
        String output = Util.stripLeadingHyphens(input);
        assertEquals(expectedOutput, output);

        // Edge cases and exception handling tests
        String nullInput = null;
        String emptyInput = "";
        String doubleLeadingHyphensInput = "----test";
        String noLeadingHyphensInput = "test";

        // Edge cases tests
        String testEdgeCases[] = {nullInput, emptyInput, doubleLeadingHyphensInput, noLeadingHyphensInput};
        for (String testCase : testEdgeCases)
        {
            String output = Util.stripLeadingHyphens(testCase);
            // Null input should return null
            if (testCase == null)
            {
                assertEquals(null, output);
            }
            // Empty string should return empty string
            else if (testCase.isEmpty())
            {
                assertEquals("", output);
            }
            // Double leading hyphens should remove both
            else if (testCase.startsWith("--"))
            {
                assertEquals(testCase.substring(2), output);
            }
            // No leading hyphens should return the original string
            else
            {
                assertEquals(testCase, output);
            }
        }

        // Exception handling tests
        try
        {
            String illegalCharacterInput = "test--";
            Util.stripLeadingHyphens(illegalCharacterInput);
        }
        catch (Exception e)
        {
            // Verify the exception is expected and correctly reported
            assertEquals("Unexpected exception in testStripLeadingHyphens()", e.getMessage());
        }
    }

    @Test
    public void testStripLeadingAndTrailingQuotes()
    {
        // Basic functionality test
        String input = "'test'";
        String expectedOutput = "test";
        String output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expectedOutput, output);

        // Edge cases and exception handling tests
        String nullInput = null;
        String emptyInput = "";
        String leadingQuotesInput = "\"test\"";
        String trailingQuotesInput = "test\"";
        String doubleLeadingQuotesInput = "\"test\"";
        String noQuotesInput = "test";
        String illegalCharacterInput = "\"test'\"";

        // Edge cases tests
        String testEdgeCases[] = {nullInput, emptyInput, leadingQuotesInput, trailingQuotesInput, doubleLeadingQuotesInput, noQuotesInput, illegalCharacterInput};
        for (String testCase : testEdgeCases)
        {
            String output = Util.stripLeadingAndTrailingQuotes(testCase);
            // Null input should return null
            if (testCase == null)
            {
                assertEquals(null, output);
            }
            // Empty string should return empty string
            else if (testCase.isEmpty())
            {
                assertEquals("", output);
            }
            // Leading quotes should be removed
            else if (testCase.startsWith("\""))
            {
                assertEquals(testCase.substring(1), output);
            }
            // Trailing quotes should be removed
            else if (testCase.endsWith("\""))
            {
                assertEquals(testCase.substring(0, testCase.length() - 1), output);
            }
            // Double leading quotes should remove both
            else if (testCase.startsWith("\"") && testCase.endsWith("\""))
            {
                assertEquals(testCase.substring(1, testCase.length() - 1), output);
            }
            // No quotes should return the original string
            else
            {
                assertEquals(testCase, output);
            }
        }

        // Exception handling tests
        try
        {
            Util.stripLeadingAndTrailingQuotes(null);
        }
        catch (Exception e)
        {
            // Verify the exception is expected and correctly reported
            assertEquals("Unexpected exception in testStripLeadingAndTrailingQuotes()", e.getMessage());
        }
    }
}
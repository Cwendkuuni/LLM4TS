package org.apache.commons.cli;
import org.apache.commons.cli.Util;
    import org.junit.Test;
    import static org.junit.Assert.*;

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

            input = "bar--baz";
            expectedOutput = "bar baz";
            output = Util.stripLeadingHyphens(input);
            assertEquals(expectedOutput, output);

            input = "";
            output = Util.stripLeadingHyphens(input);
            assertEquals("", output);

            input = null;
            try {
                output = Util.stripLeadingHyphens(input);
                fail("Expected NullPointerException");
            } catch (NullPointerException e) {
                // pass
            }

            // Edge cases tests
            input = "---foo";
            expectedOutput = "foo";
            output = Util.stripLeadingHyphens(input);
            assertEquals(expectedOutput, output);

            input = "foo--";
            expectedOutput = "foo";
            output = Util.stripLeadingHyphens(input);
            assertEquals(expectedOutput, output);

            input = "---";
            expectedOutput = "";
            output = Util.stripLeadingHyphens(input);
            assertEquals(expectedOutput, output);
        }

        @Test
        public void testStripLeadingAndTrailingQuotes() {
            String input = "\"one two\"";
            String expectedOutput = "one two";
            String output = Util.stripLeadingAndTrailingQuotes(input);
            assertEquals(expectedOutput, output);

            input = "\"\"";
            expectedOutput = "";
            output = Util.stripLeadingAndTrailingQuotes(input);
            assertEquals(expectedOutput, output);

            input = "\"one\"";
            expectedOutput = "one";
            output = Util.stripLeadingAndTrailingQuotes(input);
            assertEquals(expectedOutput, output);

            input = "\"\"one\"\""; // trailing quote alone
            expectedOutput = "one";
            output = Util.stripLeadingAndTrailingQuotes(input);
            assertEquals(expectedOutput, output);

            input = "\"one\"\""; // leading quote alone
            expectedOutput = "one";
            output = Util.stripLeadingAndTrailingQuotes(input);
            assertEquals(expectedOutput, output);

            input = "\"\""; // empty string with quotes
            expectedOutput = "";
            output = Util.stripLeadingAndTrailingQuotes(input);
            assertEquals(expectedOutput, output);

            input = "\"one two\"\""; // double quotes inside
            expectedOutput = "one two";
            output = Util.stripLeadingAndTrailingQuotes(input);
            assertEquals(expectedOutput, output);

            input = "\"one 'two'\""; // single quote inside
            expectedOutput = "one two";
            output = Util.stripLeadingAndTrailingQuotes(input);
            assertEquals(expectedOutput, output);

            input = "\"one,two\""; // comma inside
            expectedOutput = "one,two";
            output = Util.stripLeadingAndTrailingQuotes(input);
            assertEquals(expectedOutput, output);

            input = "\"one 'two' , three\""; // single quote and comma inside
            expectedOutput = "one two, three";
            output = Util.stripLeadingAndTrailingQuotes(input);
            assertEquals(expectedOutput, output);

            input = "\"\""; // empty string without quotes
            expectedOutput = "";
            output = Util.stripLeadingAndTrailingQuotes(input);
            assertEquals(expectedOutput, output);

            input = ""; // empty string
            expectedOutput = "";
            output = Util.stripLeadingAndTrailingQuotes(input);
            assertEquals(expectedOutput, output);

            // Exception handling tests
            input = null;
            try {
                output = Util.stripLeadingAndTrailingQuotes(input);
                fail("Expected NullPointerException");
            } catch (NullPointerException e) {
                // pass
            }
        }
    }
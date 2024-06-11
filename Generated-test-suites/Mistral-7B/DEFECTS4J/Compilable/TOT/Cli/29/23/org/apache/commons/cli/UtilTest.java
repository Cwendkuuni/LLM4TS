package org.apache.commons.cli;
import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        // Typical use case
        String input = "--test";
        String expectedOutput = "test";
        String output = Util.stripLeadingHyphens(input);
        assert(output.equals(expectedOutput));

        // Edge case: empty string
        String inputEmpty = "";
        String expectedOutputEmpty = "";
        String outputEmpty = Util.stripLeadingHyphens(inputEmpty);
        assert(outputEmpty.equals(expectedOutputEmpty));

        // Error scenario: null input
        String inputNull = null;
        String expectedOutputNull = null;
        String outputNull = Util.stripLeadingHyphens(inputNull);
        assert(outputNull == expectedOutputNull);

        // Edge case: leading space followed by hyphen
        String inputSpaceHyphen = " --test";
        String expectedOutputSpaceHyphen = "test";
        String outputSpaceHyphen = Util.stripLeadingHyphens(inputSpaceHyphen);
        assert(outputSpaceHyphen.equals(expectedOutputSpaceHyphen));

        // Edge case: leading space followed by hyphen and another space
        String inputSpaceHyphenSpace = " --test ";
        String expectedOutputSpaceHyphenSpace = "test";
        String outputSpaceHyphenSpace = Util.stripLeadingHyphens(inputSpaceHyphenSpace);
        assert(outputSpaceHyphenSpace.equals(expectedOutputSpaceHyphenSpace));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        // Typical use case
        String input = "'one two'";
        String expectedOutput = "one two";
        String output = Util.stripLeadingAndTrailingQuotes(input);
        assert(output.equals(expectedOutput));

        // Edge case: no leading quotes
        String inputNoLeadingQuotes = "one two";
        String expectedOutputNoLeadingQuotes = "one two";
        String outputNoLeadingQuotes = Util.stripLeadingAndTrailingQuotes(inputNoLeadingQuotes);
        assert(outputNoLeadingQuotes.equals(expectedOutputNoLeadingQuotes));

        // Error scenario: no trailing quotes
        String inputNoTrailingQuotes = "one two";
        String expectedOutputNoTrailingQuotes = "one two";
        String outputNoTrailingQuotes = Util.stripLeadingAndTrailingQuotes(inputNoTrailingQuotes);
        assert(outputNoTrailingQuotes.equals(expectedOutputNoTrailingQuotes));

        // Edge case: empty string
        String inputEmpty = "";
        String expectedOutputEmpty = "";
        String outputEmpty = Util.stripLeadingAndTrailingQuotes(inputEmpty);
        assert(outputEmpty.equals(expectedOutputEmpty));

        // Edge case: input with only leading quotes
        String inputLeadingQuotes = "''";
        String expectedOutputLeadingQuotes = "";
        String outputLeadingQuotes = Util.stripLeadingAndTrailingQuotes(inputLeadingQuotes);
        assert(outputLeadingQuotes.equals(expectedOutputLeadingQuotes));
    }
}
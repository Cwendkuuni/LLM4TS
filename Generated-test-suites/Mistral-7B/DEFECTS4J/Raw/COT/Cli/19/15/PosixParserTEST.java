package org.apache.commons.cli;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Test class for PosixParser
 */
public class PosixParserTest {

    @Test
    public void testFlatten() throws ParseException {
        // Test case for flatten method with basic arguments
        String[] args = new String[]{"test", "-a", "1", "--optionB", "2", "-c", "3", "--optionC", "4", "--"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        List<String> expectedTokens = Arrays.asList(
                "test", "-a", "--optionB", "2", "-c", "--optionC", "4"
        );
        List<String> tokens = parser.flatten(options, args, false);
        Assert.assertEquals(expectedTokens, tokens);

        // Test case for flatten method with stopAtNonOption = true
        tokens = parser.flatten(options, args, true);
        expectedTokens = Arrays.asList(
                "test", "-a", "--optionB", "2", "-c", "--optionC", "4", "--", "1"
        );
        Assert.assertEquals(expectedTokens, tokens);

        // Test case for flatten method with invalid token
        args = new String[]{"test", "-a", "--invalidOption", "1", "-c", "--optionC", "4", "--"};
        tokens = parser.flatten(options, args, false);
        expectedTokens = Arrays.asList(
                "test", "-a", "--invalidOption", "1", "-c", "--optionC", "4"
        );
        Assert.assertEquals(expectedTokens, tokens);

        // Test case for flatten method with bursting token
        args = new String[]{"test", "-a", "--optionA=valueA", "-b", "-c", "--optionB=valueB", "valueC", "--"};
        tokens = parser.flatten(options, args, false);
        expectedTokens = Arrays.asList(
                "test", "-a", "--optionA", "valueA", "-b", "--optionB", "valueB"
        );
        Assert.assertEquals(expectedTokens, tokens);

        // Test case for flatten method with bursting token and stopAtNonOption = true
        tokens = parser.flatten(options, args, true);
        expectedTokens = Arrays.asList(
                "test", "-a", "--optionA=valueA", "-b", "--optionB=valueB", "valueC"
        );
        Assert.assertEquals(expectedTokens, tokens);
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithInvalidArgument() throws ParseException {
        // Test case for flatten method with invalid argument
        String[] args = new String[]{"test", "-a", "--invalidOption"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        parser.flatten(options, args, false);
    }
}
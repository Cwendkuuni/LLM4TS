package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for {@link Parser} class
 */
public class ParserTest {

    @Test
    public void testParseBasic() throws ParseException {
        // given
        String[] args = new String[]{"-a", "1", "--optionB", "2"};
        Options options = new Options();
        options.addOptionalOption("a", "anOption", true, "A value for anOption");
        options.addOption("b", "optionB", false, "A value for optionB");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        // when

        // then
        Assert.assertEquals("1", cmd.getOptionValue("a"));
        Assert.assertFalse(cmd.hasOption("a"));
        Assert.assertTrue(cmd.hasOption("b"));
        Assert.assertNull(cmd.getOptionValue("b"));
    }

    @Test
    public void testParseWithRequiredOptions() throws ParseException {
        // given
        String[] args = new String[]{"--optionA", "--optionB"};
        Options options = new Options();
        options.addRequiredOption("a", "optionA", false, "An required option A");
        options.addRequiredOption("b", "optionB", false, "An required option B");

        CommandLineParser parser = new DefaultParser();

        // when
        try {
            CommandLine cmd = parser.parse(options, args);
        } catch (ParseException e) {
            // then
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("ParserTest", options);
            String expectedHelp = "ParserTest:\n" +
                    "  --optionA <optionA>\n" +
                    "  --optionB <optionB>\n" +
                    "  --optionB\n" +
                    "  --help\n";
            Assert.assertEquals(expectedHelp, formatter.getHelpText());
            throw e;
        }

        // then
        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertTrue(cmd.hasOption("b"));
    }

    @Test
    public void testParseWithValueOptions() throws ParseException {
        // given
        String[] args = new String[]{"-a", "valueA", "--optionB=valueB"};
        Options options = new Options();
        options.addOptionalOption("a", "anOption", true, "A value for anOption");
        options.addOption("b", "optionB", true, "A value for optionB");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        // when

        // then
        Assert.assertEquals("valueA", cmd.getOptionValue("a"));
        Assert.assertEquals("valueB", cmd.getOptionValue("b"));
    }

    @Test(expected = ParseException.class)
    public void testParseWithMissingOption() throws ParseException {
        // given
        String[] args = new String[]{"--nonexistentOption"};
        Options options = new Options();
        options.addOption("a", "anOption", false, "A value for anOption");

        CommandLineParser parser = new DefaultParser();
        parser.parse(options, args);
    }

    @Test(expected = ParseException.class)
    public void testParseWithMissingValueOption() throws ParseException {
        // given
        String[] args = new String[]{"-a"};
        Options options = new Options();
        options.addOptionalOption("a", "anOption", true, "A value for anOption");

        CommandLineParser parser = new DefaultParser();
        parser.parse(options, args);
    }

    @Test
    public void testParseWithMultipleValuesOption() throws ParseException {
        // given
        String[] args = new String[]{"-a", "value1", "value2"};
        Options options = new Options();
        options.addOptionalOption("a", "anOption", true, "A value for anOption");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        // when

        // then
        String[] values = cmd.getOptionValues("a");
        Assert.assertArrayEquals(new String[]{"value1", "value2"}, values);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueOfNonexistentOption() throws ParseException {
        // given
        String[] args = new String[]{"--nonexistentOption"};
        Options options = new Options();

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        // when
        String value = cmd.getOptionValue("nonexistentOption");

        // then
        Assert.assertNull(value);
    }
}
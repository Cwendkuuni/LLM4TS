package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.ParseOptions;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Test class for Parser
 */
public class ParserTest {

    @Test
    public void testParseWithValidArguments() throws Exception {
        // Given
        String[] arguments = new String[]{"app", "--option1", "value1", "--option2"};
        Options options = new Options();
        options.addRequiredOption("o", "option1", true, "Option 1");
        options.addOption("o", "option2", false, "Option 2");

        CommandLineParser parser = new DefaultParser();

        // When
        CommandLine cmd = parser.parse(options, arguments, false);

        // Then
        Assert.assertEquals("option1", cmd.getOptionValue("option1"));
        Assert.assertNull(cmd.getOptionValue("option1_missing"));
        Assert.assertFalse(cmd.hasOption("option2_missing"));
        Assert.assertTrue(cmd.hasOption("option2"));
        Assert.assertEquals(Arrays.asList("value1", "--option2"), cmd.getArgs());
    }

    @Test(expected = ParseException.class)
    public void testParseWithMissingRequiredOption() throws Exception {
        // Given
        String[] arguments = new String[]{"app", "--option1", "value1"};
        Options options = new Options();
        options.addRequiredOption("o", "option1", true, "Option 1");
        options.addRequiredOption("o", "option2", true, "Option 2");

        CommandLineParser parser = new DefaultParser();

        // When & Then
        parser.parse(options, arguments, false);

        Assert.fail("Expected ParseException");
    }

    @Test
    public void testParseWithUnrecognizedOption() throws Exception {
        // Given
        String[] arguments = new String[]{"app", "--unrecognized-option", "value"};
        Options options = new Options();
        options.addRequiredOption("o", "option1", true, "Option 1");

        CommandLineParser parser = new DefaultParser();

        // When
        try {
            CommandLine cmd = parser.parse(options, arguments, false);
        } catch (ParseException e) {
            // Then
            Assert.assertEquals("Unrecognized option: unrecognized-option", e.getMessage());
        }
    }

    @Test
    public void testParseWithMixedLongAndShortOptions() throws Exception {
        // Given
        String[] arguments = new String[]{"app", "-a", "--long-option", "value", "-b", "arg"};
        Options options = new Options();
        options.addOption("a", "short-option", false, "Short option A");
        options.addOption("l", "long-option", true, "Long option");

        CommandLineParser parser = new DefaultParser();

        // When
        CommandLine cmd = parser.parse(options, arguments, false);

        // Then
        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertTrue(cmd.hasOption("l"));
        Assert.assertEquals("value", cmd.getOptionValue("long-option"));
        List<String> args = cmd.getArgs();
        Assert.assertEquals(Arrays.asList("arg"), args.subList(0, args.size() - 1));
    }

    @Test
    public void testParseWithMixedOptionsAndArguments() throws Exception {
        // Given
        String[] arguments = new String[]{"app", "--option1", "value1", "--option2=arg2", "--option3", "arg3", "arg4"};
        Options options = new Options();
        options.addRequiredOption("o", "option1", true, "Option 1");
        options.addOption("o", "option2", false, "Option 2");
        options.addOption("o", "option3", true, "Option 3");

        CommandLineParser parser = new DefaultParser();

        // When
        CommandLine cmd = parser.parse(options, arguments, false);

        // Then
        Assert.assertEquals("value1", cmd.getOptionValue
                ("option1"));
        Assert.assertTrue(cmd.hasOption("option2"));
        Assert.assertEquals("arg2", cmd.getOptionValue("option2"));
        Assert.assertEquals(Arrays.asList("arg3", "arg4"), cmd.getArgs());
    }

    @Test
    public void testParseWithEmptyArguments() throws Exception {
        // Given
        String[] arguments = new String[0];
        Options options = new Options();

        CommandLineParser parser = new DefaultParser();

        // When
        CommandLine cmd = parser.parse(options, arguments, false);

        // Then
        Assert.assertEquals(0, cmd.getArgs().size());
    }

    @Test
    public void testParseWithOptionsOnly() throws Exception {
        // Given
        String[] arguments = new String[]{"--option1", "--option2"};
        Options options = new Options();
        options.addOption("o", "option1", true, "Option 1");
        options.addOption("o", "option2", true, "Option 2");

        CommandLineParser parser = new DefaultParser();

        // When
        CommandLine cmd = parser.parse(options, arguments, false);

        // Then
        Assert.assertEquals(2, cmd.getArgs().size());
    }

    @Test
    public void testParseWithLongOptionsOnly() throws Exception {
        // Given
        String[] arguments = new String[]{"--long-option1", "--long-option2"};
        Options options = new Options();
        options.addOption("l", "long-option1", true, "Long option 1");
        options.addOption("l", "long-option2", true, "Long option 2");

        CommandLineParser parser = new DefaultParser();

        // When
        CommandLine cmd = parser.parse(options, arguments, false);

        // Then
        Assert.assertEquals(0, cmd.getArgs().size());
    }

    @Test
    public void testParseWithMixedLongAndShortOptionsAndArguments() throws Exception {
        // Given
        String[] arguments = new String[]{"app", "--option1=value1", "-a", "--long-option2=arg2", "--option3", "arg3", "arg4"};
        Options options = new Options();
        options.addOption("a", "short-option", false, "Short option A");
        options.addOption("l", "long-option1", true, "Long option 1");
        options.addOption("l", "long-option2", true, "Long option 2");
        options.addOption("o", "option3", true, "Option 3");

        CommandLineParser parser = new DefaultParser();

        // When
        CommandLine cmd = parser.parse(options, arguments, false);

        // Then
        Assert.assertEquals("value1", cmd.getOptionValue("option1"));
        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertTrue(cmd.hasOption("l"));
        Assert.assertEquals("arg2", cmd.getOptionValue("long-option2"));
        Assert.assertEquals(Arrays.asList("arg3", "arg4"), cmd.getArgs());
    }

    @Test
    public void testParseWithProperties() throws Exception {
        // Given
        String[] arguments = new String[]{"app", "--option1=value1", "--option2=value2"};
        Properties properties = new Properties();
        properties.setProperty("--option1", "property_value1");
        properties.setProperty("--option2", "property_value2");
        Options options = new Options();
        options.addOption("o", "option1", false, "Option 1");
        options.addOption("o", "option2", false, "Option 2");

        CommandLineParser parser = new DefaultParser();

        // When
        CommandLine cmd = parser.parse(options, arguments, properties, false);

        // Then
        Assert.assertEquals("value1", cmd.getOptionValue("option1"));
        Assert.assertEquals("property_value1", cmd.getOptionValue("option1"));
        Assert.assertEquals("value2", cmd.getOptionValue("option2"));
        Assert.assertEquals("property_value2", cmd.getOptionValue("option2"));
    }
}
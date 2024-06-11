package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.apache.commons.cli.test.TestUtils.createTestOptions;
import static org.apache.commons.cli.test.TestUtils.createTestProperties;

/**
 * Test for Parser class.
 */
public class ParserTest {

    @Test
    public void testParserWithDefaultOptions() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = createTestOptions();
        String[] arguments = new String[]{"--inputFile", "test.txt", "--outputDir", "output"};
        CommandLine cmd = parser.parse(options, arguments);

        Assert.assertNull(parser.getRequiredOptions());
        Assert.assertEquals(options.getOptions().size(), cmd.getOptions().size());
        Assert.assertEquals(arguments.length, cmd.getArgs().size());
    }

    @Test
    public void testParserWithRequiredOptions() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = createTestOptions();
        options.addRequiredOption("a", true, "A required option");
        options.addRequiredOption("b", true, "Another required option");
        String[] arguments = new String[0];

        try {
            parser.parse(options, arguments);
            Assert.fail();
        } catch (MissingOptionException e) {
            Assert.assertEquals("All required Options are not present:", e.getMessage());
        }

        arguments = new String[]{"--a", "--b"};
        cmd = parser.parse(options, arguments);
        Assert.assertNotNull(parser.getRequiredOptions());
        Assert.assertEquals(parser.getRequiredOptions().size(), 2);
    }

    @Test
    public void testParserWithProperties() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = createTestOptions();
        Properties properties = createTestProperties();
        String[] arguments = new String[0];
        CommandLine cmd = parser.parse(options, arguments, properties);

        Assert.assertEquals(options.getOptions().size(), cmd.getOptions().size());
        Assert.assertEquals(properties.size(), cmd.getOptions().size());
    }

    @Test(expected = ParseException.class)
    public void testParserWithStopAtNonOption() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = createTestOptions();
        String[] arguments = new String[]{"--inputFile", "test.txt", "--outputDir", "output", "--", "extra"};

        parser.parse(options, arguments, true);
    }

    @Test
    public void testParserWithIllegalOption() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = createTestOptions();
        String[] arguments = new String[]{"--unknownOption", "--inputFile", "test.txt"};

        try {
            parser.parse(options, arguments);
            Assert.fail();
        } catch (UnrecognizedOptionException e) {
            Assert.assertEquals("Unrecognized option: unknownOption", e.getMessage());
        }
    }

    @Test
    public void testParserWithSingleValueOption() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = createTestOptions();
        options.addOption("a", true, "An option with a single value");
        String[] arguments = new String[]{"--a", "value"};
        CommandLine cmd = parser.parse(options, arguments);

        Option option = cmd.getOption("a");
        Assert.assertEquals("value", option.getValue()[0]);
    }

    @Test
    public void testParserWithMultipleValueOption() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = createTestOptions();
        options.addOption("a", true, "An option with multiple values");
        String[] arguments = new String[]{"--a", "value1", "--a", "value2"};
        CommandLine cmd = parser.parse(options, arguments);

        Option option = cmd.getOption("a");
        Assert.assertArrayEquals(new String[]{"value1", "value2"}, option.getValue());

        arguments = new String[]{"--a", "value1", "--a", "value3"};
        parser = new DefaultParser();
        cmd = parser.parse(options, arguments);

        option = cmd.getOption("a");
        Assert.assertArrayEquals(new String[]{"value1", "value3"}, option.getValue());
    }

    @Test
    public void testParserWithOptionalValueOption() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = createTestOptions();
        options.addOption("a", true, "An option with optional value");
        String[] arguments = new String[]{"--a"};
        CommandLine cmd = parser.parse(options, arguments);
        Assert.assertNull(cmd.getOption("a").getValue());

        arguments = new String[]{"--a", "value"};
        cmd = parser.parse(options, arguments);
        Assert.assertEquals("value", cmd.getOption("a").getValue()[0]);

        arguments = new String[]{"--a"};
        parser = new DefaultParser();
        cmd = parser.parse(options, arguments);
        Assert.assertNull(cmd.getOption("a").getValue());
    }

    @Test
    public void testParserWithGroupedOptions() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = createTestOptions();
        OptionGroup group = new OptionGroup("mygroup", "My group", 1, 1);
        group.addOption(new Option("a", true, "OptionA"));
        group.addOption(new Option("b", true, "OptionB"));
        group.addOption(new Option("c", true, "OptionC"));

        options.addOptionGroup(group);
        String[] arguments = new String[]{"--mygroup", "--a"};
        CommandLine cmd = parser.parse(options, arguments);

        Assert.assertEquals("OptionA", cmd.getOption("a").getLongOpt());
        Assert.assertNull(cmd.getOption("b").getValue());
        Assert.assertNull(cmd.getOption("c").getValue());
    }

    @Test
    public void testParserWithLongOptionAndValue() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = createTestOptions();
        options.addOption("longOption", true, "A long option with a value");
        String[] arguments = new String[]{"--longOption=longValue"};
        CommandLine cmd = parser.parse(options, arguments);

        Option option = cmd.getOption("longOption");
        Assert.assertEquals("longValue", option.getValue()[0]);
    }
}
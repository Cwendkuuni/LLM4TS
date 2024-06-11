package org.apache.commons.cli.test;

import org.apache.commons.cli.Parser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for Parser methods.
 */
public class ParserTest {

    /**
     * Test parsing with no options or arguments.
     *
     * @throws Exception if parse fails.
     */
    @Test
    public void testParseNoOptionsOrArguments() throws Exception {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String[] arguments = new String[0];
        CommandLine cmd = parser.parse(options, arguments);
        assertTrue(cmd.getArgs().isEmpty());
        assertTrue(cmd.getOptions().isEmpty());
    }

    /**
     * Test parsing with required options.
     *
     * @throws Exception if parse fails.
     */
    @Test
    public void testParseRequiredOptions() throws Exception {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addRequiredOption("a", "alpha", false, "Alpha value");
        options.addRequiredOption("b", "beta", false, "Beta value");
        String[] arguments = new String[0];
        try {
            parser.parse(options, arguments);
            fail("Missing required options exception expected");
        } catch (MissingOptionException e) {
            assertEquals("Missing required options: [a, b]", e.getMessage());
        }

        String[] requiredArguments = new String[]{"-a", "-b"};
        CommandLine cmd = parser.parse(options, requiredArguments);
        assertEquals(2, cmd.getArgs().size());
        assertTrue(cmd.getOptions().isEmpty());
    }

    /**
     * Test parsing with optional options.
     *
     * @throws Exception if parse fails.
     */
    @Test
    public void testParseOptionalOptions() throws Exception {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", "alpha", true, "Alpha value");
        options.addOption("b", "beta", true, "Beta value");
        String[] arguments = new String[0];
        CommandLine cmd = parser.parse(options, arguments);
        assertTrue(cmd.getArgs().isEmpty());
        assertTrue(cmd.getOptions().isEmpty());

        String[] optionalArguments = new String[]{"-a", "-b"};
        cmd = parser.parse(options, optionalArguments);
        assertEquals(0, cmd.getArgs().size());
        assertEquals(2, cmd.getOptions().size());
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));

        String[] argumentsWithValues = new String[]{"-a", "value1", "-b", "value2"};
        cmd = parser.parse(options, argumentsWithValues);
        assertEquals(0, cmd.getArgs().size());
        assertEquals(2, cmd.getOptions().size());
        assertTrue(cmd.getOptionValue("a").equals("value1"));
        assertTrue(cmd.getOptionValue("b").equals("value2"));
    }

    /**
     * Test parsing with a mixed set of options and arguments.
     *
     * @throws Exception if parse fails.
     */
    @Test
    public void testParseMixedOptionsAndArguments() throws Exception {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", "alpha", true, "Alpha value");
        options.addOption("b", "beta", true, "Beta value");
        String[] arguments = new String[]{"arg1", "arg2", "-a", "value1", "-b", "value2"};
        CommandLine cmd = parser.parse(options, arguments);
        assertEquals(2, cmd.getArgs().size());
        assertEquals("arg1", cmd.getArgs().get(0));
        assertEquals("arg2", cmd.getArgs().get(1));
        assertEquals(2, cmd.getOptions().size());
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertTrue(cmd.getOptionValue("a").equals("value1"));
        assertTrue(cmd.getOptionValue("b").equals("value2"));
    }

    /**
     * Test parsing with a long option.
     *
     * @throws Exception if parse fails.
     */
    @Test
    public void testParseLongOption() throws Exception {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("longopt", "long option", true, "Value for long option");
        String[] arguments = new String[]{"--longopt", "value"};
        CommandLine cmd = parser.parse(options, arguments);
        assertEquals(0, cmd.getArgs().size());
        assertEquals(1, cmd.getOptions().size());
        assertTrue(cmd.hasOption("longopt"));
        assertTrue(cmd.getOptionValue("longopt").equals("value"));
    }

    /**
     * Test parsing with a group of options.
     *
     * @throws Exception if parse fails.
     */
    @Test
    public void testParseOptionGroup() throws Exception {
        Parser parser = new DefaultParser();
        Options options = new Options();
        OptionGroup group = new OptionGroup("group");
        group.setRequired(false);
        group.addOption("a", "alpha", true, "Alpha value");
        group.addOption("b", "beta", true, "Beta value");
        options.addOptionGroup(group);
        String[] arguments = new String[0];
        CommandLine cmd = parser.parse(options, arguments);
        assertTrue(cmd.getArgs().isEmpty());
        assertTrue(cmd.getOptions().isEmpty());

        String[] argumentsWithGroup = new String[]{"-group", "-a", "value1"};
        cmd = parser.parse(options, argumentsWithGroup);
        assertEquals(1, cmd.getArgs().size());
        assertEquals("value1", cmd.getArgs().get(0));
        assertEquals(2, cmd.getOptions().size());
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertFalse(cmd.getOptionValue("b") != null);

        String[] argumentsWithBoth = new String[]{"-group", "-a", "value1", "-b", "value2"};
        cmd = parser.parse(options, argumentsWithBoth);
        assertEquals(1, cmd.getArgs().size());
        assertEquals("value1", cmd.getArgs().get(0));
        assertEquals(2, cmd.getOptions().size());
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertTrue(cmd.getOptionValue("b").equals("value2"));
    }

    /**
     * Test parsing with a single option that accepts multiple values.
     *
     * @throws Exception if parse fails.
     */
    @Test
    public void testParseMultipleValues() throws Exception {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", "alpha", true, "Alpha value");
        options.getOption("a").setArgs(Option.UNLIMITED_VALUES);
        String[] arguments = new String[]{"-a", "value1", "-a", "value2", "-a", "value3"};
        CommandLine cmd = parser.parse(options, arguments);
        assertEquals(0, cmd.getArgs().size());
        assertEquals(3, cmd.getOptions().size());
        assertTrue(cmd.hasOption("a"));
        List<String> values = cmd.getOptionValues("a");
        assertEquals(3, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
        assertEquals("value3", values.get(2));
    }

    /**
     * Test parsing with a single option that accepts a single value.
     *
     * @throws Exception if parse fails.
     */
    @Test
    public void testParseSingleValue() throws Exception {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", "alpha", true, "Alpha value");
        String[] arguments = new String[]{"-a", "value"};
        CommandLine cmd = parser.parse(options, arguments);
        assertEquals(0, cmd.getArgs().size());
        assertEquals(1, cmd.getOptions().size());
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.getOptionValue("a").equals("value"));
    }

    /**
     * Test parsing with a long option that accepts multiple values.
     *
     * @throws Exception if parse fails.
     */
    @Test
    public void testParseMultipleValuesLongOption() throws Exception {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("longopt", "long option", true, "Long option value");
        options.getOption("longopt").setArgs(Option.UNLIMITED_VALUES);
        String[] arguments = new String[]{"--longopt", "value1", "--longopt", "value2", "--longopt", "value3"};
        CommandLine cmd = parser.parse(options, arguments);
        assertEquals(0, cmd.getArgs().size());
        assertEquals(1, cmd.getOptions().size());
        assertTrue(cmd.hasOption("longopt"));
        List<String> values = cmd.getOptionValues("longopt");
        assertEquals(3, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
        assertEquals("value3", values.get(2));
    }

    /**
     * Test parsing with a long option that accepts a single value.
     *
     * @throws Exception if parse fails.
     */
    @Test
    public void testParseSingleValueLongOption() throws Exception {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("longopt", "long option", true, "Long option value");
        String[] arguments = new String[]{"--longopt", "value"};
        CommandLine cmd = parser.parse(options, arguments);
        assertEquals(0, cmd.getArgs().size());
        assertEquals(1, cmd.getOptions().size());
        assertTrue(cmd.hasOption("longopt"));
        assertTrue(cmd.getOptionValue("longopt").equals("value"));
    }

    /**
     * Test parsing with a long option that requires an argument.
     *
     * @throws Exception if parse fails.
     */
    @Test
    public void testParseLongOptionWithArg() throws Exception {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("longopt", "long option", true, "Long option value");
        options.getOption("longopt").setArgName("arg");
        String[] arguments = new String[]{"--longopt", "arg1", "arg2"};
        CommandLine cmd = parser.parse(options, arguments);
        assertEquals(0, cmd.getArgs().size());
        assertEquals(1, cmd.getOptions().size());
        assertTrue(cmd.hasOption("longopt"));
        assertTrue(cmd.getOptionValue("longopt").equals("arg1 arg2"));
    }

    /**
     * Test parsing with a long option that requires a single argument.
     *
     * @throws Exception if parse fails.
     */
    @Test
    public void testParseSingleValueLongOptionWithArg() throws Exception {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("longopt", "long option", true, "Long option value");
        options.getOption("longopt").setArgName("arg");
        options.getOption("longopt").setRequiredArg(true);
        String[] arguments = new String[]{"--longopt", "arg"};
        CommandLine cmd = parser.parse(options, arguments);
        assertEquals(0, cmd.getArgs().size());
        assertEquals(1, cmd.getOptions().size());
        assertTrue(cmd.hasOption("longopt"));
        assertTrue(cmd.getOptionValue("longopt").equals("arg"));
    }
}
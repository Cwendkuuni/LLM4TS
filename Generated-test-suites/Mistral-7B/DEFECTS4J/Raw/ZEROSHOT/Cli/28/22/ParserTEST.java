package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Test;

/**
 * Test class for {@link Parser}.
 */
public class ParserTest
{
    /**
     * Test parsing with simple options.
     *
     * @throws ParseException if parsing fails.
     */
    @Test
    public void testSimpleOptions() throws ParseException
    {
        String[] args = new String[]{"-a", "-b"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertFalse(cmd.getArgs().hasRemaining());
    }

    /**
     * Test parsing with required options.
     *
     * @throws ParseException if parsing fails.
     */
    @Test
    public void testRequiredOptions() throws ParseException
    {
        String[] args = new String[]{"-a", "-b"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addRequiredOption("c", true, "Option C");
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertTrue(cmd.hasOption("c"));
        assertFalse(cmd.getArgs().hasRemaining());
    }

    /**
     * Test parsing with optional options and arguments.
     *
     * @throws ParseException if parsing fails.
     */
    @Test
    public void testOptionalOptionsAndArgs() throws ParseException
    {
        String[] args = new String[]{"-a", "--value", "123"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("value", true, "Value");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        assertTrue(cmd.hasOption("a"));
        assertEquals("123", cmd.getOptionValue("value"));
        assertFalse(cmd.getArgs().hasRemaining());
    }

    /**
     * Test parsing with a long option.
     *
     * @throws ParseException if parsing fails.
     */
    @Test
    public void testLongOption() throws ParseException
    {
        String[] args = new String[]{"--long-option"};
        Options options = new Options();
        options.addOption("longOption", true, "Long option");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        assertTrue(cmd.hasOption("longOption"));
        assertFalse(cmd.getArgs().hasRemaining());
    }

    /**
     * Test parsing with a long option and an argument.
     *
     * @throws ParseException if parsing fails.
     */
    @Test
    public void testLongOptionWithArg() throws ParseException
    {
        String[] args = new String[]{"--long-option", "123"};
        Options options = new Options();
        options.addOption("longOption", true, "Long option");
        options.addOption("value", true, true, "Value");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        assertTrue(cmd.hasOption("longOption"));
        assertEquals("123", cmd.getOptionValue("value"));
        assertFalse(cmd.getArgs().hasRemaining());
    }

    /**
     * Test parsing with multiple long options.
     *
     * @throws ParseException if parsing fails.
     */
    @Test
    public void testMultipleLongOptions() throws ParseException
    {
        String[] args = new String[]{"--long-option1", "--long-option2"};
        Options options = new Options();
        options.addOption("longOption1", true, "Long option 1");
        options.addOption("longOption2", true, "Long option 2");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        assertTrue(cmd.hasOption("longOption1"));
        assertTrue(cmd.hasOption("longOption2"));
        assertFalse(cmd.getArgs().hasRemaining());
    }

    /**
     * Test parsing with a short option and a long option.
     *
     * @throws ParseException if parsing fails.
     */
    @Test
    public void testShortAndLongOption() throws ParseException
    {
        String[] args = new String[]{"-a", "--long-option"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("longOption", true, "Long option");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("longOption"));
        assertFalse(cmd.getArgs().hasRemaining());
    }

    /**
     * Test parsing with multiple short options.
     *
     * @throws ParseException if parsing fails.
     */
    @Test
    public void testMultipleShortOptions() throws ParseException
    {
        String[] args = new String[]{"-a", "-b"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertFalse(cmd.getArgs().hasRemaining());
    }

    /**
     * Test parsing with a short option and a long option with the same letter.
     *
     * @throws ParseException if parsing fails.
     */
    @Test(expected = UnrecognizedOptionException.class)
    public void testShortAndLongOptionWithSameLetter() throws ParseException
    {
        String[] args = new String[]{"-a", "--another"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("another", true, "Another option");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);
    }

    /**
     * Test parsing with a short option with an argument.
     *
     * @throws ParseException if parsing fails.
     */
    @Test
    public void testShortOptionWithArg() throws ParseException
    {
        String[] args = new String[]{"-a", "value"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("value", true, true, "Value");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        assertTrue(cmd.hasOption("a"));
        assertEquals("value", cmd.getOptionValue("value"));
        assertFalse(cmd.getArgs().hasRemaining());
    }

    /**
     * Test parsing with multiple short options with arguments.
     *
     * @throws ParseException if parsing fails.
     */
    @Test
    public void testMultipleShortOptionsWithArgs() throws ParseException
    {
        String[] args = new String[]{"-a", "value1", "-b", "value2"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("value", true, true, "Value");
        options.addOption("b", true, "Option B");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        assertTrue(cmd.hasOption("a"));
        assertEquals("value1", cmd.getOptionValue("value"));
        assertTrue(cmd.hasOption("b"));
        assertFalse(cmd.getArgs().hasRemaining());
    }

    /**
     * Test parsing with an unrecognized short option.
     *
     * @throws ParseException if parsing fails.
     */
    @Test(expected = UnrecognizedOptionException.class)
    public void testUnrecognizedShortOption() throws ParseException
    {
        String[] args = new String[]{"-x", "value"};
        Options options = new Options();
        options.addOption("a", true, "Option A");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);
    }

    /**
     * Test parsing with an unrecognized long option.
     *
     * @throws ParseException if parsing fails.
     */
    @Test(expected = UnrecognizedOptionException.class)
    public void testUnrecognizedLongOption() throws ParseException
    {
        String[] args = new String[]{"--unrecognized"};
        Options options = new Options();
        options.addOption("a", true, "Option A");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);
    }

    /**
     * Test parsing with a missing required option.
     *
     * @throws ParseException if parsing fails.
     */
    @Test(expected = MissingOptionException.class)
    public void testMissingRequiredOption() throws ParseException
    {
        String[] args = new String[]{"-a"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addRequiredOption("b", true, "Option B");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);
    }

    /**
     * Test parsing with a missing required option group.
     *
     * @throws ParseException if parsing fails.
     */
    @Test(expected = MissingOptionException.class)
    public void testMissingRequiredOptionGroup() throws ParseException
    {
        String[] args = new String[]{"-a"};
        Options options = new Options();
        OptionGroup group = new OptionGroup("group");
        group.setRequired(true);
        options.addOptionGroup(group);
        options.addOption("a", true, "Option A", group);

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);
    }

    /**
     * Test parsing with a missing required long option.
     *
     * @throws ParseException if parsing fails.
     */
    @Test(expected = MissingOptionException.class)
    public void testMissingRequiredLongOption() throws ParseException
    {
        String[] args = new String[]{"--longOption"};
        Options options = new Options();
        options.addOption("longOption", true, "Option A", new OptionGroup("group"));
        options.addRequiredOption("requiredOption", true, "Required option");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);
    }

    /**
     * Test parsing with an incorrect number of arguments.
     *
     * @throws ParseException if parsing fails.
     */
    @Test(expected = MissingArgumentException.class)
    public void testMissingArgument() throws ParseException
    {
        String[] args = new String[]{"-a"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("value", true, true, "Value");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);
    }

    /**
     * Test parsing with an incorrect number of arguments for an option with an optional argument.
     *
     * @throws ParseException if parsing fails.
     */
    @Test
    public void testExtraArgumentForOptionalArgument() throws ParseException
    {
        String[] args = new String[]{"-a", "value1", "value2"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("value", true, true, "Value");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        assertEquals(1, cmd.getArgs().size());
        assertEquals("value2", cmd.getArgs().get(0));
    }

    /**
     * Test parsing with mixed short and long options.
     *
     * @throws ParseException if parsing fails.
     */
    @Test
    public void testMixedShortAndLongOptions() throws ParseException
    {
        String[] args = new String[]{"-a", "--longOption"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("longOption", true, "Option B");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("longOption"));
        assertFalse(cmd.getArgs().hasRemaining());
    }

    /**
     * Test parsing with long options that have the same name but different letters.
     *
     * @throws ParseException if parsing fails.
     */
    @Test
    public void testLongOptionsWithSameNameDifferentLetters() throws ParseException
    {
        String[] args = new String[]{"--longOption1", "--longOption2"};
        Options options = new Options();
        options.addOption("longOption1", true, "Option 1");
        options.addOption("longOption2", true, "Option 2");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        assertTrue(cmd.hasOption("longOption1"));
        assertTrue(cmd.hasOption("longOption2"));
        assertFalse(cmd.getArgs().hasRemaining());
    }

    /**
     * Test parsing with properties.
     *
     * @throws ParseException if parsing fails.
     */
    @Test
    public void testProperties() throws ParseException
    {
        String[] args = new String[]{"-p", "--property=value"};
        Properties properties = new Properties();
        properties.setProperty("property", "value");

        Options options = new Options();
        options.addOption("p", true, "Property file", true, "propertyFile");
        options.addOption("property", true, true, "Property");
        options.addPropertySetter(Properties.class, "properties");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, properties);

        assertTrue(cmd.hasOption("p"));
        assertFalse(cmd.getArgs().hasRemaining());
        assertEquals(properties, cmd.getParsedProperties());
    }

    /**
     * Test parsing with a mixture of short and long options and properties.
     *
     * @throws ParseException if parsing fails.
     */
    @Test
    public void testMixedOptionsAndProperties() throws ParseException
    {
        String[] args = new String[]{"-a", "-b", "--property=value"};
        Properties properties = new Properties();
        properties.setProperty("property", "value");

        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("property", true, true, "Property");
        options.addPropertySetter(Properties.class, "properties");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, properties);

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertFalse(cmd.getArgs().hasRemaining());
        assertEquals(properties, cmd.getParsedProperties());
    }
}
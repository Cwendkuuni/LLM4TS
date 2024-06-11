package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() throws Exception {
        final String[] args = { "--foo", "-bar", "baz", "-foo=qux", "--", "-" };
        final Options options = new Options();
        options.addOption(new Option("f", "foo", true, "Sets foo"));
        options.addOption(new Option("b", "bar", false, "Sets bar"));
        options.addOption(new Option("o", "opt", true, "Sets opt"));
        options.addOption(new Option("h", "help", false, "Print this help message."));

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        assertEquals(args.length, cmd.getArgs().size());
        assertNotNull(cmd.getOption("foo"));
        assertFalse(cmd.hasOption("foo2"));
        assertNull(cmd.getOptionValue("foo"));
        assertTrue(cmd.hasOption("foo"));
        assertEquals("qux", cmd.getOptionValue("foo", "defaultValue"));
        assertEquals("baz", cmd.getArgs().get(2));
        assertNull(cmd.getOptionValue("nonExistentOption"));
        assertSame(options.getOption("foo"), cmd.getOption("foo"));
        assertFalse(cmd.getBooleanOption("help"));
    }

    @Test
    public void testParseWithProperties() throws Exception {
        final String[] args = { "--foo=qux", "-bar=quux", "--" };
        final Options options = new Options();
        options.addOption(new Option("f", "foo", true, "Sets foo"));
        options.addOption(new Option("b", "bar", true, "Sets bar"));

        Properties properties = new Properties();
        properties.setProperty("foo", "qux");
        properties.setProperty("bar", "quux");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, properties);

        assertEquals(args.length, cmd.getArgs().size());
        assertEquals("qux", cmd.getOptionValue("foo"));
        assertEquals("quux", cmd.getOptionValue("bar"));
        assertNull(cmd.getOptionValue("nonExistentOption"));
    }

    @Test(expected = ParseException.class)
    public void testParseWithMissingRequiredOption() throws Exception {
        final String[] args = { "-b" };
        final Options options = new Options();
        options.addOption(new Option("f", "foo", true, "Sets foo"));
        options.addOption(new Option("b", "bar", true, "Sets bar"));

        Parser parser = new DefaultParser();
        parser.parse(options, args);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseWithUnrecognizedOption() throws Exception {
        final String[] args = { "--foo2" };
        final Options options = new Options();
        options.addOption(new Option("f", "foo", true, "Sets foo"));

        Parser parser = new DefaultParser();
        parser.parse(options, args);
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseWithMissingArgument() throws Exception {
        final String[] args = { "-f" };
        final Options options = new Options();
        options.addOption(new Option("f", "foo", true, "Sets foo"));

        Parser parser = new DefaultParser();
        parser.parse(options, args);
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseWithMissingOptionalArgument() throws Exception {
        final String[] args = { "-f" };
        final Options options = new Options();
        options.addOption(new Option("f", "foo", true, "Sets foo"));

        Parser parser = new DefaultParser();
        parser.parse(options, args, null, true);
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidBooleanOption() throws Exception {
        final String[] args = { "--foo=qux", "--bar=quux=false", "--" };
        final Options options = new Options();
        options.addOption(new Option("f", "foo", true, "Sets foo"));
        options.addOption(new Option("b", "bar", false, "Sets bar"));

        Parser parser = new DefaultParser();
        parser.parse(options, args);
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidValueTypeOption() throws Exception {
        final String[] args = { "--foo=qux", "--bar=quux=123", "--" };
        final Options options = new Options();
        options.addOption(new Option("f", "foo", true, "Sets foo"));
        options.addOption(new Option("b", "bar", true, "Sets bar"));

        Parser parser = new DefaultParser();
        parser.parse(options, args);
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidOptionGroupOption() throws Exception {
        final String[] args = { "--foo", "--bar1", "--bar2" };
        final Options options = new Options();
        final OptionGroup barGroup = new OptionGroup("bar-group", "Bar options");
        options.addOptionGroup(barGroup);
        options.addOption(new Option("f", "foo", true, "Sets foo"));
        options.addOption(new Option("b1", "bar1", false, "Sets bar1"));
        options.addOption(new Option("b2", "bar2", false, "Sets bar2"));

        Parser parser = new DefaultParser();
        parser.parse(options, args);
    }

    @Test(expected = ParseException.class)
    public void testParseWithDuplicateOption() throws Exception {
        final String[] args = { "--foo", "--foo" };
        final Options options = new Options();
        options.addOption(new Option("f", "foo", true, "Sets foo"));

        Parser parser = new DefaultParser();
        parser.parse(options, args);
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidOptionFormat() throws Exception {
        final String[] args = { "--foo=qux=123", "--" };
        final Options options = new Options();
        options.addOption(new Option("f", "foo", true, "Sets foo"));

        Parser parser = new DefaultParser();
        parser.parse(options, args);
    }

    @Test
    public void testParseWithLongOptions() throws Exception {
        final String[] args = { "--long-option-with-arg=value" };
        final Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("longOptionWithArg")
                                        .hasArg(true)
                                        .isRequired(false)
                                        .build());

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);
        assertEquals("value", cmd.getOptionValue("longOptionWithArg"));
    }

    @Test
    public void testParseWithShortAndLongOptions() throws Exception {
        final String[] args = { "-f", "--foo=value" };
        final Options options = new Options();
        options.addOption(new Option("f", "foo", true, "Sets foo"));
        options.addOption(OptionBuilder.withLongOpt("foo")
                                        .hasArg(true)
                                        .isRequired(false)
                                        .build());

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);
        assertEquals("value", cmd.getOptionValue("foo"));
    }

    @Test
    public void testParseWithShortAndLongOptionsWithNoValue() throws Exception {
        final String[] args = { "-f", "--foo" };
        final Options options = new Options();
        options.addOption(new Option("f", "foo", true, "Sets foo"));
        options.addOption(OptionBuilder.withLongOpt("foo")
                                        .isRequired(false)
                                        .build());

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);
        assertFalse(cmd.hasOption("foo"));
    }

    @Test
    public void testParseWithShortAndLongOptionsWithBothValue() throws Exception {
        final String[] args = { "-f", "--foo=value", "--foo2" };
        final Options options = new Options();
        options.addOption(new Option("f", "foo", true, "Sets foo"));
        options.addOption(OptionBuilder.withLongOpt("foo")
                                        .hasArg(true)
                                        .isRequired(false)
                                        .build());
        options.addOption(new Option("f2", "foo2", true, "Sets foo2"));

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);
        assertEquals("value", cmd.getOptionValue("foo"));
        assertEquals(null, cmd.getOptionValue("foo2"));
    }

    @Test
    public void testParseWithOptionGroup() throws Exception {
        final String[] args = { "--mygroup", "--foo", "--bar" };
        final Options options = new Options();
        final OptionGroup myGroup = new OptionGroup("mygroup", "My group");
        options.addOptionGroup(myGroup);
        myGroup.addOption(new Option("f", "foo", false, "Sets foo"));
        myGroup.addOption(new Option("b", "bar", false, "Sets bar"));

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);
        assertTrue(cmd.hasOption("foo"));
        assertTrue(cmd.hasOption("bar"));
    }

    @Test
    public void testParseWithOptionGroupAndRequiredOption() throws Exception {
        final String[] args = { "--mygroup", "--foo" };
        final Options options = new Options();
        final OptionGroup myGroup = new OptionGroup("mygroup", "My group");
        options.addOptionGroup(myGroup);
        myGroup.addOption(new Option("f", "foo", true, "Sets foo"));

        Parser parser = new DefaultParser();
        try {
            parser.parse(options, args);
            fail("Expected MissingOptionException");
        } catch (MissingOptionException e) {
            // expected
            assertEquals("Requires additional argument: foo", e.getMessage());
        }
    }

    @Test
    public void testParseWithOptionGroupAndOptionalOption() throws Exception {
        final String[] args = { "--mygroup", "--foo" };
        final Options options = new Options();
        final OptionGroup myGroup = new OptionGroup("mygroup", "My group");
        options.addOptionGroup(myGroup);
        myGroup.addOption(new Option("f", "foo", false, "Sets foo"));

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, null, true);
        assertTrue(cmd.hasOption("foo"));
        assertEquals(new ArrayList(), cmd.getArgs());
    }

    @Test
    public void testParseWithOptionGroupAndOptionalOptionWithValue() throws Exception {
        final String[] args = { "--mygroup", "--foo=value" };
        final Options options = new Options();
        final OptionGroup myGroup = new OptionGroup("mygroup", "My group");
        options.addOptionGroup(myGroup);
        myGroup.addOption(new Option("f", "foo", true, "Sets foo"));

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, null, true);
        assertEquals("value", cmd.getOptionValue("foo"));
        assertEquals(new ArrayList(), cmd.getArgs());
    }

    @Test
    public void testParseWithOptionGroupAndRequiredOptionWithValue() throws Exception {
        final String[] args = { "--mygroup", "--foo=value" };
        final Options options = new Options();
        final OptionGroup myGroup = new OptionGroup("mygroup", "My group");
        options.addOptionGroup(myGroup);
        myGroup.addOption(new Option("f", "foo", true, "Sets foo"));

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);
        assertEquals("value", cmd.getOptionValue("foo"));
        assertEquals(new ArrayList(), cmd.getArgs());
    }

    @Test
    public void testParseWithOptionGroupAndRequiredOptionWithNoValue() throws Exception {
        final String[] args = { "--mygroup", "--foo" };
        final Options options = new Options();
        final OptionGroup myGroup = new OptionGroup("mygroup", "My group");
        options.addOptionGroup(myGroup);
        myGroup.addOption(new Option("f", "foo", true, "Sets foo"));

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);
        assertTrue(cmd.hasOption("foo"));
        assertEquals(new ArrayList(), cmd.getArgs());
    }

    @Test
    public void testParseWithOptionGroupAndOptionalOptionWithNoValue() throws Exception {
        final String[] args = { "--mygroup", "--foo" };
        final Options options = new Options();
        final OptionGroup myGroup = new OptionGroup("mygroup", "My group");
        options.addOptionGroup(myGroup);
        myGroup.addOption(new Option("f", "foo", false, "Sets foo"));

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, null, true);
        assertTrue(cmd.hasOption("foo"));
        assertEquals(new ArrayList(), cmd.getArgs());
    }

    @Test
    public void testParseWithOptionGroupAndOptionalOptionWithValue() throws Exception {
        final String[] args = { "--mygroup", "--foo=value" };
        final Options options = new Options();
        final OptionGroup myGroup = new OptionGroup("mygroup", "My group");
        options.addOptionGroup(myGroup);
        myGroup.addOption(new Option("f", "foo", true, "Sets foo"));

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, null, true);
        assertEquals("value", cmd.getOptionValue("foo"));
        assertEquals(new ArrayList(), cmd.getArgs());
    }

    @Test
    public void testParseWithOptionGroupAndOptionalOptionWithMultipleValues() throws Exception {
        final String[] args = { "--mygroup", "--foo=value1", "--foo=value2" };
        final Options options = new Options();
        final OptionGroup myGroup = new OptionGroup("mygroup", "My group");
        options.addOptionGroup(myGroup);
        myGroup.addOption(new Option("f", "foo", true, "Sets foo"));

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, null, true);
        List<String> foos = cmd.getOptionValues("foo");
        assertEquals(Arrays.asList("value1", "value2"), foos);
        assertEquals(new ArrayList(), cmd.getArgs());
    }

    @Test
    public void testParseWithOptionGroupAndOptionalOptionWithMultipleValuesAndArgs() throws Exception {
        final String[] args = { "--mygroup", "--foo=value1", "arg1", "--foo=value2", "arg2" };
        final Options options = new Options();
        final OptionGroup myGroup = new OptionGroup("mygroup", "My group");
        options.addOptionGroup(myGroup);
        myGroup.addOption(new Option("f", "foo", true, "Sets foo"));

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, null, true);
        List<String> foos = cmd.getOptionValues("foo");
        assertEquals(Arrays.asList("value1", "value2"), foos);
        assertEquals(Arrays.asList("arg1", "arg2"), cmd.getArgs());
    }
}
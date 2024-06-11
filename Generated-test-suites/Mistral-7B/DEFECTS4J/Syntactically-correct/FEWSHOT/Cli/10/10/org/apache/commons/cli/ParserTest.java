package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class ParserTest {

    @Test
    public void testParseWithEmptyArguments() throws ParseException {
        Options options = new Options();
        String[] arguments = new String[0];

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        CommandLine cmd = parser.parse(arguments);

        assertArrayEquals(new String[0], cmd.getArgs());
        assertEquals(0, cmd.getOptions().size());
    }

    @Test
    public void testParseWithSimpleOptions() throws ParseException {
        String[] arguments = new String[]{"-a", "1", "--b=2", "--c"};

        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", false, "Option C");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        CommandLine cmd = parser.parse(arguments);

        List<Option> optionsList = cmd.getOptions();

        Option optionA = optionsList.get(0);
        assertEquals("1", optionA.getValue());
        assertFalse(optionA.hasArg());

        Option optionB = optionsList.get(1);
        assertEquals("2", optionB.getValue());
        assertFalse(optionB.hasArg());

        Option optionC = optionsList.get(2);
        assertNull(optionC.getValue());
        assertFalse(optionC.hasArg());

        assertEquals(3, cmd.getArgs().length);
    }

    @Test
    public void testParseWithSimpleOptionsAndArguments() throws ParseException {
        String[] arguments = new String[]{"--foo=bar", "-a", "1", "arg1", "arg2"};

        Options options = new Options();
        options.addOption("foo", true, "Option FOO");
        options.addOption("a", true, "Option A");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        CommandLine cmd = parser.parse(arguments);

        List<Option> optionsList = cmd.getOptions();

        Option optionFoo = optionsList.get(0);
        assertEquals("bar", optionFoo.getValue());
        assertTrue(optionFoo.hasArg());

        Option optionA = optionsList.get(1);
        assertEquals("1", optionA.getValue());
        assertFalse(optionA.hasArg());

        assertEquals(3, cmd.getArgs().length);
        assertEquals("arg1", cmd.getArgs()[2]);
        assertEquals("arg2", cmd.getArgs()[3]);
    }

    @Test
    public void testParseWithRequiredOptions() throws ParseException {
        String[] arguments = new String[]{"-a"};

        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addRequiredOption("c", false, "Option C");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        try {
            parser.parse(arguments);
            fail("MissingOptionException expected");
        } catch (MissingOptionException e) {
            assertEquals("Option C is required.", e.getMessage());
        }

        assertEquals(1, cmd.getArgs().length);
    }

    @Test
    public void testParseWithLongOptions() throws ParseException {
        String[] arguments = new String[]{"--long-option"};

        Options options = new Options();
        options.addOption("long-option", false, "Long Option");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        CommandLine cmd = parser.parse(arguments);

        Option option = cmd.getOption("long-option");
        assertEquals(true, option.isSet());
    }

    @Test
    public void testParseWithOptionsInGroup() throws ParseException {
        String[] arguments = new String[]{"--foo", "--bar"};

        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "Group");
        group.addOption(new Option("foo", false, null, "Foo"));
        group.addOption(new Option("bar", false, null, "Bar"));
        options.addOptionGroup(group);

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        CommandLine cmd = parser.parse(arguments);

        Option optionFoo = cmd.getOption("foo");
        Option optionBar = cmd.getOption("bar");

        assertTrue(optionFoo.isSet());
        assertTrue(optionBar.isSet());
    }

    @Test
    public void testParseWithCustomParser() throws ParseException {
        String[] arguments = new String[]{"--foo=bar", "-a", "1", "arg1", "arg2"};

        Options options = new Options();
        options.addOption("foo", true, "Option FOO");
        options.addOption("a", true, "Option A");

        Parser parser = new MyParser();
        parser.setOptions(options);

        CommandLine cmd = parser.parse(arguments);

        List<Option> optionsList = cmd.getOptions();

        Option optionFoo = optionsList.get(0);
        assertEquals("bar", optionFoo.getValue());
        assertTrue(optionFoo.hasArg());

        Option optionA = optionsList.get(1);
        assertEquals("1", optionA.getValue());
        assertFalse(optionA.hasArg());

        assertEquals(3, cmd.getArgs().length);
        assertEquals("arg1", cmd.getArgs()[2]);
        assertEquals("arg2", cmd.getArgs()[3]);
    }

    @Test
    public void testParseWithMixedOptionsAndArguments() throws ParseException {
        String[] arguments = new String[]{"--foo=bar", "-a", "1", "-b", "arg1", "arg2"};

        Options options = new Options();
        options.addOption("foo", true, "Option FOO");
        options.addRequiredOption("a", false, "Option A");
        options.addOption("b", true, "Option B");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        CommandLine cmd = parser.parse(arguments);

        List<Option> optionsList = cmd.getOptions();

        Option optionFoo = optionsList.get(0);
        assertEquals("bar", optionFoo.getValue());
        assertTrue(optionFoo.hasArg());

        Option optionA = optionsList.get(1);
        assertEquals("1", optionA.getValue());
        assertFalse(optionA.hasArg());

        Option optionB = optionsList.get(2);
        assertNull(optionB.getValue());
        assertFalse(optionB.hasArg());

        assertEquals(5, cmd.getArgs().length);
        assertEquals("arg1", cmd.getArgs()[3]);
        assertEquals("arg2", cmd.getArgs()[4]);
    }

    @Test
    public void testParseWithDashAsArgument() throws ParseException {
        String[] arguments = new String[]{"--arg=--"};

        Options options = new Options();
        options.addOption("arg", true, "Option ARG");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        CommandLine cmd = parser.parse(arguments);

        Option optionArg = cmd.getOption("arg");
        assertEquals("--", optionArg.getValue());
    }

    @Test
    public void testParseWithMultipleValuesForOption() throws ParseException {
        String[] arguments = new String[]{"--foo=val1,val2"};

        Options options = new Options();
        options.addOption("foo", true, "Option FOO");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        CommandLine cmd = parser.parse(arguments);

        Option optionFoo = cmd.getOption("foo");
        String[] values = optionFoo.getValues();
        assertEquals(2, values.length);
        assertEquals("val1", values[0]);
        assertEquals("val2", values[1]);
    }

    @Test
    public void testParseWithInvalidArgument() throws ParseException {
        String[] arguments = new String[]{"--arg=-"};

        Options options = new Options();
        options.addOption("arg", true, "Option ARG");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        try {
            parser.parse(arguments);
            fail("MissingArgumentException expected");
        } catch (MissingArgumentException e) {
            assertEquals("Missing argument for option: arg.", e.getMessage());
        }
    }

    @Test
    public void testParseWithOptionalArgButNoValue() throws ParseException {
        String[] arguments = new String[]{"-a"};

        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        try {
            parser.parse(arguments);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            assertEquals("Option 'a' requires an argument.", e.getMessage());
        }
    }

    @Test
    public void testParseWithInvalidOption() throws ParseException {
        String[] arguments = new String[]{"--invalid-option"};

        Options options = new Options();
        options.addOption("option1", true, "Option 1");
        options.addOption("option2", true, "Option 2");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        try {
            parser.parse(arguments);
            fail("UnrecognizedOptionException expected");
        } catch (UnrecognizedOptionException e) {
            assertEquals("Unrecognized option: invalid-option", e.getMessage());
        }
    }

    @Test
    public void testParseWithNameValueProperties() throws ParseException {
        String[] arguments = new String[]{"--foo=bar", "-a", "1", "--prop1=value1", "--prop2=value2"};

        Properties properties = new Properties();
        properties.setProperty("prop1", "value1");
        properties.setProperty("prop2", "value2");

        Options options = new Options();
        options.addOption("foo", true, "Option FOO");
        options.addOption("a", true, "Option A");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        CommandLine cmd = parser.parse(arguments, properties);

        List<Option> optionsList = cmd.getOptions();

        Option optionFoo = optionsList.get(0);
        assertEquals("bar", optionFoo.getValue());
        assertTrue(optionFoo.hasArg());

        Option optionA = optionsList.get(1);
        assertEquals("1", optionA.getValue());
        assertFalse(optionA.hasArg());

        assertEquals(4, cmd.getArgs().length);
        assertEquals("", cmd.getArgs()[2]);
        assertEquals("", cmd.getArgs()[3]);

        Properties parsedProperties = cmd.getParsedProperties();
        assertEquals(2, parsedProperties.size());
        assertEquals("value1", parsedProperties.getProperty("prop1"));
        assertEquals("value2", parsedProperties.getProperty("prop2"));
    }

    @Test
    public void testParseWithNameValuePropertiesAndOptionalArgs() throws ParseException {
        String[] arguments = new String[]{"--foo=bar", "--prop1=value1", "-a", "1", "arg1", "arg2"};

        Properties properties = new Properties();
        properties.setProperty("prop1", "value1");

        Options options = new Options();
        options.addOption("foo", true, "Option FOO");
        options.addOption("a", true, "Option A");
        options.addOption("prop1", true, "Option PROP1");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        CommandLine cmd = parser.parse(arguments, properties);

        List<Option> optionsList = cmd.getOptions();

        Option optionFoo = optionsList.get(0);
        assertEquals("bar", optionFoo.getValue());
        assertTrue(optionFoo.hasArg());

        Option optionA = optionsList.get(1);
        assertEquals("1", optionA.getValue());
        assertFalse(optionA.hasArg());

        Option optionProp1 = optionsList.get(2);
        assertSame(optionProp1, cmd.getOption("prop1"));
        assertEquals("value1", optionProp1.getValue());

        assertEquals(5, cmd.getArgs().length);
        assertEquals("arg1", cmd.getArgs()[3]);
        assertEquals("arg2", cmd.getArgs()[4]);
    }

    @Test
    public void testParseWithNameValuePropertiesAndMultipleValuesForOption() throws ParseException {
        String[] arguments = new String[]{"--foo=val1,val2", "--prop1=value1,value2", "-a", "1", "arg1", "arg2"};

        Properties properties = new Properties();
        properties.setProperty("prop1", "value1,value2");

        Options options = new Options();
        options.addOption("foo", true, "Option FOO");
        options.addOption("a", true, "Option A");
        options.addOption("prop1", true, "Option PROP1");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        CommandLine cmd = parser.parse(arguments, properties);

        List<Option> optionsList = cmd.getOptions();

        Option optionFoo = optionsList.get(0);
        String[] values = optionFoo.getValues();
        assertEquals(2, values.length);
        assertEquals("val1", values[0]);
        assertEquals("val2", values[1]);

        Option optionA = optionsList.get(1);
        assertEquals("1", optionA.getValue());
        assertFalse(optionA.hasArg());

        Option optionProp1 = optionsList.get(2);
        String[] parsedValues = optionProp1.getValues();
        assertEquals(2, parsedValues.length);
        assertEquals("value1", parsedValues[0]);
        assertEquals("value2", parsedValues[1]);

        assertEquals(5, cmd.getArgs().length);
        assertEquals("arg1", cmd.getArgs()[3]);
        assertEquals("arg2", cmd.getArgs()[4]);
    }
}

class MyParser extends Parser {
    @Override
    protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
        // You can override the flatten method to implement custom logic here
        return super.flatten(opts, arguments, stopAtNonOption);
    }
}
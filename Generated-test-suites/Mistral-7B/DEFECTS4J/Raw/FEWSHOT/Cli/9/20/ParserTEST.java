package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.junit.Test;

public class ParserTest {

    @Test
    public void testParseEmpty() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine commandLine = parser.parse(options, new String[0]);
        assertEquals(0, commandLine.getArgs().size());
    }

    @Test
    public void testParseWithSingleArgument() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine commandLine = parser.parse(options, new String[]{"arg"});
        assertEquals(1, commandLine.getArgs().size());
        assertEquals("arg", commandLine.getArgs().get(0));
    }

    @Test
    public void testParseWithSingleOption() throws ParseException {
        Options options = new Options();
        Option option = new Option("o", "option", false, "Option description");
        options.addOption(option);
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine commandLine = parser.parse(options, new String[]{"-o"});
        assertEquals(1, commandLine.getOptions().size());
        Option opt = commandLine.getOption("o");
        assertTrue(opt.isSet());
    }

    @Test
    public void testParseWithSingleOptionAndArgument() throws ParseException {
        Options options = new Options();
        Option option = new Option("o", "option", true, "Option description");
        options.addOption(option);
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine commandLine = parser.parse(options, new String[]{"-o", "value"});
        assertEquals(1, commandLine.getOptions().size());
        Option opt = commandLine.getOption("o");
        assertTrue(opt.isSet());
        assertTrue(Arrays.asList(opt.getValues()).contains("value"));
    }

    @Test
    public void testParseWithMultipleOptions() throws ParseException {
        Options options = new Options();
        Option option1 = new Option("o1", "option1", false, "Option1 description");
        Option option2 = new Option("o2", "option2", false, "Option2 description");
        options.addOptions(Arrays.asList(option1, option2));
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine commandLine = parser.parse(options, new String[]{"-o1", "-o2"});
        assertEquals(2, commandLine.getOptions().size());
        Option opt1 = commandLine.getOption("o1");
        Option opt2 = commandLine.getOption("o2");
        assertTrue(opt1.isSet());
        assertTrue(opt2.isSet());
    }

    @Test
    public void testParseWithMultipleOptionsAndArguments() throws ParseException {
        Options options = new Options();
        Option option1 = new Option("o1", "option1", true, "Option1 description");
        Option option2 = new Option("o2", "option2", true, "Option2 description");
        options.addOptions(Arrays.asList(option1, option2));
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine commandLine = parser.parse(options, new String[]{"-o1", "arg1", "-o2", "arg2"});
        assertEquals(2, commandLine.getOptions().size());
        Option opt1 = commandLine.getOption("o1");
        Option opt2 = commandLine.getOption("o2");
        assertTrue(opt1.isSet());
        assertTrue(opt1.getValues().contains("arg1"));
        assertTrue(opt2.isSet());
        assertTrue(opt2.getValues().contains("arg2"));
    }

    @Test
    public void testParseWithRequiredOptions() throws ParseException {
        Options options = new Options();
        Option option1 = new Option("o1", "option1", true, "Option1 description");
        option1.setRequired(true);
        Option option2 = new Option("o2", "option2", true, "Option2 description");
        option2.setRequired(true);
        options.addOptions(Arrays.asList(option1, option2));
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        try {
            parser.parse(options, new String[0]);
            fail();
        } catch (MissingOptionException e) {
            // expected
        }
    }

    @Test
    public void testParseWithRequiredOptionsAndValues() throws ParseException {
        Options options = new Options();
        Option option1 = new Option("o1", "option1", true, "Option1 description");
        option1.setRequired(true);
        Option option2 = new Option("o2", "option2", true, "Option2 description");
        option2.setRequired(true);
        options.addOptions(Arrays.asList(option1, option2));
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine commandLine = parser.parse(options, new String[]{"-o1", "arg1", "-o2", "arg2"});
        assertEquals(2, commandLine.getOptions().size());
        Option opt1 = commandLine.getOption("o1");
        Option opt2 = commandLine.getOption("o2");
        assertTrue(opt1.isSet());
        assertTrue(opt1.getValues().contains("arg1"));
        assertTrue(opt2.isSet());
        assertTrue(opt2.getValues().contains("arg2"));
    }

    @Test
    public void testParseWithInvalidOption() throws ParseException {
        Options options = new Options();
        Option option = new Option("o", "option", false, "Option description");
        options.addOption(option);
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        try {
            parser.parse(options, new String[]{"invalid"});
            fail();
        } catch (UnrecognizedOptionException e) {
            // expected
        }
    }

    @Test
    public void testParseWithNoOptions() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine commandLine = parser.parse(options, new String[0]);
        assertEquals(0, commandLine.getOptions().size());
    }

    @Test
    public void testParseWithEmptyProperties() throws ParseException {
        Options options = new Options();
        Properties properties = new Properties();
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine commandLine = parser.parse(options, null, properties);
        assertEquals(0, commandLine.getOptions().size());
        assertEquals(0, commandLine.getArgs().size());
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Options options = new Options();
        Option option = new Option("o", "option", false, "Option description");
        options.addOption(option);
        Properties properties = new Properties();
        properties.setProperty("o", "value");
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine commandLine = parser.parse(options, null, properties);
        assertEquals(1, commandLine.getOptions().size());
        Option opt = commandLine.getOption("o");
        assertTrue(opt.isSet());
        assertTrue(Arrays.asList(opt.getValues()).contains("value"));
    }

    @Test
    public void testParseWithPropertiesAndArguments() throws ParseException {
        Options options = new Options();
        Option option = new Option("o", "option", false, "Option description");
        options.addOption(option);
        Properties properties = new Properties();
        properties.setProperty("o", "value");
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine commandLine = parser.parse(options, new String[]{"arg1", "arg2"}, properties);
        assertEquals(1, commandLine.getOptions().size());
        Option opt = commandLine.getOption("o");
        assertTrue(opt.isSet());
        assertTrue(Arrays.asList(opt.getValues()).contains("value"));
        assertEquals(2, commandLine.getArgs().size());
        assertEquals("arg1", commandLine.getArgs().get(0));
        assertEquals("arg2", commandLine.getArgs().get(1));
    }
}
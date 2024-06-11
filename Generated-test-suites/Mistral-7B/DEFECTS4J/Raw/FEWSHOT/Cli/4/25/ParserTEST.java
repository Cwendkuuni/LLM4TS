package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

public class ParserTest {

    @Test
    public void testParseWithEmptyArguments() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, new String[0]);
        assertNull(commandLine.getArgList().toArray(new String[0]));
        assertFalse(commandLine.getUnrecognizedArguments().isEmpty());
        assertNull(commandLine.getOptionValues("option1"));
        assertNull(commandLine.getOptionValues("option2"));
        assertNull(commandLine.getOptionValues("option3"));
    }

    @Test
    public void testParseWithSingleArgument() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();

        options.addOption("o", "option", true, "An option with a value");
        CommandLine commandLine = parser.parse(options, new String[]{"--option=value"});
        assertEquals(1, commandLine.getArgList().size());
        assertEquals("value", commandLine.getArgList().get(0));
        assertNull(commandLine.getUnrecognizedArguments().isEmpty());
        assertEquals("value", commandLine.getOptionValues("option"));
        assertNull(commandLine.getOptionValues("option2"));
        assertNull(commandLine.getOptionValues("option3"));
    }

    @Test
    public void testParseWithMultipleArguments() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();

        options.addOption("o", "option", true, "An option with a value");
        CommandLine commandLine = parser.parse(options, new String[]{"--option=value1", "--option=value2"});
        assertEquals(2, commandLine.getArgList().size());
        assertEquals("value1", commandLine.getArgList().get(0));
        assertEquals("value2", commandLine.getArgList().get(1));
        assertNull(commandLine.getUnrecognizedArguments().isEmpty());
        assertEquals("value1", commandLine.getOptionValues("option"));
        assertEquals(new String[]{"value1", "value2"}, commandLine.getOptionValues("option"));
    }

    @Test
    public void testParseWithOptionalArgument() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();

        options.addOption("o", "option", true, "An option with an optional argument");
        CommandLine commandLine = parser.parse(options, new String[]{"--option"});
        assertFalse(commandLine.getArgList().isEmpty());
        assertTrue(commandLine.getArgList().get(0).startsWith("-o"));
        assertNull(commandLine.getOptionValues("option"));
    }

    @Test
    public void testParseWithRequiredArgument() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();

        options.addOption("r", "required", true, "An required option with an argument");
        CommandLine commandLine = parser.parse(options, new String[0]);
        assertTrue(commandLine.getUnrecognizedArguments().size() > 0);
        assertFalse(commandLine.getArgList().isEmpty());
        assertFalse(commandLine.getOptionValues("required") == null);
        assertEquals(commandLine.getUnrecognizedArguments().get(0), commandLine.getOptionValues("required")[0]);
    }

    @Test
    public void testParseWithMultipleOptionalArguments() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();

        options.addOption("o", "option", true, "An option with an optional argument");
        CommandLine commandLine = parser.parse(options, new String[]{"--option1", "--option2"});
        assertEquals(2, commandLine.getArgList().size());
        assertTrue(commandLine.getArgList().get(0).startsWith("-o"));
        assertTrue(commandLine.getArgList().get(1).startsWith("-o"));
        assertNull(commandLine.getOptionValues("option"));
        assertEquals(new String[]{"--option1", "--option2"}, commandLine.getUnrecognizedArguments().toArray(new String[0]));
    }

    @Test
    public void testParseWithLongOptions() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();

        options.addOption("longOption", true, "A long option with a value");
        CommandLine commandLine = parser.parse(options, new String[]{"--longOption=value"});
        assertEquals(1, commandLine.getArgList().size());
        assertEquals("value", commandLine.getArgList().get(0));
        assertNull(commandLine.getUnrecognizedArguments().isEmpty());
        assertEquals("value", commandLine.getOptionValues("longOption"));
        assertNull(commandLine.getOptionValues("option"));
    }

    @Test
    public void testParseWithLongAndShortOptions() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();

        options.addOption("o", "option", true, "An option with a value");
        options.addOption("lo", "longOption", true, "A long option with a value");
        CommandLine commandLine = parser.parse(options, new String[]{"--option", "--longOption=value"});
        assertEquals(1, commandLine.getArgList().size());
        assertEquals("option", commandLine.getArgList().get(0));
        assertNull(commandLine.getUnrecognizedArguments().isEmpty());
        assertEquals("value", commandLine.getOptionValues("option"));
        assertEquals("value", commandLine.getOptionValues("longOption"));
    }

    @Test
    public void testParseWithMixedLongAndShortOptions() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();

        options.addOption("o", "option", true, "An option with a value");
        options.addOption("lo", "longOption", true, "A long option with a value");
        options.addOption("l", null, false, "A long-format option with no value");
        CommandLine commandLine = parser.parse(options, new String[]{"--option", "--longOption=value", "--l"});
        assertEquals(1, commandLine.getArgList().size());
        assertEquals("option", commandLine.getArgList().get(0));
        assertNull(commandLine.getUnrecognizedArguments().isEmpty());
        assertEquals("value", commandLine.getOptionValues("option"));
        assertEquals("value", commandLine.getOptionValues("longOption"));
        assertTrue(commandLine.hasOption("l"));
    }

    @Test
    public void testParseWithGroupedOptions() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();

        OptionGroup group = new OptionGroup("group", "This is a group");
        group.setRequired(true);
        group.addOption(new OptionBuilder("o1").longOpt("longOption1").hasArg().build());
        group.addOption(new OptionBuilder("o2").longOpt("longOption2").hasArg().build());
        options.addOptionGroup(group);

        CommandLine commandLine = parser.parse(options, new String[]{"--longOption1=value1", "--longOption2=value2"});
        assertEquals(2, commandLine.getArgList().size());
        assertTrue(commandLine.getArgList().get(0).startsWith("-o"));
        assertTrue(commandLine.getArgList().get(1).startsWith("-o"));
        assertNull(commandLine.getUnrecognizedArguments().isEmpty());
        assertEquals("value1", commandLine.getOptionValues("o1"));
        assertEquals("value2", commandLine.getOptionValues("o2"));
        assertFalse(commandLine.hasOption("o1"));
        assertFalse(commandLine.hasOption("o2"));
        assertTrue(commandLine.hasOption("group"));
    }

    @Test
    public void testParseWithGroupedOptionsAndOtherOptions() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();

        OptionGroup group = new OptionGroup("group", "This is a group");
        group.addOption(new OptionBuilder("o1").longOpt("longOption1").hasArg().build());
        group.addOption(new OptionBuilder("o2").longOpt("longOption2").hasArg().build());
        options.addOptionGroup(group);

        options.addOption(new OptionBuilder("o").longOpt("otherOption").hasArg().build());

        CommandLine commandLine = parser.parse(options, new String[]{"--longOption1=value1", "--longOption2=value2", "--otherOption=value3"});
        assertEquals(1, commandLine.getArgList().size());
        assertTrue(commandLine.getArgList().get(0).startsWith("-o"));
        assertNull(commandLine.getUnrecognizedArguments().isEmpty());
        assertEquals("value3", commandLine.getOptionValues("otherOption"));
        assertNull(commandLine.getOptionValues("o1"));
        assertNull(commandLine.getOptionValues("o2"));
        assertTrue(commandLine.hasOption("group"));
        assertTrue(commandLine.hasOption("otherOption"));
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseWithMissingRequiredArgument() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();

        options.addOption("r", "required", true, "A required option with an argument");
        CommandLine commandLine = parser.parse(options, new String[0]);
        assertTrue(commandLine.getUnrecognizedArguments().size() > 0);
    }

    @Test
    public void testParseWithUnrecognizedOption() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();

        options.addOption("o", "option", true, "An option with a value");
        CommandLine commandLine = parser.parse(options, new String[]{"--unrecognized"});
        assertTrue(commandLine.getUnrecognizedArguments().size() > 0);
        assertFalse(commandLine.hasOption("unrecognized"));
    }

    @Test
    public void testParseWithMultipleUnrecognizedOptions() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();

        options.addOption("o", "option", true, "An option with a value");
        CommandLine commandLine = parser.parse(options, new String[]{"--unrecognized1", "--unrecognized2"});
        assertTrue(commandLine.getUnrecognizedArguments().size() > 0);
        assertFalse(commandLine.hasOption("unrecognized1"));
        assertFalse(commandLine.hasOption("unrecognized2"));
    }

    @Test
    public void testParseWithEmptyOptions() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, new String[0]);
        assertNull(commandLine.getArgList().toArray(new String[0]));
        assertTrue(commandLine.getUnrecognizedArguments().isEmpty());
        assertNull(commandLine.getOptionValues("option1"));
        assertNull(commandLine.getOptionValues("option2"));
        assertNull(commandLine.getOptionValues("option3"));
    }

    @Test
    public void testParseWithEmptyArgumentsAndEmptyOptions() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, new String[0]);
        assertNull(commandLine.getArgList().toArray(new String[0]));
        assertTrue(commandLine.getUnrecognizedArguments().isEmpty());
        assertNull(commandLine.getOptionValues("option1"));
        assertNull(commandLine.getOptionValues("option2"));
        assertNull(commandLine.getOptionValues("option3"));
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();

        options.addOption("p", "property", true, "A property");

        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        properties.setProperty("property2", "value2");

        CommandLine commandLine = parser.parse(options, null, properties);
        assertNull(commandLine.getArgList().toArray(new String[0]));
        assertTrue(commandLine.getUnrecognizedArguments().isEmpty());
        assertEquals("value1", commandLine.getOptionValues("property"));
        assertEquals("value1", commandLine.getOptionValues("property1"));
        assertEquals("value2", commandLine.getOptionValues("property2"));
        assertEquals("value1", commandLine.getOptionValues("property1")[0]);
        assertEquals("value2", commandLine.getOptionValues("property2")[0]);
    }
}
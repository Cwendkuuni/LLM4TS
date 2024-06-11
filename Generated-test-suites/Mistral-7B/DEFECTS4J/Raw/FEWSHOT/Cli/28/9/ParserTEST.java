package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Properties;

public class ParserTest {

    private Options options;
    private Parser parser;

    @Before
    public void setUp() {
        options = new Options();
        options.addOption("a", true, "Help option A");
        options.addOption("b", 2, "Help option B with an argument");
        options.addOptionGroup(new OptionGroup("g", "Group"))
                .addOption(Option.builder("g1").longOpt("long-option1")
                        .required(true).build())
                .addOption(Option.builder("g2").longOpt("long-option2")
                        .required(false).build());
        parser = new DefaultParser();
        parser.setOptions(options);
    }

    @Test
    public void testParseWithNoArguments() throws ParseException {
        CommandLine cmd = parser.parse(new String[0][], null, null, false);
        assertTrue(cmd.hasOptions());
        assertTrue(cmd.getOptions().isEmpty());
        assertTrue(cmd.getArgs().isEmpty());
    }

    @Test
    public void testParseWithNoOptions() throws ParseException {
        CommandLine cmd = parser.parse(options.getArgs(), null, null, false);
        assertTrue(cmd.hasOptions());
        assertEquals(Collections.singletonList(options), cmd.getOptions());
        assertTrue(cmd.getArgs().isEmpty());
    }

    @Test
    public void testParseWithTwoOptions() throws ParseException {
        CommandLine cmd = parser.parse(new String[]{"-a", "--b=2"}, null, null, false);
        assertTrue(cmd.hasOptions());
        assertEquals(Arrays.asList(options.findOption("a"), options.findOption("b")), cmd.getOptions());
        assertTrue(cmd.getArgs().isEmpty());
    }

    @Test
    public void testParseWithOneRequiredOption() throws ParseException {
        CommandLine cmd = parser.parse(new String[]{"--long-option1"}, null, null, false);
        assertTrue(cmd.hasOptions());
        assertEquals(Arrays.asList(options.findOption("g1")), cmd.getOptions());
        assertTrue(cmd.getArgs().isEmpty());
    }

    @Test
    public void testParseWithOneOptionalOption() throws ParseException {
        CommandLine cmd = parser.parse(new String[]{"--long-option2"}, null, null, false);
        assertTrue(cmd.hasOptions());
        assertEquals(Arrays.asList(options.findOption("g2")), cmd.getOptions());
        assertTrue(cmd.getArgs().isEmpty());
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseWithUnrecognizedOption() throws ParseException {
        parser.parse(new String[]{"--unknown"}, null, null, false);
    }

    @Test(expected = MissingOptionException.class)
    public void testParseWithMissingRequiredOption() throws ParseException {
        parser.setOptions(options);
        parser.parse(new String[0][], null, null, false);
        throw new AssertionError("Expected MissingOptionException to be thrown");
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseWithMissingOptionArgument() throws ParseException {
        CommandLine cmd = parser.parse(options.getArgs(), null, null, false);
        Option optB = options.findOption("b");
        optB.setRequired(true);
        parser.setOptions(options);
        parser.parse(new String[0][], null, null, false);
        throw new AssertionError("Expected MissingArgumentException to be thrown");
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Properties props = new Properties();
        props.put("a", "true");
        props.put("b", "value");
        CommandLine cmd = parser.parse(options.getArgs(), null, props, false);
        assertTrue(cmd.hasOptions());
        assertEquals(Arrays.asList(options.findOption("a"), options.findOption("b")), cmd.getOptions());
        List<String> args = cmd.getArgs();
        assertEquals(Arrays.asList("value"), args);
    }

    @Test
    public void testParseWithBooleanValueProperty() throws ParseException {
        Properties props = new Properties();
        props.put("a", "false");
        CommandLine cmd = parser.parse(new String[0][], null, props, false);
        Option optA = options.findOption("a");
        assertFalse(optA.getValue().equals(Boolean.TRUE));
        assertTrue(optA.getValue().equals(Boolean.FALSE));
    }

    @Test
    public void testParseWithIntValueProperty() throws ParseException {
        Properties props = new Properties();
        props.put("b", "3");
        CommandLine cmd = parser.parse(new String[0][], null, props, false);
        Option optB = options.findOption("b");
        assertEquals(3, optB.getValue((String) null, null));
    }

    @Test
    public void testParseWithOptionGroup() throws ParseException {
        CommandLine cmd = parser.parse(new String[]{"--long-option1", "value1", "--long-option2", "value2"}, null, null, false);
        OptionGroup group = options.getOptionGroup("g");
        Option g1 = group.getSelectedOption();
        Option g2 = group.findOption("g2");
        assertTrue(g1 != null);
        assertTrue(g2 != null);
        assertEquals("value1", g1.getValue((String) null, null));
        assertEquals("value2", g2.getValue((String) null, null));
    }
}
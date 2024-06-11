package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class ParserTest {

    private Parser parser;
    private Options options;

    @Before
    public void setUp() {
        parser = new DefaultParser();
        options = new Options();
        options.addOption("a", "option-a", true, "Option A");
        options.addOption("b", "option-b", false, "Option B");
        options.addOption("c", "option-c", 1, "Option C");
        options.addRequiredOption("d", "option-d", false, "Option D");
        options.addOptionGroup(new OptionGroup("group", "Group options", Arrays.asList(
                new Option("e", "option-e", true, "Option E"),
                new Option("f", "option-f", true, "Option F")
        )));
    }

    @After
    public void tearDown() {
        parser = null;
        options = null;
    }

    @Test
    public void testParseWithNoArguments() throws ParseException {
        String[] arguments = new String[0];
        CommandLine cmd = parser.parse(options, arguments);
        assertEquals(0, cmd.getArgs().size());
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertFalse(cmd.hasOption("c"));
        assertFalse(cmd.hasOption("d"));
        assertFalse(cmd.hasOption("e"));
        assertFalse(cmd.hasOption("f"));
    }

    @Test
    public void testParseWithSingleArgument() throws ParseException {
        String[] arguments = new String[]{"arg"};
        CommandLine cmd = parser.parse(options, arguments);
        assertEquals(1, cmd.getArgs().size());
        assertEquals("arg", cmd.getArgs().get(0));
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertFalse(cmd.hasOption("c"));
        assertFalse(cmd.hasOption("d"));
        assertFalse(cmd.hasOption("e"));
        assertFalse(cmd.hasOption("f"));
    }

    @Test
    public void testParseWithSingleValueOption() throws ParseException {
        String[] arguments = new String[]{"-a"};
        CommandLine cmd = parser.parse(options, arguments);
        assertEquals(0, cmd.getArgs().size());
        assertTrue(cmd.hasOption("a"));
        assertEquals("true", cmd.getOptionValue("a"));
        assertFalse(cmd.hasOption("b"));
        assertFalse(cmd.hasOption("c"));
        assertFalse(cmd.hasOption("d"));
        assertFalse(cmd.hasOption("e"));
        assertFalse(cmd.hasOption("f"));
    }

    @Test
    public void testParseWithMultipleValueOption() throws ParseException {
        String[] arguments = new String[]{"-a", "value1", "-a", "value2"};
        CommandLine cmd = parser.parse(options, arguments);
        assertEquals(0, cmd.getArgs().size());
        assertTrue(cmd.hasOption("a"));
        List<String> optionAList = cmd.getOptionValues("a");
        assertEquals(2, optionAList.size());
        assertEquals("value1", optionAList.get(0));
        assertEquals("value2", optionAList.get(1));
        assertFalse(cmd.hasOption("b"));
        assertFalse(cmd.hasOption("c"));
        assertFalse(cmd.hasOption("d"));
        assertFalse(cmd.hasOption("e"));
        assertFalse(cmd.hasOption("f"));
    }

    @Test
    public void testParseWithRequiredOption() throws ParseException {
        String[] arguments = new String[0];
        assertThrows(MissingOptionException.class, () -> parser.parse(options, arguments));
    }

    @Test
    public void testParseWithOptionalOption() throws ParseException {
        String[] arguments = new String[0];
        CommandLine cmd = parser.parse(options, arguments);
        assertFalse(cmd.hasOption("d"));
    }

    @Test
    public void testParseWithGroupOption() throws ParseException {
        String[] arguments = new String[]{"-e", "valueE"};
        CommandLine cmd = parser.parse(options, arguments);
        assertEquals(0, cmd.getArgs().size());
        assertTrue(cmd.hasOption("group"));
        OptionGroup group = cmd.getOptions().getOptionGroup("group");
        Option optionE = group.getOption("e");
        assertTrue(optionE.isSelected());
        assertEquals("valueE", optionE.getValue());
        assertFalse(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("b"));
        assertFalse(cmd.hasOption("c"));
        assertFalse(cmd.hasOption("d"));
        assertFalse(cmd.hasOption("f"));
    }
}
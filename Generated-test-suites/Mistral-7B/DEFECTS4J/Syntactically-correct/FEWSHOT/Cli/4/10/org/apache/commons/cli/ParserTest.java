package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 2, "Option B");
        options.addOption("c", 3, "Option C");
        options.addOption("d", null, "Option D");
        options.addOption("e", null, "Option E", "value");
        options.addOption("f", 4, "Option F", "value");
        options.addOption("g", null, false, "Option G");

        Parser parser = new DefaultParser();
        String[] arguments = new String[] {"-a", "1", "--b", "2", "--c", "3",
                "-d", "", "--e", "value", "--f", "4"};

        CommandLine cmd = parser.parse(options, arguments);

        assertEquals(1, cmd.getArgs().size());
        assertEquals("", cmd.getArgs().get(0));

        assertEquals(8, cmd.getOptions().size());

        Option optionA = cmd.getOption("a");
        assertTrue(optionA.isSet());
        assertEquals("1", optionA.getValue());

        Option optionB = cmd.getOption("b");
        assertTrue(optionB.isSet());
        assertEquals("2", optionB.getValue());

        Option optionC = cmd.getOption("c");
        assertTrue(optionC.isSet());
        assertEquals("3", optionC.getValue());

        Option optionD = cmd.getOption("d");
        assertNull(optionD);

        Option optionE = cmd.getOption("e");
        assertSame(optionE, optionD);
        assertEquals("value", optionE.getValue());

        Option optionF = cmd.getOption("f");
        assertTrue(optionF.isSet());
        assertEquals("4", optionF.getValue());

        Option optionG = cmd.getOption("g");
        assertNull(optionG);

        List<String> args = Arrays.asList(cmd.getArgs());
        assertEquals(1, args.size());
        assertEquals("", args.get(0));
    }

    @Test
    public void testParseWithUnrecognizedOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        Parser parser = new DefaultParser();
        String[] arguments = new String[] {"-x", "--a"};

        try {
            parser.parse(options, arguments);
            fail();
        } catch (UnrecognizedOptionException e) {
            assertEquals("Unrecognized option: -x", e.getMessage());
        }

        assertEquals(0, options.getOptions().size());
    }

    @Test
    public void testParseWithMissingRequiredOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        Parser parser = new DefaultParser();
        String[] arguments = new String[] {"-a"};

        try {
            parser.parse(options, arguments);
            fail();
        } catch (MissingOptionException e) {
            assertEquals("Missing option: B", e.getMessage());
        }

        assertEquals(0, options.getOptions().size());
    }

    @Test
    public void testParseWithMissingOptionGroup() throws ParseException {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "Group");
        group.setRequired(true);
        group.addOption("a", true, "Option A");
        options.addOptionGroup(group);

        Parser parser = new DefaultParser();
        String[] arguments = new String[] {};

        try {
            parser.parse(options, arguments);
            fail();
        } catch (MissingOptionException e) {
            assertTrue(group.isRequired());
            assertSame(group, e.getOptionGroup());
            assertEquals("Group", e.getMessage());
        }

        assertEquals(0, options.getOptions().size());
    }

    @Test
    public void testParseWithMissingRequiredOptionInOptionGroup() throws ParseException {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "Group");
        group.setRequired(true);
        group.addOption("a", true, "Option A");
        options.addOptionGroup(group);

        Parser parser = new DefaultParser();
        String[] arguments = new String[] {};

        try {
            parser.parse(options, arguments);
            fail();
        } catch (MissingOptionException e) {
            assertEquals("Group", e.getOptionGroup().getLongOpt());
            List<Option> requiredOptions = new ArrayList<>(options.getOptions());
            requiredOptions.removeIf(o -> !o.isRequired());
            Option missingOption = requiredOptions.get(0);
            assertEquals("Option A", missingOption.getLongOpt());
            assertEquals("Missing option: Option A", e.getMessage());
        }

        assertEquals(0, options.getOptions().size());
    }

    @Test
    public void testParseWithMixedOptionsAndArguments() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 2, "Option B");

        Parser parser = new DefaultParser();
        String[] arguments = new String[] {"-a", "1", "--b", "2", "arg1",
                "arg2"};

        CommandLine cmd = parser.parse(options, arguments);

        assertEquals(2, cmd.getArgs().size());
        assertEquals("arg1", cmd.getArgs().get(0));
        assertEquals("arg2", cmd.getArgs().get(1));

        assertEquals(3, cmd.getOptions().size());

        Option optionA = cmd.getOption("a");
        assertTrue(optionA.isSet());
        assertEquals("1", optionA.getValue());

        Option optionB = cmd.getOption("b");
        assertTrue(optionB.isSet());
        assertEquals("2", optionB.getValue());
    }

    @Test
    public void testParseWithStopAtNonOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 2, "Option B");

        Parser parser = new DefaultParser();
        String[] arguments = new String[] {"-a", "1", "--b", "2", "arg1",
                "arg2", "--"};

        CommandLine cmd = parser.parse(options, arguments, true);

        assertEquals(2, cmd.getArgs().size());
        assertEquals("arg1", cmd.getArgs().get(0));
        assertEquals("arg2", cmd.getArgs().get(1));

        assertEquals(2, cmd.getOptions().size());

        Option optionA = cmd.getOption("a");
        assertTrue(optionA.isSet());
        assertEquals("1", optionA.getValue());

        Option optionB = cmd.getOption("b");
        assertTrue(optionB.isSet());
        assertEquals("2", optionB.getValue());
    }

    @Test
    public void testParseWithStopAtNonOptionNoArguments() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 2, "Option B");

        Parser parser = new DefaultParser();
        String[] arguments = new String[] {"--"};

        CommandLine cmd = parser.parse(options, arguments, true);

        assertEquals(0, cmd.getArgs().size());
        assertEquals(0, cmd.getOptions().size());
    }

    @Test
    public void testParseWithStopAtNonOptionEmptyArguments() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 2, "Option B");

        Parser parser = new DefaultParser();
        String[] arguments = new String[] {};

        CommandLine cmd = parser.parse(options, arguments, true);

        assertEquals(0, cmd.getArgs().size());
        assertEquals(0, cmd.getOptions().size());
    }

    @Test
    public void testParseWithStopAtNonOptionNoOptions() throws ParseException {
        Parser parser = new DefaultParser();
        String[] arguments = new String[] {"--", "arg1", "arg2"};

        CommandLine cmd = parser.parse(Collections.emptyOptions(), arguments, true);

        assertEquals(2, cmd.getArgs().size());
        assertEquals("arg1", cmd.getArgs().get(0));
        assertEquals("arg2", cmd.getArgs().get(1));

        assertEquals(0, cmd.getOptions().size());
    }

    @Test
    public void testParseWithStopAtNonOptionAndNoArguments() throws ParseException {
        Parser parser = new DefaultParser();
        String[] arguments = new String[] {"--"};

        CommandLine cmd = parser.parse(Collections.emptyOptions(), arguments, true);

        assertEquals(0, cmd.getArgs().size());
        assertEquals(0, cmd.getOptions().size());
    }
}
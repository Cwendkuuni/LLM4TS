package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse_OptionsAndArguments() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "A option");
        options.addOption("b", 2, "B option, takes an argument");
        options.addOption("c", 3, "C option, takes an optional argument");

        String[] arguments = {"-a", "--b=2", "1", "3"};
        CommandLine cmd = new DefaultParser().parse(options, arguments);

        assertEquals(2, cmd.getNumArgs());
        assertEquals("1", cmd.getArg(0));
        assertEquals("3", cmd.getArg(1));

        List<Option> optionsList = cmd.getOptions();
        assertEquals(3, optionsList.size());

        Option optionA = optionsList.get(0);
        assertTrue(optionA.isSet());
        assertEquals("A option", optionA.getLongOpt());
        assertEquals("A", optionA.getOpt());
        assertFalse(optionA.hasArg());

        Option optionB = optionsList.get(1);
        assertTrue(optionB.isSet());
        assertEquals("B", optionB.getOpt());
        assertEquals("b", optionB.getLongOpt());
        assertEquals(2, (int) optionB.getValue());

        Option optionC = optionsList.get(2);
        assertTrue(optionC.isSet());
        assertEquals("C", optionC.getOpt());
        assertEquals("c", optionC.getLongOpt());
        assertNull(optionC.getValue());
    }

    @Test
    public void testParse_OnlyOptions() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "A option");
        options.addOption("b", 2, "B option, takes an argument");
        options.addOption("c", 3, "C option, takes an optional argument");

        String[] arguments = {"-a", "--b", "--c=3"};
        CommandLine cmd = new DefaultParser().parse(options, arguments);

        assertEquals(0, cmd.getNumArgs());

        List<Option> optionsList = cmd.getOptions();
        assertEquals(3, optionsList.size());

        Option optionA = optionsList.get(0);
        assertTrue(optionA.isSet());
        assertEquals("A option", optionA.getLongOpt());
        assertEquals("A", optionA.getOpt());
        assertFalse(optionA.hasArg());

        Option optionB = optionsList.get(1);
        assertTrue(optionB.isSet());
        assertEquals("B option", optionB.getLongOpt());
        assertEquals("B", optionB.getOpt());
        assertNull(optionB.getValue());

        Option optionC = optionsList.get(2);
        assertTrue(optionC.isSet());
        assertEquals("C option", optionC.getLongOpt());
        assertEquals("C", optionC.getOpt());
        assertEquals(3, (int) optionC.getValue());
    }

    @Test
    public void testParse_MissingOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "A option");

        String[] arguments = {"--b"};
        assertThrows(MissingOptionException.class, () -> {
            new DefaultParser().parse(options, arguments);
        });
    }

    @Test
    public void testParse_MissingRequiredOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "A option");
        options.addRequiredOption("b", false, "B option");

        String[] arguments = {};
        assertThrows(MissingOptionException.class, () -> {
            new DefaultParser().parse(options, arguments);
        });
    }

    @Test
    public void testParse_MissingRequiredOptionGroup() throws ParseException {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "Group options");
        group.addOption("a", true, "A option");
        group.addRequiredOption("b", false, "B option");
        options.addOptionGroup(group);

        String[] arguments = {};
        assertThrows(MissingOptionException.class, () -> {
            new DefaultParser().parse(options, arguments);
        });
    }

    @Test
    public void testParse_InvalidOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "A option");

        String[] arguments = {"-z"};
        assertThrows(UnrecognizedOptionException.class, () -> {
            new DefaultParser().parse(options, arguments);
        });
    }

    @Test
    public void testParse_InvalidOptionWithArgument() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "A option");

        String[] arguments = {"-z=1"};
        assertThrows(UnrecognizedOptionException.class, () -> {
            new DefaultParser().parse(options, arguments);
        });
    }

    @Test
    public void testParse_InvalidOptionGroup() throws ParseException {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "Group options");
        group.setRequired(true);
        group.addOption("a", true, "A option");

        String[] arguments = {"--group"};
        assertThrows(MissingOptionException.class, () -> {
            new DefaultParser().parse(options, arguments);
        });
    }

    @Test
    public void testParse_MixedOptionsAndArguments() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "A option");
        options.addOption("b", 2, "B option, takes an argument");

        String[] arguments = {"-a", "arg1", "--b=arg2"};
        CommandLine cmd = new DefaultParser().parse(options, arguments);

        assertEquals(1, cmd.getNumArgs());
        assertEquals("arg1", cmd.getArg(0));

        List<Option> optionsList = cmd.getOptions();
        assertEquals(2, optionsList.size());

        Option optionA = optionsList.get(0);
        assertTrue(optionA.isSet());
        assertEquals("A option", optionA.getLongOpt());
        assertEquals("A", optionA.getOpt());
        assertFalse(optionA.hasArg());

        Option optionB = optionsList.get(1);
        assertTrue(optionB.isSet());
        assertEquals("B option", optionB.getLongOpt());
        assertEquals("B", optionB.getOpt());
        assertEquals("arg2", optionB.getValue());
    }

    @Test
    public void testParse_EmptyArguments() throws ParseException {
        Options options = new Options();
        String[] arguments = {};
        CommandLine cmd = new DefaultParser().parse(options, arguments);
        assertEquals(0, cmd.getNumArgs());
    }

    @Test
    public void testParse_EmptyArgumentsWithOptions() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "A option");
        String[] arguments = {};
        CommandLine cmd = new DefaultParser().parse(options, arguments);
        assertEquals(0, cmd.getNumArgs());
        assertTrue(cmd.hasOption("a"));
    }

    @Test
    public void testParse_SingleArgument() throws ParseException {
        Options options = new Options();
        String[] arguments = {"arg1"};
        CommandLine cmd = new DefaultParser().parse(options, arguments);
        assertEquals(1, cmd.getNumArgs());
        assertEquals("arg1", cmd.getArg(0));
    }

    @Test
    public void testParse_SingleArgumentWithOptions() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "A option");
        String[] arguments = {"arg1"};
        CommandLine cmd = new DefaultParser().parse(options, arguments);
        assertEquals(1, cmd.getNumArgs());
        assertEquals("arg1", cmd.getArg(0));
        assertTrue(cmd.hasOption("a"));
    }

    @Test
    public void testParse_OptionsWithArguments() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "A option");
        options.addOption("b", 2, "B option, takes an argument");

        String[] arguments = {"-a", "arg1", "--b=arg2"};
        CommandLine cmd = new DefaultParser().parse(options, arguments);

        assertEquals(1, cmd.getNumArgs());
        assertEquals("arg1", cmd.getArg(0));

        List<Option> optionsList = cmd.getOptions();
        assertEquals(2, optionsList.size());

        Option optionA = optionsList.get(0);
        assertTrue(optionA.isSet());
        assertEquals("A option", optionA.getLongOpt());
        assertEquals("A", optionA.getOpt());
        assertFalse(optionA.hasArg());

        Option optionB = optionsList.get(1);
        assertTrue(optionB.isSet());
        assertEquals("B option", optionB.getLongOpt());
        assertEquals("B", optionB.getOpt());
        assertEquals("arg2", optionB.getValue());
    }

    @Test
    public void testParse_MixedOptionsAndArgumentsWithGroup() throws ParseException {
        Options options = new Options();

        OptionGroup group = new OptionGroup("group", "Group options");
        group.addOption("g", true, "Group option");
        group.addRequiredOption("a", false, "A option");
        options.addOptionGroup(group);

        String[] arguments = {"--g", "--a"};
        CommandLine cmd = new DefaultParser().parse(options, arguments);

        assertEquals(0, cmd.getNumArgs());
        assertTrue(cmd.hasOption("g"));
        assertTrue(cmd.hasOption("a"));
    }

    @Test
    public void testParse_MixedOptionsAndArgumentsWithGroupRequired() throws ParseException {
        Options options = new Options();

        OptionGroup group = new OptionGroup("group", "Group options");
        group.addOption("g", true, "Group option");
        group.setRequired(true);

        String[] arguments = {"--g"};
        assertThrows(MissingOptionException.class, () -> {
            new DefaultParser().parse(options, arguments);
        });
    }

    @Test
    public void testParse_MixedOptionsAndArgumentsWithGroupOptional() throws ParseException {
        Options options = new Options();

        OptionGroup group = new OptionGroup("group", "Group options");
        group.addOption("g", true, "Group option");
        group.setRequired(false);

        String[] arguments = {"--g"};
        CommandLine cmd = new DefaultParser().parse(options, arguments);

        assertEquals(0, cmd.getNumArgs());
        assertTrue(cmd.hasOption("g"));
    }

    @Test
    public void testParse_MixedOptionsAndArgumentsWithGroupOptionalArg() throws ParseException {
        Options options = new Options();

        OptionGroup group = new OptionGroup("group", "Group options");
        group.addOption("g", true, "Group option");
        group.addOption("a", true, "A option");
        group.setRequired(false);

        String[] arguments = {"--g", "--a=value"};
        CommandLine cmd = new DefaultParser().parse(options, arguments);

        assertEquals(0, cmd.getNumArgs());
        assertTrue(cmd.hasOption("g"));
        assertTrue(cmd.hasOption("a"));
        assertEquals("value", cmd.getOptionValue("a"));
    }

    @Test
    public void testParse_MixedOptionsAndArgumentsWithGroupRequiredArg() throws ParseException {
        Options options = new Options();

        OptionGroup group = new OptionGroup("group", "Group options");
        group.addOption("g", true, "Group option");
        group.addRequiredOption("a", false, "A option");
        group.addOption("b", true, "B option");
        group.setRequired(true);

        String[] arguments = {"--g"};
        assertThrows(MissingOptionException.class, () -> {
            new DefaultParser().parse(options, arguments);
        });

        String[] arguments2 = {"--g", "--a", "--b"};
        CommandLine cmd = new DefaultParser().parse(options, arguments2);

        assertEquals(0, cmd.getNumArgs());
        assertTrue(cmd.hasOption("g"));
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
    }

    @Test
    public void testParse_MixedOptionsAndArgumentsWithGroupOptionalArgAndValue() throws ParseException {
        Options options = new Options();

        OptionGroup group = new OptionGroup("group", "Group options");
        group.addOption("g", true, "Group option");
        group.addOption("a", true, "A option");
        group.addOption("b", true, "B option");
        group.setRequired(false);

        String[] arguments = {"--g", "--a=value1", "--b=value2"};
        CommandLine cmd = new DefaultParser().parse(options, arguments);

        assertEquals(0, cmd.getNumArgs());
        assertTrue(cmd.hasOption("g"));
        assertTrue(cmd.hasOption("a"));
        assertEquals("value1", cmd.getOptionValue("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals("value2", cmd.getOptionValue("b"));
    }

    @Test
    public void testParse_MixedOptionsAndArgumentsWithGroupOptionalArgWithEmptyValue() throws ParseException {
        Options options = new Options();

        OptionGroup group = new OptionGroup("group", "Group options");
        group.addOption("g", true, "Group option");
        group.addOption("a", true, "A option");
        group.addOption("b", true, "B option");
        group.setRequired(false);

        String[] arguments = {"--g", "--a=", "--b=value2"};
        CommandLine cmd = new DefaultParser().parse(options, arguments);

        assertEquals(0, cmd.getNumArgs());
        assertTrue(cmd.hasOption("g"));
        assertTrue(cmd.hasOption("a"));
        assertNull(cmd.getOptionValue("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals("value2", cmd.getOptionValue("b"));
    }
}
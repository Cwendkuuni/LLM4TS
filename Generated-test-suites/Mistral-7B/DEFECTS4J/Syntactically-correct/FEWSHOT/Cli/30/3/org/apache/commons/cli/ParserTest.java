package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.junit.Test;

public class ParserTest {

    @Test
    public void testParserWithNoOptionsAndArguments() {
        Options options = new Options();
        String[] arguments = new String[0];

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, null, false);

        assertTrue(cmd.getNumArgs() == 0);
        assertTrue(cmd.getOptions().isEmpty());
    }

    @Test
    public void testParserWithOptionsAndArguments() {
        String optionA = "a";
        String optionB = "b";
        String optionC = "c";
        String arg1 = "value1";
        String arg2 = "value2";

        Options options = new Options();
        options.addOption(optionA, false, null, "Option A");
        options.addOption(optionB, true, "value", "Option B with value");
        options.addOption(optionC, true, null, "Option C without value");

        String[] arguments = {"--optionA", "arg1", "--optionB", "value2", "--optionC"};

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, null, false);

        List<Option> optionsList = cmd.getOptions();
        assertEquals(3, optionsList.size());

        Option optionAA = (Option) optionsList.get(0);
        assertEquals(optionA, optionAA.getKey());
        assertFalse(optionAA.hasVal());

        Option optionB = (Option) optionsList.get(1);
        assertEquals(optionB, options.findOption(optionB));
        assertEquals(arg1, optionB.getValue().get(0));
        assertTrue(optionB.getValue().size() == 1);

        Option optionC = (Option) optionsList.get(2);
        assertSame(optionC, options.findOption(optionC));
        assertNull(optionC.getValue());
    }

    @Test
    public void testParserWithOptionsAndProperties() {
        String optionA = "a";
        String optionB = "b";
        String arg1 = "value1";
        String arg2 = "value2";

        Options options = new Options();
        options.addOption(optionA, false, null, "Option A");
        options.addOption(optionB, true, "value", "Option B with value");

        Properties properties = new Properties();
        properties.setProperty(optionA, "value1");
        properties.setProperty(optionB, arg1);

        String[] arguments = new String[0];

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, properties, false);

        List<Option> optionsList = cmd.getOptions();
        assertEquals(2, optionsList.size());

        Option optionAOption = (Option) optionsList.get(0);
        assertEquals(optionA, optionAOption.getKey());
        assertEquals(arg1, optionAOption.getValue().get(0));
        assertTrue(optionAOption.hasVal());

        Option optionBOption = (Option) optionsList.get(1);
        assertEquals(optionB, optionBOption.getKey());
        assertEquals(arg1, optionBOption.getValue().get(0));
        assertTrue(optionBOption.hasVal());

        assertEquals(2, cmd.getArgs().size());
        assertEquals(arg2, cmd.getArgs().get(0));
        assertNull(cmd.getArgs().get(1));
    }

    @Test
    public void testParserWithOnlyRequiredOptions() {
        String optionA = "a";
        String optionB = "b";

        Options options = new Options();
        options.addOption(optionA, true, null, "Option A");
        options.addOption(optionB, true, null, "Option B");

        List<String> requiredOptions = Arrays.asList(optionA, optionB);

        String[] arguments = new String[0];

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, null, false);

        List<Option> optionsList = cmd.getOptions();
        assertEquals(2, optionsList.size());

        List<String> cmdOptions = new ArrayList<>();
        for (Option option : optionsList) {
            cmdOptions.add(option.getKey());
        }

        assertEquals(Collections.unmodifiableList(requiredOptions), cmd.getOptionNames());
    }

    @Test
    public void testParserWithStopAtNonOption() {
        String optionA = "a";
        String optionB = "b";
        String unknownOption = "unknown";

        Options options = new Options();
        options.addOption(optionA, false, null, "Option A");
        options.addOption(optionB, true, null, "Option B");

        String[] arguments = {"--optionA", "--optionB", unknownOption};

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, null, true);

        List<Option> optionsList = cmd.getOptions();
        assertEquals(2, optionsList.size());

        List<String> cmdOptions = new ArrayList<>();
        for (Option option : optionsList) {
            cmdOptions.add(option.getKey());
        }

        assertEquals(Arrays.asList("--optionA", "--optionB"), cmd.getArgList());
        assertTrue(cmd.getArgs().contains(unknownOption));
    }

    @Test
    public void testParserWithMissingOption() {
        String optionA = "a";

        Options options = new Options();
        options.addOption(optionA, true, null, "Option A");

        String[] arguments = new String[0];

        Parser parser = new DefaultParser();

        assertThrows(MissingOptionException.class, () -> {
            parser.parse(options, arguments, null, false);
        });
    }

    @Test
    public void testParserWithMissingRequiredOption() {
        String optionA = "a";

        Options options = new Options();
        options.addOption(optionA, true, null, "Option A");
        options.addOptionGroup(new OptionGroup("group", "Group"));

        List<String> requiredOptions = Arrays.asList(optionA);

        String[] arguments = new String[0];

        Parser parser = new DefaultParser();

        assertThrows(MissingOptionException.class, () -> {
            parser.parse(options, arguments, null, false);
        });

        OptionGroup optionGroup = options.getOptionGroup("group");
        assertFalse(optionGroup.isSelected());
    }

    @Test
    public void testParserWithMissingRequiredOptionGroup() {
        String optionA = "a";

        Options options = new Options();
        options.addOption(optionA, true, null, "Option A");

        List<String> requiredOptions = Arrays.asList(optionA);

        String[] arguments = new String[0];

        Parser parser = new DefaultParser();

        assertThrows(MissingOptionException.class, () -> {
            parser.parse(options, arguments, null, false);
        });

        assertNull(parser.parse(options, arguments, null, true).getOptionGroup("group"));
    }

    @Test
    public void testParserWithInvalidOptionGroupArgument() {
        String optionA = "a";

        Options options = new Options();
        options.addOption(optionA, true, null, "Option A");
        options.addOptionGroup(new OptionGroup("group", "Group with invalid argument"));

        assertThrows(IllegalArgumentException.class, () -> {
            parser.parse(options, new String[0], null, false);
        });
    }

    @Test
    public void testParserWithUnrecognizedOption() {
        String optionA = "a";
        String unknownOption = "unknown";

        Options options = new Options();
        options.addOption(optionA, true, null, "Option A");

        String[] arguments = {"--unknown"};

        Parser parser = new DefaultParser();

        assertThrows(UnrecognizedOptionException.class, () -> {
            parser.parse(options, arguments, null, false);
        });

        assertEquals(unknownOption, arguments[0]);
    }

    @Test
    public void testParserWithInvalidArgument() {
        String optionA = "a";
        String invalidArgument = "invalid";

        Options options = new Options();
        options.addOption(optionA, true, null, "Option A");

        String[] arguments = {"--optionA=" + invalidArgument};

        Parser parser = new DefaultParser();

        assertThrows(IllegalArgumentException.class, () -> {
            parser.parse(options, arguments, null, false);
        });
    }

    @Test
    public void testParserWithMissingArgument() {
        String optionA = "a";

        Options options = new Options();
        options.addOption(optionA, true, null, "Option A");

        String[] arguments = {"--optionA"};

        Parser parser = new DefaultParser();

        assertThrows(MissingArgumentException.class, () -> {
            parser.parse(options, arguments, null, false);
        });
    }

    @Test
    public void testParserWithMixedArgumentsAndOptions() {
        String optionA = "a";
        String optionB = "b";
        String arg1 = "value1";
        String arg2 = "value2";

        Options options = new Options();
        options.addOption(optionA, false, null, "Option A");
        options.addOption(optionB, true, null, "Option B");

        String[] arguments = {"--optionB", "value2", "--optionA", arg1};

        Parser parser = new DefaultParser();

        CommandLine cmd = parser.parse(options, arguments, null, false);

        List<Option> optionsList = cmd.getOptions();
        assertEquals(2, optionsList.size());

        Option optionAOption = (Option) optionsList.get(0);
        assertEquals(optionA, optionAOption.getKey());
        assertEquals(arg1, optionAOption.getValue().get(0));
        assertTrue(optionAOption.hasVal());

        Option optionBOption = (Option) optionsList.get(1);
        assertEquals(optionB, optionBOption.getKey());
        assertEquals(arg2, optionBOption.getValue().get(0));
        assertTrue(optionBOption.hasVal());

        assertEquals(1, cmd.getArgs().size());
        assertEquals(arg1, cmd.getArgs().get(0));
    }

    @Test
    public void testParserWithMixedArgumentsAndOptionsWithGroup() {
        String optionA = "a";
        String optionB = "b";
        String arg1 = "value1";
        String arg2 = "value2";

        Options options = new Options();
        options.addOption(optionA, false, null, "Option A");
        options.addOptionGroup(new OptionGroup("group", "Group"));
        options.addOption(optionB, true, null, "Option B");

        String[] arguments = {"--group", "--optionB", arg2, "--optionA", arg1};

        Parser parser = new DefaultParser();

        CommandLine cmd = parser.parse(options, arguments, null, false);

        List<Option> optionsList = cmd.getOptions();
        assertEquals(3, optionsList.size());

        Option groupOption = (Option) optionsList.get(0);
        assertEquals("group", groupOption.getKey());
        assertNull(groupOption.getValue());

        Option optionAOption = (Option) optionsList.get(1);
        assertEquals(optionA, optionAOption.getKey());
        assertEquals(arg1, optionAOption.getValue().get(0));
        assertTrue(optionAOption.hasVal());

        Option optionBOption = (Option) optionsList.get(2);
        assertEquals(optionB, optionBOption.getKey());
        assertEquals(arg2, optionBOption.getValue().get(0));
        assertTrue(optionBOption.hasVal());

        assertEquals(2, cmd.getArgs().size());
        assertEquals(arg1, cmd.getArgs().get(0));
        assertEquals(arg2, cmd.getArgs().get(1));
    }
}
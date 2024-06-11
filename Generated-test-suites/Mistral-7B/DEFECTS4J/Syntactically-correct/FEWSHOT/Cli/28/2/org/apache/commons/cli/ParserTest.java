package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
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
    public void testNullOptions() {
        try {
            new DefaultParser().parse(null, new String[0]);
            fail("Expected ParseException");
        } catch (ParseException e) {
            // expected
        }
    }

    @Test
    public void testEmptyOptions() {
        String[] args = new String[0];

        Options options = new Options();
        CommandLine cmd = new DefaultParser().parse(options, args);

        assertTrue(cmd.getArgs().isEmpty());
        assertTrue(cmd.getOptions().isEmpty());
    }

    @Test
    public void testParseWithOptionsAndArguments() {
        String[] args = new String[] {"-a", "1", "--b", "2", "--c", "3"};

        Options options = new Options();
        Option optA = new Option("a", "a", true, "a help message");
        Option optB = new Option("b", "b", true, "b help message");
        Option optC = new Option("c", "c", true, "c help message");
        options.addOption(optA);
        options.addOption(optB);
        options.addOption(optC);

        CommandLine cmd = new DefaultParser().parse(options, args);

        List<Option> opts = cmd.getOptions();
        assertEquals(3, opts.size());
        assertTrue(opts.get(0).getValue().equals("1"));
        assertTrue(opts.get(1).getValue().equals("2"));
        assertTrue(opts.get(2).getValue().equals("3"));

        List<String> argsList = cmd.getArgs();
        assertTrue(argsList.isEmpty());
    }

    @Test
    public void testParseWithOnlyOptions() {
        String[] args = new String[] {"--a", "--b", "--c"};

        Options options = new Options();
        Option optA = new Option("a", "a", true, "a help message");
        Option optB = new Option("b", "b", true, "b help message");
        Option optC = new Option("c", "c", true, "c help message");
        options.addOption(optA);
        options.addOption(optB);
        options.addOption(optC);

        CommandLine cmd = new DefaultParser().parse(options, args);

        List<Option> opts = cmd.getOptions();
        assertEquals(3, opts.size());
        assertTrue(opts.get(0).isSet());
        assertTrue(opts.get(1).isSet());
        assertTrue(opts.get(2).isSet());

        List<String> argsList = cmd.getArgs();
        assertTrue(argsList.isEmpty());
    }

    @Test
    public void testParseWithOptionalOptions() {
        String[] args = new String[] {"-a", "1", "--b", "2"};

        Options options = new Options();
        Option optA = new Option("a", "a", true, "a help message");
        Option optB = new Option("b", "b", false, "b help message");
        options.addOption(optA);
        options.addOption(optB);

        CommandLine cmd = new DefaultParser().parse(options, args);

        List<Option> opts = cmd.getOptions();
        assertEquals(2, opts.size());
        assertTrue(opts.get(0).getValue().equals("1"));
        assertFalse(opts.get(1).isSet());

        List<String> argsList = cmd.getArgs();
        assertTrue(argsList.isEmpty());
    }

    @Test
    public void testParseWithLongOptions() {
        String[] args = new String[] {"--long-option-a", "--long-option-b=value"};

        Options options = new Options();
        Option optA = new Option("a", "long-option-a", false, "Long option A help message");
        Option optB = new Option("b", "long-option-b", true, "Long option B help message");
        optB.setLongOpt("long-option-b");
        options.addOption(optA);
        options.addOption(optB);

        CommandLine cmd = new DefaultParser().parse(options, args);

        List<Option> opts = cmd.getOptions();
        assertEquals(2, opts.size());
        assertTrue(opts.get(0).isSet());
        assertNull(opts.get(1).getValue());

        List<String> argsList = cmd.getArgs();
        assertTrue(argsList.isEmpty());
    }

    @Test
    public void testParseWithGroup() {
        String[] args = new String[] {"--group", "-p", "--param"};

        OptionGroup group = new OptionGroup("group", "group help message");
        Option optParam = new Option("p", "param", true, "param help message");
        group.addOption(optParam);

        Options options = new Options();
        options.addOptionGroup(group);

        CommandLine cmd = new DefaultParser().parse(options, args);

        List<Option> opts = cmd.getOptions();
        assertEquals(1, opts.size());
        assertSame(group, opts.get(0).getParent());
        assertTrue(opts.get(0).getValue().equals("--param"));

        List<String> argsList = cmd.getArgs();
        assertTrue(argsList.isEmpty());
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseMissingArgument() {
        String[] args = new String[] {"--option"};

        Options options = new Options();
        Option opt = new Option("o", "option", true, "option help message");
        options.addOption(opt);

        new DefaultParser().parse(options, args);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseUnrecognizedOption() {
        String[] args = new String[] {"--unknown"};

        Options options = new Options();

        new DefaultParser().parse(options, args);
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidBooleanOption() {
        String[] args = new String[] {"--option=false"};

        Options options = new Options();
        Option opt = new Option("o", "option", true, "option help message");
        options.addOption(opt);

        new DefaultParser().parse(options, args);
    }

    @Test
    public void testParseWithProperties() {
        String[] args = new String[] {"--property=value"};

        Options options = new Options();
        Option opt = new Option("p", "property", true, "property help message");
        opt.setLongOpt("property");
        options.addOption(opt);

        Properties properties = new Properties();
        properties.setProperty("property", "value");

        CommandLine cmd = new DefaultParser().parse(options, args, properties);

        List<Option> opts = cmd.getOptions();
        assertEquals(1, opts.size());
        assertTrue(opts.get(0).getValue().equals("value"));

        List<String> argsList = cmd.getArgs();
        assertTrue(argsList.isEmpty());
    }

    @Test
    public void testParseWithRequiredProperties() {
        String[] args = new String[] {"--property"};

        Options options = new Options();
        Option opt = new Option("p", "property", true, "property help message");
        opt.setLongOpt("property");
        opt.setRequired(true);
        options.addOption(opt);

        Properties properties = new Properties();

        try {
            CommandLine cmd = new DefaultParser().parse(options, args, properties);
            fail("Expected ParseException");
        } catch (ParseException e) {
            // expected
        }
    }

    @Test
    public void testParseWithGroupAndProperties() {
        String[] args = new String[] {"--group", "--property=value"};

        OptionGroup group = new OptionGroup("group", "group help message");
        Option optProperty = new Option("p", "property", true, "property help message");
        optProperty.setLongOpt("property");
        group.addOption(optProperty);

        Options options = new Options();
        options.addOptionGroup(group);

        Properties properties = new Properties();
        properties.setProperty("property", "value");

        CommandLine cmd = new DefaultParser().parse(options, args, properties);

        List<Option> opts = cmd.getOptions();
        assertEquals(1, opts.size());
        assertSame(group, opts.get(0).getParent());
        assertTrue(opts.get(0).getValue().equals("value"));

        List<String> argsList = cmd.getArgs();
        assertTrue(argsList.isEmpty());
    }

    @Test
    public void testParseWithLongAndShortOptions() {
        String[] args = new String[] {"-a", "--option"};

        Options options = new Options();
        Option opt = new Option("a", "option", true, "option help message");
        opt.setLongOpt("option");
        options.addOption(opt);

        CommandLine cmd = new DefaultParser().parse(options, args);

        List<Option> opts = cmd.getOptions();
        assertEquals(1, opts.size());
        assertTrue(opts.get(0).getValue().equals("--option"));

        List<String> argsList = cmd.getArgs();
        assertTrue(argsList.isEmpty());
    }

    @Test
    public void testParseWithLongOptionsBeforeShortOptions() {
        String[] args = new String[] {"--option-b", "-a"};

        Options options = new Options();
        Option optA = new Option("a", "optionA", false, "optionA help message");
        Option optB = new Option("b", "optionB", true, "optionB help message");
        optB.setLongOpt("optionB");
        options.addOption(optB);
        options.addOption(optA);

        CommandLine cmd = new DefaultParser().parse(options, args);

        List<Option> opts = cmd.getOptions();
        assertEquals(2, opts.size());
        assertTrue(opts.get(0).isSet());
        assertTrue(opts.get(1).getValue().equals("--optionB"));

        List<String> argsList = cmd.getArgs();
        assertTrue(argsList.isEmpty());
    }

    @Test
    public void testParseWithHiddenOptions() {
        String[] args = new String[] {"--hidden", "--showhidden"};

        Options options = new Options();
        Option optHidden = new Option("h", "hidden", false, "hidden option", true, null, "hidden");
        Option optShowHidden = new Option("s", "showhidden", false, "show hidden options", true, null, "showhidden");
        optShowHidden.setHidden(true);
        options.addOption(optHidden);
        options.addOption(optShowHidden);

        CommandLine cmd = new DefaultParser().parse(options, args);

        List<Option> opts = cmd.getOptions();
        assertEquals(2, opts.size());
        assertTrue(opts.get(0).isSet());
        assertFalse(opts.get(1).isSet());

        List<String> argsList = cmd.getArgs();
        assertTrue(argsList.isEmpty());
    }

    @Test
    public void testParseWithInvalidBooleanValue() {
        String[] args = new String[] {"--boolean-option=invalid"};

        Options options = new Options();
        Option opt = new Option("b", "boolean-option", true, "boolean option");
        options.addOption(opt);

        try {
            CommandLine cmd = new DefaultParser().parse(options, args);
            fail("Expected ParseException");
        } catch (ParseException e) {
            // expected
        }
    }

    @Test
    public void testParseWithBooleanValue() {
        String[] args = new String[] {"--boolean-option=true"};

        Options options = new Options();
        Option opt = new Option("b", "boolean-option", true, "boolean option");
        options.addOption(opt);

        CommandLine cmd = new DefaultParser().parse(options, args);

        List<Option> opts = cmd.getOptions();
        assertEquals(1, opts.size());
        assertTrue(opts.get(0).getValue().equals("true"));

        List<String> argsList = cmd.getArgs();
        assertTrue(argsList.isEmpty());
    }

    @Test
    public void testParseWithBooleanValueAndArguments() {
        String[] args = new String[] {"--boolean-option=true", "arg1", "arg2"};

        Options options = new Options();
        Option opt = new Option("b", "boolean-option", true, "boolean option");
        options.addOption(opt);

        CommandLine cmd = new DefaultParser().parse(options, args);

        List<Option> opts = cmd.getOptions();
        assertEquals(1, opts.size());
        assertTrue(opts.get(0).getValue().equals("true"));

        List<String> argsList = cmd.getArgs();
        List<String> expectedArgs = Arrays.asList("arg1", "arg2");
        assertEquals(expectedArgs, argsList);
    }

}
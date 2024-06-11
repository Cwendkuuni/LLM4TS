package org.apache.commons.cli2.option;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.OptionRunner;
import org.apache.commons.cli2.TestUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class GroupImplTest {

    private OptionRunner runner;
    private HelpFormatter formatter;

    @Before
    public void setUp() throws Exception {
        final List options = new ArrayList();
        options.add(new Option("a", "arg1", true, "An argument"));
        options.add(new Option("b", "bool", false, "A boolean option"));
        options.add(new Option("c", "count", true, "A count option"));
        options.add(new Option("d", "default", false, "A default option"));
        options.add(new Option("e", "error", false, "An error option"));

        final Group group = new GroupImpl(options, "MyGroup", "Description of MyGroup", 1, 3);
        runner = new OptionRunner(group);
        formatter = new HelpFormatter();
    }

    @Test
    public void testBasicUsage() {
        final String[] args = {"-a", "arg1Value1", "-b", "-c", "countValue1"};
        runner.parse(args);

        Assert.assertEquals("arg1Value1", group.getOptionValue("a"));
        Assert.assertFalse(group.hasOption("b"));
        Assert.assertEquals("countValue1", group.getOptionValue("c"));

        final Map parsedArgs = runner.getParsedArguments();
        Assert.assertEquals(2, parsedArgs.size());
        Assert.assertEquals("arg1Value1", parsedArgs.get("arg1"));
        Assert.assertEquals("countValue1", parsedArgs.get("count"));
    }

    @Test(expected = OptionException.class)
    public void testMissingRequiredOption() {
        final String[] args = {"-c"};
        runner.parse(args);
    }

    @Test(expected = OptionException.class)
    public void testTooManyOptions() {
        final String[] args = {"-a", "arg1Value1", "-a", "arg1Value2", "-c", "countValue1"};
        runner.parse(args);
    }

    @Test(expected = OptionException.class)
    public void testInvalidOption() {
        final String[] args = {"-i", "invalidOptionValue"};
        runner.parse(args);
    }

    @Test
    public void testHelp() {
        final String[] args = {"--help"};
        runner.parse(args);

        final String help = formatter.printHelp("Test", args, true);
        System.out.println(help);
    }

    @Test
    public void testInvalidBooleanOption() {
        final String[] args = {"-b", "invalidValue"};

        try {
            runner.parse(args);
            Assert.fail();
        } catch (final OptionException e) {
            Assert.assertEquals("invalidValue", e.getOption().getValue());
        }
    }

    @Test
    public void testBooleanOptionWithValue() {
        final String[] args = {"-b", "trueValue"};

        runner.parse(args);
        Assert.assertTrue(group.hasOption("b"));
    }

    @Test
    public void testArgumentOptionWithValue() {
        final String[] args = {"-a", "argValue"};

        runner.parse(args);
        Assert.assertEquals("argValue", group.getOptionValue("a"));
    }

    @Test
    public void testCountOptionWithValue() {
        final String[] args = {"-c", "3"};

        runner.parse(args);
        Assert.assertEquals(3, Integer.parseInt(group.getOptionValue("c")));
    }

    @Test
    public void testCountOptionWithoutValue() {
        final String[] args = {"-c"};

        runner.parse(args);
        Assert.assertEquals(1, Integer.parseInt(group.getOptionValue("c")));
    }

    @Test
    public void testErrorOption() {
        final String[] args = {"--test"};

        try {
            runner.parse(args);
            Assert.fail();
        } catch (final OptionException e) {
            Assert.assertEquals("--test", e.getOption().getValue());
        }
    }

    @Test
    public void testDefaultOption() {
        final String[] args = {};

        runner.parse(args);
        Assert.assertTrue(group.hasOption("d"));
        Assert.assertNull(group.getOptionValue("d"));
    }

    @Test
    public void testDefaultOptionWithValue() {
        final String[] args = {"--default"};

        runner.parse(args);
        Assert.assertTrue(group.hasOption("d"));
        Assert.assertEquals("defaultValue", group.getOptionValue("d"));
    }

    @Test
    public void testAnonymousArgument() {
        final String[] args = {"--arg"};

        runner.parse(args);
        Assert.assertTrue(group.hasAnonymousArgument());
        Assert.assertNull(group.getAnonymousArgumentValue());
    }

    @Test
    public void testAnonymousArgumentWithValue() {
        final String[] args = {"--arg", "value"};

        runner.parse(args);
        Assert.assertTrue(group.hasAnonymousArgument());
        Assert.assertEquals("value", group.getAnonymousArgumentValue());
    }

    @Test
    public void testGroupExpanded() {
        final Group group = new GroupImpl(Collections.emptyList(), "MyGroup2", "Description of MyGroup2", 1, 3);

        final List options = new ArrayList();
        options.add(new Option("a", "arg1", true, "An argument"));
        options.addAll(TestUtils.createOptions(new String[]{"b", "bool", false, "A boolean option"},
                new String[]{"c", "count", true, "A count option"}));
        group.setOptions(options);

        final String[] args = {"--MyGroup2"};

        runner = new OptionRunner(group);
        runner.parse(args);

        Assert.assertEquals("arg1Value1", group.getOptionValue("a"));
        Assert.assertFalse(group.hasOption("b"));
        Assert.assertFalse(group.hasOption("c"));

        final Map parsedArgs = runner.getParsedArguments();
        Assert.assertEquals(0, parsedArgs.size());
    }

    @Test
    public void testGroupWithAnonymousArguments() {
        final Group group = new GroupImpl(Collections.emptyList(), "MyGroup3", "Description of MyGroup3", 1, 3);

        final List options = new ArrayList();
        options.add(new Option("a", "arg1", true, "An argument"));
        options.add(new Option("anonymous", "", Argument.VALUE_REQUIRED, "An anonymous argument"));
        group.setOptions(options);

        final String[] args = {"--MyGroup3", "--anonymous", "value"};

        runner = new OptionRunner(group);
        runner.parse(args);

        Assert.assertEquals("value", group.getAnonymousArgumentValue());
    }
}
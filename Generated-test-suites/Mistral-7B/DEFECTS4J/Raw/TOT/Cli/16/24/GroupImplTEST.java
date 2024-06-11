package org.apache.commons.cli2.option;

import org.apache.commons.cli2.CommandLineParser;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

public class GroupImplTest {

    private CommandLineParser parser;

    @Before
    public void setUp() {
        parser = new DefaultParser();
    }

    @Test
    public void testGroupWithMinimumAndMaximum() {
        final List options = new ArrayList();

        options.add(createOption("A", "a", true, "A"));
        options.add(createOption("B", "b", true, "B"));
        options.add(createOption("C", "c", false, "C"));

        final Group group = new GroupImpl(options, "MyGroup", null, 2, 3);

        final Options allOptions = new Options();
        allOptions.addOption(group);

        final String[] args = new String[]{"--a", "--b"};
        final WriteableCommandLine commandLine = new DefaultCommandLine();

        try {
            parser.parse(allOptions, commandLine, args);
            Assert.assertTrue(commandLine.hasOption("a"));
            Assert.assertTrue(commandLine.hasOption("b"));
            Assert.assertEquals(2, commandLine.getNumOptions());
        } catch (ParseException e) {
            // expected since we have reached the maximum number of options
        }

        final String[] wrongArgs = new String[]{"--a", "--a", "--b"};
        try {
            parser.parse(allOptions, commandLine, wrongArgs);
            Assert.fail("ParseException expected but not thrown");
        } catch (ParseException e) {
            // expected since we have added more options than the maximum
        }

        final String[] missingArgs = new String[0];
        try {
            parser.parse(allOptions, commandLine, missingArgs);
            Assert.fail("ParseException expected but not thrown");
        } catch (ParseException e) {
            // expected since we haven't provided enough options
        }
    }

    @Test
    public void testGroupWithOptionalOptions() {
        final List options = new ArrayList();

        options.add(createOption("A", "a", false, "A"));
        options.add(createOption("B", "b", false, "B"));

        final Group group = new GroupImpl(options, "MyGroup", null, 0, Integer.MAX_VALUE);

        final Options allOptions = new Options();
        allOptions.addOption(group);

        final String[] args = new String[]{};
        final WriteableCommandLine commandLine = new DefaultCommandLine();

        try {
            parser.parse(allOptions, commandLine, args);
            // optional options are not required, so it's okay if they are not present
        } catch (ParseException e) {
            Assert.fail("ParseException unexpected");
        }

        final String[] argsWithOneOption = new String[]{"--a"};
        try {
            parser.parse(allOptions, commandLine, argsWithOneOption);
            Assert.assertTrue(commandLine.hasOption("a"));
            Assert.assertFalse(commandLine.hasOption("b"));
            Assert.assertEquals(1, commandLine.getNumOptions());
        } catch (ParseException e) {
            Assert.fail("ParseException unexpected");
        }

        final String[] argsWithBothOptions = new String[]{"--a", "--b"};
        try {
            parser.parse(allOptions, commandLine, argsWithBothOptions);
            Assert.assertTrue(commandLine.hasOption("a"));
            Assert.assertTrue(commandLine.hasOption("b"));
            Assert.assertEquals(2, commandLine.getNumOptions());
        } catch (ParseException e) {
            Assert.fail("ParseException unexpected");
        }
    }

    @Test
    public void testGroupWithAnonymousArguments() {
        final List options = new ArrayList();

        options.add(createOption("A", "a", false, "A"));
        options.add(createArgument("arg1", true, "Argument for A"));
        options.add(createArgument("arg2", true, "Argument for B"));

        final Group group = new GroupImpl(options, "MyGroup", null, 0, Integer.MAX_VALUE);

        final Options allOptions = new Options();
        allOptions.addOption(group);

        final String[] args = new String[]{"--a=value1", "--arg1=value1", "--arg2=value2"};
        final WriteableCommandLine commandLine = new DefaultCommandLine();

        try {
            parser.parse(allOptions, commandLine, args);
            Assert.assertEquals("value1", commandLine.getOptionValue("a"));
            Assert.assertEquals("value1", commandLine.getOptionValue("arg1"));
            Assert.assertEquals("value2", commandLine.getOptionValue("arg2"));
            Assert.assertEquals(3, commandLine.getNumOptions());
        } catch (ParseException e) {
            Assert.fail("ParseException unexpected");
        }
    }

    @Test
    public void testGroupWithMixedOptionsAndAnonymousArguments() {
        final List options = new ArrayList();

        options.add(createOption("A", "a", false, "A"));
        options.add(createArgument("arg1", true, "Argument for A"));
        options.add(createOption("B", "b", false, "B"));
        options.add(createArgument("arg2", true, "Argument for B"));

        final Group group = new GroupImpl(options, "MyGroup", null, 0, Integer.MAX_VALUE);

        final Options allOptions = new Options();
        allOptions.addOption(group);

        final String[] args = new String[]{"--a=value1", "--arg1=value1", "--b=value2", "--arg2=value3"};
        final WriteableCommandLine commandLine = new DefaultCommandLine();

        try {
            parser.parse(allOptions, commandLine, args);
            Assert.assertEquals("value1", commandLine.getOptionValue("a"));
            Assert.assertEquals("value1", commandLine.getOptionValue("arg1"));
            Assert.assertEquals("value2", commandLine.getOptionValue("b"));
            Assert.assertEquals("value3", commandLine.getOptionValue("arg2"));
            Assert.assertEquals(4, commandLine.getNumOptions());
        } catch (ParseException e) {
            Assert.fail("ParseException unexpected");
        }
    }

    @Test
    public void testGroupWithNestedOptions() {
        final List options = new ArrayList();

        final List innerOptions = new ArrayList();
        innerOptions.add(createOption("innerA", "innerA", true, "Inner A"));
        innerOptions.add(createArgument("innerArg1", true, "Argument for Inner A"));

        options.add(createOption("outerA", "outerA", false, "Outer A"));
        options.add(createOption("outerB", "outerB", false, "Outer B"));
        options.add(createGroup(new GroupImpl(innerOptions, "Inner Group", null, 0, Integer.MAX_VALUE), "Inner Group"));

        final Group group = new GroupImpl(options, "MyGroup", null, 0, Integer.MAX_VALUE);

        final Options allOptions = new Options();
        allOptions.addOption(group);

        final String[] args = new String[]{"--outerA", "--outerB", "--innerA=value1", "--innerArg1=value2"};
        final WriteableCommandLine commandLine = new DefaultCommandLine();

        try {
            parser.parse(allOptions, commandLine, args);
            Assert.assertFalse(commandLine.hasOption("innerA")); // outerA is present, so inner options are not parsed
            Assert.assertEquals("value1", commandLine.getOptionValue("innerA"));
            Assert.assertEquals("value2", commandLine.getOptionValue("innerArg1"));
            Assert.assertEquals(4, commandLine.getNumOptions());
        } catch (ParseException e) {
            Assert.fail("ParseException unexpected");
        }

        final String[] argsWithOuterA = new String[]{"--outerA", "--outerB", "--innerA=value1", "--innerArg1=value2"};
        try {
            parser.parse(allOptions, commandLine, argsWithOuterA);
            Assert.assertTrue(commandLine.hasOption("outerA"));
            Assert.assertTrue(commandLine.hasOption("outerB"));
            Assert.assertTrue(commandLine.hasOption("innerA"));
            Assert.assertTrue(commandLine.hasOption("innerArg1"));
            Assert.assertEquals(5, commandLine.getNumOptions());
        } catch (ParseException e) {
            Assert.fail("ParseException unexpected");
        }
    }

    @Test
    public void testGroupWithMixedNestedOptionsAndAnonymousArguments() {
        final List options = new ArrayList();

        final List innerOptions = new ArrayList();
        innerOptions.add(createOption("innerA", "innerA", true, "Inner A"));
        innerOptions.add(createArgument("innerArg1", true, "Argument for Inner A"));

        options.add(createOption("outerA", "outerA", false, "Outer A"));
        options.add(createOption("outerB", "outerB", false, "Outer B"));
        options.add(createOption("outerC", "outerC", false, "Outer C"));
        options.add(createGroup(new GroupImpl(innerOptions, "Inner Group", null, 0, Integer.MAX_VALUE), "Inner Group"));
        options.add(createArgument("outerArg", false, "Argument for outer"));

        final Group group = new GroupImpl(options, "MyGroup", null, 0, Integer.MAX_VALUE);

        final Options allOptions = new Options();
        allOptions.addOption(group);

        final String[] args = new String[]{"--outerA", "--outerB", "--outerC", "--innerA=value1", "--innerArg1=value2", "--outerArg=outerArgValue"};
        final WriteableCommandLine commandLine = new DefaultCommandLine();

        try {
            parser.parse(allOptions, commandLine, args);
            Assert.assertFalse(commandLine.hasOption("innerA")); // outerA is present, so inner options are not parsed
            Assert.assertEquals("value1", commandLine.getOptionValue("innerA"));
            Assert.assertEquals("value2", commandLine.getOptionValue("innerArg1"));
            Assert.assertEquals("outerArgValue", commandLine.getOptionValue("outerArg"));
            Assert.assertEquals(6, commandLine.getNumOptions());
        } catch (ParseException e) {
            Assert.fail("ParseException unexpected");
        }

        final String[] argsWithOuterA = new String[]{"--outerA", "--outerB", "--outerC", "--innerA=value1", "--innerArg1=value2", "--outerArg=outerArgValue"};
        try {
            parser.parse(allOptions, commandLine, argsWithOuterA);
            Assert.assertTrue(commandLine.hasOption("outerA"));
            Assert.assertTrue(commandLine.hasOption("outerB"));
            Assert.assertTrue(commandLine.hasOption("outerC"));
            Assert.assertTrue(commandLine.hasOption("innerA"));
            Assert.assertTrue(commandLine.hasOption("innerArg1"));
            Assert.assertTrue(commandLine.hasOption("outerArg"));
            Assert.assertEquals(7, commandLine.getNumOptions());
        } catch (ParseException e) {
            Assert.fail("ParseException unexpected");
        }
    }

    private Option createOption(final String longOpt,
                               final String opt,
                               final boolean required,
                               final String description) {
        return new OptionBuilder()
                .longOpt(longOpt)
                .opt(opt)
                .required(required)
                .desc(description)
                .build();
    }

    private Argument createArgument(final String longOpt,
                                    final boolean required,
                                    final String description) {
        return new ArgumentBuilder()
                .longOpt(longOpt)
                .required(required)
                .description(description)
                .build();
    }

    private Group createGroup(final Group group, final String description) {
        return new OptionBuilder()
                .group(group)
                .description(description)
                .build();
    }
}
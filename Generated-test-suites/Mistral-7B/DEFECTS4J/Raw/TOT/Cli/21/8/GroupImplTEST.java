package org.apache.commons.cli2.option;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.parse.DefaultParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Test class for GroupImpl.
 */
public class GroupImplTest {

    /**
     * Test method for {@link GroupImpl#GroupImpl(List, String, String, int, int, boolean)}
     * with no options and no name and description.
     */
    @Test
    public void testGroupNoOptions() {
        final List options = new ArrayList();
        final Group group = new GroupImpl(options, null, null, 1, 1, false);
        final Options parsedOptions = new Options();
        parsedOptions.addGroup(group);
        final DefaultParser parser = new DefaultParser();

        // test parsing with valid arguments
        final String[] args = new String[]{"test", "--group"};
        try {
            final CommandLine commandLine = parser.parse(parsedOptions, args);
            Assert.assertNull(commandLine.getOptionValue("group"));
        } catch (final OptionException e) {
            Assert.fail("Should not throw any exception.");
        }

        // test parsing with invalid arguments
        final String[] invalidArgs = new String[]{"test", "--invalid-group"};
        try {
            parser.parse(parsedOptions, invalidArgs);
            Assert.fail("Should have thrown an OptionException.");
        } catch (final OptionException e) {
            // expected
        }

        // test help
        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("test", parsedOptions);
        Assert.assertTrue(parsedOptions.getOption("group") != null);
    }

    /**
     * Test method for {@link GroupImpl#GroupImpl(List, String, String, int, int, boolean)}
     * with an option with no value.
     */
    @Test
    public void testGroupWithNoValueOption() {
        final List options = new ArrayList();
        final Option option = new Option("g", "group", false, "A group option.");
        options.add(option);
        final Group group = new GroupImpl(options, "Group", "A group.", 1, 1, false);
        final Options parsedOptions = new Options();
        parsedOptions.addGroup(group);
        final DefaultParser parser = new DefaultParser();

        // test parsing with valid arguments
        final String[] args = new String[]{"test", "--group"};
        try {
            final CommandLine commandLine = parser.parse(parsedOptions, args);
            Assert.assertNull(commandLine.getOptionValue("group"));
            Assert.assertTrue(commandLine.hasOption("group"));
        } catch (final OptionException e) {
            Assert.fail("Should not throw any exception.");
        }

        // test help
        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("test", parsedOptions);
        Assert.assertTrue(parsedOptions.getOption("group") != null);
    }

    /**
     * Test method for {@link GroupImpl#GroupImpl(List, String, String, int, int, boolean)}
     * with an option with a value.
     */
    @Test
    public void testGroupWithValueOption() {
        final List options = new ArrayList();
        final Option option = new Option("g", "group", true, "A group option with value.");
        options.add(option);
        final Group group = new GroupImpl(options, "Group", "A group.", 1, 1, false);
        final Options parsedOptions = new Options();
        parsedOptions.addGroup(group);
        final DefaultParser parser = new DefaultParser();

        // test parsing with valid arguments
        final String[] args = new String[]{"test", "--group=test"};
        try {
            final CommandLine commandLine = parser.parse(parsedOptions, args);
            Assert.assertEquals("test", commandLine.getOptionValue("group"));
            Assert.assertTrue(commandLine.hasOption("group"));
        } catch (final OptionException e) {
            Assert.fail("Should not throw any exception.");
        }

        // test parsing with invalid arguments
        final String[] invalidArgs = new String[]{"test", "--group"};
        try {
            parser.parse(parsedOptions, invalidArgs);
            Assert.fail("Should have thrown an OptionException.");
        } catch (final OptionException e) {
            // expected
        }

        // test help
        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("test", parsedOptions);
        Assert.assertTrue(parsedOptions.getOption("group") != null);
    }

    /**
     * Test method for {@link GroupImpl#GroupImpl(List, String, String, int, int, boolean)}
     * with an option that can only appear once.
     */
    @Test
    public void testGroupWithSingleOption() {
        final List options = new ArrayList();
        final Option option = new Option("g", "group", true, "A group option.");
        options.add(option);
        final Option singleOption = new Option("s", "single-group", false, "A single group option.");
        options.add(singleOption);
        singleOption.setMinOccurs(0);
        singleOption.setMaxOccurs(1);
        final Group group = new GroupImpl(options, "Group", "A group.", 1, 1, false);
        final Options parsedOptions = new Options();
        parsedOptions.addGroup(group);
        final DefaultParser parser = new DefaultParser();

        // test parsing with valid arguments
        final String[] args = new String[]{"test", "--group", "--single-group"};
        try {
            final CommandLine commandLine = parser.parse(parsedOptions, args);
            Assert.assertTrue(commandLine.hasOption("group"));
            Assert.assertTrue(commandLine.hasOption("single-group"));
        } catch (final OptionException e) {
            Assert.fail("Should not throw any exception.");
        }

        // test parsing with invalid arguments
        final String[] invalidArgs = new String[]{"test", "--group", "--group"};
        try {
            parser.parse(parsedOptions, invalidArgs);
            Assert.fail("Should have thrown an OptionException.");
        } catch (final OptionException e) {
            // expected
        }

        // test help
        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("test", parsedOptions);
        Assert.assertTrue(parsedOptions.getOption("group") != null);
        Assert.assertTrue(parsedOptions.getOption("single-group") != null);
    }

    /**
     * Test method for {@link GroupImpl#GroupImpl(List, String, String, int, int, boolean)}
     * with an option that can appear multiple times.
     */
    @Test
    public void testGroupWithMultipleOptions() {
        final List options = new ArrayList();
        final Option option = new Option("g", "group", true, "A group option.");
        options.add(option);
        final Group group = new GroupImpl(options, "Group", "A group.", 1, 1, false);
        final Options parsedOptions = new Options();
        parsedOptions.addGroup(group);
        final DefaultParser parser = new DefaultParser();

        // test parsing with valid arguments
        final String[] args = new String[]{"test", "--group=test1", "--group=test2"};
        try {
            final CommandLine commandLine = parser.parse(parsedOptions, args);
            Assert.assertEquals(2, commandLine.getOptionValues("group").length);
            Assert.assertEquals("test1", commandLine.getOptionValue("group")[0]);
            Assert.assertEquals("test2", commandLine.getOptionValue("group")[1]);
            Assert.assertTrue(commandLine.hasOption("group"));
        } catch (final OptionException e) {
            Assert.fail("Should not throw any exception.");
        }

        // test parsing with invalid arguments
        final String[] invalidArgs = new String[]{"test", "--group", "--group", "--group"};
        try {
            parser.parse(parsedOptions, invalidArgs);
            Assert.fail("Should have thrown an OptionException.");
        } catch (final OptionException e) {
            // expected
        }

        // test help
        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("test", parsedOptions);
        Assert.assertTrue(parsedOptions.getOption("group") != null);
    }

    /**
     * Test method for {@link GroupImpl#GroupImpl(List, String, String, int, int, boolean)}
     * with anonymous arguments.
     */
    @Test
    public void testGroupWithAnonymousArguments() {
        final List options = new ArrayList();
        final Option option = new Option("g", "group", true, "A group option.");
        options.add(option);
        final Option anonymousOption = new Option(null, "", true, "An anonymous argument.");
        options.add(anonymousOption);
        final Group group = new GroupImpl(options, "Group", "A group.", 1, 1, false);
        final Options parsedOptions = new Options();
        parsedOptions.addGroup(group);
        final DefaultParser parser = new DefaultParser();

        // test parsing with valid arguments
        final String[] args = new String[]{"test", "--group", "arg1", "--"};
        try {
            final CommandLine commandLine = parser.parse(parsedOptions, args);
            Assert.assertEquals("arg1", commandLine.getOptionValue("anonymous"));
            Assert.assertTrue(commandLine.hasOption("anonymous"));
            Assert.assertNull(commandLine.getOptionValue("group"));
            Assert.assertNull(commandLine.getOption("group"));
        } catch (final OptionException e) {
            Assert.fail("Should not throw any exception.");
        }

        // test parsing with invalid arguments
        final String[] invalidArgs = new String[]{"test", "--group=test", "--anonymous", "--"};
        try {
            parser.parse(parsedOptions, invalidArgs);
            Assert.fail("Should have thrown an OptionException.");
        } catch (final OptionException e) {
            // expected
        }

        // test help
        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("test", parsedOptions);
        Assert.assertTrue(parsedOptions.getOption("group") != null);
        Assert.assertTrue(parsedOptions.getOption("anonymous") != null);
    }

    /**
     * Test method for {@link GroupImpl#GroupImpl(List, String, String, int, int, boolean)}
     * with an option that requires bursting.
     */
    @Test
    public void testGroupWithBurstingOption() {
        final List options = new ArrayList();
        final Option option = new Option("g", "group", true, "A group option.");
        options.add(option);
        final Option burstingOption = new Option("b", "bursting", true, "A bursting option.");
        options.add(burstingOption);
        final Group group = new GroupImpl(options, "Group", "A group.", 1, 1, false);
        final Options parsedOptions = new Options();
        parsedOptions.addGroup(group);
        final DefaultParser parser = new DefaultParser();

        // test parsing with valid arguments
        final String[] args = new String[]{"test", "--group", "--bursting", "--bursting"};
        try {
            final CommandLine commandLine = parser.parse(parsedOptions, args);
            Assert.assertEquals(2, commandLine.getOptionValues("bursting").length);
            Assert.assertEquals("bursting", commandLine.getOptionValue("bursting")[0]);
            Assert.assertEquals("bursting", commandLine.getOptionValue("bursting")[1]);
            Assert.assertTrue(commandLine.hasOption("group"));
            Assert.assertTrue(commandLine.hasOption("bursting"));
        } catch (final OptionException e) {
            Assert.fail("Should not throw any exception.");
        }

        // test parsing with invalid arguments
        final String[] invalidArgs = new String[]{"test", "--group", "--bursting", "--bursting", "--bursting"};
        try {
            parser.parse(parsedOptions, invalidArgs);
            Assert.fail("Should have thrown an OptionException.");
        } catch (final OptionException e) {
            // expected
        }

        // test help
        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("test", parsedOptions);
        Assert.assertTrue(parsedOptions.getOption("group") != null);
        Assert.assertTrue(parsedOptions.getOption("bursting") != null);
    }
}
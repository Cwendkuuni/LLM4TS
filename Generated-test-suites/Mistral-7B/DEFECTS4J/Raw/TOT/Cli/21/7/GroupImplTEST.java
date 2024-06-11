package org.apache.commons.cli2.option;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GroupImplTest {

    @Test
    public void testEmptyGroup() {
        final Options options = new Options();
        final Group group = new GroupImpl(Collections.emptyList(), null, null, 0, 0, false);
        options.addOption(group);

        final CommandLine parsed = new CommandLine();
        final HelpFormatter formatter = new HelpFormatter();

        try {
            options.parse(parsed, new String[0]);
            Assert.fail();
        } catch (ParseException e) {
            // expected
        }

        final String help = formatter.printHelp("testEmptyGroup", options);
        System.out.println(help);
    }

    @Test
    public void testSingleOptionGroup() {
        final Options options = new Options();
        final String groupName = "single";

        final Group group = new GroupImpl(
                Arrays.asList(
                        new Option("single", "single option")),
                groupName,
                null,
                0,
                1,
                false);
        options.addOption(group);

        final CommandLine parsed = new CommandLine();
        final HelpFormatter formatter = new HelpFormatter();

        try {
            options.parse(parsed, new String[0]);
            Assert.fail();
        } catch (ParseException e) {
            // expected
        }

        final String help = formatter.printHelp("testSingleOptionGroup", options);
        System.out.println(help);

        parsed.setOption(groupName, "single");
        options.parse(parsed, new String[0]);

        Assert.assertTrue(parsed.hasOption(groupName));
    }

    @Test
    public void testSingleOptionGroupWithMultipleOptions() {
        final Options options = new Options();
        final String groupName = "single";

        final Group group = new GroupImpl(
                Arrays.asList(
                        new Option("single1", "first single option"),
                        new Option("single2", "second single option")),
                groupName,
                null,
                0,
                1,
                false);
        options.addOption(group);

        final CommandLine parsed = new CommandLine();
        final HelpFormatter formatter = new HelpFormatter();

        try {
            options.parse(parsed, new String[0]);
            Assert.fail();
        } catch (ParseException e) {
            // expected
        }

        final String help = formatter.printHelp("testSingleOptionGroupWithMultipleOptions", options);
        System.out.println(help);

        parsed.setOption(groupName, "single1");
        try {
            options.parse(parsed, new String[0]);
            Assert.fail();
        } catch (ParseException e) {
            // expected
        }

        parsed.setOption(groupName, "single2");
        try {
            options.parse(parsed, new String[0]);
            Assert.fail();
        } catch (ParseException e) {
            // expected
        }

        parsed.setOption(groupName, "single1");
        parsed.setOption(groupName, "single2");
        try {
            options.parse(parsed, new String[0]);
            Assert.fail();
        } catch (ParseException e) {
            // expected, maximum 1
        }

        parsed.setOption(groupName, "single1");
        try {
            options.parse(parsed, new String[0]);
            Assert.fail();
        } catch (ParseException e) {
            // expected, minimum 1
        }

        parsed.setOption(groupName, "single1");
        parsed.setOption(groupName, "single1");
        try {
            options.parse(parsed, new String[0]);
            Assert.fail();
        } catch (ParseException e) {
            // expected, maximum 1
        }
    }

    @Test
    public void testSingleOptionGroupWithArgument() {
        final Options options = new Options();
        final String groupName = "single";

        final Group group = new GroupImpl(
                Arrays.asList(
                        new Option("single", "single option", true, "singleArg")),
                groupName,
                null,
                0,
                1,
                false);
        options.addOption(group);

        final CommandLine parsed = new CommandLine();
        final HelpFormatter formatter = new HelpFormatter();

        try {
            options.parse(parsed, new String[0]);
            Assert.fail();
        } catch (ParseException e) {
            // expected
        }

        final String help = formatter.printHelp("testSingleOptionGroupWithArgument", options);
        System.out.println(help);

        parsed.parseArgument(groupName, "singleArg1");
        try {
            options.parse(parsed, new String[0]);
            Assert.fail();
        } catch (ParseException e) {
            // expected
        }

        parsed.parseArgument(groupName, "singleArg2");
        try {
            options.parse(parsed, new String[0]);
            Assert.fail();
        } catch (ParseException e) {
            // expected, maximum 1
        }

        parsed.parseArgument(groupName, "singleArg1");
        parsed.parseArgument(groupName, "singleArg2");
        try {
            options.parse(parsed, new String[0]);
            Assert.fail();
        } catch (ParseException e) {
            // expected, maximum 1
        }
    }

    @Test
    public void testMultipleOptionGroup() {
        final Options options = new Options();
        final String groupName = "multiple";

        final Group group = new GroupImpl(
                Arrays.asList(
                        new Option("multiple1", "first multiple option"),
                        new Option("multiple2", "second multiple option")),
                groupName,
                null,
                0,
                2,
                false);
        options.addOption(group);

        final CommandLine parsed = new CommandLine();
        final HelpFormatter formatter = new HelpFormatter();

        try {
            options.parse(parsed, new String[0]);
            Assert.fail();
        } catch (ParseException e) {
            // expected
        }

        final String help = formatter.printHelp("testMultipleOptionGroup", options);
        System.out.println(help);

        parsed.setOption(groupName, "multiple1");
        parsed.setOption(groupName, "multiple2");
        try {
            options.parse(parsed, new String[0]);
            Assert.fail();
        } catch (ParseException e) {
            // expected, maximum 2
        }

        parsed.setOption(groupName, "multiple1");
        parsed.setOption(groupName, "multiple2");
        parsed.setOption(groupName, "multiple1");
        try {
            options.parse(parsed, new String[0]);
            Assert.fail();
        } catch (ParseException e) {
            // expected, maximum 2
        }
    }

    @Test
    public void testOptionGroupWithMixedOptionsAndArguments() {
        final Options options = new Options();
        final String groupName = "mixed";

        final Group group = new GroupImpl(
                Arrays.asList(
                        new Option("mixed1", "first mixed option", true, "mixedArg1"),
                        new Option("mixed2", "second mixed option"),
                        new Option("mixed3", "third mixed option", true),
                        new Option("mixed4", "fourth mixed option")),
                groupName,
                null,
                0,
                1,
                false);
        options.addOption(group);

        final CommandLine parsed = new CommandLine();
        final HelpFormatter formatter = new HelpFormatter();

        try {
            options.parse(parsed, new String[0]);
            Assert.fail();
        } catch (ParseException e) {
            // expected
        }

        final String help = formatter.printHelp("testOptionGroupWithMixedOptionsAndArguments", options);
        System.out.println(help);

        parsed.setOption(groupName, "mixed1", "mixedArg11");
        parsed.setOption(groupName, "mixed2");
        parsed.parseArgument(groupName, "mixedArg12");
        parsed.setOption(groupName, "mixed3");
        parsed.parseArgument(groupName, "mixedArg3");
        try {
            options.parse(parsed, new String[0]);
            Assert.fail();
        } catch (ParseException e) {
            // expected, maximum 1 for option "mixed1"
        }

        parsed.setOption(groupName, "mixed1", "mixedArg11");
        parsed.setOption(groupName, "mixed2");
        parsed.parseArgument(groupName, "mixedArg12");
        parsed.parseArgument(groupName, "mixedArg3");
        parsed.setOption(groupName, "mixed4");
        try {
            options.parse(parsed, new String[0]);
            Assert.fail();
        } catch (ParseException e) {
            // expected, minimum 1 for option "mixed4"
        }
    }

    @Test
    public void testOptionGroupWithOptionalOptions() {
        final Options options = new Options();
        final String groupName = "optional";

        final Group group = new GroupImpl(
                Arrays.asList(
                        new Option("optional1", "first optional option", false),
                        new Option("optional2", "second optional option", false),
                        new Option("optional3", "third optional option", false)),
                groupName,
                null,
                0,
                1,
                false);
        options.addOption(group);

        final CommandLine parsed = new CommandLine();
        final HelpFormatter formatter = new HelpFormatter();

        try {
            options.parse(parsed, new String[0]);
            Assert.fail();
        } catch (ParseException e) {
            // expected
        }

        final String help = formatter.printHelp("testOptionGroupWithOptionalOptions", options);
        System.out.println(help);

        parsed.setOption(groupName, "optional1");
        parsed.setOption(groupName, "optional2");
        parsed.setOption(groupName, "optional3");
        try {
            options.parse(parsed, new String[0]);
            Assert.fail();
        } catch (ParseException e) {
            // expected, maximum 1 for each optional option
        }

        parsed.setOption(groupName, "optional1");
        parsed.setOption(groupName, "optional2");
        try {
            options.parse(parsed, new String[0]);
            Assert.fail();
        } catch (ParseException e) {
            // expected, minimum 0 for each optional option
        }
    }
}
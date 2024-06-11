package org.apache.commons.cli2.option;

import static org.junit.Assert.*;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.ParseException;
import org.apache.commons.cli2.groups.GroupImpl;

public class GroupImplTest {

    @org.junit.Test
    public void testBasicUsage() {
        final Options options = new Options();
        final Group group = new Group("group1")
                .requiresArgument()
                .addOption(OptionBuilder.withName("option1")
                        .hasArg()
                        .isRequired()
                        .build())
                .addOption(OptionBuilder.withName("option2")
                        .hasArg()
                        .isRequired()
                        .build())
                .addOption(OptionBuilder.withName("option3")
                        .hasArg()
                        .isRequired()
                        .build());
        options.addGroup(group);

        final String[] args = {"-g", "group1", "--option1", "value1", "--option2", "value2", "--option3", "value3"};

        final CommandLine parsed = new CommandLineParser().parse(options, args);
        final HelpFormatter formatter = new HelpFormatter();

        formatter.printHelp("TestApp", options);

        assertEquals("value1", parsed.getOptionValue("option1"));
        assertEquals("value2", parsed.getOptionValue("option2"));
        assertEquals("value3", parsed.getOptionValue("option3"));
    }

    @org.junit.Test
    public void testMissingOption() {
        final Options options = new Options();
        final Group group = new Group("group1")
                .requiresArgument()
                .addOption(OptionBuilder.withName("option1")
                        .hasArg()
                        .isRequired()
                        .build())
                .addOption(OptionBuilder.withName("option2")
                        .hasArg()
                        .isRequired()
                        .build())
                .addOption(OptionBuilder.withName("option3")
                        .hasArg()
                        .isRequired()
                        .build());
        options.addGroup(group);

        final String[] args = {"-g", "group1"};

        try {
            new CommandLineParser().parse(options, args);
            fail("Expected ParseException");
        } catch (ParseException e) {
            final String message = e.getMessage();
            assertTrue(message.contains("missing"));
            assertTrue(message.contains("option2"));
            assertTrue(message.contains("option3"));
        }
    }

    @org.junit.Test
    public void testExtraOption() {
        final Options options = new Options();
        final Group group = new Group("group1")
                .requiresArgument()
                .addOption(OptionBuilder.withName("option1")
                        .hasArg()
                        .isRequired()
                        .build())
                .addOption(OptionBuilder.withName("option2")
                        .hasArg()
                        .isRequired()
                        .build())
                .addOption(OptionBuilder.withName("option3")
                        .hasArg()
                        .isRequired()
                        .build());
        options.addGroup(group);

        final String[] args = {"-g", "group1", "--option1", "value1", "--option2", "value2", "--option3", "value3", "--extra"};

        try {
            new CommandLineParser().parse(options, args);
            fail("Expected ParseException");
        } catch (ParseException e) {
            final String message = e.getMessage();
            assertTrue(message.contains("unrecognized"));
            assertTrue(message.contains("--extra"));
        }
    }

    @org.junit.Test
    public void testMixedUsage() {
        final Options options = new Options();
        final Group group = new Group("group1")
                .requiresArgument()
                .addOption(OptionBuilder.withName("option1")
                        .hasArg()
                        .isRequired()
                        .build())
                .addOption(OptionBuilder.withName("option2")
                        .hasArg()
                        .isRequired()
                        .build())
                .addOption(OptionBuilder.withName("option3")
                        .hasArg()
                        .isRequired()
                        .build());
        options.addGroup(group);

        final String[] args = {"-g", "group1", "--option1", "value1", "--option3", "value3"};

        try {
            new CommandLineParser().parse(options, args);
            fail("Expected ParseException");
        } catch (ParseException e) {
            final String message = e.getMessage();
            assertTrue(message.contains("missing"));
            assertTrue(message.contains("option2"));
        }
    }
}
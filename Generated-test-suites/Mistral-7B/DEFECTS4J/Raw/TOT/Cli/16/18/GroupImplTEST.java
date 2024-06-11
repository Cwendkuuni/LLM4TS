package org.apache.commons.cli2.option;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.CommandLineParser;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.builder.ArgumentBuilder;
import org.apache.commons.cli2.builder.OptionBuilder;
import org.apache.commons.cli2.exceptions.OptionException;
import org.junit.Assert;
import org.junit.Test;

/**
 * GroupImplTest
 */
public class GroupImplTest {

    @Test
    public void testBasicUsage() throws OptionException {
        final CommandLineParser parser = new DefaultParser();

        final Options options = new Options();
        options.addGroup(new OptionBuilder("g")
                .longOpt("group")
                .requiredOpt(false)
                .desc("Test Group")
                .build());
        options.addOption(new OptionBuilder("a")
                .longOpt("argument")
                .hasArg(true)
                .desc("Test Argument")
                .build());

        final HelpFormatter formatter = new HelpFormatter();

        final String[] args = new String[]{"--group", "Test"};
        final CommandLine commandLine = parser.parse(options, args);

        Assert.assertEquals(args.length, commandLine.getArgs().size());
        Assert.assertNull(commandLine.getOptionValues("g"));

        formatter.printHelp("Test", options);
        System.out.println("\nPrinted help:\n");
        System.out.println(formatter.getHelpText(true));
    }

    @Test
    public void testMinimumUsage() throws OptionException {
        final CommandLineParser parser = new DefaultParser();

        final Options options = new Options();
        options.addGroup(new OptionBuilder("g")
                .longOpt("group")
                .requiredOpt(true)
                .desc("Test Group")
                .build());
        options.addOption(new OptionBuilder("a")
                .longOpt("argument")
                .hasArg(true)
                .desc("Test Argument")
                .build());

        final HelpFormatter formatter = new HelpFormatter();

        // Test with valid input
        final String[] args1 = new String[]{"--group", "Test"};
        final CommandLine commandLine1 = parser.parse(options, args1);

        Assert.assertEquals(args1.length, commandLine1.getArgs().size());
        Assert.assertArrayEquals(new String[]{"Test"}, commandLine1.getOptionValues("g"));

        formatter.printHelp("Test", options);
        System.out.println("\nPrinted help:\n");
        System.out.println(formatter.getHelpText(true));

        // Test with invalid input
        final String[] args2 = new String[]{};
        try {
            parser.parse(options, args2);
        } catch (OptionException e) {
            System.out.println("Expected error: " + e.getMessage());
        }

        final String[] args3 = new String[]{"--group", "Test1", "--group", "Test2"};
        try {
            parser.parse(options, args3);
        } catch (OptionException e) {
            System.out.println("Expected error: " + e.getMessage());
        }
    }

    @Test
    public void testMaximumUsage() throws OptionException {
        final CommandLineParser parser = new DefaultParser();

        final Options options = new Options();
        options.addGroup(new OptionBuilder("g")
                .longOpt("group")
                .requiredOpt(false)
                .desc("Test Group")
                .build());
        options.addGroup(new OptionBuilder("g2")
                .longOpt("group2")
                .requiredOpt(true)
                .desc("Test Group2")
                .build());
        options.addOption(new OptionBuilder("a")
                .longOpt("argument")
                .hasArg(true)
                .desc("Test Argument")
                .build());

        final HelpFormatter formatter = new HelpFormatter();

        // Test with valid input
        final String[] args1 = new String[]{"--group", "Test"};
        final CommandLine commandLine1 = parser.parse(options, args1);

        Assert.assertEquals(args1.length, commandLine1.getArgs().size());
        Assert.assertNull(commandLine1.getOptionValues("g"));

        final String[] args2 = new String[]{"--group", "Test", "--group2", "Test2"};
        final CommandLine commandLine2 = parser.parse(options, args2);

        Assert.assertEquals(args2.length, commandLine2.getArgs().size());
        Assert.assertArrayEquals(new String[]{"Test"}, commandLine1.getOptionValues("g"));
        Assert.assertArrayEquals(new String[]{"Test2"}, commandLine2.getOptionValues("g2"));

        formatter.printHelp("Test", options);
        System.out.println("\nPrinted help:\n");
        System.out.println(formatter.getHelpText(true));

        // Test with more than maximum input
        final String[] args3 = new String[]{"--group", "Test", "--group2", "Test2", "--group2", "Test3"};
        try {
            parser.parse(options, args3);
        } catch (OptionException e) {
            System.out.println("Expected error: " + e.getMessage());
        }
    }

    @Test
    public void testAnonymousArgumentUsage() throws OptionException {
        final CommandLineParser parser = new DefaultParser();

        final Options options = new Options();
        options.addGroup(new OptionBuilder("g")
                .longOpt("group")
                .requiredOpt(false)
                .desc("Test Group")
                .build());
        options.addOption(new OptionBuilder("a")
                .longOpt("argument")
                .hasArg(true)
                .desc("Test Argument")
                .build());
        options.addOption(new OptionBuilder("a2")
                .longOpt("argument2")
                .hasArg(true)
                .desc("Test Argument2")
                .build());

        final HelpFormatter formatter = new HelpFormatter();

        // Test with valid input
        final String[] args1 = new String[]{"--group", "Test", "--argument", "Value1", "--argument2", "Value2"};
        final CommandLine commandLine1 = parser.parse(options, args1);

        Assert.assertEquals(args1.length, commandLine1.getArgs().size());
        Assert.assertArrayEquals(new String[]{"Value1"}, commandLine1.getOptionValues("a"));
        Assert.assertArrayEquals(new String[]{"Value2"}, commandLine1.getOptionValues("a2"));

        formatter.printHelp("Test", options);
        System.out.println("\nPrinted help:\n");
        System.out.println(formatter.getHelpText(true));

        // Test with mandatory anonymous argument
        final String[] args2 = new String[]{"--group", "Test"};
        try {
            parser.parse(options, args2);
        } catch (OptionException e) {
            System.out.println("Expected error: " + e.getMessage());
        }

        // Test with valid input with anonymous arguments
        final String[] args3 = new String[]{"--group", "Test", "--argument", "Value1", "--argument2", "Value2", "Value3"};
        final CommandLine commandLine3 = parser.parse(options, args3);

        Assert.assertEquals(args3.length, commandLine3.getArgs().size());
        Assert.assertArrayEquals(new String[]{"Value1"}, commandLine3.getOptionValues("a"));
        Assert.assertArrayEquals(new String[]{"Value2"}, commandLine3.getOptionValues("a2"));
        Assert.assertEquals(3, commandLine3.getArgs().size());
    }

    @Test
    public void testMultipleGroupUsage() throws OptionException {
        final CommandLineParser parser = new DefaultParser();

        final Options options = new Options();
        options.addGroup(new OptionBuilder("g1")
                .longOpt("group1")
                .requiredOpt(false)
                .desc("Test Group1")
                .build());
        options.addGroup(new OptionBuilder("g2")
                .longOpt("group2")
                .requiredOpt(false)
                .desc("Test Group2")
                .build());
        options.addOption(new OptionBuilder("a")
                .longOpt("argument")
                .hasArg(true)
                .desc("Test Argument")
                .build());

        final HelpFormatter formatter = new HelpFormatter();

        // Test with valid input
        final String[] args1 = new String[]{"--group1", "Test1", "--g2", "Test2", "--argument", "Value1"};
        final CommandLine commandLine1 = parser.parse(options, args1);

        Assert.assertEquals(args1.length, commandLine1.getArgs().size());
        Assert.assertArrayEquals(new String[]{"Test1"}, commandLine1.getOptionValues("g1"));
        Assert.assertArrayEquals(new String[]{"Test2"}, commandLine1.getOptionValues("g2"));
        Assert.assertArrayEquals(new String[]{"Value1"}, commandLine1.getOptionValues("a"));

        formatter.printHelp("Test", options);
        System.out.println("\nPrinted help:\n");
        System.out.println(formatter.getHelpText(true));

        // Test with only one group
        final String[] args2 = new String[]{"--group1", "Test1", "--argument", "Value1"};
        final CommandLine commandLine2 = parser.parse(options, args2);

        Assert.assertEquals(args2.length, commandLine2.getArgs().size());
        Assert.assertArrayEquals(new String[]{"Test1"}, commandLine2.getOptionValues("g1"));
        Assert.assertNull(commandLine2.getOptionValues("g2"));
        Assert.assertArrayEquals(new String[]{"Value1"}, commandLine2.getOptionValues("a"));

        // Test with only one group and mandatory anonymous argument
        final String[] args3 = new String[]{"--group1", "Test1"};
        try {
            parser.parse(options, args3);
        } catch (OptionException e) {
            System.out.println("Expected error: " + e.getMessage());
        }

        // Test with multiple groups and mandatory anonymous argument
        final String[] args4 = new String[]{"--group1", "Test1", "--argument", "Value1", "--group2", "Test2"};
        try {
            parser.parse(options, args4);
        } catch (OptionException e) {
            System.out.println("Expected error: " + e.getMessage());
        }
    }
}
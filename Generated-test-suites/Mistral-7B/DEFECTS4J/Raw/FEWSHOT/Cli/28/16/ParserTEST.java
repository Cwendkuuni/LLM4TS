package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.NoSuchOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.ParsingException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParserEmpty() throws Exception {
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(new Options(), new String[0]);
        assertTrue(cmd.getArgs().isEmpty());
        assertNull(cmd.getOption(null));
    }

    @Test
    public void testParserHelp() throws Exception {
        Options options = new Options();
        options.addOption("h", "help", false, "Print this help message.");
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[] {"h"});
        assertTrue(cmd.hasOption("help"));
        assertFalse(cmd.hasArgNames());
    }

    @Test
    public void testParserHelpWithArgument() throws Exception {
        Options options = new Options();
        Option option = OptionBuilder.withLongOpt("o")
            .withDescription("output file")
            .isRequired()
            .hasArg()
            .create("o");
        options.addOption(option);
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[] {"o", "output.txt"});
        Option opt = cmd.getOption("o");
        assertSame(option, opt);
        assertTrue(opt.getValue().equals("output.txt"));
    }

    @Test(expected = MissingArgumentException.class)
    public void testParserMissingArgument() throws Exception {
        Options options = new Options();
        Option option = OptionBuilder.withLongOpt("o")
            .withDescription("output file")
            .isRequired()
            .hasArg()
            .create("o");
        options.addOption(option);
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[] {"h"});
        fail("MissingArgumentException expected.");
    }

    @Test(expected = NoSuchOptionException.class)
    public void testParserNoSuchOption() throws Exception {
        Options options = new Options();
        options.addOption("h", "help", false, "Print this help message.");
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[] {"x"});
        fail("NoSuchOptionException expected for option x.");
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParserUnrecognizedOption() throws Exception {
        Options options = new Options();
        options.addOption("h", "help", false, "Print this help message.");
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[] {"z"});
        fail("UnrecognizedOptionException expected for option z.");
    }

    @Test
    public void testParserMultipleOptions() throws Exception {
        Options options = new Options();
        Option option1 = OptionBuilder.withLongOpt("o1")
            .withDescription("Option 1")
            .isRequired()
            .hasArg()
            .create("o1");
        Option option2 = OptionBuilder.withLongOpt("o2")
            .withDescription("Option 2")
            .isRequired()
            .hasArg()
            .create("o2");
        options.addOption(option1);
        options.addOption(option2);
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[] {"o1", "a", "o2", "b"});
        Option opt1 = cmd.getOption("o1");
        Option opt2 = cmd.getOption("o2");
        assertSame(option1, opt1);
        assertTrue(opt1.getValue().equals("a"));
        assertSame(option2, opt2);
        assertTrue(opt2.getValue().equals("b"));
    }

    @Test
    public void testParserProperties() throws Exception {
        Options options = new Options();
        Option option = OptionBuilder.withLongOpt("o")
            .withDescription("output file")
            .isRequired()
            .hasArg()
            .create("o");
        options.addOption(option);
        OptionGroup group = new OptionGroup("g", "group");
        group.setDescription("A group.");
        group.addOption(OptionBuilder.withLongOpt("g1")
            .withDescription("Group option 1")
            .create("g1"));
        group.addOption(OptionBuilder.withLongOpt("g2")
            .withDescription("Group option 2")
            .create("g2"));
        options.addOptionGroup(group);
        Properties properties = new Properties();
        properties.setProperty("o", "output.txt");
        properties.setProperty("g1", "group-value1");
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[0], properties);
        Option opt = cmd.getOption("o");
        assertSame(option, opt);
        assertTrue(opt.getValue().equals("output.txt"));
        Option groupOption = cmd.getOption("g");
        assertSame(group, groupOption);
        Option groupOption1 = cmd.getOption("g1");
        assertSame(group.getOption("g1"), groupOption1);
        assertTrue(groupOption1.getValue().equals("group-value1"));
    }

    @Test
    public void testParserPropertiesMissingOption() throws Exception {
        Options options = new Options();
        Option option = OptionBuilder.withLongOpt("o")
            .withDescription("output file")
            .isRequired()
            .hasArg()
            .create("o");
        options.addOption(option);
        Properties properties = new Properties();
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[0], properties);
        assertNull(cmd.getOption("o"));
    }

    @Test
    public void testParserPropertiesMissingGroup() throws Exception {
        Options options = new Options();
        OptionGroup group = new OptionGroup("g", "group");
        group.setDescription("A group.");
        group.addOption(OptionBuilder.withLongOpt("g1")
            .withDescription("Group option 1")
            .create("g1"));
        group.addOption(OptionBuilder.withLongOpt("g2")
            .withDescription("Group option 2")
            .create("g2"));
        options.addOptionGroup(group);
        Properties properties = new Properties();
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[0], properties);
        assertNull(cmd.getOption("g"));
    }

    @Test
    public void testParserPropertiesMissingGroupOption() throws Exception {
        Options options = new Options();
        OptionGroup group = new OptionGroup("g", "group");
        group.setDescription("A group.");
        group.addOption(OptionBuilder.withLongOpt("g1")
            .withDescription("Group option 1")
            .create("g1"));
        group.addOption(OptionBuilder.withLongOpt("g2")
            .withDescription("Group option 2")
            .create("g2"));
        options.addOptionGroup(group);
        Properties properties = new Properties();
        properties.setProperty("g1", "group-value1");
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[0], properties);
        Option groupOption = cmd.getOption("g");
        assertSame(group, groupOption);
        Option groupOption1 = cmd.getOption("g1");
        assertNull(groupOption1);
    }
}
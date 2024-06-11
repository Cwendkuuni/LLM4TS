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
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.apache.commons.cli.Values;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() throws Exception {
        Options options = new Options();
        CommandLineParser parser = new DefaultParser();

        Option option = OptionBuilder.withLongOpt("option")
                .withDescription("this is an option")
                .create("");

        Option argOption = OptionBuilder.withLongOpt("arg")
                .withDescription("this is an argument option")
                .withArgName("VALUE")
                .create("");

        Option groupOption = OptionBuilder.withLongOpt("group")
                .withDescription("this is a group option")
                .create("");

        Option groupArgOption = OptionBuilder.withLongOpt("grouparg")
                .withDescription("this is a group argument option")
                .withArgName("VALUE")
                .create("");

        OptionGroup group = new OptionGroup("group", "group options", true);
        group.addOption(groupOption);
        group.addOption(groupArgOption);

        options.addOption(option);
        options.addOption(argOption);
        options.addOptionGroup(group);

        String[] args = { "--option", "value", "--arg", "VALUE" };
        CommandLine cmd = parser.parse(options, args, true);

        assertArrayEquals(new String[] { "value" }, cmd.getOptions().getOptionValues("option"));
        assertSame(argOption, cmd.getOption(argOption.getOpt()));
        assertSame(group, cmd.getOption(group.getOpt()));
        assertTrue(group.isSelected());
        assertNull(cmd.getOption(groupArgOption.getOpt()));

        Option groupSelectedOption = group.getSelectedOption();
        assertSame(groupArgOption, groupSelectedOption);
        assertEquals(Values.parse(new String[] { "VALUE" }), groupSelectedOption.getValue());
    }

    @Test(expected = MissingOptionException.class)
    public void testMissingOption() throws Exception {
        Options options = new Options();
        CommandLineParser parser = new DefaultParser();

        Option option = OptionBuilder.withLongOpt("option")
                .isRequired()
                .create("");

        String[] args = {};
        CommandLine cmd = parser.parse(options, args, false);

        fail("Expected MissingOptionException");
    }

    @Test(expected = MissingArgumentException.class)
    public void testMissingArgument() throws Exception {
        Options options = new Options();
        CommandLineParser parser = new DefaultParser();

        Option option = OptionBuilder.withLongOpt("option")
                .withArgName("VALUE")
                .isRequired()
                .create("");

        String[] args = {};
        CommandLine cmd = parser.parse(options, args, false);

        fail("Expected MissingArgumentException");
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testUnrecognizedOption() throws Exception {
        Options options = new Options();
        CommandLineParser parser = new DefaultParser();

        Option option = OptionBuilder.withLongOpt("option")
                .create("");

        String[] args = { "--unrecognized" };
        CommandLine cmd = parser.parse(options, args, false);

        fail("Expected UnrecognizedOptionException");
    }

    @Test
    public void testParseWithProperties() throws Exception {
        Options options = new Options();
        CommandLineParser parser = new DefaultParser();

        Option option = OptionBuilder.withLongOpt("option")
                .withDescription("this is an option")
                .create("");

        String[] args = {};

        Properties properties = new Properties();
        properties.setProperty("option", "VALUE");

        CommandLine cmd = parser.parse(options, args, properties, true);

        assertArrayEquals(new String[] { "VALUE" }, cmd.getOptions().getOptionValues("option"));
    }

    @Test
    public void testParseWithStopAtNonOption() throws Exception {
        Options options = new Options();
        CommandLineParser parser = new DefaultParser();

        Option option = OptionBuilder.withLongOpt("option")
                .withDescription("this is an option")
                .create("");

        String[] args = { "--option", "--other", "VALUE" };
        CommandLine cmd = parser.parse(options, args, true);

        assertArrayEquals(new String[] { "VALUE" }, cmd.getOptions().getOptionValues("option"));
        assertNull(cmd.getOption("--other"));

        String[] args2 = { "--option", "--other", "VALUE" };
        CommandLine cmd2 = parser.parse(options, args2, false);

        fail("Expected ParseException");
    }
}
package org.apache.commons.cli2.option;

import org.apache.commons.cli2.CommandLineParser;
import org.apache.commons.cli2.DefaultParser;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class OptionImplTest {

    private CommandLineParser parser;
    private HelpFormatter formatter;

    @Before
    public void setUp() {
        parser = new DefaultParser();
        formatter = new HelpFormatter();
    }

    @Test
    public void testSimpleOption() {
        final Option option = OptionBuilder.withLongOpt("test")
                .withDescription("Test option")
                .isRequired(false)
                .create(new OptionImpl(1, false));

        final WriteableCommandLine cmdLine = new WriteableCommandLine("TestApp");

        try {
            parser.parse(new String[]{"-t"}, cmdLine);
            Assert.assertEquals("-t", cmdLine.getOptionValues("test")[0]);
            Assert.assertFalse(cmdLine.hasOption("help"));
        } catch (Exception e) {
            Assert.fail("Unexpected error: " + e.getMessage());
        }

        final String usage = formatter.printHelp("TestApp", Arrays.asList(option), true);
        System.out.println(usage);
    }

    @Test
    public void testRequiredOption() {
        final Option option = OptionBuilder.withLongOpt("test")
                .withDescription("Test option")
                .isRequired(true)
                .create(new OptionImpl(1, true));

        try {
            parser.parse(new String[]{"--test", "value"}, new WriteableCommandLine("TestApp"));
            Assert.assertEquals("value", cmdLine.getOptionValues("test")[0]);
        } catch (Exception e) {
            if (!e.getMessage().contains("required")) {
                Assert.fail("Unexpected error: " + e.getMessage());
            }
        }
    }

    @Test
    public void testOptionWithPrefixes() {
        final Option option = OptionBuilder.withLongOpt("test")
                .withDescription("Test option")
                .withPrefixes(new String[]{"t"})
                .isRequired(false)
                .create(new OptionImpl(1, false));

        try {
            parser.parse(new String[]{"-tvalue"}, new WriteableCommandLine("TestApp"));
            Assert.assertEquals("value", cmdLine.getOptionValues("test")[0]);
            Assert.assertFalse(cmdLine.hasOption("help"));
        } catch (Exception e) {
            Assert.fail("Unexpected error: " + e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionMissingPrefix() {
        final Option option = OptionBuilder.withLongOpt("test")
                .withDescription("Test option")
                .withPrefixes(new String[]{"t"})
                .isRequired(false)
                .create(new OptionImpl(1, false));

        try {
            parser.parse(new String[]{"-testvalue"}, new WriteableCommandLine("TestApp"));
        } catch (Exception e) {
            throw e;
        }

        final String usage = formatter.printHelp("TestApp", Arrays.asList(option), true);
        System.out.println(usage);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingOption() {
        final Option option = OptionBuilder.withLongOpt("test")
                .withDescription("Test option")
                .isRequired(true)
                .create(new OptionImpl(1, true));

        try {
            parser.parse(new String[]{}, new WriteableCommandLine("TestApp"));
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidOption() {
        final Option option = OptionBuilder.withLongOpt("test")
                .withDescription("Test option")
                .create(new OptionImpl(1, false));

        try {
            parser.parse(new String[]{"--invalid"}, new WriteableCommandLine("TestApp"));
        } catch (Exception e) {
            throw e;
        }
    }
}
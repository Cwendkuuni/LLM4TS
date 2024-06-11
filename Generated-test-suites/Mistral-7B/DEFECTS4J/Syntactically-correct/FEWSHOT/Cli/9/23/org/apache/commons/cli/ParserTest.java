package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.junit.Test;

public class ParserTest {

    @Test
    public void testParseEmpty() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        String[] arguments = new String[0];
        CommandLine commandLine = parser.parse(options, arguments);
        assertNull(commandLine.getArgs());
        assertFalse(commandLine.hasOptions());
    }

    @Test
    public void testParseWithOptions() throws ParseException {
        String optionString = "h,help,hhelp,helps,header,header=true";
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("help")
                .withShortOpt('h')
                .isRequired(false)
                .hasArg(false)
                .build());
        options.addOption(OptionBuilder.withLongOpt("header")
                .withShortOpt("h")
                .isRequired(false)
                .hasArg(true)
                .build());
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        String[] arguments = new String[] {"-h", "--help", "--header", "--header=true"};
        CommandLine commandLine = parser.parse(options, arguments);
        List<Option> optionsList = commandLine.getOptions();
        assertArrayEquals(new String[] {"help"}, optionsList.get(0).getLongOpt());
        assertArrayEquals(new String[] {"h"}, optionsList.get(0).getShortOpt());
        assertSame(Boolean.TRUE, optionsList.get(1).getValue());
        assertNull(optionsList.get(2).getValue());
    }

    @Test
    public void testParseWithRequiredOptions() throws ParseException {
        String optionString = "h,help,hhelp,helps,header,header=true";
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("help")
                .withShortOpt('h')
                .isRequired(true)
                .hasArg(false)
                .build());
        options.addOption(OptionBuilder.withLongOpt("header")
                .withShortOpt("h")
                .isRequired(false)
                .hasArg(true)
                .build());
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        String[] arguments = new String[] {"--header=value"};
        try {
            parser.parse(options, arguments);
            fail("Expected MissingOptionException");
        } catch (MissingOptionException e) {
            // expected
        }
        String[] requiredOptions = options.getRequiredOptions().toArray(new String[0]);
        assertArrayEquals(requiredOptions, new String[] {"help"});
    }

    @Test
    public void testParseWithOptionalAndRequiredOptions() throws ParseException {
        String optionString = "h,help,hhelp,helps,header,header=true";
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("help")
                .withShortOpt('h')
                .isRequired(true)
                .hasArg(false)
                .build());
        options.addOption(OptionBuilder.withLongOpt("header")
                .withShortOpt("h")
                .isRequired(false)
                .hasArg(true)
                .build());
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        String[] arguments = new String[] {"--header=value", "help"};
        CommandLine commandLine = parser.parse(options, arguments);
        List<Option> optionsList = commandLine.getOptions();
        assertArrayEquals(new String[] {"help"}, optionsList.get(0).getLongOpt());
        assertArrayEquals(new String[] {"h"}, optionsList.get(0).getShortOpt());
        assertNull(optionsList.get(1).getValue());
        assertSame(Boolean.TRUE, optionsList.get(2).getValue());
    }

    @Test
    public void testParseWithArg() throws ParseException {
        String optionString = "h,help,hhelp,helps,arg,arg=value";
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("arg")
                .withShortOpt("a")
                .isRequired(false)
                .hasArg(true)
                .build());
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        String[] arguments = new String[] {"-a", "value"};
        CommandLine commandLine = parser.parse(options, arguments);
        List<Option> optionsList = commandLine.getOptions();
        assertArrayEquals(new String[] {"arg"}, optionsList.get(0).getLongOpt());
        assertArrayEquals(new String[] {"a"}, optionsList.get(0).getShortOpt());
        assertSame("value", optionsList.get(0).getValue());
        assertArrayEquals(Arrays.asList("value").toArray(), commandLine.getArgs());
    }

    @Test
    public void testParseWithLongArg() throws ParseException {
        String optionString = "h,help,hhelp,helps,arg,arg=value";
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("arg")
                .withShortOpt("a")
                .isRequired(false)
                .hasArg(true)
                .build());
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        String[] arguments = new String[] {"-a", "very long value"};
        CommandLine commandLine = parser.parse(options, arguments);
        List<Option> optionsList = commandLine.getOptions();
        assertArrayEquals(new String[] {"arg"}, optionsList.get(0).getLongOpt());
        assertArrayEquals(new String[] {"a"}, optionsList.get(0).getShortOpt());
        assertSame("very long value", optionsList.get(0).getValue());
        assertArrayEquals(Arrays.asList("very long value").toArray(), commandLine.getArgs());
    }

    @Test
    public void testParseWithLongOption() throws ParseException {
        String optionString = "h,help,hhelp,helps,longopt,longopt=value";
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("longopt")
                .withShortOpt("l")
                .isRequired(false)
                .hasArg(true)
                .build());
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        String[] arguments = new String[] {"--longopt=value"};
        CommandLine commandLine = parser.parse(options, arguments);
        List<Option> optionsList = commandLine.getOptions();
        assertArrayEquals(new String[] {"longopt"}, optionsList.get(0).getLongOpt());
        assertArrayEquals(new String[] {"l"}, optionsList.get(0).getShortOpt());
        assertSame("value", optionsList.get(0).getValue());
        assertArrayEquals(Collections.emptyList().toArray(), commandLine.getArgs());
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        String optionString = "h,help,hhelp,helps,arg,arg=value";
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("arg")
                .withShortOpt("a")
                .isRequired(false)
                .hasArg(true)
                .build());
        Properties properties = new Properties();
        properties.setProperty("arg", "value");
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine commandLine = parser.parse(options, null, properties);
        List<Option> optionsList = commandLine.getOptions();
        assertArrayEquals(new String[] {"arg"}, optionsList.get(0).getLongOpt());
        assertArrayEquals(new String[] {"a"}, optionsList.get(0).getShortOpt());
        assertSame("value", optionsList.get(0).getValue());
        assertArrayEquals(Arrays.asList("value").toArray(), commandLine.getArgs());
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseWithMissingArgument() throws ParseException {
        String optionString = "h,help,hhelp,helps,arg,arg=value";
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("arg")
                .withShortOpt("a")
                .isRequired(true)
                .hasArg(true)
                .build());
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        String[] arguments = new String[] {"-h"};
        parser.parse(options, arguments);
        fail("Expected MissingArgumentException");
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseWithUnrecognizedOption() throws ParseException {
        String optionString = "h,help,hhelp,helps,arg,arg=value";
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("arg")
                .withShortOpt("a")
                .isRequired(false)
                .hasArg(true)
                .build());
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        String[] arguments = new String[] {"-x"};
        parser.parse(options, arguments);
        fail("Expected UnrecognizedOptionException");
    }
}
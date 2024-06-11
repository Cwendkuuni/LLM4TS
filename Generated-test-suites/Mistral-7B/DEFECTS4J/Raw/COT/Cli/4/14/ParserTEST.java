package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class ParserTest {

    private Parser parser;
    private Options options;

    @Before
    public void setUp() {
        parser = new DefaultParser();
        options = new Options();
    }

    

    @Test
    public void testParse_ValidArguments() throws ParseException {
        String[] arguments = {"-a", "valueA", "--optionB", "valueB"};
        CommandLine commandLine = parser.parse(options, arguments);

        Option aOption = options.getOption("a");
        Option bOption = options.getOption("optionB");

        assertEquals("valueA", aOption.getValue()[0]);
        assertEquals("valueB", bOption.getValue()[0]);
        assertEquals(2, commandLine.getArgs().size());
    }

    @Test
    public void testParse_EmptyArguments() throws ParseException {
        String[] arguments = {};
        CommandLine commandLine = parser.parse(options, arguments);
        assertTrue(commandLine.getArgs().isEmpty());
    }

    @Test(expected = ParseException.class)
    public void testParse_InvalidOption() throws ParseException {
        String[] arguments = {"--invalidOption", "value"};
        parser.parse(options, arguments);
        fail("ParseException should be thrown");
    }

    @Test
    public void testParse_MissingOptionValue() throws ParseException {
        String[] arguments = {"-a"};
        parser.parse(options, arguments);
        assertNull(options.getOption("a").getValue());
    }

    @Test
    public void testParse_MultipleOccurrencesOfTheSameOption() throws ParseException {
        String[] arguments = {"-a", "valueA", "-a", "valueB"};
        CommandLine commandLine = parser.parse(options, arguments);

        Option aOption = options.getOption("a");
        assertEquals(2, aOption.getValues().length);
        assertEquals("valueA", aOption.getValue()[0]);
        assertEquals("valueB", aOption.getValue()[1]);
    }

    @Test
    public void testParse_OptionWithValueAndWithoutValue() throws ParseException {
        String[] arguments = {"--optionB", "valueB", "-a"};
        CommandLine commandLine = parser.parse(options, arguments);

        Option optionB = options.getOption("optionB");
        Option aOption = options.getOption("a");

        assertEquals("valueB", optionB.getValue()[0]);
        assertNull(aOption.getValue());
    }

    @Test
    public void testParse_OptionsWithLongOpt() throws ParseException {
        options.addOption("longOptionA", true, "longOptA");
        String[] arguments = {"--longOptA"};
        CommandLine commandLine = parser.parse(options, arguments);
        assertTrue(commandLine.hasOption("longOptionA"));
    }

    @Test
    public void testParse_OptionsWithShortOpt() throws ParseException {
        options.addOption("a", true, "a");
        String[] arguments = {"-a"};
        CommandLine commandLine = parser.parse(options, arguments);
        assertTrue(commandLine.hasOption("a"));
    }

    @Test
    public void testParse_OptionsWithShortOptAndValue() throws ParseException {
        options.addOption("a", true, "a");
        String[] arguments = {"-a", "value"};
        CommandLine commandLine = parser.parse(options, arguments);
        assertEquals("value", options.getOption("a").getValue()[0]);
    }

    @Test
    public void testParse_OptionsWithShortOptAndLongOpt() throws ParseException {
        options.addOption("a", true, "a");
        options.addOption("longOptionA", true, "longOptA");
        String[] arguments = {"--longOptA", "-a"};
        CommandLine commandLine = parser.parse(options, arguments);
        assertTrue(commandLine.hasOption("a"));
        assertTrue(commandLine.hasOption("longOptionA"));
    }

    @Test
    public void testParse_OptionsWithRequiredOption() throws ParseException {
        options.addRequiredOption("requiredOption", true, "requiredOption");
        String[] arguments = {};
        try {
            parser.parse(options, arguments);
            fail("MissingOptionException should be thrown");
        } catch (MissingOptionException e) {
            assertEquals("Missing all required options: requiredOption", e.getMessage());
        }
    }

    @Test
    public void testParse_OptionsWithOptionalOption() throws ParseException {
        options.addOption("optionalOption", false, "optionalOption");
        String[] arguments = {};
        CommandLine commandLine = parser.parse(options, arguments);
        assertFalse(commandLine.hasOption("optionalOption"));

        String[] argumentsWithOption = {"--optionalOption"};
        CommandLine commandLineWithOption = parser.parse(options, argumentsWithOption);
        assertTrue(commandLineWithOption.hasOption("optionalOption"));
    }

    @Test(expected = MissingArgumentException.class)
    public void testParse_MissingArgumentForOption() throws ParseException {
        options.addOption("optionA", true, "optionA");
        options.addOption("optionB", true, "optionB", "valueB");
        String[] arguments = {"-a"};
        parser.parse(options, arguments);
        fail("MissingArgumentException should be thrown");
    }

    @Test
    public void testParse_OptionsWithLongOptionAndValue() throws ParseException {
        options.addOption("longOptionA", true, "longOptA");
        String[] arguments = {"--longOptA", "value"};
        CommandLine commandLine = parser.parse(options, arguments);
        assertEquals("value", options.getOption("longOptionA").getValue()[0]);
    }

    @Test
    public void testParse_OptionsWithShortOptionAndValue() throws ParseException {
        options.addOption("a", true, "a");
        String[] arguments = {"-a", "value"};
        CommandLine commandLine = parser.parse(options, arguments);
        assertEquals("value", options.getOption("a").getValue()[0]);
    }

    @Test
    public void testParse_OptionsWithShortOptionAndLongOption() throws ParseException {
        options.addOption("a", true, "a");
        options.addOption("longOptionA", true, "longOptA");
        String[] arguments = {"--longOptA", "-a"};
        CommandLine commandLine = parser.parse(options, arguments);
        assertTrue(commandLine.hasOption("a"));
        assertTrue(commandLine.hasOption("longOptionA"));
    }

    @Test
    public void testParse_OptionsWithGroup() throws ParseException {
        OptionGroup group = new OptionGroup("group");
        group.setRequired(true);
        options.addOptionGroup(group);
        group.addOption(new Option("optionA", true, "optionA"));
        group.addOption(new Option("optionB", true, "optionB"));

        String[] arguments = {};
        try {
            parser.parse(options, arguments);
            fail("MissingOptionException should be thrown");
        } catch (MissingOptionException e) {
            assertEquals("Missing all required options: group", e.getMessage());
        }

        String[] argumentsWithOptions = {"--optionA", "--optionB"};
        CommandLine commandLine = parser.parse(options, argumentsWithOptions);
        assertTrue(commandLine.hasOption("optionA"));
        assertTrue(commandLine.hasOption("optionB"));
    }

    
}
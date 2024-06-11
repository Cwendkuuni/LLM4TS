package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ParserTest {

    private Parser parser;
    private Options options;

    @Before
    public void setUp() {
        options = new Options();
        parser = new DefaultParser();
    }

    @After
    public void tearDown() {
        parser = null;
        options = null;
    }

    @Test
    public void testParse_EmptyArgs() throws ParseException {
        String[] args = {};
        CommandLine commandLine = parser.parse(options, args, null, false);
        assertEquals(0, commandLine.getArgs().size());
        assertEquals(0, commandLine.getOptions().size());
    }

    @Test
    public void testParse_SingleArg() throws ParseException {
        String[] args = {"arg"};
        options.addOption("option", true, "o");
        CommandLine commandLine = parser.parse(options, args, null, false);
        assertEquals(1, commandLine.getArgs().size());
        assertEquals("arg", commandLine.getArgs().get(0));
        assertEquals(1, commandLine.getOptions().size());
        assertTrue(commandLine.hasOption("option"));
    }

    @Test
    public void testParse_SingleRequiredOption() throws ParseException {
        String[] args = {"arg"};
        options.addOption("option", true, "o").required(true);
        assertThrows(MissingOptionException.class, () -> parser.parse(options, args, null, false));
    }

    @Test
    public void testParse_SingleRequiredOption_Provided() throws ParseException {
        String[] args = {"option", "arg"};
        options.addOption("option", true, "o").required(true);
        CommandLine commandLine = parser.parse(options, args, null, false);
        assertEquals(0, commandLine.getArgs().size());
        assertEquals(1, commandLine.getOptions().size());
        assertTrue(commandLine.hasOption("option"));
        assertEquals("arg", commandLine.getOptionValue("option"));
    }

    @Test
    public void testParse_MultipleArgs() throws ParseException {
        String[] args = {"arg1", "arg2"};
        options.addOption("option", true, "o");
        CommandLine commandLine = parser.parse(options, args, null, false);
        assertEquals(2, commandLine.getArgs().size());
        assertEquals("arg1", commandLine.getArgs().get(0));
        assertEquals("arg2", commandLine.getArgs().get(1));
        assertEquals(1, commandLine.getOptions().size());
        assertTrue(commandLine.hasOption("option"));
    }

    @Test
    public void testParse_MultipleArgs_SingleRequiredOption() throws ParseException {
        String[] args = {"arg1", "arg2"};
        options.addOption("option", true, "o").required(true);
        assertThrows(MissingOptionException.class, () -> parser.parse(options, args, null, false));
    }

    @Test
    public void testParse_MultipleArgs_SingleRequiredOption_Provided() throws ParseException {
        String[] args = {"option", "arg1", "arg2"};
        options.addOption("option", true, "o").required(true);
        CommandLine commandLine = parser.parse(options, args, null, false);
        assertEquals(1, commandLine.getArgs().size());
        assertEquals("arg1", commandLine.getArgs().get(0));
        assertEquals(1, commandLine.getOptions().size());
        assertTrue(commandLine.hasOption("option"));
        assertEquals("arg1", commandLine.getOptionValue("option"));
    }

    @Test
    public void testParse_MultipleArgs_MultipleOptions() throws ParseException {
        String[] args = {"arg1", "arg2"};
        options.addOption("option1", true, "o1");
        options.addOption("option2", true, "o2");
        CommandLine commandLine = parser.parse(options, args, null, false);
        assertEquals(2, commandLine.getArgs().size());
        assertEquals("arg1", commandLine.getArgs().get(0));
        assertEquals("arg2", commandLine.getArgs().get(1));
        assertEquals(2, commandLine.getOptions().size());
        assertTrue(commandLine.hasOption("option1"));
        assertTrue(commandLine.hasOption("option2"));
    }

    @Test
    public void testParse_MultipleArgs_MultipleOptions_SingleValue() throws ParseException {
        String[] args = {"arg1", "arg2"};
        options.addOption("option1", true, "o1").setValue("value1");
        options.addOption("option2", true, "o2");
        CommandLine commandLine = parser.parse(options, args, null, false);
        assertEquals(2, commandLine.getArgs().size());
        assertEquals("arg1", commandLine.getArgs().get(0));
        assertEquals("arg2", commandLine.getArgs().get(1));
        assertEquals(2, commandLine.getOptions().size());
        assertTrue(commandLine.hasOption("option1"));
        assertEquals("value1", commandLine.getOptionValue("option1"));
        assertTrue(commandLine.hasOption("option2"));
        assertNull(commandLine.getOptionValue("option2"));
    }

    @Test
    public void testParse_MultipleArgs_MultipleOptions_MultipleValues() throws ParseException {
        String[] args = {"arg1", "arg2"};
        options.addOption("option1", true, "o1").setValue("value1");
        options.addOption("option1", true, "o1").setValue("value2");
        options.addOption("option2", true, "o2");
        CommandLine commandLine = parser.parse(options, args, null, false);
        assertEquals(2, commandLine.getArgs().size());
        assertEquals("arg1", commandLine.getArgs().get(0));
        assertEquals("arg2", commandLine.getArgs().get(1));
        assertEquals(3, commandLine.getOptions().size());
        assertTrue(commandLine.hasOption("option1"));
        assertEquals(2, commandLine.getValues("option1").length);
        String[] optionValues = commandLine.getValues("option1");
        assertEquals("value1", optionValues[0]);
        assertEquals("value2", optionValues[1]);
        assertTrue(commandLine.hasOption("option2"));
        assertNull(commandLine.getOptionValue("option2"));
    }

    @Test
    public void testParse_MultipleArgs_MultipleOptions_MixedValues() throws ParseException {
        String[] args = {"arg1", "arg2"};
        options.addOption("option1", true, "o1").setValue("value1");
        options.addOption("option1", true, "o1").setValue("value2");
        options.addOption("option2", true, "o2").setValue("value");
        CommandLine commandLine = parser.parse(options, args, null, false);
        assertEquals(2, commandLine.getArgs().size());
        assertEquals("arg1", commandLine.getArgs().get(0));
        assertEquals("arg2", commandLine.getArgs().get(1));
        assertEquals(3, commandLine.getOptions().size());
        assertTrue(commandLine.hasOption("option1"));
        assertEquals(2, commandLine.getValues("option1").length);
        String[] optionValues = commandLine.getValues("option1");
        assertEquals("value1", optionValues[0]);
        assertEquals("value2", optionValues[1]);
        assertTrue(commandLine.hasOption("option2"));
        assertEquals("value", commandLine.getOptionValue("option2"));
    }

    @Test
    public void testParse_MultipleArgs_MultipleOptions_WithGroup() throws ParseException {
        String[] args = {"arg1", "arg2"};
        Options groupOptions = new Options();
        groupOptions.addOption("groupOption", true, "g");
        groupOptions.addOption("groupOptionValue", true, "gv");
        options.addOptionGroup(groupOptions);
        options.addOption("option1", true, "o1");
        CommandLine commandLine = parser.parse(options, args, null, false);
        assertEquals(2, commandLine.getArgs().size());
        assertEquals("arg1", commandLine.getArgs().get(0));
        assertEquals("arg2", commandLine.getArgs().get(1));
        assertEquals(3, commandLine.getOptions().size());
        assertTrue(commandLine.hasOption("groupOption"));
        assertFalse(commandLine.hasOption("groupOptionValue"));
        assertTrue(commandLine.hasOption("option1"));
    }

    @Test
    public void testParse_MultipleArgs_MultipleOptions_WithGroup_MultipleValues() throws ParseException {
        String[] args = {"arg1", "arg2"};
        Options groupOptions = new Options();
        groupOptions.addOption("groupOption", true, "g");
        groupOptions.addOption("groupOptionValue", true, "gv");
        options.addOptionGroup(groupOptions);
        options.addOption("option1", true, "o1").setValue("value1");
        options.addOption("option1", true, "o1").setValue("value2");
        CommandLine commandLine = parser.parse(options, args, null, false);
        assertEquals(2, commandLine.getArgs().size());
        assertEquals("arg1", commandLine.getArgs().get(0));
        assertEquals("arg2", commandLine.getArgs().get(1));
        assertEquals(5, commandLine.getOptions().size());
        assertTrue(commandLine.hasOption("groupOption"));
        assertFalse(commandLine.hasOption("groupOptionValue"));
        assertTrue(commandLine.hasOption("option1"));
        assertEquals(2, commandLine.getValues("option1").length);
        String[] optionValues = commandLine.getValues("option1");
        assertEquals("value1", optionValues[0]);
        assertEquals("value2", optionValues[1]);
    }

    @Test
    public void testParse_MultipleArgs_MultipleOptions_WithGroup_MultipleValues_Mixed() throws ParseException {
        String[] args = {"arg1", "arg2"};
        Options groupOptions = new Options();
        groupOptions.addOption("groupOption", true, "g");
        groupOptions.addOption("groupOptionValue", true, "gv");
        options.addOptionGroup(groupOptions);
        options.addOption("option1", true, "o1").setValue("value1");
        options.addOption("option1", true, "o1").setValue("value2");
        options.addOption("option2", true, "o2").setValue("value");
        CommandLine commandLine = parser.parse(options, args, null, false);
        assertEquals(2, commandLine.getArgs().size());
        assertEquals("arg1", commandLine.getArgs().get(0));
        assertEquals("arg2", commandLine.getArgs().get(1));
        assertEquals(5, commandLine.getOptions().size());
        assertTrue(commandLine.hasOption("groupOption"));
        assertFalse(commandLine.hasOption("groupOptionValue"));
        assertTrue(commandLine.hasOption("option1"));
        assertEquals(2, commandLine.getValues("option1").length);
        String[] optionValues = commandLine.getValues("option1");
        assertEquals("value1", optionValues[0]);
        assertEquals("value2", optionValues[1]);
        assertTrue(commandLine.hasOption("option2"));
        assertEquals("value", commandLine.getOptionValue("option2"));
    }

    @Test
    public void testParse_Properties() throws ParseException {
        String[] args = {};
        Properties properties = new Properties();
        properties.setProperty("option1", "value1");
        properties.setProperty("option2", "value2");
        CommandLine commandLine = parser.parse(options, args, properties, false);
        assertEquals(0, commandLine.getArgs().size());
        assertEquals(2, commandLine.getOptions().size());
        assertTrue(commandLine.hasOption("option1"));
        assertEquals("value1", commandLine.getOptionValue("option1"));
        assertTrue(commandLine.hasOption("option2"));
        assertEquals("value2", commandLine.getOptionValue("option2"));
    }

    @Test
    public void testParse_Properties_MissingOption() throws ParseException {
        String[] args = {};
        Properties properties = new Properties();
        properties.setProperty("missingOption", "value");
        assertThrows(UnrecognizedOptionException.class, () -> parser.parse(options, args, properties, false));
    }

    @Test
    public void testParse_Properties_MixedArgs() throws ParseException {
        String[] args = {"arg1", "arg2"};
        Properties properties = new Properties();
        properties.setProperty("option1", "value1");
        properties.setProperty("option2", "value2");
        CommandLine commandLine = parser.parse(options, args, properties, false);
        assertEquals(2, commandLine.getArgs().size());
        assertEquals("arg1", commandLine.getArgs().get(0));
        assertEquals("arg2", commandLine.getArgs().get(1));
        assertEquals(3, commandLine.getOptions().size());
        assertTrue(commandLine.hasOption("option1"));
        assertEquals("value1", commandLine.getOptionValue("option1"));
        assertTrue(commandLine.hasOption("option2"));
        assertEquals("value2", commandLine.getOptionValue("option2"));
    }

    @Test
    public void testParse_Properties_MixedArgs_MissingOption() throws ParseException {
        String[] args = {"arg1", "arg2"};
        Properties properties = new Properties();
        properties.setProperty("missingOption", "value");
        assertThrows(UnrecognizedOptionException.class, () -> parser.parse(options, args, properties, false));
    }

    @Test
    public void testParse_Properties_MultipleValues() throws ParseException {
        String[] args = {"arg1", "arg2"};
        Properties properties = new Properties();
        properties.setProperty("option1", "value1 value2");
        CommandLine commandLine = parser.parse(options, args, properties, false);
        assertEquals(2, commandLine.getArgs().size());
        assertEquals("arg1", commandLine.getArgs().get(0));
        assertEquals("arg2", commandLine.getArgs().get(1));
        assertEquals(3, commandLine.getOptions().size());
        assertTrue(commandLine.hasOption("option1"));
        assertEquals(2, commandLine.getValues("option1").length);
        String[] optionValues = commandLine.getValues("option1");
        assertEquals("value1", optionValues[0]);
        assertEquals("value2", optionValues[1]);
    }

    @Test
    public void testParse_Properties_MultipleValues_Mixed() throws ParseException {
        String[] args = {"arg1", "arg2"};
        Properties properties = new Properties();
        properties.setProperty("option1", "value1");
        properties.setProperty("option1", "value2");
        CommandLine commandLine = parser.parse(options, args, properties, false);
        assertEquals(2, commandLine.getArgs().size());
        assertEquals("arg1", commandLine.getArgs().get(0));
        assertEquals("arg2", commandLine.getArgs().get(1));
        assertEquals(3, commandLine.getOptions().size());
        assertTrue(commandLine.hasOption("option1"));
        assertEquals(2, commandLine.getValues("option1").length);
        String[] optionValues = commandLine.getValues("option1");
        assertEquals("value1", optionValues[0]);
        assertEquals("value2", optionValues[1]);
    }

    @Test
    public void testPrintHelpFormat() {
        String[] args = {"--help"};
        HelpFormatter formatter = new HelpFormatter();
        CommandLine commandLine = parser.parse(options, args, null, false);
        formatter.printHelp("TestParser", commandLine, true, true, true, false);
    }
}
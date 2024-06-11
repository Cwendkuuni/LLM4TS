
package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class CommandLineTest {

    private CommandLine commandLine;

    @Before
    public void setUp() {
        commandLine = new CommandLine();
    }
    
    @Test
    public void testHasOptionWithValidOption() {
        commandLine.addOption(new Option("f", false));
        assertTrue(commandLine.hasOption("f"));
    }

    @Test
    public void testHasOptionWithInvalidOption() {
        assertFalse(commandLine.hasOption("f"));
    }

    @Test
    public void testHasOptionWithValidCharacterOption() {
        commandLine.addOption(new Option('f', false));
        assertTrue(commandLine.hasOption('f'));
    }

    @Test
    public void testHasOptionWithInvalidCharacterOption() {
        assertFalse(commandLine.hasOption('f'));
    }

    @Test
    public void testGetOptionObjectWithValidOption() {
        Option option = new Option("f", false);
        option.setType(Integer.class);
        commandLine.addOption(option);
        assertNull(commandLine.getOptionObject("f"));
        option.setArgs(1);
        option.addValueForProcessing("123");
        assertEquals(Integer.valueOf(123), commandLine.getOptionObject("f"));
    }

    @Test
    public void testGetOptionObjectWithInvalidOption() {
        assertNull(commandLine.getOptionObject("f"));
    }

    @Test
    public void testGetOptionObjectWithValidCharacterOption() {
        Option option = new Option('f', false);
        option.setType(Integer.class);
        commandLine.addOption(option);
        assertNull(commandLine.getOptionObject('f'));
        option.setArgs(1);
        option.addValueForProcessing("123");
        assertEquals(Integer.valueOf(123), commandLine.getOptionObject('f'));
    }

    @Test
    public void testGetOptionObjectWithInvalidCharacterOption() {
        assertNull(commandLine.getOptionObject('f'));
    }

    @Test
    public void testGetOptionValueWithValidOption() {
        Option option = new Option("f", false);
        option.setArgs(1);
        option.addValueForProcessing("123");
        commandLine.addOption(option);
        assertEquals("123", commandLine.getOptionValue("f"));
    }

    @Test
    public void testGetOptionValueWithInvalidOption() {
        assertNull(commandLine.getOptionValue("f"));
    }

    @Test
    public void testGetOptionValueWithValidCharacterOption() {
        Option option = new Option('f', false);
        option.setArgs(1);
        option.addValueForProcessing("123");
        commandLine.addOption(option);
        assertEquals("123", commandLine.getOptionValue('f'));
    }

    @Test
    public void testGetOptionValueWithInvalidCharacterOption() {
        assertNull(commandLine.getOptionValue('f'));
    }

    @Test
    public void testGetOptionValuesWithValidOption() {
        Option option = new Option("f", false);
        option.setArgs(2);
        option.addValueForProcessing("123");
        option.addValueForProcessing("456");
        commandLine.addOption(option);
        String[] expected = {"123", "456"};
        assertEquals(expected, commandLine.getOptionValues("f"));
    }

    @Test
    public void testGetOptionValuesWithInvalidOption() {
        assertNull(commandLine.getOptionValues("f"));
    }

    @Test
    public void testGetOptionValuesWithValidCharacterOption() {
        Option option = new Option('f', false);
        option.setArgs(2);
        option.addValueForProcessing("123");
        option.addValueForProcessing("456");
        commandLine.addOption(option);
        String[] expected = {"123", "456"};
        assertEquals(expected, commandLine.getOptionValues('f'));
    }

    @Test
    public void testGetOptionValuesWithInvalidCharacterOption() {
        assertNull(commandLine.getOptionValues('f'));
    }

    @Test
    public void testGetOptionValueWithDefaultWithValidOption() {
        Option option = new Option("f", false);
        option.setArgs(1);
        option.addValueForProcessing("123");
        commandLine.addOption(option);
        assertEquals("123", commandLine.getOptionValue("f", "456"));
    }

    @Test
    public void testGetOptionValueWithDefaultWithInvalidOption() {
        assertEquals("456", commandLine.getOptionValue("f", "456"));
    }

    @Test
    public void testGetOptionValueWithDefaultWithValidCharacterOption() {
        Option option = new Option('f', false);
        option.setArgs(1);
        option.addValueForProcessing("123");
        commandLine.addOption(option);
        assertEquals("123", commandLine.getOptionValue('f', "456"));
    }

    @Test
    public void testGetOptionValueWithDefaultWithInvalidCharacterOption() {
        assertEquals("456", commandLine.getOptionValue('f', "456"));
    }

    @Test
    public void testGetArgs() {
        commandLine.addArg("arg1");
        commandLine.addArg("arg2");
        String[] expected = {"arg1", "arg2"};
        assertEquals(expected, commandLine.getArgs());
    }

    @Test
    public void testGetArgList() {
        commandLine.addArg("arg1");
        commandLine.addArg("arg2");
        List<String> expected = new ArrayList<>();
        expected.add("arg1");
        expected.add("arg2");
        assertEquals(expected, commandLine.getArgList());
    }

    @Test
    public void testIterator() {
        Option option1 = new Option("f", false);
        Option option2 = new Option("l", false);
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        Iterator<Option> iterator = commandLine.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(option1, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(option2, iterator.next());
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testGetOptions() {
        Option option1 = new Option("f", false);
        Option option2 = new Option("l", false);
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        Option[] expected = {option1, option2};
        assertEquals(expected, commandLine.getOptions());
    }

}

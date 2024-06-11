package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
    public void testHasOption() {
        assertFalse(commandLine.hasOption("a"));
        assertFalse(commandLine.hasOption('b'));

        Option option1 = new Option("a", false, "");
        Option option2 = new Option("b", false, "");

        commandLine.addOption(option1);
        assertTrue(commandLine.hasOption("a"));

        commandLine.addOption(option2);
        assertTrue(commandLine.hasOption('b'));
    }

    @Test
    public void testGetOptionObject() {
        Option option1 = new Option("a", false, "");
        option1.setType(Integer.class);
        option1.setValue("10");
        Option option2 = new Option("b", false, "");
        option2.setType(Double.class);
        option2.setValue("3.14");

        commandLine.addOption(option1);
        commandLine.addOption(option2);

        assertEquals(10, commandLine.getOptionObject("a"));
        assertEquals(3.14, commandLine.getOptionObject('b'));
    }

    @Test
    public void testGetOptionValue() {
        assertNull(commandLine.getOptionValue("a"));
        assertNull(commandLine.getOptionValue('b'));

        Option option1 = new Option("a", true, "value1");
        option1.setValue("value1");

        commandLine.addOption(option1);
        assertEquals("value1", commandLine.getOptionValue("a"));

        Option option2 = new Option("b", true, "value2");
        option2.setValue("value2");

        commandLine.addOption(option2);
        assertEquals("value2", commandLine.getOptionValue('b'));
    }

    @Test
    public void testGetOptionValues() {
        assertNull(commandLine.getOptionValues("a"));
        assertNull(commandLine.getOptionValues('b'));

        Option option1 = new Option("a", true, "value1");
        option1.setValues(new String[] {"value1", "value2"});

        commandLine.addOption(option1);
        assertEquals("value1", commandLine.getOptionValues("a")[0]);
        assertEquals("value2", commandLine.getOptionValues("a")[1]);

        Option option2 = new Option("b", true, "value2");
        option2.setValues(new String[] {"value3", "value4"});

        commandLine.addOption(option2);
        assertEquals("value3", commandLine.getOptionValues('b')[0]);
        assertEquals("value4", commandLine.getOptionValues('b')[1]);
    }

    @Test
    public void testGetOptionValueWithDefault() {
        assertEquals("default", commandLine.getOptionValue("a", "default"));
        assertEquals("default", commandLine.getOptionValue('b', "default"));

        Option option1 = new Option("a", true, "value1");
        option1.setValue("value1");

        commandLine.addOption(option1);
        assertEquals("value1", commandLine.getOptionValue("a", "default"));

        Option option2 = new Option("b", true, "value2");
        option2.setValue("value2");

        commandLine.addOption(option2);
        assertEquals("value2", commandLine.getOptionValue('b', "default"));
    }

    @Test
    public void testGetArgs() {
        assertEquals(0, commandLine.getArgs().length);

        commandLine.addArg("arg1");
        commandLine.addArg("arg2");

        assertEquals("arg1", commandLine.getArgs()[0]);
        assertEquals("arg2", commandLine.getArgs()[1]);
    }

    @Test
    public void testGetArgList() {
        assertEquals(0, commandLine.getArgList().size());

        commandLine.addArg("arg1");
        commandLine.addArg("arg2");

        assertEquals("arg1", commandLine.getArgList().get(0));
        assertEquals("arg2", commandLine.getArgList().get(1));
    }

    @Test
    public void testIterator() {
        Option option1 = new Option("a", false, "");
        Option option2 = new Option("b", false, "");

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
        assertEquals(0, commandLine.getOptions().length);

        Option option1 = new Option("a", false, "");
        Option option2 = new Option("b", false, "");

        commandLine.addOption(option1);
        commandLine.addOption(option2);

        Option[] options = commandLine.getOptions();
        assertEquals(2, options.length);
        assertEquals(option1, options[0]);
        assertEquals(option2, options[1]);
    }
}
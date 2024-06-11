package org.apache.commons.cli2.commandline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class WriteableCommandLineImplTest {
    @Test
    public void testAddOption() {
        Option option1 = new Option("option1", "Option 1");
        Option option2 = new Option("option2", "Option 2");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList<>());

        commandLine.addOption(option1);
        commandLine.addOption(option2);

        List<Option> options = commandLine.getOptions();

        assertEquals(2, options.size());
        assertEquals(option1, options.get(0));
        assertEquals(option2, options.get(1));
    }

    @Test
    public void testAddValue() {
        Argument argument1 = new Argument("arg1", "Argument 1");
        Argument argument2 = new Argument("arg2", "Argument 2");
        
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(argument1, new ArrayList<>());
        
        commandLine.addValue(argument1, "value1");
        commandLine.addValue(argument2, "value2");

        List<String> values1 = commandLine.getValues(argument1, new ArrayList<>());
        List<String> values2 = commandLine.getValues(argument2, new ArrayList<>());

        assertEquals(1, values1.size());
        assertEquals("value1", values1.get(0));

        assertEquals(1, values2.size());
        assertEquals("value2", values2.get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option1 = new Option("option1", "Option 1");
        Option option2 = new Option("option2", "Option 2");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList<>());

        commandLine.addSwitch(option1, true);
        commandLine.addSwitch(option2, false);

        Boolean switch1 = commandLine.getSwitch(option1, null);
        Boolean switch2 = commandLine.getSwitch(option2, null);

        assertEquals(true, switch1);
        assertEquals(false, switch2);
    }

    @Test
    public void testHasOption() {
        Option option1 = new Option("option1", "Option 1");
        Option option2 = new Option("option2", "Option 2");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList<>());

        commandLine.addOption(option1);

        assertEquals(true, commandLine.hasOption(option1));
        assertEquals(false, commandLine.hasOption(option2));
    }

    @Test
    public void testGetOption() {
        Option option1 = new Option("option1", "Option 1");
        Option option2 = new Option("option2", "Option 2");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList<>());

        commandLine.addOption(option1);
        commandLine.addOption(option2);

        Option retrievedOption1 = commandLine.getOption("option1");
        Option retrievedOption2 = commandLine.getOption("option2");
        Option retrievedOption3 = commandLine.getOption("option3");

        assertEquals(option1, retrievedOption1);
        assertEquals(option2, retrievedOption2);
        assertNull(retrievedOption3);
    }

    @Test
    public void testGetValues() {
        Argument argument1 = new Argument("arg1", "Argument 1");
        Argument argument2 = new Argument("arg2", "Argument 2");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(argument1, new ArrayList<>());

        commandLine.addValue(argument1, "value1");
        commandLine.addValue(argument1, "value2");
        commandLine.addValue(argument2, "value3");

        List<String> values1 = commandLine.getValues(argument1, new ArrayList<>());
        List<String> values2 = commandLine.getValues(argument2, new ArrayList<>());

        assertEquals(2, values1.size());
        assertEquals("value1", values1.get(0));
        assertEquals("value2", values1.get(1));

        assertEquals(1, values2.size());
        assertEquals("value3", values2.get(0));
    }

    @Test
    public void testGetSwitch() {
        Option option1 = new Option("option1", "Option 1");
        Option option2 = new Option("option2", "Option 2");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList<>());

        commandLine.addSwitch(option1, true);
        commandLine.addSwitch(option2, false);

        Boolean switch1 = commandLine.getSwitch(option1, false);
        Boolean switch2 = commandLine.getSwitch(option2, true);
        Boolean switch3 = commandLine.getSwitch(option2, null);

        assertEquals(true, switch1);
        assertEquals(false, switch2);
        assertNull(switch3);
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option1", "Option 1"), new ArrayList<>());

        commandLine.addProperty("property1", "value1");
        commandLine.addProperty("property2", "value2");

        String property1 = commandLine.getProperty("property1", null);
        String property2 = commandLine.getProperty("property2", null);
        String property3 = commandLine.getProperty("property3", null);

        assertEquals("value1", property1);
        assertEquals("value2", property2);
        assertNull(property3);
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option1", "Option 1"), new ArrayList<>());

        assertEquals(true, commandLine.looksLikeOption("-option1"));
        assertEquals(false, commandLine.looksLikeOption("value1"));
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option1", "Option 1"), new ArrayList<>());
        
        commandLine.addValue(commandLine.getRootOption(), "value1");
        commandLine.addValue(commandLine.getRootOption(), "value2");

        assertEquals("value1 value2", commandLine.toString());
    }
}
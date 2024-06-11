import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineImplTest {
    private WriteableCommandLineImpl commandLine;

    @Before
    public void setup() {
        Option option1 = new Option("o1", "option1", "Option 1");
        Option option2 = new Option("o2", "option2", "Option 2");

        Argument argument1 = new Argument("arg1", "Argument 1");
        Argument argument2 = new Argument("arg2", "Argument 2");

        List<Option> options = new ArrayList<>();
        options.add(option1);
        options.add(option2);

        List<Argument> arguments = new ArrayList<>();
        arguments.add(argument1);
        arguments.add(argument2);

        commandLine = new WriteableCommandLineImpl(options, arguments);
    }

    @Test
    public void testAddOption() {
        Option option3 = new Option("o3", "option3", "Option 3");
        commandLine.addOption(option3);

        assertTrue(commandLine.getOptions().contains(option3));
    }

    @Test
    public void testAddValue() {
        Option option1 = new Option("o1", "option1", "Option 1");
        commandLine.addValue(option1, "value1");

        assertTrue(commandLine.getValues(option1, new ArrayList<>()).contains("value1"));
    }

    @Test
    public void testAddSwitch() {
        Option option2 = new Option("o2", "option2", "Option 2");
        commandLine.addSwitch(option2, true);

        assertTrue(commandLine.getSwitch(option2, false));
    }

    @Test
    public void testHasOption() {
        Option option1 = new Option("o1", "option1", "Option 1");
        commandLine.addOption(option1);

        assertTrue(commandLine.hasOption(option1));
    }

    @Test
    public void testGetOption() {
        Option option1 = new Option("o1", "option1", "Option 1");
        commandLine.addOption(option1);

        assertEquals(option1, commandLine.getOption("o1"));
    }

    @Test
    public void testGetValues() {
        Option option1 = new Option("o1", "option1", "Option 1");
        commandLine.addOption(option1);
        commandLine.addValue(option1, "value1");

        List<String> expected = new ArrayList<>();
        expected.add("value1");

        assertEquals(expected, commandLine.getValues(option1, new ArrayList<>()));
    }

    @Test
    public void testGetSwitch() {
        Option option2 = new Option("o2", "option2", "Option 2");
        commandLine.addSwitch(option2, true);

        assertTrue(commandLine.getSwitch(option2, false));
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty("key", "value");

        assertEquals("value", commandLine.getProperty("key", ""));
    }

    @Test
    public void testGetProperties() {
        commandLine.addProperty("key", "value");

        assertTrue(commandLine.getProperties().contains("key"));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-o1"));
        assertFalse(commandLine.looksLikeOption("value"));
    }

    @Test
    public void testToString() {
        Option option1 = new Option("o1", "option1", "Option 1");
        commandLine.addOption(option1);
        commandLine.addValue(option1, "value1");

        Option option2 = new Option("o2", "option2", "Option 2");
        commandLine.addOption(option2);
        commandLine.addSwitch(option2, true);

        assertEquals("-o1 value1 -o2", commandLine.toString());
    }

    @Test
    public void testSetDefaultValues() {
        Option option1 = new Option("o1", "option1", "Option 1");
        List<String> defaults = new ArrayList<>();
        defaults.add("default1");
        
        commandLine.addOption(option1);
        commandLine.setDefaultValues(option1, defaults);

        assertEquals(defaults, commandLine.getValues(option1, new ArrayList<>()));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option2 = new Option("o2", "option2", "Option 2");
        commandLine.addOption(option2);
        commandLine.setDefaultSwitch(option2, true);

        assertTrue(commandLine.getSwitch(option2, false));
    }

    @Test
    public void testGetNormalised() {
        Argument argument1 = new Argument("arg1", "Argument 1");
        commandLine.addValue(argument1, "value1");

        Argument argument2 = new Argument("arg2", "Argument 2");
        commandLine.addValue(argument2, "value2");

        List<String> expected = new ArrayList<>();
        expected.add("value1");
        expected.add("value2");

        assertEquals(expected, commandLine.getNormalised());
    }
}
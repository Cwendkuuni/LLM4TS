import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.option.PropertyOption;
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineImplTest {
    private Option rootOption;
    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        rootOption = new Option("root");
        commandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());
    }

    @Test
    public void testAddOption() {
        Option option = new Option("test");
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
        assertEquals(option, commandLine.getOption("test"));
    }

    @Test
    public void testAddValue() {
        Argument argument = new Argument();
        commandLine.addValue(argument, "value1");
        assertTrue(commandLine.hasOption(argument));
        assertEquals("value1", commandLine.getValues(argument, null).get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("test");
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.hasOption(option));
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testHasOption() {
        Option option = new Option("test");
        assertFalse(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("test");
        assertNull(commandLine.getOption("test"));
    }

    @Test
    public void testGetValues() {
        Argument argument = new Argument();
        commandLine.addValue(argument, "value1");
        List values = new ArrayList();
        values.add("default1");
        values.add("default2");
        assertEquals(commandLine.getValues(argument, values), values);
    }

    @Test
    public void testGetUndefaultedValues() {
        Argument argument = new Argument();
        commandLine.addValue(argument, "value1");
        assertEquals(commandLine.getUndefaultedValues(argument).get(0), "value1");
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("test");
        assertTrue(commandLine.getSwitch(option, true));
    }

    @Test
    public void testGetProperty() {
        assertEquals(commandLine.getProperty("property"), commandLine.getProperty(new PropertyOption(), "property", "default"));
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty("property", "value");
        assertTrue(commandLine.getProperties().contains("property"));
        assertEquals(commandLine.getProperty("property"), "value");
    }

    @Test
    public void testGetProperties() {
        assertTrue(commandLine.getProperties().isEmpty());
    }

    @Test
    public void testLooksLikeOption() {
        assertFalse(commandLine.looksLikeOption("value"));
    }

    @Test
    public void testToString() {
        assertEquals(commandLine.toString(), "");
    }

    @Test
    public void testGetOptions() {
        assertTrue(commandLine.getOptions().isEmpty());
    }

    @Test
    public void testGetOptionTriggers() {
        assertTrue(commandLine.getOptionTriggers().isEmpty());
    }

    @Test
    public void testSetDefaultValues() {
        Argument argument = new Argument();
        List defaults = new ArrayList();
        defaults.add("default1");
        commandLine.setDefaultValues(argument, defaults);
        assertEquals(commandLine.getValues(argument, null), defaults);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("test");
        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testGetNormalised() {
        assertTrue(commandLine.getNormalised().isEmpty());
    }
}
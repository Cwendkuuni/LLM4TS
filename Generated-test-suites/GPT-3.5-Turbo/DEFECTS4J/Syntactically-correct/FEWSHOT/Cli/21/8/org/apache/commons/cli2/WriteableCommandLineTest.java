import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option1 = new Option("a");
        Option option2 = new Option("b");
        
        assertFalse(commandLine.hasOption(option1));
        
        commandLine.addOption(option1);
        
        assertTrue(commandLine.hasOption(option1));
        assertFalse(commandLine.hasOption(option2));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("a");
        
        assertFalse(commandLine.hasValue(option, "value"));
        
        commandLine.addValue(option, "value");
        
        assertTrue(commandLine.hasValue(option, "value"));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("a");
        
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        
        List<String> values = commandLine.getUndefaultedValues(option);
        
        assertEquals(2, values.size());
        assertTrue(values.contains("value1"));
        assertTrue(values.contains("value2"));
        assertFalse(values.contains("value3"));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("a");
        
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        defaultValues.add("default2");
        
        assertTrue(commandLine.getUndefaultedValues(option).isEmpty());
        
        commandLine.setDefaultValues(option, defaultValues);
        
        List<String> values = commandLine.getUndefaultedValues(option);
        
        assertEquals(2, values.size());
        assertTrue(values.contains("default1"));
        assertTrue(values.contains("default2"));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("a");
        
        assertFalse(commandLine.hasSwitch(option));
        
        commandLine.addSwitch(option, true);
        
        assertTrue(commandLine.hasSwitch(option));
        assertTrue(commandLine.getSwitchValue(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitch_alreadyAdded() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("a");
        
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("a");
        
        assertNull(commandLine.getDefaultSwitch(option));
        
        commandLine.setDefaultSwitch(option, true);
        
        assertTrue(commandLine.getDefaultSwitch(option));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("a");
        
        assertFalse(commandLine.hasProperty(option, "property"));
        
        commandLine.addProperty(option, "property", "value");
        
        assertTrue(commandLine.hasProperty(option, "property"));
        assertEquals("value", commandLine.getProperty(option, "property"));
    }

    @Test
    public void testAddProperty_defaultPropertySet() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        
        assertFalse(commandLine.hasProperty("property"));
        
        commandLine.addProperty("property", "value");
        
        assertTrue(commandLine.hasProperty("property"));
        assertEquals("value", commandLine.getProperty("property"));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        
        assertTrue(commandLine.looksLikeOption("-option"));
        assertFalse(commandLine.looksLikeOption("value"));
        assertFalse(commandLine.looksLikeOption("--long-option"));
    }
}
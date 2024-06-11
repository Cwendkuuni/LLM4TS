import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.Option;
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineTest {
    
    private WriteableCommandLine commandLine;
    
    @Before
    public void setUp() {
        commandLine = new WriteableCommandLineImpl();
    }
    
    @Test
    public void testAddOption() {
        Option option = new Option("a", "alpha", true, "Option alpha");
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption("a"));
        assertNotNull(commandLine.getOption("a"));
        assertEquals(option, commandLine.getOption("a"));
    }
    
    @Test
    public void testAddValue() {
        Option option = new Option("b", "beta", true, "Option beta");
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        assertEquals(2, commandLine.getValues(option).size());
        assertTrue(commandLine.getValues(option).contains("value1"));
        assertTrue(commandLine.getValues(option).contains("value2"));
    }
    
    @Test
    public void testGetValues() {
        Option option = new Option("c", "gamma", true, "Option gamma");
        commandLine.addOption(option);
        List<String> values = new ArrayList<>();
        values.add("value1");
        values.add("value2");
        commandLine.setValues(option, values);
        assertEquals(values, commandLine.getValues(option));
    }
    
    @Test
    public void testSetDefaultValues() {
        Option option = new Option("d", "delta", true, "Option delta");
        commandLine.addOption(option);
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("defaultValue1");
        defaultValues.add("defaultValue2");
        commandLine.setDefaultValues(option, defaultValues);
        assertEquals(defaultValues, commandLine.getDefaultValues(option));
    }
    
    @Test
    public void testAddSwitch() {
        Option option = new Option("e", "epsilon", false, "Option epsilon");
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.hasSwitch(option));
        assertTrue(commandLine.getSwitch(option));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testAddSwitch_ThrowsIllegalStateException() {
        Option option = new Option("f", "zeta", false, "Option zeta");
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }
    
    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("g", "eta", false, "Option eta");
        commandLine.addOption(option);
        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.hasDefaultSwitch(option));
        assertTrue(commandLine.getDefaultSwitch(option));
    }
    
    @Test
    public void testAddProperty() {
        commandLine.addProperty("property1", "value1");
        commandLine.addProperty("property2", "value2");
        assertEquals("value1", commandLine.getProperty("property1"));
        assertEquals("value2", commandLine.getProperty("property2"));
    }
    
    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-option1"));
        assertTrue(commandLine.looksLikeOption("--option2"));
        assertFalse(commandLine.looksLikeOption("value1"));
    }
}
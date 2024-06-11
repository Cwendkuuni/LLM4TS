
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class WriteableCommandLineTest {

    private WriteableCommandLine commandLine;
    private Option option;

    @Before
    public void setUp() {
        commandLine = new WriteableCommandLineImpl();
        option = new OptionImpl("testOption");
    }

    // Test addOption method
    @Test
    public void addOption_shouldAddOptionToCommandLine() {
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption("testOption"));
    }

    // Test addValue method
    @Test
    public void addValue_shouldAddValueToOptionInCommandLine() {
        commandLine.addOption(option);
        commandLine.addValue(option, "testValue");
        assertEquals("testValue", commandLine.getValue(option));
    }

    // Test getValues method
    @Test
    public void getValues_shouldReturnListofValuesForOption() {
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        assertEquals(2, commandLine.getValues(option).size());
        assertTrue(commandLine.getValues(option).contains("value1"));
        assertTrue(commandLine.getValues(option).contains("value2"));
    }

    // Test setDefaultValues method
    @Test
    public void setDefaultValues_shouldSetDefaultValuesForOption() {
        commandLine.addOption(option);
        commandLine.setDefaultValues(option, List.of("default1", "default2"));
        assertEquals(List.of("default1", "default2"), commandLine.getValues(option));
    }

    // Test addSwitch method
    @Test
    public void addSwitch_shouldAddSwitchValueToOption() {
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitchValue(option));
    }

    // Test setDefaultSwitch method
    @Test
    public void setDefaultSwitch_shouldSetDefaultSwitchValueForOption() {
        commandLine.addOption(option);
        commandLine.setDefaultSwitch(option, false);
        assertFalse(commandLine.getSwitchValue(option));
    }

    // Test addProperty method
    @Test
    public void addProperty_shouldAddPropertyValueToCommandLine() {
        commandLine.addProperty("propertyKey", "propertyValue");
        assertEquals("propertyValue", commandLine.getProperty("propertyKey"));
    }

    // Test looksLikeOption method
    @Test
    public void looksLikeOption_shouldReturnTrueForOptionTrigger() {
        assertTrue(commandLine.looksLikeOption("-option"));
    }

    @Test
    public void looksLikeOption_shouldReturnFalseForNonOptionTrigger() {
        assertFalse(commandLine.looksLikeOption("value"));
    }
}
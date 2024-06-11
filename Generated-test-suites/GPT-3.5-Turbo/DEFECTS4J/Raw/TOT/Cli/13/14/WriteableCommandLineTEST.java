import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class WriteableCommandLineTest {

    private WriteableCommandLine commandLine;
    private Option option;
    private Object value;
    
    @Before
    public void setup() {
        commandLine = new WriteableCommandLineImpl();
        option = new Option("test", "Test option");
        value = "Test value";
    }
    
    @Test
    public void addOption_addsOptionToCommandLine() {
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption("test"));
    }
    
    @Test
    public void addValue_addsValueToOptionInCommandLine() {
        commandLine.addValue(option, value);
        assertEquals(value, commandLine.getValue("test"));
    }
    
    @Test
    public void retrieveArgumentValues_returnsListOfValuesForOption() {
        List<String> values = new ArrayList<>();
        values.add("Value1");
        values.add("Value2");
        commandLine.addValue(option, "Value1");
        commandLine.addValue(option, "Value2");
        assertEquals(values, commandLine.retrieveArgumentValues(option));
    }
    
    @Test
    public void isOption_getsOptionAssociatedWithValuesInCommandLine() {
        commandLine.addOption(option);
        assertTrue(commandLine.isOption("test"));
    }
    
    @Test
    public void setDefaultValues_setsDefaultValuesForOption() {
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("Default1");
        defaultValues.add("Default2");
        commandLine.setDefaultValues(option, defaultValues);
        assertEquals(defaultValues, commandLine.getDefaultValues("test"));
    }
    
    @Test(expected = IllegalStateException.class)
    public void addSwitch_throwsExceptionIfSwitchAlreadyAdded() {
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }
    
    @Test
    public void addSwitch_addsSwitchValueToOptionInCommandLine() {
        commandLine.addSwitch(option, true);
        assertEquals(true, commandLine.getSwitchValue("test"));
    }
    
    @Test
    public void setDefaultSwitch_setsDefaultSwitchStateForOption() {
        commandLine.setDefaultSwitch(option, true);
        assertEquals(true, commandLine.getDefaultSwitch("test"));
    }
    
    @Test
    public void addProperty_addsPropertyWithValueToCommandLine() {
        String property = "testProperty";
        String propertyValue = "testValue";
        commandLine.addProperty(property, propertyValue);
        assertEquals(propertyValue, commandLine.getProperty(property));
    }
    
    @Test
    public void looksLikeOption_returnsTrueIfArgumentLooksLikeOptionTrigger() {
        String argument = "-t";
        assertTrue(commandLine.looksLikeOption(argument));
    }

    @Test
    public void looksLikeOption_returnsFalseIfArgumentDoesNotLookLikeOptionTrigger() {
        String argument = "test";
        assertFalse(commandLine.looksLikeOption(argument));
    }

}
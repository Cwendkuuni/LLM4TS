import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineTest {

    @Test
    public void addOption_shouldAddOptionToCommandLine() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("name", "description");
        commandLine.addOption(option);
        
        Assert.assertTrue(commandLine.getOptions().contains(option));
    }
    
    @Test
    public void addValue_shouldAddValueToOptionInCommandLine() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("name", "description");
        Object value = "value";
        commandLine.addValue(option, value);
        
        Assert.assertEquals(value, commandLine.getValue(option));
    }
    
    @Test
    public void getValues_shouldReturnListWithSpecifiedOptionValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("name", "description");
        List<Object> values = new ArrayList<>();
        values.add("value1");
        values.add("value2");
        commandLine.addValue(option, values.get(0));
        commandLine.addValue(option, values.get(1));
        
        List<Object> result = commandLine.getValues(option);
        Assert.assertEquals(values, result);
    }
    
    @Test
    public void setDefaultValues_shouldSetDefaultValuesForOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("name", "description");
        List<Object> defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        defaultValues.add("default2");
        commandLine.setDefaultValues(option, defaultValues);
        
        Assert.assertEquals(defaultValues, commandLine.getDefaultValues(option));
    }
    
    @Test(expected = IllegalStateException.class)
    public void addSwitch_shouldThrowIllegalStateExceptionIfSwitchAlreadyAdded() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("name", "description");
        commandLine.addSwitch(option, true);
        
        commandLine.addSwitch(option, false);
    }
    
    @Test
    public void setDefaultSwitch_shouldSetDefaultSwitchStateForOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("name", "description");
        commandLine.setDefaultSwitch(option, true);
        
        Assert.assertTrue(commandLine.getDefaultSwitch(option));
    }
    
    @Test
    public void addProperty_shouldAddPropertyValueToCommandLine() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        String property = "property";
        String value = "value";
        commandLine.addProperty(property, value);
        
        Assert.assertEquals(value, commandLine.getProperty(property));
    }
    
    @Test
    public void looksLikeOption_shouldReturnTrueForOptionTrigger() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        String argument = "-option";
        
        Assert.assertTrue(commandLine.looksLikeOption(argument));
    }
    
    @Test
    public void looksLikeOption_shouldReturnFalseForNonOptionTrigger() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        String argument = "argument";
        
        Assert.assertFalse(commandLine.looksLikeOption(argument));
    }
}
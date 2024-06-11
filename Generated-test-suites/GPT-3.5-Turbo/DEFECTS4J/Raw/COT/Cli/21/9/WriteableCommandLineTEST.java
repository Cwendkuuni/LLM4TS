import org.junit.Test;
import org.junit.Assert;
import java.util.List;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("option");
        commandLine.addOption(option);
        List<Option> options = commandLine.getOptions();
        Assert.assertTrue(options.contains(option));
    }
    
    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("option");
        Object value = "value";
        commandLine.addValue(option, value);
        List<Object> values = commandLine.getValues(option);
        Assert.assertTrue(values.contains(value));
    }
    
    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("option");
        Object value = "value";
        commandLine.addValue(option, value);
        List<Object> undefaultedValues = commandLine.getUndefaultedValues(option);
        Assert.assertTrue(undefaultedValues.contains(value));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("option");
        List<Object> defaultValues = new ArrayList<>();
        defaultValues.add("value1");
        defaultValues.add("value2");
        defaultValues.add("value3");
        commandLine.setDefaultValues(option, defaultValues);
        List<Object> values = commandLine.getValues(option);
        Assert.assertEquals(defaultValues, values);
    }
   
    @Test(expected = IllegalStateException.class)
    public void testAddSwitchThrowsIllegalStateException() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("option");
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("option");
        commandLine.setDefaultSwitch(option, true);
        boolean defaultSwitch = commandLine.getDefaultSwitch(option);
        Assert.assertTrue(defaultSwitch);
    }
    
    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("option");
        String property = "property";
        String value = "value";
        commandLine.addProperty(option, property, value);
        String propertyValue = commandLine.getProperty(option, property);
        Assert.assertEquals(value, propertyValue);
    }
    
    @Test
    public void testAddDefaultProperty() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        String property = "property";
        String value = "value";
        commandLine.addProperty(property, value);
        String propertyValue = commandLine.getDefaultProperty(property);
        Assert.assertEquals(value, propertyValue);
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        String argument = "-option";
        boolean looksLikeOption = commandLine.looksLikeOption(argument);
        Assert.assertTrue(looksLikeOption);
    }

    @Test
    public void testSetCurrentOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("option");
        commandLine.setCurrentOption(option);
        Option currentOption = commandLine.getCurrentOption();
        Assert.assertEquals(option, currentOption);
    }
}
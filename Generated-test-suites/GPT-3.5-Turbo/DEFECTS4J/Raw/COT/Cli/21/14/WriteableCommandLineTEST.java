
import org.junit.Test;
import org.junit.Assert;
import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineTest {
    
    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("test");
        commandLine.addOption(option);
        Assert.assertTrue(commandLine.getOptions().contains(option));
    }
    
    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("test");
        commandLine.addValue(option, "value");
        List<String> values = commandLine.getUndefaultedValues(option);
        Assert.assertTrue(values.contains("value"));
    }
    
    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("test");
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List<String> values = commandLine.getUndefaultedValues(option);
        Assert.assertTrue(values.contains("value1"));
        Assert.assertTrue(values.contains("value2"));
        Assert.assertFalse(values.contains("default"));
    }
    
    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("test");
        List<String> defaultValues = new ArrayList<String>();
        defaultValues.add("default1");
        defaultValues.add("default2");
        commandLine.setDefaultValues(option, defaultValues);
        List<String> values = commandLine.getUndefaultedValues(option);
        Assert.assertTrue(values.contains("default1"));
        Assert.assertTrue(values.contains("default2"));
    }
    
    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("test");
        commandLine.addSwitch(option, true);
        Boolean switchValue = commandLine.getOptionSwitch(option);
        Assert.assertTrue(switchValue);
    }
    
    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("test");
        commandLine.setDefaultSwitch(option, false);
        Boolean switchValue = commandLine.getOptionSwitch(option);
        Assert.assertFalse(switchValue);
    }
    
    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("test");
        commandLine.addProperty(option, "property", "value");
        String propertyValue = commandLine.getPropertyValue(option, "property");
        Assert.assertEquals("value", propertyValue);
    }
    
    @Test
    public void testAddPropertyToDefaultSet() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        commandLine.addProperty("property", "value");
        String propertyValue = commandLine.getPropertyValue("property");
        Assert.assertEquals("value", propertyValue);
    }
    
    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        String argument = "-option";
        boolean result = commandLine.looksLikeOption(argument);
        Assert.assertTrue(result);
    }
    
    @Test
    public void testSetGetCurrentOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("test");
        commandLine.setCurrentOption(option);
        Option currentOption = commandLine.getCurrentOption();
        Assert.assertEquals(option, currentOption);
    }
}

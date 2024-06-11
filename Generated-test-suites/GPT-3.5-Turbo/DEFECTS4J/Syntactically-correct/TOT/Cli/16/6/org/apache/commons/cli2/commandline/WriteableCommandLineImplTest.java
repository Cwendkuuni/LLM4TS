import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Set;

public class WriteableCommandLineImplTest {
    
    private WriteableCommandLineImpl commandLine;
    private Option option1;
    private Option option2;
    private Option option3;
    
    @Before
    public void setup() {
        option1 = new OptionBuilder().withPreferredName("option1").create();
        option2 = new OptionBuilder().withPreferredName("option2").create();
        option3 = new OptionBuilder().withPreferredName("option3").create();
        
        List<String> arguments = Arrays.asList("arg1", "arg2");
        
        commandLine = new WriteableCommandLineImpl(option1, arguments);
    }
    
    @Test
    public void testAddOption() {
        commandLine.addOption(option2);
        Assert.assertTrue(commandLine.hasOption(option2));
    }
    
    @Test
    public void testAddValue() {
        commandLine.addValue(option1, "value1");
        List<String> values = commandLine.getValues(option1, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));
    }
    
    @Test
    public void testAddSwitch() {
        commandLine.addSwitch(option1, true);
        Boolean switchValue = commandLine.getSwitch(option1, null);
        Assert.assertEquals(Boolean.TRUE, switchValue);
    }
    
    @Test
    public void testHasOption() {
        Assert.assertTrue(commandLine.hasOption(option1));
        Assert.assertFalse(commandLine.hasOption(option2));
    }
    
    @Test
    public void testGetOption() {
        Option result = commandLine.getOption("option1");
        Assert.assertEquals(option1, result);
    }
    
    @Test
    public void testGetValues() {
        commandLine.addValue(option1, "value1");
        commandLine.addValue(option1, "value2");
        List<String> values = commandLine.getValues(option1, null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }
    
    @Test
    public void testGetUndefaultedValues() {
        commandLine.addValue(option1, "value1");
        commandLine.addValue(option1, "value2");
        List<String> values = commandLine.getUndefaultedValues(option1);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }
    
    @Test
    public void testGetSwitch() {
        commandLine.addSwitch(option1, true);
        Boolean switchValue = commandLine.getSwitch(option1, null);
        Assert.assertEquals(Boolean.TRUE, switchValue);
    }
    
    @Test
    public void testGetProperty() {
        commandLine.addProperty("property1", "value1");
        String propertyValue = commandLine.getProperty("property1");
        Assert.assertEquals("value1", propertyValue);
    }
    
    @Test
    public void testAddProperty() {
        commandLine.addProperty(option1, "property1", "value1");
        Set<String> properties = commandLine.getProperties(option1);
        Assert.assertEquals(1, properties.size());
        Assert.assertTrue(properties.contains("property1"));
    }
    
    @Test
    public void testGetPropertyWithDefaultValue() {
        String propertyValue = commandLine.getProperty(option1, "property1", "defaultValue");
        Assert.assertEquals("defaultValue", propertyValue);
    }
    
    @Test
    public void testGetProperties() {
        commandLine.addProperty("property1", "value1");
        commandLine.addProperty("property2", "value2");
        Set<String> properties = commandLine.getProperties();
        Assert.assertEquals(2, properties.size());
        Assert.assertTrue(properties.contains("property1"));
        Assert.assertTrue(properties.contains("property2"));
    }
    
    @Test
    public void testLooksLikeOption() {
        boolean result = commandLine.looksLikeOption("-option");
        Assert.assertTrue(result);
        result = commandLine.looksLikeOption("arg1");
        Assert.assertFalse(result);
    }
    
    @Test
    public void testToString() {
        String result = commandLine.toString();
        Assert.assertEquals("arg1 arg2", result);
    }
    
    @Test
    public void testGetOptions() {
        List<Option> options = commandLine.getOptions();
        Assert.assertEquals(1, options.size());
        Assert.assertEquals(option1, options.get(0));
    }
    
    @Test
    public void testGetOptionTriggers() {
        Set<String> triggers = commandLine.getOptionTriggers();
        Assert.assertEquals(1, triggers.size());
        Assert.assertTrue(triggers.contains("option1"));
    }
    
    @Test
    public void testSetDefaultValues() {
        List<String> defaults = Arrays.asList("default1", "default2");
        commandLine.setDefaultValues(option1, defaults);
        List<String> result = commandLine.getValues(option1, null);
        Assert.assertEquals(2, result.size());
        Assert.assertEquals("default1", result.get(0));
        Assert.assertEquals("default2", result.get(1));
    }
    
    @Test
    public void testSetDefaultSwitch() {
        commandLine.setDefaultSwitch(option1, true);
        Boolean switchValue = commandLine.getSwitch(option1, null);
        Assert.assertEquals(Boolean.TRUE, switchValue);
    }
    
    @Test
    public void testGetNormalised() {
        List<String> normalised = commandLine.getNormalised();
        Assert.assertEquals(2, normalised.size());
        Assert.assertEquals("arg1", normalised.get(0));
        Assert.assertEquals("arg2", normalised.get(1));
    }
}
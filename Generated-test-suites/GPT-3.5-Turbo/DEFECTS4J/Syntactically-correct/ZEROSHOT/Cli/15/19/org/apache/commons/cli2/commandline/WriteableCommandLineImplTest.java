import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineImplTest {
    @Test
    public void testAddOption() {
        Option option1 = new Argument();
        Option option2 = new Argument();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList<>());
        
        commandLine.addOption(option2);
        
        List<Option> options = commandLine.getOptions();
        Assert.assertEquals(2, options.size());
        Assert.assertEquals(option1, options.get(0));
        Assert.assertEquals(option2, options.get(1));
    }
    
    @Test
    public void testAddValue() {
        Option option = new Argument();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        
        List<String> values = commandLine.getValues(option, null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }
    
    @Test
    public void testAddSwitch() {
        Option option = new Argument();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        
        commandLine.addSwitch(option, true);
        
        Boolean switchValue = commandLine.getSwitch(option, null);
        Assert.assertEquals(Boolean.TRUE, switchValue);
    }
    
    @Test
    public void testHasOption() {
        Option option1 = new Argument();
        Option option2 = new Argument();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList<>());
        
        boolean hasOption1 = commandLine.hasOption(option1);
        boolean hasOption2 = commandLine.hasOption(option2);
        
        Assert.assertTrue(hasOption1);
        Assert.assertFalse(hasOption2);
    }
    
    @Test
    public void testGetOption() {
        Option option1 = new Argument();
        Option option2 = new Argument();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList<>());
        
        Option result1 = commandLine.getOption(option1.getPreferredName());
        Option result2 = commandLine.getOption(option2.getPreferredName());
        
        Assert.assertEquals(option1, result1);
        Assert.assertNull(result2);
    }
    
    @Test
    public void testGetValues() {
        Option option = new Argument();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        defaultValues.add("default2");
        
        commandLine.setDefaultValues(option, defaultValues);
        
        List<String> values = commandLine.getValues(option, null);
        
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("default1", values.get(0));
        Assert.assertEquals("default2", values.get(1));
    }
    
    @Test
    public void testGetUndefaultedValues() {
        Option option = new Argument();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        
        List<String> values = commandLine.getUndefaultedValues(option);
        
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }
    
    @Test
    public void testGetSwitch() {
        Option option = new Argument();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        
        Boolean defaultValue = Boolean.TRUE;
        commandLine.setDefaultSwitch(option, defaultValue);
        
        Boolean switchValue = commandLine.getSwitch(option, null);
        
        Assert.assertEquals(defaultValue, switchValue);
    }
    
    @Test
    public void testGetProperty() {
        Option option = new Argument();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        
        String propertyName = "property";
        String defaultValue = "default";
        
        String propertyValue = commandLine.getProperty(option, propertyName, defaultValue);
        
        Assert.assertEquals(defaultValue, propertyValue);
    }
    
    @Test
    public void testAddProperty() {
        Option option = new Argument();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        
        String propertyName = "property";
        String propertyValue = "value";
        
        commandLine.addProperty(option, propertyName, propertyValue);
        
        Set<String> properties = commandLine.getProperties(option);
        Assert.assertEquals(1, properties.size());
        Assert.assertEquals(propertyValue, commandLine.getProperty(option, propertyName, null));
    }
    
    @Test
    public void testGetProperties() {
        Option option = new Argument();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        
        String propertyName1 = "property1";
        String propertyName2 = "property2";
        String value1 = "value1";
        String value2 = "value2";
        
        commandLine.addProperty(option, propertyName1, value1);
        commandLine.addProperty(option, propertyName2, value2);
        
        Set<String> properties = commandLine.getProperties(option);
        
        Assert.assertEquals(2, properties.size());
        Assert.assertTrue(properties.contains(propertyName1));
        Assert.assertTrue(properties.contains(propertyName2));
    }
    
    @Test
    public void testLooksLikeOption() {
        Option option = new Argument();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        
        String trigger1 = "-trigger1";
        String trigger2 = "--trigger2";
        
        boolean result1 = commandLine.looksLikeOption(trigger1);
        boolean result2 = commandLine.looksLikeOption(trigger2);
        
        Assert.assertTrue(result1);
        Assert.assertTrue(result2);
    }
    
    @Test
    public void testToString() {
        Option option = new Argument();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        
        String result = commandLine.toString();
        
        Assert.assertEquals("value1 value2", result);
    }
    
    @Test
    public void testGetNormalised() {
        Option option = new Argument();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        
        List<String> normalised = commandLine.getNormalised();
        
        Assert.assertEquals(2, normalised.size());
        Assert.assertEquals("value1", normalised.get(0));
        Assert.assertEquals("value2", normalised.get(1));
    }
}
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    @Before
    public void setup() {
        Option rootOption = new OptionBuilder().withPreferredName("root").create();
        List arguments = new ArrayList();
        commandLine = new WriteableCommandLineImpl(rootOption, arguments);
    }

    @Test
    public void testAddOption() {
        Option option = new OptionBuilder().withPreferredName("testOption").create();
        commandLine.addOption(option);
        Assert.assertTrue(commandLine.hasOption(option));
    }
    
    @Test
    public void testAddValue() {
        Option option = new OptionBuilder().withPreferredName("testOption").create();
        Object value = "testValue";
        commandLine.addValue(option, value);
        List values = commandLine.getValues(option, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals(value, values.get(0));
    }
    
    @Test
    public void testAddSwitch() {
        Option option = new OptionBuilder().withPreferredName("testOption").create();
        commandLine.addSwitch(option, true);
        Assert.assertTrue(commandLine.getSwitch(option, null));
    }
    
    @Test
    public void testHasOption() {
        Option option = new OptionBuilder().withPreferredName("testOption").create();
        commandLine.addOption(option);
        Assert.assertTrue(commandLine.hasOption(option));
        Option nonExistingOption = new OptionBuilder().withPreferredName("nonExistingOption").create();
        Assert.assertFalse(commandLine.hasOption(nonExistingOption));
    }
    
    @Test
    public void testGetOption() {
        Option option = new OptionBuilder().withPreferredName("testOption").create();
        commandLine.addOption(option);
        Option retrievedOption = commandLine.getOption("testOption");
        Assert.assertEquals(option, retrievedOption);
        Option nonExistingOption = commandLine.getOption("nonExistingOption");
        Assert.assertNull(nonExistingOption);
    }
    
    @Test
    public void testGetValues() {
        Option option = new OptionBuilder().withPreferredName("testOption").create();
        Object defaultValue = "defaultValue";
        commandLine.setDefaultValues(option, null);
        List values = commandLine.getValues(option, defaultValue);
        Assert.assertEquals(defaultValue, values);
    }
    
    @Test
    public void testGetUndefaultedValues() {
        Option option = new OptionBuilder().withPreferredName("testOption").create();
        Object value = "testValue";
        commandLine.addValue(option, value);
        List values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals(value, values.get(0));
    }
    
    @Test
    public void testGetSwitch() {
        Option option = new OptionBuilder().withPreferredName("testOption").create();
        Boolean defaultValue = Boolean.TRUE;
        commandLine.setDefaultSwitch(option, null);
        Boolean switchValue = commandLine.getSwitch(option, defaultValue);
        Assert.assertEquals(defaultValue, switchValue);
    }
    
    @Test
    public void testGetProperty() {
        String property = "testProperty";
        String value = "testValue";
        commandLine.addProperty(property, value);
        String retrievedValue = commandLine.getProperty(property);
        Assert.assertEquals(value, retrievedValue);
    }
    
    @Test
    public void testAddProperty() {
        Option option = new OptionBuilder().withPreferredName("testOption").create();
        String property = "testProperty";
        String value = "testValue";
        commandLine.addProperty(option, property, value);
        String retrievedValue = commandLine.getProperty(option, property, null);
        Assert.assertEquals(value, retrievedValue);
    }
    
    @Test
    public void testGetProperties() {
        Option option = new OptionBuilder().withPreferredName("testOption").create();
        String property = "testProperty";
        String value = "testValue";
        commandLine.addProperty(option, property, value);
        Set properties = commandLine.getProperties(option);
        Assert.assertEquals(1, properties.size());
        Assert.assertEquals(property, properties.iterator().next());
    }
    
    @Test
    public void testLooksLikeOption() {
        String optionTrigger = "--test";
        boolean result = commandLine.looksLikeOption(optionTrigger);
        Assert.assertTrue(result);
    }
    
    @Test
    public void testToString() {
        Option option = new OptionBuilder().withPreferredName("testOption").create();
        Object value = "testValue";
        commandLine.addValue(option, value);
        String expectedString = "testValue";
        String resultString = commandLine.toString();
        Assert.assertEquals(expectedString, resultString);
    }
    
    @Test
    public void testGetOptions() {
        Option option1 = new OptionBuilder().withPreferredName("testOption1").create();
        Option option2 = new OptionBuilder().withPreferredName("testOption2").create();
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        List options = commandLine.getOptions();
        Assert.assertEquals(2, options.size());
        Assert.assertTrue(options.contains(option1));
        Assert.assertTrue(options.contains(option2));
    }
    
    @Test
    public void testGetOptionTriggers() {
        Option option1 = new OptionBuilder().withPreferredName("testOption1").create();
        Option option2 = new OptionBuilder().withPreferredName("testOption2").create();
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        Set optionTriggers = commandLine.getOptionTriggers();
        Assert.assertEquals(2, optionTriggers.size());
        Assert.assertTrue(optionTriggers.contains("testOption1"));
        Assert.assertTrue(optionTriggers.contains("testOption2"));
    }
    
    @Test
    public void testSetDefaultValues() {
        Option option = new OptionBuilder().withPreferredName("testOption").create();
        List defaultValues = new ArrayList();
        commandLine.setDefaultValues(option, defaultValues);
        List values = commandLine.getValues(option, null);
        Assert.assertEquals(defaultValues, values);
    }
    
    @Test
    public void testSetDefaultSwitch() {
        Option option = new OptionBuilder().withPreferredName("testOption").create();
        Boolean defaultSwitch = Boolean.TRUE;
        commandLine.setDefaultSwitch(option, defaultSwitch);
        Boolean switchValue = commandLine.getSwitch(option, null);
        Assert.assertEquals(defaultSwitch, switchValue);
    }
    
    @Test
    public void testGetNormalised() {
        List arguments = new ArrayList();
        arguments.add("argument1");
        arguments.add("argument2");
        commandLine = new WriteableCommandLineImpl(new OptionBuilder().withPreferredName("root").create(), arguments);
        List normalised = commandLine.getNormalised();
        Assert.assertEquals(arguments, normalised);
    }

}
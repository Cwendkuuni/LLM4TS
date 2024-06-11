import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        Option option = new OptionBuilder().withName("test").create();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        commandLine.addOption(option);
        Assert.assertTrue(commandLine.hasOption(option));
    }
    
    @Test
    public void testAddValue() {
        Option option = new OptionBuilder().withName("test").create();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        commandLine.addValue(option, "value");
        List values = commandLine.getValues(option, null);
        Assert.assertEquals("value", values.get(0));
    }
    
    @Test
    public void testAddSwitch() {
        Option option = new OptionBuilder().withName("test").create();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        commandLine.addSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);
    }
    
    @Test
    public void testHasOption() {
        Option option = new OptionBuilder().withName("test").create();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        commandLine.addOption(option);
        Assert.assertTrue(commandLine.hasOption(option));
    }
    
    @Test
    public void testGetOption() {
        Option option = new OptionBuilder().withName("test").create();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        Option retrievedOption = commandLine.getOption("test");
        Assert.assertEquals(option, retrievedOption);
    }
    
    @Test
    public void testGetValues() {
        Option option = new OptionBuilder().withName("test").create();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List values = commandLine.getValues(option, null);
        List expectedValues = new ArrayList();
        expectedValues.add("value1");
        expectedValues.add("value2");
        Assert.assertEquals(expectedValues, values);
    }
    
    @Test
    public void testGetUndefaultedValues() {
        Option option = new OptionBuilder().withName("test").create();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List undefaultedValues = commandLine.getUndefaultedValues(option);
        List expectedValues = new ArrayList();
        expectedValues.add("value1");
        expectedValues.add("value2");
        Assert.assertEquals(expectedValues, undefaultedValues);
    }
    
    @Test
    public void testGetSwitch() {
        Option option = new OptionBuilder().withName("test").create();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        commandLine.addSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);
    }
    
    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new OptionBuilder().create(), new ArrayList());
        commandLine.addProperty("property", "value");
        String propertyValue = commandLine.getProperty("property");
        Assert.assertEquals("value", propertyValue);
    }
    
    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new OptionBuilder().create(), new ArrayList());
        commandLine.addProperty("property", "value");
        String propertyValue = commandLine.getProperty("property");
        Assert.assertEquals("value", propertyValue);
    }
    
    @Test
    public void testGetPropertyWithDefault() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new OptionBuilder().create(), new ArrayList());
        String propertyValue = commandLine.getProperty("property", "default");
        Assert.assertEquals("default", propertyValue);
    }
    
    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new OptionBuilder().create(), new ArrayList());
        commandLine.addProperty("property1", "value1");
        commandLine.addProperty("property2", "value2");
        Set properties = commandLine.getProperties();
        Set expectedProperties = new HashSet();
        expectedProperties.add("property1");
        expectedProperties.add("property2");
        Assert.assertEquals(expectedProperties, properties);
    }
    
    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new OptionBuilder().create(), new ArrayList());
        boolean looksLikeOption = commandLine.looksLikeOption("-t");
        Assert.assertTrue(looksLikeOption);
    }
    
    @Test
    public void testToString() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new OptionBuilder().create(), new ArrayList());
        commandLine.addValue(new ArgumentBuilder().create(), "value");
        String toString = commandLine.toString();
        Assert.assertEquals("value", toString);
    }
    
    @Test
    public void testGetOptions() {
        Option option1 = new OptionBuilder().withName("test1").create();
        Option option2 = new OptionBuilder().withName("test2").create();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new OptionBuilder().create(), new ArrayList());
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        List options = commandLine.getOptions();
        List expectedOptions = new ArrayList();
        expectedOptions.add(option1);
        expectedOptions.add(option2);
        Assert.assertEquals(expectedOptions, options);
    }
    
    @Test
    public void testGetOptionTriggers() {
        Option option1 = new OptionBuilder().withName("test1").create();
        Option option2 = new OptionBuilder().withName("test2").create();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new OptionBuilder().create(), new ArrayList());
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        Set triggers = commandLine.getOptionTriggers();
        Set expectedTriggers = new HashSet();
        expectedTriggers.add("test1");
        expectedTriggers.add("test2");
        Assert.assertEquals(expectedTriggers, triggers);
    }
    
    @Test
    public void testSetDefaultValues() {
        Option option = new OptionBuilder().withName("test").create();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new OptionBuilder().create(), new ArrayList());
        commandLine.setDefaultValues(option, Arrays.asList("default1", "default2"));
        List values = commandLine.getValues(option, null);
        List expectedValues = new ArrayList();
        expectedValues.add("default1");
        expectedValues.add("default2");
        Assert.assertEquals(expectedValues, values);
    }
    
    @Test
    public void testSetDefaultSwitch() {
        Option option = new OptionBuilder().withName("test").create();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new OptionBuilder().create(), new ArrayList());
        commandLine.setDefaultSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);
    }
    
    @Test
    public void testGetNormalised() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new OptionBuilder().create(), Arrays.asList("arg1", "arg2"));
        List normalised = commandLine.getNormalised();
        List expectedArgs = Arrays.asList("arg1", "arg2");
        Assert.assertEquals(expectedArgs, normalised);
    }

}
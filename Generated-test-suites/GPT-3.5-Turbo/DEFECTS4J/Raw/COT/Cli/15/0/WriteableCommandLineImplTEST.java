import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
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
        commandLine.addValue(option, "value");
        List values = commandLine.getValues(option, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = new OptionBuilder().withPreferredName("testSwitch").create();
        commandLine.addSwitch(option, true);
        Boolean value = commandLine.getSwitch(option, null);
        Assert.assertEquals(Boolean.TRUE, value);
    }

    @Test
    public void testHasOption() {
        Option option = new OptionBuilder().withPreferredName("testOption").create();
        commandLine.addOption(option);
        Assert.assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new OptionBuilder().withPreferredName("testOption").create();
        commandLine.addOption(option);
        Option retrievedOption = commandLine.getOption("testOption");
        Assert.assertEquals(option, retrievedOption);
    }

    @Test
    public void testGetValues() {
        Option option = new OptionBuilder().withPreferredName("testOption").create();
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List values = commandLine.getValues(option, null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new OptionBuilder().withPreferredName("testOption").create();
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetSwitch() {
        Option option = new OptionBuilder().withPreferredName("testSwitch").create();
        commandLine.addSwitch(option, true);
        Boolean value = commandLine.getSwitch(option, null);
        Assert.assertEquals(Boolean.TRUE, value);
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
        String property = "testProperty";
        String value = "testValue";
        Option option = new OptionBuilder().withPreferredName("testOption").create();
        commandLine.addProperty(option, property, value);
        String retrievedValue = commandLine.getProperty(option, property, null);
        Assert.assertEquals(value, retrievedValue);
    }

    @Test
    public void testGetPropertyWithDefault() {
        String property = "testProperty";
        String value = "testValue";
        String defaultValue = "defaultValue";
        commandLine.addProperty(property, value);
        String retrievedValue = commandLine.getProperty(property, defaultValue);
        Assert.assertEquals(value, retrievedValue);
        String retrievedDefaultValue = commandLine.getProperty("nonExistingProperty", defaultValue);
        Assert.assertEquals(defaultValue, retrievedDefaultValue);
    }

    @Test
    public void testGetProperties() {
        String property = "testProperty";
        String value = "testValue";
        commandLine.addProperty(property, value);
        Set properties = commandLine.getProperties();
        Assert.assertTrue(properties.contains(property));
    }

    @Test
    public void testLooksLikeOption() {
        Option option = new OptionBuilder().withPreferredName("testOption").withPrefix("-").create();
        commandLine.addOption(option);
        Assert.assertTrue(commandLine.looksLikeOption("-testOption"));
        Assert.assertFalse(commandLine.looksLikeOption("testOption"));
    }

    @Test
    public void testToString() {
        commandLine.addValue(new ArgumentBuilder().withName("arg1").create(), "value");
        commandLine.addValue(new ArgumentBuilder().withName("arg2").create(), "value");
        String expectedString = "value value";
        String actualString = commandLine.toString();
        Assert.assertEquals(expectedString, actualString);
    }

    @Test
    public void testGetOptions() {
        Option option1 = new OptionBuilder().withPreferredName("option1").create();
        Option option2 = new OptionBuilder().withPreferredName("option2").create();
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        List options = commandLine.getOptions();
        Assert.assertTrue(options.contains(option1));
        Assert.assertTrue(options.contains(option2));
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = new OptionBuilder().withPreferredName("option1").create();
        Option option2 = new OptionBuilder().withPreferredName("option2").create();
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        Set triggers = commandLine.getOptionTriggers();
        Assert.assertTrue(triggers.contains("option1"));
        Assert.assertTrue(triggers.contains("option2"));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new OptionBuilder().withPreferredName("testOption").create();
        List values = new ArrayList();
        values.add("default1");
        values.add("default2");
        commandLine.setDefaultValues(option, values);
        List retrievedValues = commandLine.getValues(option, null);
        Assert.assertEquals(values, retrievedValues);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new OptionBuilder().withPreferredName("testSwitch").create();
        commandLine.setDefaultSwitch(option, true);
        Boolean value = commandLine.getSwitch(option, null);
        Assert.assertEquals(Boolean.TRUE, value);
    }

    @Test
    public void testGetNormalised() {
        Argument argument1 = new ArgumentBuilder().withName("arg1").create();
        Argument argument2 = new ArgumentBuilder().withName("arg2").create();
        commandLine.addValue(argument1, "value1");
        commandLine.addValue(argument2, "value2");
        List normalised = commandLine.getNormalised();
        Assert.assertEquals("value1", normalised.get(0));
        Assert.assertEquals("value2", normalised.get(1));
    }
}
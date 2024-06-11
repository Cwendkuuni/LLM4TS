import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class WriteableCommandLineImplTest {

    private Option option;

    @Before
    public void setUp() {
        option = new OptionBuilder().withPreferredName("option").create();
    }

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addOption(option);
        List<Option> options = commandLine.getOptions();
        Assert.assertTrue(options.contains(option));
    }

    @Test
    public void testAddValue_WithArgumentOption() {
        Argument argumentOption = new ArgumentBuilder().withPreferredName("argument").create();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(argumentOption, null);
        commandLine.addValue(argumentOption, "value");
        List<String> values = commandLine.getValues(argumentOption, null);
        Assert.assertEquals(Arrays.asList("value"), values);
    }

    @Test
    public void testAddValue_WithOptionAsArgument() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addValue(option, "value");
        List<String> values = commandLine.getValues(option, null);
        Assert.assertEquals(Arrays.asList("value"), values);
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, null);
        Assert.assertTrue(switchValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitch_WithExistingSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addOption(option);
        boolean hasOption = commandLine.hasOption(option);
        Assert.assertTrue(hasOption);
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addOption(option);
        Option retrievedOption = commandLine.getOption("option");
        Assert.assertEquals(option, retrievedOption);
    }

    @Test
    public void testGetValues_WithCommandLineValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List<String> values = commandLine.getValues(option, null);
        Assert.assertEquals(Arrays.asList("value1", "value2"), values);
    }

    @Test
    public void testGetValues_WithDefaultValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.setDefaultValues(option, Arrays.asList("default1", "default2"));
        List<String> values = commandLine.getValues(option, null);
        Assert.assertEquals(Arrays.asList("default1", "default2"), values);
    }

    @Test
    public void testGetValues_WithMethodDefaultValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        List<String> values = commandLine.getValues(option, Arrays.asList("methodDefault1", "methodDefault2"));
        Assert.assertEquals(Arrays.asList("methodDefault1", "methodDefault2"), values);
    }

    @Test
    public void testGetValues_WithoutValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        List<String> values = commandLine.getValues(option, null);
        Assert.assertEquals(0, values.size());
    }

    @Test
    public void testGetSwitch_WithCommandLineValue() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, null);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testGetSwitch_WithDefaultValue() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.setDefaultSwitch(option, false);
        Boolean switchValue = commandLine.getSwitch(option, null);
        Assert.assertFalse(switchValue);
    }

    @Test
    public void testGetSwitch_WithMethodDefaultValue() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        Boolean switchValue = commandLine.getSwitch(option, true);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testGetSwitch_WithoutValue() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        Boolean switchValue = commandLine.getSwitch(option, null);
        Assert.assertNull(switchValue);
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addProperty("property", "value");
        String propertyValue = commandLine.getProperty("property", null);
        Assert.assertEquals("value", propertyValue);
    }

    @Test
    public void testGetProperty_WithDefaultValue() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        String propertyValue = commandLine.getProperty("property", "default");
        Assert.assertEquals("default", propertyValue);
    }

    @Test
    public void testGetsProperties() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addProperty("property1", "value1");
        commandLine.addProperty("property2", "value2");
        Set<String> properties = commandLine.getProperties();
        Assert.assertEquals(2, properties.size());
        Assert.assertTrue(properties.contains("property1"));
        Assert.assertTrue(properties.contains("property2"));
    }

    @Test
    public void testLooksLikeOption_WithPrefix() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.setPrefixes(Arrays.asList("--", "-"));
        boolean looksLikeOption = commandLine.looksLikeOption("--option");
        Assert.assertTrue(looksLikeOption);
    }

    @Test
    public void testLooksLikeOption_WithoutPrefix() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.setPrefixes(Arrays.asList("--", "-"));
        boolean looksLikeOption = commandLine.looksLikeOption("option");
        Assert.assertFalse(looksLikeOption);
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("arg1", "arg2"));
        String toString = commandLine.toString();
        Assert.assertEquals("arg1 arg2", toString);
    }

    @Test
    public void testGetOptionTriggers() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addOption(option);
        Set<String> optionTriggers = commandLine.getOptionTriggers();
        Assert.assertEquals(1, optionTriggers.size());
        Assert.assertTrue(optionTriggers.contains("option"));
    }

    @Test
    public void testGetNormalised() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("arg1", "arg2"));
        List<String> normalised = commandLine.getNormalised();
        Assert.assertEquals(Arrays.asList("arg1", "arg2"), normalised);
    }
}
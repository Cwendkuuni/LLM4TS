import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class WriteableCommandLineImplTest {

    private Option option;
    private List arguments;
    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        option = new Option("testOption");
        arguments = new ArrayList(Arrays.asList("arg1", "arg2", "arg3"));
        commandLine = new WriteableCommandLineImpl(option, arguments);
    }

    @After
    public void tearDown() {
        option = null;
        arguments = null;
        commandLine = null;
    }

    @Test
    public void testAddOption() {
        Option newOption = new Option("newOption");
        commandLine.addOption(newOption);
        List<Option> options = commandLine.getOptions();
        Assert.assertTrue(options.contains(newOption));
    }

    @Test
    public void testAddValue() {
        Object value = "value1";
        commandLine.addValue(option, value);
        List<Object> values = commandLine.getValues(option, null);
        Assert.assertTrue(values.contains(value));
    }

    @Test
    public void testAddSwitch() {
        boolean value = true;
        commandLine.addSwitch(option, value);
        Boolean switchValue = commandLine.getSwitch(option, null);
        Assert.assertEquals(value, switchValue);
    }

    @Test
    public void testHasOption() {
        Assert.assertTrue(commandLine.hasOption(option));
        Option nonExistentOption = new Option("nonExistentOption");
        Assert.assertFalse(commandLine.hasOption(nonExistentOption));
    }

    @Test
    public void testGetOption() {
        String trigger = option.getPreferredName();
        Option returnedOption = commandLine.getOption(trigger);
        Assert.assertEquals(option, returnedOption);
    }

    @Test
    public void testGetValues() {
        List<String> defaultValues = Arrays.asList("defaultValue1", "defaultValue2");
        List<String> returnedValues = commandLine.getValues(option, defaultValues);
        Assert.assertEquals(defaultValues, returnedValues);
    }

    @Test
    public void testGetSwitch() {
        Boolean defaultValue = Boolean.FALSE;
        Boolean switchValue = commandLine.getSwitch(option, defaultValue);
        Assert.assertEquals(defaultValue, switchValue);
    }

    @Test
    public void testAddProperty() {
        String property = "testProperty";
        String value = "testValue";
        commandLine.addProperty(property, value);
        String returnedValue = commandLine.getProperty(property, null);
        Assert.assertEquals(value, returnedValue);
    }

    @Test
    public void testLooksLikeOption() {
        String triggerWithPrefix = option.getPreferredName();
        String triggerWithoutPrefix = triggerWithPrefix.substring(1);
        Assert.assertTrue(commandLine.looksLikeOption(triggerWithPrefix));
        Assert.assertFalse(commandLine.looksLikeOption(triggerWithoutPrefix));
    }

    @Test
    public void testToString() {
        String expectedToString = "arg1 arg2 arg3";
        String actualToString = commandLine.toString();
        Assert.assertEquals(expectedToString, actualToString);
    }

    @Test
    public void testGetOptions() {
        List<Option> options = commandLine.getOptions();
        Assert.assertTrue(options.contains(option));
    }

    @Test
    public void testGetOptionTriggers() {
        String trigger = option.getPreferredName();
        Assert.assertTrue(commandLine.getOptionTriggers().contains(trigger));
    }

    @Test
    public void testSetDefaultValues() {
        List<String> defaultValues = Arrays.asList("defaultValue1", "defaultValue2");
        commandLine.setDefaultValues(option, defaultValues);
        List<String> returnedDefaultValues = commandLine.getValues(option, null);
        Assert.assertEquals(defaultValues, returnedDefaultValues);
    }

    @Test
    public void testSetDefaultSwitch() {
        Boolean defaultSwitch = Boolean.TRUE;
        commandLine.setDefaultSwitch(option, defaultSwitch);
        Boolean returnedDefaultSwitch = commandLine.getSwitch(option, null);
        Assert.assertEquals(defaultSwitch, returnedDefaultSwitch);
    }

    @Test
    public void testGetNormalised() {
        List<String> normalised = commandLine.getNormalised();
        Assert.assertEquals(arguments, normalised);
    }

}
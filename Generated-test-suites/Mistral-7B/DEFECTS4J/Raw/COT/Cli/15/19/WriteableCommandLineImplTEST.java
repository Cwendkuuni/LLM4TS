import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Test class for WriteableCommandLineImpl
 */
public class WriteableCommandLineImplTest {

    /**
     * Test constructor
     */
    @Test
    public void testConstructor() {
        Option rootOption = new Option("root", "rootOption", false, "Root option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, new ArrayList<>());
        Assert.assertNotNull(writeableCommandLine);
    }

    /**
     * Test method addOption
     */
    @Test
    public void testAddOption() {
        Option option = new Option("test", "test", false, "Test option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("root", "rootOption", false, "Root option"), new ArrayList<>());
        writeableCommandLine.addOption(option);
        Assert.assertTrue(writeableCommandLine.hasOption(option));
    }

    /**
     * Test method addValue
     */
    @Test
    public void testAddValue() {
        Option option = new Option("test", "test", false, "Test option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("root", "rootOption", false, "Root option"), new ArrayList<>());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");
        List values = writeableCommandLine.getValues(option, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));
    }

    /**
     * Test method addSwitch
     */
    @Test
    public void testAddSwitch() {
        Option option = new Option("test", "test", false, "Test option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("root", "rootOption", false, "Root option"), new ArrayList<>());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);
        Boolean switchValue = writeableCommandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);
    }

    /**
     * Test method hasOption
     */
    @Test
    public void testHasOption() {
        Option option = new Option("test", "test", false, "Test option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("root", "rootOption", false, "Root option"), new ArrayList<>());
        writeableCommandLine.addOption(option);
        Assert.assertTrue(writeableCommandLine.hasOption(option));
    }

    /**
     * Test method getOption
     */
    @Test
    public void testGetOption() {
        Option option = new Option("test", "test", false, "Test option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("root", "rootOption", false, "Root option"), new ArrayList<>());
        writeableCommandLine.addOption(option);
        Option returnedOption = writeableCommandLine.getOption("test");
        Assert.assertEquals(option, returnedOption);
    }

    /**
     * Test method getValues
     */
    @Test
    public void testGetValues() {
        Option option = new Option("test", "test", false, "Test option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("root", "rootOption", false, "Root option"), new ArrayList<>());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");
        writeableCommandLine.addValue(option, "value2");
        List values = writeableCommandLine.getValues(option, null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    /**
     * Test method getUndefaultedValues
     */
    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("test", "test", false, "Test option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("root", "rootOption", false, "Root option"), new ArrayList<>());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");
        List values = writeableCommandLine.getUndefaultedValues(option);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));
    }

    /**
     * Test method getSwitch
     */
    @Test
    public void testGetSwitch() {
        Option option = new Option("test", "test", false, "Test option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("root", "rootOption", false, "Root option"), new ArrayList<>());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);
        Boolean switchValue = writeableCommandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);
    }

    /**
     * Test method getProperty
     */
    @Test
    public void testGetProperty() {
        Option option = new Option("test", "test", false, "Test option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("root", "rootOption", false, "Root option"), new ArrayList<>());
        writeableCommandLine.addOption(option);
        Properties properties = new Properties();
        properties.put("testProperty", "testValue");
        writeableCommandLine.addProperty(option, "testProperty", "testValue");
        String propertyValue = writeableCommandLine.getProperty(option, "testProperty", null);
        Assert.assertEquals("testValue", propertyValue);
    }

    /**
     * Test method addProperty
     */
    @Test
    public void testAddProperty() {
        Option option = new Option("test", "test", false, "Test option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("root", "rootOption", false, "Root option"), new ArrayList<>());
        writeableCommandLine.addOption(option);
        Properties properties = new Properties();
        properties.put("testProperty", "testValue");
        writeableCommandLine.addProperty(option, "testProperty", "testValue");
        Map<String, String> propertiesMap = writeableCommandLine.getProperties(option);
        Assert.assertEquals("testValue", propertiesMap.get("testProperty"));
    }

    /**
     * Test method getProperties
     */
    @Test
    public void testGetProperties() {
        Option option = new Option("test", "test", false, "Test option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("root", "rootOption", false, "Root option"), new ArrayList<>());
        Properties properties = new Properties();
        properties.put("testProperty1", "testValue1");
        properties.put("testProperty2", "testValue2");
        writeableCommandLine.addProperty(option, "testProperty1", "testValue1");
        writeableCommandLine.addProperty(option, "testProperty2", "testValue2");
        Map<String, String> propertiesMap = writeableCommandLine.getProperties(option);
        Assert.assertEquals(2, propertiesMap.size());
        Assert.assertEquals("testValue1", propertiesMap.get("testProperty1"));
        Assert.assertEquals("testValue2", propertiesMap.get("testProperty2"));
    }

    /**
     * Test method looksLikeOption
     */
    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("root", "rootOption", false, "Root option"), new ArrayList<>());
        String trigger1 = "test1";
        String trigger2 = "test2-option";
        Assert.assertTrue(writeableCommandLine.looksLikeOption(trigger1));
        Assert.assertFalse(writeableCommandLine.looksLikeOption(trigger2));

        Option option1 = new Option("test1", "test1", false, "Test option 1");
        Option option2 = new Option("test2", "test2", false, "Test option 2");
        WriteableCommandLineImpl writeableCommandLineWithOptions = new WriteableCommandLineImpl(option1, new ArrayList<>());
        writeableCommandLineWithOptions.addOption(option2);
        Assert.assertTrue(writeableCommandLineWithOptions.looksLikeOption(trigger1));
        Assert.assertTrue(writeableCommandLineWithOptions.looksLikeOption(trigger2));
    }

    /**
     * Test method toString
     */
    @Test
    public void testToString() {
        Option option1 = new Option("test1", "test1", false, "Test option 1");
        Option option2 = new Option("test2", "test2", false, "Test option 2");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option1, new ArrayList<>());
        writeableCommandLine.addOption(option2);
        writeableCommandLine.addValue(option1, "value1");
        writeableCommandLine.addValue(option2, "value2");
        String commandLineString = writeableCommandLine.toString();
        String expectedResult = "test1 value1 test2 value2";
        Assert.assertEquals(expectedResult, commandLineString);
    }

    /**
     * Test method getOptions
     */
    @Test
    public void testGetOptions() {
        Option option1 = new Option("test1", "test1", false, "Test option 1");
        Option option2 = new Option("test2", "test2", false, "Test option 2");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option1, new ArrayList<>());
        writeableCommandLine.addOption(option2);
        List<Option> options = writeableCommandLine.getOptions();
        Assert.assertEquals(2, options.size());
        Assert.assertEquals(option1, options.get(0));
        Assert.assertEquals(option2, options.get(1));
    }

    /**
     * Test method getOptionTriggers
     */
    @Test
    public void testGetOptionTriggers() {
        Option option1 = new Option("test1", "test1", false, "Test option 1");
        Option option2 = new Option("test2", "test2", false, "Test option 2");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option1, new ArrayList<>());
        writeableCommandLine.addOption(option2);
        Set<String> optionTriggers = writeableCommandLine.getOptionTriggers();
        Assert.assertEquals(2, optionTriggers.size());
        Assert.assertTrue(optionTriggers.contains("test1"));
        Assert.assertTrue(optionTriggers.contains("test2"));
    }

    /**
     * Test method setDefaultValues
     */
    @Test
    public void testSetDefaultValues() {
        Option option = new Option("test", "test", false, "Test option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("root", "rootOption", false, "Root option"), new ArrayList<>());
        List defaultValues = new ArrayList();
        defaultValues.add("defaultValue1");
        writeableCommandLine.setDefaultValues(option, defaultValues);
        List values = writeableCommandLine.getValues(option, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("defaultValue1", values.get(0));
    }

    /**
     * Test method setDefaultSwitch
     */
    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("test", "test", false, "Test option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("root", "rootOption", false, "Root option"), new ArrayList<>());
        writeableCommandLine.setDefaultSwitch(option, true);
        Boolean switchValue = writeableCommandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);
    }

    /**
     * Test method setDefaultValues with empty list
     */
    @Test
    public void testSetDefaultValuesWithEmptyList() {
        Option option = new Option("test", "test", false, "Test option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("root", "rootOption", false, "Root option"), new ArrayList<>());
        writeableCommandLine.setDefaultValues(option, null);
        List values = writeableCommandLine.getValues(option, null);
        Assert.assertEquals(0, values.size());
    }

    /**
     * Test method setDefaultSwitch with null value
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetDefaultSwitchWithNullValue() {
        Option option = new Option("test", "test", false, "Test option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("root", "rootOption", false, "Root option"), new ArrayList<>());
        writeableCommandLine.setDefaultSwitch(option, null);
    }
}
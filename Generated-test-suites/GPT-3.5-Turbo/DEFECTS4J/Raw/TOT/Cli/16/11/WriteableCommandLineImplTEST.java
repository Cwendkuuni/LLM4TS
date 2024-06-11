import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl writeableCommandLine;

    @Before
    public void setUp() {
        Option rootOption = new Option("root");
        List<String> arguments = new ArrayList<>();
        writeableCommandLine = new WriteableCommandLineImpl(rootOption, arguments);
    }

    @Test
    public void testAddOption() {
        Option option = new Option("test");
        writeableCommandLine.addOption(option);

        boolean hasOption = writeableCommandLine.hasOption(option);
        Assert.assertTrue(hasOption);
    }

    @Test
    public void testAddValue() {
        Option option = new Option("test");
        writeableCommandLine.addOption(option);

        writeableCommandLine.addValue(option, "value");

        List<String> values = writeableCommandLine.getValues(option, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("test");
        writeableCommandLine.addSwitch(option, true);

        Boolean switchValue = writeableCommandLine.getSwitch(option, null);
        Assert.assertEquals(Boolean.TRUE, switchValue);
    }

    @Test
    public void testHasOption() {
        Option option = new Option("test");
        boolean hasOption = writeableCommandLine.hasOption(option);
        Assert.assertFalse(hasOption);
    }

    @Test
    public void testGetOption() {
        Option option = new Option("test");
        writeableCommandLine.addOption(option);

        Option retrievedOption = writeableCommandLine.getOption("test");
        Assert.assertEquals(option, retrievedOption);
    }

    @Test
    public void testGetValues() {
        Option option = new Option("test");
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default");

        List<String> commandLineValues = new ArrayList<>();
        commandLineValues.add("value");

        writeableCommandLine.setDefaultValues(option, defaultValues);
        writeableCommandLine.addValue(option, "value");

        List<String> values = writeableCommandLine.getValues(option, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value", values.get(0));

        values = writeableCommandLine.getValues(option, defaultValues);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value", values.get(0));
        Assert.assertEquals("default", values.get(1));

        values = writeableCommandLine.getValues(option, commandLineValues);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value", values.get(0));
        Assert.assertEquals("default", values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("test");
        List<String> commandLineValues = new ArrayList<>();
        commandLineValues.add("value");

        writeableCommandLine.addValue(option, "value");

        List<String> values = writeableCommandLine.getUndefaultedValues(option);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value", values.get(0));

        values = writeableCommandLine.getUndefaultedValues(new Option("invalid"));
        Assert.assertEquals(0, values.size());

        values = writeableCommandLine.getUndefaultedValues(option);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value", values.get(0));
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("test");
        Boolean defaultSwitch = Boolean.FALSE;

        writeableCommandLine.setDefaultSwitch(option, defaultSwitch);
        writeableCommandLine.addSwitch(option, true);

        Boolean switchValue = writeableCommandLine.getSwitch(option, defaultSwitch);
        Assert.assertEquals(Boolean.TRUE, switchValue);
    }

    @Test
    public void testGetProperty() {
        String property = "test";

        String value = writeableCommandLine.getProperty(property);
        Assert.assertNull(value);
    }

    @Test
    public void testAddProperty() {
        String property = "test";
        String value = "value";

        writeableCommandLine.addProperty(property, value);

        String retrievedValue = writeableCommandLine.getProperty(property);
        Assert.assertEquals(value, retrievedValue);
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("test");
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default");

        writeableCommandLine.setDefaultValues(option, defaultValues);

        List<String> values = writeableCommandLine.getValues(option, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("default", values.get(0));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("test");
        Boolean defaultSwitch = Boolean.FALSE;

        writeableCommandLine.setDefaultSwitch(option, defaultSwitch);

        Boolean switchValue = writeableCommandLine.getSwitch(option, null);
        Assert.assertEquals(Boolean.FALSE, switchValue);
    }

    @Test
    public void testToString() {
        writeableCommandLine.addValue(new Argument(), "value1");
        writeableCommandLine.addValue(new Argument(), "value2");

        String toString = writeableCommandLine.toString();
        Assert.assertEquals("value1 value2", toString);
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("test1");
        Option option2 = new Option("test2");

        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);

        List<Option> options = writeableCommandLine.getOptions();
        Assert.assertEquals(2, options.size());
        Assert.assertEquals(option1, options.get(0));
        Assert.assertEquals(option2, options.get(1));
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = new Option("test1");
        Option option2 = new Option("test2");

        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);

        Properties option1Properties = new Properties();
        option1Properties.setProperty("property1", "value1");
        option1Properties.setProperty("property2", "value2");

        writeableCommandLine.addProperty(option1, "property1", "value1");
        writeableCommandLine.addProperty(option1, "property2", "value2");

        writeableCommandLine.addProperty(option2, "property3", "value3");

        Set<String> optionTriggers = writeableCommandLine.getOptionTriggers();
        Assert.assertEquals(3, optionTriggers.size());
        Assert.assertTrue(optionTriggers.contains("test1"));
        Assert.assertTrue(optionTriggers.contains("test2"));
        Assert.assertTrue(optionTriggers.contains("property1"));
    }

    @Test
    public void testLooksLikeOption() {
        writeableCommandLine.addOption(new Option("test"));

        boolean looksLikeOption = writeableCommandLine.looksLikeOption("test");
        Assert.assertTrue(looksLikeOption);

        looksLikeOption = writeableCommandLine.looksLikeOption("--test");
        Assert.assertTrue(looksLikeOption);

        looksLikeOption = writeableCommandLine.looksLikeOption("-t");
        Assert.assertTrue(looksLikeOption);

        looksLikeOption = writeableCommandLine.looksLikeOption("invalid");
        Assert.assertFalse(looksLikeOption);
    }

    @Test
    public void testGetProperties() {
        Option option1 = new Option("test1");
        Option option2 = new Option("test2");

        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);

        writeableCommandLine.addProperty(option1, "property1", "value1");
        writeableCommandLine.addProperty(option1, "property2", "value2");

        writeableCommandLine.addProperty(option2, "property3", "value3");

        Set<String> option1Properties = writeableCommandLine.getProperties(option1);
        Assert.assertEquals(2, option1Properties.size());
        Assert.assertTrue(option1Properties.contains("property1"));
        Assert.assertTrue(option1Properties.contains("property2"));

        Set<String> option2Properties = writeableCommandLine.getProperties(option2);
        Assert.assertEquals(1, option2Properties.size());
        Assert.assertTrue(option2Properties.contains("property3"));

        Set<String> allProperties = writeableCommandLine.getProperties();
        Assert.assertEquals(3, allProperties.size());
        Assert.assertTrue(allProperties.contains("property1"));
        Assert.assertTrue(allProperties.contains("property2"));
        Assert.assertTrue(allProperties.contains("property3"));
    }

    @Test
    public void testGetNormalised() {
        writeableCommandLine.addValue(new Argument(), "value1");
        writeableCommandLine.addValue(new Argument(), "value2");

        List<String> normalised = writeableCommandLine.getNormalised();
        Assert.assertEquals(2, normalised.size());
        Assert.assertEquals("value1", normalised.get(0));
        Assert.assertEquals("value2", normalised.get(1));
    }
}
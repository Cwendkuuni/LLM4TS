import org.apache.commons.cli2.CommandLineParser;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl writeableCommandLine;
    private CommandLineParser parser;

    @Before
    public void setup() {
        Option option1 = new Option("o", "option1", true, "Option 1")
                .required(false)
                .defaultValue("defaultValue1")
                .desc("Description for Option 1");

        Option option2 = new Option("s", "switch1", false, "Switch 1")
                .required(false)
                .desc("Description for Switch 1");

        Option option3 = new Option("p", "property1", true, "Property 1")
                .required(false)
                .desc("Description for Property 1");

        parser = new DefaultParser();

        List args = new ArrayList() {{
            add("test");
            add("--option1=value1");
            add("--switch1");
            add("--property1=valueProperty1");
        }};

        writeableCommandLine = new WriteableCommandLineImpl(option1, args);
    }

    @Test
    public void testHasOption() {
        Assert.assertTrue(writeableCommandLine.hasOption(option1));
        Assert.assertTrue(writeableCommandLine.hasOption(option2));
        Assert.assertFalse(writeableCommandLine.hasOption(new Option("nonExistentOption", "nonExistentOption", false, "")));
    }

    @Test
    public void testGetOption() {
        Option option = writeableCommandLine.getOption("option1");
        Assert.assertNotNull(option);
        Assert.assertEquals(option1, option);
    }

    @Test (expected = IllegalStateException.class)
    public void testSetSwitchTwice() {
        writeableCommandLine.addSwitch(option2, true);
        writeableCommandLine.addSwitch(option2, false);
    }

    @Test
    public void testGetValue() {
        List values = writeableCommandLine.getValues(option1, null);
        Assert.assertNotNull(values);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        List values = writeableCommandLine.getUndefaultedValues(option1);
        Assert.assertNotNull(values);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));
    }

    @Test
    public void testGetSwitch() {
        Boolean switchValue = writeableCommandLine.getSwitch(option2, null);
        Assert.assertNull(switchValue);

        writeableCommandLine.addSwitch(option2, true);
        switchValue = writeableCommandLine.getSwitch(option2, null);
        Assert.assertTrue(switchValue);

        writeableCommandLine.addSwitch(option2, false);
        switchValue = writeableCommandLine.getSwitch(option2, null);
        Assert.assertFalse(switchValue);
    }

    @Test
    public void testGetProperty() {
        String propertyValue = writeableCommandLine.getProperty("property1");
        Assert.assertNull(propertyValue);

        writeableCommandLine.addProperty(option3, "propertyKey1", "propertyValue1");
        propertyValue = writeableCommandLine.getProperty("property1");
        Assert.assertNull(propertyValue);

        propertyValue = writeableCommandLine.getProperty("propertyKey1");
        Assert.assertNotNull(propertyValue);
        Assert.assertEquals("propertyValue1", propertyValue);
    }

    @Test
    public void testGetProperties() {
        writeableCommandLine.addProperty(option3, "propertyKey1", "propertyValue1");
        Map properties = writeableCommandLine.getProperties(option3);
        Assert.assertNotNull(properties);
        Assert.assertEquals(1, properties.size());

        Map expectedProperties = new HashMap() {{
            put("propertyKey1", "propertyValue1");
        }};
        Assert.assertEquals(expectedProperties, properties);
    }

    @Test
    public void testLooksLikeOption() {
        Assert.assertTrue(writeableCommandLine.looksLikeOption("--option1"));
        Assert.assertTrue(writeableCommandLine.looksLikeOption("--option1="));
        Assert.assertTrue(writeableCommandLine.looksLikeOption("--option1 value"));
        Assert.assertTrue(writeableCommandLine.looksLikeOption("--option1 value="));

        Assert.assertFalse(writeableCommandLine.looksLikeOption("--nonExistentOption"));
    }

    @Test (expected = ParseException.class)
    public void testAddValueWithNoOption() {
        writeableCommandLine.addValue(null, "value");
    }

    @Test (expected = IllegalStateException.class)
    public void testAddSwitchWithNoOption() {
        writeableCommandLine.addSwitch(null, true);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testAddSwitchWithNonBooleanValue() {
        writeableCommandLine.addSwitch(option2, "nonBooleanValue");
    }

    @Test (expected = IllegalArgumentException.class)
    public void testAddPropertyWithNoOption() {
        writeableCommandLine.addProperty(null, "propertyKey", "propertyValue");
    }

    @Test (expected = IllegalArgumentException.class)
    public void testAddPropertyWithNonOptionValue() {
        writeableCommandLine.addProperty(option1, "propertyKey", null);
    }
}
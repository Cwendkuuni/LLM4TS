import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.option.PropertyOption;
import org.apache.commons.cli2.resource.ResourceHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl writeableCommandLine;

    @Before
    public void setUp() {
        Option rootOption = new Option("rootOption");
        List<String> arguments = Arrays.asList("arg1", "arg2");
        writeableCommandLine = new WriteableCommandLineImpl(rootOption, arguments);
    }

    @Test
    public void testAddOption() {
        Option option = new Option("testOption");
        writeableCommandLine.addOption(option);

        assertTrue(writeableCommandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        Option option = new Argument();
        Object value = "testValue";
        writeableCommandLine.addValue(option, value);

        List<Object> values = writeableCommandLine.getValues(option, null);

        assertNotNull(values);
        assertEquals(1, values.size());
        assertEquals(value, values.get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("testSwitch");
        boolean value = true;
        writeableCommandLine.addSwitch(option, value);

        Boolean switchValue = writeableCommandLine.getSwitch(option, null);

        assertNotNull(switchValue);
        assertEquals(value, switchValue);
    }

    @Test
    public void testHasOption() {
        Option option = new Option("testOption");
        assertFalse(writeableCommandLine.hasOption(option));

        writeableCommandLine.addOption(option);

        assertTrue(writeableCommandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("testOption");
        assertNull(writeableCommandLine.getOption(option.getPreferredName()));

        writeableCommandLine.addOption(option);

        assertEquals(option, writeableCommandLine.getOption(option.getPreferredName()));
    }

    @Test
    public void testGetValues() {
        Option option = new Argument();
        Object value = "testValue";
        writeableCommandLine.addValue(option, value);

        List<Object> values = writeableCommandLine.getValues(option, null);

        assertNotNull(values);
        assertEquals(1, values.size());
        assertEquals(value, values.get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Argument();
        Object value = "testValue";
        writeableCommandLine.addValue(option, value);

        List<Object> values = writeableCommandLine.getUndefaultedValues(option);

        assertNotNull(values);
        assertEquals(1, values.size());
        assertEquals(value, values.get(0));
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("testSwitch");
        Boolean defaultValue = null;
        Boolean switchValue = writeableCommandLine.getSwitch(option, defaultValue);

        assertEquals(defaultValue, switchValue);

        boolean value = true;
        writeableCommandLine.addSwitch(option, value);

        switchValue = writeableCommandLine.getSwitch(option, defaultValue);

        assertNotNull(switchValue);
        assertEquals(value, switchValue);
    }

    @Test
    public void testGetProperty() {
        String property = "testProperty";
        String defaultValue = "defaultValue";

        String propertyValue = writeableCommandLine.getProperty(property, defaultValue);

        assertEquals(defaultValue, propertyValue);
    }

    @Test
    public void testAddProperty() {
        String property = "testProperty";
        String value = "testValue";

        writeableCommandLine.addProperty(property, value);

        String propertyValue = writeableCommandLine.getProperty(property);

        assertEquals(value, propertyValue);
    }

    @Test
    public void testGetPropertyWithDefault() {
        Option option = new PropertyOption();
        String property = "testProperty";
        String value = "testValue";

        writeableCommandLine.addProperty(option, property, value);

        String propertyValue = writeableCommandLine.getProperty(option, property, "defaultValue");

        assertEquals(value, propertyValue);
    }

    @Test
    public void testGetProperties() {
        Option option = new PropertyOption();
        String property = "testProperty";
        String value = "testValue";

        writeableCommandLine.addProperty(option, property, value);

        List<String> properties = new ArrayList<>(writeableCommandLine.getProperties(option));

        assertNotNull(properties);
        assertEquals(1, properties.size());
        assertEquals(property, properties.get(0));
    }

    @Test
    public void testGetProperties_Default() {
        Option option = new PropertyOption();
        List<String> properties = new ArrayList<>(writeableCommandLine.getProperties(option));

        assertNotNull(properties);
        assertTrue(properties.isEmpty());
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(writeableCommandLine.looksLikeOption("-test"));
        assertFalse(writeableCommandLine.looksLikeOption("test"));
    }

    @Test
    public void testToString() {
        assertEquals("arg1 arg2", writeableCommandLine.toString());
    }

    @Test
    public void testGetOptions() {
        Option option = new Option("testOption");
        writeableCommandLine.addOption(option);

        List<Option> options = new ArrayList<>(writeableCommandLine.getOptions());

        assertNotNull(options);
        assertEquals(1, options.size());
        assertEquals(option, options.get(0));
    }

    @Test
    public void testGetOptionTriggers() {
        Option option = new Option("testOption");
        writeableCommandLine.addOption(option);

        List<String> triggers = new ArrayList<>(writeableCommandLine.getOptionTriggers());

        assertNotNull(triggers);
        assertEquals(1, triggers.size());
        assertEquals(option.getPreferredName(), triggers.get(0));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("testOption");
        List<Object> defaults = Arrays.asList("default1", "default2");

        writeableCommandLine.setDefaultValues(option, defaults);
        List<Object> defaultValues = writeableCommandLine.getValues(option, null);

        assertNotNull(defaultValues);
        assertEquals(defaults, defaultValues);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("testOption");
        Boolean defaultSwitch = true;

        writeableCommandLine.setDefaultSwitch(option, defaultSwitch);
        Boolean switchValue = writeableCommandLine.getSwitch(option, null);

        assertEquals(defaultSwitch, switchValue);
    }

    @Test
    public void testGetNormalised() {
        List<String> normalised = new ArrayList<>(writeableCommandLine.getNormalised());

        assertNotNull(normalised);
        assertEquals(2, normalised.size());
        assertEquals("arg1", normalised.get(0));
        assertEquals("arg2", normalised.get(1));
    }
}
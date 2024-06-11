
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.option.PropertyOption;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        Option option1 = new OptionImpl("option1");
        Option option2 = new OptionImpl("option2");
        Argument argument1 = new ArgumentImpl("argument1");

        commandLine = new WriteableCommandLineImpl(option1, Arrays.asList("value1", "value2", "value3"));
        commandLine.addOption(option2);
        commandLine.addValue(argument1, "argumentValue");
        commandLine.addSwitch(option1, true);
    }

    @Test
    public void testAddOption() {
        Option newOption = new OptionImpl("newOption");
        commandLine.addOption(newOption);

        assertTrue(commandLine.hasOption(newOption));
    }

    @Test
    public void testAddValue() {
        Option option = new OptionImpl("option");
        commandLine.addValue(option, "value");

        assertEquals(1, commandLine.getValues(option, Collections.EMPTY_LIST).size());
        assertTrue(commandLine.getValues(option, Collections.EMPTY_LIST).contains("value"));
    }

    @Test
    public void testAddSwitch() {
        Option option = new OptionImpl("option");
        commandLine.addSwitch(option, false);

        assertTrue(commandLine.getSwitch(option, true));
    }

    @Test
    public void testHasOption() {
        Option option = new OptionImpl("option");
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = commandLine.getOption("option1");
        assertNotNull(option);

        option = commandLine.getOption("option");
        assertNull(option);
    }

    @Test
    public void testGetValues() {
        Option option = new OptionImpl("option");
        List<String> defaultValues = Arrays.asList("defaultValue1", "defaultValue2");
        commandLine.setDefaultValues(option, defaultValues);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List<String> values = commandLine.getValues(option, Collections.EMPTY_LIST);

        assertEquals(2, values.size());
        assertTrue(values.contains("value1"));
        assertTrue(values.contains("value2"));

        values = commandLine.getValues(option, defaultValues);

        assertEquals(4, values.size());
        assertTrue(values.contains("value1"));
        assertTrue(values.contains("value2"));
        assertTrue(values.contains("defaultValue1"));
        assertTrue(values.contains("defaultValue2"));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new OptionImpl("option");
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List<String> values = commandLine.getUndefaultedValues(option);

        assertEquals(2, values.size());
        assertTrue(values.contains("value1"));
        assertTrue(values.contains("value2"));
    }

    @Test
    public void testGetSwitch() {
        Option option = new OptionImpl("option");
        commandLine.addSwitch(option, true);

        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testGetProperty() {
        String property = commandLine.getProperty("property");
        assertNull(property);
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty("property", "value");

        String property = commandLine.getProperty("property");
        assertEquals("value", property);
    }

    @Test
    public void testGetPropertyWithDefault() {
        Option option = new OptionImpl("option");
        commandLine.addProperty(option, "property", "value");

        String property = commandLine.getProperty(option, "property", "defaultValue");
        assertEquals("value", property);

        property = commandLine.getProperty(option, "nonExistentProperty", "defaultValue");
        assertEquals("defaultValue", property);
    }

    @Test
    public void testGetProperties() {
        Option option = new OptionImpl("option");
        commandLine.addProperty(option, "property1", "value1");
        commandLine.addProperty(option, "property2", "value2");

        Set<String> properties = commandLine.getProperties(option);

        assertEquals(2, properties.size());
        assertTrue(properties.contains("property1"));
        assertTrue(properties.contains("property2"));
    }

    @Test
    public void testGetPropertiesForDefaultOption() {
        Option option = new OptionImpl("option");
        commandLine.addProperty(option, "property1", "value1");
        commandLine.addProperty(option, "property2", "value2");

        Set<String> properties = commandLine.getProperties();

        assertEquals(2, properties.size());
        assertTrue(properties.contains("property1"));
        assertTrue(properties.contains("property2"));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-option"));
        assertFalse(commandLine.looksLikeOption("value"));
    }

}

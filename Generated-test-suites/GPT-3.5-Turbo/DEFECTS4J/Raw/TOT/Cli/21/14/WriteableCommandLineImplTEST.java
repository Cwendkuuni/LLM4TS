import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    @Before
    public void setup() {
        Option rootOption = new OptionBuilder("root")
                .withDescription("Root option")
                .create();

        List<String> arguments = new ArrayList<>();
        arguments.add("arg1");
        arguments.add("arg2");

        commandLine = new WriteableCommandLineImpl(rootOption, arguments);
    }

    @Test
    public void testAddOption() {
        Option option = new OptionBuilder("test")
                .withDescription("Test option")
                .create();

        commandLine.addOption(option);

        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        Option option = new OptionBuilder("test")
                .withDescription("Test option")
                .create();

        commandLine.addValue(option, "value");

        List<String> values = commandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals("value", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = new OptionBuilder("test")
                .withDescription("Test option")
                .create();

        commandLine.addSwitch(option, true);

        assertTrue(commandLine.getSwitch(option, null));
    }

    @Test
    public void testHasOption() {
        Option option = new OptionBuilder("test")
                .withDescription("Test option")
                .create();

        assertFalse(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = commandLine.getOption("root");
        assertNotNull(option);
        assertEquals("root", option.getPreferredName());
    }

    @Test
    public void testGetValues() {
        Option option = new OptionBuilder("test")
                .withDescription("Test option")
                .create();

        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default");

        commandLine.setDefaultValues(option, defaultValues);

        List<String> values = commandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals("default", values.get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new OptionBuilder("test")
                .withDescription("Test option")
                .create();

        commandLine.addValue(option, "value");

        List<String> values = commandLine.getUndefaultedValues(option);
        assertEquals(1, values.size());
        assertEquals("value", values.get(0));
    }

    @Test
    public void testGetSwitch() {
        Option option = new OptionBuilder("test")
                .withDescription("Test option")
                .create();

        assertTrue(commandLine.getSwitch(option, true));
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
    public void testGetPropertyWithDefaultValue() {
        String property = commandLine.getProperty(new PropertyOption(), "property", "default");
        assertEquals("default", property);
    }

    @Test
    public void testGetProperties() {
        Option option = new OptionBuilder("test")
                .withDescription("Test option")
                .create();

        commandLine.addProperty(option, "property1", "value1");
        commandLine.addProperty(option, "property2", "value2");

        Set<String> properties = commandLine.getProperties(option);
        assertEquals(2, properties.size());
        assertTrue(properties.contains("property1"));
        assertTrue(properties.contains("property2"));
    }

    @Test
    public void testGetPropertiesWithDefaultOption() {
        commandLine.addProperty(new PropertyOption(), "property1", "value1");

        Set<String> properties = commandLine.getProperties();
        assertEquals(1, properties.size());
        assertTrue(properties.contains("property1"));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("--option"));
        assertTrue(commandLine.looksLikeOption("-o"));
        assertFalse(commandLine.looksLikeOption("arg"));
    }

    @Test
    public void testToString() {
        String expected = "arg1 arg2";
        assertEquals(expected, commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        List<Option> options = commandLine.getOptions();
        assertEquals(1, options.size());

        Option rootOption = options.get(0);
        assertEquals("root", rootOption.getPreferredName());
    }

    @Test
    public void testGetOptionTriggers() {
        Set<String> triggers = commandLine.getOptionTriggers();
        assertEquals(1, triggers.size());
        assertTrue(triggers.contains("root"));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new OptionBuilder("test")
                .withDescription("Test option")
                .create();

        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default");

        commandLine.setDefaultValues(option, defaultValues);

        List<String> values = commandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals("default", values.get(0));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new OptionBuilder("test")
                .withDescription("Test option")
                .create();

        commandLine.setDefaultSwitch(option, true);

        assertTrue(commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetNormalised() {
        List<String> normalised = commandLine.getNormalised();
        assertEquals(2, normalised.size());
        assertEquals("arg1", normalised.get(0));
        assertEquals("arg2", normalised.get(1));
    }
}
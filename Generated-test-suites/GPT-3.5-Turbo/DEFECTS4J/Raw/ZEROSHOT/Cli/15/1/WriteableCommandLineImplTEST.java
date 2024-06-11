import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        Option option = new Option("test");
        commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
    }

    @Test
    public void testAddOption() {
        Option option = new Option("test");
        commandLine.addOption(option);

        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("test");

        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        assertEquals(2, commandLine.getValues(option, null).size());
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("test");

        commandLine.addSwitch(option, true);

        assertTrue(commandLine.getSwitch(option, null));
    }

    @Test
    public void testHasOption() {
        Option option = new Option("test");
        commandLine.addOption(option);

        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("test");
        commandLine.addOption(option);

        assertEquals(option, commandLine.getOption("test"));
    }

    @Test
    public void testGetValues() {
        Option option = new Option("test");
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        assertEquals(2, commandLine.getValues(option, null).size());
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("test");
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        assertEquals(2, commandLine.getUndefaultedValues(option).size());
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("test");
        commandLine.addSwitch(option, true);

        assertTrue(commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetProperty() {
        String property = "property";
        String value = "value";
        commandLine.addProperty(property, value);

        assertEquals(value, commandLine.getProperty(property));
    }

    @Test
    public void testAddProperty() {
        String property = "property";
        String value = "value";
        commandLine.addProperty(property, value);

        assertEquals(value, commandLine.getProperty(property));
    }

    @Test
    public void testGetPropertyWithDefaultValue() {
        String property = "property";
        String value = "value";
        commandLine.addProperty(property, value);

        assertEquals(value, commandLine.getProperty(property, "default"));
    }

    @Test
    public void testGetProperties() {
        Option option = new Option("test");
        String property = "property";
        String value = "value";
        commandLine.addProperty(option, property, value);

        assertEquals(1, commandLine.getProperties(option).size());
    }

    @Test
    public void testGetPropertiesWithoutOption() {
        String property = "property";
        String value = "value";
        commandLine.addProperty(property, value);

        assertEquals(0, commandLine.getProperties().size());
    }

    @Test
    public void testLooksLikeOption() {
        String trigger = "-test";
        assertTrue(commandLine.looksLikeOption(trigger));
    }

    @Test
    public void testToString() {
        commandLine.getNormalised().add("test1");
        commandLine.getNormalised().add("test2");

        assertEquals("test1 test2", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("test1");
        Option option2 = new Option("test2");
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        assertEquals(2, commandLine.getOptions().size());
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = new Option("test1");
        Option option2 = new Option("test2");
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        assertEquals(2, commandLine.getOptionTriggers().size());
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("test");
        commandLine.setDefaultValues(option, null);

        assertEquals(null, commandLine.getValues(option, null));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("test");
        commandLine.setDefaultSwitch(option, null);

        assertEquals(null, commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetNormalised() {
        List<String> normalised = new ArrayList<>();
        normalised.add("test1");
        normalised.add("test2");
        commandLine.getNormalised().addAll(normalised);

        assertEquals(2, commandLine.getNormalised().size());
    }
}
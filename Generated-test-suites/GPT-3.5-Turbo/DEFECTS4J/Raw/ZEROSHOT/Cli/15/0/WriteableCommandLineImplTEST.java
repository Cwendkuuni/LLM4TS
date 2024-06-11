import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    private Option option;
    private WriteableCommandLineImpl writeableCommandLine;

    @Before
    public void setUp() {
        option = new Option.Builder("testOption")
                .withDescription("Test Option")
                .withArgument(new Argument.Builder("arg")
                        .withDescription("Test Argument")
                        .create())
                .create();
        writeableCommandLine = new WriteableCommandLineImpl(option, null);
    }

    @Test
    public void testAddOption() {
        writeableCommandLine.addOption(option);
        assertTrue(writeableCommandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        String value = "testValue";
        writeableCommandLine.addValue(option, value);
        assertEquals(value, writeableCommandLine.getValues(option, null).get(0));
    }

    @Test
    public void testAddSwitch() {
        boolean value = true;
        writeableCommandLine.addSwitch(option, value);
        assertTrue(writeableCommandLine.getSwitch(option, null));
    }

    @Test
    public void testHasOption() {
        assertFalse(writeableCommandLine.hasOption(option));
        writeableCommandLine.addOption(option);
        assertTrue(writeableCommandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        assertNull(writeableCommandLine.getOption("testTrigger"));
        writeableCommandLine.addOption(option);
        assertNotNull(writeableCommandLine.getOption("testTrigger"));
    }

    @Test
    public void testGetValuesWithDefaultValues() {
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("defaultValue");
        assertEquals(defaultValues, writeableCommandLine.getValues(option, defaultValues));
    }

    @Test
    public void testGetValuesWithoutDefaultValues() {
        assertNull(writeableCommandLine.getValues(option, null));
    }

    @Test
    public void testGetUndefaultedValues() {
        assertNull(writeableCommandLine.getUndefaultedValues(option));
    }

    @Test
    public void testGetSwitch() {
        Boolean defaultValue = true;
        assertNull(writeableCommandLine.getSwitch(option, defaultValue));
        writeableCommandLine.addSwitch(option, true);
        assertTrue(writeableCommandLine.getSwitch(option, defaultValue));
    }

    @Test
    public void testGetProperty() {
        assertNull(writeableCommandLine.getProperty("testProperty"));
    }

    @Test
    public void testAddProperty() {
        String property = "testProperty";
        String value = "testValue";
        writeableCommandLine.addProperty(option, property, value);
        assertEquals(value, writeableCommandLine.getProperty(option, property, null));
    }

    @Test
    public void testGetPropertyWithDefaultValue() {
        String defaultValue = "defaultValue";
        assertEquals(defaultValue, writeableCommandLine.getProperty(option, "testProperty", defaultValue));
    }

    @Test
    public void testGetProperties() {
        assertTrue(writeableCommandLine.getProperties().isEmpty());
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(writeableCommandLine.looksLikeOption("-test"));
        assertFalse(writeableCommandLine.looksLikeOption("test"));
    }

    @Test
    public void testToString() {
        String arg = "testArg";
        writeableCommandLine.addValue(option, arg);
        assertEquals(arg, writeableCommandLine.toString());
    }

    @Test
    public void testGetOptions() {
        assertFalse(writeableCommandLine.getOptions().isEmpty());
    }

    @Test
    public void testGetOptionTriggers() {
        assertFalse(writeableCommandLine.getOptionTriggers().isEmpty());
    }

    @Test
    public void testSetDefaultValues() {
        List<String> defaults = new ArrayList<>();
        defaults.add("defaultValue");
        writeableCommandLine.setDefaultValues(option, defaults);
        assertEquals(defaults, writeableCommandLine.getValues(option, null));
    }

    @Test
    public void testSetDefaultSwitch() {
        Boolean defaultSwitch = true;
        writeableCommandLine.setDefaultSwitch(option, defaultSwitch);
        assertTrue(writeableCommandLine.getSwitch(option, null));
    }

    @Test
    public void testGetNormalised() {
        List<String> normalised = new ArrayList<>();
        normalised.add("testArg");
        writeableCommandLine.addValue(option, "testArg");
        assertEquals(normalised, writeableCommandLine.getNormalised());
    }
}
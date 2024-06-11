import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    private Option option;
    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        option = new OptionBuilder().withName("testOption").create();
        commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
    }

    @Test
    public void testAddOption() {
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        String value = "testValue";
        commandLine.addValue(option, value);
        assertEquals(value, commandLine.getValues(option, null).get(0));
    }

    @Test
    public void testAddSwitch() {
        boolean value = true;
        commandLine.addSwitch(option, value);
        assertEquals(value, commandLine.getSwitch(option, null));
    }

    @Test
    public void testHasOption() {
        assertFalse(commandLine.hasOption(option));
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        assertNull(commandLine.getOption("invalidTrigger"));
        commandLine.addOption(option);
        assertEquals(option, commandLine.getOption(option.getPreferredName()));
    }

    @Test
    public void testGetValues() {
        String value = "testValue";
        commandLine.addValue(option, value);
        assertEquals(value, commandLine.getValues(option, null).get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        String value = "testValue";
        commandLine.addValue(option, value);
        assertEquals(value, commandLine.getUndefaultedValues(option).get(0));
    }

    @Test
    public void testGetSwitch() {
        boolean value = true;
        commandLine.addSwitch(option, value);
        assertEquals(value, commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetProperty() {
        String property = "testProperty";
        String value = "testValue";
        commandLine.addProperty(option, property, value);
        assertEquals(value, commandLine.getProperty(option, property, null));
    }

    @Test
    public void testAddProperty() {
        String property = "testProperty";
        String value = "testValue";
        commandLine.addProperty(option, property, value);
        assertEquals(value, commandLine.getProperty(option, property, null));
    }

    @Test
    public void testGetPropertyWithDefault() {
        String property = "testProperty";
        String defaultValue = "defaultValue";
        assertEquals(defaultValue, commandLine.getProperty(property, defaultValue));
    }

    @Test
    public void testGetProperties() {
        assertEquals(0, commandLine.getProperties(option).size());
        String property = "testProperty";
        String value = "testValue";
        commandLine.addProperty(option, property, value);
        assertEquals(1, commandLine.getProperties(option).size());
    }

    @Test
    public void testGetDefaultValues() {
        assertEquals(0, commandLine.getValues(option, null).size());
        String value = "testValue";
        commandLine.setDefaultValues(option, Collections.singletonList(value));
        assertEquals(value, commandLine.getValues(option, null).get(0));
    }

    @Test
    public void testGetDefaultSwitch() {
        assertNull(commandLine.getSwitch(option, null));
        boolean defaultValue = true;
        commandLine.setDefaultSwitch(option, defaultValue);
        assertEquals(defaultValue, commandLine.getSwitch(option, null));
    }

    @Test
    public void testLooksLikeOption() {
        assertFalse(commandLine.looksLikeOption("invalidTrigger"));
        for (String prefix : option.getPrefixes()) {
            assertTrue(commandLine.looksLikeOption(prefix + "validTrigger"));
        }
    }

    @Test
    public void testToString() {
        String argument = "testArgument";
        commandLine = new WriteableCommandLineImpl(option, Collections.singletonList(argument));
        assertEquals(argument, commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        assertEquals(0, commandLine.getOptions().size());
        commandLine.addOption(option);
        assertEquals(option, commandLine.getOptions().get(0));
    }

    @Test
    public void testGetOptionTriggers() {
        assertEquals(0, commandLine.getOptionTriggers().size());
        commandLine.addOption(option);
        assertEquals(option.getPreferredName(), commandLine.getOptionTriggers().iterator().next());
    }

    @Test
    public void testGetNormalised() {
        assertEquals(0, commandLine.getNormalised().size());
        commandLine = new WriteableCommandLineImpl(option, Collections.singletonList("testArgument"));
        assertEquals(1, commandLine.getNormalised().size());
    }
}
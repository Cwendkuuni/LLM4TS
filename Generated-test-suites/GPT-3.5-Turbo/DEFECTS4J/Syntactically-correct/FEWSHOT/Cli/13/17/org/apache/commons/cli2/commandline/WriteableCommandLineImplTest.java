import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        Option option = new Option("o", "option", true, "Option");
        Argument argument = new Argument("arg", true, "Argument");
        commandLine = new WriteableCommandLineImpl(option, Collections.singletonList(argument));
    }

    @Test
    public void testAddOption() {
        Option option = new Option("t", "test", true, "Test");
        commandLine.addOption(option);

        assertTrue(commandLine.getOptions().contains(option));
        assertNotNull(commandLine.getOption("t"));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("t", "test", true, "Test");
        commandLine.addOption(option);
        commandLine.addValue(option, "value");

        assertTrue(commandLine.getValues(option, null).contains("value"));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("t", "test", false, "Test");
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);

        assertTrue(commandLine.getSwitch(option, null));
    }

    @Test
    public void testHasOption() {
        Option option = new Option("t", "test", true, "Test");
        commandLine.addOption(option);

        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("t", "test", true, "Test");
        commandLine.addOption(option);

        assertEquals(option, commandLine.getOption("t"));
    }

    @Test
    public void testGetValues() {
        Option option = new Option("t", "test", true, "Test");
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List<String> expected = Arrays.asList("value1", "value2");
        assertEquals(expected, commandLine.getValues(option, null));
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("t", "test", false, "Test");
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);

        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty("key", "value");

        assertEquals("value", commandLine.getProperty("key", null));
    }

    @Test
    public void testGetPropertyDefault() {
        assertEquals("default", commandLine.getProperty("key", "default"));
    }

    @Test
    public void testGetPropertyNull() {
        assertNull(commandLine.getProperty("key", null));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-t"));
        assertFalse(commandLine.looksLikeOption("test"));
    }

    @Test
    public void testToString() {
        Option option = new Option("t", "test", true, "Test");
        Argument argument = new Argument("arg", true, "Argument");
        commandLine = new WriteableCommandLineImpl(option, Collections.singletonList(argument));

        commandLine.addValue(option, "value");

        assertEquals("--test value", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        Option option = new Option("t", "test", true, "Test");
        commandLine.addOption(option);

        assertTrue(commandLine.getOptions().contains(option));
    }

    @Test
    public void testGetOptionTriggers() {
        Option option = new Option("t", "test", true, "Test");
        commandLine.addOption(option);

        assertTrue(commandLine.getOptionTriggers().contains("t"));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("t", "test", true, "Test");
        commandLine.addOption(option);

        List<String> defaults = Arrays.asList("default1", "default2");
        commandLine.setDefaultValues(option, defaults);

        assertEquals(defaults, commandLine.getValues(option, null));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("t", "test", false, "Test");
        commandLine.addOption(option);

        commandLine.setDefaultSwitch(option, true);

        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testGetNormalised() {
        Option option = new Option("t", "test", true, "Test");
        Argument argument = new Argument("arg", true, "Argument");
        commandLine = new WriteableCommandLineImpl(option, Collections.singletonList(argument));

        List<String> expected = Collections.singletonList("arg");
        assertEquals(expected, commandLine.getNormalised());
    }
}
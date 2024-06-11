import org.junit.Test;

import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new Option("o");
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new Option("o");
        commandLine.addValue(option, "value");
        assertEquals("value", commandLine.getValues(option, null).get(0));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new Option("o");
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, null));
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new Option("o");
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
        assertFalse(commandLine.hasOption(new Option("x")));
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option1 = new Option("o");
        Option option2 = new Option("x");
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        assertEquals(option1, commandLine.getOption("o"));
        assertEquals(option2, commandLine.getOption("x"));
        assertNull(commandLine.getOption("y"));
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new Option("o");
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        assertEquals("value1", commandLine.getValues(option, null).get(0));
        assertEquals("value2", commandLine.getValues(option, null).get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new Option("o");
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        assertEquals("value1", commandLine.getUndefaultedValues(option).get(0));
        assertEquals("value2", commandLine.getUndefaultedValues(option).get(1));
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new Option("o");
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        assertEquals("value", commandLine.getProperty("property"));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addProperty("property", "value");
        assertEquals("value", commandLine.getProperty("property"));
    }

    @Test
    public void testGetPropertyWithDefault() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        assertEquals("default", commandLine.getProperty("property", "default"));
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        assertEquals(0, commandLine.getProperties().size());
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        assertTrue(commandLine.looksLikeOption("-o"));
        assertTrue(commandLine.looksLikeOption("--option"));
        assertFalse(commandLine.looksLikeOption("value"));
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addValue(null, "value");
        assertEquals("value", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option1 = new Option("o");
        Option option2 = new Option("x");
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        assertEquals(2, commandLine.getOptions().size());
        assertTrue(commandLine.getOptions().contains(option1));
        assertTrue(commandLine.getOptions().contains(option2));
    }

    @Test
    public void testGetOptionTriggers() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option1 = new Option("o");
        Option option2 = new Option("x");
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        assertEquals(2, commandLine.getOptionTriggers().size());
        assertTrue(commandLine.getOptionTriggers().contains("o"));
        assertTrue(commandLine.getOptionTriggers().contains("x"));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new Option("o");
        commandLine.setDefaultValues(option, Collections.singletonList("default"));
        assertEquals("default", commandLine.getValues(option, null).get(0));
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new Option("o");
        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetNormalised() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        List<String> arguments = Collections.singletonList("arg1");
        assertEquals(arguments, commandLine.getNormalised());
    }
}
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;
    private Option option1;
    private Option option2;
    private Argument argument1;
    private Argument argument2;

    @Before
    public void setUp() {
        option1 = new Option("o1");
        option2 = new Option("o2");
        argument1 = new Argument("arg1");
        argument2 = new Argument("arg2");
        commandLine = new WriteableCommandLineImpl(option1, Collections.emptyList());
    }

    @Test
    public void testAddOption() {
        commandLine.addOption(option1);
        assertTrue(commandLine.hasOption(option1));
    }

    @Test
    public void testAddValue() {
        commandLine.addValue(argument1, "value1");
        List<String> values = commandLine.getValues(argument1, Collections.emptyList());
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        commandLine.addSwitch(option1, true);
        assertTrue(commandLine.getSwitch(option1, false));
    }

    @Test
    public void testHasOption() {
        assertFalse(commandLine.hasOption(option1));
        commandLine.addOption(option1);
        assertTrue(commandLine.hasOption(option1));
    }

    @Test
    public void testGetOption() {
        assertNull(commandLine.getOption("trigger1"));
        commandLine.addOption(option1);
        assertEquals(option1, commandLine.getOption("o1"));
    }

    @Test
    public void testGetValues() {
        commandLine.addValue(argument1, "value1");
        List<String> values = commandLine.getValues(argument1, Collections.singletonList("default"));
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));

        values = commandLine.getValues(argument2, Collections.singletonList("default"));
        assertEquals(1, values.size());
        assertEquals("default", values.get(0));

        commandLine.setDefaultValues(argument1, Collections.singletonList("default1"));
        commandLine.setDefaultValues(argument2, Collections.singletonList("default2"));

        values = commandLine.getValues(argument1, Collections.emptyList());
        assertEquals(1, values.size());
        assertEquals("default1", values.get(0));

        values = commandLine.getValues(argument2, Collections.emptyList());
        assertEquals(1, values.size());
        assertEquals("default2", values.get(0));
    }

    @Test
    public void testGetSwitch() {
        assertNull(commandLine.getSwitch(option1, null));
        commandLine.addSwitch(option1, true);
        assertTrue(commandLine.getSwitch(option1, false));

        commandLine.setDefaultSwitch(option1, true);
        assertTrue(commandLine.getSwitch(option1, false));

        commandLine.setDefaultSwitch(option1, null);
        assertFalse(commandLine.getSwitch(option1, false));
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty("prop1", "value1");
        assertEquals("value1", commandLine.getProperty("prop1", "default"));
    }

    @Test
    public void testGetProperty() {
        commandLine.addProperty("prop1", "value1");
        assertEquals("value1", commandLine.getProperty("prop1", "default"));
        assertEquals("default", commandLine.getProperty("prop2", "default"));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-o1"));
        assertFalse(commandLine.looksLikeOption("value"));
    }

    @Test
    public void testToString() {
        commandLine.addValue(argument1, "value1");
        commandLine.addValue(argument2, "value2");

        assertEquals("value1 value2", commandLine.toString());

        commandLine.addValue(argument1, "value with space");

        assertEquals("\"value with space\" value2", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        List<Option> options = commandLine.getOptions();

        assertEquals(2, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test
    public void testGetOptionTriggers() {
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        Set<String> triggers = commandLine.getOptionTriggers();

        assertEquals(2, triggers.size());
        assertTrue(triggers.contains("o1"));
        assertTrue(triggers.contains("o2"));
    }

    @Test
    public void testSetDefaultValues() {
        commandLine.setDefaultValues(argument1, Collections.singletonList("default1"));

        List<String> values = commandLine.getValues(argument1, Collections.emptyList());
        assertEquals(1, values.size());
        assertEquals("default1", values.get(0));

        commandLine.setDefaultValues(argument1, null);

        values = commandLine.getValues(argument1, Collections.emptyList());
        assertEquals(0, values.size());
    }

    @Test
    public void testSetDefaultSwitch() {
        commandLine.setDefaultSwitch(option1, true);

        assertTrue(commandLine.getSwitch(option1, false));

        commandLine.setDefaultSwitch(option1, null);

        assertFalse(commandLine.getSwitch(option1, false));
    }

    @Test
    public void testGetNormalised() {
        commandLine.addValue(argument1, "value1");
        commandLine.addValue(argument2, "value2");

        List<String> normalised = commandLine.getNormalised();

        assertEquals(2, normalised.size());
        assertTrue(normalised.contains("value1"));
        assertTrue(normalised.contains("value2"));
    }
}
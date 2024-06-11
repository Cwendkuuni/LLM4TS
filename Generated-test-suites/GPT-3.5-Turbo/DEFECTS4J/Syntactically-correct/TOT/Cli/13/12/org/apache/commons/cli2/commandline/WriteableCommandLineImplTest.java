import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        Option option = new OptionBuilder().withPreferredName("option").create();
        commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
    }

    @Test
    public void testAddOption() {
        Option option = new OptionBuilder().withPreferredName("option").create();
        commandLine.addOption(option);

        assertTrue(commandLine.hasOption(option));
        assertNotNull(commandLine.getOption("option"));
        assertEquals(option, commandLine.getOption("option"));
    }

    @Test
    public void testAddValue() {
        Option argument = new ArgumentBuilder().create();
        Object value = "value";
        commandLine.addValue(argument, value);

        assertTrue(commandLine.hasOption(argument));
        assertNotNull(commandLine.getValues(argument, null));
        assertEquals(1, commandLine.getValues(argument, null).size());
        assertEquals(value, commandLine.getValues(argument, null).get(0));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitch_whenSwitchAlreadySet() {
        Option option = new OptionBuilder().create();
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }

    @Test
    public void testAddSwitch() {
        Option option = new OptionBuilder().create();
        commandLine.addSwitch(option, true);

        assertTrue(commandLine.hasOption(option));
        assertNotNull(commandLine.getSwitch(option, null));
        assertEquals(Boolean.TRUE, commandLine.getSwitch(option, null));
    }

    @Test
    public void testHasOption() {
        Option option = new OptionBuilder().create();
        assertFalse(commandLine.hasOption(option));

        commandLine.addOption(option);

        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new OptionBuilder().withPreferredName("option").create();
        assertNull(commandLine.getOption("option"));

        commandLine.addOption(option);

        assertNotNull(commandLine.getOption("option"));
        assertEquals(option, commandLine.getOption("option"));
    }

    @Test
    public void testGetValues() {
        Option argument = new ArgumentBuilder().create();
        Option option = new OptionBuilder().create();

        assertNull(commandLine.getValues(option, null));

        commandLine.addOption(option);
        commandLine.addValue(argument, "value1");
        commandLine.addValue(argument, "value2");

        assertEquals(2, commandLine.getValues(argument, null).size());
        assertTrue(commandLine.getValues(argument, null).contains("value1"));
        assertTrue(commandLine.getValues(argument, null).contains("value2"));
    }

    @Test
    public void testGetSwitch() {
        Option option = new OptionBuilder().create();

        assertNull(commandLine.getSwitch(option, null));

        commandLine.addSwitch(option, true);

        assertNotNull(commandLine.getSwitch(option, null));
        assertEquals(Boolean.TRUE, commandLine.getSwitch(option, null));
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty("property", "value");

        assertNotNull(commandLine.getProperty("property", null));
        assertEquals("value", commandLine.getProperty("property", null));
    }

    @Test
    public void testGetProperty() {
        assertNull(commandLine.getProperty("property", null));

        commandLine.addProperty("property", "value");

        assertEquals("value", commandLine.getProperty("property", null));
    }

    @Test
    public void testLooksLikeOption() {
        assertFalse(commandLine.looksLikeOption("trigger"));

        Option option = new OptionBuilder().withPrefixes("-").create();
        commandLine.addOption(option);

        assertTrue(commandLine.looksLikeOption("-trigger"));
    }

    @Test
    public void testToString() {
        assertEquals("", commandLine.toString());

        Option argument = new ArgumentBuilder().create();
        commandLine.addValue(argument, "value");

        assertEquals("value", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        Option option1 = new OptionBuilder().create();
        Option option2 = new OptionBuilder().create();

        commandLine.addOption(option1);
        commandLine.addOption(option2);

        assertFalse(commandLine.getOptions().isEmpty());
        assertEquals(2, commandLine.getOptions().size());
        assertTrue(commandLine.getOptions().contains(option1));
        assertTrue(commandLine.getOptions().contains(option2));
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = new OptionBuilder().withPreferredName("option1").create();
        Option option2 = new OptionBuilder().withPreferredName("option2").create();

        commandLine.addOption(option1);
        commandLine.addOption(option2);

        assertFalse(commandLine.getOptionTriggers().isEmpty());
        assertEquals(2, commandLine.getOptionTriggers().size());
        assertTrue(commandLine.getOptionTriggers().contains("option1"));
        assertTrue(commandLine.getOptionTriggers().contains("option2"));
    }

    @Test
    public void testSetDefaultValues() {
        Option argument = new ArgumentBuilder().create();
        Option option = new OptionBuilder().create();
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        defaultValues.add("default2");

        commandLine.setDefaultValues(option, defaultValues);

        assertEquals(defaultValues, commandLine.getValues(option, null));

        commandLine.addValue(argument, "value");

        assertEquals(defaultValues, commandLine.getValues(option, null));

        commandLine.setDefaultValues(option, null);

        assertNull(commandLine.getValues(option, null));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new OptionBuilder().create();
        commandLine.setDefaultSwitch(option, true);

        assertEquals(Boolean.TRUE, commandLine.getSwitch(option, null));

        commandLine.addSwitch(option, false);

        assertEquals(Boolean.TRUE, commandLine.getSwitch(option, null));

        commandLine.setDefaultSwitch(option, null);

        assertNull(commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetNormalised() {
        Option option = new OptionBuilder().create();
        commandLine.addOption(option);
        Option argument = new ArgumentBuilder().create();
        commandLine.addValue(argument, "value");

        assertFalse(commandLine.getNormalised().isEmpty());
        assertEquals(1, commandLine.getNormalised().size());
    }
}
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        Option rootOption = new Option("root", "Root option");
        List arguments = new ArrayList();
        commandLine = new WriteableCommandLineImpl(rootOption, arguments);
    }

    @Test
    public void testAddOption() {
        Option option1 = new Option("option1", "Option 1");
        Option option2 = new Option("option2", "Option 2");

        commandLine.addOption(option1);
        commandLine.addOption(option2);

        assertTrue(commandLine.hasOption(option1));
        assertTrue(commandLine.hasOption(option2));
    }

    @Test
    public void testAddValue() {
        Option option1 = new Option("option1", "Option 1");

        commandLine.addValue(option1, "value1");
        commandLine.addValue(option1, "value2");

        assertEquals(2, commandLine.getValues(option1, null).size());
        assertEquals("value1", commandLine.getValues(option1, null).get(0));
        assertEquals("value2", commandLine.getValues(option1, null).get(1));
    }

    @Test
    public void testAddSwitch() {
        Option option1 = new Option("option1", "Option 1");
        Option option2 = new Option("option2", "Option 2");

        commandLine.addSwitch(option1, true);
        commandLine.addSwitch(option2, false);

        assertTrue(commandLine.getSwitch(option1, null));
        assertFalse(commandLine.getSwitch(option2, null));
    }

    @Test
    public void testGetProperty() {
        commandLine.addProperty("property1", "value1");
        commandLine.addProperty("property2", "value2");

        assertEquals("value1", commandLine.getProperty("property1", null));
        assertEquals("value2", commandLine.getProperty("property2", null));
    }

    @Test
    public void testLooksLikeOption() {
        commandLine.addOption(new Option("-"));
        commandLine.addOption(new Option("--"));

        assertTrue(commandLine.looksLikeOption("-a"));
        assertTrue(commandLine.looksLikeOption("--help"));
        assertFalse(commandLine.looksLikeOption("command"));
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("option1", "Option 1");
        Option option2 = new Option("option2", "Option 2");

        commandLine.addOption(option1);
        commandLine.addOption(option2);

        assertEquals(2, commandLine.getOptions().size());
        assertTrue(commandLine.getOptions().contains(option1));
        assertTrue(commandLine.getOptions().contains(option2));
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = new Option("option1", "Option 1");
        Option option2 = new Option("option2", "Option 2");

        commandLine.addOption(option1);
        commandLine.addOption(option2);

        assertEquals(2, commandLine.getOptionTriggers().size());
        assertTrue(commandLine.getOptionTriggers().contains("option1"));
        assertTrue(commandLine.getOptionTriggers().contains("option2"));
    }

    @Test
    public void testSetDefaultValues() {
        Option option1 = new Option("option1", "Option 1");

        commandLine.setDefaultValues(option1, null);

        assertNull(commandLine.getValues(option1, null));

        List defaults = new ArrayList();
        defaults.add("default1");
        defaults.add("default2");

        commandLine.setDefaultValues(option1, defaults);

        assertEquals(2, commandLine.getValues(option1, null).size());
        assertEquals("default1", commandLine.getValues(option1, null).get(0));
        assertEquals("default2", commandLine.getValues(option1, null).get(1));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option1 = new Option("option1", "Option 1");

        commandLine.setDefaultSwitch(option1, null);

        assertNull(commandLine.getSwitch(option1, null));

        commandLine.setDefaultSwitch(option1, true);

        assertTrue(commandLine.getSwitch(option1, null));
    }

    @Test
    public void testGetNormalised() {
        List arguments1 = new ArrayList();
        arguments1.add("arg1");
        arguments1.add("arg2");

        commandLine = new WriteableCommandLineImpl(new Option("root1"), arguments1);

        assertEquals(2, commandLine.getNormalised().size());
        assertEquals("arg1", commandLine.getNormalised().get(0));
        assertEquals("arg2", commandLine.getNormalised().get(1));

        List arguments2 = new ArrayList();
        arguments2.add("arg3 arg4");

        commandLine = new WriteableCommandLineImpl(new Option("root2"), arguments2);

        assertEquals(1, commandLine.getNormalised().size());
        assertEquals("arg3 arg4", commandLine.getNormalised().get(0));
    }
}
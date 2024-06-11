import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        Option option1 = new OptionBuilder().withName("option1").create();
        Option option2 = new OptionBuilder().withName("option2").create();

        List<Argument> arguments = new ArrayList<>();
        arguments.add(option1);
        arguments.add(option2);

        commandLine = new WriteableCommandLineImpl(option1, arguments);
    }

    @Test
    public void testAddOption() {
        Option option3 = new OptionBuilder().withName("option3").create();
        commandLine.addOption(option3);

        assertTrue(commandLine.hasOption(option3));
        assertEquals(option3, commandLine.getOption("option3"));
    }

    @Test
    public void testAddValue() {
        Argument argument = new ArgumentBuilder().withName("argument").create();
        commandLine.addValue(argument, "value");

        assertTrue(commandLine.hasOption(argument));
        List<String> values = commandLine.getValues(argument, null);
        assertEquals(1, values.size());
        assertEquals("value", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = new OptionBuilder().withName("option").withSwitch(true).create();
        commandLine.addSwitch(option, true);

        assertTrue(commandLine.hasOption(option));
        assertEquals(Boolean.TRUE, commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetProperty() {
        Option option = new OptionBuilder().withName("option").withPropertyOption(true).create();
        commandLine.addProperty(option, "property", "value");

        String propertyValue = commandLine.getProperty(option, "property", null);
        assertEquals("value", propertyValue);
    }

    @Test
    public void testLookupLikeOption() {
        assertTrue(commandLine.looksLikeOption("-option1"));
        assertFalse(commandLine.looksLikeOption("value"));
    }

    @Test
    public void testToString() {
        String expected = "option1 option2";
        assertEquals(expected, commandLine.toString());
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new OptionBuilder().withName("option").create();
        List<String> defaults = new ArrayList<>();
        defaults.add("default1");
        defaults.add("default2");

        commandLine.setDefaultValues(option, defaults);
        List<String> values = commandLine.getValues(option, null);

        assertEquals(defaults, values);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new OptionBuilder().withName("option").withSwitch(true).create();
        Boolean defaultSwitch = true;

        commandLine.setDefaultSwitch(option, defaultSwitch);
        Boolean switchValue = commandLine.getSwitch(option, null);

        assertEquals(defaultSwitch, switchValue);
    }

    @Test
    public void testGetNormalised() {
        List<String> expected = new ArrayList<>();
        expected.add("option1");
        expected.add("option2");

        List<String> normalised = commandLine.getNormalised();

        assertEquals(expected, normalised);
    }
}
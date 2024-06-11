import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {
    private WriteableCommandLineImpl commandLine;
    private Option option1;
    private Option option2;
    private Option option3;

    @Before
    public void setUp() {
        option1 = new OptionBuilder().withName("option1").withDescription("Option 1").create();
        option1.setTriggers(Arrays.asList("o1", "opt1", "op1"));

        option2 = new OptionBuilder().withName("option2").withDescription("Option 2").create();
        option2.setTriggers(Arrays.asList("o2", "opt2", "op2"));

        option3 = new OptionBuilder().withName("option3").withDescription("Option 3").create();
        option3.setTriggers(Arrays.asList("o3", "opt3", "op3"));

        List<String> arguments = new ArrayList<>();
        arguments.add("arg1");
        arguments.add("arg2");

        commandLine = new WriteableCommandLineImpl(option1, arguments);
    }

    @Test
    public void testAddOption() {
        assertFalse(commandLine.hasOption(option2));

        commandLine.addOption(option2);

        assertTrue(commandLine.hasOption(option2));
        assertEquals(option2, commandLine.getOption("opt2"));
    }

    @Test
    public void testAddValue() {
        assertEquals(Collections.emptyList(), commandLine.getValues(option1, null));

        commandLine.addValue(option1, "value1");
        commandLine.addValue(option1, "value2");

        List<String> expectedValues = Arrays.asList("value1", "value2");

        assertEquals(expectedValues, commandLine.getValues(option1, null));
    }

    @Test
    public void testAddSwitch() {
        assertNull(commandLine.getSwitch(option1, null));

        commandLine.addSwitch(option1, true);

        assertTrue(commandLine.getSwitch(option1, false));

        commandLine.addSwitch(option1, false);

        assertFalse(commandLine.getSwitch(option1, true));
    }

    @Test
    public void testGetProperty() {
        assertNull(commandLine.getProperty("property"));

        commandLine.addProperty("property", "value");

        assertEquals("value", commandLine.getProperty("property"));
    }

    @Test
    public void testAddProperty() {
        assertFalse(commandLine.getProperties().contains("property"));

        commandLine.addProperty(option1, "property", "value");

        assertTrue(commandLine.getProperties().contains("property"));

        assertEquals("value", commandLine.getProperty(option1, "property", null));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("opt1"));
        assertTrue(commandLine.looksLikeOption("-opt1"));

        assertFalse(commandLine.looksLikeOption("arg1"));
        assertFalse(commandLine.looksLikeOption("option1"));
    }

    @Test
    public void testToString() {
        assertEquals("arg1 arg2", commandLine.toString());

        List<String> arguments = new ArrayList<>();
        arguments.add("arg with space");

        commandLine = new WriteableCommandLineImpl(option1, arguments);

        assertEquals("\"arg with space\"", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        assertEquals(Arrays.asList(option1), commandLine.getOptions());

        commandLine.addOption(option2);
        commandLine.addOption(option3);

        List<Option> expectedOptions = Arrays.asList(option1, option2, option3);

        assertEquals(expectedOptions, commandLine.getOptions());
    }

    @Test
    public void testGetOptionTriggers() {
        assertEquals(Arrays.asList("opt1", "op1", "o1"), commandLine.getOptionTriggers());

        commandLine.addOption(option2);
        commandLine.addOption(option3);

        List<String> expectedTriggers = Arrays.asList("opt1", "op1", "o1", "opt2", "op2", "o2", "opt3", "op3", "o3");

        assertEquals(expectedTriggers, commandLine.getOptionTriggers());
    }

    @Test
    public void testSetDefaultValues() {
        commandLine.addValue(option1, "value1");

        assertEquals(Arrays.asList("value1"), commandLine.getValues(option1, null));

        List<String> defaultValues = Arrays.asList("default1", "default2");
        commandLine.setDefaultValues(option1, defaultValues);

        assertEquals(defaultValues, commandLine.getValues(option1, null));
    }

    @Test
    public void testSetDefaultSwitch() {
        commandLine.addSwitch(option1, true);

        assertTrue(commandLine.getSwitch(option1, false));

        commandLine.setDefaultSwitch(option1, false);

        assertFalse(commandLine.getSwitch(option1, true));
    }

    @Test
    public void testGetNormalised() {
        assertEquals(Arrays.asList("arg1", "arg2"), commandLine.getNormalised());

        List<String> arguments = new ArrayList<>();
        arguments.add("arg with space");

        commandLine = new WriteableCommandLineImpl(option1, arguments);

        assertEquals(Arrays.asList("arg with space"), commandLine.getNormalised());
    }
}
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLineImpl;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;

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
        option1 = new Argument("option1");
        option2 = new Argument("option2");
        option3 = new Argument("option3");
        List<Option> options = Arrays.asList(option1, option2, option3);
        List<String> arguments = new ArrayList<>();
        commandLine = new WriteableCommandLineImpl(option1, arguments);
        for (Option option : options) {
            commandLine.addOption(option);
        }
    }

    @Test
    public void testAddOption() {
        assertTrue(commandLine.hasOption(option1));
        assertTrue(commandLine.hasOption(option2));
        assertTrue(commandLine.hasOption(option3));
    }

    @Test
    public void testAddValue() {
        String value1 = "value1";
        String value2 = "value2";
        String value3 = "value3";

        commandLine.addValue(option1, value1);
        commandLine.addValue(option1, value2);
        commandLine.addValue(option2, value3);

        List<String> values = commandLine.getValues(option1, null);
        assertEquals(2, values.size());
        assertTrue(values.contains(value1));
        assertTrue(values.contains(value2));

        values = commandLine.getValues(option2, null);
        assertEquals(1, values.size());
        assertTrue(values.contains(value3));
    }

    @Test
    public void testAddSwitch() {
        commandLine.addSwitch(option1, true);
        commandLine.addSwitch(option2, false);

        assertTrue(commandLine.getSwitch(option1, null));
        assertFalse(commandLine.getSwitch(option2, null));
    }

    @Test
    public void testHasOption() {
        assertTrue(commandLine.hasOption(option1));
        assertFalse(commandLine.hasOption(new Argument("option4")));
    }

    @Test
    public void testGetOption() {
        assertSame(option1, commandLine.getOption(option1.getPreferredName()));
        assertSame(option2, commandLine.getOption(option2.getPreferredName()));
        assertNull(commandLine.getOption("option4"));
    }

    @Test
    public void testGetValues() {
        String value1 = "value1";
        String value2 = "value2";
        List<String> defaultValues = Arrays.asList("defaultValue1", "defaultValue2");

        commandLine.addValue(option1, value1);
        commandLine.addValue(option1, value2);

        List<String> values = commandLine.getValues(option1, defaultValues);
        assertEquals(2, values.size());
        assertTrue(values.contains(value1));
        assertTrue(values.contains(value2));

        values = commandLine.getValues(option2, defaultValues);
        assertEquals(2, values.size());
        assertTrue(values.containsAll(defaultValues));

        values = commandLine.getValues(option3, defaultValues);
        assertEquals(2, values.size());
        assertTrue(values.containsAll(defaultValues));
    }

    @Test
    public void testGetSwitch() {
        commandLine.addSwitch(option1, true);
        commandLine.addSwitch(option2, false);

        assertTrue(commandLine.getSwitch(option1, false));
        assertFalse(commandLine.getSwitch(option2, true));
        assertNull(commandLine.getSwitch(option3, null));
    }

    @Test
    public void testAddProperty() {
        String property1 = "property1";
        String value1 = "value1";

        commandLine.addProperty(property1, value1);

        assertEquals(value1, commandLine.getProperty(property1, null));
        assertNull(commandLine.getProperty("property2", null));
    }

    @Test
    public void testGetProperty() {
        String property1 = "property1";
        String value1 = "value1";
        String defaultValue = "defaultValue";

        commandLine.addProperty(property1, value1);

        assertEquals(value1, commandLine.getProperty(property1, defaultValue));
        assertEquals(defaultValue, commandLine.getProperty("property2", defaultValue));
    }

    @Test
    public void testGetProperties() {
        String property1 = "property1";
        String value1 = "value1";
        String property2 = "property2";
        String value2 = "value2";

        commandLine.addProperty(property1, value1);
        commandLine.addProperty(property2, value2);

        assertEquals(2, commandLine.getProperties().size());
        assertTrue(commandLine.getProperties().contains(property1));
        assertTrue(commandLine.getProperties().contains(property2));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-option1"));
        assertTrue(commandLine.looksLikeOption("--option2"));
        assertFalse(commandLine.looksLikeOption("argument"));
    }

    @Test
    public void testToString() {
        String argument1 = "argument1";
        String argument2 = "argument2";
        List<String> arguments = Arrays.asList(argument1, argument2);

        commandLine = new WriteableCommandLineImpl(option1, arguments);

        assertEquals(argument1 + " " + argument2, commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        List<Option> options = commandLine.getOptions();

        assertEquals(3, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
        assertTrue(options.contains(option3));
    }

    @Test
    public void testGetOptionTriggers() {
        assertEquals(3, commandLine.getOptionTriggers().size());
        assertTrue(commandLine.getOptionTriggers().contains(option1.getPreferredName()));
        assertTrue(commandLine.getOptionTriggers().contains(option2.getPreferredName()));
        assertTrue(commandLine.getOptionTriggers().contains(option3.getPreferredName()));
    }

    @Test
    public void testSetDefaultValues() {
        List<String> defaults = Arrays.asList("defaultValue1", "defaultValue2");

        commandLine.setDefaultValues(option1, defaults);

        List<String> values = commandLine.getValues(option1, null);
        assertEquals(2, values.size());
        assertTrue(values.containsAll(defaults));

        commandLine.setDefaultValues(option1, null);

        values = commandLine.getValues(option1, null);
        assertEquals(0, values.size());
    }

    @Test
    public void testSetDefaultSwitch() {
        commandLine.setDefaultSwitch(option1, true);
        commandLine.setDefaultSwitch(option2, false);

        assertTrue(commandLine.getSwitch(option1, null));
        assertFalse(commandLine.getSwitch(option2, null));

        commandLine.setDefaultSwitch(option2, null);

        assertNull(commandLine.getSwitch(option2, null));
    }

    @Test
    public void testGetNormalised() {
        String argument1 = "argument1";
        String argument2 = "argument2";
        List<String> arguments = Arrays.asList(argument1, argument2);

        commandLine = new WriteableCommandLineImpl(option1, arguments);

        List<String> normalised = commandLine.getNormalised();
        assertEquals(2, normalised.size());
        assertTrue(normalised.contains(argument1));
        assertTrue(normalised.contains(argument2));
    }
}
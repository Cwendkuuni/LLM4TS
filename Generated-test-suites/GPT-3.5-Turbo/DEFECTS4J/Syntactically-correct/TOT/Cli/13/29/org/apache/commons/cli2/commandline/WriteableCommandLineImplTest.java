import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.DefaultOptionBuilder;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.commandline.WriteableCommandLineImpl;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;
    private Option option1;
    private Option option2;
    private Argument argument1;
    private List<Object> argument1Values;
    private List<String> defaultValues;

    @Before
    public void setUp() {
        option1 = DefaultOptionBuilder.withLongName("option1").create();
        option2 = DefaultOptionBuilder.withLongName("option2").create();
        argument1 = DefaultOptionBuilder.withArgumentName("argument1").create();

        argument1Values = new ArrayList<>();
        argument1Values.add("value1");
        argument1Values.add("value2");

        defaultValues = Arrays.asList("default1", "default2");

        commandLine = new WriteableCommandLineImpl(option1, new ArrayList<>());
    }

    @Test
    public void testAddOption() {
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        assertTrue(commandLine.hasOption(option1));
        assertTrue(commandLine.hasOption(option2));
    }

    @Test
    public void testAddValue() {
        commandLine.addValue(option1, "value1");
        commandLine.addValue(argument1, "value2");

        List<Object> option1Values = commandLine.getValues(option1, null);
        List<Object> argument1Values = commandLine.getValues(argument1,
                new ArrayList<>());

        assertEquals(1, option1Values.size());
        assertEquals(1, argument1Values.size());
        assertEquals("value1", option1Values.get(0));
        assertEquals("value2", argument1Values.get(0));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitch() {
        commandLine.addSwitch(option1, true);
        commandLine.addSwitch(option1, false);
    }

    @Test
    public void testHasOption() {
        commandLine.addOption(option1);
        assertTrue(commandLine.hasOption(option1));
    }

    @Test
    public void testGetOption() {
        commandLine.addOption(option1);
        assertEquals(option1, commandLine.getOption(option1.getPreferredName()));
    }

    @Test
    public void testGetValues() {
        commandLine.addValue(option1, "value1");
        commandLine.addValue(option1, "value2");

        List<Object> values = commandLine.getValues(option1, defaultValues);

        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetSwitch() {
        commandLine.addSwitch(option1, true);

        assertTrue(commandLine.getSwitch(option1, false));
        assertNull(commandLine.getSwitch(option2, false));
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty("property1", "value1");

        assertEquals("value1", commandLine.getProperty("property1", ""));
        assertEquals("", commandLine.getProperty("property2", ""));
    }

    @Test
    public void testLooksLikeOption() {
        option1 = DefaultOptionBuilder.withLongName("option").withRequired(false).create();
        option2 = DefaultOptionBuilder.withLongName("op").withRequired(false).create();
        commandLine = new WriteableCommandLineImpl(option1, new ArrayList<>());

        assertTrue(commandLine.looksLikeOption("--option"));
        assertFalse(commandLine.looksLikeOption("--op"));
        assertFalse(commandLine.looksLikeOption("option"));
    }

    @Test
    public void testToString() {
        commandLine.addValue(option1, "value1");
        commandLine.addValue(option2, "value2");

        String expected = "value1 value2";
        assertEquals(expected, commandLine.toString());
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
        commandLine.addValue(argument1, "value");

        assertEquals(3, commandLine.getOptionTriggers().size());
        assertTrue(commandLine.getOptionTriggers().contains(option1.getPreferredName()));
        assertTrue(commandLine.getOptionTriggers().contains(option2.getPreferredName()));
        assertTrue(commandLine.getOptionTriggers().contains(argument1.getPreferredName()));
    }

    @Test
    public void testSetDefaultValues() {
        commandLine.setDefaultValues(option1, defaultValues);

        List<Object> values = commandLine.getValues(option1, null);

        assertEquals(2, values.size());
        assertEquals("default1", values.get(0));
        assertEquals("default2", values.get(1));
    }

    @Test
    public void testSetDefaultSwitch() {
        commandLine.setDefaultSwitch(option1, false);

        assertFalse(commandLine.getSwitch(option1, true));
    }

    @Test
    public void testGetNormalised() {
        List<String> arguments = Arrays.asList("arg1", "arg2");
        commandLine = new WriteableCommandLineImpl(option1, arguments);

        List<String> normalised = commandLine.getNormalised();

        assertEquals(2, normalised.size());
        assertEquals("arg1", normalised.get(0));
        assertEquals("arg2", normalised.get(1));
    }
}
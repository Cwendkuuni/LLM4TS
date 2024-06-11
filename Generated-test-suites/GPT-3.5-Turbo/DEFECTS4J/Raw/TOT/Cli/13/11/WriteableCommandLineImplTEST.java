import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineImplTest {

    private Option rootOption;
    private List<String> arguments;
    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        rootOption = new OptionBuilder().withPreferredName("root").create();
        arguments = new ArrayList<String>();
        commandLine = new WriteableCommandLineImpl(rootOption, arguments);
    }

    @Test
    public void testAddOption() {
        Option option = new OptionBuilder().withPreferredName("test").create();
        commandLine.addOption(option);

        assertTrue(commandLine.getOptions().contains(option));
    }

    @Test
    public void testAddValue() {
        Option option = new OptionBuilder().withPreferredName("test").create();
        Object value = "value";
        
        commandLine.addValue(option, value);

        assertEquals(value, commandLine.getValues(option, null).get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = new OptionBuilder().withPreferredName("test").create();
        boolean value = true;

        commandLine.addSwitch(option, value);

        assertTrue(commandLine.getSwitch(option, null));
    }

    @Test
    public void testHasOption() {
        Option option = new OptionBuilder().withPreferredName("test").create();
        commandLine.addOption(option);

        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new OptionBuilder().withPreferredName("test").create();
        commandLine.addOption(option);

        assertEquals(option, commandLine.getOption("test"));
    }

    @Test
    public void testGetValues() {
        Option option = new OptionBuilder().withPreferredName("test").create();
        List<Object> defaultValues = new ArrayList<Object>();
        defaultValues.add("default value");

        commandLine.addValue(option, "value");

        assertEquals("value", commandLine.getValues(option, defaultValues).get(0));
    }

    @Test
    public void testGetSwitch() {
        Option option = new OptionBuilder().withPreferredName("test").create();
        Boolean defaultValue = Boolean.TRUE;

        commandLine.addSwitch(option, true);

        assertTrue(commandLine.getSwitch(option, defaultValue));
    }

    @Test
    public void testAddProperty() {
        String property = "test";
        String value = "value";

        commandLine.addProperty(property, value);

        assertEquals(value, commandLine.getProperty(property, null));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-test"));
        assertFalse(commandLine.looksLikeOption("value"));
    }

    @Test
    public void testToString() {
        Option option1 = new OptionBuilder().withPreferredName("option1").create();
        Option option2 = new OptionBuilder().withPreferredName("option2").create();
        commandLine.addValue(option1, "value1");
        commandLine.addValue(option2, "value2");

        String expected = "value1 value2";
        assertEquals(expected, commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        Option option = new OptionBuilder().withPreferredName("test").create();
        commandLine.addOption(option);

        assertTrue(commandLine.getOptions().contains(option));
    }

    @Test
    public void testGetOptionTriggers() {
        Option option = new OptionBuilder().withPreferredName("test").create();
        commandLine.addOption(option);

        assertTrue(commandLine.getOptionTriggers().contains("test"));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new OptionBuilder().withPreferredName("test").create();
        List<Object> defaults = new ArrayList<Object>();
        defaults.add("default value");

        commandLine.setDefaultValues(option, defaults);

        assertEquals(defaults, commandLine.getValues(option, null));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new OptionBuilder().withPreferredName("test").create();
        Boolean defaultSwitch = Boolean.TRUE;

        commandLine.setDefaultSwitch(option, defaultSwitch);

        assertTrue(commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetNormalised() {
        Option option = new OptionBuilder().withPreferredName("test").create();
        arguments.add("arg1");
        arguments.add("arg2");
        commandLine.addValue(option, "value1");

        List<String> expected = new ArrayList<String>();
        expected.add("arg1");
        expected.add("arg2");
        assertEquals(expected, commandLine.getNormalised());
    }
}
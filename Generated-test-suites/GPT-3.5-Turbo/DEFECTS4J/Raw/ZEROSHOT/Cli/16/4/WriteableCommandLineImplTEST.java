import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        Option rootOption = new Option("root");
        List<String> arguments = new ArrayList<>();
        arguments.add("arg1");
        arguments.add("arg2");
        commandLine = new WriteableCommandLineImpl(rootOption, arguments);
    }

    @Test
    public void testAddOption() {
        Option option = new Option("option1");
        commandLine.addOption(option);

        assertTrue(commandLine.getOptions().contains(option));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("option1");
        String value = "value1";
        commandLine.addValue(option, value);

        List<String> values = commandLine.getValues(option, null);
        assertTrue(values.contains(value));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("option1");
        boolean switchValue = true;
        commandLine.addSwitch(option, switchValue);

        Boolean switchResult = commandLine.getSwitch(option, null);
        assertEquals(Boolean.valueOf(switchValue), switchResult);
    }

    @Test
    public void testHasOption() {
        Option option = new Option("option1");
        commandLine.addOption(option);

        assertTrue(commandLine.hasOption(option));
        assertFalse(commandLine.hasOption(new Option("option2")));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("option1");
        commandLine.addOption(option);

        Option resultOption = commandLine.getOption(option.getPreferredName());
        assertEquals(option, resultOption);
        assertNull(commandLine.getOption("option2"));
    }

    @Test
    public void testGetValues() {
        Option option = new Option("option1");
        String value1 = "value1";
        String value2 = "value2";
        commandLine.addValue(option, value1);
        commandLine.addValue(option, value2);

        List<String> values = commandLine.getValues(option, null);
        assertTrue(values.contains(value1));
        assertTrue(values.contains(value2));

        List<String> defaultValue = new ArrayList<>();
        defaultValue.add("defaultValue");
        List<String> result = commandLine.getValues(new Option("option2"), defaultValue);
        assertEquals(defaultValue, result);
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("option1");
        String value = "value1";
        commandLine.addValue(option, value);

        List<String> values = commandLine.getUndefaultedValues(option);
        assertTrue(values.contains(value));

        List<String> result = commandLine.getUndefaultedValues(new Option("option2"));
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("option1");
        boolean switchValue = true;
        commandLine.addSwitch(option, switchValue);

        Boolean switchResult = commandLine.getSwitch(option, null);
        assertEquals(Boolean.valueOf(switchValue), switchResult);

        Boolean result = commandLine.getSwitch(new Option("option2"), Boolean.FALSE);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGetProperty() {
        Option option = new Option("option1");
        String property = "property1";
        String value = "value1";
        commandLine.addProperty(option, property, value);

        String result = commandLine.getProperty(option, property, null);
        assertEquals(value, result);

        String defaultValue = "defaultValue";
        String result2 = commandLine.getProperty(new Option("option2"), property, defaultValue);
        assertEquals(defaultValue, result2);
    }

    @Test
    public void testAddProperty() {
        Option option = new Option("option1");
        String property = "property1";
        String value = "value1";
        commandLine.addProperty(option, property, value);

        String result = commandLine.getProperty(option, property, null);
        assertEquals(value, result);
    }

    @Test
    public void testGetProperties() {
        Option option = new Option("option1");
        String property = "property1";
        String value = "value1";
        commandLine.addProperty(option, property, value);

        assertEquals(1, commandLine.getProperties(option).size());
        assertTrue(commandLine.getProperties(option).contains(property));

        assertEquals(0, commandLine.getProperties(new Option("option2")).size());
    }

    @Test
    public void testLooksLikeOption() {
        String trigger = "-trigger";
        assertTrue(commandLine.looksLikeOption(trigger));
    }

    @Test
    public void testToString() {
        String expected = "arg1 arg2";
        String result = commandLine.toString();
        assertEquals(expected, result);
    }

    @Test
    public void testGetNormalised() {
        Option option = new Option("option1");
        String value = "value1";
        commandLine.addValue(option, value);

        assertEquals(2, commandLine.getNormalised().size());
    }
}
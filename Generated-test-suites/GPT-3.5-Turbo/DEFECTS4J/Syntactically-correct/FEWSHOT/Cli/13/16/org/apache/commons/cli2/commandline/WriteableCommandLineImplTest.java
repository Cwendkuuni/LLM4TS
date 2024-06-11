import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    @Before
    public void setup() {
        Option option = new Option("[OPTION_NAME]", "[DESCRIPTION]");
        Argument argument = new Argument("[ARGUMENT_NAME]", "[DESCRIPTION]");
        commandLine = new WriteableCommandLineImpl(option, Collections.singletonList(argument));
    }

    @Test
    public void testAddOption() {
        Option option = new Option("[OPTION_NAME]", "[DESCRIPTION]");
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("[OPTION_NAME]", "[DESCRIPTION]");
        commandLine.addValue(option, "value");
        List<String> values = commandLine.getValues(option, Collections.emptyList());
        assertEquals(Collections.singletonList("value"), values);
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("[OPTION_NAME]", "[DESCRIPTION]");
        commandLine.addSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, false);
        assertTrue(switchValue);
    }

    @Test
    public void testHasOption() {
        Option option = new Option("[OPTION_NAME]", "[DESCRIPTION]");
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("[OPTION_NAME]", "[DESCRIPTION]");
        commandLine.addOption(option);
        Option result = commandLine.getOption("[OPTION_NAME]");
        assertEquals(option, result);
    }

    @Test
    public void testGetValues() {
        Option option = new Option("[OPTION_NAME]", "[DESCRIPTION]");
        commandLine.addValue(option, "value");
        List<String> values = commandLine.getValues(option, Collections.emptyList());
        assertEquals(Collections.singletonList("value"), values);
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("[OPTION_NAME]", "[DESCRIPTION]");
        commandLine.addSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, false);
        assertTrue(switchValue);
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty("[PROPERTY_NAME]", "[PROPERTY_VALUE]");
        String value = commandLine.getProperty("[PROPERTY_NAME]", "[DEFAULT_VALUE]");
        assertEquals("[PROPERTY_VALUE]", value);
    }

    @Test
    public void testGetProperty() {
        commandLine.addProperty("[PROPERTY_NAME]", "[PROPERTY_VALUE]");
        String value = commandLine.getProperty("[PROPERTY_NAME]", "[DEFAULT_VALUE]");
        assertEquals("[PROPERTY_VALUE]", value);
    }

    @Test
    public void testGetProperties() {
        commandLine.addProperty("[PROPERTY_NAME_1]", "[PROPERTY_VALUE_1]");
        commandLine.addProperty("[PROPERTY_NAME_2]", "[PROPERTY_VALUE_2]");
        Set<String> properties = commandLine.getProperties();
        assertEquals("[PROPERTY_NAME_1]", properties.get(0));
        assertEquals("[PROPERTY_NAME_2]", properties.get(1));
    }

    @Test
    public void testLooksLikeOption() {
        boolean result = commandLine.looksLikeOption("[TRIGGER]");
        assertFalse(result);
    }

    @Test
    public void testToString() {
        commandLine.addOption(new Option("-f", "Flag option"));
        commandLine.addValue(new Argument("arg", "Argument option"), "value");
        String result = commandLine.toString();
        assertEquals("-f arg=value", result);
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("-f", "Flag option");
        Option option2 = new Option("-v", "Verbose option");
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        List<Option> options = commandLine.getOptions();
        assertEquals(option1, options.get(0));
        assertEquals(option2, options.get(1));
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = new Option("-f", "Flag option");
        Option option2 = new Option("-v", "Verbose option");
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        Set<String> triggers = commandLine.getOptionTriggers();
        assertTrue(triggers.contains("-f"));
        assertTrue(triggers.contains("-v"));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("[OPTION_NAME]", "[DESCRIPTION]");
        commandLine.setDefaultValues(option, Collections.singletonList("default"));
        List<String> values = commandLine.getValues(option, Collections.emptyList());
        assertEquals(Collections.singletonList("default"), values);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("[OPTION_NAME]", "[DESCRIPTION]");
        commandLine.setDefaultSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, false);
        assertTrue(switchValue);
    }

    @Test
    public void testGetNormalised() {
        Argument argument = new Argument("[ARGUMENT_NAME]", "[DESCRIPTION]");
        commandLine = new WriteableCommandLineImpl(new Option("[OPTION_NAME]", "[DESCRIPTION]"), Collections.singletonList(argument));
        List<String> values = commandLine.getNormalised();
        assertEquals(Collections.singletonList("[ARGUMENT_NAME]"), values);
    }
}
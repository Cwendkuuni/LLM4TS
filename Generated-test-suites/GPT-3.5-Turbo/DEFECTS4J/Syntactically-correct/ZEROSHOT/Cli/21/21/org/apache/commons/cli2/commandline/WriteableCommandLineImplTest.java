import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        Option option1 = new Option("option1");
        Option option2 = new Option("option2");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, null);
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        assertTrue(commandLine.hasOption(option1));
        assertTrue(commandLine.hasOption(option2));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("option");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        assertEquals(2, commandLine.getValues(option, null).size());
        assertEquals("value1", commandLine.getValues(option, null).get(0));
        assertEquals("value2", commandLine.getValues(option, null).get(1));
    }

    @Test
    public void testAddSwitch() {
        Option option1 = new Option("option1");
        Option option2 = new Option("option2");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, null);
        commandLine.addSwitch(option1, true);
        commandLine.addSwitch(option2, false);

        assertTrue(commandLine.getSwitch(option1, null));
        assertFalse(commandLine.getSwitch(option2, null));
    }

    @Test
    public void testHasOption() {
        Option option1 = new Option("option1");
        Option option2 = new Option("option2");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, null);
        assertFalse(commandLine.hasOption(option2));

        commandLine.addOption(option1);
        assertTrue(commandLine.hasOption(option1));
    }

    @Test
    public void testGetOption() {
        Option option1 = new Option("option1");
        Option option2 = new Option("option2");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, null);
        assertNull(commandLine.getOption("option2"));

        commandLine.addOption(option1);
        assertEquals(option1, commandLine.getOption("option1"));
    }

    @Test
    public void testGetValues() {
        Option option = new Option("option");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        defaultValues.add("default2");
        commandLine.setDefaultValues(option, defaultValues);

        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        assertEquals(2, commandLine.getValues(option, null).size());
        assertEquals("value1", commandLine.getValues(option, null).get(0));
        assertEquals("value2", commandLine.getValues(option, null).get(1));

        assertEquals(2, commandLine.getValues(option, defaultValues).size());
        assertEquals("value1", commandLine.getValues(option, defaultValues).get(0));
        assertEquals("value2", commandLine.getValues(option, defaultValues).get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("option");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        assertEquals(2, commandLine.getUndefaultedValues(option).size());
        assertEquals("value1", commandLine.getUndefaultedValues(option).get(0));
        assertEquals("value2", commandLine.getUndefaultedValues(option).get(1));
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("option");
        Boolean defaultSwitch = true;

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.setDefaultSwitch(option, defaultSwitch);

        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, null));

        commandLine.addSwitch(option, false);
        assertFalse(commandLine.getSwitch(option, null));

        commandLine.addSwitch(option, true);
        assertFalse(commandLine.getSwitch(option, defaultSwitch));
    }

    @Test
    public void testGetProperty() {
        Option option = new Option("option");
        String property = "property";
        String value = "value";

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        assertEquals(value, commandLine.getProperty(property));

        commandLine.addProperty(option, property, value);
        assertEquals(value, commandLine.getProperty(property));
    }

    @Test
    public void testAddProperty() {
        Option option = new Option("option");
        String property1 = "property1";
        String value1 = "value1";
        String property2 = "property2";
        String value2 = "value2";

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addProperty(option, property1, value1);
        commandLine.addProperty(option, property2, value2);

        assertEquals(value1, commandLine.getProperty(option, property1, null));
        assertEquals(value2, commandLine.getProperty(option, property2, null));
    }

    @Test
    public void testGetProperties() {
        Option option = new Option("option");
        String property1 = "property1";
        String value1 = "value1";
        String property2 = "property2";
        String value2 = "value2";

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        assertTrue(commandLine.getProperties().isEmpty());

        commandLine.addProperty(option, property1, value1);
        assertTrue(commandLine.getProperties().contains(property1));
        assertFalse(commandLine.getProperties().contains(property2));

        commandLine.addProperty(option, property2, value2);
        assertTrue(commandLine.getProperties().contains(property1));
        assertTrue(commandLine.getProperties().contains(property2));
    }

    @Test
    public void testLooksLikeOption() {
        Option option = new Option("option");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);

        assertTrue(commandLine.looksLikeOption("-option"));
        assertTrue(commandLine.looksLikeOption("--option"));
        assertFalse(commandLine.looksLikeOption("value"));
    }

    @Test
    public void testToString() {
        Option option = new Option("option");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);

        assertNull(commandLine.toString());

        List<String> arguments = new ArrayList<>();
        arguments.add("arg1");
        arguments.add("arg2");
        commandLine = new WriteableCommandLineImpl(option, arguments);

        assertEquals("arg1 arg2", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("option1");
        Option option2 = new Option("option2");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, null);
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        assertEquals(2, commandLine.getOptions().size());
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = new Option("option1");
        Option option2 = new Option("option2");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, null);
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        assertEquals(2, commandLine.getOptionTriggers().size());
        assertTrue(commandLine.getOptionTriggers().contains("option1"));
        assertTrue(commandLine.getOptionTriggers().contains("option2"));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("option");
        List<Object> defaults = new ArrayList<>();
        defaults.add("default1");
        defaults.add("default2");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.setDefCanaultValues(option, defaults);

        assertEquals(defaults, commandLine.getValues(option, null));

        commandLine.setDefaultValues(option, null);
        assertEquals(0, commandLine.getValues(option, null).size());
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("option");
        Boolean defaultSwitch = true;

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.setDefaultSwitch(option, defaultSwitch);

        assertTrue(commandLine.getSwitch(option, null));

        commandLine.setDefaultSwitch(option, null);
        assertTrue(commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetNormalised() {
        Option option = new Option("option");
        List<String> arguments = new ArrayList<>();
        arguments.add("arg1");
        arguments.add("arg2");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, arguments);

        assertEquals(arguments, commandLine.getNormalised());
    }
}
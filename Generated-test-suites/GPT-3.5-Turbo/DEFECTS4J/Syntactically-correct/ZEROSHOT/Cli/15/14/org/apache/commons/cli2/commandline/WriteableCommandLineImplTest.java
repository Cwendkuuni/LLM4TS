import org.junit.Test;

import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        Option option = new Option("testOption");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);

        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        Option argument = new Argument("testArgument");
        Object value = "testValue";
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(argument, new ArrayList<>());
        commandLine.addValue(argument, value);

        assertTrue(commandLine.getValues(argument, null).contains(value));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("testOption");
        boolean value = true;
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addSwitch(option, value);

        assertEquals(Boolean.valueOf(value), commandLine.getSwitch(option, null));
    }

    @Test
    public void testHasOption() {
        Option option = new Option("testOption");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        assertFalse(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("testOption");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        assertEquals(option, commandLine.getOption("testOption"));
    }

    @Test
    public void testGetValues() {
        Option option = new Argument("testArgument");
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("defaultValue");
        List<String> values = new ArrayList<>();
        values.add("testValue");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addValue(option, "testValue");

        assertEquals(values, commandLine.getValues(option, defaultValues));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Argument("testArgument");
        List<String> values = new ArrayList<>();
        values.add("testValue");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addValue(option, "testValue");

        assertEquals(values, commandLine.getUndefaultedValues(option));
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("testOption");
        boolean defaultValue = true;
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addSwitch(option, true);

        assertEquals(Boolean.valueOf(true), commandLine.getSwitch(option, defaultValue));
    }

    @Test
    public void testGetProperty() {
        String property = "testProperty";
        Option option = new PropertyOption();
        String value = "testValue";
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addProperty(option, property, value);

        assertEquals(value, commandLine.getProperty(option, property, null));
    }

    @Test
    public void testAddProperty() {
        String property = "testProperty";
        String value = "testValue";
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new PropertyOption(), new ArrayList<>());
        commandLine.addProperty(property, value);

        assertEquals(value, commandLine.getProperty(property));
    }

    @Test
    public void testGetProperties() {
        String property = "testProperty";
        Option option = new PropertyOption();
        String value = "testValue";
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addProperty(option, property, value);

        assertTrue(commandLine.getProperties().contains(property));
    }

    @Test
    public void testLooksLikeOption() {
        Option option = new Option("testOption");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        assertTrue(commandLine.looksLikeOption("-testOption"));
        assertFalse(commandLine.looksLikeOption("testArgument"));
    }

    @Test
    public void testToString() {
        Option option = new Option("testOption");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addSwitch(option, true);
        commandLine.addValue(option, "testValue");

        assertEquals("-testOption true testValue", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("testOption1");
        Option option2 = new Option("testOption2");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList<>());
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        assertTrue(commandLine.getOptions().contains(option1));
        assertTrue(commandLine.getOptions().contains(option2));
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = new Option("testOption1");
        Option option2 = new Option("testOption2");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList<>());
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        assertTrue(commandLine.getOptionTriggers().contains("testOption1"));
        assertTrue(commandLine.getOptionTriggers().contains("testOption2"));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Argument("testArgument");
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("defaultValue");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        commandLine.setDefaultValues(option, defaultValues);

        assertEquals(defaultValues, commandLine.getValues(option, null));
        commandLine.setDefaultValues(option, null);
        assertEquals(Collections.EMPTY_LIST, commandLine.getValues(option, null));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("testOption");
        boolean defaultSwitchValue = true;
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        commandLine.setDefaultSwitch(option, defaultSwitchValue);

        assertEquals(Boolean.valueOf(defaultSwitchValue), commandLine.getSwitch(option, null));
        commandLine.setDefaultSwitch(option, null);
        assertEquals(null, commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetNormalised() {
        Option option = new Option("testOption");
        List<String> normalised = new ArrayList<>();
        normalised.add("testArgument");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, normalised);

        assertEquals(normalised, commandLine.getNormalised());
    }
}
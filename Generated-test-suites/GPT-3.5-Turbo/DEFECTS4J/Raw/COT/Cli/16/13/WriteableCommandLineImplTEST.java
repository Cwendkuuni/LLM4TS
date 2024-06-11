import org.junit.Test;

import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option();
        cmd.addOption(option);
        assertTrue(cmd.getOptions().contains(option));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option();
        String value = "value";
        cmd.addValue(option, value);
        assertEquals(value, cmd.getValues(option, null).get(0));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option();
        cmd.addSwitch(option, true);
        assertTrue(cmd.getSwitch(option, null));
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option();
        cmd.addOption(option);
        assertTrue(cmd.hasOption(option));
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option();
        cmd.addOption(option);
        assertEquals(option, cmd.getOption(option.getPreferredName()));
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option();
        String value = "value";
        cmd.addValue(option, value);
        assertEquals(value, cmd.getValues(option, null).get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option();
        String value = "value";
        cmd.addValue(option, value);
        assertEquals(value, cmd.getUndefaultedValues(option).get(0));
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option();
        cmd.addSwitch(option, true);
        assertTrue(cmd.getSwitch(option, null));
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option();
        String property = "property";
        String value = "value";
        cmd.addProperty(option, property, value);
        assertEquals(value, cmd.getProperty(option, property, null));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option();
        String property = "property";
        String value = "value";
        cmd.addProperty(option, property, value);
        assertEquals(value, cmd.getProperty(option, property, null));
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option();
        String property = "property";
        String value = "value";
        cmd.addProperty(option, property, value);
        assertTrue(cmd.getProperties(option).contains(property));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        String trigger = "-option";
        assertTrue(cmd.looksLikeOption(trigger));
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        List<String> arguments = Arrays.asList("arg1", "arg2");
        cmd = new WriteableCommandLineImpl(new Option(), arguments);
        assertEquals("arg1 arg2", cmd.toString());
    }

    @Test
    public void testGetOptions() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option();
        cmd.addOption(option);
        assertTrue(cmd.getOptions().contains(option));
    }

    @Test
    public void testGetOptionTriggers() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option();
        cmd.addOption(option);
        assertTrue(cmd.getOptionTriggers().contains(option.getPreferredName()));
    }

    @Test
    public void testDefaultValues() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option();
        String defaultValue = "defaultValue";
        cmd.setDefaultValues(option, Arrays.asList(defaultValue));
        assertEquals(defaultValue, cmd.getValues(option, null).get(0));
    }

    @Test
    public void testDefaultSwitch() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option();
        cmd.setDefaultSwitch(option, true);
        assertTrue(cmd.getSwitch(option, null));
    }

    @Test
    public void testGetNormalised() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        List<String> arguments = Arrays.asList("arg1", "arg2");
        cmd = new WriteableCommandLineImpl(new Option(), arguments);
        assertEquals(arguments, cmd.getNormalised());
    }

    // Additional test cases for potential edge cases and exception handling scenarios
    // ...

}
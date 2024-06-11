import static org.junit.Assert.*;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        Option option1 = new Option("option1");
        Option option2 = new Option("option2");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        assertTrue(commandLine.getOptions().contains(option1));
        assertTrue(commandLine.getOptions().contains(option2));
    }

    @Test
    public void testAddValue() {
        Option option = new Argument();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        assertTrue(commandLine.getValues(option, null).contains("value1"));
        assertTrue(commandLine.getValues(option, null).contains("value2"));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("switch");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addSwitch(option, true);

        assertEquals(true, commandLine.getSwitch(option, null));
    }

    @Test
    public void testHasOption() {
        Option option1 = new Option("option1");
        Option option2 = new Option("option2");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addOption(option1);

        assertTrue(commandLine.hasOption(option1));
        assertFalse(commandLine.hasOption(option2));
    }

    @Test
    public void testGetOption() {
        Option option1 = new Option("option1");
        Option option2 = new Option("option2");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addOption(option1);

        assertEquals(option1, commandLine.getOption("option1"));
        assertNull(commandLine.getOption("option2"));
    }

    @Test
    public void testGetValues() {
        Option option = new Argument();
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        defaultValues.add("default2");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        assertEquals(2, commandLine.getValues(option, defaultValues).size());
        assertTrue(commandLine.getValues(option, defaultValues).contains("value1"));
        assertTrue(commandLine.getValues(option, defaultValues).contains("value2"));

        assertEquals(defaultValues, commandLine.getValues(null, defaultValues));

    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("switch");
        Boolean defaultValue = false;

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addSwitch(option, true);

        assertEquals(true, commandLine.getSwitch(option, defaultValue));

        assertEquals(defaultValue, commandLine.getSwitch(null, defaultValue));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addProperty("property1", "value1");
        commandLine.addProperty("property2", "value2");

        assertEquals("value1", commandLine.getProperty("property1", ""));
        assertEquals("value2", commandLine.getProperty("property2", ""));
        assertEquals("", commandLine.getProperty("property3", ""));
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
        List<String> arguments = new ArrayList<>();
        arguments.add("arg1");
        arguments.add("arg2");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, arguments);

        assertEquals("arg1 arg2", commandLine.toString());
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Argument();
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        defaultValues.add("default2");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.setDefaultValues(option, defaultValues);

        assertEquals(defaultValues, commandLine.getValues(option, null));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("switch");
        Boolean defaultSwitch = false;

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.setDefaultSwitch(option, defaultSwitch);

        assertEquals(defaultSwitch, commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetNormalised() {
        List<String> arguments = new ArrayList<>();
        arguments.add("arg1");
        arguments.add("arg2");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, arguments);

        assertEquals(arguments, commandLine.getNormalised());
    }

}
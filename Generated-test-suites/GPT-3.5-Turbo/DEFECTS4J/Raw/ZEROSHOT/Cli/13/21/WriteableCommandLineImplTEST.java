import org.junit.*;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        Option option = new Option("option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addOption(option);

        assertTrue(commandLine.getOptions().contains(option));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addValue(option, "value");

        List values = commandLine.getValues(option, null);
        assertTrue(values.contains("value"));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addSwitch(option, true);

        Boolean switchValue = commandLine.getSwitch(option, null);
        assertTrue(switchValue);
    }

    @Test
    public void testHasOption() {
        Option option = new Option("option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addOption(option);

        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addOption(option);

        Option retrievedOption = commandLine.getOption("option");
        assertEquals(option, retrievedOption);
    }

    @Test
    public void testGetValues() {
        Option option = new Option("option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addValue(option, "value");

        List values = commandLine.getValues(option, null);
        assertNotNull(values);
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addSwitch(option, true);

        Boolean switchValue = commandLine.getSwitch(option, null);
        assertNotNull(switchValue);
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addProperty("property", "value");

        String propertyValue = commandLine.getProperty("property", null);
        assertEquals("value", propertyValue);
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addProperty("property", "value");

        String propertyValue = commandLine.getProperty("property", null);
        assertEquals("value", propertyValue);
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);    
        commandLine.addProperty("property1", "value1");
        commandLine.addProperty("property2", "value2");

        Set properties = commandLine.getProperties();
        assertTrue(properties.contains("property1"));
        assertTrue(properties.contains("property2"));
    }

    @Test
    public void testLooksLikeOption() {
        Option option = new Option("option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);

        boolean looksLikeOption = commandLine.looksLikeOption("-option");
        assertTrue(looksLikeOption);
    }

    @Test
    public void testToString() {
        Option option1 = new Option("option1");
        Option option2 = new Option("option2");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addValue(option1, "value1");
        commandLine.addValue(option2, "value2");

        String commandLineString = commandLine.toString();
        assertEquals("value1 value2", commandLineString);
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("option1");
        Option option2 = new Option("option2");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        List options = commandLine.getOptions();
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = new Option("option1");
        Option option2 = new Option("option2");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        Set triggers = commandLine.getOptionTriggers();
        assertTrue(triggers.contains("option1"));
        assertTrue(triggers.contains("option2"));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.setDefaultValues(option, null);

        List defaultValues = commandLine.getValues(option, null);
        assertNull(defaultValues);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.setDefaultSwitch(option, true);

        Boolean defaultSwitch = commandLine.getSwitch(option, null);
        assertTrue(defaultSwitch);
    }

    @Test
    public void testGetNormalised() {
        Option option = new Option("option");
        List arguments = new ArrayList();
        arguments.add("arg1");
        arguments.add("arg2");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, arguments);

        List normalised = commandLine.getNormalised();
        assertEquals(arguments, normalised);
    }
}
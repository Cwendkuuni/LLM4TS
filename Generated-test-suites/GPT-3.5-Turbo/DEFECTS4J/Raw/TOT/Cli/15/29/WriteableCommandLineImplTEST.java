import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("test"), new ArrayList<>());
        commandLine.addOption(new Option("option1"));
        assertTrue(commandLine.hasOption(new Option("option1")));     
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("test"), new ArrayList<>());
        Option option = new Option("option2");  
        commandLine.addValue(option, "value1");
        assertEquals("value1", commandLine.getValues(option, null).get(0));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("test"), new ArrayList<>());
        Option option = new Option("option3");  
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, null));
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("test"), new ArrayList<>());
        Option option = new Option("option4");  
        assertFalse(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("test"), new ArrayList<>());
        Option option = new Option("option5");  
        commandLine.addOption(option);
        assertEquals(option, commandLine.getOption("option5"));
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("test"), new ArrayList<>());
        Option option = new Option("option6");  
        commandLine.addValue(option, "value1");
        assertEquals("value1", commandLine.getValues(option, null).get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("test"), new ArrayList<>());
        Option option = new Option("option7");
        commandLine.addValue(option, "value1");
        assertEquals("value1", commandLine.getUndefaultedValues(option).get(0));
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("test"), new ArrayList<>());
        Option option = new Option("option8");  
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("test"), new ArrayList<>());
        assertEquals("defaultValue", commandLine.getProperty("property", "defaultValue"));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("test"), new ArrayList<>());
        commandLine.addProperty("property", "value");
        assertEquals("value", commandLine.getProperty("property"));
    }

    @Test
    public void testGetProperty_option() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("test"), new ArrayList<>());
        Option option = new Option("option");
        commandLine.addProperty(option, "property", "value");
        assertEquals("value", commandLine.getProperty(option, "property", "defaultValue"));
    }

    @Test
    public void testGetProperties_option() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("test"), new ArrayList<>());
        Option option = new Option("option");
        commandLine.addProperty(option, "property", "value");
        assertTrue(commandLine.getProperties(option).contains("property"));
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("test"), new ArrayList<>());
        assertTrue(commandLine.getProperties().isEmpty());
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("test"), new ArrayList<>());
        assertTrue(commandLine.looksLikeOption("-option"));
        assertFalse(commandLine.looksLikeOption("value"));
    }

    @Test
    public void testGetOptions() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("test"), new ArrayList<>());
        Option option1 = new Option("option1");
        Option option2 = new Option("option2");
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        assertEquals(2, commandLine.getOptions().size());
    }

    @Test
    public void testGetOptionTriggers() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("test"), new ArrayList<>());
        Option option1 = new Option("option1");
        Option option2 = new Option("option2");
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        assertEquals(2, commandLine.getOptionTriggers().size());
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("test"), new ArrayList<>());
        Option option = new Option("option");  
        commandLine.setDefaultValues(option, new ArrayList<>());
        assertTrue(commandLine.getValues(option, null).isEmpty());
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("test"), new ArrayList<>());
        Option option = new Option("option");
        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetNormalised() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("test"), new ArrayList<>());
        Option option = new Option("option");
        List<String> normalised = new ArrayList<>();
        normalised.add("arg1");
        normalised.add("arg2");
        commandLine.addOption(option);
        assertEquals(normalised, commandLine.getNormalised());
    }
}
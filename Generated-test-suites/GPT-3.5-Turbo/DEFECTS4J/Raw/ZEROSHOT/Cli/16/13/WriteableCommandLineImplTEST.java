import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {
    
    @Test
    public void testAddOption() {
        Option option = new Option("option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }
    
    @Test
    public void testAddValue() {
        Argument argument = new Argument("argument");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(argument, new ArrayList());
        commandLine.addValue(argument, "value");
        assertTrue(commandLine.getValues(argument, null).contains("value"));
    }
    
    @Test
    public void testAddSwitch() {
        Option option = new Option("option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, null));
    }
    
    @Test
    public void testHasOption() {
        Option option = new Option("option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }
    
    @Test
    public void testGetOption() {
        Option option = new Option("option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        commandLine.addOption(option);
        assertEquals(option, commandLine.getOption("option"));
    }
    
    @Test
    public void testGetValues() {
        Argument argument = new Argument("argument");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(argument, new ArrayList());
        List<String> values = new ArrayList<>();
        values.add("value1");
        values.add("value2");
        commandLine.addValue(argument, "value1");
        assertEquals(values, commandLine.getValues(argument, null));
    }
    
    @Test
    public void testGetUndefaultedValues() {
        Argument argument = new Argument("argument");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(argument, new ArrayList());
        List<String> values = new ArrayList<>();
        values.add("value1");
        values.add("value2");
        commandLine.addValue(argument, "value1");
        assertEquals(values, commandLine.getUndefaultedValues(argument));
    }
    
    @Test
    public void testGetSwitch() {
        Option option = new Option("option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
    }
    
    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
        commandLine.addProperty("property", "value");
        assertEquals("value", commandLine.getProperty("property"));
    }
    
    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
        commandLine.addProperty("property", "value");
        assertEquals("value", commandLine.getProperty("property"));
    }
    
    @Test
    public void testGetPropertyWithDefault() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
        assertEquals("default", commandLine.getProperty("property", "default"));
    }
    
    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
        commandLine.addProperty("property1", "value1");
        commandLine.addProperty("property2", "value2");
        assertEquals(2, commandLine.getProperties().size());
    }
    
    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
        assertTrue(commandLine.looksLikeOption("-option"));
        assertFalse(commandLine.looksLikeOption("value"));
    }
    
    @Test
    public void testToString() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
        commandLine.addValue(new Argument("argument"), "value");
        assertEquals("value", commandLine.toString());
    }
    
    @Test
    public void testGetOptions() {
        Option option1 = new Option("option1");
        Option option2 = new Option("option2");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList());
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        assertEquals(2, commandLine.getOptions().size());
    }
    
    @Test
    public void testGetOptionTriggers() {
        Option option1 = new Option("option1");
        Option option2 = new Option("option2");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList());
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        assertEquals(2, commandLine.getOptionTriggers().size());
    }
    
    @Test
    public void testSetDefaultValues() {
        Option option = new Option("option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        defaultValues.add("default2");
        commandLine.setDefaultValues(option, defaultValues);
        assertEquals(defaultValues, commandLine.getValues(option, null));
    }
    
    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
    }
    
    @Test
    public void testGetNormalised() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
        List<String> normalised = new ArrayList<>();
        normalised.add("value1");
        normalised.add("value2");
        commandLine.addNormalised("value1");
        assertEquals(normalised, commandLine.getNormalised());
    }
}
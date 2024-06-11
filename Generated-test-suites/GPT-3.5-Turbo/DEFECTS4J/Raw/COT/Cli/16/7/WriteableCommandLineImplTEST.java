import org.junit.Test;
import java.util.ArrayList;
import java.util.List;  
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl();
        Option option = new Option("test");
        
        commandLine.addOption(option);
        
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl();
        Option option = new Option("test");

        commandLine.addValue(option, "value");
        
        List<String> expectedValues = new ArrayList<>();
        expectedValues.add("value");
        assertEquals(expectedValues, commandLine.getValues(option, null));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl();
        Option option = new Option("test");

        commandLine.addSwitch(option, true);
        
        assertTrue(commandLine.getSwitch(option, null));
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl();
        Option option = new Option("test");

        commandLine.addOption(option);
        
        assertTrue(commandLine.hasOption(option));
        assertFalse(commandLine.hasOption(new Option("nonexistent")));
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl();
        Option option = new Option("test");

        commandLine.addOption(option);
        
        assertEquals(option, commandLine.getOption("test"));
        assertNull(commandLine.getOption("nonexistent"));
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl();
        Option option = new Option("test");
        List<String> values = new ArrayList<>();
        values.add("value1");
        values.add("value2");

        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        
        assertEquals(values, commandLine.getValues(option, null));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl();
        Option option = new Option("test");
        List<String> values = new ArrayList<>();
        values.add("value1");
        values.add("value2");

        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        
        assertEquals(values, commandLine.getUndefaultedValues(option));
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl();
        Option option = new Option("test");

        commandLine.addSwitch(option, true);
        
        assertTrue(commandLine.getSwitch(option, null));
        assertNull(commandLine.getSwitch(new Option("nonexistent"), null));
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl();
        Option option = new Option("test");

        commandLine.addProperty(option, "property", "value");
        
        assertEquals("value", commandLine.getProperty(option, "property", null));
        assertNull(commandLine.getProperty(new Option("nonexistent"), "property", null));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl();
        Option option = new Option("test");
        
        commandLine.addProperty(option, "property", "value");
        
        assertEquals("value", commandLine.getProperty(option, "property", null));
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl();
        Option option = new Option("test");

        commandLine.addProperty(option, "property1", "value1");
        commandLine.addProperty(option, "property2", "value2");
        
        assertEquals(2, commandLine.getProperties(option).size());
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl();
        
        assertTrue(commandLine.looksLikeOption("-t"));
        assertFalse(commandLine.looksLikeOption("nonoption"));
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl();
        commandLine.addValue(new Argument(), "value1");
        commandLine.addValue(new Argument(), "value2");
        
        assertEquals("value1 value2", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl();
        Option option = new Option("test");
        commandLine.addOption(option);
        
        assertEquals(1, commandLine.getOptions().size());
    }

    @Test
    public void testGetOptionTriggers() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl();
        Option option = new Option("test");
        commandLine.addOption(option);
        
        assertEquals(1, commandLine.getOptionTriggers().size());
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl();
        Option option = new Option("test");
        List<String> defaults = new ArrayList<>();
        defaults.add("default1");
        defaults.add("default2");

        commandLine.setDefaultValues(option, defaults);
        
        assertEquals(defaults, commandLine.getValues(option, null));
        
        commandLine.setDefaultValues(option, null);
        assertTrue(commandLine.getValues(option, null).isEmpty());
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl();
        Option option = new Option("test");

        commandLine.setDefaultSwitch(option, true);
        
        assertTrue(commandLine.getSwitch(option, null));
        
        commandLine.setDefaultSwitch(option, null);
        assertFalse(commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetNormalised() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl();
        List<String> normalized = new ArrayList<>();
        normalized.add("arg1");
        normalized.add("arg2");
        
        assertEquals(normalized, commandLine.getNormalised());
    }
}
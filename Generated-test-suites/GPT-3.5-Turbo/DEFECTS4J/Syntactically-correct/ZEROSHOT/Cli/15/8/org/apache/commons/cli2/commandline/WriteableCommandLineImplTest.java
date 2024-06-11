import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        Option option = new Option("example-option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }
    
    @Test
    public void testAddValue() {
        Option option = new Option("example-option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        String value = "example-value";
        commandLine.addValue(option, value);
        assertEquals(value, commandLine.getValues(option, null).get(0));
    }
    
    @Test
    public void testAddSwitch() {
        Option option = new Option("example-option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        boolean value = true;
        commandLine.addSwitch(option, value);
        assertEquals(value, commandLine.getSwitch(option, null));
    }
    
    @Test
    public void testHasOption() {
        Option option = new Option("example-option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        assertFalse(commandLine.hasOption(option));
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }
    
    @Test
    public void testGetOption() {
        Option option = new Option("example-option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        assertNull(commandLine.getOption("example-trigger"));
        commandLine.addOption(option);
        assertNotNull(commandLine.getOption("example-trigger"));
    }
    
    @Test
    public void testGetValues() {
        Option option = new Option("example-option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        String value = "example-value";
        commandLine.addValue(option, value);
        assertEquals(value, commandLine.getValues(option, null).get(0));
    }
    
    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("example-option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        String value = "example-value";
        commandLine.addValue(option, value);
        assertEquals(value, commandLine.getUndefaultedValues(option).get(0));
    }
    
    @Test
    public void testGetSwitch() {
        Option option = new Option("example-option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        boolean value = true;
        commandLine.addSwitch(option, value);
        assertEquals(value, commandLine.getSwitch(option, null));
    }
    
    @Test
    public void testGetProperty() {
        Option option = new Option("example-option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        String property = "example-property";
        String value = "example-value";
        commandLine.addProperty(option, property, value);
        assertEquals(value, commandLine.getProperty(option, property, null));
    }
    
    @Test
    public void testAddProperty() {
        Option option = new Option("example-option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        String property = "example-property";
        String value = "example-value";
        commandLine.addProperty(option, property, value);
        assertEquals(value, commandLine.getProperty(option, property, null));
    }
    
    @Test
    public void testGetProperties() {
        Option option = new Option("example-option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        String property = "example-property";
        String value = "example-value";
        commandLine.addProperty(option, property, value);
        assertTrue(commandLine.getProperties(option).contains(property));
    }
    
    @Test
    public void testLooksLikeOption() {
        Option option = new Option("example-option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        assertFalse(commandLine.looksLikeOption("example-trigger"));
        assertTrue(commandLine.looksLikeOption("-example-trigger"));
    }
    
    @Test
    public void testToString() {
        Option option = new Option("example-option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        String arg1 = "arg1";
        String arg2 = "arg2";
        commandLine.addNormalised(arg1);
        commandLine.addNormalised(arg2);
        assertEquals(arg1 + " " + arg2, commandLine.toString());
    }
    
    @Test
    public void testGetOptions() {
        Option option = new Option("example-option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        assertTrue(commandLine.getOptions().contains(option));
    }
    
    @Test
    public void testGetOptionTriggers() {
        Option option = new Option("example-option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        assertTrue(commandLine.getOptionTriggers().contains(option.getPreferredName()));
    }
    
    @Test
    public void testSetDefaultValues() {
        Option option = new Option("example-option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default-value");
        commandLine.setDefaultValues(option, defaultValues);
        assertEquals(defaultValues, commandLine.getValues(option, null));
    }
    
    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("example-option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        Boolean defaultValue = true;
        commandLine.setDefaultSwitch(option, defaultValue);
        assertEquals(defaultValue, commandLine.getSwitch(option, null));
    }
    
    @Test
    public void testGetNormalised() {
        Option option = new Option("example-option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        String arg1 = "arg1";
        String arg2 = "arg2";
        commandLine.addNormalised(arg1);
        commandLine.addNormalised(arg2);
        assertTrue(commandLine.getNormalised().contains(arg1));
    }
}
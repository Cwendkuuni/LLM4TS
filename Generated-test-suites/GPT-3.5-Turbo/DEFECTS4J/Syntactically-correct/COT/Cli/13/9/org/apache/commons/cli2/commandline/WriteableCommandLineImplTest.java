import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {
    
    private WriteableCommandLineImpl commandLine;
    private Option option;
    
    @Before
    public void setUp() {
        option = new Option("test");
        commandLine = new WriteableCommandLineImpl(option, new ArrayList());
    }
    
    @Test
    public void testAddOption() {
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }
    
    @Test
    public void testAddValue() {
        String value = "value";
        commandLine.addValue(option, value);
        assertEquals(value, commandLine.getValues(option, new ArrayList()).get(0));
    }
    
    @Test
    public void testAddSwitch() {
        commandLine.addSwitch(option, true);
        assertEquals(Boolean.TRUE, commandLine.getSwitch(option, null));
    }
    
    @Test
    public void testHasOption() {
        assertFalse(commandLine.hasOption(option));
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }
    
    @Test
    public void testGetOption() {
        assertNull(commandLine.getOption("test"));
        commandLine.addOption(option);
        assertNotNull(commandLine.getOption("test"));
    }
    
    @Test
    public void testGetValues() {
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default");
        String value = "value";
        commandLine.addValue(option, value);
        assertEquals(value, commandLine.getValues(option, defaultValues).get(0));
        assertEquals(defaultValues, commandLine.getValues(option, null));
    }
    
    @Test
    public void testGetSwitch() {
        assertNull(commandLine.getSwitch(option, null));
        commandLine.addSwitch(option, true);
        assertEquals(Boolean.TRUE, commandLine.getSwitch(option, null));
    }
    
    @Test
    public void testAddProperty() {
        String property = "property";
        String value = "value";
        commandLine.addProperty(property, value);
        assertEquals(value, commandLine.getProperty(property, null));
    }
    
    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-test"));
        assertFalse(commandLine.looksLikeOption("test"));
    }
    
    @Test
    public void testToString() {
        String arg1 = "arg1";
        String arg2 = "arg2";
        List<String> arguments = new ArrayList<>();
        arguments.add(arg1);
        arguments.add(arg2);
        commandLine = new WriteableCommandLineImpl(option, arguments);
        assertEquals(arg1 + " " + arg2, commandLine.toString());
    }
    
    @Test
    public void testGetOptions() {
        assertEquals(0, commandLine.getOptions().size());
        commandLine.addOption(option);
        assertEquals(1, commandLine.getOptions().size());
    }
    
    @Test
    public void testGetOptionTriggers() {
        assertEquals(0, commandLine.getOptionTriggers().size());
        commandLine.addOption(option);
        assertEquals(1, commandLine.getOptionTriggers().size());
    }
    
    @Test
    public void testSetDefaultValues() {
        List<String> defaults = new ArrayList<>();
        defaults.add("default");
        commandLine.setDefaultValues(option, defaults);
        assertEquals(defaults, commandLine.getValues(option, null));
    }
    
    @Test
    public void testSetDefaultSwitch() {
        commandLine.setDefaultSwitch(option, true);
        assertEquals(Boolean.TRUE, commandLine.getSwitch(option, null));
    }
    
    @Test
    public void testGetNormalised() {
        String arg1 = "arg1";
        String arg2 = "arg2";
        List<String> arguments = new ArrayList<>();
        arguments.add(arg1);
        arguments.add(arg2);
        commandLine = new WriteableCommandLineImpl(option, arguments);
        assertEquals(arguments, commandLine.getNormalised());
    }
}
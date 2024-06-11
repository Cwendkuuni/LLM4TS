import static org.junit.Assert.*;
import org.junit.Test;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl();
        commandLine.addOption(option);
        assertTrue(commandLine.getOptions().contains(option));
    }
    
    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl();
        Object value = new Object();
        commandLine.addValue(option, value);
        assertEquals(value, commandLine.getValues(option).get(0));
    }
    
    @Test
    public void testGetValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl();
        Object value1 = new Object();
        Object value2 = new Object();
        commandLine.addValue(option, value1);
        commandLine.addValue(option, value2);
        assertEquals(value1, commandLine.getValues(option).get(0));
        assertEquals(value2, commandLine.getValues(option).get(1));
    }
    
    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl();
        List defaultValues = new List();
        defaultValues.add(new Object());
        defaultValues.add(new Object());
        commandLine.setDefaultValues(option, defaultValues);
        assertEquals(defaultValues, commandLine.getDefaultValues(option));
    }
    
    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl();
        boolean value = true;
        commandLine.addSwitch(option, value);
        assertEquals(value, commandLine.getSwitchValue(option));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testAddSwitchThrowsIllegalStateException() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl();
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }
    
    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl();
        Boolean defaultSwitch = false;
        commandLine.setDefaultSwitch(option, defaultSwitch);
        assertEquals(defaultSwitch, commandLine.getDefaultSwitch(option));
    }
    
    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        String property = "testProperty";
        String value = "testValue";
        commandLine.addProperty(property, value);
        assertEquals(value, commandLine.getProperty(property));
    }
    
    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        String argument = "-option";
        assertTrue(commandLine.looksLikeOption(argument));
    }
}
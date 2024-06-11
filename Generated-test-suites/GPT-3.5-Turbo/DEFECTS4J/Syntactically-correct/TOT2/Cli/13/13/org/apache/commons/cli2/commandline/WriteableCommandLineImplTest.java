import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli2.Option;
import java.util.Arrays;

public class WriteableCommandLineImplTest {
    
    private WriteableCommandLineImpl writeableCommandLine;
    private Option option;
    
    @Before
    public void setUp() {
        option = new Option.Builder("test").create();
        writeableCommandLine = new WriteableCommandLineImpl(option, Arrays.asList("arg1", "arg2"));
    }
    
    @Test
    public void testAddOption() {
        Option newOption = new Option.Builder("newOption").create();
        writeableCommandLine.addOption(newOption);
        assertTrue(writeableCommandLine.getOptions().contains(newOption));
    }
    
    @Test
    public void testAddValue() {
        writeableCommandLine.addValue(option, "value1");
        assertEquals("value1", writeableCommandLine.getValues(option, null).get(0));
    }
    
    @Test
    public void testAddSwitch() {
        writeableCommandLine.addSwitch(option, true);
        assertTrue(writeableCommandLine.getSwitch(option, null));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testAddSwitchException() {
        writeableCommandLine.addSwitch(option, true);
        writeableCommandLine.addSwitch(option, false);
    }
    
    @Test
    public void testHasOption() {
        assertTrue(writeableCommandLine.hasOption(option));
    }
    
    // Test for other methods in WriteableCommandLineImpl
    
}
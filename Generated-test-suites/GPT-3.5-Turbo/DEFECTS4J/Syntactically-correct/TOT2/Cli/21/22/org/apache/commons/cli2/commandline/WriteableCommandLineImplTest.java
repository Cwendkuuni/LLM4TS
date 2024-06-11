import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        Option option = new OptionImpl("testOption", "Test Option");
        List arguments = new ArrayList<>();
        commandLine = new WriteableCommandLineImpl(option, arguments);
    }

    @Test
    public void testAddOption() {
        Option option = new OptionImpl("testOption2", "Test Option 2");
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        Option option = new ArgumentImpl("testArgument");
        commandLine.addOption(option);
        commandLine.addValue(option, "value");
        List values = commandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals("value", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = new OptionImpl("testSwitch", "Test Switch");
        commandLine.addSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, false);
        assertTrue(switchValue);
    }

    @Test
    public void testGetOption() {
        Option option = new OptionImpl("testOption3", "Test Option 3");
        commandLine.addOption(option);
        Option retrievedOption = commandLine.getOption("testOption3");
        assertEquals(option, retrievedOption);
    }

    // Additional test cases for other methods can be added here

}
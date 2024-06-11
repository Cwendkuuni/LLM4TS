import org.junit.*;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl commandLineImpl = new WriteableCommandLineImpl(new Option(), new ArrayList());
        Option option = new Option();
        commandLineImpl.addOption(option);
        assertTrue(commandLineImpl.hasOption(option));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl commandLineImpl = new WriteableCommandLineImpl(new Option(), new ArrayList());
        Option option = new Option();
        Object value = "value";
        commandLineImpl.addValue(option, value);
        assertEquals(value, commandLineImpl.getValues(option, null).get(0));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl commandLineImpl = new WriteableCommandLineImpl(new Option(), new ArrayList());
        Option option = new Option();
        commandLineImpl.addSwitch(option, true);
        assertTrue(commandLineImpl.getSwitch(option, false));
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl commandLineImpl = new WriteableCommandLineImpl(new Option(), new ArrayList());
        List arguments = new ArrayList();
        arguments.add("argument1");
        arguments.add("argument2");
        commandLineImpl.addOption(new Option());
        commandLineImpl.setNormalised(arguments);
        assertEquals("argument1 argument2", commandLineImpl.toString());
    }

    // Add more test cases for other methods

}
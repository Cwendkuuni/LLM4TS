import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl writeableCommandLine;

    @Before
    public void setUp() {
        writeableCommandLine = new WriteableCommandLineImpl(new Option("testOption"), new ArrayList());
    }

    @Test
    public void testAddOption() {
        Option option = new Option("newOption");
        writeableCommandLine.addOption(option);
        assertTrue(writeableCommandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        Argument argument = new Argument();
        writeableCommandLine.addOption(argument);
        writeableCommandLine.addValue(argument, "value1");
        assertEquals(1, writeableCommandLine.getValues(argument, null).size());
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("testSwitch");
        writeableCommandLine.addSwitch(option, true);
        assertTrue(writeableCommandLine.getSwitch(option, false));
    }

    @Test
    public void testGetProperty() {
        String property = "testProperty";
        String value = "testValue";
        writeableCommandLine.addProperty(property, value);
        assertEquals(value, writeableCommandLine.getProperty(property));
    }

    // Add more test cases for other methods in WriteableCommandLineImpl

}
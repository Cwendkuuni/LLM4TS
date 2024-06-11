import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class WriteableCommandLineTest {

    private WriteableCommandLine writeableCommandLine;

    @Before
    public void setUp() {
        writeableCommandLine = new WriteableCommandLineImpl();
    }

    @Test
    public void testAddOption() {
        Option option = new OptionImpl("test", "description");
        writeableCommandLine.addOption(option);
        assertTrue(writeableCommandLine.getOptions().contains(option));
    }

    @Test
    public void testAddValue() {
        Option option = new OptionImpl("test", "description");
        Object value = "value";
        writeableCommandLine.addValue(option, value);
        assertTrue(writeableCommandLine.getValues(option).contains(value));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new OptionImpl("test", "description");
        List values = writeableCommandLine.getUndefaultedValues(option);
        assertEquals(0, values.size());
    }

    // Additional test cases for other methods can be added here

}
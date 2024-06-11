import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli2.*;

public class WriteableCommandLineTest {

    WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl();

    @Test
    public void testAddOption() {
        Option option = new Option("testOption", "test option");
        writeableCommandLine.addOption(option);
        assertTrue(writeableCommandLine.hasOption("testOption"));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("testOption", "test option");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "testValue");
        assertEquals("testValue", writeableCommandLine.getValues(option).get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("testOption", "test option");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "testValue");
        assertEquals(1, writeableCommandLine.getUndefaultedValues(option).size());
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("testOption", "test option");
        writeableCommandLine.addOption(option);
        writeableCommandLine.setDefaultValues(option, List.of("default"));
        assertEquals("default", writeableCommandLine.getValues(option).get(0));
    }

    // Repeat the process for all other public methods

}
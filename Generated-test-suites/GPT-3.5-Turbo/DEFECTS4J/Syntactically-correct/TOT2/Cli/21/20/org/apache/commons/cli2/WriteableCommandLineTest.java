import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class WriteableCommandLineTest {

    private WriteableCommandLine writeableCommandLine;

    @Before
    public void setUp() {
        writeableCommandLine = new WriteableCommandLineImpl();
    }

    @Test
    public void testAddOption() {
        Option option = new Option("testOption");
        writeableCommandLine.addOption(option);
        assertTrue(writeableCommandLine.hasOption("testOption"));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("testOption");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "testValue");
        assertEquals("testValue", writeableCommandLine.getValue("testOption"));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("testOption");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "testValue");
        assertEquals(1, writeableCommandLine.getUndefaultedValues(option).size());
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("testOption");
        writeableCommandLine.addOption(option);
        writeableCommandLine.setDefaultValues(option, List.of("defaultValue"));
        assertEquals("defaultValue", writeableCommandLine.getValue("testOption"));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("testOption");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);
        assertTrue(writeableCommandLine.getSwitch("testOption"));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchIllegalStateException() {
        Option option = new Option("testOption");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);
        writeableCommandLine.addSwitch(option, false);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("testOption");
        writeableCommandLine.addOption(option);
        writeableCommandLine.setDefaultSwitch(option, true);
        assertTrue(writeableCommandLine.getSwitch("testOption"));
    }

    @Test
    public void testAddProperty() {
        Option option = new Option("testOption");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addProperty(option, "testProperty", "testValue");
        assertEquals("testValue", writeableCommandLine.getProperty("testOption", "testProperty"));
    }

    @Test
    public void testAddDefaultProperty() {
        writeableCommandLine.addProperty("testProperty", "testValue");
        assertEquals("testValue", writeableCommandLine.getProperty(null, "testProperty"));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(writeableCommandLine.looksLikeOption("-testOption"));
        assertFalse(writeableCommandLine.looksLikeOption("testArgument"));
    }

    // Edge case: No test for setCurrentOption as it is a package-private method

}
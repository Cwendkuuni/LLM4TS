import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineTest {

    private WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        commandLine = new WriteableCommandLineImpl();
    }

    @Test
    public void testAddOption() {
        Option option = new Option("l", "list", false, "List files");
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption("l"));
        assertFalse(commandLine.hasOption("f"));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("f", "file", true, "Specify a file");
        commandLine.addValue(option, "test.txt");
        assertTrue(commandLine.hasOption("f"));
        assertEquals("test.txt", commandLine.getValue("f"));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("f", "file", true, "Specify a file");
        commandLine.addValue(option, "test.txt");
        commandLine.addValue(option, "example.txt");
        assertEquals(2, commandLine.getUndefaultedValues("f").size());
        assertTrue(commandLine.getUndefaultedValues("f").contains("test.txt"));
        assertTrue(commandLine.getUndefaultedValues("f").contains("example.txt"));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("f", "file", true, "Specify a file");
        commandLine.setDefaultValues(option, Arrays.asList("test.txt", "example.txt"));
        assertTrue(commandLine.hasOption("f"));
        assertEquals(2, commandLine.getUndefaultedValues("f").size());
        assertTrue(commandLine.getUndefaultedValues("f").contains("test.txt"));
        assertTrue(commandLine.getUndefaultedValues("f").contains("example.txt"));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitch() {
        Option option = new Option("v", "verbose", false, "Enable verbose mode");
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("v", "verbose", false, "Enable verbose mode");
        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.hasOption("v"));
        assertTrue(commandLine.getSwitch("v"));
    }

    @Test
    public void testAddProperty() {
        Option option = new Option("p", "property", true, "Set a property");
        commandLine.addProperty(option, "key", "value");
        assertTrue(commandLine.hasOption("p"));
        assertEquals("value", commandLine.getProperty("p", "key"));
    }

    @Test
    public void testAddPropertyToDefaultSet() {
        commandLine.addProperty("key", "value");
        assertTrue(commandLine.hasOption(""));
        assertEquals("value", commandLine.getProperty("", "key"));
    }

    @Test
    public void testLooksLikeOption() {
        assertFalse(commandLine.looksLikeOption("file.txt"));
        assertTrue(commandLine.looksLikeOption("-o"));
    }

    @Test
    public void testSetCurrentOption() {
        Option option = new Option("f", "file", true, "Specify a file");
        commandLine.setCurrentOption(option);
        assertEquals(option, commandLine.getCurrentOption());
    }

}
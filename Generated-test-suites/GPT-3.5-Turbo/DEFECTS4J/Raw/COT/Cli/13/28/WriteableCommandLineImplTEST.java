import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        Option option = new OptionBuilder().withPreferredName("opt").create();
        commandLine = new WriteableCommandLineImpl(option, new ArrayList());
    }

    @Test
    public void testAddOption() {
        Option option = new OptionBuilder().withPreferredName("opt2").create();
        commandLine.addOption(option);

        assertTrue(commandLine.hasOption(option));
        assertEquals(option, commandLine.getOption("opt2"));
    }

    @Test
    public void testAddValue() {
        Option option = new ArgumentBuilder().withPreferredName("arg").create();
        commandLine.addValue(option, "value");

        List values = commandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals("value", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = new OptionBuilder().withPreferredName("switch").create();
        commandLine.addSwitch(option, true);

        assertTrue(commandLine.hasOption(option));
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testGetProperty() {
        commandLine.addProperty("property", "value");

        assertEquals("value", commandLine.getProperty("property", null));
        assertEquals("default", commandLine.getProperty("nonexistent", "default"));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-opt"));
        assertTrue(commandLine.looksLikeOption("--opt"));
        assertFalse(commandLine.looksLikeOption("value"));
    }

    @Test
    public void testToString() {
        commandLine.addValue(new ArgumentBuilder().create(), "value1");
        commandLine.addValue(new ArgumentBuilder().create(), "value2");

        assertEquals("value1 value2", commandLine.toString());
    }

    // Add additional test cases for edge cases and exception handling scenarios here

}


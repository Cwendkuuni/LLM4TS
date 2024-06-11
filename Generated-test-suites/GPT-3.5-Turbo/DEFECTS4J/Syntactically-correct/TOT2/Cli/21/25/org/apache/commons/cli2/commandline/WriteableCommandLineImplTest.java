import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        Option option1 = new Option("o1");
        Option option2 = new Option("o2");
        List<String> arguments = new ArrayList<>();
        arguments.add("arg1");
        commandLine = new WriteableCommandLineImpl(option1, arguments);
        commandLine.addOption(option2);
    }

    @Test
    public void testAddOption() {
        Option option3 = new Option("o3");
        assertFalse(commandLine.hasOption(option3));
        commandLine.addOption(option3);
        assertTrue(commandLine.hasOption(option3));
    }

    @Test
    public void testAddValue() {
        Argument argument = new Argument("arg");
        commandLine.addValue(argument, "value1");
        List<String> values = commandLine.getValues(argument, null);
        assertNotNull(values);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        Option switchOption = new Option("s");
        assertFalse(commandLine.hasOption(switchOption));
        commandLine.addSwitch(switchOption, true);
        assertTrue(commandLine.hasOption(switchOption));
        assertEquals(Boolean.TRUE, commandLine.getSwitch(switchOption, null));
    }

    @Test
    public void testGetProperty() {
        PropertyOption propertyOption = new PropertyOption();
        commandLine.addProperty(propertyOption, "key", "value");
        assertEquals("value", commandLine.getProperty(propertyOption, "key", null));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-option"));
        assertTrue(commandLine.looksLikeOption("--option"));
        assertFalse(commandLine.looksLikeOption("notanoption"));
    }

    // Additional test cases can be added for complete coverage

}
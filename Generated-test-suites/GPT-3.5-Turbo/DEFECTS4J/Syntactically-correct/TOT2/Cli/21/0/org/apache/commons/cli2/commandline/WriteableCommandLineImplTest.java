import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {
    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        Option rootOption = new OptionImpl();
        List<String> arguments = new ArrayList<>();
        commandLine = new WriteableCommandLineImpl(rootOption, arguments);
    }

    @Test
    public void testAddOption() {
        Option option = new OptionImpl();
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        Option option = new OptionImpl();
        Object value = "test value";
        commandLine.addValue(option, value);
        assertEquals(commandLine.getValues(option, null).get(0), value);
    }

    @Test
    public void testAddSwitch() {
        Option option = new OptionImpl();
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetProperty() {
        String property = "testProperty";
        commandLine.addProperty(property, "testValue");
        assertEquals(commandLine.getProperty(new PropertyOption(), property, ""), "testValue");
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-option"));
        assertFalse(commandLine.looksLikeOption("argument"));
    }

    // Add additional test cases for all other methods as needed

}
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;
    private Option option;
    private List arguments;

    @Before
    public void setUp() {
        option = mock(Option.class);
        arguments = new ArrayList();

        commandLine = new WriteableCommandLineImpl(option, arguments);
    }

    @Test
    public void testAddOption() {
        commandLine.addOption(option);

        assertTrue(commandLine.getOptions().contains(option));
        assertTrue(commandLine.getOptionTriggers().contains(option.getPreferredName()));
    }

    @Test
    public void testAddValue() {
        Object value = new Object();
        commandLine.addValue(option, value);

        assertTrue(commandLine.getValues(option, null).contains(value));
    }

    @Test
    public void testAddSwitch() {
        commandLine.addSwitch(option, true);

        assertTrue(commandLine.getSwitch(option, null));
    }

    @Test
    public void testHasOption() {
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        assertEquals(option, commandLine.getOption(option.getPreferredName()));
    }

    @Test
    public void testGetValues() {
        Object value = new Object();
        List defaults = new ArrayList();
        defaults.add(value);

        commandLine.setDefaultValues(option, defaults);

        assertTrue(commandLine.getValues(option, null).contains(value));
    }

    @Test
    public void testGetUndefaultedValues() {
        Object value = new Object();
        commandLine.addValue(option, value);

        assertTrue(commandLine.getUndefaultedValues(option).contains(value));
    }

    @Test
    public void testGetSwitch() {
        commandLine.addSwitch(option, false);

        assertFalse(commandLine.getSwitch(option, true));
    }

    @Test
    public void testGetProperty() {
        String property = "testProperty";
        String value = "testValue";
        commandLine.addProperty(option, property, value);

        assertEquals(value, commandLine.getProperty(option, property, "defaultValue"));
    }

    @Test
    public void testAddProperty() {
        String property = "testProperty";
        String value = "testValue";
        commandLine.addProperty(option, property, value);

        assertTrue(commandLine.getProperties(option).contains(property));
        assertEquals(value, commandLine.getProperty(option, property, null));
    }

    @Test
    public void testLooksLikeOption() {
        String trigger = "-t";

        assertTrue(commandLine.looksLikeOption(trigger));
    }

    @Test
    public void testToString() {
        String arg = "testArg";
        arguments.add(arg);

        assertEquals(arg, commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        assertTrue(commandLine.getOptions().contains(option));
    }

    @Test
    public void testGetOptionTriggers() {
        assertTrue(commandLine.getOptionTriggers().contains(option.getPreferredName()));
    }

    @Test
    public void testSetDefaultValues() {
        List defaults = new ArrayList();
        defaults.add(new Object());

        commandLine.setDefaultValues(option, defaults);

        assertTrue(commandLine.getValues(option, null).containsAll(defaults));
    }

    @Test
    public void testSetDefaultSwitch() {
        commandLine.setDefaultSwitch(option, true);

        assertTrue(commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetNormalised() {
        String arg = "testArg";
        arguments.add(arg);

        assertTrue(commandLine.getNormalised().contains(arg));
    }
}
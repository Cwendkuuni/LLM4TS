import org.junit.Before;
import org.junit.Test;
import java.util.*;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {
    private Option option1;
    private Option option2;
    private Argument argument;
    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        option1 = new Option.Builder("o1").create();
        option2 = new Option.Builder("o2").create();
        argument = new Argument();
        List<String> args = Arrays.asList("arg1", "arg2");
        commandLine = new WriteableCommandLineImpl(option1, args);
    }

    @Test
    public void testAddOption() {
        commandLine.addOption(option2);
        List<Option> options = commandLine.getOptions();
        assertTrue(options.contains(option2));
    }

    @Test
    public void testAddValue() {
        commandLine.addValue(option1, "value");
        List<String> values = commandLine.getValues(option1, null);
        assertTrue(values.contains("value"));
    }

    @Test
    public void testAddSwitch() {
        commandLine.addSwitch(option1, true);
        Boolean bool = commandLine.getSwitch(option1, null);
        assertEquals(true, bool);
    }

    @Test
    public void testHasOption() {
        assertTrue(commandLine.hasOption(option1));
        assertFalse(commandLine.hasOption(option2));
    }

    @Test
    public void testGetOption() {
        Option option = commandLine.getOption("o1");
        assertEquals(option1, option);
    }

    @Test
    public void testGetValues() {
        List<String> values = commandLine.getValues(option1, null);
        assertEquals(Arrays.asList("arg1", "arg2"), values);
    }

    @Test
    public void testGetUndefaultedValues() {
        List<String> values = commandLine.getUndefaultedValues(option1);
        assertEquals(Arrays.asList("arg1", "arg2"), values);
    }

    @Test
    public void testGetSwitch() {
        assertNull(commandLine.getSwitch(option1, null));
        commandLine.addSwitch(option1, true);
        Boolean bool = commandLine.getSwitch(option1, null);
        assertEquals(true, bool);
    }

    @Test
    public void testGetProperty() {
        String property = commandLine.getProperty("property");
        assertNull(property);
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty(option1, "property", "value");
        String property = commandLine.getProperty(option1, "property", null);
        assertEquals("value", property);
    }

    @Test
    public void testGetPropertyWithDefaultValue() {
        String property = commandLine.getProperty(option1, "property", "default");
        assertEquals("default", property);
    }

    @Test
    public void testGetProperties() {
        Set<String> properties = commandLine.getProperties(option1);
        assertEquals(Collections.EMPTY_SET, properties);
    }

    @Test
    public void testGetPropertyOption() {
        String property = commandLine.getProperty(option1, "property");
        assertNull(property);
    }

    @Test
    public void testGetOptions() {
        List<Option> options = commandLine.getOptions();
        assertEquals(Arrays.asList(option1), options);
    }

    @Test
    public void testGetOptionTriggers() {
        Set<String> triggers = commandLine.getOptionTriggers();
        assertEquals(Collections.singleton("o1"), triggers);
    }

    @Test
    public void testSetDefaultValues() {
        commandLine.setDefaultValues(option1, Arrays.asList("default1", "default2"));
        List<String> values = commandLine.getValues(option1, null);
        assertEquals(Arrays.asList("default1", "default2"), values);
    }

    @Test
    public void testSetDefaultSwitch() {
        commandLine.setDefaultSwitch(option1, true);
        Boolean bool = commandLine.getSwitch(option1, null);
        assertEquals(true, bool);
    }

    @Test
    public void testGetNormalised() {
        List<String> normalised = commandLine.getNormalised();
        assertEquals(Arrays.asList("arg1", "arg2"), normalised);
    }
}
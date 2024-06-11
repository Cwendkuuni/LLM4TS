import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;
    private Option option;
    private List arguments;

    @Before
    public void setUp() {
        option = new Option("test");
        arguments = new ArrayList();
        commandLine = new WriteableCommandLineImpl(option, arguments);
    }

    @Test
    public void testAddOption() {
        int initialSize = commandLine.getOptions().size();
        
        Option newOption = new Option("new");
        commandLine.addOption(newOption);
    
        assertEquals(initialSize + 1, commandLine.getOptions().size());
        assertTrue(commandLine.getOptions().contains(newOption));
        assertNotNull(commandLine.getOption("new"));
    }

    @Test
    public void testAddValue() {
        Object value = "testValue";
        
        commandLine.addValue(option, value);

        assertTrue(commandLine.getValues(option, null).contains(value));
    }

    @Test
    public void testAddSwitch() {
        Option switchOption = new Option("switch");
        
        commandLine.addSwitch(switchOption, true);

        assertEquals(Boolean.TRUE, commandLine.getSwitch(switchOption, null));
    }

    @Test
    public void testHasOption() {
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option result = commandLine.getOption("test");

        assertEquals(option, result);
    }

    @Test
    public void testGetValues() {
        List values = new ArrayList();
        values.add("value1");
        values.add("value2");
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List result = commandLine.getValues(option, null);

        assertEquals(values, result);
    }

    @Test
    public void testGetUndefaultedValues() {
        List values = new ArrayList();
        values.add("value1");
        values.add("value2");
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List result = commandLine.getUndefaultedValues(option);

        assertEquals(values, result);
    }

    @Test
    public void testGetSwitch() {
        Option switchOption = new Option("switch");
        commandLine.addSwitch(switchOption, true);

        Boolean result = commandLine.getSwitch(switchOption, false);

        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testGetProperty() {
        String property = "testProperty";
        String value = "testValue";
        commandLine.addProperty(option, property, value);

        String result = commandLine.getProperty(option, property, "");

        assertEquals(value, result);
    }

    @Test
    public void testAddProperty() {
        String property = "testProperty";
        String value = "testValue";
        
        commandLine.addProperty(option, property, value);

        String result = commandLine.getProperty(option, property, "");

        assertEquals(value, result);
    }

    @Test
    public void testGetProperties() {
        String property = "testProperty";
        String value = "testValue";
        commandLine.addProperty(option, property, value);

        List result = new ArrayList(commandLine.getProperties(option));

        assertEquals(1, result.size());
        assertTrue(result.contains(property));
    }

    @Test
    public void testToString() {
        String arg1 = "arg1";
        String arg2 = "arg2";
        arguments.add(arg1);
        arguments.add(arg2);

        String result = commandLine.toString();

        assertEquals(arg1 + " " + arg2, result);
    }

    @Test
    public void testGetOptions() {
        List optionList = new ArrayList();
        optionList.add(option);

        List result = commandLine.getOptions();

        assertEquals(optionList, result);
    }

    @Test
    public void testGetOptionTriggers() {
        List triggerList = new ArrayList();
        triggerList.add("test");

        Set result = commandLine.getOptionTriggers();

        assertEquals(triggerList, result);
    }

    @Test
    public void testSetDefaultValues() {
        List defaults = new ArrayList();
        defaults.add("value1");
        defaults.add("value2");
        commandLine.setDefaultValues(option, defaults);

        List result = commandLine.getValues(option, null);

        assertEquals(defaults, result);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option switchOption = new Option("switch");
        commandLine.setDefaultSwitch(switchOption, true);

        Boolean result = commandLine.getSwitch(switchOption, null);

        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testGetNormalised() {
        List normalised = new ArrayList();
        normalised.add("arg1");
        normalised.add("arg2");
        assertEquals(normalised, commandLine.getNormalised());
    }
}
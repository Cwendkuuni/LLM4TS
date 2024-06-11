import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    @Mock
    private Option option;

    @Before
    public void setUp() {
        List arguments = new ArrayList();
        commandLine = new WriteableCommandLineImpl(option, arguments);
    }

    @Test
    public void testAddOption() {
        commandLine.addOption(option);
        assertTrue(commandLine.getOptions().contains(option));
    }

    @Test
    public void testAddValue() {
        Object value = new Object();
        commandLine.addValue(option, value);
        List<Object> values = commandLine.getValues(option, null);
        assertTrue(values.contains(value));
    }

    @Test
    public void testAddSwitch() {
        boolean value = true;
        commandLine.addSwitch(option, value);
        Boolean switchValue = commandLine.getSwitch(option, null);
        assertEquals(Boolean.valueOf(value), switchValue);
    }

    @Test
    public void testHasOption() {
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        String trigger = "trigger";
        Mockito.when(option.getPreferredName()).thenReturn(trigger);
        commandLine.addOption(option);
        Option retrievedOption = commandLine.getOption(trigger);
        assertEquals(option, retrievedOption);
    }

    @Test
    public void testGetValues() {
        option = Mockito.spy(new Option());
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        defaultValues.add("default2");
        commandLine.setDefaultValues(option, defaultValues);

        List<String> values = new ArrayList<>();
        values.add("value1");
        commandLine.addValue(option, "value1");

        List<String> retrievedValues = commandLine.getValues(option, null);
        assertTrue(retrievedValues.containsAll(values));
        assertTrue(retrievedValues.containsAll(defaultValues));
        assertTrue(retrievedValues.size() == values.size() + defaultValues.size());
    }

    @Test
    public void testGetUndefaultedValues() {
        List<String> values = new ArrayList<>();
        values.add("value1");
        values.add("value2");
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List<String> retrievedValues = commandLine.getUndefaultedValues(option);
        assertTrue(retrievedValues.containsAll(values));
        assertTrue(values.size() == values.size());
    }

    @Test
    public void testGetSwitch() {
        option = Mockito.spy(new Option());
        Boolean defaultValue = true;
        commandLine.setDefaultSwitch(option, defaultValue);
        Boolean retrievedSwitch = commandLine.getSwitch(option, null);
        assertEquals(defaultValue, retrievedSwitch);
    }

    @Test
    public void testGetProperty() {
        String property = "property";
        String value = "value";
        commandLine.addProperty(option, property, value);
        String retrievedValue = commandLine.getProperty(option, property, null);
        assertEquals(value, retrievedValue);
    }

    @Test
    public void testAddProperty() {
        String property = "property";
        String value = "value";
        commandLine.addProperty(property, value);
        String retrievedValue = commandLine.getProperty(property, null);
        assertEquals(value, retrievedValue);
    }

    @Test
    public void testGetProperties() {
        option = Mockito.spy(new Option());
        String property1 = "property1";
        String value1 = "value1";
        String property2 = "property2";
        String value2 = "value2";
        commandLine.addProperty(option, property1, value1);
        commandLine.addProperty(option, property2, value2);
        
        Set<String> properties = commandLine.getProperties(option);
        assertTrue(properties.contains(property1));
        assertTrue(properties.contains(property2));
        assertTrue(properties.size() == 2);
    }

    @Test
    public void testGetProperties_defaultOption() {
        String property1 = "property1";
        String value1 = "value1";
        String property2 = "property2";
        String value2 = "value2";
        commandLine.addProperty(property1, value1);
        commandLine.addProperty(property2, value2);

        Set<String> properties = commandLine.getProperties();
        assertTrue(properties.contains(property1));
        assertTrue(properties.contains(property2));
        assertTrue(properties.size() == 2);
    }

    @Test
    public void testLooksLikeOption() {
        option = Mockito.spy(new Option());
        String prefix = "-";
        List<String> prefixes = new ArrayList<>();
        prefixes.add(prefix);
        Mockito.when(option.getPrefixes()).thenReturn(prefixes);

        String trigger = "-trigger";
        boolean looksLikeOption = commandLine.looksLikeOption(trigger);
        assertTrue(looksLikeOption);
    }

    @Test
    public void testToString() {
        List<String> arguments = new ArrayList<>();
        arguments.add("arg1");
        arguments.add("arg2");
        commandLine = new WriteableCommandLineImpl(option, arguments);

        String commandLineString = commandLine.toString();
        assertTrue(commandLineString.contains("arg1"));
        assertTrue(commandLineString.contains("arg2"));
    }
}
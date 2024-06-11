import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class WriteableCommandLineImplTest {

    @Mock
    private Option option;
    @InjectMocks
    private WriteableCommandLineImpl writeableCommandLine;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAddOption() {
        writeableCommandLine.addOption(option);
        assertTrue(writeableCommandLine.getOptions().contains(option));
    }

    @Test
    public void testAddOption_AlreadyExists() {
        writeableCommandLine.addOption(option);
        writeableCommandLine.addOption(option); // Add the same option again
        assertEquals(1, writeableCommandLine.getOptions().size()); // Ensure only one option is added
    }

    @Test
    public void testHasOption() {
        writeableCommandLine.addOption(option);
        assertTrue(writeableCommandLine.hasOption(option));
    }

    @Test
    public void testHasOption_NotExists() {
        assertFalse(writeableCommandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        writeableCommandLine.addOption(option);
        assertEquals(option, writeableCommandLine.getOption(option.getPreferredName()));
    }

    @Test
    public void testGetOption_NotExists() {
        assertEquals(null, writeableCommandLine.getOption("invalidTrigger"));
    }

    @Test
    public void testGetValues() {
        List<String> values = new ArrayList<>();
        values.add("value1");
        values.add("value2");
        writeableCommandLine.addValue(option, values);
        assertEquals(values, writeableCommandLine.getValues(option, null));
    }

    @Test
    public void testGetValues_DefaultValuesExists() {
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("defaultValue1");
        defaultValues.add("defaultValue2");
        writeableCommandLine.setDefaultValues(option, defaultValues);
        assertEquals(defaultValues, writeableCommandLine.getValues(option, null));
    }

    @Test
    public void testGetValues_DefaultValuesExists_NoCommandlineValues() {
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("defaultValue1");
        defaultValues.add("defaultValue2");
        writeableCommandLine.setDefaultValues(option, defaultValues);
        assertEquals(defaultValues, writeableCommandLine.getValues(option, new ArrayList<>()));
    }

    @Test
    public void testGetValues_DefaultValuesDoesNotExist() {
        assertEquals(new ArrayList<>(), writeableCommandLine.getValues(option, null));
    }

    @Test
    public void testGetSwitch() {
        writeableCommandLine.addSwitch(option, true);
        assertEquals(Boolean.TRUE, writeableCommandLine.getSwitch(option, null));
    }

    @Test
    public void testGetSwitch_DefaultSwitchExists() {
        writeableCommandLine.setDefaultSwitch(option, true);
        assertEquals(Boolean.TRUE, writeableCommandLine.getSwitch(option, null));
    }

    @Test
    public void testGetSwitch_DefaultSwitchDoesNotExist() {
        assertEquals(null, writeableCommandLine.getSwitch(option, null));
    }
    
    // Additional test cases for other public methods can be added here
    
    @Test
    public void testAddProperty() {
        writeableCommandLine.addProperty("key", "value");
        assertEquals("value", writeableCommandLine.getProperty("key", null));
    }

    @Test
    public void testGetProperty() {
        writeableCommandLine.addProperty("key", "value");
        assertEquals("value", writeableCommandLine.getProperty("key", null));
    }

    @Test
    public void testGetProperty_DefaultValue() {
        assertEquals("defaultValue", writeableCommandLine.getProperty("key", "defaultValue"));
    }

    @Test
    public void testGetProperties() {
        writeableCommandLine.addProperty("key1", "value1");
        writeableCommandLine.addProperty("key2", "value2");

        Properties properties = new Properties();
        properties.setProperty("key1", "value1");
        properties.setProperty("key2", "value2");

        assertEquals(properties.keySet(), writeableCommandLine.getProperties());
    }

    @Test
    public void testLooksLikeOption_ValidOption() {
        writeableCommandLine.addOption(option);
        Mockito.when(option.getPrefixes()).thenReturn(new String[]{"-", "--"});
        assertTrue(writeableCommandLine.looksLikeOption("--option"));
    }

    @Test
    public void testLooksLikeOption_InvalidOption() {
        assertFalse(writeableCommandLine.looksLikeOption("argument"));
    }

    // Additional test cases for other public methods can be added here
    

    @Test
    public void testToString() {
        List<String> arguments = new ArrayList<>();
        arguments.add("argument1");
        arguments.add("argument2");

        writeableCommandLine = new WriteableCommandLineImpl(option, arguments);

        assertEquals("argument1 argument2", writeableCommandLine.toString());
    }

    @Test
    public void testGetOptions() {
        Option option1 = Mockito.mock(Option.class);
        Option option2 = Mockito.mock(Option.class);

        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);

        List<Option> options = new ArrayList<>();
        options.add(option1);
        options.add(option2);

        assertEquals(options, writeableCommandLine.getOptions());
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = Mockito.mock(Option.class);
        Option option2 = Mockito.mock(Option.class);

        Mockito.when(option1.getPreferredName()).thenReturn("option1");
        Mockito.when(option2.getPreferredName()).thenReturn("option2");

        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);

        assertEquals(Set.of("option1", "option2"), writeableCommandLine.getOptionTriggers());
    }

    @Test
    public void testSetDefaultValues() {
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("defaultValue1");
        defaultValues.add("defaultValue2");

        writeableCommandLine.setDefaultValues(option, defaultValues);
        assertEquals(defaultValues, writeableCommandLine.getValues(option, null));
    }

    @Test
    public void testSetDefaultSwitch() {
        writeableCommandLine.setDefaultSwitch(option, true);
        assertEquals(Boolean.TRUE, writeableCommandLine.getSwitch(option, null));
    }

    @Test
    public void testGetNormalised() {
        List<String> arguments = new ArrayList<>();
        arguments.add("argument1");
        arguments.add("argument2");

        writeableCommandLine = new WriteableCommandLineImpl(option, arguments);

        assertEquals(arguments, writeableCommandLine.getNormalised());
    }

}
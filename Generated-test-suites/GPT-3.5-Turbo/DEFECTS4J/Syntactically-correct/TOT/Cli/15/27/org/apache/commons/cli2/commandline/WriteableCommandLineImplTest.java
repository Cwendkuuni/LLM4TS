import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli2.*;
import org.apache.commons.cli2.commandline.*;
import org.apache.commons.cli2.option.PropertyOption;
import java.util.*;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        Option rootOption = new OptionBuilder("-r").create();
        List arguments = new ArrayList();
        commandLine = new WriteableCommandLineImpl(rootOption, arguments);
    }

    @Test
    public void testAddOption() {
        Option option = new OptionBuilder("-o").create();
        commandLine.addOption(option);
        assertTrue(commandLine.getOptions().contains(option));
    }

    @Test
    public void testAddValue() {
        Option option = new ArgumentBuilder("arg").create();
        Object value = "value";
        commandLine.addValue(option, value);
        assertEquals(value, commandLine.getValues(option, null).get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = new OptionBuilder("-s").create();
        boolean value = true;
        commandLine.addSwitch(option, value);
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testHasOption() {
        Option option = new OptionBuilder("-o").create();
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new OptionBuilder("-o").create();
        commandLine.addOption(option);
        assertEquals(option, commandLine.getOption("-o"));
    }

    @Test
    public void testGetValues() {
        Option option = new ArgumentBuilder("arg").create();
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default");
        commandLine.setDefaultValue(option, defaultValues);
        List<String> values = new ArrayList<>();
        values.add("value");
        commandLine.addValue(option, "value");
        assertEquals(values, commandLine.getValues(option, defaultValues));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new ArgumentBuilder("arg").create();
        List<String> values = new ArrayList<>();
        values.add("value");
        commandLine.addValue(option, "value");
        assertEquals(values, commandLine.getUndefaultedValues(option));
    }

    @Test
    public void testGetSwitch() {
        Option option = new OptionBuilder("-s").create();
        boolean defaultValue = true;
        commandLine.setDefaultSwitch(option, defaultValue);
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testGetProperty() {
        String propertyKey = "key";
        String propertyValue = "value";
        String defaultValue = "default";
        commandLine.addProperty(propertyKey, propertyValue);
        assertEquals(propertyValue, commandLine.getProperty(propertyKey, defaultValue));
    }

    @Test
    public void testAddProperty() {
        String propertyKey = "key";
        String propertyValue = "value";
        Option option = new OptionBuilder("-p").create();
        commandLine.addProperty(option, propertyKey, propertyValue);
        assertEquals(propertyValue, commandLine.getProperty(option, propertyKey, null));
    }

    @Test
    public void testGetProperties() {
        Option option = new PropertyOption();
        String property1 = "key1";
        String value1 = "value1";
        String property2 = "key2";
        String value2 = "value2";
        commandLine.addProperty(option, property1, value1);
        commandLine.addProperty(option, property2, value2);
        Set<String> expectedProperties = new HashSet<>();
        expectedProperties.add(property1);
        expectedProperties.add(property2);
        assertEquals(expectedProperties, commandLine.getProperties(option));
    }

    @Test
    public void testLooksLikeOption() {
        String option = "-o";
        assertTrue(commandLine.looksLikeOption(option));
    }

    @Test
    public void testToString() {
        List<String> arguments = new ArrayList<>();
        arguments.add("arg1");
        arguments.add("arg2");
        commandLine = new WriteableCommandLineImpl(null, arguments);
        assertEquals("arg1 arg2", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        Option option1 = new OptionBuilder("-o1").create();
        Option option2 = new OptionBuilder("-o2").create();
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        List<Option> expectedOptions = new ArrayList<>();
        expectedOptions.add(option1);
        expectedOptions.add(option2);
        assertEquals(expectedOptions, commandLine.getOptions());
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = new OptionBuilder("-o1").create();
        Option option2 = new OptionBuilder("-o2").create();
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        Set<String> expectedTriggers = new HashSet<>();
        expectedTriggers.add("-o1");
        expectedTriggers.add("-o2");
        assertEquals(expectedTriggers, commandLine.getOptionTriggers());
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new OptionBuilder("-o").create();
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default");
        commandLine.setDefaultValue(option, defaultValues);
        assertEquals(defaultValues, commandLine.getValues(option, null));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new OptionBuilder("-s").create();
        boolean defaultSwitch = true;
        commandLine.setDefaultSwitch(option, defaultSwitch);
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testGetNormalised() {
        List<String> expectedNormalised = new ArrayList<>();
        expectedNormalised.add("arg1");
        expectedNormalised.add("arg2");
        List<String> arguments = new ArrayList<>();
        arguments.add("arg1");
        arguments.add("arg2");
        commandLine = new WriteableCommandLineImpl(null, arguments);
        assertEquals(expectedNormalised, commandLine.getNormalised());
    }
}
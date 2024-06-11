import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineImplTest {

    private Option option;
    private WriteableCommandLineImpl commandLine;
    private List arguments;

    @Before
    public void setUp() {
        arguments = new ArrayList();
        option = new Option("test");
        commandLine = new WriteableCommandLineImpl(option, arguments);
    }

    @Test
    public void shouldAddOption() {
        Option addedOption = new Option("test2");
        commandLine.addOption(addedOption);
        assertTrue(commandLine.getOptions().contains(addedOption));
    }

    @Test
    public void shouldAddValue() {
        Object value = new Object();
        commandLine.addValue(option, value);
        assertTrue(commandLine.getValues(option, null).contains(value));
    }

    @Test
    public void shouldAddSwitch() {
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, null));
    }

    @Test
    public void shouldReturnTrueIfOptionExists() {
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void shouldReturnOptionIfExists() {
        Option returnedOption = commandLine.getOption("test");
        assertEquals(option, returnedOption);
    }

    @Test
    public void shouldReturnValuesIfExists() {
        Object value = new Object();
        List values = new ArrayList();
        values.add(value);
        commandLine.addValue(option, value);
        List returnedValues = commandLine.getValues(option, values);
        assertEquals(values, returnedValues);
    }

    @Test
    public void shouldReturnDefaultValuesIfNoValuesExist() {
        Object defaultValue = new Object();
        List defaultValues = new ArrayList();
        defaultValues.add(defaultValue);
        commandLine.setDefaultValues(option, defaultValues);
        List returnedValues = commandLine.getValues(option, null);
        assertEquals(defaultValues, returnedValues);
    }

    @Test
    public void shouldReturnDefaultValuesIfValuesExist() {
        Object defaultValue = new Object();
        Object value = new Object();
        List defaultValues = new ArrayList();
        defaultValues.add(defaultValue);
        commandLine.addValue(option, value);
        commandLine.setDefaultValues(option, defaultValues);
        List returnedValues = commandLine.getValues(option, null);
        assertEquals(values, returnedValues);
    }

    @Test
    public void shouldReturnEmptyListIfNoValuesExistAndNoDefaultValues() {
        List returnedValues = commandLine.getValues(option, null);
        assertTrue(returnedValues.isEmpty());
    }

    @Test
    public void shouldReturnSwitchIfExists() {
        commandLine.addSwitch(option, true);
        Boolean returnedSwitch = commandLine.getSwitch(option, null);
        assertEquals(true, returnedSwitch);
    }

    @Test
    public void shouldReturnDefaultSwitchIfSwitchDoesNotExist() {
        Boolean defaultSwitch = true;
        commandLine.setDefaultSwitch(option, defaultSwitch);
        Boolean returnedSwitch = commandLine.getSwitch(option, null);
        assertEquals(defaultSwitch, returnedSwitch);
    }

    @Test
    public void shouldReturnDefaultSwitchIfSwitchExists() {
        commandLine.addSwitch(option, false);
        Boolean defaultSwitch = true;
        commandLine.setDefaultSwitch(option, defaultSwitch);
        Boolean returnedSwitch = commandLine.getSwitch(option, null);
        assertEquals(false, returnedSwitch);
    }

    @Test
    public void shouldAddProperty() {
        String property = "key";
        String value = "value";
        commandLine.addProperty(property, value);
        assertEquals(value, commandLine.getProperty(property, null));
    }

    @Test
    public void shouldReturnDefaultValueIfPropertyDoesNotExist() {
        String defaultValue = "defaultValue";
        String property = "key";
        assertEquals(defaultValue, commandLine.getProperty(property, defaultValue));
    }

    @Test
    public void shouldReturnProperties() {
        String property1 = "key1";
        String property2 = "key2";
        String value1 = "value1";
        String value2 = "value2";
        commandLine.addProperty(property1, value1);
        commandLine.addProperty(property2, value2);
        assertTrue(commandLine.getProperties().contains(property1));
        assertTrue(commandLine.getProperties().contains(property2));
    }

    @Test
    public void shouldReturnTrueIfTriggerLooksLikeOption() {
        String trigger = "-test";
        assertTrue(commandLine.looksLikeOption(trigger));
    }

    @Test
    public void shouldReturnFalseIfTriggerDoesNotLookLikeOption() {
        String trigger = "test";
        assertFalse(commandLine.looksLikeOption(trigger));
    }

    @Test
    public void shouldPrintCommandLineAsString() {
        String argument1 = "arg1";
        String argument2 = "arg2";
        arguments.add(argument1);
        arguments.add(argument2);
        assertEquals(argument1 + " " + argument2, commandLine.toString());
    }

    @Test
    public void shouldReturnOptions() {
        Option option1 = new Option("option1");
        Option option2 = new Option("option2");
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        assertTrue(commandLine.getOptions().contains(option1));
        assertTrue(commandLine.getOptions().contains(option2));
    }

    @Test
    public void shouldReturnOptionTriggers() {
        String trigger1 = "trigger1";
        String trigger2 = "trigger2";
        Option option1 = new Option(trigger1);
        Option option2 = new Option(trigger2);
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        assertTrue(commandLine.getOptionTriggers().contains(trigger1));
        assertTrue(commandLine.getOptionTriggers().contains(trigger2));
    }

    @Test
    public void shouldSetDefaultValues() {
        Option option1 = new Option("option1");
        Option option2 = new Option("option2");
        List defaults1 = new ArrayList();
        List defaults2 = new ArrayList();
        defaults1.add("default1");
        defaults2.add("default2");
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        commandLine.setDefaultValues(option1, defaults1);
        commandLine.setDefaultValues(option2, defaults2);
        assertEquals(defaults1, commandLine.getValues(option1, null));
        assertEquals(defaults2, commandLine.getValues(option2, null));
    }

    @Test
    public void shouldNotSetDefaultValuesIfNull() {
        Option option1 = new Option("option1");
        List defaults1 = new ArrayList();
        defaults1.add("default1");
        commandLine.addOption(option1);
        commandLine.setDefaultValues(option1, defaults1);
        commandLine.setDefaultValues(option1, null);
        assertTrue(commandLine.getValues(option1, null).isEmpty());
    }

    @Test
    public void shouldSetDefaultSwitch() {
        Option option1 = new Option("option1");
        Boolean defaultSwitch = true;
        commandLine.addOption(option1);
        commandLine.setDefaultSwitch(option1, defaultSwitch);
        assertEquals(defaultSwitch, commandLine.getSwitch(option1, null));
    }

    @Test
    public void shouldNotSetDefaultSwitchIfNull() {
        Option option1 = new Option("option1");
        Boolean defaultSwitch = true;
        commandLine.addOption(option1);
        commandLine.setDefaultSwitch(option1, defaultSwitch);
        commandLine.setDefaultSwitch(option1, null);
        assertNull(commandLine.getSwitch(option1, null));
    }

    @Test
    public void shouldReturnNormalisedArguments() {
        String argument1 = "arg1";
        String argument2 = "arg2";
        arguments.add(argument1);
        arguments.add(argument2);
        assertTrue(commandLine.getNormalised().contains(argument1));
        assertTrue(commandLine.getNormalised().contains(argument2));
    }
}
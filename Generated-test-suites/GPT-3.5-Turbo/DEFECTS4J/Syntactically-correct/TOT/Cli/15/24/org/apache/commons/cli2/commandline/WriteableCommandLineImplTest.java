import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import java.util.*;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;
    private Option mockOption;

    @Before
    public void setUp() {
        mockOption = Mockito.mock(Option.class);
        List<String> arguments = new ArrayList<>();
        commandLine = new WriteableCommandLineImpl(mockOption, arguments);
    }

    @Test
    public void addOption_shouldAddOptionToListAndMap() {
        Option testOption = new Option("test", "test description");
        commandLine.addOption(testOption);

        boolean hasOption = commandLine.hasOption(testOption);
        Option retrievedOption = commandLine.getOption("test");

        Assert.assertTrue(hasOption);
        Assert.assertEquals(testOption, retrievedOption);
    }

    @Test
    public void addValue_shouldAddValueToListForGivenOption() {
        Argument testArgument = new Argument("test", "test description");
        Object testValue = new Object();

        commandLine.addValue(testArgument, testValue);

        List<Object> retrievedValues = commandLine.getValues(testArgument, null);

        Assert.assertTrue(retrievedValues.contains(testValue));
    }

    @Test
    public void addSwitch_shouldAddSwitchToMapForGivenOption() {
        Option testOption = new Option("test", "test description");
        boolean testValue = true;

        commandLine.addSwitch(testOption, testValue);

        Boolean retrievedSwitch = commandLine.getSwitch(testOption, null);

        Assert.assertEquals(Boolean.TRUE, retrievedSwitch);
    }

    @Test
    public void hasOption_shouldReturnFalseWhenOptionNotAdded() {
        Option testOption = new Option("test", "test description");

        boolean hasOption = commandLine.hasOption(testOption);

        Assert.assertFalse(hasOption);
    }

    @Test
    public void getOption_shouldReturnNullWhenOptionNotAdded() {
        Option testOption = new Option("test", "test description");

        Option retrievedOption = commandLine.getOption("test");

        Assert.assertNull(retrievedOption);
    }

    @Test
    public void getValues_shouldReturnDefaultValuesWhenNoValuesAdded() {
        Argument testArgument = new Argument("test", "test description");
        List<String> defaultValues = Arrays.asList("default1", "default2");

        List<String> retrievedValues = commandLine.getValues(testArgument, defaultValues);

        Assert.assertEquals(defaultValues, retrievedValues);
    }

    @Test
    public void getUndefaultedValues_shouldReturnEmptyListWhenNoValuesAdded() {
        Argument testArgument = new Argument("test", "test description");

        List<Object> retrievedValues = commandLine.getUndefaultedValues(testArgument);

        Assert.assertTrue(retrievedValues.isEmpty());
    }

    @Test
    public void getSwitch_shouldReturnDefaultSwitchWhenNoSwitchAdded() {
        Option testOption = new Option("test", "test description");
        Boolean defaultSwitch = true;

        Boolean retrievedSwitch = commandLine.getSwitch(testOption, defaultSwitch);

        Assert.assertEquals(defaultSwitch, retrievedSwitch);
    }

    @Test
    public void getProperty_shouldReturnNullWhenNoPropertyAdded() {
        String propertyName = "testProperty";

        String retrievedValue = commandLine.getProperty(propertyName);

        Assert.assertNull(retrievedValue);
    }

    @Test
    public void addProperty_shouldAddPropertyToOptionProperties() {
        Option testOption = new Option("test", "test description");
        String propertyName = "testProperty";
        String propertyValue = "testValue";

        commandLine.addProperty(testOption, propertyName, propertyValue);

        String retrievedValue = commandLine.getProperty(testOption, propertyName, null);

        Assert.assertEquals(propertyValue, retrievedValue);
    }

    @Test
    public void getProperties_shouldReturnEmptySetWhenNoPropertiesAdded() {
        Option testOption = new Option("test", "test description");

        Set<String> retrievedProperties = commandLine.getProperties(testOption);

        Assert.assertTrue(retrievedProperties.isEmpty());
    }

    @Test
    public void looksLikeOption_shouldReturnTrueWhenTriggerStartsWithPrefix() {
        String trigger = "--test";

        boolean looksLikeOption = commandLine.looksLikeOption(trigger);

        Assert.assertTrue(looksLikeOption);
    }

    @Test
    public void looksLikeOption_shouldReturnFalseWhenTriggerDoesNotStartWithPrefix() {
        String trigger = "test";

        boolean looksLikeOption = commandLine.looksLikeOption(trigger);

        Assert.assertFalse(looksLikeOption);
    }

    @Test
    public void toString_shouldReturnFormattedStringRepresentation() {
        commandLine.addOption(new Option("test1", "test description"));
        commandLine.addOption(new Option("test2", "test description"));

        String stringRepresentation = commandLine.toString();

        Assert.assertEquals("test1 test2", stringRepresentation);
    }

    @Test
    public void getOptions_shouldReturnAllAddedOptions() {
        Option testOption1 = new Option("test1", "test1 description");
        Option testOption2 = new Option("test2", "test2 description");
        commandLine.addOption(testOption1);
        commandLine.addOption(testOption2);

        List<Option> options = commandLine.getOptions();

        Assert.assertTrue(options.contains(testOption1));
        Assert.assertTrue(options.contains(testOption2));
        Assert.assertEquals(2, options.size());
    }

    @Test
    public void getOptionTriggers_shouldReturnSetOfAllOptionTriggers() {
        Option testOption1 = new Option("test1", "test1 description");
        Option testOption2 = new Option("test2", "test2 description");
        commandLine.addOption(testOption1);
        commandLine.addOption(testOption2);

        Set<String> optionTriggers = commandLine.getOptionTriggers();

        Assert.assertTrue(optionTriggers.contains("test1"));
        Assert.assertTrue(optionTriggers.contains("test2"));
        Assert.assertEquals(2, optionTriggers.size());
    }

    @Test
    public void setDefaultValues_shouldSetDefaultValuesForOption() {
        Option testOption = new Option("test", "test description");
        List<String> defaultValues = Arrays.asList("default1", "default2");

        commandLine.setDefaultValues(testOption, defaultValues);

        List<Object> retrievedValues = commandLine.getValues(testOption, null);

        Assert.assertEquals(defaultValues, retrievedValues);
    }

    @Test
    public void setDefaultSwitch_shouldSetDefaultSwitchForOption() {
        Option testOption = new Option("test", "test description");
        Boolean defaultSwitch = true;

        commandLine.setDefaultSwitch(testOption, defaultSwitch);

        Boolean retrievedSwitch = commandLine.getSwitch(testOption, null);

        Assert.assertEquals(defaultSwitch, retrievedSwitch);
    }

    @Test
    public void getNormalised_shouldReturnUnmodifiableList() {
        Option testOption = new Option("test", "test description");
        commandLine.addOption(testOption);

        List<String> normalisedList = commandLine.getNormalised();
        normalisedList.add("extraArgument");

        Assert.assertEquals(Collections.EMPTY_LIST, commandLine.getNormalised());
    }
}
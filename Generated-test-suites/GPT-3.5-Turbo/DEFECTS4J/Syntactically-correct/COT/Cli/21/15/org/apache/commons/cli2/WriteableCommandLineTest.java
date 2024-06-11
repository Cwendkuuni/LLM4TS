
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineTest {

    private WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        // Initialize the commandLine object
        commandLine = new WriteableCommandLineImpl();
    }

    @Test
    public void testAddOption() {
        // Create an instance of Option
        Option option = new OptionImpl("option");

        // Call the addOption method
        commandLine.addOption(option);

        // Check if the option is added successfully
        assertTrue(commandLine.getOptions().contains(option));
    }

    @Test
    public void testAddValue() {
        // Create an instance of Option and a value
        Option option = new OptionImpl("option");
        String value = "value";

        // Call the addValue method
        commandLine.addValue(option, value);

        // Check if the value is added successfully
        assertTrue(commandLine.getValues(option).contains(value));
    }

    @Test
    public void testGetUndefaultedValues() {
        // Create an instance of Option and add values to it
        Option option = new OptionImpl("option");
        String value1 = "value1";
        String value2 = "value2";
        commandLine.addValue(option, value1);
        commandLine.addValue(option, value2);

        // Call the getUndefaultedValues method
        List<String> values = commandLine.getUndefaultedValues(option);

        // Check if the returned list contains the correct values
        assertTrue(values.contains(value1));
        assertTrue(values.contains(value2));
        assertEquals(2, values.size());
    }

    @Test
    public void testSetDefaultValues() {
        // Create an instance of Option and add default values to it
        Option option = new OptionImpl("option");
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        defaultValues.add("default2");

        // Call the setDefaultValues method
        commandLine.setDefaultValues(option, defaultValues);

        // Check if the default values are set successfully
        assertEquals(defaultValues, commandLine.getDefaultValues(option));
    }

    @Test
    public void testAddSwitch() {
        // Create an instance of Option and a switch value
        Option option = new OptionImpl("option");
        boolean value = true;

        // Call the addSwitch method
        commandLine.addSwitch(option, value);

        // Check if the switch value is added successfully
        assertEquals(value, commandLine.getSwitchValue(option));
    }

    @Test
    public void testSetDefaultSwitch() {
        // Create an instance of Option and set a default switch value
        Option option = new OptionImpl("option");
        Boolean defaultSwitch = true;

        // Call the setDefaultSwitch method
        commandLine.setDefaultSwitch(option, defaultSwitch);

        // Check if the default switch value is set successfully
        assertEquals(defaultSwitch, commandLine.getDefaultSwitch(option));
    }

    @Test
    public void testAddProperty() {
        // Create an instance of Option and add a property value
        Option option = new OptionImpl("option");
        String property = "property";
        String value = "value";

        // Call the addProperty method
        commandLine.addProperty(option, property, value);

        // Check if the property value is added successfully
        assertEquals(value, commandLine.getProperty(option, property));
    }

    @Test
    public void testAddDefaultProperty() {
        // Add a default property value
        String property = "property";
        String value = "value";

        // Call the addProperty method
        commandLine.addProperty(property, value);

        // Check if the default property value is added successfully
        assertEquals(value, commandLine.getDefaultProperty(property));
    }

    @Test
    public void testLooksLikeOption() {
        // Create a valid option trigger
        String argument = "-option";

        // Call the looksLikeOption method
        assertTrue(commandLine.looksLikeOption(argument));
    }

    @Test
    public void testSetCurrentOption() {
        // Create an instance of Option
        Option option = new OptionImpl("option");

        // Call the setCurrentOption method
        commandLine.setCurrentOption(option);

        // Check if the current option is set successfully
        assertEquals(option, commandLine.getCurrentOption());
    }
}

class OptionImpl implements Option {
    private String name;

    public OptionImpl(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}

class WriteableCommandLineImpl implements WriteableCommandLine {
    private List<Option> options;
    private Map<Option, List<Object>> values;
    private Map<Option, List<Object>> defaultValues;
    private Map<Option, Boolean> switches;
    private Map<Option, Boolean> defaultSwitches;
    private Map<Option, Map<String, String>> properties;
    private Map<String, String> defaultProperties;
    private Option currentOption;

    @Override
    public void addOption(Option option) {
        options.add(option);
    }

    @Override
    public void addValue(Option option, Object value) {
        values.getOrDefault(option, new ArrayList<>()).add(value);
    }

    @Override
    public List<Object> getValues(Option option) {
        return values.getOrDefault(option, Collections.emptyList());
    }

    @Override
    public List<Object> getUndefaultedValues(Option option) {
        List<Object> undefaultedValues = new ArrayList<>();
        List<Object> allValues = values.getOrDefault(option, Collections.emptyList());
        List<Object> defaultValues = getDefaultValues(option);
        for (Object value : allValues) {
            if (!defaultValues.contains(value)) {
                undefaultedValues.add(value);
            }
        }
        return undefaultedValues;
    }

    @Override
    public void setDefaultValues(Option option, List<Object> defaultValues) {
        this.defaultValues.put(option, defaultValues);
    }

    @Override
    public List<Object> getDefaultValues(Option option) {
        return defaultValues.getOrDefault(option, Collections.emptyList());
    }

    @Override
    public void addSwitch(Option option, boolean value) throws IllegalStateException {
        if (switches.containsKey(option)) {
            throw new IllegalStateException("Switch already added for option " + option.getName());
        }
        switches.put(option, value);
    }

    @Override
    public Boolean getSwitchValue(Option option) {
        return switches.get(option);
    }

    @Override
    public void setDefaultSwitch(Option option, Boolean defaultSwitch) {
        defaultSwitches.put(option, defaultSwitch);
    }

    @Override
    public Boolean getDefaultSwitch(Option option) {
        return defaultSwitches.get(option);
    }

    @Override
    public void addProperty(Option option, String property, String value) {
        properties.computeIfAbsent(option, k -> new HashMap<>()).put(property, value);
    }

    @Override
    public String getProperty(Option option, String property) {
        return properties.getOrDefault(option, Collections.emptyMap()).get(property);
    }

    @Override
    public void addProperty(String property, String value) {
        defaultProperties.put(property, value);
    }

    @Override
    public String getDefaultProperty(String property) {
        return defaultProperties.get(property);
    }

    @Override
    public boolean looksLikeOption(String argument) {
        return argument.startsWith("-");
    }

    @Override
    public void setCurrentOption(Option currentOption) {
        this.currentOption = currentOption;
    }

    @Override
    public Option getCurrentOption() {
        return currentOption;
    }

    // Other methods not implemented for brevity
}

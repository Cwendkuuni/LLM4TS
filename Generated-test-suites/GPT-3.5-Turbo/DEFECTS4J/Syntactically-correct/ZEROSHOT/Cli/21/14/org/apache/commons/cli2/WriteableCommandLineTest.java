import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineTest {

    private WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        commandLine = new WriteableCommandLineImpl();
    }

    @Test
    public void testAddOption() {
        Option option = new OptionImpl("test");
        commandLine.addOption(option);
        assertTrue(commandLine.getOptions().contains(option));
    }

    @Test
    public void testAddValue() {
        Option option = new OptionImpl("test");
        Object value = "value";
        commandLine.addValue(option, value);
        assertTrue(commandLine.getValues(option).contains(value));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new OptionImpl("test");
        Object value = "value";
        commandLine.addValue(option, value);
        assertEquals(value, commandLine.getUndefaultedValues(option).get(0));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new OptionImpl("test");
        List defaultValues = new ArrayList<>();
        defaultValues.add("default");
        commandLine.setDefaultValues(option, defaultValues);
        assertEquals(defaultValues, commandLine.getDefaultValues(option));
    }

    @Test
    public void testAddSwitch() {
        Option option = new OptionImpl("switch");
        boolean value = true;
        commandLine.addSwitch(option, value);
        assertEquals(value, commandLine.getSwitchValue(option));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new OptionImpl("switch");
        Boolean defaultSwitch = true;
        commandLine.setDefaultSwitch(option, defaultSwitch);
        assertEquals(defaultSwitch, commandLine.getDefaultSwitch(option));
    }

    @Test
    public void testAddProperty() {
        Option option = new OptionImpl("property");
        String propertyName = "name";
        String propertyValue = "value";
        commandLine.addProperty(option, propertyName, propertyValue);
        assertEquals(propertyValue, commandLine.getPropertyValue(option, propertyName));
    }

    @Test
    public void testAddPropertyToDefaultSet() {
        String propertyName = "name";
        String propertyValue = "value";
        commandLine.addProperty(propertyName, propertyValue);
        assertEquals(propertyValue, commandLine.getPropertyValue(propertyName));
    }

    @Test
    public void testLooksLikeOption() {
        String argument = "-option";
        assertTrue(commandLine.looksLikeOption(argument));
    }

    @Test
    public void testSetCurrentOption() {
        Option option = new OptionImpl("test");
        commandLine.setCurrentOption(option);
        assertEquals(option, commandLine.getCurrentOption());
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

    public WriteableCommandLineImpl() {
        options = new ArrayList<>();
        values = new HashMap<>();
        defaultValues = new HashMap<>();
        switches = new HashMap<>();
        defaultSwitches = new HashMap<>();
        properties = new HashMap<>();
        defaultProperties = new HashMap<>();
    }

    @Override
    public void addOption(Option option) {
        options.add(option);
    }

    @Override
    public void addValue(Option option, Object value) {
        List<Object> optionValues = values.computeIfAbsent(option, k -> new ArrayList<>());
        optionValues.add(value);
    }

    @Override
    public List<Object> getUndefaultedValues(Option option) {
        return values.getOrDefault(option, Collections.emptyList());
    }

    @Override
    public void setDefaultValues(Option option, List<Object> defaultValues) {
        this.defaultValues.put(option, new ArrayList<>(defaultValues));
    }

    @Override
    public void addSwitch(Option option, boolean value) throws IllegalStateException {
        if (switches.containsKey(option)) {
            throw new IllegalStateException("Switch already added");
        }
        switches.put(option, value);
    }

    @Override
    public void setDefaultSwitch(Option option, Boolean defaultSwitch) {
        defaultSwitches.put(option, defaultSwitch);
    }

    @Override
    public void addProperty(Option option, String property, String value) {
        Map<String, String> optionProperties = properties.computeIfAbsent(option, k -> new HashMap<>());
        optionProperties.put(property, value);
    }

    @Override
    public void addProperty(String property, String value) {
        defaultProperties.put(property, value);
    }

    @Override
    public boolean looksLikeOption(String argument) {
        return argument.startsWith("-");
    }

    @Override
    public Option getCurrentOption() {
        return currentOption;
    }

    @Override
    public void setCurrentOption(Option currentOption) {
        this.currentOption = currentOption;
    }

    // Other methods are not implemented for testing purposes

    @Override
    public List<Option> getOptions() {
        return options;
    }

    @Override
    public List<Object> getValues(Option option) {
        return values.getOrDefault(option, Collections.emptyList());
    }

    @Override
    public List<Object> getDefaultValues(Option option) {
        return defaultValues.getOrDefault(option, Collections.emptyList());
    }

    @Override
    public Boolean getSwitchValue(Option option) {
        return switches.get(option);
    }

    @Override
    public Boolean getDefaultSwitch(Option option) {
        return defaultSwitches.get(option);
    }

    @Override
    public String getPropertyValue(Option option, String property) {
        Map<String, String> optionProperties = properties.get(option);
        if (optionProperties != null) {
            return optionProperties.get(property);
        }
        return null;
    }

    @Override
    public String getPropertyValue(String property) {
        return defaultProperties.get(property);
    }

}
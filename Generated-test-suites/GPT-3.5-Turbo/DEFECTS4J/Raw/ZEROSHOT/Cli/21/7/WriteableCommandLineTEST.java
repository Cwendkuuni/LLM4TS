import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineTest {

    private WriteableCommandLine commandLine;
    private Option option;

    @Before
    public void setUp() {
        commandLine = new WriteableCommandLineImpl();
        option = new OptionImpl();
    }

    @Test
    public void testAddOption() {
        commandLine.addOption(option);

        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        Object value = new Object();
        commandLine.addValue(option, value);

        assertTrue(commandLine.hasOptionValue(option, value));
    }

    @Test
    public void testGetUndefaultedValues() {
        Object value1 = new Object();
        Object value2 = new Object();
        commandLine.addValue(option, value1);
        commandLine.addValue(option, value2);

        List values = commandLine.getUndefaultedValues(option);

        assertEquals(2, values.size());
        assertTrue(values.contains(value1));
        assertTrue(values.contains(value2));
    }

    @Test
    public void testSetDefaultValues() {
        List defaultValues = new ArrayList();
        defaultValues.add(new Object());
        defaultValues.add(new Object());
        commandLine.setDefaultValues(option, defaultValues);

        List values = commandLine.getUndefaultedValues(option);

        assertEquals(defaultValues, values);
    }

    @Test
    public void testAddSwitch() {
        boolean value = true;
        commandLine.addSwitch(option, value);

        assertTrue(commandLine.hasSwitch(option, value));
    }

    @Test
    public void testSetDefaultSwitch() {
        Boolean defaultSwitch = true;
        commandLine.setDefaultSwitch(option, defaultSwitch);

        assertTrue(commandLine.hasSwitch(option, defaultSwitch));
    }

    @Test
    public void testAddProperty() {
        String property = "property";
        String value = "value";
        commandLine.addProperty(option, property, value);

        assertTrue(commandLine.hasProperty(option, property, value));
    }

    @Test
    public void testAddPropertyToDefaultSet() {
        String property = "property";
        String value = "value";
        commandLine.addProperty(property, value);

        assertTrue(commandLine.hasDefaultProperty(property, value));
    }

    @Test
    public void testLooksLikeOption() {
        String argument = "-option";

        assertTrue(commandLine.looksLikeOption(argument));
    }
}

class WriteableCommandLineImpl implements WriteableCommandLine {

    private List<Option> options;
    private Map<Option, List<Object>> optionValues;
    private Map<Option, List<Object>> defaultValues;
    private Map<Option, List<Boolean>> switches;
    private Map<Option, List<Boolean>> defaultSwitches;
    private Map<Option, Map<String, String>> properties;
    private Map<String, String> defaultProperties;

    public WriteableCommandLineImpl() {
        options = new ArrayList<>();
        optionValues = new HashMap<>();
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
        if (!optionValues.containsKey(option)) {
            optionValues.put(option, new ArrayList<>());
        }
        optionValues.get(option).add(value);
    }

    @Override
    public List getUndefaultedValues(Option option) {
        if (optionValues.containsKey(option)) {
            return optionValues.get(option);
        }
        return new ArrayList<>();
    }

    @Override
    public void setDefaultValues(Option option, List defaultValues) {
        this.defaultValues.put(option, defaultValues);
    }

    @Override
    public void addSwitch(Option option, boolean value) throws IllegalStateException {
        if (!switches.containsKey(option)) {
            switches.put(option, new ArrayList<>());
        }
        switches.get(option).add(value);
    }

    @Override
    public void setDefaultSwitch(Option option, Boolean defaultSwitch) {
        if (!defaultSwitches.containsKey(option)) {
            defaultSwitches.put(option, new ArrayList<>());
        }
        defaultSwitches.get(option).add(defaultSwitch);
    }

    @Override
    public void addProperty(Option option, String property, String value) {
        if (!properties.containsKey(option)) {
            properties.put(option, new HashMap<>());
        }
        properties.get(option).put(property, value);
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
    public boolean hasOption(Option option) {
        return options.contains(option);
    }

    @Override
    public boolean hasOptionValue(Option option, Object value) {
        if (optionValues.containsKey(option)) {
            return optionValues.get(option).contains(value);
        }
        return false;
    }

    @Override
    public boolean hasSwitch(Option option, boolean value) {
        if (switches.containsKey(option)) {
            return switches.get(option).contains(value);
        }
        return false;
    }

    @Override
    public boolean hasDefaultSwitch(Option option, Boolean value) {
        if (defaultSwitches.containsKey(option)) {
            return defaultSwitches.get(option).contains(value);
        }
        return false;
    }

    @Override
    public boolean hasProperty(Option option, String property, String value) {
        if (properties.containsKey(option)) {
            Map<String, String> optionProperties = properties.get(option);
            if (optionProperties.containsKey(property)) {
                return optionProperties.get(property).equals(value);
            }
        }
        return false;
    }

    @Override
    public boolean hasDefaultProperty(String property, String value) {
        if (defaultProperties.containsKey(property)) {
            return defaultProperties.get(property).equals(value);
        }
        return false;
    }
}

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class WriteableCommandLineTest {

    private WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        commandLine = new WriteableCommandLine() {
            private List<Option> options = new ArrayList<>();
            private List<Object> values = new ArrayList<>();
            private List<Object> defaultValues = new ArrayList<>();
            private List<Boolean> switches = new ArrayList<>();
            private List<Boolean> defaultSwitches = new ArrayList<>();
            private List<String> properties = new ArrayList<>();

            @Override
            public void addOption(Option option) {
                options.add(option);
            }

            @Override
            public void addValue(Option option, Object value) {
                values.add(value);
            }

            @Override
            public List<Object> getValues(Option option) {
                return values;
            }

            @Override
            public void setDefaultValues(Option option, List defaultValues) {
                this.defaultValues = defaultValues;
            }

            @Override
            public void addSwitch(Option option, boolean value) {
                switches.add(value);
            }

            @Override
            public void setDefaultSwitch(Option option, Boolean defaultSwitch) {
                defaultSwitches.add(defaultSwitch);
            }

            @Override
            public void addProperty(String property, String value) {
                properties.add(value);
            }

            @Override
            public boolean looksLikeOption(String argument) {
                return argument.startsWith("-");
            }

            @Override
            public List<Option> getOptions() {
                return options;
            }
        };
    }

    @Test
    public void testAddOption() {
        Option option = new Option("a", "option", true, "option");
        commandLine.addOption(option);

        List<Option> options = commandLine.getOptions();
        assertTrue(options.contains(option));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("a", "option", true, "option");
        commandLine.addValue(option, "value");

        List<Object> values = commandLine.getValues(option);
        assertTrue(values.contains("value"));
    }

    @Test
    public void testGetValues() {
        Option option = new Option("a", "option", true, "option");
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List<Object> values = commandLine.getValues(option);
        assertEquals(2, values.size());
        assertTrue(values.contains("value1"));
        assertTrue(values.contains("value2"));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("a", "option", true, "option");
        List<Object> defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        defaultValues.add("default2");

        commandLine.setDefaultValues(option, defaultValues);

        List<Object> values = commandLine.getValues(option);
        assertTrue(values.contains("default1"));
        assertTrue(values.contains("default2"));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("a", "option", false, "option");
        commandLine.addSwitch(option, true);

        List<Option> options = commandLine.getOptions();
        assertTrue(options.contains(option));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("a", "option", false, "option");
        commandLine.setDefaultSwitch(option, true);

        List<Option> options = commandLine.getOptions();
        assertTrue(options.contains(option));
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty("property1", "value1");
        commandLine.addProperty("property2", "value2");

        List<String> properties = commandLine.getProperties();
        assertEquals(2, properties.size());
        assertTrue(properties.contains("value1"));
        assertTrue(properties.contains("value2"));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-option"));
    }
}

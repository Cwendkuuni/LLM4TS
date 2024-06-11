import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class WriteableCommandLineTest {

    private WriteableCommandLine writeableCommandLine;

    @Before
    public void setUp() {
        writeableCommandLine = new WriteableCommandLine() {
            private List<Option> options = new ArrayList<>();
            private List<Object> values = new ArrayList<>();
            private List<List<Object>> defaultValues = new ArrayList<>();
            private List<Boolean> switches = new ArrayList<>();
            private List<Boolean> defaultSwitches = new ArrayList<>();
            private List<String> properties = new ArrayList<>();
            private List<String> propertyValues = new ArrayList<>();

            @Override
            public void addOption(Option option) {
                options.add(option);
            }

            @Override
            public void addValue(Option option, Object value) {
                values.add(value);
            }

            @Override
            public List<?> getValues(Option option) {
                List<Object> optionValues = new ArrayList<>();
                for (int i = 0; i < options.size(); i++) {
                    if (options.get(i) == option) {
                        optionValues.add(values.get(i));
                    }
                }
                return optionValues;
            }

            @Override
            public void setDefaultValues(Option option, List defaultValues) {
                this.defaultValues.add(defaultValues);
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
                properties.add(property);
                propertyValues.add(value);
            }

            @Override
            public boolean looksLikeOption(String argument) {
                return argument.startsWith("-");
            }
        };
    }

    @Test
    public void testAddOption() {
        Option option = new Option("a");
        writeableCommandLine.addOption(option);

        assertTrue(writeableCommandLine.getOptions().contains(option));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("a");
        writeableCommandLine.addValue(option, "value");

        assertEquals("value", writeableCommandLine.getValues(option).get(0));
    }

    @Test
    public void testGetValues() {
        Option option1 = new Option("a");
        Option option2 = new Option("b");
        Option option3 = new Option("a");

        writeableCommandLine.addValue(option1, "value1");
        writeableCommandLine.addValue(option2, "value2");
        writeableCommandLine.addValue(option3, "value3");

        List<Object> expectedValues = new ArrayList<>();
        expectedValues.add("value1");
        expectedValues.add("value3");

        assertEquals(expectedValues, writeableCommandLine.getValues(option1));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("a");
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        defaultValues.add("default2");

        writeableCommandLine.setDefaultValues(option, defaultValues);

        assertEquals(defaultValues, writeableCommandLine.getDefaultValues(option));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("a");

        writeableCommandLine.addSwitch(option, true);

        assertTrue(writeableCommandLine.getSwitches(option).get(0));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("a");
        Boolean defaultSwitch = false;

        writeableCommandLine.setDefaultSwitch(option, defaultSwitch);

        assertEquals(false, writeableCommandLine.getDefaultSwitch(option));
    }

    @Test
    public void testAddProperty() {
        String property = "property1";
        String value = "value1";

        writeableCommandLine.addProperty(property, value);

        assertTrue(writeableCommandLine.getProperties().contains(property));
        assertTrue(writeableCommandLine.getPropertyValues().contains(value));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(writeableCommandLine.looksLikeOption("-a"));
        assertFalse(writeableCommandLine.looksLikeOption("value"));
    }
}
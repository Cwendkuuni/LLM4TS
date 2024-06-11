import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import java.util.ArrayList;

public class WriteableCommandLineTest {

    private WriteableCommandLine commandLine;
    private Option option;

    @Before
    public void setUp() {
        commandLine = new WriteableCommandLine() {
            List<Option> options = new ArrayList<>();
            List<Object> values = new ArrayList<>();
            List<List<Object>> undefaultedValues = new ArrayList<>();
            List<List<Object>> defaultValues = new ArrayList<>();
            List<Boolean> switches = new ArrayList<>();
            List<Boolean> defaultSwitches = new ArrayList<>();
            List<String> properties = new ArrayList<>();

            public void addOption(final Option option) {
                options.add(option);
            }

            public void addValue(final Option option, final Object value) {
                values.add(value);
            }

            public List getUndefaultedValues(final Option option) {
                return undefaultedValues.get(options.indexOf(option));
            }

            public void setDefaultValues(final Option option, final List defaultValues) {
                defaultValues.set(options.indexOf(option), defaultValues);
            }

            public void addSwitch(final Option option, final boolean value) throws IllegalStateException {
                if (switches.get(options.indexOf(option)) != null) {
                    throw new IllegalStateException("Switch already added");
                }
                switches.set(options.indexOf(option), value);
            }

            public void setDefaultSwitch(final Option option, final Boolean defaultSwitch) {
                defaultSwitches.set(options.indexOf(option), defaultSwitch);
            }

            public void addProperty(final Option option, final String property, final String value) {
                properties.add(property + "=" + value);
            }

            public void addProperty(final String property, final String value) {
                properties.add(property + "=" + value);
            }

            public boolean looksLikeOption(final String argument) {
                return argument.startsWith("-");
            }

            public void setCurrentOption(final Option currentOption) {
               // implementation not provided for test purposes
            }
        };

        option = new Option() {
            String name;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        };
    }

    @Test
    public void testAddOption() {
        Option option = new Option();
        commandLine.addOption(option);
        assertEquals(option, commandLine.getOptions().get(0));
    }

    @Test
    public void testAddValue() {
        commandLine.addValue(option, "value");
        assertEquals("value", commandLine.getValues().get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option1 = new Option();
        Option option2 = new Option();
        commandLine.addValue(option1, "value1");
        commandLine.addValue(option1, "value2");
        commandLine.addValue(option2, "value3");
        assertEquals(2, commandLine.getUndefaultedValues(option1).size());
        assertEquals(1, commandLine.getUndefaultedValues(option2).size());
        assertTrue(commandLine.getUndefaultedValues(option1).contains("value1"));
        assertTrue(commandLine.getUndefaultedValues(option1).contains("value2"));
        assertTrue(commandLine.getUndefaultedValues(option2).contains("value3"));
    }

    @Test
    public void testSetDefaultValues() {
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        defaultValues.add("default2");
        commandLine.setDefaultValues(option, defaultValues);
        assertEquals(defaultValues, commandLine.getDefaultValues().get(0));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitch() throws IllegalStateException {
        Option option = new Option();
        commandLine.addSwitch(option, true);
        assertEquals(true, commandLine.getSwitches().get(0));
        commandLine.addSwitch(option, false);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option();
        commandLine.setDefaultSwitch(option, true);
        assertEquals(true, commandLine.getDefaultSwitches().get(0));
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty(option, "property1", "value1");
        commandLine.addProperty(option, "property2", "value2");
        assertEquals("property1=value1", commandLine.getProperties().get(0));
        assertEquals("property2=value2", commandLine.getProperties().get(1));
    }

    @Test
    public void testAddProperty_DefaultSet() {
        commandLine.addProperty("property1", "value1");
        commandLine.addProperty("property2", "value2");
        assertEquals("property1=value1", commandLine.getProperties().get(0));
        assertEquals("property2=value2", commandLine.getProperties().get(1));
    }

    @Test
    public void testLooksLikeOption_True() {
        assertTrue(commandLine.looksLikeOption("-Option"));
        assertTrue(commandLine.looksLikeOption("--Option"));
    }

    @Test
    public void testLooksLikeOption_False() {
        assertFalse(commandLine.looksLikeOption("Argument"));
        assertFalse(commandLine.looksLikeOption("Value"));
    }
}
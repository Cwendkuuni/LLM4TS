import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;
import org.apache.commons.cli2.Option;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option.Builder("opt").create();
        commandLine.addOption(option);
        assertTrue(commandLine.getOptions().contains(option));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option.Builder("opt").create();
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List<String> values = commandLine.getValues(option);
        assertEquals(2, values.size());
        assertTrue(values.contains("value1"));
        assertTrue(values.contains("value2"));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option.Builder("opt").create();
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List<String> undefaultedValues = commandLine.getUndefaultedValues(option);
        assertEquals(2, undefaultedValues.size());
        assertTrue(undefaultedValues.contains("value1"));
        assertTrue(undefaultedValues.contains("value2"));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option.Builder("opt").create();
        commandLine.setDefaultValues(option, List.of("default1", "default2"));
        List<String> defaultValues = commandLine.getDefaultValues(option);
        assertEquals(2, defaultValues.size());
        assertTrue(defaultValues.contains("default1"));
        assertTrue(defaultValues.contains("default2"));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option.Builder("opt").create();
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitchValue(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitch_Exception() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option.Builder("opt").create();
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option.Builder("opt").create();
        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.getDefaultSwitch(option));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option.Builder("opt").create();
        commandLine.addProperty(option, "property1", "value1");
        commandLine.addProperty(option, "property2", "value2");
        String value1 = commandLine.getProperty(option, "property1");
        String value2 = commandLine.getProperty(option, "property2");
        assertEquals("value1", value1);
        assertEquals("value2", value2);
    }

    @Test
    public void testAddProperty_DefaultSet() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        commandLine.addProperty("property1", "value1");
        commandLine.addProperty("property2", "value2");
        String value1 = commandLine.getProperty(null, "property1");
        String value2 = commandLine.getProperty(null, "property2");
        assertEquals("value1", value1);
        assertEquals("value2", value2);
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        assertTrue(commandLine.looksLikeOption("-opt"));
        assertFalse(commandLine.looksLikeOption("value"));
    }

    @Test
    public void testGetCurrentOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option.Builder("opt").create();
        commandLine.setCurrentOption(option);
        assertEquals(option, commandLine.getCurrentOption());
    }

    @Test
    public void testSetCurrentOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option.Builder("opt").create();
        commandLine.setCurrentOption(option);
        assertEquals(option, commandLine.getCurrentOption());
    }

    private class WriteableCommandLineImpl implements WriteableCommandLine {

        private List<Option> options;
        private Option currentOption;
        private List<String> values;
        private List<String> defaultValues;
        private boolean switchValue;
        private Boolean defaultSwitch;
        private String property;

        @Override
        public void addOption(final Option option) {
            options.add(option);
        }

        @Override
        public void addValue(final Option option, final Object value) {
            values.add(String.valueOf(value));
        }

        @Override
        public List getUndefaultedValues(final Option option) {
            return values;
        }

        @Override
        public void setDefaultValues(final Option option, final List defaultValues) {
            this.defaultValues = defaultValues;
        }

        @Override
        public void addSwitch(final Option option, final boolean value) throws IllegalStateException {
            if (switchValue) {
                throw new IllegalStateException("Switch already added");
            }
            switchValue = value;
        }

        @Override
        public void setDefaultSwitch(final Option option, final Boolean defaultSwitch) {
            this.defaultSwitch = defaultSwitch;
        }

        @Override
        public void addProperty(final Option option, final String property, final String value) {
            this.property = value;
        }

        @Override
        public void addProperty(final String property, final String value) {
            this.property = value;
        }

        @Override
        public boolean looksLikeOption(final String argument) {
            return argument.startsWith("-");
        }

        @Override
        public Option getCurrentOption() {
            return currentOption;
        }

        @Override
        public void setCurrentOption(final Option currentOption) {
            this.currentOption = currentOption;   
        }
    }
}
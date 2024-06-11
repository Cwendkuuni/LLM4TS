import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;

public class WriteableCommandLineTest {

    private WriteableCommandLine commandLine;
    private Option option;

    @Before
    public void setUp() {
        commandLine = new WriteableCommandLineImpl();
        option = new OptionImpl("test");
    }

    @Test
    public void testAddOption() {
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption("test"));
    }

    @Test
    public void testAddValue() {
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List<String> values = commandLine.getUndefaultedValues(option);
        assertEquals(2, values.size());
        assertTrue(values.contains("value1"));
        assertTrue(values.contains("value2"));
    }

    @Test
    public void testGetUndefaultedValues() {
        List<String> values = commandLine.getUndefaultedValues(option);
        assertEquals(0, values.size());
    }

    @Test
    public void testSetDefaultValues() {
        commandLine.setDefaultValues(option, List.of("default1", "default2"));
        List<String> values = commandLine.getUndefaultedValues(option);
        assertEquals(2, values.size());
        assertTrue(values.contains("default1"));
        assertTrue(values.contains("default2"));
    }

    @Test
    public void testAddSwitch() {
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitchValue(option));
    }

    @Test
    public void testSetDefaultSwitch() {
        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.getSwitchValue(option));
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty(option, "property1", "value1");
        commandLine.addProperty(option, "property2", "value2");
        assertEquals("value1", commandLine.getPropertyValue(option, "property1"));
        assertEquals("value2", commandLine.getPropertyValue(option, "property2"));
    }

    @Test
    public void testAddPropertyToDefaultSet() {
        commandLine.addProperty("property1", "value1");
        commandLine.addProperty("property2", "value2");
        assertEquals("value1", commandLine.getPropertyValue("property1"));
        assertEquals("value2", commandLine.getPropertyValue("property2"));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-option"));
        assertTrue(commandLine.looksLikeOption("--option"));
        assertFalse(commandLine.looksLikeOption("value"));
    }

    private static class WriteableCommandLineImpl implements WriteableCommandLine {

        private Option currentOption;
        private List<Option> options;

        @Override
        public void addOption(final Option option) {
            options.add(option);
        }

        @Override
        public void addValue(Option option, Object value) {
            // Implementation not required for testing
        }

        @Override
        public List getUndefaultedValues(Option option) {
            // Implementation not required for testing
            return null;
        }

        @Override
        public void setDefaultValues(Option option, List defaultValues) {
            // Implementation not required for testing
        }

        @Override
        public void addSwitch(Option option, boolean value) throws IllegalStateException {
            // Implementation not required for testing
        }

        @Override
        public void setDefaultSwitch(Option option, Boolean defaultSwitch) {
            // Implementation not required for testing
        }

        @Override
        public void addProperty(Option option, String property, String value) {
            // Implementation not required for testing
        }

        @Override
        public void addProperty(String property, String value) {
            // Implementation not required for testing
        }

        @Override
        public boolean looksLikeOption(String argument) {
            // Implementation not required for testing
            return false;
        }

        @Override
        public Option getCurrentOption() {
            return currentOption;
        }

        @Override
        public void setCurrentOption(Option currentOption) {
            this.currentOption = currentOption;
        }
    }
}
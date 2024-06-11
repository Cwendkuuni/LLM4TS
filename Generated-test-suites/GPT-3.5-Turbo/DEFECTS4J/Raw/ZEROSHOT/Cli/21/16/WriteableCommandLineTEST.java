import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.Option;
import java.util.List;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("testOption", "testDescription");
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption("testOption"));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("testOption", "testDescription");
        commandLine.addOption(option);
        commandLine.addValue(option, "testValue");
        List values = commandLine.getUndefaultedValues(option);
        assertEquals(1, values.size());
        assertEquals("testValue", values.get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("testOption", "testDescription");
        commandLine.addOption(option);
        commandLine.addValue(option, "testValue1");
        commandLine.addValue(option, "testValue2");
        List values = commandLine.getUndefaultedValues(option);
        assertEquals(2, values.size());
        assertEquals("testValue1", values.get(0));
        assertEquals("testValue2", values.get(1));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("testOption", "testDescription");
        commandLine.addOption(option);
        List defaultValues = new ArrayList();
        defaultValues.add("defaultValue1");
        defaultValues.add("defaultValue2");
        commandLine.setDefaultValues(option, defaultValues);

        List values = commandLine.getUndefaultedValues(option);
        assertEquals(0, values.size());

        List allValues = commandLine.getAllValues(option);
        assertEquals(2, allValues.size());
        assertEquals("defaultValue1", allValues.get(0));
        assertEquals("defaultValue2", allValues.get(1));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("testOption", "testDescription");
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.hasOption("testOption"));
        assertTrue(commandLine.isSet(option));
        assertTrue(commandLine.getSwitchValue(option));
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("testOption", "testDescription");
        commandLine.addOption(option);
        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.isSet(option));
        assertTrue(commandLine.getSwitchValue(option));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("testOption", "testDescription");
        commandLine.addOption(option);
        commandLine.addProperty(option, "testProperty", "testValue");
        assertEquals("testValue", commandLine.getProperty(option, "testProperty"));
    }

    @Test
    public void testAddPropertyToDefaultSet() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        commandLine.addProperty("testProperty", "testValue");
        assertEquals("testValue", commandLine.getProperty("testProperty"));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        assertTrue(commandLine.looksLikeOption("-testOption"));
        assertFalse(commandLine.looksLikeOption("testValue"));
    }

    @Test
    public void testGetCurrentOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("testOption", "testDescription");
        commandLine.setCurrentOption(option);
        assertEquals(option, commandLine.getCurrentOption());
    }

    @Test
    public void testSetCurrentOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option1 = new Option("testOption1", "testDescription1");
        Option option2 = new Option("testOption2", "testDescription2");
        commandLine.setCurrentOption(option1);
        assertEquals(option1, commandLine.getCurrentOption());
        commandLine.setCurrentOption(option2);
        assertEquals(option2, commandLine.getCurrentOption());
    }

    private static class WriteableCommandLineImpl implements WriteableCommandLine {

        private Option currentOption;
        private Map<Option, List> values;
        private Map<Option, Boolean> switches;
        private Map<Option, String> properties;
        private Map<String, String> defaultProperties;

        public WriteableCommandLineImpl() {
            values = new HashMap<Option, List>();
            switches = new HashMap<Option, Boolean>();
            properties = new HashMap<Option, String>();
            defaultProperties = new HashMap<String, String>();
        }

        @Override
        public void addOption(Option option) {
            // Implementation here
        }

        @Override
        public void addValue(Option option, Object value) {
            // Implementation here
        }

        @Override
        public List getUndefaultedValues(Option option) {
            // Implementation here
        }

        @Override
        public void setDefaultValues(Option option, List defaultValues) {
            // Implementation here
        }

        @Override
        public void addSwitch(Option option, boolean value) throws IllegalStateException {
            // Implementation here
        }

        @Override
        public void setDefaultSwitch(Option option, Boolean defaultSwitch) {
            // Implementation here
        }

        @Override
        public void addProperty(Option option, String property, String value) {
            // Implementation here
        }

        @Override
        public void addProperty(String property, String value) {
            // Implementation here
        }

        @Override
        public boolean looksLikeOption(String argument) {
            // Implementation here
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
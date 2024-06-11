
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine cmdLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("testOption");
        cmdLine.addOption(option);
        List<Option> options = cmdLine.getOptions();
        assertTrue(options.contains(option));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine cmdLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("testOption");
        String value = "testValue";
        cmdLine.addValue(option, value);
        List<String> values = cmdLine.getValues(option);
        assertTrue(values.contains(value));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLine cmdLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("testOption");
        String value = "testValue";
        cmdLine.addValue(option, value);
        List<String> values = cmdLine.getUndefaultedValues(option);
        assertTrue(values.contains(value));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine cmdLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("testOption");
        List<String> defaultValues = Arrays.asList("defaultValue1", "defaultValue2");
        cmdLine.setDefaultValues(option, defaultValues);
        List<String> values = cmdLine.getValues(option);
        assertTrue(values.containsAll(defaultValues));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine cmdLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("testOption");
        boolean value = true;
        cmdLine.addSwitch(option, value);
        assertTrue(cmdLine.getSwitch(option));
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine cmdLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("testOption");
        Boolean defaultSwitch = false;
        cmdLine.setDefaultSwitch(option, defaultSwitch);
        assertEquals(defaultSwitch, cmdLine.getSwitch(option));
    }

    @Test
    public void testAddPropertyToOption() {
        WriteableCommandLine cmdLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("testOption");
        String property = "testProperty";
        String value = "testValue";
        cmdLine.addProperty(option, property, value);
        assertEquals(value, cmdLine.getProperty(option, property));
    }

    @Test
    public void testAddPropertyToDefaultSet() {
        WriteableCommandLine cmdLine = new WriteableCommandLineImpl();
        String property = "testProperty";
        String value = "testValue";
        cmdLine.addProperty(property, value);
        assertEquals(value, cmdLine.getProperty(property));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine cmdLine = new WriteableCommandLineImpl();
        String argument = "-testArgument";
        assertTrue(cmdLine.looksLikeOption(argument));
    }

    private class WriteableCommandLineImpl implements WriteableCommandLine {

        private List<Option> options;

        private List<String> values;

        private List<String> undefaultedValues;

        private Option currentOption;

        @Override
        public void addOption(final Option option) {
            options.add(option);
        }

        @Override
        public void addValue(final Option option, final Object value) {
            values.add(value.toString());
        }

        @Override
        public List getUndefaultedValues(final Option option) {
            return undefaultedValues;
        }

        @Override
        public void setDefaultValues(final Option option, final List defaultValues) {
            values.addAll(defaultValues);
        }

        @Override
        public void addSwitch(final Option option, final boolean value) throws IllegalStateException {
            // Add a switch to the command line
        }

        @Override
        public void setDefaultSwitch(final Option option, final Boolean defaultSwitch) {
            // Set the default switch value
        }

        @Override
        public void addProperty(final Option option, final String property, final String value) {
            // Add a property to the option
        }

        @Override
        public void addProperty(final String property, final String value) {
            // Add a property to the default set
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

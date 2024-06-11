import org.junit.*;
import static org.junit.Assert.*;
import java.util.List;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("input", "i", true, "input file");
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption("input"));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("input", "i", true, "input file");
        commandLine.addOption(option);
        commandLine.addValue(option, "file.txt");
        List<String> values = commandLine.getValues(option);
        assertEquals(1, values.size());
        assertEquals("file.txt", values.get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("input", "i", true, "input file");
        commandLine.addOption(option);
        commandLine.addValue(option, "file1.txt");
        commandLine.addValue(option, "file2.txt");
        List<String> values = commandLine.getUndefaultedValues(option);
        assertEquals(2, values.size());
        assertEquals("file1.txt", values.get(0));
        assertEquals("file2.txt", values.get(1));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("input", "i", true, "input file");
        commandLine.setDefaultValues(option, List.of("file1.txt", "file2.txt"));
        List<String> values = commandLine.getUndefaultedValues(option);
        assertEquals(2, values.size());
        assertEquals("file1.txt", values.get(0));
        assertEquals("file2.txt", values.get(1));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("verbose", "v", false, "enable verbose mode");
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.hasOption("verbose"));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitch_IllegalStateException() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("verbose", "v", false, "enable verbose mode");
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("verbose", "v", false, "enable verbose mode");
        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.hasOption("verbose"));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("property", "p", true, "property");
        commandLine.addOption(option);
        commandLine.addProperty(option, "key", "value");
        String value = commandLine.getProperty(option, "key");
        assertEquals("value", value);
    }

    @Test
    public void testAddProperty_Default() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        commandLine.addProperty("key", "value");
        String value = commandLine.getProperty("key");
        assertEquals("value", value);
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        assertTrue(commandLine.looksLikeOption("-i"));
        assertFalse(commandLine.looksLikeOption("file.txt"));
    }

    private static class WriteableCommandLineImpl implements WriteableCommandLine {

        private List<Option> options;

        private Option currentOption;

        @Override
        public void addOption(final Option option) {
            options.add(option);
        }

        @Override
        public void addValue(final Option option, final Object value) {
            // implementation
        }

        @Override
        public List getUndefaultedValues(final Option option) {
            // implementation
            return null;
        }

        @Override
        public void setDefaultValues(final Option option, final List defaultValues) {
            // implementation
        }

        @Override
        public void addSwitch(final Option option, final boolean value) throws IllegalStateException {
            // implementation
        }

        @Override
        public void setDefaultSwitch(final Option option, final Boolean defaultSwitch) {
            // implementation
        }

        @Override
        public void addProperty(final Option option, final String property, final String value) {
            // implementation
        }

        @Override
        public void addProperty(final String property, final String value) {
            // implementation
        }

        @Override
        public boolean looksLikeOption(final String argument) {
            // implementation
            return true;
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
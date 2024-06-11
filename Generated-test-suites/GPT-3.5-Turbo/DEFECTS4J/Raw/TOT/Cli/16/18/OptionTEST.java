import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class OptionTest {

    private Option option;

    @Before
    public void setup() {
        option = new OptionImpl();
    }

    // Test process method
    @Test
    public void process_validArguments_success() throws OptionException {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        ListIterator args = List.of("arg1", "arg2").listIterator();

        option.process(commandLine, args);

        assertEquals(2, commandLine.getArguments().size());
        assertEquals("arg1", commandLine.getArguments().get(0));
        assertEquals("arg2", commandLine.getArguments().get(1));
    }

    // Test defaults method
    @Test
    public void defaults_emptyCommandLine_addsDefaultOptions() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();

        option.defaults(commandLine);

        assertTrue(commandLine.getOptions().isEmpty());
    }

    // Test canProcess method with valid argument
    @Test
    public void canProcess_validArgument_returnsTrue() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        String argument = "--option";

        boolean result = option.canProcess(commandLine, argument);

        assertTrue(result);
    }

    // Test canProcess method with invalid argument
    @Test
    public void canProcess_invalidArgument_returnsFalse() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        String argument = "invalidArgument";

        boolean result = option.canProcess(commandLine, argument);

        assertFalse(result);
    }

    // Test getTriggers method
    @Test
    public void getTriggers_returnsNotEmptySet() {
        Set triggers = option.getTriggers();

        assertNotNull(triggers);
        assertFalse(triggers.isEmpty());
    }

    // Test getPrefixes method
    @Test
    public void getPrefixes_returnsNotEmptySet() {
        Set prefixes = option.getPrefixes();

        assertNotNull(prefixes);
        assertFalse(prefixes.isEmpty());
    }

    // Test validate method with valid command line
    @Test
    public void validate_validCommandLine_noExceptionThrown() throws OptionException {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();

        option.validate(commandLine);
    }

    // Test helpLines method
    @Test
    public void helpLines_validParameters_returnsNotEmptyList() {
        int depth = 0;
        Set helpSettings = new HashSet();
        Comparator comp = Comparator.naturalOrder();

        List helpLines = option.helpLines(depth, helpSettings, comp);

        assertNotNull(helpLines);
        assertFalse(helpLines.isEmpty());
    }

    // Test appendUsage method
    @Test
    public void appendUsage_validParameters_appendsToBuffer() {
        StringBuffer buffer = new StringBuffer();
        Set helpSettings = new HashSet();
        Comparator comp = Comparator.naturalOrder();

        option.appendUsage(buffer, helpSettings, comp);

        assertFalse(buffer.toString().isEmpty());
    }

    // Test getPreferredName method
    @Test
    public void getPreferredName_returnsNotEmptyString() {
        String preferredName = option.getPreferredName();

        assertNotNull(preferredName);
        assertFalse(preferredName.isEmpty());
    }

    // Test getDescription method
    @Test
    public void getDescription_returnsNotEmptyString() {
        String description = option.getDescription();

        assertNotNull(description);
        assertFalse(description.isEmpty());
    }

    // Test getId method
    @Test
    public void getId_returnsValidId() {
        int id = option.getId();

        assertNotEquals(0, id);
    }

    // Test findOption method with valid trigger
    @Test
    public void findOption_validTrigger_returnsMatchingOption() {
        String trigger = "--option";
        Option matchingOption = option.findOption(trigger);

        assertNotNull(matchingOption);
        assertEquals(trigger, matchingOption.getTriggers().iterator().next());
    }

    // Test findOption method with invalid trigger
    @Test
    public void findOption_invalidTrigger_returnsNull() {
        String trigger = "invalidTrigger";
        Option matchingOption = option.findOption(trigger);

        assertNull(matchingOption);
    }

    // Test isRequired method
    @Test
    public void isRequired_returnsFalse() {
        boolean isRequired = option.isRequired();

        assertFalse(isRequired);
    }

    // Test getParent method
    @Test
    public void getParent_returnsNull() {
        Option parent = option.getParent();

        assertNull(parent);
    }

    // Test setParent method
    @Test
    public void setParent_validParent_setsParentOption() {
        Option parentOption = new OptionImpl();

        option.setParent(parentOption);

        assertEquals(parentOption, option.getParent());
    }

    private class OptionImpl implements Option {

        @Override
        public void process(WriteableCommandLine commandLine, ListIterator args) throws OptionException {
            while (args.hasNext()) {
                String argument = args.next();
                commandLine.addArgument(argument);
            }
        }

        @Override
        public void defaults(WriteableCommandLine commandLine) {

        }

        @Override
        public boolean canProcess(WriteableCommandLine commandLine, String argument) {
            return argument.startsWith("--");
        }

        @Override
        public boolean canProcess(WriteableCommandLine commandLine, ListIterator arguments) {
            return false;
        }

        @Override
        public Set getTriggers() {
            return Set.of("--option");
        }

        @Override
        public Set getPrefixes() {
            return Set.of("--");
        }

        @Override
        public void validate(WriteableCommandLine commandLine) throws OptionException {

        }

        @Override
        public List helpLines(int depth, Set helpSettings, Comparator comp) {
            return List.of(new HelpLineImpl("Option Test Implementation"));
        }

        @Override
        public void appendUsage(StringBuffer buffer, Set helpSettings, Comparator comp) {
            buffer.append("--option");
        }

        @Override
        public String getPreferredName() {
            return "--option";
        }

        @Override
        public String getDescription() {
            return "Test option description";
        }

        @Override
        public int getId() {
            return 1;
        }

        @Override
        public Option findOption(String trigger) {
            if (getTriggers().contains(trigger)) {
                return this;
            }
            return null;
        }

        @Override
        public boolean isRequired() {
            return false;
        }

        @Override
        public Option getParent() {
            return null;
        }

        @Override
        public void setParent(Option parent) {

        }
    }
}
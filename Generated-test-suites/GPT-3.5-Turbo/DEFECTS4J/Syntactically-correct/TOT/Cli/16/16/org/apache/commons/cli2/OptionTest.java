import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class OptionTest {

    private Option option;

    @Before
    public void setUp() {
        option = new OptionImpl();
    }

    @Test
    public void testProcess() throws OptionException {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        List<String> args = new ArrayList<>();
        args.add("arg1");
        args.add("arg2");

        option.process(commandLine, args.listIterator());

        assertTrue(commandLine.hasOption(option));
        assertEquals("arg1", commandLine.getOptionValue(option));
    }

    @Test
    public void testDefaults() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();

        option.defaults(commandLine);

        assertFalse(commandLine.hasOption(option));
    }

    @Test
    public void testCanProcess_argumentString() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        String argument = "arg";

        boolean result = option.canProcess(commandLine, argument);

        assertTrue(result);
    }

    @Test
    public void testCanProcess_argumentListIterator() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        ListIterator<String> arguments = Arrays.asList("arg").listIterator();

        boolean result = option.canProcess(commandLine, arguments);

        assertTrue(result);
    }

    @Test
    public void testGetTriggers() {
        Set<String> triggers = option.getTriggers();

        assertNotNull(triggers);
        assertTrue(triggers.isEmpty());
    }

    @Test
    public void testGetPrefixes() {
        Set<String> prefixes = option.getPrefixes();

        assertNotNull(prefixes);
        assertTrue(prefixes.isEmpty());
    }

    @Test
    public void testValidate_validCommandLine() throws OptionException {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        option.defaults(commandLine);

        option.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidate_invalidCommandLine() throws OptionException {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();

        option.validate(commandLine);
    }

    @Test
    public void testHelpLines() {
        int depth = 0;
        Set<HelpSettings> helpSettings = new HashSet<>();
        Comparator comp = Comparator.comparing(Option::getId);
        List<HelpLineImpl> expectedHelpLines = new ArrayList<>();
        HelpLineImpl helpLine = new HelpLineImpl(depth, option.getPreferredName(), option.getDescription());
        expectedHelpLines.add(helpLine);

        List<HelpLineImpl> helpLines = option.helpLines(depth, helpSettings, comp);

        assertEquals(expectedHelpLines, helpLines);
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        Set<HelpSettings> helpSettings = new HashSet<>();
        Comparator comp = Comparator.comparing(Option::getId);
        option = new OptionImpl("option", "Option Description");

        option.appendUsage(buffer, helpSettings, comp);

        assertTrue(buffer.toString().startsWith("option"));
    }

    @Test
    public void testGetPreferredName() {
        String expectedPreferredName = "option";
        option = new OptionImpl(expectedPreferredName, "Option Description");

        String preferredName = option.getPreferredName();

        assertEquals(expectedPreferredName, preferredName);
    }

    @Test
    public void testGetDescription() {
        String expectedDescription = "Option Description";
        option = new OptionImpl("option", expectedDescription);

        String description = option.getDescription();

        assertEquals(expectedDescription, description);
    }

    @Test
    public void testGetId() {
        int expectedId = 1;
        option = new OptionImpl("option", "Option Description", expectedId);

        int id = option.getId();

        assertEquals(expectedId, id);
    }

    @Test
    public void testFindOption_exist() {
        String trigger = "option";
        option = new OptionImpl(trigger, "Option Description");

        Option foundOption = option.findOption(trigger);

        assertNotNull(foundOption);
        assertEquals(option, foundOption);
    }

    @Test
    public void testFindOption_notExist() {
        String trigger = "option";
        option = new OptionImpl("otherOption", "Option Description");

        Option foundOption = option.findOption(trigger);

        assertNull(foundOption);
    }

    @Test
    public void testIsRequired_true() {
        option = new OptionImpl("option", "Option Description", 1, true);

        boolean isRequired = option.isRequired();

        assertTrue(isRequired);
    }

    @Test
    public void testIsRequired_false() {
        option = new OptionImpl("option", "Option Description", 1, false);

        boolean isRequired = option.isRequired();

        assertFalse(isRequired);
    }

    // Custom OptionImpl class for testing

    private class OptionImpl implements Option {

        private String preferredName;
        private String description;
        private int id;
        private boolean isRequired;

        OptionImpl() {
        }

        OptionImpl(String preferredName, String description) {
            this.preferredName = preferredName;
            this.description = description;
        }

        OptionImpl(String preferredName, String description, int id) {
            this.preferredName = preferredName;
            this.description = description;
            this.id = id;
        }

        OptionImpl(String preferredName, String description, int id, boolean isRequired) {
            this.preferredName = preferredName;
            this.description = description;
            this.id = id;
            this.isRequired = isRequired;
        }

        @Override
        public void process(WriteableCommandLine commandLine, ListIterator args) throws OptionException {
            if (args.hasNext()) {
                String arg = (String) args.next();
                commandLine.addOption(this, arg);
            }
        }

        @Override
        public void defaults(WriteableCommandLine commandLine) {
            // No defaults for this option
        }

        @Override
        public boolean canProcess(WriteableCommandLine commandLine, String argument) {
            return true;
        }

        @Override
        public boolean canProcess(WriteableCommandLine commandLine, ListIterator arguments) {
            return true;
        }

        @Override
        public Set getTriggers() {
            return Collections.emptySet();
        }

        @Override
        public Set getPrefixes() {
            return Collections.emptySet();
        }

        @Override
        public void validate(WriteableCommandLine commandLine) throws OptionException {
            if (!commandLine.hasOption(this)) {
                throw new OptionException("Option is required: " + this.getPreferredName());
            }
        }

        @Override
        public List helpLines(int depth, Set helpSettings, Comparator comp) {
            HelpLineImpl helpLine = new HelpLineImpl(depth, this.getPreferredName(), this.getDescription());
            List<HelpLineImpl> helpLines = new ArrayList<>();
            helpLines.add(helpLine);
            return helpLines;
        }

        @Override
        public void appendUsage(StringBuffer buffer, Set helpSettings, Comparator comp) {
            buffer.append(getPreferredName());
        }

        @Override
        public String getPreferredName() {
            return preferredName;
        }

        @Override
        public String getDescription() {
            return description;
        }

        @Override
        public int getId() {
            return id;
        }

        @Override
        public Option findOption(String trigger) {
            if (this.getTriggers().contains(trigger)) {
                return this;
            }
            return null;
        }

        @Override
        public boolean isRequired() {
            return isRequired;
        }
    }
}
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class OptionTest {

    private Option option;

    @Before
    public void setUp() {
        option = new OptionImpl();
    }

    // Test for process() method

    @Test
    public void testProcess() {
        WriteableCommandLine commandLine = new CommandLineImpl();
        ListIterator<String> args = Arrays.asList("-a", "--option", "-b").listIterator();

        try {
            option.process(commandLine, args);
        } catch (OptionException e) {
            fail("Failed to process arguments: " + e.getMessage());
        }

        assertTrue("CommandLine should contain option '-a'", commandLine.hasOption("-a"));
        assertTrue("CommandLine should contain option '--option'", commandLine.hasOption("--option"));
        assertTrue("CommandLine should contain option '-b'", commandLine.hasOption("-b"));
    }

    // Test for defaults() method

    @Test
    public void testDefaults() {
        WriteableCommandLine commandLine = new CommandLineImpl();
        commandLine.addOption("-a", true);
        commandLine.addOption("--option", false);

        option.defaults(commandLine);

        assertTrue("CommandLine should contain default value for option '-a'", commandLine.hasOption("-a"));
        assertTrue("CommandLine should contain default value for option '--option'", commandLine.hasOption("--option"));
    }

    // Test for canProcess() method with single argument

    @Test
    public void testCanProcessWithArgument() {
        WriteableCommandLine commandLine = new CommandLineImpl();
        String argument = "-a";

        assertTrue("Option should be able to process argument '-a'", option.canProcess(commandLine, argument));
    }

    @Test
    public void testCanProcessWithInvalidArgument() {
        WriteableCommandLine commandLine = new CommandLineImpl();
        String argument = "-c";

        assertFalse("Option should not be able to process argument '-c'", option.canProcess(commandLine, argument));
    }

    // Test for canProcess() method with ListIterator

    @Test
    public void testCanProcessWithIterator() {
        WriteableCommandLine commandLine = new CommandLineImpl();
        ListIterator<String> argumentList = Arrays.asList("-a", "-b").listIterator();

        assertTrue("Option should be able to process argument list ['-a', '-b']", option.canProcess(commandLine, argumentList));
    }

    @Test
    public void testCanProcessWithInvalidIterator() {
        WriteableCommandLine commandLine = new CommandLineImpl();
        ListIterator<String> argumentList = Arrays.asList("-b", "-c").listIterator();

        assertFalse("Option should not be able to process argument list ['-b', '-c']", option.canProcess(commandLine, argumentList));
    }

    // Test for getTriggers() method

    @Test
    public void testGetTriggers() {
        Set<String> triggers = option.getTriggers();

        assertTrue("Triggers should contain '-a'", triggers.contains("-a"));
        assertTrue("Triggers should contain '--option'", triggers.contains("--option"));
    }

    // Test for getPrefixes() method

    @Test
    public void testGetPrefixes() {
        Set<String> prefixes = option.getPrefixes();

        assertTrue("Prefixes should contain '--'", prefixes.contains("--"));
        assertTrue("Prefixes should contain '-'", prefixes.contains("-"));
    }

    // Test for validate() method

    @Test
    public void testValidate() {
        WriteableCommandLine commandLine = new CommandLineImpl();
        commandLine.addOption("-a", true);

        try {
            option.validate(commandLine);
        } catch (OptionException e) {
            fail("Failed to validate command line: " + e.getMessage());
        }
    }

    @Test(expected = OptionException.class)
    public void testValidateWithMissingOption() throws OptionException {
        WriteableCommandLine commandLine = new CommandLineImpl();

        option.validate(commandLine);
    }

    // Test for helpLines() method

    @Test
    public void testHelpLines() {
        Set<DisplaySetting> helpSettings = new HashSet<>();
        helpSettings.add(DisplaySetting.DESCRIPTION);
        Comparator comp = null;

        List<HelpLineImpl> helpLines = option.helpLines(0, helpSettings, comp);

        assertEquals("Number of help lines should be 2", 2, helpLines.size());
        assertEquals("First help line should have depth 0", 0, helpLines.get(0).getDepth());
        assertEquals("Second help line should have depth 0", 0, helpLines.get(1).getDepth());
        assertNull("First help line should not have children", helpLines.get(0).getChildren());
        assertNull("Second help line should not have children", helpLines.get(1).getChildren());
    }

    // Test for appendUsage() method

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        Set<DisplaySetting> helpSettings = new HashSet<>();
        helpSettings.add(DisplaySetting.DESCRIPTION);
        Comparator comp = null;

        option.appendUsage(buffer, helpSettings, comp);

        assertTrue("Buffer should contain option '-a'", buffer.toString().contains("-a"));
        assertTrue("Buffer should contain option '--option'", buffer.toString().contains("--option"));
    }

    // Test for getPreferredName() method

    @Test
    public void testGetPreferredName() {
        String preferredName = option.getPreferredName();

        assertNotNull("Preferred name should not be null", preferredName);
    }

    // Test for getDescription() method

    @Test
    public void testGetDescription() {
        String description = option.getDescription();

        assertNotNull("Description should not be null", description);
    }

    // Test for getId() method

    @Test
    public void testGetId() {
        int id = option.getId();

        assertNotEquals("Id should not be 0", 0, id);
    }

    // Test for findOption() method

    @Test
    public void testFindOption() {
        Option foundOption = option.findOption("-a");

        assertNotNull("Option should be found", foundOption);
    }

    @Test
    public void testFindOptionNotFound() {
        Option foundOption = option.findOption("-c");

        assertNull("Option should not be found", foundOption);
    }

    // Test for isRequired() method

    @Test
    public void testIsRequired() {
        boolean isRequired = option.isRequired();

        assertFalse("Option should not be required", isRequired);
    }

    // Test for getParent() method

    @Test
    public void testGetParent() {
        Option parent = option.getParent();

        assertNull("Parent should be null", parent);
    }

    // Test for setParent() method

    @Test
    public void testSetParent() {
        Option parent = new OptionImpl();
        option.setParent(parent);

        assertSame("Parent should be set correctly", parent, option.getParent());
    }

    private class OptionImpl implements Option {
        @Override
        public void process(WriteableCommandLine commandLine, ListIterator args) throws OptionException {

        }

        @Override
        public void defaults(WriteableCommandLine commandLine) {

        }

        @Override
        public boolean canProcess(WriteableCommandLine commandLine, String argument) {
            return false;
        }

        @Override
        public boolean canProcess(WriteableCommandLine commandLine, ListIterator arguments) {
            return false;
        }

        @Override
        public Set getTriggers() {
            return new HashSet<>(Arrays.asList("-a", "--option"));
        }

        @Override
        public Set getPrefixes() {
            return new HashSet<>(Arrays.asList("-", "--"));
        }

        @Override
        public void validate(WriteableCommandLine commandLine) throws OptionException {

        }

        @Override
        public List helpLines(int depth, Set helpSettings, Comparator comp) {
            List<HelpLineImpl> helpLines = new ArrayList<>();
            helpLines.add(new HelpLineImpl(0, null));
            helpLines.add(new HelpLineImpl(0, null));
            return helpLines;
        }

        @Override
        public void appendUsage(StringBuffer buffer, Set helpSettings, Comparator comp) {
            buffer.append("-a");
            buffer.append("--option");
        }

        @Override
        public String getPreferredName() {
            return "-a";
        }

        @Override
        public String getDescription() {
            return "This is an option";
        }

        @Override
        public int getId() {
            return 123;
        }

        @Override
        public Option findOption(String trigger) {
            if (trigger.equals("-a")) {
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

    private class HelpLineImpl implements HelpLine {
        private int depth;
        private List<HelpLine> children;

        public HelpLineImpl(int depth, List<HelpLine> children) {
            this.depth = depth;
            this.children = children;
        }

        @Override
        public int getDepth() {
            return depth;
        }

        @Override
        public List<HelpLine> getChildren() {
            return children;
        }
    }
}
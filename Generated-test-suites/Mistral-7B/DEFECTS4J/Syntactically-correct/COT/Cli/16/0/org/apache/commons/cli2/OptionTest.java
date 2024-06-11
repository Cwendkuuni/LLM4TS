import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.help.HelpFormatter;
import org.apache.commons.cli2.util.HelpLine;
import org.junit.Test;
import org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

public class OptionTest {

    @Test
    public void testProcess() throws OptionException {
        Option option = new DummyOption();
        WriteableCommandLine commandLine = new WriteableCommandLine();
        ListIterator<String> args = Arrays.asList("--option", "value").listIterator();

        option.process(commandLine, args);

        assertEquals("--option value", commandLine.getOptionStrings().get(0));
    }

    @Test
    public void testDefaults() {
        Option option = new DummyOption();
        WriteableCommandLine commandLine = new WriteableCommandLine();
        option.defaults(commandLine);

        // Defaults should not modify the command line
        assertEquals(0, commandLine.getArguments().size());
    }

    @Test
    public void testCanProcessArgument() {
        Option option = new DummyOption();
        WriteableCommandLine commandLine = new WriteableCommandLine();

        assertTrue(option.canProcess(commandLine, "--option"));
        assertTrue(option.canProcess(commandLine, "--option value"));
        assertFalse(option.canProcess(commandLine, "--another-option"));
        assertFalse(option.canProcess(commandLine, "-another-option"));
        assertFalse(option.canProcess(commandLine, "another-option"));
    }

    @Test
    public void testCanProcessListIterator() {
        Option option = new DummyOption();
        WriteableCommandLine commandLine = new WriteableCommandLine();
        ListIterator<String> args = Arrays.asList("--option", "value", "--another-option").listIterator();

        assertTrue(option.canProcess(commandLine, args));
        assertEquals(1, args.nextIndex());

        // The iterator should be restored to its initial state after the method call
        assertEquals("--option", args.next());
        assertEquals("value", args.next());
        assertEquals("--another-option", args.next());
    }

    @Test
    public void testGetTriggers() {
        Option option = new DummyOption();
        Set triggers = option.getTriggers();

        Set expectedTriggers = new HashSet();
        expectedTriggers.add("--option");
        expectedTriggers.add("-o");

        assertEquals(expectedTriggers, triggers);
    }

    @Test
    public void testGetPrefixes() {
        Option option = new DummyOption();
        Set prefixes = option.getPrefixes();

        Set expectedPrefixes = new LinkedHashSet();
        expectedPrefixes.add("--");
        expectedPrefixes.add("-");

        assertEquals(expectedPrefixes, prefixes);
    }

    @Test(expected = OptionException.class)
    public void testValidateInvalidCommandLine() throws OptionException {
        Option option = new DummyOption();
        WriteableCommandLine commandLine = new WriteableCommandLine();
        ListIterator<String> args = Arrays.asList("--option", "--option").listIterator();

        option.validate(commandLine);

        fail("OptionException not thrown");
    }

    @Test
    public void testValidateValidCommandLine() throws OptionException {
        Option option = new DummyOption();
        WriteableCommandLine commandLine = new WriteableCommandLine();
        ListIterator<String> args = Arrays.asList("--option", "value").listIterator();

        option.validate(commandLine);

        // Validation should not modify the command line
        assertEquals("--option value", commandLine.getOptionStrings().get(0));
        assertEquals(1, commandLine.getArguments().size());
    }

    @Test
    public void testHelpLines() {
        Option option = new DummyOption();
        int depth = 1;
        Set helpSettings = new HashSet();
        Comparator comp = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return 0;
            }
        };

        List helpLines = option.helpLines(depth, helpSettings, comp);

        List expectedHelpLines = new ArrayList();
        expectedHelpLines.add(new HelpLine("--option", "Option description", null));

        assertEquals(expectedHelpLines, helpLines);
    }

    @Test
    public void testAppendUsage() {
        Option option = new DummyOption();
        StringBuffer buffer = new StringBuffer();
        Set helpSettings = new HashSet();
        Comparator comp = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return 0;
            }
        };

        option.appendUsage(buffer, helpSettings, comp);

        String expectedUsage = "--option\n";
        assertEquals(expectedUsage, buffer.toString());
    }

    @Test
    public void testGetPreferredName() {
        Option option = new DummyOption();

        String expectedPreferredName = "--option";
        assertEquals(expectedPreferredName, option.getPreferredName());
    }

    @Test
    public void testGetDescription() {
        Option option = new DummyOption();

        String expectedDescription = "Option description";
        assertEquals(expectedDescription, option.getDescription());
    }

    @Test
    public void testGetId() {
        Option option = new DummyOption();

        int expectedId = 1;
        assertEquals(expectedId, option.getId());
    }

    @Test
    public void testFindOption() {
        Option option1 = new DummyOption();
        Option option2 = new DummyOption();

        Option foundOption = option1.findOption("--option2");

        assertNull(foundOption);

        List options = Arrays.asList(option1, option2);
        option1.setParent(options);

        foundOption = option1.findOption("--option1");

        assertEquals(option1, foundOption);
    }

    @Test
    public void testIsRequired() {
        Option option = new DummyOption();

        assertFalse(option.isRequired());
    }

    @Test
    public void testHasParent() {
        Option option = new DummyOption();
        List options = Arrays.asList(option);
        option.setParent(options);

        assertTrue(option.hasParent());
    }

    private static class DummyOption implements Option {
        @Override
        public void process(WriteableCommandLine commandLine, ListIterator args) throws OptionException {
        }

        @Override
        public void defaults(WriteableCommandLine commandLine) {
        }

        @Override
        public boolean canProcess(WriteableCommandLine commandLine, String argument) {
            return true;
        }

        @Override
        public boolean canProcess(WriteableCommandLine commandLine, ListIterator args) {
            return true;
        }

        @Override
        public Set getTriggers() {
            return new LinkedHashSet();
        }

        @Override
        public Set getPrefixes() {
            return new LinkedHashSet();
        }

        @Override
        public void validate(WriteableCommandLine commandLine) throws OptionException {
        }

        @Override
        public List helpLines(int depth, Set helpSettings, Comparator comp) {
            return new ArrayList();
        }

        @Override
        public void appendUsage(StringBuffer buffer, Set helpSettings, Comparator comp) {
        }

        @Override
        public String getPreferredName() {
            return null;
        }

        @Override
        public String getDescription() {
            return null;
        }

        @Override
        public int getId() {
            return 1;
        }

        @Override
        public Option findOption(String trigger) {
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
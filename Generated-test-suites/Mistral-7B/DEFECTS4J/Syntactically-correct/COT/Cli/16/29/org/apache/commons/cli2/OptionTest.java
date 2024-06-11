import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.utils.HelpFormatter;
import org.junit.Test;
import org.junit.Before;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Comparator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class OptionTest {

    private Option option;
    private WriteableCommandLine commandLine;
    private ListIterator args;

    @Before
    public void setup() {
        option = new MockOption();
        commandLine = new WriteableCommandLine();
        args = Arrays.asList(new String[]{"option", "arg1", "arg2"}).listIterator();
    }

    @Test
    public void testProcess() throws OptionException {
        option.process(commandLine, args);
        assertEquals("arg1", commandLine.getOptionValues("option")[0]);
    }

    @Test(expected = OptionException.class)
    public void testProcessWithInvalidArgument() throws OptionException {
        option.process(commandLine, args);
        assertFalse(args.hasNext()); // Should have consumed one argument
        args.next();
        option.process(commandLine, args);
        throw new AssertionError("Method should have thrown OptionException");
    }

    @Test
    public void testDefaults() {
        option.defaults(commandLine);
        assertNull(commandLine.getOptionValues("option"));
    }

    @Test
    public void testCanProcess() {
        assertTrue(option.canProcess(commandLine, args));
        assertTrue(option.canProcess(commandLine, args));
    }

    @Test
    public void testCanProcessWithInvalidArgument() {
        assertFalse(option.canProcess(commandLine, args));
        args.next();
        assertFalse(option.canProcess(commandLine, args));
    }

    @Test
    public void testGetTriggers() {
        Set triggers = option.getTriggers();
        assertNotNull(triggers);
        assertTrue(triggers.contains("option"));
    }

    @Test
    public void testGetPrefixes() {
        Set prefixes = option.getPrefixes();
        assertNotNull(prefixes);
        assertTrue(prefixes.contains("-"));
        assertTrue(prefixes.contains("--"));
    }

    @Test(expected = OptionException.class)
    public void testInvalidValidate() throws OptionException {
        option.validate(commandLine);
        throw new AssertionError("Method should have thrown OptionException");
    }

    @Test
    public void testValidValidate() throws OptionException {
        option.validate(commandLine);
    }

    @Test
    public void testHelpLines() {
        List helpLines = option.helpLines(0, new HashSet(), null);
        assertNotNull(helpLines);
        assertEquals(1, helpLines.size());
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("Test option", helpLines.get(0), true);
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        option.appendUsage(buffer, new HashSet(), null);
        assertEquals("option: arg1, arg2\n", buffer.toString());
    }

    @Test
    public void testGetPreferredName() {
        assertEquals("option", option.getPreferredName());
    }

    @Test
    public void testGetDescription() {
        assertEquals("This is an option description.", option.getDescription());
    }

    @Test
    public void testGetId() {
        assertEquals(1, option.getId());
    }

    @Test
    public void testFindOption() {
        Option otherOption = new MockOption();
        option.add(otherOption);
        Option foundOption = option.findOption("option");
        assertEquals(option, foundOption);
    }

    @Test
    public void testIsRequired() {
        assertFalse(option.isRequired());
    }

    @Test
    public void testHasParent() {
        Option parentOption = new MockOption();
        option.setParent(parentOption);
        Option childOption = new MockOption();
        option.add(childOption);
        Option foundOption = option.findOption("childOption");
        assertEquals(childOption, foundOption);
        assertEquals(parentOption, foundOption.getParent());
    }

    private static class MockOption implements Option {
        @Override
        public void process(WriteableCommandLine commandLine, ListIterator args) throws OptionException {
            // no-op
        }

        @Override
        public void defaults(WriteableCommandLine commandLine) {
            // no-op
        }

        @Override
        public boolean canProcess(WriteableCommandLine commandLine, String argument) {
            return true;
        }

        @Override
        public boolean canProcess(WriteableCommandLine commandLine, ListIterator argIter) {
            return true;
        }

        @Override
        public Set getTriggers() {
            return new HashSet();
        }

        @Override
        public Set getPrefixes() {
            return new HashSet();
        }

        @Override
        public void validate(WriteableCommandLine commandLine) throws OptionException {
            // no-op
        }

        @Override
        public List helpLines(int depth, Set helpSettings, Comparator comp) {
            return new ArrayList();
        }

        @Override
        public void appendUsage(StringBuffer buffer, Set helpSettings, Comparator comp) {
            // no-op
        }

        @Override
        public String getPreferredName() {
            return "option";
        }

        @Override
        public String getDescription() {
            return "This is an option description.";
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
        public Option setParent(Option parent) {
            return this;
        }

        @Override
        public void add(Option option) {
            // no-op
        }
    }
}
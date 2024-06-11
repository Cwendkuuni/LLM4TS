import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OptionTest {

    @Test
    public void testProcess() {
        Option option = new StubOption();
        WriteableCommandLine commandLine = new WriteableCommandLine();
        ListIterator args = Arrays.asList("arg1", "arg2", "arg3").listIterator();

        option.process(commandLine, args);

        assertEquals("arg1", commandLine.getOptionValues("option1").get(0));
        assertEquals("arg2", commandLine.getOptionValues("option2").get(0));
        assertEquals("arg3", commandLine.getOptionValues("option3").get(0));
    }

    @Test
    public void testProcessWithException() {
        Option option = new StubOptionWithException();
        WriteableCommandLine commandLine = new WriteableCommandLine();
        ListIterator args = Arrays.asList("arg1", "arg2", "arg3").listIterator();

        assertThrows(OptionException.class, () -> {
            option.process(commandLine, args);
        });
    }

    @Test
    public void testDefaults() {
        Option option = new StubOption();
        WriteableCommandLine commandLine = new WriteableCommandLine();

        option.defaults(commandLine);

        assertNull(commandLine.getOptionValues("option1"));
        assertEquals("defaultValue1", commandLine.getOptionValue("option2"));
        assertEquals("defaultValue2", commandLine.getOptionValue("option3"));
    }

    @Test
    public void testCanProcess() {
        Option option = new StubOption();

        assertTrue(option.canProcess(new WriteableCommandLine(), "optionValue"));
        assertTrue(option.canProcess(new WriteableCommandLine(), Arrays.asList("optionValue", "arg1").listIterator()));

        assertFalse(option.canProcess(new WriteableCommandLine(), "arg"));
        assertFalse(option.canProcess(new WriteableCommandLine(), Arrays.asList("arg", "arg1").listIterator()));
    }

    @Test
    public void testGetTriggers() {
        Option option = new StubOption();
        Set triggers = option.getTriggers();

        assertNotNull(triggers);
        assertTrue(triggers.contains("option"));
        assertTrue(triggers.contains("-"));
    }

    @Test
    public void testGetPrefixes() {
        Option option = new StubOption();
        Set prefixes = option.getPrefixes();

        assertNotNull(prefixes);
        assertTrue(prefixes.contains("--"));
        assertTrue(prefixes.contains("-"));
    }

    @Test
    public void testValidate() {
        Option option = new StubOption();
        WriteableCommandLine commandLine = new WriteableCommandLine();

        option.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithException() {
        Option option = new StubOptionWithException();
        WriteableCommandLine commandLine = new WriteableCommandLine();

        option.validate(commandLine);
    }

    @Test
    public void testHelpLines() {
        Option option = new StubOption();

        List helpLines = option.helpLines(1, new HashSet(), Collections.nullComparator());

        assertNotNull(helpLines);
        assertEquals(1, helpLines.size());
        assertEquals("--option\n", helpLines.get(0).toString());
    }

    @Test
    public void testAppendUsage() {
        Option option = new StubOption();
        StringBuffer buffer = new StringBuffer();

        option.appendUsage(buffer, new HashSet(), Collections.nullComparator());

        assertEquals("--option\n", buffer.toString());
    }

    @Test
    public void testGetPreferredName() {
        Option option = new StubOption();

        assertEquals("option", option.getPreferredName());
    }

    @Test
    public void testGetDescription() {
        Option option = new StubOption();

        assertEquals("This is an option description.", option.getDescription());
    }

    @Test
    public void testgetId() {
        Option option = new StubOption();

        assertEquals(1, option.getId());
    }

    @Test
    public void testFindOption() {
        Option parentOption = new StubParentOption();
        Option option1 = new StubOption();
        Option option2 = new StubOption();
        option1.setParent(parentOption);
        option2.setParent(parentOption);

        assertEquals(option1, parentOption.findOption("option1"));
        assertEquals(option2, parentOption.findOption("option2"));
        assertNull(parentOption.findOption("nonexistentOption"));
    }

    @Test
    public void testIsRequired() {
        Option option = new StubOption();

        assertFalse(option.isRequired());
    }

    private static class StubOption extends Option {
        @Override
        public void process(WriteableCommandLine commandLine, ListIterator args)
                throws OptionException {
            // empty implementation
        }

        @Override
        public void defaults(WriteableCommandLine commandLine) {
            commandLine.setOptionValue("option2", "defaultValue1");
            commandLine.setOptionValue("option3", "defaultValue2");
        }

        @Override
        public boolean canProcess(WriteableCommandLine commandLine, String arg) {
            return true;
        }

        @Override
        public boolean canProcess(WriteableCommandLine commandLine, ListIterator args) {
            return true;
        }

        @Override
        public Set getTriggers() {
            return new HashSet(Arrays.asList("option"));
        }

        @Override
        public Set getPrefixes() {
            return new HashSet(Arrays.asList("--", "-"));
        }

        @Override
        public void validate(WriteableCommandLine commandLine) throws OptionException {
            // empty implementation
        }

        @Override
        public List helpLines(int depth, Set helpSettings, Comparator comp) {
            return new ArrayList();
        }

        @Override
        public void appendUsage(StringBuffer buffer, Set helpSettings, Comparator comp) {
            // empty implementation
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
    }

    private static class StubOptionWithException extends Option {
        @Override
        public void process(WriteableCommandLine commandLine, ListIterator args)
                throws OptionException {
            throw new OptionException("Test exception");
        }

        @Override
        public void defaults(WriteableCommandLine commandLine) {
            // empty implementation
        }

        @Override
        public boolean canProcess(WriteableCommandLine commandLine, String arg) {
            return true;
        }

        @Override
        public boolean canProcess(WriteableCommandLine commandLine, ListIterator args) {
            return true;
        }

        @Override
        public Set getTriggers() {
            return new HashSet(Arrays.asList("option"));
        }

        @Override
        public Set getPrefixes() {
            return new HashSet(Arrays.asList("--", "-"));
        }

        @Override
        public void validate(WriteableCommandLine commandLine) throws OptionException {
            throw new OptionException("Test exception");
        }

        @Override
        public List helpLines(int depth, Set helpSettings, Comparator comp) {
            return new ArrayList();
        }

        @Override
        public void appendUsage(StringBuffer buffer, Set helpSettings, Comparator comp) {
            // empty implementation
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
    }

    private static class StubParentOption extends Option {
        private Option parent;

        @Override
        public void process(WriteableCommandLine commandLine, ListIterator args)
                throws OptionException {
            // empty implementation
        }

        @Override
        public void defaults(WriteableCommandLine commandLine) {
            // empty implementation
        }

        @Override
        public boolean canProcess(WriteableCommandLine commandLine, String arg) {
            return true;
        }

        @Override
        public boolean canProcess(WriteableCommandLine commandLine, ListIterator args) {
            return true;
        }

        @Override
        public Set getTriggers() {
            return new HashSet(Arrays.asList("parentOption"));
        }

        @Override
        public Set getPrefixes() {
            return new HashSet(Arrays.asList("--", "-"));
        }

        @Override
        public void validate(WriteableCommandLine commandLine) throws OptionException {
            // empty implementation
        }

        @Override
        public List helpLines(int depth, Set helpSettings, Comparator comp) {
            return new ArrayList();
        }

        @Override
        public void appendUsage(StringBuffer buffer, Set helpSettings, Comparator comp) {
            // empty implementation
        }

        @Override
        public String getPreferredName() {
            return "parentOption";
        }

        @Override
        public String getDescription() {
            return "This is a parent option description.";
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
            this.parent = parent;
            return this;
        }
    }
}
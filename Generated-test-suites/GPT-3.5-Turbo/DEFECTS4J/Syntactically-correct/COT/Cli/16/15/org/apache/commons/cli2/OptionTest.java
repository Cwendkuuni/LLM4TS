import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class OptionTest {

    private Option option;
    private WriteableCommandLine writeableCommandLine;
    private ListIterator args;

    @Before
    public void setUp() {
        option = new OptionImpl();
        writeableCommandLine = mock(WriteableCommandLine.class);
        args = mock(ListIterator.class);
    }

    @Test
    public void testProcess() throws OptionException {
        try {
            option.process(writeableCommandLine, args);
        } catch (OptionException e) {
            fail("OptionException should not be thrown.");
        }
    }

    @Test
    public void testDefaults() {
        option.defaults(writeableCommandLine);
    }

    @Test
    public void testCanProcessString() {
        boolean result = option.canProcess(writeableCommandLine, "argument");
        assertFalse(result);
    }

    @Test
    public void testCanProcessListIterator() {
        boolean result = option.canProcess(writeableCommandLine, args);
        assertFalse(result);
    }

    @Test
    public void testGetTriggers() {
        Set triggers = option.getTriggers();
        assertNotNull(triggers);
    }

    @Test
    public void testGetPrefixes() {
        Set prefixes = option.getPrefixes();
        assertNotNull(prefixes);
    }

    @Test
    public void testValidate() throws OptionException {
        try {
            option.validate(writeableCommandLine);
        } catch (OptionException e) {
            fail("OptionException should not be thrown.");
        }
    }

    @Test
    public void testHelpLines() {
        int depth = 1;
        Set helpSettings = new HashSet();
        Comparator comp = mock(Comparator.class);
        List<HelpLine> helpLines = option.helpLines(depth, helpSettings, comp);
        assertNotNull(helpLines);
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        Set helpSettings = new HashSet();
        Comparator comp = mock(Comparator.class);
        option.appendUsage(buffer, helpSettings, comp);
        assertNotNull(buffer.toString());
    }

    @Test
    public void testGetPreferredName() {
        String preferredName = option.getPreferredName();
        assertNotNull(preferredName);
    }

    @Test
    public void testGetDescription() {
        String description = option.getDescription();
        assertNotNull(description);
    }

    @Test
    public void testGetId() {
        int id = option.getId();
        assertNotNull(id);
    }

    @Test
    public void testFindOption() {
        String trigger = "trigger";
        Option foundOption = option.findOption(trigger);
        assertNull(foundOption);
    }

    @Test
    public void testIsRequired() {
        boolean required = option.isRequired();
        assertFalse(required);
    }

    @Test
    public void testGetParent() {
        Option parent = option.getParent();
        assertNull(parent);
    }

    @Test
    public void testSetParent() {
        Option parent = new OptionImpl();
        option.setParent(parent);
        Option result = option.getParent();
        assertEquals(parent, result);
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
            return new HashSet<>();
        }

        @Override
        public Set getPrefixes() {
            return new HashSet<>();
        }

        @Override
        public void validate(WriteableCommandLine commandLine) throws OptionException {

        }

        @Override
        public List helpLines(int depth, Set helpSettings, Comparator comp) {
            return new ArrayList<>();
        }

        @Override
        public void appendUsage(StringBuffer buffer, Set helpSettings, Comparator comp) {

        }

        @Override
        public String getPreferredName() {
            return "preferredName";
        }

        @Override
        public String getDescription() {
            return "description";
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
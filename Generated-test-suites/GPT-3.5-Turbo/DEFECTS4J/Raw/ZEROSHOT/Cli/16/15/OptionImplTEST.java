import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.HashSet;

public class OptionImplTest {
    private OptionImpl option;

    @Before
    public void setUp() {
        option = new OptionImpl(1, true) {
            public boolean canProcess(final WriteableCommandLine commandLine,
                                      final ListIterator arguments) {
                return true;
            }
            public void appendUsage(final StringBuffer buffer,
                                     final DisplaySetting displaySetting,
                                     final Set optionTriggers) {
                // implementation of appendUsage for testing purposes
            }
            public void process(final WriteableCommandLine commandLine,
                                final ListIterator arguments) {
                // implementation of process for testing purposes
            }
            public void validate(final WriteableCommandLine commandLine) {
                // implementation of validate for testing purposes
            }
            public void defaults(final WriteableCommandLine commandLine) {
                // implementation of defaults for testing purposes
            }
            public boolean requiresAdvancedParsing() {
                return true;
            }
            public boolean isRequired() {
                return false;
            }
            public void addTriggers(final HashSet triggers,
                                    final HashSet matchTriggers) {
                // implementation of addTriggers for testing purposes
            }
            public void addPrefixes(final HashSet prefixes,
                                    final boolean lockedPrefixes) {
                // implementation of addPrefixes for testing purposes
            }
        };
    }

    @Test
    public void testCanProcess() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        ArrayList arguments = new ArrayList();
        arguments.add("test");
        assertTrue(option.canProcess(commandLine, arguments.listIterator()));
    }

    @Test
    public void testToString() {
        String expected = "test option";
        assertEquals(expected, option.toString());
    }

    @Test
    public void testEquals() {
        Object obj = new Object();
        assertFalse(option.equals(obj));

        OptionImpl sameOption = new OptionImpl(1, true) {
            public boolean canProcess(final WriteableCommandLine commandLine,
                                      final ListIterator arguments) {
                return true;
            }
            public void appendUsage(final StringBuffer buffer,
                                     final DisplaySetting displaySetting,
                                     final Set optionTriggers) {
                // implementation of appendUsage for testing purposes
            }
            public void process(final WriteableCommandLine commandLine,
                                final ListIterator arguments) {
                // implementation of process for testing purposes
            }
            public void validate(final WriteableCommandLine commandLine) {
                // implementation of validate for testing purposes
            }
            public void defaults(final WriteableCommandLine commandLine) {
                // implementation of defaults for testing purposes
            }
            public boolean requiresAdvancedParsing() {
                return true;
            }
            public boolean isRequired() {
                return false;
            }
            public void addTriggers(final HashSet triggers,
                                    final HashSet matchTriggers) {
                // implementation of addTriggers for testing purposes
            }
            public void addPrefixes(final HashSet prefixes,
                                    final boolean lockedPrefixes) {
                // implementation of addPrefixes for testing purposes
            }
        };
        assertTrue(option.equals(sameOption));

        OptionImpl differentOption = new OptionImpl(2, false) {
            public boolean canProcess(final WriteableCommandLine commandLine,
                                      final ListIterator arguments) {
                return true;
            }
            public void appendUsage(final StringBuffer buffer,
                                     final DisplaySetting displaySetting,
                                     final Set optionTriggers) {
                // implementation of appendUsage for testing purposes
            }
            public void process(final WriteableCommandLine commandLine,
                                final ListIterator arguments) {
                // implementation of process for testing purposes
            }
            public void validate(final WriteableCommandLine commandLine) {
                // implementation of validate for testing purposes
            }
            public void defaults(final WriteableCommandLine commandLine) {
                // implementation of defaults for testing purposes
            }
            public boolean requiresAdvancedParsing() {
                return true;
            }
            public boolean isRequired() {
                return false;
            }
            public void addTriggers(final HashSet triggers,
                                    final HashSet matchTriggers) {
                // implementation of addTriggers for testing purposes
            }
            public void addPrefixes(final HashSet prefixes,
                                    final boolean lockedPrefixes) {
                // implementation of addPrefixes for testing purposes
            }
        };
        assertFalse(option.equals(differentOption));
    }

    @Test
    public void testHashCode() {
        int hashCode = option.getId();

        assertEquals(hashCode, option.hashCode());
    }

    @Test
    public void testFindOption() {
        OptionImpl foundOption = (OptionImpl) option.findOption("test");
        assertNotNull(foundOption);

        OptionImpl notFoundOption = (OptionImpl) option.findOption("notFound");
        assertNull(notFoundOption);
    }

    @Test
    public void testIsRequired() {
        assertFalse(option.isRequired());
    }

    @Test
    public void testDefaults() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        option.defaults(commandLine);
    }

    @Test
    public void testCheckPrefixes() {
        HashSet prefixes = new HashSet();
        prefixes.add("-");
        prefixes.add("--");
        option.checkPrefixes(prefixes);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixesThrowsException() {
        HashSet prefixes = new HashSet();
        prefixes.add("#");
        option.checkPrefixes(prefixes);
    }
}
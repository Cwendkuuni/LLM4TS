import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Set;
import org.apache.commons.cli2.*;
import org.apache.commons.cli2.option.OptionImpl;

public class OptionImplTest {

    private OptionImpl option;

    @Before
    public void setUp() {
        option = new OptionImpl(1, true) {
            public boolean canProcess(final WriteableCommandLine commandLine,
                                      final ListIterator arguments) {
                return true;
            }

            public void defaults(final WriteableCommandLine commandLine) {
                // do nothing
            }

            public Option findOption(String trigger) {
                return null;
            }
        };
    }

    @Test
    public void testCanProcess() {
        WriteableCommandLine commandLine = new DefaultWriteableCommandLine();
        ListIterator arguments = commandLine.getArguments().listIterator();

        assertFalse(option.canProcess(commandLine, arguments));

        commandLine.addArgument("test");

        assertTrue(option.canProcess(commandLine, arguments));
    }

    @Test
    public void testToString() {
        assertEquals("", option.toString());
    }

    @Test
    public void testGetId() {
        assertEquals(1, option.getId());
    }

    @Test
    public void testEquals() {
        OptionImpl option1 = new OptionImpl(1, true) {
            public boolean canProcess(final WriteableCommandLine commandLine,
                                      final ListIterator arguments) {
                return true;
            }

            public void defaults(final WriteableCommandLine commandLine) {
                // do nothing
            }

            public Option findOption(String trigger) {
                return null;
            }
        };

        OptionImpl option2 = new OptionImpl(2, true) {
            public boolean canProcess(final WriteableCommandLine commandLine,
                                      final ListIterator arguments) {
                return true;
            }

            public void defaults(final WriteableCommandLine commandLine) {
                // do nothing
            }

            public Option findOption(String trigger) {
                return null;
            }
        };

        OptionImpl option3 = new OptionImpl(1, false) {
            public boolean canProcess(final WriteableCommandLine commandLine,
                                      final ListIterator arguments) {
                return true;
            }

            public void defaults(final WriteableCommandLine commandLine) {
                // do nothing
            }

            public Option findOption(String trigger) {
                return null;
            }
        };

        OptionImpl option4 = new OptionImpl(1, true) {
            public boolean canProcess(final WriteableCommandLine commandLine,
                                      final ListIterator arguments) {
                return false;
            }

            public void defaults(final WriteableCommandLine commandLine) {
                // do nothing
            }

            public Option findOption(String trigger) {
                return null;
            }
        };

        OptionImpl option5 = new OptionImpl(1, true) {
            public boolean canProcess(final WriteableCommandLine commandLine,
                                      final ListIterator arguments) {
                return true;
            }

            public void defaults(final WriteableCommandLine commandLine) {
                // do nothing
            }

            public Option findOption(String trigger) {
                return this;
            }
        };

        assertTrue(option.equals(option1));
        assertFalse(option.equals(option2));
        assertFalse(option.equals(option3));
        assertFalse(option.equals(option4));
        assertFalse(option.equals(option5));
    }

    @Test
    public void testFindOption() {
        assertNull(option.findOption("test"));
    }

    @Test
    public void testIsRequired() {
        assertTrue(option.isRequired());
    }

    @Test
    public void testDefaults() {
        WriteableCommandLine commandLine = new DefaultWriteableCommandLine();
        option.defaults(commandLine);
        // no assertions, since method does nothing
    }

    @Test
    public void testCheckPrefixes() {
        Set prefixes = new HashSet();
        prefixes.add("-");

        try {
            option.checkPrefixes(prefixes);
            fail("IllegalArgumentException should have been thrown.");
        } catch (IllegalArgumentException e) {
            // expected exception
        }

        prefixes.add("--");
        option.checkPrefixes(prefixes);
        // no assertions, since method should not throw an exception
    }

}
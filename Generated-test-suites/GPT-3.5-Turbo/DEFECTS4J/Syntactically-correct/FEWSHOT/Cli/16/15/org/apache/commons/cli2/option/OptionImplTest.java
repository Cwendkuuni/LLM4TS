//Import required packages
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.util.HashSet;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Set;

import org.apache.commons.cli2.Action;
import org.apache.commons.cli2.ArgumentAcceptingOptionSpec;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Group;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.OptionImpl;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;
import org.junit.Before;
import org.junit.Test;

public class OptionImplTest {

    private OptionImpl option;

    @Before
    public void setUp() {
        option = new OptionImpl(1, true) {
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return false;
            }

            public void defaults(WriteableCommandLine commandLine) {
                // Do nothing
            }
        };
    }

    @Test
    public void testCanProcess() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        ListIterator arguments = mock(ListIterator.class);
        assertFalse(option.canProcess(commandLine, arguments));

        verify(arguments, times(1)).hasNext();
        verify(commandLine, never()).addValue(any());
    }

    @Test
    public void testToString() {
        String toString = option.toString();
        assertEquals("", toString);
    }

    @Test
    public void testEquals() {
        OptionImpl sameOption = new OptionImpl(1, true) {
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return false;
            }

            public void defaults(WriteableCommandLine commandLine) {
                // Do nothing
            }
        };
        OptionImpl differentOption = new OptionImpl(2, false) {
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return false;
            }

            public void defaults(WriteableCommandLine commandLine) {
                // Do nothing
            }
        };

        assertTrue(option.equals(option));
        assertTrue(option.equals(sameOption));
        assertFalse(option.equals(differentOption));
        assertFalse(option.equals(null));
        assertFalse(option.equals(new Object()));
    }

    @Test
    public void testHashCode() {
        int hashCode = option.hashCode();

        assertEquals(1, hashCode);
    }

    @Test
    public void testFindOption() {
        Option foundOption = option.findOption("trigger");

        assertNull(foundOption);
    }

    @Test
    public void testIsRequired() {
        assertTrue(option.isRequired());
    }

    @Test
    public void testDefaults() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);

        option.defaults(commandLine);

        verifyZeroInteractions(commandLine);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixesWithEmptyPrefixes() {
        Set prefixes = new HashSet();

        option.checkPrefixes(prefixes);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixesWithIncorrectTriggerPrefix() {
        Set prefixes = new HashSet();
        prefixes.add("-");

        option.checkPrefixes(prefixes);
    }

    @Test
    public void testCheckPrefixesWithCorrectTriggerPrefix() {
        Set prefixes = new HashSet();
        prefixes.add("1");

        option.checkPrefixes(prefixes);
    }
}
package org.apache.commons.cli2.option;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;
import org.apache.commons.cli2.option.OptionImpl;
import org.apache.commons.cli2.option.OptionType;

public class OptionImplTest {

    @Test
    public void testConstructor() {
        OptionImpl option = new OptionImpl(1, true);
        assertEquals(1, option.getId());
        assertTrue(option.isRequired());

        OptionImpl option2 = new OptionImpl(2, false);
        assertEquals(2, option2.getId());
        assertFalse(option2.isRequired());
    }

    @Test
    public void testCanProcess() {
        OptionImpl option = new OptionImpl(1, true) {
            @Override
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return argument.equals("--foo");
            }
        };

        WriteableCommandLine commandLine = new WriteableCommandLine();
        String[] arguments = {"--foo", "bar"};
        ListIterator<String> iterator = Arrays.asList(arguments).listIterator();

        assertTrue(option.canProcess(commandLine, iterator));

        assertFalse(option.canProcess(commandLine, iterator));
        assertFalse(option.canProcess(commandLine, iterator));
        assertFalse(option.canProcess(commandLine, iterator));
    }

    @Test
    public void testToString() {
        OptionImpl option = new OptionImpl(1, true);
        String expected = "OptionImpl[id=1, required=true]";
        assertEquals(expected, option.toString());
    }

    @Test
    public void testEquals() {
        OptionImpl option1 = new OptionImpl(1, true) {
            @Override
            public String getPreferredName() {
                return "option1";
            }
        };

        OptionImpl option2 = new OptionImpl(1, true) {
            @Override
            public String getPreferredName() {
                return "option1";
            }
        };

        OptionImpl option3 = new OptionImpl(2, false);

        assertEquals(option1, option1);
        assertEquals(option1, option2);
        assertNotEquals(option1, option3);
    }

    @Test
    public void testHashCode() {
        OptionImpl option1 = new OptionImpl(1, true) {
            @Override
            public String getPreferredName() {
                return "option1";
            }
        };

        OptionImpl option2 = new OptionImpl(1, true) {
            @Override
            public String getPreferredName() {
                return "option1";
            }
        };

        OptionImpl option3 = new OptionImpl(2, false);

        assertEquals(option1.hashCode(), option1.hashCode());
        assertEquals(option1.hashCode(), option2.hashCode());
        assertNotEquals(option1.hashCode(), option3.hashCode());
    }

    @Test
    public void testFindOption() {
        OptionImpl option = new OptionImpl(1, true) {
            @Override
            public Iterable<String> getTriggers() {
                return Arrays.asList("--foo", "--bar");
            }
        };

        assertNull(option.findOption(null));
        assertNull(option.findOption(""));
        assertNull(option.findOption("--baz"));
        assertEquals(option, option.findOption("--foo"));
        assertEquals(option, option.findOption("--bar"));
    }

    @Test
    public void testIsRequired() {
        OptionImpl option1 = new OptionImpl(1, true);
        assertTrue(option1.isRequired());

        OptionImpl option2 = new OptionImpl(2, false);
        assertFalse(option2.isRequired());
    }

    @Test
    public void testDefaults() {
        OptionImpl option = new OptionImpl(1, true) {
            @Override
            public void defaults(WriteableCommandLine commandLine) {
                commandLine.setValue("--foo", true);
            }
        };

        WriteableCommandLine commandLine = new WriteableCommandLine();
        option.defaults(commandLine);
        assertTrue(commandLine.getValue("--foo", false));
    }

    @Test
    public void testCheckPrefixes() {
        OptionImpl option = new OptionImpl(1, true) {
            @Override
            public Iterable<String> getPrefixes() {
                return Arrays.asList("--", "-f");
            }
        };

        Set<String> prefixes = new HashSet<>();
        prefixes.add("--");
        prefixes.add("-f");

        option.checkPrefixes(prefixes);

        prefixes.add("--bar");
        try {
            option.checkPrefixes(prefixes);
            fail();
        } catch (IllegalArgumentException e) {
            String expectedMessage = "OptionImpl[id=1, required=true] requires prefixes: [--, -f], but was triggered by: --bar";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}
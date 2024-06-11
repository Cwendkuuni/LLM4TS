package org.apache.commons.cli2.option;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;
import org.apache.commons.cli2.util.TestUtils;

public class OptionImplTest {

    @Test
    public void testOptionImplConstructor() {
        OptionImpl option = new OptionImpl(1, false);
        assertEquals(1, option.getId());
        assertFalse(option.isRequired());
    }

    @Test
    public void testCanProcess() {
        OptionImpl option = new OptionImpl(1, true);
        WriteableCommandLine commandLine = TestUtils.createMockCommandLine();
        ListIterator<String> arguments = commandLine.getParsedArgs().listIterator();

        assertFalse(option.canProcess(commandLine, arguments));

        arguments.next(); // consume the first argument
        assertTrue(option.canProcess(commandLine, arguments));
    }

    @Test
    public void testToString() {
        OptionImpl option = new OptionImpl(1, false);
        String expected = "OptionImpl[id=1, required=false]";
        assertEquals(expected, option.toString());
    }

    @Test
    public void testEquals() {
        OptionImpl option1 = new OptionImpl(1, false);
        OptionImpl option2 = new OptionImpl(1, false);
        OptionImpl option3 = new OptionImpl(2, false);

        assertTrue(option1.equals(option1));
        assertTrue(option1.equals(option2));

        assertFalse(option1.equals(option3));
        assertFalse(option1.equals(null));
    }

    @Test
    public void testHashCode() {
        OptionImpl option = new OptionImpl(1, false);
        String name = "testName";
        String description = "testDescription";
        Set<String> prefixes = new HashSet<>(Arrays.asList("--test", "-t"));
        Set<String> triggers = new HashSet<>(Arrays.asList("test", "t"));

        int hashCode1 = option.hashCode();
        OptionImpl option2 = new OptionImpl(1, false) {
            @Override
            public String getPreferredName() {
                return name;
            }

            @Override
            public String getDescription() {
                return description;
            }

            @Override
            public Iterable<String> getPrefixes() {
                return prefixes;
            }

            @Override
            public Iterable<String> getTriggers() {
                return triggers;
            }
        };

        int hashCode2 = option2.hashCode();

        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testFindOption() {
        OptionImpl option = new OptionImpl(1, false);
        OptionImpl option2 = new OptionImpl(2, false);

        assertTrue(option.findOption(null) == null);
        assertNull(option.findOption(null));

        assertTrue(option.findOption("test1") == null);
        assertNull(option.findOption("test1"));

        assertTrue(option.findOption("test2") == option2);
        assertEquals(option2, option.findOption("test2"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixes() {
        OptionImpl option = new OptionImpl(1, false);
        Set<String> prefixes = new HashSet<>(Arrays.asList("--", "-"));

        option.checkPrefixes(prefixes);
    }

    @Test
    public void testCheckPrefixesWithPrefix() {
        OptionImpl option = new OptionImpl(1, false);
        Set<String> prefixes = new HashSet<>(Arrays.asList("--test"));

        option.checkPrefixes(prefixes);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixesWithNoMatchingPrefix() {
        OptionImpl option = new OptionImpl(1, false);
        Set<String> prefixes = new HashSet<>(Arrays.asList("--test2"));

        option.checkPrefixes(prefixes);
    }
}
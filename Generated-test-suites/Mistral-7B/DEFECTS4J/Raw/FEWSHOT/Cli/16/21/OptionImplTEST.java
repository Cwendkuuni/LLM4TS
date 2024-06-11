package org.apache.commons.cli2.option;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OptionImplTest {
    private ResourceHelper resourceHelper;

    @Before
    public void setup() {
        resourceHelper = ResourceHelper.getResourceHelper();
    }

    @Test
    public void testDefaultConstructor() {
        OptionImpl option = new OptionImpl(1, false);
        assertNotNull(option);
        assertEquals(1, option.getId());
        assertFalse(option.isRequired());
        assertNull(option.getPreferredName());
        assertNull(option.getDescription());
        assertEquals(0, option.getTriggers().size());
        assertEquals(0, option.getPrefixes().size());
    }

    @Test
    public void testConstructorWithIdAndRequired() {
        OptionImpl option = new OptionImpl(1, true);
        assertNotNull(option);
        assertEquals(1, option.getId());
        assertTrue(option.isRequired());
        assertNull(option.getPreferredName());
        assertNull(option.getDescription());
        assertEquals(0, option.getTriggers().size());
        assertEquals(0, option.getPrefixes().size());
    }

    @Test
    public void testCanProcess() {
        OptionImpl option = new OptionImpl(1, false);
        WriteableCommandLine commandLine = new WriteableCommandLine();
        List<String> arguments = Arrays.asList("--option1", "arg1");

        Iterator<String> argumentsIterator = arguments.listIterator();

        assertFalse(option.canProcess(commandLine, argumentsIterator));

        option = new OptionImpl(1, true);
        assertFalse(option.canProcess(commandLine, argumentsIterator));

        arguments.add("--option1");
        assertTrue(option.canProcess(commandLine, argumentsIterator));

        assertFalse(option.canProcess(commandLine, argumentsIterator));

        arguments.add("arg2");
        assertFalse(option.canProcess(commandLine, argumentsIterator));
    }

    @Test
    public void testToString() {
        OptionImpl option = new OptionImpl(1, false);
        String expected = String.format("%s%n", option.toString());
        String actual = option.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        OptionImpl option1 = new OptionImpl(1, false);
        OptionImpl option2 = new OptionImpl(1, false);
        OptionImpl option3 = new OptionImpl(2, false);
        OptionImpl option4 = new OptionImpl(1, false);

        assertTrue(option1.equals(option1));
        assertTrue(option1.equals(option2));
        assertTrue(option2.equals(option1));
        assertTrue(option1.equals(option4));
        assertTrue(option4.equals(option1));

        assertFalse(option1.equals(option3));
        assertFalse(option3.equals(option1));
    }

    @Test
    public void testHashCode() {
        OptionImpl option1 = new OptionImpl(1, false);
        OptionImpl option2 = new OptionImpl(1, false);
        OptionImpl option3 = new OptionImpl(2, false);

        int hashCode1 = option1.hashCode();
        int hashCode2 = option2.hashCode();
        int hashCode3 = option3.hashCode();

        assertEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
    }

    @Test
    public void testFindOption() {
        OptionImpl option1 = new OptionImpl(1, false);
        OptionImpl option2 = new OptionImpl(2, false);

        List<OptionImpl> options = Arrays.asList(option1, option2);

        OptionImpl foundOption = option1.findOption("--option1");
        assertNotNull(foundOption);
        assertEquals(option1, foundOption);

        foundOption = option1.findOption("--option2");
        assertNull(foundOption);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixesWithInvalidPrefixes() {
        OptionImpl option = new OptionImpl(1, false);
        Set<String> prefixes = new HashSet<>();
        prefixes.add("option");

        option.checkPrefixes(prefixes);
    }

    @Test
    public void testCheckPrefixesWithValidPrefixes() {
        OptionImpl option = new OptionImpl(1, false);
        Set<String> prefixes = new HashSet<>();
        prefixes.add("option--");

        option.checkPrefixes(prefixes);
    }

    @Test
    public void testCheckPrefixesWithValidTriggers() {
        OptionImpl option = new OptionImpl(1, false) {
            @Override
            public List<String> getTriggers() {
                return Arrays.asList("option", "opt");
            }
        };
        Set<String> prefixes = new HashSet<>();
        prefixes.add("option--");

        option.checkPrefixes(prefixes);
    }
}
package org.junit.tests.optionimpl;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Assert.*;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.ListIterator;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class OptionImplTest {
    @Test
    public void testConstructorWithInvalidArguments() {
        assertThrows(IllegalArgumentException.class, () -> new OptionImpl(-1, false));
        assertThrows(IllegalArgumentException.class, () -> new OptionImpl(1, "not a boolean"));
    }

    @Test
    public void testCanProcessWithEmptyArguments() {
        OptionImpl option = new OptionImpl(1, false);
        WriteableCommandLine commandLine = new WriteableCommandLine();
        ListIterator arguments = commandLine.getParsedArgs().iterator();

        assertFalse(option.canProcess(commandLine, arguments));
    }

    @Test
    public void testCanProcessWithMismatchedArgument() {
        OptionImpl option = new OptionImpl(1, false);
        WriteableCommandLine commandLine = new WriteableCommandLine();
        ListIterator arguments = commandLine.getParsedArgs().iterator();

        arguments.next(); // consume the first argument
        arguments.next(); // consume the second argument

        assertFalse(option.canProcess(commandLine, arguments));
    }

    @Test
    public void testFindOptionWithNullTrigger() {
        OptionImpl option = new OptionImpl(1, false);
        assertNull(option.findOption(null));
    }

    @Test
    public void testFindOptionWithMatchingTrigger() {
        OptionImpl option1 = new OptionImpl(1, false);
        OptionImpl option2 = new OptionImpl(2, true);
        Set<String> prefixes = new HashSet<>(Arrays.asList("option1", "option2"));

        assertEquals(option1, option1.findOption("option1"));
        assertEquals(option2, option2.findOption("option2"));
    }

    @Test
    public void testEqualsWithIdenticalObjects() {
        OptionImpl option1 = new OptionImpl(1, false);
        OptionImpl option2 = new OptionImpl(1, false);

        assertTrue(option1.equals(option2));
        assertTrue(option2.equals(option1));
    }

    @Test
    public void testEqualsWithNonIdenticalObjects() {
        OptionImpl option1 = new OptionImpl(1, false);
        OptionImpl option2 = new OptionImpl(2, false);

        assertFalse(option1.equals(option2));
        assertFalse(option2.equals(option1));
    }

    @Test
    public void testIsRequiredWithRequiredOption() {
        OptionImpl option = new OptionImpl(1, true);

        assertTrue(option.isRequired());
    }

    @Test
    public void testIsRequiredWithNonRequiredOption() {
        OptionImpl option = new OptionImpl(1, false);

        assertFalse(option.isRequired());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixesWithInvalidPrefixes() {
        OptionImpl option = new OptionImpl(1, false);
        Set<String> prefixes = new HashSet<>(Arrays.asList("invalid", "prefix"));

        option.checkPrefixes(prefixes);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixesWithInvalidTrigger() {
        OptionImpl option = new OptionImpl(1, false);
        Set<String> prefixes = new HashSet<>(Arrays.asList("option1"));

        option.checkPrefixes(prefixes);
        option.findOption("invalidTrigger");
    }
}
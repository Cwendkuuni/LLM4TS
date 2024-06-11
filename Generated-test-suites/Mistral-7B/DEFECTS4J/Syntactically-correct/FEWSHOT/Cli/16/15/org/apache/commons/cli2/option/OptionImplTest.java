package org.apache.commons.cli2.option;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;
import org.junit.Test;

public class OptionImplTest {

    @Test
    public void testOptionImplConstruction() {
        OptionImpl option = new OptionImpl(1, true);
        assertEquals(1, option.getId());
        assertTrue(option.isRequired());
    }

    @Test
    public void testCanProcessTrue() {
        OptionImpl option = new OptionImpl(1, true);
        WriteableCommandLine commandLine = new WriteableCommandLine();
        List<String> arguments = Arrays.asList("--option1", "arg1");
        ListIterator<String> argsIterator = arguments.listIterator();

        assertTrue(option.canProcess(commandLine, argsIterator));
        assertEquals("arg1", commandLine.getOptionValue("option1"));
    }

    @Test
    public void testCanProcessFalse() {
        OptionImpl option = new OptionImpl(1, true);
        WriteableCommandLine commandLine = new WriteableCommandLine();
        List<String> arguments = Arrays.asList("arg1");
        ListIterator<String> argsIterator = arguments.listIterator();

        assertFalse(option.canProcess(commandLine, argsIterator));
    }

    @Test
    public void testToString() {
        OptionImpl option = new OptionImpl(1, true);
        String expected = "OptionImpl[id=1, required=true]";
        assertEquals(expected, option.toString());
    }

    @Test
    public void testEqualsTrue() {
        OptionImpl option1 = new OptionImpl(1, true);
        OptionImpl option2 = new OptionImpl(1, true);

        assertTrue(option1.equals(option2));
    }

    @Test
    public void testEqualsFalse() {
        OptionImpl option1 = new OptionImpl(1, true);
        Object other = new Object();

        assertFalse(option1.equals(other));
    }

    @Test
    public void testFindOptionTrue() {
        OptionImpl option = new OptionImpl(1, true);
        List<String> triggers = Arrays.asList("option1", "o1");
        option.setTriggers(triggers);

        OptionImpl foundOption = option.findOption("o1");
        assertEquals(option, foundOption);
    }

    @Test
    public void testFindOptionFalse() {
        OptionImpl option = new OptionImpl(1, true);
        List<String> triggers = Arrays.asList("option1", "o1");
        option.setTriggers(triggers);

        OptionImpl foundOption = option.findOption("option2");
        assertNull(foundOption);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixesWithInvalidPrefix() {
        String invalidPrefix = "option";
        OptionImpl option = new OptionImpl(1, true);
        Set<String> prefixes = Arrays.asList(invalidPrefix).asList();
        option.checkPrefixes(prefixes);
    }

    @Test
    public void testCheckPrefixesWithValidPrefix() {
        String validPrefix = "--option";
        OptionImpl option = new OptionImpl(1, true);
        Set<String> prefixes = Arrays.asList(validPrefix).asList();
        option.checkPrefixes(prefixes);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixesWithEmptySet() {
        OptionImpl option = new OptionImpl(1, true);
        Set<String> prefixes = Collections.emptySet();
        option.checkPrefixes(prefixes);
    }
}
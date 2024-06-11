package org.apache.commons.cli2.option;

import static org.junit.Assert.*;

import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class OptionImplTest {

    @Test
    public void testOptionEquals() {
        Option option1 = new Option("--option1", "Option 1");
        Option option2 = new Option("--option1", "Option 1");
        Option option3 = new Option("--option2", "Option 2");

        assertTrue(option1.equals(option1));
        assertTrue(option1.equals(option2));
        assertFalse(option1.equals(option3));
    }

    @Test
    public void testOptionHashCode() {
        Option option1 = new Option("--option1", "Option 1");
        Option option2 = new Option("--option1", "Option 1");
        Option option3 = new Option("--option2", "Option 2");

        int hashCode1 = option1.hashCode();
        int hashCode2 = option2.hashCode();
        int hashCode3 = option3.hashCode();

        assertEquals(hashCode1, hashCode2); // equal options should have equal hash codes
        assertNotEquals(hashCode1, hashCode3); // different options should have different hash codes
    }

    @Test
    public void testOptionFindOption() {
        Set<String> triggers = new HashSet<>(Arrays.asList("--option1", "--opt1"));
        Option option = new Option("--option1", "Option 1");

        assertEquals(option, option.findOption("--option1"));
        assertEquals(option, option.findOption("--opt1"));
        assertNull(option.findOption("--option2"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionCheckPrefixesInvalidTrigger() {
        Set<String> prefixes = new HashSet<>(Arrays.asList("--"));
        Option option = new Option("--option1", "Option 1");

        option.checkPrefixes(prefixes);
    }

    @Test
    public void testOptionCheckPrefixesValidPrefixes() {
        Set<String> prefixes = new HashSet<>(Arrays.asList("--option", "--o"));
        Option option = new Option("--option1", "Option 1");

        option.checkPrefixes(prefixes);
    }

    @Test
    public void testOptionToString() {
        Option option = new Option("--option1", "Option 1");

        assertEquals("--option1\nOption 1\n", option.toString());
    }

    @Test
    public void testOptionCanProcess() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        Option option = new Option("--option1", "Option 1");

        assertTrue(option.canProcess(commandLine, new StringIterator("--option1")));
        assertFalse(option.canProcess(commandLine, new StringIterator("--option2")));
    }

    private static class StringIterator implements Iterator<String> {
        private ListIterator<String> listIterator;

        public StringIterator(String[] args) {
            listIterator = Arrays.asList(args).listIterator();
        }

        @Override
        public boolean hasNext() {
            return listIterator.hasNext();
        }

        @Override
        public String next() {
            return listIterator.next();
        }
    }

    @Test
    public void testOptionDefaults() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        Option option = new Option("--option1", "Option 1");

        option.defaults(commandLine);

        // nothing to verify as defaults method does not do anything by default
    }
}
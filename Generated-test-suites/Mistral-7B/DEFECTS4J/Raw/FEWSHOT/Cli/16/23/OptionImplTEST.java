package org.apache.commons.cli2.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;

public class OptionImplTest {

    @Test
    public void testOptionImplConstructor() {
        final OptionImpl option = new OptionImpl(1, true);
        assertEquals(1, option.getId());
        assertTrue(option.isRequired());
    }

    @Test
    public void testCanProcessTrue() {
        final OptionImpl option = new OptionImpl(1, false);
        final WriteableCommandLine commandLine = new WriteableCommandLine();
        final String argument = "test";

        assertTrue(option.canProcess(commandLine, Arrays.asList(argument).listIterator()));
    }

    @Test
    public void testCanProcessFalse() {
        final OptionImpl option = new OptionImpl(1, false);
        final WriteableCommandLine commandLine = new WriteableCommandLine();

        assertFalse(option.canProcess(commandLine, Arrays.asList().listIterator()));
    }

    @Test
    public void testToString() {
        final OptionImpl option = new OptionImpl(1, false);
        final StringBuffer expected = new StringBuffer();
        option.appendUsage(expected, DisplaySetting.ALL, null);

        assertEquals("", expected.toString());
    }

    @Test
    public void testEquals() {
        final OptionImpl option1 = new OptionImpl(1, false);
        final OptionImpl option2 = new OptionImpl(1, false);
        assertEquals(option1, option2);

        final OptionImpl option3 = new OptionImpl(2, false);
        assertFalse(option1.equals(option3));
    }

    @Test
    public void testHashCode() {
        final OptionImpl option1 = new OptionImpl(1, null);
        final OptionImpl option2 = new OptionImpl(1, null);
        assertEquals(option1.hashCode(), option2.hashCode());

        final OptionImpl option3 = new OptionImpl(2, null);
        assertNotEquals(option1.hashCode(), option3.hashCode());

        final OptionImpl option4 = new OptionImpl(1, "test");
        assertNotEquals(option1.hashCode(), option4.hashCode());
    }

    @Test
    public void testFindOption() {
        final OptionImpl option1 = new OptionImpl(1, null);
        final OptionImpl option2 = new OptionImpl(2, null);
        Set<String> triggers1 = new HashSet<>();
        triggers1.add("test1");
        option1.setTriggers(triggers1);
        Set<String> triggers2 = new HashSet<>();
        triggers2.add("test2");
        option2.setTriggers(triggers2);

        assertNull(option1.findOption("test3"));
        assertSame(option1, option1.findOption("test1"));
        assertSame(option2, option2.findOption("test2"));
    }

    @Test
    public void testIsRequired() {
        final OptionImpl option1 = new OptionImpl(1, true);
        assertTrue(option1.isRequired());

        final OptionImpl option2 = new OptionImpl(2, false);
        assertFalse(option2.isRequired());
    }

    @Test
    public void testCheckPrefixesEmptyPrefixes() {
        final OptionImpl option = new OptionImpl(1, null);
        final Set<String> prefixes = new HashSet<>();
        option.checkPrefixes(prefixes);
    }

    @Test
    public void testCheckPrefixesNoMatch() {
        final OptionImpl option = new OptionImpl(1, null);
        final Set<String> prefixes = new HashSet<>();
        prefixes.add("prefix1");

        try {
            option.checkPrefixes(prefixes);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testCheckPrefixesMatch() {
        final OptionImpl option = new OptionImpl(1, null);
        final Set<String> prefixes = new HashSet<>();
        prefixes.add("pref");
        option.setPreferredName("pref_option");

        option.checkPrefixes(prefixes);
    }

    @Test
    public void testCheckPrefixesMultipleTriggers() {
        final OptionImpl option = new OptionImpl(1, null);
        final Set<String> prefixes = new HashSet<>();
        prefixes.add("pref");
        Set<String> triggers = new HashSet<>();
        triggers.add("trigger1");
        triggers.add("trigger2");
        option.setTriggers(triggers);
        option.setPreferredName("pref_option");

        option.checkPrefixes(prefixes);
    }
}
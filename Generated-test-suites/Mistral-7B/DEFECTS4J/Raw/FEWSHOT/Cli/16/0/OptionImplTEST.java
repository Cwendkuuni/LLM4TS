package org.apache.commons.cli2.option;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;

public class OptionImplTest {

    @Test
    public void testOptionImpl() {
        final int id = 1;
        final boolean required = true;

        OptionImpl option = new OptionImpl(id, required);
        assertEquals(id, option.getId());
        assertTrue(required);
    }

    @Test
    public void testCanProcessNullCommandLine() {
        final OptionImpl option = new OptionImpl(1, false);
        assertFalse(option.canProcess(null, null));
    }

    @Test
    public void testCanProcessEmptyArgumentList() {
        final OptionImpl option = new OptionImpl(1, false);
        assertFalse(option.canProcess(null, Collections.emptyList().listIterator()));
    }

    @Test
    public void testCanProcessNullArgument() {
        final OptionImpl option = new OptionImpl(1, false);
        assertFalse(option.canProcess(null, null));
    }

    @Test
    public void testCanProcessInvalidArgument() {
        final OptionImpl option = new OptionImpl(1, false);
        assertFalse(option.canProcess(null, Arrays.asList("foo").listIterator()));
    }

    @Test
    public void testCanProcessValidArgument() {
        final OptionImpl option = new OptionImpl(1, false);
        final WriteableCommandLine commandLine = new WriteableCommandLine();
        final ListIterator<String> arguments = Arrays.asList("--option", "value").listIterator();
        assertTrue(option.canProcess(commandLine, arguments));
    }

    @Test
    public void testFindOptionNullTrigger() {
        final OptionImpl option = new OptionImpl(1, false);
        assertNull(option.findOption(null));
    }

    @Test
    public void testFindOptionValidTrigger() {
        final OptionImpl option = new OptionImpl(1, false);
        final Set<String> triggers = new HashSet<>();
        triggers.add("option");
        option.setTriggers(triggers);
        assertEquals(option, option.findOption("option"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixesWithoutPrefixes() {
        final OptionImpl option = new OptionImpl(1, false);
        option.checkPrefixes(Collections.emptySet());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixesWithInvalidPrefix() {
        final OptionImpl option = new OptionImpl(1, false);
        final Set<String> prefixes = new HashSet<>();
        prefixes.add("option");
        option.checkPrefixes(prefixes);
    }

    @Test
    public void testCheckPrefixesWithValidPrefix() {
        final OptionImpl option = new OptionImpl(1, false);
        final Set<String> prefixes = new HashSet<>();
        prefixes.add("option");
        option.checkPrefixes(prefixes);
    }

    @Test
    public void testToString() {
        final OptionImpl option = new OptionImpl(1, false);
        final StringBuffer buffer = new StringBuffer();
        option.appendUsage(buffer, DisplaySetting.ALL, null);
        assertEquals("", buffer.toString());
    }

    @Test
    public void testEqualsNull() {
        final OptionImpl option1 = new OptionImpl(1, false);
        final Object nullObj = null;
        assertFalse(option1.equals(nullObj));
    }

    @Test
    public void testEqualsSameInstance() {
        final OptionImpl option1 = new OptionImpl(1, false);
        assertTrue(option1.equals(option1));
    }

    @Test
    public void testEqualsDifferentInstance() {
        final OptionImpl option1 = new OptionImpl(1, false);
        final OptionImpl option2 = new OptionImpl(1, false);
        assertFalse(option1.equals(option2));
    }

    @Test
    public void testHashCodeId() {
        final OptionImpl option = new OptionImpl(1, false);
        assertEquals(1, option.hashCode());
    }

    @Test
    public void testHashCodePreferredName() {
        final OptionImpl option = new OptionImpl(1, false);
        option.setPreferredName("test");
        assertEquals(1 * 37 + "test".hashCode(), option.hashCode());
    }

    @Test
    public void testHashCodeDescription() {
        final OptionImpl option = new OptionImpl(1, false);
        option.setDescription("test");
        assertEquals(1 * 37 + "test".hashCode(), option.hashCode());
    }

    @Test
    public void testHashCodePrefixes() {
        final OptionImpl option = new OptionImpl(1, false);
        final Set<String> prefixes = new HashSet<>();
        prefixes.add("test");
        option.setPrefixes(prefixes);
        assertEquals(1 * 37 + 1 * 37 + prefixes.hashCode(), option.hashCode());
    }

    @Test
    public void testHashCodeTriggers() {
        final OptionImpl option = new OptionImpl(1, false);
        final Set<String> triggers = new HashSet<>();
        triggers.add("test");
        option.setTriggers(triggers);
        assertEquals(1 * 37 + 1 * 37 + triggers.hashCode(), option.hashCode());
    }
}
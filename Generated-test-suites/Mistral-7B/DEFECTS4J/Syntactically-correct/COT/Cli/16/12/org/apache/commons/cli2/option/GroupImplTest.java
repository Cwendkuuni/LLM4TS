package org.apache.commons.cli2.option;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.DisplaySetting;
import org.apache.commons.cli2.Group;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class GroupImplTest {
    private static final Comparator<String> COMPARATOR = new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            return o1.compareTo(o2);
        }
    };

    private WriteableCommandLine commandLine;
    private GroupImpl group;

    @Before
    public void setUp() {
        commandLine = Mockito.mock(WriteableCommandLine.class);
        final List options = new ArrayList();

        final Option opt1 = Mockito.mock(Option.class);
        final Argument arg1 = Mockito.mock(Argument.class);
        final Option opt2 = Mockito.mock(Option.class);
        final Option opt3 = Mockito.mock(Option.class);

        Mockito.when(opt1.isRequired()).thenReturn(false);
        Mockito.when(opt1.getTriggers()).thenReturn(Collections.singleton("t1"));
        Mockito.when(opt1.getDefaultValue()).thenReturn("d1");
        Mockito.when(opt1.getName()).thenReturn("o1");

        Mockito.when(opt2.isRequired()).thenReturn(true);
        Mockito.when(opt2.getTriggers()).thenReturn(Collections.singleton("t2"));
        Mockito.when(opt2.getDefaultValue()).thenReturn("d2");
        Mockito.when(opt2.getName()).thenReturn("o2");

        Mockito.when(opt3.isRequired()).thenReturn(true);
        Mockito.when(opt3.getTriggers()).thenReturn(Collections.singleton("t3"));
        Mockito.when(opt3.getDefaultValue()).thenReturn("d3");
        Mockito.when(opt3.getName()).thenReturn("o3");

        Mockito.when(arg1.isRequired()).thenReturn(false);
        Mockito.when(arg1.getDefaultValue()).thenReturn("dA1");
        Mockito.when(arg1.getName()).thenReturn("a1");

        options.add(opt1);
        options.add(arg1);
        options.add(opt2);
        options.add(opt3);

        group = new GroupImpl(options, null, null, 1, 3);
    }

    // Test for basic functionality

    @Test
    public void testGetPreferredName() {
        assertEquals("o1", group.getPreferredName());
    }

    @Test
    public void testGetDescription() {
        assertNull(group.getDescription());
    }

    @Test
    public void testCanProcess() {
        Mockito.when(commandLine.looksLikeOption("t1")).thenReturn(true);

        assertFalse(group.canProcess(commandLine, null));
        assertTrue(group.canProcess(commandLine, "t1"));
    }

    @Test
    public void testGetTriggers() {
        Set triggers = group.getTriggers();
        assertEquals(3, triggers.size());
        assertTrue(triggers.contains("t1"));
        assertTrue(triggers.contains("t2"));
        assertTrue(triggers.contains("t3"));
    }

    @Test
    public void testGetPrefixes() {
        Set prefixes = group.getPrefixes();
        assertTrue(prefixes.isEmpty());
    }

    @Test
    public void testValidate() throws OptionException {
        Mockito.when(commandLine.hasOption("o1")).thenReturn(false);
        Mockito.when(commandLine.hasOption("o2")).thenReturn(true);
        Mockito.when(commandLine.hasOption("o3")).thenReturn(true);

        group.validate(commandLine);
        assertTrue(group.isRequired());
        assertTrue(group.getMinimum() > 0);
        assertTrue(group.getMaximum() > 0);
    }

    // Test for edge cases and exceptions

    @Test(expected = OptionException.class)
    public void testValidateWithNoOption() throws OptionException {
        Mockito.when(commandLine.hasOption("o1")).thenReturn(false);

        group.validate(commandLine);
        assertFalse(group.isRequired());
        assertFalse(group.getMinimum() > 0);
        assertFalse(group.getMaximum() > 0);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithTooFewOptions() throws OptionException {
        Mockito.when(commandLine.hasOption("o1")).thenReturn(true);

        group.validate(commandLine);
        assertTrue(group.isRequired());
        assertTrue(group.getMinimum() > 0);
        assertFalse(group.getMaximum() > 0);
        assertThrows(OptionException.class, () -> group.validate(commandLine));
    }

    @Test(expected = OptionException.class)
    public void testValidateWithTooManyOptions() throws OptionException {
        Mockito.when(commandLine.hasOption("o1")).thenReturn(true);
        Mockito.when(commandLine.hasOption("o2")).thenReturn(true);
        Mockito.when(commandLine.hasOption("o3")).thenReturn(true);
        Mockito.when(commandLine.hasOption("o4")).thenReturn(true);

        group.validate(commandLine);
        assertTrue(group.isRequired());
        assertTrue(group.getMinimum() > 0);
        assertTrue(group.getMaximum() > 0);
        assertThrows(OptionException.class, () -> group.validate(commandLine));
    }

    @Test(expected = OptionException.class)
    public void testValidateWithInvalidOption() throws OptionException {
        Mockito.when(commandLine.hasOption("x")).thenReturn(true);

        assertThrows(OptionException.class, () -> group.validate(commandLine));
        assertFalse(group.isRequired());
        assertFalse(group.getMinimum() > 0);
        assertFalse(group.getMaximum() > 0);
    }

    // Test for help generation

    @Test
    public void testAppendUsage() {
        final StringBuilder buffer = new StringBuilder();
        group.appendUsage(buffer, null, null);
        assertEquals("o1\n", buffer.toString());

        group.appendUsage(buffer, new HashSet<>(DisplaySetting.NONE), COMPARATOR);
        assertEquals("o1 [t1]\n", buffer.toString());

        group.appendUsage(buffer, new HashSet<>(DisplaySetting.DISPLAY_OPTIONAL), COMPARATOR);
        assertEquals("o1 [t1]\n", buffer.toString());

        group.appendUsage(buffer, new HashSet<>(DisplaySetting.DISPLAY_GROUP_NAME), COMPARATOR);
        assertEquals("o1 ()\n", buffer.toString());

        group.appendUsage(buffer, new HashSet<>(DisplaySetting.DISPLAY_OPTIONAL), COMPARATOR);
        assertEquals("o1 ()\n", buffer.toString());

        group.appendUsage(buffer, new HashSet<>(DisplaySetting.DISPLAY_GROUP_EXPANDED), COMPARATOR);
        assertEquals("o1 ()\n", buffer.toString());

        group.appendUsage(buffer, new HashSet<>(DisplaySetting.DISPLAY_GROUP_EXPANDED, DisplaySetting.DISPLAY_OPTIONAL), COMPARATOR);
        assertEquals("o1 ()\n", buffer.toString());

        group.appendUsage(buffer, new HashSet<>(DisplaySetting.DISPLAY_GROUP_EXPANDED, DisplaySetting.DISPLAY_OPTIONAL, DisplaySetting.DISPLAY_GROUP_NAME), COMPARATOR);
        assertEquals("o1 (\n  t1\n)", buffer.toString());

        group.appendUsage(buffer, new HashSet<>(DisplaySetting.DISPLAY_GROUP_NAME, DisplaySetting.DISPLAY_GROUP_EXPANDED), COMPARATOR);
        assertEquals("o1 (\n  o1 [t1]\n  o2 [t2]\n  o3 [t3]\n)", buffer.toString());
    }

    @Test
    public void testHelpLines() {
        final List helpLines = group.helpLines(0, new HashSet<>(DisplaySetting.DISPLAY_GROUP_NAME, DisplaySetting.DISPLAY_GROUP_EXPANDED), null);
        assertEquals(3, helpLines.size());

        final HelpLine helpLine1 = (HelpLine) helpLines.get(0);
        assertEquals("o1 (", helpLine1.getLine());

        final HelpLine helpLine2 = (HelpLine) helpLines.get(1);
        assertEquals("o1 [t1]\n", helpLine2.getLine());

        final HelpLine helpLine3 = (HelpLine) helpLines.get(2);
        assertEquals("o2 [t2]\n", helpLine3.getLine());
    }

    // Test for default values

    @Test
    public void testDefaults() {
        group.defaults(commandLine);
        Mockito.verify(opt1, Mockito.times(1)).defaults(commandLine);
        Mockito.verify(opt2, Mockito.times(1)).defaults(commandLine);
        Mockito.verify(opt3, Mockito.times(1)).defaults(commandLine);
        Mockito.verify(arg1, Mockito.times(1)).defaults(commandLine);
    }
}
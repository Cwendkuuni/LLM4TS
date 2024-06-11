package org.apache.commons.cli2.option;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.DisplaySetting;
import org.apache.commons.cli2.Group;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.util.ReverseStringComparator;
import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GroupImplTest {

    private WriteableCommandLine commandLine;
    private Group group;

    @Before
    public void setup() {
        commandLine = mock(WriteableCommandLine.class);

        List options = new ArrayList<>();
        Argument arg1 = new Argument(null, "arg1", false, null);
        Argument arg2 = new Argument(null, "arg2", false, null);
        Option option1 = new Option(null, "option1", false, null);
        Option option2 = new Option(null, "option2", false, null);
        Option option3 = new Option(null, "option3", false, null);
        options.add(arg1);
        options.add(arg2);
        options.add(option1);
        options.add(option2);
        options.add(option3);

        group = new GroupImpl(options, "group", "group description", 0, Integer.MAX_VALUE, true);
    }

    @Test
    public void testCanProcess() {
        when(commandLine.hasOption("option1")).thenReturn(false);
        when(commandLine.hasOption("arg1")).thenReturn(false);
        when(commandLine.looksLikeOption("option1")).thenReturn(false);

        Assert.assertFalse(group.canProcess(commandLine, "option1"));
        Assert.assertFalse(group.canProcess(commandLine, "arg1"));

        when(commandLine.hasOption("option1")).thenReturn(true);
        Assert.assertTrue(group.canProcess(commandLine, "option1"));

        when(commandLine.looksLikeOption("arg1")).thenReturn(true);
        Assert.assertTrue(group.canProcess(commandLine, "arg1"));

        when(commandLine.hasOption("option3")).thenReturn(true);
        Assert.assertTrue(group.canProcess(commandLine, "option3"));
    }

    @Test
    public void testGetPrefixes() {
        Set prefixes = group.getPrefixes();
        Assert.assertEquals(0, prefixes.size());
    }

    @Test
    public void testGetTriggers() {
        Set triggers = new HashSet<>();
        triggers.add("option1");
        triggers.add("option2");
        triggers.add("option3");

        Set actualTriggers = group.getTriggers();
        Assert.assertEquals(triggers, actualTriggers);
    }

    @Test
    public void testProcess() {
        // Given
        List arguments = new ArrayList<>();
        arguments.add("arg1");
        arguments.add("arg2");
        arguments.add("option1");
        arguments.add("option3");

        // When
        group.process(commandLine, arguments.listIterator());

        // Then
        String[] expectedArgs = {"arg1", "arg2", "option1", "option3"};
        String[] actualArgs = commandLine.getArgs();
        Assert.assertArrayEquals(expectedArgs, actualArgs);
    }

    @Test(expected = OptionException.class)
    public void testValidate() throws OptionException {
        when(commandLine.hasOption("option1")).thenReturn(false);
        when(commandLine.hasOption("arg1")).thenReturn(false);

        group.validate(commandLine);

        Assert.fail("Validation should have failed");
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        Set helpSettings = new HashSet();

        // When
        group.appendUsage(buffer, helpSettings, null);

        // Then
        String expectedUsage = "group description\n" +
                "[option1]\n" +
                "[option2]\n" +
                "[option3]\n" +
                "arg1:\n" +
                "arg2:";

        String actualUsage = buffer.toString();
        Assert.assertEquals(expectedUsage, actualUsage);
    }

    @Test
    public void testHelpLines() {
        int depth = 1;
        Set helpSettings = new HashSet();

        // When
        List helpLines = group.helpLines(depth, helpSettings, null);

        // Then
        List expectedHelpLines = new ArrayList();
        expectedHelpLines.add(new HelpLineImpl("group", "group description", null, 1, null, null, null, null, null, null));
        expectedHelpLines.add(new HelpLineImpl("option1", "option1 description", null, 2, null, null, null, null, null, null));
        expectedHelpLines.add(new HelpLineImpl("option2", "option2 description", null, 2, null, null, null, null, null, null));
        expectedHelpLines.add(new HelpLineImpl("option3", "option3 description", null, 2, null, null, null, null, null, null));
        expectedHelpLines.add(new HelpLineImpl("arg1", "", null, 2, null, null, null, null, null, null));
        expectedHelpLines.add(new HelpLineImpl("arg2", "", null, 2, null, null, null, null, null, null));

        Assert.assertEquals(expectedHelpLines, helpLines);
    }

    @Test
    public void testGetOptions() {
        List options = group.getOptions();
        Assert.assertEquals(3, options.size());
        Assert.assertTrue(options.contains(option1));
        Assert.assertTrue(options.contains(option2));
        Assert.assertTrue(options.contains(option3));
    }

    @Test
    public void testGetAnonymous() {
        List anonymous = group.getAnonymous();
        Assert.assertEquals(2, anonymous.size());
        Assert.assertTrue(anonymous.contains(arg1));
        Assert.assertTrue(anonymous.contains(arg2));
    }

    @Test
    public void testFindOption() {
        Option option1 = group.findOption("option1");
        Assert.assertNotNull(option1);
        Assert.assertEquals("option1", option1.getPreferredName());

        Option option2 = group.findOption("option2");
        Assert.assertNotNull(option2);
        Assert.assertEquals("option2", option2.getPreferredName());

        Option option3 = group.findOption("option3");
        Assert.assertNotNull(option3);
        Assert.assertEquals("option3", option3.getPreferredName());

        Option option4 = group.findOption("nonexistent");
        Assert.assertNull(option4);
    }

    @Test
    public void testGetMinimum() {
        int minimum = group.getMinimum();
        Assert.assertEquals(0, minimum);
    }

    @Test
    public void testGetMaximum() {
        int maximum = group.getMaximum();
        Assert.assertEquals(Integer.MAX_VALUE, maximum);
    }

    @Test
    public void testIsRequired() {
        Assert.assertTrue(group.isRequired());
    }

    @Test
    public void testDefaults() {
        group.defaults(commandLine);

        when(commandLine.setOption(option1, false)).thenReturn(commandLine);
        when(commandLine.setOption(option2, false)).thenReturn(commandLine);
        when(commandLine.setOption(option3, false)).thenReturn(commandLine);
        when(commandLine.setOption(arg1, false)).thenReturn(commandLine);
        when(commandLine.setOption(arg2, false)).thenReturn(commandLine);

        group.defaults(commandLine);

        Option option1Default = commandLine.getOption("option1");
        Assert.assertNull(option1Default);

        Option option2Default = commandLine.getOption("option2");
        Assert.assertNull(option2Default);

        Option option3Default = commandLine.getOption("option3");
        Assert.assertNull(option3Default);

        Argument arg1Default = commandLine.getArgument("arg1");
        Assert.assertNull(arg1Default);

        Argument arg2Default = commandLine.getArgument("arg2");
        Assert.assertNull(arg2Default);
    }

    @Test
    public void testReverseComparator() {
        Comparator comparator = ReverseStringComparator.getInstance();

        String s1 = "apple";
        String s2 = "banana";

        int result = comparator.compare(s1, s2);
        Assert.assertEquals(-1, result);

        result = comparator.compare(s2, s1);
        Assert.assertEquals(1, result);
    }
}
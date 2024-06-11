package org.apache.commons.cli2.option;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.DisplaySetting;
import org.apache.commons.cli2.Group;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.test.TestUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class GroupImplTest {

    private GroupImpl group;

    @Before
    public void setUp() {
        final List options = new ArrayList();
        options.add(new Option("o", "option1", false, "Option 1"));
        options.add(new Option("o2", "option2", true, "Option 2"));
        options.add(new Option("o3", "option3", false, "Option 3"));
        options.add(new Argument("arg", "Anonymous argument"));

        group = new GroupImpl(options, "Group", "A group", 1, 3);
    }

    @Test
    public void testCanProcess() {
        final WriteableCommandLine commandLine = TestUtils.createCommandLine();
        final String[] args = {"-o", "o1", "--option2", "--arg", "-o3", "arg"};
        TestUtils.parseArguments(args, commandLine);

        Assert.assertTrue(group.canProcess(commandLine, "o1"));
        Assert.assertTrue(group.canProcess(commandLine, "o2"));
        Assert.assertTrue(group.canProcess(commandLine, "o3"));
        Assert.assertTrue(group.canProcess(commandLine, "arg"));
        Assert.assertFalse(group.canProcess(commandLine, "unknown"));
    }

    @Test
    public void testGetPrefixes() {
        final Set prefixes = group.getPrefixes();
        assertTrue(prefixes.isEmpty());
    }

    @Test
    public void testGetTriggers() {
        final Set triggers = group.getTriggers();
        assertEquals(3, triggers.size());
        assertTrue(triggers.contains("o1"));
        assertTrue(triggers.contains("o2"));
        assertTrue(triggers.contains("o3"));
    }

    @Test
    public void testProcess() {
        final WriteableCommandLine commandLine = TestUtils.createCommandLine();
        final String[] args = {"--group", "-o1", "--option2", "value2", "-o3", "value3", "-o1"};
        TestUtils.parseArguments(args, commandLine);

        group.process(commandLine, commandLine.iterator());

        assertEquals(2, commandLine.getOptionValues("o1").length);
        assertEquals("value2", commandLine.getOptionValues("o2")[0]);
        assertNull(commandLine.getOptionValues("o3")[0]);

        final Option option1 = group.findOption("o1");
        assertNotNull(option1);
        final Option option2 = group.findOption("o2");
        assertNotNull(option2);

        option1.process(commandLine, commandLine.iterator());
        option2.process(commandLine, commandLine.iterator());

        assertEquals(3, commandLine.getOptionValues("o1").length);
        assertEquals("value2", commandLine.getOptionValues("o2")[0]);
        assertEquals("value3", commandLine.getOptionValues("o2")[1]);
    }

    @Test
    public void testValidate() {
        final WriteableCommandLine commandLine = TestUtils.createCommandLine();

        try {
            group.validate(commandLine);
        } catch (OptionException e) {
            Assert.assertEquals("Group: Either 1 or more than 3 options are required", e.getMessage());
            return;
        }

        Assert.fail();
    }

    @Test(expected = OptionException.class)
    public void testValidateWithMinimum() {
        group.minimum = 2;
        final WriteableCommandLine commandLine = TestUtils.createCommandLine();

        group.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithMaximum() {
        group.maximum = 1;
        final WriteableCommandLine commandLine = TestUtils.createCommandLine();

        group.validate(commandLine);
    }

    @Test
    public void testAppendUsage() {
        final StringBuilder buffer = new StringBuilder();

        group.appendUsage(buffer, Collections.emptySet(), null);

        assertEquals("-g, --group\n" +
                "group ()\n" +
                "  A group\n" +
                "\n" +
                "OPTIONS\n" +
                "-----\n" +
                "--option1, -o1\n" +
                "option1\n" +
                "--option2, -o2\n" +
                "Option 2\n" +
                "--option3, -o3\n" +
                "Option 3\n" +
                "\n" +
                "ARGUMENTS\n" +
                "---------", buffer.toString());
    }

    @Test
    public void testHelpLines() {
        final List helpLines = group.helpLines(0, Collections.emptySet(), null);

        assertEquals(3, helpLines.size());

        final HelpLine helpLine1 = (HelpLine) helpLines.get(0);
        assertEquals("-g, --group\n" +
                "group ()\n" +
                "  A group\n" +
                "\n" +
                "OPTIONS\n" +
                "-----\n" +
                "--option1, -o1\n" +
                "option1\n" +
                "\n" +
                "ARGUMENTS\n" +
                "---------", helpLine1.getHelp());

        final HelpLine helpLine2 = (HelpLine) helpLines.get(1);
        assertEquals("--option1, -o1\n" +
                "option1\n", helpLine2.getHelp());

        final HelpLine helpLine3 = (HelpLine) helpLines.get(2);
        assertEquals("--option2, -o2\n" +
                "Option 2\n", helpLine3.getHelp());
    }

    @Test
    public void testFindOption() {
        assertNull(group.findOption("unknown"));
        assertSame(group.findOption("o1"), group.findOption("o1"));
        assertSame(group.findOption("o2"), group.findOption("--option2"));
    }

    @Test
    public void testGetAnonymous() {
        final List anonymous = group.getAnonymous();
        assertEquals(1, anonymous.size());
        assertEquals("arg", anonymous.get(0).getLongOpt());
    }

    @Test
    public void testGetOptions() {
        final List options = group.getOptions();
        assertEquals(3, options.size());
        final Option option1 = (Option) options.get(0);
        assertEquals("o1", option1.getLongOpt());
        final Option option2 = (Option) options.get(1);
        assertEquals("o2", option2.getLongOpt());
        final Option option3 = (Option) options.get(2);
        assertEquals("o3", option3.getLongOpt());
    }

    @Test
    public void testIsRequired() {
        assertFalse(group.isRequired());
    }

    @Test
    public void testDefaults() {
        final WriteableCommandLine commandLine = TestUtils.createCommandLine();

        group.defaults(commandLine);

        assertFalse(commandLine.hasOption("o1"));
        assertFalse(commandLine.hasOption("o2"));
        assertFalse(commandLine.hasOption("o3"));
        assertNull(commandLine.getOptionValues("o1")[0]);
        assertNull(commandLine.getOptionValues("o2")[0]);
        assertNull(commandLine.getOptionValues("o3")[0]);
    }
}
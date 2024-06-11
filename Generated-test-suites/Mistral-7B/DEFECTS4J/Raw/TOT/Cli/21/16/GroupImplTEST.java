package org.apache.commons.cli2.option;

import static org.junit.Assert.*;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.DisplaySetting;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.test.TestUtils;

/**
 * Tests for {@link GroupImpl}
 */
public class GroupImplTest {

    /**
     * Test method for {@link GroupImpl#GroupImpl(List, String, String, int, int, boolean)}.
     */
    @org.junit.Test
    public void testGroupImpl() {
        final List options = new ArrayList();
        options.add(TestUtils.createTestOption("option1", "desc1", false, null, null, null, null, null, null));
        options.add(TestUtils.createTestOption("option2", "desc2", true, "arg", null, null, null, null, null));
        options.add(TestUtils.createTestArgument("arg1", "desc1", false, null, null, null, null, null));

        final Group group = new GroupImpl(options, "group", "desc", 1, 2, false);

        assertEquals("group", group.getName());
        assertEquals("desc", group.getDescription());
        assertEquals(1, group.getMinimum());
        assertEquals(2, group.getMaximum());
        assertFalse(group.isRequired());
        assertEquals(options, group.getOptions());
        assertEquals(Collections.singletonList(TestUtils.createTestArgument("arg1")), group.getAnonymous());
        assertFalse(group.canProcess(TestUtils.createTestCommandLine(), null));
    }

    /**
     * Test method for {@link GroupImpl#canProcess(WriteableCommandLine, String)}.
     */
    @org.junit.Test
    public void testCanProcess() {
        final List options = new ArrayList();
        options.add(TestUtils.createTestOption("option1", "desc1", false, null, null, null, null, null, null));
        options.add(TestUtils.createTestOption("option2", "desc2", true, "arg", null, null, null, null, null));
        options.add(TestUtils.createTestArgument("arg1", "desc1", false, null, null, null, null, null));

        final Group group = new GroupImpl(options, "group", "desc", 1, 2, false);

        final CommandLine commandLine = TestUtils.createTestCommandLine();

        assertFalse(group.canProcess(commandLine, null));
        assertFalse(group.canProcess(commandLine, "option1"));
        assertFalse(group.canProcess(commandLine, "option2"));
        assertTrue(group.canProcess(commandLine, "arg1"));
        assertFalse(group.canProcess(commandLine, "unknown"));

        final CommandLine commandLineWithGroup = TestUtils.createTestCommandLine("group");

        assertFalse(group.canProcess(commandLineWithGroup, null));
        assertTrue(group.canProcess(commandLineWithGroup, "option1"));
        assertTrue(group.canProcess(commandLineWithGroup, "option2"));
        assertTrue(group.canProcess(commandLineWithGroup, "arg1"));

        final CommandLine commandLineWithGroupAndArg = TestUtils.createTestCommandLine("group arg1");

        assertFalse(group.canProcess(commandLineWithGroupAndArg, null));
        assertTrue(group.canProcess(commandLineWithGroupAndArg, "option1"));
        assertTrue(group.canProcess(commandLineWithGroupAndArg, "option2"));
        assertTrue(group.canProcess(commandLineWithGroupAndArg, "arg1"));

        final CommandLine commandLineWithGroupArgAndArg = TestUtils.createTestCommandLine("group arg1 arg2");

        assertFalse(group.canProcess(commandLineWithGroupArgAndArg, null));
        assertTrue(group.canProcess(commandLineWithGroupArgAndArg, "option1"));
        assertTrue(group.canProcess(commandLineWithGroupArgAndArg, "option2"));
        assertTrue(group.canProcess(commandLineWithGroupArgAndArg, "arg1"));
        assertTrue(group.canProcess(commandLineWithGroupArgAndArg, "arg2"));
    }

    /**
     * Test method for {@link GroupImpl#process(WriteableCommandLine, ListIterator)}.
     */
    @org.junit.Test
    public void testProcess() throws OptionException {
        final List options = new ArrayList();

        options.add(TestUtils.createTestOption("option1", "desc1", false, null, null, null, null, null, null));
        options.add(TestUtils.createTestOption("option2", "desc2", true, "arg", null, null, null, null, null));
        options.add(TestUtils.createTestArgument("arg1", "desc1", false, null, null, null, null, null));

        final Group group = new GroupImpl(options, "group", "desc", 1, 2, false);

        final CommandLine commandLine = TestUtils.createTestCommandLine();

        final ListIterator arguments = TestUtils.createTestListIterator();

        group.process(commandLine, arguments);

        assertEquals(0, commandLine.getNumArgs());
        assertEquals(0, commandLine.getNumOptions());
        assertFalse(commandLine.hasOption("option1"));
        assertFalse(commandLine.hasOption("option2"));
        assertFalse(commandLine.hasOption("arg"));
        assertFalse(commandLine.hasOption("arg1"));

        group.process(commandLine, arguments);

        assertEquals(0, commandLine.getNumArgs());
        assertEquals(2, commandLine.getNumOptions());
        assertTrue(commandLine.hasOption("option1"));
        assertFalse(commandLine.hasOption("option2"));
        assertFalse(commandLine.hasOption("arg"));
        assertFalse(commandLine.hasOption("arg1"));

        arguments.add("option1");
        arguments.add("option2");

        group.process(commandLine, arguments);

        assertEquals(0, commandLine.getNumArgs());
        assertEquals(3, commandLine.getNumOptions());
        assertTrue(commandLine.hasOption("option1"));
        assertTrue(commandLine.hasOption("option2"));
        assertFalse(commandLine.hasOption("arg"));
        assertFalse(commandLine.hasOption("arg1"));

        arguments.add("arg1");

        group.process(commandLine, arguments);

        assertEquals(1, commandLine.getNumArgs());
        assertEquals(3, commandLine.getNumOptions());
        assertTrue(commandLine.hasOption("option1"));
        assertTrue(commandLine.hasOption("option2"));
        assertFalse(commandLine.hasOption("arg"));
        assertTrue(commandLine.hasOption("arg1"));

        arguments.add(null);

        try {
            group.process(commandLine, arguments);
            fail();
        } catch (IllegalArgumentException ex) {
            // expected
        }

        group.process(commandLine, arguments);

        assertEquals(1, commandLine.getNumArgs());
        assertEquals(3, commandLine.getNumOptions());
        assertTrue(commandLine.hasOption("option1"));
        assertTrue(commandLine.hasOption("option2"));
        assertFalse(commandLine.hasOption("arg"));
        assertTrue(commandLine.hasOption("arg1"));
    }

    /**
     * Test method for {@link GroupImpl#validate(WriteableCommandLine)}.
     */
    @org.junit.Test
    public void testValidate() throws OptionException {
        final List options = new ArrayList();

        options.add(TestUtils.createTestOption("option1", "desc1", false, null, null, null, null, null, null));
        options.add(TestUtils.createTestOption("option2", "desc2", true, "arg", null, null, null, null, null));
        options.add(TestUtils.createTestArgument("arg1", "desc1", false, null, null, null, null, null));

        final Group group = new GroupImpl(options, "group", "desc", 1, 2, false);

        final CommandLine commandLine = TestUtils.createTestCommandLine();

        try {
            group.validate(commandLine);
            fail();
        } catch (OptionException ex) {
            // expected
        }

        commandLine.parse(group);

        group.validate(commandLine);

        final CommandLine commandLineWithGroup = TestUtils.createTestCommandLine("group");

        try {
            group.validate(commandLineWithGroup);
            fail();
        } catch (OptionException ex) {
            // expected
        }

        final CommandLine commandLineWithGroupAndArg = TestUtils.createTestCommandLine("group arg1");

        try {
            group.validate(commandLineWithGroupAndArg);
            fail();
        } catch (OptionException ex) {
            // expected
        }

        final CommandLine commandLineWithGroupArgAndArg = TestUtils.createTestCommandLine("group arg1 arg2");

        try {
            group.validate(commandLineWithGroupArgAndArg);
            fail();
        } catch (OptionException ex) {
            // expected
        }

        final CommandLine commandLineWithGroupArg = TestUtils.createTestCommandLine("group arg1");

        try {
            group.validate(commandLineWithGroupArg);
            fail();
        } catch (OptionException ex) {
            // expected
        }

        final CommandLine commandLineWithGroupOption = TestUtils.createTestCommandLine("group option1");

        try {
            group.validate(commandLineWithGroupOption);
            fail();
        } catch (OptionException ex) {
            // expected
        }

        final CommandLine commandLineWithGroupTwoOptions = TestUtils.createTestCommandLine("group option1 option1");

        try {
            group.validate(commandLineWithGroupTwoOptions);
            fail();
        } catch (OptionException ex) {
            // expected
        }

        final CommandLine commandLineWithGroupTwoOptionsMax = TestUtils.createTestCommandLine("group option1 option1 option1");

        try {
            group.validate(commandLineWithGroupTwoOptionsMax);
            fail();
        } catch (OptionException ex) {
            // expected
        }

        final CommandLine commandLineWithGroupTwoOptionsMin = TestUtils.createTestCommandLine("group option1 option1");

        group.validate(commandLineWithGroupTwoOptionsMin);
    }

    /**
     * Test method for {@link GroupImpl#getPrefixes()}.
     */
    @org.junit.Test
    public void testGetPrefixes() {
        final List options = new ArrayList();

        options.add(TestUtils.createTestOption("option1", "desc1", false, null, null, null, null, null, null));
        options.add(TestUtils.createTestOption("option2", "desc2", true, null, new String[]{"--"}, null, null, null, null));

        final Group group = new GroupImpl(options, "group", "desc", 1, 2, false);

        final Set prefixes = group.getPrefixes();

        assertTrue(prefixes.contains("--"));

        final List optionsWithPrefixes = new ArrayList();

        optionsWithPrefixes.add(TestUtils.createTestOption("option1", "desc1", false, null, null, null, null, null, null));
        optionsWithPrefixes.add(TestUtils.createTestOption("option2", "desc2", true, null, new String[]{"--prefix"}, null, null, null, null));

        final Group groupWithPrefixes = new GroupImpl(optionsWithPrefixes, "group", "desc", 1, 2, false);

        final Set prefixesWithPrefix = groupWithPrefixes.getPrefixes();

        assertTrue(prefixesWithPrefix.contains("--prefix"));
    }

    /**
     * Test method for {@link GroupImpl#getTriggers()}.
     */
    @org.junit.Test
    public void testGetTriggers() {
        final List options = new ArrayList();

        options.add(TestUtils.createTestOption("option1", "desc1", false, null, null, null, null, null, null));
        options.add(TestUtils.createTestOption("option2", "desc2", true, null, null, null, null, null, new String[]{"--prefix"}));

        final Group group = new GroupImpl(options, "group", "desc", 1, 2, false);

        final Set triggers = group.getTriggers();

        assertTrue(triggers.contains("option1"));
        assertTrue(triggers.contains("option2"));
        assertFalse(triggers.contains("--prefix"));

        final List optionsWithTriggers = new ArrayList();

        optionsWithTriggers.add(TestUtils.createTestOption("option1", "desc1", false, null, null, null, null, null, null));
        optionsWithTriggers.add(TestUtils.createTestOption("option2", "desc2", true, null, null, null, null, null, new String[]{"--prefix1", "--prefix2"}));

        final Group groupWithTriggers = new GroupImpl(optionsWithTriggers, "group", "desc", 1, 2, false);

        final Set triggersWithTriggers = groupWithTriggers.getTriggers();

        assertTrue(triggersWithTriggers.contains("option1"));
        assertTrue(triggersWithTriggers.contains("option2"));
        assertTrue(triggersWithTriggers.contains("--prefix1"));
        assertTrue(triggersWithTriggers.contains("--prefix2"));
    }
}
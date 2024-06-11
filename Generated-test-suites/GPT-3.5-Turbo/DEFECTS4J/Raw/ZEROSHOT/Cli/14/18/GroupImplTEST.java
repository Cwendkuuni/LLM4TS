package org.apache.commons.cli2.option;

import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GroupImplTest {
    private GroupImpl group;

    @Before
    public void setUp() {
        Option option1 = new Argument("arg1");
        Option option2 = new OptionImpl("opt1", "option 1");

        List<Option> options = Arrays.asList(option1, option2);
        group = new GroupImpl(options, "group1", "group 1", 1, 1);
    }

    @Test
    public void testCanProcess() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();

        // test with valid argument
        assertTrue(group.canProcess(commandLine, "arg1"));
        assertTrue(group.canProcess(commandLine, "opt1"));

        // test with invalid argument
        assertFalse(group.canProcess(commandLine, "invalid"));

        // test with null argument
        assertFalse(group.canProcess(commandLine, null));
    }

    @Test
    public void testGetPrefixes() {
        List<String> prefixes = new ArrayList<String>();
        prefixes.add("--");
        prefixes.add("-");
        assertEquals(prefixes, group.getPrefixes());
    }

    @Test
    public void testGetTriggers() {
        List<String> triggers = new ArrayList<String>();
        triggers.add("arg1");
        triggers.add("opt1");
        assertEquals(triggers, group.getTriggers());
    }

    @Test
    public void testProcess() throws OptionException {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();

        // process argument
        group.process(commandLine, Collections.singletonList("arg1").listIterator());
        assertTrue(commandLine.hasOption("arg1"));

        // process option
        group.process(commandLine, Collections.singletonList("--opt1").listIterator());
        assertTrue(commandLine.hasOption("opt1"));

        // process invalid argument
        group.process(commandLine, Collections.singletonList("invalid").listIterator());
        assertFalse(commandLine.hasOption("invalid"));

        // process null argument
        group.process(commandLine, Collections.singletonList(null).listIterator());
        assertFalse(commandLine.hasOption(null));
    }

    @Test
    public void testValidate() throws OptionException {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        commandLine.addOption(new Argument("arg1"));
        commandLine.addOption(new OptionImpl("opt1", "option 1"));

        // valid command line
        group.validate(commandLine);

        // missing option
        commandLine.removeOption("opt1");
        try {
            group.validate(commandLine);
            fail("OptionException expected");
        } catch (OptionException e) {
            assertEquals("Missing option", e.getMessage());
            assertTrue(e.getOption() instanceof GroupImpl);
        }

        // unexpected option
        commandLine.addOption(new OptionImpl("opt2", "option 2"));
        try {
            group.validate(commandLine);
            fail("OptionException expected");
        } catch (OptionException e) {
            assertEquals("Unexpected option", e.getMessage());
            assertTrue(e.getOption() instanceof GroupImpl);
        }
    }

    @Test
    public void testGetPreferredName() {
        assertEquals("group1", group.getPreferredName());
    }

    @Test
    public void testGetDescription() {
        assertEquals("group 1", group.getDescription());
    }

    @Test
    public void testGetOptions() {
        List<Option> expectedOptions = Arrays.asList(new Argument("arg1"), new OptionImpl("opt1", "option 1"));
        assertEquals(expectedOptions, group.getOptions());
    }

    @Test
    public void testGetAnonymous() {
        List<Option> expectedAnonymous = Arrays.asList(new Argument("arg1"));
        assertEquals(expectedAnonymous, group.getAnonymous());
    }

    @Test
    public void testFindOption() {
        Option foundOption = group.findOption("opt1");
        assertTrue(foundOption instanceof OptionImpl);
        assertEquals("opt1", foundOption.getPreferredName());

        foundOption = group.findOption("invalid");
        assertNull(foundOption);
    }

    @Test
    public void testGetMinimum() {
        assertEquals(1, group.getMinimum());
    }

    @Test
    public void testGetMaximum() {
        assertEquals(1, group.getMaximum());
    }

    @Test
    public void testIsRequired() {
        assertTrue(group.isRequired());
    }

    @Test
    public void testDefaults() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();

        group.defaults(commandLine);

        assertTrue(commandLine.hasOption("arg1"));
        assertTrue(commandLine.hasOption("opt1"));
    }
}
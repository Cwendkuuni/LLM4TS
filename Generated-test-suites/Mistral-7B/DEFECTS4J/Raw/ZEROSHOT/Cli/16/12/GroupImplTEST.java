package org.apache.commons.cli2.option;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class GroupImplTest {
    private List<Option> options;
    private GroupImpl group;

    @Before
    public void setup() {
        options = new ArrayList<>();
        options.add(new Option("a", "optionA", true, "descriptionA"));
        options.add(new Option("b", "optionB", false, "descriptionB"));
        options.add(new Option("c", "optionC", false, "descriptionC"));
        options.add(new Option("d", "optionD", true, "descriptionD"));
        options.add(new Option("e", "optionE", false, "descriptionE"));
        options.add(new Option("f", "optionF", true, "descriptionF"));

        group = new GroupImpl(options, "group", "description", 1, 3);
    }

    @Test
    public void testCanProcess() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        String[] args = {"a", "b", "c"};

        for (String arg : args) {
            assertTrue(group.canProcess(commandLine, arg));
        }

        String invalidArg = "invalidArg";
        assertFalse(group.canProcess(commandLine, invalidArg));

        args[0] = invalidArg;
        assertFalse(group.canProcess(commandLine, "a"));
    }

    @Test
    public void testGetName() {
        assertEquals("group", group.getName());
    }

    @Test
    public void testGetDescription() {
        assertEquals("description", group.getDescription());
    }

    @Test
    public void testGetMinimum() {
        assertEquals(1, group.getMinimum());
    }

    @Test
    public void testGetMaximum() {
        assertEquals(3, group.getMaximum());
    }

    @Test
    public void testIsRequired() {
        assertFalse(group.isRequired());
    }

    @Test
    public void testGetOptions() {
        List<Option> expectedOptions = new ArrayList<>(options);

        List<Option> actualOptions = group.getOptions();

        assertEquals(expectedOptions.size(), actualOptions.size());

        for (int i = 0; i < expectedOptions.size(); i++) {
            Option expectedOption = expectedOptions.get(i);
            Option actualOption = actualOptions.get(i);

            assertEquals(expectedOption.getClass(), actualOption.getClass());
            assertEquals(expectedOption.getLongOpt(), actualOption.getLongOpt());
            assertEquals(expectedOption.getName(), actualOption.getName());
            assertEquals(expectedOption.isRequired(), actualOption.isRequired());
            assertEquals(expectedOption.getDescription(), actualOption.getDescription());
        }
    }

    @Test
    public void testGetAnonymous() {
        List<Option> expectedAnonymous = new ArrayList<>();

        expectedAnonymous.add(new Option("anonymousA", true, "descriptionAnonymousA"));
        expectedAnonymous.add(new Option("anonymousB", false, "descriptionAnonymousB"));

        List<Option> actualAnonymous = group.getAnonymous();

        assertEquals(expectedAnonymous.size(), actualAnonymous.size());

        for (int i = 0; i < expectedAnonymous.size(); i++) {
            Option expectedAnonymousOption = expectedAnonymous.get(i);
            Option actualAnonymousOption = actualAnonymous.get(i);

            assertEquals(expectedAnonymousOption.getClass(), actualAnonymousOption.getClass());
            assertEquals(expectedAnonymousOption.getLongOpt(), actualAnonymousOption.getLongOpt());
            assertEquals(expectedAnonymousOption.getName(), actualAnonymousOption.getName());
            assertEquals(expectedAnonymousOption.isRequired(), actualAnonymousOption.isRequired());
            assertEquals(expectedAnonymousOption.getDescription(), actualAnonymousOption.getDescription());
        }
    }

    @Test
    public void testFindOption() {
        Option expectedOptionA = new Option("a", "optionA", true, "descriptionA");

        Option actualOptionA = group.findOption("a");

        assertEquals(expectedOptionA, actualOptionA);

        assertNull(group.findOption("invalid"));
    }

    @Test
    public void testValidate() throws OptionException {
        WriteableCommandLine commandLine = new WriteableCommandLine();

        commandLine.addOption("a");
        commandLine.addOption("b");
        commandLine.addOption("c");

        group.validate(commandLine);

        // no exception thrown, test passed
    }

    @Test(expected = OptionException.class)
    public void testValidateWithLessOptionsThanMinimum() throws OptionException {
        WriteableCommandLine commandLine = new WriteableCommandLine();

        commandLine.addOption("a");

        group.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithMoreOptionsThanMaximum() throws OptionException {
        WriteableCommandLine commandLine = new WriteableCommandLine();

        commandLine.addOption("a");
        commandLine.addOption("b");
        commandLine.addOption("c");
        commandLine.addOption("d");

        group.validate(commandLine);
    }
}

class HelpLineImpl {
    // empty implementation for JUnit test (not part of the original code)
}

class ReverseStringComparator {
    // empty implementation for JUnit test (not part of the original code)
}
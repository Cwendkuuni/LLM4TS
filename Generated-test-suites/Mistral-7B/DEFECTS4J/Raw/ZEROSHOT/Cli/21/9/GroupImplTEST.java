package org.apache.commons.cli2.option;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

public class GroupImplTest {

    private WriteableCommandLine commandLine;
    private Group group;

    @Before
    public void setUp() {
        commandLine = new DefaultCommandLine();
        List<Option> options = new ArrayList<>();
        options.add(new Option("a", "arg1", true, "description1"));
        options.add(new Option("b", "arg2", false, "description2"));
        options.add(new Option("c", "arg3", false, "description3"));
        group = new GroupImpl(options, "groupName", "groupDescription", 0, 2, false);
    }

    @Test
    public void testCanProcess() {
        String[] args = {"arg1", "arg2", "arg3"};
        ListIterator<String> arguments = commandLine.parseArgumentList(args);

        for (String arg : args) {
            assertTrue(group.canProcess(commandLine, arg));
        }

        String notAnOption = "notAnOption";
        assertFalse(group.canProcess(commandLine, notAnOption));
    }

    @Test
    public void testGetPrefixes() {
        Set prefixes = group.getPrefixes();
        assertTrue(prefixes.isEmpty());
    }

    @Test
    public void testGetTriggers() {
        Set triggers = group.getTriggers();
        assertEquals(3, triggers.size());
        assertTrue(triggers.containsAll(Collections.singletonList("a")));
        assertTrue(triggers.containsAll(Collections.singletonList("b")));
        assertTrue(triggers.containsAll(Collections.singletonList("c")));
    }

    @Test
    public void testProcess() throws OptionException {
        String[] args = {"arg1", "arg2", "arg3"};
        ListIterator<String> arguments = commandLine.parseArgumentList(args);

        group.process(commandLine, arguments);

        Option optionA = commandLine.getOption("a");
        assertEquals("arg1", optionA.getValue());
        assertEquals(true, optionA.isSet());

        Option optionB = commandLine.getOption("b");
        assertEquals(null, optionB.getValue());
        assertFalse(optionB.isSet());

        Option optionC = commandLine.getOption("c");
        assertEquals(null, optionC.getValue());
        assertFalse(optionC.isSet());

        Option optionD = commandLine.getOption("d");
        assertNull(optionD);
    }

    @Test
    public void testValidate() throws OptionException {
        String[] args = {"arg1", "arg2"};
        ListIterator<String> arguments = commandLine.parseArgumentList(args);

        group.process(commandLine, arguments);

        group.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithTooFewOptions() throws OptionException {
        String[] args = {"arg1"};
        ListIterator<String> arguments = commandLine.parseArgumentList(args);

        group.process(commandLine, arguments);

        group.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithTooManyOptions() throws OptionException {
        String[] args = {"arg1", "arg2", "arg3", "arg4"};
        ListIterator<String> arguments = commandLine.parseArgumentList(args);

        group.process(commandLine, arguments);

        group.validate(commandLine);
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        Set helpSettings = new HashSet();

        group.appendUsage(buffer, helpSettings, null);

        String expectedUsage = "groupName:\n" +
                "groupDescription\n" +
                "\noptions:\n" +
                "  -a ARG1 (=true)\n" +
                "      description1\n" +
                "  -b\n" +
                "      description2\n" +
                "  -c\n" +
                "      description3\n";

        assertEquals(expectedUsage, buffer.toString());
    }

    @Test
    public void testHelpLines() {
        List<HelpLine> helpLines = group.helpLines(0, Collections.emptySet(), null);

        String expectedUsage = "groupName:\n" +
                "groupDescription\n" +
                "\noptions:\n" +
                "  -a ARG1 (=true)\n" +
                "      description1\n" +
                "  -b\n" +
                "      description2\n" +
                "  -c\n" +
                "      description3\n";

        HelpLine helpLine = helpLines.get(0);
        assertEquals(expectedUsage, helpLine.getSummary());
        assertEquals(3, helpLines.size());
    }

    @Test
    public void testGetOptions() {
        List options = group.getOptions();
        assertEquals(3, options.size());
    }

    @Test
    public void testGetAnonymous() {
        List anonymous = group.getAnonymous();
        assertEquals(0, anonymous.size());
    }

    @Test
    public void testFindOption() {
        Option optionB = group.findOption("b");
        assertEquals(optionB, commandLine.getOption("b"));
    }

    @Test
    public void testGetMinimum() {
        assertEquals(0, group.getMinimum());
    }

    @Test
    public void testGetMaximum() {
        assertEquals(2, group.getMaximum());
    }
}
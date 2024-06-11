package org.apache.commons.cli2.option;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.DisplaySetting;
import org.apache.commons.cli2.Group;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.powermock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

@RunWith(PowerMockRunner.class)
@PrepareForTest({GroupImpl.class})
public class GroupImplTest {

    private GroupImpl group;
    private List<Option> options;
    private WriteableCommandLine commandLine;

    @Before
    public void setup() {
        options = new ArrayList<>();
        commandLine = PowerMock.mock(WriteableCommandLine.class);

        Option option1 = new Option("a", "a", true, "a description");
        Option option2 = new Option("b", "b", false, "b description");
        Option option3 = new Argument(null, "arg", true, "arg description");

        options.add(option1);
        options.add(option2);
        options.add(option3);

        group = new GroupImpl(options, null, null, 0, 1);
    }

    @Test
    public void testCanProcess() {
        assertEquals(true, group.canProcess(commandLine, "a"));
        assertEquals(true, group.canProcess(commandLine, "b"));
        assertEquals(true, group.canProcess(commandLine, "arg"));
        assertEquals(false, group.canProcess(commandLine, null));
        assertEquals(false, group.canProcess(commandLine, "c"));
    }

    @Test
    public void testGetPrefixes() {
        Set<String> prefixes = group.getPrefixes();
        assertFalse(prefixes.isEmpty());
        assertSame("--", prefixes.iterator().next());
    }

    @Test
    public void testGetTriggers() {
        Set<String> triggers = group.getTriggers();
        assertEquals(3, triggers.size());
        assertTrue(triggers.contains("a"));
        assertTrue(triggers.contains("b"));
        assertTrue(triggers.contains("arg"));
    }

    @Test
    public void testProcess() {
        // test with valid options
        group.process(commandLine, options.listIterator());
        assertEquals(3, commandLine.getNumNamedOptions());
        assertEquals("a description", commandLine.getOptionValue("a"));
        assertEquals("b description", commandLine.getOptionValue("b"));
        assertEquals("arg", commandLine.getOptionValue("arg"));

        // test with invalid option
        commandLine.clear();
        group.process(commandLine, options.listIterator());
        commandLine.nextOption();
        commandLine.nextOption();
        commandLine.nextOption();
        commandLine.nextOption();
        assertNull(commandLine.getOptionValue("c"));

        // test with anonymous argument
        commandLine.clear();
        group.process(commandLine, options.listIterator());
        commandLine.nextOption();
        commandLine.nextOption();
        commandLine.nextArgument();
        assertNotNull(commandLine.getArgument(0));
    }

    @Test
    public void testValidate() throws OptionException {
        // test with valid options
        commandLine.clear();
        group.validate(commandLine);

        // test with missing options
        commandLine.clear();
        group.validate(commandLine);
        assertEquals(1, commandLine.getNumMissing());

        // test with too many options
        commandLine.clear();
        group.validate(commandLine);
        group.process(commandLine, options.listIterator());
        group.process(commandLine, options.listIterator());
        group.process(commandLine, options.listIterator());
        group.process(commandLine, options.listIterator());
        assertSame(OptionException.class, commandLine.getUnknownOption().getClass());
    }

    @Test
    public void testGetPreferredName() {
        assertEquals(null, group.getPreferredName());
    }

    @Test
    public void testGetDescription() {
        assertEquals(null, group.getDescription());
    }

    @Test
    public void testAppendUsage() {
        final StringBuffer buffer = new StringBuffer();
        group.appendUsage(buffer, null, null);
        assertEquals("(a description)\n" +
                "  --a VALUE\n" +
                "(b description)\n" +
                "  --b [VALUE]\n" +
                "(arg description)\n" +
                "  ARG\n", buffer.toString());
    }

    @Test
    public void testHelpLines() {
        final List<HelpLine> helpLines = group.helpLines(0, null, null);
        assertEquals(4, helpLines.size());

        HelpLine helpLine = helpLines.get(0);
        assertEquals(group.getClass().getName() + ":", helpLine.getHeader());
        assertEquals("[OPTIONS]\n", helpLine.getShortDescription());

        helpLine = helpLines.get(1);
        assertEquals("  --a VALUE\n" +
                "    a description\n", helpLine.getLongDescription());

        helpLine = helpLines.get(2);
        assertEquals("  --b [VALUE]\n" +
                "    b description\n", helpLine.getLongDescription());

        helpLine = helpLines.get(3);
        assertEquals("  ARG\n" +
                "    arg description\n", helpLine.getLongDescription());
    }

    @Test
    public void testGetOptions() {
        assertEquals(options, group.getOptions());
    }

    @Test
    public void testGetAnonymous() {
        assertEquals(options.get(2), group.getAnonymous().get(0));
    }

    @Test
    public void testFindOption() {
        Option option = group.findOption("a");
        assertSame(option1, option);

        option = group.findOption("b");
        assertSame(option2, option);

        option = group.findOption("arg");
        assertSame(option3, option);

        option = group.findOption("c");
        assertNull(option);
    }

    @Test
    public void testGetMinimum() {
        assertEquals(0, group.getMinimum());
    }

    @Test
    public void testGetMaximum() {
        assertEquals(1, group.getMaximum());
    }

    @Test
    public void testIsRequired() {
        assertFalse(group.isRequired());
    }

    @Test
    public void testDefaults() {
        group.defaults(commandLine);

        assertFalse(commandLine.hasOption("a"));
        assertFalse(commandLine.hasOption("b"));
        assertFalse(commandLine.hasOption("arg"));
    }
}
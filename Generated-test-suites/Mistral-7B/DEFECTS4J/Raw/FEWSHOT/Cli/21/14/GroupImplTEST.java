package org.apache.commons.cli2.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.DisplaySetting;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.junit.Before;
import org.junit.Test;

public class GroupImplTest {

    private List<Option> options;
    private GroupImpl group;
    private WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        options = new ArrayList<>();
        commandLine = new WriteableCommandLine();
    }

    @Test
    public void testGroupImplCanProcess() throws OptionException {
        // Test empty group
        group = createGroup(Collections.emptyList());
        assertFalse(group.canProcess(commandLine, ""));

        // Test group with options
        Option option1 = createOption("o1");
        Option option2 = createOption("o2");
        options.add(option1);
        options.add(option2);
        group = new GroupImpl(options, null, null, 0, 1, false);

        // Test option found
        commandLine.parse("-o1 value1");
        assertTrue(group.canProcess(commandLine, "o1"));

        // Test option not found but option with same trigger found
        commandLine.parse("-o2 value2");
        assertTrue(group.canProcess(commandLine, "o1"));

        // Test option not found and not bursting
        commandLine.parse("-o3 value3");
        assertFalse(group.canProcess(commandLine, "o1"));

        // Test argument
        Argument arg = createArgument("a");
        options.add(arg);
        group = new GroupImpl(options, null, null, 0, 1, false);
        commandLine.parse("value1");
        assertTrue(group.canProcess(commandLine, "a"));

        // Test argument not found and anonymous arguments exist
        for (int i = 0; i < 2; i++) {
            Argument arg = createArgument("a" + i);
            options.add(arg);
        }
        assertTrue(group.canProcess(commandLine, "a0"));
    }

    @Test
    public void testGroupImplProcess() throws OptionException {
        Option option1 = createOption("o1");
        Option option2 = createOption("o2");
        options.add(option1);
        options.add(option2);
        group = new GroupImpl(options, null, null, 0, 1, false);

        String[] args = {"o1", "value1"};
        commandLine.parse(args);

        group.process(commandLine, commandLine.getArguments().listIterator());

        assertEquals("value1", option1.getValue());
        assertEquals(null, option2.getValue());
    }

    @Test
    public void testGroupImplValidate() {
        Option option1 = createOption("o1");
        Option option2 = createOption("o2");
        options.add(option1);
        options.add(option2);
        group = new GroupImpl(options, null, null, 1, 1, false);

        WriteableCommandLine commandLine = new WriteableCommandLine();
        commandLine.parse("-o1 value1 -o1 value2");
        group.validate(commandLine);

        assertEquals("value1", option1.getValue());
        assertEquals("value2", option2.getValue());

        commandLine.parse("-o1 value1");
        try {
            group.validate(commandLine);
            fail();
        } catch (OptionException e) {
            // expected
        }

        commandLine.parse("");
        try {
            group.validate(commandLine);
            fail();
        } catch (OptionException e) {
            // expected
        }
    }

    @Test
    public void testGroupImplAppendUsage() {
        Option option1 = createOption("o1");
        Option option2 = createOption("o2");
        options.add(option1);
        options.add(option2);
        group = new GroupImpl(options, null, null, 0, 1, false);

        StringBuffer buffer = new StringBuffer();
        Set<DisplaySetting> helpSettings = new HashSet<>();
        Comparator<String> comp = null;

        group.appendUsage(buffer, helpSettings, comp);

        String expectedUsage = "group (" + option1.getPreferredName() + " <value>" +
                option2.getPreferredName() + " <value>" + ")";
        assertEquals(expectedUsage, buffer.toString());
    }

    @Test
    public void testGroupImplHelpLines() {
        Option option1 = createOption("o1");
        Option option2 = createOption("o2");
        options.add(option1);
        options.add(option2);
        group = new GroupImpl(options, null, null, 0, 1, false);

        List<HelpLine> helpLines = group.helpLines(0, Collections.emptySet(), null);

        assertEquals(2, helpLines.size());
        assertEquals("group (" + option1.getPreferredName() + " <value>)",
                helpLines.get(0).getHeaders().get(0).toString());
        assertEquals("", helpLines.get(0).getDescription());
        assertEquals("", helpLines.get(1).getDescription());
    }

    @Test
    public void testGroupImplGetOptions() {
        Option option1 = createOption("o1");
        Option option2 = createOption("o2");
        options.add(option1);
        options.add(option2);
        group = new GroupImpl(options, null, null, 0, 1, false);

        List<Option> optionsList = group.getOptions();

        assertEquals(2, optionsList.size());
        assertEquals(option1, optionsList.get(0));
        assertEquals(option2, optionsList.get(1));
    }

    @Test
    public void testGroupImplGetAnonymous() {
        Option option1 = createArgument("a1");
        Option option2 = createArgument("a2");
        options.add(option1);
        options.add(option2);
        group = new GroupImpl(options, null, null, 0, 1, false);

        List<Option> anonymousList = group.getAnonymous();

        assertEquals(2, anonymousList.size());
        assertEquals(option1, anonymousList.get(0));
        assertEquals(option2, anonymousList.get(1));
    }

    @Test
    public void testGroupImplFindOption() {
        Option option1 = createOption("o1");
        Option option2 = createOption("o2");
        options.add(option1);
        options.add(option2);
        group = new GroupImpl(options, null, null, 0, 1, false);

        Option foundOption = group.findOption("o1");

        assertEquals(option1, foundOption);

        foundOption = group.findOption("o3");

        assertNull(foundOption);
    }

    @Test
    public void testGroupImplGetMinimum() {
        Option option1 = createOption("o1");
        Option option2 = createOption("o2");
        options.add(option1);
        options.add(option2);
        group = new GroupImpl(options, null, null, 0, 1, false);

        assertEquals(0, group.getMinimum());
    }

    @Test
    public void testGroupImplGetMaximum() {
        Option option1 = createOption("o1");
        Option option2 = createOption("o2");
        options.add(option1);
        options.add(option2);
        group = new GroupImpl(options, null, null, 0, 1, false);

        assertEquals(1, group.getMaximum());
    }

    @Test
    public void testGroupImplIsRequired() {
        Option option1 = createOption("o1");
        Option option2 = createOption("o2");
        options.add(option1);
        options.add(option2);
        group = new GroupImpl(options, null, null, 1, 1, false);

        assertFalse(group.isRequired());

        group = new GroupImpl(options, null, null, 1, 1, true);

        assertTrue(group.isRequired());
    }

    @Test
    public void testGroupImplDefaults() {
        Option option1 = createOption("o1");
        Option option2 = createOption("o2");
        options.add(option1);
        options.add(option2);
        group = new GroupImpl(options, null, null, 0, 1, false);

        group.defaults(commandLine);

        assertEquals(null, option1.getValue());
        assertEquals(null, option2.getValue());
    }

    private Option createOption(String name) {
        return new OptionBuilder().withName(name).build();
    }

    private Argument createArgument(String longName) {
        return new ArgumentBuilder().withLongName(longName).build();
    }
}
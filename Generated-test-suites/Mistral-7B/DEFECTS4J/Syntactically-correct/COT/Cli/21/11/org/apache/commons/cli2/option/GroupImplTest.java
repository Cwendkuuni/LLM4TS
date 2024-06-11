package org.apache.commons.cli2.option;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.DisplaySetting;
import org.apache.commons.cli2.Group;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.test.testutils.TestUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import static org.apache.commons.cli2.test.testutils.TestUtils.assertFalse;
import static org.apache.commons.cli2.test.testutils.TestUtils.assertEquals;
import static org.apache.commons.cli2.test.testutils.TestUtils.assertNotNull;
import static org.apache.commons.cli2.test.testutils.TestUtils.assertTrue;
import static org.junit.Assert.fail;

public class GroupImplTest {

    private Group group;
    private WriteableCommandLine commandLine;
    private Option option1;
    private Option option2;
    private Option option3;
    private Argument arg1;
    private Argument arg2;

    @Before
    public void setUp() {
        List<Option> options = new ArrayList<>();
        List<Argument> anonymous = new ArrayList<>();

        option1 = new Option("o1", "Option 1", true, "desc1");
        option1.setRequired(false);
        options.add(option1);

        option2 = new Option("o2", "Option 2", false, "desc2");
        option2.setRequired(false);
        options.add(option2);

        option3 = new Option("o3", "Option 3", false, "desc3");
        option3.setRequired(false);
        options.add(option3);

        arg1 = new Argument("a1", "Argument 1");
        arg1.setRequired(false);
        anonymous.add(arg1);

        arg2 = new Argument("a2", "Argument 2");
        arg2.setRequired(false);
        anonymous.add(arg2);

        group = new GroupImpl(options, null, "Group", 0, 3, false) {
            @Override
            public void appendUsage(StringBuffer buffer, Set<DisplaySetting> helpSettings, Comparator comp) {
                super.appendUsage(buffer, helpSettings, comp);
                buffer.append(" Anonymous arguments: ");
                appendUsage(buffer, helpSettings, comp, " ");
            }
        };

        commandLine = new WriteableCommandLine();
    }

    @Test
    public void testCanProcessNoArg() {
        group.canProcess(commandLine, null);
        assertTrue(group.canProcess(commandLine, ""));
    }

    @Test
    public void testCanProcessOption() {
        commandLine.parse(Arrays.asList("--o1"));
        assertTrue(group.canProcess(commandLine, "o1"));
    }

    @Test
    public void testCanProcessOptionBursting() {
        commandLine.parse(Arrays.asList("--o1", "--o2"));
        assertTrue(group.canProcess(commandLine, "o1"));
        assertTrue(group.canProcess(commandLine, "o2"));
    }

    @Test
    public void testCanProcessAnonymousArgument() {
        commandLine.parse(Arrays.asList("--o1", "a1"));
        assertTrue(group.canProcess(commandLine, "a1"));
    }

    @Test
    public void testCanProcessAnonymousArguments() {
        commandLine.parse(Arrays.asList("--o1", "a1", "a2"));
        assertTrue(group.canProcess(commandLine, "a1"));
        assertTrue(group.canProcess(commandLine, "a2"));
    }

    @Test
    public void testCanProcessUnexistingOption() {
        commandLine.parse(Arrays.asList("--unexistingOption"));
        assertFalse(group.canProcess(commandLine, "unexistingOption"));
    }

    @Test
    public void testCanProcessArgument() {
        commandLine.parse(Arrays.asList("--o1", "a1"));
        assertFalse(group.canProcess(commandLine, "o1"));
        assertTrue(group.canProcess(commandLine, "a1"));
    }

    @Test(expected = OptionException.class)
    public void testCanProcessTooManyOptions() {
        commandLine.parse(Arrays.asList("--o1", "--o1", "--o1"));
        group.canProcess(commandLine, "o1");
        fail("Should throw OptionException");
    }

    @Test(expected = OptionException.class)
    public void testCanProcessTooFewOptions() {
        commandLine.parse(Arrays.asList("--o1"));
        group.canProcess(commandLine, "o1");
        group.canProcess(commandLine, "o2");
        fail("Should throw OptionException");
    }

    @Test
    public void testGetName() {
        assertEquals("Group", group.getPreferredName());
    }

    @Test
    public void testGetDescription() {
        assertEquals("", group.getDescription());
    }

    @Test
    public void testGetMinimum() {
        assertEquals(0, group.getMinimum());
    }

    @Test
    public void testGetMaximum() {
        assertEquals(3, group.getMaximum());
    }

    @Test
    public void testGetOptions() {
        assertNotNull(group.getOptions());
        assertEquals(3, group.getOptions().size());
    }

    @Test
    public void testGetAnonymous() {
        assertNotNull(group.getAnonymous());
        assertEquals(2, group.getAnonymous().size());
    }

    @Test
    public void testFindOption() {
        Option found = group.findOption("o1");
        assertNotNull(found);
        assertEquals(option1, found);
    }

    @Test
    public void testFindOptionNotFound() {
        Option found = group.findOption("nonexistentOption");
        assertNull(found);
    }

    @Test
    public void testProcess() {
        commandLine.parse(Arrays.asList("--o1", "value1", "--o2", "value2", "a1", "a2"));
        group.process(commandLine, commandLine.iterator());
        assertEquals(2, option1.getValueCount());
        assertEquals("value1", option1.getValue(0));
        assertEquals("value2", option1.getValue(1));

        assertEquals(0, option2.getValueCount());

        assertEquals("a1", arg1.getValue());
        assertEquals("a2", arg2.getValue());
    }

    @Test(expected = OptionException.class)
    public void testProcessMissingRequiredOption() {
        commandLine.parse(Arrays.asList("--o1"));
        group.process(commandLine, commandLine.iterator());
        fail("Should throw OptionException");
    }

    @Test(expected = OptionException.class)
    public void testProcessTooManyOptions() {
        commandLine.parse(Arrays.asList("--o1", "--o1", "--o1"));
        group.process(commandLine, commandLine.iterator());
        fail("Should throw OptionException");
    }

    @Test
    public void testValidate() {
        commandLine.parse(Arrays.asList("--o1", "--o2", "--o3"));
        group.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidateTooFewOptions() {
        commandLine.parse(Arrays.asList("--o1"));
        group.validate(commandLine);
        fail("Should throw OptionException");
    }

    @Test(expected = OptionException.class)
    public void testValidateTooManyOptions() {
        commandLine.parse(Arrays.asList("--o1", "--o1", "--o1"));
        group.validate(commandLine);
        fail("Should throw OptionException");
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        Set<DisplaySetting> helpSettings = new TreeSet<>();
        helpSettings.add(DisplaySetting.DISPLAY_GROUP_NAME);
        helpSettings.add(DisplaySetting.DISPLAY_GROUP_EXPANDED);
        helpSettings.add(DisplaySetting.DISPLAY_GROUP_ARGUMENT);

        group.appendUsage(buffer, helpSettings, null);

        String expected = String.format("Group (%n" +
                "  Options: %n" +
                "    --o1 <value>...  Option 1%n" +
                "    --o2  Option 2%n" +
                "    --o3  Option 3%n" +
                "  Anonymous arguments: %s%n", System.lineSeparator());

        assertEquals(expected, buffer.toString());
    }

    @Test
    public void testHelpLines() {
        List<HelpLine> helpLines = group.helpLines(0, null, null);

        assertEquals(4, helpLines.size());
        HelpLine helpLine1 = helpLines.get(0);
        assertEquals("--o1 <value>...", helpLine1.getOptionSpec().getLongOpt());
        assertEquals("Option 1", helpLine1.getOptionSpec().getOpt());
        assertEquals("value", helpLine1.getArgumentSpec().getName());
        assertEquals("desc1", helpLine1.getDescription());

        HelpLine helpLine2 = helpLines.get(1);
        assertEquals("--o2", helpLine2.getOptionSpec().getLongOpt());
        assertEquals("Option 2", helpLine2.getOptionSpec().getOpt());
        assertEquals(null, helpLine2.getArgumentSpec());
        assertEquals("desc2", helpLine2.getDescription());

        HelpLine helpLine3 = helpLines.get(2);
        assertEquals("--o3", helpLine3.getOptionSpec().getLongOpt());
        assertEquals("Option 3", helpLine3.getOptionSpec().getOpt());
        assertEquals(null, helpLine3.getArgumentSpec());
        assertEquals("desc3", helpLine3.getDescription());

        HelpLine helpLine4 = helpLines.get(3);
        assertEquals("--", helpLine4.getOptionSpec().getLongOpt());
        assertEquals(null, helpLine4.getOptionSpec().getOpt());
        assertEquals("Anonymous arguments:", helpLine4.getArgumentSpec().getName());
        assertEquals("", helpLine4.getDescription());
    }
}
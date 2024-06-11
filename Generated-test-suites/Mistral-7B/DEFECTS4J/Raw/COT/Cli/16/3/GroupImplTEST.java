package org.apache.commons.cli2.option;

import org.apache.commons.cli2.Group;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.TestUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class GroupImplTest {
    // Test variables
    private Group group;

    @Before
    public void setUp() {
        List<Option> options = new ArrayList<>();
        options.add(TestUtils.createOption("o1", false, "opt1"));
        options.add(TestUtils.createOption("o2", true, "opt2", new String[]{"v1", "v2"}));
        options.add(TestUtils.createOption("o3", false, "opt3", new String[]{"v3", "v4"}));

        group = new GroupImpl(options, "group", "A group", 1, 3);
    }

    @Test
    public void testCanProcess() {
        WriteableCommandLine commandLine = TestUtils.createCommandLine("--o1 --o2 v1 --o3 v3");

        assertTrue(group.canProcess(commandLine, "o1"));
        assertFalse(group.canProcess(commandLine, "o11"));
        assertTrue(group.canProcess(commandLine, "o2"));
        assertTrue(group.canProcess(commandLine, "o2v1"));
        assertTrue(group.canProcess(commandLine, "o2v2"));
        assertFalse(group.canProcess(commandLine, "o3"));
        assertFalse(group.canProcess(commandLine, "o3v1"));
        assertFalse(group.canProcess(commandLine, "o3v5"));
    }

    @Test
    public void testGetName() {
        assertEquals("group", group.getName());
    }

    @Test
    public void testGetDescription() {
        assertEquals("A group", group.getDescription());
    }

    @Test
    public void testGetOptions() {
        List<Option> expectedOptions = Arrays.asList(
                TestUtils.createOption("o1", false, "opt1"),
                TestUtils.createOption("o2", true, "opt2", new String[]{"v1", "v2"}),
                TestUtils.createOption("o3", false, "opt3", new String[]{"v3", "v4"})
        );

        List<Option> options = group.getOptions();
        assertEquals(expectedOptions.size(), options.size());

        for (int i = 0; i < expectedOptions.size(); i++) {
            Option expectedOption = expectedOptions.get(i);
            Option option = options.get(i);

            assertSame(expectedOption, option);
        }
    }

    @Test
    public void testGetAnonymous() {
        List<Option> expectedAnonymous = new ArrayList<>();
        expectedAnonymous.add(TestUtils.createOption("a1", false, "anonym1"));
        expectedAnonymous.add(TestUtils.createOption("a2", false, "anonym2"));

        List<Option> anonymous = group.getAnonymous();
        assertEquals(expectedAnonymous.size(), anonymous.size());

        for (int i = 0; i < expectedAnonymous.size(); i++) {
            Option expectedOption = expectedAnonymous.get(i);
            Option option = anonymous.get(i);

            assertSame(expectedOption, option);
        }
    }

    @Test
    public void testGetMinimum() {
        assertEquals(1, group.getMinimum());
    }

    @Test
    public void testGetMaximum() {
        assertEquals(3, group.getMaximum());
    }

    @Test(expected = AssertionError.class)
    public void testProcessWithInvalidArgument() {
        WriteableCommandLine commandLine = TestUtils.createCommandLine("invalidArgument");
        group.process(commandLine, TestUtils.createListIterator());
    }

    @Test(expected = OptionException.class)
    public void testProcessWithInvalidOption() {
        WriteableCommandLine commandLine = TestUtils.createCommandLine("--oInvalid");
        group.process(commandLine, TestUtils.createListIterator());
    }

    @Test(expected = OptionException.class)
    public void testProcessWithTooFewOptions() {
        WriteableCommandLine commandLine = TestUtils.createCommandLine("o1");
        group.process(commandLine, TestUtils.createListIterator());
    }

    @Test(expected = OptionException.class)
    public void testProcessWithTooManyOptions() {
        WriteableCommandLine commandLine = TestUtils.createCommandLine("o1 o2 o3 o4");
        group.process(commandLine, TestUtils.createListIterator());
    }

    @Test
    public void testProcessWithValidArguments() {
        WriteableCommandLine commandLine = TestUtils.createCommandLine("--o1 --o2 v1 --o3 v3");

        group.process(commandLine, TestUtils.createListIterator());

        Option o1 = group.findOption("o1");
        assertTrue(o1.isSet());

        Option o2 = group.findOption("o2");
        assertTrue(o2.isSet());
        assertTrue(o2.getValue().contains("v1"));
        assertTrue(o2.getValue().contains("v2"));

        Option o3 = group.findOption("o3");
        assertTrue(o3.isSet());
        assertTrue(o3.getValue().contains("v3"));
        assertTrue(o3.getValue().contains("v4"));
    }

    @Test(expected = OptionException.class)
    public void testValidateWithInvalidArgument() {
        WriteableCommandLine commandLine = TestUtils.createCommandLine("invalidArgument");
        group.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithMissingOption() {
        WriteableCommandLine commandLine = TestUtils.createCommandLine("o1");
        group.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithTooManyOptions() {
        WriteableCommandLine commandLine = TestUtils.createCommandLine("o1 o2 o3 o4");
        group.validate(commandLine);
    }

    @Test
    public void testValidateWithValidArguments() {
        WriteableCommandLine commandLine = TestUtils.createCommandLine("--o1 --o2 v1 --o3 v3");

        group.validate(commandLine);
    }

    @Test
    public void testAppendUsage() {
        StringBuilder buffer = new StringBuilder();
        group.appendUsage(buffer, Collections.emptySet(), null);

        String expectedUsage = "group: A group\n";
        expectedUsage += "  o1 OPTION\n";
        expectedUsage += "  o2{0}\n";
        expectedUsage += "      --o2 VALUE\n";
        expectedUsage += "      --o2 VALUE ...\n";
        expectedUsage += "  o3{0,2}\n";
        expectedUsage += "      --o3 VALUE\n";
        expectedUsage += "      --o3 VALUE ...\n";

        assertEquals(expectedUsage, buffer.toString());
    }

    @Test
    public void testHelpLines() {
        List<HelpLine> helpLines = group.helpLines(1, Collections.emptySet(), null);

        List<String> expectedHelpLines = new ArrayList<>();
        expectedHelpLines.add("group: A group");
        expectedHelpLines.add("");
        expectedHelpLines.add("o1 OPTION");
        expectedHelpLines.add("");
        expectedHelpLines.add("  o1:");
        expectedHelpLines.add("    opt1\n");
        expectedHelpLines.add("");
        expectedHelpLines.add("o2 OPTION\n");
        expectedHelpLines.add("  o2:");
        expectedHelpLines.add("    opt2 {0} ...\n");
        expectedHelpLines.add("      --o2 <VALUE>:");
        expectedHelpLines.add("        VALUE\n");
        expectedHelpLines.add("");
        expectedHelpLines.add("o3 OPTION\n");
        expectedHelpLines.add("  o3:");
        expectedHelpLines.add("    opt3 {0,2} ...\n");
        expectedHelpLines.add("      --o3 <VALUE>:");
        expectedHelpLines.add("        VALUE\n");
        expectedHelpLines.add("");

        for (int i = 0; i < helpLines.size(); i++) {
            String expectedHelpLine = expectedHelpLines.get(i);
            String actualHelpLine = helpLines.get(i).getMessage();

            assertEquals(expectedHelpLine, actualHelpLine);
        }
    }
}
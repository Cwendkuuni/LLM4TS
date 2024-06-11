import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.test.TestUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class GroupImplTest {

    @Test
    public void testGroupImpl() {
        // create a list of options for the group
        List<Option> options = new ArrayList<>();
        options.add(TestUtils.createOption("o1", "Option 1", true));
        options.add(TestUtils.createOption("o2", "Option 2", false));
        options.add(TestUtils.createOption("o3", "Option 3", 3));
        options.add(TestUtils.createOption("o4", "Option 4", 3, "a", "b"));
        options.add(TestUtils.createOption("o5", "Option 5", 1, "x"));

        // create a group with the given options
        List<Option> groupOptions = new ArrayList<>(options.subList(0, 3));
        Group group = new GroupImpl(groupOptions, "My Group", "This is my group");

        // test canProcess method
        CommandLine cmd = TestUtils.createCommandLine();
        String[] arguments = {"arg1", "arg2", "arg3", "o1", "o1", "o2", "arg5"};
        assertFalse("Invalid argument should not be processed", group.canProcess(cmd, "arg4"));
        assertTrue("Valid argument should be processed", group.canProcess(cmd, "o1"));

        // test getPrefixes method
        Set<String> prefixes = group.getPrefixes();
        assertEquals("Size of prefixes set should be 1", 1, prefixes.size());
        assertTrue("Prefixes should contain '--'", prefixes.contains("--"));

        // test getTriggers method
        Set<String> triggers = group.getTriggers();
        assertEquals("Size of triggers set should be 5", 5, triggers.size());
        assertTrue("Triggers should contain 'o1'", triggers.contains("o1"));
        assertTrue("Triggers should contain 'o2'", triggers.contains("o2"));
        assertTrue("Triggers should contain 'o3'", triggers.contains("o3"));
        assertTrue("Triggers should contain 'o4'", triggers.contains("o4"));
        assertTrue("Triggers should contain 'o5'", triggers.contains("o5"));

        // test process method
        cmd = TestUtils.createCommandLine();
        cmd.addOption(TestUtils.createOption("g", "Group", false, "My Group", "This is my group"));
        cmd.addOptions(options);
        String[] cmdArgs = {"arg1", "arg2", "arg3", "o1", "o1", "o2", "arg5"};
        group.process(cmd, cmd.parse(cmdArgs));
        assertEquals("Size of args list should be 8", 8, cmdArgs.length);
        assertEquals("First processed arg should be 'o1'", "o1", cmdArgs[3]);
        assertEquals("Second processed arg should be 'o1'", "o1", cmdArgs[4]);
        assertEquals("Third processed arg should be 'o2'", "o2", cmdArgs[5]);
        assertEquals("Fourth processed arg should be 'arg3'", "arg3", cmdArgs[6]);
        assertNull("Fifth processed arg should be null", cmdArgs[7]);

        // test validate method
        cmd = TestUtils.createCommandLine();
        try {
            group.validate(cmd);
        } catch (OptionException e) {
            fail("Validation should have succeeded");
        }

        // test process method with invalid arguments
        cmd = TestUtils.createCommandLine();
        cmd.addOption(group);
        cmd.addOption("o1");
        cmd.addOption("o1");
        cmd.addOption("o1");
        try {
            group.process(cmd, cmd.parse(new String[]{"arg"}));
            fail("Validation should have failed");
        } catch (OptionException e) {
            assertEquals("Error message should be 'arg is an invalid option' ",
                    "arg is an invalid option", e.getMessage());
        }

        // test validate method with too few arguments
        cmd = TestUtils.createCommandLine();
        cmd.addOption(group);
        cmd.addOption("o1");
        try {
            group.validate(cmd);
            fail("Validation should have failed");
        } catch (OptionException e) {
            assertEquals("Error message should be 'Option 'o1' is missing'",
                    "Option 'o1' is missing", e.getMessage());
        }

        // test validate method with too many arguments
        cmd = TestUtils.createCommandLine();
        cmd.addOption(group);
        cmd.addOption("o1");
        cmd.addOption("o1");
        cmd.addOption("o1");
        cmd.addOption("o1");
        try {
            group.validate(cmd);
            fail("Validation should have failed");
        } catch (OptionException e) {
            assertEquals("Error message should be 'Option 'o1' appears 4 times but can appear a maximum of 3 times'",
                    "Option 'o1' appears 4 times but can appear a maximum of 3 times", e.getMessage());
        }

        // test appendUsage method
        StringBuffer buffer = new StringBuffer();
        Set<DisplaySetting> helpSettings = new HashSet<>();
        group.appendUsage(buffer, helpSettings, null);
        String usage = buffer.toString();
        assertTrue("Usage should start with 'My Group:'", usage.startsWith("My Group:"));
        assertTrue("Usage should contain 'This is my group' ", usage.contains("This is my group"));
        assertTrue("Usage should contain 'Option 1'", usage.contains("Option 1"));
        assertTrue("Usage should contain '--o1' ", usage.contains("--o1"));
        assertTrue("Usage should contain '--o2' ", usage.contains("--o2"));
        assertTrue("Usage should contain '--o3' ", usage.contains("--o3"));
        assertTrue("Usage should contain '--o4' ", usage.contains("--o4"));
        assertTrue("Usage should contain '--o5' ", usage.contains("--o5"));

        // test helpLines method
        List<HelpLine> helpLines = group.helpLines(0, helpSettings, null);
        assertEquals("Size of helpLines list should be 6", 6, helpLines.size());
        assertTrue("HelpLines should contain 'My Group:'", helpLines.get(0).getHead().startsWith("My Group:"));
        assertTrue("HelpLines should contain 'This is my group' ", helpLines.get(0).getLongDescription().contains("This is my group"));
        assertTrue("HelpLines should contain 'Option 1' ", helpLines.get(1).getHead().contains("Option 1"));
        assertTrue("HelpLines should contain '--o1' ", helpLines.get(1).getLongDescription().contains("--o1"));
        assertTrue("HelpLines should contain 'Option 2' ", helpLines.get(2).getHead().contains("Option 2"));
        assertTrue("HelpLines should contain '--o2' ", helpLines.get(2).getLongDescription().contains("--o2"));
        assertTrue("HelpLines should contain 'Option 3' ", helpLines.get(3).getHead().contains("Option 3"));
        assertTrue("HelpLines should contain '--o3' ", helpLines.get(3).getLongDescription().contains("--o3"));
        assertTrue("HelpLines should contain 'Option 4' ", helpLines.get(4).getHead().contains("Option 4"));
        assertTrue("HelpLines should contain '--o4 [a] [b]' ", helpLines.get(4).getLongDescription().contains("--o4 [a] [b]"));
        assertTrue("HelpLines should contain 'Option 5' ", helpLines.get(5).getHead().contains("Option 5"));
        assertTrue("HelpLines should contain '--o5' ", helpLines.get(5).getLongDescription().contains("--o5"));

        // test getOptions method
        List<Option> groupOptionsTest = group.getOptions();
        assertEquals("Size of options list should be 3", 3, groupOptionsTest.size());
        assertEquals("First option should be 'o1' ", "o1", groupOptionsTest.get(0).getLongOpt());
        assertEquals("Second option should be 'o2' ", "o2", groupOptionsTest.get(1).getLongOpt());
        assertEquals("Third option should be 'o3' ", "o3", groupOptionsTest.get(2).getLongOpt());

        // test getAnonymous method
        List<Option> anonymousOptions = group.getAnonymous();
        assertEquals("Size of anonymousOptions list should be 2", 2, anonymousOptions.size());
        assertEquals("First anonymous option should be 'o4' ", "o4", anonymousOptions.get(0).getLongOpt());
        assertEquals("Second anonymous option should be 'o5' ", "o5", anonymousOptions.get(1).getLongOpt());

        // test findOption method
        Option foundOption = group.findOption("o3");
        assertNotNull("FoundOption should not be null", foundOption);
        assertEquals("FoundOption should be 'o3' ", "o3", foundOption.getLongOpt());

        Option notFoundOption = group.findOption("o6");
        assertNull("NotFoundOption should be null", notFoundOption);
    }
}
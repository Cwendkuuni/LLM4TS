package org.apache.commons.cli2.option;

import static org.junit.Assert.*;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.TestUtils;
import org.apache.commons.cli2.option.GroupImpl;
import org.apache.commons.cli2.option.OptionBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GroupImplTest {

    private GroupImpl group;
    private Options options;

    @Before
    public void setUp() {
        List<Option> optionsList = new ArrayList<>();

        Option option1 = OptionBuilder.withLongOpt("option1")
                .withDescription("Test Option 1")
                .isRequired()
                .create();
        Option option2 = OptionBuilder.withLongOpt("option2")
                .withDescription("Test Option 2")
                .create();
        Option option3 = OptionBuilder.withLongOpt("option3")
                .withDescription("Test Option 3")
                .create();
        Option argumentOption = OptionBuilder.withLongOpt("arg")
                .withDescription("Test Argument")
                .createArg();

        optionsList.add(option1);
        optionsList.add(option2);
        optionsList.add(option3);
        optionsList.add(argumentOption);

        options = new Options();
        options.addOptionGroup(new GroupImpl(optionsList, "Group", "Test Group", 1, 3));
    }

    @Test
    public void testCanProcess() {
        CommandLine commandLine = TestUtils.createCommandLine("test", new String[0]);
        List<String> arguments = Arrays.asList("--option1", "--option2", "--option3", "--arg", "value");

        group.canProcess(commandLine, arguments.get(0));
        assertTrue(group.canProcess(commandLine, arguments.get(1)));
        assertTrue(group.canProcess(commandLine, arguments.get(2)));
        assertTrue(group.canProcess(commandLine, arguments.get(3)));
        assertFalse(group.canProcess(commandLine, arguments.get(4)));

        commandLine = TestUtils.createCommandLine("test", Arrays.asList("--option1", "--option2", "--option3"));
        List<String> args = Arrays.asList("--option1", "--option2", "--arg", "value");

        group.canProcess(commandLine, args.get(3));
        assertFalse(group.canProcess(commandLine, args.get(0)));
        assertFalse(group.canProcess(commandLine, args.get(1)));
        assertFalse(group.canProcess(commandLine, args.get(2)));
    }

    @Test(expected = OptionException.class)
    public void testValidate() throws OptionException {
        CommandLine commandLine = TestUtils.createCommandLine("test", new String[0]);

        List<String> arguments = Arrays.asList("--option1", "--option2");
        group.validate(commandLine);

        commandLine = TestUtils.createCommandLine("test", new String[0]);
        arguments = Arrays.asList("--option1", "--option2", "--arg");
        group.validate(commandLine);

        commandLine = TestUtils.createCommandLine("test", new String[0]);
        arguments = Arrays.asList("--option1", "--option2", "--arg1", "--arg2");
        group.validate(commandLine);

        commandLine = TestUtils.createCommandLine("test", new String[0]);
        arguments = Arrays.asList("--option1", "--option2", "--arg1", "--arg2", "--option3");
        group.validate(commandLine);

        commandLine = TestUtils.createCommandLine("test", new String[0]);
        arguments = Arrays.asList("--option1", "--option2", "--arg1", "--arg2", "--option3", "--option3");
        group.validate(commandLine);

        commandLine = TestUtils.createCommandLine("test", new String[0]);
        arguments = Arrays.asList("--option1", "--option2", "--arg1", "--arg2", "--option3", "--option3", "--option3");
        group.validate(commandLine);

        commandLine = TestUtils.createCommandLine("test", new String[0]);
        arguments = Arrays.asList("--option1", "--option2", "--arg1", "--arg2", "--option3", "--option3", "--option3", "--option4");
        group.validate(commandLine);
    }

    @Test
    public void testProcess() throws OptionException {
        CommandLine commandLine = TestUtils.createCommandLine("test", new String[0]);

        group.process(commandLine, TestUtils.getListIterator(commandLine.getArgList()));

        commandLine = TestUtils.createCommandLine("test", new String[]{"--option1"});
        List<String> arguments = Arrays.asList("--option1", "--option2", "--option3", "--arg");
        group.process(commandLine, TestUtils.getListIterator(commandLine.getArgList()));

        commandLine = TestUtils.createCommandLine("test", new String[]{"--option1"});
        arguments = Arrays.asList("--option1", "--option2", "--option3", "--arg1", "--arg2");
        group.process(commandLine, TestUtils.getListIterator(commandLine.getArgList()));

        commandLine = TestUtils.createCommandLine("test", new String[]{"--option1"});
        arguments = Arrays.asList("--option1", "--option2", "--option3", "--arg1", "--arg2", "--arg3");
        group.process(commandLine, TestUtils.getListIterator(commandLine.getArgList()));

        commandLine = TestUtils.createCommandLine("test", new String[]{"--option1"});
        arguments = Arrays.asList("--option1", "--option2", "--option3", "--arg1", "--arg2", "--arg3", "--option3");
        group.process(commandLine, TestUtils.getListIterator(commandLine.getArgList()));

        commandLine = TestUtils.createCommandLine("test", new String[]{"--option1"});
        arguments = Arrays.asList("--option1", "--option2", "--option3", "--arg1", "--arg2", "--arg3", "--option3", "--arg4");
        group.process(commandLine, TestUtils.getListIterator(commandLine.getArgList()));
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        Set<DisplaySetting> helpSettings = Collections.singleton(DisplaySetting.DISPLAY_GROUP_NAME);
        group.appendUsage(buffer, helpSettings, null);

        String expected = "group (Test Group)\n";
        String actual = buffer.toString();
        assertEquals(expected, actual);

        helpSettings.add(DisplaySetting.DISPLAY_GROUP_EXPANDED);
        buffer.setLength(0);
        group.appendUsage(buffer, helpSettings, null);

        expected = "group (Test Group)\n" +
                "  option1\n" +
                "      Test Option 1\n" +
                "  option2\n" +
                "      Test Option 2\n" +
                "  option3\n" +
                "      Test Option 3\n" +
                "arg\n" +
                "  Test Argument\n";
        assertEquals(expected, buffer.toString());

        helpSettings.add(DisplaySetting.DISPLAY_GROUP_ARGUMENT);
        buffer.setLength(0);
        group.appendUsage(buffer, helpSettings, null);

        expected = "group (Test Group)\n" +
                "  option1\n" +
                "      Test Option 1\n" +
                "  option2\n" +
                "      Test Option 2\n" +
                "  option3\n" +
                "      Test Option 3\n" +
                "arg\n" +
                "  Test Argument\n";
        assertEquals(expected, buffer.toString());
    }

    @Test
    public void testHelpLines() {
        StringBuffer buffer = new StringBuffer();
        HelpFormatter formatter = new HelpFormatter();
        Set<DisplaySetting> helpSettings = Collections.singleton(DisplaySetting.DISPLAY_GROUP_NAME);
        List<HelpLine> helpLines = group.helpLines(0, helpSettings, null);

        formatter.printHelp(99, "test", helpLines, 1.0, buffer, true);

        String expected = "test (options)\n" +
                "\n" +
                "Mandatory options:\n" +
                "  option1\n" +
                "      Test Option 1\n" +
                "  option2\n" +
                "      Test Option 2\n" +
                "  option3\n" +
                "      Test Option 3\n" +
                "\n" +
                "Optional options:\n" +
                "  arg\n" +
                "      Test Argument\n";
        String actual = buffer.toString();
        assertEquals(expected, actual);

        helpSettings.add(DisplaySetting.DISPLAY_GROUP_EXPANDED);
        buffer.setLength(0);
        helpLines = group.helpLines(0, helpSettings, null);

        formatter.printHelp(99, "test", helpLines, 1.0, buffer, true);

        expected = "test (options)\n" +
                "\n" +
                "Mandatory options:\n" +
                "  option1\n" +
                "      Test Option 1\n" +
                "  option2\n" +
                "      Test Option 2\n" +
                "  option3\n" +
                "      Test Option 3\n" +
                "\n" +
                "Optional options:\n" +
                "  arg\n" +
                "      Test Argument\n" +
                "\n" +
                "  option1\n" +
                "      Test Option 1\n" +
                "\n" +
                "      This option can appear 0 or more times as its value is ignored.\n" +
                "      It is not possible to specify an argument for option 'option1'\n" +
                "\n" +
                "Options that can appear only once:\n" +
                "  option2\n" +
                "      Test Option 2\n" +
                "\n" +
                "Options that can appear many times:\n" +
                "  option3\n" +
                "      Test Option 3\n" +
                "\n" +
                "      This option can appear many times, and its arguments will be combined into one string.\n" +
                "\n" +
                "Arguments:\n" +
                "  arg\n" +
                "      Test Argument\n";
        assertEquals(expected, buffer.toString());

        helpSettings.add(DisplaySetting.DISPLAY_GROUP_ARGUMENT);
        buffer.setLength(0);
        helpLines = group.helpLines(0, helpSettings, null);

        formatter.printHelp(99, "test", helpLines, 1.0, buffer, true);

        expected = "test (options)\n" +
                "\n" +
                "Mandatory options:\n" +
                "  option1\n" +
                "      Test Option 1\n" +
                "  option2\n" +
                "      Test Option 2\n" +
                "  option3\n" +
                "      Test Option 3\n" +
                "\n" +
                "Optional options:\n" +
                "  arg\n" +
                "      Test Argument\n" +
                "\n" +
                "  arg\n" +
                "      Test Argument\n";
        assertEquals(expected, buffer.toString());
    }

    @Test
    public void testGetOptions() {
        List<Option> options = group.getOptions();

        assertEquals(3, options.size());
        assertTrue(options.contains(OptionBuilder.withLongOpt("option1").create()));
        assertTrue(options.contains(OptionBuilder.withLongOpt("option2").create()));
        assertTrue(options.contains(OptionBuilder.withLongOpt("option3").create()));
        assertFalse(options.contains(OptionBuilder.withLongOpt("arg").createArg()));
    }

    @Test
    public void testGetAnonymous() {
        List<Option> anonymous = group.getAnonymous();

        assertEquals(1, anonymous.size());
        assertTrue(anonymous.contains(OptionBuilder.withLongOpt("arg").createArg()));
    }

    @Test(expected = NullPointerException.class)
    public void testFindOptionNull() {
        group.findOption(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindOptionInvalid() {
        group.findOption("invalid");
    }
}
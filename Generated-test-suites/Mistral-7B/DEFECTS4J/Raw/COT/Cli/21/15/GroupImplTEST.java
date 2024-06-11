package org.apache.commons.cli2.option;

import static org.junit.Assert.*;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.parser.BasicParser;
import org.apache.commons.cli2.util.HelpFormatterException;
import org.junit.Before;
import org.junit.Test;

public class GroupImplTest {
    private Options options;
    private Group group;

    @Before
    public void setUp() {
        List<Option> optionsList = new ArrayList<>();
        optionsList.add(new Option("a", "optionA", false, "test option A"));
        optionsList.add(new Option("b", "optionB", true, "test option B"));
        optionsList.add(new Option("c", "optionC", false, "test option C"));
        optionsList.add(new Option("d", "optionD", false, "test option D"));
        optionsList.add(new OptionBuilder("e").longOpt("optionE")
                .isRequired(false).hasArg(true).build());
        optionsList.add(new OptionBuilder("f").longOpt("optionF")
                .isRequired(false).hasArg(true).build());
        options = new Options();
        options.addGroup(new Group("group", "test group", optionsList));
        group = options.getOption("group");
    }

    @Test
    public void testCanProcess() {
        CommandLine commandLine = new CommandLine();
        assertFalse(group.canProcess(commandLine, null));
        commandLine.parse(options, new String[]{"--optionA", "--optionB", "--optionC", "--optionD", "--optionE=valueE"});
        assertTrue(group.canProcess(commandLine, "--optionB"));
        assertFalse(group.canProcess(commandLine, "--optionF"));
    }

    @Test(expected = OptionException.class)
    public void testProcessWithTooFewOptions() {
        CommandLine commandLine = new CommandLine();
        commandLine.parse(options, new String[]{"--optionA"});
        group.process(commandLine, commandLine.iterator());
    }

    @Test(expected = OptionException.class)
    public void testProcessWithTooManyOptions() {
        CommandLine commandLine = new CommandLine();
        commandLine.parse(options, new String[]{"--optionA", "--optionB", "--optionB", "--optionC", "--optionD", "--optionE=valueE", "--optionF=valueF"});
        group.process(commandLine, commandLine.iterator());
    }

    @Test(expected = OptionException.class)
    public void testProcessWithInvalidOption() {
        CommandLine commandLine = new CommandLine();
        commandLine.parse(options, new String[]{"--invalidOption"});
        try {
            group.process(commandLine, commandLine.iterator());
        } catch (OptionException e) {
            assertEquals("unknown option: invalidoption", e.getMessage());
            throw e;
        }
    }

    @Test
    public void testValidate() {
        CommandLine commandLine = new CommandLine();
        commandLine.parse(options, new String[]{});
        try {
            group.validate(commandLine);
            fail("Expected OptionException");
        } catch (OptionException e) {
            assertEquals("This group requires at least 1 valid option", e.getMessage());
        }

        commandLine = new CommandLine();
        commandLine.parse(options, new String[]{"--optionA"});
        group.validate(commandLine);

        commandLine = new CommandLine();
        commandLine.parse(options, new String[]{"--optionA", "--optionB", "--optionC", "--optionD"});
        assertFalse(group.validate(commandLine).hasMessages());
    }

    @Test
    public void testAppendUsage() {
        StringBuilder sb = new StringBuilder();
        group.appendUsage(sb, Collections.emptySet(), null);
        String expectedUsage = "group:\n" +
                "test group (group)\n" +
                "  This group requires at least 1 valid option\n" +
                "  optionA\n" +
                "      test option A (no argument)\n" +
                "  optionB\n" +
                "      test option B (optional, value required)\n" +
                "  optionC\n" +
                "      test option C (no argument)\n" +
                "  optionD\n" +
                "      test option D (no argument)\n" +
                "  optionE\n" +
                "      test option E (required, value required)\n" +
                "  optionF\n" +
                "      test option F (required, value required)\n";
        assertEquals(expectedUsage, sb.toString());
    }

    @Test
    public void testHelpLines() {
        HelpFormatter formatter = new HelpFormatter();
        List<HelpLine> helpLines = group.helpLines(0, Collections.emptySet(), null);
        HelpLine firstHelpLine = helpLines.get(0);
        assertEquals("group", firstHelpLine.getHeadText());
        assertEquals("test group (group)", firstHelpLine.getLongOpt());
        assertEquals("test group\nThis group requires at least 1 valid option\n", firstHelpLine.getHelp());
        assertEquals("[--optionA, --optionB, --optionC, --optionD, --optionE=VALUE, --optionF=VALUE]", firstHelpLine.getOptionsMetadata());

        HelpLine optionBHelpLine = null;
        for (HelpLine helpLine : helpLines) {
            if (helpLine.getLongOpt().equals("optionB")) {
                optionBHelpLine = helpLine;
                break;
            }
        }
        assertEquals(1, helpLines.size());
        assertEquals("optionB", optionBHelpLine.getLongOpt());
        assertEquals("test option B (optional, value required)", optionBHelpLine.getShortOpt());
        assertEquals("test option B", optionBHelpLine.getDescription());
        assertEquals("VALUE", optionBHelpLine.getArgName());
    }
}
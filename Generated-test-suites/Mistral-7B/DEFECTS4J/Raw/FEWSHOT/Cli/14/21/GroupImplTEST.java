package org.apache.commons.cli2.option;

import static org.junit.Assert.*;
import org.apache.commons.cli2.ArgparseArguments;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Group;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.OptionFactory;
import org.apache.commons.cli2.ParseException;
import org.junit.Test;

public class GroupImplTest {

    @Test
    public void testGroupImpl() throws ParseException {
        List<Option> options = new ArrayList<>();

        Option option1 = OptionBuilder.withLongOpt("option1")
                .withDescription("Option 1").create();
        options.add(option1);

        Option option2 = OptionBuilder.withLongOpt("option2")
                .withDescription("Option 2")
                .isRequired(true)
                .create(true);
        options.add(option2);

        Group group = new GroupImpl(options, "group", "Group of options", 1, 1);

        // test canProcess
        CommandLine commandLine = new ArgparseArguments();
        assertTrue(group.canProcess(commandLine, "option1"));
        assertTrue(group.canProcess(commandLine, "option2"));

        // test getPrefixes
        Set prefixes = group.getPrefixes();
        assertEquals(0, prefixes.size());

        // test getTriggers
        Set triggers = group.getTriggers();
        assertEquals(2, triggers.size());
        assertTrue(triggers.contains("option1"));
        assertTrue(triggers.contains("option2"));

        // test process
        commandLine.parse(group.getOptions().toArray(new String[0]));
        assertEquals(2, commandLine.getNOptions());
        assertEquals("option1", commandLine.getOptionNames().get(0));
        assertEquals("option2", commandLine.getOptionNames().get(1));

        // test validate
        commandLine = new ArgparseArguments();
        commandLine.parse(group.getOptions().toArray(new String[0]));
        group.validate(commandLine);

        // test getPreferredName
        assertEquals("group", group.getPreferredName());

        // test getDescription
        assertEquals("Group of options", group.getDescription());

        // test appendUsage
        StringBuffer buffer = new StringBuffer();
        Set helpSettings = new HashSet<>();
        group.appendUsage(buffer, helpSettings, null);
        assertEquals("group (Options:\n  --option1            Option 1\n  --option2         Option 2\n)", buffer.toString());

        // test helpLines
        List<HelpLine> helpLines = group.helpLines(0, helpSettings, null);
        assertEquals(2, helpLines.size());
        HelpLine helpLine1 = (HelpLine) helpLines.get(0);
        assertEquals("--option1", helpLine1.getOpt());
        assertEquals("Option 1", helpLine1.getLongOpt());
        assertEquals("Option 1", helpLine1.getDescription());
        HelpLine helpLine2 = (HelpLine) helpLines.get(1);
        assertEquals("--option2", helpLine2.getOpt());
        assertEquals("Option 2", helpLine2.getLongOpt());
        assertEquals("Option 2", helpLine2.getDescription());
    }
}
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.builder.ArgumentBuilder;
import org.apache.commons.cli2.builder.OptionBuilder;
import org.apache.commons.cli2.group.GroupImpl;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GroupImplTest {

    @Test
    public void testGroupImpl() {
        // Create a simple command line with some options and a group
        Options options = new Options();
        Option option1 = OptionBuilder.withLongOpt("option1")
                .withDescription("This is option 1")
                .isRequired(false)
                .create(options);
        Option option2 = OptionBuilder.withLongOpt("option2")
                .withDescription("This is option 2")
                .isRequired(false)
                .create(options);
        Option group = OptionBuilder.withLongOpt("group")
                .withDescription("This is the group")
                .createGroup(options);
        group.addOption(option1);
        group.addOption(option2);

        // Add an anonymous argument to the group
        Argument arg = ArgumentBuilder.withLongOpt("arg")
                .withDescription("This is an anonymous argument")
                .isRequired(false)
                .create(options);
        group.addArgument(arg);

        // Set minimum and maximum for the group
        group.setValue("group", 1, 2);

        // Create a command line with some arguments
        CommandLine commandLine = new CommandLine();
        commandLine.parse(options, new String[]{"--group", "--option1", "--option2", "--arg", "value"});

        // Test the group methods
        assertEquals("group", group.getPreferredName());
        assertEquals("This is the group", group.getDescription());
        assertEquals(1, group.getMinimum());
        assertEquals(2, group.getMaximum());
        assertEquals(2, group.getOptions().size());
        assertEquals(1, group.getAnonymous().size());

        // Test canProcess method
        assertEquals(true, group.canProcess(commandLine, "--option1"));
        assertEquals(true, group.canProcess(commandLine, "--option2"));
        assertEquals(true, group.canProcess(commandLine, "--arg"));
        assertEquals(false, group.canProcess(commandLine, "--nonexistent"));

        // Test process method
        assertEquals(1, commandLine.getArgs().size());
        group.process(commandLine, commandLine.listIterator());
        assertEquals(2, commandLine.getArgs().size());
        assertEquals("value", commandLine.getArgs().get(1).getValue());

        // Test validate method
        try {
            group.validate(commandLine);
        } catch (OptionException e) {
            fail("validate should not throw an exception");
        }

        // Test appendUsage method
        StringBuffer buffer = new StringBuffer();
        group.appendUsage(buffer, Collections.emptySet(), null);
        assertEquals("group:\n" +
                "This is the group\n" +
                "\n" +
                "Options:\n" +
                "=========\n" +
                "option1\n" +
                "This is option 1\n" +
                "\n" +
                "option2\n" +
                "This is option 2\n" +
                "\n" +
                "Anonymous arguments:\n" +
                "=====================\n" +
                "arg\n" +
                "This is an anonymous argument\n" +
                "----------------------------------------", buffer.toString());

        // Test helpLines method
        List<HelpLine> helpLines = group.helpLines(0, Collections.emptySet(), null);
        assertEquals(5, helpLines.size());
        assertEquals("group", helpLines.get(0).getHeadText());
        assertEquals("This is the group", helpLines.get(0).getShortText());
        assertEquals("", helpLines.get(0).getLongText());
        assertEquals("option1", helpLines.get(1).getHeadText());
        assertEquals("This is option 1", helpLines.get(1).getShortText());
        assertEquals("", helpLines.get(1).getLongText());
        assertEquals("option2", helpLines.get(2).getHeadText());
        assertEquals("This is option 2", helpLines.get(2).getShortText());
        assertEquals("", helpLines.get(2).getLongText());
        assertEquals("arg", helpLines.get(3).getHeadText());
        assertEquals("This is an anonymous argument", helpLines.get(3).getShortText());
        assertEquals("", helpLines.get(3).getLongText());
        assertEquals("group", helpLines.get(4).getHeadText());
        assertEquals("This is the group", helpLines.get(4).getShortText());
        assertEquals("Options:\n" +
                "=========\n" +
                "option1\n" +
                "This is option 1\n" +
                "\n" +
                "option2\n" +
                "This is option 2\n" +
                "\n" +
                "Anonymous arguments:\n" +
                "=====================\n" +
                "arg\n" +
                "This is an anonymous argument\n" +
                "----------------------------------------", helpLines.get(4).getLongText());
    }
}
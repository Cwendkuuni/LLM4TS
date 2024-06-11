import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.group.GroupImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GroupImplTest {

    private GroupImpl group;
    private Options options;
    private HelpFormatter formatter;

    @Before
    public void setup() {
        List<Option> optionsList = new ArrayList<>();
        options = new Options();
        this.group = new GroupImpl(optionsList, "testGroup", null, 1, 1);

        Option option1 = new Option("t", "test", false, "Test option");
        Option option2 = new Option("e", "example", true, "Example option");
        Option option3 = new Option("h", "help", false, "Print help");

        optionsList.add(option1);
        optionsList.add(option2);
        optionsList.add(option3);

        formatter = new HelpFormatter();
    }

    @Test
    public void testGroupCanProcessWithValidOptions() {
        CommandLine commandLine = new CommandLine("test");
        commandLine.parseOptions(group.getOptions());

        Assert.assertTrue(group.canProcess(commandLine, "test"));
    }

    @Test(expected = OptionException.class)
    public void testGroupCannotProcessWithInvalidOption() {
        CommandLine commandLine = new CommandLine("invalid");
        commandLine.parseOptions(group.getOptions());

        group.process(commandLine, commandLine.iterator());
    }

    @Test
    public void testGroupProcessWithValidOptions() {
        CommandLine commandLine = new CommandLine("test");
        commandLine.parseOptions(group.getOptions());

        group.process(commandLine, commandLine.iterator());
    }

    @Test
    public void testGroupProcessWithAnonymousArguments() {
        CommandLine commandLine = new CommandLine("test");
        commandLine.parseOptions(group.getOptions());

        commandLine.addOption("test", true, "Test option");
        commandLine.addOption("example", "value");

        group.process(commandLine, commandLine.iterator());
    }

    @Test(expected = OptionException.class)
    public void testGroupValidateWithTooManyOptions() {
        CommandLine commandLine = new CommandLine("test");
        commandLine.parseOptions(group.getOptions());

        commandLine.addOption("test", true, "Test option");
        commandLine.addOption("test", true, "Test option");

        group.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testGroupValidateWithTooFewOptions() {
        CommandLine commandLine = new CommandLine("test");
        commandLine.parseOptions(group.getOptions());

        group.validate(commandLine);
    }

    @Test
    public void testGroupAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        group.appendUsage(buffer, null, null);

        String expectedUsage = "-test                             Test option\n";
        String actualUsage = buffer.toString();

        Assert.assertEquals(expectedUsage, actualUsage);
    }

    @Test
    public void testGroupHelpLines() {
        List<HelpLine> helpLines = group.helpLines(1, null, null);

        String expectedHelpLines = "-test                             Test option\n";
        String actualHelpLines = getHelpLinesString(helpLines);

        Assert.assertEquals(expectedHelpLines, actualHelpLines);
    }

    private String getHelpLinesString(List<HelpLine> helpLines) {
        StringBuilder stringBuilder = new StringBuilder();
        for (HelpLine helpLine : helpLines) {
            stringBuilder.append(helpLine.getHelp());
            stringBuilder.append(System.lineSeparator());
        }
        return stringBuilder.toString();
    }
}
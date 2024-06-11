import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Options;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class GroupImplTest {

    @Test
    public void testGroupImpl() {
        // Given
        final List<Option> options = new ArrayList<>();
        options.add(Option.builder("a")
                .longOpt("longOptionA")
                .required(false)
                .build());
        options.add(Option.builder("b")
                .longOpt("longOptionB")
                .required(true)
                .build());
        options.add(Option.builder("c")
                .longOpt("longOptionC")
                .required(false)
                .build());

        final GroupImpl group = new GroupImpl(options, "group", "Test Group", 1, 1);

        // When processing valid command line arguments
        final CommandLine commandLine = new CommandLine();
        commandLine.parse(group.getParser().parse(new String[]{"-a", "-b", "--longOptionB=value", "-c"}));
        group.process(commandLine, commandLine.argList.listIterator());

        // Then check that all options have been processed correctly
        Assert.assertEquals("value", commandLine.getOptionValue("longOptionB"));
        Assert.assertNull(commandLine.getOptionValue("longOptionA"));
        Assert.assertNull(commandLine.getOptionValue("longOptionC"));

        // Given
        final List<Argument> anonymousArguments = new ArrayList<>();
        anonymousArguments.add(Argument.builder()
                .name("anonymousArgumentX")
                .required(false)
                .build());

        final GroupImpl groupWithAnonymousArguments = new GroupImpl(options, "group", "Test Group", 1, 1, anonymousArguments);

        // When processing valid command line arguments with anonymous arguments
        final CommandLine commandLineWithAnonymousArguments = new CommandLine();
        commandLineWithAnonymousArguments.parse(groupWithAnonymousArguments.getParser().parse(new String[]{"-a", "-b", "--longOptionB=value", "anonymousArgumentX=valueX"}));
        groupWithAnonymousArguments.process(commandLineWithAnonymousArguments, commandLineWithAnonymousArguments.argList.listIterator());

        // Then check that all options and anonymous arguments have been processed correctly
        Assert.assertEquals("value", commandLineWithAnonymousArguments.getOptionValue("longOptionB"));
        Assert.assertNull(commandLineWithAnonymousArguments.getOptionValue("longOptionA"));
        Assert.assertNull(commandLineWithAnonymousArguments.getOptionValue("longOptionC"));
        Assert.assertEquals("valueX", commandLineWithAnonymousArguments.getOptionValue("anonymousArgumentX"));

        // Given
        final List<Option> optionsWithMaximum = new ArrayList<>();
        optionsWithMaximum.addAll(options);
        optionsWithMaximum.add(Option.builder("d")
                .longOpt("longOptionD")
                .required(false)
                .build());

        final GroupImpl groupWithMaximum = new GroupImpl(optionsWithMaximum, "group", "Test Group", 1, 2);

        // When processing a command line with more options than allowed
        final CommandLine commandLineWithMaximum = new CommandLine();
        commandLineWithMaximum.parse(groupWithMaximum.getParser().parse(new String[]{"-a", "-b", "--longOptionB=value", "-c", "--longOptionD=valueD"}));

        // Then an OptionException should be thrown
        try {
            groupWithMaximum.process(commandLineWithMaximum, commandLineWithMaximum.argList.listIterator());
        } catch (OptionException e) {
            Assert.assertEquals("Unexpected option: -d", e.getMessage());
        }

        // Given
        final List<Option> optionsWithMinimum = new ArrayList<>();
        optionsWithMinimum.addAll(options);
        optionsWithMinimum.remove(optionsWithMinimum.size() - 1); // remove the last option

        final GroupImpl groupWithMinimum = new GroupImpl(optionsWithMinimum, "group", "Test Group", 1, 0);

        // When processing a command line with less options than required
        final CommandLine commandLineWithMinimum = new CommandLine();
        commandLineWithMinimum.parse(groupWithMinimum.getParser().parse(new String[]{"-a"}));

        // Then an OptionException should be thrown
        try {
            groupWithMinimum.process(commandLineWithMinimum, commandLineWithMinimum.argList.listIterator());
        } catch (OptionException e) {
            Assert.assertEquals("Missing option: -b", e.getMessage());
        }

        // Given
        final List<Option> optionsWithNoArgument = new ArrayList<>();
        optionsWithNoArgument.add(Option.builder("e")
                .longOpt("longOptionE")
                .required(false)
                .build());

        final GroupImpl groupWithNoArgument = new GroupImpl(optionsWithNoArgument, "group", "Test Group", 1, 1);

        // When processing a command line without the required argument for this option
        final CommandLine commandLineWithNoArgument = new CommandLine();
        commandLineWithNoArgument.parse(groupWithNoArgument.getParser().parse(new String[]{"--longOptionE"}));

        // Then an OptionException should be thrown
        try {
            groupWithNoArgument.process(commandLineWithNoArgument, commandLineWithNoArgument.argList.listIterator());
        } catch (OptionException e) {
            Assert.assertEquals("Required option '-e' is missing", e.getMessage());
        }

        // Given
        final List<Option> optionsWithArgument = new ArrayList<>();
        optionsWithArgument.add(Option.builder("f")
                .longOpt("longOptionF")
                .required(false)
                .argName("argNameF")
                .build());

        final GroupImpl groupWithArgument = new GroupImpl(optionsWithArgument, "group", "Test Group", 1, 1);

        // When processing a command line with the required argument for this option
        final CommandLine commandLineWithArgument = new CommandLine();
        commandLineWithArgument.parse(groupWithArgument.getParser().parse(new String[]{"--longOptionF=argNameFValue"}));

        // Then check that the argument has been processed correctly
        Assert.assertEquals("argNameFValue", commandLineWithArgument.getOptionValue("longOptionF"));
    }

    @Test
    public void testHelp() {
        // Given
        final List<Option> options = new ArrayList<>();
        options.add(Option.builder("a")
                .longOpt("longOptionA")
                .required(false)
                .build());
        options.add(Option.builder("b")
                .longOpt("longOptionB")
                .required(true)
                .build());
        options.add(Option.builder("c")
                .longOpt("longOptionC")
                .required(false)
                .build());

        final GroupImpl group = new GroupImpl(options, "group", "Test Group", 1, 1);

        // When generating help
        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("TestApp", group.getParser().getDescription(), true, "1.0", null);

        // Then the help should contain all the options
        final String help = formatter.getHelpText();
        Assert.assertTrue(help.contains("--longOptionA"));
        Assert.assertTrue(help.contains("--longOptionB"));
        Assert.assertTrue(help.contains("--longOptionC"));
    }
}
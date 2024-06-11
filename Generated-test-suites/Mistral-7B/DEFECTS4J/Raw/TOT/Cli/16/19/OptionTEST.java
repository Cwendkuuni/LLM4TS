import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OPTION_TYPE;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OptionTest {

    @Test
    public void testOptionProcess() {
        Option option = Option.builder("o")
                .longOpt("option")
                .hasArg(true)
                .required(false)
                .build();

        WriteableCommandLine commandLine = new CommandLine();
        ListIterator<String> args = Arrays.asList("java", "MyApplication", "--option", "test").listIterator();

        option.process(commandLine, args);

        Assert.assertEquals("test", commandLine.getOptionValue("option"));
    }

    @Test
    public void testOptionDefaults() {
        Option option = Option.builder("o")
                .longOpt("option")
                .hasArg(true)
                .required(false)
                .build();

        WriteableCommandLine commandLine = new CommandLine();
        option.defaults(commandLine);

        Assert.assertNull(commandLine.getOptionValue("option"));
    }

    @Test(expected = OptionException.class)
    public void testOptionProcessInvalidArgument() {
        Option option = Option.builder("o")
                .longOpt("option")
                .hasArg(true)
                .required(false)
                .build();

        WriteableCommandLine commandLine = new CommandLine();
        ListIterator<String> args = Arrays.asList("java", "MyApplication", "--option", "invalid").listIterator();

        option.process(commandLine, args);
    }

    @Test
    public void testOptionCanProcess() {
        Option option = Option.builder("o")
                .longOpt("option")
                .hasArg(true)
                .required(false)
                .build();

        WriteableCommandLine commandLine = new CommandLine();

        Assert.assertTrue(option.canProcess(commandLine, "--option", Arrays.asList("test").listIterator()));
    }

    @Test
    public void testOptionCanProcessWithArguments() {
        Option option = Option.builder("o")
                .longOpt("option")
                .hasArg(true)
                .required(false)
                .build();

        WriteableCommandLine commandLine = new CommandLine();

        ListIterator<String> args = Arrays.asList("java", "MyApplication", "--option", "test arg").listIterator();

        Assert.assertTrue(option.canProcess(commandLine, "--option", args));
        Assert.assertEquals("test arg", commandLine.getOptionValue("option"));
    }

    @Test
    public void testOptionGetTriggers() {
        Option option = Option.builder("o")
                .longOpt("option")
                .hasArg(true)
                .required(false)
                .build();

        Set triggers = option.getTriggers();

        Assert.assertEquals(1, triggers.size());
        Assert.assertEquals("--option", triggers.iterator().next().toString());
    }

    @Test
    public void testOptionGetPrefixes() {
        Option option = Option.builder("o")
                .longOpt("option")
                .hasArg(true)
                .required(false)
                .build();

        Set prefixes = option.getPrefixes();

        Assert.assertEquals(1, prefixes.size());
        Assert.assertEquals("--", prefixes.iterator().next().toString());
    }

    @Test(expected = OptionException.class)
    public void testOptionValidateInvalidCommandLine() {
        Option option = Option.builder("o")
                .longOpt("option")
                .hasArg(true)
                .required(false)
                .build();

        WriteableCommandLine commandLine = new CommandLine();

        option.validate(commandLine);
    }

    @Test
    public void testOptionValidateValidCommandLine() {
        Option option = Option.builder("o")
                .longOpt("option")
                .hasArg(true)
                .required(false)
                .build();

        WriteableCommandLine commandLine = new CommandLine();
        commandLine.putOption("option", "test");

        option.validate(commandLine);
    }

    @Test
    public void testOptionHelpLines() {
        Option option = Option.builder("o")
                .longOpt("option")
                .hasArg(true)
                .required(false)
                .build();

        List helpLines = option.helpLines(1, new HashSet(), null);

        Assert.assertEquals(1, helpLines.size());
        Assert.assertTrue(helpLines.get(0) instanceof HelpLine);

        HelpLine helpLine = (HelpLine) helpLines.get(0);
        Assert.assertEquals("--option <argument>", helpLine.getLongOpt());
        Assert.assertEquals("Option describing something", helpLine.getShortOpt());
        Assert.assertEquals("Option description", helpLine.getDescription());
    }

    @Test
    public void testOptionAppendUsage() {
        Option option = Option.builder("o")
                .longOpt("option")
                .hasArg(true)
                .required(false)
                .build();

        StringBuffer buffer = new StringBuffer();
        option.appendUsage(buffer, new HashSet(), null);

        Assert.assertEquals("--option <argument>\n", buffer.toString());
    }

    @Test
    public void testOptionGetPreferredName() {
        Option option = Option.builder("o")
                .longOpt("option")
                .hasArg(true)
                .required(false)
                .build();

        String preferredName = option.getPreferredName();

        Assert.assertEquals("o", preferredName);
    }

    @Test
    public void testOptionGetDescription() {
        Option option = Option.builder("o")
                .longOpt("option")
                .hasArg(true)
                .required(false)
                .build();

        String description = option.getDescription();

        Assert.assertNotNull(description);
    }

    @Test
    public void testOptiongetId() {
        Option option = Option.builder("o")
                .longOpt("option")
                .hasArg(true)
                .required(false)
                .build();

        int id = option.getId();

        Assert.assertEquals(OPTION_TYPE.UNDEFINED.getValue(), id);
    }

    @Test
    public void testOptionFindOption() {
        Option parentOption = Option.builder("p")
                .longOpt("parent")
                .hasArg(false)
                .required(false)
                .build();

        Option childOption = Option.builder("c")
                .longOpt("child")
                .hasArg(true)
                .required(false)
                .build();

        parentOption.addOption(childOption);

        Option foundOption = parentOption.findOption("c");

        Assert.assertNotNull(foundOption);
        Assert.assertEquals(childOption, foundOption);
    }

    @Test
    public void testOptionIsRequired() {
        Option option = Option.builder("o")
                .longOpt("option")
                .hasArg(true)
                .required(true)
                .build();

        Assert.assertTrue(option.isRequired());
    }

    @Test
    public void testOptionHasParent() {
        Option parentOption = Option.builder("p")
                .longOpt("parent")
                .hasArg(false)
                .required(false)
                .build();

        Option childOption = Option.builder("c")
                .longOpt("child")
                .hasArg(true)
                .required(false)
                .build();

        parentOption.addOption(childOption);

        Assert.assertNull(childOption.getParent());

        childOption.setParent(parentOption);

        Assert.assertEquals(parentOption, childOption.getParent());
    }
}
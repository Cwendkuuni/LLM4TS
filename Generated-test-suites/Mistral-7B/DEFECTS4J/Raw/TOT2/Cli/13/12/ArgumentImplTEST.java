import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.TestUtils;
import org.junit.Assert;
import org.junit.Test;

public class ArgumentImplTest {

    @Test
    public void testArgumentWithMinimumAndMaximumValues() {
        Option option = new OptionBuilder()
                .withLongOpt("example")
                .withDescription("An example option with minimum and maximum values")
                .isRequired(false)
                .withArgName("value")
                .hasArg(true)
                .withMinValues(1)
                .withMaxValues(2)
                .create();

        CommandLine cmd = new CommandLine();
        HelpFormatter formatter = new HelpFormatter();

        String[] args = new String[]{"--example", "value1", "value2"};
        TestUtils.parseArguments(args, option, cmd);

        Assert.assertEquals("value1", cmd.getOptionValue(option, "value1"));
        Assert.assertEquals("value2", cmd.getOptionValue(option, "value2"));

        formatter.printHelp("test", "Test program", true);
        System.out.println(formatter.getHelpText());
    }

    @Test(expected = OptionException.class)
    public void testArgumentWithLessThanMinimumValues() {
        Option option = new OptionBuilder()
                .withLongOpt("example")
                .withDescription("An example option with minimum and maximum values")
                .isRequired(false)
                .withArgName("value")
                .hasArg(true)
                .withMinValues(2)
                .create();

        CommandLine cmd = new CommandLine();
        String[] args = new String[]{"--example", "value1"};

        TestUtils.parseArguments(args, option, cmd);

        // OptionException should be thrown
    }

    @Test(expected = OptionException.class)
    public void testArgumentWithMoreThanMaximumValues() {
        Option option = new OptionBuilder()
                .withLongOpt("example")
                .withDescription("An example option with minimum and maximum values")
                .isRequired(false)
                .withArgName("value")
                .hasArg(true)
                .withMinValues(0)
                .withMaxValues(1)
                .create();

        CommandLine cmd = new CommandLine();
        String[] args = new String[]{"--example", "value1", "value2"};

        TestUtils.parseArguments(args, option, cmd);

        // OptionException should be thrown
    }
}
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.parse.ParseResult;
import org.junit.Assert;
import org.junit.Test;

public class ArgumentImplTest {

    @Test
    public void testArgumentWithValidValues() {
        Argument argument = new ArgumentImpl("argName", "description", 1, 3, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, DEFAULT_CONSUME_REMAINING, null, 0);

        CommandLine cmd = new CommandLine();

        Option option = OptionBuilder.withLongOpt("argName")
                .withDescription("description")
                .withArgument(argument)
                .create(false);

        cmd.parse(OptionParser.parseArguments(new String[]{"--argName", "value1", "value2"}));

        Assert.assertEquals("value1", cmd.getValue("argName", 0));
        Assert.assertEquals("value2", cmd.getValue("argName", 1));
        Assert.assertNull(cmd.getValue("argName", 2));
    }

    @Test
    public void testArgumentWithMinimumValues() {
        Argument argument = new ArgumentImpl("argName", "description", 1, 3, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, DEFAULT_CONSUME_REMAINING, null, 0);

        CommandLine cmd = new CommandLine();

        Option option = OptionBuilder.withLongOpt("argName")
                .withDescription("description")
                .withArgument(argument)
                .create(false);

        cmd.parse(OptionParser.parseArguments(new String[]{"--argName"}));

        Assert.assertEquals(null, cmd.getValue("argName", 0));
        Assert.assertEquals(null, cmd.getValue("argName", 1));
    }

    @Test(expected = OptionException.class)
    public void testArgumentWithTooManyValues() {
        Argument argument = new ArgumentImpl("argName", "description", 1, 3, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, DEFAULT_CONSUME_REMAINING, null, 0);

        CommandLine cmd = new CommandLine();

        Option option = OptionBuilder.withLongOpt("argName")
                .withDescription("description")
                .withArgument(argument)
                .create(false);

        cmd.parse(OptionParser.parseArguments(new String[]{"--argName", "value1", "value2", "value3"}));

        // An OptionException should be thrown
    }

    @Test(expected = OptionException.class)
    public void testArgumentWithInvalidValue() {
        Argument argument = new ArgumentImpl("argName", "description", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, DEFAULT_CONSUME_REMAINING, null, 0);

        CommandLine cmd = new CommandLine();

        Option option = OptionBuilder.withLongOpt("argName")
                .withDescription("description")
                .withArgument(argument)
                .create(false);

        cmd.parse(OptionParser.parseArguments(new String[]{"--argName", "invalidValue"}));

        // An OptionException should be thrown
    }

    @Test
    public void testArgumentWithConsumingOption() {
        Argument argument = new ArgumentImpl("argName", "description", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, DEFAULT_CONSUME_REMAINING, null, 0);

        CommandLine cmd = new CommandLine();

        Option option = OptionBuilder.withLongOpt("argName")
                .withDescription("description")
                .withArgument(argument)
                .create(false);

        cmd.parse(OptionParser.parseArguments(new String[]{"--argName", "--consumingOption"}));

        // No values should be set for the argument
        Assert.assertNull(cmd.getValue("argName", 0));
    }
}
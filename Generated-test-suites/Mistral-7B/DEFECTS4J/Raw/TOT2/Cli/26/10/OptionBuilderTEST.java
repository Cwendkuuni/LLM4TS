import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class OptionBuilderTest {

    @Test
    public void testOptionBuilderWithBasicUsage() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("optionA")
                .isRequired()
                .hasArg();
        Option option = optionBuilder.create('A');

        Options options = new Options();
        options.addOption(option);

        String[] args = {"-A", "testValue"};
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        Assert.assertNotNull(cmd.getOptionValue("optionA"));
        Assert.assertEquals("testValue", cmd.getOptionValue("optionA"));
    }

    @Test
    public void testOptionBuilderWithValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("optionB")
                .isRequired()
                .hasArgs(3)
                .withValueSeparator(':');
        Option option = optionBuilder.create('B');

        Options options = new Options();
        options.addOption(option);

        String[] args = {"-B", "testValue1:", "testValue2:", "testValue3:"};
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        Assert.assertArrayEquals(new String[]{"testValue1", "testValue2", "testValue3"}, cmd.getOptionValues("optionB"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionBuilderWithInvalidOptionChar() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("optionC")
                .isRequired()
                .hasArg();
        Option option = optionBuilder.create('%'); // invalid option character

        Options options = new Options();
        options.addOption(option);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionBuilderWithMissingLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        Option option = optionBuilder.create();

        Options options = new Options();
        options.addOption(option);
    }
}
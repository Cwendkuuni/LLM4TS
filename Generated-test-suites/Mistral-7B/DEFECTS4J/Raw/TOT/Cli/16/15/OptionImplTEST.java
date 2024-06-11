import org.apache.commons.cli2.CommandLineParser;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionFactory;
import org.apache.commons.cli2.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class OptionImplTest {

    @Test
    public void testOptionWithNoPrefix() throws ParseException {
        Option option = OptionFactory.builder()
                .withLongOpt("exampleOption")
                .withValueSeparator('=')
                .withDescription("This is an example option")
                .build();

        CommandLineParser parser = new DefaultParser();
        WriteableCommandLine commandLine = new DefaultCommandLine();

        parser.parse(new String[]{"--exampleOption"}, commandLine);

        Assert.assertEquals("exampleOption", option.getOpt());
        Assert.assertNull(commandLine.getOptionValues("exampleOption"));
    }

    @Test
    public void testOptionWithSingleValue() throws ParseException {
        Option option = OptionFactory.builder()
                .withLongOpt("exampleOption")
                .withValueSeparator('=')
                .withDescription("This is an example option")
                .isRequired(false)
                .build();

        CommandLineParser parser = new DefaultParser();
        WriteableCommandLine commandLine = new DefaultCommandLine();

        parser.parse(new String[]{"--exampleOption=test"}, commandLine);

        Assert.assertEquals("exampleOption", option.getOpt());
        Assert.assertEquals("test", commandLine.getOptionValues("exampleOption")[0]);
    }

    @Test(expected = ParseException.class)
    public void testOptionWithMissingValue() throws ParseException {
        Option option = OptionFactory.builder()
                .withLongOpt("exampleOption")
                .withValueSeparator('=')
                .withDescription("This is an example option")
                .isRequired(false)
                .build();

        CommandLineParser parser = new DefaultParser();
        WriteableCommandLine commandLine = new DefaultCommandLine();

        parser.parse(new String[]{"--exampleOption"}, commandLine);

        parser.parse(new String[]{"--exampleOption"}, commandLine);

        option.validate(commandLine);
    }

    @Test(expected = ParseException.class)
    public void testOptionWithMissingPrefix() throws ParseException {
        Option option = OptionFactory.builder()
                .withLongOpt("exampleOption")
                .withDescription("This is an example option")
                .build();

        CommandLineParser parser = new DefaultParser();
        WriteableCommandLine commandLine = new DefaultCommandLine();

        parser.parse(new String[]{"exampleOption"}, commandLine);

        option.validate(commandLine);
    }

    @Test
    public void testOptionWithMultipleValues() throws ParseException {
        Option option = OptionFactory.builder()
                .withLongOpt("exampleOption")
                .withValueSeparator(',')
                .withDescription("This is an example option")
                .isRequired(false)
                .build();

        CommandLineParser parser = new DefaultParser();
        WriteableCommandLine commandLine = new DefaultCommandLine();

        parser.parse(new String[]{"--exampleOption", "test1", "test2"}, commandLine);

        Assert.assertEquals("exampleOption", option.getOpt());
        Assert.assertArrayEquals(new String[]{"test1", "test2"}, commandLine.getOptionValues("exampleOption"));
    }
}
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testSimpleUsage() throws ParseException {
        String[] args = new String[]{"-a", "1", "--optionB", "2"};
        Options options = new Options();
        options.addOption("a", true, "option A");
        options.addOption("b", true, "option B");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        Assert.assertEquals(true, cmd.hasOption("a"));
        Assert.assertEquals(true, cmd.hasOption("b"));
        Assert.assertEquals("1", cmd.getOptionValue("a"));
        Assert.assertEquals("2", cmd.getOptionValue("b"));
        Assert.assertNull(cmd.getOptionValue("nonExistingOption"));
    }

    @Test(expected = ParseException.class)
    public void testMissingRequiredOption() throws ParseException {
        String[] args = new String[]{"--optionA"};
        Options options = new Options();
        options.addOption("a", true, "option A");
        options.addRequiredOption("b", true, "option B");

        CommandLineParser parser = new DefaultParser();
        parser.parse(options, args);
    }

    @Test
    public void testMultiValueOption() throws ParseException {
        String[] args = new String[]{"-c", "file1.txt", "-c", "file2.txt"};
        Options options = new Options();
        options.addOption("c", true, "input file", 1);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        Assert.assertEquals(2, cmd.getNumOptions());
        List<String> values = cmd.getOptionValues("c");
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("file1.txt", values.get(0));
        Assert.assertEquals("file2.txt", values.get(1));
    }

    @Test(expected = ParseException.class)
    public void testInvalidOptionSyntax() throws ParseException {
        String[] args = new String[]{"--badOption"};
        Options options = new Options();

        CommandLineParser parser = new DefaultParser();
        parser.parse(options, args);
    }

    @Test
    public void testParsingProperties() throws ParseException {
        String[] args = new String[]{"--prop=key=value"};
        Properties props = new Properties();
        Options options = new Options();
        options.addOption("prop", true, true, "property");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, props);

        Assert.assertEquals("value", cmd.getOptionValue("prop"));
        Assert.assertEquals(props.size(), 1);
        Assert.assertEquals("key=value", props.getProperty("prop"));
    }
}
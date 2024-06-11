import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest
{
    @Test
    public void testParse() throws ParseException
    {
        String[] args = new String[]{"-a", "--optionB", "valueB", "-c", "--optionD", "valueD", "--", "arg1", "arg2"};

        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C");
        options.addOption("d", true, "Option D");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, args, null, false);

        Assert.assertEquals(args.length, cmd.getArgs().size());
        for (int i = 0; i < args.length; i++)
        {
            Assert.assertEquals(args[i], cmd.getArgs().get(i));
        }

        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertTrue(cmd.hasOption("b"));
        Assert.assertTrue(cmd.hasOption("c"));
        Assert.assertTrue(cmd.hasOption("d"));
        Assert.assertEquals("valueB", cmd.getOptionValue("b"));
        Assert.assertEquals("valueD", cmd.getOptionValue("d"));
    }

    @Test(expected = ParseException.class)
    public void testParseMissingOption() throws ParseException
    {
        String[] args = new String[]{"--unknownOption"};

        Options options = new Options();
        options.addOption("a", true, "Option A");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        parser.parse(options, args, null, false);
    }

    @Test(expected = ParseException.class)
    public void testParseMissingValue() throws ParseException
    {
        String[] args = new String[]{"-a", "--optionB"};

        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        parser.parse(options, args, null, false);
    }

    @Test(expected = MissingOptionException.class)
    public void testParseMissingRequiredOption() throws ParseException
    {
        String[] args = new String[]{"-c"};

        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("c", true, "Option C").required(true);

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        parser.parse(options, args, null, false);
    }

    @Test(expected = MissingOptionException.class)
    public void testParseMissingRequiredGroupOption() throws ParseException
    {
        String[] args = new String[]{"-a"};

        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "Group");
        group.setRequired(true);
        group.addOption("c", true, "Option C");
        options.addOption("a", true, "Option A");
        options.addOptionGroup(group);

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        parser.parse(options, args, null, false);
    }

    @Test
    public void testParseWithProperties() throws ParseException
    {
        String[] args = new String[]{"--optionA=valueA", "--optionB=valueB"};

        Properties properties = new Properties();
        properties.setProperty("optionA", "valueA");
        properties.setProperty("optionB", "valueB");

        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, args, properties, false);

        Assert.assertEquals(args.length, cmd.getArgs().size());
        for (int i = 0; i < args.length; i++)
        {
            Assert.assertEquals(args[i], cmd.getArgs().get(i));
        }

        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertTrue(cmd.hasOption("b"));
        Assert.assertEquals("valueA", cmd.getOptionValue("a"));
        Assert.assertEquals("valueB", cmd.getOptionValue("b"));
    }
}
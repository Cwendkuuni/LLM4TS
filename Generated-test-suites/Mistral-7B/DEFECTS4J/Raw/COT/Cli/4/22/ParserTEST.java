import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ParserTest {

    @Test
    public void testParse() throws Exception {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");
        options.addOption("c", 3, "Option C");
        options.addOption("d", 4, "Option D");

        Parser parser = new DefaultParser();
        String[] args = {"-a", "--b", "3", "--c", "1", "--d"};
        CommandLine cmd = parser.parse(options, args, null);

        Assert.assertEquals(args.length, cmd.getArgs().size());
        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertTrue(cmd.getOptionValue("a").equals("true"));
        Assert.assertEquals("3", cmd.getOptionValue("b"));
        Assert.assertEquals("1", cmd.getOptionValue("c"));
        Assert.assertEquals("4", cmd.getOptionValue("d"));
    }

    @Test(expected = MissingArgumentException.class)
    public void testMissingArgument() throws Exception {
        Options options = new Options();
        options.addOption("a", 1, "Option A");

        Parser parser = new DefaultParser();
        String[] args = {"-a"};
        parser.parse(options, args, null);
    }

    @Test(expected = MissingOptionException.class)
    public void testMissingOption() throws Exception {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        Parser parser = new DefaultParser();
        String[] args = {"--b"};
        parser.parse(options, args, null);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testUnrecognizedOption() throws Exception {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        Parser parser = new DefaultParser();
        String[] args = {"--x"};
        parser.parse(options, args, null);
    }

    @Test
    public void testParseWithProperties() throws Exception {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");

        Properties properties = new Properties();
        properties.put("b", "2");
        properties.put("a", "true");

        Parser parser = new DefaultParser();
        String[] args = {};
        CommandLine cmd = parser.parse(options, args, properties);

        Assert.assertEquals(args.length + 1, cmd.getArgs().size());
        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertTrue(cmd.getOptionValue("a").equals("true"));
        Assert.assertEquals("2", cmd.getOptionValue("b"));
    }

    @Test
    public void testParseWithStopAtNonOption() throws Exception {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");

        Parser parser = new DefaultParser();
        String[] args = {"--a", "1", "--b", "2", "nonOption"};
        CommandLine cmd = parser.parse(options, args, null, true);

        Assert.assertEquals(args.length - 1, cmd.getArgs().size());
        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertTrue(cmd.getOptionValue("a").equals("true"));
        Assert.assertEquals("1", cmd.getOptionValue("b"));
        Assert.assertEquals(Arrays.asList("nonOption"), cmd.getArgs());
    }

    @Test
    public void testParseWithRequiredOptions() throws Exception {
        Options options = new Options();
        options.addRequiredOption("a", true, "Option A");
        options.addRequiredOption("b", true, "Option B");

        Parser parser = new DefaultParser();
        String[] args = {"--a", "1", "--b", "2"};
        CommandLine cmd = parser.parse(options, args, null);

        Assert.assertEquals(0, options.getRequiredOptions().size());
    }

    @Test(expected = MissingOptionException.class)
    public void testMissingRequiredOptions() throws Exception {
        Options options = new Options();
        options.addRequiredOption("a", true, "Option A");
        options.addRequiredOption("b", true, "Option B");

        Parser parser = new DefaultParser();
        String[] args = {};
        parser.parse(options, args, null);
    }
}
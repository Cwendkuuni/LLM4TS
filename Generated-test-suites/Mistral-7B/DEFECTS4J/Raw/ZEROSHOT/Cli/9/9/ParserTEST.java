import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ParserTest {

    private Options options;
    private Parser parser;

    @Before
    public void setup() {
        options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");
        options.addOption("c", 3, "Option C");
        options.addOption("d", 4, "Option D");
        options.addRequiredOption("e", true, "Option E");
        options.addOptionGroup(new OptionGroup("g", "Group",
                Arrays.asList(new Option("h", false, "Option H"),
                        new Option("i", true, "Option I"))));
    }

    @Test
    public void testParse() throws ParseException {
        String[] arguments = new String[]{"-a", "--b", "1", "--c", "3", "--d", "4", "--e", "--g", "--h", "--i", "arg1", "arg2"};
        Parser parser = new TestParser();
        parser.setOptions(options);

        CommandLine cmd = parser.parse(arguments);

        Assert.assertEquals(8, cmd.getArgs().size());
        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertTrue(cmd.hasOption("b"));
        Assert.assertEquals(1, cmd.getOptionValue("b").length());
        Assert.assertTrue(cmd.hasOption("c"));
        Assert.assertEquals(3, cmd.getOptionValue("c").length());
        Assert.assertTrue(cmd.hasOption("d"));
        Assert.assertEquals(4, cmd.getOptionValue("d").length());
        Assert.assertTrue(cmd.hasOption("e"));
        Assert.assertTrue(cmd.hasOption("g"));
        Assert.assertTrue(cmd.getOptionValue("g").isSet("h"));
        Assert.assertTrue(cmd.getOptionValue("g").isSet("i"));
        Assert.assertNull(cmd.getOptionValue("i"));
        Assert.assertNull(cmd.getOptionValue("j"));
    }

    @Test(expected = ParseException.class)
    public void testMissingOption() throws ParseException {
        String[] arguments = new String[]{"arg1", "arg2"};
        Parser parser = new TestParser();
        parser.setOptions(options);

        parser.parse(arguments);

        Assert.fail();
    }

    @Test(expected = MissingOptionException.class)
    public void testMissingRequiredOption() throws ParseException {
        String[] arguments = new String[]{"--c", "--d"};
        Parser parser = new TestParser();
        parser.setOptions(options);

        parser.parse(arguments);

        Assert.fail();
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testUnrecognizedOption() throws ParseException {
        String[] arguments = new String[]{"--j", "arg1", "arg2"};
        Parser parser = new TestParser();
        parser.setOptions(options);

        parser.parse(arguments);

        Assert.fail();
    }
}

public class TestParser extends Parser {

    @Override
    protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
        return arguments;
    }
}

import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.cli.*;

public class ParserTest {

    @Test
    public void testParse1() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "test option a");
        options.addOption("b", true, "test option b");

        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        String[] args = {"-a", "-b", "value"};
        CommandLine cmd = parser.parse(options, args);

        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertTrue(cmd.hasOption("b"));
        Assert.assertEquals("value", cmd.getOptionValue("b"));
    }

    @Test
    public void testParse2() throws ParseException {
        Options options = new Options();
        options.addRequiredOption("c", "test option c");
        options.addOption("d", false, "test option d");

        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        String[] args = {"-c", "value"};
        CommandLine cmd = parser.parse(options, args);

        Assert.assertTrue(cmd.hasOption("c"));
        Assert.assertFalse(cmd.hasOption("d"));
        Assert.assertEquals("value", cmd.getOptionValue("c"));
    }

    @Test
    public void testParse3() throws ParseException {
        Options options = new Options();
        options.addOption("e", false, "test option e");
        options.addOption("f", false, "test option f");

        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[]{"-e"};
            }
        };

        String[] args = {"-f"};
        CommandLine cmd = parser.parse(options, args);

        Assert.assertTrue(cmd.hasOption("e"));
        Assert.assertTrue(cmd.hasOption("f"));
    }

    @Test(expected = ParseException.class)
    public void testParse4() throws ParseException {
        Options options = new Options();
        options.addOption("g", true, "test option g");

        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        String[] args = {"-g"};
        parser.parse(options, args);
    }

    @Test
    public void testParse5() throws ParseException {
        Options options = new Options();
        options.addOption("h", true, "test option h");

        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        String[] args = {"-h", "value"};
        CommandLine cmd = parser.parse(options, args);

        Assert.assertTrue(cmd.hasOption("h"));
        Assert.assertEquals("value", cmd.getOptionValue("h"));
    }

    @Test
    public void testParse6() throws ParseException {
        Options options = new Options();
        options.addOption("i", true, "test option i");
        options.addOption("j", false, "test option j");

        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        String[] args = {"-i", "value1", "-j", "-i", "value2"};
        CommandLine cmd = parser.parse(options, args);

        Assert.assertTrue(cmd.hasOption("i"));
        Assert.assertTrue(cmd.hasOption("j"));
        Assert.assertEquals("value2", cmd.getOptionValue("i"));
    }

}


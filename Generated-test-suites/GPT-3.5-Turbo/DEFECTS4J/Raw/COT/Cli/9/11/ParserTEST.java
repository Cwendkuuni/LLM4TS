
import org.junit.Test;
import org.apache.commons.cli.*;

public class ParserTest {

    @Test
    public void testParse() {
        Options options = new Options();
        options.addOption("a", "option a", false, "Option A");
        options.addOption("b", "option b", true, "Option B");
        options.addOption("c", "option c", false, "Option C");

        String[] args = {"-a", "-b", "value", "-c"};
        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(options, args);
            assert cmd.hasOption("a");
            assert cmd.getOptionValue("b").equals("value");
            assert cmd.hasOption("c");
            assert cmd.getArgs().length == 0;
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidOptions() throws ParseException {
        Options options = new Options();
        options.addOption("a", "option a", false, "Option A");
        options.addOption("b", "option b", true, "Option B");

        String[] args = {"-c"};
        CommandLineParser parser = new DefaultParser();

        parser.parse(options, args);
    }

    @Test
    public void testParseWithProperties() {
        Options options = new Options();
        options.addOption("a", "option a", false, "Option A");

        String[] args = {"-a"};
        Properties properties = new Properties();
        properties.setProperty("b", "value");

        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(options, args, properties);
            assert cmd.hasOption("a");
            assert cmd.getOptionValue("b").equals("value");
            assert cmd.getArgs().length == 0;
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testParseStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", "option a", false, "Option A");

        String[] args = {"-a", "arg1", "arg2"};
        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(options, args, true);
            assert cmd.hasOption("a");
            assert cmd.getArgs().length == 2;
            assert cmd.getArgs()[0].equals("arg1");
            assert cmd.getArgs()[1].equals("arg2");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseMissingArgument() throws ParseException {
        Options options = new Options();
        options.addOption("a", "option a", true, "Option A");

        String[] args = {"-a"};
        CommandLineParser parser = new DefaultParser();

        parser.parse(options, args);
    }

    @Test(expected = MissingOptionException.class)
    public void testParseMissingRequiredOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", "option a", true, "Option A");
        options.addOption("b", "option b", true, "Option B");
        options.addOption("c", "option c", true, "Option C");
        options.addOption("d", "option d", true, "Option D");

        String[] args = {"-a", "-b", "value"};
        CommandLineParser parser = new DefaultParser();

        parser.parse(options, args);
    }
}

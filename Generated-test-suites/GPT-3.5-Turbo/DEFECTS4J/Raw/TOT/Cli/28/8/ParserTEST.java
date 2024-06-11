import org.junit.Test;
import org.apache.commons.cli.*;

public class ParserTest {

    @Test
    public void testParse() throws ParseException {
        Options options = new Options();
        options.addOption("h", "help", false, "Print help message");
        options.addOption("v", "version", false, "Print version information");
        options.addOption("o", "output", true, "Output file");

        String[] args = {"-h", "-o", "output.txt"};
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        assert(cmd.hasOption("h"));
        assert(cmd.hasOption("o"));
        assert(cmd.getOptionValue("o").equals("output.txt"));
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Options options = new Options();
        options.addOption("h", "help", false, "Print help message");
        options.addOption("v", "version", false, "Print version information");
        options.addOption("o", "output", true, "Output file");

        String[] args = {"-h"};
        Properties properties = new Properties();
        properties.setProperty("output", "output.txt");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, properties);

        assert(cmd.hasOption("h"));
        assert(cmd.hasOption("o"));
        assert(cmd.getOptionValue("o").equals("output.txt"));
    }

    @Test(expected = MissingOptionException.class)
    public void testCheckRequiredOptions() throws ParseException {
        Options options = new Options();
        options.addRequiredOption("o", "output", true, "Output file");

        String[] args = {};
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        // This should throw MissingOptionException
        cmd.getOptionValue("o");
    }
}
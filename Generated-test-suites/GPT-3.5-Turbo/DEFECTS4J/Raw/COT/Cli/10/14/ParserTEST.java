import org.junit.Test;
import org.apache.commons.cli.*;

public class ParserTest {

    @Test
    public void testParseWithOptionsAndArguments() throws ParseException {
        Options options = new Options();
        options.addOption("h", "help", false, "print help");
        options.addOption("v", "version", false, "print version");

        String[] arguments = {"-h", "input.txt", "-v"};

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments);

        assert(cmd.hasOption("h") == true);
        assert(cmd.hasOption("v") == true);
        assert(cmd.getArgs()[0].equals("input.txt") == true);
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Options options = new Options();
        options.addOption("f", "file", true, "input file");

        String[] arguments = {};

        Properties properties = new Properties();
        properties.setProperty("file", "input.txt");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, properties);

        assert(cmd.hasOption("f") == true);
        assert(cmd.getOptionValue("f").equals("input.txt") == true);
    }

    @Test
    public void testParseWithNonOptionArgs() throws ParseException {
        Options options = new Options();
        options.addOption("f", "file", true, "input file");

        String[] arguments = {"input.txt", "output.txt"};

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, true);

        assert(cmd.hasOption("f") == false);
        assert(cmd.getArgs()[0].equals("input.txt") == true);
        assert(cmd.getArgs()[1].equals("output.txt") == true);
    }

    @Test
    public void testParseWithMissingRequiredOption() {
        Options options = new Options();
        options.addOption("i", "input", true, "input file");
        options.addOption("o", "output", true, "output file");
        options.getOption("i").setRequired(true);
        options.getOption("o").setRequired(true);

        String[] arguments = {};

        Parser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(options, arguments);
            assert false;
        } catch (MissingOptionException e) {
            assert e.getMessage().equals("Missing required options: -i, -o") == true;
        }
    }

    @Test
    public void testParseWithUnrecognizedOption() {
        Options options = new Options();
        options.addOption("v", "version", false, "print version");

        String[] arguments = {"-h"};

        Parser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(options, arguments);
            assert false;
        } catch (UnrecognizedOptionException e) {
            assert e.getMessage().equals("Unrecognized option: -h") == true;
        }
    }

}
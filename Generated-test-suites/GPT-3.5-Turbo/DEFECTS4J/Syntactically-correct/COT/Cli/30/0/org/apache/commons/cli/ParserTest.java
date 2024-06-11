
import org.junit.Test;
import org.apache.commons.cli.*;

import java.util.Properties;

public class ParserTest {
    
    @Test
    public void testParseWithOptionsAndArguments() throws ParseException {
        Options options = new Options();
        Option option = new Option("h", "help", false, "display help");
        options.addOption(option);

        String[] arguments = {"-h", "argument1", "argument2"};

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments);

        assert cmd.hasOption("h");
        assert cmd.getArgs().length == 2;
        assert cmd.getArgs()[0].equals("argument1");
        assert cmd.getArgs()[1].equals("argument2");
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidOption() throws ParseException {
        Options options = new Options();
        Option option = new Option("h", "help", false, "display help");
        options.addOption(option);

        String[] arguments = {"-x"};

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments);
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseWithMissingArgument() throws ParseException {
        Options options = new Options();
        Option option = new Option("f", "file", true, "input file");
        options.addOption(option);

        String[] arguments = {"-f"};

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments);
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Options options = new Options();
        Option option = new Option("f", "file", true, "input file");
        options.addOption(option);

        Properties properties = new Properties();
        properties.setProperty("file", "test.txt");

        String[] arguments = {};

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, properties);

        assert cmd.hasOption("f");
        assert cmd.getOptionValue("f").equals("test.txt");
    }

    @Test(expected = MissingOptionException.class)
    public void testParseWithMissingRequiredOption() throws ParseException {
        Options options = new Options();
        Option option = new Option("f", "file", true, "input file");
        option.setRequired(true);
        options.addOption(option);

        String[] arguments = {};

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments);
    }
}

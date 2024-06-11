import org.apache.commons.cli.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testSetOptions() {
        Options options = new Options();
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[0];
            }
        };
        parser.setOptions(options);
        assertEquals(options, parser.getOptions());
    }

    @Test
    public void testParseWithNoArguments() throws ParseException {
        Options options = new Options();
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[0];
            }

            @Override
            public CommandLine parse(Options options, String[] arguments) throws ParseException {
                return new CommandLine();
            }
        };
        assertEquals(new CommandLine(), parser.parse(options, new String[0]));
    }

    @Test
    public void testParseWithArguments() throws ParseException {
        Options options = new Options();
        Option option = new Option("i", "input", true, "input file");
        options.addOption(option);
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[]{"--", "-i", "input.txt"};
            }

            @Override
            public CommandLine parse(Options options, String[] arguments) throws ParseException {
                return new CommandLine();
            }

            @Override
            public CommandLine parse(Options options, String[] arguments, boolean stopAtNonOption)
                    throws ParseException {
                CommandLine cmd = parse(options, arguments, false);
                processArgs(option, Arrays.asList(arguments).listIterator());
                processOption("--", Arrays.asList(arguments).listIterator());
                return cmd;
            }

            @Override
            public CommandLine parse(Options options, String[] arguments, Properties properties)
                    throws ParseException {
                return new CommandLine();
            }

            @Override
            public CommandLine parse(Options options, String[] arguments, Properties properties, boolean stopAtNonOption)
                    throws ParseException {
                return new CommandLine();
            }

            @Override
            public CommandLine parse(Options options, String[] arguments, boolean stopAtNonOption)
                    throws ParseException {
                return new CommandLine();
            }

            @Override
            public CommandLine parse(Options options, String[] arguments) throws ParseException {
                return parse(options, arguments, null, false);
            }

            @Override
            public CommandLine parse(Options options, String[] arguments, Properties properties)
                    throws ParseException {
                return parse(options, arguments, properties, false);
            }

            @Override
            public CommandLine parse(Options options, String[] arguments, Properties properties, boolean stopAtNonOption)
                    throws ParseException {
                return parse(options, arguments, properties, true);
            }

            @Override
            protected void processProperties(Properties properties) {
                // do nothing
            }

            @Override
            protected void checkRequiredOptions() throws MissingOptionException {
                // do nothing
            }
        };
        assertEquals(new CommandLine("-i", "input.txt"), parser.parse(options, new String[]{"--", "-i", "input.txt"}));
    }

    @Test(expected = ParseException.class)
    public void testParseWithMissingOption() throws ParseException {
        Options options = new Options();
        Option option = new Option("i", "input", true, "input file");
        options.addOption(option);
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[0];
            }

            @Override
            public CommandLine parse(Options options, String[] arguments) throws ParseException {
                return new CommandLine();
            }

            @Override
            public CommandLine parse(Options options, String[] arguments, boolean stopAtNonOption)
                    throws ParseException {
                return new CommandLine();
            }
        };
        parser.setOptions(options);
        parser.parse(options, new String[0]);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseWithUnrecognizedOption() throws ParseException {
        Options options = new Options();
        Option option = new Option("i", "input", true, "input file");
        options.addOption(option);
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[]{"--", "-u", "unknown"};
            }

            @Override
            public CommandLine parse(Options options, String[] arguments) throws ParseException {
                return new CommandLine();
            }

            @Override
            public CommandLine parse(Options options, String[] arguments, boolean stopAtNonOption)
                    throws ParseException {
                return new CommandLine();
            }
        };
        parser.setOptions(options);
        parser.parse(options, new String[]{"--", "-u", "unknown"});
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Options options = new Options();
        Option option = new Option("i", "input", true, "input file");
        options.addOption(option);
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[0];
            }

            @Override
            public CommandLine parse(Options options, String[] arguments) throws ParseException {
                return new CommandLine();
            }

            @Override
            public CommandLine parse(Options options, String[] arguments, Properties properties)
                    throws ParseException {
                return parse(options, arguments, null, false);
            }

            @Override
            public CommandLine parse(Options options, String[] arguments, Properties properties, boolean stopAtNonOption)
                    throws ParseException {
                Properties prop = new Properties();
                prop.setProperty("input", "input.txt");
                return parse(options, arguments, prop, false);
            }

            @Override
            protected void processProperties(Properties properties) {
                Option opt = getOptions().getOption("i");
                opt.setValues(properties.getStringValues("input"));
            }
        };
        parser.setOptions(options);
        assertEquals(new CommandLine("-i", "input.txt"), parser.parse(options, new String[0], new Properties(){{setProperty("input", "input.txt");}}));
    }

    @Test(expected = ParseException.class)
    public void testParseWithMissingRequiredOption() throws ParseException {
        Options options = new Options();
        Option option = new Option("i", "input", true, "input file");
        options.addOption(option);
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[0];
            }

            @Override
            public CommandLine parse(Options options, String[] arguments) throws ParseException {
                return new CommandLine();
            }

            @Override
            public CommandLine parse(Options options, String[] arguments, boolean stopAtNonOption)
                    throws ParseException {
                return new CommandLine();
            }

            @Override
            protected void checkRequiredOptions() throws MissingOptionException {
                throw new MissingOptionException("Missing required option: input");
            }
        };
        parser.setOptions(options);
        parser.parse(options, new String[0]);
    }

    @Test
    public void testParseWithOptionalOption() throws ParseException {
        Options options = new Options();
        Option option = new Option("o", "output", true, "output file");
        option.setRequired(false);
        options.addOption(option);
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[0];
            }

            @Override
            public CommandLine parse(Options options, String[] arguments) throws ParseException {
                return new CommandLine();
            }

            @Override
            public CommandLine parse(Options options, String[] arguments, boolean stopAtNonOption)
                    throws ParseException {
                return new CommandLine();
            }

            @Override
            public CommandLine parse(Options options, String[] arguments, Properties properties)
                    throws ParseException {
                return new CommandLine();
            }

            @Override
            public CommandLine parse(Options options, String[] arguments, Properties properties, boolean stopAtNonOption)
                    throws ParseException {
                return new CommandLine();
            }

            @Override
            public CommandLine parse(Options options, String[] arguments, boolean stopAtNonOption)
                    throws ParseException {
                return new CommandLine();
            }

            @Override
            public CommandLine parse(Options options, String[] arguments) throws ParseException {
                return parse(options, arguments, null, false);
            }

            @Override
            public CommandLine parse(Options options, String[] arguments, Properties properties)
                    throws ParseException {
                return parse(options, arguments, properties, false);
            }

            @Override
            public CommandLine parse(Options options, String[] arguments, Properties properties, boolean stopAtNonOption)
                    throws ParseException {
                return parse(options, arguments, properties, true);
            }

            @Override
            protected void processProperties(Properties properties) {
                Option opt = getOptions().getOption("o");
                opt.setValues(properties.getStringValues("output"));
            }

            @Override
            protected void checkRequiredOptions() throws MissingOptionException {
                // do nothing
            }
        };
        parser.setOptions(options);
        assertEquals(new CommandLine(), parser.parse(options, new String[0]));
        parser.parse(options, new String[]{"--", "-o", "output.txt"});
        assertEquals(new CommandLine("-o", "output.txt"), parser.parse(options, new String[0]));
    }
}
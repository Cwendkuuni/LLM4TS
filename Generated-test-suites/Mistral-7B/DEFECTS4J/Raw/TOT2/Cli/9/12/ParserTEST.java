import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;

import java.util.List;

import static org.junit.Assert.*;

public class ParserTest {

    @org.junit.Test
    public void testParseWithValidOptionsAndArguments() throws ParseException {
        Options options = new Options();
        options.addRequiredOption("i", "input", true, "Input file path");
        options.addOption("o", "output", true, "Output file path");

        CommandLineParser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        String[] args = {"-i", "input.txt", "-o", "output.txt"};
        CommandLine cmd = parser.parse(options, args, null, false);

        assertEquals("input.txt", cmd.getOptionValue("input"));
        assertEquals("output.txt", cmd.getOptionValue("output"));
    }

    @org.junit.Test(expected = MissingArgumentException.class)
    public void testParseWithMissingArgument() throws ParseException {
        Options options = new Options();
        options.addRequiredOption("i", "input", true, "Input file path");

        CommandLineParser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        String[] args = {"-i"};
        parser.parse(options, args, null, false);

        fail("MissingArgumentException expected but not thrown");
    }

    @org.junit.Test(expected = MissingOptionException.class)
    public void testParseWithMissingOption() throws ParseException {
        Options options = new Options();
        options.addOption("o", "output", true, "Output file path");

        CommandLineParser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        String[] args = {"-i", "input.txt"};
        parser.parse(options, args, null, false);

        fail("MissingOptionException expected but not thrown");
    }

    @org.junit.Test(expected = UnrecognizedOptionException.class)
    public void testParseWithUnrecognizedOption() throws ParseException {
        Options options = new Options();
        options.addOption("i", "input", true, "Input file path");

        CommandLineParser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        String[] args = {"--unknownOption", "-i", "input.txt"};
        parser.parse(options, args, null, false);

        fail("UnrecognizedOptionException expected but not thrown");
    }

    @org.junit.Test
    public void testParseWithProperties() throws ParseException {
        Options options = new Options();
        options.addOption("i", "input", true, "Input file path");

        CommandLineParser parser = new Parser() {
            @Override
            protected CommandLine parse(Options options, String[] arguments, Properties properties, boolean stopAtNonOption)
                    throws ParseException {
                parse(options, arguments, null, stopAtNonOption);

                if (properties != null) {
                    processProperties(properties);
                }

                checkRequiredOptions();

                return cmd;
            }

            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        Properties properties = new Properties();
        properties.setProperty("input", "input.txt");

        String[] args = {};
        CommandLine cmd = parser.parse(options, args, properties, false);

        assertEquals("input.txt", cmd.getOptionValue("input"));
    }

    @org.junit.Test(expected = MissingOptionException.class)
    public void testParseWithMissingRequiredOption() throws ParseException {
        Options options = new Options();
        options.addRequiredOption("i", "input", true, "Input file path");

        CommandLineParser parser = new Parser() {
            @Override
            protected CommandLine parse(Options options, String[] arguments, Properties properties, boolean stopAtNonOption)
                    throws ParseException {
                parse(options, arguments, properties, stopAtNonOption);

                checkRequiredOptions();

                return cmd;
            }

            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        String[] args = {};
        parser.parse(options, args, null, false);

        fail("MissingOptionException expected but not thrown");
    }

    @org.junit.Test
    public void testParseWithOptionGroup() throws ParseException {
        Options options = new Options();

        OptionGroup group = new OptionGroup("group");
        group.setRequired(false);
        options.addOptionGroup(group);

        group.addOption("g", "groupOption", false, "Group option");
        options.addOption("i", "input", true, "Input file path");

        CommandLineParser parser = new Parser() {
            @Override
            protected CommandLine parse(Options options, String[] arguments, Properties properties, boolean stopAtNonOption)
                    throws ParseException {
                parse(options, arguments, properties, stopAtNonOption);

                checkRequiredOptions();

                return cmd;
            }

            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        String[] args = {"-g", "--groupOption", "-i", "input.txt"};
        CommandLine cmd = parser.parse(options, args, null, false);

        assertTrue(cmd.hasOption("groupOption"));
        assertTrue(cmd.hasOption("input"));
    }
}
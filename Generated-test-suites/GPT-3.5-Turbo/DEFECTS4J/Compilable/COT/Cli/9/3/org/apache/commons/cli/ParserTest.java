package org.apache.commons.cli;
import org.junit.Test;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;

import java.util.Properties;

public class ParserTest {

    @Test
    public void testParse() throws ParseException {
        Options options = new Options();
        CommandLine cmd = new CommandLine();
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[0];
            }
        };
        parser.parse(options, new String[0]);
    }

    @Test
    public void testParseWithOptionsAndArguments() throws ParseException {
        Options options = new Options();
        CommandLine cmd = new CommandLine();
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[]{"-option1", "arg1"};
            }
        };
        parser.parse(options, new String[]{"-option1", "arg1"});
    }

    @Test
    public void testParseWithOptionsAndProperties() throws ParseException {
        Options options = new Options();
        CommandLine cmd = new CommandLine();
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[]{"-option1", "arg1"};
            }
        };
        parser.parse(options, new String[]{"-option1", "arg1"}, new Properties());
    }

    @Test
    public void testParseWithOptionsArgumentsPropertiesAndStopAtNonOption() throws ParseException {
        Options options = new Options();
        CommandLine cmd = new CommandLine();
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[]{"-option1", "arg1", "-option2", "arg2"};
            }
        };
        parser.parse(options, new String[]{"-option1", "arg1", "-option2", "arg2"}, new Properties(), true);
    }

    @Test
    public void testParseWithOptionsArgumentsPropertiesAndStopAtNonOptionAndExcessArguments() throws ParseException {
        Options options = new Options();
        CommandLine cmd = new CommandLine();
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[]{"-option1", "arg1", "-option2", "arg2", "arg3"};
            }
        };
        parser.parse(options, new String[]{"-option1", "arg1", "-option2", "arg2", "arg3"}, new Properties(), true);
    }

    @Test
    public void testProcessPropertiesWithNullProperties() {
        Options options = new Options();
        CommandLine cmd = new CommandLine();
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[0];
            }
        };
        parser.processProperties(null);
    }

    @Test
    public void testProcessPropertiesWithProperties() {
        Options options = new Options();
        CommandLine cmd = new CommandLine();
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[0];
            }
        };
        parser.processProperties(new Properties());
    }

    @Test
    public void testCheckRequiredOptionsWithNoRequiredOptions() throws MissingOptionException {
        Options options = new Options();
        CommandLine cmd = new CommandLine();
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[0];
            }
        };
        parser.checkRequiredOptions();
    }

    @Test(expected = MissingOptionException.class)
    public void testCheckRequiredOptionsWithMissingRequiredOptions() throws MissingOptionException {
        Options options = new Options();
        CommandLine cmd = new CommandLine();
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[0];
            }
        };
        parser.getRequiredOptions().add(new Option("option1", "opt", false, "description"));
        parser.checkRequiredOptions();
    }

    @Test
    public void testProcessArgs() throws ParseException {
        Options options = new Options();
        CommandLine cmd = new CommandLine();
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[0];
            }
        };
        parser.processArgs(new Option("option1", "opt", true, "description"), null);
    }

    @Test(expected = MissingArgumentException.class)
    public void testProcessArgsWithMissingArgument() throws ParseException {
        Options options = new Options();
        CommandLine cmd = new CommandLine();
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[]{};
            }
        };
        parser.processArgs(new Option("option1", "opt", true, "description"), null);
    }

    @Test
    public void testProcessOption() throws ParseException {
        Options options = new Options();
        CommandLine cmd = new CommandLine();
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[0];
            }
        };
        parser.processOption("option1", null);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testProcessOptionWithUnrecognizedOption() throws ParseException {
        Options options = new Options();
        CommandLine cmd = new CommandLine();
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[0];
            }
        };
        parser.processOption("option1", null);
    }
}
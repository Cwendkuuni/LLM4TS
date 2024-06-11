package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ParserTest {
    @Test
    public void testParse() throws ParseException {
        Options options = new Options();
        Option option = new Option("f", "file", true, "input file");
        options.addOption(option);

        String[] arguments = {"-f", "input.txt"};
        CommandLine cmd = new CommandLine();
        cmd.addOption(option);
        
        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        assertEquals(cmd, parser.parse(options, arguments));
    }

    @Test(expected = ParseException.class)
    public void testParseUnrecognizedOption() throws ParseException {
        Options options = new Options();
        String[] arguments = {"-u"};

        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        parser.parse(options, arguments);
    }

    @Test
    public void testParseMissingArgument() {
        Options options = new Options();
        Option option = new Option("f", "file", true, "input file");
        options.addOption(option);

        String[] arguments = {"-f"};

        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        try {
            parser.parse(options, arguments);
        } catch (ParseException e) {
            assertEquals("Missing argument for option:f", e.getMessage());
        }
    }

    @Test
    public void testProcessArgs() throws ParseException {
        Options options = new Options();
        Option option = new Option("f", "file", true, "input file");
        options.addOption(option);

        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        String arg1 = "input.txt";
        String arg2 = "output.txt";
        ListIterator<String> iterator = Arrays.asList(arg1, arg2).listIterator();

        parser.processArgs(option, iterator);

        assertEquals(arg1, option.getValue());
        assertEquals(arg2, option.getValue());
    }

    @Test
    public void testProcessOption() throws ParseException {
        Options options = new Options();
        Option option = new Option("f", "file", true, "input file");
        options.addOption(option);

        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        String arg = "-f";
        ListIterator<String> iterator = Collections.singletonList(arg).listIterator();

        parser.processOption(arg, iterator);

        CommandLine cmd = new CommandLine();
        cmd.addOption(option);
        assertEquals(cmd, parser.parse(options, new String[0]));
    }
}
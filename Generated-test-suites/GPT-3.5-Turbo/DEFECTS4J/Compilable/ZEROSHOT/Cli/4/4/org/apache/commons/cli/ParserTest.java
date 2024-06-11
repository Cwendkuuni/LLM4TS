package org.apache.commons.cli;
import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.cli.*;

public class ParserTest {

    @Test
    public void testParse() throws ParseException {
        Options options = new Options();
        options.addOption("h", "help", false, "display help");
        options.addOption("v", "version", false, "display version");

        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        // Test case 1: Test parsing with no options
        String[] arguments1 = new String[]{"arg1", "arg2"};
        CommandLine commandLine1 = parser.parse(options, arguments1);

        Assert.assertFalse(commandLine1.hasOption("h"));
        Assert.assertFalse(commandLine1.hasOption("v"));
        Assert.assertArrayEquals(arguments1, commandLine1.getArgs());

        // Test case 2: Test parsing with one option
        String[] arguments2 = new String[]{"-h"};
        CommandLine commandLine2 = parser.parse(options, arguments2);

        Assert.assertTrue(commandLine2.hasOption("h"));
        Assert.assertFalse(commandLine2.hasOption("v"));
        Assert.assertArrayEquals(arguments2, commandLine2.getArgs());

        // Test case 3: Test parsing with multiple options
        String[] arguments3 = new String[]{"-h", "-v"};
        CommandLine commandLine3 = parser.parse(options, arguments3);

        Assert.assertTrue(commandLine3.hasOption("h"));
        Assert.assertTrue(commandLine3.hasOption("v"));
        Assert.assertArrayEquals(arguments3, commandLine3.getArgs());
    }

}
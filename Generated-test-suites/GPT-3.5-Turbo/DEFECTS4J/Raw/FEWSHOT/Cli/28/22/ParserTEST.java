package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ParserTest {
    @Test
    public void testParse() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder("a")
                .desc("Option A")
                .build());
        options.addOption(Option.builder("b")
                .desc("Option B")
                .argName("value")
                .hasArg()
                .build());
        options.addOption(Option.builder("c")
                .desc("Option C")
                .required()
                .build());

        String[] arguments = {"-a", "-b", "value", "-c"};

        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return arguments;
            }
        };

        CommandLine cmd = parser.parse(options, arguments);

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertTrue(cmd.hasOption("c"));
        assertEquals("value", cmd.getOptionValue("b"));
    }
}
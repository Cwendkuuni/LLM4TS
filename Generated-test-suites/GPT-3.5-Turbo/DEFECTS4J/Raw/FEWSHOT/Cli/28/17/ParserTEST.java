package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

public class ParserTest {

    private Parser parser;
    private Options options;

    @Before
    public void setup() {
        parser = mock(Parser.class);
        options = new Options();
        options.addOption(Option.builder("f").hasArg().build());
        options.addOption(Option.builder("foo").hasArg().build());
        options.addOption(Option.builder("bar").hasArg().build());
    }

    @Test
    public void testParseWithoutArguments() throws ParseException {
        CommandLine cmdLine = parser.parse(options, new String[] {});

        assertEquals(0, cmdLine.getOptions().length);
        assertEquals(0, cmdLine.getArgList().size());
    }

    @Test
    public void testParseWithOptions() throws ParseException {
        CommandLine cmdLine = parser.parse(options, new String[] {"-f", "file.txt", "--foo", "fooValue", "-bar", "barValue"});

        assertEquals(3, cmdLine.getOptions().length);
        assertEquals(1, cmdLine.getArgList().size());
        assertEquals("file.txt", cmdLine.getOptionValue("f"));
        assertEquals("fooValue", cmdLine.getOptionValue("foo"));
        assertEquals("barValue", cmdLine.getOptionValue("bar"));
        assertNull(cmdLine.getOptionValue("unknown"));
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidOption() throws ParseException {
        parser.parse(options, new String[] {"-invalid", "value"});
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseWithMissingArgument() throws ParseException {
        parser.parse(options, new String[] {"-f"});
    }
}
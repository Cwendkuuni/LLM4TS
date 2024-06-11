package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RunWith(Parameterized.class)
public class ParserTest {

    private final Options options;
    private final String[] arguments;
    private final String optionString;
    private final List<String> expectedArguments;
    private final List<Option> expectedOptions;

    public ParserTest(String optionString, String[] arguments, List<String> expectedArguments, List<Option> expectedOptions) {
        this.options = new Options();
        this.arguments = arguments;
        this.optionString = optionString;
        this.expectedArguments = expectedArguments;
        this.expectedOptions = expectedOptions;
    }

    @Parameters
    public static List<Object[]> data() {
        return Arrays.asList(new Object[][]{
            { "--foo --bar --baz foo bar baz", new String[0], Arrays.asList("--foo", "--bar", "--baz"), Arrays.asList(new Option("foo", true), new Option("bar", true), new Option("baz", true)) },
            { "--foo --bar foo bar", new String[]{"baz"}, Arrays.asList("--foo", "--bar"), Arrays.asList(new Option("foo", true), new Option("bar", true)) },
            { "-f -o output.txt foo bar", new String[]{"output.txt"}, Arrays.asList("-f", "-o"), Arrays.asList(new Option("f", true), new Option("o", true)) },
            { "-f foo bar", Arrays.asList("--help"), Arrays.asList("-f"), Arrays.asList(new Option("f", true)) },
            { "-foo", Arrays.asList("--help"), Arrays.asList(), Collections.emptyList() },
            { "--", Arrays.asList("--help"), Arrays.asList(), Collections.emptyList() },
            { "foo bar", Arrays.asList("foo", "bar"), Arrays.asList(), Collections.emptyList() }
        });
    }

    @Test
    public void testParse() throws ParseException {
        CommandLine cmd = Parser.parse(options, arguments);

        assertEquals("expected arguments", expectedArguments, cmd.getArgs());
        assertEquals("expected options", expectedOptions, cmd.getOptions());

        if (arguments != null) {
            assertFalse("expected no remaining arguments", cmd.getArgs().isEmpty());
        } else {
            assertTrue("expected empty arguments", cmd.getArgs().isEmpty());
        }

        assertSame("expected same Options instance", options, cmd.getParsedOptions());
    }
}
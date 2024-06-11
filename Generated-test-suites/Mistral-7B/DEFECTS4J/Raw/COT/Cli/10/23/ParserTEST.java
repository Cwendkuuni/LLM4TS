package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static org.mockito.Mockito.*;

public class ParserTest {

    private Options options;
    private CommandLineParser parser;
    private Parser parserUnderTest;

    @Before
    public void setup() {
        options = new Options();
        parser = new DefaultParser();
        parserUnderTest = mock(Parser.class);
        when(parserUnderTest.parse(options, any(String[][].class))).thenReturn(new CommandLine());
    }

    @Test
    public void testSetOptions() {
        Parser parserUnderTest = new Parser() {};
        Options options = new Options();
        parserUnderTest.setOptions(options);
        Assert.assertSame(options, parserUnderTest.getOptions());
    }

    @Test
    public void testGetOptions() {
        Parser parserUnderTest = new Parser() {};
        parserUnderTest.setOptions(new Options());
        Assert.assertSame(parserUnderTest.getOptions(), parserUnderTest.getOptions());
    }

    @Test
    public void testGetRequiredOptions() {
        Parser parserUnderTest = new Parser() {};
        Parser parserWithOptions = new Parser() {
            @Override
            protected List<String> flatten(Options opts, String[] args, boolean stopAtNonOption) {
                return Arrays.asList("--option1", "--option2");
            }
        };
        parserWithOptions.setOptions(new Options());
        parserWithOptions.setRequiredOptions(Arrays.asList("option1", "option2"));
        Assert.assertSame(parserWithOptions.getRequiredOptions(), parserWithOptions.getRequiredOptions());
        Assert.assertNotSame(parserUnderTest.getRequiredOptions(), parserWithOptions.getRequiredOptions());
    }

    @Test(expected = NullPointerException.class)
    public void testParseNullOptions() {
        parserUnderTest.parse(null, new String[0]);
    }

    @Test(expected = NullPointerException.class)
    public void testParseNullArguments() {
        parserUnderTest.parse(options, null);
    }

    @Test
    public void testParseEmptyArguments() throws ParseException {
        when(parserUnderTest.flatten(options, new String[0][], false))
                .thenReturn(Arrays.asList(""));

        CommandLine cmd = parserUnderTest.parse(options, new String[0]);
        Assert.assertEquals(0, cmd.getArgs().size());
    }

    @Test
    public void testParseOneArgument() throws ParseException {
        when(parserUnderTest.flatten(options, new String[]{"arg"}, false))
                .thenReturn(Arrays.asList("arg"));

        CommandLine cmd = parserUnderTest.parse(options, new String[]{"arg"});
        Assert.assertEquals("arg", cmd.getArgs().get(0));
    }

    @Test
    public void testParseWithOptionalArg() throws ParseException {
        Option option = Option.builder("o").longOpt("option")
                .hasArg(true).build();
        options.addOption(option);

        when(parserUnderTest.flatten(options, new String[]{"o", "value"}, false))
                .thenReturn(Arrays.asList("o", "value"));

        CommandLine cmd = parserUnderTest.parse(options, new String[]{"o", "value"});
        Assert.assertEquals(1, cmd.getArgs().size());
        Assert.assertEquals("value", cmd.getArgs().get(0));
    }

    @Test
    public void testParseWithRequiredArg() throws ParseException {
        Option option = Option.builder("r").longOpt("requiredOption")
                .hasArg(true).isRequired(true).build();
        options.addOption(option);

        when(parserUnderTest.flatten(options, new String[0][], false))
                .thenReturn(Arrays.asList("r"));

        try {
            parserUnderTest.parse(options, new String[0]);
            fail();
        } catch (MissingOptionException e) {
            // expected
        }

        when(parserUnderTest.flatten(options, new String[]{"r", "value"}, false))
                .thenReturn(Arrays.asList("r", "value"));

        CommandLine cmd = parserUnderTest.parse(options, new String[]{"r", "value"});
        Assert.assertEquals(1, cmd.getArgs().size());
        Assert.assertEquals("value", cmd.getArgs().get(0));
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseMissingArgumentForOption() throws ParseException {
        Option option = Option.builder("a").longOpt("option")
                .hasArg(true).build();
        options.addOption(option);

        when(parserUnderTest.flatten(options, new String[]{"a"}, false))
                .thenReturn(Arrays.asList("a"));

        parserUnderTest.parse(options, new String[]{"a"});
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseUnrecognizedOption() throws ParseException {
        when(parserUnderTest.flatten(options, new String[]{"x"}, false))
                .thenReturn(Arrays.asList("x"));

        parserUnderTest.parse(options, new String[]{"x"});
    }

    @Test(expected = ParseException.class)
    public void testParseException() throws ParseException {
        when(parserUnderTest.flatten(options, new String[]{"--"}, false))
                .thenReturn(Arrays.asList("--"));

        try {
            parserUnderTest.parse(options, new String[0]);
            fail();
        } catch (ParseException e) {
            // expected
        }
    }

    @Test(expected = ParseException.class)
    public void testParseExceptionWithProperties() throws ParseException {
        Option option = Option.builder("p").longOpt("property")
                .hasArg(true).build();
        options.addOption(option);

        Properties properties = new Properties();
        properties.setProperty("property", "value");

        when(parserUnderTest.flatten(options, new String[0][], true))
                .thenReturn(Arrays.asList("--property", "value"));

        try {
            parserUnderTest.parse(options, new String[0], properties);
            fail();
        } catch (ParseException e) {
            // expected
        }
    }

    @Test(expected = ParseException.class)
    public void testParseExceptionWithStopAtNonOption() throws ParseException {
        when(parserUnderTest.flatten(options, new String[]{"--"}, false))
                .thenReturn(Arrays.asList("--"));

        try {
            parserUnderTest.parse(options, new String[]{"--"}, true);
            fail();
        } catch (ParseException e) {
            // expected
        }
    }
}
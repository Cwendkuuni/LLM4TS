package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RunWith(Parameterized.class)
public class ParserTest {

    private Parser parser;
    private Options options;
    private String[] arguments;

    public ParserTest(Parser parser, Options options, String[] arguments) {
        this.parser = parser;
        this.options = options;
        this.arguments = arguments;
    }

    @Parameterized.Parameters(name = "{0} - parse({1}) with arguments({2})")
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][]{
            {new BasicParser(), new Options(), new String[0]},
            {new BasicParser(), new Options(), new String[]{}},
            {new BasicParser(), new Options(), new String[]{"--foo"}},
            {new BasicParser(), new Options(), new String[]{"--foo", "--bar"}},
            {new BasicParser(), new Options(), new String[]{"--foo", "bar"}},
            {new BasicParser(), new Options(), new String[]{"--foo=bar", "--bar=baz"}},
            {new BasicParser(), new Options(), new String[]{"--foo=bar", "baz"}},
            {new BasicParser(), new Options(), new String[]{"--foo", "--bar=baz"}},
            {new DefaultParser(), new Options(), new String[0]},
            {new DefaultParser(), new Options(), new String[]{"--foo"}},
            {new DefaultParser(), new Options(), new String[]{"--foo", "--bar"}},
            {new DefaultParser(), new Options(), new String[]{"--foo", "bar"}},
            {new DefaultParser(), new Options(), new String[]{"--foo=bar", "--bar=baz"}},
            {new DefaultParser(), new Options(), new String[]{"--foo=bar", "baz"}},
            {new DefaultParser(), new Options(), new String[]{"--foo", "--bar=baz"}},
            {new PosixParser(), new Options(), new String[0]},
            {new PosixParser(), new Options(), new String[]{"--foo"}},
            {new PosixParser(), new Options(), new String[]{"--foo", "--bar"}},
            {new PosixParser(), new Options(), new String[]{"--foo", "bar"}},
            {new PosixParser(), new Options(), new String[]{"--foo=bar", "--bar=baz"}},
            {new PosixParser(), new Options(), new String[]{"--foo=bar", "baz"}},
            {new PosixParser(), new Options(), new String[]{"--foo", "--bar=baz"}},
        });
    }

    @Test
    public void testParse() throws Exception {
        parser.setOptions(options);
        CommandLine commandLine = parser.parse(options, arguments);
        assertNotNull(commandLine);
        assertSame(parser, commandLine.getParser());
        assertSame(options, commandLine.getOptions());
        assertSame(arguments, commandLine.getArgs());
    }

    @Test
    public void testParseWithProperties() throws Exception {
        parser.setOptions(options);
        Properties properties = new Properties();
        properties.setProperty("foo", "bar");
        CommandLine commandLine = parser.parse(options, arguments, properties);
        assertNotNull(commandLine);
        assertSame(parser, commandLine.getParser());
        assertSame(options, commandLine.getOptions());
        assertSame(arguments, commandLine.getArgs());
        List<?> values = commandLine.getValues("foo");
        assertEquals(1, values.size());
        assertEquals("bar", values.get(0));
    }

    @Test
    public void testParseWithStopAtNonOption() throws Exception {
        parser.setOptions(options);
        CommandLine commandLine = parser.parse(options, arguments, true);
        assertNotNull(commandLine);
        assertSame(parser, commandLine.getParser());
        assertSame(options, commandLine.getOptions());
        List<String> args = new ArrayList<>(Arrays.asList(arguments));
        args.remove(args.size() - 1);
        assertSame(args, commandLine.getArgs());
    }

    @Test
    public void testFlatten() {
        String[] input = {"--foo", "-a", "-b", "--bar", "arg1", "arg2", "--bar=value", "arg3", "--"};
        List<String> expected = Arrays.asList(
            "--foo", "--bar", "arg1", "arg2", "--bar=value", "--", "-a", "-b", "arg3");
        List<String> result = parser.flatten(options, input, true);
        assertEquals(expected, result);
        result = parser.flatten(options, input, false);
        assertEquals(Arrays.asList(input), result);
    }

    @Test
    public void testProcessProperties() throws Exception {
        String[] input = {"--foo=bar", "--bar=baz"};
        parser.setOptions(options);
        Properties properties = new Properties();
        properties.setProperty("foo", "bar");
        parser.parse(options, input, properties);
        List<?> values = parser.getCommandLine().getValues("foo");
        assertEquals(1, values.size());
        assertEquals("bar", values.get(0));
        values = parser.getCommandLine().getValues("bar");
        assertEquals(1, values.size());
        assertEquals("baz", values.get(0));
    }

    @Test
    public void testCheckRequiredOptions() throws Exception {
        String[] input = {"--bar"};
        parser.setOptions(options);
        options.addOption(new Option("foo", true));
        options.addOption(new Option("bar", true));
        assertThrows(MissingOptionException.class, () -> parser.parse(options, input));
    }

    @Test
    public void testProcessOption() throws Exception {
        String[] input = {"--foo", "value", "--bar=value2"};
        parser.setOptions(options);
        List<String> args = new ArrayList<>();
        Option optionFoo = new Option("foo", true);
        optionFoo.setArgName("value");
        Option optionBar = new Option("bar", true, true, "value2");
        options.addOption(optionFoo);
        options.addOption(optionBar);
        ListIterator<String> iterator = Arrays.asList(input).listIterator();
        parser.parse(options, input, true, iterator);
        assertEquals(args, parser.getCommandLine().getArgs());
        assertEquals(1, cmd.getArgs().size());
        assertEquals("value", cmd.getArgs().get(0));
        assertEquals(1, cmd.getOptions().size());
        Option parsedOptionFoo = cmd.getOption("foo");
        assertEquals(optionFoo, parsedOptionFoo);
        assertEquals(true, parsedOptionFoo.isSet());
        assertEquals("value", parsedOptionFoo.getValue());
        assertEquals(1, cmd.getOptions().size());
        Option parsedOptionBar = cmd.getOption("bar");
        assertEquals(optionBar, parsedOptionBar);
        assertEquals(true, parsedOptionBar.isSet());
        assertEquals("value2", parsedOptionBar.getValue());
    }

    @Test
    public void testProcessArgs() throws Exception {
        String[] input = {"--foo", "arg1", "--foo", "arg2"};
        parser.setOptions(options);
        Option optionFoo = new Option("foo", true);
        optionFoo.setArgName("arg");
        options.addOption(optionFoo);
        List<String> args = new ArrayList<>();
        ListIterator<String> iterator = Arrays.asList(input).listIterator();
        parser.parse(options, input, true, iterator);
        assertEquals(args, parser.getCommandLine().getArgs());
        assertEquals(2, cmd.getArgs().size());
        assertEquals("arg1", cmd.getArgs().get(0));
        assertEquals("arg2", cmd.getArgs().get(1));
    }

    @Test(expected = MissingArgumentException.class)
    public void testMissingArgument() throws Exception {
        String[] input = {"--foo"};
        parser.setOptions(options);
        Option optionFoo = new Option("foo", true);
        optionFoo.setArgName("arg");
        options.addOption(optionFoo);
        List<String> args = new ArrayList<>();
        ListIterator<String> iterator = Arrays.asList(input).listIterator();
        parser.parse(options, input, true, iterator);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testUnrecognizedOption() throws Exception {
        String[] input = {"--qux"};
        parser.setOptions(options);
        List<String> args = new ArrayList<>();
        ListIterator<String> iterator = Arrays.asList(input).listIterator();
        parser.parse(options, input, true, iterator);
    }
}
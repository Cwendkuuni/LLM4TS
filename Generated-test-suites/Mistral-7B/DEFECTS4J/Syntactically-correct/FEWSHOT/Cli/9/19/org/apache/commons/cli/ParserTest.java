package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() throws ParseException {
        Options options = new Options();
        String[] arguments = new String[] { "--foo", "-b", "1", "--bar", "arg" };
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments);

        assertNotNull(cmd);
        assertFalse(cmd.getArgs().isEmpty());
        assertArrayEquals(new String[] { "arg" }, cmd.getArgs().toArray());
        assertEquals(2, cmd.getOptions().size());

        Option foo = cmd.getOption("foo");
        assertNotNull(foo);
        assertNull(foo.getValue());

        Option bar = cmd.getOption("bar");
        assertNotNull(bar);
        assertArrayEquals(new String[] { "arg" }, bar.getValue());

        Option b = cmd.getOption("b");
        assertNotNull(b);
        assertEquals("1", b.getValue());
    }

    @Test
    public void testParse_empty() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[0]);

        assertNotNull(cmd);
        assertFalse(cmd.getArgs().isEmpty());
        assertEquals(0, cmd.getArgs().size());
        assertEquals(0, cmd.getOptions().size());
    }

    @Test
    public void testParse_noOptions() throws ParseException {
        Options options = new Options();
        String[] arguments = new String[] { "arg1", "arg2" };
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments);

        assertNotNull(cmd);
        assertFalse(cmd.getArgs().isEmpty());
        assertEquals(2, cmd.getArgs().size());
        assertEquals(0, cmd.getOptions().size());
    }

    @Test
    public void testParse_requiredOptions() throws ParseException {
        Options options = new Options();
        List requiredOptions = Arrays.asList("foo", "bar");
        options.setRequiredOptions(requiredOptions);
        String[] arguments = new String[0];
        Parser parser = new DefaultParser();

        assertThrows(MissingOptionException.class, () -> parser.parse(options, arguments));
    }

    @Test
    public void testParse_properties() throws ParseException {
        Options options = new Options();
        Properties properties = new Properties();
        properties.setProperty("foo", "value");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[0], properties);

        Option foo = cmd.getOption("foo");
        assertNotNull(foo);
        assertEquals("value", foo.getValue()[0]);
    }

    @Test
    public void testParse_stopAtNonOption() throws ParseException {
        Options options = new Options();
        List requiredOptions = Arrays.asList("foo");
        options.setRequiredOptions(requiredOptions);
        String[] arguments = new String[] { "--foo", "-b", "1", "--bar", "arg" };
        Parser parser = new DefaultParser();

        CommandLine cmd = parser.parse(options, arguments, null, true);

        assertNotNull(cmd);
        assertFalse(cmd.getArgs().isEmpty());
        assertArrayEquals(new String[] { "--bar", "arg" }, cmd.getArgs().toArray());
        assertEquals(1, cmd.getOptions().size());
        Option foo = cmd.getOption("foo");
        assertNull(foo);
    }

    @Test
    public void testParse_help() throws ParseException {
        Options options = new Options();
        String[] arguments = new String[] { "--help" };
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments);

        assertNotNull(cmd);
        assertEquals(Collections.singletonList("help"), cmd.getArgs());
        assertEquals(1, cmd.getOptions().size());
        Option help = cmd.getOption("help");
        assertNotNull(help);
        assertEquals("prints this help message.", help.getDescription());
    }
}
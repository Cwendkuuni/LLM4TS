package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.junit.Test;

public class ParserTest
{
    @Test
    public void testParse() throws Exception
    {
        // given
        String[] args = new String[]{"-a", "1", "--b", "2", "--c", "3", "-d"};
        Options options = new Options();
        options.addOption("a", true, "A");
        options.addOption("b", true, "B");
        options.addOption("c", true, "C");
        options.addOption("d", false, "D");

        // when
        CommandLineParser parser = new Parser()
        {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption)
            {
                return arguments;
            }
        };
        CommandLine cmd = parser.parse(options, args);

        // then
        assertEquals(Arrays.asList("1", "--b", "2", "--c", "3"), cmd.getArgs());
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertTrue(cmd.isSetOption("b"));
        assertTrue(cmd.hasOption("c"));
        assertTrue(cmd.isSetOption("c"));
        assertFalse(cmd.hasOption("e"));
        assertFalse(cmd.isSetOption("e"));
        assertNull(cmd.getOptionValue("e"));
        assertTrue(cmd.hasOption("d"));
        assertFalse(cmd.isSetOption("d"));
    }

    @Test
    public void testParseWithProperties() throws Exception
    {
        // given
        String[] args = new String[]{"--e=5", "-f", "file.txt"};
        Options options = new Options();
        options.addOption("e", true, "E");
        options.addOption("f", true, "F");

        // when
        CommandLineParser parser = new Parser()
        {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption)
            {
                return arguments;
            }

            @Override
            protected CommandLine parse(Options options, String[] arguments, Properties properties, boolean stopAtNonOption)
                    throws ParseException
            {
                Properties props = new Properties();
                props.setProperty("e", "5");
                return super.parse(options, arguments, props, stopAtNonOption);
            }
        };
        CommandLine cmd = parser.parse(options, args);

        // then
        assertEquals(Arrays.asList("file.txt"), cmd.getArgs());
        assertTrue(cmd.hasOption("e"));
        assertTrue(cmd.isSetOption("e"));
        assertEquals("5", cmd.getOptionValue("e"));
        assertTrue(cmd.hasOption("f"));
        assertTrue(cmd.isSetOption("f"));
        assertNull(cmd.getOptionValue("f"));
    }

    @Test
    public void testParseWithMissingArgument() throws Exception
    {
        // given
        String[] args = new String[]{"-a", "--b"};
        Options options = new Options();
        options.addOption("a", true, "A");
        options.addOption("b", true, "B");
        options.addOption("c", true, "C");

        // when
        CommandLineParser parser = new Parser()
        {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption)
            {
                return arguments;
            }
        };

        // then
        assertThrows(MissingArgumentException.class, () -> parser.parse(options, args));
    }

    @Test
    public void testParseWithUnrecognizedOption() throws Exception
    {
        // given
        String[] args = new String[]{"-x", "-a", "1", "--b", "2"};
        Options options = new Options();
        options.addOption("a", true, "A");
        options.addOption("b", true, "B");

        // when
        CommandLineParser parser = new Parser()
        {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption)
            {
                return arguments;
            }
        };

        // then
        assertThrows(UnrecognizedOptionException.class, () -> parser.parse(options, args));
    }

    @Test
    public void testParseWithRequiredOptions() throws Exception
    {
        // given
        String[] args = new String[]{"--a"};
        Options options = new Options();
        options.addOption("a", true, "A").isRequired();

        // when
        CommandLineParser parser = new Parser()
        {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption)
            {
                return arguments;
            }
        };

        // then
        assertThrows(MissingOptionException.class, () -> parser.parse(options, args));
    }

    @Test
    public void testParseWithRequiredOptionGroup() throws Exception
    {
        // given
        String[] args = new String[]{"--group"};
        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "G");
        group.setRequired(true);
        group.addOption(new Option("a", true, "A"));
        options.addOptionGroup(group);

        // when
        CommandLineParser parser = new Parser()
        {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption)
            {
                return arguments;
            }
        };

        // then
        assertThrows(MissingOptionException.class, () -> parser.parse(options, args));
    }

    @Test
    public void testParseWithOptionalArg() throws Exception
    {
        // given
        String[] args = new String[]{"-a", "--b=test"};
        Options options = new Options();
        options.addOption("a", true, "A");
        options.addOption("b", true, "B");

        // when
        CommandLineParser parser = new Parser()
        {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption)
            {
                return arguments;
            }
        };
        CommandLine cmd = parser.parse(options, args);

        // then
        assertEquals(Arrays.asList("--b=test"), cmd.getOptionNames());
        assertTrue(cmd.hasOption("b"));
        assertTrue(cmd.isSetOption("b"));
        assertEquals("test", cmd.getOptionValue("b"));
        assertFalse(cmd.hasOption("c"));
        assertFalse(cmd.isSetOption("c"));
        assertNull(cmd.getOptionValue("c"));
    }
}
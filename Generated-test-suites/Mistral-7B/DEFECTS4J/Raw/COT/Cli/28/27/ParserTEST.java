package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class ParserTest
{
    @Test
    public void testSetOptions() throws Exception
    {
        Parser parser = new DefaultParser();
        assertNull(parser.getOptions());

        Options options = new Options();
        parser.setOptions(options);
        assertEquals(options, parser.getOptions());
    }

    @Test
    public void testParseWithEmptyArguments() throws Exception
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        CommandLine cmd = parser.parse(options, new String[0]);
        assertNotNull(cmd);
        assertTrue(cmd.getArgs().isEmpty());
    }

    @Test
    public void testParseWithValidArguments() throws Exception
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String[] arguments = new String[]{"arg1", "arg2"};
        CommandLine cmd = parser.parse(options, arguments);
        assertNotNull(cmd);
        List<String> args = cmd.getArgs();
        assertEquals(2, args.size());
        assertEquals("arg1", args.get(0));
        assertEquals("arg2", args.get(1));
    }

    @Test
    public void testParseWithProperties() throws Exception
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        Properties properties = new Properties();
        properties.put("option1", "value1");
        properties.put("option2", "value2");
        CommandLine cmd = parser.parse(options, new String[0], properties);
        assertNotNull(cmd);
        assertEquals(2, cmd.getOptions().size());
        Option option1 = cmd.getOption("option1");
        assertNotNull(option1);
        assertEquals("value1", option1.getValue());
        Option option2 = cmd.getOption("option2");
        assertNotNull(option2);
        assertEquals("value2", option2.getValue());
    }

    @Test
    public void testParseWithInvalidArguments() throws Exception
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("option1", true, "o");
        CommandLine cmd;

        // test stopAtNonOption equals false
        cmd = parser.parse(options, new String[]{"--", "-o"});
        assertThrows(ParseException.class, () -> cmd);

        cmd = parser.parse(options, new String[]{"--", "-o", "arg"});
        assertThrows(ParseException.class, () -> cmd);

        cmd = parser.parse(options, new String[]{"--", "-o", "-o"});
        assertThrows(ParseException.class, () -> cmd);

        cmd = parser.parse(options, new String[]{"--", "invalidOption"});
        assertThrows(UnrecognizedOptionException.class, () -> cmd);

        cmd = parser.parse(options, new String[]{"--", "-o", "arg1", "arg2"});
        assertThrows(MissingArgumentException.class, () -> cmd);

        // test stopAtNonOption equals true
        options.getOption("option1").setRequired(false);
        cmd = parser.parse(options, new String[]{"--", "-o"});
        assertNotNull(cmd);
        assertTrue(cmd.getArgs().contains("--"));
        assertTrue(cmd.getArgs().isEmpty() || cmd.getArgs().contains("o"));

        cmd = parser.parse(options, new String[]{"--", "-o", "arg"});
        assertNotNull(cmd);
        assertTrue(cmd.getArgs().contains("--"));
        assertTrue(cmd.getArgs().isEmpty() || cmd.getArgs().contains("o"));
        assertTrue(cmd.getArgs().contains("arg"));

        cmd = parser.parse(options, new String[]{"--", "-o", "-o"});
        assertNotNull(cmd);
        assertTrue(cmd.getArgs().contains("--"));
        assertTrue(cmd.getArgs().isEmpty() || cmd.getArgs().contains("o"));
    }

    @Test
    public void testParseWithStopAtNonOption() throws Exception
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("option1", true, "o");
        CommandLine cmd;

        // test with empty arguments
        cmd = parser.parse(options, new String[0], false);
        assertNull(cmd);

        // test with arguments
        cmd = parser.parse(options, new String[]{"arg1", "arg2"}, false);
        assertThrows(ParseException.class, () -> cmd);

        cmd = parser.parse(options, new String[]{"--", "arg1", "arg2"}, false);
        assertNotNull(cmd);
        assertEquals(2, cmd.getArgs().size());
        assertEquals("arg1", cmd.getArgs().get(0));
        assertEquals("arg2", cmd.getArgs().get(1));

        cmd = parser.parse(options, new String[]{"--", "-o", "arg1", "arg2"}, false);
        assertThrows(MissingOptionException.class, () -> cmd);

        // test with required options
        options.getOption("option1").setRequired(true);
        cmd = parser.parse(options, new String[]{"arg1", "arg2"}, false);
        assertFalse(options.getOption("option1").isSet());
        assertEquals(2, cmd.getArgs().size());
        assertEquals("arg1", cmd.getArgs().get(0));
        assertEquals("arg2", cmd.getArgs().get(1));

        cmd = parser.parse(options, new String[]{"--", "arg1", "arg2"}, false);
        assertTrue(options.getOption("option1").isSet());
        assertEquals(1, cmd.getArgs().size());
        assertNull(cmd.getArgs().get(0));
    }

    @Test
    public void testParseWithPropertiesAndStopAtNonOption() throws Exception
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("option1", true, "o");
        Properties properties = new Properties();
        properties.put("option1", "value1");
        CommandLine cmd;

        // test with empty arguments
        cmd = parser.parse(options, new String[0], false, properties);
        assertNotNull(cmd);
        assertTrue(cmd.getArgs().isEmpty());
        assertEquals(1, cmd.getOptions().size());
        Option option1 = cmd.getOption("option1");
        assertNotNull(option1);
        assertEquals("value1", option1.getValue());

        // test with arguments
        cmd = parser.parse(options, new String[]{"arg1", "arg2"}, false, properties);
        assertThrows(ParseException.class, () -> cmd);

        cmd = parser.parse(options, new String[]{"--", "arg1", "arg2"}, false, properties);
        assertNotNull(cmd);
        assertEquals(3, cmd.getArgs().size());
        assertEquals("arg1", cmd.getArgs().get(0));
        assertEquals("arg2", cmd.getArgs().get(1));
        assertEquals("--", cmd.getArgs().get(2));

        cmd = parser.parse(options, new String[]{"--", "-o", "arg1", "arg2"}, false, properties);
        assertThrows(MissingOptionException.class, () -> cmd);

        // test with required options
        options.getOption("option1").setRequired(true);
        cmd = parser.parse(options, new String[0], false, properties);
        assertFalse(options.getOption("option1").isSet());
        assertTrue(cmd.getArgs().isEmpty());
        assertNotNull(cmd.getOptions());
        assertEquals(1, cmd.getOptions().size());
        Option option1 = cmd.getOption("option1");
        assertNotNull(option1);
        assertNull(option1.getValue());

        cmd = parser.parse(options, new String[0], true, properties);
        assertNotNull(cmd);
        assertEquals(1, cmd.getOptions().size());
        Option option1_true = cmd.getOption("option1");
        assertNotNull(option1_true);
        assertTrue(option1_true.isSet());
        assertNull(option1_true.getValue());

        cmd = parser.parse(options, new String[]{"--", "arg1", "arg2"}, false, properties);
        assertTrue(options.getOption("option1").isSet());
        assertEquals(1, cmd.getArgs().size());
        assertNull(cmd.getArgs().get(0));
    }
}
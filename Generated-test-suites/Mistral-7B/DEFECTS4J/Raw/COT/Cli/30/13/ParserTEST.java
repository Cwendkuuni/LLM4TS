package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.ParseResult;
import org.apache.commons.cli.TestUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class ParserTest
{
    @Test
    public void testParserMethods()
    {
        // Test method signatures
        assertNotNull(Parser.class.getProtectedMethod("setOptions", Options.class));
        assertNotNull(Parser.class.getProtectedMethod("getOptions"));
        assertNotNull(Parser.class.getProtectedMethod("getRequiredOptions"));
        assertNotNull(Parser.class.getProtectedMethod("flatten", Options.class, String[].class, boolean.class));
        assertNotNull(Parser.class.getMethod("parse", Options.class, String[].class));
        assertNotNull(Parser.class.getMethod("parse", Options.class, String[].class, boolean.class));
        assertNotNull(Parser.class.getMethod("parse", Options.class, String[].class, Properties.class));
        assertNotNull(Parser.class.getMethod("parse", Options.class, String[].class, Properties.class, boolean.class));

        // Test basic functionality of parse method
        Options options = new Options();
        options.addOption("a", "apple", true, "Apple is a fruit.");
        options.addOption("b", "banana", false, "Banana is a fruit.");
        String[] arguments = new String[]{"-a", "test"};
        CommandLineParser parser = new Parser()
        {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException
            {
                // Implement the flatten method for testing
                return new String[]{"-a", "-a", "test"};
            }
        };

        CommandLine cmd = parser.parse(options, arguments);
        assertEquals(2, cmd.getArgs().size());
        assertTrue(cmd.hasOption("a"));
        assertEquals("test", cmd.getOptionValue("a"));
        assertFalse(cmd.hasOption("b"));

        // Test with no arguments
        arguments = new String[0];
        cmd = parser.parse(options, arguments);
        assertEquals(0, cmd.getArgs().size());

        // Test with incorrect arguments
        arguments = new String[]{"-c", "test"};
        assertThrows(ParseException.class, () -> parser.parse(options, arguments));

        // Test with missing required options
        options.addRequiredOption("c", "color", true, "Specify a color.");
        assertThrows(MissingOptionException.class, () -> parser.parse(options, arguments));

        // Test processProperties method
        Properties properties = new Properties();
        properties.setProperty("a", "Apple");
        properties.setProperty("b", "Banana");
        cmd = parser.parse(options, arguments, properties, true);
        assertEquals(2, cmd.getArgs().size());
        assertTrue(cmd.hasOption("a"));
        assertEquals("Apple", cmd.getOptionValue("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals("Banana", cmd.getOptionValue("b"));

        // Test parse method with optional arguments
        options.addOption("d", "date", true, "Specify a date.");
        options.addOption("e", "example", 3, "Specify an example value.");
        arguments = new String[]{"-a", "test", "-d", "2023-03-25", "-e", "1", "--"};
        cmd = parser.parse(options, arguments);
        assertEquals(1, cmd.getArgs().size());
        assertTrue(cmd.hasOption("a"));
        assertEquals("test", cmd.getOptionValue("a"));
        assertEquals(1, cmd.getValues("d").size());
        assertEquals("2023-03-25", cmd.getValues("d").get(0));
        assertEquals(3, cmd.getValues("e").length);
        assertEquals("1", cmd.getValues("e")[0]);
        assertEquals("", cmd.getValues("e")[1]);
        assertEquals("", cmd.getValues("e")[2]);
    }
}
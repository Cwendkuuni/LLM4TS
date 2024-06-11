package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;

/**
 * Test class for the Parser class.
 */
public class ParserTest {

    /**
     * Test method for {@link Parser#parse(Options, String[])}.
     */
    @org.junit.Test
    public void testParse() throws Exception {
        Parser parser = new DefaultParser();

        // test empty arguments
        Options options = new Options();
        String[] arguments = new String[0];
        CommandLine cmd = parser.parse(options, arguments);
        assertEquals(0, cmd.getArgs().size());

        // test arguments with no options
        options = new Options();
        arguments = new String[]{"--arg1", "arg1", "--arg2", "arg2"};
        cmd = parser.parse(options, arguments);
        assertEquals(2, cmd.getArgs().size());
        assertEquals("arg1", cmd.getArgs().get(0));
        assertEquals("arg2", cmd.getArgs().get(1));

        // test arguments with options
        options.addOption("a", true, "aOption");
        options.addOption("b", 1, "bOption");
        options.addOption("c", 3, "cOption");
        options.addOption("d", 4, "dOption");
        options.addOption("e", 5, "eOption");
        arguments = new String[]{"--a", "--b=2", "--c=3", "--d=4", "--e=5", "arg1", "arg2"};
        cmd = parser.parse(options, arguments);
        assertEquals(2, cmd.getArgs().size());
        assertEquals("arg1", cmd.getArgs().get(0));
        assertEquals("arg2", cmd.getArgs().get(1));
        assertEquals(true, cmd.hasOption("a"));
        assertEquals(2, cmd.getOptionValues("b").length);
        assertEquals(3, cmd.getOptionValues("c")[0].intValue());
        assertEquals(4, cmd.getOptionValues("d")[0].intValue());
        assertEquals(5, cmd.getOptionValues("e")[0].intValue());

        // test required options
        options.addRequiredOption("a", false, "aOption", "a");
        options.addRequiredOption("b", false, "bOption", "b");
        arguments = new String[]{"--a", "arg1", "--b"};
        try {
            cmd = parser.parse(options, arguments);
            fail();
        } catch (MissingOptionException e) {
            assertEquals("Missing option: b", e.getMessage());
        }
        arguments = new String[]{"--a=arg1", "--b=arg2"};
        cmd = parser.parse(options, arguments);
        assertEquals(0, cmd.getArgs().size());
        assertEquals(true, cmd.hasOption("a"));
        assertEquals("arg1", cmd.getOptionValues("a")[0]);
        assertEquals(true, cmd.hasOption("b"));
        assertEquals("arg2", cmd.getOptionValues("b")[0]);

        // test options in a group
        Options groupOptions = new Options();
        groupOptions.addOptionGroup(new OptionGroup("group1", "Group 1"));
        groupOptions.addOption("a1", true, "a1Option", "a1");
        groupOptions.addOption("a2", true, "a2Option", "a2");
        groupOptions.addOption("a3", true, "a3Option", "a3");
        Options options1 = new Options();
        options1.addOptionGroup(groupOptions);
        options1.addOption("g", true, "gOption", "g");
        arguments = new String[]{"--g", "--group1.a1", "--group1.a2"};
        cmd = parser.parse(options1, arguments);
        assertEquals(0, cmd.getArgs().size());
        assertEquals(true, cmd.hasOption("g"));
        assertEquals(true, cmd.hasOption("a1"));
        assertEquals(true, cmd.hasOption("a2"));
        assertEquals(false, cmd.hasOption("a3"));

        // test long options
        options.addOption("--long-a", true, "longOptionA");
        options.addOption("--long-b", 1, "longOptionB");
        options.addOption("--long-c", 3, "longOptionC");
        options.addOption("--long-d", 4, "longOptionD");
        options.addOption("--long-e", 5, "longOptionE");
        arguments = new String[]{"--long-a", "--long-b=2", "--long-c=3", "--long-d=4", "--long-e=5", "arg1", "arg2"};
        cmd = parser.parse(options, arguments);
        assertEquals(2, cmd.getArgs().size());
        assertEquals("arg1", cmd.getArgs().get(0));
        assertEquals("arg2", cmd.getArgs().get(1));
        assertEquals(true, cmd.hasOption("--long-a"));
        assertEquals(2, cmd.getOptionValues("--long-b").length);
        assertEquals(3, cmd.getOptionValues("--long-c")[0].intValue());
        assertEquals(4, cmd.getOptionValues("--long-d")[0].intValue());
        assertEquals(5, cmd.getOptionValues("--long-e")[0].intValue());

        // test invalid options
        options.addOption("--invalid", false, "invalidOption", "invalid");
        arguments = new String[]{"--invalid"};
        try {
            cmd = parser.parse(options, arguments);
            fail();
        } catch (UnrecognizedOptionException e) {
            assertEquals("Unrecognized option: --invalid", e.getMessage());
        }

        // test invalid option values
        options.addOption("--value", 1, "valueOption");
        arguments = new String[]{"--value=invalid"};
        try {
            cmd = parser.parse(options, arguments);
            fail();
        } catch (ParseException e) {
            assertEquals("Invalid value for option: --value", e.getMessage());
        }

        // test invalid option value types
        options.addOption("--int", 1, "intOption");
        arguments = new String[]{"--int=invalid"};
        try {
            cmd = parser.parse(options, arguments);
            fail();
        } catch (ParseException e) {
            assertEquals("Invalid value for option: --int", e.getMessage());
        }
        options.addOption("--string", 1, "stringOption");
        arguments = new String[]{"--string=123"};
        try {
            cmd = parser.parse(options, arguments);
            fail();
        } catch (ParseException e) {
            assertEquals("Invalid value for option: --string", e.getMessage());
        }

        // test options with property values
        Properties properties = new Properties();
        properties.setProperty("propertyA", "valueA");
        properties.setProperty("propertyB", "valueB");
        options.addOption("p", true, "propertyOption");
        arguments = new String[]{"--p"};
        cmd = parser.parse(options, arguments, properties);
        assertEquals(0, cmd.getArgs().size());
        assertEquals(true, cmd.hasOption("p"));
        assertEquals("valueA", cmd.getOptionValues("p")[0]);
        assertEquals("valueB", properties.getProperty("propertyB"));
    }
}
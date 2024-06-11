package org.apache.commons.cli.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;

/**
 * Test class for Parser.
 */
public class ParserTest
{
    /**
     * Test method for {@link Parser#parse(Options, String[])}.
     */
    @Test
    public void testParse()
    {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", false, "Option B");
        options.addOption("c", 3, "Option C with an int value");
        options.addOption("d", 4, "Option D with an int value");
        options.addOption("e", "test", "Option E with a string value");
        options.addOption("f", OptType.NO_ARG.getValue(), "Option F with no argument");
        options.addOption("g", OptType.VALUE_REQ.getValue(), "Option G with required value");
        options.addOption("h", OptType.VALUE_OPT.getValue(), "Option H with optional value");
        options.addOptionGroup(new OptionGroup("Group1", "Group 1"));
        options.addOptionGroup(new OptionGroup("Group2", "Group 2"));
        options.addRequiredOption("r", false, "Required Option R");
        options.addRequiredOption("s", false, "Required Option S");

        String[] arguments = new String[]{"--a", "--b", "--c=3", "--d=4", "--e=test", "--f", "--g=test1", "--h=test2", "--g=test3",
                "--r", "--s"};

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;

        try
        {
            cmd = parser.parse(options, arguments);

            // test option values
            assertTrue(cmd.hasOption("a"));
            assertTrue(cmd.hasOption("b"));
            assertTrue(cmd.hasOption("c"));
            assertTrue(cmd.hasOption("d"));
            assertTrue(cmd.hasOption("e"));
            assertTrue(cmd.hasOption("f"));

            Option cOption = cmd.getOption("c");
            Option dOption = cmd.getOption("d");
            assertEquals(3, cOption.getValue().length);
            assertEquals(4, dOption.getValue().length);

            // test option groups
            OptionGroup group1 = options.getOptionGroup("Group1");
            OptionGroup group2 = options.getOptionGroup("Group2");
            assertTrue(group1.isSelected());
            assertFalse(group2.isSelected());

            // test required options
            assertSame(cmd.getOption("r"), options.getOption("r"));
            assertSame(cmd.getOption("s"), options.getOption("s"));

            // test no option
            assertNull(cmd.getOption("i"));

            // test unrecognized option
            assertNull(cmd.getOption("i"));
        }
        catch (ParseException e)
        {
            fail("Unexpected ParseException: " + e.getMessage());
        }
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], Properties)}.
     */
    @Test
    public void testParseWithProperties()
    {
        Options options = new Options();
        options.addOption("p", true, "Option P with a boolean value from properties");

        Properties properties = new Properties();
        properties.setProperty("p", "true");

        String[] arguments = new String[]{"--p"};

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;

        try
        {
            cmd = parser.parse(options, arguments, properties);
            assertTrue(cmd.hasOption("p"));
            assertTrue(cmd.getOption("p").getValue().equals("true"));
        }
        catch (ParseException e)
        {
            fail("Unexpected ParseException: " + e.getMessage());
        }
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], boolean)}.
     */
    @Test
    public void testParseWithStopAtNonOption()
    {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", false, "Option B");
        options.addOption("c", 3, "Option C with an int value");
        options.addOption("d", 4, "Option D with an int value");
        options.addOption("e", "test", "Option E with a string value");
        options.addOption("f", OptType.NO_ARG.getValue(), "Option F with no argument");
        options.addOption("g", OptType.VALUE_REQ.getValue(), "Option G with required value");
        options.addOption("h", OptType.VALUE_OPT.getValue(), "Option H with optional value");
        options.addOption("i", OptType.VALUE_REQ.getValue(), "Option I with required value");

        String[] arguments = new String[]{"--a", "--b", "--c=3", "--d=4", "--e=test", "--f", "--g=test1", "--h=test2", "--i", "unrecognized"};

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;

        try
        {
            cmd = parser.parse(options, arguments, false);

            // test option values
            assertTrue(cmd.hasOption("a"));
            assertTrue(cmd.hasOption("b"));
            assertTrue(cmd.hasOption("c"));
            assertTrue(cmd.hasOption("d"));
            assertTrue(cmd.hasOption("e"));
            assertTrue(cmd.hasOption("f"));
            assertTrue(cmd.hasOption("g"));
            assertTrue(cmd.hasOption("h"));

            Option cOption = cmd.getOption("c");
            Option dOption = cmd.getOption("d");
            assertEquals(3, cOption.getValue().length);
            assertEquals(4, dOption.getValue().length);

            // test unrecognized option
            Option iOption = cmd.getOption("i");
            assertEquals(iOption.getKey(), "i");
            assertNull(iOption.getValue());

            // test remaining arguments
            List<String> args = cmd.getArgs();
            List<String> expectedArgs = Arrays.asList("unrecognized");
            assertTrue(args.size() > 0);
            assertTrue(args.equals(expectedArgs));
        }
        catch (ParseException e)
        {
            fail("Unexpected ParseException: " + e.getMessage());
        }
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], boolean, Properties)}.
     */
    @Test
    public void testParseWithStopAtNonOptionAndProperties()
    {
        Options options = new Options();
        options.addOption("p", true, "Option P with a boolean value from properties");

        Properties properties = new Properties();
        properties.setProperty("p", "true");

        String[] arguments = new String[]{"--p", "--i=unrecognized"};

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;

        try
        {
            cmd = parser.parse(options, arguments, false, properties);

            // test option value
            assertTrue(cmd.hasOption("p"));
            assertTrue(cmd.getOption("p").getValue().equals("true"));

            // test unrecognized option
            assertNull(cmd.getOption("i"));

            // test remaining arguments
            List<String> args = cmd.getArgs();
            List<String> expectedArgs = Arrays.asList("unrecognized");
            assertTrue(args.size() > 0);
            assertTrue(args.equals(expectedArgs));
        }
        catch (ParseException e)
        {
            fail("Unexpected ParseException: " + e.getMessage());
        }
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], boolean, Properties, Options)}.
     */
    @Test
    public void testParseWithAdditionalOptions()
    {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", false, "Option B");
        options.addOption("c", 3, "Option C with an int value");
        options.addOption("d", 4, "Option D with an int value");
        options.addOption("e", "test", "Option E with a string value");
        options.addOption("f", OptType.NO_ARG.getValue(), "Option F with no argument");
        options.addOption("g", OptType.VALUE_REQ.getValue(), "Option G with required value");
        options.addOption("h", OptType.VALUE_OPT.getValue(), "Option H with optional value");
        options.addOption("i", OptType.VALUE_REQ.getValue(), "Option I with required value");

        Options additionalOptions = new Options();
        additionalOptions.addOption("j", true, "Additional Option J");

        String[] arguments = new String[]{"--a", "--b", "--c=3", "--d=4", "--e=test", "--f", "--g=test1", "--h=test2", "--i", "--j"};

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;

        try
        {
            cmd = parser.parse(options, arguments, false, null, additionalOptions);

            // test option values
            assertTrue(cmd.hasOption("a"));
            assertTrue(cmd.hasOption("b"));
            assertTrue(cmd.hasOption("c"));
            assertTrue(cmd.hasOption("d"));
            assertTrue(cmd.hasOption("e"));
            assertTrue(cmd.hasOption("f"));
            assertTrue(cmd.hasOption("g"));
            assertTrue(cmd.hasOption("h"));
            assertTrue(cmd.hasOption("i"));
            assertTrue(cmd.hasOption("j"));

            Option cOption = cmd.getOption("c");
            Option dOption = cmd.getOption("d");
            assertEquals(3, cOption.getValue().length);
            assertEquals(4, dOption.getValue().length);

            // test additional option
            assertTrue(cmd.hasOption("j"));
            assertTrue(cmd.getOption("j").getValue().equals(true));
        }
        catch (ParseException e)
        {
            fail("Unexpected ParseException: " + e.getMessage());
        }
    }
}

class OptType
{
    public static final String NO_ARG = "no-arg";
    public static final String VALUE_REQ = "value-required";
    public static final String VALUE_OPT = "value-optional";

    public static int valueOf(String value)
    {
        switch (value)
        {
        case "no-arg":
            return 0;
        case "value-required":
            return 1;
        case "value-optional":
            return 2;
        default:
            throw new IllegalArgumentException("Unknown value: " + value);
        }
    }
}
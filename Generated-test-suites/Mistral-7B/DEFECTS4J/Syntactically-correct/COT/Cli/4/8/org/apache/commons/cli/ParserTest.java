package org.apache.commons.cli.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.ParsingCompletionProvider;
import org.apache.commons.cli.TestUtils;
import org.apache.commons.cli.util.ArgList;
import org.apache.commons.cli.util.ArgListBuilder;
import org.apache.commons.cli.util.ClassHelper;
import org.apache.commons.cli.util.HelpFormatterBuilder;
import org.apache.commons.cli.util.HelpFormatterFactory;
import org.apache.commons.cli.util.HelpPrinter;
import org.apache.commons.cli.util.StringArrayUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for Parser.
 */
public class ParserTest
{
    private CommandLineParser parser;

    @Before
    public void setUp()
    {
        parser = new DefaultParser();
    }

    /**
     * Test method for {@link Parser#parse(Options, String[])}.
     */
    @Test
    public void testParse() throws Exception
    {
        String[] args = new String[]{"-a", "1", "--option1", "value1", 
                                    "--option2", "value2"};

        Options options = new Options();
        options.addOption("a", "optionA", true, "Option A");
        options.addOption("option1", true, "Option 1");
        options.addOption("option2", true, "Option 2");

        CommandLine cmd = parser.parse(options, args, true, true);

        assertSame(options, cmd.getOptions());
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("option1"));
        assertTrue(cmd.hasOption("option2"));
        assertEquals(Arrays.asList("1"), cmd.getOptionValues("option1"));
        assertEquals(Arrays.asList("value1"), cmd.getOptionValues("option1", true));
        assertEquals(Arrays.asList("value2"), cmd.getOptionValues("option2"));
        assertEquals(Arrays.asList("value2"), cmd.getOptionValues("option2", true));
        assertFalse(cmd.hasOption("unknown"));
        assertFalse(cmd.getArgs().isEmpty());
        assertEquals("value1 value2", cmd.getArgs().toString());
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], Properties)}.
     */
    @Test
    public void testParseWithProperties() throws Exception
    {
        String[] args = new String[]{"--option1=value1", "--option2=value2"};

        Options options = new Options();
        options.addOption("option1", true, "Option 1");
        options.addOption("option2", true, "Option 2");

        Properties properties = new Properties();
        properties.put("option1", "value1");
        properties.put("option2", "value2");

        CommandLine cmd = parser.parse(options, args, properties, true);

        assertSame(options, cmd.getOptions());
        assertTrue(cmd.hasOption("option1"));
        assertTrue(cmd.hasOption("option2"));
        assertEquals(Arrays.asList("value1"), cmd.getOptionValues("option1"));
        assertEquals(Arrays.asList("value1"), cmd.getOptionValues("option1", true));
        assertEquals(Arrays.asList("value2"), cmd.getOptionValues("option2"));
        assertEquals(Arrays.asList("value2"), cmd.getOptionValues("option2", true));
        assertFalse(cmd.hasOption("unknown"));
        assertFalse(cmd.getArgs().isEmpty());
        assertEquals("value1 value2", cmd.getArgs().toString());
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], boolean)}.
     */
    @Test
    public void testParseWithStopAtNonOption() throws Exception
    {
        String[] args = new String[]{"-a", "1", "--option1", "value1", 
                                    "--option2", "value2", "arg1", "arg2"};

        Options options = new Options();
        options.addOption("a", "optionA", true, "Option A");
        options.addOption("option1", true, "Option 1");
        options.addOption("option2", true, "Option 2");

        CommandLine cmd = parser.parse(options, args, true, false);

        assertSame(options, cmd.getOptions());
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("option1"));
        assertTrue(cmd.hasOption("option2"));
        assertEquals(Arrays.asList("1"), cmd.getOptionValues("option1"));
        assertEquals(Arrays.asList("value1"), cmd.getOptionValues("option1", true));
        assertEquals(Arrays.asList("value2"), cmd.getOptionValues("option2"));
        assertEquals(Arrays.asList("value2"), cmd.getOptionValues("option2", true));
        assertFalse(cmd.hasOption("unknown"));
        assertFalse(cmd.getArgs().isEmpty());
        assertEquals("arg1 arg2", cmd.getArgs().toString());
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], boolean, boolean)}.
     */
    @Test
    public void testParseWithStopAtNonOptionAndFlatten() throws Exception
    {
        String[] args = new String[]{"--option1=value1", "--option2=value2", 
                                    "-a", "1", "--option3", "value3", "arg1", 
                                    "arg2"};

        Options options = new Options();
        options.addOption("option1", true, "Option 1");
        options.addOption("option2", true, "Option 2");
        options.addOption("option3", true, "Option 3");

        CommandLine cmd = parser.parse(options, args, true, false);

        assertSame(options, cmd.getOptions());
        assertTrue(cmd.hasOption("option1"));
        assertTrue(cmd.hasOption("option2"));
        assertTrue(cmd.hasOption("option3"));
        assertEquals(Arrays.asList("value1"), cmd.getOptionValues("option1"));
        assertEquals(Arrays.asList("value1"), cmd.getOptionValues("option1", true));
        assertEquals(Arrays.asList("value2"), cmd.getOptionValues("option2"));
        assertEquals(Arrays.asList("value2"), cmd.getOptionValues("option2", true));
        assertEquals(Arrays.asList("value3"), cmd.getOptionValues("option3"));
        assertEquals(Arrays.asList("value3"), cmd.getOptionValues("option3", true));
        assertFalse(cmd.hasOption("unknown"));
        assertFalse(cmd.getArgs().isEmpty());
        assertEquals("arg1 arg2", cmd.getArgs().toString());
    }

    /**
     * Test method for {@link Parser#parse(Options, String[])}.
     */
    @Test
    public void testParseWithNullArgs() throws Exception
    {
        String[] args = null;

        Options options = new Options();

        assertThrows(ParseException.class, () -> parser.parse(options, args, true));
    }

    /**
     * Test method for {@link Parser#parse(Options, String[])}.
     */
    @Test
    public void testParseWithEmptyArgs() throws Exception
    {
        String[] args = new String[0];

        Options options = new Options();

        CommandLine cmd = parser.parse(options, args, true);

        assertSame(options, cmd.getOptions());
        assertFalse(cmd.hasOptions());
        assertFalse(cmd.hasOption("unknown"));
        assertFalse(cmd.getArgs().isEmpty());
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], boolean)}.
     */
    @Test
    public void testParseWithNullOptions() throws Exception
    {
        String[] args = new String[]{"-a", "1", "--option1", "value1", 
                                    "--option2", "value2"};

        Options options = null;

        assertThrows(NullPointerException.class, () -> parser.parse(options, 
                                                                 args, true));
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], boolean, boolean)}.
     */
    @Test
    public void testParseWithNullOptionsAndStopAtNonOption() throws Exception
    {
        String[] args = new String[]{"--option1=value1", "--option2=value2", 
                                    "-a", "1", "--option3", "value3", "arg1", 
                                    "arg2"};

        Options options = null;

        assertThrows(NullPointerException.class, () -> parser.parse(options, 
                                                                 args, true, false));
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], boolean)}.
     */
    @Test
    public void testParseWithInvalidOption() throws Exception
    {
        String[] args = new String[]{"--invalidoption", "1", "--option1", 
                                    "value1", "--option2", "value2"};

        Options options = new Options();
        options.addOption("option1", true, "Option 1");
        options.addOption("option2", true, "Option 2");

        assertThrows(UnrecognizedOptionException.class, () -> parser.parse(options, 
                                                                 args, true, false));
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], Properties)}.
     */
    @Test
    public void testParseWithNullProperties() throws Exception
    {
        String[] args = new String[]{"--option1=value1", "--option2=value2"};

        Options options = new Options();
        options.addOption("option1", true, "Option 1");
        options.addOption("option2", true, "Option 2");

        Properties properties = null;

        assertThrows(NullPointerException.class, () -> parser.parse(options, 
                                                                 args, properties, true));
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], Properties, boolean)}.
     */
    @Test
    public void testParseWithNullPropertiesAndStopAtNonOption() throws Exception
    {
        String[] args = new String[]{"--option1=value1", "--option2=value2", 
                                    "-a", "1", "--option3", "value3", "arg1", 
                                    "arg2"};

        Options options = new Options();
        options.addOption("option1", true, "Option 1");
        options.addOption("option2", true, "Option 2");
        options.addOption("option3", true, "Option 3");

        Properties properties = null;

        assertThrows(NullPointerException.class, () -> parser.parse(options, 
                                                                 args, properties, false));
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], Properties, boolean)}.
     */
    @Test
    public void testParseWithMissingRequiredOption() throws Exception
    {
        String[] args = new String[]{"--option1=value1", "--option2=value2"};

        Options options = new Options();
        options.addOption("option1", true, "Option 1");
        options.addOption("option2", true, "Option 2");
        options.addRequiredOption("option3", true, "Option 3");

        Properties properties = new Properties();

        assertThrows(MissingOptionException.class, () -> parser.parse(options, 
                                                                 args, properties, false));
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], Properties, boolean)}.
     */
    @Test
    public void testParseWithMissingRequiredOptionAndStopAtNonOption() throws Exception
    {
        String[] args = new String[]{"--option1=value1", "--option2=value2", 
                                    "-a", "1", "--option3", "value3", "arg1", 
                                    "arg2"};

        Options options = new Options();
        options.addOption("option1", true, "Option 1");
        options.addOption("option2", true, "Option 2");
        options.addOption("option3", true, "Option 3");
        options.addRequiredOption("option4", true, "Option 4");

        Properties properties = new Properties();

        assertThrows(MissingOptionException.class, () -> parser.parse(options, 
                                                                 args, properties, true));
    }
}
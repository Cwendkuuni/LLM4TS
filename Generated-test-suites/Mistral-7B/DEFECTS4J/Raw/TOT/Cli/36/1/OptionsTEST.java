package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for Options class.
 */
public class OptionsTest
{
    /**
     * Test for addOption method with single character short option and no argument.
     */
    @Test
    public void testAddOptionWithSingleCharShortOptionAndNoArgument()
    {
        Options options = new Options();
        options.addOption("a", "optionA", false, "This is option A.");

        assertEquals("optionA", options.getOption("a").getKey());
        assertFalse(options.getOption("a").hasArg());

        CommandLineParser parser = new BasicParser();
        String[] args = {"-a"};
        try
        {
            CommandLine cmd = parser.parse(options.toBuilder().buildParser().parse(args, null), args);
            assertTrue(cmd.hasOption("optionA"));
        }
        catch (ParseException e)
        {
            fail("Parsing should have succeeded.");
        }
    }

    /**
     * Test for addOption method with single character short option and required argument.
     */
    @Test
    public void testAddOptionWithSingleCharShortOptionAndRequiredArgument()
    {
        Options options = new Options();
        options.addOption("b", "optionB", true, "This is option B.");

        assertEquals("optionB", options.getOption("b").getKey());
        assertTrue(options.getOption("b").hasArg());

        CommandLineParser parser = new BasicParser();
        String[] args = {"-b", "valueB"};
        try
        {
            CommandLine cmd = parser.parse(options.toBuilder().buildParser().parse(args, null), args);
            assertEquals("valueB", cmd.getOptionValue("optionB"));
        }
        catch (ParseException e)
        {
            fail("Parsing should have succeeded.");
        }

        String[] missingArgArgs = {"-b"};
        try
        {
            CommandLine cmd = parser.parse(options.toBuilder().buildParser().parse(missingArgArgs, null), missingArgArgs);
            fail("Parsing should have failed due to missing argument.");
        }
        catch (ParseException e)
        {
            // Expected
        }
    }

    /**
     * Test for addOption method with long option and no argument.
     */
    @Test
    public void testAddOptionWithLongOptionAndNoArgument()
    {
        Options options = new Options();
        options.addOption("optionC", false, "This is option C.");

        assertEquals("optionC", options.getOption("optionC").getKey());
        assertFalse(options.getOption("optionC").hasArg());

        CommandLineParser parser = new BasicParser();
        String[] args = {"--optionC"};
        try
        {
            CommandLine cmd = parser.parse(options.toBuilder().buildParser().parse(args, null), args);
            assertTrue(cmd.hasOption("optionC"));
        }
        catch (ParseException e)
        {
            fail("Parsing should have succeeded.");
        }
    }

    /**
     * Test for addOption method with long option and required argument.
     */
    @Test
    public void testAddOptionWithLongOptionAndRequiredArgument()
    {
        Options options = new Options();
        options.addOption("optionD", true, "This is option D.");

        assertEquals("optionD", options.getOption("optionD").getKey());
        assertTrue(options.getOption("optionD").hasArg());

        CommandLineParser parser = new BasicParser();
        String[] args = {"--optionD", "valueD"};
        try
        {
            CommandLine cmd = parser.parse(options.toBuilder().buildParser().parse(args, null), args);
            assertEquals("valueD", cmd.getOptionValue("optionD"));
        }
        catch (ParseException e)
        {
            fail("Parsing should have succeeded.");
        }

        String[] missingArgArgs = {"--optionD"};
        try
        {
            CommandLine cmd = parser.parse(options.toBuilder().buildParser().parse(missingArgArgs, null), missingArgArgs);
            fail("Parsing should have failed due to missing argument.");
        }
        catch (ParseException e)
        {
            // Expected
        }
    }

    /**
     * Test for addOptionGroup method.
     */
    @Test
    public void testAddOptionGroup()
    {
        Options options = new Options();

        OptionGroup optionGroup = new OptionGroup("optionGroup", "Option group.");
        options.addOptionGroup(optionGroup);

        Option optionA = new Option("a", "optionA", false, "This is option A.");
        optionGroup.addOption(optionA);

        assertEquals("optionA", options.getOption("optionGroup.a").getKey());
        assertFalse(options.getOption("optionGroup.a").hasArg());
        assertEquals("Option group.", options.getOptionGroup("optionGroup").getLongOpt());

        CommandLineParser parser = new BasicParser();
        String[] args = {"--optionGroup", "-a"};
        try
        {
            CommandLine cmd = parser.parse(options.toBuilder().buildParser().parse(args, null), args);
            assertTrue(cmd.hasOption("optionGroup.a"));
        }
        catch (ParseException e)
        {
            fail("Parsing should have succeeded.");
        }
    }

    /**
     * Test for getOptions method.
     */
    @Test
    public void testGetOptions()
    {
        Options options = new Options();
        options.addOption("a", "optionA", false, "This is option A.");
        options.addOption("b", "optionB", true, "This is option B.");

        List<Option> optionsList = options.getOptions();
        assertEquals(2, optionsList.size());

        assertEquals("optionA", optionsList.get(0).getKey());
        assertFalse(optionsList.get(0).hasArg());

        assertEquals("optionB", optionsList.get(1).getKey());
        assertTrue(optionsList.get(1).hasArg());
    }

    /**
     * Test for getRequiredOptions method.
     */
    @Test
    public void testGetRequiredOptions()
    {
        Options options = new Options();
        Option optionA = new Option("a", "optionA", true, "This is option A.");
        Option optionB = new Option("b", "optionB", true, "This is option B.");
        options.addOption(optionA);
        options.addOption(optionB);

        List<Option> requiredOptionsList = options.getRequiredOptions();
        assertEquals(2, requiredOptionsList.size());

        assertEquals("optionA", requiredOptionsList.get(0).getKey());
        assertTrue(requiredOptionsList.get(0).isRequired());

        assertEquals("optionB", requiredOptionsList.get(1).getKey());
        assertTrue(requiredOptionsList.get(1).isRequired());
    }
}
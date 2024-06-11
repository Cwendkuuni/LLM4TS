import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Tests for the PosixParser class.
 */
public class PosixParserTest
{
    /**
     * Tests the parsing of a valid command line arguments with no option.
     */
    @Test
    public void testEmptyArguments()
    {
        String[] args = {};

        PosixParser parser = new PosixParser();
        Options options = new Options();

        try
        {
            String[] parsedArgs = parser.flatten(options, args, false);
            assertEquals(new String[0], parsedArgs);
        }
        catch (ParseException e)
        {
            // This should not be thrown for empty arguments
            fail("An exception was thrown: " + e.getMessage());
        }
    }

    /**
     * Tests the parsing of a valid command line arguments with single option.
     */
    @Test
    public void testSingleOption()
    {
        String[] args = {"--option"};

        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("option", false, null, "Option description.");

        try
        {
            String[] parsedArgs = parser.flatten(options, args, false);
            assertEquals(new String[]{"--option"}, parsedArgs);
        }
        catch (ParseException e)
        {
            // This should not be thrown for valid arguments
            fail("An exception was thrown: " + e.getMessage());
        }
    }

    /**
     * Tests the parsing of a valid command line arguments with single value option.
     */
    @Test
    public void testSingleValueOption()
    {
        String[] args = {"--option", "value"};

        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("option", true, "v", "Option description.");

        try
        {
            String[] parsedArgs = parser.flatten(options, args, false);
            assertEquals(new String[]{"--option", "value"}, parsedArgs);
        }
        catch (ParseException e)
        {
            // This should not be thrown for valid arguments
            fail("An exception was thrown: " + e.getMessage());
        }
    }

    /**
     * Tests the parsing of a valid command line arguments with multiple options.
     */
    @Test
    public void testMultipleOptions()
    {
        String[] args = {"--option1", "--option2"};

        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("option1", false, null, "Option 1 description.");
        options.addOption("option2", false, null, "Option 2 description.");

        try
        {
            String[] parsedArgs = parser.flatten(options, args, false);
            assertEquals(new String[]{"--option1", "--option2"}, parsedArgs);
        }
        catch (ParseException e)
        {
            // This should not be thrown for valid arguments
            fail("An exception was thrown: " + e.getMessage());
        }
    }

    /**
     * Tests the handling of long options with values.
     */
    @Test
    public void testLongOptionWithValue()
    {
        String[] args = {"--option", "value"};

        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("option", true, "o", "Option description.");

        try
        {
            String[] parsedArgs = parser.flatten(options, args, false);
            assertEquals(new String[]{"--option", "value"}, parsedArgs);
        }
        catch (ParseException e)
        {
            // This should not be thrown for valid arguments
            fail("An exception was thrown: " + e.getMessage());
        }
    }

    /**
     * Tests the handling of short options.
     */
    @Test
    public void testShortOption()
    {
        String[] args = {"-o"};

        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("option", false, "o", "Option description.");

        try
        {
            String[] parsedArgs = parser.flatten(options, args, false);
            assertEquals(new String[]{"-o"}, parsedArgs);
        }
        catch (ParseException e)
        {
            // This should not be thrown for valid arguments
            fail("An exception was thrown: " + e.getMessage());
        }
    }

    /**
     * Tests the handling of short options with values.
     */
    @Test
    public void testShortOptionWithValue()
    {
        String[] args = {"-o", "value"};

        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("option", true, "o", "Option description.");

        try
        {
            String[] parsedArgs = parser.flatten(options, args, false);
            assertEquals(new String[]{"-o", "value"}, parsedArgs);
        }
        catch (ParseException e)
        {
            // This should not be thrown for valid arguments
            fail("An exception was thrown: " + e.getMessage());
        }
    }

    /**
     * Tests the handling of multiple short options.
     */
    @Test
    public void testMultipleShortOptions()
    {
        String[] args = {"-o", "-p"};

        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("option1", false, "o", "Option 1 description.");
        options.addOption("option2", false, "p", "Option 2 description.");

        try
        {
            String[] parsedArgs = parser.flatten(options, args, false);
            assertEquals(new String[]{"-o", "-p"}, parsedArgs);
        }
        catch (ParseException e)
        {
            // This should not be thrown for valid arguments
            fail("An exception was thrown: " + e.getMessage());
        }
    }

    /**
     * Tests the handling of options with no values.
     */
    @Test
    public void testNoValueOption()
    {
        String[] args = {"--option"};

        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("option", false, null, "Option description.");

        try
        {
            String[] parsedArgs = parser.flatten(options, args, false);
            assertEquals(new String[]{"--option"}, parsedArgs);
        }
        catch (ParseException e)
        {
            // This should not be thrown for valid arguments
            fail("An exception was thrown: " + e.getMessage());
        }
    }

    /**
     * Tests the handling of a command line argument that is not an option.
     */
    @Test
    public void testNonOption()
    {
        String[] args = {"--option", "value", "arg"};

        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("option", true, "o", "Option description.");

        try
        {
            String[] parsedArgs = parser.flatten(options, args, false);
            fail("An exception should have been thrown.");
        }
        catch (ParseException e)
        {
            // This is the expected result
        }

        assertEquals(new String[]{"--option", "value"}, parser.getTokensToProcess());
        assertEquals("arg", parser.getRemainingArguments()[0]);
    }

    /**
     * Tests the handling of a command line argument that is not an option and the 'stopAtNonOption' flag is set.
     */
    @Test
    public void testNonOptionWithStopAtNonOption()
    {
        String[] args = {"--option", "value", "arg"};

        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("option", true, "o", "Option description.");

        try
        {
            String[] parsedArgs = parser.flatten(options, args, true);
            fail("An exception should have been thrown.");
        }
        catch (ParseException e)
        {
            // This is the expected result
        }

        assertEquals(new String[]{"--option", "value"}, parser.getTokensToProcess());
        assertNull(parser.getRemainingArguments());
    }
}
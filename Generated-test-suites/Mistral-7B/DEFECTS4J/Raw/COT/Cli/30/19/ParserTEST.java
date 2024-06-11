import org.apache.commons.cli.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest
{
    @Test
    public void testParse() throws ParseException
    {
        // Test parsing with no options or arguments
        Options options = new Options();
        Parser parser = new DefaultParser();
        String[] arguments = new String[0];
        CommandLine cmd = parser.parse(options, arguments);
        assertNotNull(cmd);
        assertEquals(0, cmd.getArgs().size());

        // Test parsing with options
        options.addOption("o", "option", true, "Option value");
        arguments = new String[]{"-o", "value"};
        cmd = parser.parse(options, arguments);
        assertEquals("value", cmd.getOptionValue("option"));

        // Test parsing with multiple options
        options.addOption("h", "help", false, "Print help message");
        options.addOption("v", "verbose", false, "Print verbose output");
        arguments = new String[]{"-h", "-v"};
        cmd = parser.parse(options, arguments);
        assertTrue(cmd.hasOption("help"));
        assertTrue(cmd.hasOption("verbose"));

        // Test parsing with arguments
        options.addOption("a", "arg", true, "Argument value");
        arguments = new String[]{"-a", "argument"};
        cmd = parser.parse(options, arguments);
        assertEquals("argument", cmd.getArg(0));

        // Test parsing with multiple arguments
        arguments = new String[]{"arg1", "arg2"};
        cmd = parser.parse(options, arguments);
        assertEquals(2, cmd.getArgs().size());
        assertEquals("arg1", cmd.getArg(0));
        assertEquals("arg2", cmd.getArg(1));

        // Test parsing with required options
        options.addRequiredOption("r", "required", false, "A required option");
        arguments = new String[0];
        try
        {
            cmd = parser.parse(options, arguments);
            fail("Expected MissingOptionException");
        }
        catch (MissingOptionException e)
        {
            List<String> missingOptions = e.getMissingOptions();
            assertTrue(missingOptions.contains("required"));
        }

        // Test parsing with invalid option
        options.addOption("i", "invalid", false, "An invalid option");
        arguments = new String[]{"--invalid"};
        try
        {
            cmd = parser.parse(options, arguments);
            fail("Expected UnrecognizedOptionException");
        }
        catch (UnrecognizedOptionException e)
        {
            assertEquals("Unrecognized option: invalid", e.getMessage());
        }

        // Test parsing with properties
        Properties properties = new Properties();
        properties.setProperty("option1", "value1");
        properties.setProperty("option2", "value2");
        options.addOption("p1", "option1", false, "Option value from properties");
        options.addOption("p2", "option2", false, "Option value from properties");
        cmd = parser.parse(options, new String[0], properties);
        assertEquals("value1", cmd.getOptionValue("option1"));
        assertEquals("value2", cmd.getOptionValue("option2"));
    }
}
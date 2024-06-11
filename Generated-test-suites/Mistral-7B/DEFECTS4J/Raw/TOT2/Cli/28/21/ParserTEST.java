import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ParserTest
{
    @Test
    public void testParse() throws ParseException
    {
        // Given
        String[] args = new String[]{"--input", "path/to/file.txt", "-o", "output.csv", "--help", "-v"};
        Options options = new Options();
        options.addRequiredOption("input", true, "i", "Input file");
        options.addRequiredOption("o", true, "o", "Output file");
        options.addOption("help", false, "h", "Print this help message.");
        options.addOption("version", false, "v", "Print the version of Parser.");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        // When
        CommandLine cmd = parser.parse(options, args);

        // Then
        Assert.assertEquals("path/to/file.txt", cmd.getOptionValue("input"));
        Assert.assertEquals("output.csv", cmd.getOptionValue("o"));
        Assert.assertNull(cmd.getOptionValue("unknown"));
        Assert.assertEquals(0, cmd.getArgs().length);
        Assert.assertTrue(cmd.hasOption("help"));
        Assert.assertTrue(cmd.hasOption("version"));
    }

    @Test(expected = MissingArgumentException.class)
    public void testMissingRequiredArgument() throws ParseException
    {
        // Given
        String[] args = new String[]{"--input"};
        Options options = new Options();
        options.addRequiredOption("input", true, "i", "Input file");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        // When
        parser.parse(options, args, null, false);

        // Then
        // This test should throw a MissingArgumentException
    }

    @Test(expected = MissingOptionException.class)
    public void testMissingRequiredOption() throws ParseException
    {
        // Given
        String[] args = new String[]{"--unknown"};
        Options options = new Options();
        options.addRequiredOption("input", true, "i", "Input file");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        // When
        parser.parse(options, args, null, false);

        // Then
        // This test should throw a MissingOptionException
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testUnrecognizedOption() throws ParseException
    {
        // Given
        String[] args = new String[]{"--unknown"};
        Options options = new Options();
        options.addRequiredOption("input", true, "i", "Input file");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        // When
        parser.parse(options, args, null, false);

        // Then
        // This test should throw an UnrecognizedOptionException
    }

    @Test
    public void testParseWithProperties() throws ParseException
    {
        // Given
        String[] args = new String[]{"--input", "path/to/file.txt", "--output", "output.csv", "--log", "debug"};
        Properties properties = new Properties();
        properties.setProperty("logLevel", "debug");

        Options options = new Options();
        options.addRequiredOption("input", true, "i", "Input file");
        options.addRequiredOption("output", true, "o", "Output file");
        options.addOption("log", true, "l", "Log level");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        // When
        CommandLine cmd = parser.parse(options, args, properties, false);

        // Then
        Assert.assertEquals("path/to/file.txt", cmd.getOptionValue("input"));
        Assert.assertEquals("output.csv", cmd.getOptionValue("output"));
        Assert.assertEquals("debug", cmd.getOptionValue("log"));
        Assert.assertNull(cmd.getOptionValue("unknown"));
        Assert.assertEquals(0, cmd.getArgs().length);
        Assert.assertTrue(cmd.hasOption("log"));
    }

    @Test
    public void testParseWithStopAtNonOption() throws ParseException
    {
        // Given
        String[] args = new String[]{"--input", "path/to/file.txt", "unknown", "--output", "output.csv"};
        Options options = new Options();
        options.addRequiredOption("input", true, "i", "Input file");
        options.addRequiredOption("output", true, "o", "Output file");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        // When
        CommandLine cmd = parser.parse(options, args, null, true);

        // Then
        Assert.assertEquals("path/to/file.txt", cmd.getOptionValue("input"));
        Assert.assertEquals("output.csv", cmd.getOptionValue("output"));
        Assert.assertEquals(new String[]{"unknown"}, cmd.getArgs());
        Assert.assertFalse(cmd.hasOption("unknown"));
    }

    @Test
    public void testParseWithMultipleOptions() throws ParseException
    {
        // Given
        String[] args = new String[]{"--input", "path/to/file.txt", "--output", "output.csv", "--log", "debug", "--help"};
        Options options = new Options();
        options.addRequiredOption("input", true, "i", "Input file");
        options.addRequiredOption("output", true, "o", "Output file");
        options.addOption("log", true, "l", "Log level");
        options.addOption("help", false, "h", "Print this help message.");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        // When
        CommandLine cmd = parser.parse(options, args, null, false);

        // Then
        Assert.assertEquals("path/to/file.txt", cmd.getOptionValue("input"));
        Assert.assertEquals("output.csv", cmd.getOptionValue("output"));
        Assert.assertEquals("debug", cmd.getOptionValue("log"));
        Assert.assertTrue(cmd.hasOption("help"));
        Assert.assertFalse(cmd.hasOption("unknown"));
        Assert.assertEquals(0, cmd.getArgs().length);
    }
}
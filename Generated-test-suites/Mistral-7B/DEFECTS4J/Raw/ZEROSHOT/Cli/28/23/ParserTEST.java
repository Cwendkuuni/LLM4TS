import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class ParserTest
{
    private Parser parser;
    private Options options;

    @Before
    public void setUp() throws Exception
    {
        options = new Options();
        options.addOption("i", "input", true, "input file");
        options.addOption("o", "output", true, "output file");
        options.addOption("h", "help", false, "display help");
        options.addOptionGroup(new OptionGroup("bg", "Background")
                .addOption("b", "background", true, "background color")
                .addOption("B", "brightness", true, "background brightness"));

        parser = new DefaultParser();
        parser.setOptions(options);
    }

    @After
    public void tearDown() throws Exception
    {
        parser = null;
        options = null;
    }

    @Test
    public void testParse() throws Exception
    {
        String[] args = {"input.txt", "output.txt"};
        CommandLine cmd = parser.parse(options, args, null, false);

        assertEquals(2, cmd.getArgs().size());
        assertEquals("input.txt", cmd.getArgs().get(0));
        assertEquals("output.txt", cmd.getArgs().get(1));
        assertNotNull(cmd.getOption("h"));
        assertNull(cmd.getOption("h"));
        assertNull(cmd.getOption(" unknown"));
    }

    @Test
    public void testParseWithProperties() throws Exception
    {
        String[] args = {"input.txt", "--output=output.txt", "--help"};
        Properties properties = new Properties();
        properties.setProperty("output", "output2.txt");
        CommandLine cmd = parser.parse(options, args, properties, false);

        assertEquals(2, cmd.getArgs().size());
        assertEquals("input.txt", cmd.getArgs().get(0));
        assertEquals("output2.txt", cmd.getArgs().get(1));
        assertNotNull(cmd.getOption("h"));
        assertNull(cmd.getOption("h"));
        assertNull(cmd.getOption(" unknown"));
        assertEquals("output2.txt", cmd.getOptionValue("output"));
    }

    @Test
    public void testParseWithStopAtNonOption() throws Exception
    {
        String[] args = {"--", "input.txt", "output.txt"};
        CommandLine cmd = parser.parse(options, args, null, true);

        assertEquals(2, cmd.getArgs().size());
        assertEquals("input.txt", cmd.getArgs().get(0));
        assertEquals("output.txt", cmd.getArgs().get(1));
        assertNull(cmd.getOption("h"));
        assertNull(cmd.getOption("h"));
        assertNull(cmd.getOption(" unknown"));
    }

    @Test(expected = ParseException.class)
    public void testParseMissingOption() throws Exception
    {
        String[] args = {"input.txt"};
        parser.parse(options, args, null, false);
    }

    @Test(expected = ParseException.class)
    public void testParseMissingRequiredOption() throws Exception
    {
        String[] args = {"input.txt"};
        parser.parse(options, args, null, false);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseUnrecognizedOption() throws Exception
    {
        String[] args = {"--unknown"};
        parser.parse(options, args, null, false);
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseMissingOptionArgument() throws Exception
    {
        String[] args = {"--background"};
        parser.parse(options, args, null, false);
    }

    @Test(expected = MissingOptionException.class)
    public void testParseMissingRequiredOptions() throws Exception
    {
        String[] args = {"--input", "--output"};
        assertThrows(MissingOptionException.class, () -> parser.parse(options, args, null, false));
    }
}
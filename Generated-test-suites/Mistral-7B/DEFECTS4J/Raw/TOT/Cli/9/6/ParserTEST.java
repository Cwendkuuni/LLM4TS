import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ParserTest {

    private Options options;

    @Before
    public void setUp() {
        options = new Options();
        options.addRequiredOption("a", "answer", true, "Answer to the riddle");
        options.addOption("b", "boolean", false, "A boolean value");
    }

    @Test
    public void testParseWithRequiredOption() throws ParseException {
        String[] args = {"-a", "Forty-two"};
        CommandLine cmd = new Parser().parse(options, args);

        assertEquals("Forty-two", cmd.getOptionValue("answer"));
        assertNull(cmd.getOptionValue("boolean"));
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseMissingRequiredOption() throws ParseException {
        String[] args = {"-a"};
        new Parser().parse(options, args);
    }

    @Test
    public void testParseWithOptionalOption() throws ParseException {
        String[] args = {"-b", "true"};
        CommandLine cmd = new Parser().parse(options, args);

        assertEquals("true", cmd.getOptionValue("boolean"));
    }

    @Test
    public void testParseWithoutOption() throws ParseException {
        String[] args = {};
        CommandLine cmd = new Parser().parse(options, args);

        assertNull(cmd.getOptionValue("answer"));
        assertNull(cmd.getOptionValue("boolean"));
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseUnrecognizedOption() throws ParseException {
        String[] args = {"--x"};
        new Parser().parse(options, args);
    }

    @Test
    public void testParseWithMultipleOptions() throws ParseException {
        String[] args = {"-a", "Forty-two", "-b", "true"};
        CommandLine cmd = new Parser().parse(options, args);

        assertEquals("Forty-two", cmd.getOptionValue("answer"));
        assertEquals("true", cmd.getOptionValue("boolean"));
    }

    @Test(expected = MissingOptionException.class)
    public void testParseMissingRequiredOptions() throws ParseException {
        String[] args = {"-b"};
        new Parser().parse(options, args);
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Properties properties = new Properties();
        properties.setProperty("answer", "Forty-two");
        properties.setProperty("boolean", "true");

        CommandLine cmd = new Parser().parse(options, null, properties);

        assertEquals("Forty-two", cmd.getOptionValue("answer"));
        assertEquals("true", cmd.getOptionValue("boolean"));
    }
}
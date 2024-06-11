import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.*;

import static org.junit.Assert.*;

public class ParserTest {

    private Parser parser;
    private Options options;

    @Before
    public void setUp() {
        parser = new ParserImpl();
        options = new Options();

        // Adding options to test
        options.addOption(Option.builder("a").longOpt("arg").desc("Argument option").build());
        options.addOption(Option.builder("r").longOpt("required").desc("Required option").required().build());
        options.addOption(Option.builder("o").longOpt("optional").desc("Optional option").optionalArg(true).build());
    }

    @Test
    public void testParse_ArgumentProvided_ShouldReturnCorrectCommandLine() throws ParseException {
        String[] args = { "-a", "value" };

        CommandLine cmd = parser.parse(options, args);

        assertTrue(cmd.hasOption("a"));
        assertEquals("value", cmd.getOptionValue("a"));
    }

    @Test(expected = ParseException.class)
    public void testParse_InvalidOption_ThrowParseException() throws ParseException {
        String[] args = { "-b" };

        parser.parse(options, args);
    }

    @Test(expected = MissingOptionException.class)
    public void testParse_RequiredOptionNotProvided_ThrowMissingOptionException() throws ParseException {
        String[] args = { "-a", "value" };

        parser.parse(options, args);
    }

    @Test
    public void testParse_OptionalOptionWithoutArgument_ShouldReturnNullValue() throws ParseException {
        String[] args = { "-o" };

        CommandLine cmd = parser.parse(options, args);

        assertTrue(cmd.hasOption("o"));
        assertNull(cmd.getOptionValue("o"));
    }

    @Test
    public void testParse_OptionalOptionWithArgument_ShouldReturnCorrectValue() throws ParseException {
        String[] args = { "-o", "value" };

        CommandLine cmd = parser.parse(options, args);

        assertTrue(cmd.hasOption("o"));
        assertEquals("value", cmd.getOptionValue("o"));
    }
}
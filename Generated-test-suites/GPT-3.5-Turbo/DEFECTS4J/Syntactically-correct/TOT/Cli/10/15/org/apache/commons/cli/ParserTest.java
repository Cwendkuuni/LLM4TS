import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    private Options options;
    private Parser parser;

    @Before
    public void setup() {
        options = new Options();
        parser = new ParserImpl();
    }

    @Test
    public void testParseNoArguments() throws ParseException {
        CommandLine cmd = parser.parse(options, new String[0]);
        assertNotNull(cmd);
        assertTrue(cmd.getArgList().isEmpty());
        assertTrue(cmd.getOptions().isEmpty());
        assertTrue(cmd.hasOption("help"));
    }

    @Test
    public void testParseShortOptionWithValue() throws ParseException {
        options.addOption(Option.builder("v").build());
        CommandLine cmd = parser.parse(options, new String[]{"-v", "value"});
        assertNotNull(cmd);
        assertTrue(cmd.getArgList().isEmpty());
        assertTrue(cmd.hasOption("v"));
        assertTrue(cmd.getOptionValue("v").equals("value"));
    }

    @Test
    public void testParseLongOptionWithValue() throws ParseException {
        options.addOption(Option.builder("verbose").build());
        CommandLine cmd = parser.parse(options, new String[]{"--verbose", "true"});
        assertNotNull(cmd);
        assertTrue(cmd.getArgList().isEmpty());
        assertTrue(cmd.hasOption("verbose"));
        assertTrue(cmd.getOptionValue("verbose").equals("true"));
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseOptionMissingValue() throws ParseException {
        options.addOption(Option.builder("v").hasArg(true).build());
        parser.parse(options, new String[]{"-v"});
    }

    @Test
    public void testParseRequiredOptionPresent() throws ParseException {
        options.addRequiredOption(Option.builder("r").build());
        CommandLine cmd = parser.parse(options, new String[]{"-r"});
        assertNotNull(cmd);
        assertTrue(cmd.getArgList().isEmpty());
        assertTrue(cmd.hasOption("r"));
    }

    @Test(expected = MissingOptionException.class)
    public void testParseRequiredOptionMissing() throws ParseException {
        options.addRequiredOption(Option.builder("r").build());
        parser.parse(options, new String[0]);
    }

}
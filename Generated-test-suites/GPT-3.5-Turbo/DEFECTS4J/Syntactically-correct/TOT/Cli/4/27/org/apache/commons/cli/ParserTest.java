import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.*;

public class ParserTest {

    private Parser parser;

    @Before
    public void setUp() {
        parser = new Parser() {

            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return null;
            }

        };
    }

    @Test
    public void testParse_NoOptions_Success() throws ParseException {
        Options options = new Options();
        String[] args = new String[]{"arg1", "arg2"};

        CommandLine cmd = parser.parse(options, args);

        assertEquals(2, cmd.getArgs().length);
        assertEquals("arg1", cmd.getArgs()[0]);
        assertEquals("arg2", cmd.getArgs()[1]);
    }

    @Test
    public void testParse_SingleOptionWithArgument_Success() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder("o").argName("option").hasArg().build());
        String[] args = new String[]{"-o", "value"};

        CommandLine cmd = parser.parse(options, args);

        assertTrue(cmd.hasOption("o"));
        assertEquals("value", cmd.getOptionValue("o"));
    }

    @Test(expected = ParseException.class)
    public void testParse_MissingRequiredOption_ThrowsParseException() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder("o").required().build());
        String[] args = new String[]{};

        parser.parse(options, args);
    }


    @Test
    public void testParse_StopAtNonOption_Success() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder("o").argName("option").hasArg().build());
        String[] args = new String[]{"-o", "value", "arg1", "arg2"};

        CommandLine cmd = parser.parse(options, args, true);

        assertTrue(cmd.hasOption("o"));
        assertEquals("value", cmd.getOptionValue("o"));
        assertEquals(2, cmd.getArgs().length);
        assertEquals("arg1", cmd.getArgs()[0]);
        assertEquals("arg2", cmd.getArgs()[1]);
    }

    @Test
    public void testParse_OptionsWithProperties_Success() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder("o").argName("option").hasArg().build());
        String[] args = new String[]{"-o", "value"};

        Properties properties = new Properties();
        properties.setProperty("p1", "value1");
        properties.setProperty("p2", "value2");

        CommandLine cmd = parser.parse(options, args, properties);

        assertTrue(cmd.hasOption("o"));
        assertEquals("value", cmd.getOptionValue("o"));
        assertEquals(2, cmd.getOptionProperties("o").size());
        assertEquals("value1", cmd.getOptionProperties("o").getProperty("p1"));
        assertEquals("value2", cmd.getOptionProperties("o").getProperty("p2"));
    }

    @Test
    public void testParse_NonOptionAfterDoubleDash_Success() throws ParseException {
        Options options = new Options();
        String[] args = new String[]{"-o", "value", "--", "arg1", "arg2"};

        CommandLine cmd = parser.parse(options, args);

        assertTrue(cmd.hasOption("o"));
        assertEquals("value", cmd.getOptionValue("o"));
        assertEquals(2, cmd.getArgs().length);
        assertEquals("arg1", cmd.getArgs()[0]);
        assertEquals("arg2", cmd.getArgs()[1]);
    }

    @Test
    public void testParse_InvalidOption_ThrowsUnrecognizedOptionException() {
        Options options = new Options();
        String[] args = new String[]{"-x"};

        try {
            parser.parse(options, args);
            fail("Exception not thrown");
        } catch (ParseException e) {
            assertTrue(e instanceof UnrecognizedOptionException);
        }
    }

}
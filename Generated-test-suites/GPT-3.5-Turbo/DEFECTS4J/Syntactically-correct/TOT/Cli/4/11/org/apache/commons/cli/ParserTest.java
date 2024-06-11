import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParse_WithOptionsAndArguments_Success() {
        String[] args = {"-a", "value1", "-b", "value2"};
        Options options = new Options();
        Option optionA = new Option("a", true, "Option A");
        Option optionB = new Option("b", true, "Option B");
        options.addOption(optionA);
        options.addOption(optionB);

        Parser parser = new ParserImpl();
        CommandLine result = parser.parse(options, args);

        assertTrue(result.hasOption("a"));
        assertTrue(result.hasOption("b"));
        assertEquals("value1", result.getOptionValue("a"));
        assertEquals("value2", result.getOptionValue("b"));
    }

    @Test
    public void testParse_WithRequiredOptionAndArgument_Success() {
        String[] args = {"-a", "value1"};
        Options options = new Options();
        Option optionA = Option.builder("a")
                .required()
                .hasArg()
                .type(String.class)
                .desc("Option A")
                .build();
        options.addOption(optionA);

        Parser parser = new ParserImpl();
        CommandLine result = parser.parse(options, args);

        assertTrue(result.hasOption("a"));
        assertEquals("value1", result.getOptionValue("a"));
    }

    @Test(expected = MissingOptionException.class)
    public void testParse_WithMissingRequiredOption_ThrowsMissingOptionException() {
        String[] args = {};
        Options options = new Options();
        Option optionA = Option.builder("a")
                .required()
                .hasArg()
                .type(String.class)
                .desc("Option A")
                .build();
        options.addOption(optionA);

        Parser parser = new ParserImpl();
        parser.parse(options, args);
    }

    @Test
    public void testParse_WithStopAtNonOptionOption_StopAtNonOption() {
        String[] args = {"-a", "value1", "nonOption"};
        Options options = new Options();
        Option optionA = Option.builder("a")
                .hasArg()
                .type(String.class)
                .desc("Option A")
                .build();
        options.addOption(optionA);

        Parser parser = new ParserImpl();
        CommandLine result = parser.parse(options, args, true);

        assertTrue(result.hasOption("a"));
        assertEquals("value1", result.getOptionValue("a"));
        assertEquals("nonOption", result.getArgList().get(0));
    }

}
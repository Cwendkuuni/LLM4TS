import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ParserTest {

    private Parser parser;
    private Options options;
    
    @Before
    public void setUp() {
        parser = new ParserImpl();
        options = new Options();
        Option option1 = new Option("f", "file", true, "input file");
        Option option2 = new Option("o", "output", true, "output file");
        options.addOption(option1);
        options.addOption(option2);
    }
    
    @Test
    public void testParseNoOptions() throws ParseException {
        String[] args = new String[] {};
        CommandLine cmd = parser.parse(options, args);
        assertTrue(cmd.getOptions().length == 0);
        assertTrue(cmd.getArgs().length == 0);
    }
    
    @Test
    public void testParseOptionWithValue() throws ParseException {
        String[] args = new String[] {"-f", "input.txt"};
        CommandLine cmd = parser.parse(options, args);
        assertTrue(cmd.hasOption("f"));
        assertTrue(cmd.getOptionValue("f").equals("input.txt"));
        assertTrue(cmd.getArgs().length == 0);
    }
    
    @Test
    public void testParseMultipleOptions() throws ParseException {
        String[] args = new String[] {"-f", "input.txt", "-o", "output.txt"};
        CommandLine cmd = parser.parse(options, args);
        assertTrue(cmd.hasOption("f"));
        assertTrue(cmd.getOptionValue("f").equals("input.txt"));
        assertTrue(cmd.hasOption("o"));
        assertTrue(cmd.getOptionValue("o").equals("output.txt"));
        assertTrue(cmd.getArgs().length == 0);
    }
    
    @Test(expected = MissingArgumentException.class)
    public void testParseOptionMissingValue() throws ParseException {
        String[] args = new String[] {"-f"};
        parser.parse(options, args);
    }
    
    @Test(expected = UnrecognizedOptionException.class)
    public void testParseUnrecognizedOption() throws ParseException {
        String[] args = new String[] {"-a"};
        parser.parse(options, args);
    }
    
    @Test(expected = MissingOptionException.class)
    public void testParseMissingRequiredOption() throws ParseException {
        options.getOption("f").setRequired(true);
        String[] args = new String[] {};
        parser.parse(options, args);
    }
    
    @Test
    public void testParseStopAtNonOption() throws ParseException {
        String[] args = new String[] {"-f", "input.txt", "arg1", "arg2"};
        CommandLine cmd = parser.parse(options, args, true);
        assertTrue(cmd.hasOption("f"));
        assertTrue(cmd.getOptionValue("f").equals("input.txt"));
        assertTrue(cmd.getArgs().length == 2);
        assertTrue(cmd.getArgs()[0].equals("arg1"));
        assertTrue(cmd.getArgs()[1].equals("arg2"));
    }
    
    // Additional test cases for edge cases and exception handling scenarios
    
    @Test
    public void testParseOptionWithOptionalValue() throws ParseException {
        options.getOption("f").setOptionalArg(true);
        String[] args = new String[] {"-f"};
        CommandLine cmd = parser.parse(options, args);
        assertTrue(cmd.hasOption("f"));
        assertNull(cmd.getOptionValue("f"));
        assertTrue(cmd.getArgs().length == 0);
    }
    
    @Test(expected = MissingArgumentException.class)
    public void testParseOptionWithMissingOptionalValue() throws ParseException {
        options.getOption("f").setOptionalArg(true);
        String[] args = new String[] {"-f", "-o"};
        parser.parse(options, args);
    }
    
    @Test
    public void testParseLongOption() throws ParseException {
        String[] args = new String[] {"--file", "input.txt"};
        CommandLine cmd = parser.parse(options, args);
        assertTrue(cmd.hasOption("file"));
        assertTrue(cmd.getOptionValue("file").equals("input.txt"));
        assertTrue(cmd.getArgs().length == 0);
    }
    
    @Test
    public void testParseMultipleLongOptions() throws ParseException {
        String[] args = new String[] {"--file", "input.txt", "--output", "output.txt"};
        CommandLine cmd = parser.parse(options, args);
        assertTrue(cmd.hasOption("file"));
        assertTrue(cmd.getOptionValue("file").equals("input.txt"));
        assertTrue(cmd.hasOption("output"));
        assertTrue(cmd.getOptionValue("output").equals("output.txt"));
        assertTrue(cmd.getArgs().length == 0);
    }
    
    @Test(expected = MissingArgumentException.class)
    public void testParseLongOptionMissingValue() throws ParseException {
        String[] args = new String[] {"--file"};
        parser.parse(options, args);
    }
    
    @Test(expected = UnrecognizedOptionException.class)
    public void testParseUnrecognizedLongOption() throws ParseException {
        String[] args = new String[] {"--input"};
        parser.parse(options, args);
    }
    
    @Test(expected = MissingOptionException.class)
    public void testParseMissingRequiredLongOption() throws ParseException {
        options.getOption("file").setRequired(true);
        String[] args = new String[] {};
        parser.parse(options, args);
    }
    
    @Test
    public void testParseStopAtNonOptionLongOption() throws ParseException {
        String[] args = new String[] {"--file", "input.txt", "arg1", "arg2"};
        CommandLine cmd = parser.parse(options, args, true);
        assertTrue(cmd.hasOption("file"));
        assertTrue(cmd.getOptionValue("file").equals("input.txt"));
        assertTrue(cmd.getArgs().length == 2);
        assertTrue(cmd.getArgs()[0].equals("arg1"));
        assertTrue(cmd.getArgs()[1].equals("arg2"));
    }
    
    
    // All test cases are merged into this single complete JUnit test file
    
}
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.*;

import static org.junit.Assert.*;

public class PosixParserTest {

    private PosixParser parser;

    @Before
    public void setup() {
        parser = new PosixParser();
    }

    // Test for flatten method

    // Typical use case: flattening multiple options with arguments
    @Test
    public void testFlatten_Typical() {
        Options options = new Options();
        options.addOption(Option.builder("a").hasArg().build());
        options.addOption(Option.builder("b").hasArg().build());
        
        String[] arguments = {"-a", "testA", "-b", "testB"};
        
        String[] result = parser.flatten(options, arguments, false);
        
        assertEquals(4, result.length);
        assertEquals("-a", result[0]);
        assertEquals("testA", result[1]);
        assertEquals("-b", result[2]);
        assertEquals("testB", result[3]);
    }

    // Edge case: flattening single option with argument
    @Test
    public void testFlatten_SingleOptionWithArgument() {
        Options options = new Options();
        options.addOption(Option.builder("a").hasArg().build());

        String[] arguments = {"-a", "testA"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(2, result.length);
        assertEquals("-a", result[0]);
        assertEquals("testA", result[1]);
    }

    // Edge case: flattening single option without argument
    @Test
    public void testFlatten_SingleOptionWithoutArgument() {
        Options options = new Options();
        options.addOption(Option.builder("a").hasArg(false).build());

        String[] arguments = {"-a"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(1, result.length);
        assertEquals("-a", result[0]);
    }

    // Error scenario: flattening invalid option
    @Test(expected = UnrecognizedOptionException.class)
    public void testFlatten_InvalidOption() {
        Options options = new Options();
        options.addOption(Option.builder("a").hasArg().build());

        String[] arguments = {"-b", "testB"};

        parser.flatten(options, arguments, false);
    }

    // Test for processOptionToken method

    // Typical use case: valid option provided
    @Test
    public void testProcessOptionToken_ValidOption() {
        Options options = new Options();
        options.addOption(Option.builder("a").hasArg().build());

        parser.processOptionToken("-a", false);

        assertNotNull(parser.currentOption);
        assertEquals("a", parser.currentOption.getOpt());
    }

    // Edge case: stopAtNonOption is true
    @Test
    public void testProcessOptionToken_StopAtNonOption() {
        Options options = new Options();
        options.addOption(Option.builder("a").hasArg().build());

        parser.processOptionToken("-b", true);

        assertNull(parser.currentOption);
        assertTrue(parser.eatTheRest);
    }

    // Test for process method

    // Typical use case: current option exists and can have argument value
    @Test
    public void testProcess_CurrentOptionWithArg() {
        Options options = new Options();
        options.addOption(Option.builder("a").hasArg().build());

        parser.currentOption = options.getOption("a");

        parser.process("testA");

        assertNull(parser.currentOption);
        assertFalse(parser.eatTheRest);

        String[] result = parser.tokens.toArray(new String[0]);
        assertEquals(1, result.length);
        assertEquals("testA", result[0]);
    }

    // Typical use case: current option exists and can have argument values
    @Test
    public void testProcess_CurrentOptionWithArgs() {
        Options options = new Options();
        options.addOption(Option.builder("a").hasArgs().build());

        parser.currentOption = options.getOption("a");

        parser.process("testA");

        assertNotNull(parser.currentOption);
        assertTrue(parser.currentOption.hasArgs());
        assertFalse(parser.eatTheRest);

        String[] result = parser.tokens.toArray(new String[0]);
        assertEquals(1, result.length);
        assertEquals("testA", result[0]);
    }

    // Typical use case: current option does not exist and stopAtNonOption is true
    @Test
    public void testProcess_CurrentOptionNotExists_StopAtNonOption() {
        Options options = new Options();
        options.addOption(Option.builder("a").hasArg().build());

        parser.stopAtNonOption = true;

        parser.process("testA");

        assertTrue(parser.eatTheRest);

        String[] result = parser.tokens.toArray(new String[0]);
        assertEquals(2, result.length);
        assertEquals("--", result[0]);
        assertEquals("testA", result[1]);
    }

    // Typical use case: current option does not exist and stopAtNonOption is false
    @Test
    public void testProcess_CurrentOptionNotExists_NotStopAtNonOption() {
        Options options = new Options();
        options.addOption(Option.builder("a").hasArg().build());

        parser.stopAtNonOption = false;

        parser.process("testA");

        assertFalse(parser.eatTheRest);

        String[] result = parser.tokens.toArray(new String[0]);
        assertEquals(1, result.length);
        assertEquals("testA", result[0]);
    }

    // Test for burstToken method

    // Typical use case: option exists with the current character
    @Test
    public void testBurstToken_OptionExists() {
        Options options = new Options();
        options.addOption(Option.builder("a").hasArg().build());

        parser.burstToken("-ab", false);

        assertNotNull(parser.currentOption);
        assertEquals("a", parser.currentOption.getOpt());

        String[] result = parser.tokens.toArray(new String[0]);
        assertEquals(2, result.length);
        assertEquals("-a", result[0]);
        assertEquals("b", result[1]);
    }

    // Typical use case: option exists with the current character and has additional arguments
    @Test
    public void testBurstToken_OptionExistsWithArgs() {
        Options options = new Options();
        options.addOption(Option.builder("a").hasArgs().build());

        parser.burstToken("-ab", false);

        assertNotNull(parser.currentOption);
        assertTrue(parser.currentOption.hasArgs());

        String[] result = parser.tokens.toArray(new String[0]);
        assertEquals(2, result.length);
        assertEquals("-a", result[0]);
        assertEquals("b", result[1]);
    }

    // Edge case: stopAtNonOption is true
    @Test
    public void testBurstToken_StopAtNonOption() {
        Options options = new Options();
        options.addOption(Option.builder("a").hasArg().build());

        parser.stopAtNonOption = true;

        parser.burstToken("-ab", true);

        assertNull(parser.currentOption);
        assertTrue(parser.eatTheRest);

        String[] result = parser.tokens.toArray(new String[0]);
        assertEquals(1, result.length);
        assertEquals("-ab", result[0]);
    }

}
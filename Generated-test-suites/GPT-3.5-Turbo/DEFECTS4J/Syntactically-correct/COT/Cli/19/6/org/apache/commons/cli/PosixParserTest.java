
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    private PosixParser parser = new PosixParser();

    // Test flatten method

    @Test
    public void testFlatten() {
        Options options = new Options();
        String[] arguments = {"-a", "value1", "--b=value2", "arg1", "-c"};
        String[] expected = {"-a", "value1", "--b", "value2", "--", "arg1", "-c"};
        assertArrayEquals(expected, parser.flatten(options, arguments, false));
    }

    // Test init method

    @Test
    public void testInit() {
        parser.init();
        
        assertFalse(parser.eatTheRest);
        assertEquals(null, parser.currentOption);
        assertEquals(0, parser.tokens.size());
    }

    // Test process method

    @Test
    public void testProcessNoOption() {
        parser.currentOption = null;
        parser.process("arg1");

        assertTrue(parser.eatTheRest);
        assertArrayEquals(new String[]{"--", "arg1"}, parser.tokens.toArray());
    }

    @Test
    public void testProcessOptionWithArg() {
        Option option = new Option("a", true);
        parser.currentOption = option;
        parser.process("value1");

        assertEquals(null, parser.currentOption);
        assertArrayEquals(new String[]{"value1"}, parser.tokens.toArray());
    }

    @Test
    public void testProcessOptionWithArgs() {
        Option option = new Option("a", true);
        option.setArgs(2);
        parser.currentOption = option;
        parser.process("value1");

        assertEquals(null, parser.currentOption);
        assertArrayEquals(new String[]{"value1"}, parser.tokens.toArray());
    }

    // Test processOptionToken method

    @Test
    public void testProcessOptionTokenValidOption() {
        Options options = new Options();
        Option optionA = new Option("a", true);
        options.addOption(optionA);

        parser.options = options;
        parser.processOptionToken("-a", false);

        assertEquals(optionA, parser.currentOption);
        assertArrayEquals(new String[]{"-a"}, parser.tokens.toArray());
    }

    @Test
    public void testProcessOptionTokenInvalidOptionStopAtNonOption() {
        Options options = new Options();
        Option optionA = new Option("a", true);
        options.addOption(optionA);

        parser.options = options;
        parser.processOptionToken("-b", true);

        assertTrue(parser.eatTheRest);
        assertArrayEquals(new String[]{"-b"}, parser.tokens.toArray());
    }

    // Test burstToken method

    @Test
    public void testBurstTokenValidOption() {
        Options options = new Options();
        Option optionA = new Option("a", true);
        options.addOption(optionA);
        Option optionB = new Option("b");
        options.addOption(optionB);

        parser.options = options;
        parser.burstToken("-ab", false);

        assertEquals(optionA, parser.currentOption);
        assertArrayEquals(new String[]{"-a", "-b"}, parser.tokens.toArray());
    }

    @Test
    public void testBurstTokenValidOptionWithArg() {
        Options options = new Options();
        Option optionA = new Option("a", true);
        options.addOption(optionA);
        Option optionB = new Option("b");
        options.addOption(optionB);

        parser.options = options;
        parser.burstToken("-avalue1", false);

        assertEquals(null, parser.currentOption);
        assertArrayEquals(new String[]{"-a", "value1"}, parser.tokens.toArray());
    }

    @Test
    public void testBurstTokenInvalidOptionStopAtNonOption() {
        Options options = new Options();
        Option optionA = new Option("a", true);
        options.addOption(optionA);

        parser.options = options;
        parser.burstToken("-bc", true);

        assertTrue(parser.eatTheRest);
        assertArrayEquals(new String[]{"-bc"}, parser.tokens.toArray());
    }

}

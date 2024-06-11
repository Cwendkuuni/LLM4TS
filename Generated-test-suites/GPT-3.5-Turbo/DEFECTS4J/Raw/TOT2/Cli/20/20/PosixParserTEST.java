import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli.*;

public class PosixParserTest {

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        parser.init();
        assertEquals(parser.getTokens().size(), 0);
        assertEquals(parser.getCurrentOption(), null);
        assertFalse(parser.isEatTheRest());
    }

    @Test
    public void testFlatten() {
        Options options = new Options();
        Option option1 = new Option("a", "option a");
        Option option2 = new Option("b", "option b");
        options.addOption(option1);
        options.addOption(option2);

        PosixParser parser = new PosixParser();

        String[] arguments = {"-a", "value1", "-b=value2", "--option3", "value3"};
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(result.length, 5);
        assertEquals(result[0], "-a");
        assertEquals(result[1], "value1");
        assertEquals(result[2], "-b");
        assertEquals(result[3], "value2");
        assertEquals(result[4], "--option3");
    }

    @Test
    public void testGobble() {
        PosixParser parser = new PosixParser();

        Iterator<String> iter = Arrays.asList("arg1", "arg2", "arg3").iterator();
        parser.gobble(iter);

        assertEquals(parser.getTokens().size(), 0);
    }

    @Test
    public void testProcessOptionToken() {
        Options options = new Options();
        Option option1 = new Option("a", "option a");
        options.addOption(option1);

        PosixParser parser = new PosixParser();

        parser.processOptionToken("-a", false);

        assertEquals(parser.getCurrentOption(), option1);
        assertEquals(1, parser.getTokens().size());
    }

    @Test
    public void testProcess() {
        Options options = new Options();
        Option option1 = new Option("a", "option a");
        options.addOption(option1);

        PosixParser parser = new PosixParser();

        parser.process("value");

        assertNull(parser.getCurrentOption());
        assertEquals(parser.isEatTheRest(), true);
        assertEquals(parser.getTokens().size(), 2);
    }

    @Test
    public void testBurstToken() {
        Options options = new Options();
        Option option1 = new Option("a", "option a");
        options.addOption(option1);

        PosixParser parser = new PosixParser();

        parser.burstToken("-ab", false);

        assertEquals(parser.getTokens().size(), 1);
    }

}
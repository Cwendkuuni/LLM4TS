import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        parser.init();
        assertTrue(parser.getTokens().isEmpty());
        assertFalse(parser.isEatTheRest());
        assertNull(parser.getCurrentOption());
    }

    @Test
    public void testFlatten() {
        Options options = new Options();
        Option option = new Option("a", "test option");
        option.setArgs(2);
        options.addOption(option);

        String[] arguments = {"-a", "arg1", "arg2"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, arguments, false);

        assertEquals(3, parser.getTokens().size());
    }

    @Test
    public void testProcessOptionToken() {
        Options options = new Options();
        Option option = new Option("a", "test option");
        options.addOption(option);

        PosixParser parser = new PosixParser();
        boolean stopAtNonOption = false;
        String token = "-a";

        parser.processOptionToken(token, stopAtNonOption);
        assertEquals(option, parser.getCurrentOption());
        assertEquals(1, parser.getTokens().size());
    }

    @Test
    public void testBurstToken() {
        Options options = new Options();
        Option option = new Option("a", "test option");
        options.addOption(option);

        PosixParser parser = new PosixParser();
        boolean stopAtNonOption = false;
        String token = "-ab";

        parser.burstToken(token, stopAtNonOption);
        assertEquals(2, parser.getTokens().size());
        assertNull(parser.getCurrentOption());
    }

}
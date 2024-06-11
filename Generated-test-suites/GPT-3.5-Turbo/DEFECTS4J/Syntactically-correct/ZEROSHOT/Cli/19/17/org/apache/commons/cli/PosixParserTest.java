import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class PosixParserTest {

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption(new Option("v", false, "Enable verbose mode"));
        options.addOption(new Option("f", true, "Input file"));

        PosixParser parser = new PosixParser();

        String[] args = {"-v", "-f", "test.txt"};

        String[] result = parser.flatten(options, args, false);

        assertEquals("-v", result[0]);
        assertEquals("-f", result[1]);
        assertEquals("test.txt", result[2]);
    }

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();

        parser.init();

        assertEquals(parser.isEatTheRest(), false);
        assertEquals(parser.getTokens().size(), 0);
        assertEquals(parser.getCurrentOption(), null);
    }

    @Test
    public void testProcessOptionToken() {
        Options options = new Options();
        options.addOption(new Option("v", false, "Enable verbose mode"));

        PosixParser parser = new PosixParser();
        parser.setOptions(options);

        String token = "-v";
        boolean stopAtNonOption = false;

        parser.processOptionToken(token, stopAtNonOption);

        assertEquals(parser.getCurrentOption().getOpt(), "v");
        assertEquals(parser.getTokens().get(0), token);
    }

    @Test
    public void testBurstToken() {
        Options options = new Options();
        options.addOption(new Option("v", false, "Enable verbose mode"));

        PosixParser parser = new PosixParser();
        parser.setOptions(options);

        String token = "-vf";
        boolean stopAtNonOption = false;

        parser.burstToken(token, stopAtNonOption);

        assertEquals(parser.getTokens().get(0), "-v");
        assertEquals(parser.getTokens().get(1), "-f");
    }

    @Test
    public void testProcess() {
        Options options = new Options();
        options.addOption(new Option("f", true, "Input file"));

        PosixParser parser = new PosixParser();
        parser.setOptions(options);

        String value = "test.txt";

        parser.process(value);

        assertEquals(parser.getTokens().get(0), "--");
        assertEquals(parser.getTokens().get(1), value);
    }
}
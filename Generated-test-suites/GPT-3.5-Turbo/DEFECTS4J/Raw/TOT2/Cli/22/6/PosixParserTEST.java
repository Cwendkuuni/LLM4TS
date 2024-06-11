import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;

public class PosixParserTest {

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        parser.init();
        assertTrue(parser.getTokens().isEmpty());
        assertFalse(parser.isEatTheRest());
    }

    @Test
    public void testProcessNonOptionToken() {
        PosixParser parser = new PosixParser();
        parser.processNonOptionToken("test");
        assertTrue(parser.isEatTheRest());
        assertEquals(parser.getTokens().get(0), "--");
        assertEquals(parser.getTokens().get(1), "test");
    }

    @Test
    public void testProcessOptionToken() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("t", false, "test option");
        parser.setOptions(options);
        parser.processOptionToken("-t", false);
        assertEquals(parser.getTokens().get(0), "-t");
    }

    @Test
    public void testBurstToken() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b");
        parser.setOptions(options);
        parser.burstToken("-ab", false);
        assertEquals(parser.getTokens().get(0), "-a");
        assertEquals(parser.getTokens().get(1), "-b");
    }

    @Test
    public void testFlatten() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("t", true, "test option");
        parser.setOptions(options);
        String[] arguments = {"-t", "value"};
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(result[0], "-t");
        assertEquals(result[1], "value");
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithInvalidOption() throws ParseException {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("t", false, "test option");
        parser.setOptions(options);
        String[] arguments = {"-invalid"};
        parser.flatten(options, arguments, false);
    }

}
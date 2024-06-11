import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class PosixParserTest {
    private static final Options options = new Options();

    @Test
    public void testFlatten() {
        assertEquals(new String[]{"-a", "--", "file1", "file2"}, PosixParser.flatten(options, new String[]{"-a", "--", "file1", "file2"}, true));
        assertEquals(new String[]{"--name=value"}, PosixParser.flatten(options, new String[]{"--name=value"}, true));
    }

    @Test
    public void testProcess() {
        PosixParser parser = new PosixParser();
        parser.process("file1");
        assertEquals(new String[]{"--", "file1"}, parser.processedTokens.toArray());
    }

    @Test
    public void testProcessSingleHyphen() {
        PosixParser parser = new PosixParser();
        parser.processSingleHyphen("-");
        assertEquals(new String[]{"-"}, parser.processedTokens.toArray());
    }

    @Test
    public void testProcessOptionToken() {
        PosixParser parser = new PosixParser();
        parser.options.addOption(new Option("a", false, ""));
        parser.processOptionToken("-a", true);
        assertEquals(new String[]{"-a"}, parser.processedTokens.toArray());
    }

    @Test
    public void testBurstToken() {
        PosixParser parser = new PosixParser();
        parser.options.addOption(new Option("a", false, ""));
        parser.burstToken("-abc", true);
        assertEquals(new String[]{"-a", "-b", "-c"}, parser.processedTokens.toArray());
    }
}
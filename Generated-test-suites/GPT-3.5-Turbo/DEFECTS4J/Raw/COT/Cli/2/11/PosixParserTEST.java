
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testFlatten() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");
        options.addOption("b", "optionB", true, "Option B");
        String[] args = new String[]{"-a", "-b", "value"};
        String[] result = parser.flatten(options, args, false);
        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("value", result[2]);
    }

    @Test
    public void testProcessSingleHyphen() {
        PosixParser parser = new PosixParser();
        ArrayList<String> tokens = new ArrayList<>();
        parser.processSingleHyphen("-", tokens);
        assertEquals(1, tokens.size());
        assertEquals("-", tokens.get(0));
    }

    @Test
    public void testProcessOptionToken() {
        PosixParser parser = new PosixParser();
        ArrayList<String> tokens = new ArrayList<>();
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");
        parser.processOptionToken("-a", tokens, options);
        assertEquals(1, tokens.size());
        assertEquals("-a", tokens.get(0));
    }

    @Test
    public void testBurstToken() {
        PosixParser parser = new PosixParser();
        ArrayList<String> tokens = new ArrayList<>();
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");
        parser.burstToken("-abc", tokens, options);
        assertEquals(3, tokens.size());
        assertEquals("-a", tokens.get(0));
        assertEquals("-b", tokens.get(1));
        assertEquals("-c", tokens.get(2));
    }

    @Test
    public void testProcess() {
        PosixParser parser = new PosixParser();
        ArrayList<String> tokens = new ArrayList<>();
        parser.process("value", tokens);
        assertEquals(1, tokens.size());
        assertEquals("value", tokens.get(0));
    }

    @Test
    public void testGobble() {
        PosixParser parser = new PosixParser();
        ArrayList<String> tokens = new ArrayList<>();
        Iterator<String> iter = Arrays.asList(new String[]{"value1", "value2", "value3"}).iterator();
        parser.eatTheRest = true;
        parser.gobble(iter, tokens);
        assertEquals(3, tokens.size());
        assertEquals("value1", tokens.get(0));
        assertEquals("value2", tokens.get(1));
        assertEquals("value3", tokens.get(2));
    }
}
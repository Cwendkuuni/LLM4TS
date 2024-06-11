import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.PosixParser;

public class PosixParserTest {
    private PosixParser parser;

    @Before
    public void setup() {
        parser = new PosixParser();
    }

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption(new Option("f", "file", true, "input file"));
        options.addOption(new Option("o", "output", false, "output to console"));

        String[] arguments = {"-f", "input.txt", "-o"};
        String[] processedTokens = parser.flatten(options, arguments, true);

        assertEquals(4, processedTokens.length);
        assertEquals("-f", processedTokens[0]);
        assertEquals("input.txt", processedTokens[1]);
        assertEquals("-o", processedTokens[2]);
        assertNull(processedTokens[3]);
    }
}
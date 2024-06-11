import org.junit.Test;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;

public class PosixParserTest {

    @Test
    public void testFlatten() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String[] arguments = {"--option1", "value1", "--", "--option2=value2"};
        
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(4, result.length);
        assertEquals("--option1", result[0]);
        assertEquals("value1", result[1]);
        assertEquals("--", result[2]);
        assertEquals("--option2=value2", result[3]);
    }
}
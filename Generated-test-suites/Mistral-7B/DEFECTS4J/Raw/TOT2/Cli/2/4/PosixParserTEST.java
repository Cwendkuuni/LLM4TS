import org.apache.commons.cli.Options;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.PosixParser;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

public class PosixParserTest {
    @Test
    public void testFlatten() {
        Options options = new Options();
        String[] arguments = new String[] {"--option1", "-o", "value1", "--option2", "-o", "value2", "arg1", "arg2"};
        PosixParser parser = new PosixParser();
        String[] expected = new String[] {"--option1", "-o", "value1", "--option2", "-o", "value2", "arg1", "arg2"};
        String[] actual = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, actual);
    }
}
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testFlatten() throws ParseException {
        String[] arguments = {"--foo", "-ab", "-c", "--d=e", "-ef"};
        Options options = new Options();
        options.addOption("foo", true, "Help flag");
        options.addOption("a", true, "Option A");
        options.addOption("c", true, "Option C");
        options.addOption("d", true, "Option D");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(new String[]{"--foo", "-a", "--d", "e", "-c", "-e", "f"}, result);
    }
}
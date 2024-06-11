import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.cli.Options;
import org.junit.Test;

public class PosixParserTest {
    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b");
        options.addOption("c", false, "option c");

        PosixParser parser = new PosixParser();
        String[] arguments = {"-a", "-b", "123", "-c"};

        String[] result = parser.flatten(options, arguments, true);

        String[] expected = {"-a", "-b", "123", "-c"};
        assertArrayEquals(expected, result);
    }
}
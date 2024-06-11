import org.apache.commons.cli.Options;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", false, "Option B");
        options.addOption("c", 1, "Option C");
        options.addOption("d", 2, "Option D");
        options.addOption("e", 3, "Option E");

        PosixParser parser = new PosixParser();
        String[] args = {"--a", "--b", "-c", "1", "--d=2", "--e=3", "-f", "-g", "--h=Test"};
        String[] expected = {"--a", "-c", "1", "--d", "2", "--e", "3", "-f", "-g", "--h", "Test"};

        String[] actual = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, actual);

        // test with stopAtNonOption=true
        String[] actualStopAtNonOption = parser.flatten(options, args, true);
        List<String> listExpected = Arrays.asList(Arrays.copyOfRange(expected, 0, 9));
        Assert.assertArrayEquals(listExpected.toArray(new String[0]), actualStopAtNonOption);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidOption() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] args = {"--invalidOption"};
        parser.flatten(options, args, false);
    }
}
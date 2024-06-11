import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PosixParserTest {

    private PosixParser parser;

    @Before
    public void setup() {
        parser = new PosixParser();
    }

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");

        String[] arguments = {"--a", "-b", "value"};

        String[] result = parser.flatten(options, arguments, false);

        Assert.assertEquals("--a", result[0]);
        Assert.assertEquals("-b", result[1]);
        Assert.assertEquals("value", result[2]);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", false, "Option B");

        String[] arguments = {"--a", "-b", "value"};

        String[] result = parser.flatten(options, arguments, true);

        Assert.assertEquals("--a", result[0]);
        Assert.assertEquals("-b", result[1]);
    }

    @Test
    public void testFlattenNoOptions() {
        Options options = new Options();

        String[] arguments = {"--a", "-b", "value"};

        String[] result = parser.flatten(options, arguments, true);

        Assert.assertEquals("--a", result[0]);
        Assert.assertEquals("-b", result[1]);
        Assert.assertEquals("value", result[2]);
    }
}
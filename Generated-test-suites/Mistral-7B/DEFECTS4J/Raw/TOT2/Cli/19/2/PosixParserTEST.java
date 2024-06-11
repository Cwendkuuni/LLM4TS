import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * Test cases for PosixParser class.
 */
public class PosixParserTest {

    @Test
    public void testFlattenPositive() {
        String[] args = {"--option1", "-o1", "--option2=value2", "-o2", "--", "-a", "-b"};
        Options options = new Options();
        options.addOption("option1", true, "o1");
        options.addOption("option2", true, "o2");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        String[] expected = {"--option1", "-o1", "--option2", "value2", "--", "-a", "-b"};
        Assert.assertArrayEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenNegative() {
        String[] args = {"--option1", "-o1", "--option2=value2", "-o2", "--invalid", "invalid"};
        Options options = new Options();
        options.addOption("option1", true, "o1");
        options.addOption("option2", true, "o2");

        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    @Test
    public void testProcessOptionToken() {
        PosixParser parser = new PosixParser();
        String[] args = {"--option1=value1", "--option2"};
        Options options = new Options();
        options.addOption("option1", true, "o1");
        options.addOption("option2");

        parser.flatten(options, args, false);

        Assert.assertEquals(2, parser.tokens.size());
        Assert.assertEquals("--option1", parser.tokens.get(0));
        Assert.assertEquals("value1", parser.tokens.get(1));
    }

    @Test
    public void testBurstToken() {
        String token = "-o1-option1-value1";
        PosixParser parser = new PosixParser();

        parser.burstToken(token, false);

        List tokens = (List) parser.tokens;
        Assert.assertEquals(3, tokens.size());
        Assert.assertEquals("-o1", tokens.get(0));
        Assert.assertEquals("option1", tokens.get(1));
        Assert.assertEquals("value1", tokens.get(2));
    }

    @Test
    public void testGobble() {
        String[] args = {"--option1", "--", "invalid"};
        PosixParser parser = new PosixParser();
        parser.flatten(new Options(), args, true);

        Assert.assertEquals(2, parser.tokens.size());
        Assert.assertEquals("--option1", parser.tokens.get(0));
        Assert.assertEquals("--", parser.tokens.get(1));
    }
}
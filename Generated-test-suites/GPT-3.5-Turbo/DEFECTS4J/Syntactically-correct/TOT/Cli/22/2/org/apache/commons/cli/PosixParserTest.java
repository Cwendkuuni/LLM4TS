import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.cli.Options;

public class PosixParserTest {

    private Options options;

    @Test
    public void testFlatten_StopAtNonOption_True() {
        // Setup
        String[] arguments = {"-a", "value1", "nonOption1", "-b", "nonOption2"};
        PosixParser parser = new PosixParser();
        options = new Options();

        // Exercise
        String[] result = parser.flatten(options, arguments, true);

        // Verify
        Assert.assertArrayEquals(result, new String[]{"-a", "value1", "--", "nonOption1", "-b", "nonOption2"});
    }

    @Test
    public void testFlatten_StopAtNonOption_False() {
        // Setup
        String[] arguments = {"-a", "value1", "nonOption1", "-b", "nonOption2"};
        PosixParser parser = new PosixParser();
        options = new Options();

        // Exercise
        String[] result = parser.flatten(options, arguments, false);

        // Verify
        Assert.assertArrayEquals(result, new String[]{"-a", "value1", "nonOption1", "-b", "nonOption2"});
    }

    @Test
    public void testBurstToken_ValidOption() {
        // Setup
        String token = "-abc";
        PosixParser parser = new PosixParser();
        options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", false, "Option b");
        options.addOption("c", false, "Option c");

        // Exercise
        parser.burstToken(token, false);

        // Verify
        Assert.assertArrayEquals(parser.getTokens().toArray(), new String[]{"-a", "-b", "-c"});
    }

    @Test
    public void testBurstToken_InvalidOption_StopAtNonOption_True() {
        // Setup
        String token = "-def";
        PosixParser parser = new PosixParser();
        options = new Options();

        // Exercise
        parser.burstToken(token, true);

        // Verify
        Assert.assertArrayEquals(parser.getTokens().toArray(), new String[]{"--", "-def"});
    }

    @Test
    public void testProcessNonOptionToken() {
        // Setup
        String value = "nonOption1";
        PosixParser parser = new PosixParser();

        // Exercise
        parser.processNonOptionToken(value);

        // Verify
        Assert.assertArrayEquals(parser.getTokens().toArray(), new String[]{"--", "nonOption1"});
    }

    @Test
    public void testProcessOptionToken_StopAtNonOption_True() {
        // Setup
        String token = "-a";
        PosixParser parser = new PosixParser();
        options = new Options();
        options.addOption("a", false, "Option a");

        // Exercise
        parser.processOptionToken(token, true);

        // Verify
        Assert.assertArrayEquals(parser.getTokens().toArray(), new String[]{"-a"});
    }

    @Test
    public void testProcessOptionToken_StopAtNonOption_False() {
        // Setup
        String token = "-a";
        PosixParser parser = new PosixParser();
        options = new Options();
        options.addOption("a", false, "Option a");

        // Exercise
        parser.processOptionToken(token, false);

        // Verify
        Assert.assertArrayEquals(parser.getTokens().toArray(), new String[]{"-a"});
    }
}
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PosixParserTest {

    private PosixParser posixParser;

    @Before
    public void setup() {
        posixParser = new PosixParser();
    }

    @Test
    public void testFlatten_StopAtNonOption_FlattenTokensUntilNonOption() {
        String[] arguments = {"-a", "value1", "-b", "-c", "--", "-d", "value2"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", false, "Option B");
        options.addOption("c", false, "Option C");
        options.addOption("d", true, "Option D");

        String[] result = posixParser.flatten(options, arguments, true);

        String[] expected = {"-a", "value1", "-b", "-c", "--", "-d", "value2"};
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_StopAtNonOption_IgnoreNonOptionAndFlattenRemainingTokens() {
        String[] arguments = {"-a", "value1", "-b", "-c", "--", "-d", "value2"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", false, "Option B");
        options.addOption("c", false, "Option C");
        options.addOption("d", true, "Option D");

        String[] result = posixParser.flatten(options, arguments, false);

        String[] expected = {"-a", "value1", "-b", "-c", "--", "-d", "value2"};
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_ValidOptionWithArg_AddOptionAndValueToFlattenedTokens() {
        String[] arguments = {"-a", "value1"};
        Options options = new Options();
        options.addOption("a", true, "Option A");

        String[] result = posixParser.flatten(options, arguments, true);

        String[] expected = {"-a", "value1"};
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_ValidOptionWithArgs_AddOptionAndValueToFlattenedTokens() {
        String[] arguments = {"-a", "value1", "-b", "value2", "-c"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", false, "Option C");

        String[] result = posixParser.flatten(options, arguments, true);

        String[] expected = {"-a", "value1", "-b", "value2", "-c"};
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_InvalidOptionWithoutStopAtNonOption_AddAsIsToFlattenedTokens() {
        String[] arguments = {"-d", "value1"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", false, "Option B");
        options.addOption("c", false, "Option C");

        String[] result = posixParser.flatten(options, arguments, false);

        String[] expected = {"-d", "value1"};
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_InvalidOptionWithStopAtNonOption_IgnoreOptionAndFlattenRemainingTokens() {
        String[] arguments = {"-d", "value1"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", false, "Option B");
        options.addOption("c", false, "Option C");

        String[] result = posixParser.flatten(options, arguments, true);

        String[] expected = {"-d", "value1"};
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_BurstToken_InvalidOptionsWithoutStopAtNonOption_AddAsIsToFlattenedTokens() {
        String[] arguments = {"-abc"};
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", false, "Option B");
        options.addOption("c", false, "Option C");

        String[] result = posixParser.flatten(options, arguments, false);

        String[] expected = {"-abc"};
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_BurstToken_InvalidOptionsWithStopAtNonOption_IgnoreOptionsAndFlattenRemainingTokens() {
        String[] arguments = {"-abc"};
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", false, "Option B");
        options.addOption("c", false, "Option C");

        String[] result = posixParser.flatten(options, arguments, true);

        String[] expected = {"-abc"};
        Assert.assertArrayEquals(expected, result);
    }
}
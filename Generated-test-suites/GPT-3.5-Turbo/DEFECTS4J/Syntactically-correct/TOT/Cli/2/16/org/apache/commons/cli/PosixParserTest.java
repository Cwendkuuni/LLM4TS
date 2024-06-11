import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testFlatten_StopAtNonOptionFalse_ReturnsFlattenedTokens() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b");
        options.addOption("c", true, "option c");

        PosixParser parser = new PosixParser();
        String[] args = {"-a", "-b", "value1", "--", "-c", "value2"};
        String[] result = parser.flatten(options, args, false);

        String[] expected = {"-a", "-b", "value1", "--", "-c", "value2"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_StopAtNonOptionTrue_ReturnsFlattenedTokens() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b");
        options.addOption("c", true, "option c");

        PosixParser parser = new PosixParser();
        String[] args = {"-a", "-b", "value1", "--", "-c", "value2"};
        String[] result = parser.flatten(options, args, true);

        String[] expected = {"-a", "-b", "value1", "--", "-c", "value2"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_SingleHyphen_ReturnsFlattenedTokens() {
        Options options = new Options();
        options.addOption("a", false, "option a");

        PosixParser parser = new PosixParser();
        String[] args = {"-"};
        String[] result = parser.flatten(options, args, false);

        String[] expected = {"-"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_SingleHyphen_StopAtNonOptionFalse_ReturnsFlattenedTokens() {
        Options options = new Options();
        options.addOption("a", false, "option a");

        PosixParser parser = new PosixParser();
        String[] args = {"-"};
        String[] result = parser.flatten(options, args, false);

        String[] expected = {"-"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_SingleHyphen_StopAtNonOptionTrue_ReturnsFlattenedTokens() {
        Options options = new Options();
        options.addOption("a", false, "option a");

        PosixParser parser = new PosixParser();
        String[] args = {"-"};
        String[] result = parser.flatten(options, args, true);

        String[] expected = {"-"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_ValidOption_ReturnsFlattenedTokens() {
        Options options = new Options();
        options.addOption("a", false, "option a");

        PosixParser parser = new PosixParser();
        String[] args = {"-a"};
        String[] result = parser.flatten(options, args, false);

        String[] expected = {"-a"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_ValidOptionWithArg_ReturnsFlattenedTokens() {
        Options options = new Options();
        options.addOption("a", true, "option a");

        PosixParser parser = new PosixParser();
        String[] args = {"-a", "value"};
        String[] result = parser.flatten(options, args, false);

        String[] expected = {"-a", "value"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_InvalidOption_StopAtNonOptionFalse_ReturnsFlattenedTokens() {
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] args = {"-z"};
        String[] result = parser.flatten(options, args, false);

        String[] expected = {"-z"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_InvalidOption_StopAtNonOptionTrue_ReturnsFlattenedTokens() {
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] args = {"-z"};
        String[] result = parser.flatten(options, args, true);

        String[] expected = {"--", "-z"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_BurstTokenStopAtNonOptionFalse_ReturnsFlattenedTokens() {
        Options options = new Options();
        options.addOption("a", false, "option a");

        PosixParser parser = new PosixParser();
        String[] args = {"-ab"};
        String[] result = parser.flatten(options, args, false);

        String[] expected = {"-a", "-b"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_BurstTokenStopAtNonOptionTrue_ReturnsFlattenedTokens() {
        Options options = new Options();
        options.addOption("a", false, "option a");

        PosixParser parser = new PosixParser();
        String[] args = {"-ab"};
        String[] result = parser.flatten(options, args, true);

        String[] expected = {"--", "-ab"};
        assertArrayEquals(expected, result);
    }
}
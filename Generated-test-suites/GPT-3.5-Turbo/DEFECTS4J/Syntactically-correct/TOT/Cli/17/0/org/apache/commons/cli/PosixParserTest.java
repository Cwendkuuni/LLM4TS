import static org.junit.Assert.*;
import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlatten_noOptionArgumentSuccess() {
        Options options = new Options();
        options.addOption("a", false, "");
        options.addOption("b", false, "");

        PosixParser parser = new PosixParser();
        String[] args = {"-a", "-b"};
        String[] result = parser.flatten(options, args, false);

        String[] expected = {"-a", "-b"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_singleHyphenOptionSuccess() {
        Options options = new Options();
        options.addOption("a", false, "");

        PosixParser parser = new PosixParser();
        String[] args = {"-"};
        String[] result = parser.flatten(options, args, false);

        String[] expected = {"-"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_singleHyphenArgumentSuccess() {
        Options options = new Options();
        options.addOption("a", false, "");

        PosixParser parser = new PosixParser();
        String[] args = {"arg1", "-"};
        String[] result = parser.flatten(options, args, false);

        String[] expected = {"arg1", "-"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_validOptionSuccess() {
        Options options = new Options();
        options.addOption("a", false, "");

        PosixParser parser = new PosixParser();
        String[] args = {"-a"};
        String[] result = parser.flatten(options, args, false);

        String[] expected = {"-a"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_validOptionArgumentSuccess() {
        Options options = new Options();
        options.addOption("a", true, "");

        PosixParser parser = new PosixParser();
        String[] args = {"-avalue"};
        String[] result = parser.flatten(options, args, false);

        String[] expected = {"-avalue"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_invalidOptionStopAtNonOptionSuccess() {
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] args = {"-abc"};
        String[] result = parser.flatten(options, args, true);

        String[] expected = {"--", "-abc"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_invalidOptionContinueParsingSuccess() {
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] args = {"-abc"};
        String[] result = parser.flatten(options, args, false);

        String[] expected = {"-a", "-b", "-c"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_specialTokenWithValueSuccess() {
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] args = {"--option=value"};
        String[] result = parser.flatten(options, args, false);

        String[] expected = {"--option", "value"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_specialTokenWithoutValueSuccess() {
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] args = {"--option"};
        String[] result = parser.flatten(options, args, false);

        String[] expected = {"--option"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_stopAtNonOptionWithNoRemainingTokensSuccess() {
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] args = {"arg1"};
        String[] result = parser.flatten(options, args, true);

        String[] expected = {"arg1"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_stopAtNonOptionWithRemainingTokensSuccess() {
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] args = {"arg1", "-a"};
        String[] result = parser.flatten(options, args, true);

        String[] expected = {"--", "arg1", "-a"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_noOptionArgumentWithNoRemainingTokensSuccess() {
        Options options = new Options();
        options.addOption("a", false, "");
        options.addOption("b", false, "");

        PosixParser parser = new PosixParser();
        String[] args = {};
        String[] result = parser.flatten(options, args, false);

        String[] expected = {};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_noOptionArgumentWithRemainingTokensSuccess() {
        Options options = new Options();
        options.addOption("a", false, "");
        options.addOption("b", false, "");

        PosixParser parser = new PosixParser();
        String[] args = {"arg1", "-a", "-b"};
        String[] result = parser.flatten(options, args, false);

        String[] expected = {"arg1", "-a", "-b"};
        assertArrayEquals(expected, result);
    }

}
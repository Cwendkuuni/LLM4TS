import static org.junit.Assert.*;
import org.junit.Test;

public class PosixParserTest {
    @Test
    public void testFlatten_withValidLongOption_shouldReturnValidResult() {
        Options options = new Options();
        options.addOption(new Option("a", "optionA", false, "Option A"));
        options.addOption(new Option("b", "optionB", true, "Option B"));

        PosixParser parser = new PosixParser();
        String[] args = parser.flatten(options, new String[]{"--optionA", "--optionB=arg"}, true);

        assertEquals(4, args.length);
        assertEquals("--optionA", args[0]);
        assertEquals("", args[1]);
        assertEquals("--optionB", args[2]);
        assertEquals("arg", args[3]);
    }

    @Test
    public void testFlatten_withInvalidLongOption_shouldAddToNonOptionTokens() {
        Options options = new Options();
        options.addOption(new Option("a", "optionA", false, "Option A"));
        options.addOption(new Option("b", "optionB", true, "Option B"));

        PosixParser parser = new PosixParser();
        String[] args = parser.flatten(options, new String[]{"--optionC"}, true);

        assertEquals(3, args.length);
        assertEquals("--", args[0]);
        assertEquals("--optionC", args[1]);
        assertEquals("", args[2]);
    }

    @Test
    public void testFlatten_withSingleHyphen_shouldAddToNonOptionTokens() {
        Options options = new Options();
        options.addOption(new Option("a", "optionA", false, "Option A"));
        options.addOption(new Option("b", "optionB", true, "Option B"));

        PosixParser parser = new PosixParser();
        String[] args = parser.flatten(options, new String[]{"-"}, true);

        assertEquals(2, args.length);
        assertEquals("--", args[0]);
        assertEquals("-", args[1]);
    }

    @Test
    public void testFlatten_withValidShortOption_shouldReturnValidResult() {
        Options options = new Options();
        options.addOption(new Option("a", "optionA", false, "Option A"));
        options.addOption(new Option("b", "optionB", true, "Option B"));

        PosixParser parser = new PosixParser();
        String[] args = parser.flatten(options, new String[]{"-a", "-b", "arg"}, true);

        assertEquals(4, args.length);
        assertEquals("-a", args[0]);
        assertEquals("-b", args[1]);
        assertEquals("arg", args[2]);
        assertEquals("", args[3]);
    }

    @Test
    public void testFlatten_withInvalidShortOption_shouldAddToNonOptionTokens() {
        Options options = new Options();
        options.addOption(new Option("a", "optionA", false, "Option A"));
        options.addOption(new Option("b", "optionB", true, "Option B"));

        PosixParser parser = new PosixParser();
        String[] args = parser.flatten(options, new String[]{"-c"}, true);

        assertEquals(3, args.length);
        assertEquals("--", args[0]);
        assertEquals("-c", args[1]);
        assertEquals("", args[2]);
    }

    @Test
    public void testFlatten_withMultipleShortOptionsAndArgument_shouldReturnValidResult() {
        Options options = new Options();
        options.addOption(new Option("a", "optionA", false, "Option A"));
        options.addOption(new Option("b", "optionB", true, "Option B"));

        PosixParser parser = new PosixParser();
        String[] args = parser.flatten(options, new String[]{"-ab", "arg"}, true);

        assertEquals(4, args.length);
        assertEquals("-a", args[0]);
        assertEquals("-b", args[1]);
        assertEquals("arg", args[2]);
        assertEquals("", args[3]);
    }

    @Test
    public void testFlatten_withMultipleShortOptionsAndNoArgument_shouldReturnValidResult() {
        Options options = new Options();
        options.addOption(new Option("a", "optionA", false, "Option A"));
        options.addOption(new Option("b", "optionB", true, "Option B"));

        PosixParser parser = new PosixParser();
        String[] args = parser.flatten(options, new String[]{"-ab"}, true);

        assertEquals(4, args.length);
        assertEquals("-a", args[0]);
        assertEquals("-b", args[1]);
        assertEquals("", args[2]);
        assertEquals("", args[3]);
    }

    @Test
    public void testFlatten_withNonOptionStopFlag_shouldAddRemainingTokensToProcessedTokens() {
        Options options = new Options();
        options.addOption(new Option("a", "optionA", false, "Option A"));
        options.addOption(new Option("b", "optionB", true, "Option B"));

        PosixParser parser = new PosixParser();
        String[] args = parser.flatten(options, new String[]{"-a", "-b", "arg", "extra"}, false);

        assertEquals(6, args.length);
        assertEquals("-a", args[0]);
        assertEquals("-b", args[1]);
        assertEquals("arg", args[2]);
        assertEquals("extra", args[3]);
        assertEquals("", args[4]);
        assertEquals("", args[5]);
    }

    @Test
    public void testFlatten_withValidBurstToken_shouldReturnValidResult() {
        Options options = new Options();
        options.addOption(new Option("a", "optionA", false, "Option A"));
        options.addOption(new Option("b", "optionB", true, "Option B"));

        PosixParser parser = new PosixParser();
        String[] args = parser.flatten(options, new String[]{"-ab"}, true);

        assertEquals(4, args.length);
        assertEquals("-a", args[0]);
        assertEquals("-b", args[1]);
        assertEquals("", args[2]);
        assertEquals("", args[3]);
    }

    @Test
    public void testFlatten_withInvalidBurstTokenAndNonOptionStopFlag_shouldAddRemainingTokensToProcessedTokens() {
        Options options = new Options();
        options.addOption(new Option("a", "optionA", false, "Option A"));
        options.addOption(new Option("b", "optionB", true, "Option B"));

        PosixParser parser = new PosixParser();
        String[] args = parser.flatten(options, new String[]{"-abc"}, false);

        assertEquals(5, args.length);
        assertEquals("-a", args[0]);
        assertEquals("-b", args[1]);
        assertEquals("-c", args[2]);
        assertEquals("", args[3]);
        assertEquals("", args[4]);
    }
}
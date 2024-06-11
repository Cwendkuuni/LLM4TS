import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PosixParserTest {
    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("a", false, "Option 'a'");
        options.addOption("b", true, "Option 'b' with value");

        String[] arguments = new String[] { "-a", "-b", "value" };

        PosixParser parser = new PosixParser();
        String[] flattenedArguments = parser.flatten(options, arguments, true);

        assertEquals(3, flattenedArguments.length);
        assertEquals("-a", flattenedArguments[0]);
        assertEquals("-b", flattenedArguments[1]);
        assertEquals("value", flattenedArguments[2]);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", false, "Option 'a'");
        options.addOption("b", true, "Option 'b' with value");

        String[] arguments = new String[] { "-a", "-b", "value", "-c", "--", "non-option" };

        PosixParser parser = new PosixParser();
        String[] flattenedArguments = parser.flatten(options, arguments, true);

        assertEquals(4, flattenedArguments.length);
        assertEquals("-a", flattenedArguments[0]);
        assertEquals("-b", flattenedArguments[1]);
        assertEquals("value", flattenedArguments[2]);
        assertEquals("--", flattenedArguments[3]);
    }

    @Test
    public void testFlattenWithoutStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", false, "Option 'a'");
        options.addOption("b", true, "Option 'b' with value");

        String[] arguments = new String[] { "-a", "-b", "value", "-c", "--", "non-option" };

        PosixParser parser = new PosixParser();
        String[] flattenedArguments = parser.flatten(options, arguments, false);

        assertEquals(7, flattenedArguments.length);
        assertEquals("-a", flattenedArguments[0]);
        assertEquals("-b", flattenedArguments[1]);
        assertEquals("value", flattenedArguments[2]);
        assertEquals("-c", flattenedArguments[3]);
        assertEquals("--", flattenedArguments[4]);
        assertEquals("non-option", flattenedArguments[5]);
        assertEquals("non-option", flattenedArguments[6]);
    }

    @Test
    public void testProcessSingleHyphen() {
        PosixParser parser = new PosixParser();
        parser.processSingleHyphen("singleHyphen");

        assertTrue(parser.getTokens().contains("singleHyphen"));
    }

    @Test
    public void testProcessOptionTokenWithExistingOption() {
        Options options = new Options();
        options.addOption("a", false, "Option 'a'");

        PosixParser parser = new PosixParser();
        parser.processOptionToken("-a", true);

        assertTrue(parser.getTokens().contains("-a"));
    }

    @Test
    public void testProcessOptionTokenWithoutExistingOptionAndStopAtNonOption() {
        Options options = new Options();

        PosixParser parser = new PosixParser();
        parser.processOptionToken("-c", true);

        assertTrue(parser.isEatTheRest());
    }

    @Test
    public void testProcessOptionTokenWithoutExistingOptionAndWithoutStopAtNonOption() {
        Options options = new Options();

        PosixParser parser = new PosixParser();
        parser.processOptionToken("-c", false);

        assertTrue(parser.getTokens().contains("-c"));
    }

    @Test
    public void testBurstToken() {
        Options options = new Options();
        options.addOption("a", false, "Option 'a'");
        options.addOption("b", true, "Option 'b' with value");

        PosixParser parser = new PosixParser();
        parser.burstToken("-ab", false);

        assertTrue(parser.getTokens().contains("-a"));
        assertTrue(parser.getTokens().contains("-b"));
    }

    @Test
    public void testBurstTokenWithoutExistingOptionAndStopAtNonOption() {
        Options options = new Options();

        PosixParser parser = new PosixParser();
        parser.burstToken("-cd", true);

        assertTrue(parser.isEatTheRest());
    }

    @Test
    public void testBurstTokenWithoutExistingOptionAndWithoutStopAtNonOption() {
        Options options = new Options();

        PosixParser parser = new PosixParser();
        parser.burstToken("-cd", false);

        assertTrue(parser.getTokens().contains("-cd"));
    }
}
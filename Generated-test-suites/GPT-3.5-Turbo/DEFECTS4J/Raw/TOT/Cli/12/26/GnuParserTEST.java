import org.junit.Test;
import static org.junit.Assert.*;

public class GnuParserTest {

    @Test
    public void testFlatten_optionExistsForFirstCharacter_onlyFirstCharacterAddedToProcessedTokensList() {
        Options options = new Options();
        options.addOption("a", false, "");
        options.addOption("b", false, "");
        String[] arguments = {"-a", "arg1", "-b", "arg2"};
        GnuParser parser = new GnuParser();

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(2, result.length);
        assertEquals("-a", result[0]);
        assertEquals("arg1", result[1]);
    }

    @Test
    public void testFlatten_optionDoesNotExistForWholeArgument_entireArgumentAddedToProcessedTokensList() {
        Options options = new Options();
        options.addOption("a", false, "");
        String[] arguments = {"-c", "arg1", "-b", "arg2"};
        GnuParser parser = new GnuParser();

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(4, result.length);
        assertEquals("-c", result[0]);
        assertEquals("arg1", result[1]);
        assertEquals("-b", result[2]);
        assertEquals("arg2", result[3]);
    }

    @Test
    public void testFlatten_doubleDash_encounterDoubleDashStopFlatteningAtThisPoint() {
        Options options = new Options();
        String[] arguments = {"-a", "--", "-b", "arg2"};
        GnuParser parser = new GnuParser();

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("--", result[1]);
        assertEquals("-b", result[2]);
    }

    @Test
    public void testFlatten_singleDash_dashAddedToProcessedTokensList() {
        Options options = new Options();
        String[] arguments = {"-a", "-", "-b", "arg2"};
        GnuParser parser = new GnuParser();

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-", result[1]);
        assertEquals("-b", result[2]);
    }

    @Test
    public void testFlatten_argumentStartsWithDashAndOptionDoesNotExist_entireArgumentAddedToProcessedTokensList() {
        Options options = new Options();
        options.addOption("a", false, "");
        String[] arguments = {"-ca", "-b", "arg2"};
        GnuParser parser = new GnuParser();

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(3, result.length);
        assertEquals("-ca", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("arg2", result[2]);
    }

    @Test
    public void testFlatten_argumentDoesNotStartWithDash_argumentAddedToProcessedTokensList() {
        Options options = new Options();
        String[] arguments = {"-a", "arg1", "arg2"};
        GnuParser parser = new GnuParser();

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("arg1", result[1]);
        assertEquals("arg2", result[2]);
    }

    @Test
    public void testFlatten_eatTheRestFlagSet_eatTheRestOfTheArguments() {
        Options options = new Options();
        options.addOption("a", false, "");
        String[] arguments = {"-a", "-b", "arg2", "arg3"};
        GnuParser parser = new GnuParser();

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("arg2", result[2]);
    }
}
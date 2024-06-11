import org.junit.Test;
import org.apache.commons.cli.*;

public class PosixParserTest {

    @Test
    public void testFlattenStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");

        String[] args = {"-a", "-b", "value", "nonOption"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);

        assertEquals(result.length, 4);
        assertEquals(result[0], "-a");
        assertEquals(result[1], "-b");
        assertEquals(result[2], "value");
        assertEquals(result[3], "nonOption");
    }

    @Test
    public void testFlattenNoStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");

        String[] args = {"-a", "-b", "value", "nonOption"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        assertEquals(result.length, 4);
        assertEquals(result[0], "-a");
        assertEquals(result[1], "-b");
        assertEquals(result[2], "value");
        assertEquals(result[3], "nonOption");
    }

    @Test
    public void testFlattenNoOptions() {
        Options options = new Options();

        String[] args = {"arg1", "arg2", "arg3"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);

        assertEquals(result.length, 3);
        assertEquals(result[0], "arg1");
        assertEquals(result[1], "arg2");
        assertEquals(result[2], "arg3");
    }

    @Test
    public void testFlattenEmptyArgs() {
        Options options = new Options();
        options.addOption("a", false, "Option a");

        String[] args = {};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);

        assertEquals(result.length, 0);
    }

    @Test
    public void testFlattenLongOptionWithValue() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");

        String[] args = {"--b=value"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);

        assertEquals(result.length, 2);
        assertEquals(result[0], "--b");
        assertEquals(result[1], "value");
    }

    @Test
    public void testFlattenSingleHyphen() {
        Options options = new Options();
        options.addOption("a", false, "Option a");

        String[] args = {"-"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);

        assertEquals(result.length, 1);
        assertEquals(result[0], "-");
    }

    @Test
    public void testFlattenShortOptionExists() {
        Options options = new Options();
        options.addOption("a", false, "Option a");

        String[] args = {"-a"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);

        assertEquals(result.length, 1);
        assertEquals(result[0], "-a");
    }

    @Test
    public void testFlattenShortOptionInvalidAndStopAtNonOption() {
        Options options = new Options();

        String[] args = {"-x", "nonOption"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);

        assertEquals(result.length, 2);
        assertEquals(result[0], "--");
        assertEquals(result[1], "nonOption");
    }

    @Test
    public void testFlattenShortOptionInvalidNoStopAtNonOption() {
        Options options = new Options();

        String[] args = {"-x", "nonOption"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        assertEquals(result.length, 3);
        assertEquals(result[0], "-x");
        assertEquals(result[1], "--");
        assertEquals(result[2], "nonOption");
    }

    @Test
    public void testFlattenShortOptionBursting() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", false, "Option b");

        String[] args = {"-ab"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);

        assertEquals(result.length, 2);
        assertEquals(result[0], "-a");
        assertEquals(result[1], "-b");
    }

    @Test
    public void testFlattenShortOptionBurstingStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", false, "Option b");

        String[] args = {"-abc", "nonOption"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);

        assertEquals(result.length, 3);
        assertEquals(result[0], "-a");
        assertEquals(result[1], "-b");
        assertEquals(result[2], "nonOption");
    }

    @Test
    public void testFlattenShortOptionBurstingNoStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", false, "Option b");

        String[] args = {"-abc", "nonOption"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        assertEquals(result.length, 4);
        assertEquals(result[0], "-a");
        assertEquals(result[1], "-b");
        assertEquals(result[2], "-c");
        assertEquals(result[3], "nonOption");
    }
}
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testFlatten_validOptionWithNoArg() {
        Options options = new Options();
        options.addOption(Option.builder("a").hasArg(false).build());
        PosixParser parser = new PosixParser();
        String[] arguments = new String[]{"-a"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(1, result.length);
        assertEquals("-a", result[0]);
    }

    @Test
    public void testFlatten_validOptionWithArg() {
        Options options = new Options();
        options.addOption(Option.builder("b").hasArg(true).build());
        PosixParser parser = new PosixParser();
        String[] arguments = new String[]{"-b", "value"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(2, result.length);
        assertEquals("-b", result[0]);
        assertEquals("value", result[1]);
    }

    @Test
    public void testFlatten_validOptionWithMultipleArgs() {
        Options options = new Options();
        options.addOption(Option.builder("c").hasArgs(true).build());
        PosixParser parser = new PosixParser();
        String[] arguments = new String[]{"-c", "value1", "value2"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(3, result.length);
        assertEquals("-c", result[0]);
        assertEquals("value1", result[1]);
        assertEquals("value2", result[2]);
    }

    @Test
    public void testFlatten_invalidOptionStopAtNonOption() {
        Options options = new Options();
        options.addOption(Option.builder("d").hasArg(false).build());
        PosixParser parser = new PosixParser();
        String[] arguments = new String[]{"-a", "-b", "-d", "value"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("-d", result[2]);
    }

    @Test
    public void testFlatten_invalidOptionNoStopAtNonOption() {
        Options options = new Options();
        options.addOption(Option.builder("e").hasArg(false).build());
        PosixParser parser = new PosixParser();
        String[] arguments = new String[]{"-e", "-f"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(2, result.length);
        assertEquals("-e", result[0]);
        assertEquals("-f", result[1]);
    }

    @Test
    public void testFlatten_longOptionWithValue() {
        Options options = new Options();
        options.addOption(Option.builder("g").hasArg(false).build());
        options.addOption(Option.builder().longOpt("option").hasArg(true).build());
        PosixParser parser = new PosixParser();
        String[] arguments = new String[]{"--option=value"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(1, result.length);
        assertEquals("--option=value", result[0]);
    }

    @Test
    public void testFlatten_singleHyphen() {
        Options options = new Options();
        options.addOption(Option.builder("h").hasArg(false).build());
        PosixParser parser = new PosixParser();
        String[] arguments = new String[]{"-"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(1, result.length);
        assertEquals("-", result[0]);
    }

    @Test
    public void testFlatten_unrecognizedOptionStopAtNonOption() {
        Options options = new Options();
        options.addOption(Option.builder("i").hasArg(false).build());
        PosixParser parser = new PosixParser();
        String[] arguments = new String[]{"-a", "-b", "-i", "-c"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("-i", result[2]);
    }

    @Test
    public void testFlatten_unrecognizedOptionNoStopAtNonOption() {
        Options options = new Options();
        options.addOption(Option.builder("j").hasArg(false).build());
        PosixParser parser = new PosixParser();
        String[] arguments = new String[]{"-j", "-k"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(2, result.length);
        assertEquals("-j", result[0]);
        assertEquals("-k", result[1]);
    }

    @Test
    public void testFlatten_argumentAfterLastOption() {
        Options options = new Options();
        options.addOption(Option.builder("l").hasArg(false).build());
        options.addOption(Option.builder().longOpt("option").hasArg(true).build());
        PosixParser parser = new PosixParser();
        String[] arguments = new String[]{"-l", "--option=value", "extra"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(3, result.length);
        assertEquals("-l", result[0]);
        assertEquals("--option=value", result[1]);
        assertEquals("extra", result[2]);
    }

}
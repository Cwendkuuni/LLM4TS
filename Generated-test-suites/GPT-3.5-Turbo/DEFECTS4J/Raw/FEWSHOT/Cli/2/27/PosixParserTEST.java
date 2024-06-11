import org.apache.commons.cli.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class PosixParserTest {
    private static final String[] EMPTY_STRING_ARRAY = {};

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption(Option.builder("a").hasArg().build());
        options.addOption(Option.builder("b").hasArgs().build());
        options.addOption(Option.builder("c").build());

        String[] arguments = new String[]{"-a", "value1", "-b", "value2", "value3"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(new String[]{"-a", "value1", "-b", "value2", "value3"}, result);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        Options options = new Options();
        options.addOption(Option.builder("a").hasArg().build());
        options.addOption(Option.builder("b").hasArgs().build());
        options.addOption(Option.builder("c").build());

        String[] arguments = new String[]{"-a", "value1", "-b", "value2", "value3"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(new String[]{"-a", "value1", "-b", "value2", "value3"}, result);
    }

    @Test
    public void testFlattenStopAtNonOptionWithRemainingTokens() {
        Options options = new Options();
        options.addOption(Option.builder("a").hasArg().build());
        options.addOption(Option.builder("b").hasArgs().build());
        options.addOption(Option.builder("c").build());

        String[] arguments = new String[]{"-a", "value1", "-b", "value2", "value3"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(new String[]{"-a", "value1", "-b", "value2", "value3"}, result);
    }

    @Test
    public void testFlattenWithSpecialToken() {
        Options options = new Options();
        options.addOption(Option.builder("a").hasArg().build());
        options.addOption(Option.builder("b").hasArgs().build());
        options.addOption(Option.builder("c").build());

        String[] arguments = new String[]{"--", "-a", "value1", "-b", "value2"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(new String[]{"--", "-a", "value1", "-b", "value2"}, result);
    }

    @Test
    public void testFlattenWithSingleHyphenToken() {
        Options options = new Options();
        options.addOption(Option.builder("a").hasArg().build());
        options.addOption(Option.builder("b").hasArgs().build());
        options.addOption(Option.builder("c").build());

        String[] arguments = new String[]{"-", "value1", "-b", "value2"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(new String[]{"-", "value1", "-b", "value2"}, result);
    }

    @Test
    public void testFlattenWithOptionToken() {
        Options options = new Options();
        options.addOption(Option.builder("a").hasArg().build());
        options.addOption(Option.builder("b").hasArgs().build());
        options.addOption(Option.builder("c").build());

        String[] arguments = new String[]{"-a", "value1", "-b", "value2"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(new String[]{"-a", "value1", "-b", "value2"}, result);
    }

    @Test
    public void testFlattenWithOptionTokenAndStopAtNonOption() {
        Options options = new Options();
        options.addOption(Option.builder("a").hasArg().build());
        options.addOption(Option.builder("b").hasArgs().build());
        options.addOption(Option.builder("c").build());

        String[] arguments = new String[]{"-a", "value1", "-b", "value2"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(new String[]{"-a", "value1", "-b", "value2"}, result);
    }

    @Test
    public void testFlattenBurstToken() {
        Options options = new Options();
        options.addOption(Option.builder("a").hasArg().build());
        options.addOption(Option.builder("b").hasArgs().build());
        options.addOption(Option.builder("c").build());

        String[] arguments = new String[]{"-abc"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(new String[]{"-a", "-b", "-c"}, result);
    }
}
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testFlatten() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", false, "Option C");

        String[] arguments = new String[]{"-a", "-b", "value", "arg1", "--", "arg2"};

        String[] expected = new String[]{"-a", "-b", "value", "arg1", "--", "arg2"};
        assertArrayEquals(expected, parser.flatten(options, arguments, true));
    }

    @Test
    public void testFlattenStopAtNonOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", false, "Option C");

        String[] arguments = new String[]{"-a", "-b", "value", "arg1", "--", "arg2"};

        String[] expected = new String[]{"-a", "-b", "value", "arg1", "--", "arg2"};
        assertArrayEquals(expected, parser.flatten(options, arguments, true));
    }

    @Test
    public void testFlattenNoOptions() {
        PosixParser parser = new PosixParser();
        Options options = new Options();

        String[] arguments = new String[]{"arg1", "arg2"};

        String[] expected = new String[]{"arg1", "arg2"};
        assertArrayEquals(expected, parser.flatten(options, arguments, true));
    }

    @Test
    public void testFlattenWithOptions() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", false, "Option C");

        String[] arguments = new String[]{"-a", "-b", "value", "-c"};

        String[] expected = new String[]{"-a", "-b", "value", "-c"};
        assertArrayEquals(expected, parser.flatten(options, arguments, true));
    }

    @Test
    public void testFlattenWithOptionArg() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", false, "Option C");

        String[] arguments = new String[]{"-a", "-b", "value"};

        String[] expected = new String[]{"-a", "-b", "value"};
        assertArrayEquals(expected, parser.flatten(options, arguments, true));
    }

    @Test
    public void testFlattenWithOptionArgs() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C");

        String[] arguments = new String[]{"-a", "-b", "value1", "value2"};

        String[] expected = new String[]{"-a", "-b", "value1", "value2"};
        assertArrayEquals(expected, parser.flatten(options, arguments, true));
    }
}
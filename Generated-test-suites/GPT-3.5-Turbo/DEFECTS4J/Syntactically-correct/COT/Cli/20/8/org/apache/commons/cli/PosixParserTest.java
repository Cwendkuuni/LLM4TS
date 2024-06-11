
import static org.junit.Assert.*;
import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlatten() {
        Options options = new Options();
        String[] arguments = {"--foo", "bar", "-a"};
        boolean stopAtNonOption = false;

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, stopAtNonOption);

        String[] expected = {"--foo", "bar", "-a"};
        assertArrayEquals(expected, result);
    }

    @Test(expected = NullPointerException.class)
    public void testFlattenNullOptions() {
        Options options = null;
        String[] arguments = {"--foo", "bar", "-a"};
        boolean stopAtNonOption = false;

        PosixParser parser = new PosixParser();
        parser.flatten(options, arguments, stopAtNonOption);
    }

    @Test(expected = NullPointerException.class)
    public void testFlattenNullArguments() {
        Options options = new Options();
        String[] arguments = null;
        boolean stopAtNonOption = false;

        PosixParser parser = new PosixParser();
        parser.flatten(options, arguments, stopAtNonOption);
    }

    @Test
    public void testFlattenEmptyArguments() {
        Options options = new Options();
        String[] arguments = {};
        boolean stopAtNonOption = false;

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, stopAtNonOption);

        String[] expected = {};
        assertArrayEquals(expected, result);
    }

}
import static org.junit.Assert.*;
import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlattenWithStopAtNonOptionTrue() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", "aaa", false, "Option AAA");
        String[] args = { "-a", "--bbb", "value" };
        String[] result = parser.flatten(options, args, true);
        String[] expected = { "-a", "--bbb", "value" };
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithStopAtNonOptionFalse() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", "aaa", false, "Option AAA");
        String[] args = { "-a", "--bbb", "value" };
        String[] result = parser.flatten(options, args, false);
        String[] expected = { "-a", "--bbb", "value" };
        assertArrayEquals(expected, result);
    }

    @Test
    public void testProcessSingleHyphen() {
        PosixParser parser = new PosixParser();
        String[] args = { "-" };
        String[] result = parser.flatten(new Options(), args, true);
        String[] expected = { "-" };
        assertArrayEquals(expected, result);
    }

    @Test
    public void testProcessOptionToken() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", "aaa", false, "Option AAA");
        String[] args = { "-a" };
        String[] result = parser.flatten(options, args, true);
        String[] expected = { "-a" };
        assertArrayEquals(expected, result);
    }

    @Test
    public void testProcess() {
        PosixParser parser = new PosixParser();
        String[] args = { "value" };
        String[] result = parser.flatten(new Options(), args, true);
        String[] expected = { "--", "value" };
        assertArrayEquals(expected, result);
    }

    @Test
    public void testBurstToken() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", "aaa", false, "Option AAA");
        options.addOption("b", "bbb", true, "Option BBB");
        String[] args = { "-abvalue" };
        String[] result = parser.flatten(options, args, true);
        String[] expected = { "-a", "-b", "value" };
        assertArrayEquals(expected, result);
    }

}
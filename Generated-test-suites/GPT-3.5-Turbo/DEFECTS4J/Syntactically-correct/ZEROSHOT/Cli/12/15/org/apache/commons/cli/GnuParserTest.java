import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class GnuParserTest {

    GnuParser parser;

    @Before
    public void setUp() {
        parser = new GnuParser();
    }

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("D", false, "Debug option");

        String[] arguments = {"-D", "input.txt"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(2, result.length);
        assertEquals("-D", result[0]);
        assertEquals("input.txt", result[1]);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        Options options = new Options();
        options.addOption("D", false, "Debug option");

        String[] arguments = {"-D", "input.txt", "-q"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(2, result.length);
        assertEquals("-D", result[0]);
        assertEquals("input.txt", result[1]);
    }

    @Test
    public void testFlattenWithDoubleDash() {
        Options options = new Options();
        options.addOption("D", false, "Debug option");

        String[] arguments = {"--", "-D", "input.txt"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(3, result.length);
        assertEquals("--", result[0]);
        assertEquals("-D", result[1]);
        assertEquals("input.txt", result[2]);
    }

    @Test
    public void testFlattenWithSingleDash() {
        Options options = new Options();
        options.addOption("D", false, "Debug option");

        String[] arguments = {"-", "input.txt"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(2, result.length);
        assertEquals("-", result[0]);
        assertEquals("input.txt", result[1]);
    }

    @Test
    public void testFlattenWithOptionsNotExisting() {
        Options options = new Options();
        options.addOption("D", false, "Debug option");

        String[] arguments = {"-v", "input.txt"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(2, result.length);
        assertEquals("-v", result[0]);
        assertEquals("input.txt", result[1]);
    }

    @Test
    public void testFlattenWithOptionsExisting() {
        Options options = new Options();
        options.addOption("D", false, "Debug option");

        String[] arguments = {"-Dproperty=value", "-q"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(2, result.length);
        assertEquals("-Dproperty=value", result[0]);
        assertEquals("-q", result[1]);
    }

    @Test
    public void testFlattenWithNonOption() {
        Options options = new Options();
        options.addOption("D", false, "Debug option");

        String[] arguments = {"input.txt"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(1, result.length);
        assertEquals("input.txt", result[0]);
    }

    @Test
    public void testFlattenWithMultipleNonOptions() {
        Options options = new Options();
        options.addOption("D", false, "Debug option");

        String[] arguments = {"input1.txt", "input2.txt", "input3.txt"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(3, result.length);
        assertEquals("input1.txt", result[0]);
        assertEquals("input2.txt", result[1]);
        assertEquals("input3.txt", result[2]);
    }

}
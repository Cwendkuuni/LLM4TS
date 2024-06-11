import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testFlatten_StopAtNonOption_True() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", "optionC", false, "Option C");

        PosixParser parser = new PosixParser();

        String[] arguments = {"--optionC", "-a", "valueA", "valueB"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals("The length of the result should be 4", 4, result.length);
        assertEquals("The first element of the result should be the optionC", "--optionC", result[0]);
        assertEquals("The second element of the result should be the optionA", "-a", result[1]);
        assertEquals("The third element of the result should be the valueA", "valueA", result[2]);
        assertEquals("The fourth element of the result should be the valueB", "valueB", result[3]);
    }

    @Test
    public void testFlatten_StopAtNonOption_False() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", "optionC", false, "Option C");

        PosixParser parser = new PosixParser();

        String[] arguments = {"--optionC", "-a", "valueA", "valueB"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals("The length of the result should be 6", 6, result.length);
        assertEquals("The first element of the result should be the optionC", "--optionC", result[0]);
        assertEquals("The second element of the result should be the optionA", "-a", result[1]);
        assertEquals("The third element of the result should be the valueA", "valueA", result[2]);
        assertEquals("The fourth element of the result should be the valueB", "valueB", result[3]);
        assertEquals("The fifth element of the result should be the --", "--", result[4]);
        assertEquals("The sixth element of the result should be the valueB", "valueB", result[5]);
    }
}
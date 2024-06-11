import static org.junit.Assert.*;
import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlatten_NoArguments_StopAtNonOptionFalse() {
        Options options = new Options();
        String[] arguments = {};
        boolean stopAtNonOption = false;

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, stopAtNonOption);

        assertEquals(0, result.length);
    }

    @Test
    public void testFlatten_NoArguments_StopAtNonOptionTrue() {
        Options options = new Options();
        String[] arguments = {};
        boolean stopAtNonOption = true;

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, stopAtNonOption);

        assertEquals(0, result.length);
    }

    @Test
    public void testFlatten_WithArguments_StopAtNonOptionFalse() {
        Options options = new Options();
        String[] arguments = {"--option1", "value1", "-option2", "--option3"};
        boolean stopAtNonOption = false;

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, stopAtNonOption);

        assertEquals(4, result.length);
        assertEquals("--option1", result[0]);
        assertEquals("value1", result[1]);
        assertEquals("-option2", result[2]);
        assertEquals("--option3", result[3]);
    }

    @Test
    public void testFlatten_WithArguments_StopAtNonOptionTrue() {
        Options options = new Options();
        String[] arguments = {"--option1", "value1", "-option2", "--option3"};
        boolean stopAtNonOption = true;

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, stopAtNonOption);

        assertEquals(1, result.length);
        assertEquals("--option1", result[0]);
    }

    @Test
    public void testFlatten_WithInvalidOption_StopAtNonOptionFalse() {
        Options options = new Options();
        String[] arguments = {"--option1", "-option2"};
        boolean stopAtNonOption = false;

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, stopAtNonOption);

        assertEquals(2, result.length);
        assertEquals("--option1", result[0]);
        assertEquals("-option2", result[1]);
    }

    @Test
    public void testFlatten_WithInvalidOption_StopAtNonOptionTrue() {
        Options options = new Options();
        String[] arguments = {"--option1", "-option2"};
        boolean stopAtNonOption = true;

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, stopAtNonOption);

        assertEquals(1, result.length);
        assertEquals("--option1", result[0]);
    }

    @Test
    public void testFlatten_WithArgumentValue_StopAtNonOptionFalse() {
        Options options = new Options();
        Option option1 = new Option("option1", true);
        options.addOption(option1);
        String[] arguments = {"--option1", "value1"};
        boolean stopAtNonOption = false;

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, stopAtNonOption);

        assertEquals(2, result.length);
        assertEquals("--option1", result[0]);
        assertEquals("value1", result[1]);
    }

    @Test
    public void testFlatten_WithArgumentValue_StopAtNonOptionTrue() {
        Options options = new Options();
        Option option1 = new Option("option1", true);
        options.addOption(option1);
        String[] arguments = {"--option1", "value1"};
        boolean stopAtNonOption = true;

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, stopAtNonOption);

        assertEquals(1, result.length);
        assertEquals("--option1", result[0]);
    }

    @Test
    public void testFlatten_WithMultipleArguments_StopAtNonOptionFalse() {
        Options options = new Options();
        Option option1 = new Option("option1", true);
        options.addOption(option1);
        String[] arguments = {"--option1", "value1", "--option2", "value2"};
        boolean stopAtNonOption = false;

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, stopAtNonOption);

        assertEquals(4, result.length);
        assertEquals("--option1", result[0]);
        assertEquals("value1", result[1]);
        assertEquals("--option2", result[2]);
        assertEquals("value2", result[3]);
    }

    @Test
    public void testFlatten_WithMultipleArguments_StopAtNonOptionTrue() {
        Options options = new Options();
        Option option1 = new Option("option1", true);
        options.addOption(option1);
        String[] arguments = {"--option1", "value1", "--option2", "value2"};
        boolean stopAtNonOption = true;

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, stopAtNonOption);

        assertEquals(1, result.length);
        assertEquals("--option1", result[0]);
    }
}
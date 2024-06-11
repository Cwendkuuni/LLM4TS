import static org.junit.Assert.*;
import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlatten_StopAtNonOption_ReturnFlattenedArguments() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b");
        options.addOption("c", false, "option c");

        PosixParser parser = new PosixParser();
        String[] arguments = {"-a", "-b", "value1", "arg1", "-c", "arg2"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("value1", result[2]);
        assertEquals("arg1", result[3]);
    }

    @Test
    public void testFlatten_DontStopAtNonOption_ReturnFlattenedArguments() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b");
        options.addOption("c", false, "option c");

        PosixParser parser = new PosixParser();
        String[] arguments = {"-a", "-b", "value1", "arg1", "-c", "arg2"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("value1", result[2]);
        assertEquals("arg1", result[3]);
        assertEquals("-c", result[4]);
        assertEquals("arg2", result[5]);
    }

    @Test
    public void testFlatten_NoOptions_ReturnFlattenedArguments() {
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] arguments = {"-a", "-b", "value1", "arg1"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("value1", result[2]);
        assertEquals("arg1", result[3]);
    }

    @Test
    public void testFlatten_InvalidOption_StopAtNonOption_ReturnFlattenedArguments() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b");
        options.addOption("c", false, "option c");

        PosixParser parser = new PosixParser();
        String[] arguments = {"-a", "-d", "value1", "arg1", "-c", "arg2"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals("-a", result[0]);
        assertEquals("--", result[1]);
        assertEquals("-d", result[2]);
        assertEquals("value1", result[3]);
        assertEquals("arg1", result[4]);
        assertEquals("-c", result[5]);
        assertEquals("arg2", result[6]);
    }

    @Test
    public void testFlatten_InvalidOption_DontStopAtNonOption_ReturnFlattenedArguments() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b");
        options.addOption("c", false, "option c");

        PosixParser parser = new PosixParser();
        String[] arguments = {"-a", "-d", "value1", "arg1", "-c", "arg2"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals("-a", result[0]);
        assertEquals("-d", result[1]);
        assertEquals("value1", result[2]);
        assertEquals("arg1", result[3]);
        assertEquals("-c", result[4]);
        assertEquals("arg2", result[5]);
    }
}
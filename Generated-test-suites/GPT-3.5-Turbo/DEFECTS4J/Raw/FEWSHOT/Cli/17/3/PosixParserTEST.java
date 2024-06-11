import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.Option;

public class PosixParserTest {

    @Test
    public void testFlatten_NoOptions_StopAtNonOption() {
        Options options = new Options();
        String[] arguments = {"arg1", "arg2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(arguments, result);
    }

    @Test
    public void testFlatten_NoOptions_DoNotStopAtNonOption() {
        Options options = new Options();
        String[] arguments = {"arg1", "arg2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(arguments, result);
    }

     @Test
    public void testFlatten_WithOptionAndArgument_StopAtNonOption() {
        Options options = new Options();
        options.addOption(Option.builder("f").argName("file").build());
        String[] arguments = {"-f", "filename", "arg1"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(arguments, result);
    }

    @Test
    public void testFlatten_WithOptionAndArgument_DoNotStopAtNonOption() {
        Options options = new Options();
        options.addOption(Option.builder("f").argName("file").build());
        String[] arguments = {"-f", "filename", "arg1"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(arguments, result);
    }

    @Test
    public void testFlatten_WithInvalidOption_DoNotStopAtNonOption() {
        Options options = new Options();
        options.addOption(Option.builder("f").build());
        String[] arguments = {"-g"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(arguments, result);
    }

    @Test
    public void testFlatten_WithInvalidOption_StopAtNonOption() {
        Options options = new Options();
        options.addOption(Option.builder("f").build());
        String[] arguments = {"-g"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(arguments, result);
    }
    
    @Test
    public void testFlatten_WithOptionAndMultipleArguments_StopAtNonOption() {
        Options options = new Options();
        options.addOption(Option.builder("f").argName("file").hasArgs().build());
        String[] arguments = {"-f", "file1", "file2", "arg1"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(arguments, result);
    }

    @Test
    public void testFlatten_WithOptionAndMultipleArguments_DoNotStopAtNonOption() {
        Options options = new Options();
        options.addOption(Option.builder("f").argName("file").hasArgs().build());
        String[] arguments = {"-f", "file1", "file2", "arg1"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(arguments, result);
    }

    @Test
    public void testFlatten_WithSpecialToken_StopAtNonOption() {
        Options options = new Options();
        String[] arguments = {"--", "arg1"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(arguments, result);
    }

    @Test
    public void testFlatten_WithSpecialToken_DoNotStopAtNonOption() {
        Options options = new Options();
        String[] arguments = {"--", "arg1"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(arguments, result);
    }
}
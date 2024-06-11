import static org.junit.Assert.*;
import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlatten_noOptions() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        
        String[] arguments = {"arg1", "arg2", "arg3"};
        String[] expected = {"arg1", "arg2", "arg3"};
        assertArrayEquals(expected, parser.flatten(options, arguments, false));
    }

    @Test
    public void testFlatten_singleOption() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        PosixParser parser = new PosixParser();
        
        String[] arguments = {"arg1", "-a", "arg2", "arg3"};
        String[] expected = {"arg1", "-a", "arg2", "arg3"};
        assertArrayEquals(expected, parser.flatten(options, arguments, false));
    }

    @Test
    public void testFlatten_multipleOptions() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", false, "option b");
        PosixParser parser = new PosixParser();
        
        String[] arguments = {"arg1", "-a", "arg2", "-b", "arg3"};
        String[] expected = {"arg1", "-a", "arg2", "-b", "arg3"};
        assertArrayEquals(expected, parser.flatten(options, arguments, false));
    }

    @Test
    public void testFlatten_optionWithValue() {
        Options options = new Options();
        options.addOption("a", true, "option a");
        PosixParser parser = new PosixParser();
        
        String[] arguments = {"arg1", "-a", "value1", "arg2"};
        String[] expected = {"arg1", "-a", "value1", "arg2"};
        assertArrayEquals(expected, parser.flatten(options, arguments, false));
    }

    @Test
    public void testFlatten_optionWithMultipleValues() {
        Options options = new Options();
        options.addOption("a", true, "option a");
        PosixParser parser = new PosixParser();
        
        String[] arguments = {"arg1", "-a", "value1", "value2", "arg2"};
        String[] expected = {"arg1", "-a", "value1", "value2", "arg2"};
        assertArrayEquals(expected, parser.flatten(options, arguments, false));
    }

    @Test
    public void testFlatten_optionWithRemainingValues() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        PosixParser parser = new PosixParser();
        
        String[] arguments = {"arg1", "-a", "value1", "value2", "arg2"};
        String[] expected = {"arg1", "-a", "value1", "value2", "arg2"};
        assertArrayEquals(expected, parser.flatten(options, arguments, true));
    }

}
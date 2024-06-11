import org.junit.Test;
import static org.junit.Assert.*;

public class GnuParserTest {

    @Test
    public void testFlatten_NullOptions() {
        GnuParser parser = new GnuParser();
        String[] arguments = {"arg1", "arg2"};
        String[] result = parser.flatten(null, arguments, false);
        assertArrayEquals(arguments, result);
    }
    
    @Test
    public void testFlatten_NullArguments() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        String[] result = parser.flatten(options, null, false);
        assertNull(result);
    }
    
    @Test
    public void testFlatten_EmptyArguments() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        String[] arguments = {};
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(arguments, result);
    }
    
    @Test
    public void testFlatten_NullOptionsAndArguments() {
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(null, null, false);
        assertNull(result);
    }
    
    @Test
    public void testFlatten_ArgumentStartsWithDoubleDash() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        String[] arguments = {"--arg"};
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(arguments, result);
    }
    
    @Test
    public void testFlatten_ArgumentIsSingleDash() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        String[] arguments = {"-"};
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(arguments, result);
    }
    
    @Test
    public void testFlatten_ArgumentStartsWithDash() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        String[] arguments = {"-arg"};
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(arguments, result);
    }
    
    @Test
    public void testFlatten_ValidOption() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        Option option = new Option("a", "arg", false, "Option arg");
        options.addOption(option);
        String[] arguments = {"-a"};
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(arguments, result);
    }
    
    @Test
    public void testFlatten_InvalidOption() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        String[] arguments = {"-b"};
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(arguments, result);
    }
    
    @Test
    public void testFlatten_StopAtNonOption() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        String[] arguments = {"-a", "nonOption"};
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(new String[]{"-a", "nonOption"}, result);
    }
}
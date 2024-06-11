import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testFlatten_StopAtNonOption() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        options.addOption("c", true, "Option c");
        
        String[] arguments = {"-a", "-b", "value1", "arg1", "--", "-c", "value2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);
        
        String[] expected = {"-a", "-b", "value1", "arg1", "--", "-c", "value2"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_WithoutStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        options.addOption("c", true, "Option c");
        
        String[] arguments = {"-a", "-b", "value1", "arg1", "--", "-c", "value2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        
        String[] expected = {"-a", "-b", "value1", "arg1", "--", "-c", "value2"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_MultipleOptions() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        options.addOption("c", false, "Option c");
        
        String[] arguments = {"-a", "-b", "value1", "-c"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);
        
        String[] expected = {"-a", "-b", "value1", "-c"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_SpecialToken() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        options.addOption("c", true, "Option c");
        
        String[] arguments = {"--special=arg1"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);
        
        String[] expected = {"--special", "arg1"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_SingleHyphen() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        options.addOption("c", true, "Option c");
        
        String[] arguments = {"-"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);
        
        String[] expected = {"-"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_ValidOptionId() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        options.addOption("c", true, "Option c");
        
        String[] arguments = {"-a", "-invalid"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);
        
        String[] expected = {"-a", "-invalid"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_InvalidOptionId() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        options.addOption("c", true, "Option c");
        
        String[] arguments = {"-invalid"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);
        
        String[] expected = {"-invalid"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_WithOptionArgument() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        options.addOption("c", true, "Option c");
        
        String[] arguments = {"-a", "value1", "-b", "value2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);
        
        String[] expected = {"-a", "value1", "-b", "value2"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_WithoutOptionArgument() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        options.addOption("c", true, "Option c");
        
        String[] arguments = {"-a", "-b"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);
        
        String[] expected = {"-a", "-b"};
        assertArrayEquals(expected, result);
    }
}
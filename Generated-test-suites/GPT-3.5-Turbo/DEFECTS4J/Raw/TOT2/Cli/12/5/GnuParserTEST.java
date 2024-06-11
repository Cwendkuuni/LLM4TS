import static org.junit.Assert.*;
import org.junit.Test;

public class GnuParserTest {

    @Test
    public void testFlatten_WithNoOptions() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        String[] arguments = {"arg1", "arg2"};
        String[] result = parser.flatten(options, arguments, false);
        
        assertArrayEquals(arguments, result);
    }
    
    @Test
    public void testFlatten_WithOptionAndNoArguments() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        options.addOption("d", "debug", false, "Enable debug mode");
        String[] arguments = {"-d"};
        String[] result = parser.flatten(options, arguments, false);
        
        assertArrayEquals(arguments, result);
    }
    
    @Test
    public void testFlatten_WithPropertyOption() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        String[] arguments = {"-Dproperty=value"};
        String[] result = parser.flatten(options, arguments, false);
        
        assertArrayEquals(arguments, result);
    }
    
    @Test
    public void testFlatten_WithNonOptionStop() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        options.addOption("d", "debug", false, "Enable debug mode");
        String[] arguments = {"-d", "arg1", "--", "arg2"};
        String[] expected = {"-d", "arg1", "--", "arg2"};
        String[] result = parser.flatten(options, arguments, true);
        
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_WithLeadingHyphen() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        options.addOption("d", "debug", false, "Enable debug mode");
        String[] arguments = {"--foo=value"};
        String[] expected = {"--", "foo=value"};
        String[] result = parser.flatten(options, arguments, false);
        
        assertArrayEquals(expected, result);
    }
}
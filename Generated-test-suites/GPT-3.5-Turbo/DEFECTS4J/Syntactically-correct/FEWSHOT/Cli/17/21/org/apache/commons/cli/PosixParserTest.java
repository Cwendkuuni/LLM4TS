import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class PosixParserTest {
    
    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption(new Option("a", false, "Option a"));
        options.addOption(new Option("b", true, "Option b"));
        options.addOption(new Option("c", false, "Option c"));
        
        PosixParser parser = new PosixParser();
        
        String[] arguments1 = {"-a", "-b", "value", "-c"};
        String[] expected1 = {"-a", "-b", "value", "-c"};
        assertEquals(expected1, parser.flatten(options, arguments1, false));
        
        String[] arguments2 = {"-a", "-b", "value", "-c", "extra"};
        String[] expected2 = {"-a", "-b", "value", "-c", "extra"};
        assertEquals(expected2, parser.flatten(options, arguments2, false));
        
        String[] arguments3 = {"-a", "-b", "value", "-c", "extra"};
        String[] expected3 = {"-a", "-b", "value"};
        assertEquals(expected3, parser.flatten(options, arguments3, true));
        
        String[] arguments4 = {"-a", "-c", "-b", "value"};
        String[] expected4 = {"-a", "-c", "-b", "value"};
        assertEquals(expected4, parser.flatten(options, arguments4, false));
    }
}
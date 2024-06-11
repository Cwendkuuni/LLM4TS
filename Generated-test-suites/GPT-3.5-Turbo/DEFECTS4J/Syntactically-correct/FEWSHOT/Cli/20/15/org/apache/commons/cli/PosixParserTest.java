import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class PosixParserTest {
    
    @Test
    public void testFlattenWithStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        options.addOption("c", true, "Option c");
        
        PosixParser parser = new PosixParser();
        
        String[] arguments = {"--a", "-b", "value", "--c", "value", "extra"};
        
        String[] result = parser.flatten(options, arguments, true);
        
        assertEquals(3, result.length);
        assertEquals("--a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("value", result[2]);
    }

    @Test
    public void testFlattenWithoutStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        options.addOption("c", true, "Option c");
        
        PosixParser parser = new PosixParser();
        
        String[] arguments = {"--a", "-b", "value", "--c", "value", "extra"};
        
        String[] result = parser.flatten(options, arguments, false);
        
        assertEquals(6, result.length);
        assertEquals("--a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("value", result[2]);
        assertEquals("--", result[3]);
        assertEquals("extra", result[4]);
        assertEquals("value", result[5]);
    }
    
    @Test
    public void testFlattenWithNoOptions() {
        Options options = new Options();
        
        PosixParser parser = new PosixParser();
        
        String[] arguments = {"--option", "value"};
        
        String[] result = parser.flatten(options, arguments, true);
        
        assertEquals(2, result.length);
        assertEquals("--option", result[0]);
        assertEquals("value", result[1]);
    }
}
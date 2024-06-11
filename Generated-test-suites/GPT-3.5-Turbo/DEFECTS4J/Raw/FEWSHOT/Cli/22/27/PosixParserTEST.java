import org.junit.Test;
import org.apache.commons.cli.Options;
import static org.junit.Assert.*;

public class PosixParserTest {
    
    @Test
    public void testFlatten() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        
        String[] arguments = {"--foo", "-b", "-abc", "value1", "value2"};
        
        String[] flattened = parser.flatten(options, arguments, false);
        
        assertEquals("--foo", flattened[0]);
        assertEquals("-b", flattened[1]);
        assertEquals("-a", flattened[2]);
        assertEquals("-b", flattened[3]);
        assertEquals("-c", flattened[4]);
        assertEquals("value1", flattened[5]);
        assertEquals("value2", flattened[6]);
    }
    
}
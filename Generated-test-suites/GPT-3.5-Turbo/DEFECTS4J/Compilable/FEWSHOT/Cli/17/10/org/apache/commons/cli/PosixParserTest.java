package org.apache.commons.cli;
import static org.junit.Assert.*;
import org.junit.Test;

public class PosixParserTest {
    
    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", false, "Option B");
        options.addOption("c", true, "Option C with argument");

        String[] arguments = { "-a", "--", "-b", "-c", "test" };
        
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);
        
        assertEquals(5, result.length);
        assertEquals("-a", result[0]);
        assertEquals("--", result[1]);
        assertEquals("-b", result[2]);
        assertEquals("-c", result[3]);
        assertEquals("test", result[4]);
    }

}
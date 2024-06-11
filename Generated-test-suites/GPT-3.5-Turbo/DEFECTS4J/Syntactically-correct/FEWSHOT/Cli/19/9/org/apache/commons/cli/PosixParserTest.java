import static org.junit.Assert.*;
import org.junit.Test;

public class PosixParserTest {
    
    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b");
        options.addOption("c", false, "option c");
        
        PosixParser parser = new PosixParser();

        // Test case 1: No options provided
        String[] args1 = {};
        String[] result1 = parser.flatten(options, args1, false);
        assertEquals(0, result1.length);

        // Test case 2: Option a provided
        String[] args2 = {"-a"};
        String[] result2 = parser.flatten(options, args2, false);
        assertEquals(1, result2.length);
        assertEquals("-a", result2[0]);

        // Test case 3: Option b with argument provided
        String[] args3 = {"-b", "value"};
        String[] result3 = parser.flatten(options, args3, false);
        assertEquals(2, result3.length);
        assertEquals("-b", result3[0]);
        assertEquals("value", result3[1]);

        // Test case 4: Option c provided multiple times
        String[] args4 = {"-c", "-c", "-c"};
        String[] result4 = parser.flatten(options, args4, false);
        assertEquals(3, result4.length);
        assertEquals("-c", result4[0]);
        assertEquals("-c", result4[1]);
        assertEquals("-c", result4[2]);
        
        // Test case 5: Stop at non-option
        String[] args5 = {"-a", "non-option"};
        String[] result5 = parser.flatten(options, args5, true);
        assertEquals(1, result5.length);
        assertEquals("-a", result5[0]);
    }

}
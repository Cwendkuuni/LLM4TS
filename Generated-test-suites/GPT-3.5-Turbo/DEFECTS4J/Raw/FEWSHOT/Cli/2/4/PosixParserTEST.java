import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class PosixParserTest {
    private Options options;
    private PosixParser parser;

    @Before
    public void setup() {
        options = new Options();
        parser = new PosixParser();
    }
    
    @Test
    public void testFlatten_StopAtNonOptionTrue() {
        options.addOption("f", false, "Test option f");
        options.addOption("b", true, "Test option b");
        options.addOption("a", true, "Test option a");
        options.addOption("c", false, "Test option c");
        
        String[] args = {"-f", "-a", "valueA", "--", "-c", "valueC", "extraArg"};
        String[] expected = {"-f", "-a", "valueA", "--", "-c", "valueC", "extraArg"};
        String[] result = parser.flatten(options, args, true);
        
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_StopAtNonOptionFalse() {
        options.addOption("f", false, "Test option f");
        options.addOption("b", true, "Test option b");
        options.addOption("a", true, "Test option a");
        
        String[] args = {"-f", "-a", "valueA", "-c", "valueC"};
        String[] expected = {"-f", "-a", "valueA", "-c", "valueC"};
        String[] result = parser.flatten(options, args, false);
        
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_SpecialToken() {
        options.addOption("f", false, "Test option f");
        
        String[] args = {"--"};
        String[] expected = {"--"};
        String[] result = parser.flatten(options, args, false);
        
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_SingleHyphen() {
        options.addOption("f", false, "Test option f");
        
        String[] args = {"-"};
        String[] expected = {"-"};
        String[] result = parser.flatten(options, args, false);
        
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_OptionToken_ValidId() {
        options.addOption("f", false, "Test option f");
        options.addOption("a", false, "Test option a");
        
        String[] args = {"-f", "-a"};
        String[] expected = {"-f", "-a"};
        String[] result = parser.flatten(options, args, false);
        
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_OptionToken_InvalidId_StopAtNonOption() {
        options.addOption("f", false, "Test option f");
        
        String[] args = {"-c"};
        String[] expected = {"-c"};
        String[] result = parser.flatten(options, args, true);
        
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_OptionToken_InvalidId_NoStopAtNonOption() {
        options.addOption("f", false, "Test option f");
        
        String[] args = {"-c"};
        String[] expected = {"-c"};
        String[] result = parser.flatten(options, args, false);
        
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_BurstToken() {
        options.addOption("f", false, "Test option f");
        options.addOption("a", false, "Test option a");
        
        String[] args = {"-fa"};
        String[] expected = {"-f", "-a"};
        String[] result = parser.flatten(options, args, false);
        
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_BurstToken_InvalidId_StopAtNonOption() {
        options.addOption("f", false, "Test option f");
        
        String[] args = {"-fc"};
        String[] expected = {"-f", "-c"};
        String[] result = parser.flatten(options, args, true);
        
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_BurstToken_InvalidId_NoStopAtNonOption() {
        options.addOption("f", false, "Test option f");
        
        String[] args = {"-fc"};
        String[] expected = {"-f", "-c"};
        String[] result = parser.flatten(options, args, false);
        
        assertArrayEquals(expected, result);
    }
    
    private void assertArrayEquals(String[] expected, String[] actual) {
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i]);
        }
    }
}
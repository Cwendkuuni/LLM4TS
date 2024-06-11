import org.junit.*;
import static org.junit.Assert.*;

public class PosixParserTest {
    
    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");
        options.addOption("b", "optionB", true, "Option B");
        options.addOption("c", "optionC", false, "Option C");

        PosixParser parser = new PosixParser();
        String[] arguments = {"-a", "-b", "valueB", "-c"};
        String[] flattened = parser.flatten(options, arguments, true);
        
        assertEquals(4, flattened.length);
        assertEquals("-a", flattened[0]);
        assertEquals("-b", flattened[1]);
        assertEquals("valueB", flattened[2]);
        assertEquals("-c", flattened[3]);
    }
    
    @Test
    public void testFlattenStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");
        options.addOption("b", "optionB", true, "Option B");
        options.addOption("c", "optionC", false, "Option C");

        PosixParser parser = new PosixParser();
        String[] arguments = {"-a", "-b", "valueB", "-c", "nonOption"};
        String[] flattened = parser.flatten(options, arguments, true);
        
        assertEquals(5, flattened.length);
        assertEquals("-a", flattened[0]);
        assertEquals("-b", flattened[1]);
        assertEquals("valueB", flattened[2]);
        assertEquals("-c", flattened[3]);
        assertEquals("nonOption", flattened[4]);
    }
    
    @Test
    public void testFlattenNoOptions() {
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] arguments = {"-a", "-b", "valueB", "-c"};
        String[] flattened = parser.flatten(options, arguments, true);
        
        assertEquals(4, flattened.length);
        assertEquals("-a", flattened[0]);
        assertEquals("-b", flattened[1]);
        assertEquals("valueB", flattened[2]);
        assertEquals("-c", flattened[3]);
    }
    
    @Test
    public void testFlattenNoOptionValues() {
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");
        options.addOption("b", "optionB", false, "Option B");
        options.addOption("c", "optionC", false, "Option C");

        PosixParser parser = new PosixParser();
        String[] arguments = {"-a", "-b", "-c"};
        String[] flattened = parser.flatten(options, arguments, true);
        
        assertEquals(3, flattened.length);
        assertEquals("-a", flattened[0]);
        assertEquals("-b", flattened[1]);
        assertEquals("-c", flattened[2]);
    }
    
    @Test
    public void testFlattenStopAtNonOptionFalse() {
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");
        options.addOption("b", "optionB", true, "Option B");
        options.addOption("c", "optionC", false, "Option C");

        PosixParser parser = new PosixParser();
        String[] arguments = {"-a", "-b", "valueB", "-c", "nonOption"};
        String[] flattened = parser.flatten(options, arguments, false);
        
        assertEquals(7, flattened.length);
        assertEquals("-a", flattened[0]);
        assertEquals("-b", flattened[1]);
        assertEquals("valueB", flattened[2]);
        assertEquals("-c", flattened[3]);
        assertEquals("--", flattened[4]);
        assertEquals("nonOption", flattened[5]);
        assertEquals(6, flattened[6]);
    }
    
    @Test
    public void testBurstToken() {
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");
        options.addOption("b", "optionB", false, "Option B");
        options.addOption("c", "optionC", true, "Option C");

        PosixParser parser = new PosixParser();
        String token = "-abcvalueC";
        parser.burstToken(token, true);
        
        String[] flattened = parser.flatten(options, new String[]{token}, true);
        
        assertEquals(4, flattened.length);
        assertEquals("-a", flattened[0]);
        assertEquals("-b", flattened[1]);
        assertEquals("-c", flattened[2]);
        assertEquals("valueC", flattened[3]);
    }
    
    @Test
    public void testBurstTokenStopAtNonOptionFalse() {
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");
        options.addOption("b", "optionB", false, "Option B");
        options.addOption("c", "optionC", true, "Option C");

        PosixParser parser = new PosixParser();
        String token = "-abcvalueCnonOption";
        parser.burstToken(token, false);
        
        String[] flattened = parser.flatten(options, new String[]{token}, false);
        
        assertEquals(6, flattened.length);
        assertEquals("-a", flattened[0]);
        assertEquals("-b", flattened[1]);
        assertEquals("-c", flattened[2]);
        assertEquals("valueC", flattened[3]);
        assertEquals("--", flattened[4]);
        assertEquals("nonOption", flattened[5]);
    }

}
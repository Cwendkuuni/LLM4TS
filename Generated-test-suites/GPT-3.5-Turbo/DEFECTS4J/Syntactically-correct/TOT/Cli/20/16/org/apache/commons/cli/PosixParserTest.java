import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {
    
    @Test
    public void testFlatten_StopAtNonOptionTrue() {
        Options options = new Options();
        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").hasArg().build());
        
        PosixParser parser = new PosixParser();
        String[] arguments = {"-a", "value1", "-b", "value2", "arg1", "arg2"};
        String[] result = parser.flatten(options, arguments, true);
        
        assertEquals(4, result.length);
        assertEquals("-a", result[0]);
        assertEquals("value1", result[1]);
        assertEquals("-b", result[2]);
        assertEquals("value2", result[3]);
    }
    
    @Test
    public void testFlatten_StopAtNonOptionFalse() {
        Options options = new Options();
        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").hasArg().build());
        
        PosixParser parser = new PosixParser();
        String[] arguments = {"-a", "value1", "-b", "value2", "arg1", "arg2"};
        String[] result = parser.flatten(options, arguments, false);
        
        assertEquals(6, result.length);
        assertEquals("-a", result[0]);
        assertEquals("value1", result[1]);
        assertEquals("-b", result[2]);
        assertEquals("value2", result[3]);
        assertEquals("arg1", result[4]);
        assertEquals("arg2", result[5]);
    }
    
    @Test
    public void testFlatten_LongOption() {
        Options options = new Options();
        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("long").hasArg().build());
        
        PosixParser parser = new PosixParser();
        String[] arguments = {"--long=value", "-a"};
        String[] result = parser.flatten(options, arguments, false);
        
        assertEquals(2, result.length);
        assertEquals("--long=value", result[0]);
        assertEquals("-a", result[1]);
    }
    
    @Test
    public void testFlatten_SingleHyphen() {
        Options options = new Options();
        options.addOption(Option.builder("a").build());
        
        PosixParser parser = new PosixParser();
        String[] arguments = {"-"};
        String[] result = parser.flatten(options, arguments, false);
        
        assertEquals(1, result.length);
        assertEquals("-", result[0]);
    }
    
    @Test
    public void testFlatten_ShortOption() {
        Options options = new Options();
        options.addOption(Option.builder("a").build());
        
        PosixParser parser = new PosixParser();
        String[] arguments = {"-a"};
        String[] result = parser.flatten(options, arguments, false);
        
        assertEquals(1, result.length);
        assertEquals("-a", result[0]);
    }
    
    @Test
    public void testFlatten_ShortOptionWithArg() {
        Options options = new Options();
        options.addOption(Option.builder("a").hasArg().build());
        
        PosixParser parser = new PosixParser();
        String[] arguments = {"-a", "value"};
        String[] result = parser.flatten(options, arguments, false);
        
        assertEquals(2, result.length);
        assertEquals("-a", result[0]);
        assertEquals("value", result[1]);
    }
    
    @Test
    public void testFlatten_InvalidShortOption_StopAtNonOptionTrue() {
        Options options = new Options();
        
        PosixParser parser = new PosixParser();
        String[] arguments = {"-x", "value"};
        String[] result = parser.flatten(options, arguments, true);
        
        assertEquals(2, result.length);
        assertEquals("-", result[0]);
        assertEquals("x", result[1]);
    }
    
    @Test
    public void testFlatten_InvalidShortOption_StopAtNonOptionFalse() {
        Options options = new Options();
        
        PosixParser parser = new PosixParser();
        String[] arguments = {"-x", "value"};
        String[] result = parser.flatten(options, arguments, false);
        
        assertEquals(2, result.length);
        assertEquals("-x", result[0]);
        assertEquals("value", result[1]);
    }
    
    @Test
    public void testFlatten_InvalidShortOption_NoStopAtNonOption_StopAtEnd() {
        Options options = new Options();
        
        PosixParser parser = new PosixParser();
        String[] arguments = {"-x"};
        String[] result = parser.flatten(options, arguments, false);
        
        assertEquals(1, result.length);
        assertEquals("-x", result[0]);
    }
    
    @Test
    public void testFlatten_InvalidShortOption_NoStopAtNonOption_StopInMiddle() {
        Options options = new Options();
        
        PosixParser parser = new PosixParser();
        String[] arguments = {"-x", "-a"};
        String[] result = parser.flatten(options, arguments, false);
        
        assertEquals(2, result.length);
        assertEquals("-x", result[0]);
        assertEquals("-a", result[1]);
    }
    
    @Test
    public void testFlatten_ArgumentsAfterNonOption_StopAtNonOptionTrue() {
        Options options = new Options();
        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").build());
        
        PosixParser parser = new PosixParser();
        String[] arguments = {"-a", "value", "arg1", "-b", "value", "arg2"};
        String[] result = parser.flatten(options, arguments, true);
        
        assertEquals(4, result.length);
        assertEquals("-a", result[0]);
        assertEquals("value", result[1]);
        assertEquals("arg1", result[2]);
        assertEquals("-b", result[3]);
    }
    
    @Test
    public void testFlatten_ArgumentsAfterNonOption_StopAtNonOptionFalse() {
        Options options = new Options();
        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").build());
        
        PosixParser parser = new PosixParser();
        String[] arguments = {"-a", "value", "arg1", "-b", "value", "arg2"};
        String[] result = parser.flatten(options, arguments, false);
        
        assertEquals(6, result.length);
        assertEquals("-a", result[0]);
        assertEquals("value", result[1]);
        assertEquals("arg1", result[2]);
        assertEquals("-b", result[3]);
        assertEquals("value", result[4]);
        assertEquals("arg2", result[5]);
    }
}
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testFlatten_StopAtNonOption_True() {
        String[] arguments = {"-a", "--option", "value", "arg1", "-bc"};
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption(Option.builder().longOpt("option").hasArg().desc("option with argument").build());
        PosixParser parser = new PosixParser();
        
        String[] result = parser.flatten(options, arguments, true);
        
        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("--option", result[1]);
        assertEquals("value", result[2]);
    }
    
    @Test
    public void testFlatten_StopAtNonOption_False() {
        String[] arguments = {"-a", "--option", "value", "arg1", "-bc"};
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption(Option.builder().longOpt("option").hasArg().desc("option with argument").build());
        PosixParser parser = new PosixParser();
        
        String[] result = parser.flatten(options, arguments, false);
        
        assertEquals(5, result.length);
        assertEquals("-a", result[0]);
        assertEquals("--option", result[1]);
        assertEquals("value", result[2]);
        assertEquals("arg1", result[3]);
        assertEquals("-bc", result[4]);
    }
    
    @Test
    public void testFlatten_NoOptions() {
        String[] arguments = {"arg1", "arg2"};
        Options options = new Options();
        PosixParser parser = new PosixParser();
        
        String[] result = parser.flatten(options, arguments, true);
        
        assertEquals(2, result.length);
        assertEquals("arg1", result[0]);
        assertEquals("arg2", result[1]);
    }
    
    @Test
    public void testFlatten_SingleHyphen() {
        String[] arguments = {"-"};
        Options options = new Options();
        PosixParser parser = new PosixParser();
        
        String[] result = parser.flatten(options, arguments, true);
        
        assertEquals(1, result.length);
        assertEquals("-", result[0]);
    }
    
    @Test
    public void testFlatten_SpecialToken() {
        String[] arguments = {"--option1=value1", "--option2"};
        Options options = new Options();
        options.addOption(Option.builder().longOpt("option1").hasArg().desc("option 1 with argument").build());
        options.addOption("option2", false, "option 2");
        PosixParser parser = new PosixParser();
        
        String[] result = parser.flatten(options, arguments, true);
        
        assertEquals(3, result.length);
        assertEquals("--option1", result[0]);
        assertEquals("value1", result[1]);
        assertEquals("--option2", result[2]);
    }
    
    @Test
    public void testBurstToken_ValidOptions() {
        String token = "-abc";
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", false, "option b");
        options.addOption("c", true, "option c");
        PosixParser parser = new PosixParser();
        
        parser.burstToken(token, true);
        String[] result = parser.flatten(options, new String[]{}, true);
        
        assertEquals(4, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("-c", result[2]);
        assertEquals("", result[3]);
    }
    
    @Test
    public void testBurstToken_InvalidOptions_StopAtNonOption() {
        String token = "-abz";
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", false, "option b");
        PosixParser parser = new PosixParser();
        
        parser.burstToken(token, true);
        String[] result = parser.flatten(options, new String[]{}, true);
        
        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("z", result[2]);
    }
    
    @Test
    public void testBurstToken_InvalidOptions_NotStopAtNonOption() {
        String token = "-abz";
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", false, "option b");
        PosixParser parser = new PosixParser();
        
        parser.burstToken(token, false);
        String[] result = parser.flatten(options, new String[]{}, false);
        
        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("-z", result[2]);
    }

}
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {
    
    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        parser.init();
        assertEquals(false, parser.eatTheRest);
        assertEquals(0, parser.tokens.size());
        assertEquals(null, parser.currentOption);
    }
    
    @Test
    public void testFlatten() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String[] arguments = new String[]{"--foo", "bar", "-a", "--option1=value1", "-", "test"};
        boolean stopAtNonOption = true;
        String[] result = parser.flatten(options, arguments, stopAtNonOption);
        assertEquals(7, result.length);
    }
    
    @Test
    public void testGobble() {
        PosixParser parser = new PosixParser();
        parser.init();
        Iterator iter = Arrays.asList("--foo", "bar", "-a").iterator();
        parser.gobble(iter);
        assertEquals(3, parser.tokens.size());
    }
    
    @Test
    public void testProcess() {
        PosixParser parser = new PosixParser();
        parser.init();
        parser.currentOption = new Option("t", "test", false, "Test option");
        parser.currentOption.setArgs(2);
        parser.process("value1");
        assertEquals(true, parser.eatTheRest);
        assertEquals(2, parser.tokens.size());
    }
    
    @Test
    public void testProcessOptionToken() {
        PosixParser parser = new PosixParser();
        parser.init();
        Options options = new Options();
        options.addOption("a", false, "Option a");
        parser.options = options;
        parser.processOptionToken("-a", true);
        assertEquals(1, parser.tokens.size());
        assertEquals(null, parser.currentOption);
    }
    
    @Test
    public void testBurstToken() {
        PosixParser parser = new PosixParser();
        parser.init();
        Options options = new Options();
        options.addOption("a", false, "Option a");
        parser.options = options;
        parser.burstToken("-abc", true);
        assertEquals(1, parser.tokens.size());
    }
    
}
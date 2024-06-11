import static org.junit.Assert.*;
import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        parser.init();
        
    }

    @Test
    public void testProcessSingleHyphen() {
        PosixParser parser = new PosixParser();
        parser.processSingleHyphen("-");
        
    }

    @Test
    public void testProcessOptionToken() {
        PosixParser parser = new PosixParser();
        parser.processOptionToken("-a", true);
        
    }

    @Test
    public void testBurstToken() {
        PosixParser parser = new PosixParser();
        parser.burstToken("-abc", true);
        
    }

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        String[] arguments = {"-a", "value", "--option", "optionValue", "-bc"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);
        assertNotNull(result);
        
    }

    @Test
    public void testGobble() {
        PosixParser parser = new PosixParser();
        parser.gobble(Arrays.asList("a", "b").iterator());
        
    }

    @Test
    public void testProcess() {
        PosixParser parser = new PosixParser();
        parser.process("value");
        
    }

}
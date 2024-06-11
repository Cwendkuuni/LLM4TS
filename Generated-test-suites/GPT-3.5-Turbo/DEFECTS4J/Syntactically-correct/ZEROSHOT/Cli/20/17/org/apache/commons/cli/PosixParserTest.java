import org.junit.Test;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.Option;

public class PosixParserTest {
    
    @Test
    public void testInit() {
        PosixParser posixParser = new PosixParser();
        posixParser.init();
    }
    
    @Test
    public void testFlatten() {
        PosixParser posixParser = new PosixParser();
        Options options = new Options();
        String[] arguments = new String[]{"--option1", "value1", "-option2", "value2"};
        boolean stopAtNonOption = false;
        posixParser.flatten(options, arguments, stopAtNonOption);
    }
    
    @Test
    public void testGobble() {
        PosixParser posixParser = new PosixParser();
        Iterator<String> iter = Arrays.asList("token1", "token2", "token3").iterator();
        posixParser.gobble(iter);
    }
    
    @Test
    public void testProcess() {
        PosixParser posixParser = new PosixParser();
        String value = "value";
        posixParser.process(value);
    }
    
    @Test
    public void testProcessOptionToken() {
        PosixParser posixParser = new PosixParser();
        String token = "-option";
        boolean stopAtNonOption = false;
        posixParser.processOptionToken(token, stopAtNonOption);
    }
    
    @Test
    public void testBurstToken() {
        PosixParser posixParser = new PosixParser();
        String token = "-option";
        boolean stopAtNonOption = false;
        posixParser.burstToken(token, stopAtNonOption);
    }
}
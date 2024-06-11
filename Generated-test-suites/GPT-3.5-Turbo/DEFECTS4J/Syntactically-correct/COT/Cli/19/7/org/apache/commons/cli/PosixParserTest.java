import org.junit.Test;
import org.apache.commons.cli.*;

public class PosixParserTest {

    @Test
    public void testFlatten() {
        Options options = new Options();
        String[] arguments = new String[]{"-a", "--option", "value", "-b"};
        boolean stopAtNonOption = false;
        PosixParser parser = new PosixParser();
        parser.flatten(options, arguments, stopAtNonOption);
    }

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        parser.init();
    }

    @Test
    public void testProcessOptionToken() {
        Options options = new Options();
        String token = "-a";
        boolean stopAtNonOption = false;
        PosixParser parser = new PosixParser();
        parser.processOptionToken(token, stopAtNonOption);
    }

    @Test
    public void testProcess() {
        String value = "--option";
        PosixParser parser = new PosixParser();
        parser.process(value);
    }

    @Test
    public void testBurstToken() {
        String token = "-abc";
        boolean stopAtNonOption = false;
        PosixParser parser = new PosixParser();
        parser.burstToken(token, stopAtNonOption);
    }
}
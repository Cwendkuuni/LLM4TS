import org.junit.Test;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        parser.init();
    }

    @Test
    public void testFlatten() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").hasArg().build());
        options.addOption(Option.builder("c").hasArgs().build());
        String[] arguments = {"-a", "-b", "value", "-c", "value1", "value2"};
        String[] flattened = parser.flatten(options, arguments, false);
        assertArrayEquals(arguments, flattened);
    }

    @Test
    public void testProcess() {
        PosixParser parser = new PosixParser();
        parser.process("value");
    }

    @Test
    public void testProcessSingleHyphen() {
        PosixParser parser = new PosixParser();
        parser.processSingleHyphen("-");
    }

    @Test
    public void testProcessOptionToken() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").build());
        parser.processOptionToken("a", false);
    }

    @Test
    public void testBurstToken() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").build());
        parser.burstToken("-ab", false);
    }
}
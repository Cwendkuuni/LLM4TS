import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        parser.init();
    }

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        
        PosixParser parser = new PosixParser();
        parser.flatten(options, new String[]{"-a", "-b", "value"}, false);
    }

    @Test
    public void testGobble() {
        PosixParser parser = new PosixParser();
        Iterator iter = Arrays.asList(new String[]{"arg1", "arg2", "arg3"}).iterator();
        parser.gobble(iter);
    }

    @Test
    public void testProcess() {
        PosixParser parser = new PosixParser();
        parser.process("arg");
    }

    @Test
    public void testProcessOptionToken() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        
        PosixParser parser = new PosixParser();
        parser.processOptionToken("-a", false);
    }

    @Test
    public void testBurstToken() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        
        PosixParser parser = new PosixParser();
        parser.burstToken("-ab", false);
    }
}
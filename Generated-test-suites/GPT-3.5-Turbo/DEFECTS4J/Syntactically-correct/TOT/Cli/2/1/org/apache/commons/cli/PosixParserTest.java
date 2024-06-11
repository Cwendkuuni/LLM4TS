import static org.junit.Assert.*;
import org.junit.Test;

public class PosixParserTest {

    // Testing the init() method
    @Test
    public void testInit() {
        PosixParser posixParser = new PosixParser();
        posixParser.init();
        ArrayList<String> tokens = posixParser.getTokens();
        assertEquals(0, tokens.size());
        assertFalse(posixParser.getEatTheRest());
        assertNull(posixParser.getCurrentOption());
    }

    // Testing the flatten() method with stopAtNonOption = true
    @Test
    public void testFlattenStopAtNonOptionTrue() {
        PosixParser posixParser = new PosixParser();
        Options options = new Options();
        options.addOption(Option.builder("a").hasArg().build());
        options.addOption(Option.builder("b").build());
        options.addOption(Option.builder("c").build());
        String[] arguments = {"-a", "value1", "-b", "--", "-c", "-d"};
        String[] expectedTokens = {"-a", "value1", "-b", "--", "-c", "-d"};
        String[] flattenedTokens = posixParser.flatten(options, arguments, true);
        assertArrayEquals(expectedTokens, flattenedTokens);
    }

    // Testing the flatten() method with stopAtNonOption = false
    @Test
    public void testFlattenStopAtNonOptionFalse() {
        PosixParser posixParser = new PosixParser();
        Options options = new Options();
        options.addOption(Option.builder("a").hasArg().build());
        options.addOption(Option.builder("b").build());
        options.addOption(Option.builder("c").build());
        String[] arguments = {"-a", "value1", "-b", "--", "-c", "-d"};
        String[] expectedTokens = {"-a", "value1", "-b", "--", "-c", "-d"};
        String[] flattenedTokens = posixParser.flatten(options, arguments, false);
        assertArrayEquals(expectedTokens, flattenedTokens);
    }

    // Testing the flatten() method with burstToken()
    @Test
    public void testFlattenBurstToken() {
        PosixParser posixParser = new PosixParser();
        Options options = new Options();
        options.addOption(Option.builder("a").hasArg().build());
        options.addOption(Option.builder("b").build());
        options.addOption(Option.builder("c").build());
        String[] arguments = {"-abc", "-d"};
        String[] expectedTokens = {"-a", "-b", "-c", "-d"};
        String[] flattenedTokens = posixParser.flatten(options, arguments, false);
        assertArrayEquals(expectedTokens, flattenedTokens);
    }

    // Testing the processSingleHyphen() method
    @Test
    public void testProcessSingleHyphen() {
        PosixParser posixParser = new PosixParser();
        posixParser.processSingleHyphen("-");
        ArrayList<String> tokens = posixParser.getTokens();
        assertEquals(1, tokens.size());
        assertEquals("-", tokens.get(0));
    }

    // Testing the processOptionToken() method
    @Test
    public void testProcessOptionToken() {
        PosixParser posixParser = new PosixParser();
        Options options = new Options();
        options.addOption(Option.builder("a").hasArg().build());
        options.addOption(Option.builder("b").build());
        options.addOption(Option.builder("c").build());
        posixParser.setOptions(options);
        String token = "-a";
        boolean stopAtNonOption = false;
        posixParser.processOptionToken(token, stopAtNonOption);
        ArrayList<String> tokens = posixParser.getTokens();
        assertEquals(1, tokens.size());
        assertEquals("-a", tokens.get(0));
        assertEquals("-a", posixParser.getCurrentOption().getOpt());
    }

    // Testing the process() method
    @Test
    public void testProcess() {
        PosixParser posixParser = new PosixParser();
        Options options = new Options();
        options.addOption(Option.builder("a").hasArg().build());
        options.addOption(Option.builder("b").build());
        options.addOption(Option.builder("c").build());
        posixParser.setOptions(options);
        String value = "value1";
        posixParser.process(value);
        ArrayList<String> tokens = posixParser.getTokens();
        assertEquals(1, tokens.size());
        assertEquals("value1", tokens.get(0));
        assertNull(posixParser.getCurrentOption());
    }

    // Testing the gobble() method
    @Test
    public void testGobble() {
        PosixParser posixParser = new PosixParser();
        posixParser.setEatTheRest(true);
        ArrayList<String> tokens = new ArrayList<>();
        tokens.add("value1");
        tokens.add("value2");
        Iterator<String> iter = tokens.iterator();
        posixParser.gobble(iter);
        ArrayList<String> processedTokens = posixParser.getTokens();
        assertEquals(2, processedTokens.size());
        assertEquals("value1", processedTokens.get(0));
        assertEquals("value2", processedTokens.get(1));
    }

    // Testing the burstToken() method
    @Test
    public void testBurstToken() {
        PosixParser posixParser = new PosixParser();
        Options options = new Options();
        options.addOption(Option.builder("a").hasArg().build());
        options.addOption(Option.builder("b").build());
        options.addOption(Option.builder("c").build());
        posixParser.setOptions(options);
        String token = "-abx";
        boolean stopAtNonOption = false;
        posixParser.burstToken(token, stopAtNonOption);
        ArrayList<String> tokens = posixParser.getTokens();
        assertEquals(3, tokens.size());
        assertEquals("-a", tokens.get(0));
        assertEquals("-b", tokens.get(1));
        assertEquals("-x", tokens.get(2));
    }

    // Testing the PosixParser constructor
    @Test
    public void testConstructor() {
        PosixParser posixParser = new PosixParser();
        assertNotNull(posixParser.getTokens());
        assertEquals(0, posixParser.getTokens().size());
    }
}
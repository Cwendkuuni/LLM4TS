import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        parser.init();
        assertFalse(parser.eatTheRest);
        assertEquals(0, parser.tokens.size());
        assertNull(parser.currentOption);
    }

    @Test
    public void testFlatten() {
        PosixParser parser = new PosixParser();

        // Test case 1
        Options options1 = new Options();
        String[] arguments1 = {"--option1", "value1", "--option2=value2"};
        String[] expected1 = {"--option1", "value1", "--option2", "value2"};
        String[] result1 = parser.flatten(options1, arguments1, false);
        assertArrayEquals(expected1, result1);

        // Test case 2
        Options options2 = new Options();
        options2.addOption("a", false, "Option a");
        options2.addOption("b", true, "Option b");
        String[] arguments2 = {"-a", "-b", "value"};
        String[] expected2 = {"-a", "-b", "value"};
        String[] result2 = parser.flatten(options2, arguments2, false);
        assertArrayEquals(expected2, result2);

        // Test case 3
        Options options3 = new Options();
        options3.addOption("o", false, "Option o");
        options3.addOption("p", false, "Option p");
        String[] arguments3 = {"-op"};
        String[] expected3 = {"-op"};
        String[] result3 = parser.flatten(options3, arguments3, false);
        assertArrayEquals(expected3, result3);
    }

    @Test
    public void testGobble() {
        PosixParser parser = new PosixParser();
        Iterator<String> iter = Arrays.asList("a", "b", "c").iterator();
        parser.eatTheRest = true;
        parser.gobble(iter);
        List<String> expected = Arrays.asList("a", "b", "c");
        assertEquals(expected, parser.tokens);
    }

    @Test
    public void testProcess() {
        PosixParser parser = new PosixParser();
        parser.currentOption = new Option("o", false, "Option o");
        parser.currentOption.setArgs(Option.UNLIMITED_VALUES);
        String value = "value";
        parser.process(value);
        List<String> expected = Arrays.asList(value);
        assertEquals(expected, parser.tokens);
        assertNull(parser.currentOption);

        parser.currentOption = new Option("o", false, "Option o");
        parser.currentOption.setArgs(1);
        parser.process(value);
        expected.add(value);
        assertEquals(expected, parser.tokens);
        assertNull(parser.currentOption);

        parser.currentOption = null;
        String nonOption = "nonOption";
        parser.process(nonOption);
        expected.add("--");
        expected.add(nonOption);
        assertEquals(expected, parser.tokens);
        assertTrue(parser.eatTheRest);
    }

    @Test
    public void testProcessSingleHyphen() {
        PosixParser parser = new PosixParser();
        String hyphen = "-";
        parser.processSingleHyphen(hyphen);
        List<String> expected = Arrays.asList(hyphen);
        assertEquals(expected, parser.tokens);
    }

    @Test
    public void testProcessOptionToken() {
        PosixParser parser = new PosixParser();
        parser.options = new Options();
        parser.options.addOption("o", false, "Option o");
        parser.options.addOption("p", false, "Option p");
        String validToken = "-o";
        boolean stopAtNonOption1 = false;
        parser.processOptionToken(validToken, stopAtNonOption1);
        List<String> expected1 = Arrays.asList(validToken);
        assertEquals(expected1, parser.tokens);

        String invalidToken = "-q";
        boolean stopAtNonOption2 = true;
        parser.processOptionToken(invalidToken, stopAtNonOption2);
        assertTrue(parser.eatTheRest);
    }

    @Test
    public void testBurstToken() {
        PosixParser parser = new PosixParser();
        parser.options = new Options();
        parser.options.addOption("o", false, "Option o");
        parser.options.addOption("p", true, "Option p");
        String token = "--option";
        boolean stopAtNonOption1 = false;
        parser.burstToken(token, stopAtNonOption1);
        List<String> expected1 = Arrays.asList(token);
        assertEquals(expected1, parser.tokens);

        String token2 = "-op";
        boolean stopAtNonOption2 = true;
        parser.burstToken(token2, stopAtNonOption2);
        List<String> expected2 = Arrays.asList(token2);
        assertEquals(expected2, parser.tokens);

        parser.init();
        parser.burstToken(token2, false);
        List<String> expected3 = Arrays.asList("-o", "-p");
        assertEquals(expected3, parser.tokens);
    }
}
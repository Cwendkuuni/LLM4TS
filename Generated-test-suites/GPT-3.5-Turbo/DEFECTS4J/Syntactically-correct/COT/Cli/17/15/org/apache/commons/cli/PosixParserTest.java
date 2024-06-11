import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testFlatten_stopAtNonOption_true() {
        Options options = new Options();

        PosixParser parser = new PosixParser();

        String[] arguments = {"-a", "value1", "arg1", "-b", "value2"};

        String[] expected = {"-a", "value1", "arg1", "-b", "value2"};
        assertArrayEquals(expected, parser.flatten(options, arguments, true));
    }

    @Test
    public void testFlatten_stopAtNonOption_false() {
        Options options = new Options();

        PosixParser parser = new PosixParser();

        String[] arguments = {"-a", "value1", "arg1", "-b", "value2"};

        String[] expected = {"-a", "value1", "arg1", "-b", "value2"};
        assertArrayEquals(expected, parser.flatten(options, arguments, false));
    }

    @Test
    public void testProcessOptionToken_existingOption() {
        Options options = new Options();
        Option optionA = new Option("a", false, "Option A");
        options.addOption(optionA);

        PosixParser parser = new PosixParser();

        boolean stopAtNonOption = false;
        String token = "-a";

        boolean expected = true;
        Option result = parser.processOptionToken(token, options, stopAtNonOption);
        assertEquals(expected, result.equals(optionA));
    }

    @Test
    public void testProcessOptionToken_nonExistingOption_stopAtNonOption_true() {
        Options options = new Options();

        PosixParser parser = new PosixParser();

        boolean stopAtNonOption = true;
        String token = "-c";

        boolean expected = true;
        Option result = parser.processOptionToken(token, options, stopAtNonOption);
        assertEquals(expected, result == null);
    }

    @Test
    public void testProcessOptionToken_nonExistingOption_stopAtNonOption_false() {
        Options options = new Options();

        PosixParser parser = new PosixParser();

        boolean stopAtNonOption = false;
        String token = "-c";

        boolean expected = true;
        Option result = parser.processOptionToken(token, options, stopAtNonOption);
        assertEquals(expected, result == null);
    }

    @Test
    public void testBurstToken() {
        Options options = new Options();
        Option optionA = new Option("a", false, "Option A");
        options.addOption(optionA);
        Option optionB = new Option("b", true, "Option B");
        options.addOption(optionB);

        PosixParser parser = new PosixParser();

        boolean stopAtNonOption = false;
        String token = "-abvalue";

        String[] expected = {"-a", "-bvalue"};
        String[] result = parser.burstToken(token, options, stopAtNonOption);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testProcessSingleHyphen() {
        Options options = new Options();
        Option optionA = new Option("a", false, "Option A");
        options.addOption(optionA);

        PosixParser parser = new PosixParser();

        String hyphen = "-";

        String[] expected = {"-"};
        String[] result = parser.processSingleHyphen(hyphen, options);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testProcess_noCurrentOption_argOption() {
        Options options = new Options();
        Option optionA = new Option("a", true, "Option A");
        options.addOption(optionA);

        PosixParser parser = new PosixParser();
        parser.setCurrentOption(null);

        String value = "argValue";

        String[] expected = {"--", "argValue"};
        String[] result = parser.process(value, options);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testProcess_hasCurrentOption_noArgsOption() {
        Options options = new Options();
        Option optionA = new Option("a", false, "Option A");
        options.addOption(optionA);

        PosixParser parser = new PosixParser();
        parser.setCurrentOption(optionA);

        String value = "argValue";

        String[] expected = {"argValue"};
        String[] result = parser.process(value, options);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testProcess_hasCurrentOption_hasArgsOption() {
        Options options = new Options();
        Option optionA = new Option("a", true, "Option A");
        options.addOption(optionA);

        PosixParser parser = new PosixParser();
        parser.setCurrentOption(optionA);

        String value = "argValue";

        String[] expected = {"argValue"};
        String[] result = parser.process(value, options);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testGobble() {
        Options options = new Options();

        PosixParser parser = new PosixParser();
        parser.setEatTheRest(true);

        Iterator iter = Arrays.asList("arg1", "arg2").iterator();

        String[] expected = {"arg1", "arg2"};
        String[] result = parser.gobble(iter);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        parser.setEatTheRest(true);
        parser.setCurrentOption(new Option("a"));

        parser.init();

        assertEquals(false, parser.isEatTheRest());
        assertEquals(null, parser.getCurrentOption());
    }
}
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        parser.init();
        assertTrue(parser.tokens.isEmpty());
        assertFalse(parser.eatTheRest);
        assertNull(parser.currentOption);
    }

    @Test
    public void testFlatten_StopAtNonOption_false() {
        Options options = new Options();
        options.addOption("a", "option a");
        options.addOption("b", "option b");
        options.addOption("c", "option c");

        String[] arguments = {"-a", "-b", "value1", "value2"};

        PosixParser parser = new PosixParser();
        String[] flattened = parser.flatten(options, arguments, false);

        assertEquals("-a", flattened[0]);
        assertEquals("-b", flattened[1]);
        assertEquals("value1", flattened[2]);
        assertEquals("value2", flattened[3]);
    }

    @Test
    public void testFlatten_StopAtNonOption_true() {
        Options options = new Options();
        options.addOption("a", "option a");
        options.addOption("b", "option b");
        options.addOption("c", "option c");

        String[] arguments = {"-a", "-b", "value1", "value2"};

        PosixParser parser = new PosixParser();
        String[] flattened = parser.flatten(options, arguments, true);

        assertEquals("-a", flattened[0]);
        assertEquals("-b", flattened[1]);
    }

    @Test
    public void testGobble() {
        PosixParser parser = new PosixParser();
        parser.tokens.add("value1");
        parser.tokens.add("value2");

        Iterator iterator = Arrays.asList("remaining1", "remaining2").iterator();
        parser.gobble(iterator);

        assertEquals("value1", parser.tokens.get(0));
        assertEquals("value2", parser.tokens.get(1));
        assertEquals("remaining1", parser.tokens.get(2));
        assertEquals("remaining2", parser.tokens.get(3));
    }

    @Test
    public void testProcess_currentOption_hasArg_true() {
        PosixParser parser = new PosixParser();
        parser.currentOption = new Option("a", "option a", true);
        parser.process("value");

        assertTrue(parser.tokens.contains("value"));
        assertNull(parser.currentOption);
    }

    @Test
    public void testProcess_currentOption_hasArg_false() {
        PosixParser parser = new PosixParser();
        parser.currentOption = new Option("b", "option b", false);
        parser.process("value");

        assertTrue(parser.tokens.contains("value"));
        assertNull(parser.currentOption);
    }

    @Test
    public void testProcessOptionToken_optionExists() {
        Options options = new Options();
        options.addOption("a", "option a");

        PosixParser parser = new PosixParser();
        parser.options = options;
        parser.processOptionToken("-a", false);

        assertTrue(parser.tokens.contains("-a"));
        assertNotNull(parser.currentOption);
        assertEquals("option a", parser.currentOption.getDescription());
    }

    @Test
    public void testProcessOptionToken_optionDoesNotExist_stopAtNonOption_true() {
        Options options = new Options();
        options.addOption("a", "option a");
        options.addOption("b", "option b");

        PosixParser parser = new PosixParser();
        parser.options = options;
        parser.processOptionToken("-c", true);

        assertTrue(parser.tokens.contains("-c"));
        assertTrue(parser.eatTheRest);
        assertNull(parser.currentOption);
    }

    @Test
    public void testProcessOptionToken_optionDoesNotExist_stopAtNonOption_false() {
        Options options = new Options();
        options.addOption("a", "option a");
        options.addOption("b", "option b");

        PosixParser parser = new PosixParser();
        parser.options = options;
        parser.processOptionToken("-c", false);

        assertTrue(parser.tokens.contains("-c"));
        assertFalse(parser.eatTheRest);
        assertNull(parser.currentOption);
    }

    @Test
    public void testBurstToken_optionExists_noArgument() {
        Options options = new Options();
        options.addOption("a", "option a");
        options.addOption("b", "option b");

        PosixParser parser = new PosixParser();
        parser.options = options;
        parser.burstToken("-a", false);

        assertTrue(parser.tokens.contains("-a"));
    }

    @Test
    public void testBurstToken_optionExists_withArgument() {
        Options options = new Options();
        options.addOption("a", "option a");

        PosixParser parser = new PosixParser();
        parser.options = options;
        parser.burstToken("-a=value", false);

        assertTrue(parser.tokens.contains("-a"));
        assertTrue(parser.tokens.contains("value"));
    }

    @Test
    public void testBurstToken_optionDoesNotExist_stopAtNonOption_false() {
        Options options = new Options();
        options.addOption("a", "option a");
        options.addOption("b", "option b");

        PosixParser parser = new PosixParser();
        parser.options = options;
        parser.burstToken("-c=value", false);

        assertTrue(parser.tokens.contains("-c"));
    }

    @Test
    public void testBurstToken_optionDoesNotExist_stopAtNonOption_true() {
        Options options = new Options();
        options.addOption("a", "option a");
        options.addOption("b", "option b");

        PosixParser parser = new PosixParser();
        parser.options = options;
        parser.burstToken("-c=value", true);

        assertTrue(parser.tokens.contains("--"));
        assertTrue(parser.tokens.contains("-c=value"));
    }
}
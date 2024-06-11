import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testFlatten_StopAtNonOptionTrue() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b with argument");

        String[] arguments = {"-a", "-b", "argument"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);

        assertTrue(result.length == 4);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("argument", result[2]);
        assertEquals("--", result[3]);
    }

    @Test
    public void testFlatten_StopAtNonOptionFalse() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b with argument");

        String[] arguments = {"-a", "-b", "argument"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertTrue(result.length == 4);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("argument", result[2]);
        assertEquals("--", result[3]);
    }

    @Test
    public void testBurstToken_ValidOption() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", false, "Option b");

        String token = "-ab";
        PosixParser parser = new PosixParser();
        parser.burstToken(token, false);

        String[] result = parser.flatten(options, new String[0], false);

        assertTrue(result.length == 2);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
    }

    @Test
    public void testBurstToken_InvalidOption_StopAtNonOptionTrue() {
        Options options = new Options();
        options.addOption("a", false, "Option a");

        String token = "-ab";
        PosixParser parser = new PosixParser();
        parser.burstToken(token, true);

        String[] result = parser.flatten(options, new String[0], false);

        assertTrue(result.length == 0);
    }
    
    @Test
    public void testBurstToken_InvalidOption_StopAtNonOptionFalse() {
        Options options = new Options();
        options.addOption("a", false, "Option a");

        String token = "-ab";
        PosixParser parser = new PosixParser();
        parser.burstToken(token, false);

        String[] result = parser.flatten(options, new String[0], false);

        assertTrue(result.length == 2);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
    }

    @Test
    public void testProcess_SingleHyphen() {
        Options options = new Options();
        options.addOption("a", false, "Option a");

        String value = "-";
        PosixParser parser = new PosixParser();
        parser.process(value);

        String[] result = parser.flatten(options, new String[0], false);

        assertTrue(result.length == 1);
        assertEquals("-", result[0]);
    }

    @Test
    public void testProcess_NoCurrentOptionWithStopAtNonOptionTrue() {
        Options options = new Options();
        options.addOption("a", false, "Option a");

        String value = "value";
        PosixParser parser = new PosixParser();
        parser.process(value);

        String[] result = parser.flatten(options, new String[0], true);

        assertTrue(result.length == 2);
        assertEquals("--", result[0]);
        assertEquals("value", result[1]);
    }

    @Test
    public void testProcess_NoCurrentOptionWithStopAtNonOptionFalse() {
        Options options = new Options();
        options.addOption("a", false, "Option a");

        String value = "value";
        PosixParser parser = new PosixParser();
        parser.process(value);

        String[] result = parser.flatten(options, new String[0], false);

        assertTrue(result.length == 1);
        assertEquals(value, result[0]);
    }

    @Test
    public void testProcess_CurrentOptionWithoutArg() {
        Options options = new Options();
        options.addOption("a", false, "Option a");

        PosixParser parser = new PosixParser();
        parser.process("-a");

        String[] result = parser.flatten(options, new String[0], false);

        assertTrue(result.length == 1);
        assertEquals("-a", result[0]);
    }

    @Test
    public void testProcess_CurrentOptionWithArg() {
        Options options = new Options();
        options.addOption("a", true, "Option a");

        PosixParser parser = new PosixParser();
        parser.process("-a");

        String[] result = parser.flatten(options, new String[0], false);

        assertTrue(result.length == 1);
        assertEquals("-a", result[0]);
    }

    @Test
    public void testProcess_CurrentOptionWithArgs() {
        Options options = new Options();
        options.addOption("a", true, "Option a");

        PosixParser parser = new PosixParser();
        parser.process("-a");

        String[] result = parser.flatten(options, new String[0], false);

        assertTrue(result.length == 1);
        assertEquals("-a", result[0]);
    }

    @Test
    public void testGobble_EatTheRestTrue() {
        Options options = new Options();
        options.addOption("a", false, "Option a");

        String[] arguments = {"-a", "arg1", "arg2"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, arguments, true);

        String[] result = parser.flatten(options, new String[0], false);

        assertTrue(result.length == 2);
        assertEquals("arg1", result[0]);
        assertEquals("arg2", result[1]);
    }

    @Test
    public void testGobble_EatTheRestFalse() {
        Options options = new Options();
        options.addOption("a", false, "Option a");

        String[] arguments = {"-a", "arg1", "arg2"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, arguments, false);

        String[] result = parser.flatten(options, new String[0], false);

        assertTrue(result.length == 0);
    }

}
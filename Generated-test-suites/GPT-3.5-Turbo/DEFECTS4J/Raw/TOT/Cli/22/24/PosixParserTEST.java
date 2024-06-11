import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testFlatten_LongOptionWithoutValue_ReturnsCorrectArray() {
        Options options = new Options();
        options.addOption("foo", false, "Foo option");

        String[] arguments = {"--foo"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);

        assertEquals(2, result.length);
        assertEquals("--", result[0]);
        assertEquals("foo", result[1]);
    }

    @Test
    public void testFlatten_LongOptionWithValue_ReturnsCorrectArray() {
        Options options = new Options();
        options.addOption("foo", true, "Foo option");

        String[] arguments = {"--foo=bar"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);

        assertEquals(3, result.length);
        assertEquals("--", result[0]);
        assertEquals("foo", result[1]);
        assertEquals("bar", result[2]);
    }

    @Test
    public void testFlatten_SingleHyphen_ReturnsCorrectArray() {
        Options options = new Options();

        String[] arguments = {"-"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);

        assertEquals(1, result.length);
        assertEquals("-", result[0]);
    }

    @Test
    public void testFlatten_ShortOption_ReturnsCorrectArray() {
        Options options = new Options();
        options.addOption("a", false, "A option");

        String[] arguments = {"-a"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);

        assertEquals(1, result.length);
        assertEquals("a", result[0]);
    }

    @Test
    public void testFlatten_UnknownShortOption_ReturnsCorrectArray() {
        Options options = new Options();

        String[] arguments = {"-b"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);

        assertEquals(1, result.length);
        assertEquals("b", result[0]);
    }

    @Test
    public void testFlatten_ShortOptionWithValue_ReturnsCorrectArray() {
        Options options = new Options();
        options.addOption("a", true, "A option");

        String[] arguments = {"-a", "value"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);

        assertEquals(2, result.length);
        assertEquals("a", result[0]);
        assertEquals("value", result[1]);
    }

    @Test
    public void testFlatten_UnknownShortOptionWithValue_ReturnsCorrectArray() {
        Options options = new Options();

        String[] arguments = {"-b", "value"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);

        assertEquals(2, result.length);
        assertEquals("b", result[0]);
        assertEquals("value", result[1]);
    }

    @Test
    public void testFlatten_UnknownShortOptionWithValue_StopAtNonOption_ReturnsCorrectArray() {
        Options options = new Options();

        String[] arguments = {"-b", "value"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(2, result.length);
        assertEquals("-b", result[0]);
        assertEquals("value", result[1]);
    }

    @Test
    public void testFlatten_NonOptionToken_StopAtNonOption_ReturnsCorrectArray() {
        Options options = new Options();

        String[] arguments = {"value"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(1, result.length);
        assertEquals("value", result[0]);
    }

    @Test
    public void testFlatten_NonOptionToken_DoesNotStopAtNonOption_ReturnsCorrectArray() {
        Options options = new Options();

        String[] arguments = {"value"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);

        assertEquals(1, result.length);
        assertEquals("value", result[0]);
    }

    @Test
    public void testBurstToken_ValidOptionId_ReturnsCorrectArray() {
        Options options = new Options();
        options.addOption("a", false, "A option");
        options.addOption("b", false, "B option");

        String token = "-ab";

        PosixParser parser = new PosixParser();
        parser.burstToken(token, false);
      
        List<String> result = parser.getTokens();

        assertEquals(2, result.size());
        assertEquals("-a", result.get(0));
        assertEquals("-b", result.get(1));
    }

    @Test
    public void testBurstToken_ValidOptionIdWithArgument_ReturnsCorrectArray() {
        Options options = new Options();
        options.addOption("a", true, "A option");
        options.addOption("b", false, "B option");

        String token = "-avalue";

        PosixParser parser = new PosixParser();
        parser.burstToken(token, false);
      
        List<String> result = parser.getTokens();

        assertEquals(2, result.size());
        assertEquals("-a", result.get(0));
        assertEquals("value", result.get(1));
    }

    @Test
    public void testBurstToken_InvalidOptionId_StopAtNonOption_ReturnsCorrectArray() {
        Options options = new Options();

        String token = "-c";

        PosixParser parser = new PosixParser();
        parser.burstToken(token, true);
      
        List<String> result = parser.getTokens();

        assertEquals(2, result.size());
        assertEquals("--", result.get(0));
        assertEquals("c", result.get(1));
    }

    @Test
    public void testBurstToken_InvalidOptionId_DoesNotStopAtNonOption_ReturnsCorrectArray() {
        Options options = new Options();

        String token = "-c";

        PosixParser parser = new PosixParser();
        parser.burstToken(token, false);
      
        List<String> result = parser.getTokens();

        assertEquals(1, result.size());
        assertEquals("-c", result.get(0));
    }

    @Test
    public void testGobble_RemainingTokens_AddsRemainingTokensToList() {
        Options options = new Options();

        String[] arguments = {"value1", "value2"};

        PosixParser parser = new PosixParser();
        parser.gobble(Arrays.asList(arguments).iterator());
      
        List<String> result = parser.getTokens();

        assertEquals(2, result.size());
        assertEquals("value1", result.get(0));
        assertEquals("value2", result.get(1));
    }
}
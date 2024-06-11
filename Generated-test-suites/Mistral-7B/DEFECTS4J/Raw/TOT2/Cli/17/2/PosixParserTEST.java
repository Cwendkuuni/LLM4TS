import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class PosixParserTest {

    @Test
    public void testFlattenEmptyArgs() throws ParseException {
        PosixParser parser = new PosixParser();
        String[] args = {};
        Options options = new Options();
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[0], result);
        assertEquals(0, parser.tokens.size());
        parser.init();
    }

    @Test
    public void testFlattenBasicArgs() throws ParseException {
        PosixParser parser = new PosixParser();
        String[] args = {"arg1", "arg2", "arg3"};
        Options options = new Options();
        String[] result = parser.flatten(options, args, false);
        assertEquals(Arrays.asList("arg1", "arg2", "arg3").toArray(new String[0]), result);
        assertEquals(3, parser.tokens.size());
        parser.init();
    }

    @Test
    public void testFlattenSpecialArgs() throws ParseException {
        PosixParser parser = new PosixParser();
        String[] args = {"--key1=value1", "--key2=value2"};
        Options options = new Options();
        options.addOption("key1", true, "desc1");
        options.addOption("key2", true, "desc2");
        String[] result = parser.flatten(options, args, false);
        assertEquals(Arrays.asList("--key1=value1", "--key2=value2").toArray(new String[0]), result);
        assertEquals(4, parser.tokens.size());
        parser.init();
    }

    @Test
    public void testFlattenWithStopAtNonOption() throws ParseException {
        PosixParser parser = new PosixParser();
        String[] args = {"arg1", "--nonOption", "arg3"};
        Options options = new Options();
        String[] result = parser.flatten(options, args, true);
        assertEquals(Arrays.asList("arg1", "--nonOption", "arg3").toArray(new String[0]), result);
        assertEquals(4, parser.tokens.size());
        parser.init();
    }

    @Test(expected = ParseException.class)
    public void testFlattenInvalidOption() throws ParseException {
        PosixParser parser = new PosixParser();
        String[] args = {"--invalidOption"};
        Options options = new Options();
        parser.flatten(options, args, false);
        fail("Expected ParseException");
    }

    @Test
    public void testProcessSingleHyphen() {
        PosixParser parser = new PosixParser();
        String[] args = {"-"};
        Options options = new Options();
        String[] result = parser.flatten(options, args, false);
        assertEquals(Arrays.asList("-").toArray(new String[0]), result);
        assertEquals(1, parser.tokens.size());
        parser.init();
    }
}
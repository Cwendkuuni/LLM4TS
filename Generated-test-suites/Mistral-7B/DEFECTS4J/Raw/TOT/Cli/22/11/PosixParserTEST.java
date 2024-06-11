import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testSimpleOption() {
        String[] args = new String[]{"-a", "--optionA"};
        Options options = new Options();
        options.addOption("a", true, "An option");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args, true);
        assertTrue(Arrays.equals(new String[]{"--optionA"}, parsedArgs));
    }

    @Test
    public void testOptionWithValue() {
        String[] args = new String[]{"-a", "value"};
        Options options = new Options();
        options.addOption("a", true, "An option");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args, true);
        assertEquals("value", parsedArgs[0]);
    }

    @Test(expected = ParseException.class)
    public void testUnknownOption() {
        String[] args = new String[]{"-z"};
        Options options = new Options();
        PosixParser parser = new PosixParser();
        parser.parse(options, args, true);
        fail("Expected ParseException");
    }

    @Test
    public void testLongOption() {
        String[] args = new String[]{"--longOption"};
        Options options = new Options();
        options.addOption("longOption", true, "A long option");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args, true);
        assertTrue(Arrays.equals(new String[]{"--longOption"}, parsedArgs));
    }

    @Test
    public void testLongOptionWithValue() {
        String[] args = new String[]{"--longOptionValue", "value"};
        Options options = new Options();
        options.addOption("longOption", true, "A long option");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args, true);
        assertEquals("value", parsedArgs[0]);
    }

    @Test(expected = ParseException.class)
    public void testMissingOptionValue() {
        String[] args = new String[]{"--longOption"};
        Options options = new Options();
        options.addOption("longOption", true, "A long option");

        PosixParser parser = new PosixParser();
        parser.parse(options, args, true);
        fail("Expected ParseException");
    }

    @Test
    public void testOptionWithMultipleValues() {
        String[] args = new String[]{"-a", "value1", "value2"};
        Options options = new Options();
        options.addOption("a", true, "An option");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args, true);
        assertEquals(2, parsedArgs.length);
        assertEquals("value1", parsedArgs[0]);
        assertEquals("value2", parsedArgs[1]);
    }

    @Test
    public void testOptionWithNoValue() {
        String[] args = new String[]{"-a"};
        Options options = new Options();
        options.addOption("a", false, "An option");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args, true);
        assertTrue(Arrays.equals(new String[]{"-a"}, parsedArgs));
    }

    @Test
    public void testStopAtNonOption() {
        String[] args = new String[]{"-a", "--", "optionB", "value"};
        Options options = new Options();
        options.addOption("a", true, "An option");
        options.addOption("b", true, "Another option");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args, true);
        assertEquals(2, parsedArgs.length);
        assertEquals("--", parsedArgs[0]);
        assertEquals("optionB", parsedArgs[1]);
    }

    @Test
    public void testBurstedOption() {
        String[] args = new String[]{"-ab", "value"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args, false);
        assertEquals("--", parsedArgs[0]);
        assertEquals("OptionA", parsedArgs[1]);
        assertEquals("OptionB", parsedArgs[2]);
        assertEquals("value", parsedArgs[3]);
    }
}
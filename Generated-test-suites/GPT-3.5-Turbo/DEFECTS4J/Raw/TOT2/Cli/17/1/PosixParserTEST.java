import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.cli.ParseException;

public class PosixParserTest {

    @Test
    public void testFlatten() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String[] arguments = {"-a", "arg1", "--", "-b"};
        try {
            String[] flattenedArgs = parser.flatten(options, arguments, true);
            assertEquals(3, flattenedArgs.length);
            assertEquals("-a", flattenedArgs[0]);
            assertEquals("arg1", flattenedArgs[1]);
            assertEquals("--", flattenedArgs[2]);
        } catch (ParseException e) {
            fail("ParseException thrown");
        }
    }

    @Test
    public void testFlattenWithStopAtNonOptionFalse() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String[] arguments = {"-a", "arg1", "--", "-b"};
        try {
            String[] flattenedArgs = parser.flatten(options, arguments, false);
            assertEquals(4, flattenedArgs.length);
            assertEquals("-a", flattenedArgs[0]);
            assertEquals("arg1", flattenedArgs[1]);
            assertEquals("--", flattenedArgs[2]);
            assertEquals("-b", flattenedArgs[3]);
        } catch (ParseException e) {
            fail("ParseException thrown");
        }
    }

    @Test
    public void testFlattenWithInvalidOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String[] arguments = {"-c"};
        try {
            String[] flattenedArgs = parser.flatten(options, arguments, true);
            assertEquals(1, flattenedArgs.length);
            assertEquals("-c", flattenedArgs[0]);
        } catch (ParseException e) {
            fail("ParseException thrown");
        }
    }

    @Test
    public void testFlattenWithOptionValue() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", true, "Option a");
        String[] arguments = {"-a", "value"};
        try {
            String[] flattenedArgs = parser.flatten(options, arguments, true);
            assertEquals(2, flattenedArgs.length);
            assertEquals("-a", flattenedArgs[0]);
            assertEquals("value", flattenedArgs[1]);
        } catch (ParseException e) {
            fail("ParseException thrown");
        }
    }
}
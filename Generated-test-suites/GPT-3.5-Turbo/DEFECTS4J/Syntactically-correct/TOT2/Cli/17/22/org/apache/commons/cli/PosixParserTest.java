import static org.junit.Assert.*;
import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlatten_WithValidOptions_ReturnsFlattenedArgs() {
        Options options = new Options();
        options.addOption("a", "apple", false, "Include apple");
        options.addOption("b", "banana", true, "Include banana");

        PosixParser parser = new PosixParser();

        String[] arguments = {"-a", "value1", "--banana=value2"};
        String[] flattenedArgs = parser.flatten(options, arguments, false);

        String[] expected = {"-a", "value1", "--banana", "value2"};
        assertArrayEquals(expected, flattenedArgs);
    }

    @Test
    public void testFlatten_WithInvalidOption_ThrowsException() {
        Options options = new Options();
        options.addOption("c", "cherry", false, "Include cherry");

        PosixParser parser = new PosixParser();

        String[] arguments = {"-c", "value1", "-d", "value2"};

        try {
            parser.flatten(options, arguments, false);
            fail("Should have thrown an exception for invalid option");
        } catch (Exception e) {
            assertTrue(e instanceof ParseException);
        }
    }

    @Test
    public void testFlatten_WithStopAtNonOption_StopAtFirstNonOption() {
        Options options = new Options();
        options.addOption("e", "elephant", false, "Include elephant");

        PosixParser parser = new PosixParser();

        String[] arguments = {"-e", "value1", "nonOption1", "nonOption2"};
        String[] flattenedArgs = parser.flatten(options, arguments, true);

        String[] expected = {"-e", "value1", "nonOption1", "nonOption2"};
        assertArrayEquals(expected, flattenedArgs);
    }

    @Test
    public void testFlatten_WithSingleHyphen_AddsHyphenToArgs() {
        Options options = new Options();
        options.addOption("f", "fig", false, "Include fig");

        PosixParser parser = new PosixParser();

        String[] arguments = {"-f", "-", "--", "--value"};
        String[] flattenedArgs = parser.flatten(options, arguments, false);

        String[] expected = {"-f", "-", "--", "--value"};
        assertArrayEquals(expected, flattenedArgs);
    }
}
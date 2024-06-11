import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.*;

public class PosixParserTest {

    private Options options;
    private PosixParser parser;

    @Before
    public void setUp() {
        options = new Options();
        parser = new PosixParser();
    }

    @Test
    public void testFlatten_LongOption_NoArguments() {
        options.addOption("a", "alpha", false, "Enable alpha feature");

        String[] args = { "--alpha" };

        String[] flattenedArgs = parser.flatten(options, args, false);

        assertEquals(1, flattenedArgs.length);
        assertEquals("--alpha", flattenedArgs[0]);
    }

    @Test
    public void testFlatten_LongOption_WithArgument() {
        options.addOption(Option.builder("v").longOpt("version").hasArg().argName("VERSION")
            .desc("Specify the version").build());

        String[] args = { "--version", "1.0" };

        String[] flattenedArgs = parser.flatten(options, args, false);

        assertEquals(2, flattenedArgs.length);
        assertEquals("--version", flattenedArgs[0]);
        assertEquals("1.0", flattenedArgs[1]);
    }

    @Test
    public void testFlatten_LongOption_WithInvalidArgument() {
        options.addOption(Option.builder("v").longOpt("version").hasArg().argName("VERSION")
            .desc("Specify the version").build());

        String[] args = { "--version", "-1.0" };

        String[] flattenedArgs = parser.flatten(options, args, false);

        assertEquals(2, flattenedArgs.length);
        assertEquals("--version", flattenedArgs[0]);
        assertEquals("-1.0", flattenedArgs[1]);
    }

    @Test
    public void testFlatten_SingleHyphen() {
        String[] args = { "-" };

        String[] flattenedArgs = parser.flatten(options, args, false);

        assertEquals(1, flattenedArgs.length);
        assertEquals("-", flattenedArgs[0]);
    }

    @Test
    public void testFlatten_ShortOption_Valid() {
        options.addOption("f", true, "Enable feature");

        String[] args = { "-f" };

        String[] flattenedArgs = parser.flatten(options, args, false);

        assertEquals(1, flattenedArgs.length);
        assertEquals("-f", flattenedArgs[0]);
    }

    @Test
    public void testFlatten_ShortOption_Invalid() {
        options.addOption("f", true, "Enable feature");

        String[] args = { "-x" };

        String[] flattenedArgs = parser.flatten(options, args, false);

        assertEquals(1, flattenedArgs.length);
        assertEquals("-x", flattenedArgs[0]);
    }

    @Test
    public void testFlatten_ShortOption_WithArgument() {
        options.addOption(Option.builder("v").hasArg().argName("VERSION")
            .desc("Specify the version").build());

        String[] args = { "-v", "1.0" };

        String[] flattenedArgs = parser.flatten(options, args, false);

        assertEquals(2, flattenedArgs.length);
        assertEquals("-v", flattenedArgs[0]);
        assertEquals("1.0", flattenedArgs[1]);
    }

    @Test
    public void testFlatten_StopAtNonOption_True() {
        options.addOption("a", false, "Enable alpha feature");

        String[] args = { "--version", "1.0", "-a", "file.txt" };

        String[] flattenedArgs = parser.flatten(options, args, true);

        assertEquals(2, flattenedArgs.length);
        assertEquals("--version", flattenedArgs[0]);
        assertEquals("1.0", flattenedArgs[1]);
    }

    @Test
    public void testFlatten_StopAtNonOption_False() {
        options.addOption("a", false, "Enable alpha feature");

        String[] args = { "--version", "1.0", "-a", "file.txt" };

        String[] flattenedArgs = parser.flatten(options, args, false);

        assertEquals(4, flattenedArgs.length);
        assertEquals("--version", flattenedArgs[0]);
        assertEquals("1.0", flattenedArgs[1]);
        assertEquals("-a", flattenedArgs[2]);
        assertEquals("file.txt", flattenedArgs[3]);
    }

    @Test
    public void testFlatten_MultipleOptions() {
        options.addOption("a", false, "Enable alpha feature");
        options.addOption(Option.builder("v").longOpt("version").hasArg().argName("VERSION")
            .desc("Specify the version").build());

        String[] args = { "--alpha", "--version", "1.0" };

        String[] flattenedArgs = parser.flatten(options, args, false);

        assertEquals(3, flattenedArgs.length);
        assertEquals("--alpha", flattenedArgs[0]);
        assertEquals("--version", flattenedArgs[1]);
        assertEquals("1.0", flattenedArgs[2]);
    }
}
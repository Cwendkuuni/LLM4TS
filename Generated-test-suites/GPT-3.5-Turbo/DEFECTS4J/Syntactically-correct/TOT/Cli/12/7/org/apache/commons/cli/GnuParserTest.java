import org.junit.Test;
import static org.junit.Assert.*;

public class GnuParserTest {

    @Test
    public void testFlatten_FirstCharOptionExists() {
        Options options = new Options();
        options.addOption("a", false, "Option a");

        GnuParser parser = new GnuParser();
        String[] arguments = {"-a"};

        String[] flattenedArgs = parser.flatten(options, arguments, true);

        assertEquals(1, flattenedArgs.length);
        assertEquals("-a", flattenedArgs[0]);
    }

    @Test
    public void testFlatten_FirstCharOptionNotExists() {
        Options options = new Options();

        GnuParser parser = new GnuParser();
        String[] arguments = {"-b"};

        String[] flattenedArgs = parser.flatten(options, arguments, true);

        assertEquals(1, flattenedArgs.length);
        assertEquals("-b", flattenedArgs[0]);
    }

    @Test
    public void testFlatten_DoubleDash() {
        Options options = new Options();
        options.addOption("a", false, "Option a");

        GnuParser parser = new GnuParser();
        String[] arguments = {"--"};

        String[] flattenedArgs = parser.flatten(options, arguments, true);

        assertEquals(1, flattenedArgs.length);
        assertEquals("--", flattenedArgs[0]);
    }

    @Test
    public void testFlatten_SingleDash() {
        Options options = new Options();
        options.addOption("a", false, "Option a");

        GnuParser parser = new GnuParser();
        String[] arguments = {"-"};

        String[] flattenedArgs = parser.flatten(options, arguments, true);

        assertEquals(1, flattenedArgs.length);
        assertEquals("-", flattenedArgs[0]);
    }

    @Test
    public void testFlatten_StartsWithDash() {
        Options options = new Options();
        options.addOption("a", false, "Option a");

        GnuParser parser = new GnuParser();
        String[] arguments = {"-a=value"};

        String[] flattenedArgs = parser.flatten(options, arguments, true);

        assertEquals(2, flattenedArgs.length);
        assertEquals("-a", flattenedArgs[0]);
        assertEquals("value", flattenedArgs[1]);
    }

    @Test
    public void testFlatten_StartsWithDoubleDash() {
        Options options = new Options();
        options.addOption("a", false, "Option a");

        GnuParser parser = new GnuParser();
        String[] arguments = {"--a=value"};

        String[] flattenedArgs = parser.flatten(options, arguments, true);

        assertEquals(2, flattenedArgs.length);
        assertEquals("--a", flattenedArgs[0]);
        assertEquals("value", flattenedArgs[1]);
    }

    @Test
    public void testFlatten_StartsWithDashAndMatchesOptionPrefix() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", false, "Option b");

        GnuParser parser = new GnuParser();
        String[] arguments = {"-b=value"};

        String[] flattenedArgs = parser.flatten(options, arguments, true);

        assertEquals(2, flattenedArgs.length);
        assertEquals("-b", flattenedArgs[0]);
        assertEquals("value", flattenedArgs[1]);
    }

    @Test
    public void testFlatten_InvalidOption() {
        Options options = new Options();
        options.addOption("a", false, "Option a");

        GnuParser parser = new GnuParser();
        String[] arguments = {"-c"};

        String[] flattenedArgs = parser.flatten(options, arguments, true);

        assertEquals(1, flattenedArgs.length);
        assertEquals("-c", flattenedArgs[0]);
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        Options options = new Options();
        options.addOption("a", false, "Option a");

        GnuParser parser = new GnuParser();
        String[] arguments = {"-a", "-b"};

        String[] flattenedArgs = parser.flatten(options, arguments, true);

        assertEquals(1, flattenedArgs.length);
        assertEquals("-a", flattenedArgs[0]);
    }

    @Test
    public void testFlatten_InvalidOptionStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", false, "Option a");

        GnuParser parser = new GnuParser();
        String[] arguments = {"-c", "-b"};

        String[] flattenedArgs = parser.flatten(options, arguments, true);

        assertEquals(1, flattenedArgs.length);
        assertEquals("-c", flattenedArgs[0]);
    }

    @Test
    public void testFlatten_EatTheRest() {
        Options options = new Options();
        options.addOption("a", false, "Option a");

        GnuParser parser = new GnuParser();
        String[] arguments = {"-a", "value1", "value2"};

        String[] flattenedArgs = parser.flatten(options, arguments, true);

        assertEquals(3, flattenedArgs.length);
        assertEquals("-a", flattenedArgs[0]);
        assertEquals("value1", flattenedArgs[1]);
        assertEquals("value2", flattenedArgs[2]);
    }

    @Test
    public void testFlatten_NoEatTheRest() {
        Options options = new Options();
        options.addOption("a", false, "Option a");

        GnuParser parser = new GnuParser();
        String[] arguments = {"-a", "value1", "value2"};

        String[] flattenedArgs = parser.flatten(options, arguments, false);

        assertEquals(1, flattenedArgs.length);
        assertEquals("-a", flattenedArgs[0]);
    }
}
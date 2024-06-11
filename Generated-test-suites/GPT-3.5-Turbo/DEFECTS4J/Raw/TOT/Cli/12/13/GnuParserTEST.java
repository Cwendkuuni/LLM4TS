
import org.junit.Test;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import static org.junit.Assert.*;

public class GnuParserTest {

    private GnuParser gnuparser = new GnuParser();

    @Test
    public void testFlatten_ValidOptions_ReturnFlattenedArguments() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "Option 'a'");
        options.addOption("b", true, "Option 'b' with argument");

        String[] arguments = {"-a", "-b", "value"};

        String[] flattenedArgs = gnuparser.flatten(options, arguments, false);

        assertEquals(3, flattenedArgs.length);
        assertEquals("-a", flattenedArgs[0]);
        assertEquals("-b", flattenedArgs[1]);
        assertEquals("value", flattenedArgs[2]);
    }

    @Test
    public void testFlatten_StopAtNonOption_ReturnFlattenedArgumentsUntilNonOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "Option 'a'");

        String[] arguments = {"-a", "nonOption1", "nonOption2"};

        String[] flattenedArgs = gnuparser.flatten(options, arguments, true);

        assertEquals(3, flattenedArgs.length);
        assertEquals("-a", flattenedArgs[0]);
        assertEquals("nonOption1", flattenedArgs[1]);
        assertEquals("nonOption2", flattenedArgs[2]);
    }

    @Test
    public void testFlatten_HyphenOption_ReturnFlattenedArguments() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "Option 'a'");

        String[] arguments = {"-"};

        String[] flattenedArgs = gnuparser.flatten(options, arguments, false);

        assertEquals(1, flattenedArgs.length);
        assertEquals("-", flattenedArgs[0]);
    }

    @Test
    public void testFlatten_DoubleHyphenOption_ReturnFlattenedArguments() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "Option 'a'");

        String[] arguments = {"--"};

        String[] flattenedArgs = gnuparser.flatten(options, arguments, false);

        assertEquals(1, flattenedArgs.length);
        assertEquals("--", flattenedArgs[0]);
    }

    @Test
    public void testFlatten_UnrecognizedOption_ReturnFlattenedArguments() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "Option 'a'");

        String[] arguments = {"-b"};

        String[] flattenedArgs = gnuparser.flatten(options, arguments, false);

        assertEquals(1, flattenedArgs.length);
        assertEquals("-b", flattenedArgs[0]);
    }
}

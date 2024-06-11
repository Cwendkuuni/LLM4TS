import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.cli.*;

public class GnuParserTest {
  
    @Test
    public void testFlatten_withArguments_stopAtNonOption() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");

        String[] arguments = {"-a", "-b", "value", "arg1", "arg2"};
        String[] flattenedArgs = parser.flatten(options, arguments, true);

        assertEquals("Flattened arguments should contain all arguments up to stop point", 4, flattenedArgs.length);
        assertEquals("First argument should be the -a option", "-a", flattenedArgs[0]);
        assertEquals("Second argument should be the -b option value", "-b", flattenedArgs[1]);
        assertEquals("Third argument should be 'value'", "value", flattenedArgs[2]);
        assertEquals("Last argument should be 'arg1'", "arg1", flattenedArgs[3]);
    }

    @Test
    public void testFlatten_withArguments_noStopAtNonOption() {
        GnuParser parser = new GnuParser();
        Options options = new Options();

        String[] arguments = {"-a", "-b", "value", "arg1", "arg2"};
        String[] flattenedArgs = parser.flatten(options, arguments, false);

        assertEquals("Flattened arguments should contain all arguments", arguments.length, flattenedArgs.length);
        assertArrayEquals("Flattened arguments should match input arguments", arguments, flattenedArgs);
    }

    @Test
    public void testFlatten_withSpecialCase_optionsNoMatch() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        options.addOption("x", false, "Option X");
        options.addOption("y", true, "Option Y");

        String[] arguments = {"-z", "-Dproperty=value", "arg1", "arg2"};
        String[] flattenedArgs = parser.flatten(options, arguments, true);

        assertEquals("Flattened arguments should contain all arguments", arguments.length, flattenedArgs.length);
        assertArrayEquals("Flattened arguments should match input arguments", arguments, flattenedArgs);
    }

}
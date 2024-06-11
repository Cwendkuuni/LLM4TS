import static org.junit.Assert.*;
import org.junit.Test;

public class GnuParserTest {

   @Test
   public void testFlatten() {
      GnuParser parser = new GnuParser();
      Options options = new Options();
      
      // Create and add options to the options object
      Option opt1 = new Option("f", "file", true, "input file");
      Option opt2 = new Option("d", "directory", true, "input directory");
      Option opt3 = new Option("v", "verbose", false, "verbose output");
      options.addOption(opt1);
      options.addOption(opt2);
      options.addOption(opt3);
      
      // Create arguments array
      String[] arguments = {"-f", "input.txt", "-v", "--", "-d", "dir"};

      // Call the flatten method and get the result
      String[] result = parser.flatten(options, arguments, true);
      
      // Check if the result is correct
      String[] expected = {"-f", "input.txt", "-v", "--", "-d", "dir"};
      assertArrayEquals(expected, result);
   }

}
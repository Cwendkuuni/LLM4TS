import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

   @Test
   public void testFlatten_withOptionHasArg_expectedResult() {
      Options options = new Options();
      Option option = new Option("f", "file", true, "File name");
      options.addOption(option);

      String[] arguments = {"-f", "example.txt"};
      PosixParser parser = new PosixParser();

      String[] result = parser.flatten(options, arguments, true);

      assertEquals(result.length, 2);
      assertEquals(result[0], "-f");
      assertEquals(result[1], "example.txt");
   }

   @Test
   public void testFlatten_withOptionHasArgs_expectedResult() {
      Options options = new Options();
      Option option = new Option("f", "files", true, "File names");
      option.setArgs(3);
      options.addOption(option);

      String[] arguments = {"-f", "example1.txt", "example2.txt", "example3.txt"};
      PosixParser parser = new PosixParser();

      String[] result = parser.flatten(options, arguments, true);

      assertEquals(result.length, 4);
      assertEquals(result[0], "-f");
      assertEquals(result[1], "example1.txt");
      assertEquals(result[2], "example2.txt");
      assertEquals(result[3], "example3.txt");
   }

   @Test
   public void testFlatten_withOnlyOption_expectedResult() {
      Options options = new Options();
      Option option = new Option("v", "verbose", false, "Enable verbose output");
      options.addOption(option);

      String[] arguments = {"-v"};
      PosixParser parser = new PosixParser();

      String[] result = parser.flatten(options, arguments, true);

      assertEquals(result.length, 1);
      assertEquals(result[0], "-v");
   }

   @Test
   public void testFlatten_withNonOption_expectedResult() {
      Options options = new Options();

      String[] arguments = {"file.txt"};
      PosixParser parser = new PosixParser();

      String[] result = parser.flatten(options, arguments, true);

      assertEquals(result.length, 2);
      assertEquals(result[0], "--");
      assertEquals(result[1], "file.txt");
   }

   @Test
   public void testFlatten_withStopAtNonOption_false_expectedResult() {
      Options options = new Options();

      String[] arguments = {"-v", "--", "-f", "file.txt"};
      PosixParser parser = new PosixParser();

      String[] result = parser.flatten(options, arguments, false);

      assertEquals(result.length, 4);
      assertEquals(result[0], "-v");
      assertEquals(result[1], "--");
      assertEquals(result[2], "-f");
      assertEquals(result[3], "file.txt");
   }
}
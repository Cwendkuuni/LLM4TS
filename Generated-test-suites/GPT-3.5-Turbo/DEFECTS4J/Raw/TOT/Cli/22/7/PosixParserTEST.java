import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.cli.Options;

public class PosixParserTest {

   @Test
   public void testFlatten_NoOptions_StopAtNonOption() {
      Options options = new Options();
      String[] arguments = {"-a", "arg1", "arg2"};
      PosixParser parser = new PosixParser();
      String[] result = parser.flatten(options, arguments, true);
      String[] expected = {"-a", "arg1", "arg2"};
      assertArrayEquals(expected, result);
   }

   @Test
   public void testFlatten_ShortOption_NoValue() {
      Options options = new Options();
      options.addOption("a", false, "enable option a");
      String[] arguments = {"-a"};
      PosixParser parser = new PosixParser();
      String[] result = parser.flatten(options, arguments, false);
      String[] expected = {"-a"};
      assertArrayEquals(expected, result);
   }

   @Test
   public void testFlatten_ShortOption_WithValue() {
      Options options = new Options();
      options.addOption("a", true, "option a");
      String[] arguments = {"-a", "value"};
      PosixParser parser = new PosixParser();
      String[] result = parser.flatten(options, arguments, false);
      String[] expected = {"-a", "value"};
      assertArrayEquals(expected, result);
   }

   @Test
   public void testFlatten_ShortOption_Invalid() {
      Options options = new Options();
      options.addOption("a", false, "enable option a");
      String[] arguments = {"-b"};
      PosixParser parser = new PosixParser();
      String[] result = parser.flatten(options, arguments, false);
      String[] expected = {"-b"};
      assertArrayEquals(expected, result);
   }

   @Test
   public void testFlatten_LongOption_NoValue() {
      Options options = new Options();
      options.addOption("a", false, "enable option a");
      options.addOption("foo", false, "enable option foo");
      String[] arguments = {"--foo"};
      PosixParser parser = new PosixParser();
      String[] result = parser.flatten(options, arguments, false);
      String[] expected = {"--foo"};
      assertArrayEquals(expected, result);
   }

   @Test
   public void testFlatten_LongOption_WithValue() {
      Options options = new Options();
      options.addOption("a", true, "option a");
      options.addOption("foo", true, "option foo");
      String[] arguments = {"--foo", "value"};
      PosixParser parser = new PosixParser();
      String[] result = parser.flatten(options, arguments, false);
      String[] expected = {"--foo", "value"};
      assertArrayEquals(expected, result);
   }

   @Test
   public void testFlatten_LongOption_Invalid() {
      Options options = new Options();
      options.addOption("a", false, "enable option a");
      options.addOption("foo", false, "enable option foo");
      String[] arguments = {"--bar"};
      PosixParser parser = new PosixParser();
      String[] result = parser.flatten(options, arguments, false);
      String[] expected = {"--bar"};
      assertArrayEquals(expected, result);
   }

   @Test
   public void testFlatten_SpecialOption_StopAtNonOption() {
      Options options = new Options();
      options.addOption("a", false, "enable option a");
      String[] arguments = {"--", "-b"};
      PosixParser parser = new PosixParser();
      String[] result = parser.flatten(options, arguments, true);
      String[] expected = {"--", "-b"};
      assertArrayEquals(expected, result);
   }

   @Test
   public void testFlatten_SpecialOption_NoStopAtNonOption() {
      Options options = new Options();
      options.addOption("a", false, "enable option a");
      String[] arguments = {"--", "-b"};
      PosixParser parser = new PosixParser();
      String[] result = parser.flatten(options, arguments, false);
      String[] expected = {"--", "-b"};
      assertArrayEquals(expected, result);
   }

}
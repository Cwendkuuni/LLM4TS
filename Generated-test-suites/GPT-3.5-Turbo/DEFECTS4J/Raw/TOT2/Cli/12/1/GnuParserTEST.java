import org.junit.Test;
import static org.junit.Assert.*;

public class GnuParserTest {

  @Test
  public void testFlattenValidOptions() {
      // Test valid options
      Options options = new Options();
      options.addOption("a", "apple", false, "apple option");
      options.addOption("b", "banana", true, "banana option");
      options.addOption("c", false, "cat option");
      
      GnuParser parser = new GnuParser();
      String[] arguments = {"-a", "arg1", "--", "arg2", "-b", "arg3", "-c", "arg4"};
      String[] result = parser.flatten(options, arguments, false);
      
      assertEquals(result, new String[]{"-a", "arg1", "--", "arg2", "-b", "arg3", "-c", "arg4"});
  }

  @Test
  public void testFlattenShortOption() {
      // Test short option
      Options options = new Options();
      options.addOption("a", "apple", false, "apple option");
      
      GnuParser parser = new GnuParser();
      String[] arguments = {"-a"};
      String[] result = parser.flatten(options, arguments, false);
      
      assertEquals(result, new String[]{"-a"});
  }

  @Test
  public void testFlattenLongOption() {
      // Test long option
      Options options = new Options();
      options.addOption("b", "banana", false, "banana option");
      
      GnuParser parser = new GnuParser();
      String[] arguments = {"--banana"};
      String[] result = parser.flatten(options, arguments, false);
      
      assertEquals(result, new String[]{"--banana"});
  }

  @Test
  public void testFlattenMixedOptions() {
      // Test mixed options
      Options options = new Options();
      options.addOption("a", "apple", false, "apple option");
      options.addOption("b", "banana", false, "banana option");
      
      GnuParser parser = new GnuParser();
      String[] arguments = {"-a", "--banana"};
      String[] result = parser.flatten(options, arguments, false);
      
      assertEquals(result, new String[]{"-a", "--banana"});
  }

  @Test
  public void testFlattenNonOptionArguments() {
      // Test non option arguments
      Options options = new Options();
      options.addOption("a", "apple", false, "apple option");
      
      GnuParser parser = new GnuParser();
      String[] arguments = {"arg1", "arg2"};
      String[] result = parser.flatten(options, arguments, false);
      
      assertEquals(result, new String[]{"arg1", "arg2"});
  }
}
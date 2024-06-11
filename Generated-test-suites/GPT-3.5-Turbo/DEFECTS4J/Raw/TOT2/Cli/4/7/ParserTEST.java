import static org.junit.Assert.*;
import org.junit.Test;

public class ParserTest {

  @Test
  public void testParse_OptionsArguments_ParsedCorrectly() {
      // Arrange
      Parser parser = new Parser();
      Options options = new Options();
      options.addOption(Option.builder("a").build());
      String[] arguments = {"-a", "value"};

      // Act
      CommandLine cmd = parser.parse(options, arguments);

      // Assert
      assertTrue(cmd.hasOption("a"));
      assertEquals("value", cmd.getOptionValue("a"));
  }

  @Test
  public void testParse_StopAtNonOption_FlagSet_StopParsingCorrectly() {
      // Arrange
      Parser parser = new Parser();
      Options options = new Options();
      options.addOption(Option.builder("a").build());
      String[] arguments = {"-a", "value", "nonOption"};

      // Act
      CommandLine cmd = parser.parse(options, arguments, true);

      // Assert
      assertEquals(2, cmd.getArgs().length);
      assertFalse(cmd.hasOption("nonOption"));
  }

  @Test(expected = MissingOptionException.class)
  public void testParse_RequiredOptionNotPresent_ThrowMissingOptionException() throws ParseException {
      // Arrange
      Parser parser = new Parser();
      Options options = new Options();
      options.addOption(Option.builder("a").required().build());
      String[] arguments = {};

      // Act
      parser.parse(options, arguments);
  }
  
  // Add more test cases for other methods here

}
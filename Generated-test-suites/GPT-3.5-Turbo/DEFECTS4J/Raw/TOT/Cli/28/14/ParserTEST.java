import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

  @Test
  public void testParse_NoOptions_NoArguments() {
    // Arrange
    String[] arguments = {};

    // Act
    CommandLine result = Parser.parse(new Options(), arguments);

    // Assert
    assertEquals(0, result.getOptions().size());
  }

  @Test
  public void testParse_NoOptions_ValidArguments() {
    // Arrange
    String[] arguments = {"arg1", "arg2"};

    // Act
    CommandLine result = Parser.parse(new Options(), arguments);

    // Assert
    assertEquals(0, result.getOptions().size());
    assertEquals(2, result.getArgs().length);
    assertEquals("arg1", result.getArgs()[0]);
    assertEquals("arg2", result.getArgs()[1]);
  }

  @Test
  public void testParse_WithOptions_NoArguments() {
    // Arrange
    Options options = new Options();
    options.addOption("t", "test", false, "Test option");

    String[] arguments = {};

    // Act
    CommandLine result = Parser.parse(options, arguments);

    // Assert
    assertEquals(0, result.getArgs().length);
    assertTrue(result.hasOption("t"));
  }

  @Test
  public void testParse_WithOptions_ValidArguments() {
    // Arrange
    Options options = new Options();
    options.addOption("t", "test", false, "Test option");

    String[] arguments = {"arg1", "-t", "arg2"};

    // Act
    CommandLine result = Parser.parse(options, arguments);

    // Assert
    assertEquals(1, result.getArgs().length);
    assertTrue(result.hasOption("t"));
    assertEquals("arg1", result.getArgs()[0]);
  }

  @Test
  public void testParse_WithOptions_InvalidOption() {
    // Arrange
    Options options = new Options();
    options.addOption("t", "test", false, "Test option");

    String[] arguments = {"-x"};

    // Act and Assert
    assertThrows(UnrecognizedOptionException.class, () -> {
      Parser.parse(options, arguments);
    });
  }

}
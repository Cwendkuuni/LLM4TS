import static org.junit.Assert.*;
import org.junit.Test;

public class PosixParserTest {

  @Test
  public void testFlatten_StopAtNonOptionIsFalse_ReturnsExpectedResult() {
    // Arrange
    PosixParser parser = new PosixParser();
    Options options = new Options();
    options.addOption(Option.builder("a").build());
    options.addOption(Option.builder("b").build());
    String[] arguments = {"-a", "-b", "value", "--", "non_option"};
    String[] expected = {"-a", "-b", "value", "--", "non_option"};

    // Act
    String[] result = parser.flatten(options, arguments, false);

    // Assert
    assertArrayEquals(expected, result);
  }

  @Test
  public void testFlatten_StopAtNonOptionIsTrue_ReturnsExpectedResult() {
    // Arrange
    PosixParser parser = new PosixParser();
    Options options = new Options();
    options.addOption(Option.builder("a").build());
    options.addOption(Option.builder("b").build());
    String[] arguments = {"-a", "-b", "value", "--", "non_option"};
    String[] expected = {"-a", "-b"};

    // Act
    String[] result = parser.flatten(options, arguments, true);

    // Assert
    assertArrayEquals(expected, result);
  }

  @Test
  public void testFlatten_LongOptionWithoutValue_ReturnsExpectedResult() {
    // Arrange
    PosixParser parser = new PosixParser();
    Options options = new Options();
    options.addOption(Option.builder("a").build());
    options.addOption(Option.builder("b").build());
    options.addOption(Option.builder("c").longOpt("foo").build());
    String[] arguments = {"--foo", "-a", "-b"};
    String[] expected = {"--foo", "-a", "-b"};

    // Act
    String[] result = parser.flatten(options, arguments, false);

    // Assert
    assertArrayEquals(expected, result);
  }

  @Test
  public void testFlatten_LongOptionWithValue_ReturnsExpectedResult() {
    // Arrange
    PosixParser parser = new PosixParser();
    Options options = new Options();
    options.addOption(Option.builder("a").build());
    options.addOption(Option.builder("b").build());
    options.addOption(Option.builder("c").longOpt("foo").build());
    String[] arguments = {"--foo=value", "-a", "-b"};
    String[] expected = {"--foo", "value", "-a", "-b"};

    // Act
    String[] result = parser.flatten(options, arguments, false);

    // Assert
    assertArrayEquals(expected, result);
  }

  @Test
  public void testFlatten_SingleHyphen_ReturnsExpectedResult() {
    // Arrange
    PosixParser parser = new PosixParser();
    Options options = new Options();
    String[] arguments = {"-"};
    String[] expected = {"-"};

    // Act
    String[] result = parser.flatten(options, arguments, false);

    // Assert
    assertArrayEquals(expected, result);
  }

  @Test
  public void testFlatten_InvalidOption_ReturnsExpectedResult() {
    // Arrange
    PosixParser parser = new PosixParser();
    Options options = new Options();
    String[] arguments = {"-x"};
    String[] expected = {"-x"};

    // Act
    String[] result = parser.flatten(options, arguments, false);

    // Assert
    assertArrayEquals(expected, result);
  }

  @Test
  public void testFlatten_BurstToken_StopAtNonOptionIsFalse_ReturnsExpectedResult() {
    // Arrange
    PosixParser parser = new PosixParser();
    Options options = new Options();
    options.addOption(Option.builder("a").build());
    options.addOption(Option.builder("b").build());
    String[] arguments = {"-abc"};
    String[] expected = {"-a", "-b", "-c"};

    // Act
    String[] result = parser.flatten(options, arguments, false);

    // Assert
    assertArrayEquals(expected, result);
  }

  @Test
  public void testFlatten_BurstToken_StopAtNonOptionIsTrue_ReturnsExpectedResult() {
    // Arrange
    PosixParser parser = new PosixParser();
    Options options = new Options();
    options.addOption(Option.builder("a").build());
    options.addOption(Option.builder("b").build());
    String[] arguments = {"-abc"};
    String[] expected = {"-a", "-b"};

    // Act
    String[] result = parser.flatten(options, arguments, true);

    // Assert
    assertArrayEquals(expected, result);
  }

  @Test
  public void testFlatten_NonOptionTokenAdded_ReturnsExpectedResult() {
    // Arrange
    PosixParser parser = new PosixParser();
    Options options = new Options();
    options.addOption(Option.builder("a").build());
    options.addOption(Option.builder("b").build());
    String[] arguments = {"non_option"};
    String[] expected = {"--", "non_option"};

    // Act
    String[] result = parser.flatten(options, arguments, false);

    // Assert
    assertArrayEquals(expected, result);
  }

  @Test
  public void testFlatten_RemainingTokensAdded_ReturnsExpectedResult() {
    // Arrange
    PosixParser parser = new PosixParser();
    Options options = new Options();
    options.addOption(Option.builder("a").build());
    options.addOption(Option.builder("b").build());
    String[] arguments = {"non_option", "-a", "-b"};
    String[] expected = {"--", "non_option", "-a", "-b"};

    // Act
    String[] result = parser.flatten(options, arguments, true);

    // Assert
    assertArrayEquals(expected, result);
  }
}
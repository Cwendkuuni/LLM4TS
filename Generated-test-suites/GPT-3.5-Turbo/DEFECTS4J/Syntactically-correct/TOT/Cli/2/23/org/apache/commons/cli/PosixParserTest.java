import org.junit.Test;
import org.apache.commons.cli.*;

public class PosixParserTest {

  @Test
  public void testFlatten_NoOptions_NoArguments() {
    // Given
    Options options = new Options();
    String[] arguments = {};

    // When
    PosixParser parser = new PosixParser();
    String[] result = parser.flatten(options, arguments, false);

    // Then
    assertArrayEquals(result, arguments);
  }

  @Test
  public void testFlatten_SingleOption_NoArguments() {
    // Given
    Options options = new Options();
    options.addOption(Option.builder("a").build());
    String[] arguments = {"-a"};

    // When
    PosixParser parser = new PosixParser();
    String[] result = parser.flatten(options, arguments, false);

    // Then
    assertArrayEquals(result, arguments);
  }

  @Test
  public void testFlatten_MultipleOptions_NoArguments() {
    // Given
    Options options = new Options();
    options.addOption(Option.builder("a").build());
    options.addOption(Option.builder("b").build());
    String[] arguments = {"-a", "-b"};

    // When
    PosixParser parser = new PosixParser();
    String[] result = parser.flatten(options, arguments, false);

    // Then
    assertArrayEquals(result, arguments);
  }

  @Test
  public void testFlatten_StopAtNonOption() {
    // Given
    Options options = new Options();
    String[] arguments = {"-a", "arg1", "arg2"};

    // When
    PosixParser parser = new PosixParser();
    String[] result = parser.flatten(options, arguments, true);

    // Then
    assertArrayEquals(result, arguments);
  }

  @Test
  public void testFlatten_LongOptionWithoutArg() {
    // Given
    Options options = new Options();
    options.addOption(Option.builder().longOpt("long").build());
    String[] arguments = {"--long"};

    // When
    PosixParser parser = new PosixParser();
    String[] result = parser.flatten(options, arguments, false);

    // Then
    assertArrayEquals(result, arguments);
  }

  @Test
  public void testFlatten_LongOptionWithArg() {
    // Given
    Options options = new Options();
    options.addOption(Option.builder().longOpt("long").hasArg().build());
    String[] arguments = {"--long", "arg1"};

    // When
    PosixParser parser = new PosixParser();
    String[] result = parser.flatten(options, arguments, false);

    // Then
    assertArrayEquals(result, arguments);
  }

  @Test
  public void testFlatten_LongOptionWithEqualsSeparator() {
    // Given
    Options options = new Options();
    options.addOption(Option.builder().longOpt("long").hasArg().build());
    String[] arguments = {"--long=arg1"};

    // When
    PosixParser parser = new PosixParser();
    String[] result = parser.flatten(options, arguments, false);

    // Then
    assertArrayEquals(result, arguments);
  }

  @Test
  public void testFlatten_LongOptionWithEqualsSeparator_MultipleArgs() {
    // Given
    Options options = new Options();
    options.addOption(Option.builder().longOpt("long").hasArgs().build());
    String[] arguments = {"--long=arg1", "arg2", "arg3"};

    // When
    PosixParser parser = new PosixParser();
    String[] result = parser.flatten(options, arguments, false);

    // Then
    assertArrayEquals(result, arguments);
  }

  @Test
  public void testFlatten_HyphenToken() {
    // Given
    Options options = new Options();
    String[] arguments = {"-"};

    // When
    PosixParser parser = new PosixParser();
    String[] result = parser.flatten(options, arguments, false);

    // Then
    assertArrayEquals(result, arguments);
  }

  @Test
  public void testFlatten_HyphenOption_NoArguments() {
    // Given
    Options options = new Options();
    options.addOption(Option.builder("a").build());
    String[] arguments = {"-"};

    // When
    PosixParser parser = new PosixParser();
    String[] result = parser.flatten(options, arguments, false);

    // Then
    assertArrayEquals(result, arguments);
  }

  @Test
  public void testFlatten_HyphenOption_WithArguments() {
    // Given
    Options options = new Options();
    options.addOption(Option.builder("a").hasArgs().build());
    String[] arguments = {"-", "arg1", "arg2"};

    // When
    PosixParser parser = new PosixParser();
    String[] result = parser.flatten(options, arguments, false);

    // Then
    String[] expected = {"-", "arg1", "arg2"};
    assertArrayEquals(result, expected);
  }

}
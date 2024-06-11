import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class PosixParserTest {

  private PosixParser posixParser;
  private Options options;

  @Before
  public void setup() {
    posixParser = new PosixParser();
    options = new Options();
  }

  @Test
  public void testFlatten_StopAtNonOption() {
    options.addOption("h", false, "Display help information");
    options.addOption("f", true, "Specify file name");
  
    String[] arguments = {"-h", "-f", "test.txt", "arg1"};
    String[] expected = {"-h", "-f", "test.txt", "arg1"};
  
    String[] result = posixParser.flatten(options, arguments, true);
  
    assertArrayEquals("Flatten should stop at non-option", expected, result);
  }

  @Test
  public void testFlatten_MultipleSingleOptions() {
    options.addOption("a", false, "Option A");
    options.addOption("b", false, "Option B");
  
    String[] arguments = {"-a", "-b"};
    String[] expected = {"-a", "-b"};
  
    String[] result = posixParser.flatten(options, arguments, false);
  
    assertArrayEquals("Multiple single options should be flattened correctly", expected, result);
  }

  @Test
  public void testFlatten_LongOptionWithEqualSign() {
    options.addOption("file", true, "Option with equal sign");
  
    String[] arguments = {"--file=test.txt"};
    String[] expected = {"--file", "test.txt"};
  
    String[] result = posixParser.flatten(options, arguments, false);
  
    assertArrayEquals("Long option with equal sign should be flattened correctly", expected, result);
  }

  @Test
  public void testFlatten_LongOptionWithoutEqualSign() {
    options.addOption("file", true, "Option without equal sign");
  
    String[] arguments = {"--file", "test.txt"};
    String[] expected = {"--file", "test.txt"};
  
    String[] result = posixParser.flatten(options, arguments, false);
  
    assertArrayEquals("Long option without equal sign should be flattened correctly", expected, result);
  }

  @Test
  public void testFlatten_HyphenToken() {
    options.addOption("a", false, "Option A");
    options.addOption("b", false, "Option B");
  
    String[] arguments = {"-"};
    String[] expected = {"-"};
  
    String[] result = posixParser.flatten(options, arguments, false);
  
    assertArrayEquals("Hyphen token should be added as separate argument", expected, result);
  }

  @Test
  public void testFlatten_SingleCharacterOption() {
    options.addOption("f", true, "Option F");
  
    String[] arguments = {"-f", "test.txt"};
    String[] expected = {"-f", "test.txt"};
  
    String[] result = posixParser.flatten(options, arguments, false);
  
    assertArrayEquals("Single character option should be flattened correctly", expected, result);
  }

  @Test
  public void testFlatten_UnknownSingleCharacterOption() {
    options.addOption("a", false, "Option A");
    options.addOption("b", false, "Option B");
  
    String[] arguments = {"-c"};
    String[] expected = {"-c"};
  
    String[] result = posixParser.flatten(options, arguments, true);
  
    assertArrayEquals("Unknown single character option should be added as separate argument", expected, result);
  }

  @Test
  public void testFlatten_LongOptionWithUnknownId() {
    options.addOption("file", true, "Option with equal sign");
  
    String[] arguments = {"--unknown=test.txt"};
    String[] expected = {"--unknown", "test.txt"};
  
    String[] result = posixParser.flatten(options, arguments, true);
  
    assertArrayEquals("Long option with unknown id should be flattened correctly", expected, result);
  }

  @Test
  public void testFlatten_LongOptionWithInvalidValue() {
    options.addOption("file", true, "Option with equal sign");
  
    String[] arguments = {"--file"};
    String[] expected = {"--file"};
  
    String[] result = posixParser.flatten(options, arguments, false);
  
    assertArrayEquals("Long option without value should be added as separate argument", expected, result);
  }
  
  @Test
  public void testFlatten_WithRemainingTokens() {
    options.addOption("a", true, "Option A");
  
    String[] arguments = {"-a", "value1", "value2"};
    String[] expected = {"-a", "value1", "value2"};
  
    String[] result = posixParser.flatten(options, arguments, false);
  
    assertArrayEquals("Remaining tokens should be added to the processed tokens list", expected, result);
  }

}
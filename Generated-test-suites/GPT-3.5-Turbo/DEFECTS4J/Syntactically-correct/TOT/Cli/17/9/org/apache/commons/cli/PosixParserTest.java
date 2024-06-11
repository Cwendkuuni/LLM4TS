import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class PosixParserTest {
  
  private Options options;
  private PosixParser parser;

  @Before
  public void setUp() {
    options = new Options();
    parser = new PosixParser();
  }

  @Test(expected = ParseException.class)
  public void testFlatten_NullOptions() throws ParseException {
    String[] args = {"-a"};
    parser.flatten(null, args, false);
  }

  @Test
  public void testFlatten_EmptyOptions() throws ParseException {
    String[] args = {"-a"};
    String[] result = parser.flatten(options, args, false);
    assertNotNull(result);
    assertEquals(1, result.length);
    assertEquals("-a", result[0]);
  }

  @Test
  public void testFlatten_StopAtNonOption() throws ParseException {
    String[] args = {"--input", "file.txt", "arg1", "arg2"};
    options.addOption("i", "input", true, "input file");
    String[] result = parser.flatten(options, args, true);
    assertNotNull(result);
    assertEquals(3, result.length);
    assertEquals("--input", result[0]);
    assertEquals("file.txt", result[1]);
    assertEquals("arg1", result[2]);
  }

  @Test
  public void testFlatten_NoStopAtNonOption() throws ParseException {
    String[] args = {"--input", "file.txt", "arg1", "arg2"};
    options.addOption("i", "input", true, "input file");
    String[] result = parser.flatten(options, args, false);
    assertNotNull(result);
    assertEquals(4, result.length);
    assertEquals("--input", result[0]);
    assertEquals("file.txt", result[1]);
    assertEquals("arg1", result[2]);
    assertEquals("arg2", result[3]);
  }

  @Test
  public void testFlatten_LongOptionWithValue() throws ParseException {
    String[] args = {"--input=file.txt"};
    options.addOption("i", "input", true, "input file");
    String[] result = parser.flatten(options, args, false);
    assertNotNull(result);
    assertEquals(1, result.length);
    assertEquals("--input=file.txt", result[0]);
  }

  @Test
  public void testFlatten_SingleHyphen() throws ParseException {
    String[] args = {"-"};
    String[] result = parser.flatten(options, args, false);
    assertNotNull(result);
    assertEquals(1, result.length);
    assertEquals("-", result[0]);
  }

  @Test
  public void testFlatten_ShortOptionWithArgument() throws ParseException {
    String[] args = {"-i", "file.txt"};
    options.addOption("i", "input", true, "input file");
    String[] result = parser.flatten(options, args, false);
    assertNotNull(result);
    assertEquals(2, result.length);
    assertEquals("-i", result[0]);
    assertEquals("file.txt", result[1]);
  }

  @Test
  public void testFlatten_ShortOptionWithoutArgument() throws ParseException {
    String[] args = {"-a"};
    options.addOption("a", "action", false, "perform action");
    String[] result = parser.flatten(options, args, false);
    assertNotNull(result);
    assertEquals(1, result.length);
    assertEquals("-a", result[0]);
  }

  @Test
  public void testFlatten_ShortOptionWithMultipleArguments() throws ParseException {
    String[] args = {"-i", "file1.txt", "file2.txt"};
    options.addOption("i", "input", true, "input file");
    String[] result = parser.flatten(options, args, false);
    assertNotNull(result);
    assertEquals(3, result.length);
    assertEquals("-i", result[0]);
    assertEquals("file1.txt", result[1]);
    assertEquals("file2.txt", result[2]);
  }

  @Test
  public void testFlatten_InvalidShortOption_StopAtNonOption() throws ParseException {
    String[] args = {"-z", "file1.txt", "file2.txt"};
    options.addOption("i", "input", true, "input file");
    String[] result = parser.flatten(options, args, true);
    assertNotNull(result);
    assertEquals(3, result.length);
    assertEquals("--", result[0]);
    assertEquals("file1.txt", result[1]);
    assertEquals("file2.txt", result[2]);
  }

  @Test
  public void testFlatten_InvalidShortOption_NoStopAtNonOption() throws ParseException {
    String[] args = {"-z", "file1.txt", "file2.txt"};
    options.addOption("i", "input", true, "input file");
    String[] result = parser.flatten(options, args, false);
    assertNotNull(result);
    assertEquals(1, result.length);
    assertEquals("-z", result[0]);
  }

  @Test
  public void testFlatten_ValidShortOption_InvalidShortOption_StopAtNonOption() throws ParseException {
    String[] args = {"-a", "file1.txt", "-z", "file2.txt"};
    options.addOption("a", "action", false, "perform action");
    options.addOption("i", "input", true, "input file");
    String[] result = parser.flatten(options, args, true);
    assertNotNull(result);
    assertEquals(3, result.length);
    assertEquals("--", result[0]);
    assertEquals("file1.txt", result[1]);
    assertEquals("-z", result[2]);
  }

  @Test
  public void testFlatten_ValidOption_InvalidOptionWithValue() throws ParseException {
    String[] args = {"--input=file.txt", "--output"};
    options.addOption("i", "input", true, "input file");
    options.addOption("o", "output", true, "output file");
    String[] result = parser.flatten(options, args, false);
    assertNotNull(result);
    assertEquals(2, result.length);
    assertEquals("--input=file.txt", result[0]);
    assertEquals("--output", result[1]);
  }
}
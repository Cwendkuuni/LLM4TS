import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import java.util.Arrays;

public class PosixParserTest {
  
  private Options options;
  private PosixParser parser;

  @Before
  public void setUp() {
    options = new Options();
    parser = new PosixParser();
  }

  @Test
  public void testFlatten_StopAtNonOptionOptionDoesNotExist_AllArgumentsProcessed() {
    String[] arguments = {"-a", "arg1", "arg2"};
    options.addOption("b", false, "option b");

    String[] flattenedArguments = parser.flatten(options, arguments, true);

    assertArrayEquals(new String[] {"-a", "arg1", "arg2"}, flattenedArguments);
  }
  
  @Test
  public void testFlatten_StopAtNonOptionOptionExists_StopAtNonOption() {
    String[] arguments = {"-a", "arg1", "arg2"};
    options.addOption("a", true, "option a");

    String[] flattenedArguments = parser.flatten(options, arguments, true);

    assertArrayEquals(new String[] {"-a", "arg1", "arg2"}, flattenedArguments);
  }
  
  @Test
  public void testFlatten_StopAtNonOptionOptionExists_ContinueProcessing() {
    String[] arguments = {"-a", "arg1", "arg2"};
    options.addOption("a", true, "option a");

    String[] flattenedArguments = parser.flatten(options, arguments, false);

    assertArrayEquals(new String[] {"-a", "arg1", "arg2"}, flattenedArguments);
  }
  
  @Test
  public void testFlatten_StartWithDoubleDash_AddToProcessedTokens() {
    String[] arguments = {"--option1", "value1", "--option2"};
    options.addOption("option1", true, "option 1");
    options.addOption("option2", false, "option 2");

    String[] flattenedArguments = parser.flatten(options, arguments, false);

    assertArrayEquals(new String[] {"--option1", "value1", "--option2"}, flattenedArguments);
  }
  
  @Test
  public void testFlatten_StartWithSingleHyphen_AddToProcessedTokens() {
    String[] arguments = {"-"};
    options.addOption("option1", true, "option 1");

    String[] flattenedArguments = parser.flatten(options, arguments, false);

    assertArrayEquals(new String[] {"-"}, flattenedArguments);
  }
  
  @Test
  public void testFlatten_SingleCharacterOption_AddToProcessedTokens() {
    String[] arguments = {"-a"};
    options.addOption("a", false, "option a");

    String[] flattenedArguments = parser.flatten(options, arguments, false);

    assertArrayEquals(new String[] {"-a"}, flattenedArguments);
  }
  
  @Test
  public void testFlatten_ValidOption_AddToProcessedTokens() {
    String[] arguments = {"--option1"};
    options.addOption("option1", false, "option 1");

    String[] flattenedArguments = parser.flatten(options, arguments, false);

    assertArrayEquals(new String[] {"--option1"}, flattenedArguments);
  }
  
  @Test
  public void testFlatten_ValidOptionWithValue_AddOptionAndValueToProcessedTokens() {
    String[] arguments = {"--option1=value1"};
    options.addOption("option1", true, "option 1");

    String[] flattenedArguments = parser.flatten(options, arguments, false);

    assertArrayEquals(new String[] {"--option1", "value1"}, flattenedArguments);
  }
  
  @Test
  public void testFlatten_LongestOptionWithValue_AddToProcessedTokens() {
    String[] arguments = {"--option1=value1"};
    options.addOption("option", true, "option");
    options.addOption("option1", false, "option 1");

    String[] flattenedArguments = parser.flatten(options, arguments, false);

    assertArrayEquals(new String[] {"--option1", "value1"}, flattenedArguments);
  }
  
  @Test
  public void testFlatten_InvalidOptionWithStopAtNonOption_AddRemainingTokens() {
    String[] arguments = {"-abc", "arg1", "arg2"};
    options.addOption("b", false, "option b");

    String[] flattenedArguments = parser.flatten(options, arguments, true);

    assertArrayEquals(new String[] {"-abc", "arg1", "arg2"}, flattenedArguments);
  }
  
  @Test
  public void testFlatten_InvalidOptionWithoutStopAtNonOption_AddInvalidOption() {
    String[] arguments = {"-abc"};
    options.addOption("b", false, "option b");

    String[] flattenedArguments = parser.flatten(options, arguments, false);

    assertArrayEquals(new String[] {"-a", "-b", "-c"}, flattenedArguments);
  }
  
}
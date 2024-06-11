import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.junit.Assert;
import org.junit.Test;

public class PosixParserTest {
  @Test
  public void testFlattenNoOptions() {
    Options options = new Options();
    PosixParser parser = new PosixParser();
    String[] args = new String[] {};

    String[] result = parser.flatten(options, args, false);

    Assert.assertArrayEquals(args, result);
  }

  @Test
  public void testFlattenSingleOption() {
    Options options = new Options();
    options.addOption(new Option("a", "desc"));

    PosixParser parser = new PosixParser();
    String[] args = new String[] {"-a"};

    String[] result = parser.flatten(options, args, false);

    Assert.assertArrayEquals(args, result);
  }

  @Test
  public void testFlattenOptionWithValue() {
    Options options = new Options();
    Option option = new Option("a", true, "desc");
    options.addOption(option);

    PosixParser parser = new PosixParser();
    String[] args = new String[] {"-a", "value"};

    String[] result = parser.flatten(options, args, false);

    Assert.assertArrayEquals(args, result);
  }

  @Test
  public void testFlattenOptionWithMultipleValues() {
    Options options = new Options();
    Option option = new Option("a", true, "desc");
    option.setArgs(2);
    options.addOption(option);

    PosixParser parser = new PosixParser();
    String[] args = new String[] {"-a", "value1", "value2"};

    String[] result = parser.flatten(options, args, false);

    Assert.assertArrayEquals(args, result);
  }

  @Test
  public void testFlattenOptionWithMissingValue() {
    Options options = new Options();
    Option option = new Option("a", true, "desc");
    options.addOption(option);

    PosixParser parser = new PosixParser();
    String[] args = new String[] {"-a"};

    // The missing value should not be added to the result array
    String[] expectedResult = new String[] {"-a"};

    String[] result = parser.flatten(options, args, false);

    Assert.assertArrayEquals(expectedResult, result);
  }

  @Test
  public void testFlattenStopAtNonOption() {
    Options options = new Options();
    option.addOption(new Option("a", "desc"));

    PosixParser parser = new PosixParser();
    String[] args = new String[] {"-a", "value", "nonOption"};

    String[] result = parser.flatten(options, args, true);

    String[] expectedResult = new String[] {"-a", "value", "--", "nonOption"};

    Assert.assertArrayEquals(expectedResult, result);
  }

  @Test
  public void testBurstTokenOptionExists() {
    Options options = new Options();
    options.addOption(new Option("a", "desc"));

    PosixParser parser = new PosixParser();
    String token = "-a";

    String[] expectedResult = new String[] {"-a"};

    parser.burstToken(token, false);

    String[] result = (String[]) parser.tokens.toArray(new String[parser.tokens.size()]);

    Assert.assertArrayEquals(expectedResult, result);
  }

  @Test
  public void testBurstTokenOptionDoesNotExistStopAtNonOption() {
    Options options = new Options();
    PosixParser parser = new PosixParser();
    String token = "-a";

    // The token should be added as is to the result array
    String[] expectedResult = new String[] {"-a"};

    parser.burstToken(token, true);

    String[] result = (String[]) parser.tokens.toArray(new String[parser.tokens.size()]);

    Assert.assertArrayEquals(expectedResult, result);
  }

  @Test
  public void testBurstTokenOptionDoesNotExist() {
    Options options = new Options();
    PosixParser parser = new PosixParser();
    String token = "-a";

    // The token should be added as is to the result array
    String[] expectedResult = new String[] {"-a"};

    parser.burstToken(token, false);

    String[] result = (String[]) parser.tokens.toArray(new String[parser.tokens.size()]);

    Assert.assertArrayEquals(expectedResult, result);
  }

  @Test
  public void testProcessOptionTokenOptionExists() {
    Options options = new Options();
    options.addOption(new Option("a", "desc"));

    PosixParser parser = new PosixParser();
    String token = "-a";

    String[] expectedResult = new String[] {"-a"};

    parser.processOptionToken(token, false);

    String[] result = (String[]) parser.tokens.toArray(new String[parser.tokens.size()]);

    Assert.assertArrayEquals(expectedResult, result);
  }

  @Test
  public void testProcessOptionTokenOptionDoesNotExistStopAtNonOption() {
    Options options = new Options();
    PosixParser parser = new PosixParser();
    String token = "-a";

    // The token should be added as is to the result array
    String[] expectedResult = new String[] {"-a"};
    parser.eatTheRest = true;

    parser.processOptionToken(token, true);

    String[] result = (String[]) parser.tokens.toArray(new String[parser.tokens.size()]);

    Assert.assertArrayEquals(expectedResult, result);
  }

  @Test
  public void testProcessOptionTokenOptionDoesNotExist() {
    Options options = new Options();
    PosixParser parser = new PosixParser();
    String token = "-a";

    // The token should be added as is to the result array
    String[] expectedResult = new String[] {"-a"};

    parser.processOptionToken(token, false);

    String[] result = (String[]) parser.tokens.toArray(new String[parser.tokens.size()]);

    Assert.assertArrayEquals(expectedResult, result);
  }
}
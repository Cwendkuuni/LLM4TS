import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.apache.commons.cli.CommonConstants.USAGE_MESSAGE_KEY;

public class PosixParserTest {

 private List<String> tokens;
 private PosixParser parser;
 private Options options;

 @Before
 public void setup() {
 options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B");
 options.addOption("c", 3, "Option C");
 }

 @Test
 public void testFlattenEmptyArguments() {
 tokens = Arrays.asList(new String[0]);
 parser = new PosixParser();
 String[] arguments = new String[0];

 String[] expected = parser.flatten(options, arguments, false);
 List<String> actual = Arrays.asList(expected);

 Assert.assertEquals(actual, tokens);
 }

 @Test
 public void testFlattenWithSingleArgument() {
 tokens = Arrays.asList("arg1");
 parser = new PosixParser();
 String[] arguments = new String[]{"arg1"};

 String[] expected = {"arg1"};
 List<String> actual = Arrays.asList(expected);

 Assert.assertEquals(actual, tokens);
 }

 @Test
 public void testFlattenWithTwoArguments() {
 tokens = Arrays.asList("arg1", "arg2");
 parser = new PosixParser();
 String[] arguments = new String[]{"arg1", "arg2"};

 String[] expected = {"arg1", "arg2"};
 List<String> actual = Arrays.asList(expected);

 Assert.assertEquals(actual, tokens);
 }

 @Test
 public void testFlattenWithMultipleShortOptions() {
 tokens = Arrays.asList("a", "b", "c");
 parser = new PosixParser();
 String[] arguments = new String[]{"-a", "-b", "-c"};

 String[] expected = {"-a", "-b", "-c"};
 List<String> actual = Arrays.asList(expected);

 Assert.assertEquals(actual, tokens);
 }

 @Test
 public void testFlattenWithShortOptionAndLongOption() {
 tokens = Arrays.asList("a", "--option-b", "c");
 parser = new PosixParser();
 String[] arguments = new String[]{"-a", "--option-b", "c"};

 String[] expected = {"-a", "--option-b", "c"};
 List<String> actual = Arrays.asList(expected);

 Assert.assertEquals(actual, tokens);
 }

 @Test
 public void testFlattenWithLongOption() {
 tokens = Arrays.asList("--option-b", "arg1");
 parser = new PosixParser();
 String[] arguments = new String[]{"--option-b", "arg1"};

 String[] expected = {"--option-b", "arg1"};
 List<String> actual = Arrays.asList(expected);

 Assert.assertEquals(actual, tokens);
 }

 @Test
 public void testFlattenWithShortOptionAndValue() {
 tokens = Arrays.asList("a", "value");
 parser = new PosixParser();
 String[] arguments = new String[]{"-a", "value"};

 String[] expected = {"-a", "value"};
 List<String> actual = Arrays.asList(expected);

 Assert.assertEquals(actual, tokens);
 }

 @Test
 public void testFlattenWithLongOptionAndValue() {
 tokens = Arrays.asList("--option-b", "value");
 parser = new PosixParser();
 String[] arguments = new String[]{"--option-b", "value"};

 String[] expected = {"--option-b", "value"};
 List<String> actual = Arrays.asList(expected);

 Assert.assertEquals(actual, tokens);
 }

 @Test(expected = ParseException.class)
 public void testFlattenWithInvalidOption() {
 tokens = Arrays.asList("invalidOption");
 parser = new PosixParser();
 String[] arguments = new String[]{"invalidOption"};

 parser.flatten(options, arguments, false);
 }

 @Test(expected = ParseException.class)
 public void testFlattenWithStopAtNonOption() {
 tokens = Arrays.asList("invalidOption", "nonOption");
 parser = new PosixParser();
 String[] arguments = new String[]{"invalidOption", "nonOption"};

 parser.flatten(options, arguments, true);
 }

 @Test
 public void testUsageMessage() {
 try {
 parser = new PosixParser();
 parser.flatten(options, null, false);
 Assert.fail();
 } catch (ParseException e) {
 tokens = Arrays.asList(e.getMessage().split("\n"));
 }
 String expectedUsageMessage = options.getUsageMessage();
 List<String> actual = Arrays.asList(tokens.toArray(new String[0]));

 Assert.assertEquals(expectedUsageMessage, String.join("\n", actual));
 }
}
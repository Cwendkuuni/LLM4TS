import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.List;

@RunWith(JUnit4.class)
public class PosixParserTest {

 private List<String> tokens;
 private String[] args;
 private PosixParser parser;
 private Options options;

 @Before
 public void setUp() {
 options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B");
 options.addOption("c", 2, "Option C");
 }

 @Test
 public void testFlattenEmptyArguments() {
 tokens = Arrays.asList(new String[0]);
 args = parser.flatten(options, tokens.toArray(new String[0]), false);
 Assert.assertArrayEquals(new String[0], args);
 }

 @Test
 public void testFlattenSimpleArguments() {
 tokens = Arrays.asList("--foo", "--bar=baz");
 args = parser.flatten(options, tokens.toArray(new String[0]), false);
 Assert.assertArrayEquals(new String[]{"--foo", "--bar=baz"}, args);
 }

 @Test
 public void testFlattenSingleOption() {
 tokens = Arrays.asList("-a");
 args = parser.flatten(options, tokens.toArray(new String[0]), false);
 Assert.assertArrayEquals(new String[]{"-a"}, args);
 }

 @Test
 public void testFlattenMultiOption() {
 tokens = Arrays.asList("-a", "-b", "--c=d", "e");
 args = parser.flatten(options, tokens.toArray(new String[0]), false);
 Assert.assertArrayEquals(new String[]{"-a", "-b", "--c=d", "e"}, args);
 }

 @Test
 public void testFlattenLongOptionWithValue() {
 tokens = Arrays.asList("--longOption=value");
 args = parser.flatten(options, tokens.toArray(new String[0]), false);
 Assert.assertArrayEquals(new String[]{"--longOption=value"}, args);
 }

 @Test(expected = ParseException.class)
 public void testFlattenInvalidOption() {
 tokens = Arrays.asList("--invalidOption");
 parser = new PosixParser();
 args = parser.flatten(options, tokens.toArray(new String[0]), false);
 }

 @Test(expected = ParseException.class)
 public void testFlattenInvalidOptionWithValue() {
 tokens = Arrays.asList("--invalidOption=value");
 parser = new PosixParser();
 args = parser.flatten(options, tokens.toArray(new String[0]), false);
 }

 @Test
 public void testFlattenStopAtNonOption() {
 tokens = Arrays.asList("--foo", "bar", "-a", "-b", "--c=d");
 args = parser.flatten(options, tokens.toArray(new String[0]), true);
 Assert.assertArrayEquals(new String[]{"--foo", "-a", "-b"}, args);
 }

 @Test
 public void testFlattenStopAtNonOptionWithLongOption() {
 tokens = Arrays.asList("--foo", "bar", "--longOption=value", "-a", "-b");
 args = parser.flatten(options, tokens.toArray(new String[0]), true);
 Assert.assertArrayEquals(new String[]{"--foo", "--longOption=value"}, args);
 }
}
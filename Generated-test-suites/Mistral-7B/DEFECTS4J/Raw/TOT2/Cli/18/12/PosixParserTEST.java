import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

 private PosixParser parser;
 private Options options;

 @Before
 public void setUp() {
 options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B with an integer value");
 options.addOption("c", 1, "Option C with a string value");
 options.addOption("d", 2, "Option D with two values");
 }

 @Test
 public void testSimpleOption() {
 parser = new PosixParser();
 String[] args = {"--a"};
 String[] expectedArgs = {"--a"};
 Assert.assertArrayEquals(expectedArgs, parser.flatten(options, args, false));
 }

 @Test
 public void testOptionWithValue() {
 parser = new PosixParser();
 String[] args = {"--a", "value"};
 String[] expectedArgs = {"--a", "value"};
 Assert.assertArrayEquals(expectedArgs, parser.flatten(options, args, false));
 }

 @Test
 public void testShortOption() {
 parser = new PosixParser();
 String[] args = {"-a"};
 String[] expectedArgs = {"-a"};
 Assert.assertArrayEquals(expectedArgs, parser.flatten(options, args, false));
 }

 @Test
 public void testShortOptionWithValue() {
 parser = new PosixParser();
 String[] args = {"-a", "value"};
 String[] expectedArgs = {"-a", "value"};
 Assert.assertArrayEquals(expectedArgs, parser.flatten(options, args, false));
 }

 @Test(expected = ParseException.class)
 public void testInvalidOption() {
 parser = new PosixParser();
 String[] args = {"--invalid"};
 String[] expectedArgs = {};
 try {
 String[] actualArgs = parser.flatten(options, args, false);
 Assert.fail();
 } catch (ParseException e) {
 Assert.assertArrayEquals(expectedArgs, parser.getTokens().toArray(new String[0]));
 }
 }

 @Test(expected = ParseException.class)
 public void testOptionWithTwoValues() {
 parser = new PosixParser();
 String[] args = {"--d", "value1", "value2"};
 String[] expectedArgs = {"--d", "value1", "value2"};
 Assert.assertArrayEquals(expectedArgs, parser.flatten(options, args, false));
 }

 @Test(expected = ParseException.class)
 public void testInvalidOptionWithValue() {
 parser = new PosixParser();
 String[] args = {"--invalid", "value"};
 String[] expectedArgs = {};
 try {
 String[] actualArgs = parser.flatten(options, args, false);
 Assert.fail();
 } catch (ParseException e) {
 Assert.assertArrayEquals(expectedArgs, parser.getTokens().toArray(new String[0]));
 }
 }

 @Test
 public void testLongOptionWithValue() {
 parser = new PosixParser();
 String[] args = {"--option-c", "value"};
 String[] expectedArgs = {"--option-c", "value"};
 Assert.assertArrayEquals(expectedArgs, parser.flatten(options, args, false));
 }

 @Test(expected = ParseException.class)
 public void testInvalidLongOption() {
 parser = new PosixParser();
 String[] args = {"--invalid-option", "value"};
 String[] expectedArgs = {};
 try {
 String[] actualArgs = parser.flatten(options, args, false);
 Assert.fail();
 } catch (ParseException e) {
 Assert.assertArrayEquals(expectedArgs, parser.getTokens().toArray(new String[0]));
 }
 }

 @Test(expected = ParseException.class)
 public void testInvalidOptionWithTwoValues() {
 parser = new PosixParser();
 String[] args = {"--invalid-option", "value1", "value2"};
 String[] expectedArgs = {};
 try {
 String[] actualArgs = parser.flatten(options, args, false);
 Assert.fail();
 } catch (ParseException e) {
 Assert.assertArrayEquals(expectedArgs, parser.getTokens().toArray(new String[0]));
 }
 }

 @Test(expected = ParseException.class)
 public void testInvalidOptionWithNoValue() {
 parser = new PosixParser();
 String[] args = {"--invalid-option"};
 String[] expectedArgs = {};
 try {
 String[] actualArgs = parser.flatten(options, args, false);
 Assert.fail();
 } catch (ParseException e) {
 Assert.assertArrayEquals(expectedArgs, parser.getTokens().toArray(new String[0]));
 }
 }

 @Test
 public void testStopAtNonOption() {
 parser = new PosixParser();
 String[] args = {"--a", "option", "--b"};
 String[] expectedArgs = {"--a", "option"};
 Assert.assertArrayEquals(expectedArgs, parser.flatten(options, args, true));
 }

 @Test
 public void testLongOptionWithArgument() { ----cut--->
 parser = new PosixParser();
 String[] args = {"--option-c", "value"};
 String[] expectedArgs = {"--option-c", "value"};
 Assert.assertArrayEquals(expectedArgs, parser.flatten(options, args, false));
 }

 @Test
 public void testLongOptionWithTwoArguments() {
 parser = new PosixParser();
 String[] args = {"--option-d", "value1", "value2"};
 String[] expectedArgs = {"--option-d", "value1", "value2"};
 Assert.assertArrayEquals(expectedArgs, parser.flatten(options, args, false));
 }

 @Test
 public void testBursting() {
 parser = new PosixParser();
 String[] args = {"--option-e", "value1", "value2"};
 String[] expectedArgs = {"--option-e", "value1", "value2"};
 Assert.assertArrayEquals(expectedArgs, parser.flatten(options, args, false));
 }

 @Test(expected = ParseException.class)
 public void testInvalidBursting() {
 parser = new PosixParser();
 String[] args = {"--option-e", "value1", "value2"};
 String[] expectedArgs = {};
 try {
 String[] actualArgs = parser.flatten(options, args, false);
 Assert.fail();
 } catch (ParseException e) {
 Assert.assertArrayEquals(expectedArgs, parser.getTokens().toArray(new String[0]));
 }
 }

 @Test
 public void testBurstingWithInvalidOption() {
 parser = new PosixParser();
 String[] args = {"--option-e", "invalid", "value2"};
 String[] expectedArgs = {};
 try {
 String[] actualArgs = parser.flatten(options, args, false);
 Assert.fail();
 } catch (ParseException e) {
 Assert.assertArrayEquals(expectedArgs, parser.getTokens().toArray(new String[0]));
 }
 }

 @Test
 public void testBurstingWithStopAtNonOption() {
 parser = new PosixParser();
 String[] args = {"--option-e", "value1", "--b"};
 String[] expectedArgs = {"--option-e", "value1"};
 Assert.assertArrayEquals(expectedArgs, parser.flatten(options, args, true));
 }
}
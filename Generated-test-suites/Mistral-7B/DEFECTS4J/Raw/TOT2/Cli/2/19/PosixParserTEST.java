import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class PosixParserTest {

 private Options options;
 private PosixParser parser;

 @Before
 public void setUp() {
 options = new Options();
 options.addRequiredOption("i", "input", true, "input file path");
 options.addOption("o", "output", true, "output file path");
 }

 @Test
 public void testFlatten_NoArgs() {
 String[] args = {};
 String[] expected = {};
 try {
 String[] result = parser.flatten(options, args, false);
 assertEquals(expected, result);
 } catch (ParseException e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testFlatten_SingleArg() {
 String[] args = {"input.txt"};
 String[] expected = {"input", "input.txt"};
 try {
 String[] result = parser.flatten(options, args, false);
 assertEquals(expected, result);
 } catch (ParseException e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testFlatten_SingleOption() {
 String[] args = {"--input", "input.txt"};
 String[] expected = {"input", "input.txt"};
 try {
 String[] result = parser.flatten(options, args, false);
 assertEquals(expected, result);
 } catch (ParseException e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testFlatten_MultipleOptions() {
 String[] args = {"--input", "input.txt", "--output", "output.txt"};
 String[] expected = {"input", "input.txt", "output", "output.txt"};
 try {
 String[] result = parser.flatten(options, args, false);
 assertEquals(expected, result);
 } catch (ParseException e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testFlatten_StopAtNonOption() {
 String[] args = {"--input", "input.txt", "--", "output.txt"};
 String[] expected = {"input", "input.txt", "--", "output.txt"};
 try {
 String[] result = parser.flatten(options, args, true);
 assertEquals(expected, result);
 } catch (ParseException e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testFlatten_LongOption() {
 String[] args = {"--long-input-option", "long-input-value"};
 String[] expected = {"long-input-option", "long-input-value"};
 try {
 String[] result = parser.flatten(options, args, false);
 assertEquals(expected, result);
 } catch (ParseException e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testFlatten_LongOptionWithValue() {
 String[] args = {"--long-input-option=long-input-value"};
 String[] expected = {"long-input-option", "long-input-value"};
 try {
 String[] result = parser.flatten(options, args, false);
 assertEquals(expected, result);
 } catch (ParseException e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testFlatten_InvalidOption() {
 String[] args = {"--invalid"};
 String[] expected = {"--invalid"};
 try {
 String[] result = parser.flatten(options, args, false);
 assertEquals(expected, result);
 } catch (ParseException e) {
 // expected
 }
 }

 @Test
 public void testFlatten_InvalidOptionWithValue() {
 String[] args = {"--invalid=invalid-value"};
 String[] expected = {"--invalid=invalid-value"};
 try {
 String[] result = parser.flatten(options, args, false);
 assertEquals(expected, result);
 } catch (ParseException e) {
 // expected
 }
 }

 @Test(expected = ParseException.class)
 public void testFlatten_InvalidLongOption() {
 String[] args = {"--invalid-long-option"};
 String[] expected = {};
 try {
 String[] result = parser.flatten(options, args, false);
 assertEquals(expected, result);
 } catch (ParseException e) {
 // expected
 }
 }

 @Test(expected = ParseException.class)
 public void testFlatten_InvalidLongOptionWithValue() {
 String[] args = {"--invalid-long-option=invalid-value"};
 String[] expected = {};
 try {
 String[] result = parser.flatten(options, args, false);
 assertEquals(expected, result);
 } catch (ParseException e) {
 // expected
 }
 }

 @Test
 public void testInit() {
 parser = new PosixParser();
 assertNull(parser.getCurrentOption());
 assertFalse(parser.isEatTheRest());
 assertEquals(0, parser.getTokens().size());
 }

 @Test
 public void testProcessSingleHyphen() {
 String[] args = {"-"};
 String[] expected = {"-"};
 try {
 String[] result = parser.flatten(options, args, false);
 assertEquals(expected, result);
 } catch (ParseException e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testProcessOptionToken() {
 String[] args = {"--input"};
 String[] expected = {"input"};
 try {
 String[] result = parser.flatten(options, args, false);
 assertEquals(expected, result);
 } catch (ParseException e) {
 throw new RuntimeException(e);
 }
 currentOption = parser.getCurrentOption();
 assertEquals("input", currentOption.getLongOpt());
 assertEquals("i", currentOption.getOpt());
 assertSame(options, currentOption.getOptions());
 }

 @Test
 public void testProcess() {
 String[] args = {"input.txt"};
 String[] expected = {"input", "input.txt"};
 try {
 String[] result = parser.flatten(options, args, false);
 assertEquals(expected, result);
 } catch (ParseException e) {
 throw new RuntimeException(e);
 }
 currentOption = parser.getCurrentOption();
 assertNull(currentOption);
 }

 @Test
 public void testProcess_OptionWithArgument() {
 String[] args = {"--input", "input.txt"};
 String[] expected = {"input", "input.txt"};
 try {
 String[] result = parser.flatten(options, args, false);
 assertEquals(expected, result);
 } catch (ParseException e) {
 throw new RuntimeException(e);
 }
 currentOption = parser.getCurrentOption();
 assertEquals("input", currentOption.getLongOpt());
 assertEquals("i", currentOption.getOpt());
 assertEquals("input.txt", currentOption.getValue());
 assertSame(options, currentOption.getOptions());
 }

 @Test
 public void testProcess_OptionWithMultipleArguments() {
 String[] args = {"--input", "input1.txt", "input2.txt"};
 String[] expected = {"input", "input1.txt", "input2.txt"};
 try {
 String[] result = parser.flatten(options, args, false);
 assertEquals(expected, result);
 } catch (ParseException e) {
 throw new RuntimeException(e);
 }
 currentOption = parser.getCurrentOption();
 assertEquals("input", currentOption.getLongOpt());
 assertEquals("i", currentOption.getOpt());
 assertEquals(new String[]{"input1.txt", "input2.txt"}, currentOption.getArgs());
 assertSame(options, currentOption.getOptions());
 }
}
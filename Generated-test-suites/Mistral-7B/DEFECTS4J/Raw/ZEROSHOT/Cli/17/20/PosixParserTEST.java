import org.apache.commons.cli.Options;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

public class PosixParserTest {

 @Test
 public void testFlattenEmpty() {
 PosixParser parser = new PosixParser();
 String[] args = {};
 Options options = new Options();
 String[] result = parser.flatten(options, args, false);
 Assert.assertArrayEquals(new String[0], result);
 }

 @Test
 public void testFlattenSimple() {
 PosixParser parser = new PosixParser();
 String[] args = {"--foo", "arg1", "--bar=arg2", "arg3"};
 Options options = new Options();
 options.addOption("foo", true, "f");
 options.addOption("bar", true, "b");
 options.addOption("baz", false, "z");
 String[] result = parser.flatten(options, args, false);
 Assert.assertArrayEquals(new String[]{"--foo", "arg1", "--bar=arg2", "arg3"}, result);
 }

 @Test
 public void testFlattenWithStopAtNonOption() {
 PosixParser parser = new PosixParser();
 String[] args = {"--foo", "arg1", "--bar=arg2", "arg3", "-baz"};
 Options options = new Options();
 options.addOption("foo", true, "f");
 options.addOption("bar", true, "b");
 options.addOption("baz", false, "z");
 String[] result = parser.flatten(options, args, true);
 Assert.assertArrayEquals(new String[]{"--foo", "arg1", "--bar=arg2", "arg3", "-baz"}, result);
 }

 @Test
 public void testFlattenWithStopAtNonOptionAfterOption() {
 PosixParser parser = new PosixParser();
 String[] args = {"--foo", "arg1", "--bar=arg2", "-baz"};
 Options options = new Options();
 options.addOption("foo", true, "f");
 options.addOption("bar", true, "b");
 options.addOption("baz", false, "z");
 String[] result = parser.flatten(options, args, true);
 Assert.assertArrayEquals(new String[]{"--foo", "arg1", "--bar=arg2"}, result);
 }

 @Test
 public void testFlattenWithLongOption() {
 PosixParser parser = new PosixParser();
 String[] args = {"--long-option-with-arg=value"};
 Options options = new Options();
 options.addOption("longOptionWithArg", true, "l");
 String[] result = parser.flatten(options, args, false);
 Assert.assertArrayEquals(new String[]{"--long-option-with-arg=value"}, result);
 }

 @Test
 public void testFlattenWithLongOptionAndValue() {
 PosixParser parser = new PosixParser();
 String[] args = {"--long-option-with-arg=value1", "--long-option-with-arg=value2"};
 Options options = new Options();
 options.addOption("longOptionWithArg", true, "l");
 String[] result = parser.flatten(options, args, false);
 Assert.assertArrayEquals(new String[]{"--long-option-with-arg=value1", "--long-option-with-arg=value2"}, result);
 }

 @Test
 public void testFlattenWithLongOptionAndValueAndStopAtNonOption() {
 PosixParser parser = new PosixParser();
 String[] args = {"--long-option-with-arg=value1", "--long-option-with-arg=value2", "nonOption"};
 Options options = new Options();
 options.addOption("longOptionWithArg", true, "l");
 String[] result = parser.flatten(options, args, true);
 Assert.assertArrayEquals(new String[]{"--long-option-with-arg=value1", "--long-option-with-arg=value2"}, result);
 }

 @Test
 public void testFlattenWithMultiWordOption() {
 PosixParser parser = new PosixParser();
 String[] args = {"--my-long-option-with-spaces"};
 Options options = new Options();
 options.addOption("myLongOptionWithSpaces", true, "m");
 String[] result = parser.flatten(options, args, false);
 Assert.assertArrayEquals(new String[]{"--my-long-option-with-spaces"}, result);
 }

 @Test
 public void testFlattenWithMultiWordOptionAndValue() {
 PosixParser parser = new PosixParser();
 String[] args = {"--my-long-option-with-spaces=value"};
 Options options = new Options();
 options.addOption("myLongOptionWithSpaces", true, "m");
 String[] result = parser.flatten(options, args, false);
 Assert.assertArrayEquals(new String[]{"--my-long-option-with-spaces=value"}, result);
 }

 @Test
 public void testFlattenWithMultiWordOptionAndValueAndStopAtNonOption() {
 PosixParser parser = new PosixParser();
 String[] args = {"--my-long-option-with-spaces=value1", "--my-long-option-with-spaces=value2", "nonOption"};
 Options options = new Options();
 options.addOption("myLongOptionWithSpaces", true, "m");
 String[] result = parser.flatten(options, args, true);
 Assert.assertArrayEquals(new String[]{"--my-long-option-with-spaces=value1"}, result);
 }

 @Test
 public void testFlattenWithMultiWordOptionAndMultipleValues() {
 PosixParser parser = new PosixParser();
 String[] args = {"--my-long-option-with-spaces=value1 value2"};
 Options options = new Options();
 options.addOption("myLongOptionWithSpaces", true, "m");
 String[] result = parser.flatten(options, args, false);
 Assert.assertArrayEquals(new String[]{"--my-long-option-with-spaces=value1", "--my-long-option-with-spaces=value2"}, result);
 }

 @Test
 public void testFlattenWithMultiWordOptionAndMultipleValuesAndStopAtNonOption() {
 PosixParser parser = new PosixParser();
 String[] args = {"--my-long-option-with-spaces=value1 value2", "nonOption"};
 Options options = new Options();
 options.addOption("myLongOptionWithSpaces", true, "m");
 String[] result = parser.flatten(options, args, true);
 Assert.assertArrayEquals(new String[]{"--my-long-option-with-spaces=value1"}, result);
 }

 @Test
 public void testFlattenWithOptions() {
 PosixParser parser = new PosixParser();
 String[] args = {"--foo", "--bar", "--baz=value"};
 Options options = new Options();
 options.addOption("foo", true, "f");
 options.addOption("bar", false, "b");
 options.addOption("baz", true, "z");
 String[] result = parser.flatten(options, args, false);
 Assert.assertArrayEquals(new String[]{"--foo", "--bar", "--baz=value"}, result);
 }

 @Test
 public void testFlattenWithOptionsAndStopAtNonOption() {
 PosixParser parser = new PosixParser();
 String[] args = {"--foo", "--bar", "--baz=value", "nonOption"};
 Options options = new Options();
 options.addOption("foo", true, "f");
 options.addOption("bar", false, "b");
 options.addOption("baz", true, "z");
 String[] result = parser.flatten(options, args, true);
 Assert.assertArrayEquals(new String[]{"--foo", "--bar"}, result);
 }

 @Test
 public void testFlattenWithSingleHyphen() {
 PosixParser parser = new PosixParser();
 String[] args = {"-", "-a"};
 Options options = new Options();
 String[] result = parser.flatten(options, args, false);
 Assert.assertArrayEquals(new String[]{"-", "--", "-a"}, result);
 }

 @Test
 public void testFlattenWithSingleHyphenAndStopAtNonOption() {
 PosixParser parser = new PosixParser();
 String[] args = {"-", "-a", "nonOption"};
 Options options = new Options();
 String[] result = parser.flatten(options, args, true);
 Assert.assertArrayEquals(new String[]{"-"}, result);
 }

 @Test
 public void testFlattenWithDoubleHyphen() {
 PosixParser parser = new PosixParser();
 String[] args = {"--", "--foo"};
 Options options = new Options();
 String[] result = parser.flatten(options, args, false);
 Assert.assertArrayEquals(new String[]{"--", "--foo"}, result);
 }

 @Test
 public void testFlattenWithDoubleHyphenAndStopAtNonOption() {
 PosixParser parser = new PosixParser();
 String[] args = {"--", "--foo", "nonOption"};
 Options options = new Options();
 String[] result = parser.flatten(options, args, true);
 Assert.assertArrayEquals(new String[]{"--"}, result);
 }

 @Test
 public void testFlattenWithLongOptionStartingWithTwoHyphens() {
 PosixParser parser = new PosixParser();
 String[] args = {"----longOption"};
 Options options = new Options();
 String[] result = parser.flatten(options, args, false);
 Assert.assertArrayEquals(new String[]{"----longOption"}, result);
 }

 @Test
 public void testFlattenWithLongOptionStartingWithTwoHyphensAndStopAtNonOption() {
 PosixParser parser = new PosixParser();
 String[] args = {"----longOption", "nonOption"};
 Options options = new Options();
 String[] result = parser.flatten(options, args, true);
 Assert.assertArrayEquals(new String[]{"----longOption"}, result);
 }

 @Test
 public void testFlattenWithSingleCharacterOptionAndArgument() {
 PosixParser parser = new PosixParser();
 String[] args = {"-a", "arg1"};
 Options options = new Options();
 options.addOption("a", true, "a");
 options.addOption("b", false, "b");
 String[] result = parser.flatten(options, args, false);
 Assert.assertArrayEquals(new String[]{"-a", "arg1"}, result);
 }

 @Test
 public void testFlattenWithSingleCharacterOptionAndArgumentAndStopAtNonOption() {
 PosixParser parser = new PosixParser();
 String[] args = {"-a", "arg1", "nonOption"};
 Options options = new Options();
 options.addOption("a", true, "a");
 options.addOption("b", false, "b");
 String[] result = parser.flatten(options, args, true);
 Assert.assertArrayEquals(new String[]{"-a"}, result);
 }

 @Test
 public void testFlattenWithSingleCharacterOptionWithoutArgument() {
 PosixParser parser = new PosixParser();
 String[] args = {"-a"};
 Options options = new Options();
 options.addOption("a", true, "a");
 options.addOption("b", false, "b");
 String[] result = parser.flatten(options, args, false);
 Assert.assertArrayEquals(new String[]{"-a"}, result);
 }

 @Test
 public void testFlattenWithSingleCharacterOptionWithoutArgumentAndStopAtNonOption() {
 PosixParser parser = new PosixParser();
 String[] args = {"-a", "nonOption"};
 Options options = new Options();
 options.addOption("a", true, "a");
 options.addOption("b", false, "b");
 String[] result = parser.flatten(options, args, true);
 Assert.assertArrayEquals(new String[0], result);
 }

 @Test
 public void testFlattenWithMultiCharacterOptionAndArgument() {
 PosixParser parser = new PosixParser();
 String[] args = {"--optionWithTwoLetters", "arg1"};
 Options options = new Options();
 options.addOption("optionWithTwoLetters", true, "o");
 options.addOption("b", false, "b");
 String[] result = parser.flatten(options, args, false);
 Assert.assertArrayEquals(new String[]{"--optionWithTwoLetters", "arg1"}, result);
 }

 @Test
 public void testFlattenWithMultiCharacterOptionAndArgumentAndStopAtNonOption() {
 PosixParser parser = new PosixParser();
 String[] args = {"--optionWithTwoLetters", "arg1", "nonOption"};
 Options options = new Options();
 options.addOption("optionWithTwoLetters", true, "o");
 options.addOption("b", false, "b");
 String[] result = parser.flatten(options, args, true);
 Assert.assertArrayEquals(new String[]{"--optionWithTwoLetters"}, result);
 }

 @Test
 public void testFlattenWithMultiCharacterOptionWithoutArgument() {
 PosixParser parser = new PosixParser();
 String[] args = {"--optionWithTwoLetters"};
 Options options = new Options();
 options.addOption("optionWithTwoLetters", true, "o");
 options.addOption("b", false, "b");
 String[] result = parser.flatten(options, args, false);
 Assert.assertArrayEquals(new String[]{"--optionWithTwoLetters"}, result);
 }

 @Test
 public void testFlattenWithMultiCharacterOptionWithoutArgumentAndStopAtNonOption() {
 PosixParser parser = new PosixParser();
 String[] args = {"--optionWithTwoLetters", "nonOption"};
 Options options = new Options();
 options.addOption("optionWithTwoLetters", true, "o");
 options.addOption("b", false, "b");
 String[] result = parser.flatten(options, args, true);
 Assert.assertArrayEquals(new String[0], result);
 }

 @Test
 public void testFlattenWithMultiCharacterOptionWithLongArgument() {
 PosixParser parser = new PosixParser();
 String[] args = {"--optionWithLongArgument=longArgumentValue"};
 Options options = new Options();
 options.addOption("optionWithLongArgument", true, "o");
 String[] result = parser.flatten(options, args, false);
 Assert.assertArrayEquals(new String[]{"--optionWithLongArgument=longArgumentValue"}, result);
 }

 @Test
 public void testFlattenWithMultiCharacterOptionWithLongArgumentAndStopAtNonOption() {
 PosixParser parser = new PosixParser();
 String[] args = {"--optionWithLongArgument=longArgumentValue", "nonOption"};
 Options options = new Options();
 options.addOption("optionWithLongArgument", true, "o");
 String[] result = parser.flatten(options, args, true);
 Assert.assertArrayEquals(new String[]{"--optionWithLongArgument=longArgumentValue"}, result);
 }

 @Test
 public void testFlattenWithMultiCharacterOptionWithMultipleArguments() {
 PosixParser parser = new PosixParser();
 String[] args = {"--optionWithTwoArguments=arg1 arg2"};
 Options options = new Options();
 options.addOption("optionWithTwoArguments", true, "o");
 String[] result = parser.flatten(options, args, false);
 Assert.assertArrayEquals(new String[]{"--optionWithTwoArguments=arg1 arg2"}, result);
 }

 @Test
 public void testFlattenWithMultiCharacterOptionWithMultipleArgumentsAndStopAtNonOption() {
 PosixParser parser = new PosixParser();
 String[] args = {"--optionWithTwoArguments=arg1 arg2", "nonOption"};
 Options options = new Options();
 options.addOption("optionWithTwoArguments", true, "o");
 String[] result = parser.flatten(options, args, true);
 Assert.assertArrayEquals(new String[]{"--optionWithTwoArguments=arg1 arg2"}, result);
 }
}
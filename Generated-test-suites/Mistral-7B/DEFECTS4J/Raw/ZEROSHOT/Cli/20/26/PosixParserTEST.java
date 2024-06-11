import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class PosixParserTest {

 @Test
 public void testFlattenEmptyArgs() {
 String[] args = new String[0];
 Options options = new Options();
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 Assert.assertArrayEqual(new String[0], result);
 }

 @Test
 public void testFlattenNoOptions() {
 String[] args = {"arg1", "arg2", "arg3"};
 Options options = new Options();
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 Assert.assertArrayEqual(new String[]{"arg1", "arg2", "arg3"}, result);
 }

 @Test
 public void testFlattenSingleOption() {
 String[] args = {"--option", "value"};
 Options options = new Options();
 options.addOption("option", true, "o");
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 Assert.assertArrayEqual(new String[]{"--option", "value"}, result);
 }

 @Test
 public void testFlattenSingleOptionValue() {
 String[] args = {"--option", "value1", "value2"};
 Options options = new Options();
 options.addOption("option", true, "o");
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 Assert.assertArrayEqual(new String[]{"--option", "value1", "value2"}, result);
 }

 @Test
 public void testFlattenMultiOption() {
 String[] args = {"--option1", "--option2"};
 Options options = new Options();
 options.addOption("option1", true, "o1");
 options.addOption("option2", true, "o2");
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 Assert.assertArrayEqual(new String[]{"--option1", "--option2"}, result);
 }

 @Test
 public void testFlattenMultiOptionValue() {
 String[] args = {"--option1", "value1", "--option2", "value2"};
 Options options = new Options();
 options.addOption("option1", true, "o1");
 options.addOption("option2", true, "o2");
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 Assert.assertArrayEqual(new String[]{"--option1", "value1", "--option2", "value2"}, result);
 }

 @Test
 public void testFlattenLongOption() {
 String[] args = {"--long-option", "value"};
 Options options = new Options();
 options.addOption("longOption", true, "l");
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 Assert.assertArrayEqual(new String[]{"--long-option", "value"}, result);
 }

 @Test
 public void testFlattenLongOptionValue() {
 String[] args = {"--long-option", "value1", "--long-option", "value2"};
 Options options = new Options();
 options.addOption("longOption", true, "l");
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 Assert.assertArrayEqual(new String[]{"--long-option", "value1", "--long-option", "value2"}, result);
 }

 @Test(expected = ParseException.class)
 public void testFlattenInvalidOption() {
 String[] args = {"--invalidOption", "value"};
 Options options = new Options();
 options.addOption("option", true, "o");
 PosixParser parser = new PosixParser();
 parser.flatten(options, args, false);
 }

 @Test
 public void testFlattenStopAtNonOption() {
 String[] args = {"--option", "value", "arg1", "arg2"};
 Options options = new Options();
 options.addOption("option", true, "o");
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, args, true);
 Assert.assertArrayEqual(new String[]{"--option", "value"}, result);
 }

 @Test
 public void testFlattenBurstedOption() {
 String[] args = {"-o", "option1", "--option2"};
 Options options = new Options();
 options.addOption("option", true, "o");
 options.addOption("option2", true, "o2");
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 Assert.assertArrayEqual(new String[]{"-o", "option1", "--option2"}, result);
 }

 @Test
 public void testFlattenBurstedOptionWithValue() {
 String[] args = {"-o", "option1", "--option2=value"};
 Options options = new Options();
 options.addOption("option", true, "o");
 options.addOption("option2", true, "o2");
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 Assert.assertArrayEqual(new String[]{"-o", "option1", "--option2=value"}, result);
 }

 @Test
 public void testFlattenBurstedInvalidOption() {
 String[] args = {"-o", "invalidOption", "--option2"};
 Options options = new Options();
 options.addOption("option", true, "o");
 options.addOption("option2", true, "o2");
 PosixParser parser = new PosixParser();
 try {
 parser.flatten(options, args, false);
 } catch (ParseException e) {
 // expected
 }
 }

 @Test
 public void testFlattenBurstedInvalidOptionWithValue() {
 String[] args = {"-o", "invalidOption", "--option2=value"};
 Options options = new Options();
 options.addOption("option", true, "o");
 options.addOption("option2", true, "o2");
 PosixParser parser = new PosixParser();
 try {
 parser.flatten(options, args, false);
 } catch (ParseException e) {
 // expected
 }
 }

 @Test
 public void testFlattenBurstedOptionWithArg() {
 String[] args = {"-o", "option", "--option2=value"};
 Options options = new Options();
 options.addOption("option", true, "o");
 options.addOption("option2", true, "o2");
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 Assert.assertArrayEqual(new String[]{"-o", "option", "--option2=value"}, result);
 }

 @Test
 public void testFlattenBurstedOptionWithArgValue() {
 String[] args = {"-o", "option", "--option2=value1", "--option2=value2"};
 Options options = new Options();
 options.addOption("option", true, "o");
 options.addOption("option2", true, "o2");
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 Assert.assertArrayEqual(new String[]{"-o", "option", "--option2=value1", "--option2=value2"}, result);
 }

 @Test
 public void testFlattenBurstedOptionWithArgAndRemainingArgs() {
 String[] args = {"-o", "option", "--option2=value1", "--option3", "arg1", "arg2"};
 Options options = new Options();
 options.addOption("option", true, "o");
 options.addOption("option2", true, "o2");
 options.addOption("option3", true, "o3");
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 Assert.assertArrayEqual(new String[]{"-o", "option", "--option2=value1", "--option3"}, result);
 }

 @Test
 public void testFlattenBurstedOptionWithArgAndValueAndRemainingArgs() {
 String[] args = {"-o", "option", "--option2=value1", "--option3", "arg1", "arg2"};
 Options options = new Options();
 options.addOption("option", true, "o");
 options.addOption("option2", true, "o2");
 options.addOption("option3", true, "o3");
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 Assert.assertArrayEqual(new String[]{"-o", "option", "--option2=value1", "--option3=arg1,arg2"}, result);
 }

 @Test
 public void testFlattenBurstedOptionWithArgs() {
 String[] args = {"-o", "option", "--option2=value1", "--option2=value2"};
 Options options = new Options();
 options.addOption("option", true, "o");
 options.addOption("option2", true, "o2");
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 Assert.assertArrayEqual(new String[]{"-o", "option", "--option2=value1", "--option2=value2"}, result);
 }

 @Test
 public void testFlattenBurstedOptionWithArgsValue() {
 String[] args = {"-o", "option", "--option2=value1", "--option2=value2", "arg1", "arg2"};
 Options options = new Options();
 options.addOption("option", true, "o");
 options.addOption("option2", true, "o2");
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 Assert.assertArrayEqual(new String[]{"-o", "option", "--option2=value1,arg1,arg2", "--option2=value2"}, result);
 }

 @Test
 public void testFlattenBurstedOptionWithArgsValueAndStopAtNonOption() {
 String[] args = {"-o", "option", "--option2=value1", "--option2=value2", "arg1", "arg2"};
 Options options = new Options();
 options.addOption("option", true, "o");
 options.addOption("option2", true, "o2");
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, args, true);
 Assert.assertArrayEqual(new String[]{"-o", "option", "--option2=value1", "--option2=value2"}, result);
 }
}
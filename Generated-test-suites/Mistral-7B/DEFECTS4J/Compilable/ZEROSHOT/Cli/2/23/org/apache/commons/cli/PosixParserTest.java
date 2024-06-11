package org.apache.commons.cli;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.PosixParser;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class PosixParserTest {

 @Test
 public void testFlattenEmptyArgs() {
 Options options = new Options();
 String[] args = {};
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 assertEquals(new String[0], result);
 }

 @Test
 public void testFlattenSingleOption() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 String[] args = {"-a"};
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 assertEquals(new String[]{"-a"}, result);
 }

 @Test
 public void testFlattenOptionWithArg() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", true, "Option B");
 String[] args = {"-a", "argA", "-b", "argB"};
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 assertEquals(new String[]{"-a", "argA", "-b", "argB"}, result);
 }

 @Test
 public void testFlattenMultipleOptions() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", true, "Option B");
 options.addOption("c", true, "Option C");
 String[] args = {"-a", "-b", "-c"};
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 assertEquals(new String[]{"-a", "-b", "-c"}, result);
 }

 @Test
 public void testFlattenLongOption() {
 Options options = new Options();
 options.addOption("longOptionWithArg", true, "Long Option with arg");
 String[] args = {"--longOptionWithArg", "arg"};
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 assertEquals(new String[]{"--longOptionWithArg", "arg"}, result);
 }

 @Test
 public void testFlattenOptionWithArgAndOtherArgs() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", true, "Option B");
 String[] args = {"-a", "argA", "--longOption", "argLong", "-b", "argB"};
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 assertEquals(new String[]{"-a", "argA", "--longOption", "argLong", "-b", "argB"}, result);
 }

 @Test
 public void testFlattenLongOptionWithoutArg() {
 Options options = new Options();
 options.addOption("longOption", false, "Long Option without arg");
 String[] args = {"--longOption"};
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 assertEquals(new String[]{"--longOption"}, result);
 }

 @Test
 public void testFlattenMultipleLongOptions() {
 Options options = new Options();
 options.addOption("longOption1", false, "Long Option 1");
 options.addOption("longOption2", false, "Long Option 2");
 String[] args = {"--longOption1", "--longOption2"};
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 assertEquals(new String[]{"--longOption1", "--longOption2"}, result);
 }

 @Test
 public void testFlattenSingleDash() {
 Options options = new Options();
 String[] args = {"-"};
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 assertEquals(new String[]{"-"}, result);
 }

 @Test
 public void testFlattenDoubleDash() {
 Options options = new Options();
 String[] args = {"--"};
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 assertEquals(new String[]{"--"}, result);
 }

 @Test
 public void testFlattenDoubleDashWithArg() {
 Options options = new Options();
 String[] args = {"--arg"};
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 assertEquals(new String[]{"--arg"}, result);
 }

 @Test
 public void testFlattenStopAtNonOption() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", true, "Option B");
 String[] args = {"-a", "-b", "nonOption"};
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, args, true);
 assertEquals(new String[]{"-a", "-b"}, result);
 }

 @Test
 public void testFlattenEmptyOption() {
 Options options = new Options();
 String[] args = {};
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 assertNull(result);
 }

 @Test
 public void testFlattenNullOptions() {
 PosixParser parser = new PosixParser();
 String[] args = {"arg1", "arg2"};
 assertNull(parser.flatten(null, args, false));
 }

 @Test
 public void testFlattenNullArgs() {
 Options options = new Options();
 PosixParser parser = new PosixParser();
 assertNull(parser.flatten(options, null, false));
 }
}
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class PosixParserTest {

 @Test
 public void testFlattenEmpty() {
 Options options = new Options();
 String[] arguments = new String[0];
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(new String[0], result);
 }

 @Test
 public void testFlattenSimple() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 String[] arguments = {"-a"};
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(new String[]{"-a"}, result);
 }

 @Test
 public void testFlattenLongOption() {
 Options options = new Options();
 options.addOption("long-option", true, "Option with long name");
 String[] arguments = {"--long-option"};
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(new String[]{"--long-option"}, result);
 }

 @Test
 public void testFlattenShortOptionWithArg() {
 Options options = new Options();
 options.addOption("a", true, "Option A with argument");
 String[] arguments = {"-a", "arg"};
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(new String[]{"-a", "arg"}, result);
 }

 @Test
 public void testFlattenShortOptionWithOptionalArg() {
 Options options = new Options();
 options.addOption("a", true, "Option A with optional argument");
 String[] arguments = {"-a"};
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(new String[]{"-a"}, result);
 }

 @Test
 public void testFlattenShortOptionWithArgAndAnotherOption() {
 Options options = new Options();
 options.addOption("a", true, "Option A with argument");
 options.addOption("b", true, "Option B");
 String[] arguments = {"-a", "arg1", "-b"};
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(new String[]{"-a", "arg1"}, result);
 }

 @Test(expected = ParseException.class)
 public void testFlattenUnknownOption() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 String[] arguments = {"--unknown"};
 PosixParser parser = new PosixParser();
 parser.flatten(options, arguments, false);
 }

 @Test(expected = ParseException.class)
 public void testFlattenUnknownOptionWithArguments() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 String[] arguments = {"--unknown", "arg1", "arg2"};
 PosixParser parser = new PosixParser();
 parser.flatten(options, arguments, false);
 }

 @Test
 public void testFlattenLongOptionWithArg() {
 Options options = new Options();
 options.addOption("long-option", true, "Option with long name and argument");
 String[] arguments = {"--long-option", "arg"};
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(new String[]{"--long-option", "arg"}, result);
 }

 @Test
 public void testFlattenLongOptionWithOptionalArg() {
 Options options = new Options();
 options.addOption("long-option", true, "Option with long name and optional argument");
 String[] arguments = {"--long-option"};
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(new String[]{"--long-option"}, result);
 }

 @Test
 public void testFlattenLongOptionWithArgAndAnotherOption() {
 Options options = new Options();
 options.addOption("long-option1", true, "Option with long name 1");
 options.addOption("long-option2", true, "Option with long name 2");
 String[] arguments = {"--long-option1", "arg1", "--long-option2"};
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(new String[]{"--long-option1", "arg1"}, result);
 }

 @Test
 public void testFlattenLongOptionWithArgAndAnotherOptionAndUnknownOption() {
 Options options = new Options();
 options.addOption("long-option1", true, "Option with long name 1");
 options.addOption("long-option2", true, "Option with long name 2");
 String[] arguments = {"--long-option1", "arg1", "--unknown"};
 PosixParser parser = new PosixParser();
 try {
 parser.flatten(options, arguments, false);
 } catch (ParseException e) {
 String[] result = new String[arguments.length - 1];
 System.arraycopy(arguments, 0, result, 0, arguments.length - 1);
 assertArrayEquals(result, parser.parseUnknown(arguments[arguments.length - 1]));
 }
 }

 @Test
 public void testFlattenLongOptionWithArgAndAnotherOptionAndUnknownOptionWithArguments() {
 Options options = new Options();
 options.addOption("long-option1", true, "Option with long name 1");
 options.addOption("long-option2", true, "Option with long name 2");
 String[] arguments = {"--long-option1", "arg1", "--unknown", "arg3", "arg4"};
 PosixParser parser = new PosixParser();
 try {
 parser.flatten(options, arguments, false);
 } catch (ParseException e) {
 String[] result = new String[arguments.length - 2];
 System.arraycopy(arguments, 0, result, 0, arguments.length - 2);
 assertArrayEquals(result, parser.parseUnknown(arguments[arguments.length - 2], arguments[arguments.length - 1]));
 }
 }

 @Test
 public void testParseUnknownEmpty() {
 Options options = new Options();
 String[] result = PosixParser.parseUnknown("");
 assertArrayEquals(new String[0], result);
 }

 @Test
 public void testParseUnknownSingle() {
 Options options = new Options();
 String[] result = PosixParser.parseUnknown("--unknown");
 assertEquals("unknown", result[0]);
 }

 @Test
 public void testParseUnknownWithArgs() {
 Options options = new Options();
 String[] result = PosixParser.parseUnknown("--unknown", "arg1", "arg2");
 assertEquals("unknown", result[0]);
 assertArrayEquals(new String[]{"arg1", "arg2"}, result[1]);
 }
}
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class PosixParserTest {

 @Test
 public void testFlatten_Simple() {
 Options options = new Options();
 String[] args = {"--foo", "-b", "-c=bar", "--", "-d", "-e"};
 PosixParser parser = new PosixParser();

 String[] result = parser.flatten(options, args, false);

 assertEquals("--foo, -b, -c, --, -d, -e", Arrays.toString(result));
 }

 @Test
 public void testFlatten_StopAtNonOption() {
 Options options = new Options();
 options.addOption("b", true, "boolean option");
 String[] args = {"--foo", "-b", "-c=bar", "-d"};
 PosixParser parser = new PosixParser();

 String[] result = parser.flatten(options, args, true);

 assertEquals("--foo, -b, -c=bar, -d", Arrays.toString(result));
 }

 @Test(expected = ParseException.class)
 public void testFlatten_InvalidOption() {
 Options options = new Options();
 String[] args = {"--foo", "-q", "--", "invalidOption", "-d"};
 PosixParser parser = new PosixParser();

 parser.flatten(options, args, false);
 }

 @Test
 public void testFlatten_LongOptionWithValue() {
 Options options = new Options();
 options.addOption("foo", true, "foo option");
 String[] args = {"--foo=true", "-b", "-c=bar", "--", "-d", "-e"};
 PosixParser parser = new PosixParser();

 String[] result = parser.flatten(options, args, false);

 assertEquals("--foo, true, -b, -c, --, -d, -e", Arrays.toString(result));
 }

 @Test
 public void testFlatten_LongOptionWithValue_StopAtNonOption() {
 Options options = new Options();
 options.addOption("foo", true, "foo option");
 String[] args = {"--foo=true", "-b", "-c=bar", "-d"};
 PosixParser parser = new PosixParser();

 String[] result = parser.flatten(options, args, true);

 assertEquals("--foo, true, -b, -c=bar, -d", Arrays.toString(result));
 }

 @Test(expected = ParseException.class)
 public void testFlatten_InvalidLongOptionWithValue() {
 Options options = new Options();
 options.addOption("foo", true, "foo option");
 String[] args = {"--foo=invalid", "-b", "-c=bar", "-d"};
 PosixParser parser = new PosixParser();

 parser.flatten(options, args, false);
 }

 @Test
 public void testFlatten_MultipleLongOptions() {
 Options options = new Options();
 options.addOption("foo", true, "foo option");
 options.addOption("bar", true, "bar option");
 String[] args = {"--foo=true", "--bar=false", "-b", "-c=bar", "--", "-d", "-e"};
 PosixParser parser = new PosixParser();

 String[] result = parser.flatten(options, args, false);

 assertEquals("--foo, true, --bar, false, -b, -c, --, -d, -e", Arrays.toString(result));
 }

 @Test
 public void testFlatten_MultipleLongOptions_StopAtNonOption() {
 Options options = new Options();
 options.addOption("foo", true, "foo option");
 options.addOption("bar", true, "bar option");
 String[] args = {"--foo=true", "--bar=false", "-b", "-c=bar", "-d"};
 PosixParser parser = new PosixParser();

 String[] result = parser.flatten(options, args, true);

 assertEquals("--foo, true, --bar, false, -b, -c=bar, -d", Arrays.toString(result));
 }

 @Test
 public void testFlatten_InvalidLongOption() {
 Options options = new Options();
 options.addOption("foo", true, "foo option");
 String[] args = {"--foo", "--invalid", "-b", "-c=bar", "--", "-d", "-e"};
 PosixParser parser = new PosixParser();

 String[] result = parser.flatten(options, args, false);

 assertNull(result);
 }

 @Test
 public void testFlatten_EmptyArgList() {
 Options options = new Options();PosixParser parser = new PosixParser();
 String[] args = {};
 String[] result = parser.flatten(options, args, false);
 assertEquals("", Arrays.toString(result));
 }
}
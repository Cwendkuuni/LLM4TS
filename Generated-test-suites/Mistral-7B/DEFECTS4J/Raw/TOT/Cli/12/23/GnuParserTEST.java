import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class GnuParserTest {

 @Test
 public void testFlattenWithOptionsAndArguments() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "Option A with no argument");
 options.addOption("b", 1, "Option B with an integer argument");
 options.addOption("c", 2, "Option C with a required string argument");

 String[] args = new String[]{"-a", "--b", "1", "-c", "value"};
 GnuParser parser = new GnuParser();
 String[] result = parser.parse(options, args, true);

 Assert.assertArrayEquals(new String[]{"-a", "--b", "-c", "value"}, result);
 }

 @Test
 public void testFlattenWithLongOptions() throws ParseException {
 Options options = new Options();
 options.addOption("longOptionWithNoArgument", false, "Long option with no argument");
 options.addOption("longOptionWithValue", true, "Long option with a value argument");

 String[] args = new String[]{"--longOptionWithNoArgument", "--longOptionWithValue=value"};
 GnuParser parser = new GnuParser();
 String[] result = parser.parse(options, args, true);

 Assert.assertArrayEquals(new String[]{"--longOptionWithNoArgument", "--longOptionWithValue=value"}, result);
 }

 @Test
 public void testFlattenWithUnrecognizedOption() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "Option A with no argument");
 options.addOption("b", 1, "Option B with an integer argument");

 String[] args = new String[]{"-x", "-a", "-b", "1", "value"};
 GnuParser parser = new GnuParser();
 String[] result = parser.parse(options, args, true);

 Assert.assertArrayEquals(new String[]{"-x", "-a", "-b", "1", "value"}, result); // no change, unrecognized option is not handled
 }

 @Test
 public void testFlattenWithLongArgument() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "Option A with no argument");
 options.addOption("b", 1, "Option B with an integer argument");
 options.addOption("c", 2, "Option C with a required string argument");

 String[] args = new String[]{"-a", "--longArgument=longValue"};
 GnuParser parser = new GnuParser();
 String[] result = parser.parse(options, args, true);

 Assert.assertArrayEquals(new String[]{"-a", "--longArgument=longValue"}, result);
 }

 @Test(expected = ParseException.class)
 public void testFlattenWithStopAtNonOption() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "Option A with no argument");
 options.addOption("b", 1, "Option B with an integer argument");

 String[] args = new String[]{"-a", "nonOption", "-b", "1"};
 GnuParser parser = new GnuParser();
 String[] result = parser.parse(options, args, true);

 Assert.fail("ParseException expected but was not thrown");
 }
}
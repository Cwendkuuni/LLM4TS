import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class GnuParserTest {

 private static final String[] ARGUMENTS_NORMAL = new String[]{"-a", "--optionB", "valueB", "argument1", "argument2"};
 private static final String[] ARGUMENTS_WITH_LONG_OPTION = new String[]{"--longOption"};
 private static final String[] ARGUMENTS_WITH_SHORT_OPTION = new String[]{"-o", "valueB"};
 private static final String[] ARGUMENTS_WITH_INVALID_OPTION = new String[]{"--invalidOption"};
 private static final String[] ARGUMENTS_WITH_DASH = new String[]{"--", "-"};

 @Test
 public void testFlattenNormal() throws ParseException {
 Options options = new Options();
 GnuParser parser = new GnuParser();
 String[] result = parser.parse(options, ARGUMENTS_NORMAL, true);

 String[] expected = new String[]{"-a", "--optionB", "valueB", "argument1", "argument2"};
 Assert.assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenLongOption() throws ParseException {
 Options options = new Options();
 GnuParser parser = new GnuParser();
 String[] result = parser.parse(options, ARGUMENTS_WITH_LONG_OPTION, true);

 String[] expected = new String[]{"--longOption"};
 Assert.assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenShortOption() throws ParseException {
 Options options = new Options();
 GnuParser parser = new GnuParser();
 String[] result = parser.parse(options, ARGUMENTS_WITH_SHORT_OPTION, true);

 String[] expected = new String[]{"-o", "valueB"};
 Assert.assertArrayEquals(expected, result);
 }

 @Test(expected = ParseException.class)
 public void testFlattenInvalidOption() throws ParseException {
 Options options = new Options();
 GnuParser parser = new GnuParser();
 String[] result = parser.parse(options, ARGUMENTS_WITH_INVALID_OPTION, true);
 }

 @Test
 public void testFlattenDash() throws ParseException {
 Options options = new Options();
 GnuParser parser = new GnuParser();
 String[] result = parser.parse(options, ARGUMENTS_WITH_DASH, true);

 String[] expected = new String[]{ "--", "-" };
 Assert.assertArrayEquals(expected, result);
 }
}
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class PosixParserTest {

 private static final String[] ARGUMENTS = {"--foo", "--bar=value", "-a--b", "-abc", "-d", "-efg", "--hij=123", "-i", "--qux"};
 private static final String[] ARGUMENTS_NO_OPTION = {"arg1", "arg2"};
 private static final String[] ARGUMENTS_INVALID_OPTION = {"--invalidOption", "arg1", "arg2"};

 @Test
 public void testFlattenWithValidArguments() {
 Options options = new Options();
 options.addOption("foo", true, "Help text for --foo");
 options.addOption("bar", true, "Help text for --bar");
 options.addOption("qux", false, "Help text for --qux");

 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, ARGUMENTS, false);

 assertArrayEquals(new String[]{"--foo", "--bar=value", "--b", "--a", "-c", "-d", "--hij=123", "--", "i", "--qux"}, result);
 }

 @Test(expected = ParseException.class)
 public void testFlattenWithInvalidArguments() {
 Options options = new Options();
 options.addOption("foo", true, "Help text for --foo");
 options.addOption("bar", true, "Help text for --bar");
 options.addOption("qux", false, "Help text for --qux");

 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, ARGUMENTS_INVALID_OPTION, false);
 }

 @Test
 public void testFlattenWithStopAtNonOption() {
 Options options = new Options();
 options.addOption("foo", true, "Help text for --foo");
 options.addOption("bar", true, "Help text for --bar");
 options.addOption("qux", false, "Help text for --qux");

 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, ARGUMENTS_NO_OPTION, true);

 assertArrayEquals(new String[]{"arg1", "arg2"}, result);
 }

 @Test
 public void testInit() {
 PosixParser parser = new PosixParser();

 parser.init();

 assertTrue(parser.eatTheRest == false);
 assertNull(parser.tokens);
 assertNull(parser.currentOption);
 }

 @Test
 public void testProcessSingleHyphen() {
 PosixParser parser = new PosixParser();
 String result = parser.processSingleHyphen("-");

 assertEquals("-", result);
 }

 @Test
 public void testProcessOptionToken() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("foo", true, "Help text for --foo");
 options.addOption("bar", true, "Help text for --bar");
 options.addOption("qux", false, "Help text for --qux");

 parser.init();
 parser.options = options;

 String result = parser.processOptionToken("-f", false);

 assertEquals("-f", result);
 }

 @Test
 public void testBurstTokenWithValidArguments() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("foo", true, "Help text for --foo");
 options.addOption("bar", true, "Help text for --bar");
 options.addOption("qux", false, "Help text for --qux");

 parser.init();
 parser.options = options;

 String result = parser.burstToken("-f--b--a--c--d--efg--hij=123", false);

 assertArrayEquals(new String[]{"--foo", "--b", "--a", "-c", "-d", "--efg", "--hij=123"}, Arrays.asList(result).toArray(new String[0]));
 }

 @Test
 public void testBurstTokenWithStopAtNonOption() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("foo", true, "Help text for --foo");
 options.addOption("bar", true, "Help text for --bar");
 options.addOption("qux", false, "Help text for --qux");

 parser.init();
 parser.options = options;

 String result = parser.burstToken("-f--b--a--c--d--efg--hij=123", true);

 assertArrayEquals(new String[]{"--foo", "--b", "--a--c--d--efg--hij=123"}, Arrays.asList(result).toArray(new String[0]));
 }

 @Test
 public void testBurstTokenWithNoOption() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("foo", true, "Help text for --foo");
 options.addOption("bar", true, "Help text for --bar");
 options.addOption("qux", false, "Help text for --qux");

 parser.init();
 parser.options = options;

 String result = parser.burstToken("--noOption", false);

 assertArrayEquals(new String[]{"--noOption"}, Arrays.asList(result).toArray(new String[0]));
 }
}
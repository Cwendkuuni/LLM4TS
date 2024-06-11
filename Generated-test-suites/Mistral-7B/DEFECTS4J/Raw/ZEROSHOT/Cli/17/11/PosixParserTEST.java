import org.apache.commons.cli.Options;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.HelpFormatter;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class PosixParserTest {

 @Test
 public void testFlatten() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B with an argument");
 options.addOption("c", 2, "Option C with two arguments");
 options.addOption("d", 3, "Option D with three arguments");
 options.addOption("e", false, "Option E");

 Parser parser = new PosixParser();
 String[] args = {"--a", "--e", "-b", "valueB", "valueC1", "valueC2", "-d", "valueD1", "valueD2", "valueD3"};
 String[] expected = {"--a", "--e", "-b", "valueB", "-d", "valueD1", "valueD2", "valueD3"};
 String[] actual = parser.flatten(options, args, false);
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testFlattenWithStopAtNonOption() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B with an argument");
 options.addOption("c", 2, "Option C with two arguments");
 options.addOption("d", 3, "Option D with three arguments");
 options.addOption("e", false, "Option E");

 Parser parser = new PosixParser();
 String[] args = {"--a", "--e", "-b", "valueB", "valueC1", "valueC2", "-d", "valueD1", "valueD2", "valueD3", "nonOption"};
 String[] expected = {"--a", "--e", "-b", "valueB", "-d", "valueD1", "valueD2", "valueD3", "nonOption"};
 String[] actual = parser.flatten(options, args, true);
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testFlattenWithSingleHyphen() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B with an argument");
 options.addOption("c", 2, "Option C with two arguments");
 options.addOption("d", 3, "Option D with three arguments");
 options.addOption("e", false, "Option E");

 Parser parser = new PosixParser();
 String[] args = {"--", "-a", "--e", "-b", "valueB", "-c", "--d", "valueD1", "valueD2", "valueD3"};
 String[] expected = {"--", "--a", "--e", "-b", "valueB", "--d", "valueD1", "valueD2", "valueD3"};
 String[] actual = parser.flatten(options, args, false);
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testProcessSingleHyphen() {
 Parser parser = new PosixParser();
 String[] args = {"--", "-"};
 String[] expected = {"--", "-"};
 String[] actual = parser.flatten(null, args, false);
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testProcessOptionToken() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B with an argument");
 options.addOption("c", 2, "Option C with two arguments");
 options.addOption("d", 3, "Option D with three arguments");
 options.addOption("e", false, "Option E");

 Parser parser = new PosixParser();
 String[] args = {"--a", "--e", "-b", "valueB", "-c", "--d", "valueD1", "valueD2", "valueD3"};
 String[] expected = {"--a", "--e", "-b", "valueB", "-d", "valueD1", "valueD2", "valueD3"};
 String[] actual = parser.flatten(options, args, false);

 // Test 'processOptionToken'
 Option optionB = options.getOption("b");
 Option optionD = options.getOption("d");
 assertEquals(optionB, parser.getLastOption());
 assertEquals(optionD, parser.getLastOptionAfter("--d"));
 }

 @Test
 public void testBurstToken() {
 Options options = new Options();
 options.addOption("ab", true, "Option AB");
 options.addOption("cd", 2, "Option CD with two arguments");
 options.addOption("efgh", 4, "Option EFGH with four arguments");

 Parser parser = new PosixParser();
 String[] args = {"-abcd", "efg", "h"};
 String[] expected = {"-a", "-b", "-c", "-d", "--efg", "h"};
 String[] actual = parser.flatten(options, args, false);
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testBurstTokenWithStopAtNonOption() {
 Options options = new Options();
 options.addOption("ab", true, "Option AB");
 options.addOption("cd", 2, "Option CD with two arguments");
 options.addOption("efgh", 4, "Option EFGH with four arguments");

 Parser parser = new PosixParser();
 String[] args = {"-abcd", "efg", "h", "nonOption"};
 String[] expected = {"-a", "-b", "-c", "-d", "--efg", "h", "nonOption"};
 String[] actual = parser.flatten(options, args, true);
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testBurstTokenWithLongOption() {
 Options options = new Options();
 options.addOption("longOptionWithTwoArguments", 2, "Long Option with Two Arguments");

 Parser parser = new PosixParser();
 String[] args = {"--longOptionWithTwoArguments=value1", "value2"};
 String[] expected = {"--longOptionWithTwoArguments", "value1", "value2"};
 String[] actual = parser.flatten(options, args, false);
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testBurstTokenWithLongOptionAndStopAtNonOption() {
 Options options = new Options();
 options.addOption("longOptionWithTwoArguments", 2, "Long Option with Two Arguments");

 Parser parser = new PosixParser();
 String[] args = {"--longOptionWithTwoArguments=value1", "value2", "nonOption"};
 String[] expected = {"--longOptionWithTwoArguments", "value1", "value2", "nonOption"};
 String[] actual = parser.flatten(options, args, true);
 assertArrayEquals(expected, actual);
 }
}
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class CommandLineTest {

 @Test
 public void testBasicUsage() throws ParseException {
 CommandLineParser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("a", "apple", false, "Print apple message.");
 options.addOption("b", "banana", false, "Print banana message.");

 CommandLine cmd = parser.parse(options.parse(new String[]{"-a", "-b"}));

 Assert.assertTrue(cmd.hasOption("a"));
 Assert.assertFalse(cmd.hasOption("c"));
 Assert.assertNull(cmd.getOptionValue("a"));
 Assert.assertNull(cmd.getOptionObject("a"));
 Assert.assertNull(cmd.getOptionValue("b"));
 Assert.assertNull(cmd.getOptionObject("b"));
 }

 @Test
 public void testArgUsage() throws ParseException {
 Options options = new Options();
 options.addOption("i", "input", true, "Input file.");

 CommandLine cmd = new CommandLine();
 CommandLineParser parser = new DefaultParser();

 List args = new LinkedList() {{
 add("test.txt");
 add("-i");
 add("inputFile.txt");
 add("--help");
 }} ;

 parser.parse(options.parse(args.toArray(new String[args.size()])), cmd, false);

 Assert.assertTrue(cmd.hasOption("i"));
 Assert.assertEquals("test.txt", cmd.getOptionValue("i"));
 Assert.assertEquals("test.txt", cmd.getOptionObject("i"));
 Assert.assertNull(cmd.getOptionValue("help"));
 Assert.assertNull(cmd.getOptionObject("help"));
 Assert.assertEquals(2, cmd.args.size());
 Assert.assertEquals("inputFile.txt", cmd.args.get(0));
 Assert.assertEquals("--help", cmd.args.get(1));
 }

 @Test(expected = ParseException.class)
 public void testMissingOption() throws ParseException {
 Options options = new Options();
 options.addOption("i", "input", true, "Input file.");

 CommandLine cmd = new CommandLine();
 CommandLineParser parser = new DefaultParser();

 List args = new LinkedList() {{
 add("--help");
 add("-j");
 add("inputFile.txt");
 }} ;

 parser.parse(options.parse(args.toArray(new String[args.size()])), cmd, false);
 }

 @Test(expected = ParseException.class)
 public void testInvalidOption() throws ParseException {
 Options options = new Options();
 options.addOption("i", "input", true, "Input file.");

 CommandLine cmd = new CommandLine();
 CommandLineParser parser = new DefaultParser();

 List args = new LinkedList() {{
 add("--invalidOption");
 add("-i");
 add("inputFile.txt");
 }} ;

 parser.parse(options.parse(args.toArray(new String[args.size()])), cmd, false);
 }

 @Test
 public void testHelp() throws ParseException {
 CommandLineParser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("h", "help", false, "Print help message.");

 CommandLine cmd = parser.parse(options.parse(new String[]{"--help"}));
 }

 @Test
 public void testEmpty() throws ParseException {
 CommandLineParser parser = new DefaultParser();
 Options options = new Options();

 CommandLine cmd = parser.parse(options.parse(new String[]{}), cmd, false);
 }
}
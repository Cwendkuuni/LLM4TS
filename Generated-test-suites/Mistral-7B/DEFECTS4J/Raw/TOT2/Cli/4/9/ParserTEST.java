import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

 @Test
 public void testParseNormalCase() throws ParseException {
 Options options = new Options();
 options.addOption("o", "output", true, "output file path");
 CommandLineParser parser = new DefaultParser();
 String[] args = new String[]{"-o", "test.txt"};
 CommandLine cmd = parser.parse(options, args, false);
 Assert.assertEquals("test.txt", cmd.getOptionValue("output"));
 }

 @Test(expected = ParseException.class)
 public void testParseMissingOption() throws ParseException {
 Options options = new Options();
 CommandLineParser parser = new DefaultParser();
 String[] args = new String[]{"-x", "--input", "test.txt"};
 CommandLine cmd = parser.parse(options, args, false);
 }

 @Test(expected = ParseException.class)
 public void testParseMissingValue() throws ParseException {
 Options options = new Options();
 options.addOption("o", "output", true, "output file path");
 CommandLineParser parser = new DefaultParser();
 String[] args = new String[]{"-o"};
 CommandLine cmd = parser.parse(options, args, false);
 }

 @Test
 public void testParseMultipleOptions() throws ParseException {
 Options options = new Options();
 options.addOption("o", "output", true, "output file path");
 CommandLineParser parser = new DefaultParser();
 String[] args = new String[]{"-o", "test1.txt", "-o", "test2.txt"};
 CommandLine cmd = parser.parse(options, args, false);
 Assert.assertEquals("test1.txt", cmd.getOptionValue("output"));
 }

 @Test
 public void testParseRequiredOptions() throws ParseException {
 Options options = new Options();
 options.addOption("o", "output", true, "output file path");
 options.addRequiredOption("i", "input", true, "input file path");
 CommandLineParser parser = new DefaultParser();
 String[] args = new String[]{"-i", "test.txt"};
 CommandLine cmd = parser.parse(options, args, false);
 Assert.assertEquals("test.txt", cmd.getOptionValue("input"));
 }

 @Test(expected = MissingOptionException.class)
 public void testParseMissingRequiredOption() throws ParseException {
 Options options = new Options();
 options.addOption("o", "output", true, "output file path");
 options.addRequiredOption("i", "input", true, "input file path");
 CommandLineParser parser = new DefaultParser();
 String[] args = new String[]{"-o", "test.txt"};
 }

 @Test
 public void testParseMultipleRequiredOptions() throws ParseException {
 Options options = new Options();
 options.addOption("o", "output", true, "output file path");
 options.addRequiredOption("i", "input", true, "input file path");
 options.addRequiredOption("e", "encoding", true, "encoding");
 CommandLineParser parser = new DefaultParser();
 String[] args = new String[]{"-i", "test.txt", "-e", "UTF-8", "-o", "output.txt"};
 CommandLine cmd = parser.parse(options, args, false);
 Assert.assertEquals("UTF-8", cmd.getOptionValue("encoding"));
 }

 @Test
 public void testParseWithProperties() throws ParseException {
 Options options = new Options();
 options.addOption("o", "output", true, "output file path");
 CommandLineParser parser = new DefaultParser();
 Properties properties = new Properties();
 properties.setProperty("input", "test.txt");
 CommandLine cmd = parser.parse(options, (String[]) null, properties, false);
 Assert.assertEquals("test.txt", cmd.getOptionValue("input"));
 }

 @Test
 public void testParseWithStopAtNonOption() throws ParseException {
 Options options = new Options();
 options.addOption("o", "output", true, "output file path");
 CommandLineParser parser = new DefaultParser();
 String[] args = new String[]{"--", "-o", "test.txt"};
 CommandLine cmd = parser.parse(options, args, true);
 Assert.assertArrayEquals(new String[]{"--", "-o", "test.txt"}, cmd.getArgs());
 }
}
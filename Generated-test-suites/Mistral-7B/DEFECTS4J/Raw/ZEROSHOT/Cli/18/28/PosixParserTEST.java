import org.apache.commons.cli.Options;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.HelpFormatter;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class PosixParserTest {

 private Parser parser;
 private Options options;

 @Before
 public void setUp() {
 options = new Options();
 options.addRequiredOption("i", "input", true, "Input file path");
 options.addOption("o", "output", true, "Output file path");
 }

 @Test
 public void testSimple() {
 String[] args = new String[]{"-i", "input.txt", "-o", "output.txt"};
 String[] expected = new String[]{"-i", "input.txt", "-o", "output.txt"};

 parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testSingleHyphen() {
 String[] args = new String[]{"-"};
 String[] expected = new String[]{"--"};

 parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testDoubleHyphen() {
 String[] args = new String[]{"--"};
 String[] expected = new String[]{"--"};

 parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testOption() {
 String[] args = new String[]{"-i", "input.txt", "-o", "output.txt", "-t", "text"};
 String[] expected = new String[]{"-i", "input.txt", "-o", "output.txt", "-t", "text"};

 Parser parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testOptionWithArgument() {
 String[] args = new String[]{"-i", "input.txt", "-o", "output.txt", "--outputDir", "/path/to/output"};
 String[] expected = new String[]{"-i", "input.txt", "-o", "output.txt", "--outputDir", "/path/to/output"};

 Parser parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testStopAtNonOption() {
 String[] args = new String[]{"-i", "input.txt", "input2.txt", "-o", "output.txt"};
 String[] expected = new String[]{"-i", "input.txt", "--", "input2.txt", "-o", "output.txt"};

 Parser parser = new PosixParser();
 String[] result = parser.flatten(options, args, true);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testComplex() {
 String[] args = new String[]{"-i", "input.txt", "--outputDir", "/path/to/output", "-o", "output.txt", "--param1", "value1", "--param2=value2"};
 String[] expected = new String[]{"-i", "input.txt", "--outputDir", "/path/to/output", "--param1", "value1", "--param2", "value2", "-o", "output.txt"};

 Parser parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testBursting() {
 String[] args = new String[]{"-it", "input.txt", "output.txt"};
 String[] expected = new String[]{"-i", "input.txt", "-t", "output.txt"};

 Parser parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testBurstingWithStopAtNonOption() {
 String[] args = new String[]{"-it", "input.txt", "output.txt", "arg1", "arg2"};
 String[] expected = new String[]{"-i", "--", "arg1", "arg2", "t", "output.txt"};

 Parser parser = new PosixParser();
 String[] result = parser.flatten(options, args, true);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testBurstingLongOption() {
 String[] args = new String[]{"--longOptionWithArgument", "value"};
 String[] expected = new String[]{"--longOptionWithArgument", "value"};

 Parser parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testBurstingLongOptionWithArgument() {
 String[] args = new String[]{"--longOptionWithArguments=arg1,arg2"};
 String[] expected = new String[]{"--longOptionWithArguments", "arg1,arg2"};

 Parser parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 }
}
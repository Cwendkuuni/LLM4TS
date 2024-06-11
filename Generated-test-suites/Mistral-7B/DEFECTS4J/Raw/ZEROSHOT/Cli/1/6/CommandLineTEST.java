import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class CommandLineTest {

 @Test
 public void testCommandLineParsing() throws ParseException {
 // Given
 Options options = new Options();
 options.addOption("i", "input", true, "input file");
 options.addOption("o", "output", true, "output file");
 options.addOption("h", "help", false, "print this help message");

 // When
 String[] args = new String[]{"-i", "input.txt", "-o", "output.txt"};
 CommandLine commandLine = new CommandLine().parse(options, args);

 // Then
 Assert.assertFalse("help option should not be set", commandLine.hasOption("h"));
 Assert.assertFalse("input option should have a value", commandLine.hasOption("i", false));
 Assert.assertTrue("input option should have a value", commandLine.hasOption("i"));
 Assert.assertEquals("input option value should be 'input.txt'", "input.txt", commandLine.getOptionValue("i"));
 Assert.assertFalse("output option should have a value", commandLine.hasOption("o", false));
 Assert.assertTrue("output option should have a value", commandLine.hasOption("o"));
 Assert.assertEquals("output option value should be 'output.txt'", "output.txt", commandLine.getOptionValue("o"));
 }

 @Test(expected = ParseException.class)
 public void testParseExceptionWhenOptionNotRecognized() {
 // Given
 Options options = new Options();
 options.addOption("i", "input", true, "input file");
 options.addOption("o", "output", true, "output file");

 // When & Then
 String[] args = new String[]{"-x", "unknownOption"};
 CommandLine commandLine = new CommandLine().parse(options, args);
 }

 @Test
 public void testCommandLineWithUnrecognizedOptions() {
 // Given
 Options options = new Options();
 options.addOption("i", "input", true, "input file");
 options.addOption("o", "output", true, "output file");

 // When
 String[] args = new String[]{"-i", "input.txt", "-o", "output.txt", "-x", "unknownOption"};
 CommandLine commandLine = new CommandLine().parse(options, args);

 // Then
 Assert.assertArrayEquals("unrecognized options should be in the args array", new String[]{"-x", "unknownOption"}, commandLine.getArgList().toArray());
 }
}
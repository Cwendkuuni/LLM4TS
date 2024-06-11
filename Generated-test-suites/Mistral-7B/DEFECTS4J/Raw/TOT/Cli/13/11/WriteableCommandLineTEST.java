import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.DefaultParser;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineTest {

 // Test case for addOption method
 @Test
 public void testAddOption() {
 CommandLine commandLine = new DefaultCommandLine();
 Option option = new Option("o", "output", true, "Output file path");
 commandLine.addOption(option);
 assertEquals(1, commandLine.getOptions().size());
 }

 // Test case for addValue method
 @Test
 public void testAddValue() {
 CommandLine commandLine = new DefaultCommandLine();
 Option option = new Option("i", "input", true, "Input file path");
 commandLine.addOption(option);
 commandLine.addValue(option, "input.txt");
 assertEquals("input.txt", commandLine.getOptionValues("input").get(0));
 }

 // Test case for getValues method
 @Test
 public void testGetValues() {
 CommandLine commandLine = new DefaultCommandLine();
 Option option = new Option("o", "output", true, "Output file path");
 commandLine.addOption(option);
 commandLine.addValue(option, "output.txt");
 assertEquals(1, commandLine.getOptions().size());
 assertEquals(1, commandLine.getOptionValues("output").size());
 assertEquals("output.txt", commandLine.getOptionValues("output").get(0));
 }

 // Test case for setDefaultValues method
 @Test
 public void testSetDefaultValues() {
 CommandLine commandLine = new DefaultCommandLine();
 Option option = new Option("o", "output", true, "Output file path");
 List<String> defaultValues = Arrays.asList("output1.txt", "output2.txt");
 commandLine.setDefaultValues(option, defaultValues);
 assertEquals(2, commandLine.getDefaultValues("output").size());
 }

 // Test case for addSwitch method
 @Test
 public void testAddSwitch() {
 CommandLine commandLine = new DefaultCommandLine();
 Option option = new Option("d", "debug", false, "Enable debug mode");
 try {
 commandLine.addSwitch(option, true);
 fail("Should not be able to add switch value twice");
 } catch (IllegalStateException e) {
 // Expected exception
 }
 Option option2 = new Option("d", "debug", false, "Enable debug mode");
 commandLine.addSwitch(option2, false);
 assertEquals(false, commandLine.hasOption("debug"));
 }

 // Test case for setDefaultSwitch method
 @Test
 public void testSetDefaultSwitch() {
 CommandLine commandLine = new DefaultCommandLine();
 Option option = new Option("d", "debug", false, "Enable debug mode");
 commandLine.setDefaultSwitch(option, true);
 assertEquals(true, commandLine.getOptionValue("debug", false));
 }

 // Test case for addProperty method
 @Test
 public void testAddProperty() {
 CommandLine commandLine = new DefaultCommandLine();
 commandLine.addProperty("property1", "value1");
 assertEquals("value1", commandLine.getPropertyValues("property1").get(0));
 }

 // Test case for looksLikeOption method
 @Test
 public void testLooksLikeOption() {
 CommandLine commandLine = new DefaultCommandLine();
 assertTrue(commandLine.looksLikeOption("--output"));
 assertTrue(commandLine.looksLikeOption("--output="));
 assertTrue(commandLine.looksLikeOption("output"));
 assertFalse(commandLine.looksLikeOption("input"));
 }

 // Test case for parse command line with valid options and values
 @Test
 public void testParseValidCommandLine() throws ParseException {
 CommandLine commandLine = new DefaultCommandLine();
 Option inputOption = new Option("i", "input", true, "Input file path");
 Option outputOption = new Option("o", "output", true, "Output file path");
 commandLine.addOption(inputOption);
 commandLine.addOption(outputOption);
 DefaultParser parser = new DefaultParser();
 parser.parse(new String[]{"--input", "input.txt", "--output", "output.txt"}, commandLine);
 assertEquals("input.txt", commandLine.getOptionValues("input").get(0));
 assertEquals("output.txt", commandLine.getOptionValues("output").get(0));
 }

 // Test case for parse command line with invalid options
 @Test(expected = ParseException.class)
 public void testParseInvalidCommandLine() throws ParseException {
 CommandLine commandLine = new DefaultCommandLine();
 Option inputOption = new Option("i", "input", true, "Input file path");
 Option outputOption = new Option("o", "output", true, "Output file path");
 commandLine.addOption(inputOption);
 commandLine.addOption(outputOption);
 DefaultParser parser = new DefaultParser();
 parser.parse(new String[]{"--input", "input.txt", "--output=output.txt", "--invalid"}, commandLine);
 }
}
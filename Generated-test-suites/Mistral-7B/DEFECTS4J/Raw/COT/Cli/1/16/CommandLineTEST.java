import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.junit.Test;
import static org.junit.Assert.*;

public class CommandLineTest {

 private CommandLine commandLine;

 @Before
 public void setUp() {
 commandLine = new CommandLine();
 }

 // Test for hasOption method
 @Test
 public void testHasOption() {
 Option option = new Option("t", "test", false, "Test flag");
 commandLine.addOption(option);
 assertTrue(commandLine.hasOption("t"));
 }

 // Test for hasOption method with invalid option
 @Test
 public void testHasOptionInvalid() {
 assertFalse(commandLine.hasOption("invalidOption"));
 }

 // Test for getOptionValue method with specified option
 @Test
 public void testGetOptionValueSpecified() {
 Option option = new Option("i", "input", true, "Input file");
 commandLine.addOption(option);
 commandLine.parse("--input inputFile.txt");
 assertEquals("inputFile.txt", commandLine.getOptionValue("i"));
 }

 // Test for getOptionValue method with unspecified option
 @Test
 public void testGetOptionValueUnspecified() {
 Option option = new Option("i", "input", true, "Input file");
 commandLine.addOption(option);
 assertNull(commandLine.getOptionValue("i"));
 }

 // Test for getOptionValues method with specified option
 @Test
 public void testGetOptionValuesSpecified() {
 Option option = new Option("l", "list", true, "List files");
 option.setArgs(1);
 commandLine.addOption(option);
 commandLine.parse("--list fileList.txt");
 assertArrayEquals(new String[]{"fileList.txt"}, commandLine.getOptionValues("l"));
 }

 // Test for getOptionValues method with unspecified option
 @Test
 public void testGetOptionValuesUnspecified() {
 Option option = new Option("l", "list", true, "List files");
 option.setArgs(1);
 commandLine.addOption(option);
 assertNull(commandLine.getOptionValues("l"));
 }

 // Test for getArgs method
 @Test
 public void testGetArgs() {
 commandLine.addArg("arg1");
 commandLine.addArg("arg2");
 String[] args = commandLine.getArgs();
 assertArrayEquals(new String[]{"arg1", "arg2"}, args);
 }

 // Test for getArgList method
 @Test
 public void testGetArgList() {
 commandLine.addArg("arg1");
 commandLine.addArg("arg2");
 List argList = commandLine.getArgList();
 assertEquals(2, argList.size());
 }

 // Test for addArg method
 @Test
 public void testAddArg() {
 commandLine.addArg("arg1");
 commandLine.addArg("arg2");
 List args = commandLine.getArgList();
 assertEquals("arg1", args.get(0));
 assertEquals("arg2", args.get(1));
 }

 // Test for null option value
 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionWithNullValue() {
 Option option = new Option("i", "input", true, null);
 commandLine.addOption(option);
 }

 // Test for invalid option type
 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionWithInvalidType() {
 Option option = new Option("i", "input", true, new String[0].getClass());
 commandLine.addOption(option);
 }

 // Test for empty option values
 @Test
 public void testAddOptionWithEmptyValues() {
 Option option = new Option("i", "input", true, new String[0]);
 commandLine.addOption(option);
 commandLine.parse("--input");
 assertTrue(commandLine.hasOption("i"));
 assertNull(commandLine.getOptionValue("i"));
 }

 // Test for multiple values for the same option
 @Test
 public void testAddOptionWithMultipleValues() {
 Option option = new Option("l", "list", true, "List files");
 option.setArgs(1);
 commandLine.addOption(option);
 commandLine.parse("--list file1.txt --list file2.txt");
 String[] values = commandLine.getOptionValues("l");
 assertArrayEquals(new String[]{"file1.txt", "file2.txt"}, values);
 }

 // Test for invalid option character
 @Test(expected = IllegalArgumentException.class)
 public void testInvalidOptionCharacter() {
 Option option = new Option("#", "input", false, "Invalid option");
 commandLine.addOption(option);
 }

 // Test for invalid option long name
 @Test(expected = IllegalArgumentException.class)
 public void testInvalidOptionLongName() {
 Option option = new Option("--invalid", "input", false, "Invalid option");
 commandLine.addOption(option);
 }
}
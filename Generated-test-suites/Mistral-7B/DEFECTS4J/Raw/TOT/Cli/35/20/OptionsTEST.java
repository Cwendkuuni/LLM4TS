import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

 private Options createTestOptions() {
 Options options = new Options();

 // Test short option
 options.addOption("i", "inputFile", true, "Input file path");

 // Test long option
 options.addOption("o", "outputFile", true, "Output file path");

 // Test required option
 options.addOptionGroup(new OptionGroup("requiredOptions", "Required options"));
 options.addOption("r", "requiredOption1", true, "Required option 1");
 options.addOption("r", "requiredOption2", true, "Required option 2");

 return options;
 }

 @Test
 public void testParsingShortOptions() {
 Options options = createTestOptions();

 String[] args = {"-i", "input.txt", "-o", "output.txt"};

 try {
 options.parse(args);
 assertTrue(options.isSetOption("inputFile"));
 assertTrue(options.isSetOption("outputFile"));
 } catch (ParseException e) {
 fail("Parsing failed: " + e.getMessage());
 }
 }

 @Test
 public void testParsingLongOptions() {
 Options options = createTestOptions();

 String[] args = {"--inputFile", "input.txt", "--outputFile", "output.txt"};

 try {
 options.parse(args);
 assertTrue(options.isSetOption("inputFile"));
 assertTrue(options.isSetOption("outputFile"));
 } catch (ParseException e) {
 fail("Parsing failed: " + e.getMessage());
 }
 }

 @Test
 public void testParsingShortAndLongOptionsTogether() {
 Options options = createTestOptions();

 String[] args = {"-i", "input.txt", "--outputFile", "output.txt"};

 try {
 options.parse(args);
 assertTrue(options.isSetOption("inputFile"));
 assertTrue(options.isSetOption("outputFile"));
 } catch (ParseException e) {
 fail("Parsing failed: " + e.getMessage());
 }
 }

 @Test(expected = ParseException.class)
 public void testMissingInputFileOption() {
 Options options = createTestOptions();

 String[] args = {"--outputFile", "output.txt"};

 try {
 options.parse(args);
 fail("Parsing should have failed");
 } catch (ParseException e) {
 assertEquals("Missing required option 'i' (inputFile)", e.getMessage());
 }
 }

 @Test(expected = ParseException.class)
 public void testMissingOutputFileOption() {
 Options options = createTestOptions();

 String[] args = {"-i", "input.txt"};

 try {
 options.parse(args);
 fail("Parsing should have failed");
 } catch (ParseException e) {
 assertEquals("Missing required option 'o' (outputFile)", e.getMessage());
 }
 }

 @Test
 public void testParsingWithOptionGroup() {
 Options options = createTestOptions();

 String[] args = {"--requiredOptions", "requiredOption1", "requiredOption2", "-i", "input.txt", "--outputFile", "output.txt"};

 try {
 options.parse(args);
 assertTrue(options.isSetOption("inputFile"));
 assertTrue(options.isSetOption("outputFile"));
 assertTrue(options.isSetOption("requiredOption1"));
 assertTrue(options.isSetOption("requiredOption2"));
 } catch (ParseException e) {
 fail("Parsing failed: " + e.getMessage());
 }
 }
}
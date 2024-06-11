import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class OptionsTest {

 private Options options;

 @Before
 public void setUp() {
 options = new Options();
 }

 /**
 * Test method for {@link Options#addOption(String, String, boolean, String)}
 */
 @Test
 public void testAddOptionStringStringBooleanString() {
 Option option = new Option("o", "output", false, "output directory");
 options.addOption(option);
 Assert.assertEquals(option, options.getOption("o"));
 Assert.assertEquals("output directory", option.getDescription());
 Assert.assertFalse(option.isRequired());
 }

 /**
 * Test method for {@link Options#addOption(String, String, boolean, String)} with required option
 */
 @Test
 public void testAddOptionStringStringBooleanStringWithRequired() {
 Option option = new Option("o", "output", true, "output directory");
 options.addOption(option);
 Assert.assertEquals(option, options.getOption("o"));
 Assert.assertEquals("output directory", option.getDescription());
 Assert.assertTrue(option.isRequired());
 }

 /**
 * Test method for {@link Options#addOption(String, String, boolean, String)} with an argument
 */
 @Test
 public void testAddOptionStringStringBooleanStringWithArgument() {
 Option option = new Option("o", "output", false, "output <file>");
 options.addOption(option);
 Assert.assertEquals(option, options.getOption("o"));
 Assert.assertEquals("output <file>", option.getLongOpt());
 Assert.assertFalse(option.isRequired());
 }

 /**
 * Test method for {@link Options#addOptionGroup(OptionGroup)}
 */
 @Test
 public void testAddOptionGroup() {
 OptionGroup optionGroup = new OptionGroup("g", "group");
 Option option = new Option("o", "output", false, "output directory");
 optionGroup.addOption(option);
 options.addOptionGroup(optionGroup);
 Assert.assertEquals(option, options.getOption("g.o"));
 }

 /**
 * Test method for {@link Options#addOption(Option)}
 */
 @Test
 public void testAddOptionOption() {
 Option option = new Option("o", "output", false, "output directory");
 options.addOption(option);
 Assert.assertEquals(option, options.getOption("o"));
 Assert.assertEquals("output directory", option.getDescription());
 Assert.assertFalse(option.isRequired());
 }

 /**
 * Test method for {@link Options#getOptions()}
 */
 @Test
 public void testGetOptions() {
 Option option = new Option("o", "output", false, "output directory");
 options.addOption(option);
 List<Option> optionsList = options.getOptions();
 Assert.assertEquals(1, optionsList.size());
 Assert.assertEquals(option, optionsList.get(0));
 }

 /**
 * Test method for {@link Options#getRequiredOptions()}
 */
 @Test
 public void testGetRequiredOptions() {
 Option option = new Option("o", "output", true, "output directory");
 options.addOption(option);
 List<Option> requiredOptions = options.getRequiredOptions();
 Assert.assertEquals(1, requiredOptions.size());
 Assert.assertEquals(option, requiredOptions.get(0));
 }

 /**
 * Test method for {@link Options#hasOption(String)}
 */
 @Test
 public void testHasOptionString() {
 Option option = new Option("o", "output", false, "output directory");
 options.addOption(option);
 Assert.assertTrue(options.hasOption("o"));
 Assert.assertTrue(options.hasOption("o"));
 }

 /**
 * Test method for {@link Options#hasLongOption(String)}
 */
 @Test
 public void testHasLongOptionString() {
 Option option = new Option("o", "output", false, "output directory");
 options.addOption(option);
 Assert.assertTrue(options.hasLongOption("output"));
 Assert.assertFalse(options.hasLongOption("output2"));
 }

 /**
 * Test method for {@link Options#hasShortOption(String)}
 */
 @Test
 public void testHasShortOptionString() {
 Option option = new Option("o", "output", false, "output directory");
 options.addOption(option);
 Assert.assertTrue(options.hasShortOption("o"));
 Assert.assertFalse(options.hasShortOption("o2"));
 }

 /**
 * Test method for {@link Options#getMatchingOptions(String)}
 */
 @Test
 public void testGetMatchingOptionsString() {
 Option option1 = new Option("o", "output1", false, "output1 directory");
 Option option2 = new Option("o", "output2", false, "output2 directory");
 options.addOption(option1);
 options.addOption(option2);
 List<String> matchingOptions = options.getMatchingOptions("o");
 Assert.assertEquals(2, matchingOptions.size());
 Assert.assertEquals("output1", matchingOptions.get(0));
 Assert.assertEquals("output2", matchingOptions.get(1));
 }

 /**
 * Test method for {@link Options#parse(String[], String)}
 */
 @Test(expected = ParseException.class)
 public void testParseStringArrayStringWithInvalidOption() {
 String[] args = {"-a", "--output", "test.txt"};
 options.parse(args, "TestProgram");
 }

 /**
 * Test method for {@link Options#parse(String[], String)} with valid options
 */
 @Test
 public void testParseStringArrayStringWithValidOptions() {
 Option option = new Option("o", "output", false, "output file");
 options.addOption(option);
 String[] args = {"-o", "test.txt"};
 options.parse(args, "TestProgram");
 }

 /**
 * Test method for {@link Options#parse(String[], String)} with required options
 */
 @Test(expected = ParseException.class)
 public void testParseStringArrayStringWithMissingRequiredOption() {
 Option option = new Option("o", "output", true, "output file");
 options.addOption(option);
 String[] args = {"-a"};
 options.parse(args, "TestProgram");
 }
}
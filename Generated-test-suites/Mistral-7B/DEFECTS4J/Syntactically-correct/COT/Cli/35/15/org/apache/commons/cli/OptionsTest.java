import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class OptionsTest {

 private Options createOptionsUnderTest() {
 return new Options();
 }

 @Test
 public void testAddOption() {
 Options options = createOptionsUnderTest();

 Option option = new Option("o", "output", true, "Output path.");
 options.addOption(option);

 Assert.assertEquals(1, options.getOptions().size());
 Assert.assertEquals(option, options.getOptions().get(0));
 }

 @Test
 public void testAddOptionWithLongName() {
 Options options = createOptionsUnderTest();

 Option option = new Option("i", "input", true, "Input path.");
 options.addOption(option);

 Assert.assertEquals(1, options.getOptions().size());
 Assert.assertEquals(option, options.getOptions().get(0));
 }

 @Test
 public void testAddRequiredOption() {
 Options options = createOptionsUnderTest();

 Option option = new Option("r", "required", true, "Required option.");
 option.setRequired(true);
 options.addOption(option);

 Assert.assertEquals(1, options.getOptions().size());
 Assert.assertEquals(option, options.getOptions().get(0));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionWithDuplicateShortName() {
 Options options = createOptionsUnderTest();

 Option option1 = new Option("o", "output", true, "Output path.");
 options.addOption(option1);
 Option option2 = new Option("o", "output2", true, "Output path2.");
 options.addOption(option2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionWithDuplicateLongName() {
 Options options = createOptionsUnderTest();

 Option option1 = new Option("--output", "output", true, "Output path.");
 options.addOption(option1);
 Option option2 = new Option("--output", "output2", true, "Output path2.");
 options.addOption(option2);
 }

 @Test
 public void testAddOptionGroup() {
 Options options = createOptionsUnderTest();
 OptionGroup group = new OptionGroup("g", "group");
 options.addOptionGroup(group);

 Assert.assertEquals(0, options.getOptions().size());
 Assert.assertEquals(1, options.getOptionGroups().size());
 }

 @Test
 public void testParseWithNoOptions() throws ParseException {
 Options options = createOptionsUnderTest();
 options.parse("");
 }

 @Test(expected = ParseException.class)
 public void testParseWithInvalidOption() throws ParseException {
 Options options = createOptionsUnderTest();
 options.parse("--invalid");
 }

 @Test
 public void testParseWithShortOption() throws ParseException {
 Options options = createOptionsUnderTest();
 Option option = new Option("o", "output", true, "Output path.");
 options.addOption(option);
 options.parse("--output output.txt");
 }

 @Test
 public void testParseWithLongOption() throws ParseException {
 Options options = createOptionsUnderTest();
 Option option = new Option("--output", "output", true, "Output path.");
 options.addOption(option);
 options.parse("--output output.txt");
 }

 @Test
 public void testParseWithMixedShortAndLongOptions() throws ParseException {
 Options options = createOptionsUnderTest();
 Option option1 = new Option("o", "output", true, "Output path.");
 Option option2 = new Option("--output-file", "outputFile", true, "Output file path.");
 options.addOption(option1);
 options.addOption(option2);
 options.parse("--output output.txt --output-file outputFile.txt");
 }
}
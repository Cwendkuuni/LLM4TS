import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

 private Options options;

 @Before
 public void setUp() {
 options = new Options();
 options.addOption("a", "Test option A", true, "Test option A description");
 options.addOption("b", "Test option B", false, "Test option B description");
 options.addOption("c", "Test option C", false, "Test option C description");
 options.addOptionGroup(new OptionGroup("group1", "Test option group 1",
 new Option("g1a", "Test group option A", true, "Test group option A description")));
 options.addOptionGroup(new OptionGroup("group2", "Test option group 2",
 new Option("g2a", "Test group option A", true, "Test group option A description")));
 }

 @Test
 public void testAddOptionWithShortNameAndDescription() {
 Option optA = options.getOption("a");
 assertEquals("Test option A", optA.getLongOpt());
 assertEquals("Test option A description", optA.getDescription());
 assertTrue(optA.isRequired());
 }

 @Test
 public void testAddOptionWithShortNameAndNoDescription() {
 Option optB = options.getOption("b");
 assertEquals("b", optB.getKey());
 assertNull(optB.getLongOpt());
 assertFalse(optB.isRequired());
 }

 @Test
 public void testAddOptionWithShortAndLongNames() {
 Option optC = options.getOption("c");
 assertEquals("c", optC.getKey());
 assertEquals("Test option C description", optC.getDescription());
 assertFalse(optC.isRequired());
 }

 @Test
 public void testAddOptionGroup() {
 OptionGroup group1 = options.getOptionGroup("group1");
 OptionGroup group2 = options.getOptionGroup("group2");
 assertEquals("Test option group 1", group1.getName());
 assertEquals("Test option group 2", group2.getName());
 }

 @Test(expected = ParseException.class)
 public void testParsingWithMissingRequiredOption() {
 options.parse("--test");
 fail("Expected ParseException when missing required option");
 }

 @Test
 public void testParsingWithOptionalOptions() {
 String[] args = {"--test", "--optionB"};
 options.parse(args);
 assertTrue(options.isSetOption("b"));
 }

 @Test
 public void testParsingWithShortOptionAndArgument() {
 String[] args = {"--testA", "arg"};
 options.parse(args);
 assertEquals("arg", options.getOptionValue("a"));
 }

 @Test
 public void testParsingWithLongOptionAndArgument() {
 String[] args = {"--testA-description", "arg"};
 options.parse(args);
 assertEquals("arg", options.getOptionValue("a"));
 }

 @Test
 public void testParsingWithGroupOptionAndArgument() {
 String[] args = {"--group1", "--g1a", "arg"};
 OptionGroup group1 = options.getOptionGroup("group1");
 Option optionG1A = group1.getOption("g1a");
 assertEquals("arg", optionG1A.getValue());
 }
}
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class OptionsTest {

 private Options opts;

 @Before
 public void setUp() {
 opts = new Options();
 }

 @Test
 public void testAddOption() {
 opts.addOption("a", "test", false, "Test option A");
 Option opt = opts.getOption("a");
 Assert.assertEquals("test", opt.getLongOpt());
 Assert.assertFalse(opt.hasArg());
 }

 @Test
 public void testAddOptionWithArg() {
 opts.addOption("a", null, true, "Test option A with arg");
 Option opt = opts.getOption("a");
 Assert.assertNull(opt.getLongOpt());
 Assert.assertTrue(opt.hasArg());
 }

 @Test
 public void testAddOptionWithLongName() {
 opts.addOption("a", "test-long", false, "Test option A with long name");
 Option opt = opts.getOption("test-long");
 Assert.assertEquals("test-long", opt.getLongOpt());
 }

 @Test
 public void testAddOptionGroup() {
 OptionGroup group = new OptionGroup("TestGroup", "Test group options");
 opts.addOptionGroup(group);
 Assert.assertEquals("TestGroup", group.getName());
 }

 @Test
 public void testGetOptions() {
 List<Option> options = opts.getOptions();
 Assert.assertFalse(options.isEmpty());
 }

 @Test
 public void testHasOption() {
 opts.addOption("a", "test", false, "Test option A");
 Assert.assertTrue(opts.hasOption("a"));
 }

 @Test
 public void testHasLongOption() {
 opts.addOption("a", "test-long", false, "Test option A with long name");
 Assert.assertTrue(opts.hasLongOption("test-long"));
 }

 @Test
 public void testHasShortOption() {
 opts.addOption("a", "test", false, "Test option A");
 Assert.assertTrue(opts.hasShortOption("a"));
 }

 @Test(expected = ParseException.class)
 public void testParseExceptionWithMissingOption() {
 opts.parse("-b");
 }

 @Test(expected = ParseException.class)
 public void testParseExceptionWithMissingLongOption() {
 opts.parse("--test-long");
 }

 @Test(expected = ParseException.class)
 public void testParseExceptionWithMissingShortOption() {
 opts.parse("-a");
 }

 @Test(expected = ParseException.class)
 public void testParseExceptionWithMissingArg() {
 opts.addOption("a", null, true, "Test option A with arg");
 opts.parse("-a");
 }

 @Test(expected = ParseException.class)
 public void testParseExceptionWithExtraArg() {
 opts.addOption("a", null, true, "Test option A with arg");
 opts.parse("-a arg arg");
 }

 @Test(expected = ParseException.class)
 public void testParseExceptionWithInvalidOption() {
 opts.parse("--invalid");
 }

}
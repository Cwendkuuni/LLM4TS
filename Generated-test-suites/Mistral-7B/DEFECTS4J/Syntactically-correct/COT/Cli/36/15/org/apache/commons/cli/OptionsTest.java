import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

 private Options createOptions() {
 return new Options()
 .addOption("a", "alpha", true, "Option A")
 .addOption("b", "beta", false, "Option B")
 .addOption("c", "char", "c", "Option C with argument")
 .addOptionGroup(new OptionGroup("g", "group", "Group options"))
 .addOption("d", "delta", true, "Option D")
 .addOption("e", "epsilon", false, "Option E")
 .addOption("f", "flag", false, "Option F")
 .addOptionGroup(new OptionGroup("h", "help", "Help options")
 .addOption("i", "info", true, "Option I")
 .addOption("j", "justice", false, "Option J")
 .addOption("k", "kangaroo", false, "Option K")
 .addOption("l", "lion", false, "Option L")
 );
 }

 @Test
 public void testAddOption() {
 Options options = createOptions();
 Option optionA = options.getOption("a");
 Option optionB = options.getOption("b");
 Option optionC = options.getOption("c");
 Option optionD = options.getOption("d");
 Option optionE = options.getOption("e");
 Option optionF = options.getOption("f");
 Option optionGroupG = options.getOptionGroup("g");
 Option optionGroupH = options.getOptionGroup("h");

 // Basic functionality test
 assertEquals("Alpha", optionA.getLongOpt());
 assertTrue(optionA.isRequired());
 assertFalse(optionA.hasArg());
 assertEquals("Option A", optionA.getDescription());

 assertEquals("Beta", optionB.getLongOpt());
 assertFalse(optionB.isRequired());
 assertFalse(optionB.hasArg());
 assertEquals("Option B", optionB.getDescription());

 assertEquals('c', optionC.getOpt());
 assertTrue(optionC.isRequired());
 assertTrue(optionC.hasArg());
 assertEquals("Option C with argument", optionC.getDescription());

 assertEquals("Delta", optionD.getLongOpt());
 assertTrue(optionD.isRequired());
 assertFalse(optionD.hasArg());
 assertEquals("Option D", optionD.getDescription());

 assertNull(optionE);

 assertEquals("group", optionGroupG.getName());
 assertTrue(optionGroupG.isRequired());

 assertEquals("help", optionGroupH.getName());
 assertFalse(optionGroupH.isRequired());
 }

 @Test(expected = NullPointerException.class)
 public void testAddNullOption() {
 Options options = new Options();
 options.addOption(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddDuplicateOption() {
 Options options = createOptions();
 options.addOption("a", "alpha", true, "Option A");
 options.addOption("a", "alpha2", true, "Option A2");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionWithInvalidDescription() {
 Options options = new Options();
 options.addOption("a", "alpha", true, "");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionWithInvalidHasArg() {
 Options options = new Options();
 options.addOption("a", "alpha", true, "Option A")
 .addOption("b", "beta", 1, "Option B");
 }

 @Test
 public void testHasOption() {
 Options options = createOptions();
 assertTrue(options.hasOption("a"));
 assertTrue(options.hasOption("c"));
 assertTrue(options.hasOption("d"));
 assertTrue(options.hasOption("g"));
 assertTrue(options.hasOption("h"));
 assertFalse(options.hasOption("x"));
 }

 @Test
 public void testHasLongOption() {
 Options options = createOptions();
 assertTrue(options.hasLongOption("alpha"));
 assertTrue(options.hasLongOption("char"));
 assertTrue(options.hasLongOption("group"));
 assertTrue(options.hasLongOption("help"));
 assertFalse(options.hasLongOption("x"));
 }

 @Test
 public void testHasShortOption() {
 Options options = createOptions();
 assertTrue(options.hasShortOption("a"));
 assertTrue(options.hasShortOption("c"));
 assertFalse(options.hasShortOption("x"));
 }

 @Test
 public void testGetOption() {
 Options options = createOptions();
 Option optionA = options.getOption("a");
 Option optionB = options.getOption("b");
 Option optionC = options.getOption("c");
 Option optionD = options.getOption("d");
 Option optionGroupG = options.getOptionGroup("g");
 Option optionGroupH = options.getOptionGroup("h");
 Option optionI = options.getOption("i");
 Option optionJ = options.getOption("j");
 Option optionK = options.getOption("k");
 Option optionL = options.getOption("l");

 assertEquals("Alpha", optionA.getLongOpt());
 assertNull(optionA.getOpt());
 assertFalse(optionA.isRequired());
 assertFalse(optionA.hasArg());
 assertEquals("Option A", optionA.getDescription());

 assertEquals("Beta", optionB.getLongOpt());
 assertNull(optionB.getOpt());
 assertFalse(optionB.isRequired());
 assertFalse(optionB.hasArg());
 assertEquals("Option B", optionB.getDescription());

 assertEquals('c', optionC.getOpt());
 assertNull(optionC.getLongOpt());
 assertFalse(optionC.isRequired());
 assertFalse(optionC.hasArg());
 assertEquals("Option C with argument", optionC.getDescription());

 assertEquals("Delta", optionD.getLongOpt());
 assertNull(optionD.getOpt());
 assertFalse(optionD.isRequired());
 assertFalse(optionD.hasArg());
 assertEquals("Option D", optionD.getDescription());

 assertEquals("group", optionGroupG.getName());
 assertNull(optionGroupG.getOpt());
 assertFalse(optionGroupG.isRequired());

 assertEquals("help", optionGroupH.getName());
 assertNull(optionGroupH.getOpt());
 assertFalse(optionGroupH.isRequired());

 assertNull(optionI);
 assertNull(optionJ);
 assertNull(optionK);
 assertNull(optionL);
 }

 @Test
 public void testGetMatchingOptions() {
 Options options = createOptions();
 List<String> optionsMatchingAlpha = options.getMatchingOptions("alpha");
 List<String> optionsMatchingBeta = options.getMatchingOptions("beta");
 List<String> optionsMatchingGroup = options.getMatchingOptions("group");
 List<String> optionsMatchingHelp = options.getMatchingOptions("help");
 List<String> optionsMatchingX = options.getMatchingOptions("x");

 assertEquals(1, optionsMatchingAlpha.size());
 assertEquals("alpha", optionsMatchingAlpha.get(0));

 assertEquals(1, optionsMatchingBeta.size());
 assertEquals("beta", optionsMatchingBeta.get(0));

 assertEquals(1, optionsMatchingGroup.size());
 assertEquals("group", optionsMatchingGroup.get(0));

 assertEquals(2, optionsMatchingHelp.size());
 assertEquals("help", optionsMatchingHelp.get(0));
 assertEquals("i", optionsMatchingHelp.get(1));

 assertEquals(0, optionsMatchingX.size());
 }
}
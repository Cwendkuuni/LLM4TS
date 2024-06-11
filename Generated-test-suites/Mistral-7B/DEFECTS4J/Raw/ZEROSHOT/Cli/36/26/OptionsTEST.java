import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OptionsTest {

 private Options options;

 @Before
 public void setUp() {
 options = new Options();
 }

 @After
 public void tearDown() {
 options = null;
 }

 @Test
 public void testAddOption() {
 Option option = new Option("o", "output", true, "output file");
 options.addOption(option);
 List<Option> actualOptions = options.helpOptions();
 Option actualOption = actualOptions.get(0);

 Assert.assertEquals("o", actualOption.getKey());
 Assert.assertEquals("output", actualOption.getLongOpt());
 Assert.assertEquals(true, actualOption.hasArg());
 Assert.assertEquals("output file", actualOption.getDescription());
 }

 @Test
 public void testAddOptionWithLongName() {
 Option option = new Option("o", "output-file", true, "output file");
 options.addOption(option);
 List<Option> actualOptions = options.helpOptions();
 Option actualOption = actualOptions.get(0);

 Assert.assertEquals("output-file", actualOption.getLongOpt());
 }

 @Test
 public void testAddRequiredOption() {
 Option option = new Option("o", "output", true, "output file");
 options.addOption(option);
 options.addOptionGroup(new OptionGroup("g", "group", false, "Test group"));
 options.addOptionGroup(new OptionGroup("g2", "group2", false, "Test group2"));
 options.addOption("h", "help", false, "Print help");
 options.addOption("h", "help", false, "Print help"); // should not be added as it is already required

 List<Object> requiredOptions = options.getRequiredOptions();
 List<Option> actualOptions = options.helpOptions();

 Assert.assertEquals(3, requiredOptions.size());
 Assert.assertEquals(4, actualOptions.size());
 Assert.assertEquals("output", actualOptions.get(0).getKey());
 Assert.assertEquals("output", actualOptions.get(1).getKey());
 Assert.assertEquals("help", actualOptions.get(2).getKey());
 Assert.assertEquals("help", actualOptions.get(3).getKey());
 }

 @Test
 public void testAddOptionGroup() {
 OptionGroup group = new OptionGroup("g", "group", false, "Test group");
 Option option = new Option("gopt", "group-opt", true, "Option in group-opt");
 group.addOption(option);
 options.addOptionGroup(group);

 List<Option> actualOptions = options.helpOptions();
 Option actualOption = actualOptions.get(3);

 Assert.assertEquals("group-opt", actualOption.getLongOpt());
 Assert.assertEquals("option in group-opt", actualOption.getDescription());
 Assert.assertTrue(group.isRequired());
 }

 @Test
 public void testHasOption() {
 options.addOption("o", "output", true, "output file");

 Assert.assertTrue(options.hasOption("o"));
 Assert.assertTrue(options.hasOption("output"));
 Assert.assertFalse(options.hasOption("n"));
 }

 @Test
 public void testGetOption() {
 Option option = new Option("o", "output", true, "output file");
 options.addOption(option);

 Option actualOption = options.getOption("output");

 Assert.assertEquals("output", actualOption.getKey());
 Assert.assertEquals("output", actualOption.getLongOpt());
 Assert.assertEquals(true, actualOption.hasArg());
 Assert.assertEquals("output file", actualOption.getDescription());
 }

 @Test
 public void testGetMatchingOptions() {
 options.addOption("o", "output", true, "output file");
 options.addOption("io", "input-output", false, "Input and output files");
 options.addOption("i", "input", true, "Input file");

 List<String> matchingOptions = options.getMatchingOptions("i");

 Assert.assertEquals(1, matchingOptions.size());
 Assert.assertEquals("input", matchingOptions.get(0));
 }

 @Test
 public void testToString() {
 options.addOption("o", "output", true, "output file");

 StringtoString = options.toString();

 Assert.assertTrue(toString.contains("Options: [ short [o] ] [ long [] ]"));
 }
}
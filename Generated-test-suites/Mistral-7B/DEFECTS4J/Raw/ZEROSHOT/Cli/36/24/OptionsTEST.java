import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class OptionsTest {

 @Before
 public void setUp() {
 }

 @After
 public void tearDown() {
 }

 @Test
 public void testAddOptionWithShortName() {
 Options options = new Options();
 options.addOption("a", "optionA", false, "Option A");
 assertEquals(1, options.getOptions().size());
 assertEquals("Option A", options.getOptions().get(0).getLongOpt());
 assertEquals("a", options.getOptions().get(0).getKey());
 }

 @Test
 public void testAddOptionWithShortAndLongName() {
 Options options = new Options();
 options.addOption("a", "optionA", false, "Option A");
 options.addOption("l", "optionL", false, "Option L");
 options.addOption("al", "optionAL", false, "Option AL");
 assertEquals(3, options.getOptions().size());
 assertEquals("Option A", options.getOptions().get(0).getLongOpt());
 assertEquals("a", options.getOptions().get(0).getKey());
 assertEquals("Option L", options.getOptions().get(1).getLongOpt());
 assertEquals("l", options.getOptions().get(1).getKey());
 assertEquals("Option AL", options.getOptions().get(2).getLongOpt());
 assertEquals("al", options.getOptions().get(2).getKey());
 }

 @Test
 public void testAddOptionWithShortNameAndValue() {
 Options options = new Options();
 options.addOption("a", "optionA", true, "Option A");
 assertEquals(1, options.getOptions().size());
 assertEquals("optionA", options.getOptions().get(0).getLongOpt());
 assertEquals("a", options.getOptions().get(0).getKey());
 }

 @Test
 public void testAddOptionWithShortNameAndValueAndDescription() {
 Options options = new Options();
 options.addOption("a", "optionA", true, "Option A with value and description");
 assertEquals(1, options.getOptions().size());
 assertEquals("optionA", options.getOptions().get(0).getLongOpt());
 assertEquals("a", options.getOptions().get(0).getKey());
 assertEquals("Option A with value and description", options.getOptions().get(0).getDescription());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionWithDuplicateShortKey() {
 Options options = new Options();
 options.addOption("a", "optionA", false, "Option A");
 options.addOption("a", "optionB", false, "Option B");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionWithDuplicateLongKey() {
 Options options = new Options();
 options.addOption("a", "optionA", false, "Option A");
 options.addOption("l:a", "optionA", false, "Option A with long option");
 }

 @Test
 public void testAddOptionGroup() {
 Options options = new Options();
 OptionGroup optionGroup = new OptionGroup("group", "Test Group");
 options.addOptionGroup(optionGroup);
 optionGroup.addOption(new Option("g", null, false, "Group option"));
 assertEquals(1, options.getOptions().size());
 assertEquals(1, optionGroup.getOptions().size());
 }

 @Test
 public void testGetMatchingOptions() {
 Options options = new Options();
 options.addOption("opt1", "option1");
 options.addOption("opt2", "option2");
 options.addOption("opt3", "option3");
 options.addOption("opt4", "option4");

 List<String> matchingOptions = options.getMatchingOptions("opt");
 assertEquals(3, matchingOptions.size());
 assertEquals("opt1", matchingOptions.get(0));
 assertEquals("opt2", matchingOptions.get(1));
 assertEquals("opt3", matchingOptions.get(2));
 }

 @Test
 public void testHasOption() {
 Options options = new Options();
 options.addOption("a", "optionA", false, "Option A");

 assertTrue(options.hasOption("a"));
 assertTrue(options.hasOption("a"));
 assertTrue(options.hasOption("A"));
 assertTrue(options.hasOption("a "));
 assertTrue(options.hasOption("a--"));
 assertTrue(options.hasOption("--a"));
 assertFalse(options.hasOption("b"));
 }

 @Test
 public void testParse() throws ParseException {
 Options options = new Options();
 options.addOption("a", "optionA", true, "Option A");

 CommandLineParser parser = new DefaultParser();
 String[] args = new String[]{"--optionA", "value"};
 CommandLine cmd = parser.parse(options, args);
 assertEquals("value", cmd.getOptionValue("optionA"));
 }
}
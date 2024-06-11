package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;

public class OptionsTest {

 private Options options;

 @Before
 public void setup() {
 options = new Options();
 }

 // Test for addOption(String opt, String description) method
 @Test
 public void testAddOption() {
 Option opt = options.addOption("a", "option-a", false, "description-a");
 assertEquals("option-a", opt.getKey());
 assertEquals("description-a", opt.getDescription());
 assertFalse(opt.hasArg());
 }

 // Test for addOption(String opt, boolean hasArg, String description) method
 @Test
 public void testAddOptionWithArg() {
 Option opt = options.addOption("b", "option-b", true, "description-b");
 assertEquals("option-b", opt.getKey());
 assertEquals("description-b", opt.getDescription());
 assertTrue(opt.hasArg());
 }

 // Test for addOption(String opt, String longOpt, boolean hasArg, String description) method
 @Test
 public void testAddOptionWithLongOpt() {
 Option opt = options.addOption("c", "option-c", true, "description-c", "loption-c");
 assertEquals("option-c", opt.getKey());
 assertEquals("description-c", opt.getDescription());
 assertEquals("loption-c", opt.getLongOpt());
 assertTrue(opt.hasArg());
 }

 // Test for addOptionGroup(OptionGroup group) method
 @Test
 public void testAddOptionGroup() {
 OptionGroup group = new OptionGroup();
 group.setName("group-name");
 Option opt = group.addOption("group-opt", false, "description-group-opt");
 group.addOption(opt);
 options.addOptionGroup(group);
 Option groupOpt = options.getOption("group-opt");
 assertEquals("group-name", groupOpt.getLongOpt());
 assertTrue(groupOpt.isVisible());
 }

 // Test for hasOption(String opt) method
 @Test
 public void testHasOption() {
 Option opt = options.addOption("test", false, "description-test");
 assertTrue(options.hasOption("test"));
 }

 // Test for getOption(String opt) method
 @Test
 public void testGetOption() {
 Option opt = options.addOption("test", false, "description-test");
 Option result = options.getOption("test");
 assertEquals(opt, result);
 }

 // Test for getMatchingOptions(String opt) method
 @Test
 public void testGetMatchingOptions() {
 Option opt1 = options.addOption("test1", false, "description-test1");
 Option opt2 = options.addOption("test2", false, "description-test2");
 Option opt3 = options.addOption("test3", false, "description-test3");
 List<String> result = options.getMatchingOptions("test");
 assertEquals(2, result.size());
 assertTrue(result.contains("test1"));
 assertTrue(result.contains("test2"));
 }

 // Test for getMatchingOptions(String opt) method with no result
 @Test
 public void testGetMatchingOptionsNoResult() {
 Option opt1 = options.addOption("test1", false, "description-test1");
 Option opt2 = options.addOption("test2", false, "description-test2");
 List<String> result = options.getMatchingOptions("test3");
 assertTrue(result.isEmpty());
 }

 // Test for getRequiredOptions() method
 @Test
 public void testGetRequiredOptions() {
 Option opt = options.addOption("required", true, "description-required");
 options.addOptionGroup(new OptionGroup());
 List<Option> result = options.getRequiredOptions();
 assertEquals(1, result.size());
 assertEquals(opt, result.get(0));
 }

}
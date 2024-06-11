package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

 @Test
 public void testAddOption() {
 Options options = new Options();
 Option option = new Option("o", "output", true, "Sets the output file");
 options.addOption(option);
 assertNotNull(options.getOption("o"));
 assertEquals("Sets the output file", option.getDescription());
 }

 @Test
 public void testAddOptionWithValue() {
 Options options = new Options();
 Option option = new Option("o", "output", true, "Sets the output file [VALUE]");
 options.addOption(option);
 assertEquals(1, options.getOptions().size());
 }

 @Test
 public void testAddOptionGroup() {
 Options options = new Options();
 OptionGroup group = new OptionGroup("g", "Group");
 Option option = new Option("o", "output", true, "Sets the output file");
 group.addOption(option);
 options.addOptionGroup(group);
 assertNotNull(options.getOption("o"));
 assertEquals("Sets the output file", option.getDescription());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddDuplicateOptionGroup() {
 Options options = new Options();
 OptionGroup group = new OptionGroup("g", "Group");
 options.addOptionGroup(group);
 options.addOptionGroup(group);
 }

 @Test(expected = NullPointerException.class)
 public void testAddNullOption() {
 Options options = new Options();
 Option option = null;
 assertFalse(options.addOption(option));
 }

 @Test
 public void testParseRequiredOption() throws ParseException {
 Options options = new Options();
 Option option = new Option("o", "output", true, "Sets the output file");
 options.addOption(option);
 String[] args = new String[]{"-o", "test.txt"};
 Options parsedOptions = new Options();
 parsedOptions.parse(args, null);
 assertEquals("test.txt", parsedOptions.getOptionValue("o"));
 }

 @Test(expected = ParseException.class)
 public void testParseMissingRequiredOption() throws ParseException {
 Options options = new Options();
 Option option = new Option("o", "output", true, "Sets the output file");
 options.addOption(option);
 String[] args = new String[]{"-h"};
 options.parse(args, null);
 }

 @Test
 public void testParseMultipleOptions() throws ParseException {
 Options options = new Options();
 Option option1 = new Option("o", "output1", true, "Sets the output file 1");
 Option option2 = new Option("o", "output2", true, "Sets the output file 2");
 options.addOption(option1);
 options.addOption(option2);
 String[] args = new String[]{"-o1", "output1.txt", "-o2", "output2.txt"};
 Options parsedOptions = new Options();
 parsedOptions.parse(args, null);
 assertEquals("output1.txt", parsedOptions.getOptionValue("output1"));
 assertEquals("output2.txt", parsedOptions.getOptionValue("output2"));
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidOption() throws ParseException {
 Options options = new Options();
 Option option = new Option("o", "output", true, "Sets the output file");
 options.addOption(option);
 String[] args = new String[]{"-x"};
 options.parse(args, null);
 }

 @Test(expected = ParseException.class)
 public void testParseOptionWithValueTwice() throws ParseException {
 Options options = new Options();
 Option option = new Option("o", "output", true, "Sets the output file [VALUE]");
 options.addOption(option);
 String[] args = new String[]{"-o", "output.txt", "-o", "test.txt"};
 options.parse(args, null);
 }

 @Test(expected = ParseException.class)
 public void testParseLongOptionWithShortOption() throws ParseException {
 Options options = new Options();
 Option option1 = new Option("o", "output1", true, "Sets the output file 1");
 Option option2 = new Option("o", "output2", true, "Sets the output file 2");
 options.addOption(option1);
 options.addOption(option2);
 String[] args = new String[]{"-o1", "output1.txt", "--output2", "test.txt"};
 options.parse(args, null);
 }

 @Test
 public void testParseOptionGroup() throws ParseException {
 Options options = new Options();
 OptionGroup group = new OptionGroup("g", "Group");
 Option option = new Option("o", "output", true, "Sets the output file");
 group.addOption(option);
 options.addOptionGroup(group);
 String[] args = new String[]{"-g", "--output", "test.txt"};
 Options parsedOptions = new Options();
 parsedOptions.parse(args, null);
 assertEquals("test.txt", parsedOptions.getOptionValue("output"));
 }

 @Test(expected = ParseException.class)
 public void testParseOptionGroupWithoutGroup() throws ParseException {
 Options options = new Options();
 Option option = new Option("o", "output", true, "Sets the output file");
 String[] args = new String[]{"--output", "test.txt"};
 options.parse(args, null);
 }

 @Test(expected = ParseException.class)
 public void testParseOptionGroupWithInvalidName() throws ParseException {
 Options options = new Options();
 Option option = new Option("o", "output", true, "Sets the output file");
 OptionGroup group = new OptionGroup("x", "Group");
 group.addOption(option);
 String[] args = new String[]{"--x", "--output", "test.txt"};
 options.parse(args, null);
 }

 @Test(expected = ParseException.class)
 public void testParseOptionGroupWithEmptyName() throws ParseException {
 Options options = new Options();
 Option option = new Option("o", "output", true, "Sets the output file");
 OptionGroup group = new OptionGroup("", "Group");
 group.addOption(option);
 String[] args = new String[]{"--", "--output", "test.txt"};
 options.parse(args, null);
 }
}
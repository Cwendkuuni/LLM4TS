package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Properties;

@RunWith(Parameterized.class)
public class ParserTest {

 private Parser parser;
 private Options options;
 private String[] arguments;
 private Properties properties;
 private boolean stopAtNonOption;

 @Parameterized.Parameters
 public static List<Object[]> data() {
 return Arrays.asList(new Object[][]{
 // Parser with no options
 new Object[]{new DefaultParser(), new String[0], null, null},
 // Parser with options
 new Object[]{new DefaultParser(), new String[]{"-a", "--optionB", "-o", "optionC"}, new Properties(), false},
 // Parser with properties
 new Object[]{new DefaultParser(), new String[]{"-a", "--optionB", "-o", "optionC"}, new Properties().put("optionA", "valueA"), false},
 // Parser with stopAtNonOption
 new Object[]{new DefaultParser(), new String[]{"-a", "--optionB", "-o", "optionC"}, new Properties(), true},
 // Parser with invalid arguments
 new Object[]{new DefaultParser(), new String[]{"-a", "--optionB", "-o", "optionC", "invalid"}, null, false}
 });
 }

 @Parameterized.Constructor
 public ParserTest(Parser parser, String[] arguments, Properties properties, boolean stopAtNonOption) {
 this.parser = parser;
 this.arguments = arguments;
 this.properties = properties;
 this.stopAtNonOption = stopAtNonOption;
 }

 @Test
 public void testParse() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "Short option A");
 options.addOption("optionB", null, "Long option B", "B");
 options.addOption("o", true, "Short option O");
 options.addOptionGroup(new OptionGroup("optionGroup", "Option group"));
 options.addOptionGroup(new OptionGroup("optionGroup2", "Option group 2"));
 options.addOption("c", null, "Long option C", "C");
 options.addOption("d", null, "Long option D", "D");

 parser.setOptions(options);

 CommandLine commandLine = parser.parse(options, arguments, properties, stopAtNonOption);

 assertEquals(options.getOptionNames().size(), commandLine.getOptions().size());
 assertFalse(commandLine.getArgs().isEmpty());

 for (Option opt : commandLine.getOptions()) {
 String optionKey = opt.getKey();
 Option option = options.findOption(optionKey);
 assertSame(option, opt);
 if (option.isRequired()) {
 assertTrue(commandLine.hasOption(optionKey));
 }
 }

 for (String arg : commandLine.getArgs()) {
 assertNull(options.findOption(arg));
 }
 }

 @Test(expected = MissingOptionException.class)
 public void testMissingRequiredOptions() throws ParseException {
 options.addOption("a", true, "Short option A");
 options.addOption("optionB", null, "Long option B", "B");
 options.addOption("o", true, "Short option O");
 options.addOption("c", null, "Long option C", "C");
 options.addOption("d", null, "Long option D", "D");
 options.addOptionGroup(new OptionGroup("optionGroup", "Option group"));
 options.addOptionGroup(new OptionGroup("optionGroup2", "Option group 2"));

 requiredOptions(options, "a", "c");

 parser.setOptions(options);

 try {
 parser.parse(options, new String[0], null, false);
 } catch (MissingOptionException e) {
 assertTrue(Arrays.asList(e.getMissingOptions().toArray(new String[0])).containsAll(Collections.singletonList("c")));
 }
 }

 @Test(expected = MissingOptionException.class)
 public void testMissingRequiredGroup() throws ParseException {
 options.addOption("a", true, "Short option A");
 options.addOption("optionB", null, "Long option B", "B");
 options.addOption("o", true, "Short option O");
 options.addOptionGroup(new OptionGroup("optionGroup", "Option group"));
 options.addOptionGroup(new OptionGroup("optionGroup2", "Option group 2"));
 options.addOption("c", null, "Long option C", "C");
 options.addOption("d", null, "Long option D", "D");

 requiredOptions(options, "optionGroup");

 parser.setOptions(options);

 try {
 parser.parse(options, new String[0], null, false);
 } catch (MissingOptionException e) {
 assertTrue(Arrays.asList(e.getMissingOptions().toArray(new String[0])).containsAll(Collections.singletonList("optionGroup")));
 }
 }

 @Test
 public void testParseWithProperties() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "Short option A");
 options.addOption("optionB", null, "Long option B", "B");
 options.addOption("o", true, "Short option O");
 options.addOption("c", null, "Long option C", "C");
 options.addOption("d", null, "Long option D", "D");
 options.addOptionGroup(new OptionGroup("optionGroup", "Option group"));
 options.addOptionGroup(new OptionGroup("optionGroup2", "Option group 2"));

 parser.setOptions(options);

 CommandLine commandLine = parser.parse(options, arguments, properties, stopAtNonOption);
 List<String> expectedOptions = Arrays.asList("optionA=valueA");

 assertEquals(expectedOptions.size(), commandLine.getOptions().size());

 for (Option opt : commandLine.getOptions()) {
 Option option = options.findOption(opt.getKey());
 String optionValue = properties.getProperty(option.getKey());
 if (option.isRequired()) {
 assertTrue(commandLine.hasOption(option.getKey()));
 }

 assertEquals(optionValue, opt.getValue());
 }
 }

 @Test(expected = ParseException.class)
 public void testParseWithInvalidOption() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "Short option A");
 options.addOption("optionB", null, "Long option B", "B");
 options.addOption("o", true, "Short option O");
 options.addOption("c", null, "Long option C", "C");
 options.addOption("d", null, "Long option D", "D");
 options.addOptionGroup(new OptionGroup("optionGroup", "Option group"));
 options.addOptionGroup(new OptionGroup("optionGroup2", "Option group 2"));

 parser.setOptions(options);

 try {
 parser.parse(options, arguments, null, false);
 fail("Expected ParseException when parsing invalid argument");
 } catch (ParseException e) {
 // expected
 }
 }

 @Test
 public void testParseWithStopAtNonOption() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "Short option A");
 options.addOption("optionB", null, "Long option B", "B");
 options.addOption("o", true, "Short option O");
 options.addOption("c", null, "Long option C", "C");
 options.addOption("d", null, "Long option D", "D");
 options.addOptionGroup(new OptionGroup("optionGroup", "Option group"));
 options.addOptionGroup(new OptionGroup("optionGroup2", "Option group 2"));

 parser.setOptions(options);

 CommandLine commandLine = parser.parse(options, arguments, null, true);

 assertFalse(commandLine.getArgs().isEmpty());
 for (String arg : commandLine.getArgs()) {
 assertNull(options.findOption(arg));
 }
 }

 private static void requiredOptions(Options options, String... optionsToRequire) {
 for (String option : optionsToRequire) {
 if (options.getOption(option) != null && !options.getOption(option).isRequired()) {
 options.getOption(option).setRequired(true);
 }
 }
 }
}
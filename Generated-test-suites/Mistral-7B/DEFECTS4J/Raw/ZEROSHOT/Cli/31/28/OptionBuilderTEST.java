package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;

public class OptionBuilderTest {

 @Test
 public void testOptionBuilderWithDefaultValues() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option option = optionBuilder.create('A');

 assertEquals(null, option.getLongOpt());
 assertEquals("arg", option.getArgName());
 assertFalse(option.isRequired());
 assertEquals(Option.UNINITIALIZED, option.getArgs());
 assertNull(option.getType());
 assertFalse(option.isOptionalArg());
 assertNull(option.getValueSeparator());

 }

 @Test
 public void testOptionBuilderWithCustomValues() {
 OptionBuilder optionBuilder = new OptionBuilder();
 optionBuilder.withLongOpt("longOptA")
 .withArgName("argNameA")
 .isRequired()
 .hasArg()
 .withValueSeparator(':')
 .hasOptionalArg();
 Option option = optionBuilder.create('A');

 assertEquals("longOptA", option.getLongOpt());
 assertEquals("argNameA", option.getArgName());
 assertTrue(option.isRequired());
 assertEquals(1, option.getArgs());
 assertEquals(null, option.getType());
 assertTrue(option.isOptionalArg());
 assertEquals(':', option.getValueSeparator());
 }

 @Test
 public void testOptionBuilderWithCustomValuesAndType() {
 OptionBuilder optionBuilder = new OptionBuilder();
 optionBuilder.withLongOpt("longOptB")
 .withArgName("argNameB")
 .isRequired()
 .hasArg()
 .withValueSeparator(':')
 .hasOptionalArg()
 .withType(Integer.class);
 Option option = optionBuilder.create('B');

 assertEquals("longOptB", option.getLongOpt());
 assertEquals("argNameB", option.getArgName());
 assertTrue(option.isRequired());
 assertEquals(1, option.getArgs());
 assertEquals(Integer.class, option.getType());
 assertTrue(option.isOptionalArg());
 assertEquals(':', option.getValueSeparator());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOptionBuilderWithMissingLongOpt() {
 OptionBuilder optionBuilder = new OptionBuilder();
 optionBuilder.isRequired();
 Option option = optionBuilder.create(' ');
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOptionBuilderWithInvalidNumberOfArgs() {
 OptionBuilder optionBuilder = new OptionBuilder();
 optionBuilder.withLongOpt("longOptC")
 .isRequired()
 .hasArgs(-1);
 Option option = optionBuilder.create('C');
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOptionBuilderWithInvalidNumberOfOptionalArgs() {
 OptionBuilder optionBuilder = new OptionBuilder();
 optionBuilder.withLongOpt("longOptD")
 .isRequired()
 .hasOptionalArgs(-1);
 Option option = optionBuilder.create('D');
 }

 @Test
 public void testParsingOptions() {
 String[] args = new String[]{"-optionA", "valueA", "-optionB", "valueB", "-optionC:valueC"};
 OptionParser parser = new DefaultParser();
 Options options = new Options();
 OptionBuilder optionBuilderA = new OptionBuilder();
 OptionBuilder optionBuilderB = new OptionBuilder();
 OptionBuilder optionBuilderC = new OptionBuilder();
 optionBuilderA.hasArg();
 Option optionA = optionBuilderA.create('A');
 options.addOption(optionA);
 optionBuilderB.hasArg();
 optionBuilderB.withDescription("An option with description");
 Option optionB = optionBuilderB.create('B');
 options.addOption(optionB);
 optionBuilderC.hasOptionalArg();
 optionBuilderC.withValueSeparator(';');
 optionBuilderC.withDescription("An option with description and multiple values");
 Option optionC = optionBuilderC.create('C');
 options.addOption(optionC);
 CommandLine commandLine = parser.parse(args, options);

 assertEquals(3, commandLine.getArgs().length);
 assertEquals("valueA", commandLine.getOptionValue("optionA"));
 assertEquals("valueB", commandLine.getOptionValue("optionB"));
 assertEquals(Arrays.asList("valueC"), commandLine.getOptionValues("optionC"));
 }
}
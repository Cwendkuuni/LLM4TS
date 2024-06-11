package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class OptionBuilderTest {

 @Test
 public void testWithLongOpt() {
 OptionBuilder optionBuilder = OptionBuilder.withLongOpt("option1");
 Option expectedOption = new Option("option1", null);
 Option actualOption = OptionBuilder.create();
 OptionBuilder.withLongOpt("option1").isRequired().create();
 assertEquals(expectedOption, actualOption);
 }

 @Test
 public void testHasArg() {
 OptionBuilder optionBuilder = OptionBuilder.hasArg();
 Option expectedOption = Option.builder()
 .longOpt("option2")
 .required(false)
 .argName("arg")
 .build();
 Option actualOption = OptionBuilder.create();
 OptionBuilder.withLongOpt("option2").hasArg().isRequired(false).create();
 assertEquals(expectedOption, actualOption);
 }

 @Test
 public void testIsRequired() {
 OptionBuilder optionBuilder = OptionBuilder.isRequired();
 Option expectedOption = Option.builder()
 .longOpt("option3")
 .required(true)
 .argName("arg")
 .build();
 Option actualOption = OptionBuilder.create();
 OptionBuilder.withLongOpt("option3").isRequired().create();
 assertEquals(expectedOption, actualOption);
 }

 @Test
 public void testHasOptionalArg() {
 OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
 Option expectedOption = Option.builder()
 .longOpt("option4")
 .required(false)
 .argName("arg")
 .numberOfArgs(1)
 .optionalArg(true)
 .build();
 Option actualOption = OptionBuilder.create();
 OptionBuilder.withLongOpt("option4").hasArg(true).optionalArg(true).create();
 assertEquals(expectedOption, actualOption);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateWithoutLongOpt() {
 OptionBuilder.create();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateWithoutLongOpt2() {
 OptionBuilder optionBuilder = OptionBuilder.withLongOpt("option5");
 OptionBuilder.create();
 }

 @Test
 public void testCreateWithLongOptAndDescription() {
 OptionBuilder optionBuilder = OptionBuilder.withLongOpt("option6")
 .withDescription("Test Option");
 Option expectedOption = new Option("option6", "Test Option");
 Option actualOption = OptionBuilder.create();
 OptionBuilder.withLongOpt("option6").withDescription("Test Option").create();
 assertEquals(expectedOption, actualOption);
 }

 @Test
 public void testCreateWithLongOptAndDescriptionAndArgName() {
 OptionBuilder optionBuilder = OptionBuilder.withLongOpt("option7")
 .withDescription("Test Option")
 .withArgName("argName");
 Option expectedOption = new Option("option7", "Test Option")
 .argName("argName");
 Option actualOption = OptionBuilder.create();
 OptionBuilder.withLongOpt("option7")
 .withDescription("Test Option")
 .withArgName("argName")
 .create();
 assertEquals(expectedOption, actualOption);
 }

 @Test
 public void testCreateWithLongOptAndDescriptionAndArgNameAndArgNumber() {
 OptionBuilder optionBuilder = OptionBuilder.withLongOpt("option8")
 .withDescription("Test Option")
 .withArgName("argName")
 .hasArgs(3);
 Option expectedOption = new Option("option8", "Test Option")
 .argName("argName")
 .numberOfArgs(3);
 Option actualOption = OptionBuilder.create();
 OptionBuilder.withLongOpt("option8")
 .withDescription("Test Option")
 .withArgName("argName")
 .hasArgs(3)
 .create();
 assertEquals(expectedOption, actualOption);
 }

 @Test
 public void testCreateWithLongOptAndDescriptionAndArgNameAndArgNumberAndOptionalArg() {
 OptionBuilder optionBuilder = OptionBuilder.withLongOpt("option9")
 .withDescription("Test Option")
 .withArgName("argName")
 .hasArgs(3)
 .hasOptionalArg();
 Option expectedOption = new Option("option9", "Test Option")
 .argName("argName")
 .numberOfArgs(3)
 .optionalArg(true);
 Option actualOption = OptionBuilder.create();
 OptionBuilder.withLongOpt("option9")
 .withDescription("Test Option")
 .withArgName("argName")
 .hasArgs(3)
 .hasOptionalArg()
 .create();
 assertEquals(expectedOption, actualOption);
 }

 @Test
 public void testCreateWithLongOptAndDescriptionAndArgNameAndArgNumberAndOptionalArgAndValueSeparator() {
 OptionBuilder optionBuilder = OptionBuilder.withLongOpt("option10")
 .withDescription("Test Option")
 .withArgName("argName")
 .hasArgs(3)
 .hasOptionalArg()
 .withValueSeparator(':');
 Option expectedOption = new Option("option10", "Test Option")
 .argName("argName")
 .numberOfArgs(3)
 .optionalArg(true)
 .valueSeparator(':');
 Option actualOption = OptionBuilder.create();
 OptionBuilder.withLongOpt("option10")
 .withDescription("Test Option")
 .withArgName("argName")
 .hasArgs(3)
 .hasOptionalArg()
 .withValueSeparator(':')
 .create();
 assertEquals(expectedOption, actualOption);
 }
}
package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

 @Test
 public void testWithLongOpt() {
 OptionBuilder optionBuilder = OptionBuilder.withLongOpt("optionLongName");
 Option expectedOption = new Option("optionShortName", "optionLongName", false, "desc");
 assertEquals(expectedOption, optionBuilder.create());
 }

 @Test
 public void testHasArg() {
 OptionBuilder optionBuilder = OptionBuilder.hasArg();
 Option expectedOption = new Option("arg", "optionDescription", true, "argName");
 assertEquals(expectedOption, optionBuilder.create());
 }

 @Test
 public void testIsRequired() {
 OptionBuilder optionBuilder = OptionBuilder.isRequired();
 Option expectedOption = new Option("arg", "optionDescription", true, "argName");
 assertEquals(expectedOption, optionBuilder.create());
 Option actualOption = OptionBuilder.create();
 assertTrue(actualOption.isRequired());
 }

 @Test
 public void testHasOptionalArg() {
 OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
 Option expectedOption = new Option("arg", "optionDescription", true, "argName");
 assertEquals(expectedOption, optionBuilder.create());
 Option actualOption = OptionBuilder.create();
 assertFalse(actualOption.isRequired());
 }

 @Test
 public void testWithValueSeparator() {
 OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
 Option expectedOption = new Option("arg", "optionDescription", true, "argName") {
 @Override
 public String getValueSeparator() {
 return ":";
 }
 };
 assertEquals(expectedOption, optionBuilder.create());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMissingLongOpt() {
 OptionBuilder optionBuilder = OptionBuilder.create();
 Option expectedOption = new Option("", null, false, "arg");
 assertNotEquals(expectedOption, optionBuilder.create());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidLongOptChar() {
 OptionBuilder optionBuilder = OptionBuilder.withLongOpt("\u0000");
 Option expectedOption = new Option("", null, false, "arg");
 assertNotEquals(expectedOption, optionBuilder.create());
 }

 @Test
 public void testHasArgs() {
 OptionBuilder optionBuilder = OptionBuilder.hasArgs();
 Option expectedOption = new Option("arg", "optionDescription", Option.UNLIMITED_VALUES, "argName");
 assertEquals(expectedOption, optionBuilder.create());
 }

 @Test
 public void testHasArgsWithSpecifiedNumber() {
 OptionBuilder optionBuilder = OptionBuilder.hasArgs(2);
 Option expectedOption = new Option("arg", "optionDescription", 2, "argName");
 assertEquals(expectedOption, optionBuilder.create());
 }

 @Test
 public void testHasOptionalArgs() {
 OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs();
 Option expectedOption = new Option("arg", "optionDescription", Option.UNLIMITED_VALUES, "argName");
 assertEquals(expectedOption, optionBuilder.create());
 }

 @Test
 public void testHasOptionalArgsWithSpecifiedNumber() {
 OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(3);
 Option expectedOption = new Option("arg", "optionDescription", 3, "argName");
 assertEquals(expectedOption, optionBuilder.create());
 }

 @Test
 public void testWithType() {
 OptionBuilder optionBuilder = OptionBuilder.withType(String.class);
 Option expectedOption = new Option("arg", "optionDescription", true, "argName") {
 @Override
 public Class<? extends Object> getType() {
 return String.class;
 }
 };
 assertEquals(expectedOption, optionBuilder.create());
 }

 @Test
 public void testWithDescription() {
 OptionBuilder optionBuilder = OptionBuilder.withDescription("optionDescription");
 Option expectedOption = new Option("arg", "optionDescription", true, "argName");
 assertEquals(expectedOption, optionBuilder.create());
 }

}
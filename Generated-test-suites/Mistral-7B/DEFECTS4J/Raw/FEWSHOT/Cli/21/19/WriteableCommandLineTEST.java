package org.apache.commons.cli2;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.isA;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineTest {

 private WriteableCommandLine commandLine;

 @Before
 public void setup() {
 commandLine = new DefaultWriteableCommandLine();
 }

 @Test
 public void testAddOption() {
 Option option = Option.builder("o").longOpt("option").build();
 commandLine.addOption(option);
 assertEquals(1, commandLine.getOptions().size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionNullOption() {
 Option option = null;
 commandLine.addOption(option);
 }

 @Test
 public void testAddValue() {
 Option option = Option.builder("o").longOpt("option").valueSeparator('').build();
 commandLine.addOption(option);
 commandLine.addValue(option, "value1");
 commandLine.addValue(option, "value2");
 List<String> values = commandLine.getOptionValues("o");
 assertEquals(2, values.size());
 assertEquals("value1", values.get(0));
 assertEquals("value2", values.get(1));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddValueNullOption() {
 Option option = null;
 commandLine.addOption(option);
 commandLine.addValue(option, "value");
 }

 @Test
 public void testGetUndefaultedValues() {
 Option option = Option.builder("o").longOpt("option").build();
 commandLine.addOption(option);
 commandLine.addValue(option, "value1");
 commandLine.addValue(option, "value2");
 List<String> values = commandLine.getUndefaultedValues(option);
 assertEquals(2, values.size());
 }

 @Test
 public void testSetDefaultValues() {
 Option option = Option.builder("o").longOpt("option").build();
 List<String> defaults = Arrays.asList("value1", "value2");
 commandLine.setDefaultValues(option, defaults);
 List<String> values = commandLine.getUndefaultedValues(option);
 assertEquals(0, values.size());
 assertEquals(2, defaults.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetDefaultValuesNullOption() {
 Option option = null;
 List<String> defaults = Arrays.asList("value1", "value2");
 commandLine.setDefaultValues(option, defaults);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetDefaultValuesEmptyDefaultValues() {
 Option option = Option.builder("o").longOpt("option").build();
 commandLine.setDefaultValues(option, Collections.emptyList());
 }

 @Test
 public void testAddSwitch() {
 Option option = Option.builder("o").longOpt("option").build();
 commandLine.addOption(option);
 commandLine.addSwitch(option, true);
 assertEquals(true, option.isSet());
 }

 @Test(expected = IllegalStateException.class)
 public void testAddSwitchTwice() {
 Option option = Option.builder("o").longOpt("option").build();
 commandLine.addOption(option);
 commandLine.addSwitch(option, true);
 commandLine.addSwitch(option, false);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddSwitchNullOption() {
 Option option = null;
 commandLine.addOption(option);
 commandLine.addSwitch(option, true);
 }

 @Test
 public void testSetDefaultSwitch() {
 Option option = Option.builder("o").longOpt("option").build();
 commandLine.addOption(option);
 commandLine.setDefaultSwitch(option, true);
 assertEquals(true, option.isSet());
 }

 @Test
 public void testSetDefaultSwitchTwice() {
 Option option = Option.builder("o").longOpt("option").build();
 commandLine.addOption(option);
 commandLine.setDefaultSwitch(option, true);
 commandLine.setDefaultSwitch(option, false);
 assertEquals(false, option.isSet());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetDefaultSwitchNullOption() {
 Option option = null;
 commandLine.addOption(option);
 commandLine.setDefaultSwitch(option, true);
 }

 @Test
 public void testAddProperty() {
 Option option = Option.builder("o").longOpt("option").build();
 commandLine.addOption(option);
 commandLine.addProperty(option, "property", "value");
 assertEquals("value", commandLine.getOptionProperty(option, "property"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddPropertyNullOption() {
 Option option = null;
 commandLine.addOption(option);
 commandLine.addProperty(option, "property", "value");
 }

 @Test
 public void testAddPropertyWithEmptyStringValue() {
 Option option = Option.builder("o").longOpt("option").build();
 commandLine.addOption(option);
 commandLine.addProperty(option, "property", "");
 assertEquals("", commandLine.getOptionProperty(option, "property"));
 }

 @Test
 public void testAddPropertyWithNullValue() {
 Option option = Option.builder("o").longOpt("option").build();
 commandLine.addOption(option);
 commandLine.addProperty(option, "property", null);
 assertNull(commandLine.getOptionProperty(option, "property"));
 }

 @Test
 public void testAddPropertyWithDefaultValue() {
 Option option = Option.builder("o").longOpt("option").hasDefaultValue(true).build();
 commandLine.addOption(option);
 commandLine.addProperty(option, "property", "value");
 assertEquals("value", commandLine.getOptionProperty(option, "property", "defaultValue"));
 }

 @Test
 public void testAddPropertyWithDefaultValueNull() {
 Option option = Option.builder("o").longOpt("option").hasDefaultValue(true).build();
 commandLine.addOption(option);
 commandLine.addProperty(option, "property", null);
 assertNull(commandLine.getOptionProperty(option, "property"));
 }

 @Test
 public void testGetCurrentOption() {
 Option option1 = Option.builder("o1").longOpt("option1").build();
 Option option2 = Option.builder("o2").longOpt("option2").build();
 commandLine.addOption(option1);
 commandLine.addOption(option2);
 commandLine.setCurrentOption(option1);
 Option currentOption = commandLine.getCurrentOption();
 assertEquals(option1, currentOption);
 }

 @Test
 public void testSetCurrentOption() {
 Option option1 = Option.builder("o1").longOpt("option1").build();
 Option option2 = Option.builder("o2").longOpt("option2").build();
 commandLine.addOption(option1);
 commandLine.addOption(option2);
 commandLine.setCurrentOption(option2);
 Option currentOption = commandLine.getCurrentOption();
 assertEquals(option2, currentOption);
 }

 @Test
 public void testLooksLikeOption() {
 assertFalse(commandLine.looksLikeOption(""));
 assertFalse(commandLine.looksLikeOption("--option"));
 assertFalse(commandLine.looksLikeOption("--option="));
 assertFalse(commandLine.looksLikeOption("--option=value"));
 assertTrue(commandLine.looksLikeOption("--option"));
 assertTrue(commandLine.looksLikeOption("--option="));
 assertTrue(commandLine.looksLikeOption("--option value"));
 }
}
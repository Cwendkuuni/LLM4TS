package org.apache.commons.cli2;

import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class WriteableCommandLineTest {

 // Test for addOption method
 @Test
 public void testAddOption() {
 WriteableCommandLine cmd = createMockCommandLine();
 Option option = Option.builder("o").longOpt("option").build();
 cmd.addOption(option);
 assertEquals(1, cmd.getOptions().size());
 assertEquals(option, cmd.getOptions().get(0));
 }

 // Test for addValue method
 @Test
 public void testAddValue() {
 WriteableCommandLine cmd = createMockCommandLine();
 Option option = Option.builder("o").longOpt("option").valueSeparator('=').build();
 cmd.addOption(option);
 cmd.addValue(option, "value1");
 assertEquals(1, cmd.getOptions().size());
 assertEquals(option, cmd.getOptions().get(0));
 assertEquals(1, option.getValues().size());
 assertEquals("value1", option.getValues().get(0));
 }

 // Test for getUndefaultedValues method
 @Test
 public void testGetUndefaultedValues() {
 WriteableCommandLine cmd = createMockCommandLine();
 Option option = Option.builder("o").longOpt("option").valueSeparator('=').build();
 cmd.addOption(option);
 cmd.addValue(option, "value1");
 cmd.addValue(option, "value2");
 List values = cmd.getUndefaultedValues(option);
 assertEquals(2, values.size());
 assertEquals("value1", values.get(0));
 assertEquals("value2", values.get(1));
 }

 // Test for setDefaultValues method
 @Test
 public void testSetDefaultValues() {
 WriteableCommandLine cmd = createMockCommandLine();
 Option option = Option.builder("o").longOpt("option").build();
 List defaults = Arrays.asList("value1", "value2");
 cmd.setDefaultValues(option, defaults);
 assertEquals(1, cmd.getOptions().size());
 assertEquals(option, cmd.getOptions().get(0));
 assertEquals(2, option.getDefaultValues().size());
 assertEquals("value1", option.getDefaultValues().get(0));
 assertEquals("value2", option.getDefaultValues().get(1));
 }

 // Test for addSwitch method
 @Test
 public void testAddSwitch() {
 WriteableCommandLine cmd = createMockCommandLine();
 Option option = Option.builder("o").longOpt("option").build();
 cmd.addOption(option);
 cmd.addSwitch(option, true);
 assertEquals(1, cmd.getOptions().size());
 assertEquals(option, cmd.getOptions().get(0));
 assertTrue(option.isSet());
 }

 // Test for setDefaultSwitch method
 @Test
 public void testSetDefaultSwitch() {
 WriteableCommandLine cmd = createMockCommandLine();
 Option option = Option.builder("o").longOpt("option").build();
 cmd.setDefaultSwitch(option, true);
 assertEquals(1, cmd.getOptions().size());
 assertEquals(option, cmd.getOptions().get(0));
 assertTrue(option.isSet());
 }

 // Test for addProperty method (option)
 @Test
 public void testAddPropertyOption() {
 WriteableCommandLine cmd = createMockCommandLine();
 Option option = Option.builder("o").longOpt("option").build();
 cmd.addOption(option);
 cmd.addProperty(option, "property", "value");
 assertEquals(1, cmd.getOptions().size());
 assertEquals(option, cmd.getOptions().get(0));
 assertEquals("value", option.getPropertyValue("property"));
 }

 // Test for addProperty method (default)
 @Test
 public void testAddPropertyDefault() {
 WriteableCommandLine cmd = createMockCommandLine();
 cmd.addProperty("property", "value");
 assertEquals(0, cmd.getOptions().size());
 assertEquals("value", cmd.getProperty("property"));
 }

 // Test for looksLikeOption method
 @Test
 public void testLooksLikeOption() {
 WriteableCommandLine cmd = createMockCommandLine();
 assertTrue(cmd.looksLikeOption("--option"));
 assertTrue(cmd.looksLikeOption("-o"));
 assertFalse(cmd.looksLikeOption("--unrelated"));
 assertFalse(cmd.looksLikeOption("-u"));
 }

 // Test for currentOption and setCurrentOption methods
 @Test
 public void testCurrentOption() {
 WriteableCommandLine cmd = createMockCommandLine();
 Option option1 = Option.builder("o1").longOpt("option1").build();
 Option option2 = Option.builder("o2").longOpt("option2").build();
 cmd.addOption(option1);
 cmd.addOption(option2);
 cmd.setCurrentOption(option1);
 assertEquals(option1, cmd.getCurrentOption());
 cmd.setCurrentOption(option2);
 assertEquals(option2, cmd.getCurrentOption());
 }

 private WriteableCommandLine createMockCommandLine() {
 return new MockWriteableCommandLine();
 }
}